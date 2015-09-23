/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMappingRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTImportSchemaEntity;
import com.gridnode.gtas.client.ctrl.IGTImportSchemaManager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSchemaMappingFileEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
import com.gridnode.gtas.client.web.renderers.ElvLightRenderer;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ImportSchemaRenderer extends AbstractRenderer
{
	private boolean _edit;
	
	private static final Number[] _zipFilenameField = {
		IGTImportSchemaEntity.ZIP_FILE
	};
	
	private static final Number[] _importSchemaField = {
		IGTImportSchemaEntity.BASE_DIR,
		IGTImportSchemaEntity.SCHEMA_MAPPING
	};
	
	public ImportSchemaRenderer(RenderingContext rContext, boolean edit)
	{
		super(rContext);
		_edit = edit;
		if(!_edit)
		{
			 throw new UnsupportedOperationException("[ImportSchemaRenderer] View mode not supported");
		}
		
	}
	
	@Override
	protected void render() throws RenderingException
	{
		try
		{
			String okBtnLabel = "";
			RenderingContext rContext = getRenderingContext();
			ImportSchemaAForm form = (ImportSchemaAForm)getActionForm();
			IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)getEntity();
			
			FormFileElement[] filesToBeUploaded = (FormFileElement[])importSchema.getFieldValue(IGTImportSchemaEntity.ZIP_FILE);
			
			renderCommonFormElements(IGTEntity.ENTITY_IMPORT_SCHEMA, _edit);
			
			if(filesToBeUploaded == null || filesToBeUploaded.length == 0)
			{
				Logger.log("ImportSchemaRenderer start render: rendering zipFile screen");
				
				//render the screen for uploading the schema
				renderFields( null,
				              importSchema,
				              _zipFilenameField,
				          		null,
				          		form,
				          		null);
				okBtnLabel = "importSchema.next";
				
				removeFields(importSchema, _importSchemaField, null);
				removeNode("tip_details", false);
				removeNode("ok_icon", false); //remove the image beside the ok_btn
				
				//TODO Help btn, Cancel btn
			}
			else
			{ 
				Logger.log("ImportSchemaRenderer start render: rendering schemaMapping");
				
				//render the screen for importing schema mapping
				renderFields(null, importSchema, _importSchemaField);
				renderLabelCarefully("tip1_label","importSchema.tip1",false);
				renderLabelCarefully("tip2_label","importSchema.tip2",false);
				renderLabelCarefully("tip3_label","importSchema.tip3",false);
				
				removeFields(importSchema, _zipFilenameField, null);
				
				addSchemaMappingFilesElv(rContext, form);
				
				okBtnLabel = "importSchema.importFiles";
//			TODO Help btn, Cancel btn
				
				//indicate we are importing schema mapping files
				importSchema.setFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING, true);
				
				//fireEvent to backend to retrieve the zip entry we just uploaded
				//It will also cache the returned zip entry into OP(we can't perform
				//cache in the DefaultImportSchemaManager since can't access OP).
				if(! isSchemaZipFileEntryInCache(importSchema))
				{
					cacheSchemaZipEntry(importSchema, getSchemaZipEntry(importSchema));
				}
			}
			
			renderLabelCarefully("ok",okBtnLabel, false);
		}
		catch(Throwable t)
		{
			throw new RenderingException("[ImportSchemaRenderer.render] Error rendering import schema screen",t);
		}
	}
	
	
	private void addSchemaMappingFilesElv(RenderingContext rContext, 
	                                      ImportSchemaAForm form)
		throws RenderingException
	{
		try
		{
			IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)getEntity();
			Number[] columns = {IGTSchemaMappingFileEntity.MAPPING_NAME,
					                IGTSchemaMappingFileEntity.DESCRIPTION,
					                IGTSchemaMappingFileEntity.ZIP_ENTRY_NAME
					                };
			
			IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
			
			IGTManager manager = gtasSession.getManager(IGTEntity.ENTITY_SCHEMA_MAPPING_FILE);
			ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, IGTEntity.ENTITY_SCHEMA_MAPPING_FILE);
			
			List schemaMappings = (List)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
			ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
			
			listOptions.setColumnAdapter(adapter);
			if(_edit)
      {
        listOptions.setCreateURL("divertUpdateSchemaMappingFile");
        listOptions.setDeleteURL("divertDeleteSchemaMappingFile");
        listOptions.setCreateLabelKey("schemaMapping.create");
        listOptions.setDeleteLabelKey("schemaMapping.delete");
        listOptions.setAllowsSelection(true);
        listOptions.setAllowsEdit(true);
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }
			
			listOptions.setHeadingLabelKey("importSchema.instruction");
      listOptions.setUpdateURL("divertUpdateSchemaMappingFile");
      listOptions.setViewURL("divertViewSchemaMappingFile");
      ElvLightRenderer elvRenderer = new ElvLightRenderer(rContext,
                                                "schemaMappings_details",
                                                listOptions,
                                                schemaMappings);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setTableName("schemaMappings");
      elvRenderer.setIndexModeDefault(true);
      elvRenderer.setIsRenderHeader(false); //not to render the header
      elvRenderer.render(_target);
		}
		catch(Exception ex)
		{
			throw new RenderingException("[SchemaMappingFileRenderer.addSchemaMappingFilesElv] Unable to render the schema mapping files table", ex);
		}
	} 
	
	/**
	 * Get the schema zip entry from backend
	 * @param importSchema
	 * @return
	 * @throws GTClientException
	 */
	private HashMap<String, ArrayList<String>> getSchemaZipEntry(IGTImportSchemaEntity importSchema)
		throws GTClientException
	{
		IGTSession gtasSession = importSchema.getSession();
		IGTImportSchemaManager importSchemaManager = (IGTImportSchemaManager)gtasSession.getManager(IGTManager.MANAGER_IMPORT_SCHEMA);
		return importSchemaManager.getSchemaZipEntry(importSchema);
	}
	
	/**
	 * Cache the schemaZip entry into operatin context
	 * @param importSchema
	 * @param zipEntryMap
	 * @throws GTClientException
	 */
	private void cacheSchemaZipEntry(IGTImportSchemaEntity importSchema, HashMap zipEntryMap)
		throws GTClientException
	{
		//cache into operation context
		String zipFilename = getSchemaZipFilename(importSchema);
		OperationContext opContext = this.getRenderingContext().getOperationContext();
		
		HashMap map = (HashMap)opContext.getAttribute(zipFilename);
		
		if( map == null)
		{
			Logger.log("[ImportSchemaRenderer.cacheSchemaZipEntry] caching zip entry map with key "+ zipFilename);
			opContext.setAttribute(zipFilename, zipEntryMap);
		}
	}
	
	private String getSchemaZipFilename(IGTImportSchemaEntity importSchema)
		throws GTClientException
	{
		String filename = "";
		FormFileElement[] fileElement = (FormFileElement[]) importSchema.getFieldValue(IGTImportSchemaEntity.ZIP_FILE);
		if(fileElement != null && fileElement.length > 0)
		{
			FormFileElement fileUploaded = fileElement[0];
			filename = fileUploaded.getFileName();
		}
		return filename;
	}
	
	private boolean isSchemaZipFileEntryInCache(IGTImportSchemaEntity importSchema)
		throws GTClientException
	{
		String zipFilename = getSchemaZipFilename(importSchema);
		OperationContext opContext = this.getRenderingContext().getOperationContext();
		
		return (HashMap)opContext.getAttribute(zipFilename) != null ;
	}
}
