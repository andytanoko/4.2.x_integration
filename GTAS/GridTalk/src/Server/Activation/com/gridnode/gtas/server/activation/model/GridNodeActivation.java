/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeActivation.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This entity keeps track of the data sent to the other GridNode involved
 * during an activation request and activation approval (if any).
 *
 * The Model:<BR><PRE>
 *   ActivateReason  - The reason for initiating the activation request.
 *   RequestDetails  - The data sent by the requestor GridNode.
 *   ApproveDetails  - The data sent by the approver GridNode.
 *   RequestorBeList - List of BusinessEntity(s) selected by requestor GridNode
 *                     for trading. This field is not persistent, and the list
 *                     can be obtained from the RequestDetails. This field is
 *                     created for easy retrieval.
 *   ApproverBeList  - List of BusinessEntity(s) selected by approver GridNode
 *                     for trading. This field is not persistent, and the list
 *                     can be obtained from the ApproveDetails. This field is
 *                     created for easy retrieval.
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GridNodeActivation
  extends    AbstractEntity
  implements IGridNodeActivation
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1248724034853940692L;
	private String _activateReason;
  private SyncGridNode _requestDetails;
  private SyncGridNode _approveDetails;
  private Collection   _requestorBeList;
  private Collection   _approverBeList;

  public GridNodeActivation()
  {
  }

  public String getEntityDescr()
  {
    return getActivateReason();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return null;
  }

  // ************************ Getters & Setters *****************************

  public String getActivateReason()
  {
    return _activateReason;
  }

  public void setActivateReason(String activateReason)
  {
    _activateReason = activateReason;
  }

  public SyncGridNode getRequestDetails()
  {
    return _requestDetails;
  }

  public void setRequestDetails(SyncGridNode requestDetails)
  {
    _requestDetails = requestDetails;
  }

  public SyncGridNode getApproveDetails()
  {
    return _approveDetails;
  }

  public void setApproveDetails(SyncGridNode approveDetails)
  {
    _approveDetails = approveDetails;
  }

  public Collection getRequestorBeList()
  {
    return _requestorBeList;
  }

  public void setRequestorBeList(Collection beList)
  {
    _requestorBeList = beList;
  }

  public Collection getApproverBeList()
  {
    return _approverBeList;
  }

  public void setApproverBeList(Collection beList)
  {
    _approverBeList = beList;
  }

  public void setApproverBesState(int beState)
  {
    if (_approveDetails != null)
      setBesState(_approveDetails.getBusinessEntities(), beState);
  }

  public void setRequestorBesState(int beState)
  {
    if (_requestDetails != null)
      setBesState(_requestDetails.getBusinessEntities(), beState);
  }

  public void populateBeLists()
  {
    if (_requestDetails != null)
      _requestorBeList = convertToBeList(_requestDetails.getBusinessEntities());

    if (_approveDetails != null)
      _approverBeList = convertToBeList(_approveDetails.getBusinessEntities());
  }

  /**
   * Convert an array of SyncBusinessEntity objects to a Collection of BusinessEntity
   * objects.
   */
  public static Collection convertToBeList(SyncBusinessEntity[] syncBes)
  {
    ArrayList list = new ArrayList();

    if (syncBes != null)
    {
      for (int i=0; i<syncBes.length; i++)
        list.add(syncBes[i].getBusinessEntity());
    }

    return list;
  }

  /**
   * Modify the State of the BusinessEntity objects embedded in the specified
   * SyncBusinessEntity(s).
   *
   * @param syncBes The SyncBusinessEntity(s)
   * @param beState The state to set to.
   */
  public static void setBesState(
    SyncBusinessEntity[] syncBes, int beState)
  {
    if (syncBes != null)
    {
      for (int i=0; i<syncBes.length; i++)
        syncBes[i].getBusinessEntity().setState(beState);
    }
  }

  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    if (_approveDetails != null)
      _approveDetails.preSerialize();
    if (_requestDetails != null)
      _requestDetails.preSerialize();
  }

  public void postSerialize()
  {
    if (_approveDetails != null)
      _approveDetails.postSerialize();
    if (_requestDetails != null)
      _requestDetails.postSerialize();
  }

  public void postDeserialize()
  {
    if (_approveDetails != null)
      _approveDetails.postDeserialize();
    if (_requestDetails != null)
      _requestDetails.postDeserialize();
  }

}