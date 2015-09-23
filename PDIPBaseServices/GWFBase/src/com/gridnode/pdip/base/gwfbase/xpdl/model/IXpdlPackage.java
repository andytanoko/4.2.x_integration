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


public interface IXpdlPackage
{

    //Entity name for XpdlPackage
    public static final String ENTITY_NAME = "XpdlPackage";

    //FieldIds for attributes in XpdlPackage

    public static final Number UID = new Integer(0);  //Long

    public static final Number PACKAGE_ID = new Integer(1);  //String;

    public static final Number PACKAGE_NAME = new Integer(2);  //String;

    public static final Number PACKAGE_DESCRIPTION = new Integer(3);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(4);  //Long;

    public static final Number SPECIFICATION_ID = new Integer(5);  //String;

    public static final Number SPECIFICATION_VERSION = new Integer(6);  //String;

    public static final Number SOURCE_VENDOR_INFO = new Integer(7);  //String;

    public static final Number CREATION_DATE_TIME = new Integer(8);  //java.util.Date;

    public static final Number DOCUMENTATION_URL = new Integer(9);  //String;

    public static final Number PRIORITY_UNIT = new Integer(10);  //String;

    public static final Number COST_UNIT = new Integer(11);  //String;

    public static final Number AUTHOR = new Integer(12);  //String;

    public static final Number VERSION_ID = new Integer(13);  //String;

    public static final Number CODEPAGE = new Integer(14);  //String;

    public static final Number COUNTRYKEY = new Integer(15);  //String;

    public static final Number PUBLICATION_STATUS = new Integer(16);  //String;

    public static final Number RESPONSIBLE_LIST_UID = new Integer(17);  //Long;

    public static final Number GRAPH_CONFORMANCE = new Integer(18);  //String;
    
    public static final Number STATE = new Integer(19);  //Short

    //Possible values for STATE
    public static final short STATE_DISABLED = 0;
    public static final short STATE_ENABLED  = 1;
    public static final short STATE_DELETED  = 2;
}
