/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReplyActivationHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 * Apr 28 2003    Neo Sok Lay         Notify when received reply (approve/reject)
 *                                    from partner.
 * Aug 04 2003    Neo Sok Lay         Pass additional parameter when notifying
 *                                    activation to indicate whether this GridTalk
 *                                    is participating as the approving role.
 * Jan 02 2004    Neo Sok Lay         Set Certificate file on SyncGridNodeDelegate
 *                                    before synchronizing the approved GridNode.
 * Feb 04 2004    Neo Sok Lay         GNDB00017037: Notify activation only after the 
 *                                    acknowledgement is sent. 
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.activation.exceptions.InvalidActivationRecordStateException;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.enterprise.sync.handlers.SyncGridNodeDelegate;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.gtas.server.gridnode.model.GridNode;

import java.io.File;
import java.util.Collection;

/**
 * This class is a helper class for handling the submission and reception of
 * GridNode Activation Reply.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class ReplyActivationHandler extends AbstractActivationProcessHandler
{
  public ReplyActivationHandler() throws Exception
  {
  }

  protected String readEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(REPLY_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Reply GridNode Activation configured!!");
    return eventID;

  }

  protected String readAckEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(REPLY_ACK_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Ack Reply GridNode Activation configured!!");
    return eventID;
  }

  /**
   * Approve an incoming activation request.
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @throws InvalidActivationRecordStateException If the ActivationRecord is
   * not an incoming activation request.
   */
  public void approveActivationRequest(
    Long recordUID,
    Collection approverBeUIDs)
    throws Throwable
  {
    ActivationRecord record = retrieveIncomingActivationRecord(recordUID);

    SyncGridNode approveDetails = createPayload(null, approverBeUIDs);

    submit(
      record.getGridNodeID(),
      approveDetails,
      Boolean.TRUE,
      (Long) record.getKey(),
      APPROVE_FILE_PREFIX);

    /**@todo to update only after ack received?? */
    updateActivationRecordForApproval(record, approveDetails, false);
  }

  /**
   * Deny an incoming activation request.
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @throws InvalidActivationRecordStateException If the ActivationRecord is
   * not an incoming activation request.
   */
  public void denyActivationRequest(Long recordUID) throws Throwable
  {
    ActivationRecord record = retrieveIncomingActivationRecord(recordUID);

    SyncGridNode denyDetails = createPayload();

    submit(
      record.getGridNodeID(),
      denyDetails,
      Boolean.FALSE,
      (Long) record.getKey(),
      DENY_FILE_PREFIX);

    /**@todo to update only after ack received?? */
    updateActivationRecordForDenial(record, false);
  }

  public void handleIncomingMessage(
    String refTransID,
    SyncGridNode message,
    String[] dataPayload,
    File[] filePayload)
    throws Exception
  {
    String status = null;
    String error = null;
    String replyTo = null;
    Long recordUID = null;
    try
    {
      // check existing pending activation record.
      ActivationRecord record =
        retrieveOutgoingActivationRecord(
          new Integer(message.getGridnode().getID()));

      Boolean approve = new Boolean(dataPayload[0]);
      if (approve.booleanValue())
      {
        updateActivationRecordForApproval(record, message, true);
        record.getActivationDetails().setApproverBesState(
          IBusinessEntity.STATE_ACTIVE);
        record.getActivationDetails().populateBeLists();
        approve(record, message, filePayload[1]);
      }
      else
      {
        updateActivationRecordForDenial(record, true);
      }

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
    finally
    {
      // acknowledge, may be redundant if the error happens during notification...
      acknowledge(
        replyTo,
        _ackEventID,
        refTransID,
        recordUID,
        null,
        new String[] { status, error });
    }
  }

  protected void onTransCompleted(
    ActivationRecord record,
    File[] filesReceived)
    throws Throwable
  {
    if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_APPROVAL)
    {
      if (filesReceived == null || filesReceived.length != 1)
        throw new IllegalArgumentException("No Partner Certificate file returned!");

      record.getActivationDetails().setRequestorBesState(
        IBusinessEntity.STATE_ACTIVE);
      record.getActivationDetails().populateBeLists();
      approve(
        record,
        record.getActivationDetails().getRequestDetails(),
        filesReceived[0]);
      // internal notification on approval  
      notifyActivation(
        record,
        record.getActivationDetails().getRequestDetails(),
        record.getActivationDetails().getApproverBeList());
    }
  }

  protected void onAcknowledgementSent(ActivationRecord record)
    throws Throwable
  {
    int recordType = record.getCurrentType().shortValue();
    record.getActivationDetails().populateBeLists();
    SyncGridNode otherParty = record.getActivationDetails().getApproveDetails();
    if (recordType == ActivationRecord.TYPE_APPROVAL)
    {
      notifyActivation(
        record,
        otherParty,
        record.getActivationDetails().getRequestorBeList());
      // notify (raise alert)
      notifyPartnerApprovedActivation(otherParty.getGridnode());      
    } 
    else if (recordType == ActivationRecord.TYPE_DENIAL)
    {   
      // notify (raise alert)
      notifyPartnerRejectedActivation(otherParty.getGridnode());
    }
  }
  
  // *********************** Own methods ***********************************

  /**
   * Approve a GridNode as an activated partner.
   *
   * @param record the relevant ActivationRecord.
   * @param approvedGridnode the approved GridNode.
   * @param certFile The certificate file of the approved GridNode.
   */
  private void approve(
    ActivationRecord record,
    SyncGridNode approvedGridnode,
    File certFile)
    throws Throwable
  {
    approvedGridnode.getGridnode().setState(GridNode.STATE_ACTIVE);
    approvedGridnode.getGridnode().setDTActivated(getCurrentTimestamp());
    approvedGridnode.getGridnode().setDTReqActivate(record.getDTRequested());

    /*021128NSL: Use SyncGridNodeDelegate instead of sync() directly.
    approvedGridnode.sync();
    */
    //new SyncGridNodeDelegate().startSynchronize(approvedGridnode);

    SyncGridNodeDelegate syncDelegate = new SyncGridNodeDelegate();
    syncDelegate.setCertificateFile(certFile);
    syncDelegate.startSynchronize(approvedGridnode);

    /*040204NSL: Do not notify activation here, do it after the acknowledgement is sent back
    boolean approvingRole = (record.getActivateDirection().shortValue() == ActivationRecord.DIRECTION_INCOMING);
    notifyActivated(
      approvingRole,
      approvedGridnode.getGridnode().getID(),
      approvingRole ?
        record.getActivationDetails().getRequestorBeList() :
        record.getActivationDetails().getApproverBeList(),
      exchangedBes);
    */
    //share be: moved to ResourceChangeListenerMDBean
    //shareBes(exchangedBes, approvedGridnode.getGridnode().getID());

  }

  /**
   * Internal notification on approval of a GridNode as an activated partner.
   *
   * @param record the relevant ActivationRecord.
   * @param approvedGridnode the approved GridNode.
   * @param exchangeBes My Business Entity(s) being selected for trading.
   */
  private void notifyActivation(
    ActivationRecord record,
    SyncGridNode approvedGridnode,
    Collection exchangedBes)
  {
    boolean approvingRole =
      (record.getActivateDirection().shortValue()
        == ActivationRecord.DIRECTION_INCOMING);
    notifyActivated(
      approvingRole,
      approvedGridnode.getGridnode().getID(),
      approvingRole
        ? record.getActivationDetails().getRequestorBeList()
        : record.getActivationDetails().getApproverBeList(),
      exchangedBes);
  }

  /**
   * Update an ActivationRecord to approval state.
   *
   * @param record The ActivationRecord.
   * @param approveDetails The approve details
   * @param transCompleted Whether to mark the transaction as completed.
   */
  private void updateActivationRecordForApproval(
    ActivationRecord record,
    SyncGridNode approveDetails,
    boolean transCompleted)
    throws Throwable
  {
    record.getActivationDetails().setApproveDetails(approveDetails);
    record.setCurrentType(new Short(ActivationRecord.TYPE_APPROVAL));
    record.setDTApproved(getCurrentTimestamp());
    record.setTransCompleted(transCompleted);

    ActivationRecordEntityHandler.getInstance().update(record);
  }

  /**
   * Update an ActivationRecord to denial state.
   *
   * @param record The ActivationRecord.
   * @param transCompleted Whether to mark the transaction as completed.
   */
  private void updateActivationRecordForDenial(
    ActivationRecord record,
    boolean transCompleted)
    throws Throwable
  {
    record.setCurrentType(new Short(ActivationRecord.TYPE_DENIAL));
    record.setDTDenied(getCurrentTimestamp());
    record.setTransCompleted(transCompleted);

    ActivationRecordEntityHandler.getInstance().update(record);
  }

  /**
   * Retrieve an Incoming Activation record with the specified UID.
   *
   * @param recordUID UID of the ActivationRecord.
   * @return The ActivationRecord retrieved, if any.
   * @throws InvalidActivationRecordStateException No such Incoming Activation
   * record found.
   */
  private ActivationRecord retrieveIncomingActivationRecord(Long recordUID)
    throws Throwable
  {
    ActivationRecord record =
      getActivationRecord(
        recordUID,
        null,
        new Short(ActivationRecord.TYPE_ACTIVATION),
        new Short(ActivationRecord.DIRECTION_INCOMING),
        null,
        null);

    if (record == null)
      throw InvalidActivationRecordStateException.illegalReply(
        "No Incoming Activation Record found!");

    return record;
  }

  /**
   * Retrieve an Outgoing Activation record with the specified GridNode ID.
   *
   * @param gridnodeID GridNode ID of the ActivationRecord.
   * @return The ActivationRecord retrieved, if any.
   * @throws InvalidActivationRecordStateException No such Outgoing Activation
   * record found.
   */
  private ActivationRecord retrieveOutgoingActivationRecord(Integer gridnodeID)
    throws Throwable
  {
    ActivationRecord record =
      getActivationRecord(
        null,
        gridnodeID,
        new Short(ActivationRecord.TYPE_ACTIVATION),
        new Short(ActivationRecord.DIRECTION_OUTGOING),
        null,
        null);

    if (record == null)
      throw InvalidActivationRecordStateException.illegalReply(
        "No Outgoing Activation Record found!");

    return record;
  }

  /**
   * Share the BusinessEntity(s) to the approved GridNode.
   *
   * @param sharedBes Collection of BusinessEntity(s) to share. These BusinessEntity(s)
   * do not contain the UID.
   * @param toEnterpriseId The ID of the GridNode.
   */
  //  private void shareBes(Collection sharedBes, String toEnterpriseId)
  //    throws Throwable
  //  {
  //    // obtain a list of UIDs of the BusinessEntity(s)
  //    ArrayList beUIDs = new ArrayList();
  //    for (Iterator i=sharedBes.iterator(); i.hasNext(); )
  //    {
  //      BusinessEntity be = (BusinessEntity)i.next();
  //      Long uid = ServiceLookupHelper.getBizRegManager().findBusinessEntityKey(
  //                   be.getEnterpriseId(), be.getBusEntId());
  //      beUIDs.add(uid);
  //    }
  //    // share them!
  //    ServiceLookupHelper.getSharedResourceManager().shareResourceIfNotShared(
  //      IResourceTypes.BUSINESS_ENTITY, beUIDs, toEnterpriseId);
  //  }
}