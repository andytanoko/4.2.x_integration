package com.gridnode.pdip.base.userprocedure.helpers;

import com.gridnode.pdip.base.userprocedure.helpers.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class SoapCall
{
  Call      call;
  Service   service;
  Object    response = null;

  public SoapCall() throws ServiceException
  {
    service = new org.apache.axis.client.Service();
    call = (Call)service.createCall();
    call.setUseSOAPAction(true);
    call.setMaintainSession(true);
  }

  public Object getResponse() throws AxisFault
  {
    return response;
  }

  public String getSOAPResponse() throws AxisFault
  {
    return getCall().getResponseMessage().getSOAPPartAsString();
  }

  public Object Invoke(String soapAction, String operationName, Object[] parameters) throws RemoteException
  {
    setOperationName(operationName);
    setSOAPActionURI(soapAction);
    response= Invoke(parameters);
    return response;
  }

  public Object Invoke(String url, String soapAction, String operationName, Object[] parameters) throws RemoteException, MalformedURLException
  {
    setURL(url);
    setSOAPActionURI(soapAction);
    setOperationName(operationName);
    response =  Invoke(parameters);
    return response;
  }

  public void setUsername(String username)
  {
    call.setUsername(username);
  }

  public void setPassword(String password)
  {
    call.setPassword(password);
  }

  public void setURL(String url) throws MalformedURLException
  {
    call.setTargetEndpointAddress(new URL(url));
  }

  public Object Invoke(Object[] parametes) throws RemoteException
  {
    Logger.log("[SoapCall.Invoke] invoking soap");
    response = call.invoke(parametes);
    return response;
  }

  public Call   getCall()
  {
    return call;
  }

  public void setCall(Call call)
  {
    this.call = call;
  }

  public void setSOAPActionURI(String serviceName)
  {
    call.setSOAPActionURI(serviceName);
  }

  public void setOperationName(String operationName)
  {
    call.setOperationName(operationName);
  }

  static public DataHandler gethandler(String filename)
  {
    return new DataHandler(new FileDataSource(filename));
  }
  /*
  public static void main(String[] args) throws Exception
  {
    String url ="http://localhost/WebServicesProvider/services/BackendService?wsdl";
    SoapCall call = new SoapCall();
    call.setURL("http://localhost/WebServicesProvider/services/Version");
    //FileInputStream in = new FileInputStream("C:\\gtas\\catalina\\webapps\\axis\\WEB-INF\\jwsClasses\\EchoHeaders.class");
    //byte[] data = new byte[in.available()];
    //in.read(data);
    //printMethodDetails(getMethodDetailsFromWSDL(p));
    //printMethodDetails(getMethodDetailsFromClass(data));
    System.out.println(call.Invoke("", "getVersion", null));
    //String st = call.getCall().getResponseMessage().getSOAPPartAsString();
    //System.out.println(st);
  }*/
}