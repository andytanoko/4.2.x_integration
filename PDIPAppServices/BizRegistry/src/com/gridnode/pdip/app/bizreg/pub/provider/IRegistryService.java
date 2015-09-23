/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add: querySchemes()
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.pub.provider;

import com.gridnode.pdip.app.bizreg.exceptions.RegistryProviderException;
import com.gridnode.pdip.app.bizreg.pub.model.*;

import java.util.Collection;

/**
 * This interface defines the services offered by a typical public registry.
 * The information model used by the registry are defined in the
 * {@link com.gridnode.pdip.app.bizreg.pub.model} package.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryService
{
  /**
   * Publishes an Organization information object. This may update the information 
   * to the public registry if registry key is specified in the object.  
   * 
   * @param orgInfo The Organization information object to publish. 
   * @return The successfully published Organization information object which
   * contains the registry key.
   * @throws RegistryProviderException Error when publishing using
   * the registry provider api.
   */
  OrganizationInfo publish(OrganizationInfo orgInfo)
    throws RegistryProviderException;

  /**
   * Publishes a Service information object. This may update the information to 
   * the public registry if registry key is specified in the object.
   * 
   * @param serviceInfo The Service information object to publish.
   * @return The successfully published Service information object which contains
   * the registry key.
   * @throws RegistryProviderException Error when publishing using
   * the registry provider api.
   */
  ServiceInfo publish(ServiceInfo serviceInfo)
    throws RegistryProviderException;
    
    //  BindingInfo publish(BindingInfo bindingInfo)
    //    throws RegistryProviderException;
    
  /**
   * Publishes a Scheme information object. This may update the information 
   * to the public registry if registry key is specified in the object.
   * 
   * @param schemeInfo The Scheme information object to publish.
   * @return The successfully published Scheme information object which contains
   * the registry key.
   * @throws RegistryProviderException Error when publishing using
   * the registry provider api.
   */    
  SchemeInfo publish(SchemeInfo schemeInfo)
    throws RegistryProviderException;

  /**
   * Publishes a Concept information object. This may update the information 
   * to the public registry if registry key is specified in the object.
   * 
   * @param conceptInfo The Concept information object to publish.
   * @return The successfully published Concept information object which contains
   * the registry key.
   * @throws RegistryProviderException Error when publishing using
   * the registry provider api.
   */    
  ConceptInfo publish(ConceptInfo conceptInfo)
    throws RegistryProviderException;
  
  /**
   * Query for an Organization information object using the specified registry
   * key.
   * 
   * @param key The registry key of the Organization information object.
   * @return The successfully queried Organization information object.
   * @throws RegistryProviderException Error while querying from the public
   * registry using the registry provider api.
   */
  OrganizationInfo queryOrganization(String key)
    throws RegistryProviderException;

  /**
   * Query the registry for Organization information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
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
   * @throws RegistryProviderException Error during query from Registry provider.
   */
  Collection<OrganizationInfo> queryOrganizations(
    Collection<String> namePatterns, boolean matchAllKeys, Collection<String[]> categories, 
    Collection<ConceptInfo> specifications, Collection<String[]> identifiers, Collection<String[]> links)
    throws RegistryProviderException;
    
  /**
   * Query for an Service information object using the specified registry
   * key.
   * 
   * @param key The registry key of the Service information object.
   * @return The successfully queried Service information object.
   * @throws RegistryProviderException Error while querying from the public
   * registry using the registry provider api.
   */
  ServiceInfo queryService(String key)
    throws RegistryProviderException;
    
  /**
   * Query for an Binding information object using the specified registry
   * key.
   * 
   * @param key The registry key of the Binding information object.
   * @return The successfully queried Binding information object.
   * @throws RegistryProviderException Error while querying from the public
   * registry using the registry provider api.
   */
  BindingInfo queryServiceBinding(String key)
    throws RegistryProviderException;
    
  /**
   * Query for an Scheme information object using the specified registry
   * key.
   * 
   * @param key The registry key of the Scheme information object.
   * @return The successfully queried Scheme information object.
   * @throws RegistryProviderException Error while querying from the public
   * registry using the registry provider api.
   */
  SchemeInfo queryScheme(String key)
    throws RegistryProviderException;

  /**
   * Query the registry for Scheme information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
   * @param namePatterns Collection of patterns of the Scheme names. Assume OR match. <b>null</b> if not required.
   * @param matchAllKeys <b>true</b> to match ALL keys specified in each of the rest of Collection parameters. <b>false</b>
   * to match ANY key specified in each of the rest of Collection parameters.
   * @param categories Collection of Category information objects contained in the Scheme's categoryBag. Assume AND match.
   * <b>null</b> if not required.
   * @param links Collection of Link information objects in the Scheme information object. Assume AND match.
   * <b>null</b> if not required.
   * @return Collection of SchemeInfo objects that are queried from the registry.
   * @throws RegistryProviderException Error during query from registry provider.
   */
  Collection<SchemeInfo> querySchemes(
    Collection<String> namePatterns, boolean matchAllKeys, 
    Collection<String[]> categories, Collection<String[]> links)
    throws RegistryProviderException;
    
  /**
   * Query for an Concept information object using the specified registry
   * key.
   * 
   * @param key The registry key of the Concept information object.
   * @return The successfully queried Concept information object.
   * @throws RegistryProviderException Error while querying from the public
   * registry using the registry provider api.
   */
  ConceptInfo queryConcept(String key)
    throws RegistryProviderException;

  /**
   * Query the registry for Concept information objects that match all of the specified criteria.
   * At least one of the criteria must be specified.
   * 
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
   * @throws RegistryProviderException Error during query from registry provider.
   */
  Collection<ConceptInfo> queryConcepts(
    Collection<String> namePatterns, boolean matchAllKeys, 
    Collection<String[]> categories, Collection<String[]> identifiers, Collection<String[]> links)
    throws RegistryProviderException;
  
}
