/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublicRegistryManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 16 2003    Neo Sok Lay         Use RegistryServiceProvider instead of
 *                                    direct use of JaxrConnectionManager.
 * Sep 18 2003    Neo Sok Lay         Add method: querySchemeInfos()
 * Oct 01 2003    Neo Sok Lay         Add: 
 *                                    - findRegistryObjectMappings(
 *                                        proprietaryObjectType,
 *                                        proprietaryObjectKey)
 *                                    - findRegistryObjectMapping(
 *                                        proprietaryObjectType,
 *                                        proprietaryObjectKey,
 *                                        registryQueryUrl)
 * Oct 06 2003    Neo Sok Lay         Handle embedded publishing of ServiceInfo
 *                                    when publishing OrganizationInfo.
 * Mar 01 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import com.gridnode.pdip.app.bizreg.exceptions.RegistryProviderException;
import com.gridnode.pdip.app.bizreg.exceptions.RegistryPublishException;
import com.gridnode.pdip.app.bizreg.exceptions.RegistryQueryException;
import com.gridnode.pdip.app.bizreg.helpers.Logger;
import com.gridnode.pdip.app.bizreg.helpers.RegistryObjectMappingHelper;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService;
import com.gridnode.pdip.app.bizreg.pub.provider.RegistryServiceProvider;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a EJB implementation of the PublicRegistryManager.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class PublicRegistryManagerBean
  implements IPublicRegistryManager, SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8618617054310056396L;
	transient private SessionContext _sessionCtx = null;

  /**
   * Constructor for PublicRegistryManagerBean.
   */
  public PublicRegistryManagerBean()
  {
    super();
  }

  public void ejbCreate() throws CreateException
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate()
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate()
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove()
  {
  }

  /**
   * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
   */
  public void setSessionContext(SessionContext sessionContext)
  {
    _sessionCtx = sessionContext;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.ejb.IPublicRegistryManager#publish(IRegistryInfo, String[], String, String)
   */
  public IRegistryInfo publish(IRegistryInfo regInfo, String[] connectionInfo)
    throws RegistryPublishException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "publish";
    Object[] params = new Object[] { regInfo, connectionInfo };

    try
    {
      logger.logEntry(methodName, params);

      // assert that the object is publishable
      assertPublishable(regInfo);

      //set the registry keys for those known
      findAndSetRegistryKeys(regInfo, connectionInfo);
      /*031006NSL
      RegistryObjectMapping mapping =
        RegistryObjectMappingHelper.getInstance().findPublishedObject(
          regInfo.getProprietaryObjectType(),
          regInfo.getProprietaryObjectKey(),
          connectionInfo[IConnectionInfo.FIELD_QUERY_URL]);
      if (mapping != null)
        regInfo.setKey(mapping.getRegistryObjectKey());
      switch (regInfo.getObjectType())
      {
        case IRegistryInfo.TYPE_ORGANIZATION:
          
          break;
          
        case IRegistryInfo.TYPE_SERVICE :
          ServiceInfo serviceInfo = (ServiceInfo) regInfo;
          IRegistryInfo bindingInfo;
          for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
          {
            bindingInfo = (IRegistryInfo) i.next();
            mapping = RegistryObjectMappingHelper.getInstance().findPublishedObject(
                        bindingInfo.getProprietaryObjectType(),
                        bindingInfo.getProprietaryObjectKey(),
                        connectionInfo[IConnectionInfo.FIELD_QUERY_URL]);
            if (mapping != null)
              bindingInfo.setKey(mapping.getRegistryObjectKey());
          }
          break;
      }       
      */  
      // do the publishing
      regInfo = doPublish(regInfo, connectionInfo);

      // add registry object mapping for each published object 
      // with proprietaryObjectType &  Key
      recordRegistryObjectMappings(regInfo, connectionInfo, true);
      
      /*031006NSL
      addRegistryObjectMapping(regInfo, connectionInfo, true);

      switch (regInfo.getObjectType())
      {
        case IRegistryInfo.TYPE_SERVICE :
          ServiceInfo serviceInfo = (ServiceInfo) regInfo;
          IRegistryInfo bindingInfo;
          for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
          {
            bindingInfo = (IRegistryInfo) i.next();
            addRegistryObjectMapping(bindingInfo, connectionInfo, true);
          }
          break;
      }
      */
    }
    catch (RegistryPublishException ex)
    {
      throw ex;
    }
    catch (RegistryProviderException ex)
    {
      logger.logWarn(methodName, params, ex);

      throw RegistryPublishException.registryProviderError(ex);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw RegistryPublishException.unknownRegistryError(t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return regInfo;
  }

  /**
   * Find existing recorded registry object mappings for the specified registry info object and 
   * populate the recorded registry object key. This method performs recursively to set the
   * registry keys of any embeded registry info objects, e.g. ServiceInfo embedded in OrganizationInfo,
   * BindingInfo embedded in ServiceInfo.
   * 
   * @param regInfo The Registry info object.
   * @param connectionInfo The connection info object for the target registry.
   * @throws Throwable Error in performing the operation.
   */
  private void findAndSetRegistryKeys(IRegistryInfo regInfo, String[] connectionInfo)
    throws Throwable
  {
    //Only allow change to previously published object, if any
    RegistryObjectMapping mapping =
      RegistryObjectMappingHelper.getInstance().getByProprietaryIfExists(
        regInfo.getProprietaryObjectType(),
        regInfo.getProprietaryObjectKey(),
        connectionInfo[IConnectionInfo.FIELD_QUERY_URL]);
    if (mapping != null)
      regInfo.setKey(mapping.getRegistryObjectKey());
    
    // try to set the keys for embedded registry objects  
    switch (regInfo.getObjectType())
    {
      case IRegistryInfo.TYPE_ORGANIZATION:
        {
          OrganizationInfo orgInfo = (OrganizationInfo)regInfo;
          ServiceInfo serviceInfo;
          for (Iterator i = orgInfo.getServices().iterator(); i.hasNext(); )
          {
            serviceInfo = (ServiceInfo)i.next();
            serviceInfo.setOrganizationKey(orgInfo.getKey());
            serviceInfo.setProprietaryObjectKey(orgInfo.getKey());
            findAndSetRegistryKeys(serviceInfo, connectionInfo);          
          }
        }
        break;
          
      case IRegistryInfo.TYPE_SERVICE :
        {
          ServiceInfo serviceInfo = (ServiceInfo) regInfo;
          IRegistryInfo bindingInfo;
          for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
          {
            bindingInfo = (IRegistryInfo) i.next();
            findAndSetRegistryKeys(bindingInfo, connectionInfo);
          }
        }
        break;
    }       
  }
  
  /**
   * Update the RegistryObjectMapping records for the specified registry info objects (including embedded objects).
   * 
   * @param regInfo The registry info object
   * @param connectionInfo The connection info to the target registry
   * @param published Whether the objects are published or not.
   * @throws Exception Error during the recording process.
   */
  private void recordRegistryObjectMappings(IRegistryInfo regInfo, String[] connectionInfo, boolean published)
    throws Exception
  {
    addRegistryObjectMapping(regInfo, connectionInfo, published);

    // record mappings for embedded registry info objects
    switch (regInfo.getObjectType())
    {
      case IRegistryInfo.TYPE_ORGANIZATION :
        {
          OrganizationInfo orgInfo = (OrganizationInfo)regInfo;
          ServiceInfo serviceInfo;
          for (Iterator i = orgInfo.getServices().iterator(); i.hasNext(); )
          {
            serviceInfo = (ServiceInfo)i.next();
            recordRegistryObjectMappings(serviceInfo, connectionInfo, published);
          }
        }
        break;
        
      case IRegistryInfo.TYPE_SERVICE :
        {
          ServiceInfo serviceInfo = (ServiceInfo) regInfo;
          IRegistryInfo bindingInfo;
          for (Iterator i = serviceInfo.getBindings().iterator(); i.hasNext();)
          {
            bindingInfo = (IRegistryInfo) i.next();
            recordRegistryObjectMappings(bindingInfo, connectionInfo, published);
          }
        }
        break;
    }
  }
  
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
   * @throws SystemException Unexception system error.
   */
  public void addRegistryObjectMapping(
    IRegistryInfo regInfo,
    String[] connInfo,
    boolean isPublished)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "addRegistryObjectMapping";
    Object[] params =
      new Object[] { regInfo, connInfo, new Boolean(isPublished), };

    try
    {
      logger.logEntry(methodName, params);

      if (regInfo.getProprietaryObjectKey() == null
        || regInfo.getProprietaryObjectType() == null)
      {
        logger.logMessage(
          methodName,
          params,
          "No Proprietary Object Type or Key specified. No Registry Object Mapping added.");
      }
      else
      {
        RegistryObjectMapping mapping =
          RegistryObjectMappingHelper.getInstance().getIfExists(
            regInfo.getObjectTypeDescr(),
            regInfo.getKey(),
            connInfo[IConnectionInfo.FIELD_QUERY_URL]);

        if (mapping == null)
        {
          // try get by proprietary info
          mapping =
            RegistryObjectMappingHelper.getInstance().getByProprietaryIfExists(
              regInfo.getProprietaryObjectType(),
              regInfo.getProprietaryObjectKey(),
              connInfo[IConnectionInfo.FIELD_QUERY_URL]);
        }
        
        if (mapping == null)
        {
          // create
          RegistryObjectMappingHelper.getInstance().create(
            regInfo.getObjectTypeDescr(),
            regInfo.getKey(),
            connInfo[IConnectionInfo.FIELD_QUERY_URL],
            connInfo[IConnectionInfo.FIELD_PUBLISH_URL],
            regInfo.getProprietaryObjectType(),
            regInfo.getProprietaryObjectKey(),
            isPublished);
        }
        else
        {
          // update
          mapping.setRegistryObjectType(regInfo.getObjectTypeDescr());
          mapping.setRegistryObjectKey(regInfo.getKey());
          mapping.setProprietaryObjectKey(regInfo.getProprietaryObjectKey());
          mapping.setProprietaryObjectType(regInfo.getProprietaryObjectType());
          mapping.setRegistryPublishUrl(connInfo[IConnectionInfo.FIELD_PUBLISH_URL]);
          mapping.setPublishedObject(isPublished);
          mapping.setState(RegistryObjectMapping.STATE_SYNCHRONIZED);
          RegistryObjectMappingHelper.getInstance().update(mapping);
        }
      }
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Assert that the specified registry information object is valid
   * for publishing to the public registry.
   * 
   * @param regInfo The registry information object to publish.
   * @throws RegistryPublishException If <code>regInfo</code> if
   * not valid for publishing due to e.g. Unsupported publish method,
   * null information object specified, etc. 
   */
  private void assertPublishable(IRegistryInfo regInfo)
    throws RegistryPublishException
  {
    if (regInfo == null)
    {
      throw RegistryPublishException.nullRegistryInfo();
    }

    if (!regInfo.isPublishSupported())
    {
      throw RegistryPublishException.unsupportedRegistryType(
        regInfo.getObjectType());
    }
  }

  /**
   * Performs the publishing of the specified registry information object
   * to a public registry.
   * 
   * @param regInfo The registry information object to publish.
   * @param connInfo The connection information to use for publishing.
   * @return The successfully published registry information object.
   * @throws Exception Error during publishing.
   */
  private IRegistryInfo doPublish(IRegistryInfo regInfo, String[] connInfo)
    throws Exception
  {
    IRegistryService regService = getRegistryService(connInfo);

    switch (regInfo.getObjectType())
    {
      case IRegistryInfo.TYPE_ORGANIZATION :
        regInfo = regService.publish((OrganizationInfo) regInfo);
        break;
      case IRegistryInfo.TYPE_SERVICE :
        regInfo = regService.publish((ServiceInfo) regInfo);
        break;
      case IRegistryInfo.TYPE_SCHEME :
        regInfo = regService.publish((SchemeInfo) regInfo);
        break;
      case IRegistryInfo.TYPE_CONCEPT :
        regInfo = regService.publish((ConceptInfo) regInfo);
        break;
    }

    return regInfo;
  }
  
  /**
   * Performs the inquiry of a registry information object with the specified key
   * from a public registry.
   * 
   * @param objectType The type of registry information object to query.
   * @param key The key of the registry information object.
   * @param queryUrl The url to query for the registry information object.
   * @return The successfully queried registry information object.
   * @throws Exception Error while performing the query.
   */
  private IRegistryInfo doQuery(int objectType, String key, String queryUrl)
    throws Exception
  {
    IRegistryService regService =
      getRegistryService(createConnectionInfo(queryUrl));

    IRegistryInfo regInfo = null;

    switch (objectType)
    {
      case IRegistryInfo.TYPE_ORGANIZATION :
        regInfo = regService.queryOrganization(key);
        break;
      case IRegistryInfo.TYPE_SERVICE :
        regInfo = regService.queryService(key);
        break;
      case IRegistryInfo.TYPE_BINDING :
        regInfo = regService.queryServiceBinding(key);
        break;
      case IRegistryInfo.TYPE_SCHEME :
        regInfo = regService.queryScheme(key);
        break;
      case IRegistryInfo.TYPE_CONCEPT :
        regInfo = regService.queryConcept(key);
        break;
    }

    return regInfo;
  }

  /**
   * Create a connection information object based on the specified query url.
   * 
   * @param queryUrl The query url.
   * @return A connection information object.
   */
  private String[] createConnectionInfo(String queryUrl)
  {
    String[] connInfo = new String[IConnectionInfo.NUM_FIELDS];
    connInfo[IConnectionInfo.FIELD_QUERY_URL] = queryUrl;
    //connInfo[IConnectionInfo.FIELD_PUBLISH_URL] = queryUrl;

    return connInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.ejb.IPublicRegistryManager#findQueriableObjectKey(int, java.lang.String, java.lang.String, java.lang.String)
   */
  public IRegistryInfo findQueriableObject(
    int registryObjectType,
    String proprietaryObjectType,
    String proprietaryObjectKey,
    String queryUrl)
    throws RegistryQueryException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "findQueriableObject";
    Object[] params =
      new Object[] {
        new Integer(registryObjectType),
        proprietaryObjectType,
        proprietaryObjectKey,
        queryUrl };

    IRegistryInfo registryInfo = null;

    try
    {
      logger.logEntry(methodName, params);

      RegistryObjectMapping mapping =
        RegistryObjectMappingHelper.getInstance().findPublishedObject(
          proprietaryObjectType,
          proprietaryObjectKey,
          queryUrl);

      if (mapping != null)
      {
        String key = mapping.getRegistryObjectKey();

        // check whether queriable
        registryInfo = doQuery(registryObjectType, key, queryUrl);
      }
    }
    catch (RegistryProviderException e)
    {
      throw RegistryQueryException.registryProviderError(e);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw RegistryQueryException.unknownRegistryError(t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return registryInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.ejb.IPublicRegistryManager#connectToRegistry(java.lang.String[])
   */
  public void connectToRegistry(String[] connectionInfo)
    throws RegistryProviderException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "connectToRegistry";
    Object[] params = new Object[] { connectionInfo };

    try
    {
      logger.logEntry(methodName, params);

      getRegistryService(connectionInfo);
    }
    catch (RegistryProviderException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      logger.logWarn(methodName, params, e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#disconnectFromRegistry(java.lang.String[])
   */
  public void disconnectFromRegistry(String[] connectionInfo)
    throws RegistryProviderException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "disconnectFromRegistry";
    Object[] params = new Object[] { connectionInfo };

    try
    {
      logger.logEntry(methodName, params);

      removeRegistryService(connectionInfo);
    }
    catch (Exception e)
    {
      logger.logWarn(methodName, params, e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Removes a RegistryService connection.
   * 
   * @param connectionInfo The Connection information that made the connection
   *  to the registry earlier.
   * @return A RegistryService implementation
   * @throws Exception Error in establishing the connection witht the registry.
   * @see IConnectionInfo
   */
  private void removeRegistryService(String[] connectionInfo)
  {
    try
    {
      RegistryServiceProvider.getRegistryServiceManager().removeRegistryService(
         connectionInfo);
    }
    catch (Exception e)
    {
      //ignore the error
    }
  }

  /**
   * Get the RegistryService connection.
   * 
   * @param connectionInfo The connection information for connecting to the registry.
   * @return A RegistryService implementation
   * @throws Exception Error in establishing the connection witht the registry.
   */
  private IRegistryService getRegistryService(String[] connectionInfo)
    throws Exception
  {
    return RegistryServiceProvider.getRegistryServiceManager().getRegistryService(
              connectionInfo);
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#publishIfNotQueriable(com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo, java.lang.String[])
   */
//  public IRegistryInfo publishIfNotQueriable(
//    IRegistryInfo regInfo,
//    String[] connInfo)
//    throws RegistryPublishException, RegistryQueryException, SystemException
//  {
//    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
//    String methodName = "publishIfNotQueriable";
//    Object[] params =
//      new Object[] {
//        regInfo,
//        connInfo};
//
//    IRegistryInfo registryInfo = null;
//
//    try
//    {
//      logger.logEntry(methodName, params);
//
//      IRegistryInfo queriedInfo = findQueriableObject(regInfo.getObjectType(), 
//                                    regInfo.getProprietaryObjectType(),
//                                    regInfo.getProprietaryObjectKey(), 
//                                    connInfo[IConnectionInfo.FIELD_QUERY_URL]);
//
//      if (queriedInfo == null)
//      {
//        IRegistryInfo publishedInfo = publish(regInfo, connInfo);
//        regInfo.setKey(publishedInfo.getKey());
//      }
//      else
//      {
//        regInfo.setKey(queriedInfo.getKey());
//      }
//    }
//    catch (RegistryProviderException e)
//    {
//      throw RegistryQueryException.registryProviderError(e);
//    }
//    catch (Throwable t)
//    {
//      logger.logError(methodName, params, t);
//      throw RegistryQueryException.unknownRegistryError(t);
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//    return regInfo;  
//  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#queryConceptInfos(java.lang.String, java.util.Collection, boolean, java.util.Collection, java.util.Collection, java.util.Collection)
   */
  public Collection<ConceptInfo> queryConceptInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "queryConceptInfos";
    Object[] params =
      new Object[] {
        queryUrl,
        namePatterns,
        Boolean.valueOf(matchAllKeys),
        categories,
        identifiers,
        links };

    Collection<ConceptInfo> conceptInfoList = null;

    try
    {
      logger.logEntry(methodName, params);

      IRegistryService regService =
        getRegistryService(createConnectionInfo(queryUrl));

      conceptInfoList = regService.queryConcepts(namePatterns, matchAllKeys, categories, identifiers, links);
    }
    catch (RegistryProviderException e)
    {
      throw RegistryQueryException.registryProviderError(e);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw RegistryQueryException.unknownRegistryError(t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return conceptInfoList;  
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#queryOrganizationInfos(java.lang.String, java.util.Collection, boolean, java.util.Collection, java.util.Collection, java.util.Collection, java.util.Collection)
   */
  public Collection<OrganizationInfo> queryOrganizationInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<ConceptInfo> specifications,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "queryOrganizationInfos";
    Object[] params =
      new Object[] {
        queryUrl,
        namePatterns,
        Boolean.valueOf(matchAllKeys),
        categories,
        specifications,
        identifiers,
        links };

    Collection<OrganizationInfo> orgInfoList = null;

    try
    {
      logger.logEntry(methodName, params);

      IRegistryService regService =
        getRegistryService(createConnectionInfo(queryUrl));

      orgInfoList = regService.queryOrganizations(namePatterns, matchAllKeys, categories, specifications, identifiers, links);
    }
    catch (RegistryProviderException e)
    {
      throw RegistryQueryException.registryProviderError(e);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw RegistryQueryException.unknownRegistryError(t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return orgInfoList;    
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#querySchemeInfos(java.lang.String, java.util.Collection, boolean, java.util.Collection, java.util.Collection)
   */
  public Collection<SchemeInfo> querySchemeInfos(
    String queryUrl,
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> links)
    throws RegistryQueryException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "querySchemeInfos";
    Object[] params =
      new Object[] {
        queryUrl,
        namePatterns,
        Boolean.valueOf(matchAllKeys),
        categories,
        links };

    Collection<SchemeInfo> schemeInfoList = null;

    try
    {
      logger.logEntry(methodName, params);

      IRegistryService regService =
        getRegistryService(createConnectionInfo(queryUrl));

      schemeInfoList = regService.querySchemes(namePatterns, matchAllKeys, categories, links);
    }
    catch (RegistryProviderException e)
    {
      throw RegistryQueryException.registryProviderError(e);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw RegistryQueryException.unknownRegistryError(t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return schemeInfoList;  
  }



  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#pendDeleteRegistryObject(com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo)
   */
  public void pendDeleteRegistryObject(IRegistryInfo regInfo)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "pendDeleteRegistryObject";
    Object[] params =
      new Object[] {
        regInfo};

    try
    {
      logger.logEntry(methodName, params);

      // pend delete the registry object
      RegistryObjectMappingHelper.getInstance().pendDeletePublishedObject(
        regInfo.getProprietaryObjectType(),
        regInfo.getProprietaryObjectKey());
        
      // recurse for embedded objects  
      switch (regInfo.getObjectType())
      {
        case IRegistryInfo.TYPE_ORGANIZATION :
          // services
          OrganizationInfo orgInfo = (OrganizationInfo)regInfo;
          Collection<ServiceInfo> services = orgInfo.getServices();
          if (services != null)
          {
            for (ServiceInfo i : services)
            {
              pendDeleteRegistryObject(i);
            }
          }
          break;
        case IRegistryInfo.TYPE_SERVICE :
          // bindings
          ServiceInfo serviceInfo = (ServiceInfo)regInfo;
          Collection<BindingInfo> bindings = serviceInfo.getBindings();
          if (bindings != null)
          {
            for (BindingInfo i : bindings)
            {
              pendDeleteRegistryObject(i);
            }
          }
          break;
      }
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#pendUpdateRegistryObject(com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo)
   */
  public void pendUpdateRegistryObject(IRegistryInfo regInfo)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "pendUpdateRegistryObject";
    Object[] params =
      new Object[] {
        regInfo};

    try
    {
      logger.logEntry(methodName, params);

      // pend update the registry object
      RegistryObjectMappingHelper.getInstance().pendUpdatePublishedObject(
        regInfo.getProprietaryObjectType(),
        regInfo.getProprietaryObjectKey());
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#findRegistryObjectMappingByProprietary(java.lang.String, java.lang.String, java.lang.String)
   */
  public RegistryObjectMapping findRegistryObjectMappingByProprietary(
    String proprietaryObjectType,
    String proprietaryObjectKey,
    String queryUrl)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "findRegistryObjectMappingByProprietary";
    Object[] params =
      new Object[] {
        proprietaryObjectType,
        proprietaryObjectKey,
        queryUrl};

    RegistryObjectMapping found = null;
    try
    {
      logger.logEntry(methodName, params);

      found = RegistryObjectMappingHelper.getInstance().getByProprietaryIfExists(
                proprietaryObjectType, proprietaryObjectKey, queryUrl);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    
    return found;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#findRegistryObjectMappingsByProprietary(java.lang.String, java.lang.String)
   */
  public Collection<RegistryObjectMapping> findRegistryObjectMappingsByProprietary(
    String proprietaryObjectType,
    String proprietaryObjectKey)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "findRegistryObjectMappingsByProprietary";
    Object[] params =
      new Object[] {
        proprietaryObjectType,
        proprietaryObjectKey};

    Collection<RegistryObjectMapping> found = null;
    try
    {
      logger.logEntry(methodName, params);

      found = RegistryObjectMappingHelper.getInstance().findObjectsByProprietary(
                proprietaryObjectType,
                proprietaryObjectKey,
                RegistryObjectMapping.STATE_SYNCHRONIZED);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    
    return found;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager#findRegistryObjectMapping(java.lang.String, java.lang.String, java.lang.String)
   */
  public RegistryObjectMapping findRegistryObjectMapping(
    String registryObjectType,
    String registryObjectKey,
    String queryUrl)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getPublicRegistryFacadeLogger();
    String methodName = "findRegistryObjectMapping";
    Object[] params =
      new Object[] {
        registryObjectType,
        registryObjectKey,
        queryUrl};

    RegistryObjectMapping found = null;
    try
    {
      logger.logEntry(methodName, params);

      found = RegistryObjectMappingHelper.getInstance().getIfExists(
                registryObjectType, registryObjectKey, queryUrl);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    
    return found;
  }

}
