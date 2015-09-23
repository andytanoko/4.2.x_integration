/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGTEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-05-20     Neo Sok Lay         Change the fieldNames to Number type.
 * 2002-06-12     Andrew Hill         Change fieldName to fieldId, provide new text fieldNames
 * 2002-07-25     Andrew Hill         Modify for new metaInfo implementation techniques
 * 2002-08-19     Andrew Hill         Track dirty fields & loading of unloaded fields
 * 2002-08-22     Andrew Hill         Modified field get and set methods
 * 2002-10-10     Daniel D'Cotta      Modifield to store FormFileElement[] for filenames
 * 2002-10-25     Andrew Hill         Eliminate unshared metainfo support
 * 2002-10-30     Andrew Hill         Log writes to uneditable fields
 * 2002-10-31     Andrew Hill         getFieldEntities() to support dynamic entity fields
 * 2002-11-13     Andrew Hill         getFieldContainer()
 * 2003-01-17     Andrew Hill         fieldValueChanging() and isLegalValue()
 * 2003-05-09     Andrew Hill         fieldValueChanged()
 * 2003-05-22     Andrew Hill         Added an assertion in setNewEntity()
 */
package com.gridnode.gtas.client.ctrl;

import org.apache.commons.logging.*;
import java.util.*;

import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;

abstract class AbstractGTEntity implements IGTEntity
{
  private static final Log _log = LogFactory.getLog(AbstractGTEntity.class); // 20031209 DDJ
  
  protected DefaultAbstractManager _manager;
  protected Number _uidFieldId;
  protected IGTSession _session;
  protected boolean _isNewEntity = false;
  protected String _type;
  protected Map _fields = Collections.synchronizedMap(new HashMap());
  protected Hashtable _cachedForeignEntities = new Hashtable();
  protected Map _dirtyFields = Collections.synchronizedMap(new HashMap());
  protected boolean _entityDirty;

  protected boolean _fromCache;

  AbstractGTEntity()
  {
  }

  public boolean canDelete() throws GTClientException
  {
    return true;
  }

  public boolean canEdit() throws GTClientException
  {
    return true;
  }

  public boolean isFieldDirty(Number fieldId)
  {
    Object dirtyToken = _dirtyFields.get(fieldId);
    return dirtyToken != null;
  }

  void setFieldDirty(Number fieldId, boolean dirty)
  {
    if(dirty)
    {
      _dirtyFields.put(fieldId, Boolean.TRUE);
      _entityDirty = true;
    }
    else
    {
      _dirtyFields.put(fieldId, null);
    }
  }

  public boolean isEntityDirty()
  {
    return _entityDirty;
  }

  void setEntityDirty(boolean dirty)
  {
    _entityDirty = dirty;
    if(dirty == false)
    {
      _dirtyFields.clear();
    }
  }

  public boolean isFromCache()
  {
    return _fromCache;
  }

  void setFromCache(boolean fromCache)
  {
    _fromCache = fromCache;
  }

  /**
   * @deprecated - use getUid()
   */
  public final long getUID()
  {
    return getUidLong().longValue();
  }

  public final long getUid()
  {
    Long uid = getUidLong();
    if(uid == null) throw new java.lang.NullPointerException("Uid is null for entity " + this);
    return uid.longValue();
  }

  public final Long getUidLong()
  {
    if(_uidFieldId == null)
    {
      throw new java.lang.IllegalStateException("This entity does not have a uid");
    }
    //_log.debug("[AbstractGTEntity.getUidLong] uidFieldId class = "+_uidFieldId.getClass().getName());
    try
    {
      return (Long)_fields.get(_uidFieldId);
    }
    catch(Throwable t)
    {
      if (_log.isWarnEnabled())
      {
        _log.warn("[AbstractGTEntity.getUidLong] Error getting UidLong from:"+_fields.get(_uidFieldId), t);
      }
      return null;
    }
  }

  void setSession(IGTSession session)
  {
    _session = session;
  }

  void setManager(DefaultAbstractManager manager)
  {
    _manager = manager;
  }

  void setType(String entityType)
  {
    _type = entityType;
  }

  public IGTSession getSession()
  {
    return _session;
  }

  public String getFieldName(Number fieldId) throws GTClientException
  {
    IGTFieldMetaInfo fmi = getFieldMetaInfo(fieldId);
    if(fmi == null)
    {
      throw new java.lang.IllegalArgumentException("Field id " + fieldId + " does not correspond to a valid field");
    }
    return (String)fmi.getFieldName();
  }

  public Number getFieldId(String fieldName) throws GTClientException
  {
    Number fieldId = _manager.getFieldId(_type,fieldName); //20021025AH
    if(fieldId == null) throw new java.lang.IllegalArgumentException(this + " has no field named:" + fieldName);
    return fieldId;
  }

  public String getType()
  {
    return _type;
  }

  public void setFieldValue(String fieldName, Object value)
    throws GTClientException
  {
    if(!canEdit())
    {
      throw new java.lang.IllegalStateException("Entity is frozen and may not be modified:" + this);
    }
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("fieldName is null");
    }
    String superField = StaticCtrlUtils.extractSuperFieldName(fieldName);
    if(superField != null)
    {
      IGTEntity subEntity = (IGTEntity)getFieldValue(superField);
      if(subEntity == null)
      {
        throw new java.lang.NullPointerException("field " + superField + " is null");
      }
      String subField = StaticCtrlUtils.extractSubFieldName(fieldName);
      subEntity.setFieldValue(subField,value);
    }
    else
    {
      Number fieldId = getFieldId(fieldName);
      setFieldValue(fieldId, value);
    }
  }

  public void setFieldValue(Number field, Object value)
    throws GTClientException
  {
    if(!canEdit())
    {
      throw new IllegalStateException("Entity is frozen and may not be modified:" + this);
    }
    IGTFieldMetaInfo fmi = getFieldMetaInfo(field);
    if(fmi == null)
    {
      throw new IllegalArgumentException("Field (" + field + ") of "
          + getType() + "does not exist");
    }
    if(fmi.isEditable(this._isNewEntity) != true)
    {
      if(_log.isWarnEnabled())
      {
        _log.warn("Attempt made to write to non-editable field "
                  + field + " of entity " + this + " with value " + value);
      }
      return;
    }
    if(!isLegalValue(field,value))
    { //20030117AH
      throw new IllegalArgumentException("Not a legal value for "
          + getFieldName(field));
    }
    Object oldValue = _fields.get(field); //20030117AH
    value = fieldValueChanging(field, fmi, value, oldValue); //20030117AH
    if(value != oldValue) //instance comparison (as opposed to .equals)
    {
      if(value == null)
      {
        _fields.remove(field);
      }
      else
      {
        _fields.put(field, value);
      }
      setFieldDirty(field, true);
      fieldValueChanged(field, fmi, value, oldValue); //20030509AH
    }
  }

  /**
   * Subclass may override, to intercept and modify or prevent a field value modification.
   * Called only if value will be changed (ie: non-editable check etc.. passed)
   * If returns oldValue (compared with ==) will not change fields value or mark field dirty
   * Subclass should delegate back to superclass if special handling not required for a
   * particular field.
   * @param fieldId the id of the field being changed
   * @param fmi the FieldMetaInfo for the field
   * @param newValue the value to which the field is changing
   * @param oldValue the current value of the field
   * @return value to change the field to
   * @throws GTClientException
   */
  protected Object fieldValueChanging(Number fieldId,
                                      IGTFieldMetaInfo fmi,
                                      Object newValue,
                                      Object oldValue)
    throws GTClientException
  { //20030117AH
    return newValue;
  }
  
  /**
   * Subclass may override, to perform extra changes when something is changed.
   * (An example of this is the
   * handling of the RECIPIENTS and ALERT_RECIPIENTS fields in an IGTAlertTriggerEntity)
   * nb: Is not called if the field value wasnt actually set (ie: fieldValueChanging returned oldValue)
   * 
   * @param fieldId the id of the field changed
   * @param fmi the FieldMetaInfo for the field
   * @param newValue the value to which the field changed (as returned by fieldValueChanging)
   * @param oldValue the previous value of the field
   * @throws GTClientException
   */
  protected void fieldValueChanged(Number fieldId,
                                    IGTFieldMetaInfo fmi,
                                    Object newValue,
                                    Object oldValue)
    throws GTClientException
  { //20030509AH
    //default action is to do nothing
  }

  /**
   * Subclass may override but should remember to delegate back to superclass for default handling
   * where required.
   * @todo: implement code here in AbstractGTEntity to test datatypes, enums etc...
   * Currently always returns true (unless overridden by subclass)
   * @param fieldId
   * @param value value to be checked
   * @returns true if this value is valid for the field
   * @throws GTClientException
   */
  public boolean isLegalValue( Number fieldId,
                                Object newValue)
    throws GTClientException
  { //20030117AH
    return true; //@todo: check data types, enums, etc... for validity
  }

  void setUidFieldId(Number fieldId)
  {
    _uidFieldId = fieldId;
  }

  /**
   * For use within package only to set a field value without checking against metadata
   * or dirtying the field.
   * 20020620AH
   */
  void setNewFieldValue(Number field, Object value)
  {
    _fields.put(field,value);
  }

  /**
   * Internal use only.
   */
  Map getInternalFieldMap()
  {
    return _fields;
  }

  public Object getFieldValue(Number field)
    throws GTClientException
  {
    Object o = _fields.get(field);

    if(o == null)
    { //20020911AH
      if(isFromCache() && !getFieldMetaInfo(field).isAvailableInCache())
      {
        throw new java.lang.UnsupportedOperationException("Field "
          + field + " not available in cached entity " + this);
      }
    }

    if(o instanceof UnloadedFieldToken)
    { // Process request for value of load-on-demand field
      _manager.loadField(field,this);
      o = _fields.get(field);
    }
    return o;
  }

  public Object getFieldValue(String fieldName)
    throws GTClientException
  {
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("fieldName is null");
    }
    String superField = StaticCtrlUtils.extractSuperFieldName(fieldName);
    if(superField != null)
    {
      IGTEntity subEntity = (IGTEntity)getFieldValue(superField);
      if(subEntity == null)
      {
        return null;
      }
      String subField = StaticCtrlUtils.extractSubFieldName(fieldName);
      return subEntity.getFieldValue(subField);
    }
    else
    {
      Number fieldId = getFieldId(fieldName);
      return getFieldValue(fieldId);
    }
  }

  public String getFieldString(Number field)
    throws GTClientException
  {
    Object o = getFieldValue(field);
    if(o instanceof FormFileElement[])                                  // 08102002 DDJ
      return (String)getFileNamesForEvent((FormFileElement[])o, false); // 08102002 DDJ
    return StaticUtils.stringValue(o);
  }

  public String getFieldString(String fieldName)
    throws GTClientException
  {
    Object o = getFieldValue(fieldName);
    if(o instanceof FormFileElement[])                                  // 08102002 DDJ
      return (String)getFileNamesForEvent((FormFileElement[])o, false); // 08102002 DDJ
    return StaticUtils.stringValue(o);
  }

  public String[] getFieldStringArray(Number field)
    throws GTClientException
  {
    Object o = getFieldValue(field);
    if(o instanceof FormFileElement[])                                    // 08102002 DDJ
      return (String[])getFileNamesForEvent((FormFileElement[])o, true);  // 08102002 DDJ
    return StaticUtils.stringArrayValue(o);
  }

  public String[] getFieldStringArray(String fieldName)
    throws GTClientException
  {
    Object o = getFieldValue(fieldName);
    if(o instanceof FormFileElement[])                                    // 08102002 DDJ
      return (String[])getFileNamesForEvent((FormFileElement[])o, true);  // 08102002 DDJ
    return StaticUtils.stringArrayValue(o);
  }

  private Object getFileNamesForEvent(FormFileElement[] formFileElements, boolean isCollection) // 08102002 DDJ
  {
    // Single: return a String of either "" to keep existing, "filename" to replace, "/" to delete existing (for optional fields only)
    // Multi: return a String[] with "filename" to add a new file, "/filename" to delete existing file, empty collection for no changes
    Vector fileNamesForEvent = new Vector();

    for(int i = 0; i < formFileElements.length; i++)
    {
      if(formFileElements[i].isToUpload())
      {
        fileNamesForEvent.add(formFileElements[i].getUploadedFilename());
      }
      else if(formFileElements[i].isToDelete())
      {
        if(isCollection)
        {
          fileNamesForEvent.add("/" + formFileElements[i].getFileName());
        }
        else
        {
          if(formFileElements.length == 1)
          {
            fileNamesForEvent.add("/");
          }
        }
      }
      else
      {
        if(formFileElements.length == 1)
        {
          fileNamesForEvent.add("");
        }
      }
    }
    if(isCollection)
    {
      return StaticUtils.getStringArray(fileNamesForEvent);
    }
    else
    {
      return (String)fileNamesForEvent.firstElement();
    }
  }

  public IGTFieldMetaInfo[] getFieldMetaInfo() throws GTClientException
  {
    return _manager.getSharedFieldMetaInfo(_type);
  }

  public IGTFieldMetaInfo getFieldMetaInfo(String fieldName)
    throws GTClientException
  {
    try
    {
      if(fieldName == null)
      {
        throw new java.lang.NullPointerException("fieldName is null");
      }
      String superField = StaticCtrlUtils.extractSuperFieldName(fieldName);
      if(superField != null)
      {
        IGTEntity subEntity = (IGTEntity)getFieldValue(superField);
        String subField = StaticCtrlUtils.extractSubFieldName(fieldName);
        return subEntity.getFieldMetaInfo(subField);
      }
      else
      {
        Number fieldId = getFieldId(fieldName);
        IGTFieldMetaInfo fmi = getFieldMetaInfo(fieldId);
        return fmi;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get metaInfo for field '" + fieldName + "'",t);
    }
  }

  public IGTEntity getFieldContainer(String fieldName)
    throws GTClientException
  {
    try
    {
      if(fieldName == null)
      {
        throw new java.lang.NullPointerException("fieldName is null");
      }
      String superField = StaticCtrlUtils.extractSuperFieldName(fieldName);
      if(superField != null)
      {
        IGTEntity subEntity = (IGTEntity)getFieldValue(superField);
        String subField = StaticCtrlUtils.extractSubFieldName(fieldName);
        return subEntity.getFieldContainer(subField);
      }
      else
      {
        return this;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get metaInfo for field '" + fieldName + "'",t);
    }
  }

  public IGTEntity getFieldContainer(Number fieldId)
    throws GTClientException
  {
    if(getFieldMetaInfo(fieldId) == null)
    {
      return null;
    }
    else
    {
      return this;
    }
  }

  public IGTFieldMetaInfo getFieldMetaInfo(Number field)
    throws GTClientException
  {
      return _manager.getSharedFieldMetaInfo(_type,field); //20021025AH
  }

  public boolean isNewEntity()
  {
    return _isNewEntity;
  }

  void setNewEntity(boolean isNew) throws GTClientException
  {
    try
    {
      _isNewEntity = isNew;
      //20021116AH - Recursively set embedded entities to new also
      //IGTFieldMetaInfo[] fmi = _manager.getSharedFieldMetaInfo(this.getType());
      //IGTFieldMetaInfo[] fmi = (IGTFieldMetaInfo[])_manager._fieldMetaInfo.get(this.getType()); //20021126AH
      IGTFieldMetaInfo[] fmi = _manager.getSharedFieldMetaInfo(this.getType()); //20030522AH (nowdays it includes vfmi)
      if (fmi == null) //20030522AH
        throw new NullPointerException("fmi is null");
      for(int i=0; i < fmi.length; i++)
      {
        int constraintType = fmi[i].getConstraintType();
        if( (constraintType == IConstraint.TYPE_LOCAL_ENTITY)
            || (constraintType == IConstraint.TYPE_DYNAMIC_ENTITY) )
        {
          if(fmi[i].isCollection())
          {
            Collection children = (Collection)getFieldValue(fmi[i].getFieldId());
            if(children != null)
            {
              if(!children.isEmpty())
              {
                Iterator iterator = children.iterator();
                while(iterator.hasNext())
                {
                  AbstractGTEntity child = (AbstractGTEntity)iterator.next();
                  if(child != null)
                  {
                    child.setNewEntity(isNew);
                  }
                }
              }
            }
          }
          else
          {
            AbstractGTEntity child = (AbstractGTEntity)getFieldValue(fmi[i].getFieldId());
            if(child != null)
            {
              child.setNewEntity(isNew);
            }
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error setting newEntity flag to " + isNew
                                  + " for entity " + this,t);
    }
  }

  /*void setFields(Map fields)
  {
    try
    {
      _fields = fields;
      dumpFields(fields);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }*/

  public final String toString()
  {
    String uid = "none";
    try
    {
      uid = "" + getUidLong();
    }
    catch(Throwable t)
    {
    }
    return this.getClass().getName() + "(" + getType() + ")uid=" + uid;
  }

  public Collection getFieldEntities(Number fieldId)
    throws GTClientException
  {
    IGTFieldMetaInfo fmi = getFieldMetaInfo(fieldId);
    boolean isCollection = fmi.isCollection();
    Collection entities = null;
    if(!isCollection)
    {
      entities = new Vector();
    }
    int constraintType = fmi.getConstraintType();
    switch(constraintType)
    {
      case IConstraint.TYPE_FOREIGN_ENTITY:
      {
        IForeignEntityConstraint fec = (IForeignEntityConstraint)fmi.getConstraint();
        if(fec.isCached())
        {
          if(isCollection)
          {
            entities = (Collection)_cachedForeignEntities.get(fieldId);
          }
          else
          {
            Object singleEntity = _cachedForeignEntities.get(fieldId);
            if(singleEntity != null)
            {
              ((Vector)entities).add(singleEntity);
            }
          }
        }
        else // else not cached
        {
          try
          {
            entities = new Vector();
            String foreignType = fec.getEntityType();
            String keyFieldName = fec.getKeyFieldName();
            int managerType = _session.getManagerType(foreignType);
            IGTManager manager = _session.getManager(managerType);
            Number keyFieldId = manager.getFieldId(foreignType,keyFieldName );
            Object key = getFieldValue(fieldId);
            if(key == null)
            { //20020924AH
              return entities;
            }
            if(isCollection)
            {
              Iterator keys = ((Collection)key).iterator();
              int totalKeys = ((Collection)key).size(); //20030515AH
              int kCount = 0; //20030515AH
              while(keys.hasNext())
              {
                Object thisKey = keys.next();
                if(thisKey == null)
                { //20030515AH - Internal sanity check failed. Give detailed info in exception
                  throw new NullPointerException( "Non cached FER key value at position "
                                                  + kCount
                                                  + " in collection of "
                                                  + totalKeys 
                                                  + " keys in field "
                                                  + fieldId
                                                  + " of entity:"
                                                  + this
                                                  + " is null!");
                }
                Collection c =  manager.getByKey(thisKey,keyFieldId);
                Iterator i = c.iterator();
                while(i.hasNext())
                { // In theory each key could refer to several fers so copy all of them into
                  // results collection
                  Object next = i.next();
                  ((Vector)entities).add( next );
                }
                kCount++; //20030515AH
              }
            }
            else // else not collection
            {
              Collection c =  manager.getByKey(key,keyFieldId);
              Iterator i = c.iterator();
              while(i.hasNext())
              {
                ((Vector)entities).add( i.next() );
              }
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException(this + " encountered error dereferencing non-cached "
                                            + "foreign entity reference for fieldId=" + fieldId,t);
          }
        }
      }break;

      case IConstraint.TYPE_LOCAL_ENTITY:
      case IConstraint.TYPE_DYNAMIC_ENTITY: //20021031AH
      {
        if(isCollection)
        {
          entities = (Collection)getFieldValue(fieldId);
        }
        else
        {
          //20021031AH - Always return a collection even for null single value
          Object singleEntity = getFieldValue(fieldId);
          ((Vector)entities).add(singleEntity);
        }
      }break;

      default:
      {
        throw new java.lang.IllegalArgumentException("Field " + fieldId + " does not contain or refer to entities");
      }
    }
    return entities;
  }

  void addCachedForeignEntity(Number fieldId, AbstractGTEntity entity)
  {
    _cachedForeignEntities.put(fieldId, entity);
  }

  void addCachedForeignEntity(Number fieldId, Collection entities)
  {
    _cachedForeignEntities.put(fieldId, entities);
  }

  public boolean isVirtualEntity()
  {
    return _manager.isVirtual(getType());
  }
}