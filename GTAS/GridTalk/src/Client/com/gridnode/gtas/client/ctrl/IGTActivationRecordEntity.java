/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTActivationRecordEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 * 2002-11-15     Andrew Hill         CURRENT_TYPE field
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.activation.IActivationRecord;

public interface IGTActivationRecordEntity extends IGTEntity
{
  //Constants for direction (ACT_DIRECTION and DEACT_DIRECTION)
  public static final Short DIRECTION_INCOMING = new Short(IActivationRecord.DIRECTION_INCOMING);
  public static final Short DIRECTION_OUTGOING = new Short(IActivationRecord.DIRECTION_OUTGOING);

  //Constants for CURRENT_TYPE
  public static final Short CURRENT_TYPE_ACTIVATION   = new Short(IActivationRecord.TYPE_ACTIVATION);
  public static final Short CURRENT_TYPE_DEACTIVATION = new Short(IActivationRecord.TYPE_DEACTIVATION);
  public static final Short CURRENT_TYPE_APPROVAL     = new Short(IActivationRecord.TYPE_APPROVAL);
  public static final Short CURRENT_TYPE_DENIAL       = new Short(IActivationRecord.TYPE_DENIAL);
  public static final Short CURRENT_TYPE_ABORTION     = new Short(IActivationRecord.TYPE_ABORTION);

  //Constants for DO_ACTION
  public static final Short DO_ACTION_REQUEST = new Short((short)0);
  public static final Short DO_ACTION_APPROVE = new Short((short)1);
  public static final Short DO_ACTION_DENY    = new Short((short)2);
  public static final Short DO_ACTION_ABORT   = new Short((short)3);

  //Constants for FILTER_TYPE
  public static final String FILTER_TYPE_INCOMING_ACTIVATION    = "incomingActivation";
  public static final String FILTER_TYPE_OUTGOING_ACTIVATION    = "outgoingActivation";
  public static final String FILTER_TYPE_INCOMING_DEACTIVATION  = "incomingDeactivation";
  public static final String FILTER_TYPE_OUTGOING_DEACTIVATION  = "outgoingDeactivation";
  public static final String FILTER_TYPE_APPROVED               = "approved";
  public static final String FILTER_TYPE_DENIED                 = "denied";
  public static final String FILTER_TYPE_ABORTED                = "aborted";
  public static final String FILTER_TYPE_DEACTIVATED            = "deactivated";

  // Field ids
  public static final Number UID = IActivationRecord.UID;
  public static final Number ACT_DIRECTION        = IActivationRecord.ACT_DIRECTION;
  public static final Number DEACT_DIRECTION      = IActivationRecord.DEACT_DIRECTION;
  public static final Number GRIDNODE_ID          = IActivationRecord.GRIDNODE_ID;
  public static final Number GRIDNODE_NAME        = IActivationRecord.GRIDNODE_NAME;
  public static final Number DT_REQUESTED         = IActivationRecord.DT_REQUESTED;
  public static final Number DT_APPROVED          = IActivationRecord.DT_APPROVED;
  public static final Number DT_ABORTED           = IActivationRecord.DT_ABORTED;
  public static final Number DT_DENIED            = IActivationRecord.DT_DENIED;
  public static final Number DT_DEACTIVATED       = IActivationRecord.DT_DEACTIVATED;
  public static final Number ACTIVATION_DETAILS   = IActivationRecord.ACTIVATION_DETAILS;
  public static final Number IS_LATEST            = IActivationRecord.IS_LATEST;
  public static final Number CURRENT_TYPE         = IActivationRecord.CURRENT_TYPE;

  // vfields
  public static final Number DO_ACTION = new Integer(-10);
  public static final Number FILTER_TYPE = new Integer(-20);
}