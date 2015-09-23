/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import java.util.List;
import java.util.Vector;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerFunctionEntity;
import com.gridnode.gtas.client.ctrl.IGTWorkflowActivityEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PartnerFunctionDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PARTNER_FUNCTION;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PartnerFunctionRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PARTNER_FUNCTION_UPDATE : IDocumentKeys.PARTNER_FUNCTION_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)entity;
    PartnerFunctionAForm form = (PartnerFunctionAForm)actionContext.getActionForm();

    form.setId( pf.getFieldString(IGTPartnerFunctionEntity.ID) );
    form.setDescription( pf.getFieldString(IGTPartnerFunctionEntity.DESCRIPTION) );
    form.setTriggerOn( pf.getFieldString(IGTPartnerFunctionEntity.TRIGGER_ON) );

    List wfas = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
    form.initWorkflowActivitiesOrder( wfas==null ? 0 : wfas.size() );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PartnerFunctionAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PARTNER_FUNCTION;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    basicValidateString(actionErrors, IGTPartnerFunctionEntity.ID, form, entity);
    basicValidateString(actionErrors, IGTPartnerFunctionEntity.DESCRIPTION, form, entity);
    basicValidateString(actionErrors, IGTPartnerFunctionEntity.TRIGGER_ON, form, entity);
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)entity;
    PartnerFunctionAForm form = (PartnerFunctionAForm)actionContext.getActionForm();

    pf.setFieldValue( IGTPartnerFunctionEntity.ID, form.getId() );
    pf.setFieldValue( IGTPartnerFunctionEntity.DESCRIPTION, form.getDescription());
    pf.setFieldValue( IGTPartnerFunctionEntity.TRIGGER_ON, StaticUtils.integerValue(form.getTriggerOn()) );
  }

  protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  {
    String index = actionContext.getRequest().getParameter("singleIndex");
    index = translateIndex(actionContext,index);
    ActionForward divertForward = mapping.findForward(divertTo);
    if(divertForward == null)
    {
      throw new GTClientException("No mapping found for " + divertTo);
    }
    if(     ("divertUpdateWorkflowActivity".equals(divertTo))
        ||  ("divertViewWorkflowActivity".equals(divertTo)))
    {

      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",index),
                          divertForward.getRedirect());

    }
    return processSOCForward( divertForward, opCon );
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeWorkflowActivities(actionContext);
  }

  protected void performPreSaveProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeWorkflowActivities(actionContext);
  }

  protected String performPreDivertProcessing(ActionContext actionContext, String divertMappingName)
    throws GTClientException
  {
    arrangeWorkflowActivities(actionContext);
    return divertMappingName;
  }

  private String translateIndex(ActionContext actionContext, String oldIndex)
  {
    if("new".equals(oldIndex)) return oldIndex;
    String newIndex = null;
    PartnerFunctionAForm form = (PartnerFunctionAForm)actionContext.getActionForm();
    String[] order = form.getWorkflowActivitiesOrderExploded();
    for(int i=0; i < order.length; i++)
    {
      if(order[i].equals(oldIndex))
      {
        newIndex = "" + i;
      }
    }
    return newIndex;
  }

  private void arrangeWorkflowActivities(ActionContext actionContext)
    throws GTClientException
  {
    PartnerFunctionAForm form = (PartnerFunctionAForm)actionContext.getActionForm();
    IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)getEntity(actionContext);
    List oldOrder = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
    if( (oldOrder == null) || (oldOrder.size() == 0) )
    {
      return;
    }

    String[] order = form.getWorkflowActivitiesOrderExploded();
    if(order == null)
    {
      order = new String[0];
    }
    List newOrder = new Vector(order.length);

    for(int i=0; i < order.length; i++)
    {
      String nextStr = order[i];
      int next = StaticUtils.primitiveIntValue( nextStr );
      IGTWorkflowActivityEntity entity = (IGTWorkflowActivityEntity)oldOrder.get(next);
      newOrder.add(entity);
    }
    pf.setFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES, newOrder);
  }

  protected void prepareView(ActionContext actionContext,
                                    OperationContext opCon,
                                    boolean edit)
    throws GTClientException
  {
    try
    {
      PartnerFunctionAForm form = (PartnerFunctionAForm)actionContext.getActionForm();
      IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)getEntity(actionContext);
      List activities = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
      int size = activities == null ? 0 : activities.size();
      form.initWorkflowActivitiesOrder(size);
      super.prepareView(actionContext,opCon,edit);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing entity view",t);
    }
  }
}