/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 25 2002    Koh Han Sing        Created
 * Aug 02 2002    Koh Han Sing        Add in GridDocument methods
 * Feb 10 2002    Koh Han Sing        Add in attachments methods
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.model.document.DocumentTypeEntityFieldID;
import com.gridnode.gtas.model.document.FileTypeEntityFieldID;
import com.gridnode.gtas.model.document.GridDocumentEntityFieldID;
import com.gridnode.gtas.model.document.AttachmentEntityFieldID;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.Attachment;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Map;

/**
 * This Action class provides common services used by the action classes.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ActionHelper
{
  /**
   * Obtain the EJBObject for the DocumentManagerBean.
   *
   * @return The EJBObject to the DocumentManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IDocumentManagerObj getManager() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  /**
   * Convert an FileType to Map object.
   *
   * @param fileType The FileType to convert.
   * @return A Map object converted from the specified FileType.
   *
   * @since 2.0
   */
  public static Map convertFileTypeToMap(FileType fileType)
  {
    return FileType.convertToMap(
             fileType,
             FileTypeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of FileType to Map objects.
   *
   * @param fileTypeList The collection of FileType to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of FileTypes.
   *
   * @since 2.0
   */
  public static Collection convertFileTypeToMapObjects(Collection fileTypeList)
  {
    return FileType.convertEntitiesToMap(
             (FileType[])fileTypeList.toArray(new FileType[fileTypeList.size()]),
             FileTypeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an GridDocument to Map object.
   *
   * @param gDoc The GridDocument to convert.
   * @return A Map object converted from the specified GridDocument.
   *
   * @since 2.0
   */
  public static Map convertGridDocumentToMap(GridDocument gDoc)
  {
    return GridDocument.convertToMap(
             gDoc,
             GridDocumentEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of GridDocument to Map objects.
   *
   * @param gDocList The collection of GridDocument to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of GridDocuments.
   *
   * @since 2.0
   */
  public static Collection convertGridDocumentToMapObjects(Collection gDocList)
  {
    return GridDocument.convertEntitiesToMap(
             (GridDocument[])gDocList.toArray(new GridDocument[gDocList.size()]),
             GridDocumentEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an Attachment to Map object.
   *
   * @param gDoc The Attachment to convert.
   * @return A Map object converted from the specified Attachment.
   *
   * @since 2.0
   */
  public static Map convertAttachmentToMap(Attachment att)
  {
    return Attachment.convertToMap(
             att,
             AttachmentEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Attachment to Map objects.
   *
   * @param gDocList The collection of Attachment to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Attachments.
   *
   * @since 2.0
   */
  public static Collection convertAttachmentToMapObjects(Collection attList)
  {
    return Attachment.convertEntitiesToMap(
             (Attachment[])attList.toArray(new Attachment[attList.size()]),
             AttachmentEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of DocumentType to Map objects.
   *
   * @param docTypeList The collection of DocumentType to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of DocumentTypes.
   *
   * @since 2.0
   */
  public static Collection convertDocumentTypeToMapObjects(Collection docTypeList)
  {
    return DocumentType.convertEntitiesToMap(
             (DocumentType[])docTypeList.toArray(new DocumentType[docTypeList.size()]),
             DocumentTypeEntityFieldID.getEntityFieldID(),
             null);
  }

}