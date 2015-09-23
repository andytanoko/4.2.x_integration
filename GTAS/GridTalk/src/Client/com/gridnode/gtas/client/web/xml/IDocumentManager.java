/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Remove constants for cache type
 */
package com.gridnode.gtas.client.web.xml;

import org.w3c.dom.Document;

/**
 * The document manager allows the retrieval of an org.w3c.dom.Document based on a key
 * passed to identify the required document.
 * It provides abstraction over the details of finding and parsing the xml (usually xhtml in this
 * case) file, and caching of the resulting DOMs for speed.
 */
public interface IDocumentManager
{
  public Document getDocument(String documentKey, boolean requireClone) throws BadDocumentException;
  public String getDocumentHelpKey(String documentKey) throws BadDocumentException;
}