/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WebService.java
 *
 ****************************************************************************
 * Date						Author              Changes
 ****************************************************************************
 * Feb 6, 2004		Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class WebService extends AbstractEntity implements IWebService
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5506168691016444304L;
	protected String _wsdlUrl;
  protected String _endPoint;
  protected String _serviceName;
  protected String _serviceGroup;
  
  public WebService()
  {
  }

  // *************** Methods from AbstractEntity ***************************

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_serviceName).append('/').append(_serviceGroup).append('/').append(_wsdlUrl).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

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
