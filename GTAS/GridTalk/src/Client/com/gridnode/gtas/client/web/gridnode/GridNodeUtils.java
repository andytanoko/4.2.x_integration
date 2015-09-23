/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.gridnode;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
 
public class GridNodeUtils
{
  static final String NAVGROUP = "navgroup_be";
  static final int MANAGER_TYPE = IGTManager.MANAGER_GRIDNODE;

  static Short getState(ActionContext actionContext)
  {
    Short state = (Short)actionContext.getAttribute("state");
    if(state == null)
    {
      String stateStr = actionContext.getRequest().getParameter("state");
      state = StaticUtils.shortValue(stateStr);
      actionContext.setAttribute("state",state);
    }
    return state;
  }

  static String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    Short state = getState(actionContext);
    if(state != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,"state",state.toString());
    }
    return refreshUrl;
  }
}