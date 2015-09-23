/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsRetryListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 24, 2007   Yee Wah, Wong       #38  Created
 */
package com.gridnode.pdip.base.certificate.helpers;


import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.base.certificate.helpers.ICertificateConfig;



public class RNIFConfiguration {
	
	protected static Configuration config = ConfigurationManager.getInstance().getConfig(ICertificateConfig.CONFIG_NAME);

	public static long getSignCertTakeOverPeriod()
	{
		return config.getLong(ICertificateConfig.SIGN_CERT_TAKEOVER, 24) * 3600 * 1000; //default 24 hours
	}

	
}
