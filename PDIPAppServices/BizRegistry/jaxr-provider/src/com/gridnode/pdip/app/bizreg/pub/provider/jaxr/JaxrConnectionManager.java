/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JaxrConnectionManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 16 2003    Neo Sok Lay         Instantiate the factory impl class here 
 *                                    instead of leaving to the ConnectionFactory
 *                                    class.
 * Sep 17 2003    Neo Sok Lay         Implements IRegistryServiceManager.
 *                                    Formats coding style.
 * Mar 01 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.bizreg.pub.provider.jaxr;

import com.gridnode.pdip.app.bizreg.pub.model.IConnectionInfo;
import com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService;
import com.gridnode.pdip.app.bizreg.pub.provider.IRegistryServiceManager;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;

import java.net.PasswordAuthentication;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

/**
 * Manages the connections to JAXR registries.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class JaxrConnectionManager implements IRegistryServiceManager
{
  private static final JaxrConnectionManager _self =
    new JaxrConnectionManager();

  private Hashtable<String,JaxrRegistryService> _regServices = new Hashtable<String,JaxrRegistryService>();
  private Hashtable<String,JaxrRegistryService> _queryServices = new Hashtable<String,JaxrRegistryService>();

  /**
   * Constructor for JaxrConnectionManager.
   */
  private JaxrConnectionManager()
  {
  }

  /**
   * Gets an instance of the JaxrConnectionManager.
   * 
   * @return an instance of the JaxrConnectionManager
   */
  public static JaxrConnectionManager getInstance()
  {
    return _self;
  }

  /**
   * Gets a JaxrRegistryService object for the specified connection
   * information.
   * 
   * @param connectionInfo The connection information.
   * @return A JaxrRegistryService that can serve requests to
   * the registry specified in the connection information.
   * @throws Exception Exception when connecting to the
   * registry specified in the connection information.
   * @see IConnectionInfo
   */
  public synchronized IRegistryService getRegistryService(String[] connectionInfo)
    throws Exception
  {
    JaxrRegistryService regService = null;

    String queryUrl = connectionInfo[IConnectionInfo.FIELD_QUERY_URL];
    String publishUrl = connectionInfo[IConnectionInfo.FIELD_PUBLISH_URL];
    String username = connectionInfo[IConnectionInfo.FIELD_USER];

    regService =
      (publishUrl == null)
        ? createRegistryService(_queryServices, queryUrl, connectionInfo)
        : createRegistryService(
          _regServices,
          publishUrl + "_" + username,
          connectionInfo);

    return regService;
  }

  /**
   * Creates a JaxrRegistryService instance using the specified connection info
   * and store in the specified services table with the specified key.
   * 
   * @param existingServices The Services table to store to.
   * @param key The key of the created JaxrRegistryService instance.
   * @param connInfo The connection information object for connection to the registry.
   * @throws Exception Error creating the connection to the registry.
   */
  private JaxrRegistryService createRegistryService(
    Hashtable<String,JaxrRegistryService> existingServices,
    String key,
    String[] connInfo)
    throws Exception
  {
    JaxrRegistryService regService;
    if (existingServices.containsKey(key))
    {
      regService = existingServices.get(key);
    }
    else
    {
      Connection regServiceConn = getRegistryServiceConnection(connInfo);
      regService = new JaxrRegistryService(regServiceConn);
      existingServices.put(key, regService);
    }

    return regService;
  }

  /**
   * Removes the registry service associated with the specified connection information
   * object. 
   * 
   * @param connectionInfo The Connection information of registry that the registry service
   * was made connection to. 
   */
  public synchronized void removeRegistryService(String[] connectionInfo)
  {
    //JaxrRegistryService regService = null;

    String queryUrl = connectionInfo[IConnectionInfo.FIELD_QUERY_URL];
    String publishUrl = connectionInfo[IConnectionInfo.FIELD_PUBLISH_URL];
    String username = connectionInfo[IConnectionInfo.FIELD_USER];

    if (publishUrl == null)
    {
      removeRegistryService(_queryServices, queryUrl);
    }
    else
    {
      removeRegistryService(_regServices, publishUrl + "_" + username);
    }
  }

  /**
   * Removes a JaxrRegistryService instance with the specified key in the 
   * services table.
   * 
   * @param services Hashtable of JaxrRegistryService instances.
   * @param key The key of the JaxrRegistryService instance to remove
   */
  private void removeRegistryService(Hashtable services, String key)
  {
    JaxrRegistryService regService = null;
    if (services.containsKey(key))
    {
      regService = (JaxrRegistryService) services.get(key);
      if (regService.close())
        services.remove(key);
    }
  }

  /**
   * Get a connection to the registry using the specified connection information.
   * 
   * @param connInfo The connection information.
   * @return A JAXR RegistryService object serving the requests to the registry specified
   * in the connection information.
   * @throws Exception Error while creating connection to the registry.
   * @see IConnectionInfo
   */
  private Connection getRegistryServiceConnection(String[] connInfo)
    throws Exception
  {
    // lookup connection factory
    ConnectionFactory factory = getConnectionFactory();

    // create connection
    Connection connection = createConnection(factory, connInfo);

    // get registry service
    return connection;
  }

  /**
   * Creates a connection to a registry using the specified ConnectionFactory.
   * 
   * @param factory The ConnectionFactory to create connection from.
   * @param connInfo The connection information.
   * @return The created JAXR Connection.
   * @throws Exception Error while creating Connection.
   */
  private Connection createConnection(
    ConnectionFactory factory,
    String[] connInfo)
    throws Exception
  {
    Configuration config = getJaxrConfig();

    String queryUrl = connInfo[IConnectionInfo.FIELD_QUERY_URL];
    String publishUrl = connInfo[IConnectionInfo.FIELD_PUBLISH_URL];
    if (publishUrl == null || publishUrl.trim().length() == 0)
      publishUrl = queryUrl;

    String httpProxyHost = config.getString(IJaxrKeys.CONN_HTTP_PROXY_HOST, "");
    String httpProxyPort = config.getString(IJaxrKeys.CONN_HTTP_PROXY_PORT, "");
    String httpsProxyHost =
      config.getString(IJaxrKeys.CONN_HTTPS_PROXY_HOST, "");
    String httpsProxyPort =
      config.getString(IJaxrKeys.CONN_HTTPS_PROXY_PORT, "");
    String username = connInfo[IConnectionInfo.FIELD_USER];
    String password = connInfo[IConnectionInfo.FIELD_PASSWORD];

    Properties props = new Properties();

    props.setProperty(IJaxrKeys.CONN_BQM_URL, queryUrl);
    props.setProperty(IJaxrKeys.CONN_BLM_URL, publishUrl);
    props.setProperty(IJaxrKeys.CONN_HTTP_PROXY_HOST, httpProxyHost);
    props.setProperty(IJaxrKeys.CONN_HTTP_PROXY_PORT, httpProxyPort);
    props.setProperty(IJaxrKeys.CONN_HTTPS_PROXY_HOST, httpsProxyHost);
    props.setProperty(IJaxrKeys.CONN_HTTPS_PROXY_PORT, httpsProxyPort);

    factory.setProperties(props);
    Connection conn = factory.createConnection();

    // set the credentials if present
    if (username != null && password != null)
    {
      PasswordAuthentication passwdAuth =
        new PasswordAuthentication(username, password.toCharArray());

      Set<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();
      creds.add(passwdAuth);
      conn.setCredentials(creds);
    }

    conn.setSynchronous(true);

    return conn;
  }

  /**
   * Obtain the JAXR ConnectionFactory.
   * 
   * @return The JAXR ConnectionFactory obtained.
   * @throws Exception Error while obtaining the JAXR ConnectionFactory.
   */
  private ConnectionFactory getConnectionFactory() throws Exception
  {
    Configuration config = getJaxrConfig();
    String factoryImpl = config.getString(IJaxrKeys.KEY_FACTORY_IMPL);

    return createConnectionFactory(factoryImpl);
  }

  private ConnectionFactory createConnectionFactory(String factoryName)
    throws Exception
  {
    Class factoryClass = getClass().getClassLoader().loadClass(factoryName);

    return (ConnectionFactory) factoryClass.newInstance();
  }
  /**
   * Gets the Configuration for JAXR registry provider.
   * 
   * @return The Configuration object containing properties for use to initialize
   * the JAXR registry provider.
   * @throws Exception Error while getting the Configuration from ConfigurationManager.
   */
  private Configuration getJaxrConfig() throws Exception
  {
    return ConfigurationManager.getInstance().getConfig(IJaxrKeys.CONFIG_NAME);
  }

}
