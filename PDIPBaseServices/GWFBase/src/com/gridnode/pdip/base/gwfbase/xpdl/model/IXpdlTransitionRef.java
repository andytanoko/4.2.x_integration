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


public interface IXpdlTransitionRef
{

    //Entity name for XpdlTransitionRef
    public static final String ENTITY_NAME = "XpdlTransitionRef";

    //FieldIds for attributes in XpdlTransitionRef

    public static final Number UID = new Integer(0);  //Long

    public static final Number TRANSITION_ID = new Integer(1);  //String;

    public static final Number LIST_UID = new Integer(2);  //Long;

}
