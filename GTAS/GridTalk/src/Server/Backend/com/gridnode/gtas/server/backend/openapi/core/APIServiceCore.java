package com.gridnode.gtas.server.backend.openapi.core;

import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.net.APIConnect;
import com.gridnode.pdip.framework.util.MaskedPass;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 * 
 * 2008-11-11  Ong Eu Soon      add line to decode masked password
 */

public class APIServiceCore
{
  private APIConfig config = null;
  private APIAuthenticator auth = null;
  private APIConnect connect = null;
  private APIComm comm = null;
  private ObjectInputStream input = null;
  private ObjectOutputStream output = null;
  private IAPICallback callback = null;
  private String username = null;
  private CallbackListener callbackListener = null;

  public APIServiceCore(APIConfig configSrc)
  {
    setConfig(configSrc);
  }

  public void setConfig(APIConfig configSrc)
  {
    config = configSrc;
  }

  //public void serviceConnect(String user, String password) throws Exception
  public APIParams serviceConnect(String user, String password) throws Exception
  {
    if (connect != null)
      throw (new Exception("Connect not available while already connected."));

    username = user;
    connect = new APIConnect(config);
    comm = connect.getCommunication();
    auth = new APIAuthenticator(username, password);
    input = comm.getInput();
    output = comm.getOutput();
    String unmaskedPassword =MaskedPass.decode(password); 
    output.writeObject(new String("CONNECT " + username));
    output.flush();
    output.writeObject(unmaskedPassword);
    output.flush();

    /*
    byte[] authKey = new byte[GTAuthenticator.KEYLENGTH];
    input.read(authKey);
    auth.setAuthKey(authKey);
    byte[] sessionKey = auth.getSessionKey();
    output.writeInt(sessionKey.length);
    output.flush();
    output.write(sessionKey);
    output.flush();
    */

    APIParams returnParams = (APIParams)input.readObject();
    Object[] connectID = returnParams.getParamArray();
    if (connectID == null)
    {
    //boolean authSuccess = input.readBoolean();
    //if (!authSuccess)
    //{
      comm.close();
      auth = null;
      comm = null;
      connect = null;
      throw (new Exception("Authentication Unsucessful."));
    }

    System.out.println("Connect Successful! Connect ID = " + connectID[0]);
    return returnParams;
  }

  public void serviceDisconnect() throws Exception
  {
    if (connect != null)
    {
      output.writeObject(new String("DISCONNECT " + username));
      output.flush();
      boolean disconnectSuccess = input.readBoolean();

      if (disconnectSuccess)
      {
        comm.close();
        auth = null;
        comm = null;
        connect = null;
      }
      else
        throw (new Exception("Disconnect unsucessful."));
    }
    else
      throw (new Exception("Disconnect not available while not connected."));
  }

  public synchronized APIParams performService(int serviceID, APIParams params)
    throws Exception
  {
    output.writeObject(new Integer(serviceID));

    output.writeObject(params);
    if(params.getFileArray() == null)
    {
      output.writeObject(new Boolean(false));
    }
    else
    {
      System.out.println("performService: Has files");
      output.writeObject(new Boolean(true));
      params.writeFiles(comm);
    }

    output.flush();
    APIParams returnParams = (APIParams)input.readObject();
    return returnParams;
  }

  public void registerCallback(IAPICallback callbackSrc) throws Exception
  {
    if (connect == null)
      throw (new Exception("Callback not available while not connected."));
    else if (callback == null)
    {
      callback = callbackSrc;
      callbackListener = new CallbackListener(callback);
      callbackListener.start();
      output.writeObject(new String("CALLBACK " +
        callbackListener.getIPAddress() + ":" +
        callbackListener.getPortNum()));
      output.flush();
      boolean success = input.readBoolean();

      if (!(success))
        throw (new Exception("Callback Registration Unsucessful."));
    }
    else
      throw (new Exception("Callback already registered."));
  }
}