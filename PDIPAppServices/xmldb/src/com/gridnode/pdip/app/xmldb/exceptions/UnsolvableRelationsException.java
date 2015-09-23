/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UnsolvableRelationsException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.exceptions;

public class UnsolvableRelationsException extends XMLDBException
{
    public UnsolvableRelationsException(String message)
    {
        super(message);
    }
}
