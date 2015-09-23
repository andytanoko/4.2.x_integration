/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivationRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.activation;

/**
 * This interface defines the field IDs and constants for accessing the
 * fields of ActivationRecord entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IActivationRecord
{
  /**
   * Name for the ActivationRecord entity.
   */
  public static final String ENTITY_NAME = "ActivationRecord";

  /**
   * Field ID for UID. A Long.
   */
  public static final Number UID         = new Integer(0);

  /**
   * Field ID for CurrentType. A Short.
   */
  public static final Number CURRENT_TYPE  = new Integer(1);

  /**
   * Field ID for ActivateDirection. A Short.
   */
  public static final Number ACT_DIRECTION = new Integer(2);

  /**
   * Field ID for DeactivateDirection. A Short.
   */
  public static final Number DEACT_DIRECTION = new Integer(3);

  /**
   * Field ID for GridNodeID. An Integer.
   */
  public static final Number GRIDNODE_ID     = new Integer(4);

  /**
   * Field ID for GridNodeName. A String.
   */
  public static final Number GRIDNODE_NAME   = new Integer(5);

  /**
   * Field ID for DTRequested. A Timestamp.
   */
  public static final Number DT_REQUESTED    = new Integer(6);

  /**
   * Field ID for DTApproved. A Timestamp.
   */
  public static final Number DT_APPROVED     = new Integer(7);

  /**
   * Field ID for DTAborted. A Timestamp.
   */
  public static final Number DT_ABORTED      = new Integer(8);

  /**
   * Field ID for DTDenied. A Timestamp.
   */
  public static final Number DT_DENIED       = new Integer(9);

  /**
   * Field ID for DTDeactivated. A Timestamp.
   */
  public static final Number DT_DEACTIVATED  = new Integer(10);

  /**
   * Field ID for ActivationDetails.  A GridNodeActivation.
   */
  public static final Number ACTIVATION_DETAILS = new Integer(11);

  /**
   * Field ID for IsLatest. A Boolean.
   */
  public static final Number IS_LATEST          = new Integer(12);

  /**
   * Field ID for TransCompleted. A Boolean.
   */
  public static final Number TRANS_COMPLETED    = new Integer(13);


  // Values for CURRENT_TYPE

  public static final short TYPE_ACTIVATION    = 0;
  public static final short TYPE_DEACTIVATION  = 1;
  public static final short TYPE_APPROVAL      = 2;
  public static final short TYPE_DENIAL        = 3;
  public static final short TYPE_ABORTION      = 4;

  // Values for DEACT_DIRECTION and ACT_DIRECTION

  public static final short DIRECTION_INCOMING  = 1;
  public static final short DIRECTION_OUTGOING  = 2;

}