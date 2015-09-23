/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-29     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.ctrl.IGTActionEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ActionRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number fields[] =
  {
    IGTActionEntity.NAME,
    IGTActionEntity.DESCRIPTION,
    IGTActionEntity.MSG_ID,
  };

  public ActionRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      //RenderingContext rContext = getRenderingContext();
      //ActionAForm form = (ActionAForm)getActionForm();
      IGTActionEntity action = (IGTActionEntity)getEntity();

      renderCommonFormElements(action.getType(), _edit);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, action, fields);

      if(_edit)
      {
        renderLabel("msgId_create", "action.messageTemplate.create", false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering action.alert screen",t);
    }
  }
}