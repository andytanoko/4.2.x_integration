/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.cfg;

import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class PartnerConfig
{
	private static final String KEY_PARTNER_ID = "partner.id";
	private static final String KEY_DUNS = "duns";
	private static final String KEY_PIP_CODE = "pip.code";
	private static final String KEY_GRIDTALK_RN_URL = "gridtalk.rn.url";
  
  private String _partnerId = null;
  private String _duns = null;
  private URL _gridtalkRnUrl = null;
  private String _pipCode = null;

  private PartnerConfig()
  {
  	
  }
  
  public PartnerConfig(File f)
  {
  	if(f.exists())
  	{
  		Properties p = load(f);
  		init(p);
  	}
  }
  
	/**
	 * @return Returns the partnerId;
	 */
	public String getPartnerId()
	{
		return _partnerId;
	}

	/**
	 * @return Returns the duns.
	 */
	public String getDuns()
	{
		return _duns;
	}

	/**
	 * @return Returns the gridtalkRnUrl.
	 */
	public URL getGridtalkRnUrl()
	{
		return _gridtalkRnUrl;
	}
	
	/**
	 * @return Returns the pipCode.
	 */
	public String getPipCode()
	{
		return _pipCode;
	}

	private static Properties load(File f)
  {
  	Properties p = new Properties();
    try
    {
      p.load(new FileInputStream(f));
    }
    catch (IOException ex)
    {
      Logger.error(PartnerConfig.class.getSimpleName(), "load", "Unable to load properties", ex);
    }
    return p;
  }
  
  private void init(Properties p)
  {
  	String mn = "init";

  	_partnerId = p.getProperty(KEY_PARTNER_ID, null);
  	
  	_duns = p.getProperty(KEY_DUNS, null);
  	
		try
		{
			_gridtalkRnUrl = new URL(p.getProperty(KEY_GRIDTALK_RN_URL, null));
		}
		catch (Throwable e)
		{
			Logger.error(this.getClass().getSimpleName(), mn, "Failed to initialize URL", e);
		}
		
		_pipCode = p.getProperty(KEY_PIP_CODE, null);
  }
}
