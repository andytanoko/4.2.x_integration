/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingRule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 26 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class MappingRule
  extends    AbstractEntity
  implements IMappingRule
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7567273962587939762L;
	protected String      _name;
  protected String      _description;
  protected Short       _type;
  protected MappingFile _mappingFile;
  protected Boolean     _transformRefDoc;
  protected Long        _refDocUID;
  protected String      _xpath;
  protected String      _paramName;
  protected Boolean     _keepOriginal;
  protected String      _mappingClass;
  

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "-" + getDescription();
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

  public Short getType()
  {
    return _type;
  }

  public MappingFile getMappingFile()
  {
    return _mappingFile;
  }

  public Boolean isTransformRefDoc()
  {
    return _transformRefDoc;
  }

  public Long getRefDocUID()
  {
    return _refDocUID;
  }

  public String getXPath()
  {
    return _xpath;
  }

  public String getParamName()
  {
    return _paramName;
  }

  public Boolean isKeepOriginal()
  {
    return _keepOriginal;
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

  public void setType(Short type)
  {
    _type = type;
  }

  public void setMappingFile(MappingFile mappingFile)
  {
    _mappingFile = mappingFile;
  }

  public void setTransformRefDoc(Boolean transformRefDoc)
  {
    _transformRefDoc = transformRefDoc;
  }

  public void setRefDocUID(Long refDocUID)
  {
    _refDocUID = refDocUID;
  }

  public void setXPath(String xpath)
  {
    _xpath = xpath;
  }

  public void setParamName(String paramName)
  {
    _paramName = paramName;
  }

  public void setKeepOriginal(Boolean keepOriginal)
  {
    _keepOriginal = keepOriginal;
  }

  public void setMappingClass(String mappingClass)
  {
    _mappingClass = mappingClass;
  }
}