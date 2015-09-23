/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractActionTag.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 18 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.taglib.action;

import javax.servlet.jsp.tagext.TagSupport;

public abstract class AbstractActionTag extends TagSupport
{

    public static int SUCCESS = 9;
    public static int FAILURE = 10;
    public static int NO_DATA_FOUND = 11;
    public static int INVALID_FILE_FORMAT = 12;

    protected int result = 9;
    protected String message = "";

    public int getResult()
    {
        return result;
    }

    public String getMessage()
    {
        return this.message;
    }
}