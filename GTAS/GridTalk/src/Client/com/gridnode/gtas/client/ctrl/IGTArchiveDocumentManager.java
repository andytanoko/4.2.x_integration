/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTArchiveDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTArchiveDocumentManager extends IGTManager
{
  /*
   * Get a new IGTRestoreDocument instance that you can set the values in and pass
   * to the restoreDocuments() method to invoke the restoration of documents from the archive
   * file specified in the IGTRestoreDocument
   * @return restoreDocumentObject
   * @throws GTClientException
   */
  public IGTRestoreDocument getRestoreDocument() throws GTClientException;
  
  /*
   * Invoke restoration of documents form a document archive file. The file must have
   * been transferred to gtas already to the appropriate path, and its name is specified
   * in the ARCHIVE_FILE field of the IGTRestoreDocument instane you pass
   * @param restoreDocument
   * @throws GTClientException
   */
  public void restoreDocuments(IGTRestoreDocument restoreDocument) throws GTClientException;
}