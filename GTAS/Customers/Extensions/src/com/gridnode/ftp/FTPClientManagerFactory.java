/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPClientManagerFactory.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 * Mar 13 2009    Tam Wei Xiang           #132 Add the method for initialization the FTP Provider 
 *                                             that support the connection timeout props
 */


package com.gridnode.ftp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.gridnode.ftp.exception.FTPConnectionException;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.facade.IFTPClientManager;

//import org.apache.log4j.Category;


/**
 * A factory class to Create a new Instance of FTPCleint, given the FTP Client
 * Provider. FTPClient Provider is configured externally by Properties file
 * (ex: ftpclient.properties).
 */

 public class FTPClientManagerFactory
{

  //private static Category _logger = Category.getInstance(FTPClientManagerFactory.class);
  /**
   * This method is essentialy responsible to create a new instance of
   * IFTPClientManager implementation, given the provider implementing the
   * IFTPClientManager.
   *
   * @param host  Host FTP Server to connet.
   * @param port  Port on which FTP Server is lisitning (Ex.21)
   * @param provider Concrete implementation of IFTPClientManager.
   * @return Returns a new Insatance of IFTPClientManager implementation.
   * @throws Exception
   */

  public static IFTPClientManager getFTPClientManager(String host,
      String port,String provider ) throws FTPServiceException
  {
    try
    {
      return getFTPClientByProvider(host,port,provider);
    }
    catch(Exception ex)
    {
      throw new FTPServiceException(ex.getMessage(), ex);
    }
  }
  
  /**
   * #132: The FTPClientManager constructor is allowed to set connectionTimeout props.
   * 
   * This method is essentialy responsible to create a new instance of
   * IFTPClientManager implementation, given the provider implementing the
   * IFTPClientManager.
   *
   * @param host  Host FTP Server to connet.
   * @param port  Port on which FTP Server is lisitning (Ex.21)
   * @param socketTimeout Set the connection time out in milliseconds
   * @param provider Concrete implementation of IFTPClientManager.
   * @return Returns a new Insatance of IFTPClientManager implementation.
   * @throws Exception
   */

  public static IFTPClientManager getFTPClientManager(String host,
      String port, int socketTimeout, String provider ) throws FTPServiceException
  {
    try
    {
      return getFTPClientByProvider(host,port, socketTimeout, provider);
    }
    catch(Exception ex)
    {
      throw new FTPServiceException(ex.getMessage(), ex);
    }
  }
  
  public static IFTPClientManager getFTPClientManager(FTPParam param)
      throws FTPServiceException,Exception
  {
      return getFTPClientByProvider(param.getHost(),
                      param.getPort(),param.getProvider());
  }

  private static IFTPClientManager getFTPClientByProvider(
  String host,
  String port,
  String provider
  ) throws FTPServiceException,FTPConnectionException,Exception
  {
    IFTPClientManager ftpClientManager = null;
    Class ftpProviderDefinition = null;
    Constructor ftpProviderConstructor = null;
    Class[] argsClass = new Class[] {String.class,int.class};
    Object[] args     = new Object[] {host,Integer.valueOf(port)};
    try
    {
     /* _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][Begin...]");
      _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][Provider=]'"+
      provider+"'");

      _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][Host=]'"+
      host+"'");

      _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][Port=]'"+
      port+"'");

      _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][B4 Instantiating]");
      */
      ftpProviderDefinition = Class.forName(provider.trim());
      ftpProviderConstructor =
          ftpProviderDefinition.getConstructor(argsClass);
      ftpClientManager =
          (IFTPClientManager)createObject(ftpProviderConstructor,args);

     // _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][After Instantiating]");
      return ftpClientManager;
    }
    catch(ClassNotFoundException cle)
    {
      cle.printStackTrace(System.err);
      throw new FTPServiceException("Class Not Found With className '"+
      provider+"'\n"+cle.getMessage(),cle);
    }
    catch(NoSuchMethodException nse)
    {
      nse.printStackTrace(System.err);
      throw new FTPServiceException(nse.getMessage(),nse);
    }
    catch(FTPConnectionException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new Exception("[FTPClientManagerFactory][getFTPClientByProvider()]"+
      "FTP - Service Not Ready");
    }

  }

  //#132 13032009
  private static IFTPClientManager getFTPClientByProvider(String host,String port,int socketTimeout, String provider) 
    throws FTPServiceException,FTPConnectionException,Exception
  {
    IFTPClientManager ftpClientManager = null;
    Class ftpProviderDefinition = null;
    Constructor ftpProviderConstructor = null;
    Class[] argsClass = new Class[] {String.class,int.class, int.class};
    Object[] args     = new Object[] {host,Integer.valueOf(port), socketTimeout};
    try
    {

      ftpProviderDefinition = Class.forName(provider.trim());
      ftpProviderConstructor =ftpProviderDefinition.getConstructor(argsClass);
      ftpClientManager =(IFTPClientManager)createObject(ftpProviderConstructor,args);

      // _logger.info("[FTPClientManagerFactory][getFTPClientByProvider()][After Instantiating]");
      return ftpClientManager;
    }
    catch(ClassNotFoundException cle)
    {
        cle.printStackTrace(System.err);
        throw new FTPServiceException("Class Not Found With className '"+provider+"'\n"+cle.getMessage(),cle);
    }
    catch(NoSuchMethodException nse)
    {
       nse.printStackTrace(System.err);
       throw new FTPServiceException(nse.getMessage(),nse);
    }
    catch(FTPConnectionException ex)
    {
       throw ex;
    }
    catch(Exception ex)
    {
       throw new Exception("[FTPClientManagerFactory][getFTPClientByProvider()]"+"FTP - Service Not Ready");
    }

  }

  
  private static Object createObject(Constructor constructor,Object[] arguments)
    throws FTPServiceException,FTPConnectionException,Exception
  {
    Object object = null;

    try
    {
      object = constructor.newInstance(arguments);
      return object;
    }
    catch(InstantiationException e)
    {
      //e.printStackTrace(System.err);
      throw new FTPServiceException("[Could Not Invoke on Class"+
        "or Method\n"+e.getMessage(),e);
    }
    catch(IllegalAccessException e)
    {
      //e.printStackTrace(System.err);
      throw new FTPServiceException(e.getMessage(),e);
    }
    catch(InvocationTargetException e)
    {
      //throw new Exception(e.getMessage());
      throw new FTPConnectionException(e.getMessage());
    }
  }

}

