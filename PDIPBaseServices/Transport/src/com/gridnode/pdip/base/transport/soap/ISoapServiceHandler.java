/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISoapServiceHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * SEP 23 2003     Jagadeesh               Created
 */

package com.gridnode.pdip.base.transport.soap;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.exceptions.GNTransportException;

public interface ISoapServiceHandler
{
	public void setCommInfo(ICommInfo commInfo);

	public Object invokeService(GNTransportPayload payLoad)
		throws GNTransportException;
}