/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.model;

import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class GridTalkMappingRule
  extends    AbstractEntity
  implements IGridTalkMappingRule
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5998519441564600439L;
	protected String      _name;
  protected String      _description;
  protected String      _sourceDocType;
  protected String      _targetDocType;
  protected String      _sourceDocFileType;
  protected String      _targetDocFileType;
  protected Boolean     _headerTransformation;
  protected Boolean     _transformWithHeader;
  protected Boolean     _transformWithSource;
  protected MappingRule _mappingRule;
  protected String      _mappingClass;

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

  public String getSourceDocType()
  {
    return _sourceDocType;
  }

  public String getTargetDocType()
  {
    return _targetDocType;
  }

  public String getSourceDocFileType()
  {
    return _sourceDocFileType;
  }

  public String getTargetDocFileType()
  {
    return _targetDocFileType;
  }

  public boolean isHeaderTransformation()
  {
    return _headerTransformation.booleanValue();
  }

  public boolean isTransformWithHeader()
  {
    return _transformWithHeader.booleanValue();
  }

  public boolean isTransformWithSource()
  {
    return _transformWithSource.booleanValue();
  }

  public MappingRule getMappingRule()
  {
    return _mappingRule;
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

  public void setSourceDocType(String sourceDocType)
  {
    _sourceDocType = sourceDocType;
  }

  public void setTargetDocType(String targetDocType)
  {
    _targetDocType = targetDocType;
  }

  public void setSourceDocFileType(String sourceDocFileType)
  {
    _sourceDocFileType = sourceDocFileType;
  }

  public void setTargetDocFileType(String targetDocFileType)
  {
    _targetDocFileType = targetDocFileType;
  }

  public void setHeaderTransformation(Boolean headerTransformation)
  {
    _headerTransformation = headerTransformation;
  }

  public void setTransformWithHeader(Boolean transformWithHeader)
  {
    _transformWithHeader = transformWithHeader;
  }

  public void setTransformWithSource(Boolean transformWithSource)
  {
    _transformWithSource = transformWithSource;
  }

  public void setMappingRule(MappingRule mappingRule)
  {
    _mappingRule = mappingRule;
  }
  
  public void setMappingClass(String mappingClass)
  {
    _mappingClass = mappingClass;
  }

}