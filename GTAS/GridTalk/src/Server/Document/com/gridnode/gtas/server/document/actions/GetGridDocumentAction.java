/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Koh Han Sing        Created
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Nov 21 2005    Neo Sok Lay         Use FileUtil only to get gdoc file                                   
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.document.GetGridDocumentEvent;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.io.File;
import java.util.Map;

/**
 * This Action class handles the retrieving of a GridDocument.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridDocumentAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6045933466764421903L;
	public static final String ACTION_NAME = "GetGridDocumentAction";

  protected Class getExpectedEventClass()
  {
    return GetGridDocumentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetGridDocumentEvent getEvent = (GetGridDocumentEvent)event;
    GridDocument gdoc = ActionHelper.getManager().findGridDocument(
                          getEvent.getGridDocumentUID());
    long uid = gdoc.getUId();
    String gdocFilename = gdoc.getGdocFilename();
    
    //String gdocFullPath = FileUtil.getDomain()+File.separator+
    //                      FileUtil.getPath(IDocumentPathConfig.PATH_GDOC)+
    //                      File.separator+gdoc.getFolder()+
    //                      File.separator+gdocFilename;
    //NSL20051121 Use only FileUtil, dun try to construct the full path by myself
    String gdocFullPath = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gdoc.getFolder()+"/", gdocFilename).getCanonicalPath();
    gdoc = (GridDocument)gdoc.deserialize(gdocFullPath);
    gdoc.setUId(uid);
    return gdoc;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGridDocumentToMap((GridDocument)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetGridDocumentEvent updEvent = (GetGridDocumentEvent)event;
    return new Object[]
           {
             GridDocument.ENTITY_NAME,
             updEvent.getGridDocumentUID()
           };
  }
}