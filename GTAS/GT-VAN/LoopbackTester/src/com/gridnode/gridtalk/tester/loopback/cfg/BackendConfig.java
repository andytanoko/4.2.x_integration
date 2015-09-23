/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.gridnode.gridtalk.tester.loopback.log.Logger;

/**
 * The configuration required by GLBT when simulating the backend
 * @author Alain Ah Ming
 *
 */
public class BackendConfig
{
	/*
	 * be.duns=999999999
be.id=TBKN
httpbc.url
	 */
	
	private static final String KEY_BE_ID = "be.id";
	private static final String KEY_DUNS = "duns";
	private static final String KEY_HTTPBC_URL = "httpbc.url";
	private static final String KEY_DOC_TYPE = "doc.type";
	
	private File _configFile = null;
	private String _duns = null;
	private String _beId = null;
	private URL _httpbcUrl = null;
	private String _docType = null;
	
	/**
	 * 
	 */
	private BackendConfig()
	{
	}
	
	public BackendConfig(File configFile)
	{
		try
		{
			Properties p = load(configFile);
			init(p);
		}
		catch (IOException e)
		{
			String fileName = configFile != null? configFile.getAbsolutePath():null;
				
			logError("<init>", "Failed to load config file: "+fileName, e);
		}
	}

	private Properties load(File configFile) throws IOException
	{
		_configFile = configFile;
		FileInputStream fis = new FileInputStream(_configFile);
		Properties p  = new Properties();
		p.load(fis);
		fis.close();
		return p;
	}
	
	private void init(Properties p)
	{		
		String mn = "init";
		_beId = p.getProperty(KEY_BE_ID, null);
		_docType = p.getProperty(KEY_DOC_TYPE, null);
		_duns = p.getProperty(KEY_DUNS, null);
		String urlStr = p.getProperty(KEY_HTTPBC_URL, null);
		if(urlStr != null && !urlStr.trim().equals(""))
		{
			try
			{
				_httpbcUrl = new URL(urlStr);
			}
			catch (MalformedURLException e)
			{
				logError(mn, "Invalid URL String: " +urlStr, e);
			}
		}
	}
	
	/**
	 * @return Returns the beId.
	 */
	public String getBeId()
	{
		return _beId;
	}

	/**
	 * @return Returns the duns.
	 */
	public String getDuns()
	{
		return _duns;
	}

	/**
	 * @return Returns the httpbcUrl.
	 */
	public URL getHttpbcUrl()
	{
		return _httpbcUrl;
	}

	private void logError(String mn, String msg, Throwable t)
	{
		Logger.error(getClass().getSimpleName(), mn, msg,t);
	}

	/**
	 * @return Returns the docType.
	 */
	public String getDocType()
	{
		return _docType;
	}
}
