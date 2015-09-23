/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RequestDeactivationHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Apr 28 2003    Neo Sok Lay         Notify when received deactivation from
 *                                    partner.
 * Aug 04 2003    Neo Sok Lay         Pass additional parameter when sending
 *                                    deactivate notification to indicate
 *                                    the role that this GridTalk is participating.
 * Feb 05 2004    Neo Sok Lay         Add onAcknowledgementSent() - to alert
 *                                    deactivation only after positive acknowledgement
 *                                    has been sent successfully.
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.activation.exceptions.InvalidActivationRecordStateException;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is a helper class for handling the submission and reception of
 * GridNode Activation Abort.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class RequestDeactivationHandler
  extends    AbstractActivationProcessHandler
{
  public RequestDeactivationHandler()
    throws Exception
  {
  }

  protected String readEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(DEACT_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Abort GridNode Activation configured!!");
    return eventID;

  }

  protected String readAckEventID() throws java.lang.Exception
  {
    String eventID = _config.getString(DEACT_ACK_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Ack Abort GridNode Activation configured!!");
    return eventID;
  }

  /**
   * Submit a Deactivation request for a GridNode.
   *
   * @param gridnodeID The ID of the GridNode to deactivate.
   */
  public void submitDeactivationRequest(Integer gridnodeID)
    throws Throwable
  {
    ActivationRecord record = retrieveApprovedRecord(gridnodeID);

    SyncGridNode deactDetails = createPayload();

    submit(record.getGridNodeID(), deactDetails, (Long)record.getKey(),
      DEACT_FILE_PREFIX);

    /**@todo to update only after ack received?? */
    updateActivationRecordForDeact(record, ActivationRecord.DIRECTION_OUTGOING, false);
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
      // check existing approved activation record.
      ActivationRecord record = retrieveApprovedRecord(
                                  new Integer(message.getGridnode().getID()));
      updateActivationRecordForDeact(record, ActivationRecord.DIRECTION_INCOMING, true);
      deactivate(record);

      status = Boolean.TRUE.toString();
      replyTo = record.getGridNodeID().toString();
      
      recordUID = new Long(record.getUId());
    }
    catch (Throwable ex)
    {
      Logger.warn("[RequestDeactivationHandler.handleIncomingMessage] Error ", ex);
      status = Boolean.FALSE.toString();
      error = ex.getMessage();
    }
    // acknowledge
    acknowledge(replyTo, _ackEventID, refTransID, recordUID, null, new String[] {status, error});
  }

  protected void onTransCompleted(ActivationRecord record, File[] filesReceived)
    throws Throwable
  {
    if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_DEACTIVATION
       && record.getDeactivateDirection().shortValue() == ActivationRecord.DIRECTION_OUTGOING)
    {
      deactivate(record);
    }
  }

  /**
   * @see com.gridnode.gtas.server.activation.helpers.AbstractActivationProcessHandler#onAcknowledgementSent(com.gridnode.gtas.server.activation.model.ActivationRecord)
   */
  protected void onAcknowledgementSent(ActivationRecord record)
    throws Throwable
  {
    if (record.getCurrentType().shortValue() == ActivationRecord.TYPE_DEACTIVATION)
    {
      String partnerGnId = record.getGridNodeID().toString();
      GridNode reqGn = record.getActivationDetails().getRequestDetails().getGridnode();
      GridNode apprGn = record.getActivationDetails().getApproveDetails().getGridnode();
      if (partnerGnId.equals(reqGn.getID()))
        notifyPartnerRequestedDeactivation(reqGn);
      else
        notifyPartnerRequestedDeactivation(apprGn);
    }
  }


  // *********************** Own methods ***********************************

  /**
   * Deactivates a GridNode
   *
   * @param gridnodeID The ID of the GridNode to deactivate.
   */
  private void deactivate(ActivationRecord record)
    throws Throwable
  {
    record.getActivationDetails().populateBeLists();

    GridNode gridnode = getGridNode(record.getGridNodeID());
    gridnode.setState(GridNode.STATE_INACTIVE);
    gridnode.setDTDeactivated(getCurrentTimestamp());
    updateGridNode(gridnode);

    Collection partnerBes = null, myBes = null;
    boolean deactivatingRole = false;
    switch (record.getActivateDirection().shortValue())
    {
      case ActivationRecord.DIRECTION_INCOMING:
           partnerBes = record.getActivationDetails().getRequestorBeList();
           myBes = record.getActivationDetails().getApproverBeList();
           break;
      case ActivationRecord.DIRECTION_OUTGOING:
           partnerBes = record.getActivationDetails().getApproverBeList();
           myBes = record.getActivationDetails().getApproverBeList();
           deactivatingRole = true;
           break;
    }

    // update partnerBes
    updateBesInactive(partnerBes);

    // notify deactivation
    notifyDeactivated(
      deactivatingRole,
      gridnode.getID(),
      partnerBes,
      myBes);
  }

  private void updateBesInactive(Collection bes)
    throws Throwable
  {
    IBizRegistryManagerObj mgr = ServiceLookupHelper.getBizRegManager();
    for (Iterator i=bes.iterator(); i.hasNext(); )
    {
      BusinessEntity be = (BusinessEntity)i.next();
      be = mgr.findBusinessEntity(
                   be.getEnterpriseId(), be.getBusEntId());
      be.setState(IBusinessEntity.STATE_INACTIVE);
      mgr.updateBusinessEntity(be);
    }
  }


  /**
   * Update an ActivationRecord to Deactivated state.
   *
   * @param record The ActivationRecord
   * @param transCompleted Whether to mark the transaction completed.
   */
  private void updateActivationRecordForDeact(
    ActivationRecord record, short direction, boolean transCompleted) throws Throwable
  {
    record.setCurrentType(new Short(ActivationRecord.TYPE_DEACTIVATION));
    record.setDeactivateDirection(new Short(direction));
    record.setDTDeactivated(getCurrentTimestamp());
    record.setTransCompleted(transCompleted);

    ActivationRecordEntityHandler.getInstance().update(record);
  }

  /**
   * Retrieve a currently approved ActivationRecord.
   *
   * @param gridnodeID The GridNode ID of the record.
   *
   * @return The ActivationRecord retrieved, if any.
   * @throws InvalidActivationRecordStateException No Approved activation record
   * found.
   */
  private ActivationRecord retrieveApprovedRecord(Integer gridnodeID)
    throws Throwable
  {
    ActivationRecord record = getActivationRecord(
                                null,
                                gridnodeID,
                                new Short(ActivationRecord.TYPE_APPROVAL),
                                null,
                                null,
                                Boolean.TRUE);
    if (record == null)
      throw InvalidActivationRecordStateException.illegalDeactivation(
            "No Approved Activation Record found!");

    return record;
  }
}