/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: Util.java
 * 
 * ***************************************************************************
 * Date            Author                 Changes
 * ***************************************************************************
 * 21 Oct 2005     Sumedh Chalermkanjana  Created
 * 24 Mar 2006     Tam Wei Xiang          New search criterias are added to 
 *                                        EsPISearchQuery.
 * 27 Mar 2006     Tam Wei Xiang          New search criteria are added to 
 *                                        EsDocSearchQuery.
 * 17 Oct 2006     Regina Zeng            Populate user tracking ID from the entity, and remark field  
 * 14 Dec 2006     Tam Wei Xiang          Added remark field in EsDocSearchQuery obj                              
 */
package com.gridnode.gtas.client.web.archive.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSearchEsDocDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchEsPiDocumentEntity;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.events.dbarchive.EsDocSearchQuery;
import com.gridnode.gtas.events.dbarchive.EsPiSearchQuery;

/**
 * Contains utitlity method.
 */
public class Util
{
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm";

	public static EsPiSearchQuery getEsPiSearchQuery(IGTSearchEsPiDocumentEntity searchDocEntity)
		throws GTClientException
	{
		String docNo = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.DOC_NO);
		String processDef = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_DEF);
		String processState = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_STATE);
		String partnerID = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PARTNER_ID);
		String partnerName = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PARTNER_NAME);
		String docDateFrom = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.FROM_DOC_DATE);
		String docDateTo = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.TO_DOC_DATE);
		
		String processStartFromDate = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME);
		String processStartToDate = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME);
		
		String processStartFromTime = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.FROM_ST_HOUR);
		String processStartToTime = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.TO_ST_HOUR);
		
		//TODO populate the user tracking ID from the entity
		String userTrackingID = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.USER_TRACKING_ID);
    String remark = searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.REMARK); //04122006 RZ: Added search criteria
		
		return new EsPiSearchQuery(docNo, processDef, processState, partnerID,
																partnerName, docDateFrom, docDateTo,
																processStartFromDate, processStartFromTime,
																processStartToDate, processStartToTime, userTrackingID, remark);
	}
	
	public static EsDocSearchQuery getEsDocSearchQuery(IGTSearchEsDocDocumentEntity searchDocEntity)
		throws GTClientException
	{
		String partnerID = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.PARTNER_ID);
		String partnerName = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.PARTNER_NAME);
		String folder = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FOLDER);
		String docType = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.DOC_TYPE);
		String userTrackingID = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.USER_TRACKING_ID);
		String fromCreateDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE);
		String fromCreateDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR);
		String toCreateDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE);
		String toCreateDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR);
		String fromSentDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE);
		String fromSentDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR);
		String toSentDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_SENT_DATE);
		String toSentDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR);
		String fromReceivedDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE);
		String fromReceivedDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR);
		String toReceivedDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE);
		String toReceivedDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR);
		String docNo = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.DOC_NO);
		String fromDocDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE);
		String fromDocDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR);
		String toDocDate = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_DOC_DATE);
		String toDocDateHour = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR);
		String remark = searchDocEntity.getFieldString(IGTSearchEsDocDocumentEntity.REMARK);
    
		return new EsDocSearchQuery(docNo, docType, partnerID, partnerName,
																fromDocDate, toDocDate, fromSentDate,
																toSentDate, fromReceivedDate,
																toReceivedDate,
																fromCreateDate, fromCreateDateHour,
																toCreateDate, toCreateDateHour,
																fromSentDateHour, toSentDateHour,
																fromReceivedDateHour, toReceivedDateHour,
																fromDocDateHour, toDocDateHour,
																folder, userTrackingID, remark);
	}
	
	public static boolean isNotValidDate(String date)
	{
		return isValidDate(date) == false;
	}

	public static boolean isValidDate(String date)
	{
		if (isEmpty(date))
		{
			return true;
		}
		else
		{
			Date parsedDate = DateUtils.parseDate(date, null, null,
																						new SimpleDateFormat(DATE_FORMAT));
			return parsedDate != null;
		}
	}
	
	public static boolean isNotValidTime(String time)
	{
		return ! isValidTime(time);
	}
	
	public static boolean isValidTime(String time)
	{
		if (isEmpty(time))
		{
			return true;
		}
		else
		{
			Date parsedDate = DateUtils.parseDate(time, null, null,
																						new SimpleDateFormat(TIME_FORMAT));
			return parsedDate != null;
		}
	}
	
	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().equals("");
	}
	
	public static boolean isNotEmpty(String str)
	{
		return isEmpty(str) == false;
	}
	
	/**
	 * Return true if s1 is empty and s2 is not empty
	 * OR
	 * if s1 is not empty and s2 is empty.
	 */
	public static boolean isOneEmptyAndOneNotEmpty(String s1, String s2)
	{
		return (isEmpty(s1) && isNotEmpty(s2))
			|| (isNotEmpty(s1) && isEmpty(s2));
	}
}
