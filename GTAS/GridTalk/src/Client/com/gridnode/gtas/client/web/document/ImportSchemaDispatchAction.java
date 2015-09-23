/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMappingDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 10, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTImportSchemaEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSchemaMappingFileEntity;
import com.gridnode.gtas.client.ctrl.ITextConstraint;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.utils.*;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ImportSchemaDispatchAction extends EntityDispatchAction2
{
	
	private static final String _fileExt[] = 
  { //Valid file extensions for importSchema
    "zip",
  };
	
	/* (non-Javadoc)
	 * @see com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2#getEntityName()
	 */
	@Override
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_IMPORT_SCHEMA;
	}

	@Override
	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new ImportSchemaRenderer(rContext, edit);
	}

	@Override
	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		if(!edit)
		{
			throw new UnsupportedOperationException("[SchemaMappingFileDispatchAction.getFormDocumentKey] View mode not supported.");
		}
		else
		{
			return IDocumentKeys.IMPORT_SCHEMA_MAPPING_UPDATE;
		}
	}

	@Override
	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new ImportSchemaAForm();
	}

	@Override
	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm actionForm,
																		ActionErrors actionErrors) throws GTClientException
	{
		try
		{
			Logger.log("[ImportSchemaDispatchAction] validateActionForm");
			getGridTalkSession(actionContext);
			IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
			ImportSchemaAForm form = (ImportSchemaAForm)actionForm;
		
		
		
			if(isFormFileElementEmpty(importSchema))
			{
				EntityFieldValidator.basicValidateFiles(actionErrors, IGTImportSchemaEntity.ZIP_FILE, form, 
				                                        importSchema, _fileExt);
			}
			else if((Boolean)importSchema.getFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING))
			{
				//validate the total length of subDir + path
				validatePathLength(actionErrors, actionForm, entity);
			}
		}
		catch(Exception ex)
		{
			throw new GTClientException("[ImportSchemaDispatchAction.validateActionForm] Error validating importSchema.", ex);
		}
	}

	@Override
	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		Logger.log("[ImportSchemaDispatchAction] updateEntityFields");
		//IGTImportSchema importSchema = (IGTImportSchema)entity;
		ImportSchemaAForm form = (ImportSchemaAForm)actionContext.getActionForm();
		IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
		
		if(isFormFileElementEmpty(importSchema))
		{
			
				transferFieldFiles(actionContext, importSchema, IGTImportSchemaEntity.ZIP_FILE, false);
		}
		else
		{
			importSchema.setFieldValue(IGTImportSchemaEntity.BASE_DIR, form.getBaseDir());
		}
	}

	@Override
	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		Logger.log("ImportSchemaDispatchAction.initialiseActionForm");
		
		IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
		ImportSchemaAForm form = (ImportSchemaAForm)actionContext.getActionForm();
	}

	@Override
	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_IMPORT_SCHEMA;
	}
	
	/**
	 * Determine the state of the screen based on whether user is uploading 
	 * the zip file or not.
	 * 
	 * For example the FormFileElement will not be empty(provided that user 
	 * has uploaded a file) after the "updateEntityFields" is executed.
	 * 
	 * @param importSchema
	 * @return
	 * @throws GTClientException
	 */
	private boolean isFormFileElementEmpty(IGTImportSchemaEntity importSchema)
		 throws GTClientException
	{
		FormFileElement[] fileElement = (FormFileElement[])importSchema.getFieldValue(IGTImportSchemaEntity.ZIP_FILE);
		
		if(fileElement == null)
		{
			Logger.log("[ImportSchemaDispathcAction.isFormFileElementEmpty] file element size is null ");
		}
		else if(fileElement.length >= 0)
		{
			Logger.log("[ImportSchemaDispathcAction.isFormFileElementEmpty] file element size is  "+fileElement.length);
		}
		
		//Prior we transfer the 
		return (fileElement == null || fileElement.length == 0) ;
	}
	

	protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
		IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
		super.saveWithManager(actionContext, manager, importSchema);
		
		boolean isImportSchemaMapping = (Boolean)importSchema.getFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING);
		
		if(!isFormFileElementEmpty(importSchema) && !isImportSchemaMapping)
		{
			//marked the operation context's status to not yet complete ??
			setReturnToView(actionContext, true);
		}
  }
	
	//MUST delegate back to their superclass's getDivertForward
  //for any mappingName they aren't planning to take specific action for.
	protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  {
    String index = actionContext.getRequest().getParameter("singleIndex");
    ActionForward divertForward = mapping.findForward(divertTo);
    if(divertForward == null)
    {
      throw new GTClientException("[ImportSchemaDispatchAction.getDivertForward] No forward mapping found for " + divertTo);
    }
    if(     ("divertUpdateSchemaMappingFile".equals(divertTo))
        ||  ("divertViewSchemaMappingFile".equals(divertTo)))
    {

      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",index),
                          divertForward.getRedirect());

    }
    return processSOCForward( divertForward, opCon );
  }
	
	/*
	@Override
	protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
		//we try to get the schema mapping that marked as delete
		String indexString = actionContext.getRequest().getParameter("markAsDelete");
		IGTImportSchemaEntity importSchema = null;
		
		if("".equals(indexString))
		{
			Logger.log("[ImportSchemaDispatchAction.processPreparedOperationContext] marked as delete is null or empty");
		}
		
		importSchema = (IGTImportSchemaEntity)getRequestedEntity(actionContext);
		importSchema.setFieldValue(IGTImportSchemaEntity.MARK_AS_DELETE,indexString);
		Logger.log("[ImportSchemaDispatchAction.processPreparedOperationContext] mark as delete is "+indexString);
			
		opContext.setAttribute(IOperationContextKeys.ENTITY, importSchema);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }  */
	
	/**
   * Override to provide custom processing for an update operation before
   * preparing the view renderers occurs.
   */
	@Override
  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)getEntity(actionContext);
    ImportSchemaAForm form = (ImportSchemaAForm)actionContext.getActionForm();
    
    if(form == null)
    {
    	return;
    }
    
    String markAsDelete = form.getMarkAsDelete();
    Logger.log("[ImportSchemaDispatchAction.performUpdateProcessing] mark as delete is "+markAsDelete);
    
    if(markAsDelete != null && ! "".equals(markAsDelete))
    {
    	ArrayList schemaMappingList = (ArrayList)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
    	
    	StringTokenizer st = new StringTokenizer(markAsDelete,";");
    	while(st.hasMoreTokens())
    	{
    		int indexToDelete = Integer.parseInt(st.nextToken());
    		schemaMappingList.set(indexToDelete, null);
    	}
    	
    	ArrayList schemaMappingAfterDelete = removeNullFromList(schemaMappingList);
    	importSchema.setFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING, schemaMappingAfterDelete);
    }
    
  }
	
	/**
	 * It copy the non-null value from the list we passed in.
	 * @param list
	 * @return 
	 */
	private ArrayList removeNullFromList(ArrayList list)
	{
		ArrayList newList = new ArrayList();
		for(int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			if(obj != null)
			{
				newList.add(obj);
			}
		}
		return newList;
	}
	
	private boolean validatePathLength(ActionErrors actionErrors,
                                     ActionForm form,
                                     IGTEntity entity)
		throws GTClientException
	{
		try
		{
			IGTSchemaMappingFileEntity schemaMappingEntity = getSchemaMappingFileEntity(
			                                                                   (IGTImportSchemaEntity)entity);
			
			if(schemaMappingEntity == null) //user are allowed not to create schema mapping.
			{
				return true;
			}
			
			String fieldName = schemaMappingEntity.getFieldName(IGTSchemaMappingFileEntity.PATH);
			
			IGTFieldMetaInfo fmi = schemaMappingEntity.getFieldMetaInfo(IGTSchemaMappingFileEntity.PATH);
			
      if(fmi == null)
      {
        throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                  + IGTSchemaMappingFileEntity.PATH + "("
                                                  + fieldName +") of entity " + schemaMappingEntity);
      }
      
      ITextConstraint textConstraint = (ITextConstraint)fmi.getConstraint();
      int maxLength = textConstraint.getMaxLength();
      
      int index = validatePathBaseDirTotalLength((IGTImportSchemaEntity)entity, maxLength);
      
      Logger.log("invalid index record is "+ index);
      
      if(index >= 0)
      {
      	String baseFieldName = entity.getFieldName(IGTImportSchemaEntity.BASE_DIR);
      	
      	//we want to display the error msg just beside the baseDir
      	EntityFieldValidator.addFieldError(actionErrors, baseFieldName, entity.getType(),FieldValidator.INVALID,
                                         new Object[]{String.valueOf(index), String.valueOf(maxLength)});
      	
      	printActionErrors(actionErrors);
      	
      	return false;
      }
      else
      {
      	return true;
      }
		}
		catch(Exception ex)
		{
			throw new GTClientException("[ImportSchemaDispatchAction: validatePathLength]  Error validating fields", ex);
		}
	}
	
	/**
	 * Validate the sum length for base didr and path. The maximum length constraint
	 * is defined in the fieldmetainfo.
	 * @param importSchema
	 * @param maxLength
	 * @return Return the index of the record if such a violation has been detected. -1 otherwise.
	 * @throws GTClientException
	 */
	private int validatePathBaseDirTotalLength(IGTImportSchemaEntity importSchema, int maxLength)
		throws GTClientException
	{
		List schemaMappingList = (List)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
		String baseDir = (String)importSchema.getFieldValue(IGTImportSchemaEntity.BASE_DIR);
		
		int baseDirLength = baseDir == null ? 0 : baseDir.length();
		
		for(int i = 0; schemaMappingList != null && i < schemaMappingList.size(); i++)
		{
			IGTSchemaMappingFileEntity schemaMapping = (IGTSchemaMappingFileEntity)
			                                               schemaMappingList.get(i);
			String path = (String)schemaMapping.getFieldValue(IGTSchemaMappingFileEntity.PATH);
			if(path != null && (path.length() + baseDirLength) > maxLength)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Get the 1st SchemaMapping entity if any.
	 * @param importSchema
	 * @return
	 * @throws GTClientException
	 */
	private IGTSchemaMappingFileEntity getSchemaMappingFileEntity(IGTImportSchemaEntity importSchema)
		throws GTClientException
	{
		List schemaMappingList = (List)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
		if(schemaMappingList != null && schemaMappingList.size() > 0)
		{
			return (IGTSchemaMappingFileEntity)schemaMappingList.get(0);
		}
		else
		{
			return null;
		}
	}
	
	private void printActionErrors(ActionErrors errors)
	{
		Iterator msg = errors.get();
		while(msg.hasNext())
		{
			ActionMessage actMsg = (ActionMessage)msg.next();
			Logger.log("key is "+actMsg.getKey()+" value is "+actMsg.getValues());
		}
	}
}
