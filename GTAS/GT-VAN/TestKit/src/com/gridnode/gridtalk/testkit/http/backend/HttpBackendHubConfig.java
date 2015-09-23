/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpBackendHubConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 * Aug 23, 2007   Vineeth          Added Methods- getActionTemplateStr(), getActionPropertiesStr(), getAckTemplateStr(), getAckPropertiesStr()
 * Aug 28, 2007   Vineeth          Added Method- getPropField()
 */
 
package com.gridnode.gridtalk.testkit.http.backend;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Configuration for Hub in Backend send test case.
 * @author i00107
 */
public class HttpBackendHubConfig
{
  private File _baseDir;
  private Properties _props = new Properties();
  private boolean _valid = false;
  private StringBuffer _error = new StringBuffer();
  
  private int _sendInterval;
  private int _sendNumTx;
  private File _docTemplate;
  private Properties _docProp;
  private URL _url;

  /**
   * @param f Hub config file
   */
  public HttpBackendHubConfig(File f)
  {
    load(f);
  }

  /**
   * @return <b>true</b> if the hub config file is valid.
   */
  public boolean isValid()
  {
    return _valid;
  }
  
  /**
   * @return Errors in hub config file.
   */
  public String getError()
  {
    return _error.toString();
  }
  
  private void load(File f)
  {
    try
    {
      FileInputStream fis = new FileInputStream(f);
      _props.load(fis);
      _baseDir = f.getParentFile();
      validate();
      fis.close();
    }
    catch (Exception ex)
    {
      System.out.println("[HttpBackendHubConfig.load()] Error reading properties");
      ex.printStackTrace();
      _error.append("Error reading properties: "+ex.getMessage());
    }
  }
  
  private void validate()
  {
    String beid = getBEid();
    if (beid == null || beid.trim().length()==0)
    {
      _error.append("'beid' not specified.<br>");
    }
    String url = getUrlStr();
    if (url == null || url.trim().length()==0)
    {
      _error.append("'url' not specified.<br>");
    }
    else
    {
      try
      {
        _url = new URL(url);
      }
      catch (MalformedURLException ex)
      {
        _error.append("'url' is not in a valid URL format: "+url+"<br>");
        System.out.println("[HttpBackendHubConfig.validate()] 'url' is not in a valid URL format: "+url);
        ex.printStackTrace();
      }
    }
    
    String sendIntStr = getSendIntervalStr();
    if (sendIntStr == null || sendIntStr.trim().length()==0)
    {
      _error.append("'send.interval' not specified.<br>");
    }
    else
    {
      try
      {
        _sendInterval = Integer.parseInt(sendIntStr);
      }
      catch (NumberFormatException ex)
      {
        _error.append("'send.interval' is not an integer: "+sendIntStr+"<br>");
        System.out.println("[HttpBackendHubConfig.validate()] 'send.interval' is not an integer: "+sendIntStr);
        ex.printStackTrace();
      }
    }
    String sendNumTxStr = getSendNumTxStr();
    if (sendNumTxStr == null || sendNumTxStr.trim().length()==0)
    {
      _error.append("'send.num.tx' not specified.<br>");
    }
    else
    {
      try
      {
        _sendNumTx = Integer.parseInt(sendNumTxStr);
      }
      catch (NumberFormatException ex)
      {
        _error.append("'send.num.tx' is not an integer: "+sendNumTxStr+"<br>");
        System.out.println("[HttpBackendHubConfig.validate()] 'send.num.tx' is not an integer: "+sendNumTxStr);
        ex.printStackTrace();
      }
    }
    
    String docTemplate = getDocTemplate();
    if (docTemplate == null || docTemplate.trim().length()==0)
    {
      _error.append("'doc.template' not specified.<br>");
    }
    else
    {
      _docTemplate = new File(_baseDir, docTemplate);
      if (!_docTemplate.isFile() || !_docTemplate.exists())
      {
        _error.append("doc template file "+docTemplate+ " does not exist.<br>");
      }
    }
    String docProp = getDocProp();
    if (docProp == null || docProp.trim().length()==0)
    {
      _error.append("'doc.properties' not specified.<br>");
    }
    else
    {
      File f = new File(_baseDir, docProp);
      if (!f.isFile() || !f.exists())
      {
        _error.append("doc properties file "+docProp+ " does not exist.<br>");
      }
      else
      {
        _docProp = getProperties(f);
      }
    }
    if (_error.length()==0)
    {
      _valid = true;
    }
  }
  
  /**
   * @return Business entity id of Hub
   */
  public String getBEid()
  {
    return _props.getProperty("beid", null);
  }
  public String getDuns()
  {
    return _props.getProperty("duns", null);
  }
  /**
   * @return URL to send to HTTP backend connector
   */
  public String getUrlStr()
  {
    return _props.getProperty("url", null);
  }
  
  /**
   * @return URL to send to HTTP backend connector
   */
  public URL getURL()
  {
    return _url;
  }
  
  /**
   * @return Interval to between each send.
   */
  public String getSendIntervalStr()
  {
    return _props.getProperty("send.interval", null);
  }
  
  /**
   * @return Interval to between each send.
   */
  public int getSendInterval()
  {
    return _sendInterval;
  }
  
  /**
   * @return Number of transactions to send each time.
   */
  public String getSendNumTxStr()
  {
    return _props.getProperty("send.num.tx", null);
  }
  
  /**
   * @return Number of transactions to send each time.
   */
  public int getSendNumTx()
  {
    return _sendNumTx;
  }
  
  /**
   * @return Document template (filename) to use for generating messages.
   */
  public String getDocTemplate()
  {
    return _props.getProperty("doc.template", null);
  }
  /**
   * @return Action template to use for generating messages.
   */
  public String getActionTemplateStr()
  {
    return _props.getProperty("action.template", null);
  }
  /**
   * @return Action Properties to use for generating messages.
   */
  public String getActionPropertiesStr()
  {
    return _props.getProperty("action.properties", null);
  }
  /**
   * @return Acknowledgement template to use for generating messages.
   */
  public String getAckTemplateStr()
  {
    return _props.getProperty("ack.template", null);
  }
  /**
   * @return Acknowledgement Properties to use for generating messages.
   */
  public String getAckPropertiesStr()
  {
    return _props.getProperty("ack.properties", null);
  }
  
  /**
   * @return Document template file to use for generating messages.
   */
  public File getDocTemplateFile()
  {
    return _docTemplate;
  }
  
  /**
   * @return Document properties (filename) to set into HTTP headers during send
   */
  public String getDocProp()
  {
    return _props.getProperty("doc.properties", null);
  }
  
  /**
   * @return Document properties to set into HTTP headers during send
   */
  public Properties getDocProperties()
  {
    return _docProp;
  }
  /**
   * @return value for the corresponding key in the properties file tp.conf
   */
  public String getPropField(String f)
  {
    return _props.getProperty(f, null) ;
  }
  
  
  private Properties getProperties(File f)
  {
    Properties prop = new Properties();
    try
    {
      prop.load(new FileInputStream(f));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return prop;
  }

}
