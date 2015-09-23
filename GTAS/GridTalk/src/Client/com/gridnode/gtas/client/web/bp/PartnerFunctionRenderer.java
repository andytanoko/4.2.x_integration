/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import java.util.List;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class PartnerFunctionRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = {
    IGTPartnerFunctionEntity.ID,
    IGTPartnerFunctionEntity.DESCRIPTION,
    IGTPartnerFunctionEntity.TRIGGER_ON,
  };

  public PartnerFunctionRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)getEntity();
      PartnerFunctionAForm form = (PartnerFunctionAForm)getActionForm();

      renderCommonFormElements(pf.getType(),_edit);
      if(_edit) renderLabel("channels_create","businessEntity.channels.create",false);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, pf, fields);
      addWorkflowActivitiesElv(rContext,form);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering partnerFunction screen",t);
    }
  }

  private void addWorkflowActivitiesElv(RenderingContext rContext,
                                        PartnerFunctionAForm form)
    throws RenderingException
  {
    try
    {
      IGTPartnerFunctionEntity pFunction = (IGTPartnerFunctionEntity)getEntity();
      //20021106AH - Put description before type in column order at request of Thomas
      Number[] columns = {  IGTWorkflowActivityEntity.DESCRIPTION,
                            IGTWorkflowActivityEntity.TYPE };

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_WORKFLOW_ACTIVITY;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      List workflowActivities = (List)pFunction.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();

      listOptions.setColumnAdapter(adapter);
      if(_edit)
      {
        listOptions.setCreateURL("divertUpdateWorkflowActivity");
        listOptions.setDeleteURL("divertDeleteWorkflowActivity");
        listOptions.setCreateLabelKey("partnerFunction.workflowActivities.create");
        listOptions.setDeleteLabelKey("partnerFunction.workflowActivities.delete");
        listOptions.setAllowsSelection(true);
        listOptions.setAllowsEdit(true);
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }

      listOptions.setHeadingLabelKey("partnerFunction.workflowActivities");
      listOptions.setUpdateURL("divertUpdateWorkflowActivity");
      listOptions.setViewURL("divertViewWorkflowActivity");
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "workflowActivities_details",
                                                listOptions,
                                                workflowActivities);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(true);
      elvRenderer.setTableName("workflowActivities");
      elvRenderer.setOrder(  form.getWorkflowActivitiesOrderExploded() );
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the WorkflowActivity table for the role",e);
    }
  }
}

