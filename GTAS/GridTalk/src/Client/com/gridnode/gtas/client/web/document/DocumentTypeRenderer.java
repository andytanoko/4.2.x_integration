/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 * 2002-08-01     ANdrew Hill         Refactored due to new metaInfo goodies :-)
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;

public class DocumentTypeRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = new Number[]{IGTDocumentTypeEntity.DESCRIPTION,
                                                        IGTDocumentTypeEntity.DOC_TYPE, };

  public DocumentTypeRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_DOCUMENT_TYPE,_edit);
      renderFields(null, getEntity(), fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering documentType screen",t);
    }
  }

  /*protected void render() throws RenderingException
  {
    IGTEntity entity;
    try
    {
      entity = getEntity();
    }
    catch(Exception e)
    {
      throw new RenderingException("Couldnt get entity",e);
    }
    renderFormAction("form");
    renderOperationContext();
    if(_edit)
    {
      if(!entity.isNewEntity())
      { // Change input box to text if its not new
        this.replaceValueNodeWithNewElement("span",IGTDocumentTypeEntity.DOC_TYPE,true,false);
      }
      renderLabel("ok","documentType.edit.ok");
    }
    else
    {
      renderLabel("edit","documentType.view.edit");
    }
    renderLabel("heading","documentType.edit.heading");
    IValueElement name = createValueElement(IGTDocumentTypeEntity.DOC_TYPE);
    if(!entity.isNewEntity()) name.setReadOnly(true);
    createValueElement(IGTDocumentTypeEntity.DESCRIPTION);
    renderLabel("cancel","documentType.view.cancel");
  }*/
}