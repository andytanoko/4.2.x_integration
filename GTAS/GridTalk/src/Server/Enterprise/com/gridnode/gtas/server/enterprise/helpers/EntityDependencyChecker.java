/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Enterprise module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * BusinessEntity - dependent on ChannelInfo (managed by ResourceLink)
 * Partner        - dependent on ChannelInfo (managed by ResourceLink)
 * Partner        - dependent on BusinessEntity (managed by ResourceLink)
 * User           - dependent on BusinessEntity (managed by ResourceLink)
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {
  }


  /**
   * Checks whether there are dependent BusinessEntities on the specified ChannelInfo.
   * 
   * @param channelUid The UID of the ChannelInfo.
   * @return A Set of BusinessEntity entities that are dependent on the ChannelInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentBusinessEntitiesForChannelInfo(Long channelUid)
  {
    Set dependents = null;
    try
    {
      dependents = getBizEntityListByChannel(channelUid); 
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentBusinessEntitiesForChannelInfo] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Partners on the specified ChannelInfo.
   * 
   * @param channelUid The UID of the ChannelInfo.
   * @return A Set of Partner entities that are dependent on the ChannelInfo, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnersForChannelInfo(Long channelUid)
  {
    Set dependents = null;
    try
    {
      dependents = getPartnerListByChannel(channelUid);      
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentBusinessEntitiesForChannelInfo] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent UserAccounts on the specified BusinessEntity.
   * 
   * @param beUid The UID of the BusinessEntity.
   * @return A Set of UserAccount entities that are dependent on the BusinessEntity, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentUsersForBusinessEntity(Long beUid)
  {
    Set dependents = null;
    try
    {
      dependents = getUserListByBusinessEntity(beUid); 
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentUsersForBusinessEntity] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent Partners on the specified BusinessEntity.
   * 
   * @param beUid The UID of the BusinessEntity.
   * @return A Set of Partner entities that are dependent on the BusinessEntity, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnersForBusinessEntity(Long beUid)
  {
    Set dependents = null;
    try
    {
      dependents = getPartnerListByBusinessEntity(beUid);      
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnersForBusinessEntity] Error", t);
    }
    
    return dependents;
  }


  /**
   * Get a list of BusinessEntity entities that are associated with the
   * specified ChannelInfo.
   * 
   * @param channelUid The UID of the ChannelInfo.
   * @return A Set of BusinessEntity entities associated with the ChannelInfo.
   * @throws Throwable Error in retrieving the associations from
   * EnterpriseHierarchyManager or in retrieving the BusinessEntity(s) from
   * BusinessRegistryManager.
   */
  private Set getBizEntityListByChannel(Long channelUid)
    throws Throwable
  {
    Set set = new HashSet();
    
    Collection beUidList = ServiceLookupHelper.getEnterpriseHierarchyMgr().getBizEntitiesForChannel(channelUid);
    
    if (beUidList != null)
    {
      set.addAll(ActionHelper.getBusinessEntities(beUidList));
    }   
    return set;
  }
  
  /**
   * Get a list of Partner entities that are associated with the
   * specified ChannelInfo.
   * 
   * @param channelUid The UID of the ChannelInfo.
   * @return A Set of Partner entities associated with the ChannelInfo.
   * @throws Throwable Error in retrieving the associations from
   * EnterpriseHierarchyManager or in retrieving the Partner(s) from
   * PartnerManager.
   */
  private Set getPartnerListByChannel(Long channelUid)
    throws Throwable
  {
    Set set = new HashSet();
    
    Collection partnerUidList = ServiceLookupHelper.getEnterpriseHierarchyMgr().getPartnersForChannel(channelUid);
    
    if (partnerUidList != null)
    {
      set.addAll(ActionHelper.getPartners(partnerUidList));
    }   
    return set;
  }


  /**
   * Get a list of Partner entities that are associated with the
   * specified BusinessEntity.
   * 
   * @param beUid The UID of the BusinessEntity.
   * @return A Set of Partner entities associated with the BusinessEntity.
   * @throws Throwable Error in retrieving the associations from
   * EnterpriseHierarchyManager or in retrieving the Partner(s) from
   * PartnerManager.
   */
  private Set getPartnerListByBusinessEntity(Long beUid)
    throws Throwable
  {
    Set set = new HashSet();
    
    Collection partnerUidList = ServiceLookupHelper.getEnterpriseHierarchyMgr().getPartnersForBizEntity(beUid);
    
    if (partnerUidList != null)
    {
      set.addAll(ActionHelper.getExistingPartners(partnerUidList));
    }   
    return set;
  }

  /**
   * Get a list of UserAccount entities that are associated with the
   * specified BusinessEntity.
   * 
   * @param beUid The UID of the BusinessEntity.
   * @return A Set of UserAccount entities associated with the BusinessEntity.
   * @throws Throwable Error in retrieving the associations from
   * EnterpriseHierarchyManager or in retrieving the Partner(s) from
   * PartnerManager.
   */
  private Set getUserListByBusinessEntity(Long beUid)
    throws Throwable
  {
    Set set = new HashSet();
    
    Collection userUidList = ServiceLookupHelper.getEnterpriseHierarchyMgr().getUsersForBizEntity(beUid);
    
    if (userUidList != null)
    {
      set.addAll(ActionHelper.getExistingUserAccounts(userUidList));
    }   
    return set;
  }
  
  
}
