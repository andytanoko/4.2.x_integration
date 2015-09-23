/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGdocDetailAction.java
 *
 ****************************************************************************
 * Date           Author            Changes
 ****************************************************************************
 * 2  Nov 2006    Regina Zeng        Created
 * 12 Dec 2006    Tam Wei Xiang      Modified getEntity(...). We dun need to refetch
 *                                   the gdoc through the DocumentManager.
 */
package com.gridnode.gtas.server.dbarchive.actions.doc.temp;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.dbarchive.doc.temp.GetGdocDetailEvent;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.server.dbarchive.facade.ejb.ISearchESDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;

import java.util.Map;
import java.io.File;

/**
 * This Action class handles the retrieving of a gdoc detail info based on specific DocumentMetaInfo uid.
 */
public class GetGdocDetailAction extends AbstractGetEntityAction
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -4383252636036409555L;
  public static final String ACTION_NAME = "GetGdocDetailAction";

  protected Class getExpectedEventClass()
  {
    return GetGdocDetailEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
  
  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetGdocDetailEvent getEvent = (GetGdocDetailEvent) event;
    long uid = getEvent.getUid();
    ISearchESDocumentManagerObj manager = com.gridnode.gtas.server.dbarchive.helpers.ActionHelper.getManager();
    File file = (File)manager.getGDocInfoByDocUID(uid);
    //GridDocument gdoc = com.gridnode.gtas.server.document.helpers.ActionHelper.getManager().findGridDocument(uid);
    
    //TWX 14 Dec 2006 
    if(file != null)
    {
      GridDocument gdoc = new GridDocument();
      gdoc = (GridDocument)gdoc.deserialize(file.getAbsolutePath());
      gdoc.setUId(uid);
      return gdoc;
    }
    else
    {
      throw new ApplicationException("[GetGdocDetailAction.getEntity] No physical gdoc file can be located given the DocumentMetaInfo's UID "+uid);
    }
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGridDocumentToMap((GridDocument)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetGdocDetailEvent updEvent = (GetGdocDetailEvent)event;
    return new Object[] 
      {GridDocument.ENTITY_NAME, updEvent.getUid()
      };
  }
}
