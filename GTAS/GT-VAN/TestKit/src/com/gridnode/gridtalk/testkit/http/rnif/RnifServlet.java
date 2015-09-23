/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2006    i00107             Created
 * Aug 23 2007    Vineeth            Added methods -"printHubConf", "printTpConf" 
 */

package com.gridnode.gridtalk.testkit.http.rnif;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.testkit.http.Partner;
import com.gridnode.gridtalk.testkit.http.PartnerConfig;
import com.gridnode.gridtalk.testkit.http.backend.HttpBackendHubConfig;

/**
 * @author i00107
 * Servlet to handle RNIF test cases.
 */
public class RnifServlet extends HttpServlet
{
  private static Hashtable<String, RnifTestCaseRunner> _tests = new Hashtable<String, RnifTestCaseRunner>();
  
  /**
   * 
   */
  private static final long serialVersionUID = 5479594314438401777L;

  /**
   * 
   */
  public RnifServlet()
  {
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {

    String mode = request.getParameter("mode");
    final String testCaseId = request.getParameter("testcaseid");
    if (mode == null || testCaseId == null)
    {
      log("Received RNIF request for GET, not supported.");
      response.setContentType("text/html");
      ServletOutputStream os = response.getOutputStream();
      String msg = "Please use POST";
      os.write(msg.getBytes());
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    StringBuffer buf = new StringBuffer(1000);
    buf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 ");
    buf.append("Transitional//EN\">\n");
    buf.append("<HTML>\n");
    buf.append("<HEAD><TITLE>Test Kit</TITLE></HEAD>\n");
    buf.append("<BODY>");
    RnifTestCaseRunner runner = _tests.get(testCaseId);

    response.setContentType("text/html");
    ServletOutputStream os = response.getOutputStream();
    String msg;
    File tpConfile = null;
    File hubConfile = null;
    
    buf.append("<p><a href = \"rnif?mode=status&testcaseid=" + testCaseId + "\">> Status <</a></p>");
    
    if (mode.equals("start"))
    {
      if (runner != null)
      {	
    	msg = "Test Case "+testCaseId+" is running since "+runner.getTestStartTime() + 
        ". You may either stop it or wait until it finished to start another round of test.";
        log(msg);
        buf.append(msg);
        response.setStatus(HttpServletResponse.SC_OK);    
      }
      else
      {
        runner = new RnifTestCaseRunner(testCaseId);
        if (runner.isTestCaseValid())
        { 
          _tests.put(testCaseId, runner);
          runner.run();
          msg = "Test Case " + testCaseId + "is initiated at " + runner.getTestStartTime();
          log(msg);
          buf.append(msg);
          response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else
        {
          msg = runner.getError();
          log(msg);
          buf.append(msg.getBytes());
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
        buf.append(msg);
        response.setStatus(HttpServletResponse.SC_OK);    
      }
      else
      {
        runner.stop();
        _tests.remove(testCaseId);
        msg = "Test Case "+testCaseId+" has been stopped.";
        log(msg);
        buf.append(msg);        
        response.setStatus(HttpServletResponse.SC_OK);    
      }
    }
    else if (mode.equals("status"))
    {
    	if (runner != null)
        {	
    		msg = "Test Case "+testCaseId+" is running since " + runner.getTestStartTime();
    	}
    	else
    	{
    		msg = "Test Case " + testCaseId + " is not running.";
    	}
    	buf.append(msg);
    }
    else
    {
      msg = "Invalid Parameter value for mode. Expected [start | stop | status]";
      log(msg);
      buf.append(msg);
      response.setStatus(HttpServletResponse.SC_OK);    
    }

    RnifHubConfig hubConfig = null;
    PartnerConfig tpConfig = null;
 
    if (runner == null)
     {
    	 tpConfile = getFile(testCaseId, "tp.conf"); 
    	 hubConfile = getFile(testCaseId, "hub.conf"); 
         hubConfig = new RnifHubConfig(hubConfile, true);
    	 tpConfig = new PartnerConfig(tpConfile);

     }
     else
     {
    	hubConfig = runner.getHubConfig();
    	tpConfig = runner.getPartnerConfig();
     }

     printHubConf(hubConfig, buf).append("\n");
  	 printTpConf(tpConfig, buf);
     os.write(buf.toString().getBytes());
  }
  
  /**
   * prints the hub config
   * @param RnifHubConfig hubConfig object
   * @param StringBuffer buf 
   * @return returns the String buffer
   */
  private StringBuffer printHubConf(RnifHubConfig hubConfig, StringBuffer buf)
  {
	
    buf.append("<table id=\"status_table\" width=\"70%\"  border=\"1\" cellspacing=\"2\" cellpadding=\"0\">\n");
    buf.append("<tr><th>Property</th><th>Value</th></tr>\n");
    buf.append("<tr>\n");
	buf.append("<td id=\"duns\">duns</td>\n");
	buf.append("<td id=\"duns_value\">" + hubConfig.getDuns() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("<td id=\"url\">url</td>\n");
	buf.append("<td id=\"url_value\">" + hubConfig.getUrlStr() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("<td id=\"send_interval\">send interval</td>\n");
	buf.append("<td id=\"send_interval_value\">" + hubConfig.getSendIntervalStr() + "</td>\n");
	buf.append("</tr>\n");	
	buf.append("<tr>\n");
	buf.append("<td id=\"send_num\">send num</td>\n");
	buf.append("<td id=\"send_num_value\">" + hubConfig.getSendNumTxStr() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("<td id=\"action_template\">action template</td>\n");
	buf.append("<td id=\"action_template_value\">" + hubConfig.getActionTemplate() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("<td id=\"ack_template\">ack template</td>\n");
	buf.append("<td id=\"ack_template_value\">" + hubConfig.getAckTemplate() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("<tr>\n");
	buf.append("<td id=\"ack_properties\">ack properties</td>\n");
	buf.append("<td id=\"ack_properties_value\">" + hubConfig.getAckProp() + "</td>\n");
	buf.append("</tr>\n");
	buf.append("</table>");   
    
	return buf;
  }
  
  /**
   * prints the Trading partner table
   * @param PartnerConfig tpConfig object
   * @param StringBuffer buf 
   * @return returns the String buffer
   */
  private StringBuffer printTpConf(PartnerConfig tpConfig, StringBuffer buf)
  {
	  buf.append("<br>\n");
	  buf.append("<table id=\"tp_table\" width=\"70%\"  border=\"1\" cellspacing=\"2\" cellpadding=\"0\">\n");
	  buf.append("<tr>\n");
	  buf.append("<th id=\"Key\">Key</th>\n");
	  buf.append("<th id=\"key_value\">Total tx to send</th>\n");
	  buf.append("<th id=\"key_value\">Total tx sent</th>\n");
	  buf.append("</tr>\n");  	
	  
	  List<Partner> partnerList = new ArrayList<Partner>(tpConfig.getPartnerList());
	  Collections.sort(partnerList);
	  Partner[] listArraySorted = new  Partner[partnerList.size()];
	  listArraySorted = partnerList.toArray(listArraySorted);
	  
	  for (int i = 0 ; i < listArraySorted.length ; i++)
	  {
		  	buf.append("<tr>\n");
	  		buf.append("<td>" + listArraySorted[i].getId() + "</td>\n");
	  		buf.append("<td>" + listArraySorted[i].getNumTx() + "</td>\n");
	  		buf.append("<td>" + listArraySorted[i].getCounter() + "</td>\n");
	  		buf.append("</tr>\n");  	
	  }
	  
	  return buf;
  }
  /**
   * Creates new file
   * @param String testCaseId
   * @param String fileName 
   * @return returns the created file
   */
  private File getFile(String testCaseId, String fileName)
  {
	  File baseDir = new File("conf", testCaseId);
	  File f = new File(baseDir, fileName);
	  return f;
  }
  
  public static void removeTestCase(String testCaseID)
  {
    _tests.remove(testCaseID);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    final String testCaseId = request.getParameter("testcaseid");
    ServletInputStream is = request.getInputStream();
    String rnifVersion = request.getHeader("x-RN-Version");
    String synchronous = request.getHeader("X-RN-Response-Type");
    String content = printContent(rnifVersion, synchronous, is);
    response.setStatus(HttpServletResponse.SC_ACCEPTED);
    handleMsg(testCaseId, content);
    
  }

  private void handleMsg(String testCaseId, String content)
  {
    //check if ack or action msg
    //System.out.println("Content is "+content);
    int svcContentIdx = content.indexOf("RN-Service-Content");
    if (svcContentIdx < 0)
    {
      log("Received message is not RosettaNet, ignore.");
      return;
    }
    int ackDocTypeIdx = content.indexOf("<!DOCTYPE ReceiptAcknowledgment", svcContentIdx);
    if (ackDocTypeIdx < 0)
    {
      //action msg, need to return ack
      RnifTestCaseRunner runner = new RnifTestCaseRunner(testCaseId, true);
      if (runner.isTestCaseValid())
      {
        runner.sendAck(content);
      }
      else
      {
        log("Invalid test case: "+runner.getError());
      }
      
    }
    else
    {
      //ack msg
      log("Received ACK, Content: " + content);
    }
  }
  
  
  private String printContent(String rnifVersion, String synchronous,
                            ServletInputStream is) throws IOException
  {
    log("Received RNIF POST request>>> RnifVersion: " + rnifVersion + ", ResponseType is " + synchronous);
    
    byte[] buff = new byte[1024];
    int len = -1;
    String content = "";
    while ((len = is.read(buff))>0)
    {
      content += new String(buff, 0, len);
    }    
    String pipInstId = RnifTestCaseRunner.findPipInstId(content);
    String senderDuns = RnifTestCaseRunner.findSenderDuns(content);
    String recpDuns = RnifTestCaseRunner.findReceiverDuns(content);
    String pipCode = RnifTestCaseRunner.findPipCode(content);
    log("PipInstId: " + pipInstId + ", SenderDuns: " + senderDuns + ", ReceiverDuns: " + recpDuns);
    File f = new File(pipCode + "_" + senderDuns + "_" + recpDuns);
    f.mkdirs();
    File doc = new File(f, (++count)+"_"+System.currentTimeMillis()+".xml");
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(doc));
    bos.write(content.getBytes());
    bos.close();
    log("Written content to file: "+doc.getAbsolutePath());
    return content;
  }
  
  static int count = 0;
}
