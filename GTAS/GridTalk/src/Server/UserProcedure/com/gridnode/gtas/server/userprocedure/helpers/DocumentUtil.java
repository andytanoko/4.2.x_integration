/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 20 2003    Neo Sok Lay         Created
 * Nov 14 2003    Neo Sok Lay         Add method: getAttachmentOriginalName().
 */
package com.gridnode.gtas.server.userprocedure.helpers;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * Utility for accessing document related stuff.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class DocumentUtil
{
  /**
   * Get the DocumentManagerBean.
   * 
   * @return The DocumentManagerBean.
   * @throws Exception Error obtaining a handle to the DocumentManagerBean.
   */
  public static IDocumentManagerObj getDocumentManager()
     throws  Exception
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
              ServiceLocator.CLIENT_CONTEXT).getObj(
              IDocumentManagerHome.class.getName(),
              IDocumentManagerHome.class,
              new Object[]{});
  }
  
  /**
   * Look for an Outbound GridDocument with the specified GridDoc. ID.
   * 
   * @param gdocId The GridDoc ID to look for.
   * @return The Outbound GridDocument found, or <b>null</b> if none exists.
   * @throws Exception Error while querying from the database.
   */
  public static GridDocument findOutboundGridDocument(String gdocId)
    throws Exception
  {
    IDocumentManagerObj mgr = getDocumentManager();
    return mgr.findGridDocument(
      GridDocument.FOLDER_OUTBOUND,
      Long.valueOf(gdocId));
  }
  
  /**
   * Get the original filename of an Attachment.
   * 
   * @param attachmentUid The UID of the attachment.
   * @return The original filename of the specified attachment.
   * @throws Exception The Attachment could not be found.
   */
  public static String getAttachmentOriginalName(Long attachmentUid)
    throws Exception
  {
    IDocumentManagerObj mgr = getDocumentManager();
    Attachment attachment = mgr.findAttachment(attachmentUid);
    return attachment.getOriginalFilename();
  }
}
