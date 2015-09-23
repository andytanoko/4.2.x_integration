/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultMappingRuleEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


/**
 * MappingRuleEntity is embedded in GridTalkMappingRuleEntity
 */
class DefaultMappingRuleEntity extends AbstractGTEntity
  implements IGTMappingRuleEntity
{

//  DefaultMappingRuleEntity(IGTSession session, IGTFieldMetaInfo[] metaInfo)
//  {
//    super(session, IGTEntity.ENTITY_MAPPING_RULE, metaInfo);
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
    if(TYPE_MAPPING_CONVERT.equals(type))
    {
      return "mappingRule.type.convert";
    }
    if(TYPE_MAPPING_SPLIT.equals(type))
    {
      return "mappingRule.type.split";
    }
    if(TYPE_MAPPING_TRANSFORM.equals(type))
    {
      return "mappingRule.type.transform";
    }
    throw new GTClientException("Unsupported type:" + type);
  }

  public Collection getAllowedTypes() throws GTClientException
  {
    ArrayList types = new ArrayList(3);
    types.add(this.TYPE_MAPPING_CONVERT);
    types.add(this.TYPE_MAPPING_SPLIT);
    types.add(this.TYPE_MAPPING_TRANSFORM);
    return types;
  }*/
}