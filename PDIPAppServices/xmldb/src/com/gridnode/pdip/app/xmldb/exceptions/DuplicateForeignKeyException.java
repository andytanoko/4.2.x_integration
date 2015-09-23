/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DuplicateForeignKeyException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.exceptions;

public class DuplicateForeignKeyException extends XMLDBException
{
    public DuplicateForeignKeyException(String message)
    {
        super(message);
    }
}
