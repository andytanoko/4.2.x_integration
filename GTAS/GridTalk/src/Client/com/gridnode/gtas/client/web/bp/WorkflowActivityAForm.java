/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivityAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-19     Andrew Hill         Created
 * 2003-05-15     Andrew Hill         Fields to support raiseAlert type
 * 2004-04-01     Daniel D'Cotta      Added support for SUSPEND_ACTIVITY
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class WorkflowActivityAForm extends GTActionFormBase
{
  private String _activityType;
  private String _description;
  private String[] _mappingRuleUids;
  private String[] _userProcedureUids;
  private String[] _portUids;
  private String _alertType; //20030515AH
  private String[] _userDefinedAlertUids; //20030515AH
  private String _dispatchInterval;
  private String _dispatchCount;

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setActivityType(String value)
  { _activityType = value; }

  public String getActivityType()
  { return _activityType; }

  public Integer getActivityTypeInteger()
  {
    try
    {
      return new Integer( Integer.parseInt(_activityType) );
    }
    catch(NumberFormatException e)
    {
      return null;
    }
  }

  public void setMappingRuleUids(String[] values)
  { _mappingRuleUids = values; }

  public String[] getMappingRuleUids()
  { return _mappingRuleUids; }

  public void setUserProcedureUids(String[] values)
  { _userProcedureUids = values; }

  public String[] getUserProcedureUids()
  { return _userProcedureUids; }

  public void setPortUids(String[] values)
  { _portUids = values;  }

  public String[] getPortUids()
  { return _portUids; }

  public String getAlertType()
  {
    return _alertType;
  }

  public String[] getUserDefinedAlertUids()
  {
    return _userDefinedAlertUids;
  }

  public void setAlertType(String alertType)
  {
    _alertType = alertType;
  }

  public void setUserDefinedAlertUids(String[] userDefinedAlertUids)
  {
    _userDefinedAlertUids = userDefinedAlertUids;
  }

  public String getDispatchInterval()
  {
    return _dispatchInterval;
  }

  public void setDispatchInterval(String string)
  {
    _dispatchInterval = string;
  }

  public String getDispatchCount()
  {
    return _dispatchCount;
  }

  public void setDispatchCount(String string)
  {
    _dispatchCount = string;
  }
}