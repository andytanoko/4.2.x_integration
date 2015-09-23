/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 5 27 2002	MAHESH              Created
 */
package com.gridnode.pdip.base.data.entities.model;

public interface IDataItem {

	//Entity name for DataItem
	public static final String ENTITY_NAME = "DataItem";

	//FieldIds for attributes in DataItem

	public static final Number UID = new Integer(0);  //Long

	public static final Number DATA_CONTEXT_TYPE = new Integer(1);  //String;

	public static final Number CONTEXT_UID = new Integer(2);  //Long;

	public static final Number DATA_DEF_KEY = new Integer(3);  //String;

	public static final Number IS_COPY = new Integer(4);  //Boolean;

	public static final Number DATA_DEF_TYPE = new Integer(5);  //String;

	public static final Number DATA_DEF_NAME = new Integer(6);  //String;

}