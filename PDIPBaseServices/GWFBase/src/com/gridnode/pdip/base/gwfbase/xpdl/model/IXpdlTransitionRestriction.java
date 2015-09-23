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


public interface IXpdlTransitionRestriction
{

    //Entity name for XpdlTransitionRestriction
    public static final String ENTITY_NAME = "XpdlTransitionRestriction";

    //FieldIds for attributes in XpdlTransitionRestriction

    public static final Number UID = new Integer(0);  //Long

    public static final Number IS_INLINE_BLOCK = new Integer(1);  //Boolean;

    public static final Number BLOCK_NAME = new Integer(2);  //String;

    public static final Number BLOCK_DESCRIPTION = new Integer(3);  //String;

    public static final Number BLOCK_ICON_URL = new Integer(4);  //String;

    public static final Number BLOCK_DOCUMENTATION_URL = new Integer(5);  //String;

    public static final Number BLOCK_BEGIN_ACTIVITY_ID = new Integer(6);  //String;

    public static final Number BLOCK_END_ACTIVITY_ID = new Integer(7);  //String;

    public static final Number JOIN_TYPE = new Integer(8);  //String;

    public static final Number SPLIT_TYPE = new Integer(9);  //String;

    public static final Number TRANSITION_REF_LIST_UID = new Integer(10);  //Long;

    public static final Number LIST_UID = new Integer(11);  //Long;

}
