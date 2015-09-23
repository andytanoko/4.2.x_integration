/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommonContentRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-08     Andrew Hill         Created
 * 2002-10-2x     Andrew Hill         Autorender exceptions using ExceptionRenderer
 * 2002-12-14     Andrew Hill         Render GridNode Name
 * 2003-02-19     Andrew Hill         Fix bug when no IGTSession
 * 2003-02-21     Andrew Hill         Render rmocConfirm message script if appropriate
 * 2003-03-11     Andrew Hill         Render opConId as js variable
 * 2003-03-12     Andrew Hill         onunload handler rendering support
 * 2003-04-08     Andrew Hill         Use script urls from IGlobals
 * 2003-06-17     Andrew Hill         Render GridNode Id
 * 2003-07-09     Andrew Hill         Invoke exception renderer without needing an ActionError instance
 */
package com.gridnode.gtas.client.web.renderers;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.strutsbase.InvalidOperationContextException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.strutsbase.TaskDispatchAction;
 
/**
 * 20030312AH - From being a decorator for entityForms it is evolving into a general-purpose decorator used
 * all over the place to render stuff that stops everything falling to pieces...
 * ...of course theres now lots of redundant bits too...
 */
public class CommonContentRenderer  extends AbstractRenderer
{
  private IGTSession _gtasSession;
  private ActionMapping _mapping; //20030312AH

  public CommonContentRenderer(RenderingContext rContext,
                               IGTSession gtasSession,
                               ActionMapping mapping)
  {
    super(rContext);
    _gtasSession = gtasSession;
    _mapping = mapping;
  }

  public void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      boolean isDetailView = isDetailView();

      try
      { //20030221AH - Render code to ensure that confirmation of opCon removal will occur
        //if necessary
        OperationContext opCon = OperationContext.getOperationContext(rContext.getRequest());
        renderOpConId(rContext, opCon); //20030321AH - now render even if null
        if(opCon != null)
        {
          if(isDetailView)
          {
            renderLabel("test","INDETAILVIEW",false);
            renderRmocOnClose(rContext);
          }

          Boolean rmocConfirmationFlag = (Boolean)opCon.getAttribute(TaskDispatchAction.FLAG_RMOC_CONFIRMATION);
          if(Boolean.TRUE.equals(rmocConfirmationFlag))
          { //If the flag is set we render script to the page to set the message. If jalan()
            //finds this defined and the link includes 'rmoc' in the url, then it will prompt
            //for confirmation.
            String rmocConfirmMsg = rContext.getResourceLookup().getMessage("rmoc.confirm");
            //nb: be sure message in bundle doesnt use chars that would break the script ie: '
            addJavaScriptNode(null,"window.rmocConfirmMsg='" + rmocConfirmMsg + "'");
          }
        }
      }
      catch(InvalidOperationContextException ex)
      {
        //ignore (no opcon) - Actually this is probably bad as it means an opconId was spec'd but
        //the opCon wasnt there. Hmm... Maybe I should throw something?
      }

      if(_gtasSession != null)
      {
        String userId = _gtasSession.getUserId();
        replaceText("status_userId_value",userId,false);
        renderLabelCarefully("status_userId_label","gridtalk.status.userId",false);
      }
      else
      {
        removeNode("status_userId_value",false);
        removeNode("status_userId_label",false);
      }

      //20021214AH - Lookup gridnode name....
      Element gnNameNode = getElementById("gridNode_name",false);
      if(gnNameNode != null)
      {
        Element gnIdNode = getElementById("gridNode_id",false); //20030617AH
        if(_gtasSession != null)
        { //20030219AH - Test for null igtSession
          String gridNodeName = _gtasSession.getGridNodeName();
          if(gridNodeName == null)
          {
            gridNodeName = rContext.getResourceLookup().getMessage("gridTalk.status.unregistered");
            replaceText(gnIdNode," "); //20030617AH
            removeNode("gnSpacer",false); //20030617AH
          }
          else
          { //20030617AH
            Integer gnid = _gtasSession.getGridNodeId();
            replaceText(gnIdNode,"" + gnid);
          }
          replaceText(gnNameNode,gridNodeName); //20021214AH
        }
        else
        {
          replaceText(gnNameNode," "); //20030219AH (or should I remove it too?)
          replaceText(gnIdNode," "); //20030617AH
          removeNode("gnSpacer",false); //20030617AH
        }
      }
      //.............

      //20021214AH - Changed these to use careful (but slower) rendering to allow designers to
      //more easily include images (on the left onlY!!!) with these bits of text
      renderLabelCarefully("title","gridtalk.title",false);
      renderLabelCarefully("gridtalk_title_label","gridtalk.title",false);
      renderLabelCarefully("gridtalk_version_label","gridtalk.version",false); // 20021127 DDJ
      renderLabelCarefully("gridtalk_build_label","gridtalk.build",false);     // 20021127 DDJ
      renderLabelCarefully("processing_request_message","generic.communicatingWithServer",false);
      //...

      ActionErrors actionErrors = rContext.getActionErrors();
      Throwable throwable = (Throwable)rContext.getRequest().getAttribute(IRequestKeys.REQUEST_EXCEPTION); //20030709AH
      if( (actionErrors != null) || (throwable != null) ) //20030709AH
      {
        if( (MessageUtils.getFirstError(actionErrors, IGlobals.EXCEPTION_ERROR_PROPERTY) != null)
            || (throwable != null) ) //20030709AH
        {
          ExceptionRenderer eRenderer = new ExceptionRenderer(rContext);
          eRenderer.render(_target);
        }
        else
        {
          removeNode("insert_error_parent",false);
        }
      }
      else
      {
        removeNode("insert_error_parent",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering common content",t);
    }
  }

  protected void renderOpConId(  RenderingContext rContext,
                                 OperationContext opCon)
    throws RenderingException
  { //20030311AH, 20030321AH - Always render jsOpConId
    try
    {
      String jsOpConScript = "jsOpConId = "
                             + (opCon == null ? "null" : "'" + opCon.getOperationContextId() + "'")
                             + ";"; //nb: var name jsOpConId also hardcoded in several javascripts
      addJavaScriptNode(null,jsOpConScript);

      if(opCon != null) //20030321AH
      {
        //20030312AH - while we are here we now also need to do funny stuff to handle window closure sometimes
        //@todo: only render for detail views where its needed (nb: its only rendered where there is
        //an opCon anyhow which seems to cover most of the bases already)
        includeJavaScript(IGlobals.JS_NAVIGATION_METHODS); //20030408AH
        appendEventMethod(getBodyNode(),"onunload","checkWindowClosing();");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error in renderOpConId",t);
    }
  }

  protected void renderRmocOnClose( RenderingContext rContext )
    throws RenderingException
  { //20030311AH
    try
    {
      if(_mapping == null) return;
      StringBuffer buffer = new StringBuffer();

      ActionForward closeWindow = _mapping.findForward(TaskDispatchAction.CLOSE_WINDOW_FORWARD);
      if(closeWindow == null) throw new NullPointerException("Internal Assertion Failed: closeWindow==null");
      String rmocUrl = rContext.getUrlRewriter().rewriteURL(closeWindow.getPath(),true);

      buffer.append("rmocUrl = '"); //20030409AH
      buffer.append(rmocUrl); //20030409AH
      buffer.append("';\n"); //20030409AH
      buffer.append("function rmocOnClose()\n{\n");
      buffer.append("performRmoc(rmocUrl);\n"); //20030318AH
      buffer.append("}");

      addJavaScriptNode(null,buffer.toString());
      includeJavaScript(IGlobals.JS_NAVIGATION_METHODS); //Though if I havent already Ill be surprised!
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error in renderRmocOnClose",t);
    }
  }
}