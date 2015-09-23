/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMappingFileRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 14, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTImportSchemaEntity;
import com.gridnode.gtas.client.ctrl.IGTSchemaMappingFileEntity;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.IOptionValueRetriever;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.utils.*;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class SchemaMappingFileRenderer extends AbstractRenderer
{
	private boolean _edit;
	
	protected static final Number[] _fields = {
		IGTSchemaMappingFileEntity.PATH,
		IGTSchemaMappingFileEntity.FILENAME,
		IGTSchemaMappingFileEntity.MAPPING_NAME,
		IGTSchemaMappingFileEntity.DESCRIPTION
	};
	
	public SchemaMappingFileRenderer(RenderingContext rContext, boolean edit)
	{
		super(rContext);
		_edit = edit;
	}
	
	@Override
	protected void render() throws RenderingException
	{
		try
		{
			RenderingContext rContext = getRenderingContext();
			IGTSchemaMappingFileEntity smf = (IGTSchemaMappingFileEntity)getEntity();
			SchemaMappingFileAForm form = (SchemaMappingFileAForm)getActionForm();
			
			renderCommonFormElements(smf.getType(), _edit);
			renderFields(null, smf, _fields);
			
			renderLabelCarefully("ok","schema.mapping.file.save", false);
			
			if(_edit)
			{
				includeJavaScript(IGlobals.JS_SUBSTITUTION_UTILS);
				
				//Retrieve the pre-process zip entries we store before
				HashMap zipEntries = getCacheZipEntries(rContext);
				Collection objectValues = new HashSet(zipEntries.keySet());
				
				String selectedPath = form.getPath();
        String selectedFilename = form.getFileName();
				
        Collection filenameList = (ArrayList)zipEntries.get(selectedPath); 
        
				//Object combo box
				IOptionValueRetriever objectRetriever = new IOptionValueRetriever()
				{
					public String getOptionText(Object choice) throws GTClientException
					{
						return (String)choice;
					}
					
					public String getOptionValue(Object choice) throws GTClientException
					{
						return (String)choice;
					}
				};
				renderSelectOptions("path_value", objectValues, objectRetriever, true, "");
				renderSelectedOptions("path_value", selectedPath, false);
				
				//field combo box
				IOptionValueRetriever fieldRetriever = new IOptionValueRetriever()
				{
					public String getOptionText(Object choice) throws GTClientException
					{
						return (String)choice;
					}
					public String getOptionValue(Object choice) throws GTClientException
					{
						return (String)choice;
					}
				};
				renderSelectOptions("fileName_value", filenameList, fieldRetriever, true, "");
				renderSelectedOptions("fileName_value", selectedFilename, false);
				
			}
		}
		catch(Throwable t)
		{
			throw new RenderingException("[SchemaMappingFileRenderer.renderer] Error rendering SchemaMappingFile screen", t);
		}
	}
	
	private HashMap getCacheZipEntries(RenderingContext rContext)
		throws GTClientException
	{
		try
		{
			OperationContext context = OperationContext.getOperationContext(rContext.getRequest());
			
			OperationContext parentOPContext = context.getPreviousContext();
			if(parentOPContext == null)
			{
				throw new NullPointerException("null parent OperationContext reference");
			}
			
			IGTImportSchemaEntity importSchema = getImportSchema(parentOPContext);
			String attributeName = getSchemaZipFilename(importSchema);
			
			HashMap zipEntries = (HashMap)parentOPContext.getAttribute(attributeName);
			if(zipEntries == null)
			{
				throw new NullPointerException("No correspond attribute value has been associated with key "+attributeName+". Can't get cache zip entries.");
			}
			return zipEntries;
		}
		catch(Exception ex)
		{
			throw new GTClientException("[SchemaMappingFileRenderer.getCacheZipEntries] Unable to retrieve cached zip entries.", ex);
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
	
	/**
	 * Get the entity  ImportSchemaEntity from the opContext.
	 * @param opContext
	 * @return
	 * @throws GTClientException
	 */
	private IGTImportSchemaEntity getImportSchema(OperationContext opContext)
		throws GTClientException
	{
		try
		{
			IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity) opContext.getAttribute(IOperationContextKeys.ENTITY);
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
			throw new GTClientException("[SchemaMappingFileRenderer.getImportSchema] Error retrieving ImportSchema entity from previous OperationContext", ex);
		}
	}
}
