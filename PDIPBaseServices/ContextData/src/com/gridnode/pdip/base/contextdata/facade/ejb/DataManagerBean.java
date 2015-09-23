/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002   MAHESH              Created
 */
package com.gridnode.pdip.base.contextdata.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.contextdata.entities.model.ContextData;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.exceptions.DataException;
import com.gridnode.pdip.base.contextdata.facade.helpers.ContextDataEntityHandler;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.exceptions.SystemException;

public class DataManagerBean implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8642790589533035161L;

	private SessionContext ctx;

  String CONTEXT_NAME = "ContextKey";

  public void setSessionContext(SessionContext context)
  {
    ctx = context;
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate() throws CreateException, RemoteException
  {
  }

  //*************************************************************

  /**
   * This method creates new context uid
   * @return context uid
   * @throws DataException
   */
  public Long createContextUId() throws DataException
  {
    try
    {
      return createContextUId(null);
    }
    catch (DataException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new DataException("[DataManagerBean.createContextUId] Unable to create contextUid", th);
    }
  }

  /**
   * This method creates new context uid with initial data
   * @return context uid
   * @throws DataException
   */
  public Long createContextUId(HashMap contexDataMap) throws DataException
  {
    try
    {
      ContextData ctxData = new ContextData();
      if (contexDataMap != null)
      {
        for (Iterator iterator = contexDataMap.keySet().iterator(); iterator.hasNext();)
        {
          Object key = iterator.next();
          if (key == null)
            throw new DataException("DataKey cannot be null ");
          if (!(key instanceof ContextKey))
            key = new ContextKey(key.toString());
          ctxData.getContextData().put(key, contexDataMap.get(key));
        }
      }
      ctxData.setContextUId(KeyGen.getNextId(CONTEXT_NAME));
      getEntityHandler().create(ctxData);
      return ctxData.getContextUId();
    }
    catch (Throwable th)
    {
      throw new DataException("Unable to create contextUid", th);
    }
  }

  /**
   * Sets the data to this context with this ContextKey,
   * if data with that ContextKey already exists then it replaces it.
   * @param contextUId This is the context uid
   * @param key This is the ContextKey
   * @param objData This is the data
   * @throws DataException
   * @throws SystemException
   */
  public void setContextData(Long contextUId, ContextKey key, Object objData) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().updateContextData(contextUId, key, objData);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in setting ContextData: "+th.getMessage(), th);
    }
  }

  /**
   * Sets the data to this context where ContextKey and data pair is in HashMap,
   * if data with that ContextKey already exists then it replaces it.
   * @param contextUId This is the context uid
   * @param contexDataMap This HashMap contains the ContextKey and data pairs
   * @throws DataException
   * @throws SystemException
   */
  public void setContextData(Long contextUId, HashMap contexDataMap) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().updateContextData(contextUId, contexDataMap);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error while updating ContextData", th);
    }
  }

  /**
   * This method reterives the data from this context with the specified ContextKey
   * @param contextUId This is the context uid
   * @param key This is the ContextKey
   * @return the object data for this key
   * @throws DataException
   * @throws SystemException
   */
  public Object getContextData(Long contextUId, ContextKey key) throws DataException, SystemException
  {
    try
    {
      if (key == null)
        throw new DataException(
          "DataKey cannot be null,contextUId=" + contextUId);

      ContextData ctxData = getEntityHandler().getEntityByContextUId(contextUId);
      if (ctxData == null)
        throw new DataException(
          "Context does not exist contextUId=" + contextUId);
      return ctxData.getContextData().get(key);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in getting ContextData: "+th.getMessage(), th);
    }
  }

  /**
   * This method reterives the entire context data and returns in the form of HashMap
   * @param contextUId This is the context uid
   * @return ContextKey and data pairs of this context in HashMap
   * @throws DataException
   * @throws SystemException
   */
  public HashMap getContextData(Long contextUId) throws DataException, SystemException
  {
    try
    {
      ContextData ctxData = getEntityHandler().getEntityByContextUId(contextUId);
      if (ctxData == null)
        throw new DataException("Context does not exist contextUId=" + contextUId);
      return (HashMap) ctxData.getContextData().clone();
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in getting ContextData", th);
    }
  }

  /**
   * This method returns the ContextKey's in this context
   * @param contextUId This is the context uid
   * @return Collection of ContextKey's in this context
   * @throws DataException
   * @throws SystemException
   */
  public Collection getContextKeys(Long contextUId) throws DataException, SystemException
  {
    try
    {
      ContextData ctxData = getEntityHandler().getEntityByContextUId(contextUId);
      if (ctxData == null)
        throw new DataException("[DataManagerBean.getContextKeys] Context does not exist contextUId=" + contextUId);
      return ctxData.getContextData().keySet();
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[DataManagerBean.getContextKeys] Error ", th);
    }
  }

  /**
   * Removes the context
   * @param contextUId
   * @throws DataException
   * @throws SystemException
   */
  public void removeContextUId(Long contextUId) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().removeByContextUId(contextUId);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[DataManagerBean.removeContextUId(Long contextUId)] Error ", th);
    }
  }

  /**
   * Removes data in the context
   * @param contextUId
   * @throws DataException
   * @throws SystemException
   */
  public void removeContextData(Long contextUId) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().clearContextData(contextUId);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in removing ContextData: "+th.getMessage(), th);
    }
  }

  /**
   * This method removes the context data with the ContextKey's specified in this Collection
   * @param contextUId This is the context uid
   * @param contextKeyColl This is the collection of ContextKey's
   * @throws DataException
   * @throws SystemException
   */
  public void removeContextData(Long contextUId, Collection contextKeyColl) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().clearContextData(contextUId, contextKeyColl);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[DataManagerBean.removeContextData(Long contextUId,Collection contextKeyColl)] Error ", th);
    }
  }

  /**
   * This method removes the context data with the ContextKey
   * @param contextUId This is the context uid
   * @param key This is the ContextKey
   * @throws DataException
   * @throws SystemException
   */
  public void removeContextData(Long contextUId, ContextKey key) throws DataException, SystemException
  {
    try
    {
      getEntityHandler().clearContextData(contextUId, key);
    }
    catch (DataException de)
    {
      throw de;
    }
    catch (SystemException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[DataManagerBean.removeContextData(Long contextUId,ContextKey key)] Error ", th);
    }
  }

  //**************************************************************************

  private ContextDataEntityHandler getEntityHandler()
  {
    return ContextDataEntityHandler.getInstance();
  }
}
