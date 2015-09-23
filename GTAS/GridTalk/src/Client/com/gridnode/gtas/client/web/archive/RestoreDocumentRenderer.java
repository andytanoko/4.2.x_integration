/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RestoreDocumentRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-22     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.archive;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTRestoreDocument;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class RestoreDocumentRenderer extends AbstractRenderer
{
  private static final Number[] _fields =
  {
    IGTRestoreDocument.ARCHIVE_FILE,
  };
  
  private boolean _edit;

  public RestoreDocumentRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    if(!_edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
  }

  protected void render() throws RenderingException
  {
    try
    {
      //RenderingContext rContext = getRenderingContext();
      renderCommonFormElements(IGTEntity.ENTITY_RESTORE_DOCUMENT,_edit);
      RestoreDocumentAForm form = (RestoreDocumentAForm)getActionForm();
      
      IGTRestoreDocument restoreDocument = (IGTRestoreDocument)getEntity();
      
      if(restoreDocument.isNewEntity())
      {
        renderLabelCarefully("edit_message", "restoreDocument.edit.message.upload", false);     
        //BindingFieldPropertyRenderer bfpr = 
        renderFields( null,
        		restoreDocument,
        		_fields,
        		null,
        		form,
        		null);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering restoreDocument screen",t);
    }
  }
}