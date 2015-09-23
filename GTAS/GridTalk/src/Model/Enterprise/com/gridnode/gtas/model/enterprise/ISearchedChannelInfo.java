/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchedChannelInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.enterprise;

import com.gridnode.gtas.model.channel.IChannelInfo;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in ChannelInfo entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */

public interface ISearchedChannelInfo extends IChannelInfo
{
  /**
   * Entity Name for SearchedChannelInfo.
   */
  public static final String ENTITY_NAME = "SearchedChannelInfo";

}