/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeQueryDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-17     Andrew Hill         Created
 * 2002-11-11     Andrew Hill         Save the searchIds in the session
 */
package com.gridnode.gtas.client.web.activation;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

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

public class SearchGridNodeQueryDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_be";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SearchGridNodeQueryRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SEARCH_GRIDNODE_QUERY_UPDATE : IDocumentKeys.SEARCH_GRIDNODE_QUERY_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SearchGridNodeQueryAForm form = (SearchGridNodeQueryAForm)actionContext.getActionForm();
    IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)entity;
    IGTSearchGridNodeCriteriaEntity criteria = (IGTSearchGridNodeCriteriaEntity)query.getFieldValue(IGTSearchGridNodeQueryEntity.CRITERIA);

    form.setSearchId( query.getFieldString(IGTSearchGridNodeQueryEntity.SEARCH_ID) );
    form.setDtSubmitted( query.getFieldString(IGTSearchGridNodeQueryEntity.DT_SUBMITTED) );
    form.setDtResponded( query.getFieldString(IGTSearchGridNodeQueryEntity.DT_RESPONDED) );

    form.setCriteriaType( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE) );
    form.setMatchType( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.MATCH_TYPE) );
    form.setGridNodeId( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.GRIDNODE_ID) );
    form.setGridNodeName( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.GRIDNODE_NAME) );
    form.setCategory( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.CATEGORY) );
    form.setBusinessDesc( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.BUSINESS_DESC) );
    form.setDuns( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.DUNS) );
    form.setContactPerson( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.CONTACT_PERSON) );
    form.setEmail( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.EMAIL) );
    form.setTel( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.TEL) );
    form.setFax( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.FAX) );
    form.setWebsite( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.WEBSITE) );
    form.setCountry( criteria.getFieldString(IGTSearchGridNodeCriteriaEntity.COUNTRY) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SearchGridNodeQueryAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    SearchGridNodeQueryAForm sForm = (SearchGridNodeQueryAForm)actionContext.getActionForm();
    IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)entity;
    IGTSearchGridNodeCriteriaEntity criteria = (IGTSearchGridNodeCriteriaEntity)
                                                query.getFieldValue(IGTSearchGridNodeQueryEntity.CRITERIA);

    basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE, sForm, criteria);
    Short criteriaType = sForm.getCriteriaTypeShort();
    if(IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE_BY_GRIDNODE.equals(criteriaType))
    {
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.MATCH_TYPE, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.GRIDNODE_ID, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.GRIDNODE_NAME, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.CATEGORY, sForm, criteria);
    }
    else if(IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE_BY_WHITEPAGE.equals(criteriaType))
    {
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.MATCH_TYPE, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.BUSINESS_DESC, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.DUNS, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.CONTACT_PERSON, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.EMAIL, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.WEBSITE, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.TEL, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.FAX, sForm, criteria);
      basicValidateString(errors, IGTSearchGridNodeCriteriaEntity.COUNTRY, sForm, criteria);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SearchGridNodeQueryAForm form = (SearchGridNodeQueryAForm)actionContext.getActionForm();
    IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)entity;
    IGTSearchGridNodeCriteriaEntity criteria = (IGTSearchGridNodeCriteriaEntity)query.getFieldValue(IGTSearchGridNodeQueryEntity.CRITERIA);

    Short criteriaType = StaticUtils.shortValue( form.getCriteriaType() );
    Short matchType = StaticUtils.shortValue( form.getMatchType() );
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE, criteriaType);
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.MATCH_TYPE, matchType);
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.GRIDNODE_ID, form.getGridNodeId());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.GRIDNODE_NAME, form.getGridNodeName());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.CATEGORY, form.getCategory());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.BUSINESS_DESC, form.getBusinessDesc());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.DUNS, form.getDuns());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.CONTACT_PERSON, form.getContactPerson());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.EMAIL,form.getEmail());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.TEL,form.getTel());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.FAX,form.getFax());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.WEBSITE,form.getWebsite());
    criteria.setFieldValue(IGTSearchGridNodeCriteriaEntity.COUNTRY,form.getCountry());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    IGTSearchGridNodeQueryEntity searchEntity = (IGTSearchGridNodeQueryEntity)entity;
    if(searchEntity.isNewEntity())
    {
      HttpSession session = actionContext.getSession();
      synchronized(session)
      { //20021111AH - Stroe searchIds in the users session
        manager.create(searchEntity);
        Collection searches = (Collection)session.getAttribute(SearchGridNodeQueryListAction.SEARCHES_KEY);
        if(searches == null)
        {
          searches = new Vector(1);
        }
        searches.add(searchEntity.getFieldValue(IGTSearchGridNodeQueryEntity.SEARCH_ID));
        session.setAttribute(SearchGridNodeQueryListAction.SEARCHES_KEY,searches);
      }
    }
    else
    {
      throw new java.lang.IllegalStateException("Searches may not be updated");
    }
  }

  private IGTSearchedGridNodeEntity getSearchedGridNode(ActionContext actionContext,
                                                        OperationContext searchOpCon,
                                                        String index) throws GTClientException
  {
    try
    {
      int i = Integer.parseInt(index);
      if (searchOpCon == null)
        throw new NullPointerException("searchOpCon is null"); //20030424AH
      IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)
                                            searchOpCon.getAttribute(IOperationContextKeys.ENTITY);
      List results = (List)query.getFieldEntities(IGTSearchGridNodeQueryEntity.RESULTS);
      IGTSearchedGridNodeEntity searchedGridNode = (IGTSearchedGridNodeEntity)results.get(i);
      if (searchedGridNode == null)
        throw new NullPointerException("searchedGridNode is null"); //20030424AH
      return searchedGridNode;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting IGTSearchedGridNodeEntity (at index="
                                  + index + ") in results collection",t);
    }
  }

  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext opCon,
                                  OperationContext newOpCon,
                                  String mappingName)
    throws GTClientException
  {
    if("divertCreateActivationRecord".equals(mappingName))
    {
      String index = actionContext.getRequest().getParameter("singleIndex");
      IGTSearchedGridNodeEntity searchedGridNode = getSearchedGridNode(actionContext,opCon,index);
      newOpCon.setAttribute(IGTActivationRecordEntity.GRIDNODE_ID,
                            searchedGridNode.getFieldValue(IGTSearchedGridNodeEntity.GRIDNODE_ID));
      newOpCon.setAttribute(IGTActivationRecordEntity.GRIDNODE_NAME,
                            searchedGridNode.getFieldValue(IGTSearchedGridNodeEntity.GRIDNODE_NAME));
    }
  }

  protected ActionForward getResumeForward( ActionContext actionContext,
                                            ActionForm actionForm)
    throws GTClientException
  {
    IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)getEntity(actionContext);
    if(query != null)
    {
      if(query.getFieldValue(IGTSearchGridNodeQueryEntity.DT_RESPONDED) != null)
      {
        ActionForward resume = actionContext.getMapping().findForward(RESUME_VIEW_MAPPING);
        return resume;
      }
    }
    ActionForward resume = actionContext.getMapping().findForward(RESUME_UPDATE_MAPPING);
    return resume;
  }
}