/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultImportSchemaManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.events.mapper.BatchImportSchemasEvent;
import com.gridnode.gtas.events.mapper.GetSchemaZipFileEntryEvent;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DefaultImportSchemaManager extends DefaultAbstractManager
	implements IGTImportSchemaManager
{
	
	
	DefaultImportSchemaManager(DefaultGTSession session)
  	throws GTClientException
  {
		super(IGTManager.MANAGER_IMPORT_SCHEMA, session);
  }

	@Override
	protected IEvent getGetEvent(Long uid) throws EventException
	{
		throw new UnsupportedOperationException("getGetEvent is not supported.");
	}

	@Override
	protected IEvent getGetListEvent(IDataFilter filter) throws EventException
	{
		throw new UnsupportedOperationException("getGetListEvent is not supported.");
	}

	@Override
	protected IEvent getDeleteEvent(Collection uids) throws EventException
	{
		throw new UnsupportedOperationException("getDeleteEvent is not supported.");
	}

	@Override
	protected AbstractGTEntity createEntityObject(String entityType) throws GTClientException
	{
		if(IGTEntity.ENTITY_IMPORT_SCHEMA.equals(entityType))
		{
			return new DefaultImportSchemaEntity();
		}
		else
    {
      throw new java.lang.UnsupportedOperationException("[DefaultImportSchemaManager.createEntityObject] Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
	}

	@Override
	protected void doUpdate(IGTEntity entity) throws GTClientException
	{

	}

	@Override
	//TODO
	protected void doCreate(IGTEntity entity) throws GTClientException
	{
		IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
		
		//Can use to determine whether we are in upload zip file screen or importSchema mapping screen
		boolean isImportingSchemaMapping = (Boolean)importSchema.getFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING);
		
		Collection schemaMapping = (ArrayList)importSchema.getFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING);
		if(isImportingSchemaMapping)
		{
			String zipFilename = getSchemaZipFilename(importSchema);
			try
			{
				Collection entitiesHashMapList = convertEntitiesToHashMapCollection(schemaMapping);
				String baseDir = (String)importSchema.getFieldValue(IGTImportSchemaEntity.BASE_DIR);
				
				IEvent batchImportSchemasEvent = new BatchImportSchemasEvent(zipFilename,baseDir,entitiesHashMapList);
				Logger.log("[DefaultImportSchemaManager.doCreate] baseDir is "+ baseDir);
				handleEvent(batchImportSchemasEvent);
				
				//importSchema.setFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING, false);
			}
			catch(Exception ex)
			{
				throw new GTClientException("[DefaultImportSchemaManager.doCreate]Error importing schema: "+ex.getMessage(),ex);
			}
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
	
	@Override
	protected int getManagerType()
	{
		return IGTManager.MANAGER_IMPORT_SCHEMA;
	}

	@Override
	protected String getEntityType()
	{
		return IGTEntity.ENTITY_IMPORT_SCHEMA;
	}
	
	/**
   * If the manager is responsible for any virtual entities, must override this and return
   * true for virtual entity types.
   * @param entityType
   * @return isVirtual
   */
  boolean isVirtual(String entityType)
  {
    return true;
  }
  
  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
		throws GTClientException
	{
  	if(IGTEntity.ENTITY_IMPORT_SCHEMA.equals(entityType))
  	{
  		VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[4];
  		int i = 0;
  		
  		//vField for schemaMapping
  		sharedVfmi[i] = new VirtualSharedFMI("importSchema.schemaMapping", IGTImportSchemaEntity.SCHEMA_MAPPING);
      sharedVfmi[i].setMandatoryCreate(false);
      sharedVfmi[i].setMandatoryUpdate(false);
      sharedVfmi[i].setEditableCreate(true);
      sharedVfmi[i].setEditableUpdate(true);
      sharedVfmi[i].setDisplayableCreate(true);
      sharedVfmi[i].setDisplayableUpdate(true);
      sharedVfmi[i].setCollection(true);
      sharedVfmi[i].setValueClass("java.lang.ArrayList");
  		Properties detail = new Properties();
  		detail.setProperty("type", "embedded");
  		detail.setProperty("embedded.type", "schemaMapping"); //see defaultGTEntities ... i guess this is how the framework locate the embedded entity
  		detail.setProperty("collection", "true");
  		LocalEntityConstraint localEntityCon = new LocalEntityConstraint(detail);
  		sharedVfmi[i].setConstraint(localEntityCon);
  		
  		++i;
  		
  		//vField for baseDir
  		sharedVfmi[i] = new VirtualSharedFMI("importSchema.baseDir", IGTImportSchemaEntity.BASE_DIR);
      sharedVfmi[i].setMandatoryCreate(false);
      sharedVfmi[i].setMandatoryUpdate(false);
      sharedVfmi[i].setEditableCreate(true);
      sharedVfmi[i].setEditableUpdate(true);
      sharedVfmi[i].setDisplayableCreate(true);
      sharedVfmi[i].setDisplayableUpdate(true);
      sharedVfmi[i].setCollection(false);
      sharedVfmi[i].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      detail.setProperty("text.length.max","30"); //put the zip file under gtas def temp folder
      TextConstraint textConstraint = new TextConstraint(detail);
      sharedVfmi[i].setConstraint(textConstraint); 
      
      ++i;
      
      //vField for zipFilename
      sharedVfmi[i] = new VirtualSharedFMI("importSchema.zipFile", IGTImportSchemaEntity.ZIP_FILE);
      sharedVfmi[i].setMandatoryCreate(true);
      sharedVfmi[i].setMandatoryUpdate(false);
      sharedVfmi[i].setEditableCreate(true);
      sharedVfmi[i].setEditableUpdate(false);
      sharedVfmi[i].setDisplayableCreate(true);
      sharedVfmi[i].setDisplayableUpdate(true);
      sharedVfmi[i].setCollection(false);
      sharedVfmi[i].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("file.downloadable","true");
      detail.setProperty("file.fixedKey",IPathConfig.PATH_TEMP); //put the zip file under gtas def temp folder
      FileConstraint fileConstraint = new FileConstraint(detail);
      sharedVfmi[i].setConstraint(fileConstraint);
      
      ++i;
      
      //vField for isImportSchemaMapping
      sharedVfmi[i] = new VirtualSharedFMI("importSchema.isImportSchemaMapping", IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING);
      sharedVfmi[i].setMandatoryCreate(false);
      sharedVfmi[i].setMandatoryUpdate(false);
      sharedVfmi[i].setEditableCreate(true);
      sharedVfmi[i].setEditableUpdate(true);
      sharedVfmi[i].setDisplayableCreate(true);
      sharedVfmi[i].setDisplayableUpdate(true);
      sharedVfmi[i].setCollection(false);
      sharedVfmi[i].setValueClass("java.lang.Boolean");
      detail = new Properties();
      detail.setProperty("importSchema.true","true");
      detail.setProperty("importSchema.false","false"); 
      EnumeratedConstraint enumConstraint = new EnumeratedConstraint(detail);
      sharedVfmi[i].setConstraint(enumConstraint);
      
      /*
      ++i;
      sharedVfmi[i] = new VirtualSharedFMI("importSchema.markAsDelete", IGTImportSchemaEntity.MARK_AS_DELETE);
      sharedVfmi[i].setMandatoryCreate(false);
      sharedVfmi[i].setMandatoryUpdate(false);
      sharedVfmi[i].setEditableCreate(true);
      sharedVfmi[i].setEditableUpdate(true);
      sharedVfmi[i].setDisplayableCreate(true);
      sharedVfmi[i].setDisplayableUpdate(true);
      sharedVfmi[i].setCollection(false);
      sharedVfmi[i].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      detail.setProperty("text.length.max","100"); 
      textConstraint = new TextConstraint(detail);
      sharedVfmi[i].setConstraint(textConstraint);  */
      
      return sharedVfmi;
  	}
  	return new IGTFieldMetaInfo[0];
	}
  
  /*
   * Subclass should override to set the initial field values for a newly created AbstractGTEntity object.
   * @param entity the entity just created
   * @throws GTClientException
   */
  protected void setDefaultFieldValues(AbstractGTEntity entity)
  	throws GTClientException
  {
  	//initialize the embedded schema mapping list(since it is a VField. The DefaultAbstractManager will not
  	//initialize the default value to it)
  	IGTImportSchemaEntity importSchema = (IGTImportSchemaEntity)entity;
  	importSchema.setFieldValue(IGTImportSchemaEntity.SCHEMA_MAPPING, new ArrayList());
  	importSchema.setFieldValue(IGTImportSchemaEntity.IS_IMPORT_SCHEMA_MAPPING, false);
  }
  
  public HashMap<String, ArrayList<String> > getSchemaZipEntry(IGTImportSchemaEntity importSchema)
  	throws GTClientException
  {
  	try
  	{
  		String schemaZipFilename = getSchemaZipFilename(importSchema);
  		IEvent getZipEntryEvent = new GetSchemaZipFileEntryEvent(schemaZipFilename);
  		return (HashMap)handleEvent(getZipEntryEvent);
  	}
  	catch(Exception ex)
  	{
  		throw new GTClientException("[DefaultImportSchemaManager.getSchemaZipEntry] Error getting schema zip entry from backend.", ex);
  	}
  }
  
  /**
   * Convert a collection of entities into a collection of HashMap. 
   * @param entities
   * @return
   */
  private Collection convertEntitiesToHashMapCollection(Collection<IGTEntity> entities)
  	throws GTClientException
  {
  	Vector<HashMap> mapList = null;
  	if(entities != null)
  	{
  		mapList = new Vector<HashMap>(entities.size());
  	}
  	else
  	{
  		mapList = new Vector<HashMap>();
  	}
  
  	for(IGTEntity entity : entities)
  	{
  		mapList.add(convertEntityToHashMap(entity));
  	}
  	return mapList;
  }
  
  private HashMap<Number, Object> convertEntityToHashMap(IGTEntity entity)
  	throws GTClientException
  {
  	try
  	{
  		Number[] fieldID = {IGTSchemaMappingFileEntity.PATH, IGTSchemaMappingFileEntity.FILENAME,
        								IGTSchemaMappingFileEntity.MAPPING_NAME, IGTSchemaMappingFileEntity.DESCRIPTION,
        								IGTSchemaMappingFileEntity.ZIP_ENTRY_NAME};
  		HashMap<Number, Object> entityMap = new HashMap<Number, Object>();
  		for(Number id : fieldID)
  		{
  			entityMap.put(id, entity.getFieldValue(id));
  		}
  		return entityMap;
  	}
  	catch(Exception ex)
  	{
  		throw new GTClientException("[DefaultImportSchemaManager.convertEntityToHashMap] Error in converting the entity to map.", ex);
  	}
  }
}
