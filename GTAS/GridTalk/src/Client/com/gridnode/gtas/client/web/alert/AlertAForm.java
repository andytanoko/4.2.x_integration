/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.utils.StaticUtils;

public class AlertAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _type;
  private String[] _actions;

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getType()
  { return _type; }

  public Long getTypeLong()
  { return StaticUtils.longValue(_type); }

  public void setType(String type)
  { _type=type; }

  public String[] getActions()
  { return _actions; }

  public Collection getActionsLongCollection()
  { return StaticUtils.getLongCollection(_actions); }

  public void setActions(String[] actions)
  { _actions=actions; }

  public void doReset(ActionMapping p0, HttpServletRequest p1)
  {
    _actions = null;
  }
}