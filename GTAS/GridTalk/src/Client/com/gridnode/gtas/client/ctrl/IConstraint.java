/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConstraintType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2002-09-02     Andrew Hill         Removed getProperties() method
 * 2002-10-31     Andrew Hill         Added constant for dynamic entity constraint
 */
package com.gridnode.gtas.client.ctrl;

 
public interface IConstraint
{
  public static final int TYPE_TEXT            = 0;
  public static final int TYPE_ENUMERATED      = 1;
  public static final int TYPE_RANGE           = 2;
  public static final int TYPE_LOCAL_ENTITY    = 3;
  public static final int TYPE_FOREIGN_ENTITY  = 4;
  public static final int TYPE_FILE            = 5;
  public static final int TYPE_UID             = 6;
  public static final int TYPE_TIME            = 7;
  public static final int TYPE_OTHER           = 8;
  public static final int TYPE_DYNAMIC_ENTITY  = 9;

  public int getType();
}