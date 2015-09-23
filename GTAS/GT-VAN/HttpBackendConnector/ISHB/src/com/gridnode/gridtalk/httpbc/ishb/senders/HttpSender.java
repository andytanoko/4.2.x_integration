/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 15 3007    i00107              Use GNProxyInitializer for proxy connections.
 * Feb 24 2007    i00107              Set response code & msg.
 * Mar 05 2007		Alain Ah Ming				Log warning if not handling errors
 * May 07 2007    i00107              GNDB00028341: Construct own SSL Context instead
 *                                    of using default.
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.security.KeyStore;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.*;

import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.util.StringUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.net.GNProxyInitializer;

/**
 * @author i00107
 * This class is responsible for sending transactions to backend systems via Http.
 */
public class HttpSender
{
  private ProxyAuthConnInfo _proxyAuth;
  private HttpURLConnection _conn;
  private int _respCode;
  private String _respMsg;
  
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "HttpSender");
  
  public HttpSender(ProxyAuthConnInfo proxyAuth)
  {
    setAuth(proxyAuth);
  }
  
  public void sendMessage(Properties headers, byte[] content) throws SenderException
  {
    String mtdName = "sendMessage";
    try
    {
      addRequestHeaders(headers);
      _conn.setRequestMethod("POST");
      _conn.setDoInput(true);
      _conn.setDoOutput(true);
      OutputStream os = _conn.getOutputStream();
      os.write(content);
      _respCode = _conn.getResponseCode();
      _respMsg = _conn.getResponseMessage();
      _logger.logMessage(mtdName, null, "Response: "+_respCode + ", "+_respMsg);
      //need response headers?
      if (_respCode != HttpURLConnection.HTTP_ACCEPTED && _respCode != HttpURLConnection.HTTP_OK)
      {
        throw new SenderException("Fail to send message, response is "+_respCode +" "+_respMsg);
      }
    }
    catch (IOException ex)
    {
      _logger.logWarn(mtdName, null, "Fail to send message", ex);
      throw new SenderException("IO Error. Fail to send message", ex);
    }
  }
  
  protected void addRequestHeaders(Properties headers)
  {
    if (headers != null)
    {
      for (Object key : headers.keySet())
      {
        String name = (String)key;
        String val = headers.getProperty(name);
        _conn.addRequestProperty(name, val);
      }
    }
  }

  public void createConnection(HttpTargetConnInfo targetConn) throws SenderException
  {
    String mtdName = "createConnection";
    Object[] params = {targetConn};
    try
    {
      URL url = new URL(targetConn.getHttpUrl());
      List<Proxy> proxyList = findProxy(url.toURI());
      if (proxyList != null && !proxyList.isEmpty())
      {
        for (Proxy proxy : proxyList)
        {
          if (openConnection(url, proxy))
          {
            _logger.debugMessage(mtdName, params, "Connection established.");
            break;
          }
        }
      }
      else if (openConnection(url))
      {
        _logger.debugMessage(mtdName, params, "Connection established.");
      }
    }
    catch (URISyntaxException ex)
    {
      _logger.logWarn(mtdName, params, "Unable to convert URL to URI", ex);
      throw new SenderException("Unable to convert URL to URI", ex);
    }
    catch (MalformedURLException ex)
    {
      _logger.logWarn(mtdName, params, "Invalid URL", ex);
      throw new SenderException("Invalid URL", ex);
    }
    
    if (_conn != null)
    {
      //if (_conn instanceof HttpsURLConnection && !targetConn.isVerifyHostName())
      if (_conn instanceof HttpsURLConnection)
      {
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(_proxyAuth.getKeyStore(), _proxyAuth.getKeyStorePass(), 
                                                                _proxyAuth.getTrustStore(), _proxyAuth.getTrustStorePass());

        
        HttpsURLConnection httpsConn = (HttpsURLConnection)_conn;
        httpsConn.setSSLSocketFactory(sslSocketFactory);
        if (!targetConn.isVerifyHostName())
        {
          httpsConn.setHostnameVerifier(new NoVerificationHostnameVerifier());
        }
      }
    }
    else
    {
      throw new SenderException("Unable to establish Http connection to "+targetConn.getHttpUrl());
    }
  }
  
  private boolean openConnection(URL url, Proxy proxy)
  {
    try
    {
      _conn = (HttpURLConnection)url.openConnection(proxy);
      return true;
    }
    catch (IOException ex)
    {
      Object[] params = {url, proxy};
      _logger.logWarn("openConnection", params, "Unable to open connection thru proxy", ex);
      return false;
    }
  }

  private boolean openConnection(URL url)
  {
    try
    {
      _conn = (HttpURLConnection)url.openConnection();
      return true;
    }
    catch (IOException ex)
    {
      Object[] params = {url};
      _logger.logWarn("openConnection", params, "Unable to open connection", ex);
      return false;
    }
  }

  public void closeConnection()
  {
    if (_conn != null)
    {
      _conn.disconnect();
      _conn = null;
    }
  }
  
  public int getResponseCode()
  {
    return _respCode;
  }
  
  public String getResponseMessage()
  {
    return _respMsg;
  }
  
  private List<Proxy> findProxy(URI uri)
  {
    try 
    { 
      return GNProxyInitializer.selectProxies(uri);
    }
    catch (IllegalArgumentException e)
    {
    }
    return null;
  }
  
  private void setAuth(ProxyAuthConnInfo proxyAuth)
  {
    GNProxyInitializer.init(proxyAuth);
    /*NSL20070507
    if (!StringUtil.isBlank(proxyAuth.getKeyStore()))
    {
      System.setProperty("javax.net.ssl.keyStore", proxyAuth.getKeyStore());
      System.setProperty("javax.net.ssl.keyStorePassword", proxyAuth.getKeyStorePass());
    }
    if (!StringUtil.isBlank(proxyAuth.getTrustStore()))
    {
      System.setProperty("javax.net.ssl.trustStore", proxyAuth.getTrustStore());
      System.setProperty("javax.net.ssl.trustStorePassword", proxyAuth.getTrustStore());
    }*/
    _proxyAuth = proxyAuth;
  }
  
  /**
   * Get the SSLSocketFactory for the specified keystore and truststore
   * @param keyStoreFile The keystore
   * @param keyStorePass The password for keystore
   * @param trustStoreFile The truststore
   * @param trustSttorePass the password for truststore
   * @throws SenderException Unable to initialize SSL Context
   */
  protected SSLSocketFactory getSSLSocketFactory(String keyStoreFile, String keyStorePass, String trustStoreFile, String trustStorePass) 
    throws SenderException
  {
    Object[] params = {keyStoreFile, "********", trustStoreFile, "********"};
    try
    {
      KeyManager[] km = getKeyManagers(keyStoreFile, keyStorePass);
      TrustManager[] tm = getTrustManagers(trustStoreFile, trustStorePass);
      
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(km, tm, null);
      
      return sslContext.getSocketFactory();
    }
    catch (Exception ex)
    {
      _logger.logWarn("getSSLSocketFactory", params, "Unable to initialize SSL Context", ex);
      throw new SenderException("Unable to initialise SSL Context", ex);
    }
  }
  
  /**
   * Get the KeyManagers for the the specified keystore
   * @param ksFile The keystore to manager
   * @param ksPass The keystore password
   * @return KeyManagers that can manage the keystore
   * @throws Exception
   */
  protected KeyManager[] getKeyManagers(String ksFile, String ksPass) throws Exception
  {
    if (StringUtil.isBlank(ksFile))
    {
      ksFile = System.getProperty("javax.net.ssl.keyStore");
    }
    if (StringUtil.isBlank(ksPass))
    {
      ksPass = System.getProperty("javax.net.ssl.keyStorePassword", "changeit");
    }
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(ksFile), ksPass.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, ksPass.toCharArray());
    
    return kmf.getKeyManagers();
  }
  
  /**
   * Get the TrustManagers for the specified trust store.
   * @param tsFile The trust store file
   * @param tsPass The trust store password
   * @return The TrustManagers that can manager the specified trust store.
   * @throws Exception
   */
  protected TrustManager[] getTrustManagers(String tsFile, String tsPass) throws Exception
  {
    if (StringUtil.isBlank(tsFile))
    {    
      tsFile = System.getProperty("javax.net.ssl.trustStore");
    }
    if (StringUtil.isBlank(tsPass))
    {
      tsPass = System.getProperty("javax.net.ssl.trustStorePassword", "changeit");
    }
    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(new FileInputStream(tsFile), tsPass.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(ts);
    return tmf.getTrustManagers();
  }


  private class NoVerificationHostnameVerifier implements HostnameVerifier
  {
    public boolean verify(String hostName, SSLSession session)
    {
      Object[] params = {hostName, session};
      _logger.debugMessage("verify", params, "Bypassing hostname verification.");
      return true;
    }
  }
}
