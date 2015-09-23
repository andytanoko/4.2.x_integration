/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchESPIDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 21, 2006    Tam Wei Xiang       Created
 * Oct 12, 2006    Regina Zeng         Add user tracking ID, remark
 */
package com.gridnode.gtas.client.ctrl;

/**
 * @author Tam Wei Xiang
 * @author Regina Zeng
 * 
 * @since GT 4.0
 */
public interface IGTSearchEsPiDocumentEntity extends IGTEntity
{
	//vField
  public static final Number PROCESS_DEF = new Integer(-10); 
  public static final Number PROCESS_FROM_START_TIME = new Integer(-20);
  public static final Number PROCESS_TO_START_TIME = new Integer(-30);
  public static final Number FROM_ST_HOUR = new Integer(-40);
  public static final Number TO_ST_HOUR = new Integer(-50);
  public static final Number PROCESS_STATE = new Integer(-60);
  public static final Number PARTNER_ID = new Integer(-70);
  public static final Number PARTNER_NAME = new Integer(-80);
  public static final Number DOC_NO = new Integer(-90);
  public static final Number FROM_DOC_DATE = new Integer(-100);
  public static final Number TO_DOC_DATE = new Integer(-110);
  
  public static final Number FORM_MSG = new Integer(-200);
  public static final Number USER_TRACKING_ID = new Integer(-120);
 
  public static final Number REMARK = new Integer(-130); //04122006 RZ:Added
  
  //use as a key for passing the entity from entityDispatchAction
  //to EntitylistAction
  public static final String SEARCH_PI_ENTITY = "searchPiEntity";  
}
