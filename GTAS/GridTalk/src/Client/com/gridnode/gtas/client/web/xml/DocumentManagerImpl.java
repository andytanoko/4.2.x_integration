/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentManagerImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2002-10-21     Andrew Hill         Corrected a CACHE_MEMORY logic error
 * 2002-10-24     Andrew Hill         Use DocumentConfig objects and support individual cache settings
 * 2002-10-31     Andrew Hill         Preload logging
 * Nov 24 2005    Neo Sok Lay         Specify use of Xerces parser to get Document
 */
package com.gridnode.gtas.client.web.xml;

//testing:
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;

public class DocumentManagerImpl implements IDocumentManager
{
  private static final Log _log = LogFactory.getLog(DocumentManagerImpl.class); // 20031209 DDJ

  private LocalXHTMLEntityResolver _entityResolver; // Used in parsing to get local copy of DTDs
  private IDocumentFinder _finder; // Used to locate the source xml file
  private Hashtable _domCache; // Hashtable that stores either File or Document objects
  private File _tempdir; // Used to obtain file reference to a temporary directory
  private DocumentConfig _config;

  public DocumentManagerImpl( IDocumentFinder documentFinder,
                              File tempdir,
                              DocumentConfig documentConfig)
  {
    if(documentFinder == null) throw new NullPointerException("documentFinder is null"); //20030416AH
    if(tempdir == null) throw new NullPointerException("tempdir is null"); //20030416AH
    if(documentConfig == null) throw new NullPointerException("documentConfig is null"); //20030416AH

    _entityResolver = new LocalXHTMLEntityResolver(documentFinder);
    _finder = documentFinder;
    _tempdir = tempdir;
    _config = documentConfig;
    _domCache = new Hashtable();
  }

  /**
   * Will obtain the specified document either from a resource or a cache. If document is
   * not in the cache will cache it if caching is enabled. If the returned document is to be
   * modified in any way, then you MUST specify true for requireClone parameter so as to get your
   * own clone to work on.
   * @param documentKey
   * @param requireClone
   * @return Document
   * @throws BadDocumentException
   */
  public synchronized Document getDocument(String documentKey, boolean requireClone)
    throws BadDocumentException
  {
    Document document = null;
    try
    {
      DocumentDescriptor descriptor = _config.getDescriptor(documentKey);
      if(descriptor == null)
      {
        throw new BadDocumentException("Invalid key - no document descriptor for \"" + documentKey + "\"");
      }
      // Try and retrieve an object from the hashtable in relation to this document.
      Object cachedDom = _domCache.get(documentKey);
      // Now we check to see what we found in the hashtable and take appropriate action.
      if(cachedDom == null)
      { // dom not cached yet, so read and cache it, returning read dom
        document = readAndCacheDocument(documentKey, descriptor);
      }
      else if(cachedDom instanceof File)
      {
        try
        {
          document = readCachedDocument((File)cachedDom);
        }
        catch(BadDocumentException bde)
        { // Check exception. If its an io exception we shall assume the temp file was erased but
          // anything else is a real problem so we throw it again.
          Throwable t = bde.getNestedException();
          if(t == null) throw bde;
          if(t instanceof java.io.FileNotFoundException)
          {
            _domCache.remove(documentKey); // so remove from the lookup table
            document = getDocument(documentKey, requireClone); // and try again (this time will re-parse)
          }
          else
          {
            throw bde;
          }
        }
      }
      else if(cachedDom instanceof Document)
      { // Return the dom from memory, cloning if requested
        if(requireClone)
        {
          document = (Document)StaticUtils.cloneObject(cachedDom);
        }
        else
        {
          document = (Document)cachedDom;
        }
      }
      else
      { // sanity check - throw exception if found anything else!
        throw new BadDocumentException("DOM cache corrupted. - not expecting "
                                        + cachedDom.getClass().getName() + " for document "
                                        + documentKey);
      }
    }
    catch(Throwable t)
    {
      throw new BadDocumentException("Unable to obtain document for key=" + documentKey, t);
    }
    return document;
  }

  /**
   * Uses SAX and DOM to read the xml from the input stream and create a DOM tree.
   * @param inputStream
   * @returns Document
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public Document readDom(InputStream inputStream, boolean validate)
    throws  javax.xml.parsers.ParserConfigurationException,
            java.io.IOException,
            org.xml.sax.SAXException
  {
    Document document = null;
    //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    //NSL20051123
    DocumentBuilderFactory dbf = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
    /*
    DocumentBuilderFactory dbf;
    ClassLoader oldLoader = null;
    String oldFactory = System.getProperty("javax.xml.parsers.DocumentBuilderFactory");
    try
    {
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
      oldLoader = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      dbf = DocumentBuilderFactory.newInstance();
    }
    finally
    {
      if (oldLoader != null)
        Thread.currentThread().setContextClassLoader(oldLoader);
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", oldFactory);
    }*/
    
    dbf.setExpandEntityReferences(false);
    dbf.setIgnoringComments(false);
    dbf.setIgnoringElementContentWhitespace(true);
    dbf.setValidating(validate);
    DocumentBuilder db = dbf.newDocumentBuilder();
    if(_entityResolver != null) db.setEntityResolver(_entityResolver);
    document = db.parse(inputStream);
    return document;

    /*DOMParser parser = new DOMParser();
    InputSource source = new InputSource(inputStream);
    parser.setEntityResolver(_entityResolver);
    parser.parse(source);
    return parser.getDocument();*/
  }

  /**
   * Will make use of the document finder to obtain an input stream for the document and will read
   * the document into a DOM and cache it under the documentKey using the specified caching method.
   * (Will throw IllegalArgumentException if cacheType is not valid)
   * @param documentKey key to identify the document to the finder and in the cache.
   * @param cacheType as defined in IDocumentManager
   * @return Document created by parsing source xml file
   * @throws BadDocumentException
   */
  private Document readAndCacheDocument(String documentKey, DocumentDescriptor descriptor) throws BadDocumentException
  {
    try
    {
      InputStream stream = _finder.getInputStream(documentKey);
      Document dom = readDom(stream, descriptor.isValidate());
      String cacheType = descriptor.getCache();
      if(DocumentDescriptor.CACHE_MEMORY.equals(cacheType))
      {
        cacheInMemory(documentKey, (Document)StaticUtils.cloneObject(dom));
      }
      else if(DocumentDescriptor.CACHE_FILE.equals(cacheType))
      {
        cacheInTempdir(documentKey, dom);
      }
      else if(DocumentDescriptor.CACHE_NONE.equals(cacheType))
      {
        
      }
      return dom;
    }
    catch(java.io.IOException ioe)
    {
      throw new BadDocumentException("Error reading document", ioe);
    }
    catch(org.xml.sax.SAXException se)
    {
      throw new BadDocumentException("Error parsing document", se);
    }
    catch(BadDocumentException bde)
    {
      throw bde;
    }
    catch(Throwable t)
    {
      throw new BadDocumentException("Error occured while reading and caching document", t);
    }
  }

  /**
   * Store the document in memory for quick future retrieval.
   * @param documentKey
   * @param dom
   */
  private void cacheInMemory(String documentKey, Document document)
  {
    _domCache.put(documentKey, document);
  }

  /**
   * Store the document in the temp dir as a serialized object to allow
   * fasetr retrieval by avoiding having to parse again.
   * A File object will be cached referring to the saved dom.
   * @param documentKey
   * @param dom
   * @throws BadDocumentException
   */
  private void cacheInTempdir(String documentKey, Document document) throws  BadDocumentException
  {
    try
    {
      File domFile = File.createTempFile("cache-" + documentKey, ".tmp", _tempdir);
      FileOutputStream fos = new FileOutputStream(domFile);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(document);
      oos.flush();
      oos.close();
      domFile.deleteOnExit();
      _domCache.put(documentKey, domFile);
    }
    catch(Exception e)
    {
      throw new BadDocumentException("Error caching document " + documentKey + " in tempdir",e);
    }
  }

  /**
   * Retrieve a dom cached in the temp directory given a File that refers to it.
   * @param domObjectFile
   * @return Document
   * @throws BadDocumentException
   */
  private Document readCachedDocument(File domFile) throws BadDocumentException
  {
    try
    {
      FileInputStream fis = new FileInputStream(domFile);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Document dom = (Document)ois.readObject();
      ois.close();
      return dom;
    }
    catch(Exception e)
    {
      throw new BadDocumentException("Unable to read cached document", e);
    }
  }

  public void preload() throws GTClientException
  {
    try
    {
      Iterator i = _config.getDescriptors();
      while(i.hasNext())
      {
        DocumentDescriptor descriptor = (DocumentDescriptor)i.next();
        if( descriptor.isPreload()
            && (DocumentDescriptor.CACHE_NONE.equals( descriptor.getCache() ) == false )
            && descriptor.isXml() )
        {
          long startTime = System.currentTimeMillis();
          
          getDocument(descriptor.getKey(), false);
          
          long endTime = System.currentTimeMillis();
          if(_log.isInfoEnabled())
          {
            _log.info( "Preloaded document for key '"
                      + descriptor.getKey()
                      + "' (in approx "
                      + (endTime - startTime) + " ms)" );
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preloading documents",t);
    }
  }

  public String getDocumentHelpKey(String documentKey) throws BadDocumentException
  {
    try
    {
      DocumentDescriptor descriptor = _config.getDescriptor(documentKey);
      if(descriptor != null)
      {
        return descriptor.getHelpKey();
      }
      else
      {
       throw new BadDocumentException("Invalid key - no document descriptor for \"" + documentKey + "\"");
      }
    }
    catch(Throwable t)
    {
      throw new BadDocumentException("Unable to obtain document helpKey for key=" + documentKey, t);
    }
  }
}