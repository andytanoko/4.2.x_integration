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


public interface IXpdlFormalParam
{

    //Entity name for XpdlFormalParam
    public static final String ENTITY_NAME = "XpdlFormalParam";

    //FieldIds for attributes in XpdlFormalParam

    public static final Number UID = new Integer(0);  //Long

    public static final Number FORMAL_PARAM_ID = new Integer(1);  //String;

    public static final Number MODE = new Integer(2);  //String;

    public static final Number INDEX_NUMBER = new Integer(3);  //Integer;

    public static final Number FORMAL_PARAM_DESCRIPTION = new Integer(4);  //String;

    public static final Number APPLICATION_ID = new Integer(5);  //String;

    public static final Number PROCESS_ID = new Integer(6);  //String;

    public static final Number PACKAGE_ID = new Integer(7);  //String;

    public static final Number PKG_VERSION_ID = new Integer(8);  //String;

    public static final Number DATATYPE_NAME = new Integer(9);

    public static final Number COMPLEX_DATATYPE_UID = new Integer(10);


}
