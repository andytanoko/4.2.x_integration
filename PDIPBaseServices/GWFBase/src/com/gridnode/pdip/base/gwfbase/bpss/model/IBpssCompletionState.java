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


public interface IBpssCompletionState
{

    /**
     * Name for BpssCompletionState entity.
     */
    public static final String ENTITY_NAME = "BpssCompletionState";

    public static final Number UID = new Integer(0);

    public static final Number PROCESS_UID = new Integer(1);

    public static final Number PROCESS_TYPE = new Integer(2);

    public static final Number CONDITION_GUARD = new Integer(3);

    public static final Number FROM_BUSINESS_STATE_KEY = new Integer(4);

    public static final Number MPC_UID = new Integer(5);

    public static final Number EXPRESSION_LANGUAGE = new Integer(6);

    public static final Number CONDITION_EXPRESSION = new Integer(7);

    public static final Number COMPLETION_TYPE = new Integer(8);

    //constants
    public static final String SUCCESS_TYPE="Success";

    public static final String FAILURE_TYPE="Failure";
}
