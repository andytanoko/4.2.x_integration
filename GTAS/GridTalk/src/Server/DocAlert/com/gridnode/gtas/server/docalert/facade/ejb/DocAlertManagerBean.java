/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocAlertManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 * Feb 26 2003    Neo Sok Lay         Add additional data providers for raising
 *                                    DocAlert.
 * Mar 10 2003    Neo Sok Lay         Allow receive confirmation alert to
 *                                    obtain the attachment using reflection.
 * Apr 02 2003    Neo Sok Lay         GNDB00013325: To delete the reminder alarm
 *                                    that has been raised.
 * Jun 06 2003    Neo Sok Lay         GNDB00014169: Enabled flag not checked before
 *                                    triggering alert.
 * Oct 26 2005    Neo Sok Lay         Business methods throws Throwable is not acceptable
 *                                    for SAP J2EE deployment            
 * Nov 23 2005    Neo Sok Lay         Use IAttachmentProvider to provide alert attachment file
 *                                    instead of method reflection invocation.                                                          
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.docalert.exceptions.ResponseTrackingException;
import com.gridnode.gtas.server.docalert.helpers.*;
import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;


/**
 * This bean provides the document alert notification services.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DocAlertManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1276704086030751818L;
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

  // ********************* ResponseTrackRecord *********************

  /**
   * Create a new ResponseTrackRecord
   *
   * @param trackRecord The new ResponseTrackRecord entity to create.
   */
  public Long createResponseTrackRecord(ResponseTrackRecord trackRecord)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "createResponseTrackRecord";
    Object[] params     = new Object[] {trackRecord};

    Long key = null;

    try
    {
      logger.logEntry(methodName, params);

      key = (Long)ResponseTrackRecordEntityHandler.getInstance().createEntity(
                  trackRecord).getKey();

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
   * Update a ResponseTrackRecord
   *
   * @param trackRecord The ResponseTrackRecord entity with changes.
   */
  public void updateResponseTrackRecord(ResponseTrackRecord trackRecord)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "updateResponseTrackRecord";
    Object[] params     = new Object[] {trackRecord};

    try
    {
      logger.logEntry(methodName, params);

      ResponseTrackRecordEntityHandler.getInstance().update(trackRecord);

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
   * Delete a ResponseTrackRecord.
   *
   * @param trackRecordUId The UID of the ResponseTrackRecord to delete.
   */
  public void deleteResponseTrackRecord(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "deleteResponseTrackRecord";
    Object[] params     = new Object[] {uid};

    try
    {
      logger.logEntry(methodName, params);

      ResponseTrackRecordEntityHandler.getInstance().remove(uid);

      // remove reminder alerts for track record
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
        filter.getEqualOperator(), uid, false);
      ReminderAlertEntityHandler.getInstance().removeByFilter(filter);

      // remove active track records for track record
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, ActiveTrackRecord.TRACK_RECORD_UID,
        filter.getEqualOperator(), uid, false);
      ActiveTrackRecordEntityHandler.getInstance().removeByFilter(filter);

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
   * Find a ResponseTrackRecord using the ResponseTrackRecord UID.
   *
   * @param trackRecordUId The UID of the ResponseTrackRecord to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists with that
   * UID.
   */
  public ResponseTrackRecord findResponseTrackRecord(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findResponseTrackRecord";
    Object[] params     = new Object[] {uid};

    ResponseTrackRecord record = null;

    try
    {
      logger.logEntry(methodName, params);

      record = (ResponseTrackRecord)ResponseTrackRecordEntityHandler.getInstance(
                ).getEntityByKeyForReadOnly(uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return record;
  }

  /**
   * Find a ResponseTrackRecord using the SentDocType Name.
   *
   * @param docTypeName The ResponseTrackRecord with the SentDocType to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists.
   */
  public ResponseTrackRecord findResponseTrackRecordBySentDocType(String docTypeName)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findResponseTrackRecordBySentDocType";
    Object[] params     = new Object[] {docTypeName};

    ResponseTrackRecord record = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResponseTrackRecord.SENT_DOC_TYPE,
                             filter.getEqualOperator(), docTypeName, false);

      Collection results = findResponseTrackRecords(filter);
      if (!results.isEmpty())
        record = (ResponseTrackRecord)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return record;
  }

  /**
   * Find a ResponseTrackRecord using the SentDocType Name.
   *
   * @param docTypeName The ResponseTrackRecord with the ResponseDocType to find.
   * @return The ResponseTrackRecord found, or <B>null</B> if none exists.
   */
  public ResponseTrackRecord findResponseTrackRecordByResponseDocType(String docTypeName)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findResponseTrackRecordByResponseDocType";
    Object[] params     = new Object[] {docTypeName};

    ResponseTrackRecord record = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ResponseTrackRecord.RESPONSE_DOC_TYPE,
                             filter.getEqualOperator(), docTypeName, false);

      Collection results = findResponseTrackRecords(filter);
      if (!results.isEmpty())
        record = (ResponseTrackRecord)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return record;
  }

  /**
   * Find a number of ResponseTrackRecord that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ResponseTrackRecord found, or empty collection if none
   * exists.
   */
  public Collection findResponseTrackRecords(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findResponseTrackRecords";
    Object[] params     = new Object[] {filter};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      results = ResponseTrackRecordEntityHandler.getInstance().getEntityByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Find a number of ResponseTrackRecord that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of ResponseTrackRecord found, or empty collection if
   * none exists.
   */
  public Collection findResponseTrackRecordsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findResponseTrackRecordsKeys";
    Object[] params     = new Object[] {filter};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      results = ResponseTrackRecordEntityHandler.getInstance().getKeyByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }


  // ************************* ReminderAlert **************************

  /**
   * Create a new ReminderAlert
   *
   * @param reminderAlert The new ReminderAlert entity to create.
   */
  public Long createReminderAlert(ReminderAlert reminderAlert)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "createReminderAlert";
    Object[] params     = new Object[] {reminderAlert};

    Long key = null;

    try
    {
      logger.logEntry(methodName, params);

      // check ResponseTrackRecord ??

      key = (Long)ReminderAlertEntityHandler.getInstance().createEntity(
                  reminderAlert).getKey();

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
   * Update a ReminderAlert
   *
   * @param reminderAlert The ReminderAlert entity with changes.
   */
  public void updateReminderAlert(ReminderAlert reminderAlert)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "updateReminderAlert";
    Object[] params     = new Object[] {reminderAlert};

    try
    {
      logger.logEntry(methodName, params);

      ReminderAlertEntityHandler.getInstance().update(reminderAlert);
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
   * Delete a ReminderAlert.
   *
   * @param reminderAlertUId The UID of the ReminderAlert to delete.
   */
  public void deleteReminderAlert(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "deleteReminderAlert";
    Object[] params     = new Object[] {uid};

    try
    {
      logger.logEntry(methodName, params);

      ReminderAlertEntityHandler.getInstance().remove(uid);
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
   * Find a ReminderAlert using the ReminderAlert UID.
   *
   * @param reminderAlertUId The UID of the ReminderAlert to find.
   * @return The ReminderAlert found, or <B>null</B> if none exists with that
   * UID.
   */
  public ReminderAlert findReminderAlert(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findReminderAlert";
    Object[] params     = new Object[] {uid};

    ReminderAlert reminderAlert = null;

    try
    {
      logger.logEntry(methodName, params);

      reminderAlert = (ReminderAlert)ReminderAlertEntityHandler.getInstance(
                ).getEntityByKeyForReadOnly(uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return reminderAlert;
  }

  /**
   * Find the earliest ReminderAlert for a ResponseTrackRecord.
   *
   * @param trackRecordUID The UID of the ResponseTrackRecord.
   * @return The ReminderAlert found, or <B>null</B> if none exists.
   */
  public ReminderAlert findEarliestReminderAlert(Long trackRecordUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findEarliestReminderAlert";
    Object[] params     = new Object[] {trackRecordUID};

    ReminderAlert reminderAlert = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
                             filter.getEqualOperator(), trackRecordUID, false);
      filter.setOrderFields(new Object[] {ReminderAlert.DAYS_TO_REMINDER});

      Collection results = findReminderAlertsKeys(filter);
      if (!results.isEmpty())
        reminderAlert = findReminderAlert((Long)results.toArray()[0]);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return reminderAlert;
  }

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
    throws FindEntityException, SystemException
  {
    Integer daysToReminder = new Integer(currDaysToReminder);
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findNextReminderAlert";
    Object[] params     = new Object[] {
                              trackRecordUID,
                              daysToReminder,
                          };

    ReminderAlert reminderAlert = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
                             filter.getEqualOperator(), trackRecordUID, false);
      filter.addSingleFilter(filter.getAndConnector(), ReminderAlert.DAYS_TO_REMINDER,
                             filter.getGreaterOperator(), daysToReminder, false);
      filter.setOrderFields(new Object[] {ReminderAlert.DAYS_TO_REMINDER});

      Collection results = findReminderAlerts(filter);
      if (!results.isEmpty())
        reminderAlert = (ReminderAlert)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return reminderAlert;
  }

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
    throws FindEntityException, SystemException
  {
    Integer integerDTR = new Integer(daysToReminder);
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findReminderAlert";
    Object[] params     = new Object[] {
                              trackRecordUID,
                              integerDTR,
                          };

    ReminderAlert reminderAlert = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
                             filter.getEqualOperator(), trackRecordUID, false);
      filter.addSingleFilter(filter.getAndConnector(), ReminderAlert.DAYS_TO_REMINDER,
                             filter.getEqualOperator(), integerDTR, false);

      Collection results = findReminderAlerts(filter);
      if (!results.isEmpty())
        reminderAlert = (ReminderAlert)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return reminderAlert;
  }

  /**
   * Find all ReminderAlert(s) for a ResponseTrackRecord. The ReminderAlerts
   * are ordered by the DaysToReminder.
   *
   * @param trackRecordUID The UID of the ResponseTrackRecord.
   * @return a Collection of ReminderAlert found, or empty collection if none
   * exists.
   */
  public Collection findReminderAlertsByTrackRecord(Long trackRecordUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findReminderAlertsByTrackRecord";
    Object[] params     = new Object[] {trackRecordUID};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
                             filter.getEqualOperator(), trackRecordUID, false);
      filter.setOrderFields(new Object[] {ReminderAlert.DAYS_TO_REMINDER});

      results = findReminderAlerts(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Find a number of ReminderAlert that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ReminderAlert found, or empty collection if none
   * exists.
   */
  public Collection findReminderAlerts(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findReminderAlerts";
    Object[] params     = new Object[] {filter};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      results = ReminderAlertEntityHandler.getInstance().getEntityByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Find a number of ReminderAlert that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of ReminderAlert found, or empty collection if
   * none exists.
   */
  public Collection findReminderAlertsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findReminderAlertsKeys";
    Object[] params     = new Object[] {filter};

    Collection results = null;

    try
    {
      logger.logEntry(methodName, params);

      results = ReminderAlertEntityHandler.getInstance().getKeyByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

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
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findActiveTrackRecord";
    Object[] params     = new Object[]
                          {
                            trackRecordUId,
                            sentGDocUId,
                            String.valueOf(daysToReminder),
                          };

    ActiveTrackRecord rec = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ActiveTrackRecord.TRACK_RECORD_UID,
        filter.getEqualOperator(), trackRecordUId, false);
      filter.addSingleFilter(filter.getAndConnector(),
        ActiveTrackRecord.SENT_GRIDDOC_UID, filter.getEqualOperator(),
        sentGDocUId, false);
      filter.addSingleFilter(filter.getAndConnector(),
        ActiveTrackRecord.DAYS_TO_REMINDER, filter.getEqualOperator(),
        new Integer(daysToReminder), false);

      Collection results = ActiveTrackRecordEntityHandler.getInstance().
                             getEntityByFilterForReadOnly(filter);
      if (!results.isEmpty())
        rec = (ActiveTrackRecord)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return rec;
  }

  // ********************* Tracking Process ****************************

  /**
   * Start the Response tracking process for a sent document.
   *
   * @param sentDoc the sent document.
   */
  public void startResponseTracking(GridDocument sentDoc) throws ResponseTrackingException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "startResponseTracking";
    Object[] params     = new Object[] {sentDoc};

    try
    {
      logger.logEntry(methodName, params);

      ResponseTrackRecord trackRecord = findResponseTrackRecordBySentDocType(
                                          sentDoc.getUdocDocType());
      if (trackRecord != null)
      {
        // start tracking
        Long trackRecordUID = (Long)trackRecord.getKey();

        ReminderAlert reminderAlert = findEarliestReminderAlert(trackRecordUID);
        if (reminderAlert != null)
        {
          // determine start track date
          Date startTrackDate = DocExtractionHelper.determineTrackingDate(
                                  trackRecord,
                                  reminderAlert,
                                  sentDoc);
          // schedule alarm
          Long alarmUID = (Long)AlarmScheduler.scheduleReminderAlarm(
                             startTrackDate).getKey();
          Long sentGridDocUID = (Long)sentDoc.getKey();
          int daysToReminder = reminderAlert.getDaysToReminder();

          // create ActiveTrackRecord
          ActiveTrackRecordEntityHandler.getInstance().createActiveTrackRecord(
            trackRecordUID,
            alarmUID,
            sentGridDocUID,
            daysToReminder);
        }

      }
    }
    catch (ResponseTrackingException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);

      throw new ResponseTrackingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

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
    IAttachmentProvider attachmentProvider) throws ResponseTrackingException, SystemException
//    Method getAttachmentMethod,
//    Object methodTarget) throws ResponseTrackingException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "invalidateResponseTracking";
    //Object[] params     = new Object[] {receivedDoc, getAttachmentMethod, methodTarget};
    Object[] params     = new Object[] {receivedDoc, attachmentProvider};

    try
    {
      logger.logEntry(methodName, params);

      ResponseTrackRecord trackRecord = findResponseTrackRecordByResponseDocType(
                                          receivedDoc.getUdocDocType());
      if (trackRecord != null)
      {
        deactivateTrackRecord(trackRecord, receivedDoc);

        // send receive response alert
        AlertHelper.raiseReceiveResponseAlert(receivedDoc,
                                              trackRecord.getAlertRecipientXpath(),
                                              trackRecord.getReceiveResponseAlert(),
                                              attachmentProvider);
                                              //getAttachmentMethod,
                                              //methodTarget);
      }
    }
    catch (ResponseTrackingException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);

      throw new ResponseTrackingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  //new method for invalidating response tracking only
  //raise alert not done here
  /**
   * @see IDocAlertManagerObj#invalidateResponseTracking(GridDocument)
   */
  public void invalidateResponseTracking(GridDocument receivedDoc)
    throws ResponseTrackingException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "invalidateResponseTracking";
    Object[] params     = new Object[] {receivedDoc};

    try
    {
      logger.logEntry(methodName, params);

      ResponseTrackRecord trackRecord = findResponseTrackRecordByResponseDocType(
                                          receivedDoc.getUdocDocType());
      if (trackRecord != null)
      {
        deactivateTrackRecord(trackRecord, receivedDoc);

        /*
        Method attachmentMethod = null;
        if (trackRecord.isAttachResponseDoc())
        {
          attachmentMethod = AlertHelper.getDefaultAttachmentMethod();
        }*/
        //NSL20051123
        IAttachmentProvider attachmentProvider = null;
        if (trackRecord.isAttachResponseDoc())
        {
          attachmentProvider = AlertHelper.getDefaultAttachmentProvider();
        }

        // send receive response alert
        AlertHelper.raiseReceiveResponseAlert(receivedDoc,
                                              trackRecord.getAlertRecipientXpath(),
                                              trackRecord.getReceiveResponseAlert(),
                                              //attachmentMethod,
                                              //null);
                                              attachmentProvider);
      }
    }
    catch (ResponseTrackingException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);

      throw new ResponseTrackingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  private void deactivateTrackRecord(ResponseTrackRecord trackRecord,
                                     GridDocument receivedDoc)
                                     throws Throwable
  {
    Long trackRecordUID = (Long)trackRecord.getKey();

    // cancel scheduled reminder alarm
    Collection activeTrackRecords =
        ActiveTrackRecordEntityHandler.getInstance().findActiveTrackRecords(
        trackRecordUID);

    if (!activeTrackRecords.isEmpty())
    {
      String receivedDocId = DocExtractionHelper.getDocumentIdentifier(
                               trackRecord.getResponseDocIdXpath(),
                               receivedDoc);

      ActiveTrackRecord foundATR = null;

      for (Iterator i=activeTrackRecords.iterator(); i.hasNext() && foundATR == null; )
      {
        ActiveTrackRecord atr = (ActiveTrackRecord)i.next();
        if (DocExtractionHelper.checkDocumentIdentifier(trackRecord,
                                                        atr,
                                                        receivedDocId))
        {
          foundATR = atr;
        }
      }

      if (foundATR != null)
      {
        ActiveTrackRecordEntityHandler.getInstance().remove(
          (Long)foundATR.getKey());
        AlarmScheduler.cancelReminderAlarm(foundATR.getAlarmUID());
      }
    }
  }

  /**
   * It's time to raise reminder alert! After raising the reminder alert,
   * the next reminder alarm would be scheduled, if any.
   */
  public void timeForReminder(Long dueAlarmUID, Date dueDate) throws ResponseTrackingException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "timeForReminder";
    Object[] params     = new Object[] {dueAlarmUID, dueDate};

    try
    {
      logger.logEntry(methodName, params);

      ActiveTrackRecord activeTrackRecord =
        ActiveTrackRecordEntityHandler.getInstance().findActiveTrackRecordByAlarm(
        dueAlarmUID);

      if (activeTrackRecord != null)
      {
        GridDocument sentGdoc = DocExtractionHelper.findGridDocument(
                                  activeTrackRecord.getSentGridDocUID());
        if (sentGdoc == null)
        {
          logger.logMessage(methodName, params,
                            "Sent GridDoc "+activeTrackRecord.getSentGridDocUID() +
                            " not found to raise reminder alert! ActiveTrackRecord will be deleted.");
          ActiveTrackRecordEntityHandler.getInstance().remove(
            (Long)activeTrackRecord.getKey());
        }
        else
        {
          ReminderAlert reminderAlert = findReminderAlert(
                                          activeTrackRecord.getTrackRecordUID(),
                                          activeTrackRecord.getDaysToReminder());
          if (reminderAlert == null)
          {
            logger.logMessage(methodName, params,
                              "No ReminderAlert found for ActiveTrackRecord: "+
                              activeTrackRecord.getEntityDescr());
          }
          else
          {
            AlertHelper.raiseReminderAlert(sentGdoc,
                                           reminderAlert.getDocRecipientXpath(),
                                           reminderAlert.getDocSenderXpath(),
                                           reminderAlert.getAlertToRaise());
          }
          // set next reminder scheduler
          scheduleNextReminder(activeTrackRecord, dueDate);
        }
        // remove the alarm that has been raised
        // NOTE: There is a racing condition. Time module is updating the
        // iCalAlarm for next due time when invoking the TimeInvokedMDBean.
        // If cancel is done first, it may cause error if it cannot find the
        // record to update.
        // hopefully, this is a low chance of happening.
        AlarmScheduler.cancelReminderAlarm(dueAlarmUID);
      }
    }
    catch (ResponseTrackingException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);

      throw new ResponseTrackingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }


  /**
   * Schedule the alarm for raising next ReminderAlert.
   *
   * @param activeTrackRecord The current ActiveTrackRecord
   * @param currDueAlarmDate The due date of the current alarm that runs off
   */
  private void scheduleNextReminder(ActiveTrackRecord activeTrackRecord,
                                   Date currDueAlarmDate) throws Throwable
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "scheduleNextReminder";
    Object[] params     = new Object[] {activeTrackRecord, currDueAlarmDate};

    try
    {
      logger.logEntry(methodName, params);

      ReminderAlert nextReminderAlert = findNextReminderAlert(
                                          activeTrackRecord.getTrackRecordUID(),
                                          activeTrackRecord.getDaysToReminder());

      if (nextReminderAlert == null)
      {
        logger.logMessage(methodName, params,
                          "No configured next ReminderAlert. "+
                          "End response tracking process for GridDoc "+
                          activeTrackRecord.getSentGridDocUID());
        ActiveTrackRecordEntityHandler.getInstance().remove(
          (Long)activeTrackRecord.getKey());
      }
      else
      {
        int daysToNextReminder = nextReminderAlert.getDaysToReminder() -
                                 activeTrackRecord.getDaysToReminder();

        Date nextDueDate = DateUtil.rollDate(currDueAlarmDate, daysToNextReminder);

        // schedule alarm
        Long nextAlarmUID = (Long)AlarmScheduler.scheduleReminderAlarm(
                           nextDueDate).getKey();

        // update ActiveTrackRecord
        activeTrackRecord.setAlarmUID(nextAlarmUID);
        activeTrackRecord.setDaysToReminder(nextReminderAlert.getDaysToReminder());
        ActiveTrackRecordEntityHandler.getInstance().update(activeTrackRecord);
       }
    }
    catch (ResponseTrackingException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);

      throw new ResponseTrackingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

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
   */
//  public List raiseDocAlert(GridDocument gdoc,
//                              String recipientXpath,
//                              String alertName,
//                              boolean attachDoc)
//  {
//    FacadeLogger logger = Logger.getManagerFacadeLogger();
//    String methodName   = "raiseDocAlert";
//    Object[] params     = new Object[] {
//                              gdoc,
//                              recipientXpath,
//                              alertName,
//                              attachDoc?Boolean.TRUE:Boolean.FALSE};
//
//    List result = null;
//    try
//    {
//      logger.logEntry(methodName, params);
//
//      result = AlertHelper.raiseDocAlert(gdoc, recipientXpath, alertName, attachDoc);
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//    return result;
//  }

  /**
   * @see IDocAlertManagerObj#raiseDocAlert(GridDocument,String,String,boolean,IDataProvider[])
   */
  public List raiseDocAlert(GridDocument gdoc,
                                   String recipientXpath,
                                   String alertName,
                                   boolean attachDoc,
                                   IDataProvider[] additionalProviders)
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "raiseDocAlert";
    Object[] params     = new Object[] {
                              gdoc,
                              recipientXpath,
                              alertName,
                              attachDoc?Boolean.TRUE:Boolean.FALSE,
                              additionalProviders};

    List result = null;
    try
    {
      logger.logEntry(methodName, params);

      result = AlertHelper.raiseDocAlert(gdoc, recipientXpath, alertName,
                 attachDoc, additionalProviders);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return result;
  }

  /**
   * @see IDocAlertManagerObj#raiseDocAlert(Long,GridDocument,IAttachmentProvider,Object,IDataProvider[])
   */
  public List raiseDocAlert(Long alertUID,
                            GridDocument gdoc,
                            //Method getAttachmentMethod,
                            //Object methodTarget,
                            IAttachmentProvider attachmentProvider,
                            IDataProvider[] providers)
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "raiseDocAlert";
    Object[] params     = new Object[] {
                              alertUID,
                              gdoc,
                              //getAttachmentMethod,
                              //methodTarget,
                              attachmentProvider,
                              providers};

    List result = null;
    try
    {
      logger.logEntry(methodName, params);

      result = AlertHelper.raiseAlert(alertUID, gdoc, providers,
                                      attachmentProvider, null);
                 //getAttachmentMethod, methodTarget, null);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return result;
  }

  /**
   * @see IDocAlertManagerObj#raiseDocAlert(String,GridDocument,IDataProvider[])
   */
  public List raiseDocAlert(String alertType,
                            GridDocument gdoc,
                            IDataProvider[] providers)
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "raiseDocAlert";
    Object[] params     = new Object[] {alertType, gdoc, providers};

    List result = null;
    try
    {
      logger.logEntry(methodName, params);

      AlertTriggerCriteria criteria = AlertTriggerCriteria.getCriteria(gdoc);

      // search alert trigger (level 0, alert type)
      AlertTrigger trigger = ServiceLookupHelper.getGridTalkAlertMgr().progressiveFindAlertTrigger(
                               alertType, criteria.getDocType(), criteria.getPartnerType(),
                               criteria.getPartnerGroup(), criteria.getPartnerID());
      if (trigger != null && trigger.isEnabled())
      {
        result = AlertHelper.raiseAlert(trigger.getAlertUID(),
                                        gdoc,
                                        providers,
                                        trigger.isAttachDoc(),
                                        trigger.getRecipients());
      }
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return result;
  }

  /**
   * @see IDocAlertManagerObj#raiseDocAlert(String,GridDocument,IAttachmentProvider)
   */
  public List raiseDocAlert(String alertType,
                            GridDocument gdoc,
                            IAttachmentProvider attachmentProvider)
                            //Method getAttachmentMethod,
                            //Object methodTarget)
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "raiseDocAlert";
    //Object[] params     = new Object[] {alertType, gdoc, getAttachmentMethod, methodTarget};
    Object[] params     = new Object[] {alertType, gdoc, attachmentProvider};

    List result = null;
    try
    {
      logger.logEntry(methodName, params);


      AlertTriggerCriteria criteria = AlertTriggerCriteria.getCriteria(gdoc);

      // search alert trigger (level 0, alert type)
      AlertTrigger trigger = ServiceLookupHelper.getGridTalkAlertMgr().progressiveFindAlertTrigger(
                               alertType, criteria.getDocType(), criteria.getPartnerType(),
                               criteria.getPartnerGroup(), criteria.getPartnerID());
      if (trigger != null && trigger.isEnabled())
      {
        result = AlertHelper.raiseAlert(trigger.getAlertUID(),
                                        gdoc,
                                        null,
                                        //getAttachmentMethod,
                                        //methodTarget,
                                        attachmentProvider,
                                        trigger.getRecipients());
      }
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return result;
  }
}