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


public interface IXpdlParticipantList
{

    //Entity name for XpdlParticipantList
    public static final String ENTITY_NAME = "XpdlParticipantList";

    //FieldIds for attributes in XpdlParticipantList

    public static final Number UID = new Integer(0);  //Long

    public static final Number PARTICIPANT_ID = new Integer(1);  //String;

    public static final Number PARTICIPANT_INDEX = new Integer(2);  //Long;

    public static final Number LIST_UID = new Integer(3);  //Long;

}
