/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INodeTypes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-0?-??     Andrew Hill         Created
 * 2002-08-27     Andrew Hill         Moved up to renderers package, renamed INodeTypes
 * 2002-12-11     Andrew Hill         Added INLINE_FRAME
 * 2002-12-18     Andrew Hill         CHECKBOX_PARENT and RADIO_PARENT renamed (INPUT_*_MULTIPLE)
 * 2003-07-17     Andrew Hill         Added SELECTION_TABLE & INSERT_SELECTION_TABLE
 */
package com.gridnode.gtas.client.web.renderers;

public interface INodeTypes
{
  public static final int NONE              = -1;

  public static final int OTHER             =  0;

  public static final int INPUT_CHECKBOX    =  10;
  public static final int INPUT_HIDDEN      =  20;
  public static final int INPUT_TEXT        =  30;
  public static final int INPUT_PASSWORD    =  40;
  public static final int INPUT_RADIO       =  41;
  public static final int INPUT_BUTTON      =  42;
  public static final int INPUT_FILE        =  43;
  public static final int INPUT_IMAGE       =  44;
  public static final int INPUT_RESET       =  45;
  public static final int INPUT_SUBMIT      =  46;

  public static final int TEXTAREA          =  60;

  public static final int TEXT_PARENT       =  70;
  public static final int OPTION_PARENT     =  80;

  public static final int INPUT_TEXT_PARENT = 100;

  public static final int IMAGE             = 110;
  public static final int LINK              = 120;

  public static final int LI_PARENT         = 130;

  public static final int TABLE             = 140;
  public static final int TABLE_ROW         = 150;
  public static final int TABLE_HEAD        = 160;
  public static final int TABLE_BODY        = 170;
  public static final int TABLE_FOOT        = 180;

  public static final int INSERT_MULTISELECTOR  = 190;
  public static final int MULTISELECTOR_VALUE   = 191;

  public static final int MULTIFILES        = 200;

  public static final int INLINE_FRAME      = 210;
  
  public static final int INSERT_SELECTION_TABLE = 220; //20030717AH
  public static final int SELECTION_TABLE = 221; //20030717AH
}