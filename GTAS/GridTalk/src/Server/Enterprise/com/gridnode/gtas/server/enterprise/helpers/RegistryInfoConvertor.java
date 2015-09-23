/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryInfoConvertor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2003    Neo Sok Lay         Created
 * Sep 02 2003    Neo Sok Lay         Convert from registry information objects
 *                                    to GridTalk entity objects.
 * Sep 23 2003    Neo Sok Lay         RegistryInfoConvertor.toSearchedBusinessEntity()
 *                                    is no longer statically accessible.
 *                                    Set ConceptInfo.name with Fingerprint.value
 *                                    instead of Fingerprint.type
 * Sep 24 2003    Neo Sok Lay         Move RegistryConnectInfo to PDIPAppServices/BizRegistry
 * Sep 30 2003    Neo Sok Lay         SOAP is changed to be a transport protocol 
 *                                    instead of packaging envelope --
 *                                    Publish Binding with 2 SpecificationLinks (
 *                                    1 for packaging and 1 for transport) for SOAP
 *                                    channel. 
 * Oct 06 2003    Neo Sok Lay         Construct Message service name & description based
 *                                    on patterns. 
 * Oct 10 2003    Neo Sok Lay         Decide the Zip option in PackagingInfo base on
 *                                    the isZipPayload field in the Fingerprint that is
 *                                    a transport protocol.
 * Jan 08 2004    Neo Sok Lay         Move the Zip option to FlowControlInfo. 
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: MessageFormat.format() must
 *                                    pass Object[] instead of String[]
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.bizreg.model.Fingerprint;
import com.gridnode.gtas.server.bizreg.model.Namespace;
import com.gridnode.gtas.server.bizreg.model.TechnicalSpecs;
import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;

import java.text.MessageFormat;
import java.util.*;

/**
 * This is a helper class to convert GridTalk entity models to and from 
 * the registry information models
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryInfoConvertor implements IServiceIdentifierKeys
{
  static final String WEBSITE_LINK_DESCR = "Website";

  private TechnicalSpecs _techSpecs;

  public RegistryInfoConvertor(TechnicalSpecs techSpecs)
  {
    _techSpecs = techSpecs;
  }

  /**
   * Create a Connection information object based on the specified information.
   * 
   * @param queryUrl The url for querying.
   * @param publishUrl The url for publishing.
   * @param user The user for publishing.
   * @param pwd The password for publishing.
   * @return The created Connection information object.
   */
  public static String[] toConnectionInfo(
    String queryUrl,
    String publishUrl,
    String user,
    String pwd)
  {
    String[] connInfo = new String[IConnectionInfo.NUM_FIELDS];
    connInfo[IConnectionInfo.FIELD_PASSWORD] = pwd;
    connInfo[IConnectionInfo.FIELD_PUBLISH_URL] = publishUrl;
    connInfo[IConnectionInfo.FIELD_QUERY_URL] = queryUrl;
    connInfo[IConnectionInfo.FIELD_USER] = user;

    return connInfo;
  }

  /**
   * Create a Connection information object based on the specified RegistryConnectInfo.
   * 
   * @param regConnInfo The RegistryConnectInfo.
   * @return The created Connection information object.
   */
  public static String[] toConnectionInfo(RegistryConnectInfo regConnInfo)
  {
    String[] connInfo = new String[IConnectionInfo.NUM_FIELDS];
    connInfo[IConnectionInfo.FIELD_PASSWORD] = regConnInfo.getPublishPassword();
    connInfo[IConnectionInfo.FIELD_PUBLISH_URL] = regConnInfo.getPublishUrl();
    connInfo[IConnectionInfo.FIELD_QUERY_URL] = regConnInfo.getQueryUrl();
    connInfo[IConnectionInfo.FIELD_USER] = regConnInfo.getPublishUser();

    return connInfo;
  }

  /**
   * Creates an Organization information object based on the specified information.
   * 
   * @param be The GridTalk BusinessEntity entity to create from.
   * @return The created Organization information object.
   */
  public OrganizationInfo toOrganizationInfo(BusinessEntity be)
  {
    OrganizationInfo orgInfo = new OrganizationInfo(be.getDescription());
    orgInfo.setProprietaryObjectKey(be.getKey().toString());
    orgInfo.setProprietaryObjectType(BusinessEntity.ENTITY_NAME);

    WhitePage wp = be.getWhitePage();
    orgInfo.setDescription(wp.getBusinessDesc());
    orgInfo.setPostalAddress(toAddressInfo(wp));
    orgInfo.addContact(toContactInfo(wp));

    String identificationSchemeKey =
      _techSpecs.findNamespaceKey(IBusinessIdentifierKeys.NAMESPACE_TYPE);

    if (!isEmptyStr(be.getEnterpriseId()))
      orgInfo.addIdentifier(
        toIdentifierInfo(
          identificationSchemeKey,
          IBusinessIdentifierKeys.ENTERPRISE_ID,
          be.getEnterpriseId()));
    orgInfo.addIdentifier(
      toIdentifierInfo(
        identificationSchemeKey,
        IBusinessIdentifierKeys.BUS_ENTITY_ID,
        be.getBusEntId()));
    if (!isEmptyStr(wp.getDUNS()))      
      orgInfo.addIdentifier(toIdentifierInfo(
      //identificationSchemeKey,
      SchemeInfo.getDunsSchemeInfo().getKey(),
      //use global DUNS identification scheme
      IBusinessIdentifierKeys.DUNS_NUMBER, wp.getDUNS()));
    if (!isEmptyStr(wp.getGlobalSupplyChainCode()))  
      orgInfo.addIdentifier(
        toIdentifierInfo(
          identificationSchemeKey,
          IBusinessIdentifierKeys.GLOBAL_SC_CODE,
          wp.getGlobalSupplyChainCode()));
    if (!isEmptyStr(wp.getPOBox()))      
      orgInfo.addIdentifier(
        toIdentifierInfo(
          identificationSchemeKey,
          IBusinessIdentifierKeys.PO_BOX,
          wp.getPOBox()));
    if (!isEmptyStr(wp.getLanguage()))      
      orgInfo.addIdentifier(
        toIdentifierInfo(
          identificationSchemeKey,
          IBusinessIdentifierKeys.LANGUAGE,
          wp.getLanguage()));
    if (!isEmptyStr(wp.getWebsite()))
    {    
      orgInfo.addIdentifier(
        toIdentifierInfo(
          identificationSchemeKey,
          IBusinessIdentifierKeys.WEBSITE,
          wp.getWebsite()));
      orgInfo.addLink(toLinkInfo(wp.getWebsite(), WEBSITE_LINK_DESCR));
    }

    return orgInfo;
  }

  /**
   * Checks if a string value is null or contains only whitespaces.
   * 
   * @param str The String value to check.
   * @return <b>true</b> if <code>str</code> is <b>null</b> or
   * contains only whitespaces.
   */
  private boolean isEmptyStr(String str)
  {
    return str == null || str.trim().length() == 0;
  }

  /**
   * Convert a collection of OrganizationInfo objects to SearchedBusinessEntity objects.
   * 
   * @param orgInfos The Collection of OrganizationInfo objects to convert.
   * @return Collection of SearchedBusinessEntity objects.
   */
  public Collection toSearchedBusinessEntityList(Collection orgInfos)
  {
    ArrayList converted = new ArrayList(orgInfos.size());
    for (Iterator i = orgInfos.iterator(); i.hasNext();)
    {
      converted.add(toSearchedBusinessEntity((OrganizationInfo) i.next()));
    }

    return converted;
  }

  /**
   * Convert an OrganizationInfo object to a SearchedBusinessEntity object.
   * 
   * @param orgInfo The OrganizationInfo object to convert from.
   * @return The SearchedBusinessEntity created.
   */
  public SearchedBusinessEntity toSearchedBusinessEntity(OrganizationInfo orgInfo)
  {
    SearchedBusinessEntity be = new SearchedBusinessEntity();
    be.setUuid(orgInfo.getKey());

    String[] params = new String[] { orgInfo.getName(), };
    be.setDescription(
      formatPattern(IBusinessIdentifierKeys.BE_DESC_PATTERN, params));

    WhitePage wp = new WhitePage();
    wp.setBusinessDesc(orgInfo.getDescription());
    setAddressInfo(wp, orgInfo.getPostalAddress());
    setContactInfo(wp, orgInfo.getContacts());
    be.setWhitePage(wp);

    String[][] identifiers = orgInfo.getIdentifiers();
    for (int i = 0; i < identifiers.length; i++)
    {
      String idName = identifiers[i][IIdentifierInfo.FIELD_IDENTIFIER_NAME];
      String idVal = identifiers[i][IIdentifierInfo.FIELD_IDENTIFIER_VALUE];
      if (IBusinessIdentifierKeys.ENTERPRISE_ID.equals(idName))
      {
        be.setEnterpriseId(idVal);
      }
      else if (IBusinessIdentifierKeys.BUS_ENTITY_ID.equals(idName))
      {
        be.setBusEntId(idVal);
      }
      else if (IBusinessIdentifierKeys.DUNS_NUMBER.equals(idName))
      {
        wp.setDUNS(idVal);
      }
      else if (IBusinessIdentifierKeys.GLOBAL_SC_CODE.equals(idName))
      {
        wp.setGlobalSupplyChainCode(idVal);
      }
      else if (IBusinessIdentifierKeys.PO_BOX.equals(idName))
      {
        wp.setPOBox(idVal);
      }
      else if (IBusinessIdentifierKeys.LANGUAGE.equals(idName))
      {
        wp.setLanguage(idVal);
      }
      else if (IBusinessIdentifierKeys.WEBSITE.equals(idName))
      {
        wp.setWebsite(idVal);
      }
    }

    String[][] links = orgInfo.getLinks();
    for (int i = 0; i < links.length; i++)
    {
      String url = links[i][ILinkInfo.FIELD_LINK_URL];
      String desc = links[i][ILinkInfo.FIELD_LINK_DESCRIPTION];
      if (WEBSITE_LINK_DESCR.equals(desc))
      {
        wp.setWebsite(url);
      }
    }

    // services
    String msgServiceName = formatPattern(
                              IServiceIdentifierKeys.MSG_SERVICE_NAME_PATTERN, 
                              new String[]{be.getBusEntId()});
      
    List services = orgInfo.getServices();
    for (Iterator i = services.iterator(); i.hasNext();)
    {
      ServiceInfo serviceInfo = (ServiceInfo) i.next();
      if (msgServiceName.equals(serviceInfo.getName()))
      {
        be.setChannels(toChannelInfos(serviceInfo, be.getEnterpriseId()));
        break;
      }
    }
    return be;
  }

  /**
   * Creates an Identifier information object based on the specified information.
   * 
   * @param identificationSchemeKey The key of the identification scheme.
   * @param name The key name of the identifier to create.
   * @param value The value of the identifier to create.
   * @return The created Identifier information object.
   */
  public String[] toIdentifierInfo(
    String identificationSchemeKey,
    String name,
    String value)
  {
    String[] identifierInfo = new String[IIdentifierInfo.NUM_FIELDS];
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFICATION_SCHEME_KEY] =
      identificationSchemeKey;
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_NAME] = name;
    identifierInfo[IIdentifierInfo.FIELD_IDENTIFIER_VALUE] = value;

    return identifierInfo;
  }

  /**
   * Creates an Address information object based on the specified whitepage
   * information.
   * 
   * @param wp The GridTalk WhitePage entity to create from.
   * @return The created Address information object.
   * @see IAddressInfo
   */
  public String[] toAddressInfo(WhitePage wp)
  {
    String[] addressInfo = new String[IAddressInfo.NUM_FIELDS];
    addressInfo[IAddressInfo.FIELD_CITY] = wp.getCity();
    addressInfo[IAddressInfo.FIELD_STATE] = wp.getState();
    addressInfo[IAddressInfo.FIELD_COUNTRY] = wp.getCountry();
    addressInfo[IAddressInfo.FIELD_STREET] = wp.getAddress();
    addressInfo[IAddressInfo.FIELD_ZIPCODE] = wp.getZipCode();

    return addressInfo;
  }

  /**
   * Sets the address information to a WhitePage.
   * 
   * @param wp The WhitePage to which to set address information.
   * @param addressInfo The Address information as specified in IAddressInfo
   * @see IAddressInfo
   */
  public void setAddressInfo(WhitePage wp, String[] addressInfo)
  {
    wp.setAddress(addressInfo[IAddressInfo.FIELD_STREET]);
    wp.setCity(addressInfo[IAddressInfo.FIELD_CITY]);
    wp.setCountry(addressInfo[IAddressInfo.FIELD_COUNTRY]);
    wp.setState(addressInfo[IAddressInfo.FIELD_STATE]);
    wp.setZipCode(addressInfo[IAddressInfo.FIELD_ZIPCODE]);
  }

  /**
   * Creates a Contact information object based on the specified whitepage
   * information.
   * 
   * @param wp The GridTalk WhitePage entity to create from.
   * @return The created Contact information object.
   * @see IContactInfo
   */
  public String[] toContactInfo(WhitePage wp)
  {
    String[] contactInfo = new String[IContactInfo.NUM_FIELDS];
    contactInfo[IContactInfo.FIELD_CONTACT_EMAIL] = wp.getEmail();
    contactInfo[IContactInfo.FIELD_CONTACT_FAX] = wp.getFax();
    contactInfo[IContactInfo.FIELD_CONTACT_NAME] = wp.getContactPerson();
    contactInfo[IContactInfo.FIELD_CONTACT_TEL] = wp.getTel();

    return contactInfo;
  }

  /**
   * Sets contact information to a WhitePage.
   * 
   * @param wp The WhitePage to which contact information will be set.
   * @param contactInfos The contact information as specified in IContactInfo
   * @see IContactInfo
   */
  public void setContactInfo(WhitePage wp, String[][] contactInfos)
  {
    if (contactInfos.length > 0)
    {
      //take the first contact info
      wp.setContactPerson(contactInfos[0][IContactInfo.FIELD_CONTACT_NAME]);
      wp.setEmail(contactInfos[0][IContactInfo.FIELD_CONTACT_EMAIL]);
      wp.setFax(contactInfos[0][IContactInfo.FIELD_CONTACT_FAX]);
      wp.setTel(contactInfos[0][IContactInfo.FIELD_CONTACT_TEL]);
    }
  }

  /**
   * Creates a Service information object based on the specified information.
   * 
   * @param beId ID of Business Entity that the service is created for.
   * @param orgKey Key of the Organization that offered this service.
   * @param channels Collection of GridTalk ChannelInfo entity(s) to
   * create ServiceBinding(s) from.
   * @return The created Service information object.
   */
  public ServiceInfo toServiceInfo(String beId, String orgKey, Collection channels)
  {
    String[] params = new String[] {beId, };
    
    String serviceName = formatPattern(IServiceIdentifierKeys.MSG_SERVICE_NAME_PATTERN, params);
    String serviceDesc = formatPattern(IServiceIdentifierKeys.MSG_SERVICE_DESC_PATTERN, params);

    ServiceInfo serviceInfo = new ServiceInfo(serviceName, orgKey);
    serviceInfo.setDescription(serviceDesc);
    serviceInfo.setProprietaryObjectKey(orgKey);
    serviceInfo.setProprietaryObjectType(MSG_SERVICE_PROPRIETARY_OBJ_TYPE);
    ChannelInfo channel;

    //String identificationSchemeKey = 
    //  _techSpecs.findNamespaceKey(IServiceIdentifierKeys.NAMESPACE_TYPE);
    Namespace messagingStandards =
      _techSpecs.findNamespace(IMessagingStandardKeys.NAMESPACE_TYPE);
    for (Iterator i = channels.iterator(); i.hasNext();)
    {
      channel = (ChannelInfo) i.next();
      Fingerprint[] fingerprints =
        messagingStandards
          .findFingerprints(new String[] {
            channel.getPackagingProfile().getEnvelope(),
        // packaging
        channel.getTptProtocolType() // transport
      });

      if (fingerprints[0] != null
        || fingerprints[1] != null) // make sure it supports at least one tmodel
        serviceInfo.addBinding(
          toBindingInfo(channel, fingerprints, messagingStandards.getKey()));
    }
    return serviceInfo;
  }

  /**
   * Convert a ServiceInfo objects to a collection of ChannelInfo entity objects base
   * on the BindingInfos of the ServiceInfo.
   * 
   * @param serviceInfo The Collection of ServiceInfo objects to convert from.
   * @param enterpriseId Enterprise Id
   * @return Collection of ChannelInfo entity objects created.
   */
  public Collection toChannelInfos(
    ServiceInfo serviceInfo,
    String enterpriseId)
  {
    Object[] bindings = serviceInfo.getBindings().toArray();
    Arrays.sort(bindings);
    // sort, so that can retrieve later on in same order.

    Collection channelInfos = new ArrayList(bindings.length);
    BindingInfo bindingInfo;
    for (int i = 0; i < bindings.length; i++)
    {
      bindingInfo = (BindingInfo) bindings[i];
      channelInfos.add(toChannelInfo(bindingInfo, enterpriseId));
    }

    return channelInfos;
  }

  /**
   * Creates a Binding information object based on the specified information.
   * 
   * @param channel The GridTalk ChannelInfo entity to create from.
   * @param fingerprints Technical fingerprints linked to the binding.
   * @param namespaceKey Key of the Namespace in which the fingerprint is in.
   * @return The created Binding information object.
   */
  public BindingInfo toBindingInfo(ChannelInfo channel,
  //String identificationSchemeKey,
  Fingerprint[] fingerprints, String namespaceKey)
  {
    BindingInfo bindingInfo = new BindingInfo(channel.getName());
    bindingInfo.setProprietaryObjectKey(channel.getKey().toString());
    bindingInfo.setProprietaryObjectType(ChannelInfo.ENTITY_NAME);

    bindingInfo.setDescription(channel.getName());
    bindingInfo.setAccessUri(channel.getTptCommInfo().getURL());

    for (int i = 0; i < fingerprints.length; i++)
    {
      if (fingerprints[i] != null)
        bindingInfo.addSpecificationLink(
          toSpecificationInfo(channel, fingerprints[i], namespaceKey));
    }
    /* Not Supported for ServiceBinding
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.REFERENCE_ID,
      channel.getReferenceId()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.CHANNEL_DESC,
      channel.getDescription()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.PACKAGING_NAME,
      channel.getPackagingProfile().getName()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.PACKAGING_DESC,
      channel.getPackagingProfile().getDescription()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.SECURITY_DESC,
      channel.getSecurityProfile().getDescription()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.SECURITY_NAME,
      channel.getSecurityProfile().getName()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.TRANSPORT_NAME,
      channel.getTptCommInfo().getName()));
    bindingInfo.addIdentifier(toIdentifierInfo(
      identificationSchemeKey,
      IServiceIdentifierKeys.TRANSPORT_DESC,
      channel.getTptCommInfo().getDescription()));
    */
    return bindingInfo;
  }

  /**
   * Convert a BindingInfo object to a ChannelInfo entity object.
   * 
   * @param bindingInfo The BindingInfo object to convert from
   * @param enterpriseId Enterprise id
   * @return The ChannelInfo entity object created.
   */
  public ChannelInfo toChannelInfo(
    BindingInfo bindingInfo,
    String enterpriseId)
  {
    ChannelInfo channel = new ChannelInfo();
    PackagingInfo packagingInfo = new PackagingInfo();
    SecurityInfo securityInfo = new SecurityInfo();
    CommInfo commInfo = new CommInfo();
    FlowControlInfo flowCtrl = new FlowControlInfo();

    channel.setName(bindingInfo.getDescription());
    commInfo.setURL(bindingInfo.getAccessUri());

    String packagingEnvelope = PackagingInfo.DEFAULT_ENVELOPE_TYPE;
    String protocolType = CommInfo.HTTP;
    boolean zipPayload = false;

    SpecificationInfo specInfo;
    Fingerprint fingerprint;
    for (Iterator i = bindingInfo.getSpecificationLinks().iterator();
      i.hasNext();
      )
    {
      specInfo = (SpecificationInfo) i.next();
      fingerprint =
        _techSpecs
          .findNamespace(IMessagingStandardKeys.NAMESPACE_TYPE)
          .findFingerprintByValue(specInfo.getName());

      if (fingerprint.isTransportProtocol())
      {
        protocolType = fingerprint.getType();
        zipPayload = fingerprint.isZipPayload();
      }
      else
      {
        packagingEnvelope = fingerprint.getType();
      }
    }
    packagingInfo.setEnvelope(packagingEnvelope);
    commInfo.setProtocolType(protocolType);
    channel.setTptProtocolType(protocolType);
    /*080104NSL
    packagingInfo.setZip(zipPayload);
    */
    flowCtrl.setIsZip(zipPayload);
    
    /*030930
    Set specLinks = bindingInfo.getSpecificationLinks();
    if (specLinks.size() > 0)
    {
      SpecificationInfo specInfo = (SpecificationInfo) specLinks.toArray()[0];
      Fingerprint fingerprint =
        _techSpecs
          .findNamespace(IMessagingStandardKeys.NAMESPACE_TYPE)
          .findFingerprintByValue(specInfo.getName());
      packagingInfo.setEnvelope(fingerprint.getType());
    }
    */

    // set the reference ids
    channel.setReferenceId(enterpriseId);
    packagingInfo.setReferenceId(enterpriseId);
    securityInfo.setReferenceId(enterpriseId);
    commInfo.setRefId(enterpriseId);

    // set the profile names and descriptions
    // temporary workaround
    String[] params = new String[] { channel.getName()};
    channel.setDescription(
      formatPattern(IServiceIdentifierKeys.CHANNEL_DESC_PATTERN, params));
    packagingInfo.setName(
      formatPattern(IServiceIdentifierKeys.PACKAGING_NAME_PATTERN, params));
    packagingInfo.setDescription(
      formatPattern(IServiceIdentifierKeys.PACKAGING_DESC_PATTERN, params));
    securityInfo.setName(
      formatPattern(IServiceIdentifierKeys.SECURITY_NAME_PATTERN, params));
    securityInfo.setDescription(
      formatPattern(IServiceIdentifierKeys.SECURITY_DESC_PATTERN, params));
    commInfo.setName(
      formatPattern(IServiceIdentifierKeys.TRANSPORT_NAME_PATTERN, params));
    commInfo.setDescription(
      formatPattern(IServiceIdentifierKeys.TRANSPORT_DESC_PATTERN, params));

    // setup default values    
    commInfo.setTptImplVersion(CommInfo.DEFAULT_TPTIMPL_VERSION);
    securityInfo.setEncryptionType(SecurityInfo.ENCRYPTION_TYPE_NONE);
    securityInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_NONE);

    channel.setPackagingProfile(packagingInfo);
    channel.setSecurityProfile(securityInfo);
    channel.setTptCommInfo(commInfo);
    channel.setFlowControlInfo(flowCtrl);

    return channel;
  }

  public static String formatPattern(String pattern, Object[] params)
  {
    return MessageFormat.format(pattern, params);
  }

  /**
   * Creates a Specification information object based on the specified information.
   * 
   * @param channel The ChannelInfo entity to create from.
   * @param fingerprint The technical fingerprint linked by the specification.
   * @param namespaceKey Key of the Namespace which the fingerprint is in.
   * @return The created Specification information object.
   */
  public SpecificationInfo toSpecificationInfo(
    ChannelInfo channel,
    Fingerprint fingerprint,
    String namespaceKey)
  //String identificationSchemeKey)
  {
    PackagingInfo pkg = channel.getPackagingProfile();

    // dummy values: the specification name & description are not relevant
    // the Name will be taken automatically taken from the ConceptInfo
    SpecificationInfo specInfo = new SpecificationInfo(fingerprint.getValue());
    specInfo.setDescription(pkg.getDescription());

    if (fingerprint != null)
    {
      specInfo.setOverviewLink(
        toLinkInfo(
          fingerprint.getOverviewUrl(),
          fingerprint.getOverviewDescription()));
    }

    ConceptInfo conceptInfo =
      new ConceptInfo(
        namespaceKey,
        fingerprint.getValue(),
        fingerprint.getValue());
    conceptInfo.setKey(fingerprint.getKey());
    specInfo.setSpecifiedObject(conceptInfo);

    return specInfo;
  }

  /**
   * Creates a Link information object based on the specified information.
   * 
   * @param conceptInfo The Concept information object to create from.
   * @return The created Link information object.
   * @see ILinkInfo
   */
  public String[] toLinkInfo(ConceptInfo conceptInfo)
  {
    return conceptInfo.getOverviewLink();
  }

  /**
   * Creates a Link information object based on the specified information.
   * 
   * @param url The url
   * @param description The description of the link.
   * @return The created Link information object.
   * @see ILinkInfo
   */
  public String[] toLinkInfo(String url, String description)
  {
    String[] linkInfo = new String[ILinkInfo.NUM_FIELDS];
    linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION] = description;
    linkInfo[ILinkInfo.FIELD_LINK_URL] = url;

    return linkInfo;
  }

  /**
   * If the specified <code>val</code> is null or empty, returns the <code>defVal</code> specified.
   * Otherwise, returns <code>val</code> as it is.
   * 
   * @param val The string value to check.
   * @param defVal The default value to return if <code>val</code> is null or empty.
   * @return <code>val</code> or <code>defVal</code>
   */
  public String setIfEmpty(String val, String defVal)
  {
    if (val == null || val.trim().length() == 0)
      val = defVal;

    return val;
  }
}
