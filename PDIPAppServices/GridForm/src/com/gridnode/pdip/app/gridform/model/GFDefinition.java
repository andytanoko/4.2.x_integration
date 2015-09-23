/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFDefinition.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * Jun 04 2002    Daniel D'Cotta      Renamed getName() and getDescription()
 */
package com.gridnode.pdip.app.gridform.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;

/**
 * Model for GFDefinition entity.
 *
 * Uid      - Unique ID for this GFDefinition entity instance.
 * Name     - Name of this GFDefinition entity.
 * Filename - The actual definition file in Webdav that contains the mappings.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFDefinition extends AbstractEntity implements IGFDefinition
{
  protected String _name;
  protected String _domain;
  protected String _path;
  protected String _filename;
  protected GFTemplate _template;

  public GFDefinition()
  {
  }

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return getDefinitionName();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ---------------------------------------------------------------------------

  public String getDefinitionName()
  {
    return _name;
  }

  public void setDefinitionName(String name)
  {
    _name = name;
  }

  public String getDomain()
  {
    return _domain;
  }

  public void setDomain(String domain)
  {
    _domain = domain;
  }

  public String getPath()
  {
    return _path;
  }

  public void setPath(String path)
  {
    _path = path;
  }

  public String getFilename()
  {
    return _filename;
  }

  public void setFilename(String filename)
  {
    _filename = filename;
  }

  public GFTemplate getTemplate()
  {
    return _template;
  }

  public void setTemplate(GFTemplate template)
  {
    _template = template;
  }
}