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


public interface IGWFRtActivity {

  public static final String ENTITY_NAME = "GWFRtActivity";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Integer

  public static final Number ACTIVITY_UID = new Integer(1);

  public static final Number STATE = new Integer(2);

  public static final Number PRIORITY = new Integer(3);

  public static final Number RT_PROCESS_UID = new Integer(4);

  public static final Number ACTIVITY_TYPE = new Integer(5);

  public static final Number FINISH_INTERVAL = new Integer(6);

  public static final Number START_TIME = new Integer(7);

  public static final Number END_TIME = new Integer(8);

  public static final Number CONTEXT_UID = new Integer(9);

  public static final Number ENGINE_TYPE = new Integer(10);

  public static final Number BRANCH_NAME = new Integer(11);

  public static final Number PROCESSDEF_KEY = new Integer(12);
//-------------------------------------------------------------
  public static final int OPEN_NOTRUNNING = 0;
  public static final int OPEN_RUNNING = 1;
  public static final int CLOSED_COMPLETED = 2;
  public static final int OPEN_NOTRUNNING_SUSPENDED = 3;
  public static final int CLOSED_ABNORMALCOMPLETED = 4;

  public String getActivityType();

  public Integer getState();

  public void setState(Integer state);
/*
  public IGWFRtActivity begin() throws Exception;

  public IGWFRtActivity complete() throws Exception;

  public IGWFRtActivity abort() throws Exception;

  public IGWFRtActivity pause() throws Exception;

  public IGWFRtActivity resume() throws Exception;
*/
}
