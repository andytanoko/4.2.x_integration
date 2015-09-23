/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 26 2002    Koh Han Sing        Created
 * Jun 09 2004    Neo Sok Lay         Initialize subPath to "" so that importing
 *                                    exported config from previous versions 
 *                                    would not cause a Column 'SubPath' cannot be null
 *                                    sql error.
 */
package com.gridnode.pdip.app.mapper.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class MappingFile
  extends    AbstractEntity
  implements IMappingFile
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8692595129222823926L;
	protected String  _name;
  protected String  _description;
  protected String  _filename;
  protected String  _path;
  protected String  _subPath = "";
  protected Short   _type;
  protected Object  _mappingObject;
  protected String  _mappingClass;

  public MappingFile()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "/" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getFilename()
  {
    return _filename;
  }

  public String getPath()
  {
    return _path;
  }

  public String getSubPath()
  {
    return _subPath;
  }

  public Short getType()
  {
    return _type;
  }

  public Object getMappingObject()
  {
    return _mappingObject;
  }
  
  public String getMappingClass()
  {
    return _mappingClass;
  }

  // *************** Setters for attributes *************************

  public void setName(String name)
  {
    _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setFilename(String filename)
  {
    _filename = filename;
  }

  public void setPath(String path)
  {
    _path = path;
  }

  public void setSubPath(String subPath)
  {
    _subPath = subPath;
    if (_subPath == null)
      _subPath = "";
  }

  public void setType(Short type)
  {
    _type = type;
  }

  public void setMappingObject(Object mappingObject)
  {
    _mappingObject = mappingObject;
  }
  
  public void setMappingClass(String mappingClass)
  {
    _mappingClass = mappingClass;
  }
}