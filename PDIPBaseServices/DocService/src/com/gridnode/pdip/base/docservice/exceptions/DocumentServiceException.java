/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentServiceException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.exceptions;

import com.gridnode.pdip.framework.exceptions.TypedException;

public class DocumentServiceException extends TypedException
{

    public static short ILLEGAL_ARGUMENTS = 0;
    public static short SYSTEM_ERROR = 1;
    public static short SOURCE_NOT_FOUND = 2;
    public static short DESTINATION_NOT_FOUND = 3;
    public static short NAME_EXISTS = 4;
    public static short INTER_DOMAIN_TRANSFER = 5;
    public static short SAME_SOURCE_AND_DESTINATION = 6;
    public static short FILE_MANAGER_ERROR = 7;
    public static short DOCUMENT_PARENT = 8;

    public DocumentServiceException(short type)
    {
        super(type);
    }

    public DocumentServiceException(String message, short type)
    {
        super(message, type);
    }

    public DocumentServiceException(Throwable t, short type)
    {
        super(t, type);
    }
}
