/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-12-04     Andrew Hill         Refactor to new commInfo model
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTCommInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class CommInfoDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_channel";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_COMM_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new CommInfoRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.COMM_INFO_UPDATE : IDocumentKeys.COMM_INFO_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTCommInfoEntity commInfo = (IGTCommInfoEntity)entity;
    CommInfoAForm form = (CommInfoAForm)actionContext.getActionForm();
    form.setIsNewEntity(commInfo.isNewEntity());

    form.setDefaultTpt(commInfo.getFieldString(IGTCommInfoEntity.IS_DEFAULT_TPT));
    form.setDescription(commInfo.getFieldString(IGTCommInfoEntity.DESCRIPTION));
    form.setIsPartner( commInfo.getFieldString(IGTCommInfoEntity.IS_PARTNER) );
    form.setName(commInfo.getFieldString(IGTCommInfoEntity.NAME));
    form.setProtocolType(commInfo.getFieldString(IGTCommInfoEntity.PROTOCOL_TYPE));
    form.setRefId(commInfo.getFieldString(IGTCommInfoEntity.REF_ID));
    form.setTptImplVersion(commInfo.getFieldString(IGTCommInfoEntity.TPT_IMPL_VERSION));
    form.setPartnerCategory(commInfo.getFieldString(IGTCommInfoEntity.PARTNER_CAT));
    form.setUrl(commInfo.getFieldString(IGTCommInfoEntity.URL));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new CommInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_COMM_INFO;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTCommInfoEntity commInfo = (IGTCommInfoEntity)entity;
    CommInfoAForm form = (CommInfoAForm)actionForm;
    basicValidateString(errors, IGTCommInfoEntity.NAME, form, commInfo);
    basicValidateString(errors, IGTCommInfoEntity.DESCRIPTION, form, commInfo);
    basicValidateString(errors, IGTCommInfoEntity.IS_PARTNER, form, commInfo);
    basicValidateString(errors, IGTCommInfoEntity.PROTOCOL_TYPE, form, commInfo);
    basicValidateString(errors, IGTCommInfoEntity.REF_ID, form, commInfo);
    basicValidateString(errors,IGTCommInfoEntity.URL, form, commInfo);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    CommInfoAForm form = (CommInfoAForm)actionContext.getActionForm();
    IGTCommInfoEntity commInfo = (IGTCommInfoEntity)entity;

    commInfo.setFieldValue(IGTCommInfoEntity.DESCRIPTION, form.getDescription());
    commInfo.setFieldValue(IGTCommInfoEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()));
    commInfo.setFieldValue(IGTCommInfoEntity.IS_DEFAULT_TPT, StaticUtils.booleanValue(form.getDefaultTpt()));
    commInfo.setFieldValue(IGTCommInfoEntity.NAME, form.getName());
    commInfo.setFieldValue(IGTCommInfoEntity.PROTOCOL_TYPE, form.getProtocolType());
    commInfo.setFieldValue(IGTCommInfoEntity.URL, form.getUrl());
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTCommInfoEntity cInfo = (IGTCommInfoEntity)entity;

    String isPartner = actionContext.getRequest().getParameter("isPartner");
    if(isPartner != null)
    {
      cInfo.setFieldValue(IGTCommInfoEntity.IS_PARTNER, StaticUtils.booleanValue(isPartner));
    }
  }
}