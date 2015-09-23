/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentFinderImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 29 May 2002    Andrew Hill         Created
 * 06 Jun 2002    Daniel D'Cotta      Added GridForm pages
 * 16 Jun 2002    Andrew Hill         Corrected format of Daniels date written above cos it doesnt look as good as my way ;-)
 * 16 Oct 2002    Daniel D'Cotta      Changed the format of the dates written above so as to follow GridNode documentation standard
 * 2002-10-24     Andrew Hill         Refactored to use DocumentConfig
 */
package com.gridnode.gtas.client.web.xml;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentFinderImpl implements IDocumentFinder
{
  private static final Log _log = LogFactory.getLog(DocumentFinderImpl.class); // 20031209 DDJ

  private DocumentConfig _config;
  private ServletContext _context;

  public DocumentFinderImpl(ServletContext context, DocumentConfig config)
  {
    if(context == null) throw new NullPointerException("context is null"); //20030416AH
    if(config == null) throw new NullPointerException("config is null"); //20030416AH
    _config = config;
    _context = context;
  }

  /**
   * Will try to lookup the mapped URI for the given documentKey from the document
   * descriptor with that key and open an InputStream to that resource.
   * @param documentKey
   * @return inputStream
   * @throws BadDocumentException
   */
  public InputStream getInputStream(String documentKey) throws BadDocumentException
  {
    if(documentKey == null) throw new NullPointerException("documentKey is null"); //20030416AH
    DocumentDescriptor descriptor = _config.getDescriptor(documentKey);
    if(descriptor == null)
    {
      throw new BadDocumentException("Invalid key - no document descriptor for \"" + documentKey + "\"");
    }
    String uri = descriptor.getUri();
    if(_log.isInfoEnabled())
    {
      _log.info("DocumentFinder is looking for a document with the uri:" + uri);
    }
    if(uri == null) throw new NullPointerException("uri is null"); //20030416AH
    try
    {
      InputStream stream = _context.getResourceAsStream(uri);
      if(stream == null)
      {
        throw new java.lang.NullPointerException("ServletContext.getResourceAsStream() returned null input stream");
      }
      return stream;
    }
    catch(Throwable t)
    {
      throw new BadDocumentException("Unable to get InputStream for document \"" + documentKey
                                     + "\" mapped to URI \"" + uri + "\"", t);
    }
  }
}