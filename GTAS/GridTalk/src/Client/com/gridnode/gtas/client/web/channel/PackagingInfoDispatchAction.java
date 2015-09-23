/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2003-12-22     Daniel D'Cotta      Moved Zip & ZipTreshold to FlowControlInfo
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPackagingInfoEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.ctrl.IGTAs2PackagingInfoExtensionEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTPackagingInfoManager;

public class PackagingInfoDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_channel";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PACKAGING_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PackagingInfoRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PACKAGING_INFO_UPDATE : IDocumentKeys.PACKAGING_INFO_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    PackagingInfoAForm form = (PackagingInfoAForm)actionContext.getActionForm();
    IGTPackagingInfoEntity pInfo = (IGTPackagingInfoEntity)entity;
    form.setIsNewEntity( pInfo.isNewEntity() );

    form.setName( pInfo.getFieldString(IGTPackagingInfoEntity.NAME) );
    form.setDescription( pInfo.getFieldString(IGTPackagingInfoEntity.DESCRIPTION) );
    form.setIsPartner( pInfo.getFieldString(IGTPackagingInfoEntity.IS_PARTNER) );  // 20040527: Added to fix GNDB00017225 
    form.setEnvelope( pInfo.getFieldString(IGTPackagingInfoEntity.ENVELOPE) );
    // form.setZip( pInfo.getFieldString(IGTPackagingInfoEntity.ZIP) );
    // form.setZipThreshold( pInfo.getFieldString(IGTPackagingInfoEntity.ZIP_THRESHOLD) );
    form.setPartnerCategory( pInfo.getFieldString(IGTPackagingInfoEntity.PARTNER_CAT) );
    form.setRefId( pInfo.getFieldString(IGTPackagingInfoEntity.REF_ID) );

    // 20031124 DDJ: AS2 fields
    IGTAs2PackagingInfoExtensionEntity as2 = (IGTAs2PackagingInfoExtensionEntity)pInfo.getFieldValue(IGTPackagingInfoEntity.PKG_INFO_EXTENSION);
    if(as2 != null)
    {
      form.setIsAckReq( as2.getFieldString(IGTAs2PackagingInfoExtensionEntity.IS_ACK_REQ) );
      form.setIsAckSigned( as2.getFieldString(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SIGNED) );
      form.setIsNrrReq( as2.getFieldString(IGTAs2PackagingInfoExtensionEntity.IS_NRR_REQ) );
      form.setIsAckSyn( as2.getFieldString(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SYN) );
      form.setReturnUrl( as2.getFieldString(IGTAs2PackagingInfoExtensionEntity.RETURN_URL) );
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PackagingInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PACKAGING_INFO;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //PackagingInfoAForm piForm = (PackagingInfoAForm)form;

    basicValidateString(errors, IGTPackagingInfoEntity.NAME, form, entity);
    basicValidateString(errors, IGTPackagingInfoEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTPackagingInfoEntity.ENVELOPE, form, entity);
    //basicValidateString(errors, IGTPackagingInfoEntity.ZIP, form, entity);
    basicValidateString(errors, IGTPackagingInfoEntity.IS_PARTNER, form, entity);
    // if(StaticUtils.primitiveBooleanValue(piForm.getZip()))
    // {
    //   basicValidateString(errors, IGTPackagingInfoEntity.ZIP_THRESHOLD, form, entity);
    // }

    // 20031124 DDJ: AS2 fields
    // no validation needed
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    PackagingInfoAForm form = (PackagingInfoAForm)actionContext.getActionForm();
    IGTPackagingInfoEntity pInfo = (IGTPackagingInfoEntity)entity;

    pInfo.setFieldValue( IGTPackagingInfoEntity.NAME, form.getName() );
    pInfo.setFieldValue( IGTPackagingInfoEntity.DESCRIPTION, form.getDescription() );
    pInfo.setFieldValue( IGTPackagingInfoEntity.ENVELOPE, form.getEnvelope() );
    // pInfo.setFieldValue( IGTPackagingInfoEntity.ZIP, StaticUtils.booleanValue(form.getZip()) );
    // pInfo.setFieldValue( IGTPackagingInfoEntity.ZIP_THRESHOLD, StaticUtils.integerValue(form.getZipThreshold()) );
    pInfo.setFieldValue( IGTPackagingInfoEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()) );

    // 20031124 DDJ: AS2 fields
    IGTAs2PackagingInfoExtensionEntity as2 = null;
    if(IGTPackagingInfoEntity.AS2_ENVELOPE_TYPE.equals(form.getEnvelope()))
    {
      as2 = (IGTAs2PackagingInfoExtensionEntity)pInfo.getFieldValue(IGTPackagingInfoEntity.PKG_INFO_EXTENSION);
      if(as2 == null)
      {
        IGTSession gtasSession = pInfo.getSession();
        IGTPackagingInfoManager manager = (IGTPackagingInfoManager)gtasSession.getManager(IGTManager.MANAGER_PACKAGING_INFO);
        as2 = manager.newAs2PackagingInfoExtension();
      }
      as2.setFieldValue( IGTAs2PackagingInfoExtensionEntity.IS_ACK_REQ, form.getIsAckReqAsBoolean() );
      as2.setFieldValue( IGTAs2PackagingInfoExtensionEntity.IS_ACK_SIGNED, form.getIsAckSignedAsBoolean() );
      as2.setFieldValue( IGTAs2PackagingInfoExtensionEntity.IS_NRR_REQ, form.getIsNrrReqAsBoolean() );
      as2.setFieldValue( IGTAs2PackagingInfoExtensionEntity.IS_ACK_SYN, form.getIsAckSynAsBoolean() );
      as2.setFieldValue( IGTAs2PackagingInfoExtensionEntity.RETURN_URL, form.getReturnUrl() );
    }
    pInfo.setFieldValue( IGTPackagingInfoEntity.PKG_INFO_EXTENSION, as2 );
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTPackagingInfoEntity pInfo = (IGTPackagingInfoEntity)entity;

    String isPartner = actionContext.getRequest().getParameter("isPartner");
    if(isPartner != null)
    {
      pInfo.setFieldValue(IGTPackagingInfoEntity.IS_PARTNER, StaticUtils.booleanValue(isPartner));
    }
  }
}