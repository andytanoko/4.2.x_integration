/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbortActivationHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Apr 28 2003    Neo Sok Lay         Notify when received activation abort (cancel)
 *                                    from partner.
 * Feb 05 2004    Neo Sok Lay         Add onAcknowledgementSent() -- to alert only
 *                                    after the postive acknowledgement of AbortActivation
 *                                    has been successfully sent. 
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.server.activation.exceptions.InvalidActivationRecordStateException;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;

import java.io.File;

/**
 * This class is a helper class for handling the submission and reception of
 * GridNode Activation Abort message.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class AbortActivationHandler
  extends    AbstractActivationProcessHandler
{
//  private static final Object _lock = new Object();
//  private static AbortActivationHandler _self = null;

  public AbortActivationHandler()
    throws Exception
  {
  }

//  public static final AbortActivationHandler getInstance()
//    throws Exception
//  {
//    if (_self == null)
//    {
//      synchronized (_lock)
//      {
//        if (_self == null)
//        {
//          _self = new AbortActivationHandler();
//        }
//      }
//    }
//
//    return _self;
//  }

  protected String readEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(ABORT_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Abort GridNode Activation configured!!");
    return eventID;

  }

  protected String readAckEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(ABORT_ACK_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Ack Abort GridNode Activation configured!!");
    return eventID;
  }

  /**
   * Abort an outgoing activation request.
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @throws InvalidActivationRecordStateException If the ActivationRecord is
   * not an outgoing activation request.
   */
  public void abortActivationRequest(Long recordUID)
    throws Throwable
  {
    ActivationRecord record = retrieveOutgoingActivationRecord(recordUID);

    SyncGridNode abortDetails =  createPayload();

    submit(record.getGridNodeID(), abortDetails, (Long)record.getKey(),
      ABORT_FILE_PREFIX);

    updateActivationRecordForAbort(record, false);
  }

  public void handleIncomingMessage(
    String refTransID, SyncGridNode message,
    String[] dataPayload, File[] filePayload)
    throws Exception
  {
    String status = null;
    String error   = null;
    String replyTo = null;
    Long recordUID = null;
    try
    {
      // check existing pending activation record.
      ActivationRecord record = retrieveIncomingActivationRecord(
                                  new Integer(message.getGridnode().getID()));
      updateActivationRecordForAbort(record, true);
      status = Boolean.TRUE.toString();
      replyTo = record.getGridNodeID().toString();

      recordUID = new Long(record.getUId());
    }
    catch (Throwable ex)
    {
      Logger.warn("[RequestActivationHandler.handleIncomingMessage] Error ", ex);
      status = Boolean.FALSE.toString();
      error = ex.getMessage();
    }
    // acknowledge
    acknowledge(replyTo, _ackEventID, refTransID, recordUID, null, new String[] {status, error});
  }

  /**
   * @see com.gridnode.gtas.server.activation.helpers.AbstractActivationProcessHandler#onAcknowledgementSent(com.gridnode.gtas.server.activation.model.ActivationRecord)
   */
  protected void onAcknowledgementSent(ActivationRecord record)
    throws Throwable
  {
    if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_ABORTION)
    {
      // notify (raise alert)
      notifyPartnerCancelledActivation(record.getActivationDetails().getRequestDetails().getGridnode());
    }
  }


  // *********************** Own methods ***********************************

  /**
   * Update the ActivationRecord for Aborted state.
   *
   * @param record The ActivationRecord.
   * @param transCompleted Whether to mark the transaction as completed.
   */
  private void updateActivationRecordForAbort(
    ActivationRecord record, boolean transCompleted) throws Throwable
  {
    record.setCurrentType(new Short(ActivationRecord.TYPE_ABORTION));
    record.setDTAborted(getCurrentTimestamp());
    record.setTransCompleted(transCompleted);

    ActivationRecordEntityHandler.getInstance().update(record);
  }

  /**
   * Retrieve an Outgoing Activation record with the specified UID.
   *
   * @param recordUID UID of the ActivationRecord.
   * @return The ActivationRecord retrieved, if any.
   * @throws InvalidActivationRecordStateException No such Outgoing Activation
   * record found.
   */
  private ActivationRecord retrieveOutgoingActivationRecord(Long recordUID)
    throws Throwable
  {
    ActivationRecord record = getActivationRecord(
                                recordUID,
                                null,
                                new Short(ActivationRecord.TYPE_ACTIVATION),
                                new Short(ActivationRecord.DIRECTION_OUTGOING),
                                null,
                                null);
    if (record == null)
       throw InvalidActivationRecordStateException.illegalCancellation(
             "No Outgoing Activation Record found!");

    return record;
  }

  /**
   * Retrieve an Incoming Activation record with the specified GridNode ID.
   *
   * @param gridnodeID GridNode ID of the ActivationRecord.
   * @return The ActivationRecord retrieved, if any.
   * @throws InvalidActivationRecordStateException No such Incoming Activation
   * record found.
   */
  private ActivationRecord retrieveIncomingActivationRecord(Integer gridnodeID)
    throws Throwable
  {
    ActivationRecord record = getActivationRecord(
                                null,
                                gridnodeID,
                                new Short(ActivationRecord.TYPE_ACTIVATION),
                                new Short(ActivationRecord.DIRECTION_INCOMING),
                                null,
                                null);
    if (record == null)
       throw InvalidActivationRecordStateException.illegalCancellation(
             "No Incoming Activation Record found!");

    return record;
  }

}