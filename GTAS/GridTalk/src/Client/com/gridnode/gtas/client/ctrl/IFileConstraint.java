/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFileConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

public interface IFileConstraint extends IConstraint
{
  public boolean isPath();
  public boolean isFileName();
  public String  getPathKeyFieldName();
  public String  getFixedPathKey();
  public String  getSubPathFieldName();
  public boolean isDownloadable();
}