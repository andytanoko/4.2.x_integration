/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 * 2003-01-23     Andrew Hill         Support sendChannelUid in responding role
 * 2003-03-17     Daniel D'Cotta      Modified to also display docType if TwoActionProcess
 */
package com.gridnode.gtas.client.web.bp;

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

public class ProcessMappingDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PROCESS_MAPPING;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ProcessMappingRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PROCESS_MAPPING_UPDATE : IDocumentKeys.PROCESS_MAPPING_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;

    ProcessMappingAForm form = (ProcessMappingAForm)actionContext.getActionForm();

    form.setProcessDef( mapping.getFieldString(IGTProcessMappingEntity.PROCESS_DEF) );
    form.setIsInitiatingRole( mapping.getFieldString(IGTProcessMappingEntity.IS_INITIATING_ROLE) );
    form.setDocType( mapping.getFieldString(IGTProcessMappingEntity.DOC_TYPE) );
    form.setSendChannelUid( mapping.getFieldString(IGTProcessMappingEntity.SEND_CHANNEL_UID) );
    form.setPartnerId( mapping.getFieldString(IGTProcessMappingEntity.PARTNER_ID) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ProcessMappingAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PROCESS_MAPPING;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;
    ProcessMappingAForm form = (ProcessMappingAForm)actionForm;

    basicValidateString(errors, IGTProcessMappingEntity.PROCESS_DEF, form, mapping);
    basicValidateString(errors, IGTProcessMappingEntity.PARTNER_ID, form, mapping);
    basicValidateString(errors, IGTProcessMappingEntity.IS_INITIATING_ROLE, form, mapping);
    //20030123AH - Now allow sendChannelUid to be selected for responding role
    basicValidateString(errors, IGTProcessMappingEntity.SEND_CHANNEL_UID, form, mapping);

    boolean displayDocType = false;
    if(form.getIsInitiatingRolePrimitiveBoolean())
    {
      // Do not hide docType if TwoActionProcess
      String processDefName = form.getProcessDef();
      if(processDefName != null && processDefName.length() != 0)
      {
        IGTSession gtasSession = mapping.getSession();
        IGTProcessDefManager manager = (IGTProcessDefManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_DEF);
        IGTProcessDefEntity processDef = (IGTProcessDefEntity)StaticUtils.getFirst(manager.getByKey(processDefName, IGTProcessDefEntity.DEF_NAME));
        if(processDef != null)
          if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE)))
            displayDocType = true;
      }
    }
    else
    {
      // Do not hide docType if respondingRole
      displayDocType = true;
    }
    if(displayDocType)
      basicValidateString(errors, IGTProcessMappingEntity.DOC_TYPE, form, mapping);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;
    ProcessMappingAForm form = (ProcessMappingAForm)actionContext.getActionForm();
    boolean isInitiatingRole = form.getIsInitiatingRolePrimitiveBoolean();
    mapping.setFieldValue(IGTProcessMappingEntity.PROCESS_DEF, form.getProcessDef() );
    mapping.setFieldValue(IGTProcessMappingEntity.PARTNER_ID, form.getPartnerId() );
    mapping.setFieldValue(IGTProcessMappingEntity.IS_INITIATING_ROLE, new Boolean(isInitiatingRole) );
    mapping.setFieldValue(IGTProcessMappingEntity.SEND_CHANNEL_UID, StaticUtils.longValue(form.getSendChannelUid()) );
    mapping.setFieldValue(IGTProcessMappingEntity.DOC_TYPE, form.getDocType() );
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;

    mapping.setFieldValue(IGTProcessMappingEntity.IS_INITIATING_ROLE, Boolean.TRUE );
  }
}