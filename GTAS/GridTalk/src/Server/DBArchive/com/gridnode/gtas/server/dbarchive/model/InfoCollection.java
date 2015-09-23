/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InfoCollection.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 7, 2005        Tam Wei Xiang       Created
 */

package com.gridnode.gtas.server.dbarchive.model;

import java.util.ArrayList;

/**
 * The class contain a list of info object eg. DocInfo or ProcessInstanceInfo.
 * It is used when ObjectSeliarize of ObjectDeserialize
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
*/
public class InfoCollection
{
	private ArrayList infolist = new ArrayList();;
	
	public InfoCollection()
	{
	}
	
	public InfoCollection(ArrayList a)
	{
		this();
		infolist.addAll(a);
	}
	
	public void addInfo(Object info)
	{
		infolist.add(info);
	}
	
	public ArrayList getInfoList()
	{
		return infolist;
	}
}
