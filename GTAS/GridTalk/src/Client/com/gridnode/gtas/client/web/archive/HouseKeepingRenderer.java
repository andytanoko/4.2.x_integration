/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HouseKeepingRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 * 2006-06-26     Tam Wei Xiang       Added new field 'WF_RECORDS_DAYS_TO_KEEP'
 */
package com.gridnode.gtas.client.web.archive;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;

public class HouseKeepingRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = new Number[]
  {
    IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP, 
    IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP, 
    IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP
  };

  public HouseKeepingRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_HOUSE_KEEPING, _edit);
      renderFields(null, getEntity(), fields);
      
      renderLabelCarefully("edit_message","houseKeeping.edit.message", false); //TWX 27062006
      // Hack for custom date (as BFPR only renders standard dates)
      // Commented on 16-09-2004
      //HouseKeepingAForm form = (HouseKeepingAForm)getActionForm();
      //renderElementValue("startTime_value", form.getStartTime());
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering houseKeeping screen",t);
    }
  }
}