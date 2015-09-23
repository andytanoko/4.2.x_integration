/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EditGridDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-23     Daniel D'Cotta      Created (GridForm 2.0 intergration)
 * Nov 24 2005    Neo Sok Lay         Specify use of Xerces parser implementation to parse doc
 * Feb 12 2007    Neo Sok Lay         Remove attempt to connect to GAIA.
 */
package com.gridnode.gtas.client.web.download;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.pdip.framework.file.access.FileAccess;

public class EditGridDocumentAction extends GTActionBase
{
  protected static final Log _log = LogFactory.getLog(EditGridDocumentAction.class); // 20031209 DDJ

  protected static final String DOWNLOAD_MAPPING = "download";
  protected static final String SAVE_GRID_DOCUMENT_MAPPING = "saveGridDocument";

  public ActionForward execute( ActionMapping mapping,
                                ActionForm actionForm,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception
  {
    try
    {
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
      actionContext.setLog(_log);
      EditGridDocumentAForm form = (EditGridDocumentAForm)actionForm;
      validateFormState(form);

      /*NSL20070212
      String domain = form.getDomain();
      String filePath = form.getFilePath();
      String gridDocId = form.getGridDocId();
      File file = getFile(domain, filePath);
      String root = getRoot(file); */

      ActionForward actionForward = null;
      /*
      try
      {
        // check if managed to obtain GAIA project name
        if(StaticUtils.stringEmpty(root))
        {
          throw new GTClientException("Unable to retrieve GridForm project of document"); // 20040108 DDJ
        }
        
        // check if GAIA can handle the document
        GAIAClient gaiaClient = new GAIAClient();
        boolean isProjectAvailable = gaiaClient.isProjectAvailable(root);
        if(!isProjectAvailable)
        {
          throw new UnsupportedOperationException("GridForm does not support root: " + root);
        }

        // upload the file to GAIA
        String filename = StaticUtils.extractFilename(filePath);
        String desc = filename + " (GTAS GridDocument Id: " + gridDocId + ")";
        int gaiaDocId = gaiaClient.uploadDocument(file, desc);

        // construct the return URL
        // note: currently GAIA appends "http://" to the url
        String saveGridDocumentUrl = request.getServerName() + ":" +
                                     request.getServerPort() +
                                     response.encodeURL(request.getContextPath() + mapping.findForward(SAVE_GRID_DOCUMENT_MAPPING).getPath());
        saveGridDocumentUrl = StaticWebUtils.addParameterToURL(saveGridDocumentUrl, IRequestKeys.GRID_DOC_ID, gridDocId);
        saveGridDocumentUrl = StaticWebUtils.addParameterToURL(saveGridDocumentUrl, IRequestKeys.GAIA_DOC_ID, gaiaDocId + "");
        if(_log.isDebugEnabled())
        {
          _log.debug("saveGridDocumentUrl=" + saveGridDocumentUrl);
        }

        // redirect to the URL given by GAIA
        String gaiaDocUrl = gaiaClient.getUrl(gaiaDocId, saveGridDocumentUrl);
        actionForward = new ActionForward(gaiaDocUrl, true);
      }
      catch(Throwable t)
      {
        if(_log.isErrorEnabled())
        {
          _log.error("Caught error trying to view using GridForm, using default viewer", t);
        }*/
        actionForward = mapping.findForward(DOWNLOAD_MAPPING);
      /*}*/

      return actionForward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error executing EditGridDocumentAction",t);
    }
  }

  protected void validateFormState(EditGridDocumentAForm form)
    throws IllegalStateException
  {
    if(StaticUtils.stringEmpty(form.getDomain()))
      throw new IllegalStateException("domain is undefined");
    if(StaticUtils.stringEmpty(form.getFilePath()))
      throw new IllegalStateException("filePath is undefined");
    if(StaticUtils.stringEmpty(form.getGridDocId()))
      throw new IllegalStateException("gridDocId is undefined");
    if(StaticUtils.primitiveLongValue(form.getGridDocId()) <= 0)
      throw new IllegalStateException("gridDocId is not a valid value");
  }

  // return the file specified
  protected File getFile(String domain, String filePath)
  {
    FileAccess fileAccess = new FileAccess(domain);
    File file = fileAccess.getFile(filePath);
    if(file == null)
    {
      throw new NullPointerException("FileAccess.getFile(" + filePath + ") returned null");
    }
    return file;
  }

  // return the root element
  protected String getRoot(File file)
  {
    String root = null;
    try
    {
      Document document = null;
      //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      //NSL20051124
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

      DocumentBuilder db = dbf.newDocumentBuilder();
      db.setEntityResolver(new DummyResolver());
      document = db.parse(file);
      Node rootNode = document.getDocumentElement();
      root = rootNode.getNodeName();
    }
    catch(Exception ex)
    {
      if(_log.isErrorEnabled())
      {
        _log.error("Error extracting root element", ex);
      }
    }
    return root;
  }
  
  protected class DummyResolver implements EntityResolver
  {
    public DummyResolver()
    {
    }
      
    public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException
    {
      if(_log.isDebugEnabled())
      {
        _log.debug("Returning 'null' for resolveEntity(" + publicId + ", " + systemId + ")");
      }
      return new InputSource(new StringReader(""));
    }
  }
}
