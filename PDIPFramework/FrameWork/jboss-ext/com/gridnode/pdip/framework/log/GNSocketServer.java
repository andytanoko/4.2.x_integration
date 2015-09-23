/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: GNSocketServer.java
 * 
 * ***************************************************************************
 * Date Author Changes
 * ***************************************************************************
 * Dec 13, 2006 i00107 Copied from Log4j SimpleSocketServer and modified to
 * support watching of log configuration.
 */
package com.gridnode.pdip.framework.log;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.net.SocketNode;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * A simple {@link SocketNode} based server.
 * 
 * <pre>
 *  &lt;b&gt;Usage:&lt;/b&gt; java com.gridnode.pdip.framework.GNSocketServer port [configFile | configDir]
 * 
 *  where
 * <em>
 * port
 * </em>
 *  is a part number where the server listens and
 * <em>
 * configFile
 * </em>
 *  is a configuration file fed to the {@link
 *  PropertyConfigurator} or to {@link DOMConfigurator} if an XML file, and
 *  <em>
 *  configDir
 *  </em>
 *  is a directory containing all configuration files to be fed.
 * </pre>
 */
public class GNSocketServer
{

  static Logger cat = Logger.getLogger(GNSocketServer.class);

  static int port;

  public static void main(String argv[])
  {
    if (argv.length == 2)
    {
      init(argv[0], argv[1]);
    }
    else
    {
      usage("Wrong number of arguments.");
    }

    try
    {
      cat.info("Listening on port " + port);
      ServerSocket serverSocket = new ServerSocket(port);
      while (true)
      {
        cat.info("Waiting to accept a new client.");
        Socket socket = serverSocket.accept();
        cat.info("Connected to client at " + socket.getInetAddress());
        cat.info("Starting new socket node.");
        new Thread(new SocketNode(socket, LogManager.getLoggerRepository()))
            .start();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  static void usage(String msg)
  {
    System.err.println(msg);
    System.err.println("Usage: java " + GNSocketServer.class.getName()
                       + " port [configFile | configDir]");
    System.exit(1);
  }

  static void init(String portStr, String configFile)
  {
    try
    {
      port = Integer.parseInt(portStr);
    }
    catch (java.lang.NumberFormatException e)
    {
      e.printStackTrace();
      usage("Could not interpret port number [" + portStr + "].");
    }

    File f = new File(configFile);
    if (f.isDirectory())
    {
      File[] lcfs = f.listFiles();
      for (File lcf : lcfs)
      {
        configure(lcf.getAbsolutePath());
      }
    }
    else
    {
      configure(configFile);
    }
  }
  
  private static void configure(String configFile)
  {
    if (configFile.endsWith(".xml"))
    {
      DOMConfigurator.configureAndWatch(configFile);
    }
    else
    {
      PropertyConfigurator.configureAndWatch(configFile);
    }
  }
}
