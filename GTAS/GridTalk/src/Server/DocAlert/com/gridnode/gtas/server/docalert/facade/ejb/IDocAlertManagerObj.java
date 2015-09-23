/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocAlertManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 * Feb 26 2003    Neo Sok Lay         Add additional data providers for raising
 *                                    DocAlert.
 * Mar 10 2003    Neo Sok Lay         Allow receive confirmation alert to
 *                                    obtain the attachment using reflection.
 * Oct 26 2005    Neo Sok Lay         Business methods throws Throwable is not acceptable
 *                                    for SAP J2EE deployment                      
 * Nov 23 2005    Neo Sok Lay         Change to pass an IAttachmentProvider instead of 
 *                                    java.lang.reflect.Method for providing the attachment file.                                                
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import com.gridnode.gtas.server.docalert.exceptions.ResponseTrackingException;
import com.gridnode.gtas.server.docalert.helpers.IAttachmentProvider;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for DocAlertManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IDocAlertManagerObj
  extends        EJBObject
{
  // ********************* ResponseTrackRecord *********************

  /**
   * Create a new ResponseTrackRecord
   *
   * @param trackRecord The new ResponseTrackRecord entity to create.
   */
  public Long createResponseTrackRecord(ResponseTrackRecord trackRecord)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ResponseTrackRecord
   *
   * @param trackRecord The ResponseTrackRecord entity with changes.
   */
  public void updateResponseTrackRecord(ResponseTrackRecord trackRecord)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a ResponseTrackRecord.
   *
   * @param trackRecordUId The UID of the ResponseTrackRecord to delete.
   */
  public void deleteResponseTrackRecord(Long trackRecordUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a ResponseTrackRecord using the ResponseTrackRecord UID.
   *
   * @param trackRecordUId The UID of the ResponseTrackRecord to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists with that
   * UID.
   */
  public ResponseTrackRecord findResponseTrackRecord(Long trackRecordUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ResponseTrackRecord using the SentDocType Name.
   *
   * @param docTypeName The ResponseTrackRecord with the SentDocType to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists.
   */
  public ResponseTrackRecord findResponseTrackRecordBySentDocType(String docTypeName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ResponseTrackRecord using the SentDocType Name.
   *
   * @param docTypeName The ResponseTrackRecord with the ResponseDocType to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists.
   */
  public ResponseTrackRecord findResponseTrackRecordByResponseDocType(String docTypeName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ResponseTrackRecord that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ResponseTrackRecord found, or empty collection if none
   * exists.
   */
  public Collection findResponseTrackRecords(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ResponseTrackRecord that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of ResponseTrackRecord found, or empty collection if
   * none exists.
   */
  public Collection findResponseTrackRecordsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;


  // ************************* ReminderAlert **************************

  /**
   * Create a new ReminderAlert
   *
   * @param reminderAlert The new ReminderAlert entity to create.
   */
  public Long createReminderAlert(ReminderAlert reminderAlert)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ReminderAlert
   *
   * @param reminderAlert The ReminderAlert entity with changes.
   */
  public void updateReminderAlert(ReminderAlert reminderAlert)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a ReminderAlert.
   *
   * @param reminderAlertUId The UID of the ReminderAlert to delete.
   */
  public void deleteReminderAlert(Long reminderAlertUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a ReminderAlert using the ReminderAlert UID.
   *
   * @param reminderAlertUId The UID of the ReminderAlert to find.
   * @return The ReminderAlert found, or <B>null</B> if none exists with that
   * UID.
   */
  public ReminderAlert findReminderAlert(Long reminderAlertUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the earliest ReminderAlert for a ResponseTrackRecord.
   *
   * @param trackRecordUID The UID of the ResponseTrackRecord.
   * @return The ReminderAlert found, or <B>null</B> if none exists.
   */
  public ReminderAlert findEarliestReminderAlert(Long trackRecordUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the next ReminderAlert for a ResponseTrackRecord after a specified
   * current DaysToReminder.
   *
   * @param trackRecordUID The UID of the ResponseTrackRecord.
   * @param currDaysToReminder Current DaysToReminder. The retrieved ReminderAlert's
   * DaysToReminder must be larger than this value.
   * @return The ReminderAlert found, or <B>null</B> if none exists.
   */
  public ReminderAlert findNextReminderAlert(Long trackRecordUID, int currDaysToReminder)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the ReminderAlert for a ResponseTrackRecord.
   *
   * @param trackRecordUID UID of the ResponseTrackRecord.
   * @param daysToReminder DaysToReminder of the ReminderAlert
   * @return The ReminderAlert found, or <B>null</B> if none exists.
   * @throws FindEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public ReminderAlert findReminderAlert(Long trackRecordUID, int daysToReminder)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all ReminderAlert(s) for a ResponseTrackRecord.
   *
   * @param trackRecordUID The UID of the ResponseTrackRecord.
   * @return a Collection of ReminderAlert found, or empty collection if none
   * exists.
   */
  public Collection findReminderAlertsByTrackRecord(Long trackRecordUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ReminderAlert that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ReminderAlert found, or empty collection if none
   * exists.
   */
  public Collection findReminderAlerts(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ReminderAlert that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of ReminderAlert found, or empty collection if
   * none exists.
   */
  public Collection findReminderAlertsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ******************** ActiveTrackRecord ****************************

  /**
   * Find an ActiveTrackRecord.
   *
   * @param trackRecordUId The UID of the ResponseTrackRecord that the
   * ActiveTrackRecord is created from.
   * @param sentGDocUId UID of the sent GridDocument.
   * @param daysToReminder Number of days after sending that no response is
   * received to send reminder notification.
   * @return The ActiveTrackRecord found, or <B>null</B> if none exists with
   * the specified arguments.
   */
  public ActiveTrackRecord findActiveTrackRecord(
    Long trackRecordUId,
    Long sentGDocUId,
    int  daysToReminder)
    throws FindEntityException, SystemException, RemoteException;

  // ********************* Tracking Process ****************************

  /**
   * Start the Response tracking process for a sent document.
   *
   * @param sentDoc the sent document.
   */
  public void startResponseTracking(GridDocument sentDoc) throws ResponseTrackingException, SystemException, RemoteException;

  /**
   * Invalidate the response tracking process on receipt of a document. If
   * the document is a response document for a sent document being tracked for,
   * receive response alerts may be raised and the tracking process is deactivated.
   *
   * @param receivedDoc The received document.
   * @param attachmentProvider Provides the alert attachment file. Can be <b>null</b>
   * no attachment is required.
   */
//* @param getAttachmentMethod Method to be invoked to obtain the attachment
//* for alert if the receivedDoc requires confirmation alert. <b>null</b>
//* if no attachment is required.
//* @param methodTarget The Object upon which to invoke the <code>getAttachmentMethod</code>.
//* <b>null</b> if <code>getAttachmentMethod</code> is declared static. This
//* parameter is ignored if <code>getAttachmentMethod</code> is not specified.
  public void invalidateResponseTracking(
    GridDocument receivedDoc,
    IAttachmentProvider attachmentProvider) throws ResponseTrackingException, SystemException, RemoteException;
    //Method getAttachmentMethod,
    //Object methodTarget) throws ResponseTrackingException, SystemException, RemoteException;

  /**
   * Invalidate the response tracking process on receipt of a document. If
   * the document is a response document for a sent document being tracked for,
   * receive response alerts may be raised and the tracking process is deactivated.
   * Any attachment required will be determined from the configured ResponseTrackRecord
   * and the attachment will be defaulted to be for received user document if
   * required.
   *
   * @param receivedDoc The received document.
   */
  public void invalidateResponseTracking(
    GridDocument receivedDoc) throws ResponseTrackingException, SystemException, RemoteException;

  /**
   * It's time to raise reminder alert! After raising the reminder alert,
   * the next reminder alarm would be scheduled, if any.
   *
   * @param dueAlarmUID UID of the reminder alarm that is dued currently.
   * @param dueDate The date that the alarm is supposed to be dued.
   */
  public void timeForReminder(Long dueAlarmUID, Date dueDate) throws ResponseTrackingException, SystemException, RemoteException;

  // ****************** Raising Document related alerts ********************

  /**
   * Raising an alert using the GridDocument and User document as the data
   * providers.
   *
   * @param gdoc The GridDocument
   * @param recipientXpath Xpath for extracting email code for the recipients
   * of the alert. The email code (s) should point to Role (s) defined in the
   * GridTalk. <b>Null</b> if not applicable.
   * @param alertName Name of the alert to raise.
   * @param attachDoc <b>true</b> to attach the user document (if the alert results
   * in an email being sent), <b>false</b> otherwise.
   * @return The result of raising the alert -- each element represents the
   * result of each action performed.
   *
   */
//  public List raiseDocAlert(GridDocument gdoc,
//                              String recipientXpath,
//                              String alertName,
//                              boolean attachDoc);

  /**
   * Raise an alert using the GridDocument, and User document as the default
   * data providers. Additional data providers can be specified in the
   * <code>additionalProviders</code> field.
   *
   * @param gdoc The GridDocument
   * @param recipientXpath Xpath for extracting email code for the recipients
   * of the alert. The email code (s) should point to Role (s) defined in the
   * GridTalk. <b>Null</b> if not applicable.
   * @param alertName Name of the alert to raise.
   * @param attachDoc <b>true</b> to attach the user document (if the alert results
   * in an email being sent), <b>false</b> otherwise.
   * @param additionalProviders The additional data providers for the alert.
   * @return The result of raising the alert -- each element represents the
   * result of each action performed.
   *
   */
  public List raiseDocAlert(GridDocument gdoc,
                            String recipientXpath,
                            String alertName,
                            boolean attachDoc,
                            IDataProvider[] additionalProviders)
                            throws RemoteException;


  /**
   * Raise an alert regarding a document. Data providers can be specified in the
   * <code>providers</code> field. GridDocument and UserDocument data providers
   * will be constructed by this method.
   * <p>
   * The Alert to be raised will be determined by attributes of the <code>gdoc</code>
   * and the <code>alertType</code> and the AlertTrigger(s) configured.
   *
   * @param alertType The type of Alert to raise.
   * @param gdoc The GridDocument
   * @param providers The data providers for the alert (excluding GridDocument and
   * UserDocument).
   * @return The result of raising the alert -- each element represents the
   * result of each action performed.
   *
   */
  public List raiseDocAlert(String alertType,
                            GridDocument gdoc,
                            IDataProvider[] providers)
                            throws RemoteException;

  /**
   * Raise an alert regarding a document. Data providers can be specified in the
   * <code>providers</code> field. GridDocument and UserDocument data providers
   * will be constructed by this method.
   * <p>
   * The Alert to be raised will be determined by attributes of the <code>gdoc</code>
   * and the <code>alertType</code> and the AlertTrigger(s) configured.
   *
   * @param alertType The type of Alert to raise.
   * @param gdoc The GridDocument
   * @param attachmentProvider Provides the alert attachment file. Can be <b>null</b>
   * if no attachment is required.
   * @return The result of raising the alert -- each element represents the
   * result of each action performed.
   */
//* @param getAttachmentMethod Method to be invoked to obtain the attachment
//* for alert. <b>null</b> if no attachment is required.
//* @param methodTarget The Object upon which to invoke the <code>getAttachmentMethod</code>.
//* <b>null</b> if <code>getAttachmentMethod</code> is declared static. This
//* parameter is ignored if <code>getAttachmentMethod</code> is not specified.
  public List raiseDocAlert(String alertType,
                            GridDocument gdoc,
                            IAttachmentProvider attachmentProvider)
                            //Method getAttachmentMethod,
                            //Object methodTarget)
                            throws RemoteException;

  /**
   * Raise an alert regarding a document. Data providers can be specified in the
   * <code>providers</code> field. GridDocument and UserDocument data providers
   * will be constructed by this method.
   *
   * @param alertUID The alert to raise
   * @param gdoc The GridDocument
   * @param attachmentProvider Provides the alert attachment file. Can be <b>null</b> if
   * no attachment is required.
   * @param providers The data providers for the alert (excluding GridDocument and
   * UserDocument).
   * @return The result of raising the alert -- each element represents the
   * result of each action performed.
   */
//* @param getAttachmentMethod Method to be invoked to obtain the attachment
//* for alert. <b>null</b> if no attachment is required.
//* @param methodTarget The Object upon which to invoke the <code>getAttachmentMethod</code>.
//* <b>null</b> if <code>getAttachmentMethod</code> is declared static. This
//* parameter is ignored if <code>getAttachmentMethod</code> is not specified.
  public List raiseDocAlert(Long alertUID,
                            GridDocument gdoc,
                            IAttachmentProvider attachmentProvider,
//                            Method getAttachmentMethod,
//                            Object methodTarget,
                            IDataProvider[] providers)
                            throws RemoteException;

 }