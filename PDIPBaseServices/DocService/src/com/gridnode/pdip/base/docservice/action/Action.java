/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Action.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.action;

import java.io.Serializable;

public abstract class Action implements Serializable
{
    protected int actionType;
    protected int nodeType;
    protected long id;

    public static int CUT = 0;
    public static int COPY = 1;
    public static int PASTE = 2;
    public static int DELETE = 3;
    public static int RENAME = 4;
    public static int CREATE_FOLDER = 5;
    public static int ADD_DOCUMENT = 6;

    public Action(int actionType, int nodeType, long id)
    {
        this.actionType = actionType;
        this.nodeType = nodeType;
        this.id = id;
    }

    public abstract void doAction() throws Exception;

    public int getNodeType()
    {
        return this.nodeType;
    }

    public int getActionType()
    {
        return this.actionType;
    }

    public long getId()
    {
        return this.id;
    }
}
