/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerWatchListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-11     Andrew Hill         Created
 * 2003-03-26     Andrew Hill         Refactored. Outputs direct to response!
 */
package com.gridnode.gtas.client.web.be;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.strutsbase.GTWatchListActionMapping;
import com.gridnode.gtas.client.web.strutsbase.NoSessionException;
import com.gridnode.gtas.model.enterprise.IServerWatchlist;

public class PartnerWatchListAction extends GTActionBase
{
  private static final String WATCHLIST_FORWARD = "partnerWatchList";
  private static final String TITLE_LABEL_KEY = "partner.watchList.heading";

  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  { //20030326AH
    try
    {

      IGTSession gtasSession = null;
      try
      {
        gtasSession = StaticWebUtils.getGridTalkSession(request.getSession());
      }
      catch(NoSessionException nse)
      {
        ActionForward forward = mapping.findForward("closeWindow");
        return forward;
      }
      Object[] partnerWatchListData = getPartnerWatchListData(gtasSession);
      renderPwl(gtasSession, mapping, partnerWatchListData, request, response);
      return null;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in partnerWatchList action",t);
    }
  }

  protected Object[] getPartnerWatchListData(IGTSession gtasSession)
    throws GTClientException
  {
    try
    {
      IGTPartnerManager manager = (IGTPartnerManager)gtasSession.getManager(IGTManager.MANAGER_PARTNER);
      return manager.getPartnerWatchListData();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get partner watch list data",t);
    }
  }

  protected void renderPwl( IGTSession gtasSession,
                            ActionMapping mapping,
                            Object[] pwlData,
                            HttpServletRequest request,
                            HttpServletResponse response)
    throws GTClientException
  {
    /**
     * 20030326AH
     * We are rendering directly to the response instead of using our nice comfortable
     * rendering technology, as we may well later need to move the watchlist into its own
     * (rather minimal) war file as a seperate application to allow it to have its own session
     * - which with cookies enabled will need its own context path (not tested) otherwise it
     * will just get the same shared session cookie (I think).
     * This is to meet the requirement that the watchlist to work even after the user has
     * logged out, and that while visible it does not reset the timeout on the users session.
     * We do not currently meet these two conditions.
     * Rendering directly to the output will reduce the amount of refactoring we must do when this
     * time comes, although we still have a lot of other things to deal with. We will need to change
     * from a struts action to a servlet - and say goodbye to our mappings and such like. We will
     * also need to create a special type of IGTSession for firing the necessary events etc...
     * i18n will be another big problem. (Unless we decide to include struts in this extra war -
     * which will make it rather heavy...) :-(
     */
    try
    {
      Locale loc = (Locale)request.getSession().getAttribute(GTActionBase.LOCALE_KEY);
      if (loc == null) loc = Locale.getDefault();
      MessageResources res = getResources(request);
      if(res == null)
      {
        throw new NullPointerException("messageResources is null");
      }

      //Object[] gridMasterDetails = (Object[])pwlData[IServerWatchlist.MAIN_INDEX_GRIDMASTER];
      Short connectionStatus = (Short)pwlData[IServerWatchlist.MAIN_INDEX_STATUS];
      String statusString = null;
      if(IServerWatchlist.STATUS_ONLINE.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.online",null);
      }
      else if(IServerWatchlist.STATUS_OFFLINE.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.offline",null);
      }
      else if(IServerWatchlist.STATUS_EXPIRED.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.expired",null);
      }
      else if(IServerWatchlist.STATUS_DETERMINING.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.determining",null);
      }
      else if(IServerWatchlist.STATUS_UNKNOWN.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.unknown",null);
      }
      else if(IServerWatchlist.STATUS_CONNECTING.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.connecting",null);
      }
      else if(IServerWatchlist.STATUS_RECONNECTING.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.reconnecting",null);
      }
      else if(IServerWatchlist.STATUS_DISCONNECTING.equals(connectionStatus))
      {
        statusString = getMessage(loc,res,"gridTalk.status.connection.disconnecting",null);
      }
      else
      { //20030326AH - what is this for????
        Object[] statusArray = new Object[1];
        statusArray[0] = connectionStatus;
        statusString = getMessage(loc,res,"gridTalk.status.connection.online",statusArray);
      }

      Integer gridNodeId = gtasSession.getGridNodeId();
      String gnid = (gridNodeId == null) ? getMessage(loc,res,"gridTalk.status.unregistered",null)
                                         : gridNodeId.toString();

      String pwlTitle = getMessage(loc, res, TITLE_LABEL_KEY, null);


      StringBuffer buffer = new StringBuffer(512);
      buffer.append("<head>");
      buffer.append("<title>");
      buffer.append(pwlTitle);
      buffer.append(" (");
      buffer.append(gnid);
      buffer.append(") ");
      buffer.append(statusString);
      buffer.append("</title>");

      buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"gt.css\"/>");
      buffer.append("</head>");
      buffer.append("<body ");
      long interval = 0;
      if(mapping instanceof GTWatchListActionMapping)
      {
        interval = ((GTWatchListActionMapping)mapping).getPwlRefreshInterval();
      }
      if(interval > 0)
      {
        ActionForward forward = mapping.findForward(WATCHLIST_FORWARD);
        if(forward == null) throw new NullPointerException("Couldnt find forward:" + WATCHLIST_FORWARD);
        String url = response.encodeURL( request.getContextPath() + forward.getPath());

        buffer.append("onload=\"setTimeout('location.replace(\\'" + url + "\\');',");
        buffer.append(interval);
        buffer.append(");\" ");
      }
      buffer.append("topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\">");
      buffer.append("<div align=\"center\">");
      buffer.append("<table>");
      buffer.append("<tr>");
      buffer.append("<td align=\"center\">");
      buffer.append("<img src=\"images/pwl/offline.gif\"/><br/>");
      buffer.append("<select multiple=\"multiple\" rows=\"3\" class=\"pwlselect\">");
      insertPartners(buffer, pwlData, false);
      buffer.append("</select>");
      buffer.append("</td>");

      buffer.append("<td align=\"center\">");
      buffer.append("<img src=\"images/pwl/online.gif\"/><br/>");
      buffer.append("<select multiple=\"multiple\" rows=\"3\" class=\"pwlselect\">");
      insertPartners(buffer, pwlData, true);
      buffer.append("</select>");
      buffer.append("</td>");

      buffer.append("</tr>");
      buffer.append("</table");
      buffer.append("</div>");

      response.getOutputStream().print(buffer.toString());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error rendering PartnerWatchList direct to response",t);
    }
  }

  protected void insertPartners(StringBuffer buffer, Object[] pwlData, boolean online) throws GTClientException
  { //20030326AH
    if(pwlData == null) throw new NullPointerException("pwlData is null");
    Object[] partnerDetails = (Object[])pwlData[IServerWatchlist.MAIN_INDEX_PARTNER];
    if(partnerDetails == null) return;
    for(int i = 0; i < partnerDetails.length; i++)
    {
      Object[] partner = (Object[])partnerDetails[i];
      Short status = (Short)partner[IServerWatchlist.PARTNER_INDEX_STATUS];
      boolean insert = online ? IServerWatchlist.STATUS_ONLINE.equals(status)
                              : !IServerWatchlist.STATUS_ONLINE.equals(status);
      if(insert)
      {
        String name = (String)partner[IServerWatchlist.PARTNER_INDEX_NAME];
        buffer.append("<option>");
        buffer.append(name);
        buffer.append("</option>");
      }
    }
  }

  private String getMessage(Locale loc, MessageResources res, String key, Object[] params)
  {
    String msg = (params==null) ? res.getMessage(loc,key) : res.getMessage(loc,key,params);
    return (msg==null) ? "?" + key + "?" : msg;
  }
}

