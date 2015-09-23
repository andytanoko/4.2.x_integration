/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.ctrl.IGTRfcEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class RfcRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number[] _mainFields =
  {
    IGTRfcEntity.NAME,
    IGTRfcEntity.DESCRIPTION,
    IGTRfcEntity.HOST,
    IGTRfcEntity.PORT_NUMBER,
    IGTRfcEntity.USE_COMMAND_FILE,
  };
  private static final Number[] _cmdFields =
  {
    IGTRfcEntity.COMMAND_FILE_DIR,
    IGTRfcEntity.COMMAND_FILE,
    IGTRfcEntity.COMMAND_LINE,
  };

  public RfcRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      //RenderingContext rContext = getRenderingContext();
      RfcAForm form = (RfcAForm)getActionForm();
      IGTRfcEntity rfc = (IGTRfcEntity)getEntity();
      renderCommonFormElements(rfc.getType(),_edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null,rfc,_mainFields);
      boolean useCommandFile = StaticUtils.primitiveBooleanValue( form.getUseCommandFile() );
      if(useCommandFile)
      {
        renderFields(bfpr,rfc,_cmdFields);
      }
      else
      {
        removeNode("commandFileDir_details",false);
        removeNode("commandFile_details",false);
        removeNode("commandLine_details",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering rfc screen",t);
    }
  }
}