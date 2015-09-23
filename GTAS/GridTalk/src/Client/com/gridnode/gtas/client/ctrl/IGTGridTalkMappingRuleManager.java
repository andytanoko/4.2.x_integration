/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGridTalkMappingRuleManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Collection;

public interface IGTGridTalkMappingRuleManager extends IGTManager
{
  public IGTGridTalkMappingRuleEntity getGridTalkMappingRuleByUID(long uid)
    throws GTClientException;

  public Collection getAllOfHeaderTransformation(Boolean headerTransformation)
    throws GTClientException;
}