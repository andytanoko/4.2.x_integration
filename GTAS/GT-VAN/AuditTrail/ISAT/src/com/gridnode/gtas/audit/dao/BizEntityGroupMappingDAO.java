/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizEntiyGroupMappingDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.dao;

import java.util.Collection;

import com.gridnode.gtas.audit.model.BizEntityGroupMapping;


/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.1.2)
 */
public class BizEntityGroupMappingDAO extends AuditTrailEntityDAO
{
  public BizEntityGroupMappingDAO()
  {
    super(false);
  }
  
  public BizEntityGroupMappingDAO(boolean newSession)
  {
    super(newSession);
  }
  
  public Collection<BizEntityGroupMapping> getBizEntityGroupMappingsByGroups(Collection<String> groupNames)
  {
    String queryName = getPersistenceClass().getName()+".getGroupMappingsByGroupNames";
    String[] param = new String[]{"groupName"};
    Object[] paramValue = new Object[]{groupNames};
    return query(queryName, param, paramValue);
  }
  
  public Collection<BizEntityGroupMapping> getAllBizEntityGroupMapping()
  {
    String queryName = getPersistenceClass().getName()+".getAllGroupMapping";
    return query(queryName, null, null);
  }
  
  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.dao.AuditTrailEntityDAO#getPersistenceClass()
   */
  @Override
  public Class getPersistenceClass()
  {
    // TODO Auto-generated method stub
    return BizEntityGroupMapping.class;
  }

}
