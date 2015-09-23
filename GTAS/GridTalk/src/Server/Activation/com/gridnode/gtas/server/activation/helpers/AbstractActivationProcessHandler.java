/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractActivationProcessHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 * Nov 06 2002    neo Sok Lay         GridMasterPostOffice moved to Enterprise
 *                                    module. Use PostOfficeBean instead.
 * Apr 28 2003    Neo Sok Lay         Notify when received activation messages
 *                                    from partner.
 * Aug 04 2003    Neo Sok Lay         Pass additional parameter to indicate
 *                                    participating role when broadcasting
 *                                    activation notifications.
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.notify.ActivationNotification;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This abstract class provides the common services required by the various
 * functions of the Activation process, such as the obtaining of event IDs,
 * posting of messages, and getting the current timestamp.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public abstract class AbstractActivationProcessHandler
  implements IActivationProcessKeys
{
  protected static Configuration _config;
  protected static String _filePath;
  protected String _eventID;
  protected String _ackEventID;

  protected static final Hashtable _transIdRefTable = new Hashtable();
  
  protected AbstractActivationProcessHandler() throws Exception
  {
    loadConfig();
  }

  /**
   * Checks whether this handler can handle messages of the specified event ID.
   *
   * @param eventID The event ID.
   * @return <b>true</b> if the specified event ID is the same as the event ID
   * configured for this handler, <b>false</b> otherwise.
   */
  public boolean canHandleMessage(String eventID)
  {
    return eventID.equals(_eventID);
  }

  /**
   * Get UID of the ActivationRecord that is related to the specified
   * transaction of the other party. This relationship is kept only when
   * an acknowledgement is about to be sent back to the other party. By
   * invoking this method, the relationship will be removed from the tracking table.
   * 
   * @param refTransId The reference transaction id.
   * @return The UID of the ActivationRecord, or <b>null</b> if no known ActivationRecord is tracked.
   */
  public Long getRecordUId(String refTransId)
  {
    return (Long)_transIdRefTable.remove(refTransId);
  }
  
  /**
   * Handle an incoming message. Sub-classes should override this method to
   * provide handling logic for the specific messages that they can handle.
   *
   * @param refTransID The TransID from the sender of the message. This will
   * be used to send acknowledgement back to the sender.
   * @param message The received message, which is deserialized from the received
   * activation file.
   * @param dataPayload The data payload received.
   * @param filePayload The file payload received. The first element of the
   * array is the activation file.
   */
  public void handleIncomingMessage(
    String refTransID, SyncGridNode message,
    String[] dataPayload, File[] filePayload)
    throws Exception
  {
  }

  /**
   * Perform necessary after action when the transaction for an activity is
   * completed successfully. Sub-class should override this method to provide
   * specific logic for the type of record it is handling.
   *
   * @param record The relevant ActivationRecord.
   * @param filesReceived The file payload received.
   */
  protected void onTransCompleted(ActivationRecord record, File[] filesReceived)
    throws Throwable
  {
  }

  public void handleSubmissionFeedback(
    Long recordUID, boolean success, String message) throws Throwable
  {
    Logger.log("[AbstractActivationProcessHandler.handleSubmissionFeedback] "
      + "Record "+recordUID + ", feedback: "+message);

    if (!success)
    {
      ActivationRecord record =
        (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);

      updateTransFailed(record, message);
    }

  }

  public void handleAcknowledgementFeedback(
    String refTransID, boolean success, String message) throws Throwable
  {
    Logger.log("[AbstractActivationProcessHandler.handleAcknowledgementFeedback] "
      + "RefTransID "+refTransID + ", feedback: "+message);

    Long recordUID = getRecordUId(refTransID);
    if (recordUID == null)
    {
      Logger.debug("[AbstractActivationProcessHandler.handleAcknowledgementFeedback] "
        + "No known ActivationRecord waiting for handling");
        return;
    }
    
    ActivationRecord record =
      (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);

    if (!success)
    {
      updateTransFailed(record, message);
    }
    else
    {
      onAcknowledgementSent(record);
    }
  }

  /**
   * Perform necessary after action when the acknowledgement for an activity is
   * sent successfully. Sub-class should override this method to provide
   * specific logic for the type of record it is handling.
   *
   * @param record The relevant ActivationRecord.
   */
  protected void onAcknowledgementSent(ActivationRecord record) throws Throwable
  {
  }
  
  public void handleSubmissionAck(
    Long recordUID, String[] dataPayload, File[] filePayload)
    throws Throwable
  {
    Logger.log("[AbstractActivationProcessHandler.handleSubmissionAck] "
      + "Record "+recordUID);

    try
    {
      ActivationRecord record =
        (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);

      boolean succeed = new Boolean(dataPayload[0]).booleanValue();
      if (succeed)
      {
        record.setTransCompleted(true);
        try
        {
          onTransCompleted(record, filePayload);
          ActivationRecordEntityHandler.getInstance().update(record);
        }
        catch (Throwable t)
        {
          record.setTransCompleted(false);
          updateTransFailed(record, t.getMessage());
        }
      }
      else
      {
        updateTransFailed(record, dataPayload[1]);
      }
    }
    catch (Throwable t)
    {
      Logger.warn("[AbstractActivationProcessHandler.handleSubmissionAck] ", t);
      throw t;
    }

  }

   /**
   * Update the relevant ActivationRecord that the transaction has failed (
   * trans is also completed).
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @param errorMessage The error message received from the acknowledgement.
   * @throws InvalidActivationRecordStateException The record has already completed
   * trans.
   */
  protected void updateTransFailed(ActivationRecord record, String errorMessage)
    throws Throwable
  {
    try
    {
      Logger.log("[AbstractActivationProcessHander.updateTransFailed] Enter ");

      //record.setTransCompleted(true);
      StringBuffer buff = new StringBuffer();
      buff.append('[').append(getCurrentTimestamp()).append(']');
      buff.append(errorMessage);
      if (record.getTransFailReason() != null)
        buff.append("\r\n").append(record.getTransFailReason());

      record.setTransFailReason(buff.toString());

      ActivationRecordEntityHandler.getInstance().update(record);
    }
    finally
    {
      Logger.log("[AbstractActivationProcessHander.updateTransFailed] Exit ");
    }
  }

  /**
   * Update the relevant ActivationRecord that the transaction has failed (
   * trans is also completed).
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @param errorMessage The error message received from the acknowledgement.
   * @throws InvalidActivationRecordStateException The record has already completed
   * trans.
   */
//  private void transFailed(Long recordUID, String errorMessage)
//    throws Throwable
//  {
//    try
//    {
//      Logger.log("[ActivationAcknowledgementReceiver.transFailed] Enter ");
//      ActivationRecord record = (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);
//
//      if (record.isTransCompleted())
//        throw new InvalidActivationRecordStateException(
//                  "Transaction has already been completed for Record "+recordUID);
//
//      record.setTransCompleted(true);
//      record.setTransFailReason(errorMessage);
//
//      ActivationRecordEntityHandler.getInstance().update(record);
//    }
//    finally
//    {
//      Logger.log("[ActivationAcknowledgementReceiver.transFailed] Exit ");
//    }
//  }


  /**
   * Create the standard payload that forms the activation file.
   *
   * @return The standard payload object that contains own Gridnode information.
   */
  protected SyncGridNode createPayload()
    throws Throwable
  {
    // create GridNode
    GridNode gn = getMyGridNode();

    // create SyncGridNode
    SyncGridNode syncGn = new SyncGridNode();
    syncGn.setGridnode(gn);

    return syncGn;
  }

  /**
   * Create the request/approve payload that forms the activation file.
   *
   * @return The request/approve payload object that contains own GridNode
   * information, activation reason (if request), and UIDs of BusinessEntity(s)
   * to trade.
   */
  protected SyncGridNode createPayload(
    String activateReason, Collection beUIDs)
    throws Throwable
  {
    // create GridNode
    GridNode gn = getMyGridNode();
    gn.setActivationReason(activateReason);

    BusinessEntity[] bes = retrieveBusinessEntities(beUIDs);
    SyncBusinessEntity[] syncBes = new SyncBusinessEntity[bes.length];

    //create SyncBusinessEntity(s) for each be
    for (int i=0; i<bes.length; i++)
    {
      syncBes[i] = new SyncBusinessEntity(bes[i]);
    }

    // create SyncGridNode
    SyncGridNode syncGn = new SyncGridNode();
    syncGn.setGridnode(gn);
    syncGn.setBusinessEntities(syncBes);

    return syncGn;
  }

  /**
   * Submit the activation file to the GridMaster.
   *
   * @param recipient The recipient of the activation.
   * @param payload The payload that will be converted to the activation file.
   * @param recordID The UID of the relevant ActivationRecord, used as the transID
   * for messaging.
   * @param filePrefix The prefix to use for the activation filename.
   */
  protected void submit(
    Integer recipient, SyncGridNode payload, Long recordID, String filePrefix)
    throws Exception
  {
    File serFile = writeToFile(payload, recordID, filePrefix);

    post(recipient.toString(), _eventID, recordID.toString(), new File[] {serFile}, null);
  }

  /**
   * Submit the activation file with a status flag to the GridMaster.
   *
   * @param recipient The recipient of the activation.
   * @param payload The payload that will be converted to the activation file.
   * @param status The status flag which will be part of the data payload.
   * @param recordID The UID of the relevant ActivationRecord, used as the transID
   * for messaging.
   * @param filePrefix The prefix to use for the activation filename.
   */
  protected void submit(
    Integer recipient, SyncGridNode payload, Boolean status,
    Long recordID, String filePrefix)
    throws Exception
  {
    File serFile = writeToFile(payload, recordID, filePrefix);

    post(recipient.toString(), _eventID, recordID.toString(), new File[] {serFile},
      new String[] {status.toString()});
  }

  /**
   * Post an acknowledgement message to the GridMasterPostOffice to be queued for sending to the
   * GridMaster.
   *
   * @param recipient Target recipient of the message.
   * @param eventID the Event ID for the message
   * @param refTransID the reference Trans ID for the message that this acknowledgement is
   * targeted for.
   * @param recordUID The UID of the ActivationRecord that is related to this acknowledgement.
   * @param filePayload The file payload to send.
   * @param dataPayload The data payload to send.
   */
  protected void acknowledge(
    String recipient, String eventID, String refTransID, Long recordUID,
    File[] filePayload, String[] dataPayload)
    throws Exception
  {
    if (recordUID != null)
      _transIdRefTable.put(refTransID, recordUID);
    post(recipient, eventID, refTransID, filePayload, dataPayload);
  }
  
  /**
   * Post a message to the GridMasterPostOffice to be queued for sending to the
   * GridMaster.
   *
   * @param recipient Target recipient of the message.
   * @param eventID the Event ID for the message
   * @param transID the Trans ID for this message instance.
   * @param filePayload The file payload to send.
   * @param dataPayload The data payload to send.
   */
  protected void post(
    String recipient, String eventID, String transID,
    File[] filePayload, String[] dataPayload)
    throws Exception
  {
    PostInstruction instruction = new PostInstruction();
    instruction.setFilePayload(filePayload);
    instruction.setDataPayload(dataPayload);
    instruction.setEventID(eventID);
    instruction.setTransID(transID);
    instruction.setRecipientNodeID(recipient);
    ServiceLookupHelper.getPostOffice().dropToGridMasterPostOffice(instruction);
  }

  /**
   * Read the Event ID configured for this handler.
   */
  protected abstract String readEventID() throws Exception;

  /**
   * Read the Ack Event ID configured for this handler.
   */
  protected abstract String readAckEventID() throws Exception;

  /**
   * Loads the configuration file for activation process handling (done only
   * once). The event ID and Ack event ID will be read in by the individual
   * handler.
   */
  private final void loadConfig() throws Exception
  {
    if (_config == null)
    {
      _config = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
      _filePath = FileUtil.getFile(PATH_TEMP, "").getAbsolutePath() + "/";
    }

    _eventID = readEventID();
    _ackEventID = readAckEventID();
  }

  /**
   * Get the current timestamp.
   *
   * @return The current timestamp.
   */
  protected Timestamp getCurrentTimestamp()
  {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * Get own GridNode configured for this GridTalk.
   *
   * @return My GridNode.
   */
  protected GridNode getMyGridNode() throws Throwable
  {
    return ServiceLookupHelper.getGridNodeManager().findMyGridNode();
  }

  /**
   * Find a GridNode with the specified GridNode ID.
   *
   * @param gridnodeID the GridNode ID of the GridNode to find.
   * @return the found GridNode, if any.
   */
  protected GridNode getGridNode(Integer gridnodeID) throws Throwable
  {
    return ServiceLookupHelper.getGridNodeManager().findGridNodeByID(
           gridnodeID.toString());
  }

  /**
   * Update a GridNode to the database.
   *
   * @param gridnode The GridNode to update.
   */
  protected void updateGridNode(GridNode gridnode) throws Throwable
  {
    ServiceLookupHelper.getGridNodeManager().updateGridNode(gridnode);
  }

  /**
   * Retrieve the ActivationRecord using the specified fields as criteria.
   * If the criteria is not relevant, provide <b>null</b> for it. In all cases,
   * the <b>latest</b> ActivationRecord will be retrieved.
   *
   * @param recordUID The UID of the ActivationRecord.
   * @param gridnodeID The GridNode ID of the ActivationRecord.
   * @param type The CurrentType of the ActivationRecord.
   * @param actDirection The ActivateDirection of the ActivationRecord.
   * @param deactDirection The DeactivateDirection of the ActivationRecord.
   * @param transCompleted The TransCompleted flag of the ActivationRecord.
   *
   * @return the ActivationRecord found, if any, or <b>null</b> if none.
   */
  protected ActivationRecord getActivationRecord(
    Long recordUID, Integer gridnodeID, Short type, Short actDirection,
    Short deactDirection, Boolean transCompleted)
    throws Throwable
  {
    ActivationRecord record = null;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ActivationRecord.IS_LATEST,
      filter.getEqualOperator(), Boolean.TRUE, false);
    if (recordUID != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.UID,
        filter.getEqualOperator(), recordUID, false);
    if (gridnodeID != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.GRIDNODE_ID,
        filter.getEqualOperator(), gridnodeID, false);
    if (type != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.CURRENT_TYPE,
        filter.getEqualOperator(), type, false);
    if (actDirection != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.ACT_DIRECTION,
        filter.getEqualOperator(), actDirection, false);
    if (deactDirection != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.DEACT_DIRECTION,
        filter.getEqualOperator(), deactDirection, false);
    if (transCompleted != null)
      filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.TRANS_COMPLETED,
        filter.getEqualOperator(), transCompleted, false);

    Object[] results = ActivationRecordEntityHandler.getInstance().getEntityByFilterForReadOnly(
                         filter).toArray();

    if (results.length > 0)
      record = (ActivationRecord)results[0];

    return record;
  }

  /**
   * Broadcast a notification that a partner has been newly activated.
   * 
   * @param approvingRole <b>true</b> if this GridTalk is the approving party.
   * @param partnerNodeID GridNode ID of the approved partner GridNode.
   * @param partnerBeList Collection of BusinessEntity(s) of the approved
   * partner GridNode.
   * @param myBeList Collection of BusinessEntity(s) exchanged with the
   * approved partner GridTalk.
   */
  protected void notifyActivated(
    boolean approvingRole,
    String partnerNodeID, 
    Collection partnerBeList, 
    Collection myBeList)
  {
    try
    {
      String myRole = approvingRole ? 
                      ActivationNotification.ROLE_APPROVE : 
                      ActivationNotification.ROLE_PASSIVE;
      
      ActivationNotification notification =
        new ActivationNotification(
        ActivationNotification.STATE_ACTIVATED,
        partnerNodeID,
        (Long[])getBeUIDs(myBeList).toArray(new Long[0]),
        (Long[])getBeUIDs(partnerBeList).toArray(new Long[0]),
        myRole);

      Notifier.getInstance().broadcast(notification);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_PROCESS_HANDLER,
                   "[AbstractActivationProcessHandler.notifyActivated] Error: "+ex.getMessage(), ex);
    }
  }

  protected void notifyDeactivated(
    boolean deactivatingRole,
    String partnerNodeID, Collection partnerBeList, Collection myBeList)
  {
    try
    {
      String myRole = deactivatingRole ? 
                      ActivationNotification.ROLE_DEACTIVATE : 
                      ActivationNotification.ROLE_PASSIVE;
      
      ActivationNotification notification =
        new ActivationNotification(
        ActivationNotification.STATE_DEACTIVATED,
        partnerNodeID,
        (Long[])getBeUIDs(myBeList).toArray(new Long[0]),
        (Long[])getBeUIDs(partnerBeList).toArray(new Long[0]),
        myRole);

      Notifier.getInstance().broadcast(notification);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_PROCESS_HANDLER,
                   "[AbstractActivationProcessHandler.notifyDeactivated] Error: "+ex.getMessage(), ex);
    }
  }

  protected Collection getBeUIDs(Collection bes)
    throws Throwable
  {
    // obtain a list of UIDs of the BusinessEntity(s)
    ArrayList beUIDs = new ArrayList();
    for (Iterator i=bes.iterator(); i.hasNext(); )
    {
      BusinessEntity be = (BusinessEntity)i.next();
      Long uid = ServiceLookupHelper.getBizRegManager().findBusinessEntityKey(
                   be.getEnterpriseId(), be.getBusEntId());
      beUIDs.add(uid);
    }

    return beUIDs;
  }

  protected void notifyPartnerRequestedActivation(GridNode partner)
  {
    notifyReceivedFromPartner(ActivationData.TYPE_REQ_ACTIVATION, partner.getID(), partner.getName());
  }

  protected void notifyPartnerApprovedActivation(GridNode partner)
  {
    notifyReceivedFromPartner(ActivationData.TYPE_APPROVE_ACTIVATION, partner.getID(), partner.getName());
  }

  protected void notifyPartnerRejectedActivation(GridNode partner)
  {
    notifyReceivedFromPartner(ActivationData.TYPE_REJECT_ACTIVATION, partner.getID(), partner.getName());
  }

  protected void notifyPartnerCancelledActivation(GridNode partner)
  {
    notifyReceivedFromPartner(ActivationData.TYPE_CANCEL_ACTIVATION, partner.getID(), partner.getName());
  }

  protected void notifyPartnerRequestedDeactivation(GridNode partner)
  {
    notifyReceivedFromPartner(ActivationData.TYPE_REQ_DEACTIVATION, partner.getID(), partner.getName());
  }

  private void notifyReceivedFromPartner(String type,
                                         String partnerNodeID,
                                         String partnerNodeName)
  {
    try
    {
      ActivationData data = new ActivationData(type,
                                               partnerNodeID,
                                               partnerNodeName,
                                               TimeUtil.getCurrentLocalTimestamp());
      ArrayList providers = new ArrayList();
      providers.add(data);

      //retrieve
      AlertRequestNotification notification = new AlertRequestNotification(
                                                    AlertRequestNotification.PARTNER_ACTIVATION,
                                                    providers);

      Notifier.getInstance().broadcast(notification);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_PROCESS_HANDLER,
                   "[AbstractActivationProcessHandler.notifyDeactivated] Error: "+ex.getMessage(), ex);
    }
  }

  /**
   * Convert the payload object to activation file.
   *
   * @param payload The payload object.
   * @param recordID the UID of the relevant Activation record.
   * @param filePrefix The prefix for the filename of the activation file.
   *
   * @return The converted activation file.
   */
  private File writeToFile(SyncGridNode payload, Long recordID, String filePrefix)
    throws Exception
  {
    String filename = new StringBuffer(_filePath).append(
                        filePrefix).append(recordID).append(
                        ACTIVATION_FILE_EXT).toString();

    payload.serialize(filename);

    return new File(filename);
  }

  /**
   * Retrieve BusinessEntity(s) using the specified UIDs.
   *
   * @param beUIDs The UIDs of the BusinessEntity(s) to retrieve.
   * @return The BusinessEntity(s) retrieved.
   */
  private BusinessEntity[] retrieveBusinessEntities(Collection beUIDs)
    throws Exception
  {
    Collection bes = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, BusinessEntity.UID, beUIDs, false);

      bes = ServiceLookupHelper.getBizRegManager().findBusinessEntities(filter);
    }
    catch (Exception ex)
    {
      Logger.warn("[RequestActivationHandler.retrieveBusinessEntities] Unable to retrieve Bes ",
        ex);
      throw ex;
    }

    return (BusinessEntity[])bes.toArray(new BusinessEntity[0]);
  }

}