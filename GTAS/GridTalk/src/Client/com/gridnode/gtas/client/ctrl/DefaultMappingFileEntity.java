/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultMappingFileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 * 2002-07-15     Andrew Hill         Add enumerative support for type field
 */
package com.gridnode.gtas.client.ctrl;


class DefaultMappingFileEntity extends AbstractGTEntity
  implements IGTMappingFileEntity
{

//  DefaultMappingFileEntity(IGTSession session, IGTFieldMetaInfo[] metaInfo)
//  {
//    super(session, IGTEntity.ENTITY_MAPPING_FILE, metaInfo);
//  }

//  public long getUID()
//  {
//    Long uid = (Long) _fields.get(UID);
//    return uid.longValue();
//  }

  /*public String getTypeLabelKey() throws GTClientException
  {
    Short type = (Short)getFieldValue(TYPE);
    return getTypeLabelKey(type);
  }

  public String getTypeLabelKey(Short type) throws GTClientException
  {
    if(TYPE_CONVERSION_RULE.equals(type))
    {
      return "mappingFile.type.conversionRule";
    }
    if(TYPE_REFERENCE_DOC.equals(type))
    {
      return "mappingFile.type.referenceDoc";
    }
    if(TYPE_XSL.equals(type))
    {
      return "mappingFile.type.xsl";
    }
    throw new GTClientException("Unsupported type:" + type);
  }

  public Collection getAllowedTypes() throws GTClientException
  {
    ArrayList types = new ArrayList(3);
    types.add(TYPE_CONVERSION_RULE);
    types.add(TYPE_REFERENCE_DOC);
    types.add(TYPE_XSL);
    return types;
  }*/
}