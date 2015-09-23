/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DownloadServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-22     Daniel D'Cotta      Created
 * 2002-10-30     Andrew Hill         Log the file retrieval using commons logging
 * 2003-01-20     Andrew Hill         DownloadHelper support & some reorganisation of code
 * 2003-01-27     Andrew Hill         Put down like an old sick dog! Gameovermangameover!
 */
package com.gridnode.gtas.client.web.download;

/*import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.pdip.framework.file.access.FileAccess;
import com.gridnode.gtas.client.utils.StaticUtils; //20030120AH

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.commons.logging.*;*/

/**
 * @deprecated - Use the DownloadAction please! 20030127AH
 */
public class DownloadServlet /*extends HttpServlet implements IDownloadHelper*/
{
  /*private static final String DEFAULT_HELPER_KEY = "DEFAULT_HELPER_KEY"; //20030120AH
  private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";*/

  /*public void init(ServletConfig config) throws ServletException
  {
    Log log = LogFactory.getLog(this.getClass());
    try
    {
      super.init(config);
      //20030120AH - Init default helper in application context
      IDownloadHelper defaultHelper = this;
      if(log.isInfoEnabled())
      {
        log.info("Initialising default IDownloadHelper instance in application context");
      }
      setDefaultHelper(defaultHelper);
      //...
    }
    catch(Throwable t)
    { //20030120AH
      if(log.isErrorEnabled())
      {
        log.error("Error caught in DownloadServlet",t);
      }
      throw new ServletException("Error initialising DownloadServlet",t);
    }
  }*/

  /**
   * Implemented to route all GET requests to the DownloadServlet to the POST method.
   */
  /*public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }*/

  /**
   * Entry point for the DownloadServlet. GET requests are directed here also.
   * nb: Since we arent implemented as a struts Action yet, we may encounter problems if the
   * request is a multipart request and the parameters we need are POSTed rather than appended
   * to the URL.
   */
  /*public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  { //20030120AH - Reorganised code into extra methods for readability
    Log log = LogFactory.getLog(this.getClass());
    try
    {
      IDownloadHelper dlHelper = getDownloadHelper(request);

      String domain = request.getParameter(IRequestKeys.DOMAIN);
      String filepath = request.getParameter(IRequestKeys.FILE_PATH);
      String filename = getFilename(filePath);



      Object dlContext = dlHelper.getDownloadContext(request, filename, domain, filepath);
      StaticUtils.assertNotNull(dlHelper,"dlHelper"); //Internal sanity check
      dlHelper.doPreDownload(request);
      writeFileToResponse(request, response, dlHelper, log);
      dlHelper.doPostDownload(request);
      if(dlHelper.isRemovable()) removeDownloadHelper(request);
    }
    catch(Throwable t)
    { //20030120AH - Handle errors (marginally) better
      if(log.isErrorEnabled()) log.error("Error caught in download servlet",t);
      throw new ServletException("Error caught in download servlet", t);
    }
  }*/

  /**
   * Will return an instance of an IDownloadHelper. This object may be specified by the
   * request to the DownloadServlet by placing in the request parameter named by
   * IDownloadHelper.DOWNLOAD_HELPER_ID_KEY a key that identifies the session attribute under
   * which the download helper is actually stored. If such a key is specified and the corresponding
   * download helper is not found, an exception is thrown. If no download helper key was specified
   * in the request then a default no-op helper is returned (in fact this is implemented as the
   * DownloadServlet object itself as Im lazy...). This method never returns null.
   * 20030120AH
   * @param request
   * @return downloadHelper
   * @throws GTClientException
   */
  /*private IDownloadHelper getDownloadHelper(HttpServletRequest request)
    throws GTClientException
  { //20030120AH - Obtain DownloadHelper object from session if any
    try
    {
      IDownloadHelper dlHelper = null;
      String dlHelperId = getDownloadHelperKey(request);
      if(dlHelperId != null)
      { //If a helper was specified...
        HttpSession httpSession = request.getSession(false);
        StaticUtils.assertNotNull(httpSession,"httpSession"); //If specify helper must have session!
        dlHelper = (IDownloadHelper)httpSession.getAttribute(dlHelperId);
        if(dlHelper == null)
        { //If they said to use a helper and its not there then we have a problem...
          throw new NullPointerException("No IDownloadHelper object found in session with id="
            + httpSession.getId() + " under key '" + dlHelperId + "'");
        }
      }
      if(dlHelper == null)
      {
        dlHelper = getDefaultHelper(request); //No helper found. Use default helper.
      }
      return dlHelper;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining an IDownloadHelper instance",t);
    }
  }*/

  /**
   * Will remove the IDownloadHelper instance from the session attributes. Does not check the
   * helpers isRemovable property as this should have been done already. (This method doenst
   * directly have access to the helper object anyway.) The key in the session is obtained from
   * the request in the same manner as by getDownloadHelper - by calling getDownloadHelperKey()
   * 20030120AH
   * @param request
   * @throws GTClientException
   */
  /*private void removeDownloadHelper(HttpServletRequest request)
    throws GTClientException
  { //20030120AH - Remove DownloadHelper object from session if any
    try
    {
      String dlHelperId = getDownloadHelperKey(request);
      if(dlHelperId != null)
      { //If a helper was specified...
        HttpSession httpSession = request.getSession(false);
        StaticUtils.assertNotNull(httpSession,"httpSession"); //If specify helper must have session!
        httpSession.removeAttribute(dlHelperId);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error removing an IDownloadHelper instance",t);
    }
  }*/

  /**
   * Interrogates the request to determine the key in the session attributes of the IDownloadHelper
   * instance, and returns this, or null if no helper specified.
   * 20030120AH
   * @param request
   * @return dlHelperKey
   */
  /*private String getDownloadHelperKey(HttpServletRequest request)
  { //20030120AH
    String dlHelperId = request.getParameter(IDownloadHelper.DOWNLOAD_HELPER_ID_KEY);
    if(StaticUtils.stringNotEmpty(dlHelperId))
    {
      return dlHelperId;
    }
    else
    {
      return null;
    }
  }*/

  /*private void writeFileToResponse( HttpServletRequest request,
                                    HttpServletResponse response,
                                    IDownloadHelper dlHelper,
                                    Log log)
    throws GTClientException
  { //20030120AH - Factored code out into this method
    try
    {
      String domain = dlHelper.getDomain();
      String filePath = dlHelper.getFilePath();
      String filename = dlHelper.getFilename();

      if(domain == null || domain.equals(""))
      {
        throw new GTClientException("domain is " + ((domain == null) ? "null" : "empty string"));
      }
      if(filePath == null || filePath.equals(""))
      {
        throw new GTClientException("filePath is " + ((filePath == null) ? "null" : "empty string"));
      }

      if(log.isInfoEnabled())
      {
        log.info("Retrieving file: " + filePath);
      }
      FileAccess fileAccess = new FileAccess(domain);
      File file = fileAccess.getFile(filePath);
      if(file == null)
      {
        throw new java.lang.NullPointerException("file retrieved from FileAccess is null");
      }

      String contentType = getContentType(dlHelper, request); //20030120AH
      response.setContentType(contentType); //20030120AH

      String contentDisposition = getContentDisposition(dlHelper, request, contentType, filename); //20030120AH
      response.setHeader("Content-Disposition", contentDisposition);

      ServletOutputStream out = response.getOutputStream();

      byte[] inBytes = new byte[fileAccess.DEFAULT_BUFFER_SIZE];
      FileInputStream fileInputstream = new FileInputStream(file);
      int len = fileInputstream.read(inBytes);
      while(len != -1) // Check for EOF
      {
        out.write(inBytes, 0, len);
        len = fileInputstream.read(inBytes);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error writing file to response",t);
    }
  }*/

  /**Clean up resources*/
  /*public void destroy()
  {
    ;
  }*/

  /**
   * Get the filename without any path
   */
  /*public static String getFilename(String path)
  { //20030120AH
    return path.substring(Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\')) + 1);
  }*/

  /**
   * Tries to return the content type string from the dlHelper. If none is provided by the helper
   * will retrieve it from the default helper.
   * @param dlHelper
   * @param request
   * @return contentType
   * @throws GTClientException
   */
  /*private String getContentType(  IDownloadHelper dlHelper,
                                  HttpServletRequest request)
    throws GTClientException
  { //20030120AH
    try
    {
      String contentType = dlHelper.getContentType(request);
      if(contentType == null)
      { //20030120AH
        contentType = getDefaultHelper(request).getContentType(request);
      }
      return contentType;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining content-type",t);
    }
  }*/


  /**
   * Tries to read the content-disposition string from the dlHelper - and if that doesnt give a
   * result will fallback to retrieving it from the default helper.
   * @param dlHelper
   * @param request
   * @param contentType
   * @param filename
   * @return contentDisposition
   * @throws GTClientException
   */
  /*private String getContentDisposition( IDownloadHelper dlHelper,
                                        HttpServletRequest request,
                                        String contentType,
                                        String filename)
    throws GTClientException
  { //20030120AH
    try
    {
      String contentDisposition = dlHelper.getContentDisposition(request, filename, contentType);
      if(contentDisposition == null)
      {
        contentDisposition = getDefaultHelper(request).getContentDisposition(
                              request, filename, contentType);
      }
      return contentDisposition;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining content-disposition",t);
    }
  }*/

  /**
   * Returns the default IDownloadHelper. This is stored in application context, but this method
   * actually retrieves it from the request attributes! The first time it looks of course, it doesnt
   * find the helper in the request attributes. It then pulls it from the application context and
   * stores it in the request attributes, where later invocations during the processing of this
   * request will find it. This means that if some other thread changes the default helper
   * (unlikely!) this request will still all be processed consistently with the helper it started
   * out with. (This could also have been implemented by grabbing the default helper at the start
   * of doPost and passing it to all the method that might need it as a method parameter...)
   * @param request
   * @return defaultDownloadhelper
   */
  /*private IDownloadHelper getDefaultHelper(HttpServletRequest request)
  { //20030120AH
    synchronized(request)
    {
      IDownloadHelper dlHelper = (IDownloadHelper)request.getAttribute(DEFAULT_HELPER_KEY);
      if(dlHelper == null)
      {
        dlHelper = (IDownloadHelper)getServletContext().getAttribute(DEFAULT_HELPER_KEY);
        request.setAttribute(DEFAULT_HELPER_KEY, dlHelper);
      }
      if(dlHelper == null)
      {
        throw new NullPointerException("Unable to find default IDownloadHelper instance in servletContext");
      }
      return dlHelper;
    }
  }*/

  /**
   * Stores the specified default IDownloadHelper instance in the application context (to facilitate
   * replication with clustered servers (ignoring the fact nothing else works with them yet!))
   * @param defaultHelper
   */
  /*private void setDefaultHelper(IDownloadHelper defaultHelper)
  { //20030120AH
    StaticUtils.assertNotNull(defaultHelper,"defaultHelper");
    getServletContext().setAttribute(DEFAULT_HELPER_KEY, defaultHelper);
  }

  //Following methods implement default IDownloadHelper implementation......................
  public void doPreDownload(HttpServletRequest request) throws GTClientException
  { //20030120AH
    ;
  }
  public void doPostDownload(HttpServletRequest request) throws GTClientException
  { //20030120AH
    ;
  }

  public boolean isRemovable()
  { //20030120AH
    return false; //Default dlHelper is not removable (Its not in the session to begin with!)
  }

  public String getContentType(HttpServletRequest request) throws GTClientException
  { //20030120AH
    return DEFAULT_CONTENT_TYPE; //Use default please
  }

  public String getContentDisposition(HttpServletRequest request,
                                      String filename,
                                      String contentType) throws GTClientException
  { //20030120AH
    //20021204 DDJ: Open inline since now using a new browserwindow
    return "inline; filename=\"" + filename + "\"";
  }*/
  //End of IDownloadHelper default implementation............................................
}