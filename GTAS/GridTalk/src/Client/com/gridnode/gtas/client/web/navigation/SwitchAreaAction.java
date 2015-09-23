/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SwitchAreaAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;

public class SwitchAreaAction extends GTActionBase
{
  public static final String NAV_AREA_PARAMETER = "navArea";

  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    //ActionContext actionContext = new ActionContext(mapping,form,request,response);
    try
    {
      String newArea = request.getParameter(NAV_AREA_PARAMETER);
      if(newArea == null)
      {
        throw new NullPointerException("navArea parameter is null");
      }
      //@todo: check user authorised for that area?
      AreaResolver.setCurrentArea(request, newArea);

      String forwardName = "area." + newArea;
      ActionForward forward = mapping.findForward(forwardName);
      if(forward == null)
      {
        throw new NullPointerException("Unable to find ActionForward:" + forwardName);
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unexpected error caught in SwitchAreaAction",t);
    }
  }
}

