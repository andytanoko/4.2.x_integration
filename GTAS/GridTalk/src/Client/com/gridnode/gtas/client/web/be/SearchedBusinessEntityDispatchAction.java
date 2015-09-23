/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-10-03     Neo Sok Lay         Also display the UUID field.
 */
package com.gridnode.gtas.client.web.be;

import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchRegistryQueryEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchedBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTWhitePageEntity;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SearchedBusinessEntityDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SEARCHED_BUSINESS_ENTITY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SearchedBusinessEntityRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(edit) throw new UnsupportedOperationException("searched business entity does not support edit");
    return IDocumentKeys.SEARCHED_BUSINESS_ENTITY_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SearchedBusinessEntityAForm form = (SearchedBusinessEntityAForm)actionContext.getActionForm();
    IGTSearchedBusinessEntityEntity searchedBusinessEntity = (IGTSearchedBusinessEntityEntity)entity;
    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)searchedBusinessEntity.getFieldValue(IGTSearchedBusinessEntityEntity.WHITE_PAGE);

    form.setUuid                  (searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.UUID));
    form.setId                    (searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.ID));
    form.setEnterpriseId          (searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.ENTERPRISE_ID));
    form.setDescription           (searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.DESCRIPTION));
    form.setBeState               (searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.BE_STATE));

    form.setBusinessDesc          (whitePage.getFieldString(IGTWhitePageEntity.BUSINESS_DESC));
    form.setDuns                  (whitePage.getFieldString(IGTWhitePageEntity.DUNS));
    form.setGlobalSupplyChainCode (whitePage.getFieldString(IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE));
    form.setContactPerson         (whitePage.getFieldString(IGTWhitePageEntity.CONTACT_PERSON));
    form.setEmail                 (whitePage.getFieldString(IGTWhitePageEntity.EMAIL));
    form.setTel                   (whitePage.getFieldString(IGTWhitePageEntity.TEL));
    form.setFax                   (whitePage.getFieldString(IGTWhitePageEntity.FAX));
    form.setWebsite               (whitePage.getFieldString(IGTWhitePageEntity.WEBSITE));
    form.setAddress               (whitePage.getFieldString(IGTWhitePageEntity.ADDRESS));
    form.setPoBox                 (whitePage.getFieldString(IGTWhitePageEntity.PO_BOX));
    form.setCity                  (whitePage.getFieldString(IGTWhitePageEntity.CITY));
    form.setState                 (whitePage.getFieldString(IGTWhitePageEntity.STATE));
    form.setZipCode               (whitePage.getFieldString(IGTWhitePageEntity.POSTCODE));
    form.setCountry               (whitePage.getFieldString(IGTWhitePageEntity.COUNTRY));
    form.setLanguage              (whitePage.getFieldString(IGTWhitePageEntity.LANGUAGE));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SearchedBusinessEntityAForm();
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
    throw new UnsupportedOperationException("searched business entity does not support edit");
//    IGTSearchedBusinessEntityEntity searchedBusinessEntity = (IGTSearchedBusinessEntityEntity)entity;
//    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)searchedBusinessEntity.getFieldValue(IGTSearchedBusinessEntityEntity.WHITE_PAGE);
//
//    basicValidateString(actionErrors, IGTSearchedBusinessEntityEntity.ID,             form, entity);
//    basicValidateString(actionErrors, IGTSearchedBusinessEntityEntity.ENTERPRISE_ID,  form, entity);
//    basicValidateString(actionErrors, IGTSearchedBusinessEntityEntity.DESCRIPTION,    form, entity);
//    basicValidateString(actionErrors, IGTSearchedBusinessEntityEntity.BE_STATE,       form, entity);
//
//    basicValidateString(actionErrors, IGTWhitePageEntity.BUSINESS_DESC,               form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.DUNS,                        form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE,         form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.CONTACT_PERSON,              form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.EMAIL,                       form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.TEL,                         form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.FAX,                         form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.WEBSITE,                     form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.ADDRESS,                     form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.PO_BOX,                      form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.CITY,                        form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.STATE,                       form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.POSTCODE,                    form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.COUNTRY,                     form, whitePage);
//    basicValidateString(actionErrors, IGTWhitePageEntity.LANGUAGE,                    form, whitePage);
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    throw new UnsupportedOperationException("searched business entity does not support edit");
//    SearchedBusinessEntityAForm form = (SearchedBusinessEntityAForm)actionContext.getActionForm();
//
//    entity.setFieldValue(IGTSearchedBusinessEntityEntity.ID,              form.getId());
//    entity.setFieldValue(IGTSearchedBusinessEntityEntity.ENTERPRISE_ID,   form.getEnterpriseId());
//    entity.setFieldValue(IGTSearchedBusinessEntityEntity.DESCRIPTION,     form.getDescription());
//    entity.setFieldValue(IGTSearchedBusinessEntityEntity.BE_STATE,        form.getBeStateShort());
  }

  protected IGTSearchRegistryQueryEntity getSearchRegistryQuery(ActionContext actionContext)
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

      IGTEntity searchRegistryQuery = (IGTEntity)pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(searchRegistryQuery == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(searchRegistryQuery instanceof IGTSearchRegistryQueryEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is not a searchRegistryQuery entity. Entity=" + searchRegistryQuery);
      }
      return (IGTSearchRegistryQueryEntity)searchRegistryQuery;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving searchRegistryQuery entity from parent OperationContext", t);
    }
  }
  
  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTSearchedBusinessEntityEntity entity = null;
      String indexString = actionContext.getRequest().getParameter("index");
      if(indexString != null)
      {
        if(indexString.equals("new"))
        {
          throw new UnsupportedOperationException("new searched business entity is not supported");
        }
        else
        {
          try
          {
            IGTSearchRegistryQueryEntity searchRegistryQuery = getSearchRegistryQuery(actionContext);
            List results = (List)searchRegistryQuery.getFieldValue(IGTSearchRegistryQueryEntity.RESULTS);
            if(results == null)
            {
              throw new java.lang.NullPointerException("Null value for results in searchRegistryQuery entity=" + searchRegistryQuery);
            }

            int index = Integer.parseInt(indexString); 
            try
            {
              entity = (IGTSearchedBusinessEntityEntity)results.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving searchedBusinessEntity from list at index " + index, t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of searchedBusinessEntity list retrieved from searchRegistryQuery entity "
                        + searchRegistryQuery);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving searchedBusinessEntity entity object from searchRegistryQuery entity in parent OperationContext", t);
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
      throw new GTClientException("Error obtaining searchedBusinessEntity entity object", t);
    }
  }

  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext opCon,
                                  OperationContext newOpCon,
                                  String mappingName)
    throws GTClientException
  {
    if("configure".equals(mappingName))
    {
      Long searchId = (Long)opCon.getAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_SEARCH_ID);
      newOpCon.setAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_SEARCH_ID, searchId);

      String uuid = (String)opCon.getAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_UUID);
      newOpCon.setAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_UUID, uuid);
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
    if("divertViewEmbeddedChannelInfo".equals(divertTo))
    {
      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(), "index", fuid),
                          divertForward.getRedirect());
    }
    return processSOCForward(divertForward, opCon);
  }
}