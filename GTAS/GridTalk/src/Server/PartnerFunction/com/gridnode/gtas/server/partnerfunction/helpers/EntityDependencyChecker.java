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
package com.gridnode.gtas.server.partnerfunction.helpers;

import com.gridnode.gtas.server.notify.IAlertTypes;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.IPartnerFunctionManagerObj;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * GTAS PartnerFunction module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * PartnerFunction - dependent on Alert via WorkflowActivity
 * PartnerFunction - dependent on Port via WorkflowActivity
 * PartnerFunction - dependent on GridTalkMappingRule via WorkflowActivity
 * PartnerFunction - dependent on UserProcedure via WorkflowActivity
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{
  private static final String USER_DEF_ALERT_PATTERN = IAlertTypes.USER_DEFINED + ";{0};"; 
  private static final String EXIT_TO_PORT_PATTERN = ";{0};"; 
  private static final String MAPPING_RULE_PATTERN = ";{0};"; 
  private static final String USER_PROC_PATTERN    = ";{0};"; 
  private static final String WF_ACTIVITY_UID_PATTERN = ";{0};"; 
  
  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {

  }

  /**
   * Checks whether there are dependent PartnerFunctions on the specified Alert.
   * 
   * @param alertUid The UID of the Alert.
   * @return A Set of PartnerFunction entities that are dependent on the Alert, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnerFunctionsForAlert(Long alertUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, WorkflowActivity.PARAM_LIST,
        filter.getLocateOperator(), getRaiseAlertPattern(alertUid), false);
      filter.addSingleFilter(filter.getAndConnector(), WorkflowActivity.ACTIVITY_TYPE,
        filter.getEqualOperator(), WorkflowActivity.RAISE_ALERT, false);

      dependents = getPartnerFunctionListByWorkflowActivityFilter(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnerFunctionsForAlert] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent PartnerFunctions on the specified Port.
   * 
   * @param portUid The UID of the Port.
   * @return A Set of PartnerFunction entities that are dependent on the Port, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnerFunctionsForPort(Long portUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, WorkflowActivity.PARAM_LIST,
        filter.getLocateOperator(), getExitToPortPattern(portUid), false);
      filter.addSingleFilter(filter.getAndConnector(), WorkflowActivity.ACTIVITY_TYPE,
        filter.getEqualOperator(), WorkflowActivity.EXIT_TO_PORT, false);

      dependents = getPartnerFunctionListByWorkflowActivityFilter(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnerFunctionsForPort] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent PartnerFunctions on the specified UserProcedure.
   * 
   * @param userProcUid The UID of the UserProcedure.
   * @return A Set of PartnerFunction entities that are dependent on the UserProcedure, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnerFunctionsForUserProcedure(Long userProcUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, WorkflowActivity.PARAM_LIST,
        filter.getLocateOperator(), getUserProcedurePattern(userProcUid), false);
      filter.addSingleFilter(filter.getAndConnector(), WorkflowActivity.ACTIVITY_TYPE,
        filter.getEqualOperator(), WorkflowActivity.USER_PROCEDURE, false);

      dependents = getPartnerFunctionListByWorkflowActivityFilter(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnerFunctionsForUserProcedure] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent PartnerFunctions on the specified GridTalkMappingRule.
   * 
   * @param gtMRUid The UID of the Port.
   * @return A Set of PartnerFunction entities that are dependent on the GridTalkMappingRule, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentPartnerFunctionsForGtMappingRule(Long gtMRUid)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, WorkflowActivity.PARAM_LIST,
        filter.getLocateOperator(), getMappingRulePattern(gtMRUid), false);
      filter.addSingleFilter(filter.getAndConnector(), WorkflowActivity.ACTIVITY_TYPE,
        filter.getEqualOperator(), WorkflowActivity.MAPPING_RULE, false);

      dependents = getPartnerFunctionListByWorkflowActivityFilter(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentPartnerFunctionsForGtMappingRule] Error", t);
    }
    
    return dependents;
  }

  private String getRaiseAlertPattern(Long alertUid)
  {
    return MessageFormat.format(USER_DEF_ALERT_PATTERN, new Object[] {alertUid});  
  }
  
  private String getExitToPortPattern(Long portUid)
  {
    return MessageFormat.format(EXIT_TO_PORT_PATTERN, new Object[] {portUid});  
  }

  private String getMappingRulePattern(Long gtMRUid)
  {
    return MessageFormat.format(MAPPING_RULE_PATTERN, new Object[] {gtMRUid});  
  }

  private String getUserProcedurePattern(Long userProcUid)
  {
    return MessageFormat.format(USER_PROC_PATTERN, new Object[] {userProcUid});  
  }

  private String getWorkflowActivityUidPattern(Long workflowActivityUid)
  {
    return MessageFormat.format(WF_ACTIVITY_UID_PATTERN, new Object[] {workflowActivityUid});  
  }

  /**
   * Get the list of PartnerFunction(s) using the specified
   * filter condition for WorkflowActivity.
   * 
   * @param filter The filter condition on the WorkflowActivity db.
   * @return A Set of PartnerFunction entities having the WorkflowActivity(s)
   * that satisfy the specified filter condition. 
   * @throws Throwable Error in retrieving the associations from PartnerFunctionManager.
   */
  private Set getPartnerFunctionListByWorkflowActivityFilter(DataFilterImpl filter)
    throws Throwable
  {
    IPartnerFunctionManagerObj mgr = ActionHelper.getManager();

    Set set = new HashSet();

    Collection workflowActivityUidList = mgr.findWorkflowActivityKeys(filter);

    if (workflowActivityUidList != null)
    {
      for (Iterator i=workflowActivityUidList.iterator(); i.hasNext(); )
      {
        set.addAll(getPartnerFunctionListByWorkflowActivity((Long)i.next(), mgr));
      }    
    }

    return set;    
  }
  
  /**
   * Get the list of PartnerFunctions that have the specified WorkflowActivity.
   * 
   * @param workflowActivityUid The UID of the WorkflowActivity.
   * @return A Set of PartnerFunction entities that are associated to the
   * specified WorkflowActivity.
   * @throws Throwable Error in retrieving the associations from PartnerFunctionManager.
   */  
  private Collection getPartnerFunctionListByWorkflowActivity(Long workflowActivityUid, IPartnerFunctionManagerObj mgr) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerFunction.WORKFLOW_ACTIVITY_UIDS, 
      filter.getLocateOperator(), getWorkflowActivityUidPattern(workflowActivityUid), false);
      
    return mgr.findPartnerFunctions(filter);  
  }
      
}
