/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DownloadAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Andrew Hill         Created (Replaces DownloadServlet)
 * 2006-06-21     Tam Wei Xiang       Modified method writeFileToResponse(...) :
 *                                    close the FileInputStream and SevletOutputStream
 *                                    after used. This fixed the defect GNDB00026735
 */
package com.gridnode.gtas.client.web.download;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.pdip.framework.file.access.FileAccess;

public class DownloadAction extends Action implements IDownloadHelper
{
  private static final Log _log = LogFactory.getLog(DownloadAction.class); // 20031209 DDJ

  private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream"; //20030128AH

  //No-op dlHelper methods (null object pattern)
  public void doPreDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException
  {
    
  }

  public void doPostDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException
  {
    
  }
  //...


  //Doing this in an action instead of a servlet allows us to
  //leverage the exception handler we set up and other code that actions get (like getlocale)
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
      DownloadAForm form = (DownloadAForm)actionForm;

      initForm(actionContext, form);
      IDownloadHelper dlHelper = getDownloadHelper(actionContext);
      doFileDownload(actionContext, form, dlHelper );

      return null; //We have written response already
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error executing DownloadAction",t);
    }
  }

  private void initForm(ActionContext actionContext, DownloadAForm form)
    throws GTClientException
  {
    try
    {
      String filePath = form.getFilePath();
      String filename = StaticUtils.extractFilename(filePath);
      form.setFilename(filename);
      form.setActualFilename(filename);
      form.setContentType(DEFAULT_CONTENT_TYPE);
      setContentDisposition(actionContext, form, filename); //20030218AH
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing download ActionForm",t);
    }
  }

  private void setContentDisposition(ActionContext actionContext, DownloadAForm form, String filename)
  { //20030218AH
    String contentDisposition = "inline; filename=\"" + filename + "\"";
    form.setContentDisposition(contentDisposition);
  }

  private void doFileDownload(ActionContext actionContext,
                              DownloadAForm form,
                              IDownloadHelper dlHelper)
    throws GTClientException
  {
    try
    {
      dlHelper.doPreDownload(actionContext, form);
      validateFormState(actionContext, form);
      writeFileToResponse( actionContext, form);
      dlHelper.doPostDownload(actionContext, form);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error performing file download",t);
    }
  }

  private void validateFormState(ActionContext actionContext, DownloadAForm form)
    throws IllegalStateException
  {
    if(StaticUtils.stringEmpty(form.getDomain()))
      throw new IllegalStateException("domain is undefined");
    if(StaticUtils.stringEmpty(form.getFilePath()))
      throw new IllegalStateException("filePath is undefined");
    if(StaticUtils.stringEmpty(form.getFilename()))
      throw new IllegalStateException("filename is undefined");
    if(StaticUtils.stringEmpty(form.getContentType()))
      throw new IllegalStateException("contentType is undefined");
    if(StaticUtils.stringEmpty(form.getContentDisposition()))
      throw new IllegalStateException("contentDisposition is undefined");
  }

  private void writeFileToResponse( ActionContext actionContext, DownloadAForm form )
    throws GTClientException
  {
  	FileInputStream fileInputStream = null;
  	ServletOutputStream out = null;
    try
    {
      Log log = actionContext.getLog();
      if(log.isInfoEnabled()) log.info("Retrieving file: " + form.getFilePath() );
      FileAccess fileAccess = new FileAccess( form.getDomain() );

      actionContext.getResponse().setContentType(form.getContentType());
      actionContext.getResponse().setHeader("Content-Disposition", form.getContentDisposition());

      String actualFilename = form.getFilePath();
      String actualFullPath = StaticUtils.extractPath(actualFilename) + form.getActualFilename();
      File file = fileAccess.getFile( actualFullPath );
      if(file == null)
      {
        throw new NullPointerException("FileAccess.getFile(" + actualFullPath + "returned null");
      }
      fileInputStream = new FileInputStream(file);
      if(fileInputStream == null)
      {
        throw new NullPointerException("FileInputStream for " + file + " is null");
      }

      //Try to read first block of bytes from the file
      byte[] inBytes = new byte[fileAccess.DEFAULT_BUFFER_SIZE];
      int len = fileInputStream.read(inBytes);

      //Try and delay getting output stream as long as possible because if an error occurs and we
      //got it already we wont be able to render the error!
      out = actionContext.getResponse().getOutputStream();

      //Iterate through all the bytes writing them out one block at a time
      while(len != -1) // Check for EOF
      {
        out.write(inBytes, 0, len);
        len = fileInputStream.read(inBytes);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error writing file to response",t);
    }
    finally
    {
    	//TWX 21062006
    	if(fileInputStream != null)
    	{
    		try
    		{
    			fileInputStream.close();
    		}
    		catch(Exception ex)
    		{
    			_log.info("[DownloadAction.writeFileToResponse] Error in closing fileInputStream. "+ ex.getMessage(), ex);
    		}
    	}
    	if(out != null)
    	{
    		try
    		{
    			out.close();
    		}
    		catch(Exception ex)
    		{
    			_log.info("[DownloadAction.writeFileToResponse] Error in closing SevletOutputStream. "+ ex.getMessage(), ex);
    		}
    	}
    } 
  }

  private String getDownloadHelperKey(HttpServletRequest request)
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
  }

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
  private IDownloadHelper getDownloadHelper(ActionContext actionContext)
    throws GTClientException
  { //20030120AH - Obtain DownloadHelper object from session if any
    try
    {
      HttpServletRequest request = actionContext.getRequest();
      IDownloadHelper dlHelper = null;
      String dlHelperId = getDownloadHelperKey(request);
      if(dlHelperId != null)
      { //If a helper was specified...
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null)
          throw new NullPointerException("httpSession is null"); //20030424AH
        dlHelper = (IDownloadHelper)httpSession.getAttribute(dlHelperId);
        if(dlHelper == null)
        { //If they said to use a helper and its not there then we have a problem...
          throw new NullPointerException("No IDownloadHelper object found in session with id="
            + httpSession.getId() + " under key '" + dlHelperId + "'");
        }
      }
      if(dlHelper == null)
      {
        dlHelper = this;
      }
      return dlHelper;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining an IDownloadHelper instance",t);
    }
  }
}
