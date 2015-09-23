/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Receiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 * Sep 30 2003    Koh Han Sing        Changed to use log4j
 */
package com.gridnode.gtas.backend.receiver;

import java.util.Date;

import com.gridnode.gtas.backend.util.Log;
import com.gridnode.gtas.backend.util.ThreadPool;
import com.gridnode.gtas.server.backend.openapi.core.APIConfig;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.gtas.server.backend.openapi.core.APIServiceInterface;
import com.gridnode.gtas.server.backend.openapi.core.IAPIService;
import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.net.APIListener;

public class Receiver extends APIListener
{
  private static final String CATEGORY = "Receiver";
  private static final String DEFAULT_GT_USERNAME = "admin";
  private static final String DEFAULT_GT_PASSWORD = "admin";

  private String _GT_IP   = "127.0.0.1";
  private int    _GT_PORT = 6763;
  private String _SMTP    = "";
  private String _EMAIL   = "";
  private ThreadPool _pool;

  Receiver(int myPort, String gtIP, int gtPort, String smtp, String email, int maxThread)
  {
    super(myPort);
    _GT_IP = gtIP;
    _GT_PORT = gtPort;
    _SMTP = smtp;
    _EMAIL = email;
    _pool = new ThreadPool(maxThread);

  }

  public static void main(String[] args)
  {
    try
    {
      if(args.length < 3)
      {
        System.out.println("$$$$$$$$$$ Please specify port number $$$$$$$$$$");
        System.exit(1);
      }

      int portNum = new Integer(args[0]).intValue();
      String gtIP = args[1];
      int gtPORT = new Integer(args[2]).intValue();

      //Koh Han Sing 13/12/2001
      String smtp = "";
      String email = "";
      int maxThread = 10;

      if (args.length > 5)
      {
        smtp = args[3];
        email = args[4];
        maxThread = new Integer(args[5].substring(2)).intValue();
      }
      else if (args.length > 4)
      {
        smtp = args[3];
        email = args[4];
      }
      else if (args.length == 4)
      {
        maxThread = new Integer(args[3].substring(2)).intValue();
      }

      System.out.println("$$$$$$$$$$ Listener Start " +
        new Date(System.currentTimeMillis()) + " $$$$$$$$$$");
      log("$$$$$$$$$$ Start " + new Date(System.currentTimeMillis()) +
        " $$$$$$$$$$");
      System.out.println("Listening at " + portNum);
      log("Listening at " + portNum);
      Receiver receiver = new Receiver(portNum, gtIP, gtPORT, smtp, email, maxThread);
      //receiver.receiveFile();
      receiver.start();
    }
    catch(Exception e)
    {
      log("**********Exit with exception**********", e);
      e.printStackTrace(System.out);
      System.exit(2);
    }
  }

  public void receiveFile()
  {
    try
    {
      // inform GridTalk on startup
      APIConfig config = new APIConfig(
                           _GT_IP,
                           _GT_PORT,
                           Receiver.DEFAULT_GT_USERNAME,
                           Receiver.DEFAULT_GT_PASSWORD);

      APIServiceInterface serviceInterface = new APIServiceInterface(config);

      log("[Receiver.receiveFile] Performing service connect");
      serviceInterface.serviceConnect();

      log("[Receiver.receiveFile] Initiating export service");
      serviceInterface.performService(IAPIService.EXPORT, new APIParams(null));

      log("[Receiver.receiveFile] Performing service disconnect");
      serviceInterface.serviceDisconnect();
    }
    catch (Exception ex)
    {
      log("[Receiver.receiveFile] Fail to connect to GridTalk!");
      ex.printStackTrace();
    }
    finally
    {
      start();
    }
  }

  public void handleConnection(APIComm comm)
  {
    log("====================================================================");
    log("Received connection");
    BEAPIHandler apiHandler = new BEAPIHandler(comm, _SMTP, _EMAIL);
    _pool.run(apiHandler);
    //apiHandler.start();
  }

  private static void log(String msg, Exception e)
  {
    Log.err(CATEGORY, msg, e);
//    System.out.println("[Receiver] " + msg + " Ex msg = " + e.getMessage());
  }

  private static void log(String msg)
  {
    Log.log(CATEGORY, msg);
//    System.out.println("[Receiver] " + msg);
  }
}
