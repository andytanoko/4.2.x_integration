/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.gridnode;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 
public class GridNodeAForm extends GTActionFormBase
{
  private String _id;
  private String _name;
  private String _category;
  private String _state;
  private String _activationReason;
  private String _dtCreated;
  private String _dtReqActivate;
  private String _dtActivated;
  private String _dtDeactivated;

  public String getId()
  { return _id; }

  public void setId(String id)
  { _id=id; }

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getCategory()
  { return _category; }

  public void setCategory(String category)
  { _category=category; }

  public String getState()
  { return _state; }

  public Short getStateShort()
  { return StaticUtils.shortValue(_state); }

  public void setState(String state)
  { _state=state; }

  public String getActivationReason()
  { return _activationReason; }

  public void setActivationReason(String activationReason)
  { _activationReason=activationReason; }

  public String getDtCreated()
  { return _dtCreated; }

  public void setDtCreated(String dtCreated)
  { _dtCreated=dtCreated; }

  public String getDtReqActivate()
  { return _dtReqActivate; }

  public void setDtReqActivate(String dtReqActivate)
  { _dtReqActivate=dtReqActivate; }

  public String getDtActivated()
  { return _dtActivated; }

  public void setDtActivated(String dtActivated)
  { _dtActivated=dtActivated; }

  public String getDtDeactivated()
  { return _dtDeactivated; }

  public void setDtDeactivated(String dtDeactivated)
  { _dtDeactivated=dtDeactivated; }
}