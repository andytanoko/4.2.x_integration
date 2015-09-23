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


public interface IXpdlSubFlow
{

    //Entity name for XpdlSubFlow
    public static final String ENTITY_NAME = "XpdlSubFlow";

    //FieldIds for attributes in XpdlSubFlow

    public static final Number UID = new Integer(0);  //Long

    public static final Number SUB_FLOW_ID = new Integer(1);  //String;

    public static final Number SUB_FLOW_TYPE = new Integer(2);  //String;

    public static final Number ACTUAL_PARAMETERS = new Integer(3);  //String;

    public static final Number ACTIVITY_ID = new Integer(4);  //String;

    public static final Number PROCESS_ID = new Integer(5);  //String;

    public static final Number PACKAGE_ID = new Integer(6);  //String;

    public static final Number PKG_VERSION_ID = new Integer(7);  //String;

}
