/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchEsPiDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 21, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTSearchEsPiPageManager extends IGTManager
{
	/**
	 * Get the new instance for SearchEsDocDocumentEntity. The primary entity that the
	 * SearchEsPiPageManager handle is SearchEsPiDocumentEntity
	 * @return
	 */
	public IGTSearchEsDocDocumentEntity getSearchEsDocument() throws GTClientException;
}
