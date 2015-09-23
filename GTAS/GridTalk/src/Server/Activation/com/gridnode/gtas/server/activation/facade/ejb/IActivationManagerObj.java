/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivationManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Neo Sok Lay         Created
 * Feb 05 2004    Neo Sok Lay         GNDB00017037: Handle feedback for acknowledgement.
 */
package com.gridnode.gtas.server.activation.facade.ejb;

import com.gridnode.gtas.server.activation.exceptions.GridNodeActivationException;
import com.gridnode.gtas.server.activation.exceptions.SearchGridNodeException;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria;
import com.gridnode.gtas.server.activation.model.SearchGridNodeQuery;
import com.gridnode.gtas.server.activation.model.SearchGridNodeResults;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.ejb.EJBObject;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * EJBObject for the ActivationManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public interface IActivationManagerObj extends EJBObject
{
  // ******************** Search ******************************************
  /**
   * Submits a GridNode Search.
   *
   * @param criteria The SearchGridNodeCriteria.
   * @return a Search ID to retrieve the search results later on.
   */
  public Long submitSearch(SearchGridNodeCriteria criteria)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Retrieve a GridNode Search submitted previously (in the same session).
   *
   * @param searchID The Search ID returned when the search was submitted
   * previously.
   */
  public SearchGridNodeQuery retrieveSearch(Long searchID)
    throws SearchGridNodeException, SystemException, RemoteException;

  /**
   * Invoked when the search results are received from the GridMaster.
   *
   * @param searchID the SearchID for the submitted search
   * @param results The search results.
   */
  public void notifySearchResults(Long searchID, SearchGridNodeResults results)
    throws SearchGridNodeException, SystemException, RemoteException;

  // ********************** Activations ********************************

  /**
   * Submit an Activation request.
   *
   * @param gridnodeID The ID of the GridNode to request for activation.
   * @param gridnodeName The name of the GridNode to activate.
   * @param activateReason The reason for activation.
   * @param requestorBeUIDs List of UIDs of the BusinessEntity(s) to trade.
   */
  public void submitActivationRequest(
    Integer gridnodeID, String gridnodeName, String activateReason,
    Collection requestorBeUIDs)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Approve an incoming Activation request.
   *
   * @param recordUID The UID of the ActivationRecord (Incoming Activation).
   * @param approverBeUIDs List of UIDs of the BusinessEntity(s) to trade.
   */
  public void approveActivationRequest(
    Long recordUID, Collection approverBeUIDs)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Deny an incoming Activation request.
   *
   * @param recordUID The UID of the ActivationRecord (Incoming Activation).
   */
  public void denyActivationRequest(
    Long recordUID)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Abort an outgoing Activation request.
   *
   * @param recordUID The UID of the ActivationRecord (Outgoing Activation).
   */
  public void abortActivationRequest(
    Long recordUID)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Submit an outgoing Deactivation request. An existing Approved Activation
   * record must exist for the GridNode to deactive.
   *
   * @param gridnodeID The ID of the active GridNode to deactivate.
   */
  public void submitDeactivationRequest(
    Integer gridnodeID)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Invoked when a request is received.
   *
   * @param eventID The EventID determining the type of request received
   * @param refTransID The reference TransID to be used for sending acknowledgement
   * back to the sender.
   * @param dataPayload The data payload received.
   * @param filePayload The file payload received. At least one file is received
   * which is the received activation file.
   */
  public void receiveRequest(
    String eventID, String refTransID, String[] dataPayload, File[] filePayload)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Invoked when an acknowledgement for a previous submit request is received.
   *
   * @param recordUID The UID of the related ActivationRecord that the acknowledgement
   * is for.
   * @param dataPayload The data payload received. The first element must be
   * a flag indicating whether (true/false) the request was processed successfully.
   * If false, the second element would indicate the reason for the failure.
   * @param filePayload The file payload received.
   */
  public void receiveAcknowledgement(
    String eventID, Long recordUID, String[] dataPayload, File[] filePayload)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Invoked when feedback is received for a submitting request message is received.
   *
   * @param eventID Event ID for the submit.
   * @param recordUID The UID of the related ActivationRecord that the feedback
   * is for.
   * @param success Whether the submit was successful.
   * @param message If success=false, indicates the reason for the failure.
   */
  public void receiveSubmissionFeedback(
    String eventID, Long recordUID, boolean success, String message)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Invoked when feedback is received for a submitting acknowledgement message is received.
   *
   * @param eventID Event ID for the acknowledgement.
   * @param refTransID The reference TransID used for sending acknowledgement
   * back to the sender.
   * @param success Whether the submit was successful.
   * @param message If success=false, indicates the reason for the failure.
   */
  public void receiveAcknowledgementFeedback(
    String eventID, String refTransID, boolean success, String message)
    throws GridNodeActivationException, SystemException, RemoteException;

  /**
   * Find an ActivationRecord.
   *
   * @param uID The UID of the ActivationRecord to find.
   * @return The ActivationRecord found, if any.
   * @throws FindEntityException No ActivationRecord found with the specified
   * UID.
   */
  public ActivationRecord findActivationRecord(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find ActivationRecord(s) that fulfill a filtering condition.
   *
   * @param filter The filtering condition.
   * @return List of ActivationRecord(s) found.
   */
  public Collection findActivationRecordsByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Delete an ActivationRecord.
   *
   * @param uID UID of the ActivationRecord to delete
   */
  public void deleteActivationRecord(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;

}