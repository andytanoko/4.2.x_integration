/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridNodeActivation.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.activation;

/**
 * This interface defines the Field IDs and constants for accessing fields
 * in the GridNodeActivation entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IGridNodeActivation
{
  /**
   * Name for the GridNodeActivation entity.
   */
  public static final String ENTITY_NAME = "GridNodeActivation";

  /**
   * Field ID for ActivateReason. A String.
   */
  public static final Number ACTIVATE_REASON = new Integer(1);

  /**
   * Field ID for RequestorBeList. A Collection of BusinessEntity(s).
   */
  public static final Number REQUESTOR_BE_LIST = new Integer(4);

  /**
   * Field ID for ApproverBeList. A Collection of BusinessEntity(s).
   */
  public static final Number APPROVER_BE_LIST   = new Integer(5);
}