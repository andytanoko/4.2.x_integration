/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBizCertMappingEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class BizCertMappingDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_BIZ_CERT_MAPPING;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new BizCertMappingRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.BIZ_CERT_MAPPING_UPDATE : IDocumentKeys.BIZ_CERT_MAPPING_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)entity;
    BizCertMappingAForm form = (BizCertMappingAForm)actionContext.getActionForm();

    form.setPartnerId( bcm.getFieldString(IGTBizCertMappingEntity.PARTNER_ID) );
    form.setPartnerCert( bcm.getFieldString(IGTBizCertMappingEntity.PARTNER_CERT) );
    form.setOwnCert( bcm.getFieldString(IGTBizCertMappingEntity.OWN_CERT) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new BizCertMappingAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_BIZ_CERT_MAPPING;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)entity;
    BizCertMappingAForm form = (BizCertMappingAForm)actionForm;

    basicValidateString( errors, IGTBizCertMappingEntity.PARTNER_ID, form, bcm );
    basicValidateString( errors, IGTBizCertMappingEntity.PARTNER_CERT, form, bcm );
    basicValidateString( errors, IGTBizCertMappingEntity.OWN_CERT, form, bcm );
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)entity;
    BizCertMappingAForm form = (BizCertMappingAForm)actionContext.getActionForm();

    bcm.setFieldValue( IGTBizCertMappingEntity.PARTNER_ID, form.getPartnerId() );
    bcm.setFieldValue( IGTBizCertMappingEntity.PARTNER_CERT, StaticUtils.longValue(form.getPartnerCert()) );
    bcm.setFieldValue( IGTBizCertMappingEntity.OWN_CERT, StaticUtils.longValue(form.getOwnCert()) );
  }

  /*protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {

  }*/
}