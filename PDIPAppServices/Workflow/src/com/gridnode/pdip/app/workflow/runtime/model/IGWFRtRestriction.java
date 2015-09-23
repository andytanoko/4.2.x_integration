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


public interface IGWFRtRestriction {

  public static final String ENTITY_NAME = "GWFRtRestriction";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Integer

  public static final Number RESTRICTION_UID = new Integer(1);

  public static final Number RESTRICTION_TYPE = new Integer(2);

  public static final Number SUB_RESTRICTION_TYPE = new Integer(3);

  public static final Number RT_PROCESS_UID = new Integer(4);

  public static final Number RT_ACTIVITY_UID = new Integer(5);

  public static final Number TRANS_ACTIVATION_STATE_LIST_UID = new Integer(6);

  public static final Number PROCESSDEF_KEY = new Integer(7);

}
