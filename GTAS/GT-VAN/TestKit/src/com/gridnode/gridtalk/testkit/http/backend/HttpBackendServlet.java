/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpBackendServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 */
package com.gridnode.gridtalk.testkit.http.backend;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling Backend test cases 
 * @author i00107
 */
public class HttpBackendServlet extends HttpServlet
{
  private static final Hashtable<String, HttpBackendTestCaseRunner> _tests = new Hashtable<String, HttpBackendTestCaseRunner>();

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 5218107135079421543L;

  public HttpBackendServlet()
  {
    
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String mode = request.getParameter("mode");
    final String testCaseId = request.getParameter("testcaseid");
    if (mode == null || testCaseId == null)
    {
      log("Received backend request for GET, not supported.");
      response.setContentType("text/html");
      ServletOutputStream os = response.getOutputStream();
      String msg = "Please use POST";
      os.write(msg.getBytes());
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    HttpBackendTestCaseRunner runner = _tests.get(testCaseId);

    response.setContentType("text/html");
    ServletOutputStream os = response.getOutputStream();
    String msg;
    if (mode.equals("start"))
    {
      if (runner != null)
      {
        
        msg = "Test Case "+testCaseId+" is running since "+runner.getTestStartTime() + 
        ". You may either stop it or wait until it finished to start another round of test.";
        log(msg);
        os.write(msg.getBytes());
        response.setStatus(HttpServletResponse.SC_OK);    
      }
      else
      {
        runner = new HttpBackendTestCaseRunner(testCaseId);
        if (runner.isTestCaseValid())
        {
          _tests.put(testCaseId, runner);
          runner.run();
          msg = "Test Case "+testCaseId+" is initiated at "+runner.getTestStartTime();
          log(msg);
          os.write(msg.getBytes());
          response.setStatus(HttpServletResponse.SC_ACCEPTED);    
        }
        else
        {
          msg = runner.getError();
          log(msg);
          os.write(msg.getBytes());
          response.setStatus(HttpServletResponse.SC_OK);    
        }
      }
    }
    else if (mode.equals("stop"))
    {
      if (runner == null)
      {
        msg = "Test Case "+testCaseId+" is not running.";
        log(msg);
        os.write(msg.getBytes());
        response.setStatus(HttpServletResponse.SC_OK);    
      }
      else
      {
        runner.stop();
        _tests.remove(testCaseId);
        msg = "Test Case "+testCaseId+" has been stopped.";
        log(msg);
        os.write(msg.getBytes());
        response.setStatus(HttpServletResponse.SC_OK);    
      }
    }
    else
    {
      msg = "Invalid Parameter value for mode. Expected [start | stop]";
      log(msg);
      os.write(msg.getBytes());
      response.setStatus(HttpServletResponse.SC_OK);    
    }

  }

  public static void removeTestCase(String testCaseID)
  {
    _tests.remove(testCaseID);
  }
  
  
  /**
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    ServletInputStream is = request.getInputStream();
    String docType = request.getHeader("DocType");
    String docFilename = request.getHeader("DocFilename");
    String sender = request.getHeader("SenderId");
    String recipient = request.getHeader("RecipientId");
    String isSaveDoc = request.getParameter("saveDoc");
    printContent(docType, docFilename, sender, recipient, isSaveDoc, is);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }
  
  private void printContent(String docType, String docFilename,
                            String sender, String recipient, String isSaveDoc, 
                            ServletInputStream is) throws IOException
  {
    log("Received backend POST request:>>> Sender: "+sender + ", Recipient: "+recipient+ ", DocType: "+docType+
                       ", DocFilename: "+docFilename+", saveDoc="+isSaveDoc);
    if (isSaveDoc != null && Boolean.valueOf(isSaveDoc))
    {
      File f = new File(sender+"_"+recipient+"_"+docType);
      f.mkdirs();
      File doc = new File(f, docFilename);
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(doc));
      byte[] buff = new byte[1024];
      int len = -1;
      while ((len = is.read(buff))>0)
      {
        bos.write(buff, 0, len);
      }
      bos.close();
      log("Written content to file: "+doc.getAbsolutePath());
    }
    else
    {
      byte[] buff = new byte[1024];
      int len = -1;
      String content = "";
      while ((len = is.read(buff))>0)
      {
        content += new String(buff, 0, len);
      }
      log("Content: "+content);
    }
  }

}
