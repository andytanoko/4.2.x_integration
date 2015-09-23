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

public interface IDataItemLocationMap {

	//Entity name for DataItemLocationMap
	public static final String ENTITY_NAME = "DataItemLocationMap";

	//FieldIds for attributes in DataItemLocationMap

	public static final Number UID = new Integer(0);  //Long

	public static final Number DATA_ITEM_UID = new Integer(1);  //Long;

        public static final Number DATA_FIELD_NAME = new Integer(2);  //String;

	public static final Number DATA_LOCATION = new Integer(3);  //String;


}