/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveDocumentDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 * 2002-09-22     Daniel D'Cotta      Commented out GDOC_FILTER, added re-designed 
 *                                    ProcessInstance and GridDocument options
 * 2006-04-26     Tam Wei Xiang       Modified method updateEntityFields()
 *                                    The fromDate and toDate will not be parsed
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream.   
 * 2006-09-14     Tam Wei Xiang       Added field ENABLE_RESTORE_ARCHIVED, 
 *                                    ENABLE_SEARCH_ARCHIVED, PARTNER                                
 *                                                                       
 */

package com.gridnode.gtas.client.web.archive;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.DefaultArchiveDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTArchiveDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ArchiveDocumentDispatchAction extends EntityDispatchAction2
{
  private static final Number[] _fields = { 
                                        IGTArchiveDocumentEntity.NAME,
                                        IGTArchiveDocumentEntity.DESCRIPTION,
                                        IGTArchiveDocumentEntity.FROM_DATE,
                                        IGTArchiveDocumentEntity.FROM_TIME,
                                        IGTArchiveDocumentEntity.TO_DATE,
                                        IGTArchiveDocumentEntity.TO_TIME,
                                        IGTArchiveDocumentEntity.ARCHIVE_TYPE,
                                        IGTArchiveDocumentEntity.PROCESS_DEF,
                                        IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE,
                                        IGTArchiveDocumentEntity.FOLDER,
                                        IGTArchiveDocumentEntity.DOC_TYPE,
                                      };

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ARCHIVE_DOCUMENT;
  }
  
  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ARCHIVE_DOCUMENT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ArchiveDocumentRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(!edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
    else
    {
      return IDocumentKeys.ARCHIVE_DOCUMENT_UPDATE;
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ArchiveDocumentAForm form = (ArchiveDocumentAForm)actionContext.getActionForm();
    IGTArchiveDocumentEntity archiveDocument = (IGTArchiveDocumentEntity)entity;
    
    /*
    form.setArchiveFile(archiveDocument.getFieldString(archiveDocument.ARCHIVE_FILE));
    form.setArchiveFilePath(archiveDocument.getFieldString(archiveDocument.ARCHIVE_FILE_PATH));
    */

    form.setName              (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.NAME));
    form.setDescription       (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.DESCRIPTION));
    form.setFromDate          (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.FROM_DATE));
    form.setFromTime          (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.FROM_TIME));
    form.setToDate            (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.TO_DATE));
    form.setToTime            (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.TO_TIME));
    form.setArchiveType       (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.ARCHIVE_TYPE));
    form.setProcessDef        (archiveDocument.getFieldStringArray(IGTArchiveDocumentEntity.PROCESS_DEF));
    form.setIncludeIncomplete (archiveDocument.getFieldString     (IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE));
    form.setFolder            (archiveDocument.getFieldStringArray(IGTArchiveDocumentEntity.FOLDER));
    form.setDocType           (archiveDocument.getFieldStringArray(IGTArchiveDocumentEntity.DOC_TYPE));
    form.setEnableRestoreArchived (archiveDocument.getFieldString (IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED));
    form.setEnableSearchArchived (archiveDocument.getFieldString  (IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED));
    form.setPartner          (archiveDocument.getFieldStringArray (IGTArchiveDocumentEntity.PARTNER));
  }


  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    ArchiveDocumentAForm form = new ArchiveDocumentAForm();
    return form;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    try
    {
      //IGTSession gtasSession = getGridTalkSession(actionContext);
      ArchiveDocumentAForm form = (ArchiveDocumentAForm)actionForm;
      IGTArchiveDocumentEntity archiveDocument = (IGTArchiveDocumentEntity)entity;

      /*
      DataFilterAForm filterForm = form.getGdocFilter();

      boolean filterOk = DataFilterValidator.validateDataFilter( errors,
                                              filterForm,
                                              true,
                                              IGTEntity.ENTITY_GRID_DOCUMENT,
                                              "gdocFilter",
                                              "archiveDocument.error.gdocFilter",
                                              gtasSession);
      */
      
      basicValidateString       (errors, IGTArchiveDocumentEntity.NAME,                    form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.DESCRIPTION,             form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.FROM_DATE,               form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.FROM_TIME,               form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.TO_DATE,                 form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.TO_TIME,                 form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.ARCHIVE_TYPE,            form, archiveDocument);
      
      //TWx 14092006
      basicValidateString       (errors, IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED, form, archiveDocument);
      basicValidateString       (errors, IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED,  form, archiveDocument);
      basicValidateStringArray  (errors, IGTArchiveDocumentEntity.PARTNER,                 form, archiveDocument);
      
      String archiveType = form.getArchiveType();
      if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
      {
        basicValidateStringArray(errors, IGTArchiveDocumentEntity.PROCESS_DEF,         form, archiveDocument);
        basicValidateString     (errors, IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE,  form, archiveDocument);
      }
      else if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
      {
        basicValidateStringArray(errors, IGTArchiveDocumentEntity.FOLDER,              form, archiveDocument);
        basicValidateStringArray(errors, IGTArchiveDocumentEntity.DOC_TYPE,            form, archiveDocument);
      }
      
      validateDate(archiveDocument.getType(), "fromDate", form.getFromDate(), "yyyy-MM-dd", errors);
      
      
      validateDate(archiveDocument.getType(), "fromTime", form.getFromTime(), "HH:mm",      errors);
      
      validateDate(archiveDocument.getType(), "toDate",   form.getToDate(),   "yyyy-MM-dd", errors);
      
      validateDate(archiveDocument.getType(), "toTime",   form.getToTime(),   "HH:mm",      errors);
      
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating archiveDocument",t);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    //IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTArchiveDocumentEntity archiveDocument = (IGTArchiveDocumentEntity)entity;
    ArchiveDocumentAForm form = (ArchiveDocumentAForm)actionContext.getActionForm();

    /*
    DataFilterAForm filterForm = (DataFilterAForm)form.getGdocFilter();

    QueryUtils qu = new QueryUtils();
    qu.setTimeZone( StaticWebUtils.getTimeZone(actionContext.getRequest()) );
    qu.setLocale( StaticWebUtils.getLocale(actionContext.getRequest()) );
    DataFilterImpl gdocFilter = (DataFilterImpl)qu.constructFilter( filterForm,
                                                                    gtasSession,
                                                                    IGTEntity.ENTITY_GRID_DOCUMENT);

    archiveDocument.setFieldValue(archiveDocument.GDOC_FILTER, gdocFilter);
    */
    
    String fromDateStr = form.getFromDate();

    String toDateStr = form.getToDate();

    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.NAME,                    form.getName());
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.DESCRIPTION,             form.getDescription());
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.FROM_DATE,               fromDateStr);
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.FROM_TIME,               form.getFromTime());
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.TO_DATE,                 toDateStr);
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.TO_TIME,                 form.getToTime());
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.ARCHIVE_TYPE,            form.getArchiveType());
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.PROCESS_DEF,             StaticUtils.collectionValue (form.getProcessDef()));
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE,      StaticUtils.booleanValue    (form.getIncludeIncomplete()));
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.FOLDER,                  StaticUtils.collectionValue (form.getFolder()));
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.DOC_TYPE,                StaticUtils.collectionValue (form.getDocType()));
    
    //TWX 14092006
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED, StaticUtils.booleanValue    (form.getEnableRestoreArchived()));
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED,  StaticUtils.booleanValue    (form.getEnableSearchArchived()));
    archiveDocument.setFieldValue(IGTArchiveDocumentEntity.PARTNER,                 StaticUtils.collectionValue (form.getPartner()));
    /*
    DefaultArchiveDocumentEntity dade = (DefaultArchiveDocumentEntity) archiveDocument;
    String isEstore = actionContext.getRequest().getParameter("isEstore");
    dade.setIsEstore(isEstore); */
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    entity.setFieldValue(IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE, Boolean.FALSE);
  }
  
  /*  
  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    ArchiveDocumentAForm form = (ArchiveDocumentAForm)actionContext.getActionForm();
    DataFilterAForm gdocFilter = form.getGdocFilter();
    QueryUtils.processUpdateActions(gdocFilter);
  }
  */
  
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    super.saveWithManager(actionContext, manager, entity);
    initialiseActionForm(actionContext, entity);
    setReturnToView(actionContext, true);
  }

  protected void validateDate(
    String entityType,
    String field,
    String value,
    String format,
    ActionErrors actionErrors)
  {
    Date date = null;
    if (!value.equals(""))
    {         
      date = DateUtils.parseDate(value, null, null, new SimpleDateFormat(format));
    
      if (date == null)
        EntityFieldValidator.addFieldError(
          actionErrors,
          field,
          entityType,
          EntityFieldValidator.INVALID,
          null);
    }
  }
}