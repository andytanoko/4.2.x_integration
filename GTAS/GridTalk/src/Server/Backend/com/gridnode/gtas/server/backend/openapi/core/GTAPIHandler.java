package com.gridnode.gtas.server.backend.openapi.core;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.services.GTServiceFactory;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class GTAPIHandler extends Thread
{
  private APIComm comm;
  private ObjectInputStream input;
  private ObjectOutputStream output;

  private String username;
  private String password;
  private String callbackIP = null;
  private int callbackPort = 0;

  IAPIServiceFactory serviceFactory = null;

  public GTAPIHandler(APIComm commSrc)
         throws Exception
  {
    serviceFactory = new GTServiceFactory(true);

    comm = commSrc;
    input = comm.getInput();
    output = comm.getOutput();

    username = (String)(input.readObject());
    username = username.substring(username.indexOf(' ') + 1);
    password = (String)(input.readObject());

    System.out.println("User Name : " + username + " Password " + password);

    // verify the username and password here, initiate a connect service
    Object[] connectInfo = new Object[2];
    connectInfo[0] = username;
    connectInfo[1] = password;
    APIParams params = new APIParams(connectInfo);
    params = serviceFactory.initiateService(IAPIService.CONNECT, params);
    output.writeObject(params);

    //output.writeBoolean(true);
    output.flush();
  }

//  public void performCallback(int serviceID, APIParams params)
//    throws Exception
//  {
//    APIConnect callbackConnect = new APIConnect(callbackIP, callbackPort);
//    APIComm callbackComm = callbackConnect.getCommunication();
//    ObjectOutputStream callbackOutput = callbackComm.getOutput();
//    callbackOutput.writeObject(new Integer(serviceID));
//    callbackOutput.flush();
//    callbackOutput.writeObject(params);
//    callbackOutput.flush();
//    params.writeFiles(callbackComm);
//    callbackComm.close();
//  }
//
  public void run()
  {
    Object received;
    APIParams params = null;
    boolean runHandler = true;

    while (runHandler)
    {
      try
      {
        received = input.readObject();
        System.out.println("GTAPIHandler");
        if (received instanceof Integer) // handleService
        {
          System.out.println("GTAPIHandler: integer " + received.toString());
          try
          {
          params = (APIParams)(input.readObject());
          System.out.println(params);
          }
          catch(Exception ex)
          {
          ex.printStackTrace();
          System.out.println("fail to receive params");
          }

          Boolean hasFileArray = (Boolean)(input.readObject());
          System.out.println("hasFileArray = " + hasFileArray);
          if(hasFileArray.booleanValue())
          {
            params.readFiles(comm);
          }
          
          /* TWX 10112006 The waiting time at the sender side will be delayed since
           * the status will be returned after the execution of the service.
          Object[] success = new Object[1];
          success[0] = new Boolean(true);
          APIParams parameters = new  APIParams(success, null);

          output.writeObject(parameters);
          output.flush(); */
          APIParams parameters = serviceFactory.initiateService(((Integer)received).intValue(), params);
          
          if(parameters == null)
          {
            parameters = new APIParams(null);
          }
          output.writeObject(parameters); //TWX 10112006 return the status of the service we have performed.
          output.flush();
        }
        else if (received instanceof String) // handleCommand
        {
         System.out.println("GTAPIHandler: String");
         String command = (String)received;
         System.out.println("GTAPIHandler: String 2 " + command);
         String keyword = command.substring(0, command.indexOf(' '));

          if (keyword.equals("DISCONNECT"))
          {
            output.writeBoolean(true); //uncommented
            output.flush();
            comm.close();
            runHandler = false;
          }
          else if (keyword.equals("CALLBACK"))
          {
            String ipPort = command.substring(command.indexOf(' '));
            callbackIP = ipPort.substring(0, ipPort.indexOf(':'));
            callbackPort = Integer.parseInt(ipPort.substring(ipPort.indexOf(':') + 1));
            output.writeBoolean(true);
            output.flush();
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace(System.out);
        //output.writeBoolean(true);
        //output.flush();
        comm.close();
        runHandler = false;
      }
    }
  }

}