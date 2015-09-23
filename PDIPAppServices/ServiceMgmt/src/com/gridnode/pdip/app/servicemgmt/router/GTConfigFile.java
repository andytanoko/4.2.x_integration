package com.gridnode.pdip.app.servicemgmt.router;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTConfigFile.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 */

import java.io.*;
import java.util.*;

public class GTConfigFile
{
  private Hashtable h = new Hashtable();
  private boolean casesensitive = false;
  private boolean autofix = true;
  private boolean autocomp = false;

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

  public void readConfigArg(String args[])
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

  public boolean writeConfigFile(String filename)
  {
  try
  {
    FileWriter fr = new FileWriter(filename);
    Vector keys = getKeys();
     for(int i = 0; i < keys.size();i++)
     {
        String key = (String)keys.get(i);
        Object value = getProperties().get(key);
        fr.write(key + "=" + value + "\r\n");
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

  public boolean readConfigFile(String filename)
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
    //ex.printStackTrace();
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

  public void setStringProperty(String name, String value)
  {
    setProperty(name, value);
  }

  public String getStringProperty(String name, String defaultvalue)
  {
    String para = getProperty(name, defaultvalue);
    if(para.equals("true") || para.equals("false") || para.equals("TRUE") || para.equals("FALSE"))
      {
        if(isAutofix())
          setProperty(name, defaultvalue);
        para = defaultvalue;
      }
    if(isAutocomp())
      setStringProperty(name, para);
    return para;
  }

  public String getStringProperty(String name)
  {
    return getStringProperty(name, "");
  }

  public boolean getBooleanProperty(String name)
  {
    return getBooleanProperty(name, false);
  }

  public boolean getBooleanProperty(String name, boolean defaultvalue)
  {
    String value = getProperty(name);
    if("".equals(value))
     {
        if(isAutocomp())
          setBooleanProperty(name, defaultvalue);
        return defaultvalue;
     }
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
    String value = getStringProperty(name);
    if("".equals(value))
      {
        if(isAutocomp())
          setIntProperty(name, defaultvalue);
        return defaultvalue;
      }
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

  public Vector sortKeys(Vector data)
  {
    if(data == null || data.size() <= 1)
     return data;
    for(int i = 0; i < data.size() - 1; i++)
    {
      String key = (String)data.get(i);
      int pos = i;
      for(int j = i + 1; j < data.size(); j++)
        {
          String key1 = (String)data.get(j);
          if(key1.compareTo(key) < 0)
            {
              key = key1;
              pos = j;
            }
        }
      if(i != pos)
      {
        String t = (String)data.get(pos);
        String t1 = (String)data.get(i);
        data.set(i, t);
        data.set(pos, t1);
      }
    }
    return data;
  }

  public Vector getKeys()
  {
    Vector keys = new Vector();
    Enumeration en = getProperties().keys();
    //String content = "";
    while(en.hasMoreElements())
    {
      String key = (String)en.nextElement();
      keys.add(key);
    }
    return sortKeys(keys);
  }

  public String toString()
  {
     String content = "";
     Vector keys = getKeys();
     for(int i = 0; i < keys.size();i++)
     {
        String key = (String)keys.get(i);
        Object value = getProperties().get(key);
        content += key + ": " + value + "\r\n";
     }
      return content;
  }
  public boolean isAutofix()
  {
    return autofix;
  }
  public void setAutofix(boolean autofix)
  {
    this.autofix = autofix;
  }
  public boolean isAutocomp()
  {
    return autocomp;
  }
  public void setAutocomp(boolean autocomp)
  {
    this.autocomp = autocomp;
  }
}

