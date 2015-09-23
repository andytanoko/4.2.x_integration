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

public interface IBpssBusinessDocument {

	//Entity name for BpssBusinessDocument
	public static final String ENTITY_NAME = "BpssBusinessDocument";

	//FieldIds for attributes in BusinessDocument

        public static final Number UID = new Integer(0);  //Long

        public static final Number NAME = new Integer(1);  //String;

	public static final Number CONDITION_EXPR = new Integer(2);  //String;

        public static final Number EXPRESSION_LANGUAGE = new Integer(3);  //String;

	public static final Number SPEC_ELEMENT = new Integer(4);  //String;

	public static final Number SPEC_LOCATION = new Integer(5);  //String;


}