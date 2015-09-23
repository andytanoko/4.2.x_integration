/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Modify publishTechSpecs logic.
 *                                    Format coding style.
 * Sep 23 2003    Neo Sok Lay         Set ConceptInfo.name with Fingerprint.value
 *                                    instead of Fingerprint.type
 */
package com.gridnode.gtas.server.bizreg.helpers;

import com.gridnode.gtas.server.bizreg.model.Fingerprint;
import com.gridnode.gtas.server.bizreg.model.Namespace;
import com.gridnode.gtas.server.bizreg.model.Taxonomy;
import com.gridnode.gtas.server.bizreg.model.TechnicalSpecs;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.app.bizreg.pub.model.ConceptInfo;
import com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo;
import com.gridnode.pdip.app.bizreg.pub.model.ILinkInfo;
import com.gridnode.pdip.app.bizreg.pub.model.SchemeInfo;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Delegate handles business with the public registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class PublishDelegate implements IRegistryConfig
{
  /**
   * Connects to the public registry using the specified connection info.
   * 
   * @param connInfo The connection information object.
   * @throws Exception Error occurs during connection.
   * @see com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo
   */
  public static void connectToRegistry(String[] connInfo) throws Exception
  {
    getPublicRegistryMgr().connectToRegistry(connInfo);
  }

  /**
   * Gets the default TechnicalSpecs that need to be published
   * to the public registry.
   * 
   * @return The TechnicalSpecs object.
   */
  public static TechnicalSpecs getTechnicalSpecifications()
  {
    TechnicalSpecs techspecs =
      TechnicalSpecsHandler.getInstance().loadSpecs(DEFAULT_TECH_SPECS_NAME);

    return techspecs;
  }

  /**
   * Public the technical specifications contained in the TechnicalSpecs
   * object to the public registry. 
   * 
   * @param techspecs The TechnicalSpecs object.
   * @param connInfo The connection information object.
   * @return The TechnicalSpecs object containing the publiched
   * technical specifications.
   * @throws Exception Error during the publishing.
   */
  public static TechnicalSpecs publishTechnicalSpecs(
    TechnicalSpecs techspecs,
    String[] connInfo)
    throws Exception
  {
    IPublicRegistryManager mgr = getPublicRegistryMgr();
    Logger.debug(
      "[PublishDelegate.publishTecnicalSpecs] TechSpecs = " + techspecs);

    Namespace[] namespaces = techspecs.getNamespaces();
    if (namespaces != null)
    {
      mgr.connectToRegistry(connInfo);
      for (int i = 0; i < namespaces.length; i++)
      {
        publishNamespace(namespaces[i], connInfo, mgr);
      }
    }

    return techspecs;
  }

  /**
   * Publish a technical Namespace model if it does not exist in the target registry.
   * 
   * @param namespace The Namespace model to publish
   * @param connInfo The Connection information to the target registry.
   * @param mgr The public registry manager.
   * @throws Exception Error in publishing the Namespace model.
   */
  private static void publishNamespace(
    Namespace namespace,
    String[] connInfo,
    IPublicRegistryManager mgr)
    throws Exception
  {
    SchemeInfo schemeInfo =
      findExistingNamespace(
        namespace,
        connInfo[IConnectionInfo.FIELD_QUERY_URL],
        mgr);

    if (schemeInfo == null)
    {
      //publish
      schemeInfo =
        (SchemeInfo) mgr.publish(createSchemeInfo(namespace), connInfo);
    }
    else
    {
      updateSchemeInfo(schemeInfo, namespace);
      // create mapping if not exist
      mgr.addRegistryObjectMapping(schemeInfo, connInfo, false);
    }

    String schemeKey = schemeInfo.getKey();
    namespace.setKey(schemeKey);

    Fingerprint[] fingerprints = namespace.getFingerprints();

    if (fingerprints != null)
    {
      publishFingerprints(fingerprints, schemeKey, connInfo, mgr);
    }
  }

  /**
   * Publish technical Fingerprint models if it does not exist at the target registry.
   * 
   * @param fingerprints The Fingerprint models to publish
   * @param schemeKey Key of the Namespace of the Fingerprint models.
   * @param connInfo The connection information to the target registry.
   * @param mgr Public registry manager.
   * @throws Exception Error in publishing the fingerprints.
   */
  private static void publishFingerprints(
    Fingerprint[] fingerprints,
    String schemeKey,
    String[] connInfo,
    IPublicRegistryManager mgr)
    throws Exception
  {
    for (int j = 0; j < fingerprints.length; j++)
    {
      ConceptInfo conceptInfo =
        findExistingFingerprint(
          fingerprints[j],
          connInfo[IConnectionInfo.FIELD_QUERY_URL],
          mgr);

      if (conceptInfo == null)
      {
        //publish
        conceptInfo =
          (ConceptInfo) mgr.publish(
            createConceptInfo(fingerprints[j], schemeKey),
            connInfo);
      }
      else
      {
        // could not retrieve the parent from Concept, so explicity set here
        conceptInfo.setParentKey(schemeKey);
        updateConceptInfo(conceptInfo, fingerprints[j]);

        //create mapping if not exist
        mgr.addRegistryObjectMapping(conceptInfo, connInfo, false);
      }

      fingerprints[j].setKey(conceptInfo.getKey());
    }
  }

  /**
   * Updates SchemeInfo object using the specified Namespace. Mainly updates the proprietary information.
   * 
   * @param schemeInfo The SchemeInfo object to update.
   * @param namespace The Namespace to obtain values from.
   */
  private static void updateSchemeInfo(
    SchemeInfo schemeInfo,
    Namespace namespace)
  {
    schemeInfo.setDescription(namespace.getDescription());
    schemeInfo.setProprietaryObjectKey(namespace.getType());
    schemeInfo.setProprietaryObjectType(Namespace.getObjectType());
  }

  /**
   * Creates a SchemeInfo object using the values in the specificed Namespace.
   * 
   * @param namespace The Namespace to obtain values from.
   * @return The created SchemeInfo object.
   */
  public static SchemeInfo createSchemeInfo(Namespace namespace)
  {
    SchemeInfo schemeInfo = new SchemeInfo(namespace.getValue());
    updateSchemeInfo(schemeInfo, namespace);

    Taxonomy[] taxonomies = namespace.getTaxonomies();
    for (int i = 0; i < taxonomies.length; i++)
    {
      schemeInfo.addCategory(
        createCategory(
          taxonomies[i].getKey(),
          null,
          taxonomies[i].getName(),
          taxonomies[i].getValue()));
    }

    return schemeInfo;
  }

  /**
   * Updates a ConceptInfo object using the specified Fingerprint. Mainly the proprietary information
   * are updated.
   * 
   * @param conceptInfo The ConceptInfo object to update.
   * @param fingerprint The Fingerprint to obtain values from.
   */
  private static void updateConceptInfo(
    ConceptInfo conceptInfo,
    Fingerprint fingerprint)
  {
    String[] linkInfo = new String[ILinkInfo.NUM_FIELDS];
    linkInfo[ILinkInfo.FIELD_LINK_DESCRIPTION] =
      fingerprint.getOverviewDescription();
    linkInfo[ILinkInfo.FIELD_LINK_URL] = fingerprint.getOverviewUrl();
    conceptInfo.setOverviewLink(linkInfo);
    conceptInfo.setProprietaryObjectKey(fingerprint.getType());
    conceptInfo.setProprietaryObjectType(Fingerprint.getObjectType());
  }

  /**
   * Creates a ConceptInfo object from a specified Fingerprint.
   * 
   * @param fingerprint The Fingerprint to obtain values from.
   * @param schemeKey Key of the Namespace of the Fingerprint.
   * @return The created ConceptInfo.
   */
  public static ConceptInfo createConceptInfo(
    Fingerprint fingerprint,
    String schemeKey)
  {
    ConceptInfo conceptInfo =
      new ConceptInfo(schemeKey, fingerprint.getValue(), fingerprint.getValue());

    updateConceptInfo(conceptInfo, fingerprint);

    Taxonomy[] taxonomies = fingerprint.getTaxonomies();
    for (int i = 0; i < taxonomies.length; i++)
    {
      conceptInfo.addCategory(
        createCategory(
          taxonomies[i].getKey(),
          null,
          taxonomies[i].getName(),
          taxonomies[i].getValue()));
    }

    return conceptInfo;
  }

  /**
   * Tries to discover a Namespace model from the target registry, and returns the corresponding SchemeInfo
   * for the Namespace.
   * 
   * @param namespace The Namespace containing the information for the query.
   * @param queryUrl The Query URL of the target registry.
   * @param mgr The Public registry manager.
   * @return The SchemeInfo for the discovered Namespace, or <b>null</b> if none is discovered.
   * @throws Exception Error querying from the registry.
   */
  private static SchemeInfo findExistingNamespace(
    Namespace namespace,
    String queryUrl,
    IPublicRegistryManager mgr)
    throws Exception
  {
    ArrayList names = new ArrayList();
    names.add(namespace.getValue());

    //category
    Collection categories = createCategoryInfos(namespace.getTaxonomies());
    if (categories.isEmpty())
      categories = null;

    Collection existingNamespaces =
      mgr.querySchemeInfos(queryUrl, names, true, categories, null);

    return existingNamespaces.isEmpty()
      ? null
      : (SchemeInfo) existingNamespaces.toArray()[0];
  }

  /**
   * Tries to discover a Fingerprint model from the target registry, and returns the corresponding ConceptInfo
   * for the Fingerprint.
   * 
   * @param fingerprint The Fingerprint containing the information for the query.
   * @param queryUrl The Query URL of the target registry.
   * @param mgr The Public registry manager.
   * @return The ConceptInfo for the discovered Fingerprint, or <b>null</b> if none is discovered. 
   * @throws Exception Error querying from the registry.
   */
  private static ConceptInfo findExistingFingerprint(
    Fingerprint fingerprint,
    String queryUrl,
    IPublicRegistryManager mgr)
    throws Exception
  {
    ArrayList names = new ArrayList();
    names.add(fingerprint.getValue());

    //category
    Collection categories = createCategoryInfos(fingerprint.getTaxonomies());
    if (categories.isEmpty())
      categories = null;
      
    Collection existingFingerprints =
      mgr.queryConceptInfos(queryUrl, names, true, categories, null, null);

    return existingFingerprints.isEmpty()
      ? null
      : (ConceptInfo) existingFingerprints.toArray()[0];
  }

  /**
   * Create Category information objects for each specified Taxonomy.
   * 
   * @param taxonomies Array of Taxonomy objects to create Category for.
   * @return Collection of Category information objects (String[]) created, or empty collection
   * if no Taxonomy is specified.
   */
  private static Collection createCategoryInfos(Taxonomy[] taxonomies)
  {
    ArrayList categories = new ArrayList();
    
    if (taxonomies != null && taxonomies.length > 0)
    {
      for (int i = 0; i < taxonomies.length; i++)
      {
        categories.add(
          createCategory(
            taxonomies[i].getKey(),
            null,
            taxonomies[i].getName(),
            taxonomies[i].getValue()));
      }
    }
    
    return categories;
  }
  
  /**
   * Get all the Taxonomies for certain Fingerprints of a Namespace.
   * 
   * @param namespace The Namespace.
   * @param fingerprinValues The Values of the Fingerprint models to get Taxonomies from.
   * @param queryUrl The Query URL of the target registry.
   * @return Collection of CategoryInfo(s) for the Taxonomies, or <b>null</b> if no Taxonomy found.
   */
  public static Collection getAllTaxonomies(
    Namespace namespace,
    Collection fingerprintValues)
  {
    ArrayList categories = new ArrayList();
    Fingerprint fingerprint;
    for (Iterator i = fingerprintValues.iterator(); i.hasNext();)
    {
      fingerprint = namespace.findFingerprintByValue((String) i.next());
      if (fingerprint != null)
        categories.addAll(createCategoryInfos(fingerprint.getTaxonomies()));
    }
    if (categories.isEmpty())
      categories = null;

    return categories;
  }

  /**
   * Creates a Category information object.
   * 
   * @param schemeKey The Key of the SchemeInfo for classifying  
   * @param conceptKey The Key of the ConceptInfo for classifying, if any
   * @param categoryName Name of the category
   * @param categoryValue Value of the category
   * @return The create Category information object.
   */
  private static String[] createCategory(
    String schemeKey,
    String conceptKey,
    String categoryName,
    String categoryValue)
  {
    String[] categoryInfo = new String[ICategoryInfo.NUM_FIELDS];

    categoryInfo[ICategoryInfo.FIELD_CATEGORY_NAME] = categoryName;
    categoryInfo[ICategoryInfo.FIELD_CATEGORY_VALUE] = categoryValue;
    categoryInfo[ICategoryInfo.FIELD_SCHEME_KEY] = schemeKey;
    categoryInfo[ICategoryInfo.FIELD_CONCEPT_KEY] = conceptKey;

    return categoryInfo;
  }

  /**
   * Obtain the interface for the PublicRegistryManagerBean.
   *
   * @return The interface to the PublicRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since GT 2.2
   */
  public static IPublicRegistryManager getPublicRegistryMgr()
    throws ServiceLookupException
  {
    return (IPublicRegistryManager) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IPublicRegistryManagerHome.class.getName(),
        IPublicRegistryManagerHome.class,
        new Object[0]);
  }

}
