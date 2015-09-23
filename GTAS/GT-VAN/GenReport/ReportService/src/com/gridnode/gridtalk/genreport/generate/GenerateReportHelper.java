/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenerateReportHelper.java
 *
 ****************************************************************************
 * Date                Author              Changes
 ****************************************************************************
 * Apr 5, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.generate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

public class GenerateReportHelper
{
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_GENERATE, "GenerateReportHelper");
  
  public GenerateReportHelper()
  {    
  }
  
  protected boolean generateOnline(String filename, OutputStream os, HttpServletResponse response, String contentType)
  {
    String mn = "generateOnline";
    Object[] params = {filename, os, response, contentType};
    boolean status = false;
    try
    {
      File file = new File(getEnginePath(filename));
      InputStream is = new FileInputStream(file);
      long fileLength = file.length();
      byte[] b = new byte[(int)fileLength];
        
      int offset = 0;
      int numRead = 0;
      while(offset < b.length && (numRead = is.read(b, offset, b.length - offset)) >=0)
      {
        offset +=numRead;
      }
      if(offset < b.length)
      {
        _logger.logWarn(mn, params, "Could not completely read file " +file.getName() , null);
      }
      is.close();
          
      response.setContentType(contentType);
      response.setContentLength((int)fileLength);
      response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
       
      ByteArrayInputStream input = new ByteArrayInputStream(b);
      byte[] buffer = new byte[512];
      int readSoFar = 0;
      while( (readSoFar = input.read(buffer)) != -1)
      {
        os.write(buffer,0,readSoFar);
      }
      os.flush();
      input.close();
      status = true;
    }
    catch(FileNotFoundException e)
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "File Not Found Error. Unable to find the file "+filename, e);
    }
    catch(IOException e)
    {
      _logger.logError(ILogErrorCodes.REPORT_GENERATOR_GENERATE_ONLINE, mn, null, "IO Error. Unable to generate report online.", e);
    }
    return status;
  } 
  
  protected String getReportMain(String reportTemplate)
  {
    File f = new File(SystemUtil.getWorkingDirPath(), IReportConstants.TEMPLATE_PATH+reportTemplate+".jrxml");
    return f.getAbsolutePath(); 
  }
  
  protected String getReportSub(String reportTemplate)
  {
    File f = new File(SystemUtil.getWorkingDirPath(), IReportConstants.TEMPLATE_PATH+reportTemplate+"Sub.jrxml");
    return f.getAbsolutePath(); 
  }  
  
  protected String getSubReportDir(String reportTemplate)
  {
    File f = new File(SystemUtil.getWorkingDirPath(), IReportConstants.TEMPLATE_PATH);
    return f.getAbsolutePath()+"/";   
  }
  
  protected String getEnginePath(String filename)
  {
    File f = new File(SystemUtil.getWorkingDirPath(), IReportConstants.ENGINE_PATH+"/"+filename);
    return f.getAbsolutePath();
  }
}
