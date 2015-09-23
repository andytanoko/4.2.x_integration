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

public interface IBpssDocumentEnvelope {

	//Entity name for BpssBusinessDocument
	public static final String ENTITY_NAME = "BpssDocumentEnvelope";

	//FieldIds for attributes in BusinessDocument

        public static final Number UID = new Integer(0);  //Long

	public static final Number BUSINESS_DOCUMENT_NAME = new Integer(1);  //String

	public static final Number BUSINESS_DOCUMENTID_REF = new Integer(2);  //String

	public static final Number ISPOSITIVE_RESPONSE = new Integer(3);  //Boolean

	public static final Number ISAUTHENTICATED = new Integer(4);  //Boolean

	public static final Number ISCONFIDENTIAL = new Integer(5);  //Boolean

	public static final Number ISTAMPERPROOF = new Integer(6);  //Boolean
}