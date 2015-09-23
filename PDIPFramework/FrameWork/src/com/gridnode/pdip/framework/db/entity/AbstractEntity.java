/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Jun 18 2001    Neo Sok Lay         Handle List type fields.
 * Sep 05 2001    Liu Dan             Handle IDataFilter type fields.
 * Nov 08 2001    Qingsong            Add read and write
 * Jan 24 2002    Mahesh              Commented IFile Conversion
 * Apr 12 2002    Neo Sok Lay         Remove extra line breaks.
 *                                    Change getFieldValue() to return field
 *                                    value even if the fieldId is not in
 *                                    currentIds list.
 * Apr 24 2002    Neo Sok Lay         Add getFieldValueMap() to return values
 *                                    of a list of fields in a HashMap.
 * Apr 29 2002    Mahesh              Changed getMetaInfo() method,pass entityName
 *                                    insted of class name to retrive metainfo from
 *                                    metainfo factory.
 *                                    Set the _metaInfoFactory in getMetaInfoFactory
 *                                    if it is null.
 * May 07 2002    Neo Sok Lay         Change convert() to instance methods so that
 *                                    subclasses can extend to convert specific
 *                                    data types.
 * May 23 2002    Neo Sok Lay         Undo the change above.
 * Jun 04 2002    Neo Sok Lay         Add convertToBytes() for serializing
 *                                    object to byte[].
 * Jun 04 2002    Ang Meng Hua        Add _canDelete attribute and the
 *                                    getter/setter methods.
 * Jun 12 2002    Ang Meng Hua        Change all reference to getName() to getEntityName()
 * Jul 08 2002    Neo Sok Lay         GNDB00008872: Check for converted
 *                                    filter is null in convertToFilter().
 * Aug 02 2002    Neo Sok Lay         Allow conversion of fields to a Collection
 *                                    type.
 * Sep 16 2002    Neo Sok Lay         Allow conversion of byte[] to Object by
 *                                    object deserialization.
 * Sep 24 2002    Neo Sok Lay         Bug fix for field conversion:
 *                                    Hashtable<->String. Change of format by
 *                                    making use of Properties.
 *                                    Add conversion Properties<->String.
 * Sep 27 2002   Jagadeesh            Add conversion Map to Entity.
 * Oct 11 2002   Neo Sok Lay          Add conversion DataObject<->String through
 *                                    Object-Xml (De)serialization.
 * Dec 11 2002   Neo Sok Lay          Use MetaInfoFactory instead of EntityMetaInfoLoader.
 * Jan 10 2003   Neo Sok Lay          Add conversion from String to Vector.
 * Jan 20 2003   Jagadeesh            Modified:-conversionMap to Entity to get Fieldname.
 * Mar 28 2003   Neo Sok Lay          Hard-code serialVersionUID for compatibility
 *                                    with serialized objects using JDK1.3 and
 *                                    compiled entities using JDK1.4.
 * Sep 01 2003   Neo Sok Lay          Modify convertToMap() to convert embedded "Collection" of
 *                                    entities instead of "List" in order to take care of those
 *                                    entities that don't use implementations of "List". 
 * Jun 16 2004   Mahesh               Allow conversion of byte[] to String.
 * Sep 21 2005   Tam Wei Xiang        Enhance the debugging info for method getFieldValue()
 * 				      and getObjectField()
 * Dec 30 2005   Tam Wei Xiang        Modified method convert(Object, String) to support 
 *                                    conversion from/to DataObjectList.
 * Mar 03 2006   Neo Sok Lay          Allow conversion from Collection type to DataObjectList  
 * Jun 13 2006   Tam Wei Xiang        If the toClass type is java.lang.Object, then in some 
 *                                    situation, we are not able to convert correctly. 
 *                                    Eg GWFRtProcessDoc's reason we get from RS is in byte[]
 *                                    type and we need to perform deserialization; Thus we will do
 *                                    deserialize while the type of the value we need to convert is
 *                                    byte[] and the toClass type is 'ObjectSER'  
 * Jan 30 2007   Chong SoonFui        Modified method convert(Object, String) to support 
 *                                    conversion from/to java.math.BigDecimal.
 * Feb 09 2007   Neo Sok Lay          Change getFieldValue(): Do not printStackTrace() when unable to 
 *                                    find fieldmetainfo.                                                                                                         
 */

package com.gridnode.pdip.framework.db.entity;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import com.gridnode.pdip.framework.db.DataFilterHandler;
import com.gridnode.pdip.framework.db.DataObject;
import com.gridnode.pdip.framework.db.DataObjectList;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.IMetaInfoFactory;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.util.ReflectionUtility;

/**
 * This is an abstract implementation of a data entity Value Object.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 1.0a build 0.9.9.6
 */

public abstract class AbstractEntity
  extends       DataObject
  implements    IEntity, Serializable
{
  private static final long serialVersionUID = 6947106074616756679L;

  private static IMetaInfoFactory _metaInfoFactory;

  private boolean _isComplete = false;
  protected boolean _canDelete = true;
  private Vector _currentIds = new Vector();

  public AbstractEntity()
  {
    //290402MAHESH: Changed to set _metaInfoFactory in getMetaInfoFactory.
    //if(_metaInfoFactory==null)
    //    _metaInfoFactory=EntityMetaInfoLoader.getMetaInfoFactory();
  }

  public byte[] write(IEntity entity)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream objOS = new ObjectOutputStream(new BufferedOutputStream(baos));

      objOS.writeObject(entity);
      objOS.close();

      return baos.toByteArray();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  public IEntity read(byte[] entity)
  {
    try
    {
      ByteArrayInputStream bais = new ByteArrayInputStream(entity);
      ObjectInputStream objIS = new ObjectInputStream(new BufferedInputStream(bais));

      IEntity ret = (IEntity)objIS.readObject();
      objIS.close();

      return ret;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  public Object clone()
  {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;

    try
    {
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));

      objOS.writeObject(this);
      objOS.close();

      //read
      bais = new ByteArrayInputStream(baos.toByteArray());
      objIS = new ObjectInputStream(new BufferedInputStream(bais));
      Object ret = objIS.readObject();
      objIS.close();

      return ret;
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.clone]", ex);
      return null;
    }
    finally
    {
      try
      {
        if (objOS != null) objOS.close();
        if (objIS != null) objIS.close();
      }
      catch (Exception ex) {}
    }
  }

  /**
   * Set the available fields in the entity according to the meta info.
   *
   * @param proxyOn <B>true</B> to make fields that are marked isProxy=true
   * inavailable. <B>false</B> to make all fields available.
   * @param encapEntity The entity that contains this entity. <B>null</B> if
   * none.
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setAvailableFields(boolean proxyOn, AbstractEntity encapEntity)
  {
    _currentIds.clear();

    FieldMetaInfo[] fieldMeta = getMetaInfo().getFieldMetaInfo();

    for (int i=0; i<fieldMeta.length; i++)
    {
      if (fieldMeta[i].getFieldId() == null)
        continue;

      if (proxyOn && fieldMeta[i].isProxy())
        continue;

      _currentIds.add(fieldMeta[i].getFieldId());

      Object field = getFieldValue(fieldMeta[i].getFieldId());

      if (field == null) continue;

      if (field instanceof AbstractEntity)
      {
        //embedded entity
        AbstractEntity embEntity = (AbstractEntity)field;

        if (embEntity.equals(encapEntity)) //reference loop
          setFieldValue(fieldMeta[i].getFieldId(), encapEntity);
        else
          embEntity.setAvailableFields(proxyOn, this);
      }

      if (field instanceof List)
      {
        List fieldV = (List)field;

        if (fieldV.size() == 0 || !(fieldV.get(0) instanceof AbstractEntity))
          continue;

        //embed entities in list
        for (int j=0; j<fieldV.size(); j++)
        {
          AbstractEntity embEntity = (AbstractEntity)fieldV.get(j);

          if (embEntity.equals(encapEntity))
            fieldV.set(j, encapEntity);
          else
            embEntity.setAvailableFields(proxyOn, this);
        }
      }
    }
  }

  /**
   * Compares this entity instance with the specified object.
   *
   * @param obj The object to compare with.
   * @return <B>true</B> iff <I>obj</I> is of IEntity type and
   * entity names are the same and either keys are equal or all other keys are
   * equal if the keys are both null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public boolean equals(Object obj)
  {
    if (obj == null || !(obj instanceof IEntity))
      return false;

    if(obj == this)
      return true;

    IEntity other = (IEntity)obj;

    if (getClass().getName().equals(other.getClass().getName()))
    {
      if (getKey() == null && other.getKey() == null)
      {
        Enumeration ids = getFieldIds();

        while (ids.hasMoreElements())
        {
          Number id = (Number)ids.nextElement();
          Object val = getFieldValue(id);

          if (val != null && val.equals(other.getFieldValue(id)))
            continue;

          if (val == other.getFieldValue(id))
            continue;

          return false;
        }
        return true;
      }

      if (getKey() == null)
        return false;

      return (getKey().equals(other.getKey()));
    }

    return false;
  }

  public EntityMetaInfo getMetaInfo()
  {
    try
    {
      //290402MAHESH: Changed to pass entityName insted of Class name
      //return _metaInfoFactory.getMetaInfoFor(getClass().getEntityName());
      return getMetaInfoFactory().getMetaInfoFor(getEntityName());
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.getMetaInfo] "+getMetaInfoFactory(), ex);
      return null;
    }
  }

  public Object getKey()
  {
    return getFieldValue(getKeyId());
  }



  public Object getFieldValue(Number fieldId)
  {
    FieldMetaInfo fieldMeta = getFieldMetaInfo(fieldId);

    if (fieldMeta == null)
    {
      //added 21 Sep 2005 by TWX
      //Exception ex = new Exception("Cannot find field meta info for field ID"+ fieldId);
      err("[AbstractEntity.getFieldValue] unable to find field meta info for field ID "+fieldId+" for entity "+getEntityName(), null);
      return null;
    }

    //120402NSL: Return field value even though not in currentId list
    //if (_currentIds.contains(fieldId))
    //{
      return getObjectField(fieldMeta);
    //}

    //return null;
  }

  /**
   * Simple check if the list contains AbstractEntity object. Assumes that
   * the objects in the list is of homogeneous type.
   *
   * @param entities list of objects to check.
   * @return <B>true</B> if first element of the list is an AbstractEntity,
   * <B>false</B> otherwise.
   */
  public static boolean isEntityList(List entities)
  {
    return (!entities.isEmpty() &&
        (entities.get(0) instanceof AbstractEntity));
  }

  /**
   * Simple check if the collection contains AbstractEntity object. Assumes that
   * the objects in the collection is of homogeneous type.
   *
   * @param entities Collection of objects to check.
   * @return <B>true</B> if first element of the collection is an AbstractEntity,
   * <B>false</B> otherwise.
   */
  public static boolean isEntityCollection(Collection entities)
  {
    return (!entities.isEmpty() &&
        (entities.iterator().next() instanceof AbstractEntity));
  }

  /**
   * Convert an array of entities to a List of HashMaps containing the
   * fieldID:fieldValue pairs of the entity.
   *
   * @param entities The array of entities to convert.
   * @param entitiesFieldIds A table of
   * entityName(entity.getEntityName()):fieldIds(Number[]) indicating
   * the list of fieldIds against each type of entity for which to return the
   * fields in its HashMap. There may be embedded entities which an entity, thus
   * this Hashtable should contain the fieldIds for each possible type of entity.
   * @param encapEntities A table of entity:hashMap. This is used for resolving
   * the cyclic reference. Pass in <B>null</B> for initial call.
   *
   * @return A List of HashMaps for each entity specified in the entities array.
   */
  public static List convertEntitiesToMap(
    AbstractEntity[] entities, Hashtable entitiesFieldIds, Hashtable encapEntities)
  {
    ArrayList list = new ArrayList();

    for (int i=0; i<entities.length; i++)
    {
      HashMap map = null;
      AbstractEntity embEntity = entities[i];

      if (encapEntities != null &&
          encapEntities.containsKey(embEntity))
      {
        map = (HashMap)encapEntities.get(embEntity);
      }
      else
      {
        map = convertToMap(embEntity, entitiesFieldIds, encapEntities);
      }
      list.add(i, map);
    }

    return list;
  }

  /**
   * Convert an entity to a HashMaps containing the
   * fieldID:fieldValue pairs of the entity.
   *
   * @param entity The entity to convert.
   * @param entitiesFieldIds A table of
   * entityName(entity.getEntityName()):fieldIds(Number[]) indicating
   * the list of fieldIds against each type of entity for which to return the
   * fields in its HashMap. There may be embedded entities which an entity, thus
   * this Hashtable should contain the fieldIds for each possible type of entity.
   * @param encapEntities A table of entity:hashMap. This is used for resolving
   * the cyclic reference. Pass in <B>null</B> for initial call.
   *
   * @return A HashMap for the specified entity.
   */
  public static HashMap convertToMap(
    AbstractEntity entity, Hashtable entitiesFieldIds, Hashtable encapEntities)
  {
    HashMap map = new HashMap();

    if (entity == null)
      return map;

    Number[] fieldIds = (Number[])entitiesFieldIds.get(entity.getEntityName());

    if (fieldIds != null)
    {
      if (encapEntities == null)
        encapEntities = new Hashtable();

      encapEntities.put(entity, map);
      for (int i=0; i<fieldIds.length; i++)
      {
        Object value = entity.getFieldValue(fieldIds[i]);
        //if (value instanceof List)
        if (value instanceof Collection)
        {
          //List entities = (List)value;
          Collection entities = (Collection)value;
          //if (isEntityList(entities))
          if (isEntityCollection(entities))
          {
            value = convertEntitiesToMap((AbstractEntity[])entities.toArray(
                      new AbstractEntity[entities.size()]),
                      entitiesFieldIds, encapEntities);
          }
        }
        else if (value instanceof AbstractEntity)
        {
          value = convertToMap((AbstractEntity)value, entitiesFieldIds, encapEntities);
        }
        map.put(fieldIds[i], value);
      }
    }
    return map;
  }
/**
 * This Method is essentially responsible to convert EntityField Value's represented
 * as HashMap of Key - Value pair to AbstractEntity.
 * Here Key is = FieldName - value in the FieldMetaInfo(Table-Database).
 *      Value =  The value need to be assigned to this key.
 *
 * EntityFieldIDs
 * ==============
 * Here Key is = EntityName - value in EntityMetaInfo(Table-Database).
 *      value =  Array of FieldID's represented in corresponding I<xxx>Entity for this
 *               Entity Model.
 *
 * Note:- Multiple Entities cannot be converted with this method. (To Be supported)
 *
 *  Entity------>     Entity to Convert
 *            |
 *            |
 *            |--->SubEntity1  (Convert This Entity and Return)
 *            |                              OR
 *            |--->SubEntity2   (Convert This Entity and Return)
 *
 * @param entitiesFieldIds - EntityFieldIds
 * @param entityFieldValues - EntityFieldValues
 * @return - AbstractEntity
 */

  public static AbstractEntity convertMapToEntity(Hashtable entitiesFieldIds,HashMap entityFieldValues)
  {
    AbstractEntity entity = null;

    if(entityFieldValues != null)
    {
        Enumeration enm = entitiesFieldIds.keys();
        String entityName = (String)enm.nextElement();
        Number[] fieldIds = (Number[])entitiesFieldIds.get(entityName);
        //** Let's retrieve EntityMetaInfo for this EntityName ***//
        EntityMetaInfo metaInfo= MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
        if(metaInfo == null)
          return null;
        String classname = metaInfo.getObjectName();
        try
        {
          String className = metaInfo.getObjectName();
          //*** Instantiate the Class for this Entity - as this class is what user want to instantiate ****//
          //**  No Issue **//
          entity =  (AbstractEntity)Class.forName(className).newInstance();
        }
        catch(Exception ex)
        {
          err("[AbstractEntity.convertMapToEntity - Cannot Instantiate Class -]"+classname, ex);
          return null;
        }
        if(entity != null)
        {
          for(int i=0;i<fieldIds.length;i++)
          {
            //** Get FieldMetaInfo for each field - Retrieve the Values using fieldMetaInfo's Name**//
            FieldMetaInfo fieldMetaInfo = entity.getFieldMetaInfo(fieldIds[i]);
            if(fieldMetaInfo != null)
            {
              //log("[AbstractEntity.convertMapToEntity - "+"FieldName: "+fieldMetaInfo.getFieldName());
              //log("[AbstractEntity.convertMapToEntity - "+"FieldValue: "+entityFieldValues.get(fieldMetaInfo.getFieldName()));
              //****** Ignore the field's whos EntityFieldValues are not set. **** //
              if(entityFieldValues.get(fieldIds[i]) != null)
              entity.setFieldValue(fieldIds[i],entityFieldValues.get(fieldIds[i]));
            }
          }
        }
    }

    return entity;
  }


  public FieldMetaInfo getFieldMetaInfo(Number fieldId)
  {
    EntityMetaInfo entityMeta = getMetaInfo();

    if (entityMeta != null)
    {
      return  entityMeta.findFieldMetaInfo(fieldId);
    }

    return null;
  }

  public void setFieldValue(Number fieldId, Object value)
  {
    FieldMetaInfo fieldMeta = getFieldMetaInfo(fieldId);

    if (fieldMeta == null)
    {
      return;
    }

    try
    {
      setObjectField(fieldMeta, value);

      if (!_currentIds.contains(fieldId))
        _currentIds.add(fieldId);
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.setFieldValue]", ex);
    }
  }

  public IEntity createNew()
  {
    try
    {
      AbstractEntity entity = getClass().newInstance();
      return entity;
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.createNew]", ex);
      return null;
    }
  }

  /**
   * Sets the key value of the entity.
   *
   * @param key The key to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setKey(Object key)
  {
    setFieldValue(getKeyId(), key);
  }

  public Enumeration getFieldIds()
  {
    return _currentIds.elements();
  }


  public boolean isComplete()
  {
    return _isComplete;
  }

  public boolean canDelete()
  {
    return _canDelete;
  }

  abstract public String getEntityName();

  /**
   * Get the fieldIds of all fields currently set in this data entity.
   *
   * @return fieldIds of all fields set in this entity as a Vector of Number.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Vector getCurrentIds()
  {
    return (Vector)_currentIds.clone();
  }

  /**
   * Sets whether the entity has all its persistent fields in place.
   *
   * @param complete <B>true</B> if entity has all persistent fields set.
   * <B>false</B> otherwise.
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setComplete(boolean complete)
  {
    _isComplete = complete;
  }

  public void setCanDelete(boolean canDelete)
  {
    _canDelete = canDelete;
  }

  /**
   * Modifies the value of a field.
   *
   * @param fieldMeta The meta info of the field to modify
   * @param value     The value to set in the field
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setObjectField(FieldMetaInfo fieldMeta, Object value)
  {
    Field f = ReflectionUtility.getAccessibleField(
                getClass(), fieldMeta.getObjectName());

    if (f == null) return;
    
    //12062006: TWX
    String toClass = getToClassType(fieldMeta, f);
    
    try
    {
      value = convert(value, toClass);
      f.set(this, value);
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.setObjectField] Unable to set field "+
        fieldMeta.getFieldName() + " with value "+value, ex);
    }
  }

  /**
   * Gets the value of a field.
   *
   * @param fieldMeta The meta info of the field to get
   * @return The value of the field with <I>fieldMeta</I> as its meta info.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getObjectField(FieldMetaInfo fieldMeta)
  {
    Field f = ReflectionUtility.getAccessibleField(
                getClass(), fieldMeta.getObjectName());

    if (f == null) 
    {
    	//added 21 Sep 2005 by TWX
    	Exception ex = new Exception("Cannot find field name "+fieldMeta.getFieldName()+" for "+getEntityName());
    	err("[AbstractEntity.getObjectField] unable to find field for entity name "+fieldMeta.getEntityName()+" for class "+getClass(),ex);
    	return null;
    }

    try
    {
      Object val = f.get(this);
      return convert(val, fieldMeta.getValueClass());
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.getObjectField] Unable to get field "+
        fieldMeta.getFieldName(), ex);
      return null;
    }
  }

  public IMetaInfoFactory  getMetaInfoFactory()
  {
    if(_metaInfoFactory==null)
      _metaInfoFactory = MetaInfoFactory.getInstance();
    return _metaInfoFactory;
  }

  public void setMetaInfoFactory(IMetaInfoFactory metaInfoFactory)
  {
    _metaInfoFactory = metaInfoFactory;
  }

  /**
   * Performs validation of the object with desired types.
   * Convert when necessary.
   * @param obj The object to validated
   * @param toClass Full name of the class type that <I>obj</I> should be
   * <BR>Currently support conversions include:
   * <UL>java.lang.Object -> (string) -> java.lang.String</UL>
   * <UL>java.lang.String | java.util.Date | java.sql.Date | java.sql.Timestamp
   * -> (date | timestamp) -> java.sql.Timestamp</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal ->
   * (int | integer) -> java.lang.Integer</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal ->
   * (short) -> java.lang.Short</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal -> 
   * (byte) -> java.lang.Byte</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal -> 
   * (long) -> java.lang.Long</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal -> 
   * (float) -> java.lang.Float</UL>
   * <UL>java.lang.String | java.lang.Number | java.lang.Boolean[01] | java.math.BigDecimal -> 
   * (double) -> java.lang.Double</UL>
   * <UL>java.util.Collection | object[] -> (vector) -> java.util.Vector</UL>
   * <UL>byte[] -> (byte[]) -> byte[]</UL>
   * <UL>java.util.Hashtable | java.lang.String -> (hashtable)
   * -> java.util.Hashtable</UL>
   * <UL>com.gridnode.admin.IFile -> (ifile) -> com.gridnode.admin.IFile</UL>
   * <UL>java.util.List | java.lang.String -> (list|numberlist) -> java.util.List</UL>
   * <UL>java.lang.Number | java.lang.Boolean[01] -> (BigDecimal) -> java.math.BigDecimal</UL>
   * @exception Exception Conversion not supported
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Object convert(Object obj, String toClass)
    throws Exception
  {
    if (obj == null) return null;

    if (isInstanceOf(obj, toClass))
      return obj;

    String toClassLC = toClass.toLowerCase();

    //no conversion required if object is already in toClass
    //special handling for date types
    if (obj.getClass().getName().equals(toClass) &&
        !toClassLC.endsWith("date") && !toClassLC.endsWith("timestamp")){
      return obj;
    }
    
    if (obj instanceof byte[])
    {
      if (toClass.endsWith("byte[]"))
        return obj;
      //else if (toClassLC.endsWith("string"))
      //  return new String((byte[])obj);
      else if("ObjectSER".equalsIgnoreCase(toClassLC)) //TWX 13062006
      {
      	return convertToObject((byte[])obj);
      }
      else
        return convertToObject((byte[])obj);
    }

    //java.util.Hashtable | java.util.List | com.gridnode.db.filter.IDataFilter |
    //java.lang.Object -> java.lang.String
    if (toClassLC.endsWith("string"))
    {
      if (obj instanceof Properties)
        return convertToString((Properties)obj);
      else if (obj instanceof Hashtable)
        return convertToString((Hashtable)obj);
      else if(obj instanceof DataObjectList)
      	return convertToString((DataObjectList)obj);
      else if (obj instanceof List)
        return convertToString((List)obj);
      else if (obj instanceof IDataFilter)
        return convertToString((IDataFilter)obj);
      else if (obj instanceof DataObject)
        return convertToString((DataObject)obj);

      return obj.toString();
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String -> java.lang.Byte
    if (toClassLC.endsWith("byte"))
    {
      if (obj instanceof String)
        return convertToByte((String)obj);
      else if (obj instanceof Number)
        return convertToByte((Number)obj);

      else if (obj instanceof Boolean)
        return convertToByte((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String ->
    //  java.lang.Integer
    if (toClassLC.endsWith("integer") || toClassLC.endsWith("int"))
    {
      if (obj instanceof String)
        return convertToInteger((String)obj);
      else if (obj instanceof Number)
        return convertToInteger((Number)obj);
      else if (obj instanceof Boolean)
        return convertToInteger((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String -> java.lang.Short
    if (toClassLC.endsWith("short"))
    {
      if (obj instanceof String)
        return convertToShort((String)obj);
      else if (obj instanceof Number)
        return convertToShort((Number)obj);
      else if (obj instanceof Boolean)
        return convertToShort((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String -> java.lang.Long
    if (toClassLC.endsWith("long"))
    {
      if (obj instanceof String)
        return convertToLong((String)obj);
      else if (obj instanceof Number)
        return convertToLong((Number)obj);
      else if (obj instanceof Boolean)
        return convertToLong((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String -> java.lang.Float
    if (toClassLC.endsWith("float"))
    {
      if (obj instanceof String)
        return convertToFloat((String)obj);
      else if (obj instanceof Number)
        return convertToFloat((Number)obj);
      else if (obj instanceof Boolean)
        return convertToFloat((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.Boolean | java.lang.String ->
    //  java.lang.Double
    if (toClassLC.endsWith("double"))
    {
      if (obj instanceof String)
        return convertToDouble((String)obj);
      else if (obj instanceof Number)
        return convertToDouble((Number)obj);
      else if (obj instanceof Boolean)
        return convertToDouble((Boolean)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.Number | java.lang.String -> java.lang.Boolean
    //OR java.math.BigDecimal -> java.lang.Boolean
    if (toClassLC.endsWith("boolean"))
    {
      if (obj instanceof String)
        return convertToBoolean((String)obj);
      else if (obj instanceof Number)
        return convertToBoolean((Number)obj);
      else if (obj instanceof Boolean)
        return obj;
      else if (obj instanceof BigDecimal)
        return convertToBoolean((BigDecimal)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.util.Collection | java.lang.Object[] -> java.util.Vector
    if (toClassLC.endsWith("vector"))
    {
      if (obj instanceof String)
      {
        return convertToVector((String)obj);
      } 

      if (obj instanceof Collection)
        return convertToVector((Collection)obj);
      else if (obj instanceof Object[])
        return convertToVector((Object[])obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //byte[] -> byte[] | java.lang.Object -> byte[]
    if (toClassLC.endsWith("byte[]"))
    {
      if (obj instanceof byte[])
        return obj;

      return convertToBytes(obj);

//      throw new Exception(
//            obj.getClass().getName() + " to "+toClass +
//            " conversion not supported");
    }

    //java.util.Date -> java.sql.Timestamp
    if (toClassLC.endsWith("date") || toClassLC.endsWith("timestamp"))
    {
      if (obj instanceof Date)
        return convertToTimestamp((Date)obj);
      if (obj instanceof String)
        return convertToTimestamp((String)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.String -> java.util.Properties
    if (toClassLC.endsWith("properties"))
    {
      if (obj instanceof String)
        return convertToProperties((String)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //java.lang.String -> java.util.Hashtable
    if (toClassLC.endsWith("hashtable"))
    {
      if (obj instanceof String)
        return convertToTable((String)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //com.gridnode.admin.IFile -> com.gridnode.admin.IFile
/*    if (toClassLC.endsWith("ifile"))
    {
      if (obj instanceof com.gridnode.pdip.admin.IFile)
        return obj;

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }
*/
    
    if(toClassLC.endsWith("dataobjectlist"))
    {
    	if(obj instanceof String)
    	{
    		return convertToList((String)obj);
    	}
    	else if (obj instanceof Collection) //NSL20060303
    	{
    		return new DataObjectList((Collection)obj);
    	}
    }
    
    //java.lang.String | java.util.Collection | java.lang.Object[] -> java.util.List
    if (toClassLC.endsWith("list") || toClassLC.endsWith(".collection"))
    {
      if (obj instanceof String)
      {
        return convertToList((String)obj, (toClassLC.indexOf("number") != -1));
      }

      if (obj instanceof Collection)
        return convertToList((Collection)obj);
      else if (obj instanceof Object[])
        return convertToList((Object[])obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }

    //com.gridnode.db.filter.IDataFilter -> com.gridnode.db.filter.IDataFilter
    if (toClassLC.endsWith("idatafilter"))
    {
      if (obj instanceof com.gridnode.pdip.framework.db.filter.IDataFilter)
        return obj;

      if (obj instanceof String)
        return convertToFilter((String)obj);

      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }
    
    //13062006 no convertion is required
    if (toClassLC.equalsIgnoreCase("ObjectSER"))
    {
    	return obj;
    }
    
    if (obj instanceof String)
    {
      if (isAssignableFrom(DataObject.class, toClass))
        return convertToDataObject((String)obj, toClass);
    }
    
    //java.lang.Boolean  -> java.math.BigDecimal 
    if (toClassLC.endsWith("bigdecimal"))
    {
      if (obj instanceof Boolean)
        return convertToBigDecimal((Boolean)obj);
      else if (obj instanceof Short)
      	return convertToBigDecimal((Short)obj);
      else if (obj instanceof Integer)
      	return convertToBigDecimal((Integer)obj);
      else if (obj instanceof Long)
      	return convertToBigDecimal((Long)obj);
      else if (obj instanceof Double)
      	return convertToBigDecimal((Double)obj);
      else if (obj instanceof Float)
      	return convertToBigDecimal((Float)obj);
      
      throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
    }    

    throw new Exception(
            obj.getClass().getName() + " to "+toClass +
            " conversion not supported");
  }

  public static boolean isInstanceOf(Object obj, String className)
  {
    boolean instanceOf = false;
    try
    {
      Class checkClass = Class.forName(className);
      instanceOf = (checkClass.isInstance(obj));
    }
    catch (Exception ex)
    {

    }
    return instanceOf;
  }

  public static boolean isAssignableFrom(Class assignableClass, String className)
  {
    boolean assignable = false;
    try
    {
      Class checkClass = Class.forName(className);
      assignable = (assignableClass.isAssignableFrom(checkClass));
    }
    catch (Exception ex)
    {

    }
    return assignable;
  }

  public static byte[] convertToBytes(Object obj)
  {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;

    try
    {
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));

      objOS.writeObject(obj);
      objOS.close();

      //return the object in byte content
      return baos.toByteArray();
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.convertToBytes]", ex);
      return null;
    }
    finally
    {
      try
      {
        if (objOS != null) objOS.close();
      }
      catch (Exception ex) {}
    }
  }

  public static Object convertToObject(byte[] bytes)
  {
    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;

    Object obj = null;
    try
    {
      //read
      bais = new ByteArrayInputStream(bytes);
      objIS = new ObjectInputStream(new BufferedInputStream(bais));

      obj = objIS.readObject();
      objIS.close();

      //return the object in byte content
      return obj;
    }
    catch (Exception ex)
    {
      err("[AbstractEntity.convertToObject]", ex);
      return null;
    }
    finally
    {
      try
      {
        if (objIS != null) objIS.close();
      }
      catch (Exception ex) {}
    }
  }

  /**
   * Converts a IDataFilter to a string:
   * @param filter The filer to convert
   * @return Converted string representation of the IDataFilter
   *
   * @since 1.1
   */
  public static String convertToString(IDataFilter filter)
  {

    log("Converting Datafilter to string:"+ filter.getFilterExpr());
    String str = DataFilterHandler.serializeToString(filter,filter.getFilterName());
    log("Converted String = "+str);

    return str;
//    return DataFilterHandler.serializeToString(filter,filter.getFilterName());
  }

  /**
   * Converts a string to a IDataFilter:
   * @param value The string to convert
   * @return Converted IDataFilter representation of the string
   *
   * @since 2.0
   */
  public static IDataFilter convertToFilter(String value)
  {
    log("Converting string to Datafilter:"+ value);
    IDataFilter filter = DataFilterHandler.deserializeFromString(value);

    log("Converted Filter = "+((filter==null)?"null":filter.getFilterExpr()));

    return filter;
  }

  /**
   * Converts a hashtable to a formatted string according to java Properties
   * format.
   *
   * @param table The hashtable to convert
   * @return Converted string representation of the hashtable
   *
   * @since 1.0a build 0.9.9.6
   * @see #convertToString(Properties)
   */
  public static String convertToString(Hashtable table)
  {
    /* 240902NSL Convert to string via Properties class
    Hashtable val = (Hashtable)table;
    Enumeration keys = val.keys();
    String paramStr = "";

    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      paramStr += key + "@" + val.get(key) + ";";
    }
    return paramStr;
    */

    Properties props = new Properties();
    Object[] keys = table.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      props.setProperty(keys[i].toString(), table.get(keys[i]).toString());
    }
    return convertToString(props);
  }

  /**
   * Converts a Properties to the java Properties format:
   * <CODE>key1</CODE>=<CODE>value1</CODE>\r\n<CODE>key2</CODE>=<CODE>value2</CODE>
   * \r\n<CODE>...</CODE>
   *
   * @param props The Properties to convert
   * @return Converted string representation of the Properties
   *
   * @since 2.0 I5
   */
  public static String convertToString(Properties props)
  {
    String s = "";
    if (!props.isEmpty())
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        props.store(baos, null);
        baos.close();
        s = baos.toString();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    return s;

  }

  /**
   * Converts a List to a formatted string:
   * <CODE>elem1</CODE>;<CODE>elem2</CODE>;<CODE>...</CODE>
   * <BR>The order of the elements are in their proper sequence as in the list.
   *
   * @param list The List to convert.
   * @return Converted string representation of the List
   *
   * @since 1.0b
   */
  public static String convertToString(List list)
  {
    Iterator iter = list.iterator();

    String listStr = ";";
    while (iter.hasNext())
    {
      Object elem = iter.next();
      listStr += elem + ";";
    }

    return listStr;
  }

  /**
   * Convert a DataObject to String object through object-xml serialization.
   *
   * @param obj The DataObject to convert.
   * @return The converted String representing the Xml format of the DataObject.
   */
  public static String convertToString(DataObject obj)
    throws Exception
  {
    StringWriter writer = new StringWriter();
    obj.serialize(writer);

    return writer.toString();
  }

  /**
   * Convert a String to DataObject through Xml-Object deserialization.
   *
   * @param objStr The String representing the xml format of the DataObject to
   * be converted to.
   * @param toClass The target Class of the DataObject.
   * @return The converted DataObject.
   */
  public static DataObject convertToDataObject(String objStr, String toClass)
    throws Exception
  {
    DataObject obj = (DataObject)Class.forName(toClass).newInstance();
    StringReader reader = new StringReader(objStr);
    obj = (DataObject)obj.deserialize(reader);

    return obj;
  }

  /**
   * Converts a string to java.lang.Byte type.
   *
   * @param str The string value to convert
   * @return converted Byte or <B>null</B> if <I>str</I> is not numeric
   * or does not fall between -128 to 127.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Byte convertToByte(String str)
  {
    try
    {
      return new Byte(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToByte]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Byte type. <I>num</I> may be truncated
   * if does not fall between -128 to 127.
   *
   * @param num The number value to convert
   * @return Converted Byte value
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Byte convertToByte(Number num)
  {
    return new Byte(num.byteValue());
  }

  /**
   * Converts a boolean value to java.lang.Byte type.
   *
   * @param bool The boolean value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Byte convertToByte(Boolean bool)
  {
    if (bool.booleanValue())
      return new Byte("1");
    return new Byte("0");
  }

  /**
   * Converts a string into java.lang.Integer type.
   *
   * @param str The string value to convert
   * @return converted Integer value, or <B>null</B> if <I>str</I> is non-numeric
   * or does not fall between -2147483648 to 2147483647.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Integer convertToInteger(String str)
  {
    try
    {
      return new Integer(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToInteger]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Integer type. <I>num</I> may be truncated
   * if does not fall between -2147483648 to 2147483647.
   *
   * @param num The number value to convert
   * @return Converted Integer value
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Integer convertToInteger(Number num)
  {
    return new Integer(num.intValue());
  }

  /**
   * Converts a boolean value to java.lang.Integer type.
   *
   * @param bool The boolean value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Integer convertToInteger(Boolean bool)
  {
    if (bool.booleanValue())
      return new Integer(1);
    return new Integer(0);
  }
  
  /**
   * Converts a BigDecimal to java.lang.Integer type. <I>dec</I> may be truncated
   * if does not fall between -2147483648 to 2147483647.
   *
   * @param dec The BigDecimal value to convert
   * @return Converted Integer value
   *
   * @since 4.1
   */
  public static Integer convertToInteger(BigDecimal dec)
  {
    return new Integer(dec.intValue());
  }

  /**
   * Converts a string into java.lang.Short type.
   *
   * @param str The string value to convert
   * @return converted Short value, or <B>null</B> if <I>str</I> is non-numeric
   * or does not fall between -32768 to 32767
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Short convertToShort(String str)
  {
    try
    {
      return new Short(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToShort]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Short type. <I>num</I> may be truncated
   * if does not fall between -32768 to 32767.
   *
   * @param num The number value to convert
   * @return Converted Short value
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Short convertToShort(Number num)
  {
    return new Short(num.shortValue());
  }

  /**
   * Converts a boolean value to java.lang.Short type.
   *
   * @param bool The boolean value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Short convertToShort(Boolean bool)
  {
    if (bool.booleanValue())
      return new Short("1");
    return new Short("0");
  }

  /**
   * Converts a BigDecimal value to java.lang.Short type.<I>dec</I> may be truncated
   * if does not fall between -32768 to 32767.
   *
   * @param dec The BigDecimal value to convert
   * @returns Converted Short value
   *
   * @since 4.1
   */
  public static Short convertToShort(BigDecimal dec)
  {
    return new Short(dec.shortValue());
  }
 
  /**
   * Converts a string into java.lang.Long type.
   *
   * @param str The string value to convert
   * @return converted Long value, or <B>null</B> if <I>str</I> is non-numeric
   * or does not fall between -2<SUP>63</SUP> to 2<SUP>63</SUP>-1.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Long convertToLong(String str)
  {
    try
    {
      return new Long(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToLong]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Long type. <I>num</I> may be truncated
   * if does not fall between -2<SUP>63</SUP> to 2<SUP>63</SUP>-1.
   *
   * @param num The number value to convert
   * @return Converted Long value
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Long convertToLong(Number num)
  {
    return new Long(num.longValue());
  }

  /**
   * Converts a boolean value to java.lang.Long type.
   *
   * @param bool The boolean value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Long convertToLong(Boolean bool)
  {
    if (bool.booleanValue())
      return new Long(1L);
    return new Long(0L);
  }
  
  /**
   * Converts a BigDecimal to java.lang.Long type. <I>dec</I> may be truncated
   * if does not fall between -2147483648 to 2147483647.
   *
   * @param dec The BigDecimal value to convert
   * @return Converted Long value
   *
   * @since 4.1
   */
  public static Long convertToLong(BigDecimal dec)
  {
    return new Long(dec.longValue());
  }

  /**
   * Converts a string into java.lang.Float type.
   *
   * @param str The string value to convert
   * @return converted Float value, or <B>null</B> if <I>str</I> is non-numeric
   * or does not fall between 1.40129846432481707e-45f to
   * 3.40282346638528860e+38f.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Float convertToFloat(String str)
  {
    try
    {
      return new Float(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToFloat]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Float type. <I>num</I> may be truncated
   * if does not fall between 1.40129846432481707e-45f to
   * 3.40282346638528860e+38f.
   *
   * @param num The number value to convert
   * @return Converted float value.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Float convertToFloat(Number num)
  {
    return new Float(num.floatValue());
  }

  /**
   * Converts a boolean value to java.lang.Float type.
   *
   * @param bool The boolean value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Float convertToFloat(Boolean bool)
  {
    if (bool.booleanValue())
      return new Float("1");
    return new Float("0");
  }

  /**
   * Converts a BigDecimal to java.lang.Float type. <I>dec</I> may be truncated
   * if does not fall between 1.40129846432481707e-45f to
   * 3.40282346638528860e+38f.
   *
   * @param dec The BigDecimal value to convert
   * @return Converted Float value
   *
   * @since 4.1
   */
  public static Float convertToFloat(BigDecimal dec)
  {
    return new Float(dec.floatValue());
  }
  
  /**
   * Converts a string into java.lang.Float type.
   *
   * @param str The string value to convert
   * @return converted Float value, or <B>null</B> if <I>str</I> is non-numeric
   * or does not fall between 4.94065645841246544e-324 to
   * 1.79769313486231570e+308.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Double convertToDouble(String str)
  {
    try
    {
      return new Double(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToDouble]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Double type. <I>num</I> may be truncated
   * if does not fall between 4.94065645841246544e-324 to
   * 1.79769313486231570e+308.
   *
   * @param num The number to convert
   * @return Converted double value.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Double convertToDouble(Number num)
  {
    return new Double(num.doubleValue());
  }

  /**
   * Converts a boolean value to java.lang.Double type.
   *
   * @param bool The boolean value to convert
   * @return 1 if <B>true</B>, otherwise 0.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Double convertToDouble(Boolean bool)
  {
    if (bool.booleanValue())
      return new Double("1");
    return new Double("0");
  }

  /**
   * Converts a BigDecimal to java.lang.Double type. <I>dec</I> may be truncated
   * if does not fall between 14.94065645841246544e-324 to
   * 1.79769313486231570e+308.
   *
   * @param dec The BigDecimal value to convert
   * @return Converted Double value
   *
   * @since 4.1
   */
  public static Double convertToDouble(BigDecimal dec)
  {
    return new Double(dec.doubleValue());
  }
  
  /**
   * Converts a string value to java.lang.Boolean type.
   *
   * @param str The string to convert
   * @return <B>true</B> if <I>str</I>.equalsIgnoreCase("true"),
   * otherwise <B>false</B>.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Boolean convertToBoolean(String str)
  {
    try
    {
      return new Boolean(str);
    }
    catch (NumberFormatException ex)
    {
      err("AbstractEntity.convertToBoolean]", ex);
      return null;
    }
  }

  /**
   * Converts a number to java.lang.Boolean type.
   *
   * @param num The number to convert
   * @return <B>false</B> if <I>num</I> == 0, otherwise <B>true</B>
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Boolean convertToBoolean(Number num)
  {
    return (num.intValue() == 0)? new Boolean(false) : new Boolean(true);
  }
  
  /**
   * Converts a BigDecimal to java.lang.Boolean type.
   *
   * @param num The BigDecimal to convert
   * @return <B>false</B> if <I>dec</I> == 0, otherwise <B>true</B>
   *
   * @since 4.1
   */
  public static Boolean convertToBoolean(BigDecimal dec)
  {
    return (dec.intValue() == 0)? new Boolean(false) : new Boolean(true);
  }

  /**
   * Converts an object array to java.util.Vector type.
   *
   * @param array The array to convert
   * @return Converted vector. Each element of the <I>array</I> is added as
   * element of the vector, in same order.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Vector convertToVector(Object[] array)
  {
    Vector v = new Vector();
    for (int i=0; i<array.length; i++)
    {
      v.add(array[i]);
    }
    return v;
  }

  /**
   * Converts a collection to java.util.Vector type.
   *
   * @param collection The collection to convert
   * @return The converted vector. The order of elements in the collection
   * is retained.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Vector convertToVector(Collection collection)
  {
    return new Vector(collection);
  }

  /**
   * Converts a string to java.util.Vector type.
   *
   * @param listStr The string to convert. Must be of a specified format:
   * <elem1>;<elem2>;...
   * @return Converted Vector. The elements will be returned as strings.
   *
   * @since 2.0 I7
   */
  public static Vector convertToVector(String listStr)
  {
    Vector v = new Vector();
    if (v != null && listStr.length() >0)
    {
      StringTokenizer st = new StringTokenizer(listStr, ";", false);
      while (st.hasMoreTokens())
      {
        String elem = st.nextToken();
        if (elem.length() > 0)
        {
          v.add(elem);
        }
      }
    }
    return v;
  }

  /**
   * Converts an object array to java.util.List type.
   *
   * @param array The array to convert
   * @return Converted List. Each element of the <I>array</I> is added as
   * element of the List, in same order.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static List convertToList(Object[] array)
  {
    ArrayList list = new ArrayList();
    for (int i=0; i<array.length; i++)
    {
      list.add(array[i]);
    }
    return list;
  }

  /**
   * Converts a collection to java.util.List type.
   *
   * @param collection The collection to convert
   * @return The converted list. The order of elements in the collection
   * is retained.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static List convertToList(Collection collection)
  {
    return new ArrayList(collection);
  }

  /**
   * Converts a string to java.sql.Timestamp.
   *
   * @param dateStr The string to convert. Must be of a valid date str, eg.
   * YYYY-mm-dd | YYYY-mm-dd HH:MM:ss.SSS
   * @return Converted Timestamp value. <B>null</B> if <I>dateStr</I> not
   * convertable.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Timestamp convertToTimestamp(String dateStr)
  {
    int len = dateStr.trim().length();

    if (len == 0)
      return null;

    try
    {
      if (len > 10)
      {
        if (len < 30)
          return Timestamp.valueOf(dateStr);

        return null;
      }
      return convertToTimestamp(java.sql.Date.valueOf(dateStr));
    }
    catch (Exception ex)
    {
      err("Unable to convert string "+dateStr + " to timestamp", ex);
      return null;
    }
  }

  /**
   * Convert a java.sql.Date to java.sql.Timestamp type.
   *
   * @param date The date value to convert
   * @return Converted Timestamp value.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Timestamp convertToTimestamp(Date date)
  {
    if (date instanceof Timestamp)
      return (Timestamp)date;

    return new Timestamp(date.getTime());
  }

  /**
   * Converts a string to java.util.Hashtable type.
   *
   * @param paramStr The string to convert. Must be of the java Properties
   * format:
   * <key1>=<value1>\r\n<key2>=<value2>\r\n...<p>
   * <b>Note that all keys and values returned in the Hashtable are of String
   * type.</b>
   * @return Converted Hastable
   *
   * @since 1.0a build 0.9.9.6
   */
  public static Hashtable convertToTable(String paramStr)
  {
    /* 240902NSL Convert to Properties instead of Hashtable.
    Hashtable paramTable = new Hashtable();
    if (paramStr != null && paramStr.length() >0)
    {
      StringTokenizer st = new StringTokenizer(paramStr, ";", false);
      while (st.hasMoreTokens())
      {
        String param = st.nextToken("@;");
        if (param.length() > 0)
        {
          String val = st.nextToken("@;");
          paramTable.put(param, val);
        }
      }
    }
    return paramTable;
    */

    return convertToProperties(paramStr);
  }

  /**
   * Converts a string to java.util.Properties type.
   *
   * @param propStr The string to convert. Must be of java Properties format:
   * <key1>=<value1>\r\n<key2>=<value2>\r\n...
   * @return Converted Properties
   *
   * @since 2.0 I5
   */
  public static Properties convertToProperties(String propStr)
  {
    Properties props = new Properties();
    if (propStr != null && propStr.length() >0)
    {
      try
      {
        props.load(new ByteArrayInputStream(propStr.getBytes()));
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    return props;
  }

  /**
   * Converts a string to java.util.List type.
   *
   * @param listStr The string to convert. Must be of a specified format:
   * <elem1>;<elem2>;...
   * @param numeric <B>true</B> for the list elements to be numeric i.e. Integer,
   * <B>false</B> otherwise.
   * @return Converted List. The elements will be returned as strings or Integer
   * depending on the <I>numeric</I> argument value.
   *
   * @since 1.0b
   */
  public static List convertToList(String listStr, boolean numeric)
  {
    List list = new ArrayList();
    if (list != null && listStr.length() >0)
    {
      StringTokenizer st = new StringTokenizer(listStr, ";", false);
      while (st.hasMoreTokens())
      {
        String elem = st.nextToken();
        if (elem.length() > 0)
        {
          try
          {
            if (numeric)
              list.add(new Integer(elem));
            else
              list.add(elem);  
            //list.add(numeric ? (Object)new Integer(elem) : (Object)elem);
          }
          catch (Exception ex)
          {
            err("[AbstractEntity.convertToList] Non-numeric list element", ex);
          }
        }
      }
    }
    return list;
  }
  
  /**
   * Converts a boolean value to java.math.BigDecimal type.
   *
   * @param bool The BigDecimal value to convert
   * @returns 1 if <B>true</B>, otherwise 0.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Boolean bool)
  {
    if (bool.booleanValue())
      return new BigDecimal("1");
    return new BigDecimal("0");
  }
  
  /**
   * Converts a short value to java.math.BigDecimal type.
   *
   * @param num The Short value to convert
   * @returns the converted BigDecimal value.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Short num)
  {
    return new BigDecimal(num);
  }
  
  /**
   * Converts a integer value to java.math.BigDecimal type.
   *
   * @param num The Integer value to convert
   * @returns the converted BigDecimal value.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Integer num)
  {
  	return new BigDecimal(num);
  }
  
  /**
   * Converts a long value to java.math.BigDecimal type.
   *
   * @param num The Long value to convert
   * @returns the converted BigDecimal value.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Long num)
  {
  	return new BigDecimal(num);
  }
  
  /**
   * Converts a double value to java.math.BigDecimal type.
   *
   * @param num The Double value to convert
   * @returns the converted BigDecimal value.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Double num)
  {
  	return new BigDecimal(num);
  }
  
  /**
   * Converts a float value to java.math.BigDecimal type.
   *
   * @param num The Float value to convert
   * @returns the converted BigDecimal value.
   *
   * @since 4.1
   */
  public static BigDecimal convertToBigDecimal(Float num)
  {
  	return new BigDecimal(num);
  }

  /**
   * Converts a string to com.gridnode.db.filter.IDataFilter type.
   *
   * @param filterStr The string to convert. Must be of a specified format:
   * @return Converted IDataFilter
   *
   * @since 1.1
   */
//  public static IDataFilter convertToFilter(String filterStr)
//  {
//   log("Converting string to datafilter:"+ filterStr);
//
//    return DataFilterHandler.deserializeFromString(filterStr);
//  }

  /**
   * Gives a description of this data entity instance.
   *
   * @return String representation of the fields currently set in the entity.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    StringBuffer str = new StringBuffer();
    str.append(getEntityName());
    str.append("[\n");
    Enumeration fieldIds = getFieldIds();
    while (fieldIds.hasMoreElements())
    {
      Number fieldId = (Number)fieldIds.nextElement();
      FieldMetaInfo meta = getFieldMetaInfo(fieldId);
      if (meta != null)
      {
        str.append(meta.getFieldName());
        str.append(": ");
        Object obj = getFieldValue(fieldId);
        if (obj != null && obj instanceof IEntity)
        {
          str.append(((IEntity)obj).getEntityDescr());
        }
        else
          str.append(obj);
        str.append("\n");
      }
      else debug("[AbstractEntity.toString] No meta info for "+fieldId);
    }
    str.append("]");

    return str.toString();
  }

  // **************** Loggin methods **************

  /**
   * Log a message.
   *
   * @param msg The message to log
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void log(String msg)
  {
    System.out.println(msg);
  }

  /**
   * Log an error.
   *
   * @param msg The error message.
   * @param ex The exception error.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void err(String msg, Exception ex)
  {
    if (msg != null)
      System.out.println(msg);
    if (ex != null)
      ex.printStackTrace();
  }

  /**
   * Log a debugging message.
   *
   * @param msg The debug message.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void debug(String msg)
  {
    System.out.println(msg);
  }
  
  /**
   * TWX 30122005
   * @param objList
   * @return String converted from objList
   * @throws Exception
   */
  public static String convertToString(DataObjectList objList)
  	throws Exception
  {
  	StringWriter writer = new StringWriter();
  	objList.serialize(writer);
  	return writer.toString();
  }
  
  /**
   * TWX 30122005
   * @param dataObjs
   * @return DataObjectList converted from dataObjs
   * @throws Exception
   */
  public static DataObjectList convertToList(String dataObjs)
  	throws Exception
  {
  	StringReader reader = new StringReader(dataObjs);
  	DataObjectList objList = new DataObjectList();
  	return objList.deserialize(reader);
  }
  
  /**
   * TWX 12062006 Determine the toClass type for a certain field.
   * If user specify 'ObjectSER' in the metaInfo's value class,
   * meaning we need to perform deserialization and convert to 
   * java.lang.Object (can refer to GWFRtProcessDoc's reason).  
   * @param metaInfo
   * @param f
   * @return
   */
  private String getToClassType(FieldMetaInfo metaInfo, Field f)
  {
  	String valueClass = metaInfo.getValueClass();
  	if("ObjectSER".equalsIgnoreCase(valueClass))
  	{
  		return valueClass;
  	}
  	else
  	{
  		return f.getType().getName();
  	}
  }
}