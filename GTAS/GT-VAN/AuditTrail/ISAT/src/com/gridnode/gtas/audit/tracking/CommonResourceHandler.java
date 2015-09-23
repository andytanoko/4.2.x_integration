/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommonResourceHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 05, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.gtas.audit.dao.CommonResourceDAO;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.model.helpers.CommonResourcePK;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to take care the Common Resource eg Pip name, doc type and populate them into DB.
 * It help the fetching of common resource within the UI more efficient.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class CommonResourceHandler 
{
	private static final String CLASS_NAME = "CommonResourceHandler";
  private static CommonResourceHandler _instance = new CommonResourceHandler();
	private Hashtable<CommonResourcePK, Boolean> _commonResource = new Hashtable<CommonResourcePK, Boolean>();
	private final Object _lock = new Object();
	private Logger _logger = null;
  private CommonResourceDAO _dao = null;
  
	public CommonResourceHandler()
	{
    _logger = getLogger();
    _dao = new CommonResourceDAO();
	}
	
  /*
	public static CommonResourceHandler getInstance()
	{
		return _instance;
	}*/
	
  /**
   * Insert a collection of CommonResource entity into DB if it hasn't been persisted in DB yet.
   * @param cmmResources
   * @throws AuditTrailTrackingException
   */
  public void insertCommonResources(Collection<CommonResource> cmmResources)throws AuditTrailTrackingException
  {
    if(cmmResources != null && cmmResources.size() > 0)
    {
      Iterator<CommonResource> ite = cmmResources.iterator();
      while(ite.hasNext())
      {
        insertCommonResource(ite.next());
      }
    }
  }
  
  /**
   * Insert the CommonResource entity into DB if it hasn't been persisted in DB yet.
   * @param cmmResource
   * @throws AuditTrailTrackingException
   */
  public void insertCommonResource(CommonResource cmmResource) throws AuditTrailTrackingException
  {
    _logger.logMessage("",null,"Adding Common Resource "+cmmResource+" .....");
    String group = cmmResource.getGroupName();
    String type = cmmResource.getType();
    String code = cmmResource.getCode();
    int cmmResourceCount = _dao.getCommonResourceCount(group, type, code);
    if(cmmResourceCount > 0)
    {
      _logger.logMessage("",null,"Common Resource "+cmmResource+" already existed !");
      return;
    }
    else
    {
      persistCmmResource(cmmResource, _dao);
    }
  }
  
  public String persistCmmResource(IAuditTrailEntity entity, CommonResourceDAO dao)
  {
      return (String)dao.create(entity);
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
	/**
	 * Insert the commonResource into DB if such a resource not yet persisted
	 * before.
	 * @param cmmResource
	 */
  /*
	public void insertCommonResource(CommonResource cmmResource) throws AuditTrailTrackingException
	{
    String methodName = "insertCommonResource";
    
		synchronized (_lock) 
		{
			CommonResourcePK cmmResourceKey = new CommonResourcePK(cmmResource.getType(), 
					                                      cmmResource.getCode(), cmmResource.getGroupName());
			if(_commonResource.containsKey(cmmResourceKey))
			{
				return;
			}
			else
			{
				//Insert into DB. Must ensure we already persist the entity prior return
        try
        {
          persistCmmResource(cmmResource);
         _logger.logMessage("insertCommonResource", null, "Inserted cmm resource ... "+cmmResource);
        }
        catch(Exception ex)
        {
          _logger.logError(methodName, null, "Error in inserting cmm resource "+ex.getMessage(), ex);
          throw new AuditTrailTrackingException("Error in inserting cmm resource "+ex.getMessage(), ex);
        }
				_commonResource.put(cmmResourceKey, true);
			}
		}
	} */
	
  /**
   * Cache the available common resource from DB.
   * @param cmmResources
   * @param commonResourceHtb
   */
  /*
	private void init( Hashtable<CommonResourcePK,Boolean> commonResourceHtb)
	{
    CommonResourceDAO dao = new CommonResourceDAO(true);
    Collection<CommonResource> cmmResources = null;
    try
    {
      dao.beginTransaction();
      cmmResources = dao.getCommonResources();
      dao.commitTransaction();
    }
    catch(Exception ex)
    {
      _logger.logError("init", null, "Error in fetching Common Resources entity from DB", ex);
      if(dao.hasTransaction())
      {
        dao.rollbackTransaction();
      }
    }
    finally
    {
      dao.closeTransactionContext();
    }
    
		if(cmmResources != null && cmmResources.size() > 0)
		{
			for(Iterator<CommonResource> i = cmmResources.iterator(); i.hasNext();)
			{
				CommonResource cmmResource = i.next();
				
				String groupName = cmmResource.getGroupName();
				String type = cmmResource.getType();
				String code = cmmResource.getCode();
				CommonResourcePK resourcePK = new CommonResourcePK(type, code, groupName);
				commonResourceHtb.put(resourcePK, true);
			}
      _logger.logMessage("", null, "Retrieve cmm rsource is "+(commonResourceHtb != null ? commonResourceHtb.size()+"" : "null"));
		}
	} */
  
  /*
	public String persistCmmResource(IAuditTrailEntity entity)
  {
    CommonResourceDAO dao = new CommonResourceDAO(true);
    
    try
    {
      dao.beginTransaction();
      String uid = (String)dao.create(entity);
      dao.commitTransaction();
      return uid;
    }
    catch(Exception ex)
    {
      _logger.logError("insertAuditTrailEntity", new Object[]{}, "Error in inserting entity CommonResource into DB. ", ex);
      if(dao.hasTransaction())
      {
        dao.rollbackTransaction();
      }
      return null;
    }
    finally
    {
      dao.closeTransactionContext();
    }
  } */
}
