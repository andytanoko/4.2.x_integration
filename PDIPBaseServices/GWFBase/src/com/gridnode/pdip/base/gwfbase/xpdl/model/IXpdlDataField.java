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


public interface IXpdlDataField
{

    //Entity name for XpdlDataField
    public static final String ENTITY_NAME = "XpdlDataField";

    //FieldIds for attributes in XpdlDataField

    public static final Number UID = new Integer(0);  //Long

    public static final Number DATA_FIELD_ID = new Integer(1);  //String;

    public static final Number DATA_FIELD_NAME = new Integer(2);  //String;

    public static final Number DATA_FIELD_DESCRIPTION = new Integer(3);  //String;

    public static final Number EXTENDED_ATTRIBUTES = new Integer(4);  //Long;

    public static final Number IS_ARRAY = new Integer(5);  //Boolean;

    public static final Number INITIAL_VALUE = new Integer(6);  //String;

    public static final Number LENGTH_BYTES = new Integer(7);  //Long;

    public static final Number PROCESS_ID = new Integer(8);  //String;

    public static final Number PACKAGE_ID = new Integer(9);  //String;

    public static final Number PKG_VERSION_ID = new Integer(10);  //String;

    public static final Number DATATYPE_NAME = new Integer(11);

    public static final Number COMPLEX_DATATYPE_UID = new Integer(12);

}
