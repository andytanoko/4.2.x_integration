/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryQueryDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SearchRegistryQueryDispatchAction extends EntityDispatchAction2
{
  public static final String OPCON_ATTRIB_SEARCH_ID = "searchId";
  public static final String OPCON_ATTRIB_UUID      = "uuid";
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SearchRegistryQueryRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SEARCH_REGISTRY_QUERY_UPDATE : IDocumentKeys.SEARCH_REGISTRY_QUERY_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SearchRegistryQueryAForm form = (SearchRegistryQueryAForm)actionContext.getActionForm();
    IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)entity;
    IGTSearchRegistryCriteriaEntity criteria = (IGTSearchRegistryCriteriaEntity)query.getFieldValue(IGTSearchRegistryQueryEntity.CRITERIA);

    form.setSearchId          (query.getFieldString         (IGTSearchRegistryQueryEntity.SEARCH_ID));
    form.setDtSubmitted       (query.getFieldString         (IGTSearchRegistryQueryEntity.DT_SUBMITTED));
    form.setDtResponded       (query.getFieldString         (IGTSearchRegistryQueryEntity.DT_RESPONDED));
    form.setExceptionStr      (query.getFieldString         (IGTSearchRegistryQueryEntity.EXCEPTION_STR));

    form.setBusEntityDesc     (criteria.getFieldString      (IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC));
    form.setDuns              (criteria.getFieldString      (IGTSearchRegistryCriteriaEntity.DUNS));
    form.setMessagingStandards(criteria.getFieldStringArray (IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS));
    form.setMatch             (criteria.getFieldString      (IGTSearchRegistryCriteriaEntity.MATCH));
    form.setQueryUrl          (criteria.getFieldString      (IGTSearchRegistryCriteriaEntity.QUERY_URL));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SearchRegistryQueryAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SEARCH_REGISTRY_QUERY;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    // only validate embeded SearchRegistryCriteriaEntity
    IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)entity;
    IGTSearchRegistryCriteriaEntity criteria = (IGTSearchRegistryCriteriaEntity)query.getFieldValue(IGTSearchRegistryQueryEntity.CRITERIA);

    basicValidateString     (actionErrors, IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC,     form, criteria);
    basicValidateString     (actionErrors, IGTSearchRegistryCriteriaEntity.DUNS,                form, criteria);
    basicValidateStringArray(actionErrors, IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS, form, criteria);
    basicValidateString     (actionErrors, IGTSearchRegistryCriteriaEntity.MATCH,               form, criteria);
    basicValidateString     (actionErrors, IGTSearchRegistryCriteriaEntity.QUERY_URL,           form, criteria);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SearchRegistryQueryAForm form = (SearchRegistryQueryAForm)actionContext.getActionForm();
    IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)entity;
    IGTSearchRegistryCriteriaEntity criteria = (IGTSearchRegistryCriteriaEntity)query.getFieldValue(IGTSearchRegistryQueryEntity.CRITERIA);

    // Convert to a Collection of String
    String[] messagingStandardsArray = form.getMessagingStandards();
    Collection messagingStandards = new Vector(messagingStandardsArray.length);
    for(int i = 0; i < messagingStandardsArray.length; i++)
    {
      messagingStandards.add(messagingStandardsArray[i]);
    }

    criteria.setFieldValue(IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC,      form.getBusEntityDesc());
    criteria.setFieldValue(IGTSearchRegistryCriteriaEntity.DUNS,                 form.getDuns());
    criteria.setFieldValue(IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS,  messagingStandards);
    criteria.setFieldValue(IGTSearchRegistryCriteriaEntity.MATCH,                form.getMatchShort());
    criteria.setFieldValue(IGTSearchRegistryCriteriaEntity.QUERY_URL,            form.getQueryUrl());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    IGTSearchRegistryQueryEntity searchEntity = (IGTSearchRegistryQueryEntity)entity;
    if(searchEntity.isNewEntity())
    {
      HttpSession session = actionContext.getSession();
      synchronized(session)
      { // Store searchIds in the users session
        manager.create(searchEntity);
        Collection searches = (Collection)session.getAttribute(SearchRegistryQueryListAction.SEARCHES_KEY);
        if(searches == null)
        {
          searches = new Vector(1);
        }
        searches.add(searchEntity.getFieldValue(IGTSearchRegistryQueryEntity.SEARCH_ID));
        session.setAttribute(SearchRegistryQueryListAction.SEARCHES_KEY, searches);
      }
    }
    else
    {
      throw new java.lang.IllegalStateException("Searches may not be updated");
    }
  }

  private IGTSearchedBusinessEntityEntity getSearchedBusinessEntity(ActionContext actionContext,
                                                                    OperationContext searchOpCon,
                                                                    String index)
    throws GTClientException
  {
    try
    {
      int i = Integer.parseInt(index);
      if (searchOpCon == null)
        throw new NullPointerException("searchOpCon is null");
      IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)searchOpCon.getAttribute(IOperationContextKeys.ENTITY);
      List results = (List)query.getFieldEntities(IGTSearchRegistryQueryEntity.RESULTS);
      IGTSearchedBusinessEntityEntity searchedBusinessEntity = (IGTSearchedBusinessEntityEntity)results.get(i);
      if (searchedBusinessEntity == null)
        throw new NullPointerException("searchedBusinessEntity is null");
      return searchedBusinessEntity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting IGTSearchedBusinessEntityEntity (at index="
                                  + index + ") in results collection",t);
    }
  }

  // For testing purposes
  protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  {
    String fuid = actionContext.getRequest().getParameter("singleFuid");
    ActionForward divertForward = mapping.findForward(divertTo);
    if(divertForward == null)
    {
      throw new GTClientException("No mapping found for " + divertTo);
    }
    if( ("divertViewSearchedBusinessEntity".equals(divertTo))
        || ("divertConfigureBizEntity".equals(divertTo)) )
    {
      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",fuid),
                          divertForward.getRedirect());
    }
    return processSOCForward( divertForward, opCon );
  }

  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext opCon,
                                  OperationContext newOpCon,
                                  String mappingName)
    throws GTClientException
  {
    if( ("divertViewSearchedBusinessEntity".equals(mappingName))
        || ("divertConfigureBizEntity".equals(mappingName)) )
    {
      IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)opCon.getAttribute(IOperationContextKeys.ENTITY);
      
      Long searchId = (Long)query.getFieldValue(IGTSearchRegistryQueryEntity.SEARCH_ID);
      newOpCon.setAttribute(OPCON_ATTRIB_SEARCH_ID, searchId);

      String index = actionContext.getRequest().getParameter("singleIndex");
      IGTSearchedBusinessEntityEntity searchedBusinessEntity = getSearchedBusinessEntity(actionContext,opCon,index);
      String uuid = searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.UUID);
      newOpCon.setAttribute(OPCON_ATTRIB_UUID, uuid);
    }
  }

  protected ActionForward getResumeForward( ActionContext actionContext,
                                            ActionForm actionForm)
    throws GTClientException
  {
    IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)getEntity(actionContext);
    if(query != null)
    {
      if(query.getFieldValue(IGTSearchRegistryQueryEntity.DT_RESPONDED) != null)
      {
        ActionForward resume = actionContext.getMapping().findForward(RESUME_VIEW_MAPPING);
        return resume;
      }
    }
    ActionForward resume = actionContext.getMapping().findForward(RESUME_UPDATE_MAPPING);
    return resume;
  }
  
  // Called by the Struts' RequestProcessor
  public ActionForward configureBizEntity(ActionMapping mapping, ActionForm actionForm,
                                          HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    try
    {
      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      Long searchId;
      String uuid;
      try
      {
        searchId = (Long)opCon.getAttribute(OPCON_ATTRIB_SEARCH_ID);
        uuid = (String)opCon.getAttribute(OPCON_ATTRIB_UUID);
      }
      catch(Throwable t)
      {
        searchId = new Long(request.getParameter("searchId"));
        uuid = request.getParameter("uuid");
      }

      Collection uuids = new Vector(1);
      uuids.add(uuid);
      
      IGTSession gtasSession = getGridTalkSession(actionContext);
      IGTSearchRegistryQueryManager manager = (IGTSearchRegistryQueryManager)gtasSession.getManager(getIGTManagerType(actionContext));
      manager.configureBizEntity(searchId, uuids);
          
      String resumeViewMapping = mapping.findForward(RESUME_VIEW_MAPPING).getPath();
      resumeViewMapping = StaticWebUtils.addParameterToURL(resumeViewMapping, "uid", searchId.toString());       
      return new ActionForward(resumeViewMapping, true);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error configuring Business Entity", t);
    }
  }
}