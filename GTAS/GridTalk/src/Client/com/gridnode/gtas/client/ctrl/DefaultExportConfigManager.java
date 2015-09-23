/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultExportConfigManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-28     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events, remove mock code
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.exportconfig.ExportEntitiesEvent;
import com.gridnode.gtas.events.exportconfig.GetExportableEntitiesEvent;
import com.gridnode.gtas.events.exportconfig.ImportEntitiesEvent;
import com.gridnode.gtas.events.exportconfig.ResolveEntitiesEvent;
import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;
import com.gridnode.gtas.model.exportconfig.ConfigEntityDescriptor;
import com.gridnode.gtas.model.exportconfig.ConfigEntityList;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultExportConfigManager extends DefaultAbstractManager
  implements IGTExportConfigManager
{
  private static final String CONFIG_PATH_KEY = IPathConfig.PATH_TEMP;  

  DefaultExportConfigManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_EXPORT_CONFIG, session);
    
    //ImportConfig isnt the primary entity so we need to do this explicitly
    loadFmi(IGTEntity.ENTITY_IMPORT_CONFIG);
    //...
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("exportConfig may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    if(IGTEntity.ENTITY_EXPORT_CONFIG.equals( entity.getType() ))
    {
      exportConfig( (IGTExportConfig)entity);
    }
    else if(IGTEntity.ENTITY_IMPORT_CONFIG.equals( entity.getType() ))
    {
      importConfig( (IGTImportConfig)entity);
    }
    else
    {
      throw new UnsupportedOperationException("Bad entity:" + entity);
    }
  }
  
  public void exportConfig(IGTExportConfig exportConfig) throws GTClientException
  {
    try
    {
      IGTMheReference mheRef = (IGTMheReference)exportConfig.getFieldValue(IGTExportConfig.SELECTED_ENTITIES);
      ConfigEntitiesContainer cec = convertMheReference(mheRef);
      ExportEntitiesEvent event = new ExportEntitiesEvent(cec);
      String fileName = (String)handleEvent(event);
      //20030718AH - removed the mock data generation code
      if (fileName == null)
        throw new NullPointerException("returned fileName is null");
      ((AbstractGTEntity)exportConfig).setNewFieldValue(IGTExportConfig.EXPORT_FILE, fileName);
      ((AbstractGTEntity)exportConfig).setNewEntity(false);      
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting config",t);
    }
  }
  
  public void importConfig(IGTImportConfig importConfig) throws GTClientException
  {
    try
    {
      IGTMheReference conflictingEntities = (IGTMheReference)importConfig.getFieldValue(IGTImportConfig.CONFLICTING_ENTITIES);
      if(conflictingEntities == null)
      { //If we have no conflicts then its a new import (or repeat import?!)
        Boolean isOverwrite = (Boolean)importConfig.getFieldValue(IGTImportConfig.IS_OVERWRITE);
        String fileName = importConfig.getFieldString(IGTImportConfig.IMPORT_FILE);
        IEvent event = new ImportEntitiesEvent(fileName, isOverwrite);
        ConfigEntitiesContainer cec = (ConfigEntitiesContainer)handleEvent(event);
        //20030718AH - Removed the mock data generation code
        if( (cec == null) || ((cec != null) && cec.isEmpty()) )
        {
          ((AbstractGTEntity)importConfig).setNewFieldValue(IGTImportConfig.CONFLICTING_ENTITIES, null);
        }
        else
        { //There was an import conflict which must be resolved manually. We will set a list of all the
          //conflicting records in the importConfig vEntity and our caller can check this and
          //set in a list of which ones to overwrite, and they can then try to import again
          //and our code will branch accordingly next time
          conflictingEntities = convertConfigEntitiesContainer(cec);
          ((AbstractGTEntity)importConfig).setNewFieldValue(IGTImportConfig.CONFLICTING_ENTITIES, conflictingEntities);
        }
      }
      else
      { //We had conflicts before, ourt caller has set into out vEntity a list of records
        //to be overwritten now we shall try to resolve them with data provided by caller
        IGTMheReference overwriteEntities = (IGTMheReference)importConfig.getFieldValue(IGTImportConfig.OVERWRITE_ENTITIES);
        ConfigEntitiesContainer cec = null;
        cec = (overwriteEntities == null) ? new ConfigEntitiesContainer() : convertMheReference(overwriteEntities);      
        IEvent event = new ResolveEntitiesEvent(cec);
        handleEvent(event);
        //20030718ah - Removed the mock data generation code
        //Clear out the conflictingEntities field since if this event worked then thats out of date
        ((AbstractGTEntity)importConfig).setNewFieldValue(IGTImportConfig.CONFLICTING_ENTITIES, null);
      }
      ((AbstractGTEntity)importConfig).setNewEntity(true); //Peter Pan hack... ;-)
      
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error importing config",t);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_EXPORT_CONFIG;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_EXPORT_CONFIG;
  }

  protected IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get event for this type");
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get list event for this type");
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new UnsupportedOperationException("No delete event for this type");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_EXPORT_CONFIG.equals(entityType))
    {
      return new DefaultExportConfig();
    }
    else if(IGTEntity.ENTITY_IMPORT_CONFIG.equals(entityType))
    {
      return new DefaultImportConfig();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("Non persistant virtual entity");
  }
  
  public IGTEntity newEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_EXPORT_CONFIG);
    entity.setNewFieldValue(IGTExportConfig.EXPORTABLE_ENTITIES, UnloadedFieldToken.getInstance() );
    entity.setNewEntity(true);
    return entity;
  }
  
  public IGTImportConfig getImportConfig() throws GTClientException
  {
    try
    {
      AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_IMPORT_CONFIG);
      entity.setNewFieldValue(IGTImportConfig.IS_OVERWRITE, Boolean.FALSE);
      entity.setNewEntity(true);
      return (IGTImportConfig)entity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error creating IGTImportConfig instance",t);
    }
  }

  boolean isVirtual(String entityType)
  {
    if(     IGTEntity.ENTITY_EXPORT_CONFIG.equals(entityType)
        ||  IGTEntity.ENTITY_IMPORT_CONFIG.equals(entityType) )
    {
      return true;
    }
    else
    {
      throw new UnsupportedOperationException("Manager:"
                                                + this
                                                + " does not handle entities of type "
                                                + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_EXPORT_CONFIG.equals(entityType))
    {
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[3];
      
      int f=-1; //Talk about lazy, this is to facilitate cut & paste!
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("exportConfig.exportFile",
                                              IGTExportConfig.EXPORT_FILE);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(false);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("file.downloadable","true");
        detail.setProperty("file.fixedKey",CONFIG_PATH_KEY);
        constraint = new FileConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("exportConfig.exportableEntities",
                                              IGTExportConfig.EXPORTABLE_ENTITIES);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(false);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("com.gridnode.gtas.client.ctrl.IGTMheReference");
        detail = new Properties();
        sharedVfmi[f].setConstraint(null);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("exportConfig.selectedEntities",
                                              IGTExportConfig.SELECTED_ENTITIES);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("com.gridnode.gtas.client.ctrl.IGTMheReference");
        detail = new Properties();
        sharedVfmi[f].setConstraint(null);
      }

      return sharedVfmi;
    }
    else if(IGTEntity.ENTITY_IMPORT_CONFIG.equals(entityType))
    { 
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[4];
      
      int f=-1; //Talk about lazy, this is to facilitate cut & paste!
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("importConfig.importFile",
                                              IGTImportConfig.IMPORT_FILE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("file.downloadable","false");
        detail.setProperty("file.fixedKey",CONFIG_PATH_KEY);
        constraint = new FileConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("importConfig.conflictingEntities",
                                              IGTImportConfig.CONFLICTING_ENTITIES);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(false);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("com.gridnode.gtas.client.ctrl.IGTMheReference");
        detail = new Properties();
        sharedVfmi[f].setConstraint(null);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("importConfig.overwriteEntities",
                                              IGTImportConfig.OVERWRITE_ENTITIES);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("com.gridnode.gtas.client.ctrl.IGTMheReference");
        detail = new Properties();
        sharedVfmi[f].setConstraint(null);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("importConfig.isOverwrite",
                                              IGTImportConfig.IS_OVERWRITE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.Boolean");
        detail = new Properties();
        detail.setProperty("importConfig.isOverwrite.automatically","true");
        detail.setProperty("importConfig.isOverwrite.manually","false");
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      return sharedVfmi;
    }
    else
    {
      return new IGTFieldMetaInfo[0];
    }   
  }

  private IGTMheReference convertConfigEntitiesContainer(ConfigEntitiesContainer cec)
    throws GTClientException
  {
    try
    {
      if (cec == null)
        throw new NullPointerException("cec is null");
      SimpleMheReference mheRef = new SimpleMheReference();
      if(!cec.isEmpty())
      {
        Collection entityLists = cec.getConfigEntityLists();
        if (entityLists == null)
          throw new NullPointerException("entityLists is null");
        Iterator iterator = entityLists.iterator();
        while(iterator.hasNext())
        {
          ConfigEntityList cel = (ConfigEntityList)iterator.next();
          if (cel == null)
            throw new NullPointerException("cel is null");
          String entityName = cel.getEntityName();
          if (entityName == null)
            throw new NullPointerException("entityName is null");
          Collection descriptors = cel.getConfigEntityDescriptors();
          if (descriptors == null)
            throw new NullPointerException("descriptors is null"); 
          String entityType = DefaultGTEntities.getClientMappedName(entityName);
          IGTManager manager = _session.getManager(entityType);
          Number keyFieldId = manager.getUidFieldId(entityType);
          if (keyFieldId == null)
            throw new NullPointerException("Unable to determine uid field id for:" + entityType); 
          Iterator dIterator = descriptors.iterator();
          while(dIterator.hasNext())
          {         
            ConfigEntityDescriptor descriptor = (ConfigEntityDescriptor)dIterator.next();
            Long keyValue = descriptor.getUid();
            String display = descriptor.getDescription();
            SimpleEntityReference ref = new SimpleEntityReference(entityType, keyFieldId, keyValue, display);
            mheRef.addReference(ref);
          }
        }
      }
      return mheRef;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error converting ConfigEntitiesContainer to IGTMheReference",t);
    }
  }
  
  private ConfigEntitiesContainer convertMheReference(IGTMheReference mheRef)
    throws GTClientException
  {
    try
    {
      if (mheRef == null)
        throw new NullPointerException("mheRef is null");
      ConfigEntitiesContainer cec = new ConfigEntitiesContainer();   
      String[] entityTypes = mheRef.getReferencedTypes();
      for(int i=0; i < entityTypes.length; i++)
      {
        String entityType = entityTypes[i];
        String entityName = DefaultGTEntities.getServerMappedName(entityType);
        ConfigEntityList cel = new ConfigEntityList();
        cel.setEntityName(entityName);
        List refs = mheRef.get(entityType);
        IGTManager manager = _session.getManager(entityType);
        Iterator dIterator = refs.iterator();
        while(dIterator.hasNext())
        {
          IGTEntityReference ref = (IGTEntityReference)dIterator.next();
          Number uidFieldId = manager.getUidFieldId(entityType);
          if (uidFieldId == null)
            throw new NullPointerException("uidFieldId is null");
          if(uidFieldId.equals(ref.getKeyFieldId()))
          {
            ConfigEntityDescriptor ced = new ConfigEntityDescriptor();
            ced.setUid((Long)ref.getKeyValue());
            ced.setDescription(ref.toString());
            cel.addConfigEntityDescriptor(ced);
          }
          else
          {
            throw new IllegalArgumentException("Not keyed on the UID field in ref:" + ref);
          }
        }
        cec.addConfigEntityList(cel);        
      }
      return cec;  
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error converting IGTMheReference to ConfigEntitiesContainer",t);
    }
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      String entityType = entity.getType();
      if( IGTEntity.ENTITY_EXPORT_CONFIG.equals(entityType) )
      {
        DefaultExportConfig exportConfig = (DefaultExportConfig)entity;
        if(IGTExportConfig.EXPORTABLE_ENTITIES.equals(fieldId))
        {
          try
          {
            GetExportableEntitiesEvent event = new GetExportableEntitiesEvent();
            
            ConfigEntitiesContainer cec = (ConfigEntitiesContainer)handleEvent(event);
            //20030718AH - Removed the mock data generation code

            if (cec == null)
              throw new NullPointerException("cec is null");
            IGTMheReference mheRef = convertConfigEntitiesContainer(cec);
            exportConfig.setNewFieldValue(IGTExportConfig.EXPORTABLE_ENTITIES, mheRef);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error initialising exportableEntities field",t);
          } 
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading field " + fieldId + " for:" + entity,t);
    }
  }
}