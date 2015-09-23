/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITimeConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2003-01-09     Andrew Hill         UTC Adjustment Support
 * 2004-02-17     Neo Sok Lay         Display pattern support
 */
package com.gridnode.gtas.client.ctrl;

public interface ITimeConstraint extends IConstraint
{
  public static final int ADJ_NONE  = 0; //20030109AH - No adjustment (default)
  public static final int ADJ_GTS   = 1; //20030109AH - The incorrect adjustment method as used by GTS
  public static final int ADJ_GTAS  = 2; //20030109AH - Correct adjustment method used by GTAS (Unimplemented)

  public boolean hasDate();
  public boolean hasTime();
  public int getAdjustment(); //20030109AH
  public String getPattern(); //20040217NSL
}