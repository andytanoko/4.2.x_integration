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


public interface IBpssBusinessTransActivity extends IBpssBusinessActivity
{

    /**
     * Name for BpssBusinessTransActivity entity.
     */
    public static final String ENTITY_NAME = "BpssBusinessTransActivity";

    public static final Number TIMETO_PERFORM = new Integer(2);  //DateTime

    public static final Number ISCONCURRENT = new Integer(3);  //Boolean

    public static final Number ISLEGALLY_BINDING = new Integer(4);  //Boolean

    public static final Number BUSINESS_TRANS_UID = new Integer(5);  //Long

    public static final Number FROM_AUTHORIZED_ROLE = new Integer(6);  //Long

    public static final Number TO_AUTHORIZED_ROLE = new Integer(7);  //Long

}
