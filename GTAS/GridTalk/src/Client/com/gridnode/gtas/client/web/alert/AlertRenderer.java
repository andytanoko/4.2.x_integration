/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.ctrl.IGTAlertEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class AlertRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number fields[] =
  {
    IGTAlertEntity.NAME,
    IGTAlertEntity.DESCRIPTION,
    IGTAlertEntity.TYPE,
    IGTAlertEntity.ACTIONS,
  };

  public AlertRenderer(RenderingContext rContext,
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
      //AlertAForm form = (AlertAForm)getActionForm();
      IGTAlertEntity alert = (IGTAlertEntity)getEntity();

      renderCommonFormElements(alert.getType(), _edit);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, alert, fields);

      if(_edit)
      {
        renderLabel("actions_create", "alert.action.create", false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering alert screen",t);
    }
  }
}