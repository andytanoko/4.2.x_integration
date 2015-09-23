/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-26     Andrew Hill         Created (Moved code here to share)
 */
package com.gridnode.gtas.client.web.activation;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

/**
 * Some utility code & constants that can be shared between list action, renderer, and dispatch action
 */
public class ActivationUtils
{
  static final String NAVGROUP = "navgroup_be";
  static final int MANAGER_TYPE = IGTManager.MANAGER_ACTIVATION_RECORD;

  static String getFilterType(ActionContext actionContext)
  {
    String filterType = (String)actionContext.getAttribute("filterType");
    if("".equals(filterType)) filterType = null;
    if(filterType == null)
    {
      filterType = actionContext.getRequest().getParameter("filterType");
      actionContext.setAttribute("filterType",filterType);
    }
    return filterType;
  }

  static String getSummaryType(ActionContext actionContext)
  { //20021202AH
    String summaryType = (String)actionContext.getAttribute("summaryType");
    if("".equals(summaryType)) summaryType = null;
    if(summaryType == null)
    {
      summaryType = actionContext.getRequest().getParameter("summaryType");
      actionContext.setAttribute("summaryType",summaryType);
    }
    return summaryType;
  }

  static String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    String filterType = getFilterType(actionContext);
    if(filterType != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,"filterType",filterType);
    }
    return refreshUrl;
  }
}