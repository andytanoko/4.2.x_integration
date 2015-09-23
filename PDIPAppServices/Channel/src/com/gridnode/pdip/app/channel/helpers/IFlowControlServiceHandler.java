/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFlowControlServiceHandler.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 03 2003    Jagadeesh                   Modified : Added Constants to imply
 *                                            Zip/Split possible selections.
 */

package com.gridnode.pdip.app.channel.helpers;

public interface IFlowControlServiceHandler
{
  //No Zip and No Split.
  public static final int FLOWCONTROL_LEVEL_0 = 0;
  //Zip but no Split.
  public static final int FLOWCONTROL_LEVEL_1 = 1;
  //Zip and Split.
  public static final int FLOWCONTROL_LEVEL_2 = 2;
  //No Zip but Split.
  public static final int FLOWCONTROL_LEVEL_3 = 3;

}