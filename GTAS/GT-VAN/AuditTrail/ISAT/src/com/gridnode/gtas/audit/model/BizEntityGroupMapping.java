/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizEntityGroupMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 4, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.model;

import com.gridnode.util.db.AbstractPersistable;

/**
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 * 
 * @hibernate.class table = "`isat_biz_entity_group_mapping`"
 * 
 * @hibernate.query name = "getGroupMappingByBizID"
 *    query = "FROM BizEntityGroupMapping groupMapping WHERE groupMapping.beID = :beID"
 * @hibernate.query name = "getAllGroupMapping"
 *    query = "FROM BizEntityGroupMapping"   
 * @hibernate.query name = "getGroupMappingByGroupName"
 *    query = "FROM BizEntityGroupMapping groupMapping WHERE groupMapping.groupName = :groupName"  
 * @hibernate.query name = "getGroupMappingsByGroupNames"
 *    query = "FROM BizEntityGroupMapping groupMapping WHERE groupMapping.groupName IN (:groupName)"    
 */
public class BizEntityGroupMapping extends AbstractPersistable implements IAuditTrailEntity
{
  private String _beID;
  private String _groupName;
  
  public BizEntityGroupMapping() {}
  
  /**
   * @hibernate.property name = "groupName" column = "`group_name`"
   */
  public String getGroupName()
  {
    return _groupName;
  }

  public void setGroupName(String groupName)
  {
    _groupName = groupName;
  }
  
  /**
   * @hibernate.property name = "beID" column = "`be_id`"
   * @return
   */
  public String getBeID()
  {
    return _beID;
  }

  public void setBeID(String _beid)
  {
    _beID = _beid;
  }
  

  public String toString()
  {
    return "BizEntityGroupMapping[UID: "+getUID()+" groupName: "+getGroupName()+" beID: "+getGroupName()+"]";
  }
}
