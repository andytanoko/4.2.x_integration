/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GroupMappingHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.model.BizEntityGroupMapping;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;

/**
 * This class is responsible to load the mapping between the BEID and its correspond groupName identifier
 * in OTC.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class GroupMappingHelper
{
  private static final String CLASS_NAME = "GroupMappingHelper";
  private static final GroupMappingHelper _helper = new GroupMappingHelper();
  
  public static GroupMappingHelper getInstance()
  {
    return _helper;
  }
  
  /**
   * Retrieve the group mapping value given the BeID. If can't find the matched groupName, reload again
   * from DB.
   * @param beID
   * @return
   */
  public String getGroupMapping(String beID, AuditTrailEntityDAO dao) throws AuditTrailTrackingException
  {
    //TODO maybe can use hibernate EhCache as cache provider, cache strategy: read-only
    
    if(beID == null)
    {
      throw new AuditTrailTrackingException("BeID is null");
    }
    String queryName = BizEntityGroupMapping.class.getName()+".getGroupMappingByBizID";
    String[] paramName = new String[]{"beID"};
    String[] paramValue = new String[]{beID};
    
    BizEntityGroupMapping groupMapping = (BizEntityGroupMapping)dao.queryOne(queryName, paramName, paramValue);
    if(groupMapping == null)
    {
      throw new AuditTrailTrackingException("Can't get any group mapping given BE ID ["+beID+"]. Please initialize it in GTVAN DB");
    }
    else
    {
      return groupMapping.getGroupName();
    }
  }
  
  /*
  private void loadGroupMapping(Hashtable<String, String> groupMapping, AuditTrailEntityDAO dao) throws AuditTrailTrackingException
  {
    String queryName = BizEntityGroupMapping.class.getName()+".getAllGroupMapping";
    Collection<BizEntityGroupMapping> mappings = dao.query(queryName, null, null);
    if(mappings == null || mappings.size() == 0)
    {
      throw new AuditTrailTrackingException("["+CLASS_NAME+".loadGroupMapping] Can't found the group mapping in DB. Pls initialize it");
    }
    else
    {
      Iterator<BizEntityGroupMapping> ite = mappings.iterator();
      while(ite.hasNext())
      {
        BizEntityGroupMapping mapping = ite.next();
        String beID = mapping.getBeID();
        String groupName = mapping.getGroupName();
        groupMapping.put(beID, groupName);
      }
    }
  }*/
}
