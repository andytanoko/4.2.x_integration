/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CopyAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.action;

public class CopyAction extends Action
{

    public CopyAction(int nodeType, long id)
    {
        super(Action.COPY, nodeType, id);
    }

    public void doAction() throws Exception
    {
    }

}
