/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ProcedureDefFileRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number fields[] =
  {
    IGTProcedureDefFileEntity.NAME,
    IGTProcedureDefFileEntity.DESCRIPTION,
    IGTProcedureDefFileEntity.TYPE,
    IGTProcedureDefFileEntity.FILE_NAME,
  };

  public ProcedureDefFileRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
  	try
  	{
  		renderCommonFormElements(IGTEntity.ENTITY_PROCEDURE_DEF_FILE,_edit);
  		renderFields(null, getEntity(), fields);
  	}
  	catch (Throwable t) 
  	{
  		throw new RenderingException("Error rendering procedureDefFile screen", t);
  	}
  }
}