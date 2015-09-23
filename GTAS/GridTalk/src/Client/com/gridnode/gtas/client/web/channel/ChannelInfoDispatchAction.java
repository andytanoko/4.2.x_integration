/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-09-17     Andrew Hill         Add isPartner, packagingProfile, securityProfile
 * 2002-10-08     Andrew Hill         More "partnerCat" stuff
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ChannelInfoDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_channel";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_CHANNEL_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ChannelInfoRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.CHANNEL_INFO_UPDATE : IDocumentKeys.CHANNEL_INFO_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)entity;
    ChannelInfoAForm form = (ChannelInfoAForm)actionContext.getActionForm();
    form.setIsNewEntity(channelInfo.isNewEntity());

    form.setDescription( channelInfo.getFieldString(IGTChannelInfoEntity.DESCRIPTION));
    form.setName(channelInfo.getFieldString(IGTChannelInfoEntity.NAME));
    form.setTptCommInfoUid(channelInfo.getFieldString(IGTChannelInfoEntity.TPT_COMM_INFO_UID));
    form.setTptProtocolType(channelInfo.getFieldString(IGTChannelInfoEntity.TPT_PROTOCOL_TYPE));
    form.setRefId(channelInfo.getFieldString(IGTChannelInfoEntity.REF_ID));
    form.setIsPartner( channelInfo.getFieldString(IGTChannelInfoEntity.IS_PARTNER) );
    form.setPackagingProfile( channelInfo.getFieldString(IGTChannelInfoEntity.PACKAGING_PROFILE) );
    form.setSecurityProfile( channelInfo.getFieldString(IGTChannelInfoEntity.SECURITY_PROFILE) );
    form.setPartnerCategory( channelInfo.getFieldString(IGTChannelInfoEntity.PARTNER_CAT) );

    // FlowControl
    IGTFlowControlInfoEntity flowControl = (IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO);
    form.setIsZip( flowControl.getFieldString(IGTFlowControlInfoEntity.IS_ZIP) );
    form.setZipThreshold( flowControl.getFieldString(IGTFlowControlInfoEntity.ZIP_THRESHOLD) );
    form.setIsSplit( flowControl.getFieldString(IGTFlowControlInfoEntity.IS_SPLIT) );
    form.setSplitSize( flowControl.getFieldString(IGTFlowControlInfoEntity.SPLIT_SIZE) );
    form.setSplitThreshold( flowControl.getFieldString(IGTFlowControlInfoEntity.SPLIT_THRESHOLD) );

  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ChannelInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_CHANNEL_INFO;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)entity;

    basicValidateString(errors, IGTChannelInfoEntity.DESCRIPTION, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.NAME, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.REF_ID, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.TPT_COMM_INFO_UID, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.TPT_PROTOCOL_TYPE, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.PACKAGING_PROFILE, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.SECURITY_PROFILE, form, channelInfo);
    basicValidateString(errors, IGTChannelInfoEntity.IS_PARTNER, form, channelInfo);

    // FlowControl
    IGTFlowControlInfoEntity flowControl = (IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO);
    basicValidateString(errors, IGTFlowControlInfoEntity.IS_ZIP, form, flowControl);
    if(((ChannelInfoAForm)form).getIsZipAsBoolean().booleanValue())
    {
      basicValidateString(errors, IGTFlowControlInfoEntity.ZIP_THRESHOLD, form, flowControl);
    }
    basicValidateString(errors, IGTFlowControlInfoEntity.IS_SPLIT, form, flowControl);
    if(((ChannelInfoAForm)form).getIsSplitAsBoolean().booleanValue())
    {
      basicValidateString(errors, IGTFlowControlInfoEntity.SPLIT_SIZE, form, flowControl);
      basicValidateString(errors, IGTFlowControlInfoEntity.SPLIT_THRESHOLD, form, flowControl);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ChannelInfoAForm form = (ChannelInfoAForm)actionContext.getActionForm();
    IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)entity;

    channelInfo.setFieldValue( IGTChannelInfoEntity.DESCRIPTION, form.getDescription() );
    channelInfo.setFieldValue( IGTChannelInfoEntity.NAME, form.getName() );
    channelInfo.setFieldValue( IGTChannelInfoEntity.TPT_COMM_INFO_UID,
                               StaticUtils.longValue( form.getTptCommInfoUid() ) );
    channelInfo.setFieldValue( IGTChannelInfoEntity.TPT_PROTOCOL_TYPE, form.getTptProtocolType() );
    channelInfo.setFieldValue( IGTChannelInfoEntity.PACKAGING_PROFILE, StaticUtils.longValue(form.getPackagingProfile()) );
    channelInfo.setFieldValue( IGTChannelInfoEntity.SECURITY_PROFILE, StaticUtils.longValue(form.getSecurityProfile()) );
    channelInfo.setFieldValue( IGTChannelInfoEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()) );

    // FlowControl
    IGTFlowControlInfoEntity flowControl = (IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO);
    flowControl.setFieldValue( IGTFlowControlInfoEntity.IS_ZIP, StaticUtils.booleanValue(form.getIsZip()) );
    flowControl.setFieldValue( IGTFlowControlInfoEntity.ZIP_THRESHOLD, StaticUtils.integerValue(form.getZipThreshold()) );
    flowControl.setFieldValue( IGTFlowControlInfoEntity.IS_SPLIT, StaticUtils.booleanValue(form.getIsSplit()) );
    flowControl.setFieldValue( IGTFlowControlInfoEntity.SPLIT_SIZE, StaticUtils.integerValue(form.getSplitSize()) );
    flowControl.setFieldValue( IGTFlowControlInfoEntity.SPLIT_THRESHOLD, StaticUtils.integerValue(form.getSplitThreshold()) );

  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTChannelInfoEntity cInfo = (IGTChannelInfoEntity)entity;
    String isPartner = actionContext.getRequest().getParameter("isPartner");
    if(isPartner != null)
    {
      cInfo.setFieldValue(IGTChannelInfoEntity.IS_PARTNER, StaticUtils.booleanValue(isPartner));
    }
    else
    {
      cInfo.setFieldValue(IGTChannelInfoEntity.IS_PARTNER, Boolean.FALSE);
    }
    cInfo.setFieldValue(IGTChannelInfoEntity.TPT_PROTOCOL_TYPE, IGTChannelInfoEntity.TPT_PROTOCOL_TYPE_JMS);

    // FlowControl
    IGTChannelInfoManager mgr = (IGTChannelInfoManager)cInfo.getSession().getManager(IGTManager.MANAGER_CHANNEL_INFO);
    IGTFlowControlInfoEntity flowControl = mgr.newFlowControlInfo();
    flowControl.setFieldValue(IGTFlowControlInfoEntity.ZIP_THRESHOLD, new Integer(2048));
    flowControl.setFieldValue(IGTFlowControlInfoEntity.SPLIT_SIZE, new Integer(64));
    flowControl.setFieldValue(IGTFlowControlInfoEntity.SPLIT_THRESHOLD, new Integer(1048));
    
    //NSL20060425 Default to false
    flowControl.setFieldValue(IGTFlowControlInfoEntity.IS_ZIP, Boolean.FALSE);
    flowControl.setFieldValue(IGTFlowControlInfoEntity.IS_SPLIT, Boolean.FALSE);
    
    cInfo.setFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO, flowControl);
  }
}