/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.xpdl.model;


public interface IXpdlProcess
{

    //Entity name for XpdlProcess
    public static final String ENTITY_NAME = "XpdlProcess";

    //FieldIds for attributes in XpdlProcess

    public static final Number UID = new Integer(0);  //Long

    public static final Number PROCESS_ID = new Integer(1);  //String;

    public static final Number PROCESS_NAME = new Integer(2);  //String;

    public static final Number PROCESS_DESCRIPTION = new Integer(3);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(4);  //Long;

    public static final Number DURATION_UNIT = new Integer(5);  //String;

    public static final Number CREATION_DATE_TIME = new Integer(6);  //java.util.Date;

    public static final Number HEADER_DESCRIPTION = new Integer(7);  //String;

    public static final Number PRIORITY = new Integer(8);  //Long;

    public static final Number PROCESS_LIMIT = new Integer(9);  //Double;

    public static final Number VALID_FROM_DATE = new Integer(10);  //java.util.Date;

    public static final Number VALID_TO_DATE = new Integer(11);  //java.util.Date;

    public static final Number WAITING_TIME = new Integer(12);  //Double;

    public static final Number WORKING_TIME = new Integer(13);  //Double;

    public static final Number DURATION = new Integer(14);  //Double;

    public static final Number AUTHOR = new Integer(15);  //String;

    public static final Number VERSION_ID = new Integer(16);  //String;

    public static final Number CODEPAGE = new Integer(17);  //String;

    public static final Number COUNTRYKEY = new Integer(18);  //String;

    public static final Number PUBLICATION_STATUS = new Integer(19);  //String;

    public static final Number RESPONSIBLE_LIST_UID = new Integer(20);  //Long;

    public static final Number PACKAGE_ID = new Integer(21);  //String;

    public static final Number DEFAULT_START_ACTIVITY_ID = new Integer(22);  //String;

    public static final Number PKG_VERSION_ID = new Integer(23);  //String;

}
