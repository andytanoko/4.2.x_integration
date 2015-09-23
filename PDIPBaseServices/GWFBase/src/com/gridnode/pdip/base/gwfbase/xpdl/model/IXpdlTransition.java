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


public interface IXpdlTransition
{

    //Entity name for XpdlTransition
    public static final String ENTITY_NAME = "XpdlTransition";

    //FieldIds for attributes in XpdlTransition

    public static final Number UID = new Integer(0);  //Long

    public static final Number TRANSITION_ID = new Integer(1);  //String;

    public static final Number TRANSITION_NAME = new Integer(2);  //String;

    public static final Number TRANSITION_DESCRIPTION = new Integer(3);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(4);  //Long;

    public static final Number FROM_ACTIVITY_ID = new Integer(5);  //String;

    public static final Number TO_ACTIVITY_ID = new Integer(6);  //String;

    public static final Number LOOP_TYPE = new Integer(7);  //String;

    public static final Number CONDITION_TYPE = new Integer(8);  //String;

    public static final Number CONDITION_EXPR = new Integer(9);  //String;

    public static final Number PROCESS_ID = new Integer(10);  //String;

    public static final Number PACKAGE_ID = new Integer(11);  //String;

    public static final Number PKG_VERSION_ID = new Integer(12);  //String;

}
