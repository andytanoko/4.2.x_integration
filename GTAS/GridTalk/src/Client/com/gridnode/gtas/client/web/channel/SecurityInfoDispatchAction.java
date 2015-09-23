/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSecurityInfoEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SecurityInfoDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_channel";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SECURITY_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SecurityInfoRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SECURITY_INFO_UPDATE : IDocumentKeys.SECURITY_INFO_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SecurityInfoAForm form = (SecurityInfoAForm)actionContext.getActionForm();
    IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)entity;
    form.setIsNewEntity(entity.isNewEntity()); //20021008AH

    form.setName( sInfo.getFieldString(IGTSecurityInfoEntity.NAME) );
    form.setDescription( sInfo.getFieldString(IGTSecurityInfoEntity.DESCRIPTION) );
    form.setEncType( sInfo.getFieldString(IGTSecurityInfoEntity.ENC_TYPE) );
    form.setEncLevel( sInfo.getFieldString(IGTSecurityInfoEntity.ENC_LEVEL) );
    form.setEncCert( sInfo.getFieldString(IGTSecurityInfoEntity.ENC_CERT) );
    form.setSigType( sInfo.getFieldString(IGTSecurityInfoEntity.SIG_TYPE) );
    form.setDigestAlgorithm( sInfo.getFieldString(IGTSecurityInfoEntity.DIGEST_ALGORITHM) );
    form.setSigEncCert( sInfo.getFieldString(IGTSecurityInfoEntity.SIG_ENC_CERT) );
    form.setIsPartner( sInfo.getFieldString(IGTSecurityInfoEntity.IS_PARTNER) ); //20021008AH
    form.setPartnerCategory( sInfo.getFieldString(IGTSecurityInfoEntity.PARTNER_CAT) ); //20021008AH
    form.setRefId( sInfo.getFieldString(IGTSecurityInfoEntity.REF_ID) ); //20021008AH
    form.setEncryptionAlgorithm (sInfo.getFieldString(IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM)); // 20031126 DDJ
    form.setSequence            (sInfo.getFieldString(IGTSecurityInfoEntity.SEQUENCE));             // 20031126 DDJ
    form.setCompressionType     (sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_TYPE));     // 20031126 DDJ
    form.setCompressionMethod   (sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_METHOD));   // 20031126 DDJ
    form.setCompressionLevel    (sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_LEVEL));    // 20031126 DDJ
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SecurityInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SECURITY_INFO;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    SecurityInfoAForm siForm = (SecurityInfoAForm)form;

    basicValidateString(errors, IGTSecurityInfoEntity.NAME, form, entity);
    basicValidateString(errors, IGTSecurityInfoEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTSecurityInfoEntity.ENC_TYPE, form, entity);
    
    if( StaticUtils.primitiveBooleanValue(siForm.getIsPartner()) )
    {
      if(((siForm.encTypeHasValue() ? 1 : 0) + (siForm.sigTypeHasValue() ? 1 : 0) + (siForm.compressionTypeHasValue() ? 1 : 0)) > 1)
      {
        basicValidateString(errors, IGTSecurityInfoEntity.SEQUENCE, form, entity);  // 20031126 DDJ
      }

      basicValidateString(errors, IGTSecurityInfoEntity.SIG_TYPE, form, entity);
      basicValidateString(errors, IGTSecurityInfoEntity.COMPRESSION_TYPE, form, entity);  // 20031126 DDJ
    }

    if( siForm.encTypeHasValue() )
    {
      if(IGTSecurityInfoEntity.ENC_TYPE_SMIME.equals(siForm.getEncType()))
      { // 20031126 DDJ
        basicValidateString(errors, IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM, siForm, entity);
      } 
      
      basicValidateString(errors, IGTSecurityInfoEntity.ENC_LEVEL, siForm, entity);
      basicValidateString(errors, IGTSecurityInfoEntity.ENC_CERT, siForm, entity);
    }

    if( siForm.sigTypeHasValue() )
    {
      basicValidateString(errors, IGTSecurityInfoEntity.DIGEST_ALGORITHM, siForm, entity);
      basicValidateString(errors, IGTSecurityInfoEntity.SIG_ENC_CERT, siForm, entity);
    }

    if( siForm.compressionTypeHasValue() )
    { // 20031126 DDJ
      basicValidateString(errors, IGTSecurityInfoEntity.COMPRESSION_METHOD, siForm, entity);
      basicValidateString(errors, IGTSecurityInfoEntity.COMPRESSION_LEVEL, siForm, entity);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    SecurityInfoAForm form = (SecurityInfoAForm)actionContext.getActionForm();
    IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)entity;

    sInfo.setFieldValue( IGTSecurityInfoEntity.NAME, form.getName() );
    sInfo.setFieldValue( IGTSecurityInfoEntity.DESCRIPTION, form.getDescription() );
    sInfo.setFieldValue( IGTSecurityInfoEntity.ENC_TYPE, form.getEncType() );
    sInfo.setFieldValue( IGTSecurityInfoEntity.ENC_LEVEL, StaticUtils.integerValue(form.getEncLevel()) );
    sInfo.setFieldValue( IGTSecurityInfoEntity.ENC_CERT, StaticUtils.longValue(form.getEncCert()) );
    sInfo.setFieldValue( IGTSecurityInfoEntity.SIG_TYPE, form.getSigType() );
    sInfo.setFieldValue( IGTSecurityInfoEntity.DIGEST_ALGORITHM, form.getDigestAlgorithm() );
    sInfo.setFieldValue( IGTSecurityInfoEntity.SIG_ENC_CERT, StaticUtils.longValue(form.getSigEncCert()) );
    sInfo.setFieldValue( IGTSecurityInfoEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()) ); //20021008AH
    sInfo.setFieldValue( IGTSecurityInfoEntity.SEQUENCE, form.getSequence() );                            // 20031126 DDJ
    sInfo.setFieldValue( IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM, form.getEncryptionAlgorithm() );     // 20031126 DDJ
    sInfo.setFieldValue( IGTSecurityInfoEntity.COMPRESSION_TYPE, form.getCompressionType() );             // 20031126 DDJ
    sInfo.setFieldValue( IGTSecurityInfoEntity.COMPRESSION_METHOD, form.getCompressionMethod() );         // 20031126 DDJ
    sInfo.setFieldValue( IGTSecurityInfoEntity.COMPRESSION_LEVEL, form.getCompressionLevelAsInteger() );  // 20031126 DDJ
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)entity;

    String isPartner = actionContext.getRequest().getParameter("isPartner");
    if(isPartner != null)
    {
      sInfo.setFieldValue(IGTSecurityInfoEntity.IS_PARTNER, StaticUtils.booleanValue(isPartner));
    }

    sInfo.setFieldValue(IGTSecurityInfoEntity.ENC_TYPE, IGTSecurityInfoEntity.ENC_TYPE_NONE);
    sInfo.setFieldValue(IGTSecurityInfoEntity.SIG_TYPE, IGTSecurityInfoEntity.SIG_TYPE_NONE);
    sInfo.setFieldValue(IGTSecurityInfoEntity.COMPRESSION_TYPE, IGTSecurityInfoEntity.COMPRESSION_TYPE_NONE); //20031126 DDJ
  }
}