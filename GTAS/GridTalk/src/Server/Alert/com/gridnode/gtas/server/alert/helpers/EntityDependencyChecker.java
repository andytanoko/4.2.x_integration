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
 * Jul 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS Alert module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * AlertTrigger - dependent on Alert
 * AlertTrigger - dependent on Partner
 * AlertTrigger - dependent on PartnerType
 * AlertTrigger - dependent on PartnerGroup 
 * AlertTrigger - dependent on DocumentType
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
   * Checks whether there are dependent AlertTriggers on the specified Alert.
   * 
   * @param alertUid The UID of the Alert.
   * @return A Set of AlertTrigger entities that are dependent on the Alert, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentAlertTriggersForAlert(Long alertUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertTrigger.ALERT_UID,
        filter.getEqualOperator(), alertUid, false);

      dependents = getAlertTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentAlertTriggersForAlert] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent AlertTriggers on the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of AlertTrigger entities that are dependent on the Partner, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentAlertTriggersForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertTrigger.PARTNER_ID,
        filter.getEqualOperator(), partnerId, false);
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.LEVEL,
        filter.getEqualOperator(), AlertTrigger.LEVEL_4, false);

      dependents = getAlertTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentAlertTriggersForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent AlertTriggers on the specified PartnerType.
   * 
   * @param name The name of the PartnerType.
   * @return A Set of AlertTrigger entities that are dependent on the PartnerType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentAlertTriggersForPartnerType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertTrigger.PARTNER_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.LEVEL,
        filter.getLessOperator(), AlertTrigger.LEVEL_4, false);
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.LEVEL,
        filter.getGreaterOperator(), AlertTrigger.LEVEL_1, false);
        
      dependents = getAlertTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentAlertTriggersForPartnerType] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent AlertTriggers on the specified PartnerGroup.
   * 
   * @param name The name of the PartnerGroup.
   * @return A Set of AlertTrigger entities that are dependent on the PartnerGroup, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentAlertTriggersForPartnerGroup(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertTrigger.PARTNER_GROUP,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.LEVEL,
        filter.getEqualOperator(), AlertTrigger.LEVEL_3, false);
        
      dependents = getAlertTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentAlertTriggersForPartnerGroup] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent AlertTriggers on the specified DocumentType.
   * 
   * @param name The name of the DocumentType.
   * @return A Set of AlertTrigger entities that are dependent on the DocumentType, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentAlertTriggersForDocumentType(String name)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertTrigger.DOC_TYPE,
        filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.LEVEL,
        filter.getGreaterOperator(), AlertTrigger.LEVEL_0, false);
        
      dependents = getAlertTriggerList(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentAlertTriggersForPartnerGroup] Error", t);
    }
    
    return dependents;
  }
    
  /**
   * Get a list of AlertTriggers that satisfy the specified filter condition.
   * 
   * @param filter The Filtering condition
   * @return A Set of AlertTrigger entities.
   * @throws Throwable Error in retrieving the AlertTriggers from GridTalkAlertManager.
   */    
  private Set getAlertTriggerList(DataFilterImpl filter) throws Throwable
  {
    Set set = new HashSet();

    Collection alertTriggerList = ServiceLookupHelper.getGridTalkAlertMgr().findAlertTriggers(filter);

    if (alertTriggerList != null)
    {
      set.addAll(alertTriggerList);
    }

    return set;    
  }
      
}
