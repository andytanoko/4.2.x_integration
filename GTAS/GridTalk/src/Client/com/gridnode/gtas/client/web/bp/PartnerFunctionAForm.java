/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class PartnerFunctionAForm extends GTActionFormBase
{
  private String _id;
  private String _description;
  private String _triggerOn;
  private String _workflowActivitiesOrder;
  private String[] _workflowActivitiesOrderExploded;

  public void setId(String value)
  { _id = value; }

  public String getId()
  { return _id; }

  public void setDescription(String value)
  { _description = value; }

  public String getDescription()
  { return _description; }

  public void setTriggerOn(String value)
  { _triggerOn = value; }

  public String getTriggerOn()
  { return _triggerOn; }

  public void setWorkflowActivitiesOrder(String values)
  {
    _workflowActivitiesOrder = values;
    _workflowActivitiesOrderExploded = StaticUtils.explode(values,",");
  }

  public void setWorkflowActivitiesOrderExploded(String[] values)
  {
    _workflowActivitiesOrderExploded = values;
    _workflowActivitiesOrder = StaticUtils.implode(values,",");
  }

  public void initWorkflowActivitiesOrder(int size)
  {
    _workflowActivitiesOrderExploded = new String[size];
    for(int i=0; i < size; i++)
    {
      _workflowActivitiesOrderExploded[i] = "" + i;
    }
    _workflowActivitiesOrder = StaticUtils.implode(_workflowActivitiesOrderExploded,",");
  }

  public String[] getWorkflowActivitiesOrderExploded()
  {
    return _workflowActivitiesOrderExploded;
  }

  public String getWorkflowActivitiesOrder()
  { return _workflowActivitiesOrder; }
}