/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtIbTestStateContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IGtTestStateContext;

import java.net.URL;

/**
 * Interface for a GridTalk Inbound test
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtIbTestStateContext extends IGtTestStateContext
{
	/**
	 * Return the GridTalk RNIF URL
	 * @return A <code>URL</code> object representing the
	 * GridTalk RNIF URL
	 */
	public URL getGtRnifUrl();
	
	/**
	 * Return the RNIF version
	 * @return The RNIF version
	 */
	public String getRnifVersion();
	
	/**
	 * Return the RNIF response type
	 * @return the RNIF response type
	 */
	public String getSendRnifResponseType();
	
	/**
	 * Return the RNIF message type
	 * @return The RNIF message type
	 */
	public String getSendRnifMessageType();
	
	public void terminate();
}
