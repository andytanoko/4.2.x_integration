/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 * 2002-08-20     Andrew Hill         Add vf handling for BE / ChannelInfo
 * 2003-04-03     Andrew Hill         Made be field mandatory
 * 2003-07-16     Andrew Hill         Kill/modify deprecated method overrides
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PartnerDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PARTNER;
  }

  /**
   * Create a PartnerRenderer and pass it the appropriate collections for partnerTypes and Groups
   */
  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PartnerRenderer(rContext, edit);
  }

  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PARTNER_UPDATE : IDocumentKeys.PARTNER_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
      PartnerAForm form = (PartnerAForm)actionContext.getActionForm();

      form.setDescription( entity.getFieldString(IGTPartnerEntity.DESCRIPTION) );
      form.setName( entity.getFieldString(IGTPartnerEntity.NAME) );
      form.setPartnerGroup( entity.getFieldString(IGTPartnerEntity.PARTNER_GROUP) );
      form.setPartnerId( entity.getFieldString(IGTPartnerEntity.PARTNER_ID) );
      form.setPartnerType( entity.getFieldString(IGTPartnerEntity.PARTNER_TYPE) );
      form.setState( entity.getFieldString(IGTPartnerEntity.STATE) );
      form.setCreated( entity.getFieldString(IGTPartnerEntity.CREATE_TIME));
      form.setCreator( entity.getFieldString(IGTPartnerEntity.CREATE_BY));

      form.setBe( entity.getFieldString(IGTPartnerEntity.BE) );
      form.setChannel( entity.getFieldString(IGTPartnerEntity.CHANNEL) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PartnerAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PARTNER;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    basicValidateString(actionErrors, IGTPartnerEntity.DESCRIPTION, form, entity);
    basicValidateString(actionErrors, IGTPartnerEntity.NAME, form, entity);
    basicValidateString(actionErrors, IGTPartnerEntity.PARTNER_ID, form, entity);
    basicValidateString(actionErrors, IGTPartnerEntity.STATE, form, entity);
    //Check for presence of ids for group and type. Doesn't check if they actually exist though.
    basicValidateString(actionErrors, IGTPartnerEntity.PARTNER_GROUP, form, entity);
    basicValidateString(actionErrors, IGTPartnerEntity.PARTNER_TYPE, form, entity);

    basicValidateString(actionErrors, IGTPartnerEntity.PARTNER_TYPE, form, entity);

    basicValidateString(actionErrors, IGTPartnerEntity.BE, form, entity); //20030403AH - BE is now mandatory

    if(StaticUtils.longValue( ((PartnerAForm)form).getBe() ) != null)
    {
      basicValidateString(actionErrors, IGTPartnerEntity.CHANNEL, form, entity);
    }
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    PartnerAForm form = (PartnerAForm)actionContext.getActionForm();

    // PartnerGroup isnt mandatory so we must null if no value in form.
    String partnerGroupString = form.getPartnerGroup();
    Long partnerGroup = null;
    if( (partnerGroupString == null) || partnerGroupString.equals(""))
    {
      partnerGroup = null;
    }
    else
    {
      partnerGroup = new Long(partnerGroupString);
    }

    if(StaticUtils.longValue( ((PartnerAForm)form).getBe() ) == null)
    {
      entity.setFieldValue(IGTPartnerEntity.CHANNEL, null);
    }

    entity.setFieldValue(IGTPartnerEntity.DESCRIPTION, form.getDescription());
    entity.setFieldValue(IGTPartnerEntity.NAME, form.getName());
    entity.setFieldValue(IGTPartnerEntity.PARTNER_GROUP, partnerGroup);
    entity.setFieldValue(IGTPartnerEntity.PARTNER_ID, form.getPartnerId());
    entity.setFieldValue(IGTPartnerEntity.PARTNER_TYPE, new Long(form.getPartnerType()));
    entity.setFieldValue(IGTPartnerEntity.STATE, new Short(form.getState()));

    entity.setFieldValue(IGTPartnerEntity.BE, StaticUtils.longValue( form.getBe() ) );
    entity.setFieldValue(IGTPartnerEntity.CHANNEL, StaticUtils.longValue( form.getChannel() ) );
  }

  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext thisOperation,
                                  OperationContext childOperation,
                                  String mappingName)
    throws GTClientException
  { //20030716AH - add mappingName param so not overriding deprecated version
    String divertTo = (String)actionContext.getRequest().getParameter("divertTo");
    if("divertCreatePartnerGroup".equals((String)divertTo))
    {
      PartnerAForm form = (PartnerAForm)thisOperation.getActionForm();
      childOperation.setAttribute("partner.partnerType", form.getPartnerType());
    }
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    entity.setFieldValue(IGTPartnerEntity.STATE,IGTPartnerEntity.STATE_ENABLED);
  }
}