/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UtCommand.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2003    Neo Sok Lay         Created
 * Jan 15 2004    Neo Sok Lay         Add functionality to be able to transform
 *                                    the input message file into the response
 *                                    message based on a specified Xsl file.
 */
package com.gridnode.simulation.utadaptor;

import java.io.PrintWriter;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Random;

import com.gridnode.ext.util.ExtProperties;
import com.gridnode.ext.util.StringUtil;

/**
 * This is a simulation program for the UtCommand of the actual UtAdaptor.
 * What this program will do is simply:
 * <p><Pre>
 * 1. Check that the <code>org.uccnet.ums.adaptor.messageFileAndPath</code>
 *    property is set and pointing to an existing file.
 * 2. Generate a random response message by printing the message to System.out
 * 3. The response messages (*.xml) are randomly picked from the directory path
 *    specified by the <code>simulator.response.dir</code> property.
 * 4. On occasions where there are exceptions or missing/invalid property set,
 *    an UCCnetErrorMessage will be printed to System.out
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class UtCommand
{
  private static final String MESSAGE_FILE_PROPERTY = "org.uccnet.ums.adaptor.messageFileAndPath";
  //private static final String RESPONSE_FILE = "response.xml";
  private static final String RESPONSE_DIR = "simulator.response.dir";
  private static final String DEFAULT_RESPONSE_DIR = "sim_response";
  private static final String RESPONSE_XSL = "response.xsl";

  private ExtProperties _props = new ExtProperties();
  private String _response = null;
  private XmlFileFilter XML_FILES = new XmlFileFilter();

  private class XmlFileFilter implements FilenameFilter
  {
    public boolean accept(File dir, String name)
    {
      return (name.endsWith(".xml"));
    }

  }

  public UtCommand()
  {
  }

  public String getResponse()
  {
    return _response;
  }

  public int start(String[] args) throws Exception
  {
    int returnCode = 0;
    _props.addPropertiesFromArray(args);
    String msgFile = _props.getProperty(MESSAGE_FILE_PROPERTY, null);
    if (msgFile == null)
    {
      //ERROR
      _response = ErrorMessage.getErrorMessage(
                    "-900",
                    "No Message to send",
                    MESSAGE_FILE_PROPERTY + " is not specified");
      returnCode = -900;
    }
    else if (isTransformResponse())
    {
      returnCode = transformToResponse(msgFile);
    }
    else
    {
      File responseFile = getRandomResponse();
      if (responseFile!=null)
      {
        // print content
        _response = readFileContent(responseFile);
      }
      else
      {
        //ERROR
        _response = ErrorMessage.getErrorMessage(
                    "-901",
                    "No response message returned",
                    "No response message is returned from UCCnet (simulator)");
        returnCode = -901;
      }
    }

    return returnCode;
  }

  private boolean isTransformResponse()
  {
    String responseXsl = _props.getProperty(RESPONSE_XSL, null);
    return !StringUtil.isBlank(responseXsl);
  }

  private int transformToResponse(String msg)
  {
    String responseXsl = _props.getProperty(RESPONSE_XSL);
    int returnCode = 0;

    try
    {
      ResponseMessageTransformer transformer = new ResponseMessageTransformer(responseXsl);
      _response = transformer.getResponseMessage(msg);
    }
    catch (Exception ex)
    {
      //ERROR
      _response = ErrorMessage.getErrorMessage(
                  "-904",
                  "Error transforming response message",
                  ex.toString());
      ex.printStackTrace(System.err);
      returnCode = -904;
    }
    return returnCode;
  }

  private File getRandomResponse()
  {
    String responseDir = _props.getProperty(RESPONSE_DIR, DEFAULT_RESPONSE_DIR);
    File parentDir = new File(responseDir);
    File[] responseFiles = parentDir.listFiles(XML_FILES);
    if (responseFiles == null || responseFiles.length==0)
    {
      return null;
    }
    else
    {
      Random random = new Random(System.currentTimeMillis());
      int fileIndex = random.nextInt(responseFiles.length);
      return responseFiles[fileIndex];
    }
  }

  private String readFileContent(File file) throws Exception
  {
    BufferedReader br = new BufferedReader(new FileReader(file));
    StringBuffer buff = new StringBuffer();
    char[] cbuff = new char[2048];
    int num =-1;
    while ((num=br.read(cbuff))!=-1)
    {
      buff.append(cbuff, 0, num);
      cbuff = new char[2048];
    }
    br.close();
    return buff.toString();
  }

  public static void main(String[] args)
  {
    UtCommand utCommand1 = new UtCommand();
    String response = null;
    int returnCode = 0;
    try
    {
      returnCode = utCommand1.start(args);
      response = utCommand1.getResponse();
    }
    catch (Exception ex)
    {
      response = ErrorMessage.getErrorMessage(
                   "-902",
                   "Error encountered",
                   ex.getMessage());
      returnCode = -902;
    }
    finally
    {
      if (response == null)
      {
        response = ErrorMessage.getErrorMessage(
                     "-903",
                     "Runtime error",
                     "");
        returnCode = -903;
      }
      printResponse(response);
      System.exit(returnCode);
    }

  }

  private static void printResponse(String response)
  {
    PrintWriter pw = new PrintWriter(System.out, true);
    pw.print(response);
    pw.flush();
  }
}