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
 * Jan 11 2006    Tam Wei Xiang       Added checkDependentMessageTemplatesForJmsDestination(Long),
 *                                    getMessageTemplateList(Long)
 * Mar 03 2006    Neo Sok Lay         Use generics                                         
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * AppServices Alert module. <p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * Alert  - dependent on Action (managed in AlertAction)
 * Action - dependent on MessageTemplate
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDependencyChecker
{

  /**
   * Constructor for AlertActionDependencyChecker.
   */
  public EntityDependencyChecker()
  {
  }

  /**
   * Checks whether there are dependent Alerts on the specified Action.
   * 
   * @param actionUid The UID of the Action.
   * @return A Set of Alert entities that are dependent on the Action, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set<Alert> checkDependentAlertsForAction(Long actionUid)
  {
    Set<Alert> dependents = null;
    try
    {
      dependents = getAlertList(actionUid);
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("EntityDependencyChecker", "checkDependentAlertsForAction",
        "Error", t);
    }
    
    return dependents;   
  }
  
  /**
   * Get the list of Alerts that have the specified Action.
   * 
   * @param actionUid The UID of the Action.
   * @return A Set of Alert entities that are associated to the
   * specified Action.
   * @throws Throwable Error in retrieving the associations from AlertManager.
   */  
  private Set<Alert> getAlertList(Long actionUid) throws Throwable
  {
    IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();
    Collection<AlertAction> alertActionList = mgr.getAlertActionsByActionUId(actionUid);

    Set<Alert> set = new HashSet<Alert>();
    if (alertActionList != null)
    {
      Long alertUid = null;
      for (AlertAction alertAction : alertActionList)
      {
        try
        {
          alertUid = alertAction.getAlertUid();
          set.add(mgr.getAlertByAlertUId(alertUid));
        }
        catch (Exception ex)
        {
          //skip if alert not found
        }
      }
    }

    return set;    
  }
  
  /**
   * Checks whether there are dependent Actions on the specified MessageTemplate.
   * 
   * @param msgTemplateUid The UID of the MessageTemplate.
   * @return A Set of Action entities that are dependent on the MessageTemplate, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set<Action> checkDependentActionsForMessageTemplate(Long msgTemplateUid)
  {
    Set<Action> dependents = null;
    try
    {
      dependents = getActionList(msgTemplateUid);
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("EntityDependencyChecker", "checkDependentActionsForMessageTemplates",
        "Error", t);
    }
    
    return dependents;   
  }

  /**
   * Get the list of Actions that have the specified MessageTemplate.
   * 
   * @param msgTemplateUid The UID of the MessageTemplate.
   * @return A Set of Action entities that are associated to the
   * specified MessageTemplate.
   * @throws Throwable Error in retrieving the associations from AlertManager.
   */  
  private Set<Action> getActionList(Long msgTemplateUid) throws Throwable
  {
    IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();
    
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Action.MSG_UID,
      filter.getEqualOperator(), msgTemplateUid, false);

    Set<Action> set = new HashSet<Action>();

    Collection<Action> actionList = mgr.getActions(filter);

    if (actionList != null)
    {
      set.addAll(actionList);
    }

    return set;    
  }    
  
  /**
   * TWX: Check is there any message template depend on the jms destination.
   * @param jmsDestUID
   * @return a set of message templates or empty set if no message templates
   *         associate with the jms destination. Null if exception has occured.
   */
  public Set<MessageTemplate> checkDependentMessageTemplatesForJmsDestination(Long jmsDestUID)
  {
  	Set<MessageTemplate> dependents = null;
  	try
  	{
  		dependents = getMessageTemplateList(jmsDestUID);
  	}
  	catch(Throwable th)
  	{
  		AlertLogger.warnLog("EntityDependencyChecker", "checkDependentMessageTemplatesForJmsDestination",
  		                     "Error", th);
  	}
  	return dependents;
  }
  
  /**
   * TWX: Get a list of message templates that link to the jms destination.
   * @param jmsDestUID PK of jms_destination table
   * @return a list of message templates of empty set if no msg template have
   *         the specified jms destination.
   * @throws Exception
   */
  private Set<MessageTemplate> getMessageTemplateList(Long jmsDestUID)
  	throws Exception
  {
  	Set<MessageTemplate> set = new HashSet<MessageTemplate>();
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(filter.getAndConnector(), MessageTemplate.JMS_DESTINATION,
  	                       filter.getEqualOperator(), jmsDestUID, false);
  	
  	IAlertManagerObj manager = ServiceLookupHelper.getAlertManager();
  	Collection<MessageTemplate> result = manager.getMessageTemplates(filter);
  	
  	if(result != null)
  	{
  		set.addAll(result);
  	}
  	return set;
  }
}
