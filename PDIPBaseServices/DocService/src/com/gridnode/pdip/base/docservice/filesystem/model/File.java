/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: File.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.model;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;

public class File extends AbstractEntity implements IFile
{

    protected String _fileName;
    protected Boolean _isMainFile;
    protected long _documentId;
    protected long _parentId;
    protected long _domainId;

    public File()
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
        return _fileName;
    }

    public Boolean getIsMainFile()
    {
        return _isMainFile;
    }

    public long getDocumentId()
    {
        return _documentId;
    }

    public long getParentId()
    {
        return _parentId;
    }

    public long getDomainId()
    {
        return _domainId;
    }

    public void setName(String name)
    {
        this._fileName = name;
    }

    public void setIsMainFile(Boolean isMainFile)
    {
        this._isMainFile = isMainFile;
    }

    public void setDocumentId(long documentId)
    {
        this._documentId = documentId;
    }

    public void setParentId(long parentId)
    {
        this._parentId = parentId;
    }

    public void setDomainId(long domainId)
    {
        this._domainId = domainId;
    }

}