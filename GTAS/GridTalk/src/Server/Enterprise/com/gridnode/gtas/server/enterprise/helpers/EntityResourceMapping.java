/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityResourceMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.user.model.UserAccount;

/**
 * This class handles the mapping of Entities to Resource types handled by
 * this module.
 * 
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class EntityResourceMapping
{
  public final String UNKNOWN_RESOURCE = "UnknownRes";

  private final String[][] MAPPING =
    {
      {BusinessEntity.ENTITY_NAME, IResourceTypes.BUSINESS_ENTITY},
      {ChannelInfo.ENTITY_NAME,    IResourceTypes.CHANNEL},
      //{GridNode.ENTITY_NAME,       IResourceTypes.GRIDNODE},
      {Partner.ENTITY_NAME,        IResourceTypes.PARTNER},
      {UserAccount.ENTITY_NAME,    IResourceTypes.USER},
    };
  private final int ENTITY_IDX    = 0;
  private final int RESOURCE_IDX  = 1;
  private static EntityResourceMapping _self;
  private static final Object _lock = new Object();

  private EntityResourceMapping()
  {
  }

  public static EntityResourceMapping getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new EntityResourceMapping();
      }
    }

    return _self;
  }

  /**
   * Get the resource type based on the entity name.
   *
   * @param entityName The name of the entity.
   * @return The resource type determined from the entity name. If the entity
   * is not of interest as a resource type, "UnknownRes" would be returned.
   */
  public String getResourceType(String entityName)
  {
    String resType = null;

    for (int i=0; i<MAPPING.length; i++)
    {
      if (MAPPING[i][ENTITY_IDX].equals(entityName))
      {
        resType = MAPPING[i][RESOURCE_IDX];
        break;
      }
    }

    if (resType == null)
      resType = UNKNOWN_RESOURCE;

    return resType;
  }
}