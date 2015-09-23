/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchEsDocDocumentEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 27, 2006    Tam Wei Xiang       Created
 * Dec 14  2006    Tam Wei Xiang       Added remark (indicate valid or invalid)
 */
package com.gridnode.gtas.client.ctrl;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTSearchEsDocDocumentEntity extends IGTEntity
{
	//vField
	public static final Number PARTNER_ID = new Integer(-10);
	public static final Number PARTNER_NAME = new Integer(-20);
	public static final Number FOLDER = new Integer(-30);
	public static final Number DOC_TYPE = new Integer(-40);
	
	public static final Number FROM_CREATE_DATE = new Integer(-50);
	public static final Number FROM_CREATE_DATE_HOUR = new Integer(-51);
	public static final Number TO_CREATE_DATE = new Integer(-52);
	public static final Number TO_CREATE_DATE_HOUR = new Integer(-53);
	
	public static final Number FROM_SENT_DATE = new Integer(-60);
	public static final Number FROM_SENT_DATE_HOUR = new Integer(-61);
	public static final Number TO_SENT_DATE = new Integer(-62);
	public static final Number TO_SENT_DATE_HOUR = new Integer(-63);
	
	public static final Number FROM_RECEIVED_DATE = new Integer(-70);
	public static final Number FROM_RECEIVED_DATE_HOUR = new Integer(-71);
	public static final Number TO_RECEIVED_DATE = new Integer(-72);
	public static final Number TO_RECEIVED_DATE_HOUR = new Integer(-73);
	
	public static final Number DOC_NO = new Integer(-80);
	
	public static final Number FROM_DOC_DATE = new Integer(-90);
	public static final Number FROM_DOC_DATE_HOUR = new Integer(-91);
	public static final Number TO_DOC_DATE = new Integer(-92);
	public static final Number TO_DOC_DATE_HOUR = new Integer(-93);
	
	public static final Number USER_TRACKING_ID = new Integer(-100);
	
  public static final Number REMARK = new Integer(-110);
  
	public static final Number FORM_MSG = new Integer(-200);
	
	//use as a key for passing the entity from entityDispatchAction
  //to EntitylistAction
	public static final String SEARCH_DOC_ENTITY = "searchDocEntity";
  
  //constant for Remark field (for render drop down value)
	public static final String REMARK_VALID = "Valid";
  public static final String REMARK_INVALID = "Invalid";
}
