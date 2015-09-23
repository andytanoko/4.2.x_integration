/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertExpiryData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 18, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.certificate.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gridnode.pdip.app.alert.providers.AbstractDetails;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class CertExpiryData extends AbstractDetails
{
	public static final String NAME = "CertExpiry";
	public static final String FIELD_CATEGORY = "CATEGORY";
	public static final String FIELD_CERT_NAME = "CERT_NAME";
	public static final String FIELD_ISSUER_NAME = "ISSUER_NAME";
	public static final String FIELD_SERIAL_NUM = "SERIAL_NUM";
	public static final String FIELD_SUBJECT_COMMON_NAME = "SUBJECT_CN";
	public static final String FIELD_ORG_UNIT = "SUBJECT_OU";
	public static final String FIELD_ORGANISATION = "SUBJECT_O";
	public static final String FIELD_COUNTRY = "SUBJECT_C";
	public static final String FIELD_VALID_FROM = "START_DT";
	public static final String FIELD_VALID_TO = "END_DT";
	public static final String FIELD_EXPIRING_DAYS = "CHECK_DAYS";
	
	public CertExpiryData(String category, String certName, String issuerName,
	                      String serialNum, String subjectCommonName, String orgUnit, String organisation,
	                      String country, Date validFrom, Date validTo, Integer expiringDays)
	{
		set(FIELD_CATEGORY, category);
		set(FIELD_CERT_NAME, certName);
		set(FIELD_ISSUER_NAME, issuerName);
		set(FIELD_SERIAL_NUM, serialNum);
		set(FIELD_SUBJECT_COMMON_NAME, subjectCommonName);
		set(FIELD_ORG_UNIT, orgUnit);
		set(FIELD_ORGANISATION, organisation);
		set(FIELD_COUNTRY, country);
		set(FIELD_VALID_FROM, validFrom);
		set(FIELD_VALID_TO, validTo);
		set(FIELD_EXPIRING_DAYS, expiringDays);
	}
	
	public final static List<String> getFieldNameList()
	{
		List<String> list = new ArrayList<String>();
		list.add(FIELD_CATEGORY);
		list.add(FIELD_CERT_NAME);
		list.add(FIELD_ISSUER_NAME);
		list.add(FIELD_SERIAL_NUM);
		list.add(FIELD_SUBJECT_COMMON_NAME);
		list.add(FIELD_ORG_UNIT);
		list.add(FIELD_ORGANISATION);
		list.add(FIELD_COUNTRY);
		list.add(FIELD_VALID_FROM);
		list.add(FIELD_VALID_TO);
		list.add(FIELD_EXPIRING_DAYS);
		return list;
	}
	
	public String getName()
	{
		return NAME;
	}
	
}
