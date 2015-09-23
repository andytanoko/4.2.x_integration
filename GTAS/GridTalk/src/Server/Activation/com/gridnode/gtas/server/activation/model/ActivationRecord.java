/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import java.sql.Timestamp;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This entity keeps track of records of activation activities.
 *
 * The Model:<BR><PRE>
 *   UID                 - UID of the ActivationRecord instance.
 *   CurrentType         - Current activity: Activation/Deactivation/Abortion/
 *                         Denial/Approval.
 *   ActivateDirection   - What is the direction of Activation: Incoming/Outgoing
 *   DeactivateDirection - What is the direction of Deactivation, if any:
 *                         Incoming/Outgoing.
 *   GridNodeID          - The ID of the other GridNode involved in the activities.
 *   GridNodeName        - Name of the other GridNode
 *   DTRequested         - Timetstamp when the Activation activity started.
 *   DTApproved          - Timestamp when the Activation was approved (CurrentType
 *                         changes to Approval).
 *   DTDenied            - Timestamp when the Activation was denied (CurrentType
 *                         changes to Denial).
 *   DTAborted           - Timestamp when the Activation was aborted (CurrentType
 *                         changes to Aborted).
 *   DTDeactivated       - Timestamp when the Approved activation was deactivated
 *                         (CurrentType changes to Deactivation).
 *   ActivationDetails   - The details of the activation request and approval.
 *   IsLatest            - Whether the record is the latest activity for the
 *                         other GridNode involved.
 *   TransCompleted      - Whether the latest activity has been completed.
 *   TransFailReason     - If TransCompleted, indicates any failure.
 * </PRE>
 * <P>

 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationRecord
  extends    AbstractEntity
  implements IActivationRecord
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8627339323337230098L;
	private Short _currentType;
  private Short _actDirection;
  private Short _deactDirection;
  private Integer _gridnodeID;
  private String  _gridnodeName;
  private Timestamp _dtRequested;
  private Timestamp _dtApproved;
  private Timestamp _dtDenied;
  private Timestamp _dtAborted;
  private Timestamp _dtDeactivated;
  private GridNodeActivation _activationDetails;
  private boolean _isLatest = true;
  private boolean _transCompleted = false;
  private String _transFailReason;

  public ActivationRecord()
  {
  }

  public String getEntityDescr()
  {
    return getUId() + "Type: "+getCurrentType();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ********************** Getters & Setters *********************************

  public Short getCurrentType()
  {
    return _currentType;
  }

  public void setCurrentType(Short currentType)
  {
    _currentType = currentType;
  }

  public Short getActivateDirection()
  {
    return _actDirection;
  }

  public void setActivateDirection(Short direction)
  {
    _actDirection = direction;
  }

  public Short getDeactivateDirection()
  {
    return _deactDirection;
  }

  public void setDeactivateDirection(Short direction)
  {
    _deactDirection = direction;
  }

  public Integer getGridNodeID()
  {
    return _gridnodeID;
  }

  public void setGridNodeID(Integer gridnodeID)
  {
    _gridnodeID = gridnodeID;
  }

  public String getGridNodeName()
  {
    return _gridnodeName;
  }

  public void setGridNodeName(String gridnodeName)
  {
    _gridnodeName = gridnodeName;
  }

  public Timestamp getDTRequested()
  {
    return _dtRequested;
  }

  public void setDTRequested(Timestamp dtRequested)
  {
    _dtRequested = dtRequested;
  }

  public Timestamp getDTDenied()
  {
    return _dtDenied;
  }

  public void setDTDenied(Timestamp dtDenied)
  {
    _dtDenied = dtDenied;
  }

  public Timestamp getDTDeactivated()
  {
    return _dtDeactivated;
  }

  public void setDTDeactivated(Timestamp dtDeactivated)
  {
    _dtDeactivated = dtDeactivated;
  }

  public Timestamp getDTApproved()
  {
    return _dtApproved;
  }

  public void setDTApproved(Timestamp dtApproved)
  {
    _dtApproved = dtApproved;
  }

  public Timestamp getDTAborted()
  {
    return _dtAborted;
  }

  public void setDTAborted(Timestamp dtAborted)
  {
    _dtAborted = dtAborted;
  }

  public GridNodeActivation getActivationDetails()
  {
    return _activationDetails;
  }

  public void setActivationDetails(GridNodeActivation actDetails)
  {
    _activationDetails = actDetails;
  }

  public boolean isLatest()
  {
    return _isLatest;
  }

  public void setLatest(boolean isLatest)
  {
    _isLatest = isLatest;
  }

  public boolean isTransCompleted()
  {
    return _transCompleted;
  }

  public void setTransCompleted(boolean completed)
  {
    _transCompleted = completed;
  }

  public String getTransFailReason()
  {
    return _transFailReason;
  }

  public void setTransFailReason(String failReason)
  {
    _transFailReason = failReason;
  }
}