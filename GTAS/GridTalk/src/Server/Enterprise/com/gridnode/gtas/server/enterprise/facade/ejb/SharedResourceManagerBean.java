/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SharedResourceManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 * Jan 06 2004    Neo Sok Lay         Remove passing of RouteChannelUID when synchronizing.
 *                                    Add checkSum for completeSynchronization().
 * Jan 20 2004    Neo Sok Lay         Modify shareResourceIfNotShared() to cater for
 *                                    deleted sharedResource --> to undelete it.
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizeResourceException;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.SharedResourceEntityHandler;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.sync.SyncResourceController;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This bean provides services to manage the Enterprise Hierarchy.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SharedResourceManagerBean
  implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6165691451877728322L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ******************** SHARED RESOURCE (begin) ********************** //

  /**
   * @see ISharedResourceManagerObj#addSharedResource
   */
  public Long addSharedResource(String resourceType, Long resourceUID, String toEnterpriseID)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "addSharedResource";
    Object[] params     = new Object[] {resourceType,resourceUID,toEnterpriseID};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      SharedResource existing = getSharedResource(
                                  resourceType, resourceUID, toEnterpriseID);
      if (existing != null) //exist
      {
        //already marked deleted --> update back to unsync
        if (existing.getState() == SharedResource.STATE_DELETED)
        {
          existing.setState(SharedResource.STATE_UNSYNC);
          getEntityHandler().update(existing);
          key = (Long)existing.getKey();
        }
        else
          throw new DuplicateEntityException("SharedResource ["+
            existing.getEntityDescr() + "] already exists, no duplicate allowed!");
      }
      else
      {
        SharedResource sharedRes = new SharedResource();
        sharedRes.setResourceType(resourceType);
        sharedRes.setResourceUID(resourceUID);
        sharedRes.setToEnterpriseID(toEnterpriseID);
        key = getEntityHandler().addSharedResource(sharedRes);
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
    return key;
  }

  /**
   * @see ISharedResourceManagerObj#addSharedResources
   */
  public Collection addSharedResources(
    String resourceType, Collection resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "addSharedResources";
    Object[] params     = new Object[] {resourceType,resourceUIDs,toEnterpriseID};

    Collection keys          = new ArrayList();
    try
    {
      logger.logEntry(methodName, params);

      for (Iterator i=resourceUIDs.iterator(); i.hasNext(); )
      {
        keys.add(addSharedResource(
          resourceType,
          (Long)i.next(),
          toEnterpriseID));
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
    return keys;
  }

  /**
   * @see ISharedResourceManagerObj#shareResourceIfNotShared(String,Collection,String)
   */
  public Collection shareResourceIfNotShared(
    String resourceType, Collection resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "shareResourceIfNotShared";
    Object[] params     = new Object[] {resourceType,resourceUIDs,toEnterpriseID};

    Collection keys          = new ArrayList();
    try
    {
      logger.logEntry(methodName, params);

      for (Iterator i=resourceUIDs.iterator(); i.hasNext(); )
      {
        Long resUID = (Long)i.next();
        SharedResource sharedRes = getSharedResource(resourceType, resUID,
                                     toEnterpriseID);

        if (sharedRes == null || sharedRes.getState() == SharedResource.STATE_DELETED)
        {
          keys.add(addSharedResource(
                     resourceType,
                     resUID,
                     toEnterpriseID));
        }
        else
        {
          keys.add(sharedRes.getKey());
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
    return keys;
  }

  /**
   * @see ISharedResourceManagerObj#shareResourceIfNotShared(String,Long[],String)
   */
  public Collection shareResourceIfNotShared(
    String resourceType, Long[] resourceUIDs, String toEnterpriseID)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "shareResourceIfNotShared";
    Object[] params     = new Object[] {resourceType,resourceUIDs,toEnterpriseID};

    Collection keys          = new ArrayList();
    try
    {
      logger.logEntry(methodName, params);

      for (int i=0; i<resourceUIDs.length; i++ )
      {
        Long resUID = resourceUIDs[i];
        SharedResource sharedRes = getSharedResource(resourceType, resUID,
                                     toEnterpriseID);

        if (sharedRes == null)
        {
          keys.add(addSharedResource(
                     resourceType,
                     resUID,
                     toEnterpriseID));
        }
        else
        {
          keys.add(sharedRes.getKey());
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
    return keys;
  }

  /**
   * @see ISharedResourceManagerObj#removeSharedResource
   */
  public void removeSharedResource(Long sharedResourceUID)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "removeSharedResource";
    Object[] params     = new Object[] {sharedResourceUID};

    try
    {
      logger.logEntry(methodName, params);

      SharedResource affected = (SharedResource)getEntityHandler().getEntityByKeyForReadOnly(sharedResourceUID);
      getEntityHandler().markDeleteSharedResource(affected);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#removeSharedResourceByFilter
   */
  public void removeSharedResourcesByFilter(IDataFilter filter)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "removeSharedResourcesByFilter";
    Object[] params     = new Object[] {filter};

    try
    {
      logger.logEntry(methodName, params);

      Collection affected = getEntityHandler().getEntityByFilterForReadOnly(
                              filter);
      getEntityHandler().markDeleteSharedResources(affected);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#removeSharedResources(String,Long)
   */
  public void removeSharedResources(String resourceType, Long resourceUID)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "removeSharedResources";
    Object[] params     = new Object[] {resourceType, resourceUID};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_UID,
        filter.getEqualOperator(), resourceUID, false);

      Collection affected = getEntityHandler().getEntityByFilterForReadOnly(
                              filter);
      getEntityHandler().markDeleteSharedResources(affected);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#removeSharedResource(String,Long,String)
   */
  public void removeSharedResource(
    String resourceType, Long resourceUID, String toEnterpriseID)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "removeSharedResources";
    Object[] params     = new Object[] {resourceType, resourceUID, toEnterpriseID};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_UID,
        filter.getEqualOperator(), resourceUID, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.TO_ENTERPRISE_ID,
        filter.getEqualOperator(), toEnterpriseID, false);

      Collection affected = getEntityHandler().getEntityByFilterForReadOnly(
                              filter);
      getEntityHandler().markDeleteSharedResources(affected);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#purgeDeletedSharedResources(String)
   */
  public void purgeDeletedSharedResources(String resourceType)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "purgeDeletedSharedResources";
    Object[] params     = new Object[] {};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.STATE,
        filter.getEqualOperator(), new Short(SharedResource.STATE_DELETED), false);

      getEntityHandler().removeByFilter(filter);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#setSharedResourceUnsync
   */
  public void setSharedResourceUnsync(Long sharedResUID, long checkSum)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "setSharedResourceUnsync";
    Object[] params     = new Object[] {sharedResUID, new Long(checkSum)};

    try
    {
      logger.logEntry(methodName, params);

      SharedResource sharedRes = getEntityHandler().getSharedResource(sharedResUID);
      //if (sharedRes.getState() != SharedResource.STATE_UNSYNC)
      //{
        sharedRes.setState(SharedResource.STATE_UNSYNC);
        sharedRes.setSyncChecksum(checkSum);
        getEntityHandler().update(sharedRes);
      //}
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
   * @see ISharedResourceManagerObj#setSharedResourcesUnsync
   */
  public void setSharedResourcesUnsync(Collection sharedResUIDs)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "setSharedResourceUnsync";
    Object[] params     = new Object[] {};

    try
    {
      logger.logEntry(methodName, params);
      long checkSum = System.currentTimeMillis();
      for (Iterator i=sharedResUIDs.iterator(); i.hasNext(); )
      {
        setSharedResourceUnsync((Long)i.next(), checkSum);
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

  // ************************ Finders ***************************************

  /**
   * @see ISharedResourceManagerObj#getSharedResources(String,String)
   */
  public Collection getSharedResources(String resourceType, String toEnterpriseID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getSharedResources";
    Object[] params     = new Object[] {resourceType, toEnterpriseID};

    Collection sharedResources = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.TO_ENTERPRISE_ID,
        filter.getEqualOperator(), toEnterpriseID, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.STATE,
        filter.getNotEqualOperator(), new Short(SharedResource.STATE_DELETED), false);

      sharedResources = getEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedResources;
  }

  /**
   * @see ISharedResourceManagerObj#getSharedResources(String,Long)
   */
  public Collection getSharedResources(String resourceType, Long resourceUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getSharedResources";
    Object[] params     = new Object[] {resourceType, resourceUID};

    Collection sharedResources = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_UID,
        filter.getEqualOperator(), resourceUID, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.STATE,
        filter.getNotEqualOperator(), new Short(SharedResource.STATE_DELETED), false);

      sharedResources = getEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedResources;
  }

  /**
   * @see ISharedResourceManagerObj#getSharedResourceByUID
   */
  public SharedResource getSharedResourceByUID(Long sharedResourceUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getSharedResourceByUID";
    Object[] params     = new Object[] {sharedResourceUID};

    SharedResource sharedRes = null;

    try
    {
      logger.logEntry(methodName, params);

      sharedRes = (SharedResource)getEntityHandler().getEntityByKeyForReadOnly(
                    sharedResourceUID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedRes;
  }

  /**
   * @see ISharedResourceManagerObj#getSharedResource
   */
  public SharedResource getSharedResource(
    String resourceType, Long resourceUID, String toEnterpriseID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getSharedResource";
    Object[] params     = new Object[] {resourceType, resourceUID, toEnterpriseID};

    SharedResource sharedRes = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_UID,
        filter.getEqualOperator(), resourceUID, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.TO_ENTERPRISE_ID,
        filter.getEqualOperator(), toEnterpriseID, false);

      Collection result = getEntityHandler().getEntityByFilterForReadOnly(
                              filter);

      //get the first one (should return only one, if any)
      if (result != null && !result.isEmpty())
        sharedRes = (SharedResource)result.iterator().next();
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedRes;
  }

  /**
   * @see ISharedResourceManagerObj#getUnsyncSharedResourceUIDs
   */
  public Collection getUnsyncSharedResourceUIDs(String resourceType, String toEnterpriseID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getUnsyncSharedResources";
    Object[] params     = new Object[] {resourceType, toEnterpriseID};

    Collection sharedResources = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), resourceType, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.TO_ENTERPRISE_ID,
        filter.getEqualOperator(), toEnterpriseID, false);
      filter.addSingleFilter(filter.getAndConnector(), SharedResource.STATE,
        filter.getEqualOperator(), new Short(SharedResource.STATE_UNSYNC), false);

      sharedResources = getEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedResources;
  }

  /**
   * @see ISharedResourceManagerObj#getSharedResourcesByFilter
   */
  public Collection getSharedResourcesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "getSharedResourcesByFilter";
    Object[] params     = new Object[] {filter};

    Collection sharedResources = null;

    try
    {
      logger.logEntry(methodName, params);

      sharedResources = getEntityHandler().getEntityByFilterForReadOnly(
                          filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return sharedResources;
  }

  // ********************** SHARED RESOURCE(end) ************************

  // ************************ SYNCHRONIZATION (begin) *********************** //

  /**
   * @see ISharedResourceManagerObj#synchronizeSharedResource
   */
  public void synchronizeSharedResource(
    Long sharedResourceUID, Long destChannelUID)
    throws SynchronizeResourceException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "synchronizeSharedResource";
    Object[] params     = new Object[] {
                            sharedResourceUID,
                            destChannelUID,
                          };


    try
    {
      logger.logEntry(methodName, params);

      SharedResource sharedRes = getSharedResourceByUID(sharedResourceUID);

      if (sharedRes.getState() == SharedResource.STATE_UNSYNC)
      {
        /*060104NSL: Don't set checksum here, this should be set when the resource was changed
        sharedRes.setSyncChecksum(System.currentTimeMillis());
        getEntityHandler().update(sharedRes);
        */
        SyncResourceController.getInstance().sendResource(
          sharedRes, destChannelUID);
      }
      else
        logger.logMessage(methodName, params,
          "SharedResource is not in UnSync state.");
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SynchronizeResourceException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @see ISharedResourceManagerObj#synchronizeSharedResources
   */
  public void synchronizeSharedResources(
    Collection sharedResourceUIDs, Long destChannelUID)
    throws SynchronizeResourceException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "synchronizeSharedResource";
    Object[] params     = new Object[] {
                            sharedResourceUIDs,
                            destChannelUID,
                            };


    try
    {
      logger.logEntry(methodName, params);

      for (Iterator i=sharedResourceUIDs.iterator(); i.hasNext(); )
      {
        Long uID = (Long)i.next();
        synchronizeSharedResource(uID, destChannelUID);
      }
    }
    catch (SynchronizeResourceException ex)
    {
      logger.logWarn(methodName, params, ex);
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SynchronizeResourceException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see ISharedResourceManagerObj#completeSynchronization
   */
  public void completeSynchronization(Long sharedResourceUID, long checkSum)
    throws SynchronizeResourceException, SystemException
  {
    FacadeLogger logger = Logger.getSharedResourceFacadeLogger();
    String methodName   = "completeSynchronization";
    Object[] params     = new Object[] {
                            sharedResourceUID,
                            new Long(checkSum),
                          };


    try
    {
      logger.logEntry(methodName, params);

      SharedResource sharedRes = getSharedResourceByUID(sharedResourceUID);

      if (checkSum == sharedRes.getSyncChecksum())
      {
        sharedRes.setState(SharedResource.STATE_SYNC);
        getEntityHandler().update(sharedRes);
      }
      else
        logger.logMessage(methodName, params,
          "Synchronization checkSum does not tally. Possibly there are newer changes not synchronized.");
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SynchronizeResourceException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  // ************************ SYNCHRONIZATION (end) *********************** //


  private SharedResourceEntityHandler getEntityHandler()
  {
     return SharedResourceEntityHandler.getInstance();
  }

}