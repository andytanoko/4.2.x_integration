/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtObTestStateContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IGtTestStateContext;

import java.net.URL;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtObTestStateContext extends IGtTestStateContext
{
	/**
	 * Return the URL of the GridTalk back-end
	 * @return A <code>URL</code> object representing
	 * the GridTalk back-end
	 */
	public URL getGtBackendUrl();
	
	/**
	 * Return the URL of the GridTalk RN ACK listener
	 * @return A <code>URL</code> object representing
	 * the GridTalk RN ACK listener
	 */
	public URL getGtRnifAckUrl();
	
	/**
	 * Get the HTTP message type for back-end
	 * messages sent to GridTalk Backend HTTP Connector 
	 * @return The HTTP message type for back-end
	 * messages sent to GridTalk Backend HTTP Connector
	 */
	public String getHttpBackendMessageType();
	
	public void terminate();
}
