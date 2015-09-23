/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IHeaderCategory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 26 2003    Jagadeesh           Created
 */
package com.gridnode.pdip.framework.messaging;

/**
 * The IHeaderCategory Interface defines the available category of 
 * headers. At present the headers are categorized, as 1.Common Headers,
 * 2. Message Specific Headers.
 * 
 * Any Header (Key-Value pair) should belong to any of the category defined 
 * in this Interface.
 * 
 * All Headers belonging to a perticular category should specify to which category
 * they belong,(by defining the HEADER_CATEGORY key). 
 * 
 * @see ICommonHeaders 
 * 
 * @author Jagadeesh.
 * @since 2.3
 *
 */

public interface IHeaderCategory
{
	//Common Header Category.
	public static final String CATEGORY_COMMON_HEADERS = "category.common.headers";		
	
	//Message Header Category.
	public static final String CATEGORY_MESSAGE_HEADERS = "category.message.headers";		 

}
