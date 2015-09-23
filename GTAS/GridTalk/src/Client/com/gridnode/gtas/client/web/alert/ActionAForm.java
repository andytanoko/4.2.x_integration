/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-29     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;
 
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ActionAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _msgId;

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getMsgId()
  { return _msgId; }

  public Long getMsgIdLong()
  { return StaticUtils.longValue(_msgId); }

  public void setMsgId(String msgId)
  { _msgId=msgId; }
}