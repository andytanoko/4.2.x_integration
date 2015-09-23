/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RequestActivationHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 * Apr 28 2003    Neo Sok Lay         Notify when received activation from
 *                                    partner.
 * Feb 05 2004    Neo Sok Lay         Add onAcknowledgementSent() -- to alert
 *                                    activation request received only after
 *                                    postive acknowledgement has been successfully
 *                                    sent.
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.server.activation.exceptions.InvalidActivationRecordStateException;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.activation.model.GridNodeActivation;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;

import java.io.File;
import java.util.Collection;

/**
 * This class is a helper class for handling the submission and reception of
 * GridNode Activation Request.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class RequestActivationHandler
  extends    AbstractActivationProcessHandler
{

  public RequestActivationHandler()
    throws Exception
  {
  }

  protected String readEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(REQUEST_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Request GridNode Activation configured!!");
    return eventID;

  }

  protected String readAckEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(REQUEST_ACK_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Ack Request GridNode Activation configured!!");
    return eventID;
  }

  /**
   * Submits an Activation request to a potential partner.
   *
   * @param gridnodeID ID of the GridNode to activate.
   * @param gridnodeName Name of the GridNode to activate.
   * @param activateReason The reason for activation
   * @param requestorBeUIDs The UIDs of the BusinessEntity(s) to trade.
   */
  public void submitActivationRequest(
    Integer gridnodeID, String gridnodeName, String activateReason,
    Collection requestorBeUIDs) throws Throwable
  {
    checkCanProceed(gridnodeID);

    SyncGridNode requestDetails = createPayload(activateReason, requestorBeUIDs);

    updateLatestRecord(gridnodeID);

    ActivationRecord record = createOutgoingActivationRecord(
                                   requestDetails, gridnodeID, gridnodeName);

    submit(record.getGridNodeID(), requestDetails, (Long)record.getKey(),
      REQUEST_FILE_PREFIX);
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
      // check no existing approved or pending activation record.
      Integer gridnodeID = new Integer(message.getGridnode().getID());
      checkCanProceed(gridnodeID);
      updateLatestRecord(gridnodeID);
      ActivationRecord record = createIncomingActivationRecord(message); //, refTransID);
      status = Boolean.TRUE.toString();
      replyTo = gridnodeID.toString();
  
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
    if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_ACTIVATION)
    {
      // notify (raise alert)
      notifyPartnerRequestedActivation(record.getActivationDetails().getRequestDetails().getGridnode());
    }
  }

  // *********************** Own methods ***********************************

  /**
   * Check whether the activation request process can proceed for the specified
   * Gridnode
   *
   * @param gridnodeID The ID of the GridNode.
   * @throws InvalidActivationRecordsStateException Activation is already in
   * progress or has been approved.
   */
  private void checkCanProceed(Integer gridnodeID) throws Throwable
  {
    ActivationRecord record = getActivationRecord(
                                null, gridnodeID, null, null, null, null);
    if (record != null)
    {
      // if existing already approved or in progress, not allowed to submit again.
      if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_APPROVAL ||
          record.getCurrentType().shortValue() == ActivationRecord.TYPE_ACTIVATION)
         throw InvalidActivationRecordStateException.illegalActivation(
               "Activation is already in progress or has completed!");
    }
  }

  /**
   * Update the current Latest ActivationRecord for a GridNode, if any, to
   * not the latest in order to prepare for the new ActivationRecord to be created
   * in the next step.
   *
   * @param gridnodeID The GridNode ID.
   */
  private void updateLatestRecord(Integer gridnodeID) throws Throwable
  {
    ActivationRecord record = getActivationRecord(
                                null,
                                gridnodeID,
                                null,
                                null,
                                null,
                                null);

    if (record != null)
    {
      record.setLatest(false);
      ActivationRecordEntityHandler.getInstance().update(record);
    }
  }

  /**
   * Create a new Outgoing ActivationRecord.
   *
   * @param requestDetails The requestDetails for the new record
   * @param actGridnodeID the ID of the GridNode to activate
   * @param actGridnodeName The Name of the GridNode to activate
   */
  private ActivationRecord createOutgoingActivationRecord(
    SyncGridNode requestDetails, Integer actGridnodeID, String actGridnodeName)
    throws Throwable
  {
    GridNodeActivation actDetails = new GridNodeActivation();
    actDetails.setActivateReason(requestDetails.getGridnode().getActivationReason());
    actDetails.setRequestDetails(requestDetails);

    ActivationRecord actRecord = new ActivationRecord();
    actRecord.setActivateDirection(new Short(ActivationRecord.DIRECTION_OUTGOING));
    actRecord.setCurrentType(new Short(ActivationRecord.TYPE_ACTIVATION));
    actRecord.setDTRequested(getCurrentTimestamp());
    actRecord.setGridNodeID(actGridnodeID);
    actRecord.setGridNodeName(actGridnodeName);
    actRecord.setLatest(true);
    actRecord.setActivationDetails(actDetails);
    actRecord.setTransCompleted(false);

    actRecord = (ActivationRecord)ActivationRecordEntityHandler.getInstance(
                    ).createEntity(actRecord);

Logger.debug("**** RequestActivationHandler.createOutgoingActivationRecord()"
 + " actRecord = "+actRecord);
    return actRecord;
  }

  /**
   * Create a new Incoming ActivationRecord.
   *
   * @param requestDetails The requestDetails for the new record
   */
  private ActivationRecord createIncomingActivationRecord(
    SyncGridNode requestDetails) //, String refTransID)
    throws Throwable
  {
    GridNodeActivation actDetails = new GridNodeActivation();
    actDetails.setActivateReason(requestDetails.getGridnode().getActivationReason());
    actDetails.setRequestDetails(requestDetails);

    ActivationRecord actRecord = new ActivationRecord();
    actRecord.setActivateDirection(new Short(ActivationRecord.DIRECTION_INCOMING));
    actRecord.setActivationDetails(actDetails);
    actRecord.setCurrentType(new Short(ActivationRecord.TYPE_ACTIVATION));
    actRecord.setDTRequested(getCurrentTimestamp());
    actRecord.setGridNodeID(new Integer(requestDetails.getGridnode().getID()));
    actRecord.setGridNodeName(requestDetails.getGridnode().getName());
    actRecord.setLatest(true);
    actRecord.setTransCompleted(true);

    actRecord = (ActivationRecord)ActivationRecordEntityHandler.getInstance(
                    ).createEntity(actRecord);

    return actRecord;
  }

}