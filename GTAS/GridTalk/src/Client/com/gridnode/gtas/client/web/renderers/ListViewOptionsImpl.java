/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ListViewOptionsImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-15     Andrew Hill         Created (UserLVO.java)
 * 2002-05-15     Daniel J D'Cotta    Created
 * 2002-06-03     Andrew Hill         Refactored into ListViewOptionsImpl class
 * 2002-08-28     Andrew Hill         Added allowsEdit property
 * 2002-09-26     Andrew Hill         Added allowsSelection property
 * 2002-10-19     Daniel D'Cotta      Added allowsSorting property
 */
package com.gridnode.gtas.client.web.renderers;

//@todo: make this class and interface redundant

public class ListViewOptionsImpl implements IListViewOptions
{
  private IColumnEntityAdapter _columnAdapter;
  private String _viewURL = "";
  private String _updateURL = "";
  private String _createURL = "";
  private String _deleteURL = "";
  private String _createLabelKey = "";
  private String _deleteLabelKey = "";
  private String _confirmDeleteMsgKey = "";
  private String _headingLabelKey = "";
  private String _messageLabelKey = "";
  private boolean _allowsEdit = true;
  private boolean _allowsSelection = true;
  private boolean _allowsSorting = false;

  public void setAllowsSelection(boolean flag)
  { _allowsSelection = flag; }

  public boolean isAllowsSelection()
  { return _allowsSelection; }

  public void setColumnAdapter(IColumnEntityAdapter columnAdapter)
  {
    _columnAdapter = columnAdapter;
  }

  public IColumnEntityAdapter getColumnAdapter()
  {
    return _columnAdapter;
  }

  public void setViewURL(String newViewURL)
  {
    _viewURL = newViewURL;
  }

  public String getViewURL()
  {
    return _viewURL;
  }

  public void setUpdateURL(String newUpdateURL)
  {
    _updateURL = newUpdateURL;
  }

  public String getUpdateURL()
  {
    return _updateURL;
  }

  public void setCreateURL(String newCreateURL)
  {
    _createURL = newCreateURL;
  }

  public String getCreateURL()
  {
    return _createURL;
  }

  public void setDeleteURL(String newDeleteURL)
  {
    _deleteURL = newDeleteURL;
  }

  public String getDeleteURL()
  {
    return _deleteURL;
  }

  public void setCreateLabelKey(String key)
  {
    _createLabelKey = key;
  }

  public String getCreateLabelKey()
  {
    return _createLabelKey;
  }

  public void setDeleteLabelKey(String key)
  {
    _deleteLabelKey = key;
  }

  public String getDeleteLabelKey()
  {
    return _deleteLabelKey;
  }

  public void setConfirmDeleteMsgKey(String key)
  {
    _confirmDeleteMsgKey = key;
  }

  public String getConfirmDeleteMsgKey()
  {
    return _confirmDeleteMsgKey;
  }

  public void setHeadingLabelKey(String key)
  {
    _headingLabelKey = key;
  }

  public String getHeadingLabelKey()
  {
    return _headingLabelKey;
  }

  public void setMessageLabelKey(String key)
  {
    _messageLabelKey = key;
  }

  public String getMessageLabelKey()
  {
    return _messageLabelKey;
  }

  public void setAllowsEdit(boolean flag)
  {
    _allowsEdit = flag;
  }

  public boolean isAllowsEdit()
  {
    return _allowsEdit;
  }

  public void setAllowsSorting(boolean flag)
  { 
    _allowsSorting = flag;
  }

  public boolean isAllowsSorting()
  { 
    return _allowsSorting;
  }
}