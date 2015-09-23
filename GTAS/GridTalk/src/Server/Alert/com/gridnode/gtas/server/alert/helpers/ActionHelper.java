/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-30     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Add methods for manipulating Alert and
 *                                    AlertAction.
 * Apr 23 2003    Neo Sok Lay         Add conversionToMap for AlertTrigger.
 * 6 Jan 06				SC									add methods: setMessageProperties, copyEntityFields
 * 13 Jan 06			SC									add method processMessageProperties
 * 																		comment method setMessageProperties
 */
package com.gridnode.gtas.server.alert.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.gridnode.gtas.events.alert.CreateMessageTemplateEvent;
import com.gridnode.gtas.model.alert.AlertEntityFieldID;
import com.gridnode.gtas.model.alert.IMessageProperty;
import com.gridnode.gtas.model.alert.JmsDestinationEntityFieldID;
import com.gridnode.gtas.model.bizreg.BizRegEntityFieldID;
import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerObj;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.model.*;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

/**
 * This Action class provides common services used by the action classes.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.1
 * @since 2.0
 */
public class ActionHelper
{
  /**
   * Convert an Alert to Map object.
   *
   * @param alert The Alert to convert.
   * @return A Map object converted from the specified Alert.
   *
   * @since 2.0
   */
  public static Map convertAlertToMap(Alert alert)
  {
    return Alert.convertToMap(
             alert,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Alert to Map objects.
   *
   * @param alertList The collection of Alert to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Alert.
   *
   * @since 2.0
   */
  public static Collection convertAlertToMapObjects(Collection alertList)
  {
    return Alert.convertEntitiesToMap(
             (Alert[])alertList.toArray(
             new Alert[alertList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an Action to Map object.
   *
   * @param action The Action to convert.
   * @return A Map object converted from the specified Action.
   *
   * @since 2.0
   */
  public static Map convertActionToMap(Action action)
  {
    return Action.convertToMap(
             action,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Action to Map objects.
   *
   * @param actionList The collection of Action to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Action.
   *
   * @since 2.0
   */
  public static Collection convertActionToMapObjects(Collection actionList)
  {
    return Action.convertEntitiesToMap(
             (Action[])actionList.toArray(
             new Action[actionList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an MessageTemplate to Map object.
   *
   * @param mesageTemplate The MessageTemplate to convert.
   * @return A Map object converted from the specified MessageTemplate.
   *
   * @since 2.0
   */
  public static Map convertMessageTemplateToMap(MessageTemplate mesageTemplate)
  {
    return MessageTemplate.convertToMap(
             mesageTemplate,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of MessageTemplate to Map objects.
   *
   * @param actionList The collection of MessageTemplate to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of MessageTemplate.
   *
   * @since 2.0
   */
  public static Collection convertMessageTemplateToMapObjects(Collection mesageTemplateList)
  {
    return MessageTemplate.convertEntitiesToMap(
             (MessageTemplate[])mesageTemplateList.toArray(
             new MessageTemplate[mesageTemplateList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an AlertType to Map object.
   *
   * @param alertType The AlertType to convert.
   * @return A Map object converted from the specified AlertType.
   *
   * @since 2.0
   */
  public static Map convertAlertTypeToMap(AlertType alertType)
  {
    return Alert.convertToMap(
             alertType,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of AlertType to Map objects.
   *
   * @param alertTypeList The collection of AlertType to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of AlertType.
   *
   * @since 2.0
   */
  public static Collection convertAlertTypeToMapObjects(Collection alertTypeList)
  {
    return Alert.convertEntitiesToMap(
             (AlertType[])alertTypeList.toArray(
             new AlertType[alertTypeList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an AlertCategory to Map object.
   *
   * @param alert The AlertCategory to convert.
   * @return A Map object converted from the specified AlertCategory.
   *
   * @since 2.0
   */
  public static Map convertAlertCategoryToMap(AlertCategory alertCategory)
  {
    return AlertCategory.convertToMap(
             alertCategory,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of AlertCategory to Map objects.
   *
   * @param alertCategoryList The collection of AlertCategory to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of AlertCategory.
   *
   * @since 2.0
   */
  public static Collection convertAlertCategoryToMapObjects(Collection alertCategoryList)
  {
    return AlertCategory.convertEntitiesToMap(
             (AlertCategory[])alertCategoryList.toArray(
             new AlertCategory[alertCategoryList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an AlertTrigger to Map object.
   *
   * @param trigger The AlertTrigger to convert.
   * @return A Map object converted from the specified AlertTrigger.
   *
   * @since 2.1
   */
  public static Map convertAlertTriggerToMap(AlertTrigger trigger)
  {
    return AlertTrigger.convertToMap(
             trigger,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of AlertTrigger to Map objects.
   *
   * @param triggerList The collection of AlertTrigger to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of AlertTrigger.
   *
   * @since 2.1
   */
  public static Collection convertAlertTriggersToMapObjects(Collection triggerList)
  {
    return AlertTrigger.convertEntitiesToMap(
             (AlertTrigger[])triggerList.toArray(
             new AlertTrigger[triggerList.size()]),
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an EmailConfig to Map object.
   *
   * @param emailConfig The EmailConfig to convert.
   * @return A Map object converted from the specified EmailConfig.
   *
   * @since 2.2
   */
  public static Map convertEmailConfigToMap(EmailConfig emailConfig)
  {
    return EmailConfig.convertToMap(
             emailConfig,
             AlertEntityFieldID.getEntityFieldID(),
             null);
  }


  /**
   * Create an Alert in the database. AlertAction(s) are also created based
   * on the BindActions field.
   *
   * @param alert the Alert to create.
   * @return UID of the created alert.
   */
  public static Long createAlert(Alert alert)
    throws Exception
  {
    Long alertUid = null;

    try
    {
      Logger.debug("[ActionHelper.createAlert] Enter");
      IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();
      Collection actionUids = alert.getBindActions();
      alertUid = mgr.createAlert(alert);

      if (actionUids != null)
      {
        bindAlertActions(alertUid, actionUids, mgr);
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.createAlert] Exit");
    }

    return alertUid;
  }

  /**
   * Update an Alert in the database. The AlertAction(s) are also created/deleted
   * based on the current and new set of BindActions.
   *
   * @param alert The Alert to update.
   *
   */
  public static void updateAlert(Alert alert)
    throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.updateAlert] Enter");

      Long alertUid = (Long)alert.getKey();
      Collection actionUids = alert.getBindActions();

      IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();
      mgr.updateAlert(alert);

      if (actionUids != null)
      {
        //make sure any modification do not
        //affect caller.
        actionUids = new ArrayList(alert.getBindActions());
        Collection currBindings = mgr.getAlertActionsByAlertUId(alertUid);

        if (actionUids.isEmpty())
        {
          //delete all
          unbindAlertActions(currBindings, mgr);
        }
        else if (currBindings.isEmpty())
        {
          //create all
          bindAlertActions(alertUid, actionUids, mgr);
        }
        else
        {
          for (Iterator i=currBindings.iterator(); i.hasNext(); )
          {
            AlertAction alertAction = (AlertAction)i.next();
            if (!actionUids.contains(alertAction.getActionUid()))
            {
              //delete
              mgr.deleteAlertAction((Long)alertAction.getKey());
            }
            else
              actionUids.remove(alertAction.getActionUid());
          }
          // what's left are new bindings
          bindAlertActions(alertUid, actionUids, mgr);
        }
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.updateAlert] Exit");
    }
  }

  /**
   * Bind a list of Actions to an Alert i.e. an AlertAction is created for
   * each Action UID.
   *
   * @param alertUid UID of the Alert.
   * @param actionUIds Collection of UIDs of the Actions to bind to Alert.
   * @param mgr AlertManager.
   */
  public static void bindAlertActions(Long alertUid,
                                       Collection actionUids,
                                       IAlertManagerObj mgr)
    throws Exception
  {
    for (Iterator i=actionUids.iterator(); i.hasNext(); )
    {
      Long actionUid = (Long)i.next();
      AlertAction alertAction = new AlertAction();
      alertAction.setActionUid(actionUid);
      alertAction.setAlertUid(alertUid);
      mgr.createAlertAction(alertAction);
    }
  }

  /**
   * Remove AlertAction bindings from the database.
   *
   * @param bindings Collection of AlertAction(s) to remove.
   * @param mgr AlertManager.
   */
  public static void unbindAlertActions(Collection bindings, IAlertManagerObj mgr)
    throws Exception
  {
    for (Iterator i=bindings.iterator(); i.hasNext(); )
    {
      AlertAction alertAction = (AlertAction)i.next();
      mgr.deleteAlertAction((Long)alertAction.getKey());
    }
  }

  /**
   * Delete an Alert from the database. The AlertAction bindings are also deleted.
   *
   * @param alertUid UID of the Alert to delete.
   */
  public static void deleteAlert(Long alertUid) throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.deleteAlert] Enter");

      IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();

      Collection currBindings = mgr.getAlertActionsByAlertUId(alertUid);
      unbindAlertActions(currBindings, mgr);
      mgr.deleteAlert(alertUid);
    }
    finally
    {
      Logger.debug("[ActionHelper.deleteAlert] Exit");
    }
  }

  /**
   * Retrieve an Alert from the database. The UIDs of the Actions bound to the
   * Alert are retrieved into the BindActions field.
   *
   * @param alertUid UID of the Alert.
   * @return The retrieved Alert.
   */
  public static Alert getAlertByUid(Long alertUid) throws Exception
  {
    Alert alert = null;

    try
    {
      Logger.debug("[ActionHelper.getAlertByUid] Exit");

      AlertAction alertAction = null;

      IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();

      alert = mgr.getAlertByAlertUId(alertUid);
      Collection currBindings = mgr.getAlertActionsByAlertUId(alertUid);
      ArrayList actionUids = new ArrayList();
      for (Iterator i=currBindings.iterator(); i.hasNext(); )
      {
        alertAction = (AlertAction)i.next();
        actionUids.add(alertAction.getActionUid());
      }
      alert.setBindActions(actionUids);
    }
    finally
    {
      Logger.debug("[ActionHelper.getAlertByUid] Exit");
    }
    return alert;
  }

  /**
   * Retrieve a Collection of Alerts. The UIDs of the Actions bound to the
   * Alerts are retrieved into the respective BindActions field.
   *
   * @param filter The filtering condition.
   * @return Collection of Alert(s) retrieved.
   */
  public static Collection getAlertsByFilter(IDataFilter filter) throws Exception
  {
    Collection alerts = null;

    try
    {
      Logger.debug("[ActionHelper.getAlertsByFilter] Enter");

      Alert alert = null;
      Collection currBindings = null;
      ArrayList actionUids = null;
      AlertAction alertAction = null;

      IAlertManagerObj mgr = ServiceLookupHelper.getAlertManager();

      alerts = mgr.getAlerts(filter);
      for (Iterator i=alerts.iterator(); i.hasNext(); )
      {
        alert = (Alert)i.next();
        currBindings = mgr.getAlertActionsByAlertUId((Long)alert.getKey());
        actionUids = new ArrayList();
        for (Iterator c=currBindings.iterator(); c.hasNext(); )
        {
          alertAction = (AlertAction)c.next();
          actionUids.add(alertAction.getActionUid());
        }
        alert.setBindActions(actionUids);
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.getAlertsByFilter] Exit");
    }
    return alerts;
  }

  public static void checkAlertExists(Long uid) throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.checkAlertExist] Enter");

      Alert alert = ServiceLookupHelper.getAlertManager().getAlertByAlertUId(uid);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Alert UID: "+uid);
    }
    finally
    {
      Logger.debug("[ActionHelper.checkAlertExist] Exit");
    }

  }

  public static void checkAlertTypeExists(String name) throws Exception
  {
    try
    {
      Logger.debug("[ActionHelper.checkAlertTypeExists] Enter");

      AlertType alertType = ServiceLookupHelper.getAlertManager().getAlertTypeByName(name);
      if (alertType == null)
        throw new Exception("Bad Alert Type: "+name);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Alert Type: "+name);
    }
    finally
    {
      Logger.debug("[ActionHelper.checkAlertTypeExists] Exit");
    }

  }

  public static Collection deleteAlertTriggers(Collection uids) throws Exception
  {
    ArrayList failed = new ArrayList();
    try
    {
      Logger.debug("[ActionHelper.deleteAlertTriggers] Enter");

      IGridTalkAlertManagerObj mgr = ServiceLookupHelper.getGridTalkAlertMgr();
      for (Iterator i=uids.iterator(); i.hasNext(); )
      {
        Long uid = null;
        try
        {
          uid = (Long)i.next();
          mgr.deleteAlertTrigger(uid);
        }
        catch (Throwable t)
        {
          Logger.warn(
            "[ActionHelper.deleteAlertTriggers] Unable to delete record "+uid,
            t);
          failed.add(uid);
        }
      }
    }
    finally
    {
      Logger.debug("[ActionHelper.deleteAlertTriggers] Exit");
    }
    return failed;
  }

  public static Map convertJmsDestinationToMap(JmsDestination jmsDestination)
  {
    return JmsDestination.convertToMap(
             jmsDestination,
             JmsDestinationEntityFieldID.getEntityFieldID(),
             null);
  }
  
  public static Collection convertJmsDestinationsToMapObjects(Collection list)
  {
    return JmsDestination.convertEntitiesToMap(
             (JmsDestination[])list.toArray(new JmsDestination[list.size()]),
             JmsDestinationEntityFieldID.getEntityFieldID(),
             null);
  }
  
//  public static void setMessageProperties(MessageTemplate messageTemplate, Collection messagePropertyMaps)
//  {
//    Vector mpVector = new Vector();
//    MessageProperty mp;
////    String domain;
//    Iterator it = messagePropertyMaps.iterator();
//    while (it.hasNext())
//    {
//    	mp = new MessageProperty();
//      ActionHelper.copyEntityFields((Map)it.next(), mp);
//      mpVector.add(mp);
//    }
//    messageTemplate.setMessageProperty(mpVector);
//  }
  
  public static void copyEntityFields(Map from, AbstractEntity entity)
  {
    if (from != null)
    {
      for (Iterator i=from.keySet().iterator(); i.hasNext(); )
      {
        Number fieldID = (Number)i.next();
        entity.setFieldValue(fieldID, from.get(fieldID));
      }
    }
  }
  
  public static Vector processMessageProperties(Vector inputVector)
  {
  	Vector ret = new Vector();
  	Iterator it = inputVector.iterator();
  	while (it.hasNext())
  	{
  		HashMap map = (HashMap) it.next();
  		String key = (String) map.get(IMessageProperty.KEY);
  		Integer type = (Integer) map.get(IMessageProperty.TYPE);
  		String value = (String) map.get(IMessageProperty.VALUE);
  		MessageProperty mp = new MessageProperty(key, type, value);
  		ret.add(mp);
  	}
  	return ret;
  }
  
  public static void setJmsDestination(Long jmsDestinationUid, MessageTemplate mt)
  {
		JmsDestination jmsDestination = new JmsDestination();
		jmsDestination.setUId(jmsDestinationUid);
		mt.setJmsDestination(jmsDestination);
  }
}