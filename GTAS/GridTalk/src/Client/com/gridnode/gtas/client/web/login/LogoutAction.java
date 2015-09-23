/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LogoutAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-13     Andrew Hill         Created
 * 2002-07-06     Andrew Hill         Catch and ignore NoSessionException
 * 2002-11-06     Andrew Hill         Clear LogoutListener
 */
package com.gridnode.gtas.client.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.ISessionKeys;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;

 
public class LogoutAction extends GTActionBase
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return null;
  }

  public ActionForward execute(ActionMapping mapping,
				                        ActionForm form,
				                        HttpServletRequest request,
				                        HttpServletResponse response)
	                              throws GTClientException
  {
    try
    {
      try
      {
        HttpSession session = request.getSession();
        LogoutListener.clearListener(session);
        IGTSession gtasSession = getGridTalkSession(session);
        gtasSession.logout();
        session.removeAttribute(ISessionKeys.GTAS_SESSION);
        session.invalidate();
      }
      //catch(NoSessionException nse) // 20031223 DDJ: Solve problem of customers when they log out
      catch(Throwable ex) 
      {
        
      }
      return mapping.findForward("login");
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error during logout",t);
    }
  }
}

