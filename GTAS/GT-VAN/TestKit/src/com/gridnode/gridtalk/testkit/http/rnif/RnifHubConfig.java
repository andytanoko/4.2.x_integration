/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifHubConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 */
 
package com.gridnode.gridtalk.testkit.http.rnif;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * HUB config for RNIF test case
 * @author i00107
 */
public class RnifHubConfig
{
  private File _baseDir;
  private Properties _props = new Properties();
  private boolean _valid = false;
  private StringBuffer _error = new StringBuffer();
  
  private int _sendInterval;
  private int _sendNumTx;
  private File _actionTemplate;
  private Properties _actionProp;
  private File _ackTemplate;
  private Properties _ackProp;
  private URL _url;
  private int _ackSendDelay;

  /**
   * 
   * @param f Hub config file
   * @param respondMode Whether to load hub config for respond mode.
   */
  public RnifHubConfig(File f, boolean respondMode)
  {
    load(f, respondMode);
  }

  /**
   * 
   * @return <b>true</b> if the hub config is valid
   */
  public boolean isValid()
  {
    return _valid;
  }
  
  /**
   * 
   * @return Errors in the hub config file
   */
  public String getError()
  {
    return _error.toString();
  }
  
  private void load(File f, boolean respondMode)
  {
    try
    {
      FileInputStream fis = new FileInputStream(f);
      _props.load(fis);
      _baseDir = f.getParentFile();
      validate(respondMode);
      fis.close();
    }
    catch (Exception ex)
    {
      System.out.println("[RnifHubConfig.load()] Error reading properties");
      ex.printStackTrace();
      _error.append("Error reading properties: "+ex.getMessage());
    }
  }
  
  private void validate(boolean respondMode)
  {
    validateCommon();
    if (respondMode)
    {
      validateRespondMode();
    }
    else
    {
      validateInitiateMode();
    }
    
    if (_error.length()==0)
    {
      _valid = true;
    }
  }
  
  private void validateCommon()
  {
    String duns = getDuns();
    if (duns == null || duns.trim().length()==0)
    {
      _error.append("'duns' not specified.<br>");
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
        System.out.println("[RnifHubConfig.validate()] 'url' is not in a valid URL format: "+url);
        ex.printStackTrace();
      }
    }
    
  }

  private void validateInitiateMode()
  {
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
        System.out.println("[RnifHubConfig.validate()] 'send.interval' is not an integer: "+sendIntStr);
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
        System.out.println("[RnifHubConfig.validate()] 'send.num.tx' is not an integer: "+sendNumTxStr);
        ex.printStackTrace();
      }
    }
    
    String actionTemplate = getActionTemplate();
    if (actionTemplate == null || actionTemplate.trim().length()==0)
    {
      _error.append("'action.template' not specified.<br>");
    }
    else
    {
      _actionTemplate = new File(_baseDir, actionTemplate);
      if (!_actionTemplate.isFile() || !_actionTemplate.exists())
      {
        _error.append("action template file "+actionTemplate+ " does not exist.<br>");
      }
    }
    String actionProp = getActionProp();
    if (actionProp == null || actionProp.trim().length()==0)
    {
      _error.append("'action.properties' not specified.<br>");
    }
    else
    {
      File f = new File(_baseDir, actionProp);
      if (!f.isFile() || !f.exists())
      {
        _error.append("action properties file "+actionProp+ " does not exist.<br>");
      }
      else
      {
        _actionProp = getProperties(f);
      }
    }
    
  }
  
  private void validateRespondMode()
  {
    String ackSendDelayStr = getAckSendDelayStr();
    try
    {
      _ackSendDelay = Integer.parseInt(ackSendDelayStr);
    }
    catch (NumberFormatException ex)
    {
      _error.append("'ack.send.delay' is not an integer: "+ackSendDelayStr+"<br>");
      System.out.println("[RnifHubConfig.validate()] 'ack.send.delay' is not an integer: "+ackSendDelayStr);
      ex.printStackTrace();
    }
    
    String ackTemplate = getAckTemplate();
    if (ackTemplate == null || ackTemplate.trim().length()==0)
    {
      _error.append("'ack.template' not specified.<br>");
    }
    else
    {
    	
      _ackTemplate = new File(_baseDir, ackTemplate);
      if (!_ackTemplate.isFile() || !_ackTemplate.exists())
      {
        _error.append("ack template file "+ackTemplate+ " does not exist.<br>");
      }
    }
    String ackProp = getAckProp();
    if (ackProp == null || ackProp.trim().length()==0)
    {
      _error.append("'ack.properties' not specified.<br>");
    }
    else
    {
      File f = new File(_baseDir, ackProp);
      if (!f.isFile() || !f.exists())
      {
        _error.append("ack properties file "+ackProp+ " does not exist.<br>");
      }
      else
      {
        _ackProp = getProperties(f);
      }
    }
  }
  
  /**
   * 
   * @return Duns number of Hub
   */
  public String getDuns()
  {
    return _props.getProperty("duns", null);
  }
  
  /**
   * 
   * @return URL to send to RNIF receiver
   */
  public String getUrlStr()
  {
    return _props.getProperty("url", null);
  }
  
  /**
   * 
   * @return URL to send to RNIF receiver
   */
  public URL getURL()
  {
    return _url;
  }
  
  /**
   * 
   * @return Interval between each batch of sending
   */
  public String getSendIntervalStr()
  {
    return _props.getProperty("send.interval", null);
  }
  
  /**
   * 
   * @return Interval between each batch of sending
   */
  public int getSendInterval()
  {
    return _sendInterval;
  }
  
  /**
   * 
   * @return Number of transactions to send in each batch
   */
  public String getSendNumTxStr()
  {
    return _props.getProperty("send.num.tx", null);
  }
  
  /**
   * 
   * @return Number of transactions to send in each batch
   */
  public int getSendNumTx()
  {
    return _sendNumTx;
  }
  
  /**
   * 
   * @return Template (filename) to use to generate action message
   */
  public String getActionTemplate()
  {
    return _props.getProperty("action.template", null);
  }
  
  /**
   * 
   * @return Template to use to generate action message
   */
  public File getActionTemplateFile()
  {
    return _actionTemplate;
  }
  
  /**
   * 
   * @return Properties (filename) to replace into action template during
   * generation of action message
   */
  public String getActionProp()
  {
    return _props.getProperty("action.properties", null);
  }
  
  /**
   * 
   * @return Properties to replace into action template during
   * generation of action message
   */
  public Properties getActionProperties()
  {
    return _actionProp;
  }
  
  /**
   * 
   * @return Template (filename) to use to generate ack.
   */
  public String getAckTemplate()
  {
    return _props.getProperty("ack.template", null);
  }
  
  /**
   * 
   * @return Template to use to generate ack.
   */
  public File getAckTemplateFile()
  {
    return _ackTemplate;
  }
  
  /**
   * 
   * @return Properties (filename) to replace into ack template during
   * generation of ack message
   */
  public String getAckProp()
  {
    return _props.getProperty("ack.properties", null);
  }
  
  /**
   * 
   * @return Properties to replace into ack template during
   * generation of ack message
   */
  public Properties getAckProperties()
  {
    return _ackProp;
  }
  
  /**
   * 
   * @return Delay in sending ack upon receiving action message
   */
  public String getAckSendDelayStr()
  {
    return _props.getProperty("ack.send.delay", "10");
  }
  
  /**
   * 
   * @return Delay in sending ack upon receiving action message
   */
  public int getAckSendDelay()
  {
    return _ackSendDelay;
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
