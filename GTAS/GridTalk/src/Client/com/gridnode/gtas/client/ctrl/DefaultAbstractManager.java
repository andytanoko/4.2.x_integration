/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAbstractManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-27     Andrew Hill         Created
 * 2002-09-26     Andrew Hill         Finished support for multiple embedded entities
 * 2002-09-27     Andrew Hill         Relax same manager assumption for embedded entities
 * 2002-10-24     Andrew Hill         Share metainfo using global context
 * 2002-10-25     Andrew Hill         Eliminate support for unshared metainfo
 * 2002-10-31     Andrew Hill         Add (untested) support for new DynamicEntityConstraint
 * 2002-11-19     Andrew Hill         getByKey() now supports vFields (albeit inefficiently!)
 * 2002-12-11     Neo Sok Lay         Use MetaInfoFactory instead of EntityMetaInfoLoader
 *                                    - for lazy loading of metainfo.
 * 2003-01-17     Andrew Hill         getByKey() now reports more info in exception
 * 2003-01-22     Andrew Hill         Fixed bug where vFmi not returned by getSharedFieldMetaInfo()
 * 2003-01-29     Andrew Hill         Added preProcessEntityFieldMap() hook
 * 2003-01-30     Andrew Hill         Beef up exceptions for entity processing errors
 *                                    getByKey() now falls back to heavy filtering if necessary
 * 2003-03-20     Andrew Hill         ListPager support
 * 2003-05-29     Andrew Hill         getByKey() thats takes an IGTEntityReference
 * 2003-06-02     Andrew Hill         Prevent redundant FMI construction by post-first session manager instances
 * 2003-07-18     Andrew Hill         New handling for multiple deletion
 * 2003-10-19     Daniel D'Cotta      Added sorting
 * 2004-01-08     Neo Sok Lay         Change initEntityFields() to setNewEntity(false) at the end.
 */
package com.gridnode.gtas.client.ctrl;
import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.rpf.event.*;

abstract class DefaultAbstractManager extends AbstractGTManager
{
  protected Boolean _supportsGtasListPaging = null; //20030320AH

  /**
   * Hashtable to store the DefaultSharedFMI arrays for all the entity types for which this manager
   * is responsible. - that is, the primary entity type, and its embedded entities.
   * (20030529 - nb: some managers also now manage a couple of virtual entity types that are related
   * only by problem domain (for example the archiveDocument manager).)
   */
  protected Hashtable _fieldMetaInfo;
  protected Hashtable _virtualFieldMetaInfo;

  /**
   * Hashtable that stores hashtable of IGTSharedMetaInfo[] for lookup purposes.
   * Outer table keyed by entityType, inner by fieldId
   */
  protected Hashtable _fieldMetaInfoLookup;

  /**
   * Hashtable that stores hashtables of fieldNames for lookup purposes.
   * Outer table keyed by entityType, inner by fieldName
   */
  protected Hashtable _fieldIdLookup;

    /**
     * Constructor.
     */
  DefaultAbstractManager(int managerType, DefaultGTSession session)
    throws GTClientException
  {
    super(managerType, (IGTSession)session);
    String sharedDataKey = this.getClass().getName();
    GlobalContext context = session.getContext();
    synchronized(context)
    { //20021024AH - Use global context to shared processed metainfo between managers
      //in different sessions.
      DefaultSharedManagerData sharedData = (DefaultSharedManagerData)
                                            context.getAttribute(sharedDataKey);
      if(sharedData == null)
      {
        _fieldMetaInfo = new Hashtable();
        _virtualFieldMetaInfo = new Hashtable();
        _fieldMetaInfoLookup = new Hashtable();
        _fieldIdLookup = new Hashtable();
        loadFmi(getEntityType());
        sharedData = new DefaultSharedManagerData(_fieldMetaInfo,
                                                  _virtualFieldMetaInfo,
                                                  _fieldMetaInfoLookup,
                                                  _fieldIdLookup);
        context.setAttribute(sharedDataKey,sharedData);
      }
      else
      {
        _fieldMetaInfo = sharedData.getFieldMetaInfo();
        _virtualFieldMetaInfo = sharedData.getVirtualFieldMetaInfo();
        _fieldMetaInfoLookup = sharedData.getFieldMetaInfoLookup();
        _fieldIdLookup = sharedData.getFieldIdLookup();
      }
      //System.out.println("DefaultAbstractManager(): fieldIdLookup="+_fieldIdLookup);
      //System.out.println("DefaultAbstractManager(): fieldMetaInfo="+_fieldMetaInfo);
      //System.out.println("DefaultAbstractManager(): fieldMetaInfoLookup="+_fieldMetaInfoLookup);
    }
  }


  /**
   * Subclass must implement this method to instantiate a get event of the appropriate type
   * for the entity that the manager manages.
   * @param uid
   * @return IEvent
   */
  protected abstract IEvent getGetEvent(Long uid)
    throws EventException;

  protected IEvent getGetListEvent(IDataFilter filter, String listId, int maxRows, int startRow)
    throws EventException
  { //20030320AH
    IEvent event = getGetListEvent( (listId == null) ? filter : null);
    if(event == null) throw new NullPointerException("Call to getGetListEvent(IDataFilter) returned null");
    if( (listId != null) || (maxRows > 0) )
    {
      if( supportsGtasListPaging() )
      {
        GetEntityListEvent gela = (GetEntityListEvent)event;
        if(listId != null)
        {
          gela.setEventData(GetEntityListEvent.LIST_ID, listId);
          gela.setEventData(GetEntityListEvent.MAX_ROWS, new Integer(maxRows) );
          gela.setEventData(GetEntityListEvent.START_ROW, new Integer(startRow) );
        }
        else if(maxRows > 0)
        {
          gela.setEventData(GetEntityListEvent.MAX_ROWS, new Integer(maxRows) );
          gela.setEventData(GetEntityListEvent.START_ROW, new Integer(startRow) );
        }
      }
      else
      {
        String klass = event.getClass().getName();
        throw new UnsupportedOperationException("Event class " + klass
                  + " does not support necessary attributes and methods to set listId and row parameters");
      }
    }
    return event;
  }

  protected abstract IEvent getGetListEvent(IDataFilter filter)
    throws EventException;


  public boolean supportsGtasListPaging() throws EventException
  { //20030320AH
    try
    {
      if(_supportsGtasListPaging == null)
      {
        synchronized(this)
        {
          if(_supportsGtasListPaging == null)
          {
            IEvent event = getGetListEvent(null);
            if( (event instanceof EventSupport) && (event instanceof GetEntityListEvent) )
            {
              _supportsGtasListPaging = Boolean.TRUE;
            }
            else
            {
              _supportsGtasListPaging = Boolean.FALSE;
            }
            event = null;
          }
        }
      }
      return _supportsGtasListPaging.booleanValue();
    }
    catch(UnsupportedOperationException t)
    {
      return false; //hmmmm
    }
  }

  /**
   * Subclass must implement this method to instantiate a delete event of the appropriate type
   * for the entity that the manager manages.
   * 20030718AH - Modified signature to support new multiple delete events
   * @param uid
   * @return IEvent
   */
  protected abstract IEvent getDeleteEvent(Collection uids)
    throws EventException;

  protected final IEvent getDeleteEvent(Long uid)
    throws EventException { return null; }

  /**
   * Subclass must override this to create an object of the specified type, where the
   * type is either the type of object the manager manages , or an embedded object that may occur.
   * Subclass should not initialise anything here, just instantiate the object.
   * @param entityType the (ctrl) entity type name
   * @return AbstracGTEntity subclass instance
   * @throws GTClientException
   */
  protected abstract AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException;

  protected abstract void doUpdate(IGTEntity entity)
    throws GTClientException;

  protected abstract void doCreate(IGTEntity entity)
    throws GTClientException;

  /**
   * Subclass may override to initialise virtual fields when entity is populated.
   * @param entityType
   * @param entity
   * @param fieldMap
   * @throws GTClientException
   */
  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
  }

  /**
   * If the manager is responsible for any virtual entities, must override this and return
   * true for virtual entity types.
   * @param entityType
   * @return isVirtual
   */
  boolean isVirtual(String entityType)
  {
    return false;
  }

  /**
   * Subclass must override this method to return the manager type as defined in IGTManager.
   * @return managerType
   */
  protected abstract int getManagerType();

  /**
   * Subclass must override this method to return the entity type as defined in IGTEntity for which
   * this manager is primarily responsible.
   * @return entityType
   */
  protected abstract String getEntityType();

  /**
   * Subclass may override to define virtual fields.
   */
  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    return new IGTFieldMetaInfo[0];
  }

  /**
   * Subclass to override to determine type of a dynamic embedded entity in a field of the map
   * returned by the server. This method will only be called when processing a fieldmap returned
   * from gtas. It wont be called for an existing entity. To get dynamic types in that case just
   * get the entity in question and check its type...
   * 20021031AH
   */
  protected String getDynamicType(Number fieldId,
                                  IDynamicEntityConstraint constraint,
                                  Map serverMap,
                                  IGTEntity entity,
                                  int index)
    throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Subclass has not implemented getDynamicType()."
                                                      + " Cannot get type for field id=" + fieldId
                                                      + " index=" + index
                                                      + " for entity:" + entity);
  }

  /**
   * Subclass may implement to load value(s) for load on demand fields.
   */
  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Subclass has not implemented code to"
                                                      + " load field id=" + fieldId
                                                      + " for entity:" + entity);
  }

  /**
   * Handles an event for an entity by directing it to the Session to handle.
   *
   * @param event The event to handle.
   */
  protected final Object handleEvent(IEvent event)
    throws GTClientException
  {
    assertLogin();
    return ((DefaultGTSession)_session).fireEvent(event);
  }

  /**
   * Return the DefaultSharedFMI array for the specified entityType.
   * (entityType must be the type handled by this manager, or the type of an embedded entity that
   * the entity handled by this manager handles).
   * @param entityType (ctrl) entity type name
   * @return DefaultSharedFMI[]
   */
  protected DefaultSharedFMI[] getSharedFmi(String entityType)
  {
    if (entityType == null)
      throw new NullPointerException("entityType is null");
    Object fmi = _fieldMetaInfo.get(entityType);
    if(fmi instanceof DefaultSharedFMI[] )
    { //20030523AH
      return (DefaultSharedFMI[])fmi;
    }
    else if(fmi == null)
    { //this isnt actually valid, but its checked elsewhere
      return null;
    }
    else
    {
      throw new IllegalStateException("Expecting DefaultSharedFMI[] but got "
                                      + fmi.getClass().getName()
                                      + " from _fieldMetaInfo.get(\""
                                      + entityType
                                      + "\")");
    }
  }

  protected IGTFieldMetaInfo[] getSharedVirtualFmi(String entityType)
    throws GTClientException
  {
    return (IGTFieldMetaInfo[])_virtualFieldMetaInfo.get(entityType);
  }

  /**
   * Will initialise the _fieldMetaInfo hashtable with arrays of DefaultSharedFMI[] objects.
   * When an embedded entity is encountered, the metaInfo for that entity type is also loaded
   * and has an entry placed in the hashtable. The entries are keyed by the (ctrl) entityType.
   * When the embedded entiy is encountered if we have already loaded metaInfo for this type then
   * we do not load it again.
   * Also invokes loading of vfmi?
   * @param entityType
   * @throws GTClientException
   */
  protected void loadFmi(String entityType)
    throws GTClientException
  { // Modified 2002-08-24 To provide better support for virtual fields and virtual entities
    if(entityType == null)
    { // Sanity check
      throw new java.lang.NullPointerException("Cannot load meta-info because specified entityType is null");
    }

    //20030602AH - I was encountering a problem in both my new importConfig and the earlier restoreDocuments
    //code where I could create the entity in the first session, but on logout and login again
    //attempts to create those entities would cause a class cast exception where virtual fmi was
    //being returned where it sgould have been DefaultSharedFMI! It seems that managers were recomstructing
    //their metainfo for new manager instances instead of recycling the global stuff that the first
    //instance had prepared! Im not quite sure how that resulted in vfmi being in the fmi lookup
    //since its just the same code being run (will need to trace the code) but the line below
    //fixes the problem by preventing redundant metainfo reconstruction.
    if(!( (_fieldMetaInfo.get(entityType) == null) &&
        (_virtualFieldMetaInfo.get(entityType) == null) ) )
    {
      return; //20030602AH - If the fmi is already loaded dont load it again!
    }

    try
    {
      //DefaultSharedFMI[] sharedFmi = new DefaultSharedFMI[]{};
      DefaultSharedFMI[] sharedFmi = new DefaultSharedFMI[0]; //20030522AH 20030623AH - todo: use shared empty array for efficiency
      boolean isReal = !isVirtual(entityType);
      if(isReal)
      {
        // Load the EntityMetaInfo from gtas
        EntityMetaInfo gtasMetaInfo = loadGtasMetaInfo(entityType);
        if(gtasMetaInfo == null)
        { // Sanity check
          throw new java.lang.NullPointerException("EntityMetaInfo returned by loadGtasMetaInfo() is null");
        }
        // Convert this into an array of DefaultSharedFMI objects
        sharedFmi = convertMetaInfo(entityType, gtasMetaInfo);
        if(sharedFmi == null)
        { // Sanity check
          throw new java.lang.NullPointerException("sharedFmi array returned by convertmetaInfo() is null");
        }
      }
      // Store this in the _fieldMetaInfo hashtable keyed by entityType
      // Will be an empty array for a virtual entity
      _fieldMetaInfo.put(entityType, sharedFmi);

      // Allow subclass to provide vitual fields
      IGTFieldMetaInfo[] sharedVfmi = defineVirtualFields(entityType);
      _virtualFieldMetaInfo.put(entityType, sharedVfmi);

      // Also prepare the fieldId key lookup table
      initSharedFieldMetaInfoLookup(entityType, sharedFmi, sharedVfmi);

      if(isReal)
      {
        // Now we iterate through this field meta info searching for embedded entity fields
        for(int i=0; i < sharedFmi.length; i++)
        {
          switch(sharedFmi[i].getConstraintType())
          {
            case IConstraint.TYPE_LOCAL_ENTITY:
            { // When we find a field that is for an embedded entity
              ILocalEntityConstraint constraint = (ILocalEntityConstraint)sharedFmi[i].getConstraint();
              // We obtain its (ctrl) entityType from the ILocalEntityConstraint
              String embeddedType = constraint.getEntityType();
              if(getSharedFmi(embeddedType) == null)
              { // If we havent already loaded metaInfo for this type then we do so now
                // by making a recursive call back to this method
                loadFmi(embeddedType);
              }
            }break;

            case IConstraint.TYPE_DYNAMIC_ENTITY:
            { //20021031AH
              IDynamicEntityConstraint constraint = (IDynamicEntityConstraint)sharedFmi[i].getConstraint();
              String[] allowedTypes = constraint.getAllowedTypes();
              for(int j=0; j < allowedTypes.length; j++)
              {
                loadFmi(allowedTypes[j]);
              }
            }break;
          }
        }
      }

      // Now we also iterate through virtual field meta info searching for embedded entity fields
      // 20030602AH - question: should we not also do this step for virtual entities???
      // 20030623AH - answer: ummm arent we doing that. Check your brackets mate!

      for(int i=0; i < sharedVfmi.length; i++)
      {
        switch(sharedVfmi[i].getConstraintType())
        {
          case IConstraint.TYPE_LOCAL_ENTITY:
          { // When we find a field that is for an embedded entity
            ILocalEntityConstraint constraint = (ILocalEntityConstraint)sharedVfmi[i].getConstraint();
            // We obtain its (ctrl) entityType from the ILocalEntityConstraint
            String embeddedType = constraint.getEntityType();
            if(getSharedFmi(embeddedType) == null)
            { // If we havent already loaded metaInfo for this type then we do so now
              // by making a recursive call back to this method
              loadFmi(embeddedType);
            }
          }break;

          case IConstraint.TYPE_DYNAMIC_ENTITY:
          { //20021031AH
            IDynamicEntityConstraint constraint = (IDynamicEntityConstraint)sharedFmi[i].getConstraint();
            String[] allowedTypes = constraint.getAllowedTypes();
            for(int j=0; j < allowedTypes.length; j++)
            {
              loadFmi(allowedTypes[j]);
            }
          }break;
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error initialising metainfo for entityType:" + entityType,t);
    }
  }

  /**
   * Loads the EntityMetaInfo object from the server using the serverMappedName for the entityType.
   * (GTAS uses slighlty different names for its entities than the ctrl package).
   * @param entityType the ctrl package name for this entityType
   * @return EntityMetaInfo
   * @throws GTClientException
   */
  protected EntityMetaInfo loadGtasMetaInfo(String entityType)
    throws GTClientException
  {
    // We obtain the serverMappedName that is used by gtas to represent this entityType
    String serverMappedName = DefaultGTEntities.getServerMappedName(entityType);
    try
    {
      // We now request gtas to return us the EntityMetaInfo object for this entityType
      EntityMetaInfo metaInfo = MetaInfoFactory.getInstance().getMetaInfoFor(serverMappedName);
      if(metaInfo == null)
      { // We do a sanity check to make sure something was returned
        throw new java.lang.NullPointerException("MetaInfo returned by MetaInfoFactory is null");
      }
      // And then return it to the caller
      return metaInfo;
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Unable to retrieve field meta info from server for entityType:"
                                  + entityType + ", serverMappedName:" + serverMappedName,ex);
    }
  }

  /**
   * Will extract the FieldMetaInfo array from the EntityMetaInfo and return an
   * array of DefaultSharedFMI objects that wraps this information.
   * @param gtasEntityMetaInfo
   * @return DefaultSharedFMI[]
   * @throws GTClientException
   */
  protected DefaultSharedFMI[] convertMetaInfo(String entityType, EntityMetaInfo entityMetaInfo)
    throws GTClientException
  {
    try
    {
      // Extract array of FieldMetaInfo from the EntityMetaInfo object
      FieldMetaInfo[] fieldMetaInfo = entityMetaInfo.getFieldMetaInfo();
      // Create an array of DefaultSharedFMI[] of same length
      DefaultSharedFMI[] sharedFmi = new DefaultSharedFMI[fieldMetaInfo.length];
      for (int i=0; i < fieldMetaInfo.length; i++)
      { // Iterate through the field meta info creating DefaultSharedFMI objects and store
        // these in the sharedFmi array
        Number fieldId = fieldMetaInfo[i].getFieldId();
        String label = fieldMetaInfo[i].getLabel(); // Used for debugging in the exception msg
        try
        {
          sharedFmi[i] = new DefaultSharedFMI(fieldMetaInfo[i]);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Unable to create DefaultSharedFMI object for field id=" + fieldId
                                      + " (label='" + label + "')",t);
        }
      }
      // return the sharedFmi array of DefaultSharedFMI objects
      return sharedFmi;
    }
    catch(Exception e)
    {
      throw new GTClientException("Error converting EntityMetaInfo for entityType='" + entityType +"'",e);
    }
  }



  /**
   * Create a new instance of the entity object and its embedded entity objects. The fields
   * will not have any values initialised at this stage except for embedded entity fields.
   * The method is recursive to allow for embedded entities which themselves have embedded
   * entities.
   * @param entityType (ctrl) entity type
   */
  AbstractGTEntity initEntityObjects( String entityType )
    throws GTClientException
  {
    // Create new instance of this type of entity
    AbstractGTEntity entity = createEntityObject(entityType);
    // Setup the entities 'structural' data. This includes type, session, manager, and of course
    // the infamous field meta info.
    initEntitySetup(entity, entityType);
    if(!(entity instanceof IGTVirtualEntity))
    { //20021031AH - question:  Why do we check if its virtual????
      //20030522AH - answer:    Because virtual ones have no sharedFmi and will fail the null check.
      //20030602AH - question:  But surely then we should iterate on the results of getSharedFieldMetaInfo() which returns an IGTFieldMetaInfo[]???
      //20030602AH - answer:    Yes but that creates a new array instance each time. Inefficient!
      //20030602AH - question:  Is that the best excuse you have? We really should init the vFields as well!!!!

      // Obtain the metainfo for this entity type
      //20030602AH - DefaultSharedFMI[] sharedFmi = (DefaultSharedFMI[])_fieldMetaInfo.get(entityType);
      DefaultSharedFMI[] sharedFmi = getSharedFmi(entityType); //20030602AH - Use the internal method provided
      if(sharedFmi == null)
      { // Sanity check
        throw new IllegalStateException("No shared field meta info loaded for entityType:" + entityType); //20030602AH - Change from GTClientException to IllegalStateException
      }
      for(int i=0; i < sharedFmi.length; i++)
      { // Iterate the fields it will have looking for embedded entitites
        //20030602AH - @todo: It seems there are several places that need to recursively iterate the local
        //entity tree. Perhaps we should factor out the iteration code and use command pattern for
        //the work on each object?
        if(sharedFmi[i].getConstraintType() == IConstraint.TYPE_LOCAL_ENTITY)
        { // When we find an embedded entity
          if(sharedFmi[i].isCollection())
          {
            // Collections of embedded entities cannot have the IGTEntity instance instantiated yet
            // as we dont know how many there are. In this case we put an empty collection in the field.
            Vector embeddedEntities = new Vector();
            entity.setNewFieldValue(sharedFmi[i].getFieldId(), embeddedEntities);
          }
          else
          {
            ILocalEntityConstraint constraint = (ILocalEntityConstraint)sharedFmi[i].getConstraint();
            String embeddedType = constraint.getEntityType();
            int managerType = _session.getManagerType(embeddedType);
            DefaultAbstractManager manager = (DefaultAbstractManager)_session.getManager(managerType);
            // We do a recursive call back to this method to initialise it
            AbstractGTEntity embeddedEntity = manager.initEntityObjects(embeddedType);
            // We then init that field in this entity with the newly created embedded entity
            entity.setNewFieldValue(sharedFmi[i].getFieldId(), embeddedEntity);
          }
        }
      }
      // Finally we return the created instance. At this stage only fields for embedded entitites
      // have been created (as they are essentially a structural part of this entity).
      // Other field values within this and its embedded entities have yet to be initialised
    }
    return entity;
  }

  /**
   * Handles a create event. Gtas will return its version of the object after the event
   * too so any fields set in response to the update at the server end in the entity will be
   * reflected at this end.
   * @param event
   * @param entity
   * @throws GTClientException
   */
  protected final void handleCreateEvent(IEvent event, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      // When the event is handled we are returned a Map in same manner as a get event.
      Map fieldMap = (Map)handleEvent(event);
      if (fieldMap == null)
      { // Sanity check on what we were returned
        throw new java.lang.NullPointerException("Returned fieldMap is null for create event " + event);
      }

      // Reload entities fields from the returned fieldMap
      initEntityFields(getEntityType(), entity, fieldMap);
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling create event", ex);
    }
  }

  /**
   * Handles an update event. Gtas will return its version of the object after the event
   * too so any fields set in response to the update at the server end in the entity will be
   * reflected at this end.
   * @param event
   * @param entity
   * @throws GTClientException
   */
  protected final void handleUpdateEvent(IEvent event, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      // When the event is handled we are returned a Map in same manner as a get event.
      Map fieldMap = (Map)handleEvent(event);
      if (fieldMap == null)
      { // Sanity check on what we were returned
        throw new java.lang.NullPointerException("Returned fieldMap is null for update event " + event);
      }
      // Reload entities fields from the returned fieldMap
      initEntityFields(getEntityType(), entity, fieldMap);
    }
    catch(Throwable ex)
    {
      throw new GTClientException("Error handling update event", ex);
    }
  }

  /*protected final void handleDeleteEvent(IEvent event)
    throws GTClientException
  {
    try
    {
      handleEvent(event);
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling delete event", ex);
    }
  }*/

  /**
   * Handle the getListEvent() and return a collection of entities
   * @param event
   * @return collection of entities
   * @throws GTClientException
   */
  final List handleGetListEvent(IEvent event)
    throws GTClientException
  { //20030321AH - Now returns List instead of collection
    try
    {
      // The handleEvent method will return us an EntityListResponseData object
      EntityListResponseData results = (EntityListResponseData)handleEvent(event);
      return processEntityListResponse(results, event); //20030320AH - Factored out processing
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Manager:" + this
                                  + " encountered error handling get list event:"
                                  + event, ex);
    }

  }

  final List processEntityListResponse(EntityListResponseData results, IEvent event)
    throws GTClientException
  { //20030320AH
    //20030321AH - Now returns List instead of Collection
    // Create a Vector to house the list of returned entities
    if(results == null) throw new NullPointerException("results is null");
    if(event == null) throw new NullPointerException("event is null");
    List returnList = new Vector();
    try
    {
      // From it we extract the collection of fieldMaps
      Collection list = results.getEntityList();
      String entityType = getEntityType(); //20030130AH
      int element = 0; //20030130AH
      if(list != null)
      {
        Iterator i=list.iterator();
        while( i.hasNext() )
        { // We iterate through the collection
          Object elementObject = i.next(); //20030130AH
          Map fieldMap = null;
          try
          { //20030130AH - Extra error checking
            fieldMap = (Map)elementObject;
          }
          catch(ClassCastException cce)
          { //20030130AH
            throw new GTClientException("Expecting java.util.Map for element "
              + element + " of returned collection for event " + event
              + " but found " + elementObject.getClass().getName(),cce);
          }
          if(fieldMap == null)
          { // If one of the elements is null then we throw an exception
            throw new java.lang.NullPointerException("Null field map for entity in element "
                + element + " of returned collection for event:" + event);
          }
          try
          {
            fieldMap = preProcessEntityFieldMap(fieldMap, event); //20030129AH
            //System.out.println("processEntityListResponse(): fieldMap="+fieldMap);
            // Create an entity object to hold the values from the map
            AbstractGTEntity entity = initEntityObjects(entityType);
            //System.out.println("processEntityListResponse(): initEntity="+entity);
            // Initialise the entity object from the map
            initEntityFields(entityType, entity, fieldMap);
            //System.out.println("processEntityListResponse(): entity="+entity);
            // And add the entity object to our collection to be returned
            returnList.add(entity);
            element++; //20030130AH
          }
          catch(Throwable t)
          { //20030130AH
            throw new GTClientException("Manager:" + this + " caught error"
                                        + " processing entity of type "
                                        + entityType
                                        + " in element " + element
                                        + " of returned collection (size="
                                        + list.size() + ") of field maps",t);
          }
        }
      }
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Manager:" + this
                                  + " encountered error processing EntityListResponseData"
                                  + " returned for event:" + event, ex);
    }
    // Finally we return the collection of entities
    return returnList;
  }

  /**
   * Handle the getXXXEvent passed us. Return an entity.
   * @param event
   * @return entity
   * @throws GTClientException
   */
  protected final AbstractGTEntity handleGetEvent(IEvent event)
    throws GTClientException
  {
    try
    {
      // Pass the event to handleEvent() which should return us a fieldMap
      Map fieldMap = (Map)handleEvent(event);
      if (fieldMap == null)
      { // Sanity check on what we were returned
        throw new java.lang.NullPointerException("Returned fieldMap is null for event " + event);
      }
      fieldMap = preProcessEntityFieldMap(fieldMap, event); //20030129AH
      // Create an entity object to house the returned data
      AbstractGTEntity entity = initEntityObjects(getEntityType());
      // Initialise its fields from the fieldMap
      initEntityFields(getEntityType(), entity, fieldMap);
      // Return the initialised entity
      return entity;
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling get event: " + event, ex);
    }
  }

  /**
   * Will initialise an entity object based on its type (and fieldMeetaInfo) from the map
   * that gtas provided us.
   * @param entityType (ctrl) entity type name
   * @param entity
   * @param fieldMap
   * @throws GTClientException
   */
  void initEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
   try {

     // Since we got the map from gtas is obviously not a new entity so set this flag to false
     /*080104NSL Don't set at the start, wait till the end when the rest of the fields are init.
     entity.setNewEntity(false);
     */
     // Obtain the shared field meta info for this entity type
     DefaultSharedFMI[] sharedFmi = getSharedFmi(entityType);
     // Do a sanity check to ensure we found metaInfo for this entity type.
     if (sharedFmi == null) 
     { // Nope. Quite insane. This ones for the looney bin Im afraid. :-(
       throw new java.lang.NullPointerException("Manager" + this
           +" has no shared field meta info loaded for entityType:"
           + entityType);
     }

     for (int i = 0; i < sharedFmi.length; i++) 
     { // Iterate through all the field meta info objects
       try 
       {
         // and initialise the individual field from the map based on its entityType
         // and the metaInfo for this field
         initRetrievedField(entityType, entity, fieldMap, sharedFmi[i]);
       }
       catch (Throwable t) 
       { // Holy peanut butter and jelly sandwiches Batman! It didnt work!
         // Yes Robin. Now we must inform the good citizens of Gotham City before it is
         // too late. Ill have to throw an exception! Stand back Robin! This is no task for a boy.
         throw new GTClientException("Error initialising field " +
                                     sharedFmi[i].getFieldId()
                                     + " for entityType:" + entityType, t);
       }
     }

     try 
     {
       initVirtualEntityFields(entityType, entity, fieldMap);
     }
     catch (Throwable t) 
     {
       throw new GTClientException(
           "Error initialising virtual fields for entityType:" + entityType,
           t);
     }

      //080104NSL: set now
     entity.setNewEntity(false);
   }
   catch (Throwable t) 
   {
     // Oh no Batman! Another exception!
     // Yes Robin! A crimefighters work is never done!
     // Stay clear - I'll need to throw it again!
     // Alright Batman! I just love watching the way you throw these incorrigible exceptions!
     throw new GTClientException(
         "Error initialising fields of retrieved entity of entityType:"
         + entityType, t);
   }
  }

  /**
   * Will initialse the field in the entity from the map returned by gtas handling embedded/foreign
   * entities, etc...
   * @param entityType the (ctrl) entity type name
   * @param fieldMap the map returned by gtas
   * @param fmi the DefaultSharedFMI object for this field
   */
  void initRetrievedField(String entityType,
                          AbstractGTEntity entity,
                          Map fieldMap,
                          DefaultSharedFMI fmi)
    throws GTClientException
  {
    //@todo: this method is getting long winded with lots of redundant bits. A refactoring is in order....
    try
    {
      int managerType = -1;
      DefaultAbstractManager manager = null;
      // Obtain the constraint object for this field
      IConstraint constraint = fmi.getConstraint();
      // Obtain the constraint type for this field
      int constraintType = fmi.getConstraintType();
      // For convienience obtain the fieldId for this field from the fmi
      Number fieldId = fmi.getFieldId();
      // Process the field based on its constraint type
      //System.out.println("initRetrievedField(): entityType="+entityType+", constraintType="+constraintType+", fieldId="+fieldId +", fieldIdClass="+fieldId.getClass().getName());
      switch(constraintType)
      {
        case IConstraint.TYPE_UID:
        {
          //System.out.println("initRetrievedField(): constraintType=TYPE_UID");
          // For a uid field we shall inform the entity that this field is its uid so it will
          // know which field to use in its getUid() methods.
          if(fmi.isCollection())
          { // Sanity check - cant have uid as a collection!
            throw new java.lang.IllegalArgumentException("UID field id=" + fieldId + " may not be a collection");
          }
          // Inform the entity which field to use for its uid
          entity.setUidFieldId(fieldId);
          // Copy in the field value
          entity.setNewFieldValue(fieldId, fieldMap.get(fieldId) );
          //System.out.println("initRetrievedField(): fieldMap.get(fieldId)="+fieldMap.get(fieldId));
        }break;

        case IConstraint.TYPE_ENUMERATED:
        case IConstraint.TYPE_FILE:
        case IConstraint.TYPE_OTHER:
        case IConstraint.TYPE_RANGE:
        case IConstraint.TYPE_TEXT:
        case IConstraint.TYPE_TIME:
        {
          // Just copy in the field value as normal. Collections will be returned as such so this
          // will handle them too.
          entity.setNewFieldValue(fieldId, fieldMap.get(fieldId) );
        }break;

        case IConstraint.TYPE_DYNAMIC_ENTITY: //20021031AH
        {
          // Extract the object in the gtas returned map representing this contents of this dynamic
          // entity field.
          Object efm = fieldMap.get(fieldId);
          if(fmi.isCollection())
          { // If this field is a collection
            try
            {
              // Declare a variable that will hold the collection fo entities. nb: Even if there are
              // not actually any values in the collection this field should still return an empty
              // collection rather than null.
              Vector dynamicEntities = null;
              if(efm == null)
              {
                // Initialise the collection with an empty vector
                dynamicEntities = new Vector(0);
              }
              else
              {
                if(efm instanceof Collection)
                { // If the field value is a collection (which it darn well better be!)
                  int index = 0; // to keep track of where we are in collection
                  Collection efmCol = (Collection)efm;
                  // We now create our own Vector (rather than recycling whatever it is thats in
                  // the map). For efficiency we make this the correct size to start with.
                  dynamicEntities = new Vector(efmCol.size());
                  Iterator i = efmCol.iterator();
                  while(i.hasNext())
                  { // Iterate through each element of this collection
                    Object dfm = i.next();
                    if(dfm == null)
                    { // In a dynamic entity collection field, a null value is acceptable
                      dynamicEntities.add(null);
                    }
                    else if(dfm instanceof Map) //Sanity check to see that its a Map if not null
                    {
                      // Initialise the entity object for this element in the collection
                      AbstractGTEntity dynamicEntity = initRetrievedDynamicEntity(fieldId,
                                                                              constraint,
                                                                              fieldMap,
                                                                              (Map)efm,
                                                                              entity,
                                                                              index);
                      // And add it to our vector
                      entity.setNewFieldValue(fieldId, dynamicEntity);
                    }
                    else
                    { // We have been asked to process garbage. We dont do that. Sorry.
                      // Maybe you could try next door. Please wait while we cast your sorry class
                      // out of here...
                      throw new java.lang.ClassCastException("Expecting java.util.Map but found "
                                                            + efm.getClass().getName()
                                                            + " at index " + index
                                                            + " of collection of dynamic entities");
                    }
                    index ++;
                  }
                }
                else
                { // Dodgy data (agaisnt our specs) in this field of the serverMap....
                  throw new java.lang.ClassCastException("Expecting java.util.Collection but found "
                                                            + efm.getClass().getName());
                }
              }
              //Store the vector we created in the entity field
              entity.setNewFieldValue(fieldId, dynamicEntities);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error processing dynamic entity collection field",t);
            }
          }
          else // not a collection
          {
            try
            {
              if(efm == null)
              { // Dynamic entity fields are allowed to have null values
                entity.setNewFieldValue(fieldId, null);
              }
              else
              {
                if(efm instanceof Map) // Sanity check
                {
                  // Get the entity object that is the content of this field
                  AbstractGTEntity dynamicEntity = initRetrievedDynamicEntity(fieldId,
                                                                              constraint,
                                                                              fieldMap,
                                                                              (Map)efm,
                                                                              entity,
                                                                              -1);
                  // and add it to the field
                  entity.setNewFieldValue(fieldId, dynamicEntity);
                }
                else // not a map
                {
                  // Throw out the garbage we have been given. Maps, null ok, but anything else we
                  // can't handle and is agaisnt our specs for responses from the b-tier
                  throw new java.lang.ClassCastException("Expecting java.util.Map but found "
                                                          + efm.getClass().getName());
                }
              }
            }
            catch(Throwable t)
            { // Why do bad things happen to good entities?
              throw new GTClientException("Error processing dynamic entity field",t);
            }
          }
        }break;

        case IConstraint.TYPE_LOCAL_ENTITY:
        {
          // Get the type of embedded entity
          String embeddedType = ((ILocalEntityConstraint)constraint).getEntityType();
          managerType = _session.getManagerType(embeddedType);
          manager = (DefaultAbstractManager)_session.getManager(managerType);
          if(fmi.isCollection())
          {
            try
            {
              // If its a collection:
              // Get the collection object from the map
              Collection embeddedEntities = (Collection)fieldMap.get(fieldId);
              if(embeddedEntities == null)
              { // If no collection create one!
                embeddedEntities = new Vector(0);
              }
              // Create a new vector to store the embedded entities (yep. We DONT use the same
              // type as was passed us (unless its a vector). In any case we wont be using the
              // collection object instance itself that was passed us).
              // (Collection was instantiated and stored in entity object when it was created)
              Vector localEntities = (Vector)entity.getFieldValue(fieldId);
              // We may be doing an update. In this case clean out all the old data and reinitialise
              // the lot.
              localEntities.clear();
              // Get an iterator!
              Iterator i = embeddedEntities.iterator();
              int count = 0;
              while(i.hasNext())
              {
                try
                { // For each entity in the collection, retrieve the map from the collection
                  // passed to us.
                  Object efm = i.next();
                  if(efm == null)
                  { // Sanity check
                    throw new java.lang.NullPointerException("Null field map encountered");
                  }
                  if(! (efm instanceof Map) )
                  { // Sanity check
                    throw new java.lang.ClassCastException("Expecting java.util.Map but found "
                                                            + efm.getClass().getName());
                  }
                  Map embeddedFieldMap = (Map)efm;
                  // Recursive call to get an entity of this type
                  AbstractGTEntity embeddedEntity = manager.initEntityObjects(embeddedType);
                  // Recursive call to initialise the object from the map
                  manager.initEntityFields(embeddedType, embeddedEntity, embeddedFieldMap);
                  // Add to the Vector object stored in our entity for this field
                  localEntities.add(embeddedEntity);
                }
                catch(Throwable t)
                {
                  throw new GTClientException("Error processing embedded entity "+ count
                                              + " in embedded entity collection",t);
                }
              }
              entity.setNewFieldValue(fieldId,localEntities);
            }
            catch(Throwable t)
            { // Arrgh!
              throw new GTClientException("Error processing collection of embedded entities",t);
            }
          }
          else
          {
            try
            {
              // If its not a collection but rather a single embedded entity:
              // Obtain the map for its values from the fieldMap returned by gtas
              Object efm = fieldMap.get(fieldId);
              if(efm == null)
              { // Sanity check
                throw new java.lang.NullPointerException("Null field map encountered");
              }
              if(! (efm instanceof Map) )
              { // Sanity check
                throw new java.lang.ClassCastException("Expecting java.util.Map but found "
                                                        + efm.getClass().getName());
              }
              Map embeddedFieldMap = (Map)efm;
              // Get the empty embedded entity object from our entity object
              AbstractGTEntity embeddedEntity = (AbstractGTEntity)entity.getFieldValue(fieldId);
              // Recursive call to initialise its fields based on the fieldMap
              manager.initEntityFields(embeddedType, embeddedEntity, embeddedFieldMap);
            }
            catch(Throwable t)
            { //Oops!
              throw new GTClientException("Error processing embedded entity",t);
            }
          }
        }break;

        case IConstraint.TYPE_FOREIGN_ENTITY:
        {
          // Get the constraint object as an IForeignEntityConstraint for convienience
          IForeignEntityConstraint foreignConstraint = (IForeignEntityConstraint)constraint;
          if(foreignConstraint.isCached())
          { // If its a cached foreign entity
            // Obtain the entityType for it
            String foreignEntityType = foreignConstraint.getEntityType();
            // Obtain the manager type associated with this type of entity
            managerType = _session.getManagerType(foreignEntityType);
            // Obtain a manager of this type
            manager = (DefaultAbstractManager)_session.getManager(managerType);
            if(fmi.isCollection())
            { // If its a collection of foreign entities
              // Extract the collection object from the map returned us by gtas
              Collection cachedEntities = (Collection)fieldMap.get(fieldId);
              if(cachedEntities == null)
              { // If no collection create one!
                cachedEntities = new Vector(0);
              }
              // Create a new Vector (regardless of returned collection type!) to store our cached
              // foreign entities
              Vector foreignEntities = new Vector(cachedEntities.size());
              // Create a new vector to store the key values
              Vector foreignKeys = new Vector(cachedEntities.size());
              Iterator i = cachedEntities.iterator();
              while(i.hasNext())
              { // Iterate through the collection
                // Get the fieldMap for the cached foreign entity
                
                //20040210AH - Improve checking of the data and reporting classcast issues (for example if supposed to be cached but returns long)
                Object foreignCacheMapObject = i.next();
                if( (foreignCacheMapObject != null) && (!(foreignCacheMapObject instanceof Map)) )
                { //Andrews entry for the 2004 'long-winded exception message' competition ;-)
                  //todo - move the null check block above here so we dont waste time checking null twice!
                  throw new ClassCastException("Manager "
                            + this
                            + " was expecting an instanceof Map as an element of foreign cached entity list for fieldId="
                            + fieldId
                            + " of entityType:"
                            + entityType
                            + " but encountered an instance of "
                            + foreignCacheMapObject.getClass().getName()
                            + " (Check that B-Tier is actually returning cached object rather than a key");
                }
                //...               
                Map foreignCacheMap = (Map)foreignCacheMapObject; //20040210AH
                if(foreignCacheMap == null)
                { // Sanity check to make sure element in collection is valid
                  throw new java.lang.NullPointerException("No data for foreign cached entity (in collection) of entityType:"
                                                            + entityType + " in field id=" + fieldId);
                }
                else
                {
                  // Obtain an entity instance from this map
                  AbstractGTEntity cachedForeignEntity = null;
                  try
                  { //20030130AH
                    cachedForeignEntity = buildEntityFromMap( manager,
                                                              foreignEntityType,
                                                              foreignCacheMap);
                  }
                  catch(Throwable t)
                  { //20030130AH
                    throw new GTClientException("Manager:" + this
                      + " encountered error using manager:" + manager
                      + " to build foreign cached entity in collectionn of type:" + foreignEntityType
                      + " from map for field id=" + fieldId
                      + " of entity:" + entity,t);
                  }
                  // Add to our cache collection
                  foreignEntities.add(cachedForeignEntity);
                  // Get the field in the cached entity which is referred to by this entity
                  // (Need to convert the string fieldName to numeric fieldId)
                  Number foreignKeyFieldId = cachedForeignEntity.getFieldId(foreignConstraint.getKeyFieldName());
                  // Set the value of this field from the cached entity
                  Object foreignKeyValue = cachedForeignEntity.getFieldValue(foreignKeyFieldId);
                  // Store this in our Vector of foreign key values
                  foreignKeys.add(foreignKeyValue);
                }
              }
              // Add the cached entity collection to this entitys foreign entity object cache
              // keyed by fieldId
              entity.addCachedForeignEntity(fieldId, foreignEntities);
              // Set the value for this field to the Vector of keys
              entity.setNewFieldValue(fieldId, foreignKeys);
            }
            else
            { // If its a cached foreign entity thats not a collection
              // Obtain the fieldMap for the cached foreign entity
              Map foreignCacheMap = null;
              try
              {
                foreignCacheMap = (Map)fieldMap.get(fieldId);
              }
              catch(java.lang.ClassCastException cce)
              {
                Object badField = fieldMap.get(fieldId);
                throw new GTClientException("Manager:" + this
                                            + " was expecting java.util.Map for field " + fieldId
                                            + " (FER) of entity " + entity
                                            + " but found "
                                            + badField.getClass().getName() + " (value=" + badField + ")",cce);
              }
              if(foreignCacheMap == null)
              {
                /*throw new java.lang.NullPointerException("No data in map for foreign cached entity of entityType:"
                                                            + foreignEntityType + " in field id=" + fieldId + " of entity:" + entity);*/
                entity.setNewFieldValue(fieldId, null);
              }
              else
              {
                // Obtain an entity instance from this map
                AbstractGTEntity cachedForeignEntity = null;
                try
                { //20030130AH
                  cachedForeignEntity = buildEntityFromMap( manager,
                                                            foreignEntityType,
                                                            foreignCacheMap);
                }
                catch(Throwable t)
                { //20030130AH
                  throw new GTClientException("Manager:" + this
                    + " encountered error using manager:" + manager
                    + " to build foreign cached entity of type:" + foreignEntityType
                    + " from map for field id=" + fieldId
                    + " of entity:" + entity,t);
                }
                // Add the cached entity to our entities foreign entity cache keyed by fieldId
                entity.addCachedForeignEntity(fieldId, cachedForeignEntity);
                // Obtain the fieldId for the referenced key field
                // (need to convert string fieldname into numeric fieldId)
                String foreignKeyFieldName = foreignConstraint.getKeyFieldName();
                Number foreignKeyFieldId = cachedForeignEntity.getFieldId(foreignKeyFieldName);
                // Get the value of this field in the cached entity
                Object foreignKeyValue = cachedForeignEntity.getFieldValue(foreignKeyFieldId);
                // Make this the value of the field in our entity
                entity.setNewFieldValue(fieldId, foreignKeyValue);
              }
            }
          }
          else
          { // Foreign entity. No cache. The value in the map will be the reference key value already
            // just copy it to our entity
            entity.setNewFieldValue(fieldId, fieldMap.get(fieldId) );
          }
        }break;

        default:
        {
          // Whoa! No idea what to do with this field. Just die already!
          throw new java.lang.IllegalArgumentException("Unknown constraintType:" + constraintType);
          // no need to "break" ah!, cos ah!, already broken one!
        }
      }
    }
    catch(Throwable t)
    { // Arrrghhh! the pain the pain! hot potato! hot potato! Throw it away!
      throw new GTClientException("Error initialising field id="
                                  + fmi.getFieldId() + " (label='" + fmi.getLabel() + "')"
                                  + " of entity:" + entity
                                  + " encountered by manager:" + this,t);
    }
  }

  /**
   * Create an AbstractGTEntity instance for a dynamic entity field.
   * @param fieldId id of field being processed
   * @param constraint the constraint record for this field
   * @param fieldMap field map from gtas of entity of which this field is a member
   * @param entity the entity being processed
   * @param index index into collection of dynamic entities being processed (-1 if not a collection)
   * @return the IGTEntity for the field (caller will put it in entity)
   */
  protected AbstractGTEntity initRetrievedDynamicEntity(Number fieldId,
                                            IConstraint constraint,
                                            Map fieldMap,
                                            Map dynamicFieldMap,
                                            AbstractGTEntity entity,
                                            int index)
  throws GTClientException
  {
    try
    {
      if(constraint.getType() != IConstraint.TYPE_DYNAMIC_ENTITY)
      { //Sanity check
        throw new java.lang.IllegalArgumentException("Field " + fieldId
          + " is not a dynamic entity. Constraint type = " + constraint.getType());
      }
      if(dynamicFieldMap == null)
      { //If null do no processing. Null is a valid value though so return it.
        //(We probably wont get this far as initRetrievedFields() already catches nulls,
        //but we may later need to call this from somewhere else...
        return null;
      }
      //Delegate to subclass to determine what type of entity it actually is
      String dynamicType = getDynamicType(fieldId,(IDynamicEntityConstraint)constraint,
                                          fieldMap,entity,index);
      if(dynamicType == null)
      { // Another sanity check
        throw new GTClientException("Unable to determine type of dynamic entity in field "
                                      + fieldId + " of entity " + entity);
      }
      // Get a manager that can manage this entity type
      DefaultAbstractManager manager = (DefaultAbstractManager)_session.getManager(dynamicType);
      try
      {
        // Delegate to appropriate manager to create and initialise the entity object
        // based on its gtas returned fieldMap
        AbstractGTEntity dynamicEntity = manager.createEntityObject(dynamicType);
        manager.initEntitySetup(dynamicEntity, dynamicType); // 20021209 DDJ
        manager.initEntityFields(dynamicType, dynamicEntity, dynamicFieldMap);
        // return processed object
        return dynamicEntity;
      }
      catch(Throwable t)
      { // Add debugging info to help us track down errors that might occur
        throw new GTClientException("Error delegating to manager " + manager
          + " to create and initialise entity object for dynamic entity in field " + fieldId
          + " of entity " + entity,t);
      }
    }
    catch(Throwable t)
    { // Ohno!
      throw new GTClientException("Error initialising retrieved dynamic entity field "
                                  + fieldId + " of entity " + entity,t);
    }
  }

  /**
   * Get the manager to create a new object of this type, and initialise its fields
   * based on the map passed.
   * @param manager
   * @param entityType
   * @param fieldMap
   * @return entity
   * @throws GTClientException
   */
  protected AbstractGTEntity buildEntityFromMap(DefaultAbstractManager manager,
                                                        String entityType,
                                                        Map fieldMap)
    throws GTClientException
  {
    try
    {
      // Get the manager to return a new object for this entity type
      AbstractGTEntity entity = manager.initEntityObjects(entityType);
      // Get the manager to initialise its fields based on the passed fieldMap (got from gtas)
      manager.initEntityFields(entityType,entity,fieldMap);
      // Set the flag indicating its cached
      entity.setFromCache(true);
      // and return the new entity
      return entity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Manager: " + this +" encountered error when delegating to manager: "
                                  + manager + " to process foreign cached entity (of type:" + entityType + ")"
                                  ,t);
    }
  }

  /**
   * Delete a single entity.
   * @param entity
   * @throws GTClientException
   */
  public final void delete(IGTEntity entity) throws GTClientException
  { //20030718AH - Refactored
    if(entity.isNewEntity())
      throw new IllegalStateException("Entity is new");
    if (entity == null)
      throw new NullPointerException("entity is null");
    long[] uids = new long[1];
    uids[0] = entity.getUid();
    delete(uids);
  }

  /**
   * Pass delete events to gtas for all entities of the entityType for which the
   * manager is responsible.
   * @param long[] uids
   * @throws GTClientException
   */
  public final void delete(long[] uids) throws GTClientException
  {
    //20020919AH - Modified to throw normal nested exception immediately upon
    //encountering problem. (Previous behaviour was to try all uids and record arraylist of
    //any exceptions encountered and throw one exception at the end with this list in the
    //extraData field).
    //20030718AH - Modified to use a single event
    if(uids == null) throw new java.lang.NullPointerException("Null uids array");
    if(uids.length == 0) return;

    try
    {
      List uidList = Arrays.asList( StaticCtrlUtils.longArray(uids) ); //20030722AH
//System.out.println("uidList=" + uidList);
      IEvent event = getDeleteEvent( uidList );
      handleEvent( event );
    }
    catch(DeleteException deleteException)
    {
      throw deleteException;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing deletion",t);
    }

    /*20030718AH - co: for (int i=0; i<uids.length; i++)
    { // Iterate the array of uids
      try
      { // Try to delete the specified entity
        // we get the subclass to return us an event object of the appropriate type
        handleDeleteEvent(getDeleteEvent(new Long(uids[i])));
//throw DeleteException.getFakeOne();
      }
      catch(Throwable t)
      {
        if(t instanceof DeleteException)
        { //20030709AH
          throw (GTClientException)t;
        }
        else
        {
          throw new GTClientException("Error deleting entity with uid=" + uids[i],t);
        }
      }
    }*/
  }

  /**
   * Once the entity object instance is created it is necessary to furnish it with the information
   * it will require to do its job. That is the function of this method, which sets up the
   * following data within the entity: entityType, session reference, manager reference,
   * field meta info wrappers.
   * @param entity
   * @param entityType
   * @throws GTClientException
   */
  void initEntitySetup(AbstractGTEntity entity, String entityType)
    throws GTClientException
  {
    try
    {
      if(entity == null)
      { // Sanity check
        throw new java.lang.NullPointerException("entity is null");
      }
      if(entityType == null)
      { // Sanity check
        throw new java.lang.NullPointerException("entityType is null");
      }
      // Give the newborn a sense of identity
      entity.setType(entityType);
      // Let it know about the community it lives in
      entity.setSession(_session);
      // Make sure it knows whos boss
      entity.setManager(this);
      // And outline its job responsibilities
      //entity.setFieldMetaInfo( getWrappedFmi(entity) ); 20021025AH - eliminate this step
    }
    catch(Throwable t)
    { // Throw a tantrum 'cos things didnt go our way!
      throw new GTClientException("Error initialising entity of type:" + entityType,t);
    }
  }

  /**
   * Creates a new instance of the entity object , and initialises its structural setup data,
   * including entityType, session reference, manager reference, and field meta info, and sets
   * the newEntity flag such that isNewEntity() returns true.
   */
  public IGTEntity newEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(getEntityType());
    entity.setNewEntity(true);
    setDefaultFieldValues(entity);
    return entity;
  }

  /*
   * Subclass should override to set the initial field values for a newly created AbstractGTEntity object.
   * @param entity the entity just created
   * @throws GTClientException
   */
  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
  }

  /*
   * Gets a filtered collection of IGTEntities the hard way, by getting all the entities from
   * GTAS and then comparing the keys of each and returning a collection of those that match
   * @param key The value the key must have to be included in the collection (using equals method)
   * @param keyFieldId The numeric fieldId of the field that we chack the key against
   * @return subset of IGTEntities retrieved by getAll() whose keys match
   * @throws GTClientException
   */
  protected final Collection getByKeyHeavy(Object key, Number keyFieldId)
    throws GTClientException
  { //20030130AH - Factored out
    try
    {
      Vector results = new Vector();
      Collection all = getAll();
      Iterator i = all.iterator();
      while(i.hasNext())
      {
        IGTEntity entity = (IGTEntity)i.next();
        Object value = entity.getFieldValue(keyFieldId);
        if(key != null)
        {
          if(key.equals(value))
          {
            results.add(entity);
          }
        }
        else
        {
          if(value == null)
          {
            results.add(entity);
          }
        }
      }
      return results;
    }
    catch(Throwable t)
    { //20030117AH
      throw new GTClientException("Error using p-tier filtering to perform getByKey()"
        + "where keyField is vfield",t);
    }
  }

  public final Collection getByKey(Object key, Number keyFieldId)
    throws GTClientException
  {
    //nb: if our fieldId doent match gtas for non-virtual field, results are 'undefined'!
    //20030130AH - Mod to fallback to using heavy filtering if gtas filtering unsupported
    try
    {
      if(key == null)
      { //20030117AH - Extra sanity check
        throw new java.lang.NullPointerException("key is null");
      }
      IGTFieldMetaInfo fmi = getSharedFieldMetaInfo(getEntityType(),keyFieldId);
      if(fmi.isVirtualField())
      { // Slow filtering by manager
        return getByKeyHeavy(key, keyFieldId);
      }
      else
      { // Fast filtering by GTAS
        //20030117AH - Modify to allow returning of extra info in exception message
        DataFilterImpl filter = null;
        IEvent event = null;
        try
        {
          filter = new DataFilterImpl();
          filter.addSingleFilter( null,
                                keyFieldId,
                                filter.getEqualOperator(),
                                key,
                                false);
        }
        catch(Throwable t)
        { //20030130AH
          throw new GTClientException("Error constructing IDataFilter instance to perform getByKey()",t);
        }
        try
        {
          event = getGetListEvent(filter);
        }
        catch(UnsupportedOperationException ux)
        { //20030130AH - Fall back to heavy filtering if GTAS filtering not supported
          return getByKeyHeavy(key, keyFieldId);
        }
        try
        { //20030130AH
          return handleGetListEvent(event);
        }
        catch(Throwable t)
        { //20030117AH
          String filterString = (filter == null) ? "null" : filter.getFilterExpr();
          String eventString = (event == null) ? "null" : event.getClass().getName();
          throw new GTClientException("Error using GTAS filtering to perform getByKey()"
              + " using IDataFilter=" + filterString
              + " with GetListEvent=" + eventString,t);
        }
      }
    }
    catch(Throwable t)
    { //20030117AH - Add extra information into thrown exception to aid debugging
      String keyClassString = (key == null) ? "(unknown class)" : "(" + key.getClass().getName() + ")";
      throw new GTClientException("Manager " + this + " caught error executing getByKey() where"
      + " key=" + key
      + keyClassString
      + " keyFieldId=" + keyFieldId
      ,t);
    }
  }

  public final Collection getByKey(IGTEntityReference ref)
    throws GTClientException
  { //20030529AH
    try
    {
      String entityType = getEntityType();
      if(entityType.equals(ref.getType()))
      {
        return getByKey(ref.getKeyValue(), ref.getKeyFieldId());
      }
      throw new IllegalArgumentException("Bad type:" + ref.getType());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting entity using reference:" + ref,t);
    }
  }

  public Collection getAll() throws GTClientException
  {
    try
    {
      IEvent event = getGetListEvent(null);
      return handleGetListEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing getAll() request", t);
    }
  }

  public IGTListPager getListPager() throws GTClientException
  { //20030321AH
    return getListPager(null, null, false);
  }

  public IGTListPager getListPager(Object key, Number keyFieldId) throws GTClientException
  { //20030321AH - Convenience method
    return getListPager(key,keyFieldId, false);
  }

  public IGTListPager getListPager(Object key, Number keyFieldId, boolean forceHeavy) throws GTClientException
  {
    return getListPager(key,keyFieldId, false, null, null);
  }

  // 20031019 DDJ: Sorting only supported by IDataFilter
  public IGTListPager getListPager(Object key, Number keyFieldId, boolean forceHeavy, Number[] sortField, boolean[] sortAscending)
    throws GTClientException
  { //20030321AH
    try
    {
      ListPager pager = null;
      boolean vfield = false;
      boolean filtered = !( (key == null) || (keyFieldId == null) );
      boolean sorted = sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0;
      
      if(filtered)
      {
        try
        {
          IGTFieldMetaInfo fmi = getSharedFieldMetaInfo(getEntityType(),keyFieldId);
                    
          if(fmi == null)
          {
            throw new NullPointerException("No metainfo for field " + keyFieldId);
          }
          vfield = fmi.isVirtualField();
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error determining if field "
                    + keyFieldId + " is a vfield",t);
        }
      }
            
      if( vfield || (!supportsGtasListPaging()) || forceHeavy)
      {
        pager = new HeavyListPager();
               
        if( filtered )
        {
          IFilter filter = new EntityKeyFilter(keyFieldId, key);
          ((HeavyListPager)pager).setFilter(filter);
        }
      }
      else
      {
        pager = new ListPager();
               
        if( filtered || sorted )
        {
          try
          {
            DataFilterImpl filter = new DataFilterImpl();
            
            if( filtered )
            {
              filter.addSingleFilter( null,
                                      keyFieldId,
                                      filter.getEqualOperator(),
                                      key,
                                      false);
            }

            if( sorted )
            {
              filter.setOrderFields(sortField, sortAscending); // 20031019 DDJ
            }
            pager.setFilter(filter);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error constructing IDataFilter instance for use in ListPager",t);
          }
        }
      }
      if(pager == null)
      {
        throw new UnsupportedOperationException("This manager does not support creation of a ListPager for given parameters");
      }
      try
      {
        pager.setManager(this);
        pager.setSession(_session);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error initialising ListPager:" + pager,t);
      }
    
      return pager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Manager "
                + this
                + " encountered an error attempting to provide a ListPager",t);
    }
  }

  /**
   * @deprecated use getByUid() instead
   */
  public final IGTEntity getByUID(long uid) throws GTClientException
  {
    return getByUid(uid);
  }

  /**
   * Will attempt to return the entity (of the type managed by this manager) that has the
   * specified uid.
   * @param uid
   * @return entity
   * @throws GTClientException
   */
  public final IGTEntity getByUid(long uid) throws GTClientException
  {
    return getByUid(new Long(uid));
  }

  /**
   * Will attempt to return the entity (of the type managed by this manager) that has the
   * specified uid.
   * @param uid
   * @return entity
   * @throws GTClientException
   */
  public IGTEntity getByUid(Long uid) throws GTClientException
  {
    try
    {
      IEvent event = getGetEvent(uid);
      return handleGetEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error attempting to get entity by uid:" + uid, e);
    }
  }

  public String toString()
  {
    return this.getClass().getName() + "(" + getType() + "," + getEntityType() +")";
  }

  protected final void initSharedFieldMetaInfoLookup( String entityType,
                                                DefaultSharedFMI[] sharedFmi,
                                                IGTFieldMetaInfo[] sharedVfmi)
  {
    //20030602AH - Made method final
    if(entityType == null)
    {
      throw new java.lang.NullPointerException("entityType is null");
    }
    if(sharedFmi == null)
    {
      throw new java.lang.NullPointerException("sharedFmi array is null");
    }
    Hashtable fmiLookup = new Hashtable();
    Hashtable fieldIdLookup = new Hashtable();
    for(int i=0; i < sharedFmi.length; i++)
    { // Iterate through real fields and add to hashtable for lookup
      if(sharedFmi[i] == null)
      {
        throw new java.lang.NullPointerException("sharedFmi[" + i + "] is null");
      }
      Number fieldId = sharedFmi[i].getFieldId();
      if(fieldId == null)
      {
        throw new java.lang.NullPointerException("Null fieldId encountered in sharedFmi[" + i
        + "] for real field of entityType '" + entityType + "'. FMI=" + sharedFmi[i]);
      }
      fmiLookup.put(fieldId, sharedFmi[i]);
      fieldIdLookup.put(sharedFmi[i].getFieldName(), sharedFmi[i].getFieldId());
    }
    for(int i=0; i < sharedVfmi.length; i++)
    { // Iterate through virtual fields and add to hashtable for lookup
      if(sharedVfmi[i] == null)
      {
        throw new java.lang.NullPointerException("sharedVfmi[" + i + "] is null");
      }
      fmiLookup.put(sharedVfmi[i].getFieldId(), sharedVfmi[i]);
      fieldIdLookup.put(sharedVfmi[i].getFieldName(), sharedVfmi[i].getFieldId());
    }
    // Store created lookup tables under their entityType name
    _fieldMetaInfoLookup.put(entityType,fmiLookup);
    _fieldIdLookup.put(entityType,fieldIdLookup);
  }

  public Number getFieldId(String entityType, String fieldName)
    throws GTClientException
  {
    if(entityType == null)
    {
      throw new java.lang.NullPointerException("entityType is null");
    }
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("fieldName is null");
    }
    if(StaticCtrlUtils.isNestedFieldName(fieldName))
    {
      throw new java.lang.IllegalArgumentException("fieldName is nested");
    }
    if(_fieldIdLookup == null)
    {
      throw new GTClientException("Internal assertion failure in " + this + " _fieldIdLookup not initialised");
    }
    Hashtable fieldIdLookup = (Hashtable)_fieldIdLookup.get(entityType);
    if(fieldIdLookup == null)
    {
      throw new GTClientException(this + " - does not have fieldIdLookup table for entities of type=" + entityType);
    }
    Number fieldId = (Number)fieldIdLookup.get(fieldName);
    if(fieldId == null)
    {
      throw new GTClientException("Entities of type=" + entityType + " do not have a field named " + fieldName);
    }
    return fieldId;
  }

  public IGTFieldMetaInfo getSharedFieldMetaInfo(String entityType, Number fieldId)
    throws GTClientException
  {
    if(entityType == null)
    {
      throw new java.lang.NullPointerException("entityType is null");
    }
    if(fieldId == null)
    {
      throw new java.lang.NullPointerException("fieldId is null");
    }
    Hashtable fmiLookup = (Hashtable)_fieldMetaInfoLookup.get(entityType);
    if(fmiLookup == null)
    {
      throw new GTClientException(this + " - does not have fieldMetaInfoLookup table for entities of type=" + entityType);
    }
    IGTFieldMetaInfo fmi = (IGTFieldMetaInfo)fmiLookup.get(fieldId);
    if(fmi == null)
    {
      throw new GTClientException("Entities of type=" + entityType + " do not have a field with id=" + fieldId );
    }
    return fmi;
  }

  public IGTFieldMetaInfo getSharedFieldMetaInfo(String entityType, String fieldName)
    throws GTClientException
  {
    if(entityType == null)
    {
      throw new java.lang.NullPointerException("entityType is null");
    }
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("fieldName is null");
    }
    String superField = StaticCtrlUtils.extractSuperFieldName(fieldName);
    if(superField != null)
    {
      String subField = StaticCtrlUtils.extractSubFieldName(fieldName);
      try
      {
        IGTFieldMetaInfo fmi = getSharedFieldMetaInfo(entityType,superField);
        if(fmi == null)
        {
          throw new GTClientException("Entities of type=" + entityType + " do not have a field with name " + superField );
        }
        int constraintType = fmi.getConstraintType();
        if( !((constraintType == IConstraint.TYPE_LOCAL_ENTITY)
            || (constraintType == IConstraint.TYPE_FOREIGN_ENTITY)) )
        {
          throw new GTClientException("Field " + superField + " of entityType " + entityType + " is not an entity reference" );
        }
        String nestedType = ((IEntityConstraint)fmi.getConstraint()).getEntityType();
        IGTManager mgr = _session.getManager(nestedType);
        return mgr.getSharedFieldMetaInfo(nestedType,subField);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error dereferencing nested fieldName",t);
      }
    }
    else
    {
      Number fieldId = getFieldId(entityType,fieldName);
      return getSharedFieldMetaInfo(entityType,fieldId);
    }
  }

  public IGTFieldMetaInfo[] getSharedFieldMetaInfo(String entityType)
    throws GTClientException
  { //20030122AH - Mod to return real and virtual fmi
    if(entityType == null)
    {
      throw new java.lang.NullPointerException("entityType is null");
    }
    //DefaultSharedFMI[] sharedFmi = (DefaultSharedFMI[])_fieldMetaInfo.get(entityType);
    //IGTFieldMetaInfo[] sharedFmi = (IGTFieldMetaInfo[])_fieldMetaInfo.get(entityType); //20021126AH
    IGTFieldMetaInfo[] sharedFmi = (IGTFieldMetaInfo[])getSharedFmi(entityType); //20030122AH
    if(sharedFmi == null)
    {
      throw new GTClientException(this + " - does not have fieldMetaInfo table for entities of type=" + entityType);
    }
    // In addition to returning array of appropriate type, this cloning also stops them fooling with our metainfo
    //20030122AH - also include vfmi
    IGTFieldMetaInfo[] sharedVfmi = (IGTFieldMetaInfo[])getSharedVirtualFmi(entityType);
    if(sharedVfmi == null)
    {
      throw new GTClientException(this + " - does not have virtual fieldMetaInfo table for entities of type=" + entityType);
    }
    int length = sharedFmi.length + sharedVfmi.length;
    IGTFieldMetaInfo[] fmi = new IGTFieldMetaInfo[length];
    for(int i=0; i < sharedFmi.length; i++)
    {
      fmi[i] = (IGTFieldMetaInfo)sharedFmi[i];
    }
    for(int i=0; i < sharedVfmi.length; i++)
    {
      fmi[i+sharedFmi.length] = (IGTFieldMetaInfo)sharedVfmi[i];
    }
    //...
    return fmi;
  }

  protected Collection processMapCollection(IForeignEntityConstraint constraint, Collection maps)
    throws GTClientException
  {
    if(constraint == null)
    {
      throw new java.lang.NullPointerException("null constraint");
    }
    if(maps == null)
    {
      throw new java.lang.NullPointerException("null maps collection");
    }
    if(maps.size() == 0)
    {
      return maps;
    }
    try
    {
      String entityType = constraint.getEntityType();
      IGTManager mgr = _session.getManager(entityType);
      Vector entities = new Vector(maps.size());
      Iterator i = maps.iterator();
      while(i.hasNext())
      {
        Map fieldMap = (Map)i.next();
        IGTEntity entity = buildEntityFromMap((DefaultAbstractManager)mgr,entityType,fieldMap);
        entities.add(entity);
      }
      return entities;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error converting Map collection to IGTEntity collection",t);
    }
  }

  /**
   * Utility method that given a collection of entities returns a collection of values
   * of those entities key fields as defined in the foreign entity constraint passed.
   * If the collection is null, will return null.
   * @param foreignEntityConstraint
   * @param entities
   * @returns keyValues
   * @throws GTClientException
   */
  protected Collection extractKeys(IForeignEntityConstraint constraint, Collection entities)
    throws GTClientException
  {
    if(constraint == null)
    {
      throw new java.lang.NullPointerException("null constraint");
    }
    if(entities == null)
    {
      return null;
    }
    try
    {
      if(entities.size() == 0)
      {
        return entities;
      }
      IGTManager mgr = _session.getManager(constraint.getEntityType());
      Number keyField = mgr.getFieldId(constraint.getEntityType(), constraint.getKeyFieldName());
      Collection keys = new Vector(entities.size());
      Iterator i = entities.iterator();
      while(i.hasNext())
      {
        IGTEntity entity = (IGTEntity)i.next();
        if(entity == null)
        {
          throw new java.lang.NullPointerException("Encountered null reference inside collection");
        }
        Object value = entity.getFieldValue(keyField);
        keys.add(value);
      }
      return keys;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting reference field from collection of entities",t);
    }
  }

  public final void update(IGTEntity entity) throws GTClientException
  {
    if(entity.canEdit())
    {
      doUpdate(entity);
    }
    else
    {
      throw new java.lang.IllegalStateException("Entity may not be modified");
    }
  }

  public final void create(IGTEntity entity) throws GTClientException
  {
    if(entity.canEdit())
    {
      doCreate(entity);
    }
    else
    {
      throw new java.lang.IllegalStateException("Entity may not be modified");
    }
  }

  /**
   * Hook subclass may override to playplay with fieldmap returned from get event or get list event
   * prior to its conversion to an IGTEntity. (This is a good sport for debugging stuff too!)
   */
  protected Map preProcessEntityFieldMap(Map fieldMap, IEvent event)
    throws GTClientException
  { //20030129AH
    return fieldMap;
  }

  public Number getUidFieldId(String entityType)
    throws GTClientException
  { //20030529AH - Quick and dirty impl. Should be cached value when fmi loaded (todo)
    if (entityType == null)
      throw new NullPointerException("entityType is null");
    try
    {
      IGTFieldMetaInfo[] fmi = getSharedFieldMetaInfo(entityType);
      if (fmi == null)
        throw new NullPointerException("fmi is null");
      for(int i=0; i < fmi.length; i++)
      {
        if(fmi[i].getConstraintType() == IConstraint.TYPE_UID)
        {
          return fmi[i].getFieldId();
        }
      }
      return null;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting uid field id",t);
    }
  }
}