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


public interface IXpdlTool
{

    //Entity name for XpdlTool
    public static final String ENTITY_NAME = "XpdlTool";

    //FieldIds for attributes in XpdlTool

    public static final Number UID = new Integer(0);  //Long

    public static final Number TOOL_ID = new Integer(1);  //String;

    public static final Number TOOL_TYPE = new Integer(2);  //String;

    public static final Number TOOL_DESCRIPTION = new Integer(3);  //String;

    public static final Number ACTUAL_PARAMETERS = new Integer(4);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(5);  //String;

    public static final Number LOOP_KIND = new Integer(6);  //String;

    public static final Number CONDITION_EXPR = new Integer(7);  //String;

    public static final Number ACTIVITY_ID = new Integer(8);  //String;

    public static final Number PROCESS_ID = new Integer(9);  //String;

    public static final Number PACKAGE_ID = new Integer(10);  //String;

    public static final Number PKG_VERSION_ID = new Integer(11);  //String;

}
