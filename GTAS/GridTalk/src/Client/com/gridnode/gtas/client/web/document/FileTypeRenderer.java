/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;

public class FileTypeRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = new Number[]{IGTFileTypeEntity.DESCRIPTION,
                                                        IGTFileTypeEntity.FILE_TYPE,
                                                        IGTFileTypeEntity.PROGRAM_NAME,
                                                        IGTFileTypeEntity.PROGRAM_PATH,
                                                        IGTFileTypeEntity.PARAMETERS,
                                                        IGTFileTypeEntity.WORKING_DIR, };

  public FileTypeRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_FILE_TYPE,_edit);
      renderFields(null, getEntity(), fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering fileType screen",t);
    }
  }
}