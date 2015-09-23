package com.gridnode.pdip.framework.jms;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 19 2002   Mathew         Created
 */


public interface IJMSEvent {
    public static final int PROCESS_ACTIVITY_EVENT = 1;

    public static final int CREATE_PROCESS_EVENT = 1;
    public static final int BEGIN_PROCESS_EVENT = 2;
    public static final int COMPLETE_PROCESS_EVENT = 3;
    public static final int PAUSE_PROCESS_EVENT = 4;
    public static final int RESUME_PROCESS_EVENT = 5;
    public static final int ABORT_PROCESS_EVENT = 6;
    public static final int REMOVE_PROCESS_EVENT = 7;

    public static final int CREATE_ACTIVITY_EVENT = 1;
    public static final int BEGIN_ACTIVITY_EVENT = 2;
    public static final int COMPLETE_ACTIVITY_EVENT = 3;
    public static final int PAUSE_ACTIVITY_EVENT = 4;
    public static final int RESUME_ACTIVITY_EVENT = 5;
    public static final int ABORT_ACTIVITY_EVENT = 6;
    public static final int REMOVE_ACTIVITY_EVENT = 7;

    public static final int SELECT_PROCESS_EVENT = 1;
    public static final int SELECT_ACTIVITY_EVENT = 2;
    public static final int SELECT_RESTRICTION_EVENT = 3;

    public static final int ADD_WORKLIST_EVENT = 1;
}