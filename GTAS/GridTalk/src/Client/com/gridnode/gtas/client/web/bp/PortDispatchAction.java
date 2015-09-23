/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2002-05-22     Daniel D'Cotta      Added running sequence number support
 * 2005-08-23     Tam Wei Xiang       For method initialiseActionForm(),
 *                                    updateEntityFields(), the set method related
 *                                    to attachmentDir has been removed(To improve 
 *                                    the Attachment functionality of GTAS) 
 *
 *                                    For method validateActionForm(), the validatation
 *                                    for field AttachmentDir is removed.
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPortEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PortDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PORT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PortRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PORT_UPDATE : IDocumentKeys.PORT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTPortEntity port = (IGTPortEntity)entity;
    PortAForm form = (PortAForm)actionContext.getActionForm();

    form.setName( port.getFieldString(IGTPortEntity.NAME) );
    form.setDescription( port.getFieldString(IGTPortEntity.DESCRIPTION) );
    form.setHostDir( port.getFieldString(IGTPortEntity.HOST_DIR) );
    form.setIsRfc( port.getFieldString(IGTPortEntity.IS_RFC) );
    form.setRfc( port.getFieldString(IGTPortEntity.RFC) );
    form.setIsDiffFileName( port.getFieldString(IGTPortEntity.IS_DIFF_FILE_NAME) );
    form.setIsOverwrite( port.getFieldString(IGTPortEntity.IS_OVERWRITE) );
    form.setFileName( port.getFieldString(IGTPortEntity.FILE_NAME) );
    form.setIsAddFileExt( port.getFieldString(IGTPortEntity.IS_ADD_FILE_EXT) );
    form.setFileExtType( port.getFieldString(IGTPortEntity.FILE_EXT_TYPE) );
    form.setFileExtValue( port.getFieldString(IGTPortEntity.FILE_EXT_VALUE) );
    form.setStartNum( port.getFieldString(IGTPortEntity.START_NUM) );
    form.setRolloverNum( port.getFieldString(IGTPortEntity.ROLLOVER_NUM) );
    form.setNextNum( port.getFieldString(IGTPortEntity.NEXT_NUM) );
    form.setIsPadded( port.getFieldString(IGTPortEntity.IS_PADDED) );
    form.setFixNumLength( port.getFieldString(IGTPortEntity.FIX_NUM_LENGTH) );
    form.setIsExportGdoc( port.getFieldString(IGTPortEntity.IS_EXPORT_GDOC) );
    form.setFileGrouping( port.getFieldString(IGTPortEntity.FILE_GROUPING) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PortAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PORT;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTPortEntity port = (IGTPortEntity)entity;
    PortAForm form = (PortAForm)actionForm;

    basicValidateString(errors, IGTPortEntity.NAME, form, port);
    basicValidateString(errors, IGTPortEntity.DESCRIPTION, form, port);
    basicValidateString(errors, IGTPortEntity.IS_RFC, form, port);
    boolean isRfc = StaticUtils.primitiveBooleanValue( form.getIsRfc() );
    if(isRfc)
    {
      basicValidateString(errors, IGTPortEntity.RFC, form, port);
    }
    basicValidateString(errors, IGTPortEntity.HOST_DIR, form, port);
    basicValidateString(errors, IGTPortEntity.IS_OVERWRITE, form, port);
    basicValidateString(errors, IGTPortEntity.IS_DIFF_FILE_NAME, form, port);
    boolean isDiffFileName = StaticUtils.primitiveBooleanValue( form.getIsDiffFileName() );
    if(isDiffFileName)
    {
      basicValidateString(errors, IGTPortEntity.FILE_NAME, form, port);
      basicValidateString(errors, IGTPortEntity.IS_ADD_FILE_EXT, form, port);
      boolean isAddFileExt = StaticUtils.primitiveBooleanValue( form.getIsAddFileExt() );
      if(isAddFileExt)
      {
        basicValidateString(errors, IGTPortEntity.FILE_EXT_TYPE, form, port);
        if(IGTPortEntity.FILE_EXT_TYPE_SEQUENCE.equals( StaticUtils.integerValue( form.getFileExtType() ) ))
        {
          basicValidateString(errors, IGTPortEntity.START_NUM, form, port);
          basicValidateString(errors, IGTPortEntity.ROLLOVER_NUM, form, port);
          basicValidateString(errors, IGTPortEntity.NEXT_NUM, form, port);
          basicValidateString(errors, IGTPortEntity.IS_PADDED, form, port);
          boolean isPadded = StaticUtils.primitiveBooleanValue( form.getIsPadded() );
          if(isPadded)
          {
            basicValidateString(errors, IGTPortEntity.FIX_NUM_LENGTH, form, port);
          }
        }
        else
        {
          basicValidateString(errors, IGTPortEntity.FILE_EXT_VALUE, form, port);
          //do we need special validation checking for fileExtValue or will the above line work
          //for both situations?
        }
      }
    }
    //Modified by Wei Xiang Validation for Attachment Field is not require. 
    //we removed the Attachment Directory field from the UI 
    //basicValidateString(errors, port.ATTACHMENT_DIR, form, port);
    basicValidateString(errors, IGTPortEntity.IS_EXPORT_GDOC, form, port);
    
    /* By Wei Xiang: The export directory of GDoc will be same as the host_directory
     *               so the following checking can be omitted. (To improve the GTAS attachment)
    boolean isExportGdoc = StaticUtils.primitiveBooleanValue( form.getIsExportGdoc() );
    if(isExportGdoc)
    {
      if(!StaticUtils.stringNotEmpty(form.getAttachmentDir()))
      {
        EntityFieldValidator.addFieldError(errors,
          "attachmentDir",
          port.getType(),
          EntityFieldValidator.REQUIRED,
          null);
      }
    }
    */
    
    basicValidateString(errors, IGTPortEntity.FILE_GROUPING, form, port);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTPortEntity port = (IGTPortEntity)entity;
    PortAForm form = (PortAForm)actionContext.getActionForm();

    port.setFieldValue( IGTPortEntity.NAME, form.getName() );
    port.setFieldValue( IGTPortEntity.DESCRIPTION, form.getDescription() );
    port.setFieldValue( IGTPortEntity.IS_RFC, StaticUtils.booleanValue(form.getIsRfc()) );
    Long rfcUid = StaticUtils.longValue(form.getRfc());
    port.setFieldValue( IGTPortEntity.RFC, rfcUid );
    port.setFieldValue( IGTPortEntity.HOST_DIR, form.getHostDir() );
    port.setFieldValue( IGTPortEntity.IS_OVERWRITE, StaticUtils.booleanValue(form.getIsOverwrite()) );
    port.setFieldValue( IGTPortEntity.IS_DIFF_FILE_NAME, StaticUtils.booleanValue(form.getIsDiffFileName()) );
    port.setFieldValue( IGTPortEntity.FILE_NAME, form.getFileName() );
    port.setFieldValue( IGTPortEntity.IS_ADD_FILE_EXT, StaticUtils.booleanValue(form.getIsAddFileExt()) );
    port.setFieldValue( IGTPortEntity.FILE_EXT_TYPE, StaticUtils.integerValue(form.getFileExtType()) );
    port.setFieldValue( IGTPortEntity.FILE_EXT_VALUE, form.getFileExtValue() );
    port.setFieldValue( IGTPortEntity.START_NUM, StaticUtils.integerValue(form.getStartNum()) );
    port.setFieldValue( IGTPortEntity.ROLLOVER_NUM, StaticUtils.integerValue(form.getRolloverNum()) );
    port.setFieldValue( IGTPortEntity.NEXT_NUM, StaticUtils.integerValue(form.getNextNum()) );
    port.setFieldValue( IGTPortEntity.IS_PADDED, StaticUtils.booleanValue(form.getIsPadded()) );
    port.setFieldValue( IGTPortEntity.FIX_NUM_LENGTH, StaticUtils.integerValue(form.getFixNumLength()) );
    port.setFieldValue( IGTPortEntity.IS_EXPORT_GDOC, StaticUtils.booleanValue(form.getIsExportGdoc()) );
    //By Wei Xiang
    //port.setFieldValue( port.ATTACHMENT_DIR, form.getHostDir() ); //port.setFieldValue( port.ATTACHMENT_DIR, form.getAttachmentDir() );
    port.setFieldValue( IGTPortEntity.FILE_GROUPING, StaticUtils.integerValue(form.getFileGrouping()) );
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    entity.setFieldValue(IGTPortEntity.FILE_EXT_TYPE, IGTPortEntity.FILE_EXT_TYPE_DATE_TIME);
    entity.setFieldValue(IGTPortEntity.FILE_GROUPING, IGTPortEntity.FILE_GROUPING_OPTION_ATTACHMENT_GDOC);
  }
}