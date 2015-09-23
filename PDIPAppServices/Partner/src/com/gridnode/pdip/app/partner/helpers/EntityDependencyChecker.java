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
package com.gridnode.pdip.app.partner.helpers;

import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * AppServices Partner module. <p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * Partner      - dependent on PartnerType
 * Partner      - dependent on PartnerGroup
 * PartnerGroup - dependent on PartnerType
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
   * Checks whether there are dependent Partners on the specified PartnerType.
   * 
   * @param partnerTypeUid The UID of the PartnerType.
   * @return A Set of Partner entities that are dependent on the PartnerType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnersForPartnerType(Long partnerTypeUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Partner.PARTNER_TYPE, filter.getEqualOperator(),
        partnerTypeUid, false);
        
      dependents = getPartnerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnersForPartnerType] Error", t);
    }
    
    return dependents;   
  }
  
  /**
   * Checks whether there are dependent Partners on the specified PartnerGroup.
   * 
   * @param partnerGrpUid The UID of the PartnerGroup.
   * @return A Set of Partner entities that are dependent on the PartnerGroup, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnersForPartnerGroup(Long partnerGrpUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Partner.PARTNER_GROUP, filter.getEqualOperator(),
        partnerGrpUid, false);
        
      dependents = getPartnerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnersForPartnerGroup] Error", t);
    }
    
    return dependents;   
  }
    
  /**
   * Get the list of Partners that satisfy the specified filter
   * condition.
   * 
   * @param filter The filtering condition.
   * @return A Set of Partner entities that satisfy the filter condition.
   * @throws Throwable Error in retrieving the associations from PartnerManager.
   */  
  private Set getPartnerList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection partnerList = getPartnerManager().findPartner(filter);

    if (partnerList != null)
    {
      set.addAll(partnerList);
    }

    return set;    
  }

  /**
   * Checks whether there are dependent PartnerGroups on the specified PartnerType.
   * 
   * @param partnerTypeUid The UID of the PartnerType.
   * @return A Set of PartnerGroup entities that are dependent on the PartnerType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnerGroupsForPartnerType(Long partnerTypeUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, PartnerGroup.PARTNER_TYPE, filter.getEqualOperator(),
        partnerTypeUid, false);
        
      dependents = getPartnerGroupList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnerGroupsForPartnerType] Error", t);
    }
    
    return dependents;   
  }
  
  /**
   * Get the list of PartnerGroups that satisfy the specified filter
   * condition.
   * 
   * @param filter The filtering condition.
   * @return A Set of PartnerGroup entities that satisfy the filter condition.
   * @throws Throwable Error in retrieving the entities from PartnerManager.
   */  
  private Set getPartnerGroupList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection partnerGroupList = getPartnerManager().findPartnerGroup(filter);

    if (partnerGroupList != null)
    {
      set.addAll(partnerGroupList);
    }

    return set;    
  }  


  private IPartnerManagerObj getPartnerManager() throws Exception
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
        IPartnerManagerHome.class.getName(),
        IPartnerManagerHome.class,
        new Object[0]);
  }
}
