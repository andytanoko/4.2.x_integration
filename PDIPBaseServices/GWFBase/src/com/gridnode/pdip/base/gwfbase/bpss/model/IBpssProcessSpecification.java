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


public interface IBpssProcessSpecification
{

    /**
     * Name for BpssProcessSpecEntry entity.
     */
    public static final String ENTITY_NAME = "BpssProcessSpecification";

    public static final Number UID = new Integer(0);  //Integer

    public static final Number UUID = new Integer(1);

    public static final Number VERSION = new Integer(2);

    public static final Number NAME = new Integer(3);

    public static final Number TIMESTAMP = new Integer(4);
}
