/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: ArchiveDispatchAction.java
 * 
 **************************************************************************** 
 * Date             Author                 Changes
 **************************************************************************** 
 * 2008-12-17       Ong Eu Soon            Created
 * 2008-03-31       Tam Wei Xiang          #122 
 *                                         1) Comment "doSave", we can leverage on
 *                                         the framework instead of hand coding ourselves.
 *                                         2) Enhance the field validation
 *                                         3) add field isArchiveFrequencyOnce, archiveOlderThan,
 *                                            clientTZ
 * 2009-04-11     Tam Wei Xiang            #122 - Remove ARCHIVE_NAME
 */

package com.gridnode.gtas.client.web.archive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTArchiveEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.model.dbarchive.IArchiveMetaInfo;
import com.gridnode.gtas.server.activation.helpers.Logger;

public class ArchiveDispatchAction extends EntityDispatchAction2
{
  private static final Number[] _fields = {
      IGTArchiveEntity.ARCHIVE_ID, 
      IGTArchiveEntity.ARCHIVE_DESCRIPTION,
      IGTArchiveEntity.ARCHIVE_TYPE,
      IGTArchiveEntity.FROM_START_DATE, IGTArchiveEntity.FROM_START_TIME,
      IGTArchiveEntity.TO_START_DATE, IGTArchiveEntity.TO_START_TIME,
      IGTArchiveEntity.ARCHIVE_TYPE,
      IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
      IGTArchiveEntity.PROCESS_DEF_NAME_LIST,
      IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
      IGTArchiveEntity.FOLDER_LIST, IGTArchiveEntity.DOCUMENT_TYPE_LIST,
      IGTArchiveEntity.ARCHIVE_OLDER_THAN
  };

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ARCHIVE;
  }

  protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
  {
    return IGTManager.MANAGER_ARCHIVE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit) throws GTClientException
  {
    return new ArchiveRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
  {
    return (
        edit
          ? IDocumentKeys.ARCHIVE_UPDATE
          : IDocumentKeys.ARCHIVE_VIEW);
   
  }

  protected void initialiseActionForm(ActionContext actionContext,
                                      IGTEntity entity) throws GTClientException
  {
    ArchiveAForm form = (ArchiveAForm) actionContext
        .getActionForm();
    IGTArchiveEntity archive = (IGTArchiveEntity) entity;
    form.setIsNewEntity(archive.isNewEntity());
    
    form.setArchiveID(archive
        .getFieldString(IGTArchiveEntity.ARCHIVE_ID));
    form.setArchiveDescription(archive
        .getFieldString(IGTArchiveEntity.ARCHIVE_DESCRIPTION));
    form.setArchiveType(archive
        .getFieldString(IGTArchiveEntity.ARCHIVE_TYPE));
    form.setFromStartDate(archive
        .getFieldString(IGTArchiveEntity.FROM_START_DATE));
    form.setFromStartTime(archive
        .getFieldString(IGTArchiveEntity.FROM_START_TIME));
    form.setToStartDate(archive
        .getFieldString(IGTArchiveEntity.TO_START_DATE));
    form.setToStartTime(archive
        .getFieldString(IGTArchiveEntity.TO_START_TIME));
    form.setProcessDefNameList(archive
        .getFieldStringArray(IGTArchiveEntity.PROCESS_DEF_NAME_LIST));
    form.setIsIncludeIncomplete(archive
        .getFieldString(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES));
    form.setFolderList(archive
        .getFieldStringArray(IGTArchiveEntity.FOLDER_LIST));
    form.setDocumentTypeList(archive
        .getFieldStringArray(IGTArchiveEntity.DOCUMENT_TYPE_LIST));
    form.setIsEnableRestoreArchived(archive
        .getFieldString(IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED));
    form.setIsEnableSearchArchived(archive
        .getFieldString(IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED));
    form.setPartnerIDForArchive(archive
        .getFieldStringArray(IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE));
    
    form.setArchiveRecordOlderThan(archive.getFieldString(IGTArchiveEntity.ARCHIVE_OLDER_THAN));
    form.setIsArchiveFrequencyOnce(archive.getFieldString(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE));
    
    System.out.println("Archive form init: "+archive.getFieldString(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE));
    
    
  }

  protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
  {
    ArchiveAForm form = new ArchiveAForm();
    return form;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors) throws GTClientException
  {
    try
    {
      System.out.println("Validate archive action form.");
      
      ArchiveAForm form = (ArchiveAForm) actionForm;
      IGTArchiveEntity archive = (IGTArchiveEntity) entity;
      basicValidateString(errors, IGTArchiveEntity.ARCHIVE_ID, form,
                          archive);
      basicValidateString(errors, IGTArchiveEntity.ARCHIVE_DESCRIPTION, form,
                          archive);
      basicValidateString(errors, IGTArchiveEntity.ARCHIVE_TYPE, form,
                          archive);
      
      // TWx 14092006
      basicValidateString(errors,
                          IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
                          form, archive);
      basicValidateString(errors,
                          IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,
                          form, archive);
      basicValidateStringArray(errors, IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE
                               , form,
                               archive);

      //TWX 30032009
      basicValidateString(errors, IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE, form, archive);
      String isArchiveOnce = form.getisArchiveFrequencyOnce() == null ? "false" : form.getisArchiveFrequencyOnce();
      if( ! StaticUtils.booleanValue(isArchiveOnce))
      {
        basicValidateString(errors, IGTArchiveEntity.ARCHIVE_OLDER_THAN, form, archive);
      }
      else
      {
        String fromStartDateField = getFieldname(archive, IGTArchiveEntity.FROM_START_DATE);
        String fromStartTimeField = getFieldname(archive, IGTArchiveEntity.FROM_START_TIME);
        String toStartDateField = getFieldname(archive, IGTArchiveEntity.TO_START_DATE);
        String toStartTimeField = getFieldname(archive, IGTArchiveEntity.TO_START_TIME);
        
        validateDate(archive.getType(), fromStartDateField, form.getFromStartDate(), "yyyy-MM-dd", errors, true);
        validateDate(archive.getType(), fromStartTimeField, form.getFromStartTime(), "HH:mm", errors, true);
        validateDate(archive.getType(), toStartDateField, form.getToStartDate(), "yyyy-MM-dd", errors, true);
        validateDate(archive.getType(), toStartTimeField, form.getToStartTime(), "HH:mm", errors, true);
      }
      
      
      
      System.out.println("validateActionForm Usr entered archive older than: "+form.getArchiveRecordOlderThan()+" isArchiveOnce: "+form.getisArchiveFrequencyOnce());
      
      String archiveType = form.getArchiveType();
      if (IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE
          .equals(archiveType))
      {
        basicValidateStringArray(errors, IGTArchiveEntity.PROCESS_DEF_NAME_LIST,
                                 form, archive);
        basicValidateString(errors,
                            IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES, form,
                            archive);
      }
      else if (IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT
          .equals(archiveType))
      {
        basicValidateStringArray(errors, IGTArchiveEntity.FOLDER_LIST,
                                 form, archive);
        basicValidateStringArray(errors, IGTArchiveEntity.DOCUMENT_TYPE_LIST,
                                 form, archive);
      }

    }
    catch (Throwable t)
    {
      throw new GTClientException("Error validating archive", t);
    }
  }

  private String getFieldname(IGTEntity entity, Number field)
    throws GTClientException
  {
    IGTFieldMetaInfo fmi1 = entity.getFieldMetaInfo(field);
    if(fmi1 == null)
    {
      throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                              + field + " of entity " + entity);
    }
    return fmi1.getFieldName();
  }
  
  private void validateDate(String entityType, String field, String value,
                              String format, ActionErrors actionErrors, boolean isRequired)
  {
      System.out.println("ArchiveDispatchAction:validateDate -->fieldID:"+field+" value:"+value+" isRequired:"+isRequired);
      Date date = null;
      if (!value.equals(""))
      {         
         date = DateUtils.parseDate(value, null, null, new SimpleDateFormat(format));
                              
         if (date == null)
         {
            EntityFieldValidator.addFieldError(actionErrors, field,
                                    entityType, EntityFieldValidator.INVALID,null);
         }
      }
      else
      {
        if(isRequired)
        {
          System.out.println("ArchiveDispatchAction: IsRequired. Adding error Required for field:"+field);
          EntityFieldValidator.addFieldError(actionErrors, field, 
                                             entityType, EntityFieldValidator.REQUIRED, null);
        }
      }
  }
  
  protected void updateEntityFields(ActionContext actionContext,
                                    IGTEntity entity) throws GTClientException
  {

    IGTArchiveEntity archive = (IGTArchiveEntity) entity;
    ArchiveAForm form = (ArchiveAForm) actionContext
        .getActionForm();

    String archiveType=form.getArchiveType();
    archive.setFieldValue(IGTArchiveEntity.ARCHIVE_ID, form
        .getArchiveID());

    archive.setFieldValue(IGTArchiveEntity.ARCHIVE_DESCRIPTION, form
        .getArchiveDescription());

    archive.setFieldValue(IGTArchiveEntity.ARCHIVE_TYPE, form
        .getArchiveType());
    Logger.debug("ArchiveDispatchAction: Archive Type - "
                 + form.getArchiveType());
    archive.setFieldValue(IGTArchiveEntity.FROM_START_DATE, form
        .getFromStartDate());
    archive.setFieldValue(IGTArchiveEntity.FROM_START_TIME, form
        .getFromStartTime());
    archive.setFieldValue(IGTArchiveEntity.TO_START_DATE, form
        .getToStartDate());
    archive.setFieldValue(IGTArchiveEntity.TO_START_TIME, form
        .getToStartTime());
    if (IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE
        .equals(archiveType))
    {
      archive.setFieldValue(IGTArchiveEntity.PROCESS_DEF_NAME_LIST,
                                   StaticUtils.collectionValue(form
                                       .getProcessDefNameList()));
      archive.setFieldValue(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                       StaticUtils.booleanValue(form.getIsIncludeIncomplete()));
    }
    else if (IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      archive.setFieldValue(IGTArchiveEntity.FOLDER_LIST,
                                   StaticUtils.collectionValue(form
                                       .getFolderList()));
      archive.setFieldValue(IGTArchiveEntity.DOCUMENT_TYPE_LIST,
                                   StaticUtils.collectionValue(form
                                       .getDocumentTypeList()));
      archive.setFieldValue(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                   new Boolean(false));
    }
    archive.setFieldValue(IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
                       StaticUtils.booleanValue(form
                           .getIsEnableRestoreArchived()));
    archive.setFieldValue(IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,
                       StaticUtils.booleanValue(form
                           .getIsEnableSearchArchived()));
    archive.setFieldValue(IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE,
                                   StaticUtils.collectionValue(form
                                       .getPartnerIDForArchive()));
    
    //TWX: 30032009
    archive.setFieldValue(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE, form.getisArchiveFrequencyOnce());
    
    if(!StaticUtils.booleanValue(form.getisArchiveFrequencyOnce()))
    {
      archive.setFieldValue(IGTArchiveEntity.ARCHIVE_OLDER_THAN, form.getArchiveRecordOlderThan());
    }
    else
    {
      archive.setFieldValue(IGTArchiveEntity.ARCHIVE_OLDER_THAN, "");
    }
    
    //set the client login timezone
    HttpSession session = actionContext.getSession();
    
    TimeZone clientTZ = (TimeZone)session.getAttribute(GTActionBase.TIMEZONE_KEY);
    archive.setFieldValue(IGTArchiveEntity.CLIENT_TZ, clientTZ.getID());
  }

  protected void initialiseNewEntity(ActionContext actionContext,
                                     IGTEntity entity) throws GTClientException
  {
    entity.setFieldValue(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                         Boolean.FALSE);
  }

  /*
  protected void saveWithManager(ActionContext actionContext,
                                 IGTManager manager,
                                 IGTEntity entity) throws GTClientException
  {
    super.saveWithManager(actionContext, manager, entity);
    initialiseActionForm(actionContext, entity);
    setReturnToView(actionContext, true);
  }*/

  protected void validateDate(String entityType,
                              String field,
                              String value,
                              String format,
                              ActionErrors actionErrors)
  {
    Date date = null;
    if (!value.equals(""))
    {
      date = DateUtils.parseDate(value, null, null,
                                 new SimpleDateFormat(format));

      if (date == null)
        EntityFieldValidator.addFieldError(actionErrors, field, entityType,
                                           EntityFieldValidator.INVALID, null);
    } 
  }

  /*
  protected boolean doSave(ActionContext actionContext, ActionErrors errors) throws GTClientException
  {
    // ISimpleResourceLookup rLookup =
    createResourceLookup(actionContext);
    OperationContext opContext = OperationContext
        .getOperationContext(actionContext.getRequest());

    if (opContext == null)
      throw new java.lang.NullPointerException("No OperationContext found");

    IGTEntity entity = (IGTEntity) opContext
        .getAttribute(IOperationContextKeys.ENTITY);
    if (entity == null)
      throw new java.lang.NullPointerException(
                                               "No entity found in Operation Context");

    ArchiveAForm archiveAForm = (ArchiveAForm) actionContext
        .getActionForm();

    // Validate the request parameters specified by the user
    boolean failed = false;
    if (archiveAForm.getArchiveID().equals(""))
    {
      errors.add("archiveID", new ActionError("error.archiveID.required"));
      failed = true;
    }
    if (archiveAForm.getArchiveDescription().equals(""))
    {
      errors.add("archiveDescription",
                 new ActionError("error.description.required"));
      failed = true;
    }
    if (!failed)
    {
      entity.setFieldValue(IGTArchiveEntity.ARCHIVE_ID,
                           archiveAForm.getArchiveID());
      entity.setFieldValue(IGTArchiveEntity.ARCHIVE_NAME,
                           archiveAForm.getArchiveName());
      entity.setFieldValue(IGTArchiveEntity.ARCHIVE_DESCRIPTION,
                           archiveAForm.getArchiveDescription());
      entity.setFieldValue(IGTArchiveEntity.ARCHIVE_TYPE,
                           archiveAForm.getArchiveType());
      entity.setFieldValue(IGTArchiveEntity.FROM_START_DATE,
                           archiveAForm.getFromStartDate());
      entity.setFieldValue(IGTArchiveEntity.TO_START_DATE,
                           archiveAForm.getToStartDate());
      entity.setFieldValue(IGTArchiveEntity.FROM_START_TIME,
                           archiveAForm.getFromStartTime());
      entity.setFieldValue(IGTArchiveEntity.TO_START_TIME,
                           archiveAForm.getToStartTime());
      entity.setFieldValue(IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,
                           StaticUtils.booleanValue(archiveAForm
                               .getIsEnableSearchArchived()));
      entity.setFieldValue(IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
                           StaticUtils.booleanValue(archiveAForm
                               .getIsEnableRestoreArchived()));
      
      entity.setFieldValue(IGTArchiveEntity.ARCHIVE_OLDER_THAN, archiveAForm.getArchiveRecordOlderThan());
      entity.setFieldValue(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE, archiveAForm.getisArchiveFrequencyOnce());
      HttpSession session = actionContext.getSession();
      TimeZone clientTZ = (TimeZone)session.getAttribute(GTActionBase.TIMEZONE_KEY);
      
      entity.setFieldValue(IGTArchiveEntity.CLIENT_TZ, clientTZ.getID());
      
      System.out.println("doSave Usr entered archive older than: "+archiveAForm.getArchiveRecordOlderThan()+" isArchiveOnce: "+archiveAForm.getisArchiveFrequencyOnce()+ " timezone: "+session.getAttribute(GTActionBase.TIMEZONE_KEY));
      
      String archiveType = archiveAForm.getArchiveType();
      if (IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE
          .equals(archiveType))
      {
        entity.setFieldValue(IGTArchiveEntity.PROCESS_DEF_NAME_LIST, StaticUtils
            .collectionValue(archiveAForm.getProcessDefNameList()));
        entity.setFieldValue(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                             StaticUtils.booleanValue(archiveAForm
                                 .getIsIncludeIncomplete()));
      }
      else if (IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT
          .equals(archiveType))
      {
        entity.setFieldValue(IGTArchiveEntity.FOLDER_LIST, StaticUtils
            .collectionValue(archiveAForm.getFolderList()));
        entity.setFieldValue(IGTArchiveEntity.DOCUMENT_TYPE_LIST, StaticUtils
            .collectionValue(archiveAForm.getDocumentTypeList()));
      }
      entity.setFieldValue(IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE, StaticUtils
          .collectionValue(archiveAForm.getPartnerIDForArchive()));
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest()
          .getSession());
      IGTManager manager = gtasSession
          .getManager(IGTManager.MANAGER_ARCHIVE);
      try
      {
        if (entity.isNewEntity())
        {
          manager.create(entity);
        }
        else
        {
          manager.update(entity);
        }
      }
      catch (Exception e)
      {
        throw new GTClientException("Error saving Archive", e);
      }
    }
    return failed;
  } */
}