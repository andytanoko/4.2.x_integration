/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBeDispatchActionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         Modified for gn selection instead of channel
 * 2003-03-25     Andrew Hill         Support for newUi
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.*;
import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.utils.StaticUtils;

public class SendBeDispatchAction extends TaskDispatchAction
{
    /*
      20030325AH - We currently have a bit of a problem with this action as we are coming from
      the listview page and inheriting its opCon. May be we should refactor the listview to
      submit to itself and then do a diversion here. This is complicated by the fact that
      EntityListAction, although it uses an opCon is for historical reasons
      NOT an OperationDispatchAction. (Its not even a DispatchAction!)
    */

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SendBeRenderer(rContext);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SendBeAForm();
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return IDocumentKeys.SEND_BE_UPDATE;
  }

  protected boolean validate(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    SendBeAForm form = (SendBeAForm)actionContext.getActionForm();

    String[] beUids = form.getBeUids();
    if( (beUids == null) || (beUids.length == 0) )
    {
      EntityFieldValidator.addFieldError( errors,"beUids","sendBe",
                                          EntityFieldValidator.REQUIRED,null); //20030115AH
    }
    String[] gnUids = form.getGnUids(); //20030115AH
    if( (gnUids == null) || (gnUids.length == 0) )
    { //20030115AH
      EntityFieldValidator.addFieldError( errors,"gnUids","sendBe",
                                          EntityFieldValidator.REQUIRED,null); //20030115AH
    }
    return (errors.size() != 0);
  }

  protected boolean doComplete(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    boolean failed = validate(actionContext, errors);
    if(!failed)
    {
      SendBeAForm form = (SendBeAForm)actionContext.getActionForm();
      IGTSession gtasSession = getGridTalkSession(actionContext);
      IGTBusinessEntityManager beMgr = (IGTBusinessEntityManager)
                                        gtasSession.getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
      //20030115AH
      Collection beUids = StaticUtils.getLongCollection( form.getBeUids() );
      Collection gnUids = StaticUtils.getLongCollection( form.getGnUids() );
      beMgr.send(beUids,gnUids);
      //...
    }
    return failed;
  }

  protected void initialiseActionForm(ActionContext actionContext)
    throws GTClientException
  { // Prepopulate list of BEs to send with those that were selected in the list view (if any)
    SendBeAForm form = (SendBeAForm)actionContext.getActionForm();
    form.setBeUids( actionContext.getRequest().getParameterValues("uid") );
  }

  protected boolean forceUseDvTemplate(ActionContext actionContext) throws GTClientException
  { //20030325AH
    return true;
  }
}