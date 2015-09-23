/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WebServiceAForm.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class WebServiceAForm extends GTActionFormBase
{
  private String _wsdlUrl;
  private String _endPoint;
  private String _serviceName;
  private String _serviceGroup;
  

  // ************************* Getters for attributes ***********************

  public String getEndPoint()
  { 
    return _endPoint;
  }

  public String getServiceGroup()
  {
    return _serviceGroup;
  }

  public String getServiceName()
  {
    return _serviceName;
  }

  public String getWsdlUrl()
  {
    return _wsdlUrl;
  }

  //********************** Setters for attributes ***************************

  public void setEndPoint(String endPoint)
  {
    _endPoint = endPoint;
  }

  public void setServiceGroup(String serviceGroup)
  {
    _serviceGroup = serviceGroup;
  }

  public void setServiceName(String serviceName)
  {
    _serviceName = serviceName;
  }

  public void setWsdlUrl(String wsdlUrl)
  {
    _wsdlUrl = wsdlUrl;
  }
  
}