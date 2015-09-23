/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridDocumentListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.GetGridDocumentListEvent;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of GridDocument.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridDocumentListAction
  extends    AbstractGetEntityListAction2
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4270861980839938551L;
	public static final String CURSOR_PREFIX = "GridDocumentListCursor.";
  public static final String ACTION_NAME = "GetGridDocumentListAction";

  protected Class getExpectedEventClass()
  {
    return GetGridDocumentListEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }
  
  protected Number getEntityKeyID()
  {
  	return GridDocument.UID;    
  }
  
  protected Collection retrieveEntityKeys(IDataFilter filter)
	throws Exception
  {
	Collection entityKeys = 
	  ActionHelper.getManager().findGridDocumentsKeys(filter);
	return entityKeys;  	  
  }

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    Collection entityList = ActionHelper.getManager().findGridDocuments(filter);
//    ArrayList deserializeGdocs = new ArrayList();
//    for(Iterator i = gdocs.iterator(); i.hasNext();)
//    {
//      GridDocument gdoc = (GridDocument)i.next();
//      long uid = gdoc.getUId();
//      String gdocFilename = gdoc.getGdocFilename();
//      String gdocFullPath = FileUtil.getDomain()+"/"+
//                            FileUtil.getPath(IDocumentPathConfig.PATH_GDOC)+"/"+
//                            gdocFilename;
//      gdoc = (GridDocument)gdoc.deserialize(gdocFullPath);
//      gdoc.setUId(uid);
//      deserializeGdocs.add(gdoc);
//    }
    return entityList;
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertGridDocumentToMapObjects(entityList);
  }

}
