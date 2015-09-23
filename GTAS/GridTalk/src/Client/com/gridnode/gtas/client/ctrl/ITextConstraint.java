/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITextConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

public interface ITextConstraint extends IConstraint
{
  public int getMinLength();
  public int getMaxLength();
}