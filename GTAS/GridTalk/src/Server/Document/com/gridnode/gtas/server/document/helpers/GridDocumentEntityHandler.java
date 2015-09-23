/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 * Jan 15 2003    Neo Sok Lay         Change findDuplicate() to findInboundDocFor().
 * Feb 17 2003    Koh Han Sing        Handle deletion of attachments
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.server.document.entities.ejb.IGridDocumentLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IGridDocumentLocalObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the GridDocumentBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class GridDocumentEntityHandler
  extends          LocalEntityHandler
{
  private GridDocumentEntityHandler()
  {
    super(GridDocument.ENTITY_NAME);
  }

  /**
   * Get an instance of a GridDocumentEntityHandler.
   */
  public static GridDocumentEntityHandler getInstance()
  {
    Logger.debug("[GridDocumentEntityHandler].getInstance");
    GridDocumentEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GridDocument.ENTITY_NAME, true))
    {
      handler = (GridDocumentEntityHandler)EntityHandlerFactory.getHandlerFor(
                  GridDocument.ENTITY_NAME, true);
    }
    else
    {
      handler = new GridDocumentEntityHandler();
      EntityHandlerFactory.putEntityHandler(GridDocument.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  public Object create(GridDocument gridDoc) throws Throwable
  {
    Logger.debug("[GridDocumentEntityHandler].create");
    Long nextGdocId = KeyGen.getNextId(gridDoc.getFolder());
    gridDoc.setGdocId(nextGdocId);
    return super.create(gridDoc);
  }
  
  public IEntity createEntity(GridDocument gridDoc, boolean updataGDocId) throws Throwable
  {
    Logger.debug("[GridDocumentEntityHandler].createEntity");
    if(updataGDocId)
    {
      Long nextGdocId = KeyGen.getNextId(gridDoc.getFolder());
      gridDoc.setGdocId(nextGdocId);
    }
    if(gridDoc.getDateTimeCreate()==null)
      gridDoc.setDateTimeCreate(new Date(TimeUtil.localToUtc())); 

    return super.createEntity(gridDoc);
  }

  public IEntity createEntity(GridDocument gridDoc) throws Throwable
  {
    return createEntity(gridDoc, true);
  }

  /**
   * Check whether GridDocument has already been received in the Inbound folder.
   *
   * @param gdoc The GridDocument to check.
   * @return whether there is duplicate.
   */
  //public boolean findDuplicate(GridDocument gdoc) throws Throwable

  /**
   * Find the Inbound document for a original document received from sender.
   *
   * @param gdoc The original document received from sender.
   * @return The Inbound document or <b>null</b> if gdoc has not been received
   * before.
   */
  public GridDocument findInboundDocFor(GridDocument gdoc) throws Throwable
  {
    Logger.debug("[GridDocumentEntityHandler.findDuplicate]");
    IDataFilter filter = new DataFilterImpl();

    filter.addSingleFilter(
      null,
      GridDocument.FOLDER,
      filter.getEqualOperator(),
      GridDocument.FOLDER_INBOUND,
      false);

    filter.addSingleFilter(
      filter.getAndConnector(),
      GridDocument.S_NODE_ID,
      filter.getEqualOperator(),
      gdoc.getSenderNodeId(),
      false);

    filter.addSingleFilter(
      filter.getAndConnector(),
      GridDocument.REF_G_DOC_ID,
      filter.getEqualOperator(),
      gdoc.getGdocId(),
      false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (GridDocument)result.toArray()[0];
  }

  /**
   * Overwrites to handle deletion of attachments
   */
  public void remove(Long uId) throws Throwable
  {
    GridDocument gdoc = (GridDocument)getEntityByKeyForReadOnly(uId);
    super.remove(uId);
    deleteAttachments(gdoc);
  }

  /**
   * Overwrites to handle deletion of attachments
   */
  public void removeByFilter(IDataFilter filter) throws Throwable
  {
    Collection gdocs = getEntityByFilterForReadOnly(filter);
    super.removeByFilter(filter);
    for (Iterator i = gdocs.iterator(); i.hasNext(); )
    {
      GridDocument gdoc = (GridDocument)i.next();
      deleteAttachments(gdoc);
    }
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGridDocumentLocalHome.class.getName(),
      IGridDocumentLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IGridDocumentLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IGridDocumentLocalObj.class;
  }

  private void deleteAttachments(GridDocument gdoc)
    throws Throwable
  {
    if (gdoc.hasAttachment().booleanValue() && gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      List attachments = gdoc.getAttachments();
      for (Iterator i = attachments.iterator(); i.hasNext(); )
      {
        Long uid = new Long(i.next().toString());
        Logger.debug("[GridDocumentEntityHandler.deleteAttachments] uid = "+uid);
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(
          null,
          GridDocument.ATTACHMENTS,
          filter.getLocateOperator(),
          uid,
          false);
        Collection gdocs = getEntityByFilterForReadOnly(filter);
        if (gdocs.isEmpty())
        {
          Logger.debug("[GridDocumentEntityHandler.deleteAttachments] no gdoc with attachment uid found");
          AttachmentEntityHandler.getInstance().remove(uid);
        }
        else
        {
          Logger.debug("[GridDocumentEntityHandler.deleteAttachments] other gdoc still using attachment");
        }
      }
    }
  }

//  private long getLastGDocID(String folder)
//    throws Exception
//  {
//    IDataFilter filter = new DataFilterImpl();
//    filter.addSingleFilter(
//      null,
//      GridDocument.FOLDER,
//      filter.getEqualOperator(),
//      folder,
//      false);
//    filter.setOrderFields(new Number[] {GridDocument.G_DOC_ID});
//
//    Collection result = getEntityByFilterForReadOnly(filter);
//    if(result == null || result.isEmpty())
//      return 0;
//
//    Object[] gridDocs = result.toArray();
//    int lastposition = gridDocs.length - 1;
//    GridDocument lastGridDoc = (GridDocument)gridDocs[lastposition];
//    return lastGridDoc.getGdocId().longValue();
//  }
}