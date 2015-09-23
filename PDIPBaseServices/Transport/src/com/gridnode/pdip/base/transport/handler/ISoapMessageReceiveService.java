/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISoapMessageReceiveService.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 25 2002    Jagadeesh               Created
 * Oct 11 2010    Tam Wei Xiang           #1900 - Throw GtasWsWxception instead of
 *                                        the generic RemoteException as the soap client
 *                                        can not generic the stub correctly
 */

package com.gridnode.pdip.base.transport.handler;

import javax.activation.DataHandler;

import com.gridnode.pdip.base.transport.soap.exception.GtasWsException;

/**
 * This Interface defines service methods, essentially responsible for 
 * receiving messages through SOAP Protocol.
 * 
 * @author Jagadeesh.
 * @since 2.2
 * @see SoapMessageReceiverService
 */


public interface ISoapMessageReceiveService
{
	public void receiveMessage(
		String[] attributeNames,
		String[] attributeValues,
		DataHandler inboundMessage)
		throws GtasWsException;
}