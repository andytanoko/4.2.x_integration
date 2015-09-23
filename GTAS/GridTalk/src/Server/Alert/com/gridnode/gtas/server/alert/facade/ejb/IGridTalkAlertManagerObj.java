/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkAlertManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 21 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.alert.exceptions.NoMatchingAlertTriggerException;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for GridTalkAlertManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IGridTalkAlertManagerObj
       extends   EJBObject
{
  /**
   * Create a new AlertTrigger.
   *
   * @param trigger The AlertTrigger entity.
   */
  public Long createAlertTrigger(AlertTrigger trigger)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a AlertTrigger
   *
   * @param trigger The AlertTrigger entity with changes.
   */
  public void updateAlertTrigger(AlertTrigger trigger)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a AlertTrigger.
   *
   * @param triggerUId The UID of the AlertTrigger to delete.
   */
  public void deleteAlertTrigger(Long uid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a AlertTrigger using the AlertTrigger UID.
   *
   * @param triggerUId The UID of the AlertTrigger to find.
   * @return The AlertTrigger found, or <B>null</B> if none exists with that
   * UID.
   */
  public AlertTrigger findAlertTrigger(Long uid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of AlertTrigger that is under the same level.
   *
   * @param triggerLevel The level of the triggers to find.
   * @return a Collection of AlertTrigger found, or empty collection if none
   * exists.
   */
  public Collection findAlertTriggers(Integer triggerLevel)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of AlertTrigger(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of AlertTrigger(s) found, or empty collection if none
   * exists.
   */
  public Collection findAlertTriggers(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of AlertTrigger that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of AlertTrigger found, or empty collection if
   * none exists.
   */
  public Collection findAlertTriggersKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

 /**
   * Find an AlertTrigger that satisfy all the criteria specified.
   *
   * @param level The level of the AlertTrigger.
   * @param alertType The type of alert to trigger.
   * @param docType The Document type criteria.
   * @param partnerType The Partner type criteria.
   * @param partnerGroup The Partner group criteria.
   * @param partnerId The Partner Id criteria.
   * @return The AlertTrigger found, or <b>null</b> if none found.
   * @exception
   *
   * @since 2.1
   */
  public AlertTrigger findAlertTrigger(Integer level,
                                       String alertType,
                                       String docType,
                                       String partnerType,
                                       String partnerGroup,
                                       String partnerId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Progressive find the AlertTrigger from Level 4 - 0 based on the given alert
   * type, document type, partner type, partner group and partner id until a
   * match is found. If after Level 0 search, there is still no matching AlertTrigger,
   * A NoMatchingAlertTriggerException will be thrown.
   *
   * @param alertType The type of alert to trigger.
   * @param docType The document type of trigger criteria.
   * @param partnerType The partner type of trigger criteria.
   * @param partnerGroup The partner group of trigger criteria.
   * @param partnerId The partner id of trigger criteria.
   * @return The AlertTrigger found.
   * @throws NoMatchingAlertTriggerException If no matching AlertTrigger found
   * at the end of the progressive search.
   */
  public AlertTrigger progressiveFindAlertTrigger(String alertType,
                                                  String docType,
                                                  String partnerType,
                                                  String partnerGroup,
                                                  String partnerId)
    throws NoMatchingAlertTriggerException,
           FindEntityException,
           SystemException,
           RemoteException;

}