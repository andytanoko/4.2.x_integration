/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentConfigManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Andrew Hill         Created
 * 2002-10-31     Andrew Hill         Logging
 * 2002-11-25     Andrew Hill         Throw Exceptions rather than ServletExceptions
 * 2003-07-01     Andrew Hill         Modify init() to suit 1.1 final signature, use non-deprecated Digester syntax
 */
package com.gridnode.gtas.client.web.xml;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.digester.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class DocumentConfigManager implements PlugIn
{
  private static final Log _log = LogFactory.getLog(DocumentConfigManager.class); // 20031209 DDJ

  public static final String DOCUMENT_CONFIG_CLASS = "com.gridnode.gtas.client.web.xml.DocumentConfig";
  public static final String DESCRIPTOR_CLASS = "com.gridnode.gtas.client.web.xml.DocumentDescriptor";

  private static final String DESCRIPTOR_MAPPING = "document-config/descriptors/document";
  private static final String DOCUMENT_CONFIG_MAPPING = "document-config";

  private String _configUrl;
  private ServletContext _context;
  private DocumentConfig _documentConfig;

  public void setConfigUrl(String configUrl)
  {
    _configUrl = configUrl;
  }

  public String getConfigUrl()
  {
    return _configUrl;
  }

  public void destroy()
  {
    
  }

  public void init(ActionServlet servlet, ModuleConfig config) throws ServletException //20030701 - Use ModuleConfig parameter and throw ServletException
  {
    try
    {
      _context = servlet.getServletContext();
      String configUrl = getConfigUrl();
      if(_log.isInfoEnabled())
      {
        _log.info("Parsing document configuration file: \"" + configUrl + "\"");
      }
      if(configUrl == null) throw new NullPointerException("configUrl is null"); //20030416AH
      InputStream stream = _context.getResourceAsStream(configUrl);
      if(stream == null) throw new NullPointerException("stream is null"); //20030416AH
      Digester digester = new Digester();
      digester.setNamespaceAware(false);
      digester.setValidating(false);
      //digester.setLogger(_log); // 20031211 DDJ: Commented out to allow it to use its own class to log

      //20030701AH - Use the non-deprecated sytax to setup rules
      Rule setProperties = new SetPropertiesRule();

      Rule documentConfig = new ObjectCreateRule(DOCUMENT_CONFIG_CLASS);
      Rule addConfig = new SetNextRule("setDocumentConfig",DOCUMENT_CONFIG_CLASS);
      digester.addRule(DOCUMENT_CONFIG_MAPPING,documentConfig);
      digester.addRule(DOCUMENT_CONFIG_MAPPING,addConfig);

      Rule descriptor = new ObjectCreateRule(DESCRIPTOR_CLASS);
      Rule addDescriptor = new SetNextRule("addDescriptor", DESCRIPTOR_CLASS);
      //...
      
      digester.addRule(DESCRIPTOR_MAPPING,descriptor);
      digester.addRule(DESCRIPTOR_MAPPING,setProperties);
      digester.addRule(DESCRIPTOR_MAPPING,addDescriptor);

      digester.push(this);
      digester.parse(stream);

      _documentConfig.freeze();

      instantiateManager();
    }
    catch(Throwable t)
    {
      if(_log.isErrorEnabled())
      {
        _log.error("Exception thrown in DocumentConfigManager", t);
        _log.error("Root cause", StaticWebUtils.getRootException(t));
      }
      throw new ServletException("Error initialising document config", t); //20030701AH - Use ServletException as interface no longer supports other types
    }
  }

  public void setDocumentConfig(DocumentConfig config)
  {
    _documentConfig = config;
  }

  private void instantiateManager()
    throws Exception
  {
    try
    {
      File tempdir = (File)_context.getAttribute(IGlobals.TEMPDIR);
      DocumentFinderImpl docFinder = new DocumentFinderImpl(_context, _documentConfig);
      DocumentManagerImpl docMgr = new DocumentManagerImpl(docFinder, tempdir, _documentConfig);
      docMgr.preload();
      _context.setAttribute(IGlobals.DOCUMENT_FINDER, docFinder);
      _context.setAttribute(IGlobals.DOCUMENT_MANAGER, docMgr);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing document management classes in app context",t);
    }
  }
}