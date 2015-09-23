/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommonResourceDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 05, 2007    Tam Wei Xiang       Created
 */

package com.gridnode.gtas.audit.dao;

import java.util.List;

import org.hibernate.LockMode;

import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to handle the DB services for entity CommonResource.
 * 
 * NOTE: the caller require to under the JTA transaction. EG call from SessionBean, MDBean
 * @author Tam Wei Xiang
 * 
 * @since GT 4.03 (GTVAN)
 */
public class CommonResourceDAO extends AuditTrailEntityDAO 
{
  private static final String CLASS_NAME = "CommonResourceDAO";
  private Logger _logger = null;
  
  public CommonResourceDAO()
  {
    super(false);
    _logger = getLogger();
  }
  
  public CommonResourceDAO(boolean newSession)
  {
    super(newSession);
    _logger = getLogger();
  }
  
	/**
   * Get the common resource that has been categoried into group. 
   * @return
	 */
  /*
	public Collection getCommonResources()
	{
		String queryName = getPersistenceClass().getName()+".getCommonResourceWithCategory";
    String[] param = new String[]{"groupName"};
    String[] paramValue = new String[]{""}; //It WILL NOT WORK in Oracle
    
		return query(queryName, param, paramValue);
	}*/
  
  /**
   * Get the total number of the common resource given groupName, type, code
   * @param groupName
   * @param type
   * @param code
   * @return
   */
  public int getCommonResourceCount(String groupName, String type, String code)
  {
    String queryName = getPersistenceClass().getName()+".getCommonResourceCount";
    String[] param = new String[]{"groupName", "type", "code"};
    String[] paramValue = new String[]{groupName, type, code};
    
    Long total = (Long)queryOne(queryName, param, paramValue);
    return total != null ? total.intValue(): 0;
  }
  
  /**
   * Lock a particular isat_resource record in DB to simulate the java lock. This will be useful in
   * the clustering environment. However it may not be scalable due to it is an global lock (eg all
   * other threads will be trying to obtain the same lock)
   * @param type
   * @param code
   * @return
   */
  public List lockResource(String type, String code)
  {
    String queryName = getPersistenceClass().getName()+".getCommonResource";
    String[] param = new String[]{"type", "code"};
    String[] paramValue = new String[]{type, code};
    
    return queryWithLock(queryName, param, paramValue, "cr", LockMode.UPGRADE);
  }
  
	@Override
	public Class getPersistenceClass() 
	{
		return CommonResource.class;
	}
	
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
