/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 28 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.xpdl.model;


public interface IXpdlComplexDataType
{

    //Entity name for XpdlComplexDataType
    public static final String ENTITY_NAME = "XpdlComplexDataType";

    //FieldIds for attributes in XpdlTypeDeclaration

    public static final Number UID = new Integer(0);  //Long

    public static final Number DATATYPE_NAME = new Integer(1);

    public static final Number COMPLEX_DATATYPE_UID = new Integer(2);

    public static final Number SUBTYPE_UID = new Integer(3);

    public static final Number ARRAY_LOWERINDEX = new Integer(4);

    public static final Number ARRAY_UPPERINDEX = new Integer(5);

    //public static final Number MEMBERPARENTTYPEINFOUID = new Integer(4);

}
