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
package com.gridnode.pdip.base.contextdata.entities.model;

public interface IByteData {

	//Entity name for StringData
	public static final String ENTITY_NAME = "ByteData";

	//FieldIds for attributes in StringData

	public static final Number UID = new Integer(0);  //Long

	public static final Number DATA = new Integer(1);  //byte[];

	public static final Number DATA_TYPE = new Integer(2);  //String;


}