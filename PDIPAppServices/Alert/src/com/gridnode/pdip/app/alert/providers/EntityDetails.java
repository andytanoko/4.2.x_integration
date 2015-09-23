/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDetails.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 23 Jan 2003    Neo Sok Lay         Created.
 * 26 Feb 2003    Neo Sok Lay         If FieldId not found for a fieldname,
 *                                    get the fieldvalue from the content hashmap
 *                                    by calling the super.getFieldValue().
 * 03 Mar 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.alert.providers;

import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.entity.IEntity;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Data provider for Entity type object.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public abstract class EntityDetails extends AbstractDetails
{
  private IEntity _entity;

  public EntityDetails(IEntity entity)
  {
    _entity = entity;
  }

  public String getName()
  {
    return _entity.getEntityName();
  }

  public String getType()
  {
    return TYPE_ENTITY;
  }

  protected static List<String> getFieldNames(String entityName)
  {
    ArrayList<String> fieldNames = new ArrayList<String>();

    EntityMetaInfo emi = MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    if (emi != null)
    {
      FieldMetaInfo[] fmis = emi.getFieldMetaInfo();

      for (FieldMetaInfo fmi : fmis)
      {
        if (isSelectable(fmi))
          fieldNames.add(fmi.getFieldName());
      }
    }
    return fieldNames;
  }

  protected Object getFieldValue(String fieldName)
  {
    Object val = null;
    Number fieldId = getFieldId(fieldName);

    val = (fieldId == null)?
          super.getFieldValue(fieldName) :
          getFieldValue(fieldId);

    return val;
  }

  protected final Number getFieldId(String fieldName)
  {
    Number fieldId = null;
    FieldMetaInfo fInfo = _entity.getMetaInfo().findFieldMetaInfoWithFieldName(fieldName);
    if (fInfo != null)
    {
      fieldId = fInfo.getFieldId();
    }

    return fieldId;
  }

  protected Object getFieldValue(Number fieldId)
  {
    return _entity.getFieldValue(fieldId);
  }

  protected static boolean isSelectable(FieldMetaInfo fmi)
  {
    boolean selectable = false;
    Properties props = fmi.getPresentation();
    if (props != null)
      selectable = "true".equals(props.getProperty("displayable", "false"));

    return selectable;
  }

}