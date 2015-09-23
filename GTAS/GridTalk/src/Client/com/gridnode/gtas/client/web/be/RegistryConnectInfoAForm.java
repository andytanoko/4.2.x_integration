/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class RegistryConnectInfoAForm extends GTActionFormBase
{
  private String _name;
  private String _publishUrl;
  private String _queryUrl;
  private String _publishUser;
  private String _publishPassword;

  /**
   * @return
   */
  public String getName()
  {
    return _name;
  }

  /**
   * @return
   */
  public String getPublishPassword()
  {
    return _publishPassword;
  }

  /**
   * @return
   */
  public String getPublishUrl()
  {
    return _publishUrl;
  }

  /**
   * @return
   */
  public String getPublishUser()
  {
    return _publishUser;
  }

  /**
   * @return
   */
  public String getQueryUrl()
  {
    return _queryUrl;
  }

  /**
   * @param string
   */
  public void setName(String string)
  {
    _name = string;
  }

  /**
   * @param string
   */
  public void setPublishPassword(String string)
  {
    _publishPassword = string;
  }

  /**
   * @param string
   */
  public void setPublishUrl(String string)
  {
    _publishUrl = string;
  }

  /**
   * @param string
   */
  public void setPublishUser(String string)
  {
    _publishUser = string;
  }

  /**
   * @param string
   */
  public void setQueryUrl(String string)
  {
    _queryUrl = string;
  }
}