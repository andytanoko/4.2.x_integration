/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JaxrClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add method: findSchemeInfos()
 * Sep 23 2003    Neo Sok Lay         Missing saving of Classifications,
 *                                    ExternalLinks, and ExternalIdentifiers
 *                                    in save(ConceptInfo) and save(SchemeInfo).
 *                                    Missing saving of ExternalLinks in 
 *                                    save(OrganizationInfo) 
 * Oct 06 2003    Neo Sok Lay         Also save serviceInfos when saving OrganizationInfo.
 *                                    Re-organize methods declarations.
 *                                    Modify save(ServiceInfo) to use new method
 *                                    createService(ServiceInfo).
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: MessageFormat.format()
 *                                    must pass in Object[] instead of String[]      
 * Mar 01 2006    Neo Sok Lay         Use generics                                                                
 */
package com.gridnode.pdip.app.bizreg.pub.provider.jaxr;

import com.gridnode.pdip.app.bizreg.pub.model.*;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Acts a client of the JAXR registry provider.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class JaxrClient
{
  private final static boolean DEBUG = false;
  private BusinessQueryManager _queryMgr;
  private BusinessLifeCycleManager _lifeCycleMgr;

  /**
   * Constructor for JaxrClient.
   * 
   * @param bqm The JAXR BusinessQueryManager for inquiry.
   * @param blm The JAXR BusinessLifeCycleManager for publishing.
   */
  public JaxrClient(BusinessQueryManager bqm, BusinessLifeCycleManager blm)
  {
    _queryMgr = bqm;
    _lifeCycleMgr = blm;
  }

  // *********************** SAVE Operations *****************************************
  
  /**
   * Saves an Organization information object to the registry.
   * 
   * @param orgInfo The Organization information object to save,
   * @return The successfully saved Organization information object which
   * contains the registered key.
   * @throws JAXRException Error in accessing the JAXR registry provider.
   */
  public OrganizationInfo save(OrganizationInfo orgInfo) throws JAXRException
  {
    Organization org = createOrganization(orgInfo);

    BulkResponse response =
      _lifeCycleMgr.saveOrganizations(wrapCollection(org));
    setSingleKeyFromResponse(response, orgInfo);

    return orgInfo;
  }

  /**
   * Saves a Service information object to the registry.
   * 
   * @param serviceInfo The Service information object to save,
   * @return The successfully saved Service information object which
   * contains the registered key.
   * @throws JAXRException Error in accessing the JAXR registry provider.
   */
  public ServiceInfo save(ServiceInfo serviceInfo) throws JAXRException
  {
    Organization org = getOrganization(serviceInfo.getOrganizationKey());

    /*031006NSL
    Service service = _lifeCycleMgr.createService(serviceInfo.getName());
    setKeyIfExists(service, serviceInfo);
    service.setDescription(createI18nString(serviceInfo.getDescription()));
    service.setProvidingOrganization(org);

    Collection serviceBindings = new ArrayList();
    BindingInfo bindingInfo;
    for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
    {
      bindingInfo = (BindingInfo) i.next();
      serviceBindings.add(createServiceBinding(bindingInfo));
    }
    if (serviceBindings.size() > 0)
      service.addServiceBindings(serviceBindings);
    */
    
    Service service = createService(serviceInfo);
    service.setProvidingOrganization(org);
    
    BulkResponse response = _lifeCycleMgr.saveServices(wrapCollection(service));

    setSingleKeyFromResponse(response, serviceInfo);

    return serviceInfo;
  }

  /**
   * Saves a Scheme information object to the registry.
   * 
   * @param schemeInfo The Scheme information object to save,
   * @return The successfully saved Scheme information object which
   * contains the registered key.
   * @throws JAXRException Error in accessing the JAXR registry provider.
   */
  public SchemeInfo save(SchemeInfo schemeInfo) throws JAXRException
  {
    ClassificationScheme scheme =
      _lifeCycleMgr.createClassificationScheme(
        schemeInfo.getName(),
        schemeInfo.getDescription());
    setKeyIfExists(scheme, schemeInfo);
    addClassifications(scheme, schemeInfo);
    addExternalIdentifiers(scheme, schemeInfo);
    addExternalLinks(scheme, schemeInfo);

    /*
    Collection concepts = new ArrayList();
    ConceptInfo conceptInfo;
    for (Iterator i=schemeInfo.getConcepts().iterator(); i.hasNext(); )
    {
      conceptInfo = (ConceptInfo)i.next();
      concepts.add(createConcept(conceptInfo));                
    }
    if (concepts.size() > 0)
      scheme.addChildConcepts(concepts);
    */

    //NOT support on UDDI (Capability Level 0)
    //scheme.setValueType(ClassificationScheme.VALUE_TYPE_UNIQUE);
    BulkResponse response =
      _lifeCycleMgr.saveClassificationSchemes(wrapCollection(scheme));
    setSingleKeyFromResponse(response, schemeInfo);

    return schemeInfo;
  }

  /**
   * Saves a Concept information object to the registry.
   * 
   * @param conceptInfo The Concept information object to save,
   * @return The successfully saved Concept information object which
   * contains the registered key.
   * @throws JAXRException Error in accessing the JAXR registry provider.
   */
  public ConceptInfo save(ConceptInfo conceptInfo) throws JAXRException
  {
    //assume no sub-level concepts
    ClassificationScheme parent =
      getClassificationScheme(conceptInfo.getParentKey());

    Concept concept =
      _lifeCycleMgr.createConcept(
        parent,
        conceptInfo.getName(),
        conceptInfo.getValue());
    setKeyIfExists(concept, conceptInfo);
    addClassifications(concept, conceptInfo);
    addExternalIdentifiers(concept, conceptInfo);
    addExternalLinks(concept, conceptInfo);

    BulkResponse response = _lifeCycleMgr.saveConcepts(wrapCollection(concept));
    setSingleKeyFromResponse(response, conceptInfo);

    return conceptInfo;
  }
  
  // ********************* TO BE REMOVED ***************************************
  
  /**
   * Find the Bindings of a Service information object and
   * return the Binding information objects, with registered key.
   * 
   * @param serviceInfo The Service information object to search for
   * Bindings.
   * @return Collection of BindingInfo objects.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public Collection<BindingInfo> findAndSetBindingKeys(ServiceInfo serviceInfo)
    throws JAXRException
  {
    Service service = getService(serviceInfo.getKey());

    Collection<BindingInfo> bindingInfos = setBindingKeys(serviceInfo, service);

    return bindingInfos;
  }

  /**
   * Set the keys for the BindingInfo(s) in the specified ServiceInfo. The keys are obtained
   * from the specified JAXR Service registry object. The matching bindings are found based on Description
   * of the binding. 
   * 
   * @param serviceInfo The ServiceInfo whose BindingInfo(s) keys are to set.
   * @param service The JAXR Service registry object. 
   * @return Collection of BindingInfo(s) with its registry Key set.
   * @throws JAXRException Error while getting information using the JAXR registry provider api.
   */
  private Collection<BindingInfo> setBindingKeys(ServiceInfo serviceInfo, Service service)
    throws JAXRException
  {
    ArrayList<BindingInfo> bindingInfos = new ArrayList<BindingInfo>();
    ServiceBinding[] serviceBindings =
      (ServiceBinding[]) service.getServiceBindings().toArray(
        new ServiceBinding[0]);

    BindingInfo bindingInfo;
    for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
    {
      bindingInfo = (BindingInfo) i.next();
      for (int j = 0; j < serviceBindings.length; j++)
      {
        if (bindingInfo
          .getDescription()
          .equals(getDescription(serviceBindings[j])))
        {
          bindingInfo.setKey(serviceBindings[j].getKey().getId());
          bindingInfos.add(bindingInfo);
          break;
        }
      }
      //assume all will be found
    }
    
    return bindingInfos;
  }
  
  /**
   * Find the Services of an Organization information object and
   * return the Service information objects, with registered key.
   * 
   * @param orgInfo The Organization information object to search for
   * Services.
   * @return Collection of ServiceInfo objects.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public Collection<ServiceInfo> findAndSetServiceKeys(OrganizationInfo orgInfo)
    throws JAXRException
  {
    ArrayList<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

    Organization organization = getOrganization(orgInfo.getKey());
    Service[] services =
      (Service[]) organization.getServices().toArray(new Service[0]);

    ServiceInfo serviceInfo;
    for (Iterator i = orgInfo.getServices().iterator(); i.hasNext();)
    {
      serviceInfo = (ServiceInfo)i.next();
      for (int j = 0; j < services.length; j++)
      {
        if (serviceInfo
          .getName()
          .equals(getName(services[j])))
        {
          serviceInfo.setKey(services[j].getKey().getId());
          serviceInfo.setBindings(
            new HashSet<BindingInfo>(setBindingKeys(serviceInfo, services[j])));
          serviceInfos.add(serviceInfo);
          break;
        }
      }
      //assume all will be found
    }
    return serviceInfos;
  }
    
  /*
    public Collection findAndSetConceptKeys(SchemeInfo schemeInfo)
      throws JAXRException
    {
      ArrayList conceptInfos = new ArrayList();
      
      Collection findQualifiers = wrapCollection(FindQualifier.OR_ALL_KEYS);
      findQualifiers.add(FindQualifier.EXACT_NAME_MATCH);
      Hashtable namePatterns = new Hashtable();
      ConceptInfo conceptInfo;
      for (Iterator i=schemeInfo.getConcepts().iterator(); i.hasNext(); )
      {
        conceptInfo = (ConceptInfo)i.next();
        if (conceptInfo.getKey() == null)
        {
          namePatterns.put(conceptInfo.getName(), conceptInfo); 
        }
        else
        {
          conceptInfos.add(conceptInfo);
        }
      }
          
      BulkResponse response = _queryMgr.findConcepts(findQualifiers, namePatterns.keySet(), null, null, null);
      
      if (response.getStatus() == JAXRResponse.STATUS_SUCCESS)
      {
        Concept[] concepts = (Concept[])response.getCollection().toArray(new Concept[0]);
        BindingInfo bindingInfo;
        for (int j=0; j<concepts.length; j++)
        {
          conceptInfo = (ConceptInfo)namePatterns.get(concepts[j].getName());
          if (conceptInfo != null)
          {
            conceptInfo.setKey(concepts[j].getKey().getId());
            conceptInfos.add(conceptInfo);
          }
        }
      }
      else
      {
        // throw exception
        
      }
      
      return conceptInfos;
    }
  */
  
  // ************************ FIND Operations *************************************
  
  /**
   * Finds a Concept information object using the specified key.
   * 
   * @param key The key of the Concept information object.
   * @return The successfully found Concept information object.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public ConceptInfo findConceptInfo(String key) throws JAXRException
  {
    Concept concept = getConcept(key);

    ConceptInfo conceptInfo = createConceptInfo(concept);

    return conceptInfo;
  }

  /**
   * Query the registry for Concept information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param findQualifiers The qualifiers for finding
   * @param namePatterns Collection of patterns of the Concept names. Assume OR match. <b>null</b> if not required.
   * @param categories Collection of Category information objects contained in the Concept's categoryBag. Assume AND match.
   * <b>null</b> if not required.
   * @param identifiers Collection of Identifier information objects in the Concept's IdentifierBag. Assume AND match.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Concept information object. Assume AND match.
   * <b>null</b> if not required.
   * @return Collection of ConceptInfo objects that are queried from the registry.
   * @throws JAXRException Error during query using JAXR provider.
   */
  public Collection<ConceptInfo> findConceptInfos(
    Collection<String> findQualifiers,
    Collection<String> namePatterns,
    Collection<String[]> categories,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws JAXRException
  {
    ArrayList<ConceptInfo> conceptInfoList = new ArrayList<ConceptInfo>();
    BulkResponse response =
      _queryMgr.findConcepts(
        findQualifiers,
        namePatterns,
        createClassifications(categories),
        createExternalIdentifiers(identifiers),
        createExternalLinks(links));

    if (response.getStatus() == BulkResponse.STATUS_SUCCESS)
    {
      if (DEBUG)
        System.out.println("==>findConceptInfos() response = "+response.getCollection());
             
      Collection conceptList = response.getCollection();
      for (Iterator i = conceptList.iterator(); i.hasNext();)
      {
        conceptInfoList.add(createConceptInfo((Concept) i.next()));
      }
    }
    else
    {
      throw (JAXRException) response.getExceptions().iterator().next();
    }
    return conceptInfoList;
  }

  /**
   * Finds an Organization information object using the specified key.
   * 
   * @param key The key of the Organization information object.
   * @return The successfully found Organization information object.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public OrganizationInfo findOrganizationInfo(String key) throws JAXRException
  {
    Organization org = getOrganization(key);

    OrganizationInfo orgInfo = createOrganizationInfo(org);

    return orgInfo;
  }

  /**
   * Query the registry for Organization information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param findQualifiers The qualifiers for finding.
   * @param namePatterns Collection of patterns of the Organization names. Assume OR match. <b>null</b> if not required.
   * @param categories Collection of Category information objects contained in the Organization's categoryBag. 
   * <b>null</b> if not required.
   * @param specifications Collection of Concept (for UDDI) information objects in the Organization's tModelBag.
   * <b>null</b> if not required.
   * @param identifiers Collection of Identifier information objects in the Organization's IdentifierBag.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Organization information object.
   * <b>null</b> if not required.
   * @return Collection of OrganizationInfo objects that are queried from the registry.
   * @throws JAXRException Error during query from Registry provider.
   */
  public Collection<OrganizationInfo> findOrganizationInfos(
    Collection<String> findQualifiers,
    Collection<String> namePatterns,
    Collection<String[]> categories,
    Collection<ConceptInfo> specifications,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws JAXRException
  {
    ArrayList<OrganizationInfo> orgInfoList = new ArrayList<OrganizationInfo>();
    BulkResponse response =
      _queryMgr.findOrganizations(
        findQualifiers,
        addWildcards(namePatterns),
        createClassifications(categories),
        createConcepts(specifications),
        createExternalIdentifiers(identifiers),
        createExternalLinks(links));

    if (response.getStatus() == BulkResponse.STATUS_SUCCESS)
    {
      Collection conceptList = response.getCollection();
      for (Iterator i = conceptList.iterator(); i.hasNext();)
      {
        orgInfoList.add(createOrganizationInfo((Organization) i.next()));
      }
    }
    else
    {
      throw (JAXRException) response.getExceptions().iterator().next();
    }
    return orgInfoList;
  }

  /**
   * Finds a Scheme information object using the specified key.
   * 
   * @param key The key of the Scheme information object.
   * @return The successfully found Scheme information object.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public SchemeInfo findSchemeInfo(String key) throws JAXRException
  {
    ClassificationScheme scheme = getClassificationScheme(key);

    SchemeInfo schemeInfo = createSchemeInfo(scheme);

    return schemeInfo;
  }

  /**
   * Query the registry for Scheme information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param findQualifiers The qualifiers for finding.
   * @param namePatterns Collection of patterns of the Scheme names. Assume OR match. <b>null</b> if not required.
   * @param categories Collection of Category information objects contained in the Scheme's categoryBag. Assume AND match.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Scheme information object. Assume AND match.
   * <b>null</b> if not required.
   * @return Collection of SchemeInfo objects that are queried from the registry.
   * @throws JAXRException Error during query using JAXR provider.
   */
  public Collection<SchemeInfo> findSchemeInfos(
    Collection<String> findQualifiers,
    Collection<String> namePatterns,
    Collection<String[]> categories,
    Collection<String[]> links)
    throws JAXRException
  {
    ArrayList<SchemeInfo> schemeInfoList = new ArrayList<SchemeInfo>();
    BulkResponse response =
      _queryMgr.findClassificationSchemes(
        findQualifiers,
        namePatterns,
        createClassifications(categories),
        createExternalLinks(links));

    if (response.getStatus() == BulkResponse.STATUS_SUCCESS)
    {
      if (DEBUG)
        System.out.println("==>findSchemeInfos() response = "+response.getCollection());
             
      Collection schemeList = response.getCollection();
      for (Iterator i = schemeList.iterator(); i.hasNext();)
      {
        schemeInfoList.add(createSchemeInfo((ClassificationScheme) i.next()));
      }
    }
    else
    {
      throw (JAXRException) response.getExceptions().iterator().next();
    }
    return schemeInfoList;
  }

  /**
   * Finds a Service information object using the specified key.
   * 
   * @param key The key of the Service information object.
   * @return The successfully found Service information object.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public ServiceInfo findServiceInfo(String key) throws JAXRException
  {
    Service service = getService(key);

    ServiceInfo serviceInfo = createServiceInfo(service);

    return serviceInfo;
  }

  /**
   * Finds a Binding information object using the specified key.
   * 
   * @param key The key of the Binding information object.
   * @return The successfully found Binding information object.
   * @throws JAXRException Error while querying using the JAXR registry
   * provider api.
   */
  public BindingInfo findServiceBindingInfo(String key) throws JAXRException
  {
    ServiceBinding binding = getServiceBinding(key);

    BindingInfo bindingInfo = createServiceBindingInfo(binding);

    return bindingInfo;
  }

  // *********************** CREATE INFO Operations ****************************
  
  /**
   * Creates a Service information object from the JAXR Service registry object.
   * 
   * @param service The JAXR Service registry object to create from.
   * @return The created Service information object.
   * @throws JAXRException Error while getting information from the JAXR registry object.
   */
  private ServiceInfo createServiceInfo(Service service) throws JAXRException
  {
    ServiceInfo serviceInfo =
      new ServiceInfo(
        getName(service),
        service.getProvidingOrganization().getKey().getId());

    setGenericFieldsFromRegistry(serviceInfo, service);

    //specific    
    for (Iterator i = service.getServiceBindings().iterator(); i.hasNext();)
    {
      ServiceBinding binding = (ServiceBinding) i.next();
      serviceInfo.addBinding(createServiceBindingInfo(binding));
    }
    return serviceInfo;
  }

  /**
   * Create ServiceInfo objects based on the specified collection of Service(s).
   * 
   * @param services Collection of Service registry objects.
   * @return Collection of ServiceInfo objects.
   * @throws JAXRException Error while getting information from the Service registry objects.
   */
  private Collection<ServiceInfo> createServiceInfos(Collection services)
    throws JAXRException
  {
    if (services == null || services.isEmpty())
      return new ArrayList<ServiceInfo>();

    ArrayList<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
    for (Iterator i = services.iterator(); i.hasNext();)
    {
      serviceInfos.add(createServiceInfo((Service) i.next()));
    }

    return serviceInfos;
  }

  /**
   * Creates a Binding information object from the JAXR ServiceBinding registry object.
   * 
   * @param binding The JAXR ServiceBinding registry object to create from.
   * @return The created Binding information object.
   * @throws JAXRException Error while getting information from the JAXR registry object.
   */
  private BindingInfo createServiceBindingInfo(ServiceBinding binding)
    throws JAXRException
  {
    BindingInfo bindingInfo = new BindingInfo(getName(binding));

    setGenericFieldsFromRegistry(bindingInfo, binding);

    //specific
    bindingInfo.setAccessUri(denull(binding.getAccessURI()));

    Collection specLinks = binding.getSpecificationLinks();
    for (Iterator i = specLinks.iterator(); i.hasNext();)
    {
      SpecificationLink specLink = (SpecificationLink) i.next();
      bindingInfo.addSpecificationLink(createSpecificationLinkInfo(specLink));
    }

    //TODO what to do? bindingName returned is null
    if (bindingInfo.getName() == null)
      bindingInfo.setName("");
      
    return bindingInfo;
  }

  /**
   * Creates a Specification information object from the JAXR SpecificationLink registry object.
   * 
   * @param specLink The JAXR SpecificationLink registry object to create from.
   * @return The created Specification information object.
   * @throws JAXRException Error while getting information from the JAXR registry object.
   */
  private SpecificationInfo createSpecificationLinkInfo(SpecificationLink specLink)
    throws JAXRException
  {
    SpecificationInfo specInfo = new SpecificationInfo(getName(specLink));

    setGenericFieldsFromRegistry(specInfo, specLink);

    //specific
    if (specLink.getUsageDescription() != null)
      specInfo.setUsageDescription(specLink.getUsageDescription().getValue());
    //if (!specLink.getUsageParameters().isEmpty())
    //  specInfo.setUsageParams((String)specLink.getUsageParameters().iterator().next());

    // Assumes the specification object is a Concept for now.
    specInfo.setSpecifiedObject(
      createConceptInfo((Concept) specLink.getSpecificationObject()));
    
    if (specInfo.getName() == null)
    {
      specInfo.setName(specInfo.getSpecifiedObject().getName());  
    }
    return specInfo;
  }

  /**
   * Creates a Scheme information object from the JAXR ClassificationScheme registry object.
   * 
   * @param scheme The JAXR ClassificationScheme registry object to create from.
   * @return The created Scheme information object.
   * @throws JAXRException Error while getting information from the JAXR registry object.
   */
  private SchemeInfo createSchemeInfo(ClassificationScheme scheme)
    throws JAXRException
  {
    SchemeInfo schemeInfo = new SchemeInfo(getName(scheme));

    setGenericFieldsFromRegistry(schemeInfo, scheme);

    //specific    
    /*SOMEHOW DID NOT RETURN THE CONCEPTS, DON'T KNOW WHY???
    for (Iterator i=scheme.getChildrenConcepts().iterator(); i.hasNext(); )
    {
      Concept concept = (Concept)i.next();
      schemeInfo.addConcept(createConceptInfo(concept));
    }
    */
    return schemeInfo;
  }

  /**
   * Creates a Organization information object from the JAXR Organization registry object.
   * 
   * @param org The JAXR Organization registry object to create from.
   * @return The created Organization information object.
   * @throws JAXRException Error while getting information from the JAXR registry object.
   */
  private OrganizationInfo createOrganizationInfo(Organization org)
    throws JAXRException
  {
    OrganizationInfo orgInfo = new OrganizationInfo(getName(org));

    setGenericFieldsFromRegistry(orgInfo, org);

    //specific
    //NOT Support in UDDI (CapabilityLevel 0)
    //orgInfo.setPostalAddress(createAddressInfo(org.getPostalAddress()));
    addContactInfos(orgInfo, org.getUsers());
    addPhoneInfos(orgInfo, org.getTelephoneNumbers(null));

    //get the services
    Collection<ServiceInfo> serviceInfos = createServiceInfos(org.getServices());
    orgInfo.getServices().addAll(serviceInfos);

    return orgInfo;
  }

  /**
   * Add Phone information objects to an Organization information object.
   * 
   * @param orgInfo The Organization information object to add to.
   * @param phones Collection of JAXR TelephoneNumber registry object to
   * add from.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   */
  private void addPhoneInfos(OrganizationInfo orgInfo, Collection phones)
    throws JAXRException
  {
    for (Iterator i = phones.iterator(); i.hasNext();)
    {
      TelephoneNumber phone = (TelephoneNumber) i.next();
      orgInfo.addPhoneNumber(createPhoneInfo(phone));
    }
  }

  /**
   * Create Phone information object from JAXR TelephoneNumber registry object.
   * 
   * @param phone The JAXR TelephoneNumber registry object to create from.
   * @return The created Phone information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see IPhoneInfo
   */
  private String[] createPhoneInfo(TelephoneNumber phone) throws JAXRException
  {
    String[] phoneInfo = new String[IPhoneInfo.NUM_FIELDS];
    phoneInfo[IPhoneInfo.FIELD_NUMBER] = phone.getNumber();
    phoneInfo[IPhoneInfo.FIELD_TYPE] = phone.getType();

    return phoneInfo;
  }

  /**
   * Create Address information object from JAXR PostalAddress registry object.
   * 
   * @param address The JAXR PostalAddress registry object to create from.
   * @return The created Address information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see IAddressInfo
   */
  private String[] createAddressInfo(PostalAddress address)
    throws JAXRException
  {
    String[] addressInfo = new String[IAddressInfo.NUM_FIELDS];

    Slot addressLines = address.getSlot("addressLines");
    String[] values = (String[])addressLines.getValues().toArray(new String[0]);
    
    if (values.length > 0)
    {
      addressInfo[IAddressInfo.FIELD_STREET] = values[0];
      if (values.length > 1)
      {
        addressInfo[IAddressInfo.FIELD_CITY] = values[1];
        if (values.length > 2)
        {
          addressInfo[IAddressInfo.FIELD_STATE] = values[2];
          if (values.length > 3)
          {  
            addressInfo[IAddressInfo.FIELD_ZIPCODE] = values[3];
            if (values.length > 4)
              addressInfo[IAddressInfo.FIELD_COUNTRY] = values[4];
          }
        }
      }
    }
      
    /* Needs PostalScheme
    addressInfo[IAddressInfo.FIELD_CITY] = address.getCity();
    addressInfo[IAddressInfo.FIELD_COUNTRY] = address.getCountry();
    addressInfo[IAddressInfo.FIELD_STATE] = address.getStateOrProvince();
    addressInfo[IAddressInfo.FIELD_STREET] = address.getStreet();
    addressInfo[IAddressInfo.FIELD_ZIPCODE] = address.getPostalCode();
    */
    return addressInfo;
  }

  /**
   * Add Contact information objects to an Organization information object.
   * 
   * @param orgInfo The Organization information object to add to.
   * @param users Collection of JAXR User registry object to
   * add from.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see IContactInfo
   */
  private void addContactInfos(OrganizationInfo orgInfo, Collection users)
    throws JAXRException
  {
    if (users != null)
    {
      for (Iterator i = users.iterator(); i.hasNext();)
      {
        User user = (User) i.next();
        orgInfo.addContact(createContactInfo(user));
        if (orgInfo.getPostalAddress() == null)
        {
          Object address = getOne(user.getPostalAddresses());
          if (address != null)
            orgInfo.setPostalAddress(
              createAddressInfo((PostalAddress) address));
        }
      }
    }
  }

  /**
   * Create Contact information object from JAXR User registry object.
   * 
   * @param user The JAXR User registry object to create from.
   * @return The created Contact information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see IContactInfo
   */
  private String[] createContactInfo(User user) throws JAXRException
  {
    String[] contactInfo = new String[IContactInfo.NUM_FIELDS];

    Collection emails = user.getEmailAddresses();
    for (Iterator i = emails.iterator(); i.hasNext();)
    {
      EmailAddress email = (EmailAddress) i.next();
      if (IContactInfo.ALT_EMAIL_TYPE.equals(email.getType()))
        contactInfo[IContactInfo.FIELD_CONTACT_ALT_EMAIL] = email.getAddress();
      else if (IContactInfo.EMAIL_TYPE.equals(email.getType()))
        contactInfo[IContactInfo.FIELD_CONTACT_EMAIL] = email.getAddress();
    }

    Collection phones = user.getTelephoneNumbers(null);
    for (Iterator i = phones.iterator(); i.hasNext();)
    {
      TelephoneNumber phone = (TelephoneNumber) i.next();
      if (IContactInfo.ALT_TEL_TYPE.equals(phone.getType()))
        contactInfo[IContactInfo.FIELD_CONTACT_ALT_TEL] = phone.getNumber();
      else if (IContactInfo.TEL_TYPE.equals(phone.getType()))
        contactInfo[IContactInfo.FIELD_CONTACT_TEL] = phone.getNumber();
      else if (IContactInfo.FAX_TYPE.equals(phone.getType()))
        contactInfo[IContactInfo.FIELD_CONTACT_FAX] = phone.getNumber();  
    }

    contactInfo[IContactInfo.FIELD_CONTACT_NAME] =
      user.getPersonName().getFullName();

    return contactInfo;
  }

  /**
   * Create Concept information object from JAXR Concept registry object.
   * 
   * @param concept The JAXR Concept registry object to create from.
   * @return The created Concept information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   */
  private ConceptInfo createConceptInfo(Concept concept) throws JAXRException
  {
    //Logger.debug("[JaxrClient.createConceptInfo] Concept="+concept);    
    //Logger.debug("[JaxrClient.createConceptInfo] Concept's ClassificationScheme="+concept.getClassificationScheme());    
    //Logger.debug("[JaxrClient.createConceptInfo] Concept's ClassificationScheme Key="+concept.getClassificationScheme().getKey());    

    // getParent returns null, why??? Now assumes directly under classificationscheme
    String parentKey = null; //concept.getParent().getKey().getId();
    ConceptInfo conceptInfo =
      new ConceptInfo(parentKey, getName(concept), concept.getValue());
    setGenericFieldsFromRegistry(conceptInfo, concept);

    return conceptInfo;
  }

  /**
   * Add Link information objects to a registry information object.
   * 
   * @param regInfo The registry information object to add to.
   * @param regObj The JAXR registry object to get the ExternalLink
   * registry objects from which to create Link information objects.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   */
  private void addLinkInfos(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    Collection extLinks = regObj.getExternalLinks();
    if (extLinks != null)
    {
      for (Iterator i = extLinks.iterator(); i.hasNext();)
      {
        ExternalLink extLink = (ExternalLink) i.next();
        regInfo.addLink(createLinkInfo(extLink));
      }
    }
  }

  /**
   * Create Link information object from JAXR ExternalLink registry object.
   * 
   * @param extLink The JAXR ExternalLink registry object to create from.
   * @return The created Link information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see ILinkInfo
   */
  private String[] createLinkInfo(ExternalLink extLink) throws JAXRException
  {
    String[] linkInfo = new String[ILinkInfo.NUM_FIELDS];
    
    // for DiscoveryURL, take from Name
    // for OverviewDoc, take from Description
    linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION] = getDescription(extLink);
    if (linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION] == null || extLink.getDescription()==null)
      linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION] = getName(extLink);
    
    linkInfo[ILinkInfo.FIELD_LINK_URL] = extLink.getExternalURI();

    return linkInfo;
  }

  /**
   * Add Identifier information objects to a registry information object.
   * 
   * @param regInfo The registry information object to add to.
   * @param regObj The JAXR registry object to get the ExternalIdentifier
   * registry objects from which to create Identifier information objects.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   */
  private void addIdentifierInfos(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    Collection extIdentifiers = regObj.getExternalIdentifiers();
    if (extIdentifiers != null)
    {
      for (Iterator i = extIdentifiers.iterator(); i.hasNext();)
      {
        ExternalIdentifier extIdentifier = (ExternalIdentifier) i.next();
        regInfo.addIdentifier(createIdentifierInfo(extIdentifier));
      }
    }
  }

  /**
   * Create Identifier information object from JAXR ExternalIdentifier registry object.
   * 
   * @param extIdentifier The JAXR ExternalIdentifier registry object to create from.
   * @return The created Identifier information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see IIdentifierInfo
   */
  private String[] createIdentifierInfo(ExternalIdentifier extIdentifier)
    throws JAXRException
  {
    String[] identifierInfo = new String[IIdentifierInfo.NUM_FIELDS];
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_NAME] =
      getName(extIdentifier);
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_VALUE] =
      extIdentifier.getValue();
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFICATION_SCHEME_KEY] =
      extIdentifier.getIdentificationScheme().getKey().getId();
    return identifierInfo;
  }

  /**
   * Add Category information objects to a registry information object.
   * 
   * @param regInfo The registry information object to add to.
   * @param regObj The JAXR registry object to get the Classification
   * registry objects from which to create Link information objects.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   */
  private void addCategoryInfos(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    Collection classifications = regObj.getClassifications();
    if (classifications != null)
    {
      for (Iterator i = classifications.iterator(); i.hasNext();)
      {
        Classification classification = (Classification) i.next();
        regInfo.addLink(createCategoryInfo(classification));
      }
    }
  }

  /**
   * Create Category information object from JAXR Classification registry object.
   * 
   * @param classification The JAXR Classification registry object to create from.
   * @return The created Category information object.
   * @throws JAXRException Error while getting information from JAXR registry
   * objects.
   * @see ICategoryInfo
   */
  private String[] createCategoryInfo(Classification classification)
    throws JAXRException
  {
    String[] catInfo = new String[ICategoryInfo.NUM_FIELDS];
    catInfo[ICategoryInfo.FIELD_CATEGORY_NAME] = getName(classification);
    catInfo[ICategoryInfo.FIELD_CATEGORY_VALUE] = classification.getValue();
    if (classification.isExternal() && classification.getClassificationScheme()!=null)
    {
      catInfo[ICategoryInfo.FIELD_SCHEME_KEY] =
        classification.getClassificationScheme().getKey().getId();
    }
    else if (classification.getConcept() != null)
    {
      catInfo[ICategoryInfo.FIELD_CONCEPT_KEY] =
        classification.getConcept().getKey().getId();
    }

    return catInfo;
  }

  // ****************************** CREATE REGISTRY OBJ Operations **************************
  
  /**
   * Create Concept registry objects based on the specified collection of
   * ConceptInfo objects.
   * 
   * @param conceptInfos Collection of ConceptInfo objects.
   * @return Collection of JAXR Concept registry objects created.
   * @throws JAXRException Error while creating or setting information to JAXR Concept registry
   * objects.
   */
  private Collection<Concept> createConcepts(Collection<ConceptInfo> conceptInfos)
    throws JAXRException
  {
    if (conceptInfos == null || conceptInfos.isEmpty())
      return new ArrayList<Concept>();

    ArrayList<Concept> concepts = new ArrayList<Concept>();
    for (ConceptInfo conceptInfo : conceptInfos)
    {
      concepts.add(createConcept(conceptInfo));
    }

    return concepts;
  }

  /**
   * Create Concept registry object based on the specified ConceptInfo object.
   * 
   * @param conceptInfo ConceptInfo object.
   * @return JAXR Concept registry object created.
   * @throws JAXRException Error while creating or setting information to JAXR Concept registry
   * object.
   */
  private Concept createConcept(ConceptInfo conceptInfo) throws JAXRException
  {
    ClassificationScheme parent = null;
    
    if (conceptInfo.getParentKey() != null)
      parent = getClassificationScheme(conceptInfo.getParentKey());

    Concept concept =
      _lifeCycleMgr.createConcept(
        parent,
        conceptInfo.getName(),
        conceptInfo.getDescription());
    setKeyIfExists(concept, conceptInfo);

    return concept;
  }

  /**
   * Create JAXR Organization registry object from an Organization information object.
   * 
   * @param orgInfo The Organization information object to create from.
   * @return The created JAXR Organization registry object.
   * @throws JAXRException Error while setting information to JAXR registry
   * objects.
   */
  private Organization createOrganization(OrganizationInfo orgInfo)
    throws JAXRException
  {
    Organization org = _lifeCycleMgr.createOrganization(orgInfo.getName());

    // standard attributes
    setKeyIfExists(org, orgInfo);
    org.setDescription(createI18nString(orgInfo.getDescription()));
    addExternalIdentifiers(org, orgInfo);
    addClassifications(org, orgInfo);
    addExternalLinks(org, orgInfo);

    // Organization specific
    //NOT support in UDDI (Capability Level 0)
    //org.setPostalAddress(createPostalAddress(orgInfo.getPostalAddress()));
    
    addContacts(
      org,
      orgInfo.getContacts(),
      createPostalAddress(orgInfo.getPostalAddress()));
    org.setTelephoneNumbers(createPhoneNumbers(orgInfo.getPhoneNumbers()));

    //services
    Collection<Service> services = new ArrayList<Service>();
    
    for (ServiceInfo serviceInfo : orgInfo.getServices())
    {
      services.add(createService(serviceInfo));
    }
    if (services.size() > 0)
      org.addServices(services);
    
    return org;
  }

  /**
   * Create JAXR PostalAddress registry object from an Address information object.
   * 
   * @param addressInfo The Address information object to create from.
   * @return The created JAXR PostalAddress registry object.
   * @throws JAXRException Error while setting information to JAXR registry
   * objects.
   * @see IAddressInfo
   */
  private PostalAddress createPostalAddress(String[] addressInfo)
    throws JAXRException
  {
    PostalAddress address =
      _lifeCycleMgr.createPostalAddress(
        "",
        denull(addressInfo[IAddressInfo.FIELD_STREET]),
        denull(addressInfo[IAddressInfo.FIELD_CITY]),
        denull(addressInfo[IAddressInfo.FIELD_STATE]),
        denull(addressInfo[IAddressInfo.FIELD_COUNTRY]),
        denull(addressInfo[IAddressInfo.FIELD_ZIPCODE]),
        "Office");
    return address;
  }

  /**
   * Add JAXR User registry objects to a JAXR Organization registry object.
   * 
   * @param org The JAXR Organization registry object to add to.
   * @param contactInfos Array of Contact information object to create the
   * JAXR User registry objects from.
   * @param postalAddress Postal address of the contacts
   * @throws JAXRException Error while setting information to JAXR registry
   * objects.
   */
  private void addContacts(
    Organization org,
    String[][] contactInfos,
    PostalAddress postalAddress)
    throws JAXRException
  {
    //User contact;
    for (int i = 0; i < contactInfos.length; i++)
    {
      org.addUser(createUser(contactInfos[i], i == 0, postalAddress));
    }
  }

  /**
   * Create JAXR User registry object from a Contact information object.
   * 
   * @param contactInfo The Contact information object to create from.
   * @param primary <b>true</b> to set this User as the primary contact of
   * the organization.
   * @param postalAddress Postal address of the User
   * @return The created JAXR User registry object.
   * @throws JAXRException Error while setting information to JAXR registry
   * objects.
   */
  private User createUser(
    String[] contactInfo,
    boolean primary,
    PostalAddress postalAddress)
    throws JAXRException
  {
    User contact = _lifeCycleMgr.createUser();

    contact.setPersonName(
      createPersonName(contactInfo[IContactInfo.FIELD_CONTACT_NAME]));

    contact.setType(primary ? "Main Contact" : "Other Contacts");
    contact.setPostalAddresses(wrapCollection(postalAddress));

    // add telephone numbers
    ArrayList<TelephoneNumber> phones = new ArrayList<TelephoneNumber>();
    phones.add(
      createPhoneNumber(
        contactInfo[IContactInfo.FIELD_CONTACT_TEL],
        IContactInfo.TEL_TYPE));
    phones.add(
      createPhoneNumber(
        contactInfo[IContactInfo.FIELD_CONTACT_ALT_TEL],
        IContactInfo.ALT_TEL_TYPE));
    phones.add(
      createPhoneNumber(
        contactInfo[IContactInfo.FIELD_CONTACT_FAX],
        IContactInfo.FAX_TYPE));
    contact.setTelephoneNumbers(phones);

    // add emails
    ArrayList<EmailAddress> emails = new ArrayList<EmailAddress>();
    emails.add(
      createEmailAddress(
        contactInfo[IContactInfo.FIELD_CONTACT_EMAIL],
        IContactInfo.EMAIL_TYPE));
    emails.add(
      createEmailAddress(
        contactInfo[IContactInfo.FIELD_CONTACT_ALT_EMAIL],
        IContactInfo.ALT_EMAIL_TYPE));
    contact.setEmailAddresses(emails);

    // set url: Not supported for UDDI

    return contact;
  }

  /**
   * Creates a JAXR PersonName object from the specified name.
   * 
   * @param fullName The full name of the contact person.
   * @return The created JAXR PersonName object.
   * @throws JAXRException Error while creating the JAXR registry object.
   */
  private PersonName createPersonName(String fullName) throws JAXRException
  {
    PersonName personName = _lifeCycleMgr.createPersonName(denull(fullName));

    return personName;
  }

  /**
   * Creates a JAXR EmailAddress object from the specified information.
   * 
   * @param address The email address.
   * @param type The type of email address, e.g. "Home", "Office".
   * @return The created JAXR EmailAddress object.
   * @throws JAXRException Error while creating the JAXR registry object.
   */
  private EmailAddress createEmailAddress(String address, String type)
    throws JAXRException
  {
    EmailAddress email =
      _lifeCycleMgr.createEmailAddress(denull(address), type);

    return email;
  }

  /**
   * Create JAXR TelephoneNumber registry objects from Phone information objects.
   * 
   * @param phoneInfos The Phone information objects to create from.
   * @return Collection of the created JAXR TelephoneNumber registry objects.
   * @throws JAXRException Error while creating JAXR registry
   * objects.
   */
  private Collection<TelephoneNumber> createPhoneNumbers(String[][] phoneInfos)
    throws JAXRException
  {
    ArrayList<TelephoneNumber> phoneNumbers = new ArrayList<TelephoneNumber>();

    TelephoneNumber phone;
    for (int i = 0; i < phoneInfos.length; i++)
    {
      phone = _lifeCycleMgr.createTelephoneNumber();
      phone.setNumber(denull(phoneInfos[i][IPhoneInfo.FIELD_NUMBER]));
      phone.setType(denull(phoneInfos[i][IPhoneInfo.FIELD_TYPE]));
      phoneNumbers.add(phone);
    }

    return phoneNumbers;
  }

  /**
   * Creates a JAXR TelephoneNumber object from the specified information.
   * 
   * @param number The telephone number.
   * @param type The type of the telephone number, e.g. "Home", "Office".
   * @return The created JAXR TelephoneNumber object.
   * @throws JAXRException
   */
  private TelephoneNumber createPhoneNumber(String number, String type)
    throws JAXRException
  {
    TelephoneNumber phone = _lifeCycleMgr.createTelephoneNumber();
    phone.setNumber(denull(number));
    phone.setType(denull(type));
    return phone;
  }

  /**
   * Create JAXR Classification registry object from a Category information object.
   * 
   * @param categoryInfo The Category information object to create from.
   * @return The created JAXR Classification registry object.
   * @throws JAXRException Error while creating JAXR registry
   * objects.
   */
  private Classification createClassification(String[] categoryInfo)
    throws JAXRException
  {
    Classification classification = null;

    if (categoryInfo[ICategoryInfo.FIELD_CONCEPT_KEY] != null)
    {
      Concept concept =
        getConcept(categoryInfo[ICategoryInfo.FIELD_CONCEPT_KEY]);
      classification = _lifeCycleMgr.createClassification(concept);
    }
    else
    {   
      ClassificationScheme scheme = (ClassificationScheme)_lifeCycleMgr.createObject(BusinessLifeCycleManager.CLASSIFICATION_SCHEME);
      scheme.setKey(createKeyObj(categoryInfo[ICategoryInfo.FIELD_SCHEME_KEY]));

      classification =
        _lifeCycleMgr.createClassification(
          scheme,
          categoryInfo[ICategoryInfo.FIELD_CATEGORY_NAME],
          categoryInfo[ICategoryInfo.FIELD_CATEGORY_VALUE]);
    }

    return classification;
  }

  /**
   * Create JAXR Classification registry objects from a specified collection
   * of Category information objects.
   * 
   * @param categoryInfos Collection of Category information objects (String[]).
   * @return Collection of JAXR Classification registry objects created.
   * @throws JAXRException Error creating or setting information to JAXR Classification
   * registry objects.
   */
  private Collection<Classification> createClassifications(Collection<String[]> categoryInfos)
    throws JAXRException
  {
    if (categoryInfos == null || categoryInfos.isEmpty())
      return new ArrayList<Classification>();

    ArrayList<Classification> classifications = new ArrayList<Classification>();
    for (String[] categoryInfo : categoryInfos)
    {
      classifications.add(createClassification(categoryInfo));
    }

    return classifications;
  }

  /**
   * Adds JAXR Classification registry objects to a JAXR registry object.
   * 
   * @param regObj The JAXR registry object to add to.
   * @param regInfo The registry information object to get the Category information
   * objects to create the JAXR Classification registry objects.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private void addClassifications(RegistryObject regObj, IRegistryInfo regInfo)
    throws JAXRException
  {
    for (String[] categoryInfo : regInfo.getCategories())
    {
      regObj.addClassification(createClassification(categoryInfo));
    }
  }

  /**
   * Adds JAXR ExternalIdentifier registry objects to a JAXR registry object.
   * 
   * @param regObj The JAXR registry object to add to.
   * @param regInfo The registry information object to get the Identifier information
   * objects to create the JAXR ExternalIdentifier registry objects.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private void addExternalIdentifiers(
    RegistryObject regObj,
    IRegistryInfo regInfo)
    throws JAXRException
  {
    for (String[] identifierInfo : regInfo.getIdentifiers())
    {
      regObj.addExternalIdentifier(
        createExternalIdentifier(identifierInfo));
    }
  }

  /**
   * Adds JAXR ExternalLink registry objects to a JAXR registry object.
   * 
   * @param regObj The JAXR registry object to add to.
   * @param regInfo The registry information object to get the Link information
   * objects to create the JAXR ExternalLink registry objects.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private void addExternalLinks(
    RegistryObject regObj,
    IRegistryInfo regInfo)
    throws JAXRException
  {
    for (String[] linkInfo : regInfo.getLinks())
    {
      regObj.addExternalLink(
        createExternalLink(linkInfo));
    }
  }

  /**
   * Create JAXR ExternalIdentifier registry object from a Identifier information object.
   * 
   * @param identifierInfo The Identifier information object to create from.
   * @return The created JAXR ExternalIdentifer registry object.
   * @throws JAXRException Error while creating JAXR registry
   * objects.
   */
  private ExternalIdentifier createExternalIdentifier(String[] identifierInfo)
    throws JAXRException
  {
    ClassificationScheme scheme =
      getClassificationScheme(
        identifierInfo[IIdentifierInfo.FIELD_IDENTIFICATION_SCHEME_KEY]);

    ExternalIdentifier identifier =
      _lifeCycleMgr.createExternalIdentifier(
        scheme,
        identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_NAME],
        identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_VALUE]);

    return identifier;
  }

  /**
   * Create JAXR ExternalIdentifier registry objects from a specified collection
   * of Identifier information objects.
   * 
   * @param identifierInfos Collection of Identifier information objects (String[]).
   * @return Collection of JAXR ExternalIdentifier registry objects created.
   * @throws JAXRException Error creating or setting information to JAXR ExternalIdentifier
   * registry objects.
   */
  private Collection<ExternalIdentifier> createExternalIdentifiers(Collection<String[]> identifierInfos)
    throws JAXRException
  {
    if (identifierInfos == null || identifierInfos.isEmpty())
      return new ArrayList<ExternalIdentifier>();

    ArrayList<ExternalIdentifier> extIdentifiers = new ArrayList<ExternalIdentifier>();
    for (String[] identifierInfo : identifierInfos)
    {
      extIdentifiers.add(createExternalIdentifier(identifierInfo));
    }

    return extIdentifiers;
  }

  /**
   * Creates a JAXR Service registry object from the Service
   * information object.
   * 
   * @param serviceInfo The Service information object to create from.
   * @return The created JAXR Service registry object.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private Service createService(ServiceInfo serviceInfo)
    throws JAXRException
  {
    Service service = _lifeCycleMgr.createService(serviceInfo.getName());
    setKeyIfExists(service, serviceInfo);
    service.setDescription(createI18nString(serviceInfo.getDescription()));

    Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
    
    for (BindingInfo bindingInfo : serviceInfo.getBindings())
    {
      serviceBindings.add(createServiceBinding(bindingInfo));
    }
    if (serviceBindings.size() > 0)
      service.addServiceBindings(serviceBindings);
      
    return service;  
  }
  
  /**
   * Creates a JAXR ServiceBinding registry object from the ServiceBinding
   * information object.
   * 
   * @param bindingInfo The Binding information object to create from.
   * @return The created JAXR ServiceBinding registry object.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private ServiceBinding createServiceBinding(BindingInfo bindingInfo)
    throws JAXRException
  {
    ServiceBinding serviceBinding = _lifeCycleMgr.createServiceBinding();

    setKeyIfExists(serviceBinding, bindingInfo);
    serviceBinding.setAccessURI(denull(bindingInfo.getAccessUri()));
    serviceBinding.setDescription(
      createI18nString(bindingInfo.getDescription()));
    serviceBinding.setName(createI18nString(bindingInfo.getName()));

    for (SpecificationInfo specInfo : bindingInfo.getSpecificationLinks())
    {
      serviceBinding.addSpecificationLink(createSpecificationLink(specInfo));
    }

    addExternalIdentifiers(serviceBinding, bindingInfo);

    return serviceBinding;
  }

  /**
   * Creates a JAXR SpecificationLink registry object from the SpecificationLink
   * information object.
   * 
   * @param specInfo The Specification information object to create from.
   * @return The created JAXR SpecificationLink registry object.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private SpecificationLink createSpecificationLink(SpecificationInfo specInfo)
    throws JAXRException
  {
    SpecificationLink specLink = _lifeCycleMgr.createSpecificationLink();
    setKeyIfExists(specLink, specInfo);
    specLink.setName(createI18nString(specInfo.getName()));
    specLink.setDescription(createI18nString(specInfo.getDescription()));
    specLink.setUsageDescription(
      createI18nString(specInfo.getUsageDescription()));
    //?? Throws error complaining UsageParameters must be strings.
    //specLink.setUsageParameters(wrapCollection(specInfo.getUsageParams()));
    if (specInfo.getOverviewLink() != null)
    {
      ExternalLink extLink = createExternalLink(specInfo.getOverviewLink());
      specLink.addExternalLink(extLink);
    }

    specLink.setSpecificationObject(
      createConcept(specInfo.getSpecifiedObject()));

    return specLink;
  }

  /**
   * Creates a JAXR ExternalLink registry object from the Link
   * information object.
   * 
   * @param linkInfo The Link information object to create from.
   * @return The created JAXR ExternalLink registry object.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private ExternalLink createExternalLink(String[] linkInfo)
    throws JAXRException
  {
    ExternalLink extLink =
      _lifeCycleMgr.createExternalLink(
        denull(linkInfo[ILinkInfo.FIELD_LINK_URL]),
        denull(linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION]));

    return extLink;
  }

  /**
   * Creates a JAXR ExternalLink registry objects from the specified Link
   * information objects.
   * 
   * @param linkInfos Collection of the Link information objects to create from.
   * @return Collection of the created JAXR ExternalLink registry objects.
   * @throws JAXRException Error while creating JAXR registry objects.
   */
  private Collection<ExternalLink> createExternalLinks(Collection<String[]> linkInfos)
    throws JAXRException
  {
    if (linkInfos == null || linkInfos.isEmpty())
      return new ArrayList<ExternalLink>();

    ArrayList<ExternalLink> extLinks = new ArrayList<ExternalLink>();
    for (String[] linkInfo : linkInfos)
    {
      extLinks.add(createExternalLink(linkInfo));
    }

    return extLinks;
  }

  /**
   * Creates a JAXR InternationalString object from the specified string value.
   * 
   * @param value The String value.
   * @return The created JAXR InternalString object.
   * @throws JAXRException Error while creating JAXR objects.
   */
  private InternationalString createI18nString(String value)
    throws JAXRException
  {
    InternationalString i18n =
      _lifeCycleMgr.createInternationalString(denull(value));

    return i18n;
  }

  /**
   * Creates a JAXR Key object.
   * 
   * @param key The key value to create from.
   * @return The created JAXR Key object.
   * @throws JAXRException Error while creating the JAXR Key object.
   */
  private Key createKeyObj(String key) throws JAXRException
  {
    return _lifeCycleMgr.createKey(key);
  }


  // ******************************** GET REGISTRY OBJECT Operations *********************************
  
  /**
   * Get the JAXR Concept registry object using the specified key.
   * 
   * @param key The key of the JAXR Concept registry object to get.
   * @throws JAXRException Error while getting the JAXR registry object.
   */
  private Concept getConcept(String key) throws JAXRException
  {
    return (Concept) _queryMgr.getRegistryObject(
      key,
      BusinessLifeCycleManager.CONCEPT);
  }

  /**
   * Get the JAXR ClassificationScheme registry object using the specified key.
   * 
   * @param key The key of the JAXR ClassificationScheme registry object to get.
   * @throws JAXRException Error while getting the JAXR registry object.
   */
  private ClassificationScheme getClassificationScheme(String key)
    throws JAXRException
  {
    return (ClassificationScheme) _queryMgr.getRegistryObject(
      key,
      BusinessLifeCycleManager.CLASSIFICATION_SCHEME);
  }

  /**
   * Get the JAXR Organization registry object using the specified key.
   * 
   * @param key The key of the JAXR Organization registry object to get.
   * @throws JAXRException Error while getting the JAXR registry object.
   */
  private Organization getOrganization(String key) throws JAXRException
  {
    return (Organization) _queryMgr.getRegistryObject(
      key,
      BusinessLifeCycleManager.ORGANIZATION);
  }

  /**
   * Get the JAXR Service registry object using the specified key.
   * 
   * @param key The key of the JAXR Service registry object to get.
   * @throws JAXRException Error while getting the JAXR registry object.
   */
  private Service getService(String key) throws JAXRException
  {
    return (Service) _queryMgr.getRegistryObject(
      key,
      BusinessLifeCycleManager.SERVICE);
  }

  /**
   * Get the JAXR ServiceBinding registry object using the specified key.
   * 
   * @param key The key of the JAXR ServiceBinding registry object to get.
   * @throws JAXRException Error while getting the JAXR registry object.
   */
  private ServiceBinding getServiceBinding(String key) throws JAXRException
  {
    return (ServiceBinding) _queryMgr.getRegistryObject(
      key,
      BusinessLifeCycleManager.SERVICE_BINDING);
  }

  // ******************** UTILITY Operations ******************************
  

  /**
   * Sets the common fields of a registry information object from a JAXR registry object.
   * 
   * @param regInfo The registry information object to set fields to.
   * @param regObj The JAXR registry object to get the field values from.
   * @throws JAXRException Error while getting information from JAXR registry objects.
   */
  private void setGenericFieldsFromRegistry(
    IRegistryInfo regInfo,
    RegistryObject regObj)
    throws JAXRException
  {
    setKey(regInfo, regObj);
    setName(regInfo, regObj);
    setDescription(regInfo, regObj);
    addLinkInfos(regInfo, regObj);
    addIdentifierInfos(regInfo, regObj);
    addCategoryInfos(regInfo, regObj);
  }

  /**
   * Sets the Name into the registry information object.
   * 
   * @param regInfo The registry information object to set name to.
   * @param regObj The JAXR registry object to get name from.
   * @throws JAXRException Error while getting Name from the JAXR
   * registry object.
   */
  private void setName(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    regInfo.setName(getName(regObj));
  }

  /**
   * Sets the Description into the registry information object.
   * 
   * @param regInfo The registry information object to set description to.
   * @param regObj The JAXR registry object to get description from.
   * @throws JAXRException Error while getting Description from the JAXR
   * registry object.
   */
  private void setDescription(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    regInfo.setDescription(getDescription(regObj));
  }

  /**
   * Wrap an object in a Collection object.
   * 
   * @param obj The obj to wrap.
   * @return The created Collection object containing <code>obj</code>
   * as the single element.
   */
  private Collection wrapCollection(Object obj)
  {
    Collection<Object> col = new ArrayList<Object>();
    col.add(obj);
    return col;
  }

  /**
   * Get the first element out from the specified collection.
   * 
   * @param coll The collection.
   * @return The first element in the collection or <b>null</b> if the
   * <code>coll</code> is <b>null</b> or empty.
   */
  private Object getOne(Collection coll)
  {
    Object obj = null;

    if (coll != null && !coll.isEmpty())
    {
      obj = coll.toArray()[0];
    }
    return obj;
  }

  /**
   * Sets the key to JAXR registry object if it is specified in the
   * registry information object.
   * 
   * @param regObj The JAXR registry object to set key to.
   * @param regInfo The registry information object to get key from.
   * @throws JAXRException Error while create JAXR key object or setting
   * Key into the JAXR registry object.
   */
  private void setKeyIfExists(RegistryObject regObj, IRegistryInfo regInfo)
    throws JAXRException
  {
    if (regInfo.getKey() != null)
    {
      regObj.setKey(createKeyObj(regInfo.getKey()));
    }
  }

  /**
   * Sets the key into the registry information object.
   * 
   * @param regInfo The registry information object to set key to.
   * @param regObj The JAXR registry object to get the key value from.
   * @throws JAXRException Error while getting Key from the JAXR registry
   * object.
   */
  private void setKey(IRegistryInfo regInfo, RegistryObject regObj)
    throws JAXRException
  {
    if (regObj.getKey() != null)
    {
      regInfo.setKey(regObj.getKey().getId());
    }
  }

  /**
   * Use the first key in the JAXR BulkResponse object and
   * set into the registry information object.
   * 
   * @param response The JAXR BulkResponse object.
   * @param regInfo The registry information object to set key to.
   * @throws JAXRException The JAXR BulkResponse object indicates
   * error status.
   */
  private void setSingleKeyFromResponse(
    BulkResponse response,
    IRegistryInfo regInfo)
    throws JAXRException
  {
    if (response.getStatus() == JAXRResponse.STATUS_SUCCESS)
    {
      Collection keys = response.getCollection();
      Key keyObj = (Key) keys.iterator().next();
      regInfo.setKey(keyObj.getId());
    }
    else
    {
      //exception
      throw (JAXRException) response.getExceptions().iterator().next();
    }
  }

  /**
   * Gets a non-null Name from the JAXR registry object.
   * 
   * @param ro The JAXR registry object.
   * @return The Name of the JAXR registry object. Returns empty string
   * if no name is specified.
   * @throws JAXRException Error while getting Name from the JAXR registry
   * object.
   */
  private String getName(RegistryObject ro) throws JAXRException
  {
    try
    {
      return ro.getName().getValue();
    }
    catch (NullPointerException npe)
    {
      return "";
    }
  }

  /**
   * Gets a non-null Description from the JAXR registry object.
   * 
   * @param ro The JAXR registry object.
   * @return The Description of the JAXR registry object. Returns empty string
   * if no description is specified.
   * @throws JAXRException Error while getting Description from the JAXR registry
   * object.
   */
  private String getDescription(RegistryObject ro) throws JAXRException
  {
    try
    {
      return ro.getDescription().getValue();
    }
    catch (NullPointerException npe)
    {
      return "";
    }
  }

  /**
   * Returns a non-null string value.
   * 
   * @param val The string value to return.
   * @return <code>val</code> if not null, otherwise returns an
   * empty string.
   */
  private String denull(String val)
  {
    if (val == null)
      val = "";

    return val;
  }

  /**
   * Add Wildcard chars to the specified collection of name patterns.
   * 
   * @param namePatterns Collection of String.
   * @return Collection of Strings from namePatterns with wildcards added.
   */
  private Collection<String> addWildcards(Collection<String> namePatterns)
  {
    if (namePatterns == null)
      return namePatterns;

    String pattern = "%{0}%";
    ArrayList<String> patternList = new ArrayList<String>();
    for (String name : namePatterns)
    {
      patternList.add(MessageFormat.format(pattern, new Object[] { name }));
    }
    return patternList;
  }
}
