/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-08-20     Andrew Hill         Added support for the userTrackingId field
 * 2005-03-15     Andrew Hill         Hide magiclink for docType from non-admins
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessInstanceEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.ElvRenderer;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ProcessInstanceRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number _commonFields[] =
  {
    IGTProcessInstanceEntity.PROCESS_INSTANCE_ID,
    IGTProcessInstanceEntity.PARTNER,
    IGTProcessInstanceEntity.STATE,
    IGTProcessInstanceEntity.START_TIME,
    IGTProcessInstanceEntity.END_TIME,
    IGTProcessInstanceEntity.RETRY_NUM,
    IGTProcessInstanceEntity.IS_FAILED,
    IGTProcessInstanceEntity.PROCESS_DEF_NAME,
    IGTProcessInstanceEntity.ROLE_TYPE,
    IGTProcessInstanceEntity.USER_TRACKING_IDENTIFIER, //20030820AH
  };
  private static final Number _failureFields[] =
  {
    IGTProcessInstanceEntity.FAIL_REASON,
    IGTProcessInstanceEntity.DETAIL_REASON,
  };

  public ProcessInstanceRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTProcessInstanceEntity processInstance = (IGTProcessInstanceEntity)getEntity();
      ProcessInstanceAForm form = (ProcessInstanceAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(processInstance.getType(), _edit);

      //20030820AH - co: BindingFieldPropertyRenderer bfpr = renderFields(_commonFields);
      BindingFieldPropertyRenderer bfpr = renderFields(null,processInstance,_commonFields,null,form,null); //20030820AH

      // Render embeded listview
      addAssocDocsElv(rContext, form);

      // Render group headings
      renderLabel("general_heading", "processInstance.general.heading", false);

      if(form.getIsFailedPrimitiveBoolean())
      {
        renderLabel("failure_heading", "processInstance.failure.heading", false);

        //20030820AH - co: renderFields(_failureFields);
        bfpr = renderFields(bfpr,processInstance,_failureFields,null,form,null); //20030820AH
      }
      else
      {
        removeNode("failure_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering processInstance screen", t);
    }
  }

  private void addAssocDocsElv(RenderingContext rContext,
                               ProcessInstanceAForm form)
    throws RenderingException
  {
    try
    {
      IGTProcessInstanceEntity processInstance = (IGTProcessInstanceEntity)getEntity();
      Number[] columns = {
        IGTGridDocumentEntity.FOLDER,
        IGTGridDocumentEntity.G_DOC_ID,
        IGTGridDocumentEntity.U_DOC_DOC_TYPE,
        IGTGridDocumentEntity.U_DOC_NUM,
        IGTGridDocumentEntity.U_DOC_FILENAME,
      };

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_GRID_DOCUMENT;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, entityType);
      
      if(!gtasSession.isAdmin())
      { //20050315AH - Hide diversion for docType from non-admins
        adapter.setColumnLinksForFieldEnabled(IGTGridDocumentEntity.U_DOC_DOC_TYPE, false);
      }
      
      
      List assocDocUidsList = (List)processInstance.getFieldValue(IGTProcessInstanceEntity.ASSOC_DOCS);

      List assocDocsList = new Vector(assocDocUidsList.size());
      Iterator i = assocDocUidsList.iterator();
      while(i.hasNext())
      {
        Long uid = (Long)i.next();
        IGTGridDocumentEntity gridDoc = (IGTGridDocumentEntity)manager.getByUid(uid);
        if(gridDoc == null) throw new java.lang.NullPointerException("gridDoc is null for uid = " + uid);
        assocDocsList.add(gridDoc);
      }

/** @todo remove this, for testing only */
//can we though? its been here so long. What if something depends on it? arrgh!
if(assocDocsList == null) assocDocsList = new Vector();
//System.out.println("DEBUG: ProcessInstanceRenderer.addAssocDocsElv(): Hardcode assocDocsList for testing");

      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      listOptions.setColumnAdapter(adapter);
      if(_edit)
      {
        throw new java.lang.UnsupportedOperationException("Not Allowed to edit ProcessInstance entity");
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }

      listOptions.setHeadingLabelKey("processInstance.assocDocsList");
      listOptions.setUpdateURL("divertUpdateGridDocument");
      listOptions.setViewURL("divertViewGridDocument");
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "assocDocsList_details",
                                                listOptions,
                                                assocDocsList);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.setTableName("assocDocsList");
      elvRenderer.setOrder(form.getAssocDocsListOrderExploded());
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the assocDocsList table for the processInstance", e);
    }
  }

}