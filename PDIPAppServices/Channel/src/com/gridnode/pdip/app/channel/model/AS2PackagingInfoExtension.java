/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2PackagingInfoExtension.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 30 2003      Guo Jianyu              Created
 */
package com.gridnode.pdip.app.channel.model;

public class AS2PackagingInfoExtension
  extends PackagingInfoExtension
  implements IAS2PackagingInfoExtension
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4013862872075492586L;
	private boolean _isAckReq;
  private boolean _isAckSigned;
  private boolean _isNRRReq;
  private boolean _isAckSyn;
  private String _returnURL;

  private String _className = null;
  private String _methodName = null;

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return null;
  }

  public boolean getIsAckReq()
  {
    return _isAckReq;
  }

  public void setIsAckReq(boolean isAckReq)
  {
    _isAckReq = isAckReq;
  }

  public boolean getIsAckSigned()
  {
    return _isAckSigned;
  }

  public void setIsAckSigned(boolean isAckSigned)
  {
    _isAckSigned = isAckSigned;
  }

  public boolean getIsNRRReq()
  {
    return _isNRRReq;
  }

  public void setIsNRRReq(boolean isNRRReq)
  {
    _isNRRReq = isNRRReq;
  }

  public boolean getIsAckSyn()
  {
    return _isAckSyn;
  }

  public void setIsAckSyn(boolean isAckSyn)
  {
    _isAckSyn = isAckSyn;
  }

  public String getReturnURL()
  {
    return _returnURL;
  }

  public void setReturnURL(String returnURL)
  {
    _returnURL = returnURL;
  }

  public String getEntityDescr()
  {
    return getEntityName() + "/" + getClassName() + "/" + getMethodName();
  }

  public void setClassName(String className)
  {
    _className = className;
  }

  public void setMethodName(String methodName)
  {
    _methodName = methodName;
  }

  public String getClassName()
  {
    return _className;
  }

  public String getMethodName()
  {
    return _methodName;
  }

}