/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMappingFileListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28 2002    Koh Han Sing        Created
 * Mar 27 2003    Neo Sok Lay         Extend from AbstractGetEntityListAction.
 */
package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.model.mapper.MappingFileEntityFieldID;
import com.gridnode.gtas.events.mapper.GetMappingFileListEvent;

import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 * This Action class handles the retrieving of a list of MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetMappingFileListAction
  extends    AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3632405978856134082L;
	public static final String CURSOR_PREFIX = "MappingFileListCursor.";
  public static final String ACTION_NAME = "GetMappingFileListAction";

  protected Class getExpectedEventClass()
  {
    return GetMappingFileListEvent.class;
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
    return getManager().findMappingFiles(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return MappingFile.convertEntitiesToMap(
             (MappingFile[])entityList.toArray(new MappingFile[entityList.size()]),
             MappingFileEntityFieldID.getEntityFieldID(),
             null);
  }

  private IMappingManagerObj getManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }
}