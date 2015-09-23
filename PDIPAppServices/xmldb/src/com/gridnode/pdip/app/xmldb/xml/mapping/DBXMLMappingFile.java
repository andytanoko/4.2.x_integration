/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DBXMLMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.xml.mapping;

import org.jdom.Document;

public class DBXMLMappingFile implements IDBXMLMappingFile
{

    private DocumentElement root;

    public DBXMLMappingFile(Document document)
    {
        this.root = getRootElement(document);
    }

    private DocumentElement getRootElement(Document document)
    {
        return new DocumentElement(document.getRootElement(), null);
    }

    public DocumentElement getRoot()
    {
        return this.root;
    }
}
