package com.gridnode.gtas.server.rnif.helpers.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>Title: J2EE</p>
 * <p>Description: Jbuilder 6</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Gridnode</p>
 * @author Qingsong
 * @version 1.0
 */

public class GNConfigFile
{
  protected Hashtable h= new Hashtable();
  public static String stripQuotes(String value)
  {
    if (!value.equals(""))
    {
      if (value.startsWith("\""))
        value= value.substring(1);
      if (value.endsWith("\""))
        value= value.substring(0, value.length() - 1);
    }
    return value;
  }

  public GNConfigFile()
  {
  }

  public GNConfigFile(Hashtable property)
  {
    setProperty(property);
  }

  public GNConfigFile(String filename)
  {
    readConfigFile(filename);
  }

  public Hashtable getProperties()
  {
    return h;
  }

  boolean writeConfigFile(String filename)
  {
    try
    {
      FileWriter fr= new FileWriter(filename);
      writeHashtable(fr, h);
      fr.close();
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  void writeHashtable(FileWriter fr, Hashtable table) throws IOException
  {
    Enumeration en= table.keys();
    while (en.hasMoreElements())
    {
      String key= (String) en.nextElement();
      Object va= table.get(key);
      if (va instanceof String)
      {
        String value= (String) va;
        fr.write(key + "=" + value + "\r\n");
      }else
      {
        if(va instanceof Hashtable)
        {
          fr.write(key);
            
            writeHashtable(fr, (Hashtable)va);
          
          fr.write("***");
        }
      }
    }
  }

  boolean readConfigFile(String filename)
  {
    try
    {
      h= new Hashtable();
      ArrayList hashList = new ArrayList();
      hashList.add(h);
      Hashtable curHash = h;
            
      FileReader fr= new FileReader(filename);
      LineNumberReader lnr= new LineNumberReader(fr);
      String s= "dummy";
      
      while (s != null)
      {
        s= lnr.readLine();
        if ((s != null) && (s.length() != 0) && (s.charAt(0) != '#'))
        {
          // Get the equals sign
          int equalSignIndex= s.indexOf("=");
          if (equalSignIndex == -1)
          {
            System.out.println(
              "Line "
                + lnr.getLineNumber()
                + " ["
                + s
                + "] in properties file ["
                + filename
                + "] is invalid : no equals sign.");
            if("***".equals(s))
            {
              hashList.remove(hashList.size()-1);
              curHash = (Hashtable) hashList.get(hashList.size()-1);
            }
            else
            {
            Hashtable newHash = new Hashtable();
            curHash.put(s, newHash);
            hashList.add(newHash);
            curHash = newHash;
            }
          }
          else
          {
            String propertyName= s.substring(0, equalSignIndex);
            String propertyValue= s.substring(equalSignIndex + 1, s.length());
            curHash.put(propertyName, propertyValue);
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
    h= new Hashtable();
  }

  public void removeProperty(String propertyName)
  {
    setProperty(propertyName, null);
  }

  public void setProperty(Hashtable property)
  {
    if (property == null)
      return;
    Enumeration en= property.keys();
    while (en.hasMoreElements())
    {
      Object key= en.nextElement();
      Object value= property.get(key);
      setProperty(key, value);
    }
  }

  public void setProperty(Object propertyName, Object propertyValue)
  {
    if (propertyValue == null)
      h.remove(propertyName);
    else
      h.put(propertyName, propertyValue);
  }

  public String getProperty(String propertyName)
  {
    Object ob= h.get(propertyName);
    if (ob != null)
      return (String) ob;
    else
      return "";
  }
  public boolean getBooleanProperty(String name)
  {
    String value= getProperty(name);
    if ("true".equals(value) || "TRUE".equals(value) || "True".equals(value) || "1".equals(value))
      return true;
    else
      return false;
  }

  public void setBooleanProperty(String name, boolean value)
  {
    if (value)
      setProperty(name, "true");
    else
      setProperty(name, "false");
  }

  public int getIntProperty(String name)
  {
    return Integer.parseInt(getProperty(name).toString());
  }

  public void setIntProperty(String name, int value)
  {
    setProperty(name, "" + value);
  }
}