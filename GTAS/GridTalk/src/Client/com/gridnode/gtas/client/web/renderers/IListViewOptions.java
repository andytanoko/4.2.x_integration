/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IListViewOptions.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-15     Andrew Hill         Created
 * 2002-06-03     Andrew Hill         Refactored
 * 2002-08-28     Andrew Hill         Added isAllowsEdit()
 * 2002-09-26     Andrew Hill         Added isAllowsSelection()
 * 2003-10-19     Daniel D'Cotta      Added isAllowsSorting()
 */
package com.gridnode.gtas.client.web.renderers;

public interface IListViewOptions
{
  public IColumnEntityAdapter getColumnAdapter();

  public String getViewURL();

  public String getUpdateURL();

  public String getCreateURL();

  public String getDeleteURL();

  public String getCreateLabelKey();

  public String getDeleteLabelKey();

  public String getConfirmDeleteMsgKey();

  public String getHeadingLabelKey();

  public String getMessageLabelKey();

  public boolean isAllowsEdit();

  public void setAllowsSelection(boolean flag);

  public boolean isAllowsSelection();

  public boolean isAllowsSorting();
}