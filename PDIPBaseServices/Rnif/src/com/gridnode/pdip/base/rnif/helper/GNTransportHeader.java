package com.gridnode.pdip.base.rnif.helper;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */
import java.io.*;
import java.util.*;

import com.gridnode.pdip.framework.messaging.ICommonHeaders;

class GTConfigFile
{
  private Hashtable h = new Hashtable();
  private boolean casesensitive = false;

  public void setCasesensitive(boolean casesensitive)
  {
    this.casesensitive = casesensitive;
  }
  public boolean isCasesensitive()
  {
    return casesensitive;
  }

  public GTConfigFile()
  {
  }

 public GTConfigFile(Hashtable property)
  {
    h = property;
  }

  public GTConfigFile(String filename)
  {
    readConfigFile(filename);
  }

  public GTConfigFile(String[] args)
  {
    if(args != null && args.length > 0)
    {
      readConfigArg(args);
    }
  }

  protected void readConfigArg(String args[])
  {
      h = new Hashtable();
      try
      {
          for(int i = 0; i < args.length; i++)
          {
              String token = args[i];
               if(token.startsWith("-"))
                {
                    String value = "";
                    token = token.substring(1);
                    if(i + 1 < args.length)
                      value = args[i + 1];
                    else
                      value = "-";
                    if(value.startsWith("-"))
                      {
                        setBooleanProperty(token, true);
                        continue;
                      }
                    value = stripQuotes(value);
                    setProperty(token, value);
                }
          }
      }
      catch(Exception ex)
      {
      }
  }

  protected String stripQuotes(String value)
  {
      if(!value.equals(""))
      {
          if(value.startsWith("\""))
              value = value.substring(1);
          if(value.endsWith("\""))
              value = value.substring(0, value.length() - 1);
      }
      return value;
  }



  public Hashtable getProperties()
  {
    return h;
  }

  boolean writeConfigFile(String filename)
  {
  try
  {
    FileWriter fr = new FileWriter(filename);
    Enumeration en = h.keys();
    while(en.hasMoreElements())
    {
      String key = (String)en.nextElement();
      Object va = h.get(key);
      if(va instanceof String)
      {
        String value = (String)va;
        fr.write(key + "=" + value + "\r\n");
      }
    }
    fr.close();
    return true;
  }
  catch (Exception ex)
  {
    ex.printStackTrace();
    return false;
  }
}

  boolean readConfigFile(String filename)
  {
  try
  {
    h = new Hashtable();
    FileReader fr = new FileReader(filename);
    LineNumberReader lnr = new LineNumberReader(fr);
    String s = "dummy";

    while( s != null)
    {
      s = lnr.readLine();
      if ( (s!=null) && (s.length()!=0) && (s.charAt(0)!='#') && (s.charAt(0)!='!'))
      {
        // Get the equals sign
        int equalSignIndex = s.indexOf("=");
        if (equalSignIndex==-1)
           System.out.println("Line " + lnr.getLineNumber() + " [" + s +"] in properties file [" + filename + "] is invalid : no equals sign.");
        else
        {
          String propertyName = s.substring(0, equalSignIndex);
          String propertyValue = s.substring(equalSignIndex+1, s.length());
          h.put(propertyName, propertyValue);
        }
      }
    }
    return true;
  }
  catch (Exception ex)
  {
    ex.printStackTrace();
    return false;
  }
}

  public void removeAllProperty()
  {
    h = new Hashtable();
  }

  public void removeProperty(String propertyName)
  {
    setProperty(propertyName,null);
  }

  public void setProperty(Hashtable property)
  {
   if(property == null)
    return;
   Enumeration en = property.keys();
    while(en.hasMoreElements())
    {
      Object key = en.nextElement();
      Object value = property.get(key);
      setProperty(key, value);
    }
  }

  public Object findKeyName(Object propertyName)
  {
      if(!isCasesensitive())
      {
          Enumeration en = h.keys();
          while(en.hasMoreElements())
          {
            Object key = en.nextElement();
            String strkey = (String)key;
            if(strkey.equalsIgnoreCase((String)propertyName))
                return key;
          }
      }
      return propertyName;
  }

  public void setProperty(Object propertyName, Object propertyValue)
  {
    String keyname = (String)findKeyName(propertyName);
    if(propertyValue == null)
        h.remove(keyname);
    else
        h.put(keyname, propertyValue);
  }

  public String getProperty(String propertyName)
  {
    return getProperty(propertyName, "");
  }

  public String getProperty(String propertyName, String defaultvalue)
  {
    String keyname = (String)findKeyName(propertyName);
    Object ob = h.get(keyname);
    if(ob != null)
       return (String)ob;
    else
      return defaultvalue;
  }

  public boolean getBooleanProperty(String name)
  {
    return getBooleanProperty(name, false);
  }

  public boolean getBooleanProperty(String name, boolean defaultvalue)
  {
    String value = getProperty(name);
    if("".equals(value))
     return defaultvalue;
    try
    {
      return Boolean.valueOf(value).booleanValue();
    }
    catch(Exception e)
    {
      return false;
    }
  }


  public void setBooleanProperty(String name, boolean value)
  {
    if(value)
     setProperty(name, "true");
    else
     setProperty(name, "false");
  }

  public int getIntProperty(String name)
  {
    return getIntProperty(name, -1);
  }

  public int getIntProperty(String name, int defaultvalue)
  {
    String value = getProperty(name);
    if("".equals(value))
      return defaultvalue;
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception ex)
    {
      return -1;
    }
  }


  public void setIntProperty(String name, int value)
  {
     setProperty(name, "" + value);
  }

  public String toString()
  {
    Enumeration en = getProperties().keys();
    String content = "";
    while(en.hasMoreElements())
    {
      String key = (String)en.nextElement();
      Object value = getProperties().get(key);
      content += key + ": " + value + "\r\n";
    }
    return content;
  }

}

public class GNTransportHeader extends GTConfigFile
{
  public GNTransportHeader()
  {
    super();
  }

  public GNTransportHeader(Hashtable property)
  {
    super(property);
  }

  public GNTransportHeader(String[] args)
  {
    super(args);
  }

  public String getPackageType()
  {
    return getProperty(ICommonHeaders.PAYLOAD_TYPE);
   // ITransportConstants.PACKAGE_TYPE_KEY);
  }

  /*public String getSenderPackageType()
  {
    return getProperty(ITransportConstants.PACKAGE_TYPE_SENDER_KEY);
  }


  public void setSenderPackageType(String senderPackageType)
  {
    setProperty(ITransportConstants.PACKAGE_TYPE_SENDER_KEY, senderPackageType);
  }
*/
  public void setPackageType(String PackageType)
  {
    /** @todo Need to change this header from common headers to transport specific headers */
    /**
     * This header property is a transport property. This property is transformed from
     * ICommonHeaders.PAYLOAD_TYPE (NONE,RNIF1,RNIF2) to a transport specific property,
     * which is PACKAGE_TYPE_KEY and corresponds to value - GTAS_PACKAGE,RNIF_PACKAGE,...
     */
    setProperty(ICommonHeaders.PAYLOAD_TYPE,PackageType);
    //ITransportConstants.PACKAGE_TYPE_KEY, PackageType);
  }

/*  public void setSenderPayloadMessage()
  {
    setSenderPackageType(ITransportConstants.PACKAGE_TYPE_SENDER_PAYLOAD);
  }

  public void setSenderCMDMessage()
  {
    setSenderPackageType(ITransportConstants.PACKAGE_TYPE_SENDER_CMD);
  }

  public boolean isSenderCMD_SetTrustStore()
  {
    return getSenderCMD().equals(ITransportConstants.SENDER_CMD_SET_TRUSTSTORE);
  }

  public boolean isSenderCMD_SetKeyStore()
  {
    return getSenderCMD().equals(ITransportConstants.SENDER_CMD_SET_KEYSTORE);
  }

  public void setSenderCMD_SetTrustStore()
  {
    setSenderCMD(ITransportConstants.SENDER_CMD_SET_TRUSTSTORE);
  }

  public void setSenderCMD_SetKeyStore()
  {
    setSenderCMD(ITransportConstants.SENDER_CMD_SET_KEYSTORE);
  }

  public void setSenderCMD(String cmd)
  {
    setSenderCMDMessage();
    setProperty(ITransportConstants.SENDER_CMD_KEY, cmd);
  }

  public String getSenderCMD()
  {
    if(!isSenderCMDMessage())
      return "";
    return getProperty(ITransportConstants.SENDER_CMD_KEY);
  }

  public void setGTASMessage()
  {
    setPackageType(ITransportConstants.PACKAGE_TYPE_GTAS);
  }

  public void setRNMessage()
  {
      setPackageType(ITransportConstants.PACKAGE_TYPE_RosettaNet);
  }

  public boolean isSenderPayloadMessage()
  {
    return getSenderPackageType().equals(ITransportConstants.PACKAGE_TYPE_SENDER_PAYLOAD);
  }

  public boolean isSenderCMDMessage()
  {
    return getSenderPackageType().equals(ITransportConstants.PACKAGE_TYPE_SENDER_CMD);
  }

  public boolean isGTASMessage()
  {
    return getPackageType().equals(ITransportConstants.PACKAGE_TYPE_GTAS);
  }

  public boolean isRNMessage()
  {
    return getPackageType().equals(ITransportConstants.PACKAGE_TYPE_RosettaNet);
  }
*/
  public boolean isNativeRNMessage()
  {
    //rnif 1.1
    if(getContentType().indexOf("application/x-rosettanet-agent") >= 0)
      return true;
    //rnif2.0
    return   getRNVersion().length() > 0;
  }

  public String getRNVersion()
  {
    return getProperty(IRNHeaderConstants.RN_VERSION_KEY);
  }

  public void setRNVersion(String RNVersion)
  {
    setProperty(IRNHeaderConstants.RN_VERSION_KEY, RNVersion);
  }

  public void setRNSyncMessage(boolean isSyn)
  {
    if(isSyn)
      setProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY, IRNHeaderConstants.RN_RESPONSE_TYPE_SYNC);
    else
      setProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY, IRNHeaderConstants.RN_RESPONSE_TYPE_ASYNC);
  }

  public boolean isRNReplyMessage()
  {
    return getBooleanProperty(IRNHeaderConstants.GN_MESSAGE_RESPONSE_KEY);
  }

  public void setRNReplyMessage(boolean response)
  {
    setBooleanProperty(IRNHeaderConstants.GN_MESSAGE_RESPONSE_KEY, response);
  }


  public boolean isRNSyncMessage()
  {
      String RNSync = getProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY);
      //boolean isRNSync = false;
      if(RNSync != null && RNSync.equals(IRNHeaderConstants.RN_RESPONSE_TYPE_SYNC))
          return true;
      else
         return false;
  }

  public String  getRNSyncMessageID()
  {
    return getProperty(IRNHeaderConstants.GN_MESSAGE_ID_KEY);
  }

  public void    setRNSyncMessageID(String ID)
  {
    setProperty(IRNHeaderConstants.GN_MESSAGE_ID_KEY, ID);
  }

  public void setContentType(String contentType)
  {
    setProperty(IRNHeaderConstants.CONTENT_TYPE_KEY, contentType);
  }

  public String getContentType()
  {
    return getProperty(IRNHeaderConstants.CONTENT_TYPE_KEY);
  }

  public void setType(String type)
  {
    setProperty(IRNHeaderConstants.TYPE_KEY, type);
  }

  public String getType()
  {
      return getProperty(IRNHeaderConstants.TYPE_KEY);
  }
}