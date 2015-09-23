/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryQueryAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class SearchRegistryQueryAForm extends GTActionFormBase
{
  private String    _searchId;
  private String    _dtSubmitted;
  private String    _dtResponded;
  private String    _exceptionStr;

  // embeded SearchRegistryCriteria fields
  private String    _busEntityDesc;
  private String    _duns;
  private String[]  _messagingStandards;
  private String    _match;
  private String    _queryUrl;
  
  /**
   * @return
   */
  public String getSearchId()
  {
    return _searchId;
  }

  public Long getSearchIdLong()
  {
    return StaticUtils.longValue(_searchId);
  }

  /**
   * @param string
   */
  public void setSearchId(String string)
  {
    _searchId = string;
  }

  /**
   * @return
   */
  public String getDtSubmitted()
  {
    return _dtSubmitted;
  }

  /**
   * @param string
   */
  public void setDtSubmitted(String string)
  {
    _dtSubmitted = string;
  }

  /**
   * @return
   */
  public String getDtResponded()
  {
    return _dtResponded;
  }

  /**
   * @param strings
   */
  public void setDtResponded(String string)
  {
    _dtResponded = string;
  }

  /**
   * @return
   */
  public String getExceptionStr()
  {
    return _exceptionStr;
  }

  /**
   * @param string
   */
  public void setExceptionStr(String string)
  {
    _exceptionStr = string;
  }

  /**
   * @return
   */
  public String getBusEntityDesc()
  {
    return _busEntityDesc;
  }

  /**
   * @param string
   */
  public void setBusEntityDesc(String string)
  {
    _busEntityDesc = string;
  }

  /**
   * @return
   */
  public String getDuns()
  {
    return _duns;
  }

  /**
   * @param string
   */
  public void setDuns(String string)
  {
    _duns = string;
  }

  /**
   * @return
   */
  public String[] getMessagingStandards()
  {
    return _messagingStandards;
  }

  /**
   * @param strings
   */
  public void setMessagingStandards(String[] strings)
  {
    _messagingStandards = strings;
  }

  /**
   * @return
   */
  public String getMatch()
  {
    return _match;
  }

  public Short getMatchShort()
  {
    return StaticUtils.shortValue(_match);
  }

  /**
   * @param string
   */
  public void setMatch(String string)
  {
    _match = string;
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
  public void setQueryUrl(String string)
  {
    _queryUrl = string;
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _messagingStandards = new String[] {};
  }
}