/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFTemplate.java
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
 * Model for GFTemplate entity.
 *
 * Uid      - Unique ID for this GFTemplate entity instance.
 * Name     - Name of this GFTemplate entity.
 * Filename - The actual template file in Webdav.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFTemplate extends AbstractEntity implements IGFTemplate
{
  protected String _name;
  protected String _filename;

  public GFTemplate()
  {
  }

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return getTemplateName();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ---------------------------------------------------------------------------

  public String getTemplateName()
  {
    return _name;
  }

  public void setTemplateName(String name)
  {
    _name = name;
  }

  public String getFilename()
  {
    return _filename;
  }

  public void setFilename (String filename)
  {
    _filename = filename;
  }
}