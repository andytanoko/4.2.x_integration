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


public interface IBpssProcessSpecEntry
{

    /**
     * Name for BpssProcessSpecEntry entity.
     */
    public static final String ENTITY_NAME = "BpssProcessSpecEntry";

    public static final Number UID = new Integer(0);  //Integer

    public static final Number SPEC_UID = new Integer(1);

    public static final Number ENTRY_UID = new Integer(2);

    public static final Number ENTRY_NAME = new Integer(3);

    public static final Number ENTRY_TYPE = new Integer(4);

    public static final Number PARENT_ENTRY_UID = new Integer(5);
}
