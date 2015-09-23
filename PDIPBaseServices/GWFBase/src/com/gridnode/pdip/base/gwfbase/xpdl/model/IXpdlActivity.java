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


public interface IXpdlActivity
{

    //Entity name for XpdlActivity
    public static final String ENTITY_NAME = "XpdlActivity";

    //FieldIds for attributes in XpdlActivity

    public static final Number UID = new Integer(0);  //Long

    public static final Number ACTIVITY_ID = new Integer(1);  //String;

    public static final Number ACTIVITY_NAME = new Integer(2);  //String;

    public static final Number ACTIVITY_DESCRIPTION = new Integer(3);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(4);  //Long;

    public static final Number ACTIVITY_LIMIT = new Integer(5);  //Double;

    public static final Number IS_ROUTE = new Integer(6);  //Boolean;

    public static final Number IMPLEMENTATION_TYPE = new Integer(7);  //String;

    public static final Number PERFORMER_ID = new Integer(8);  //String;

    public static final Number START_MODE = new Integer(9);  //String;

    public static final Number FINISH_MODE = new Integer(10);  //String;

    public static final Number PRIORITY = new Integer(11);  //Long;

    public static final Number INSTANTIATION = new Integer(12);  //String;

    public static final Number COST = new Integer(13);  //Double;

    public static final Number WAITING_TIME = new Integer(14);  //Double;

    public static final Number DURATION = new Integer(15);  //Double;

    public static final Number ICON_URL = new Integer(16);  //String;

    public static final Number DOCUMENTATION_URL = new Integer(17);  //String;

    public static final Number TRANSITION_RESTRICTION_LIST_UID = new Integer(18);  //Long;

    public static final Number WORKING_TIME = new Integer(19);  //Double;

    public static final Number PROCESS_ID = new Integer(20);  //String;

    public static final Number PACKAGE_ID = new Integer(21);  //String;

    public static final Number PKG_VERSION_ID = new Integer(22);  //String;

}
