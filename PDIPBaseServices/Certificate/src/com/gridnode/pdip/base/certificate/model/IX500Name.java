/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IX500Names
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 06-Jan-2003    Jagadeesh          Created.
 *
 */

package com.gridnode.pdip.base.certificate.model;


public interface IX500Name
{

   public static final String ISSUERNAMES_ENTITY_NAME = "IssuerNames";

   public static final String SUBJECT_ENTITY_NAME = "SubjectName";

   public static final Number COUNTRY = new Integer(0);

   public static final Number STATE  = new Integer(1);

   public static final Number ORGANIZATION = new Integer(2);

   public static final Number LOCALITY  = new Integer(3);

   public static final Number ORGANIZATIONAL_UNIT = new Integer(4);

   public static final Number STREET_ADDRESS = new Integer(5);

   public static final Number COMMAN_NAME = new Integer(6);

   public static final Number TITLE = new Integer(7);

   public static final Number EMAIL_ADDRESS = new Integer(8);

   public static final Number BUSINESS_CATEORY = new Integer(9);

   public static final Number TELEPHONE_NUMBER = new Integer(10);

   public static final Number POSTAL_CODE = new Integer(11);

   public static final Number UNKOWN_ATTRIBUTE_TYPE = new Integer(12);

}