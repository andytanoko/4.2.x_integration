/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.app.workflow.runtime.model;

public interface IGWFTransActivationState {
  /**
   * Name for GWFTransActivationState entity.
   */
  public static final String ENTITY_NAME = "GWFTransActivationState";

  public static final Number UID = new Integer(0);

  public static final Number TRANSITION_UID = new Integer(1);

  public static final Number RT_RESTRICTION_UID = new Integer(2);

  public static final Number RT_RESTRICTION_TYPE = new Integer(3);

  public static final Number STATE = new Integer(4);

  public static final Number LIST_UID = new Integer(5);

}
