/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.entities.ejb.IAttachmentLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IAttachmentLocalObj;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AttachmentBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class AttachmentEntityHandler
  extends          LocalEntityHandler
{
  private AttachmentEntityHandler()
  {
    super(Attachment.ENTITY_NAME);
  }

  /**
   * Get an instance of a AttachmentEntityHandler.
   */
  public static AttachmentEntityHandler getInstance()
  {
    Logger.debug("[AttachmentEntityHandler.getInstance]");
    AttachmentEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(Attachment.ENTITY_NAME, true))
    {
      handler = (AttachmentEntityHandler)EntityHandlerFactory.getHandlerFor(
                  Attachment.ENTITY_NAME, true);
    }
    else
    {
      handler = new AttachmentEntityHandler();
      EntityHandlerFactory.putEntityHandler(Attachment.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  public void remove(Long uId) throws Throwable
  {
    Attachment att = (Attachment)getEntityByKeyForReadOnly(uId);
    super.remove(uId);
    FileUtil.delete(IDocumentPathConfig.PATH_ATTACHMENT, att.getFilename());
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAttachmentLocalHome.class.getName(),
      IAttachmentLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAttachmentLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IAttachmentLocalObj.class;
  }
}