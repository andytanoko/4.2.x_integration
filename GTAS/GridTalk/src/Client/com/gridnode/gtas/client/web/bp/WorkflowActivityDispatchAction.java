/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivityDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-19     Andrew Hill         Created
 * 2002-12-20     Andrew Hill         Handle ports for exit to export
 * 2003-01-15     Andrew Hill         Ports are now for exitToPort
 * 2003-01-16     Andrew Hill         UserProcedure selection
 * 2004-04-01     Daniel D'Cotta      Added support for SUSPEND_ACTIVITY
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class WorkflowActivityDispatchAction extends EntityDispatchAction2
{
  private static final String INDEX_KEY = "workflowActivity index";

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_WORKFLOW_ACTIVITY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new WorkflowActivityRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.WORKFLOW_ACTIVITY_UPDATE : IDocumentKeys.WORKFLOW_ACTIVITY_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTWorkflowActivityEntity wfa = (IGTWorkflowActivityEntity)entity;
    WorkflowActivityAForm form = (WorkflowActivityAForm)actionContext.getActionForm();

    form.setActivityType( wfa.getFieldString(IGTWorkflowActivityEntity.TYPE) );
    form.setDescription( wfa.getFieldString(IGTWorkflowActivityEntity.DESCRIPTION) );
    form.setMappingRuleUids( StaticUtils.getStringArray((Collection)wfa.getFieldValue(IGTWorkflowActivityEntity.MAPPING_RULE_UIDS) ) );
    form.setUserProcedureUids( StaticUtils.getStringArray((Collection)wfa.getFieldValue(IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS) ) );
    form.setPortUids( StaticUtils.getStringArray((Collection)wfa.getFieldValue(IGTWorkflowActivityEntity.PORT_UIDS) ) );
    form.setDescription(wfa.getFieldString(IGTWorkflowActivityEntity.DESCRIPTION));
    form.setUserDefinedAlertUids( StaticUtils.getStringArray((Collection)wfa.getFieldValue(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS) ) ); //20030515AH
    form.setAlertType( wfa.getFieldString(IGTWorkflowActivityEntity.ALERT_TYPE) ); //20030515AH
    form.setDispatchInterval( wfa.getFieldString(IGTWorkflowActivityEntity.DISPATCH_INTERVAL) );  // 20040401 DDJ
    form.setDispatchCount( wfa.getFieldString(IGTWorkflowActivityEntity.DISPATCH_COUNT) );        // 20040401 DDJ
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new WorkflowActivityAForm();
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
    WorkflowActivityAForm wfaForm = (WorkflowActivityAForm)form;
    IGTWorkflowActivityEntity workflow = (IGTWorkflowActivityEntity)entity;

    basicValidateString(actionErrors,IGTWorkflowActivityEntity.TYPE,wfaForm,workflow);

    Integer type = wfaForm.getActivityTypeInteger();
    if( IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type) )
    {
      basicValidateKeys(actionErrors, IGTWorkflowActivityEntity.MAPPING_RULE_UIDS, wfaForm, workflow); //20021116AH
    }
    else if( IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type) )
    {
      basicValidateKeys(actionErrors, IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS, wfaForm, workflow); //20030116AH
    }
    else if( IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type) ) //20030115AH
    {
      basicValidateKeys(actionErrors, IGTWorkflowActivityEntity.PORT_UIDS, wfaForm, workflow); //200212206AH
    }
    else if( IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type) ) //20030515AH
    {
      basicValidateString(actionErrors,IGTWorkflowActivityEntity.ALERT_TYPE,wfaForm,workflow);
      String alertType = wfaForm.getAlertType();
System.out.println("alertType=" + alertType);
      if( IGTAlertTypeEntity.NAME_USER_DEFINED.equals(alertType) )
      {
System.out.println("validating user defined alert uids:" + StaticUtils.stringValue(wfaForm.getUserDefinedAlertUids()));
        basicValidateKeys(actionErrors, IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS, wfaForm, workflow); 
      }
    }

    basicValidateString(actionErrors,IGTWorkflowActivityEntity.DISPATCH_INTERVAL,wfaForm,workflow);  // 20040401 DDJ
    basicValidateString(actionErrors,IGTWorkflowActivityEntity.DISPATCH_COUNT,wfaForm,workflow);     // 20040401 DDJ
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    IGTWorkflowActivityEntity workflow = (IGTWorkflowActivityEntity)entity;
    WorkflowActivityAForm wfaForm = (WorkflowActivityAForm)actionContext.getActionForm();

    workflow.setFieldValue(IGTWorkflowActivityEntity.TYPE, StaticUtils.integerValue(wfaForm.getActivityType()));
    Integer type = wfaForm.getActivityTypeInteger();
    if( IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type) )
    { //20030115AH - mod to get Long collection
      Collection mappingRules = StaticUtils.getLongCollection(wfaForm.getMappingRuleUids());
      workflow.setFieldValue( IGTWorkflowActivityEntity.MAPPING_RULE_UIDS, mappingRules);
    }
    else if( IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type) )
    { //20030116AH
      Collection userProcedures = StaticUtils.getLongCollection(wfaForm.getUserProcedureUids());
      workflow.setFieldValue( IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS, userProcedures);
    }
    else if( IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type) ) 
    { //20030115AH
      Collection ports = StaticUtils.getLongCollection(wfaForm.getPortUids());
      workflow.setFieldValue( IGTWorkflowActivityEntity.PORT_UIDS,ports ); //20021220AH
    }
    else if( IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type) ) 
    { //20030515AH
      Collection userDefinedAlertUids = StaticUtils.getLongCollection(wfaForm.getUserDefinedAlertUids());
      workflow.setFieldValue( IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS,userDefinedAlertUids );
      workflow.setFieldValue( IGTWorkflowActivityEntity.ALERT_TYPE, wfaForm.getAlertType() );
    }

    workflow.setFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL,  StaticUtils.longValue(wfaForm.getDispatchInterval())); //20040401 DDJ
    workflow.setFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT,     StaticUtils.integerValue(wfaForm.getDispatchCount())); //20040401 DDJ
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      String index = (String)opContext.getAttribute(INDEX_KEY);
      IGTWorkflowActivityEntity rawWfa = (IGTWorkflowActivityEntity)entity;
      //Set a default description that might be overriden by the cooking
      rawWfa.setFieldValue(IGTWorkflowActivityEntity.DESCRIPTION, "");
      List cookedWfas = ((IGTPartnerFunctionManager)manager).cookWorkflowActivity(rawWfa);
      OperationContext pOpContext = opContext.getPreviousContext();
      IGTPartnerFunctionEntity pf = getPartnerFunction(actionContext);
      List activities = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
      if("new".equals(index))
      {
        activities.addAll(cookedWfas);
      }
      else
      {
        int indeks = Integer.parseInt(index);
        activities.remove(indeks);
        activities.addAll( indeks, cookedWfas );
      }

      PartnerFunctionAForm pfForm = (PartnerFunctionAForm)pOpContext.getActionForm();
      pfForm.initWorkflowActivitiesOrder( activities==null ? 0 : activities.size() );
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving workflow entity in partnerFunction:"+entity,t);
    }
  }

  protected IGTPartnerFunctionEntity getPartnerFunction(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      OperationContext pOpContext = opContext.getPreviousContext();
      if(pOpContext == null)
      {
        throw new java.lang.NullPointerException("null parent OperationContext reference");
      }
      IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)
                                      pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(pf == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(pf instanceof IGTPartnerFunctionEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is"
                                              + " not a partnerFunction entity. Entity=" + pf);
      }
      return pf;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving partnerFunction entity from parent OperationContext",t);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTWorkflowActivityEntity entity = null;
      String indexString = actionContext.getRequest().getParameter("index");
      opContext.setAttribute(INDEX_KEY,indexString);
      if(indexString != null)
      {
        if(indexString.equals("new"))
        {
          try
          {
            IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
            IGTPartnerFunctionManager manager = (IGTPartnerFunctionManager)
                                                gtasSession.getManager(IGTManager.MANAGER_PARTNER_FUNCTION);
            entity = manager.newWorkflowActivity();
            initialiseNewEntity(actionContext, entity);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error instantiating new workflowActivity entity object",t);
          }
        }
        else
        {
          try
          {
            int index = Integer.parseInt(indexString);
            IGTPartnerFunctionEntity pf = getPartnerFunction(actionContext);
            List wfaList = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
            if(wfaList == null)
            {
              throw new java.lang.NullPointerException("Null value for partnerFunction.workflow"
                                                    + "Activities in partnerFunction entity=" + pf);
            }
            try
            {
              entity = (IGTWorkflowActivityEntity)wfaList.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving workflowActivity from list at index "
                                          + index,t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of workflowActivity list retrieved from partnerFunction entity "
                        + pf);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving workflowActivity entity object from"
                                        + " partnerFunction entity in parent OperationContext",t);
          }
        }
      }
      else
      {
        throw new GTClientException("No index specified");
      }
      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining workflowActivity entity object",t);
    }
  }
}