/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRfcEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class RfcDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_RFC;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new RfcRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.RFC_UPDATE : IDocumentKeys.RFC_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTRfcEntity rfc = (IGTRfcEntity)entity;
    RfcAForm form = (RfcAForm)actionContext.getActionForm();

    form.setName( rfc.getFieldString(IGTRfcEntity.NAME) );
    form.setDescription( rfc.getFieldString(IGTRfcEntity.DESCRIPTION) );
    form.setHost( rfc.getFieldString(IGTRfcEntity.HOST) );
    form.setPortNumber( rfc.getFieldString(IGTRfcEntity.PORT_NUMBER) );
    form.setCommandFileDir( rfc.getFieldString(IGTRfcEntity.COMMAND_FILE_DIR) );
    form.setCommandFile( rfc.getFieldString(IGTRfcEntity.COMMAND_FILE) );
    form.setCommandLine( rfc.getFieldString(IGTRfcEntity.COMMAND_LINE) );
    form.setUseCommandFile( rfc.getFieldString(IGTRfcEntity.USE_COMMAND_FILE) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new RfcAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_RFC;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTRfcEntity rfc = (IGTRfcEntity)entity;
    RfcAForm form = (RfcAForm)actionForm;

    basicValidateString(errors,IGTRfcEntity.NAME,form,rfc);
    basicValidateString(errors,IGTRfcEntity.DESCRIPTION,form,rfc);
    basicValidateString(errors,IGTRfcEntity.HOST,form,rfc);
    basicValidateString(errors,IGTRfcEntity.PORT_NUMBER,form,rfc);
    basicValidateString(errors,IGTRfcEntity.USE_COMMAND_FILE,form,rfc);
    boolean useCommandFile = StaticUtils.primitiveBooleanValue( form.getUseCommandFile() );
    if(useCommandFile)
    {
      basicValidateString(errors,IGTRfcEntity.COMMAND_FILE_DIR,form,rfc);
      basicValidateString(errors,IGTRfcEntity.COMMAND_FILE,form,rfc);
      basicValidateString(errors,IGTRfcEntity.COMMAND_LINE,form,rfc);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTRfcEntity rfc = (IGTRfcEntity)entity;
    RfcAForm form = (RfcAForm)actionContext.getActionForm();

    rfc.setFieldValue( IGTRfcEntity.NAME, form.getName() );
    rfc.setFieldValue( IGTRfcEntity.DESCRIPTION, form.getDescription() );
    rfc.setFieldValue( IGTRfcEntity.HOST, form.getHost() );
    rfc.setFieldValue( IGTRfcEntity.PORT_NUMBER, StaticUtils.integerValue(form.getPortNumber()) );
    rfc.setFieldValue( IGTRfcEntity.USE_COMMAND_FILE, StaticUtils.booleanValue(form.getUseCommandFile()) );
    rfc.setFieldValue( IGTRfcEntity.COMMAND_FILE_DIR, form.getCommandFileDir() );
    rfc.setFieldValue( IGTRfcEntity.COMMAND_FILE, form.getCommandFile() );
    rfc.setFieldValue( IGTRfcEntity.COMMAND_LINE, form.getCommandLine() );
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    
  }
}