/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupResultAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-02     Andrew Hill         Created
 * 2002-11-14     Andrew Hill         Added securityPassword property
 */
package com.gridnode.gtas.client.web.connection;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.utils.StaticUtils;

public class ConnectionSetupResultAForm extends GTActionFormBase
{
  private String _currentLocation;
  private String _servicingRouter;
  private String _originalLocation;
  private String _originalServicingRouter;
  private String _status;
  private String _failureReason;
  private String _securityPassword;

  private String _availableRoutersOrder;
  private String _availableGridMastersOrder;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    
  }

  public String getCurrentLocation()
  { return _currentLocation; }

  public void setCurrentLocation(String currentLocation)
  { _currentLocation=currentLocation; }

  public String getServicingRouter()
  { return _servicingRouter; }

  public void setServicingRouter(String servicingRouter)
  { _servicingRouter=servicingRouter; }

  public String getOriginalLocation()
  { return _originalLocation; }

  public void setOriginalLocation(String originalLocation)
  { _originalLocation=originalLocation; }

  public String getOriginalServicingRouter()
  { return _originalServicingRouter; }

  public void setOriginalServicingRouter(String originalServicingRouter)
  { _originalServicingRouter=originalServicingRouter; }

  public String getStatus()
  { return _status; }

  public Short getStatusShort()
  { return StaticUtils.shortValue(_status); }

  public void setStatus(String status)
  { _status=status; }

  public String getFailureReason()
  { return _failureReason; }

  public void setFailureReason(String failureReason)
  { _failureReason=failureReason; }

  public void setSecurityPassword(String securityPassword)
  { _securityPassword = securityPassword; }

  public String getSecurityPassword()
  { return _securityPassword; }

  public void setAvailableRoutersOrder(String values)
  { _availableRoutersOrder = values; }

  public String getAvailableRoutersOrder()
  { return _availableRoutersOrder; }

  public void setAvailableGridMastersOrder(String values)
  { _availableGridMastersOrder = values; }

  public String getAvailableGridMastersOrder()
  { return _availableGridMastersOrder; }
}