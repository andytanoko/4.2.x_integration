/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultArchiveDocumentEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream.
 */
package com.gridnode.gtas.client.ctrl;


public class DefaultArchiveDocumentEntity extends AbstractGTEntity implements IGTArchiveDocumentEntity, IGTVirtualEntity
{
	private String isEstore;
	
	public void setIsEstore(String isEstore)
	{
		this.isEstore = isEstore;
	}
	
	public String getIsEstore()
	{
		return isEstore;
	}
} 