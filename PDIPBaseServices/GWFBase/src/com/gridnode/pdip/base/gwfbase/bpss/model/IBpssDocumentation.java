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
package com.gridnode.pdip.base.gwfbase.bpss.model;

public interface IBpssDocumentation {

	//Entity name for BpssDocumentation
	public static final String ENTITY_NAME = "BpssDocumentation";

	//FieldIds for attributes in Documentation

        public static final Number UID = new Integer(0);  //Long

        public static final Number URI = new Integer(1);  //String;

	public static final Number DOCUMENTATION = new Integer(2);  //String;

}