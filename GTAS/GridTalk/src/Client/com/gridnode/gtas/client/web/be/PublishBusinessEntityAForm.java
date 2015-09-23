/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class PublishBusinessEntityAForm extends GTActionFormBase
{
  private String[]  _be;
  private String    _registryConnectInfo;
  /**
   * @return
   */
  public String[] getBe()
  {
    return _be;
  }

  /**
   * @param strings
   */
  public void setBe(String[] strings)
  {
    _be = strings;
  }

  /**
   * @return
   */
  public String getRegistryConnectInfo()
  {
    return _registryConnectInfo;
  }

  /**
   * @param string
   */
  public void setRegistryConnectInfo(String string)
  {
    _registryConnectInfo = string;
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _be = new String[] {};
  }
}