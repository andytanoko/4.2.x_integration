/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServerRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-03     Andrew Hill         Created
 * 2002-11-20     Andrew Hill         Added SecurityPassword
 * 2002-12-20     Andrew Hill         Render label for startBackendListener
 * 2003-03-19     Andrew Hill         Some mods to support new look & use renderLabelCarefully()
 * 2003-04-08     Andrew Hill         Use script url constants in IGlobals
 * 2003-04-14     Andrew Hill         Change Certificate Password Button
 * 2003-04-15     Andrew Hill         Hide menu if unregistered
 * 2003-05-17     Andrew Hill         Render i18n text for archiveDocument link
 * 2003-05-22     Andrew Hill         Render i18n text for restoreDocument link
 * 2003-06-02     Andrew Hill         Render i18n text for exportConfig, importConfig link
 * 2003-07-04     Andrew Hill         Ensure securityPassword field is visible if rendering an error for it
 * 2003-11-05     Andrew Hill         noSecurity hack support for GNDB00016109
 * 2004-01-12     Daniel D'Cotta      Added House Keeping 
 * 2006-04-24     Neo Sok Lay         Check for noP2P
 * 2009-04-12     Tam Wei Xiang       #122 - Add link to ScheduledArchive
 */
package com.gridnode.gtas.client.web.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class ServerRenderer extends AbstractRenderer
{
  private static final Log _log = LogFactory.getLog(ServerRenderer.class); // 20031209 DDJ

  public ServerRenderer( RenderingContext rContext )
  {
    super(rContext);
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      renderCommonFormElements("server",true);
      includeJavaScript(IGlobals.JS_SERVER_UTILS); //20030317AH, 20030408AH
      includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS); //20030408AH

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest()); //20030415AH

      if(gtasSession.isRegistered())
      {
        removeNode("unregistered", false); //20030415AH
        
        try
        {
          if(gtasSession.isPrivateCertificatePasswordKnown() && !(gtasSession.isNoSecurity())) //20031105AH - Add noSecurity condition
          {
            renderLabelCarefully("certificatePassword_link","server.changeCertificatePassword",false); //20030414AH
          }
          else
          { //20030424AH
            removeNode("certificatePassword_details",false); 
          }
        }
        catch(GTClientException e)
        { //20030505AH
          if(e.getRootException() instanceof NullPointerException)
          {
            if(_log.isDebugEnabled())
            {
              _log.error("Error determining if pcp known caught by ServerRenderer", e);
            }
            removeNode("certificatePassword_details",false);
          }
          else
          {
            throw e;
          }
        }
        
        //NSL20060424 Do not render connect/disconnect GM if noP2P is on
        if (!gtasSession.isNoP2P())
        {
        	renderLabelCarefully("connect_link","server.connect",false);
        	renderLabelCarefully("disconnect_link","server.disconnect",false);
        }
        else
        {
        	removeNode("connectGridMaster_details");
        }
        renderLabelCarefully("startBackendListener_link","server.startBackendListener",false); //20021220AH
        renderLabelCarefully("connect_wait_heading","server.connect.wait.heading",false);
        renderLabelCarefully("connect_wait_message","server.connect.wait.message",false);
        renderLabelCarefully("houseKeeping_link","server.houseKeeping",false); //20040112 DDJ
        renderLabelCarefully("archiveDocument_link","server.archiveDocument",false); //20030517AH
        renderLabelCarefully("scheduledArchiveDocument_link","server.scheduledArchiveDocument",false); //20090412 TWX
        renderLabelCarefully("restoreDocument_link","server.restoreDocument",false); //20030522AH

        ActionErrors actionErrors = rContext.getActionErrors();

        renderLabelCarefully("securityPassword_label","server.securityPassword",false);
        renderLabelCarefully("securityPassword_ok","server.securityPassword.ok",false);
        renderLabelCarefully("securityPassword_cancel","server.securityPassword.cancel",false);
        if(actionErrors != null)
        {
          ActionError securityPasswordAErr = MessageUtils.getFirstError(actionErrors, "securityPassword");
          if(securityPasswordAErr != null)
          {
            renderLabelCarefully("securityPassword_error", securityPasswordAErr.getKey(),false);
            appendOnloadEventMethod("askForSecurityPassword();"); //20030704AH - Also need to ensure its visible!
          }
        }
      }
      else
      { //20030415AH
        removeNode("menu",false);
        renderLabelCarefully("unregistered_message","generic.unregistered",false);
      }
      removeNode("common_buttons",false); //20030319AH
      renderLabelCarefully("exportConfig_link","server.exportConfig",false); //20030602AH
      renderLabelCarefully("importConfig_link","server.importConfig",false); //20030602AH
      
      
      if( gtasSession.isNoSecurity() )
      { //20031105AH
        Element connectLink = getElementById("connect_link",false);
        if(connectLink != null)
        {
          connectLink.setAttribute("href","javascript: connectToGridMaster();");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering registrationInfo screen",t);
    }
  }
}

