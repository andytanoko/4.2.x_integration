/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentFinder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.xml;

import java.io.InputStream;

/**
 * The document finder when passed a key to identify the document will locate and obtain an input
 * stream to allow reading of the relevant xml (probably xhtml) file. (It is used to obtain the
 * original source file. Not the object cached by the DocumentManager. The document manager makes
 * use of an IDocumentFinder when reading the document for the first time.)
 * The implementing class will abstract away the details of obtaining the resource using the
 * servlet context, or the locl filesystem, an internet url, etc...
 * (sneakernet implementations may require further user intervention ;-)
 */
public interface IDocumentFinder
{
  /**
   * Will attempt to open an input stream on the document identified by documentKey.
   * Throws a BadDocumentException if the key is not registered or if there was an error
   * obtaining the stream.
   * @param documentKey
   * @return InputStream
   * @throws BadDocumentException
   */
  public InputStream getInputStream(String documentKey) throws BadDocumentException;
}