/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocalXHTMLEntityResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2003-07-31     Andrew Hill         Refactored to support fallback modes for dodgy parsers
 */
package com.gridnode.gtas.client.web.xml;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class LocalXHTMLEntityResolver implements EntityResolver
{
  private static final Log _log = LogFactory.getLog(LocalXHTMLEntityResolver.class); // 20031209 DDJ
  private static final String HARDCODED_DTD_URI_PREFIX = "http://www.w3.org/TR/xhtml1/DTD";
  
  //The fallbacks could much better be implemented as objects, but I'll leave that till later
  private static final int FALLBACK_DEFAULT = 0;
  private static final int FALLBACK_PUBLIC = 1;
  private static final int FALLBACK_HARDCODED_ENTITY_1 = 2;

  private static final int[] _fallbacks = new int[]
  {
    FALLBACK_DEFAULT,
    FALLBACK_PUBLIC,
    FALLBACK_HARDCODED_ENTITY_1,
  };
  
  private static final String[] _fallbackDescriptions = new String[]
  {
    "Default - Use systemId as key for document descriptor",
    "Public - Use publicId as key for document descriptor",
    "Hardcoded-Entity-1 - Extract filename from path in systemId and prepend proper path for a .ent file",
  };

  private IDocumentFinder _finder;

  public LocalXHTMLEntityResolver(IDocumentFinder finder)
  {
    _finder = finder;
  }

  public InputSource resolveEntity(String publicId, String systemId)
    throws  SAXException,
            IOException
  {
    for(int mode=0; mode < _fallbacks.length; mode++)
    { //A successful mode will exit the loop via a return statement
      try
      {
        return resolveEntity(publicId, systemId, mode);
      }
      catch(BadDocumentException bde)
      {
        //We will now try the next fallback mode
        if(_log.isWarnEnabled())
        {
          _log.warn("Unable to create InputSource using resolution mode "
                    + mode
                    + " ("
                    + _fallbackDescriptions[mode]
                    + ") due to: "
                    + bde.getMessage());
        }  
      }
      catch(Throwable t)
      {
        _log.error("Unexpected error occured while resolving entity", t);
        return null;
      }
    }
    //All fallback modes exhausted. Leave it to the parser default behaviour which iirc will be to
    //try and pull it from the url specified in the systemId (which for the entity files (.ent) referenced
    //by a dtd should be relative to the dtds url, but under xerces2.4 will in fact be relative
    //to the working directory of the local filesystem. doh!)
    return null;
  }
  
  public InputSource resolveEntity(String publicId, String systemId, int mode)
    throws Exception
  {
    if( (mode <0) || (mode>_fallbackDescriptions.length) )
    { //Sanity check
      throw new IllegalArgumentException("Unrecognised resolution mode:" + mode);
    }
    if(_log.isInfoEnabled())
    {
      _log.info("Resolving entity with publicId=\""
                + publicId
                + "\" systemId=\""
                + systemId
                + "\" using resolution mode "
                + mode
                + " ("
                + _fallbackDescriptions[mode]
                + ")");
    }
    if (_finder == null)
      throw new NullPointerException("_finder is null");
    
    InputStream stream = null;
    switch(mode)
    {
      case FALLBACK_DEFAULT:
      {
        stream = _finder.getInputStream(systemId);  
      }
      break;
      
      case FALLBACK_PUBLIC:
      {
        stream = _finder.getInputStream(publicId);
      }
      break;
      
      case FALLBACK_HARDCODED_ENTITY_1:
      {
        if( systemId.endsWith(".ent") || systemId.endsWith(".dtd") )
        {
          int slash = systemId.lastIndexOf('/');
          String fileName = systemId.substring(slash); //this will include the slash
          String correctedId = HARDCODED_DTD_URI_PREFIX + fileName;
          stream = _finder.getInputStream(correctedId);
        }
        else
        {
          throw new UnsupportedOperationException("This mode only supports a systemId ending in \".ent\" or \".dtd\"");
        }
      }
      break;
      
      default:
      {
        throw new IllegalArgumentException("Unrecognised resolution mode:" + mode);
      }
    }
    //20040317AH - co: return stream == null ? null : new InputSource(stream);
    //20040317AH - Set the systemId on the inputSource so that any references made by this
    //entity which are relative (such as ents in an xhtml dtd) are correctly absolutised
    //See xerces bugzilla report 13202 for more info on this
    if(stream==null)
    {
      return null;
    }
    else
    {
      InputSource inputSource = new InputSource(stream);
      inputSource.setSystemId(systemId);
      inputSource.setPublicId(publicId);
      return inputSource;
    }
    //...
    
  }
}