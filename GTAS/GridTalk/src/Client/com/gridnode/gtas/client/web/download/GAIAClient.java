/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GAIAClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-23     Daniel D'Cotta      Created (GridForm 2.0 intergration)
 * 2003-08-11     Daniel D'Cotta      Removed "Commons-HttpClient 2.0 beta 2".jar
 *                                    Repackaged nessecary files as a sub-package
 *                                    till webdavclient.jar is updated or removed
 *                                    due to webdavclient containing old
 *                                    httpclient classes which conflict with this
 */
package com.gridnode.gtas.client.web.download;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gridnode.gtas.client.web.download.httpclient.HttpClient;
import com.gridnode.gtas.client.web.download.httpclient.methods.EntityEnclosingMethod;
import com.gridnode.gtas.client.web.download.httpclient.methods.MultipartPostMethod;
import com.gridnode.gtas.client.web.download.httpclient.methods.PostMethod;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

// Class that communicates with GAIA using the HTTP protocol.
public class GAIAClient
{
  private static final Log _log = LogFactory.getLog(GAIAClient.class);
  private static final Configuration _gridformConfig = ConfigurationManager.getInstance().getConfig(IGridFormConfig.CONFIG_NAME);

  // Default constants.
  private static final int BUFFER_SIZE       = 1024;
  private static final String ATTRIB_SERVICE = "service";

  private static boolean _isInitialised = false;

  private static String SERVER_ADDRESS;
  private static String SERVER_PORT;
  private static String SERVER_URL;

  // Instance variables.
  private HttpClient client = new HttpClient();

  /**
   * Default constructor.
   */
  public GAIAClient()
  {
    if(!_isInitialised)
    {
      SERVER_ADDRESS  = _gridformConfig.getString(IGridFormConfig.SERVER_ADDRESS);
      SERVER_PORT     = _gridformConfig.getString(IGridFormConfig.SERVER_PORT);
      SERVER_URL      = _gridformConfig.getString(IGridFormConfig.SERVER_URL);
      _isInitialised  = true;
      if(_log.isInfoEnabled())
      {
        _log.info("Initialised GridForm configurations");
      }
    }
  }

  /**
   * Get the whole GAIA URL with the service method.
   *
   * @param service The service method.
   * @return The full URL.
   */
  private String getServiceURL(String service)
  {
//    ConfigManager.getInstance().refresh();
    StringBuffer url = new StringBuffer();
    url.append("http://");
    url.append(SERVER_ADDRESS);
    url.append(":");
    url.append(SERVER_PORT);
    url.append("/");
    url.append(SERVER_URL);
    url.append("&");
    url.append(ATTRIB_SERVICE);
    url.append("=");
    url.append(service);
    if(_log.isDebugEnabled())
    {
      _log.debug("url=" + url.toString());
    }
    return url.toString();
  }

  /**
   * Get the URL from GAIA that will present the edit page for the
   * particular document uid.
   * <p>
   * If a return URL is provided, GAIA will forward to it when the document is
   * saved.
   *
   * @param uid The document uid.
   * @param returnUrl The return URL, if any.
   * @return The edit page URL.
   * @throws Exception
   */
  public String getUrl(int uid, String returnUrl) throws Exception
  {
    PostMethod method = new PostMethod(getServiceURL("doGetUrl"));
    method.addParameter("uid", String.valueOf(uid));
    method.addParameter("returnUrl", returnUrl);
    client.executeMethod(method);
    String response = ((EntityEnclosingMethod)method).getResponseBodyAsString();
    BufferedReader reader = new BufferedReader(new StringReader(response));
    method.releaseConnection();
    String url = reader.readLine();
    if(_log.isDebugEnabled())
    {
      _log.debug("url=" + url);
    }
    return url;
  }

  /**
   * Check whether GAIA can edit the particular XML document of the
   * specified root name.
   *
   * @param root The name of the root.
   * @return True if GAIA can edit such XML documents.
   * @throws Exception
   */
  public boolean isProjectAvailable(String root) throws Exception
  {
    PostMethod method = new PostMethod(getServiceURL("doIsProjectAvailable"));
    method.addParameter("root", root);
    client.executeMethod(method);
    String response = method.getResponseBodyAsString();
    BufferedReader reader = new BufferedReader(new StringReader(response));
    method.releaseConnection();
    boolean isProjectAvailable = Boolean.valueOf(reader.readLine()).booleanValue();
    if(_log.isDebugEnabled())
    {
      _log.debug("isProjectAvailable=" + isProjectAvailable);
    }
    return isProjectAvailable;
  }

  /**
   * Upload a XML document to GAIA.
   *
   * @param file The XML document.
   * @param desc Description for the XML document, optional.
   * @return The uid of the XML document.
   * @throws Exception
   */
  public int uploadDocument(File file, String desc) throws Exception
  {
    MultipartPostMethod method = new MultipartPostMethod(getServiceURL("doUploadDocument"));
    method.addParameter("xmlfile", file);
    method.addParameter("desc", desc);
    client.executeMethod(method);
    String response = method.getResponseBodyAsString();
    BufferedReader reader = new BufferedReader(new StringReader(response));
    method.releaseConnection();
    int uid = Integer.parseInt(reader.readLine()); 
    if(_log.isInfoEnabled())
    {
      _log.info("Uploaded document (uid = " + uid + ") to GAIA");
    }
    return uid;
  }

  /**
   * Download a XML document from GAIA.
   *
   * @param uid The uid of the XML document.
   * @return The XML document.
   * @throws Exception
   */
  public File downloadDocument(int uid) throws Exception
  {
    PostMethod method = new PostMethod(getServiceURL("doDownloadDocument"));
    method.addParameter("uid", String.valueOf(uid));
    client.executeMethod(method);
    File tempFile = File.createTempFile("gaia-", null);

    InputStream is = method.getResponseBodyAsStream();
    OutputStream os = new FileOutputStream(tempFile);
    byte[] buffer = new byte[BUFFER_SIZE];
    int size;
    while ((size = is.read(buffer, 0, BUFFER_SIZE)) != -1)
    {
      os.write(buffer, 0, size);
      os.flush();
    }
    os.close();
    method.releaseConnection();
    if(_log.isInfoEnabled())
    {
      _log.info("Downloaded document (uid = " + uid + ") from GAIA");
    }
    return tempFile;
  }

  /**
   * Delete a XML document from GAIA.
   *
   * @param uid The document uid.
   * @throws Exception
   */
  public void deleteDocument(int uid) throws Exception
  {
    PostMethod method = new PostMethod(getServiceURL("doDeleteDocument"));
    method.addParameter("uid", String.valueOf(uid));
    client.executeMethod(method);
    String response = method.getResponseBodyAsString();
    method.releaseConnection();
    if(_log.isInfoEnabled())
    {
      _log.info("Deleted document (uid = " + uid + ") from GAIA");
    }
  }
}
