/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 * 2003-05-21     Andrew Hill         Support for the isAttachResponseDoc field
 */
package com.gridnode.gtas.client.web.alert;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ResponseTrackRecordAForm extends GTActionFormBase
{
  private String _sentDocType;
  private String _sentDocIdXpath;
  private String _startTrackDateXpath;
  private String _responseDocType;
  private String _responseDocIdXpath;
  private String _receiveResponseAlert;
  private String _alertRecipientXpath;
//  private String[] _reminderAlerts;
  private String _reminderAlertsOrder;
  private String[] _reminderAlertsOrderExploded;
  private String _isAttachResponseDoc = "false"; //20030521AH

  public String getSentDocType()
  { return _sentDocType; }

  public void setSentDocType(String sentDocType)
  { _sentDocType=sentDocType; }

  public String getSentDocIdXpath()
  { return _sentDocIdXpath; }

  public void setSentDocIdXpath(String sentDocIdXpath)
  { _sentDocIdXpath=sentDocIdXpath; }

  public String getStartTrackDateXpath()
  { return _startTrackDateXpath; }

  public void setStartTrackDateXpath(String startTrackDateXpath)
  { _startTrackDateXpath=startTrackDateXpath; }

  public String getResponseDocType()
  { return _responseDocType; }

  public void setResponseDocType(String responseDocType)
  { _responseDocType=responseDocType; }

  public String getResponseDocIdXpath()
  { return _responseDocIdXpath; }

  public void setResponseDocIdXpath(String responseDocIdXpath)
  { _responseDocIdXpath=responseDocIdXpath; }

  public String getReceiveResponseAlert()
  { return _receiveResponseAlert; }

  public void setReceiveResponseAlert(String receiveResponseAlert)
  { _receiveResponseAlert=receiveResponseAlert; }

  public String getAlertRecipientXpath()
  { return _alertRecipientXpath; }

  public void setAlertRecipientXpath(String alertRecipientXpath)
  { _alertRecipientXpath=alertRecipientXpath; }

//  public String[] getReminderAlerts()
//  { return _reminderAlerts; }
//
//  public Collection getReminderAlertsCollection()
//  { return StaticUtils.collectionValue(_reminderAlerts); }
//
//  public void setReminderAlerts(String[] reminderAlerts)
//  { _reminderAlerts=reminderAlerts; }

  public void setReminderAlertsOrder(String values)
  {
    _reminderAlertsOrder = values;
    _reminderAlertsOrderExploded = StaticUtils.explode(values,",");
  }

  public void setReminderAlertsOrderExploded(String[] values)
  {
    _reminderAlertsOrderExploded = values;
    _reminderAlertsOrder = StaticUtils.implode(values,",");
  }

  public void initReminderAlertsOrder(int size)
  {
    _reminderAlertsOrderExploded = new String[size];
    for(int i=0; i < size; i++)
    {
      _reminderAlertsOrderExploded[i] = "" + i;
    }
    _reminderAlertsOrder = StaticUtils.implode(_reminderAlertsOrderExploded,",");
  }

  public String[] getReminderAlertsOrderExploded()
  {
    return _reminderAlertsOrderExploded;
  }

  public String getReminderAlertsOrder()
  { return _reminderAlertsOrder; }

  public void doReset(ActionMapping p0, HttpServletRequest p1)
  {
    //_reminderAlerts = null; //20030521AH - (this was commented out already so I leave it so)
    setIsAttachResponseDoc("false");
  }

  public void setIsAttachResponseDoc(String isAttachResponseDoc)
  {
    _isAttachResponseDoc = isAttachResponseDoc;
  }

  public String getIsAttachResponseDoc()
  {
    return _isAttachResponseDoc;
  }
}