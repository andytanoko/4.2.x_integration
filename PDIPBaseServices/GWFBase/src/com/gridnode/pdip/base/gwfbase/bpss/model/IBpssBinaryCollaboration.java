/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;


public interface IBpssBinaryCollaboration
{

    public static final String ENTITY_NAME = "BpssBinaryCollaboration";

    /**
     * FieldId for UId. A Number.
     */
    public static final Number UID = new Integer(0);  //Integer

    public static final Number NAME = new Integer(1);

    public static final Number PATTERN = new Integer(2);

    public static final Number TIMETO_PERFORM = new Integer(3);

    public static final Number PRE_CONDITION = new Integer(4);

    public static final Number POST_CONDITION = new Integer(5);

    public static final Number BEGINS_WHEN = new Integer(6);

    public static final Number ENDS_WHEN = new Integer(7);

    public static final Number MAX_CONCURRENCY = new Integer(8);

}
