/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigMgr.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.cfg;

import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

/**
 * Manager class to access the configuration data
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ConfigMgr
{
	private static final String CFG_FILE_EXT = ".conf";
	
	private static final String PARTNER_CFG_FILENAME = "partner"+CFG_FILE_EXT;
	private static final String BACKEND_CFG_FILENAME = "backend"+CFG_FILE_EXT;
	private static final String MAIN_CFG_FILENAME = "main"+CFG_FILE_EXT;
	
	private static PartnerConfig _partnerCfg = 
		new PartnerConfig(FileUtil.getFile(FileUtil.TYPE_CONF, PARTNER_CFG_FILENAME));
	
	private static MainConfig _mainCfg = 
		new MainConfig(FileUtil.getFile(FileUtil.TYPE_CONF, MAIN_CFG_FILENAME));
	
	private static BackendConfig _backendCfg = 
		new BackendConfig(FileUtil.getFile(FileUtil.TYPE_CONF, BACKEND_CFG_FILENAME));
	
	private ConfigMgr()
	{
		
	}
	
	public static PartnerConfig getPartnerConfig()
	{
		return _partnerCfg;
	}

	public static MainConfig getMainConfig()
	{
		return _mainCfg;
	}
	
	public static BackendConfig getBackendConfig()
	{
		return _backendCfg;
	}
}
