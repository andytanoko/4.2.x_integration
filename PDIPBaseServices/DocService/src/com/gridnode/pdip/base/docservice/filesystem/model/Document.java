/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Document.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.model;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;

import java.util.Date;

public class Document extends AbstractEntity implements IDocument
{

    protected String _documentName;
    protected String _docType;
    protected long _parentId;
    protected long _domainId;
    protected int _fileCount;
    protected String _author;
    protected long _size;
    protected Date _createdOn;
    protected Date _lastAccessed;

    public Document()
    {
    }

    // ******************* Methods from AbstractEntity ******************
    public String getEntityName()
    {
        return ENTITY_NAME;
    }

    public String getEntityDescr()
    {
        return getEntityName();
    }

    public Number getKeyId()
    {
        return UID;
    }
    //****************************************

    public String getName()
    {
        return _documentName;
    }

    public String getDocType()
    {
        return _docType;
    }

    public long getParentId()
    {
        return _parentId;
    }

    public long getDomainId()
    {
        return _domainId;
    }

    public int getFileCount()
    {
        return _fileCount;
    }

    public String getAuthor()
    {
        return _author;
    }

    public long getSize()
    {
        return _size;
    }

    public Date getCreatedOnDate()
    {
        return _createdOn;
    }

    public Date getLastAccessedDate()
    {
        return _lastAccessed;
    }

    public void setName(String name)
    {
        this._documentName = name;
    }

    public void setDocType(String docType)
    {
        this._docType = docType;
    }

    public void setParentId(long parentId)
    {
        this._parentId = parentId;
    }

    public void setDomainId(long domainId)
    {
        this._domainId = domainId;
    }

    public void setFileCount(int fileCount)
    {
        this._fileCount = fileCount;
    }

    public void setAuthor(String author)
    {
        this._author = author;
    }

    public void setSize(long size)
    {
        this._size = size;
    }

    public void setCreatedOnDate(Date createdOn)
    {
        this._createdOn = createdOn;
    }

    public void setLastAccessedDate(Date lastAccessed)
    {
        this._lastAccessed = lastAccessed;
    }

}