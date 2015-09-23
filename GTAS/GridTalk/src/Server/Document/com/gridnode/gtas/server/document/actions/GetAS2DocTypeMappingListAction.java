/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.GetAS2DocTypeMappingListEvent;
import com.gridnode.gtas.model.document.AS2DocTypeMappingEntityFieldID;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.pdip.framework.db.filter.IDataFilter;


public class GetAS2DocTypeMappingListAction
extends    AbstractGetEntityListAction
{
  private static final long serialVersionUID = -8185703784455145640L;
  public static final String CURSOR_PREFIX = "AS2DocTypeListCursor.";
  public static final String ACTION_NAME = "GetAS2DocTypeMappingListAction";
  
  protected Class getExpectedEventClass()
  {
    return GetAS2DocTypeMappingListEvent.class;
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }
  
  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }
  
  protected Collection retrieveEntityList(IDataFilter filter)
  throws Exception
  {
    return ActionHelper.getManager().findAS2DocTypeMappingByFilter(filter);
  }
  
  protected Collection convertToMapObjects(Collection entityList)
  {
    return AS2DocTypeMapping.convertEntitiesToMap(
          (AS2DocTypeMapping[])entityList.toArray(new AS2DocTypeMapping[entityList.size()]),
          AS2DocTypeMappingEntityFieldID.getEntityFieldID(),
          null);
  }

}
