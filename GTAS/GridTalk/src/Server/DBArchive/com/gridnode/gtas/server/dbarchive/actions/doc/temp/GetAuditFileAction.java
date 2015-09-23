/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAuditFileAction.java
 *
 ****************************************************************************
 * Date           Author              		Changes
 ****************************************************************************
 * 12 Oct 2005    Sumedh Chalermkanjana		Created
 */
package com.gridnode.gtas.server.dbarchive.actions.doc.temp;

import com.gridnode.gtas.model.dbarchive.docforpi.AuditFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.dbarchive.doc.temp.GetAuditFileEvent;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.server.dbarchive.facade.ejb.ISearchESDocumentManagerObj;
import com.gridnode.gtas.server.dbarchive.model.*;

import java.util.Map;

/**
 * This Action class handles the retrieving of a audit file info based on specific DocumentMetaInfo uid.
 */
public class GetAuditFileAction
  extends    AbstractGetEntityAction
{
  public static final String ACTION_NAME = "GetAuditFileAction";

  protected Class getExpectedEventClass()
  {
    return GetAuditFileEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
  	GetAuditFileEvent getEvent = (GetAuditFileEvent) event;
  	Long uid = getEvent.getUid();
  	ISearchESDocumentManagerObj manager = com.gridnode.gtas.server.dbarchive.helpers.ActionHelper.getManager();
  	return manager.findAuditFileInfoByUID(uid);

    //GridDocument gdoc = ActionHelper.getManager().findGridDocument(                          
    //getEvent.getGridDocumentUID());
    //long uid = gdoc.getUId();
    //String gdocFilename = gdoc.getGdocFilename();
    //String gdocFullPath = FileUtil.getDomain()+File.separator+
    //                      FileUtil.getPath(IDocumentPathConfig.PATH_GDOC)+
    //                      File.separator+gdoc.getFolder()+
    //                      File.separator+gdocFilename;
    //gdoc = (GridDocument)gdoc.deserialize(gdocFullPath);
    //gdoc.setUId(uid);
    //return gdoc;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
  	return AbstractEntity.convertToMap(entity, AuditFileEntityFieldID.getEntityFieldID(), null);
//    return ActionHelper.convertGridDocumentToMap((GridDocument)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
  	GetAuditFileEvent updEvent = (GetAuditFileEvent) event;
    return new Object[]
           {
             IAuditFileMetaInfo.ENTITY_NAME,
             updEvent.getUid()
           };
  }
}