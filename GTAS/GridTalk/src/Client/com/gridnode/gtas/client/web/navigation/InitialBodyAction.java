/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitialBodyAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-17     Andrew Hill         Created
 * 2003-03-24     Andrew Hill         Internal assertion checking
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * Will forward the user to the appropriate initial page based on whether or not they are registered
 * or an admin or user type user.
 */
public class InitialBodyAction extends GTActionBase
{
  public static final String ADMIN_AREA_FWD = "area.admin";
  public static final String USER_AREA_FWD  = "area.user";
  public static final String REGISTER_FWD   = "register";

  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception
  {
  	//2005 Nov 21 SUMEDH
  	log("isToNwInitialBodyAction = " + isToNwInitialBodyAction(request));
  	if (isToNwInitialBodyAction(request))
  	{
  		ActionForward forward = mapping.findForward("NwInitialBody");
  		AssertUtil.assertTrue(forward != null);
  		return forward;
  	}
  	
    try
    {
      IGTSession gtasSession = this.getGridTalkSession(request);
      String forwardName = null;
      if(gtasSession.isRegistered(true)) //20030502AH
      {
        String area = AreaResolver.getCurrentArea(request);

        if(AreaResolver.ADMIN_AREA.equals(area))
        {
          forwardName = ADMIN_AREA_FWD; //20030324AH
        }
        else
        {
          forwardName = USER_AREA_FWD; //20030324AH
        }
      }
      else
      {
        forwardName = REGISTER_FWD; //20030324AH
      }
      ActionForward forward = mapping.findForward(forwardName); //20030324AH
      if(forward == null)
      { //20030324AH
        throw new NullPointerException("Unable to find ActionForward:" + forwardName);
      }
      return (forward);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unexpected error caught in InitialBodyAction",t);
    }
  }
  
  private boolean isToNwInitialBodyAction(HttpServletRequest request)
  {
  	HttpSession session = request.getSession();
  	return session.getAttribute("nw_forward") != null;
  }
  
  private void log(String message)
  {
  	com.gridnode.gtas.client.netweaver.helper.Logger.debug("[InitialBodyAction]" + message);
  }
}

