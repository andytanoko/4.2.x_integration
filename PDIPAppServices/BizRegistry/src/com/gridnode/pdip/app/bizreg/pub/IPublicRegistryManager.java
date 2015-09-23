/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPublicRegistryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 01 2003    Neo Sok Lay         Add queryOrganizationInfos()
 * Sep 08 2003    Neo Sok Lay         Add: addRegistryObjectMapping()
 * Sep 18 2003    Neo Sok Lay         Add: querySchemeInfos()
 *                                    Format coding style.
 * Oct 01 2003    Neo Sok Lay         Add: 
 *                                    - findRegistryObjectMappingsByProprietary(
 *                                        proprietaryObjectType,
 *                                        proprietaryObjectKey)
 *                                    - findRegistryObjectMappingByProprietary(
 *                                        proprietaryObjectType,
 *                                        proprietaryObjectKey,
 *                                        registryQueryUrl)
 *                                    - findRegistryObjectMapping(
 *                                        registryObjectType,
 *                                        registryObjectKey,
 *                                        registryQueryUrl)
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException.                                       
 * Dec 14 2005    Tam Wei Xiang       all the method will throw RemoteException       
 * Mar 01 2006    Neo Sok Lay         Use generics                                
 */
package com.gridnode.pdip.app.bizreg.pub;

import com.gridnode.pdip.app.bizreg.exceptions.RegistryProviderException;
import com.gridnode.pdip.app.bizreg.exceptions.RegistryPublishException;
import com.gridnode.pdip.app.bizreg.exceptions.RegistryQueryException;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 * This interface defines the business methods for implementing
 * the manager for a public registry. 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IPublicRegistryManager
{
  /**
   * Connect to the public registry.
   * 
   * @param connectionInfo The connection information required to make the
   * connection.
   * @throws RegistryProviderException Exception occurs at the registry provider
   * layer during connection.
   * @throws SystemException Unexpected system exception occurs.
   * @see com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo
   */
  public void connectToRegistry(String[] connectionInfo)
    throws RegistryProviderException, SystemException, RemoteException;

  /**
   * Disconnects from the public registry.
   * 
   * @param connectionInfo The Connection information that made the connection
   * to the registry earlier.
   * @throws RegistryProviderException Exception occurs at the registry provider
   * layer during disconnection.
   * @throws SystemException Unexpected system exception occurs.
   * @see com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo
   */
  public void disconnectFromRegistry(String[] connectionInfo)
    throws RegistryProviderException, SystemException, RemoteException;

  /**
   * Publish a registry information object to the public registry.
   * 
   * @param regInfo The registry information object to publish.
   * @param connectionInfo The connection information required for publishing.
   * @return The published registry information object, populated with the
   * registry key if successfully published.
   * @throws RegistryPublishException Exception occurs when publishing the
   * specified registry information object.
   * @throws SystemException Unexpected system exception occurs.
   * @see com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo
   * @see com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo
   * @see com.gridnode.pdip.app.bizreg.pub.model.ServiceInfo
   * @see com.gridnode.pdip.app.bizreg.pub.model.SchemeInfo
   * @see com.gridnode.pdip.app.bizreg.pub.model.ConceptInfo
   */
  public IRegistryInfo publish(IRegistryInfo regInfo, String[] connectionInfo)
    throws RegistryPublishException, SystemException, RemoteException;

  /**
   * Find a registry information object that have been published (from here)
   * to a public registry and still queriable from public registry. 
   * 
   * @param registryObjectType The type of the registry information object to find.
   * @param proprietaryObjectType The type of the proprietary object that the
   * registry information object is mapped to.
   * @param proprietaryObjectKey The key of the proprietary object that the
   * registry information object is mapped to.
   * @param queryUrl Url to use to query from the public registry for the 
   * registry object.
   * @return The queried registry information object, or <b>null</b> if 
   * the registry information object (implied) have not been published
   * from (here).
   * @throws RegistryQueryException Error in performing the query.
   * @throws SystemException Unexpected system exception.
   */
  public IRegistryInfo findQueriableObject(
    int registryObjectType,
    String proprietaryObjectType,
    String proprietaryObjectKey,
    String queryUrl)
    throws RegistryQueryException, SystemException, RemoteException;

  /**
   * Query the registry for Scheme information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param queryUrl The Url for querying the registry.
   * @param namePatterns Collection of patterns of the Concept names. Assume OR match. <b>null</b> if not required.
   * @param matchAllKeys <b>true</b> to match ALL keys specified in each of the rest of Collection parameters. <b>false</b>
   * to match ANY key specified in each of the rest of Collection parameters.
   * @param categories Collection of Category information objects contained in the Scheme's categoryBag. Assume AND match.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Scheme information object. Assume AND match.
   * <b>null</b> if not required.
   * @return Collection of SchemeInfo objects that are queried from the registry.
   * @throws RegistryQueryException Error during query.
   * @throws SystemException Unexpected system exception.
   */
  public Collection<SchemeInfo> querySchemeInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException, RemoteException;

  /**
   * Query the registry for Organization information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param queryUrl The Url for querying the registry.
   * @param namePatterns Collection of patterns of the Organization names. Assume OR match. <b>null</b> if not required.
   * @param matchAllKeys <b>true</b> to match ALL keys specified in each of the rest of Collection parameters. <b>false</b>
   * to match ANY key specified in each of the rest of Collection parameters.
   * @param categories Collection of Category information objects contained in the Organization's categoryBag. 
   * <b>null</b> if not required.
   * @param specifications Collection of Concept (for UDDI) information objects in the Organization's tModelBag.
   * <b>null</b> if not required.
   * @param identifiers Collection of Identifier information objects in the Organization's IdentifierBag.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Organization information object.
   * <b>null</b> if not required.
   * @return Collection of OrganizationInfo objects that are queried from the registry.
   * @throws RegistryQueryException Error during query.
   * @throws SystemException Unexpected system exception.
   */
  public Collection<OrganizationInfo> queryOrganizationInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<ConceptInfo> specifications,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException, RemoteException;

  /**
   * Query the registry for Service information objects that match all specified criteria from an Organization.
   * 
   * @param queryUrl The url for querying the registry.
   * @param orgKey Registry key of the Organization information object.
   * @param namePatterns Collection of patterns of the Service names. <b>null</b> if not required.
   * @param categories Collection of Category information objects contained in the Service's categoryBag.
   * Assume AND match. <b>null</b> if not required.
   * @param specifications Collection of Concept (for UDDI) information objects in the Service's tModelBag.
   * Assume AND match. <b>null</b> if not required.
   * @return Collection of ServiceInfo objects that are queried from the registry.
   * @throws RegistryQueryException Error during query.
   * @throws SystemException Unexpected system exception.
   */
  //  public Collection queryServiceInfos(
  //    String queryUrl, String orgKey,
  //    Collection namePatterns, Collection categories, Collection specifications)  
  //    throws RegistryQueryException, SystemException;

  /**
   * Query the registry for Binding information objects that match all specified criteria from a Service.
   * 
   * @param queryUrl The url for querying the registry.
   * @param serviceKey Registry key of the Service information object.
   * @param categories Collection of Category information objects contained in the Binding's categoryBag.
   * Assume AND match. <b>null</b> if not required.
   * @param specifications Collection of Concept (for UDDI) information objects in the Binding's tModelBag.
   * Assume AND match. <b>null</b> if not required.
   * @return Collection of BindingInfo objects that are queried from the registry.
   * @throws RegistryQueryException Error during query.
   * @throws SystemException Unexpected system exception.
   */
  //  public Collection queryBindingInfos(
  //    String queryUrl, String serviceKey,
  //    Collection categories, Collection specifications)  
  //    throws RegistryQueryException, SystemException;

  /**
   * Query the registry for Concept information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param queryUrl The Url for querying the registry.
   * @param namePatterns Collection of patterns of the Concept names. Assume OR match. <b>null</b> if not required.
   * @param matchAllKeys <b>true</b> to match ALL keys specified in each of the rest of Collection parameters. <b>false</b>
   * to match ANY key specified in each of the rest of Collection parameters.
   * @param categories Collection of Category information objects contained in the Concept's categoryBag. Assume AND match.
   * <b>null</b> if not required.
   * @param identifiers Collection of Identifier information objects in the Concept's IdentifierBag. Assume AND match.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Concept information object. Assume AND match.
   * <b>null</b> if not required.
   * @return Collection of ConceptInfo objects that are queried from the registry.
   * @throws RegistryQueryException Error during query.
   * @throws SystemException Unexpected system exception.
   */
  public Collection<ConceptInfo> queryConceptInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException, RemoteException;

  /**
   * Publish a registry information object if it is not queriable from the
   * registry.
   * 
   * @param regInfo The registry information object.
   * @param connInfo The connection information to the registry.
   * @return The queried or published registry information object.
   * @throws RegistryPublishException Error in publishing.
   * @throws RegistryQueryException Error in querying.
   * @throws SystemException Unexpected error.
   */
  //  public IRegistryInfo publishIfNotQueriable(
  //    IRegistryInfo regInfo,
  //    String[] connInfo)  
  //  throws RegistryPublishException, RegistryQueryException, SystemException;

  /**
   * Add a RegistryObjectMapping record for the specified registry information 
   * object.
   * 
   * @param regInfo The registry information object.
   * @param connInfo The connection information for publishing/querying the
   * <code>regInfo</code>
   * @param isPublished <b>true</b> if the registry information object is
   * published from here.
   * @throws CreateEntityException Error in creating the RegistryObjectMapping
   * object.
   * @throws SystemException Unexpected system error.
   */
  public void addRegistryObjectMapping(
    IRegistryInfo regInfo,
    String[] connInfo,
    boolean isPublished)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Find the RegistryObjectMapping(s) that maps the specified proprietary object to published or queried 
   * registry objects.
   * 
   * @param proprietaryObjectType The type of proprietary object.
   * @param proprietaryObjectKey The key of the proprietary object.
   * @return Collection of RegistryObjectMapping objects found.
   * @throws FindEntityException Application error while performing the find.
   * @throws SystemException Unexpected system error encountered e.g. fail to retrieve from database
   */
  public Collection<RegistryObjectMapping> findRegistryObjectMappingsByProprietary(
    String proprietaryObjectType,
    String proprietaryObjectKey)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the RegistryObjectMapping that maps the specified proprietary object to published or queried
   * registry object at a target registry.
   * 
   * @param proprietaryObjectType The type of the proprietary object.
   * @param proprietaryObjectKey The key of the proprietary object.
   * @param queryUrl The Query URL of the target registry.
   * @return The RegistryObjectMapping found, or <b>null</b> if none found.
   * @throws FindEntityException Application error while performing the find.
   * @throws SystemException Unexpected system error encountered e.g. fail to retrieve from database.
   */
  public RegistryObjectMapping findRegistryObjectMappingByProprietary(
    String proprietaryObjectType,
    String proprietaryObjectKey,
    String queryUrl)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the RegistryObjectMapping for based on the specified registry information.
   * 
   * @param registryObjectType The type of the registry object.
   * @param registryObjectKey The key of the registry object.
   * @param queryUrl The Query URL of the target registry.
   * @return The RegistryObjectMapping found, or <b>null</b> if none found.
   * @throws FindEntityException Application error while performing the find.
   * @throws SystemException Unexpected system error encountered e.g. fail to retrieve from database.
   */
  public RegistryObjectMapping findRegistryObjectMapping(
    String registryObjectType,
    String registryObjectKey,
    String queryUrl)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Pend a Registry object for deletion from the target registry.
   * 
   * @param regInfo The registry information object to be pend deleted. ProprietaryObjectType,
   * ProprietaryObjectKey, and RegistryObjectKey are mandatory information to be present. 
   * Any associated registry objects are also removed from the registry.
   * @throws UpdateEntityException Error in updating the state of the registry object.
   * @throws SystemException Unexpected system error.
   */
  public void pendDeleteRegistryObject(IRegistryInfo regInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Pend a Registry object for update to the target registry.
   * 
   * @param regInfo The registry information object to be pend updated. ProprietaryObjectType,
   * ProprietaryObjectKey, and RegistryObjectKey are mandatory information to be present. 
   * Any associated registry objects are NOT pend updated to the registry.
   * @throws UpdateEntityException Error in updating the state of the registry object.
   * @throws SystemException Unexpected system error.
   */
  public void pendUpdateRegistryObject(IRegistryInfo regInfo)
    throws UpdateEntityException, SystemException, RemoteException;

}
