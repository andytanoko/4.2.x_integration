/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMappingFileDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 14, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;


import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTImportSchemaEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSchemaMappingFileEntity;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class SchemaMappingFileDispatchAction extends EntityDispatchAction2
{
	private static final String INDEX_KEY = "schemaMappingFileEntity";
	
	@Override
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_SCHEMA_MAPPING_FILE;
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2#getFormRenderer(com.gridnode.gtas.client.web.strutsbase.ActionContext, com.gridnode.gtas.client.web.renderers.RenderingContext, boolean)
	 */
	@Override
	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new SchemaMappingFileRenderer(rContext, edit);
	}

	@Override
	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		return edit ? IDocumentKeys.SCHEMA_MAPPING_FILE_UPDATE : IDocumentKeys.SCHEMA_MAPPING_FILE_VIEW;
	}

	@Override
	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new SchemaMappingFileAForm();
	}

	@Override
	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm form,
																		ActionErrors actionErrors) throws GTClientException
	{
		SchemaMappingFileAForm mappingForm = (SchemaMappingFileAForm)form;
		IGTSchemaMappingFileEntity schemaMappingEnt = (IGTSchemaMappingFileEntity)entity;
		
		//path is not mandatory
		basicValidateString(actionErrors, IGTSchemaMappingFileEntity.FILENAME, mappingForm, schemaMappingEnt);
		basicValidateString(actionErrors, IGTSchemaMappingFileEntity.MAPPING_NAME, mappingForm, schemaMappingEnt);
		basicValidateString(actionErrors, IGTSchemaMappingFileEntity.DESCRIPTION, mappingForm, schemaMappingEnt);
		
	}

	@Override
	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		IGTSchemaMappingFileEntity schemaMapping = (IGTSchemaMappingFileEntity)entity;
		SchemaMappingFileAForm form = (SchemaMappingFileAForm)actionContext.getActionForm();
		
		schemaMapping.setFieldValue(IGTSchemaMappingFileEntity.MAPPING_NAME, form.getMappingName());
		schemaMapping.setFieldValue(IGTSchemaMappingFileEntity.DESCRIPTION, form.getDescription());
		schemaMapping.setFieldValue(IGTSchemaMappingFileEntity.PATH, form.getPath());
		schemaMapping.setFieldValue(IGTSchemaMappingFileEntity.FILENAME, form.getFileName());
		
		//the zipEntryName info is required in the batch import schema mapping files screen
		schemaMapping.setFieldValue(IGTSchemaMappingFileEntity.ZIP_ENTRY_NAME, form.getPath()+form.getFileName());
	}

	@Override
	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		IGTSchemaMappingFileEntity schemaMapping = (IGTSchemaMappingFileEntity)entity;
		SchemaMappingFileAForm form = (SchemaMappingFileAForm)actionContext.getActionForm();
		
		form.setDescription((String)schemaMapping.getFieldValue(IGTSchemaMappingFileEntity.DESCRIPTION));
		form.setFileName((String)schemaMapping.getFieldValue(IGTSchemaMappingFileEntity.FILENAME));
		form.setMappingName((String)schemaMapping.getFieldValue(IGTSchemaMappingFileEntity.MAPPING_NAME));
		form.setPath((String)schemaMapping.getFieldValue(IGTSchemaMappingFileEntity.PATH));
	}

	@Override
	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		// TODO Auto-generated method stub
		return IGTManager.MANAGER_SCHEMA_MAPPING_FILE;
	}
	
	/**
	 * instead of save the entity into backend, we save it in to the importSchema entity
	 */
	@Override
	protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
		throws GTClientException
	{
		try
		{
			OperationContext op = OperationContext.getOperationContext(actionContext.getRequest());
			String index = (String)op.getAttribute(INDEX_KEY); //It can be used to differentiate whether we are saving a new entity or an existing one
			                                                   //I followed the existing implementation, so didn't use boolean.
			
			IGTImportSchemaEntity importSchema = getImportSchema(actionContext);
			ArrayList schemaMappingList = (ArrayList)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING); 
			
			if("new".equals(index))
			{
				schemaMappingList.add(entity);
			}
		}
		catch(Exception ex)
		{
			throw new GTClientException("[SchemaMappingFileDispatchAction.saveWithManager] Error perfroming saving in ImportSchema entity", ex);
		}
		
	}
	
	/**
	 * Obtain the ImportSchema entity from the previous entity.
	 * @param actionContext
	 * @return
	 * @throws GTClientException
	 */
	private IGTImportSchemaEntity getImportSchema(ActionContext actionContext)
		throws GTClientException
	{
		try
		{
			OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
			OperationContext parentOpContext = opContext.getPreviousContext();
			if(parentOpContext == null)
      {
        throw new java.lang.NullPointerException("null parent OperationContext reference");
      }
      IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity) parentOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(importSchema == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(importSchema instanceof IGTImportSchemaEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is"
                                              + " not a IGTImportSchemaEntity entity. Entity=" + importSchema);
      }
      
      return importSchema;
		}
		catch(Exception ex)
		{
			throw new GTClientException("[SchemaMappingFileDispatchAction.getImportSchema] Error retrieving ImportSchema entity from previous OperationContext", ex);
		}
	}
	
	/**
	 * Override super class method to perform additional population for 
	 * Operation Context
	 */
	@Override
	protected void processPreparedOperationContext (ActionContext actionContext,
	                                                OperationContext opContext)
		throws GTClientException
	{
		try
		{
			IGTSchemaMappingFileEntity entity = null;
			String indexString = actionContext.getRequest().getParameter("index");
			opContext.setAttribute(INDEX_KEY,indexString); //so that when we save the entity,
			                                               //we can differentiate whether we are saving
			                                               //a new entity or an existing entity. (see detail in saveWithManager)
			
			if(indexString != null)
			{
				if(!indexString.equals("new"))
				{
					try
					{
						int index = Integer.parseInt(indexString);
						IGTImportSchemaEntity importSchema = getImportSchema(actionContext);
						ArrayList schemaMappingList = (ArrayList)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
						if(schemaMappingList == null)
						{
							throw new NullPointerException("[SchemaMappingFileDispatchAction.processPreparedOperationContext] Null value for schemaMappingList.");
						}
						try
						{
							entity = (IGTSchemaMappingFileEntity)schemaMappingList.get(index);
						}
						catch(Throwable t)
						{
							throw new GTClientException("[SchemaMappingFileDispatchAction.processPreparedOperationContext] Error retrieving schemaMappingFile entity from list at index "
                                        + index,t);
						}
						if(entity == null)
						{
								throw new java.lang.NullPointerException("[SchemaMappingFileDispatchAction.processPreparedOperationContext] Null entity object at index "
                        + index + " of schemaMappingList list retrieved from ImportSchema entity "
                        + importSchema);
						}
					}
					catch(Throwable t)
          {
            throw new GTClientException("[SchemaMappingFileDispatchAction.processPreparedOperationContext] Error retrieving schemaMappingFile entity object from"
                                        + " importSchema entity in parent OperationContext",t);
          }
				}
				else
				{
					entity = (IGTSchemaMappingFileEntity)getRequestedEntity(actionContext);
				}
			}
			else
			{
				throw new GTClientException("[SchemaMappingFileDispatchAction.processPreparedOperationContext]No index specified");
			}
			opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
		}
		catch(Exception ex)
		{
			throw new GTClientException("[SchemaMappingFileDispatchAction.processPreparedOperationContext] Error obtaining SchemaMappingFile entity object",ex);
		}
	}
}
