/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFlowControlInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 24 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel.model;

public interface IFlowControlInfo
{

  public static final String ENTITY_NAME = "FlowControlInfo";

  public static final int DEFAULT_SPLIT_THRESHOLD = 1024;

  // To set the Default BlockSize to 64KB.
  public static final int DEFAULT_SPLIT_SIZE = 64;

  //public static final int DEFAULT_SPLIT_SIZE = 500;

  public static final Number IS_ZIP = new Integer(1);

  public static final Number ZIP_THRESHOLD = new Integer(2);

  public static final Number IS_SPLIT = new Integer(3);

  public static final Number SPLIT_THRESHOLD = new Integer(4);

  public static final Number SPLIT_SIZE = new Integer(5);

  public static final int STATUS_FLOWCONTROL_SET = 1;

  public static final int STATUS_FLOWCONTROL_UNSET = 0;
}