/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CustomSenderLite.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Wang Khong Hai        Created
 */

package com.gridnode.gtas.backend.sender;
//package com.gridnode.backend.sender;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gridnode.gridmail.GridMail;
import com.gridnode.gridmail.IGmail;
import com.gridnode.gtas.backend.exception.ConnectException;
import com.gridnode.gtas.backend.exception.FileNotFoundException;
import com.gridnode.gtas.backend.exception.SendFileException;
import com.gridnode.gtas.backend.util.BackendEntityResolver;
import com.gridnode.gtas.server.backend.openapi.core.APIConfig;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.gtas.server.backend.openapi.core.APIServiceInterface;
import com.gridnode.gtas.server.backend.openapi.core.IAPIService;

public class CustomSenderLite
{
  private static final int UNKNOWN_ERROR          = 0;
  private static final int FILE_NOT_FOUND         = 1;
  private static final int UNABLE_TO_DELETE_FILE  = 2;
  private static final int CONNECTION_ERROR       = 3;

  private static final int INFO_MSG               = 0;
  private static final int DEBUG_MSG              = 1;
  private static final int ERROR_MSG              = 2;

  private static final String[][] paramMap =
  {
    {"I", "0"}, {"O", "1"}, {"N", "2"}, {"W", "3"}, {"S", "4"}, {"E", "5"},
    {"D", "6"}, {"P", "7"}, {"F", "8"}, {"A", "9"}, {"X", "10"}, {"Y", "11"},
    {"Z", "12"}, {"R", "13"}, {"B", "14"}, {"U", "15"}, {"DUNS", "16"}, {"XPATH", "17"}
  };
  private static final int  GT_IP               = 0;    //I
  private static final int  GT_PORT             = 1;    //O
  private static final int  USERNAME            = 2;    //N
  private static final int  PASSWORD            = 3;    //W
  private static final int  SMTP                = 4;    //S
  private static final int  EMAIL               = 5;    //E
  private static final int  DOC_TYPE            = 6;    //D
  private static final int  PARTNER_ID          = 7;    //P
  private static final int  FILE_TO_SEND        = 8;    //F
  private static final int  ATTACHMENTS         = 9;    //A
  private static final int  DELETE_FILE         = 10;   //X
  private static final int  DELETE_ATTACHMENTS  = 11;   //Y
  private static final int  DISPLAY_DEBUG_MSG   = 12;   //Z
  private static final int  RN_PROFILE          = 13;   //R
  private static final int  BIZ_ENT_ID          = 14;   //B
  private static final int  UNIQUE_ID           = 15;   //U
  private static final int  DUNS_LOOKUP         = 16;   //DUNS
  private static final int  XPATH_LOOKUP        = 17;   //XPATH

  private static String   gt_ip               = "";         //mandatory
  private static int      gt_port             = -1;         //mandatory
  private static String   username            = "";         //mandatory
  private static String   password            = "";         //mandatory
  private static String   smtp                = "";         //optional
  private static String   email               = "";         //optional
  private static String   doc_type            = "";         //optional
  private static String   partner_id          = "";         //optional
  private static String   file_to_send        = "";         //mandatory
  private static String   attachments         = "";         //optional
  private static boolean  delete_file         = false;      //optional
  private static boolean  delete_attachments  = false;      //optional
  private static String   rn_profile          = "";         //optional
  private static String   biz_ent_id          = "";         //optional
  private static String   unique_id           = "";         //optional
  private static String   xPath_lookup        = "xpath.xml";//optional
  private static String   dUNS_lookup         = "duns.xml"; //optional

  private static boolean  debug_mode          = false;
  private static String   date_format         = "";
  private static SimpleDateFormat sdf         = null;

  public static void main(String[] args)
  {
    boolean success = false;
    try
    {
      sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
      if(args.length == 0)
      {
        //log("$$$$$$$$$$ no parameters specified $$$$$$$$$$");
        System.out.println("Usage : SenderLite -I \"IP\" -O \"PORT\" -N \"USERNAME\" -W \"PASSWORD\"\n "+
                           "-F \"IMPORT_FILE;..\" [-A \"ATTACHMENT_FILE;..\"] [-X] [-Y] [-S \"SMTP\"] \n"+
                           "[-E \"EMAIL\"] [-D \"DOC_TYPE\"] [-P \"PARTNER_ID\"] [-R \"RN_PROFILE\"] \n"+
                           "[-B \"BIZ_ENT_ID\"] [-U \"UNIQUE_ID\"] [-Z] \n"+
                           "[-DUNS \"DUNS_LOOKUP_FILE\"] [-XPATH \"XPATH_LOOKUP_FILE\"] \n"+
                           "IP              - The IP address of the GridTalk server\n"+
                           "PORT            - The listening port on the GridTalk server\n"+
                           "USERNAME        - The GridTalk user making the connection to GridTalk server\n"+
                           "PASSWORD        - The password of the GridTalk user\n"+
                           "IMPORT_FILE     - The full path of the file to be imported to GridTalk. To\n"+
                           "                  specify more than one file, use ; to separate them\n"+
                           "ATTACHMENT_FILE - The full path of the file to be attached to the import file.\n"+
                           "                  To specify more than one file, use ; to separate them\n"+
                           "X               - Delete the import files after sending to GridTalk\n"+
                           "Y               - Delete the attachment files after sending to GridTalk\n"+
                           "SMTP            - The SMTP server to use to send error notifications\n"+
                           "EMAIL           - The email to receive error notifications\n"+
                           "DOC_TYPE        - The document type of the import file\n"+
                           "PARTNER_ID      - The partner ID of the partner to recieve the files\n"+
                           "RN_PROFILE      - The rn_profile UID to link the import files to the RosettaNet\n"+
                           "                  process in GridTalk\n"+
                           "BIZ_ENT_ID      - The business entity ID of the user who imports the files\n"+
                           "UNIQUE_ID       - A unique identifier for the file to be imported\n"+
                           "Z               - Switch on debugging messages");
        System.exit(1);
      }
      log("Backend Sender Start", INFO_MSG);
      setUpParameters(args);
      if (checkMandatoryParam())
      {
        success = sendDocument();
      }
      else
      {
        System.exit(1);
      }

      log("Backend Sender End", INFO_MSG);
      System.exit(0);
    }
    catch(ConnectException cex)
    {
      log("**********Exit with exception**********", ERROR_MSG);
      log("Exception", cex);
      ArrayList params = new ArrayList();
      params.add(cex);
      sendFailureMail(UNKNOWN_ERROR, params);
      System.exit(2);
    }
    catch(SendFileException sfex)
    {
      log("**********Exit with exception**********", ERROR_MSG);
      log("Exception", sfex);
      ArrayList params = new ArrayList();
      params.add(sfex);
      sendFailureMail(UNKNOWN_ERROR, params);
      System.exit(3);
    }
    catch(Exception ex)
    {
      log("**********Exit with exception**********", ERROR_MSG);
      log("Exception", ex);
      ArrayList params = new ArrayList();
      params.add(ex);
      sendFailureMail(UNKNOWN_ERROR, params);
      if (success)
      {
        System.exit(0);
      }
      else
      {
        System.exit(4);
      }
    }
  }

  /**
   * WKH:Map DUNS to correct BIZ ENT
   */
  private static void mapBizEnt(File[] fileArray) throws JaxenException, Exception
  {
    File dataFile = fileArray[0];
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    db.setEntityResolver(new BackendEntityResolver());
    Document uDoc = db.parse(dataFile);
    log("[mapBizEnt]:" + dataFile.getAbsolutePath(), DEBUG_MSG);
    log("[mapBizEnt]:Root = " + uDoc.getDocumentElement().getNodeName(), DEBUG_MSG);
    Document xpathLookupDoc = db.parse(xPath_lookup);
    Document dunsLoopupDoc = db.parse(dUNS_lookup);
    XPath mappingXPath = new DOMXPath("/XPaths/Mapping[Root=\"" + uDoc.getDocumentElement().getNodeName() + "\"]");
    Element xpathMappingEle = (Element)mappingXPath.selectNodes(xpathLookupDoc).get(0);
    String xPath = xpathMappingEle.getElementsByTagName("XPath").item(0).getFirstChild().getNodeValue();
    log("[mapBizEnt]:xPath = " + xPath, DEBUG_MSG);
    XPath uDocXPath = new DOMXPath(xPath);
    String duns = uDocXPath.stringValueOf(uDoc);
    log("[mapBizEnt]:duns = " + duns, DEBUG_MSG);
    XPath dunsXPath = new DOMXPath("/DUNSLookup/Mapping[DUNS=\"" + duns + "\"]");
    Element dunsMappingEle = (Element)dunsXPath.selectNodes(dunsLoopupDoc).get(0);
    String bizEnt = dunsMappingEle.getElementsByTagName("BizEnt").item(0).getFirstChild().getNodeValue();
    log("[mapBizEnt]:bizEnt = " + bizEnt, DEBUG_MSG);
    biz_ent_id = bizEnt;
  }

  /**
   * Read in the parameters from the commandline
   */
  private static void setUpParameters(String[] args)
  {
    try
    {
      Hashtable paramTable = getParamTable();
      String param = "";
      String value = "";
      for (int i = 0; i < args.length; i++)
      {
        String token = args[i];
        //log("token = " + token, DEBUG_MSG);
        if (token.startsWith("-"))
        {
          param = token.substring(1);
          if (paramTable.containsKey(param))
          {
            if (i+1 < args.length)
            {
              value = args[i+1];
            }
            if (value.startsWith("-"))
            {
              value = "";
            }
            Object paramKey = paramTable.get(param);
            if (paramKey != null)
            {
              value = stripQuotes(value);
              setParam(paramKey.toString(), value);
            }
          }
          else
          {
            log("Unrecognize parameter " + param, ERROR_MSG);
          }
        }
      }
    }
    catch (Exception ex)
    {
      log("Exception in setUpParameters", ex);
    }

  }

  /**
   * Stores the valid parameters into the hashtable, so that it can be
   * used to validate the parameters from the command line.
   */
  private static Hashtable getParamTable()
    throws Exception
  {
    Hashtable table = new Hashtable();
    for (int i = 0; i < paramMap.length; i++)
    {
      table.put(paramMap[i][0], paramMap[i][1]);
    }
    return table;
  }

  /**
   * If the values from the command line are enclosed in quotes, it will
   * be removed.
   */
  private static String stripQuotes(String value)
  {
    if (!value.equals(""))
    {
      if (value.startsWith("\""))
      {
        value = value.substring(1);
      }

      if (value.endsWith("\""))
      {
        value = value.substring(0, value.length()-1);
      }
    }
    return value;
  }

  /**
   * Sets the values from the parameters into the local variables.
   */
  private static void setParam(String paramKey, String value)
    throws Exception
  {
    int paramType = new Integer(paramKey).intValue();
    switch (paramType)
    {
      case GT_IP:
        gt_ip = value;
        break;

      case GT_PORT:
        gt_port = new Integer(value).intValue();
        break;

      case USERNAME:
        username = value;
        break;

      case PASSWORD:
        password = value;
        break;

      case SMTP:
        smtp = value;
        GridMail.setSMTP(smtp);
        break;

      case EMAIL:
        email = value;
        break;

      case DOC_TYPE:
        doc_type = value;
        break;

      case PARTNER_ID:
        partner_id = value;
        break;

      case FILE_TO_SEND:
        file_to_send = value;
        break;

      case ATTACHMENTS:
        attachments = value;
        break;

      case DELETE_FILE:
        delete_file = true;
        break;

      case DELETE_ATTACHMENTS:
        delete_attachments = true;
        break;

      case DISPLAY_DEBUG_MSG:
        debug_mode = true;
        break;

      case RN_PROFILE:
        rn_profile = value;
        break;

      case BIZ_ENT_ID:
        biz_ent_id = value;
        break;

      case UNIQUE_ID:
        unique_id = value;
        break;

      case DUNS_LOOKUP:
        dUNS_lookup = value;
        break;

      case XPATH_LOOKUP:
        xPath_lookup = value;
        break;
    }
  }

  /**
   * Check to ensure that all mandatory variables are set.
   */
  private static boolean checkMandatoryParam()
  {
    try
    {
      boolean isparamOK = true;

      if (gt_ip.equals(""))
      {
        isparamOK = false;
        log("Mandatory param -I for GridTalk IP has not been set", ERROR_MSG);
      }
      if (gt_port == -1)
      {
        isparamOK = false;
        log("Mandatory param -O for GridTalk Port has not been set", ERROR_MSG);
      }
      if (username.equals(""))
      {
        isparamOK = false;
        log("Mandatory param -N for GridTalk user name has not been set", ERROR_MSG);
      }
      if (password.equals(""))
      {
        isparamOK = false;
        log("Mandatory param -W for GridTalk user password has not been set", ERROR_MSG);
      }
      if (file_to_send.equals(""))
      {
        isparamOK = false;
        log("Mandatory param -F for the file to be send to GridTalk has not been set", ERROR_MSG);
      }
      return isparamOK;
    }
    catch (Exception ex)
    {
      log("Exception in checkMandatoryParam", ex);
      return false;
    }
  }

  /**
   * Logic for sending the documents. First get the files to be send, if there
   * are files to send, check for whether there are attachments. Secondly, send
   * the files. After that, if the sending is successful, delete the files
   * if they are marked for deletion.
   */
  private static boolean sendDocument()
    throws Exception
  {
    boolean success = false;
    File[] fileArray = prepareFileArray(file_to_send);
    if (fileArray.length > 0)
    {
      File[] attFileArray = prepareFileArray(attachments);
      success = sendFiles(fileArray, attFileArray);
      if (success)
      {
        log("send files success", DEBUG_MSG);
        if (delete_file)
        {
          log("deleting import files", DEBUG_MSG);
          deleteFiles(fileArray);
        }
        if (delete_attachments)
        {
          log("deleting attachment files", DEBUG_MSG);
          deleteFiles(attFileArray);
        }
      }
    }
    else
    {
      log("No files to send, file array is empty", INFO_MSG);
    }
    return success;
  }

  /**
   * Extract from the parameters passed in, the files that are to be send.
   * The files are separated by a semi-colon. Directories are ignored.
   */
  private static File[] prepareFileArray(String files)
    throws Exception
  {
    ArrayList fileList = new ArrayList();
    StringTokenizer fileST = new StringTokenizer(files, ";");
    while (fileST.hasMoreTokens())
    {
      String fileToSent = fileST.nextToken();
      log("File to sent = " + fileToSent, DEBUG_MSG);
      File fileObj = new File(fileToSent);
      if (fileObj.exists())
      {
        if (fileObj.isDirectory())
        {
          log("The import file " + fileToSent + " is a directory", INFO_MSG);
        }
        else
        {
          fileList.add(fileObj);
        }
      }
      else
      {
        log("File " + fileToSent + " not found", INFO_MSG);
        ArrayList params = new ArrayList();
        params.add(fileToSent);
        sendFailureMail(FILE_NOT_FOUND, params);
        throw new FileNotFoundException("File " + fileToSent + " not found");
      }
    }

    File[] fileArray = new File[fileList.size()];
    for (int i = 0; i < fileList.size(); i++)
    {
      fileArray[i] = (File)fileList.get(i);
    }
    return fileArray;
  }

  /**
   * Send the import files and attachments to GridTalk.
   */
  private static boolean sendFiles(File[] fileArray, File[] attFileArray)
    throws Exception
  {
    boolean success = false;
    APIConfig config = new APIConfig(gt_ip, gt_port, username, password);
    APIServiceInterface serviceInterface = new APIServiceInterface(config);
    APIParams apiParams = null;

    try
    {
      apiParams = serviceInterface.serviceConnect();
    }
    catch (Exception ex)
    {
      log("Error connecting to GridTalk", ex);
      ArrayList params = new ArrayList();
      params.add(gt_ip);
      params.add(new Integer(gt_port));
      params.add(username);
      sendFailureMail(CONNECTION_ERROR, params);
      throw new ConnectException("Error connecting to GridTalk");
    }

    //WKH:Map DUNS to correct biz ent before sending
    File xPathLookupFile = new File(xPath_lookup);
    File dunsLookupFile =  new File(dUNS_lookup);
    log("[sendFiles]:xPath_lookup = " + xPath_lookup, DEBUG_MSG);
    log("[sendFiles]:dUNS_lookup = " + dUNS_lookup, DEBUG_MSG);
    if(xPathLookupFile.exists() && dunsLookupFile.exists())
    {
      mapBizEnt(fileArray);
    }

    if (apiParams != null)
    {
      Object[] connectID = apiParams.getParamArray();

      // Filling in return APIParams object array
      Object[] returnObjs = new Object[8];
      returnObjs[0] = connectID[0];
      returnObjs[1] = username;
      returnObjs[2] = partner_id;
      returnObjs[3] = doc_type;
      returnObjs[4] = new Integer(attFileArray.length - 1);
      returnObjs[5] = biz_ent_id;
      returnObjs[6] = rn_profile;
      returnObjs[7] = unique_id;

      for(int i = 0; i < returnObjs.length; i++)
      {
        log("Object " + i + " = " + returnObjs[i], DEBUG_MSG);
      }

      File[] filesToSent = prepareFiles(fileArray, attFileArray);

      for(int j = 0; j < filesToSent.length; j++)
      {
        log("sending " + filesToSent[j].getAbsolutePath(), INFO_MSG);
      }

      APIParams parameters = new  APIParams(returnObjs, filesToSent);
      try
      {
        serviceInterface.performService(IAPIService.IMPORT, parameters);
      }
      catch (Exception ex)
      {
        serviceInterface.serviceDisconnect();
        throw new SendFileException("Error sending files to GridTalk");
      }
      serviceInterface.serviceDisconnect();
      success = true;
    }
    return success;
  }

  /**
   * Combine the two file arrays into one file array to be send to GridTalk.
   */
  private static File[] prepareFiles(File[] fileArray, File[] attFileArray)
  {
    log("fileArray size = " + fileArray.length +
        "   attFileArray size = " + attFileArray.length, DEBUG_MSG);
    int newFileArraySize = fileArray.length + attFileArray.length;
    File[] newFileArray = new File[newFileArraySize];
    int counter = -1;
    for (int i = 0; i < attFileArray.length; i++)
    {
      counter++;
      newFileArray[counter] = attFileArray[i];
    }
    for (int j = 0; j < fileArray.length; j++)
    {
      counter++;
      newFileArray[counter] = fileArray[j];
    }
    return newFileArray;
  }

  /**
   * Deletes the files in the file array.
   */
  private static void deleteFiles(File[] filesToDelete)
  {
    for (int i = 0; i < filesToDelete.length; i++)
    {
      File fileToDelete = filesToDelete[i];
      if (!fileToDelete.delete())
      {
        log("Unable to delete file " + fileToDelete.getAbsolutePath(), ERROR_MSG);
      }
      else
      {
        log(fileToDelete.getAbsolutePath() + " deleted", INFO_MSG);
      }
    }
  }

  private static void sendFailureMail(int errorCode, ArrayList params)
  {
    if ( (!smtp.equals("")) && (!email.equals("")) )
    {
      IGmail mail = GridMail.createGmail();
      mail.setSender("gt_backend@gridnode.com");
      log("Sending email for failure to " + email, INFO_MSG);
      mail.setRecipient(email);

      StringBuffer message = new StringBuffer();
      switch (errorCode)
      {
        case FILE_NOT_FOUND:
          mail.setSubject("Backend Sender Exception : Unable to locate file");
          message.append("Backend sender fails to find the specified file");
          message.append(params.get(0).toString());
          message.append(" while importing documents into GridTalk.");
          log("Sending email for failure message for FILE_NOT_FOUND", INFO_MSG);
          break;

        case CONNECTION_ERROR:
          mail.setSubject("Backend Sender Exception : Unable to connect to GridTalk");
          message.append("Backend sender fails to connect to GridTalk IP ");
          message.append(params.get(0).toString());
          message.append(" Port ");
          message.append(params.get(1).toString());
          message.append(" Username ");
          message.append(params.get(2).toString());
          log("Sending email for failure message for CONNECTION_ERROR", INFO_MSG);
          break;

        default:
          //params contain exception
          Exception ex = (Exception)params.get(0);
          mail.setSubject("Backend import fail");
          message.append("GridTalk Ver 1.3 Backend ");
          message.append(getStackTrace(ex));
          log("Sending email for failure message " + message, INFO_MSG);
          break;
      }
      mail.setMessage(message.toString());
      log("Sending mail", INFO_MSG);
      GridMail.send(mail);
    }
    else
    {
      log("Missing email settings: SMTP = \"" + smtp + "\"" +
          " email = \"" + email + "\"", INFO_MSG);
    }
  }

  private static String getStackTrace(Exception e)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    e.printStackTrace(ps);
    String st = baos.toString();
    return st;
  }

//  private static void log(String msg, Exception e)
//  {
//    Log.err(Log.LOG_XML, CustomSenderLite.class, msg, e);
//  }
//
//  private static void log(String msg)
//  {
//    Log.log(Log.LOG_XML, CustomSenderLite.class, msg);
//  }

  private static void log(String msg)
  {
    System.out.println(msg);
  }

  private static void log(String msg, Exception e)
  {
    //Log.err(Log.LOG_XML, SenderLite.class, msg, e);
    System.err.println(msg);
    e.printStackTrace(System.err);
  }

  private static void log(String msg, int type)
  {
    date_format = "["+sdf.format(new Date(System.currentTimeMillis()))+"] ";
    switch (type)
    {
      case INFO_MSG:
        System.out.println(date_format + msg);
        break;

      case DEBUG_MSG:
        if (debug_mode)
        {
          System.out.println(date_format + msg);
        }
        break;

      case ERROR_MSG:
        System.err.println(date_format + "ERROR " + msg);
        break;

    }
  }
}
