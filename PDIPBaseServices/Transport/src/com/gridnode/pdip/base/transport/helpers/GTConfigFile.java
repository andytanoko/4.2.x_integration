package com.gridnode.pdip.base.transport.helpers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Enumeration;
import java.util.Hashtable;

public class GTConfigFile 
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
