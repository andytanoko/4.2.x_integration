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
 * 2003-09-30     Daniel D'Cotta      Created
 * 2003-10-10     Neo Sok Lay         Change to use IGTSearchedChannelInfoEntity
 *                                    instead of IGTChannelInfoEntity.
 */
package com.gridnode.gtas.client.web.be;

import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class EmbeddedChannelInfoDispatchAction extends EntityDispatchAction2
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
    return new EmbeddedChannelInfoRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(edit) throw new UnsupportedOperationException("searched business entity does not support edit");
    return IDocumentKeys.EMBEDDED_CHANNEL_INFO_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    EmbeddedChannelInfoAForm form = (EmbeddedChannelInfoAForm)actionContext.getActionForm();
    IGTSearchedChannelInfoEntity embeddedChannelInfo = (IGTSearchedChannelInfoEntity)entity;

    IGTPackagingInfoEntity  packagingInfo = (IGTPackagingInfoEntity)embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.PACKAGING_PROFILE);
    IGTSecurityInfoEntity   securityInfo  = (IGTSecurityInfoEntity) embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.SECURITY_PROFILE);
    IGTCommInfoEntity       commInfo      = (IGTCommInfoEntity)     embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.TPT_COMM_INFO);

    form.setName                          (embeddedChannelInfo.getFieldString(IGTSearchedChannelInfoEntity.NAME));
    form.setDescription                   (embeddedChannelInfo.getFieldString(IGTSearchedChannelInfoEntity.DESCRIPTION));
    form.setRefId                         (embeddedChannelInfo.getFieldString(IGTSearchedChannelInfoEntity.REF_ID));

    form.setPackagingInfo_name            (packagingInfo.getFieldString(IGTPackagingInfoEntity.NAME));
    form.setPackagingInfo_description     (packagingInfo.getFieldString(IGTPackagingInfoEntity.DESCRIPTION));
    form.setPackagingInfo_refId           (packagingInfo.getFieldString(IGTPackagingInfoEntity.REF_ID));
    form.setPackagingInfo_envelope        (packagingInfo.getFieldString(IGTPackagingInfoEntity.ENVELOPE));

    form.setSecurityInfo_name             (securityInfo.getFieldString(IGTSecurityInfoEntity.NAME));
    form.setSecurityInfo_description      (securityInfo.getFieldString(IGTSecurityInfoEntity.DESCRIPTION));
    form.setSecurityInfo_refId            (securityInfo.getFieldString(IGTSecurityInfoEntity.REF_ID));
    form.setSecurityInfo_encType          (securityInfo.getFieldString(IGTSecurityInfoEntity.ENC_TYPE));
    form.setSecurityInfo_encLevel         (securityInfo.getFieldString(IGTSecurityInfoEntity.ENC_LEVEL));
    form.setSecurityInfo_sigType          (securityInfo.getFieldString(IGTSecurityInfoEntity.SIG_TYPE));
    form.setSecurityInfo_digestAlgorithm  (securityInfo.getFieldString(IGTSecurityInfoEntity.DIGEST_ALGORITHM));

    form.setCommInfo_name                 (commInfo.getFieldString(IGTCommInfoEntity.NAME));
    form.setCommInfo_description          (commInfo.getFieldString(IGTCommInfoEntity.DESCRIPTION));
    form.setCommInfo_refId                (commInfo.getFieldString(IGTCommInfoEntity.REF_ID));
    form.setCommInfo_protocolType         (commInfo.getFieldString(IGTCommInfoEntity.PROTOCOL_TYPE));
    form.setCommInfo_url                  (commInfo.getFieldString(IGTCommInfoEntity.URL));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new EmbeddedChannelInfoAForm();
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
//    IGTEmbeddedChannelInfoEntity embeddedChannelInfo = (IGTEmbeddedChannelInfoEntity)entity;
//    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)embeddedChannelInfo.getFieldValue(IGTEmbeddedChannelInfoEntity.WHITE_PAGE);
//
//    basicValidateString(actionErrors, IGTEmbeddedChannelInfoEntity.ID,             form, entity);
//    basicValidateString(actionErrors, IGTEmbeddedChannelInfoEntity.ENTERPRISE_ID,  form, entity);
//    basicValidateString(actionErrors, IGTEmbeddedChannelInfoEntity.DESCRIPTION,    form, entity);
//    basicValidateString(actionErrors, IGTEmbeddedChannelInfoEntity.BE_STATE,       form, entity);
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
//    EmbeddedChannelInfoAForm form = (EmbeddedChannelInfoAForm)actionContext.getActionForm();
//
//    entity.setFieldValue(IGTEmbeddedChannelInfoEntity.ID,              form.getId());
//    entity.setFieldValue(IGTEmbeddedChannelInfoEntity.ENTERPRISE_ID,   form.getEnterpriseId());
//    entity.setFieldValue(IGTEmbeddedChannelInfoEntity.DESCRIPTION,     form.getDescription());
//    entity.setFieldValue(IGTEmbeddedChannelInfoEntity.BE_STATE,        form.getBeStateShort());
  }

  protected IGTSearchedBusinessEntityEntity getSearchedBusinessEntity(ActionContext actionContext)
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

      IGTEntity searchedBusinessEntity = (IGTEntity)pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(searchedBusinessEntity == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(searchedBusinessEntity instanceof IGTSearchedBusinessEntityEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is not a searchedBusinessEntity entity. Entity=" + searchedBusinessEntity);
      }
      return (IGTSearchedBusinessEntityEntity)searchedBusinessEntity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving searchedBusinessEntity entity from parent OperationContext", t);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTSearchedChannelInfoEntity entity = null;
      String indexString = actionContext.getRequest().getParameter("index");
      if(indexString != null)
      {
        if(indexString.equals("new"))
        {
          throw new UnsupportedOperationException("new embedded channel info is not supported");
        }
        else
        {
          try
          {
            IGTSearchedBusinessEntityEntity searchedBusinessEntity = getSearchedBusinessEntity(actionContext);
            List channels = (List)searchedBusinessEntity.getFieldValue(IGTSearchedBusinessEntityEntity.CHANNELS);
            if(channels == null)
            {
              throw new java.lang.NullPointerException("Null value for channels in searchedBusinessEntity entity=" + searchedBusinessEntity);
            }

            int index = Integer.parseInt(indexString);
            try
            {
              entity = (IGTSearchedChannelInfoEntity)channels.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving embeddedChannelInfo from list at index " + index, t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of embeddedChannelInfo list retrieved from searchedBusinessEntity entity "
                        + searchedBusinessEntity);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving embeddedChannelInfo entity object from searchedBusinessEntity entity in parent OperationContext", t);
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
      throw new GTClientException("Error obtaining embeddedChannelInfo entity object", t);
    }
  }
}