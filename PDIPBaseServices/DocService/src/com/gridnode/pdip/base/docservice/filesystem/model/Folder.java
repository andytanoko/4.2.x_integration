/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Folder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.model;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;

public class Folder extends AbstractEntity implements IFolder
{

    protected String _folderName;
    protected long _domainId;
    protected long _parentId;
    protected long _childCount;

    public Folder()
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
        return _folderName;
    }

    public long getDomainId()
    {
        return _domainId;
    }

    public long getParentId()
    {
        return _parentId;
    }

    public long getChildCount()
    {
        return _childCount;
    }

    public void setName(String name)
    {
        this._folderName = name;
    }

    public void setDomainId(long domainId)
    {
        this._domainId = domainId;
    }

    public void setParentId(long parentId)
    {
        this._parentId = parentId;
    }

    public void setChildCount(long childCount)
    {
        this._childCount = childCount;
    }

}