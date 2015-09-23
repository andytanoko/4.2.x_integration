/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTConditionEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchQueryManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SearchQueryDispatchAction extends EntityDispatchAction2
{
  private static final String ADD_CONDITION = "addCondition";
  private static final String REMOVE_CONDITION = "removeCondition";

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SEARCH_QUERY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SearchQueryRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    /*TWX 26042009
     * 
    //20050322AH - assert that the user is an admin
    if(edit)
    {
      IGTSession gtasSession = getGridTalkSession(actionContext);
      if(!gtasSession.isAdmin())
      {
        throw new IllegalStateException("not an admin user");
      }
    }*/
    //.....
    
    
    return (edit ? IDocumentKeys.SEARCH_QUERY_UPDATE : IDocumentKeys.SEARCH_QUERY_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): entered");
    IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)entity;
    SearchQueryAForm form = (SearchQueryAForm)actionContext.getActionForm();
    form.setName        (searchQuery.getFieldString(searchQuery.NAME));
    form.setDescription (searchQuery.getFieldString(searchQuery.DESCRIPTION));
    form.setCreatedBy   (searchQuery.getFieldString(searchQuery.CREATED_BY));
    
    if("".equals(searchQuery.getFieldString(searchQuery.IS_PUBLIC)) || (null == searchQuery.getFieldString(searchQuery.IS_PUBLIC)))
    {
      form.setIsPublic("false");
    }
    else
    {
      form.setIsPublic(searchQuery.getFieldString(searchQuery.IS_PUBLIC));
    }
    
    if(entity.isNewEntity())
    {
      if(form.getConditions().length == 0)
      {
        addNewCondition(searchQuery, form);
      }      
    }
    else
    {
      int noOfConditions = getConditionArray(searchQuery).length;
      for(int i = 0; i < noOfConditions; i++)
      {
        form.addNewCondition(null);
      }      
    }
    
    IGTConditionEntity[] conditions = getConditionArray(searchQuery); 
    ConditionAForm[] conditionForms = form.getConditions();
    for(int i = 0; i < conditions.length; i++)
    {
      conditionForms[i].setType     (conditions[i].getFieldString(IGTConditionEntity.TYPE));
      conditionForms[i].setField    (conditions[i].getFieldString(IGTConditionEntity.FIELD));
      conditionForms[i].setXpath    (conditions[i].getFieldString(IGTConditionEntity.XPATH));
      conditionForms[i].setOperator (conditions[i].getFieldString(IGTConditionEntity.OPERATOR));
      conditionForms[i].setValues   (conditions[i].getFieldString(IGTConditionEntity.VALUES));

//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): conditionForm.getType()=" + conditionForms[i].getType());
//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): conditionForm.getField()=" + conditionForms[i].getField());
//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): conditionForm.getXpath()=" + conditionForms[i].getXpath());
//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): conditionForm.getOperator()=" + conditionForms[i].getOperator());
//System.out.println("### SearchQueryDispatchAction.initialiseActionForm(): conditionForm.getValues()=" + conditionForms[i].getValues());
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SearchQueryAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SEARCH_QUERY;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)entity;
    basicValidateString(errors, searchQuery.NAME, form, entity);
    basicValidateString(errors, searchQuery.DESCRIPTION, form, entity);
    basicValidateString(errors, searchQuery.CREATED_BY, form, entity);
    basicValidateString(errors, searchQuery.IS_PUBLIC, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): entered");
    SearchQueryAForm form = (SearchQueryAForm)actionContext.getActionForm();
    IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)entity;

    searchQuery.setFieldValue(searchQuery.NAME,         form.getName());
    searchQuery.setFieldValue(searchQuery.DESCRIPTION,  form.getDescription());
    searchQuery.setFieldValue(searchQuery.CREATED_BY,   form.getCreatedBy());
    searchQuery.setFieldValue(searchQuery.IS_PUBLIC,    form.getIsPublic());
    
    System.out.println("UpdateEntityFields: IsPublic:"+form.getIsPublic());
    
    IGTConditionEntity[] conditions = getConditionArray(searchQuery); 
    ConditionAForm[] conditionForms = form.getConditions();
    for(int i = 0; i < conditionForms.length; i++)
    {
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): conditionForm.getType()=" + conditionForms[i].getType());
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): conditionForm.getField()=" + conditionForms[i].getField());
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): conditionForm.getXpath()=" + conditionForms[i].getXpath());
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): conditionForm.getOperator()=" + conditionForms[i].getOperator());
//System.out.println("### SearchQueryDispatchAction.updateEntityFields(): conditionForm.getValues()=" + conditionForms[i].getValues());

      conditions[i].setFieldValue(IGTConditionEntity.TYPE,     conditionForms[i].getTypeAsShort());
      conditions[i].setFieldValue(IGTConditionEntity.FIELD,    conditionForms[i].getFieldAsInteger());
      conditions[i].setFieldValue(IGTConditionEntity.XPATH,    conditionForms[i].getXpath());
      conditions[i].setFieldValue(IGTConditionEntity.OPERATOR, conditionForms[i].getOperator());
      conditions[i].setFieldValue(IGTConditionEntity.VALUES,   conditionForms[i].getValuesAsCollection());
    }
  }
  
  // Helper method 
  public static IGTConditionEntity[] getConditionArray(IGTSearchQueryEntity searchQuery) throws GTClientException
  {
    Collection conditions = (Collection)searchQuery.getFieldValue(searchQuery.CONDITIONS);
    return (IGTConditionEntity[])conditions.toArray(new IGTConditionEntity[conditions.size()]);
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)getEntity(actionContext);
    SearchQueryAForm searchQueryForm = (SearchQueryAForm)actionContext.getActionForm();
    if(searchQueryForm == null) return;
    
    String updateAction = searchQueryForm.getUpdateAction();
    
    if(ADD_CONDITION.equals(updateAction))
    {
      addNewCondition(searchQuery, searchQueryForm);
    }
    else if(REMOVE_CONDITION.equals(updateAction))
    {
      removeSelectedConditions(searchQuery, searchQueryForm);
    }
    
    searchQueryForm.setUpdateAction(null);
  }
  
  private void addNewCondition(IGTSearchQueryEntity searchQuery, SearchQueryAForm searchQueryForm) throws GTClientException
  {
    // Add a new conditon to the entity
    IGTSearchQueryManager manager = (IGTSearchQueryManager)searchQuery.getSession().getManager(IGTManager.MANAGER_SEARCH_QUERY);
    Collection conditions = (Collection)searchQuery.getFieldValue(searchQuery.CONDITIONS);
    conditions.add(manager.newCondition());
    searchQuery.setFieldValue(searchQuery.CONDITIONS, conditions);

    // Add a new conditon to the action form
    searchQueryForm.addNewCondition(null);
  }

  private void removeSelectedConditions(IGTSearchQueryEntity searchQuery, SearchQueryAForm searchQueryForm) throws GTClientException
  {
    // Remove selected conditons to the entity
    Collection conditions = (Collection)searchQuery.getFieldValue(IGTSearchQueryEntity.CONDITIONS);
    ConditionAForm[] conditionForms = searchQueryForm.getConditions();

    if(conditionForms == null) return;

    ListIterator iterator = ((List)conditions).listIterator();
    for(int i = 0; i < conditionForms.length; i++)
    {
      //IGTConditionEntity condition = (IGTConditionEntity)iterator.next();
      iterator.next();
      if(conditionForms[i].isSelected())
      {
        // conditions.remove(condition); // 20031203 DDJ: Causes java.util.ConcurrentModificationException
        iterator.remove();
      }      
    }
    searchQuery.setFieldValue(searchQuery.CONDITIONS, conditions);
    
    // Remove selected conditons to the action form
    searchQueryForm.removeSelectedConditions();
  }
}