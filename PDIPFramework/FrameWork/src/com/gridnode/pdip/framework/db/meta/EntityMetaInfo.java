/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Apr 12 2002    Neo Sok Lay         Add getSqlFieldMetaInfo().
 */
package com.gridnode.pdip.framework.db.meta;

import java.io.*;
import java.util.*;

/**
 * A meta data representation to be used for mapping between a data entity
 * and its data object or persistent representation.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public class EntityMetaInfo
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2277904666254675054L;
	private String _objectName;
  private String _entityName;
  private String _sqlName;
  private Vector _fieldMetaMap = new Vector();

  public EntityMetaInfo()
  {
  }

  public String getEntityName()
  {
    return _entityName;
  }

  /**
   * Sets the name of the entity this meta info is for.
   *
   * @param entityName Name of the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setEntityName(String entityName)
  {
    _entityName = entityName;
  }

  public String getSqlName()
  {
    return _sqlName;
  }

  /**
   * Sets the table name that this entity corresponds to in database.
   *
   * @param sqlName Table name of the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setSqlName(String sqlName)
  {
    _sqlName = sqlName;
  }

  public String getObjectName()
  {
    return _objectName;
  }

  /**
   * Sets the name of the entity known to JDO.
   *
   * @param objName Name of the entity known to JDO
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setObjectName(String objName)
  {
    _objectName = objName;
  }

  /**
   * Creates an empty meta info for a field.
   *
   * @return A new instance of field meta info.
   *
   * @since 1.0a build 0.9.9.6
   */
  public FieldMetaInfo createFieldMetaInfo()
  {
    return new FieldMetaInfo();
  }

  /**
   * Add a meta info for a field of the entity.
   *
   * @param fieldMetaInfo Meta info to add
   *
   * @since 1.0a build 0.9.9.6
   */
  public void addFieldMetaInfo(FieldMetaInfo fieldMetaInfo)
  {
    synchronized (_fieldMetaMap)
    {
      if (!_fieldMetaMap.contains(fieldMetaInfo))
      {
        _fieldMetaMap.add(fieldMetaInfo);
        fieldMetaInfo.setEntityName(this.getEntityName());
      }
    }
  }

  /**
   * Get the map of meta info for all fields in the entity.
   *
   * @return Vector of FieldMetaInfo
   *
   * @since 1.0a build 0.9.9.6
   */
  public Vector getFieldMetaMap()
  {
    return _fieldMetaMap;
  }

  public FieldMetaInfo findFieldMetaInfo(Number fieldId)
  {
    FieldMetaInfo fieldMeta;

    Iterator it = _fieldMetaMap.iterator();
    while ( it.hasNext() ) {
      fieldMeta = (FieldMetaInfo) it.next();
      Number metaFieldId = fieldMeta.getFieldId();
      if ( metaFieldId != null && metaFieldId.equals(fieldId) )
      {
        return fieldMeta;
      }
    }
    return null;
  }

  public FieldMetaInfo findFieldMetaInfo(String objectName)
  {
    FieldMetaInfo fieldMeta;

    Iterator it = _fieldMetaMap.iterator();
    while ( it.hasNext() )
    {
      fieldMeta = (FieldMetaInfo) it.next();
      if ( fieldMeta.getObjectName().equals(objectName) )
      {
        return fieldMeta;
      }
    }
    return null;
  }

  public FieldMetaInfo findFieldMetaInfoWithFieldName(String fieldName)
  {
    FieldMetaInfo fieldMeta;

    Iterator it = _fieldMetaMap.iterator();
    while ( it.hasNext() )
    {
      fieldMeta = (FieldMetaInfo) it.next();
      if ( fieldMeta.getFieldName().equals(fieldName) )
      {
        return fieldMeta;
      }
    }
    return null;
  }

  public FieldMetaInfo[] getFieldMetaInfo()
  {
    Collections.sort(_fieldMetaMap);
    return (FieldMetaInfo[])_fieldMetaMap.toArray(new FieldMetaInfo[0]);
  }

  /**
   * Get the meta info the Sql Fields (fields persist in DB).
   *
   * @return Array of FieldMetaInfo of Sql Fields, sorted according to Sequence
   * field.
   */
  public FieldMetaInfo[] getSqlFieldMetaInfo()
  {
    Vector sqlV = new Vector();
    for (Iterator i=_fieldMetaMap.iterator(); i.hasNext(); )
    {
      FieldMetaInfo fieldMeta = (FieldMetaInfo)i.next();
      if (fieldMeta.getSqlName() != null &&
          fieldMeta.getSqlName().trim().length() > 0)
        sqlV.add(fieldMeta);
    }
    Collections.sort(sqlV);
    return (FieldMetaInfo[])sqlV.toArray(new FieldMetaInfo[sqlV.size()]);
  }

  /**
   * Gives a short description of this entity meta info object.
   *
   * @return A string representation of a short summary of this meta info
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    return "Entity:   " + getObjectName() + "\n" +
           "  Name:   " + getEntityName() + "\n" +
           "  SqlName:" + getSqlName() + "\n" +
           "  #Fields:" + getFieldMetaMap().size();
  }
}