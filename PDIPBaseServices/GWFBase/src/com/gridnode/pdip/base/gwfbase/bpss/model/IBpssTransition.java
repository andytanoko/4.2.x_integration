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


public interface IBpssTransition
{

    /**
     * Name for BpssTransition entity.
     */
    public static final String ENTITY_NAME = "BpssTransition";

    public static final Number ON_INITIATION = new Integer(3);

    public static final Number CONDITION_GUARD = new Integer(4);

    public static final Number FROM_BUSINESS_STATE_KEY = new Integer(5);

    public static final Number TO_BUSINESS_STATE_KEY = new Integer(6);

    public static final Number EXPRESSION_LANGUAGE = new Integer(7);

    public static final Number CONDITION_EXPRESSION = new Integer(8);


}
