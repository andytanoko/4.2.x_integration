/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkAlertManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 21 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.facade.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.alert.exceptions.IncorrectRecipientFormatException;
import com.gridnode.gtas.server.alert.exceptions.IncorrectTriggerCriteriaException;
import com.gridnode.gtas.server.alert.exceptions.NoMatchingAlertTriggerException;
import com.gridnode.gtas.server.alert.helpers.AlertTriggerEntityHandler;
import com.gridnode.gtas.server.alert.helpers.Logger;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.app.alert.providers.RecipientListData;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

public class GridTalkAlertManagerBean
       implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7678572604155750031L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IGridTalkAlertManagerObj

  // ********************* Methods for AlertTrigger

  /**
   * Create a new AlertTrigger.
   *
   * @param trigger The AlertTrigger entity.
   */
  public Long createAlertTrigger(AlertTrigger trigger)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "createAlertTrigger";
    Object[] params     = new Object[] {
                            trigger};

    Long key = null;
    try
    {
      logger.logEntry(methodName, params);

      if (!trigger.validate())
        throw IncorrectTriggerCriteriaException.createEx(trigger);

      validateRecipients(trigger.getRecipients());

      trigger.nullifyByLevel();
      key = (Long)getAlertTriggerEntityHandler().createEntity(trigger).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return key;
  }

  /**
   * Update a AlertTrigger
   *
   * @param trigger The AlertTrigger entity with changes.
   */
  public void updateAlertTrigger(AlertTrigger trigger)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "updateAlertTrigger";
    Object[] params     = new Object[] {
                            trigger};

    try
    {
      logger.logEntry(methodName, params);

      if (!trigger.validate())
        throw IncorrectTriggerCriteriaException.createEx(trigger);

      validateRecipients(trigger.getRecipients());

      trigger.nullifyByLevel();
      getAlertTriggerEntityHandler().update(trigger);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Delete a AlertTrigger.
   *
   * @param uid The UID of the AlertTrigger to delete.
   */
  public void deleteAlertTrigger(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "deleteAlertTrigger";
    Object[] params     = new Object[] {
                            uid};

    try
    {
      logger.logEntry(methodName, params);

      getAlertTriggerEntityHandler().remove(uid);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Find a AlertTrigger using the AlertTrigger UID.
   *
   * @param uid The UID of the AlertTrigger to find.
   * @return The AlertTrigger found, or <B>null</B> if none exists with that
   * UID.
   */
  public AlertTrigger findAlertTrigger(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "findAlertTrigger";
    Object[] params     = new Object[] {
                            uid};

    AlertTrigger trigger = null;

    try
    {
      logger.logEntry(methodName, params);

      trigger = (AlertTrigger)getAlertTriggerEntityHandler().getEntityByKeyForReadOnly(uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return trigger;

  }

  /**
   * Find a number of AlertTrigger(s) that is under the same level.
   *
   * @param triggerLevel The level of the triggers to find.
   * @return a Collection of AlertTrigger(s) found, or empty collection if none
   * exists.
   */
  public Collection findAlertTriggers(Integer triggerLevel)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "findAlertTriggers";
    Object[] params     = new Object[] {
                            triggerLevel};

    Collection triggers = null;

    try
    {
      logger.logEntry(methodName, params);

      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        AlertTrigger.LEVEL,
        filter.getEqualOperator(),
        triggerLevel,
        false);

      triggers =
        getAlertTriggerEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return triggers;
  }

  /**
   * Find a number of AlertTrigger(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of AlertTrigger(s) found, or empty collection if none
   * exists.
   */
  public Collection findAlertTriggers(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "findAlertTriggers";
    Object[] params     = new Object[] {
                            filter};

    Collection triggers = null;

    try
    {
      logger.logEntry(methodName, params);

      triggers =
        getAlertTriggerEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return triggers;
  }

  /**
   * Find a number of AlertTrigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of AlertTrigger found, or empty collection if
   * none exists.
   */
  public Collection findAlertTriggersKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "findAlertTriggersKeys";
    Object[] params     = new Object[] {
                            filter};

    Collection keys = null;

    try
    {
      logger.logEntry(methodName, params);

      keys =
        getAlertTriggerEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return keys;
  }

  /**
   * @see IGridTalkAlertManagerObj#progressiveFindAlertTrigger
   */
  public AlertTrigger progressiveFindAlertTrigger(
    String alertType,
    String docType,
    String partnerType,
    String partnerGroup,
    String partnerId)
    throws NoMatchingAlertTriggerException,
           FindEntityException,
           SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "progressiveFindAlertTrigger";
    Object[] params     = new Object[] {
                            alertType,
                            docType,
                            partnerType,
                            partnerGroup,
                            partnerId};

    AlertTrigger trigger = null;

    try
    {
      logger.logEntry(methodName, params);

      Collection results = null;
      docType = nullifyIfEmpty(docType);
      partnerType = nullifyIfEmpty(partnerType);
      partnerGroup = nullifyIfEmpty(partnerGroup);
      partnerId = nullifyIfEmpty(partnerId);

      //Level 4: alertType,level4,docType,partnerId
      trigger = findAlertTrigger(AlertTrigger.LEVEL_4, alertType, docType,
                                 null, null, partnerId);
      if (trigger == null)
      {
        //Level 3: alertType,level3,docType,partnerType,partnerGroup
        trigger = findAlertTrigger(AlertTrigger.LEVEL_3, alertType, docType,
                                   partnerType, partnerGroup, null);
      }

      if (trigger == null)
      {
        //Level 2: alertType,level2,docType,partnerType
        trigger = findAlertTrigger(AlertTrigger.LEVEL_2, alertType, docType,
                                   partnerType, null, null);
      }

      if (trigger == null)
      {
        //Level 1: alertType,level1,docType
        trigger = findAlertTrigger(AlertTrigger.LEVEL_1, alertType, docType,
                                   null, null, null);
      }

      if (trigger == null)
      {
        //Level 0: alertType
        trigger = findAlertTrigger(AlertTrigger.LEVEL_0, alertType, null,
                                   null, null, null);
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    if (trigger == null)
    {
      throw NoMatchingAlertTriggerException.createEx(alertType, docType,
            partnerType, partnerGroup, partnerId);
    }
    return trigger;
  }

  /**
   * @see IGridTalkAlertManagerObj#findAlertTrigger(Integer,String,String,String,String,String)
   */
  public AlertTrigger findAlertTrigger(
    Integer level,
    String alertType,
    String docType,
    String partnerType,
    String partnerGroup,
    String partnerId)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getAlertTriggerFacadeLogger();
    String methodName   = "findAlertTrigger";
    Object[] params     = new Object[] {
                            level,
                            alertType,
                            docType,
                            partnerType,
                            partnerGroup,
                            partnerId};

    try
    {
      logger.logEntry(methodName, params);

      AlertTrigger found = null;

      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(null, AlertTrigger.LEVEL, filter.getEqualOperator(),
                             level, false);

      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.ALERT_TYPE,
                             filter.getEqualOperator(), alertType, false);

      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.DOC_TYPE,
                             filter.getEqualOperator(), docType, false);

      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_TYPE,
                             filter.getEqualOperator(), partnerType, false);

      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_GROUP,
                             filter.getEqualOperator(), partnerGroup, false);

      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_ID,
                             filter.getEqualOperator(), partnerId, false);

      // there should be only one Trigger with the same criteria fields.
      Collection results = findAlertTriggers(filter);
      if (!results.isEmpty())
        found = (AlertTrigger)results.iterator().next();

      return found;
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  private void validateRecipients(List recipients)
    throws IncorrectRecipientFormatException
  {
    List invalidList = new ArrayList();
    for (Iterator i=recipients.iterator(); i.hasNext(); )
    {
      String recpt = (String)i.next();
      if (!RecipientListData.isValidFieldFormat(recpt))
        invalidList.add(recpt);
    }
    if (!invalidList.isEmpty())
      throw IncorrectRecipientFormatException.createEx(invalidList);
  }

  private String nullifyIfEmpty(String val)
  {
    if (val != null && val.trim().length()==0)
      val = null;

    return val;
  }

  // ********************* Methods for EntityHandler

  private AlertTriggerEntityHandler getAlertTriggerEntityHandler()
  {
     return AlertTriggerEntityHandler.getInstance();
  }
}