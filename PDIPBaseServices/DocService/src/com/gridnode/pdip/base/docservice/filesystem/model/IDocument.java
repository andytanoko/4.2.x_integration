/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.model;


public interface IDocument
{

    public static final String ENTITY_NAME = "Document";

    public static final Number UID = new Integer(0);  //Integer

    public static final Number DOCUMENTNAME = new Integer(1);

    public static final Number DOCTYPE = new Integer(2);

    public static final Number PARENTID = new Integer(3);

    public static final Number DOMAINID = new Integer(4);

    public static final Number FILECOUNT = new Integer(5);

    public static final Number AUTHOR = new Integer(6);

    public static final Number SIZE = new Integer(7);

    public static final Number CREATEDON = new Integer(8);

    public static final Number LASTACCESSED = new Integer(9);
}
