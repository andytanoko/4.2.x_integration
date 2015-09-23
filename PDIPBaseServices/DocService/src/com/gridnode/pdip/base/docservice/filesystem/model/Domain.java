/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Domain.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.model;

import com.gridnode.pdip.framework.db.*;
import com.gridnode.pdip.framework.db.entity.*;

public class Domain extends AbstractEntity implements IDomain
{

    protected String _domainName;
    protected String _propertyName;
    protected long _childCount;

    public Domain()
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

    public String getDomainName()
    {
        return _domainName;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    public long getChildCount()
    {
        return _childCount;
    }

    public void setDomainName(String domainName)
    {
        this._domainName = domainName;
    }

    public void setPropertyName(String propertyName)
    {
        this._propertyName = propertyName;
    }

    public void setChildCount(long childCount)
    {
        this._childCount = childCount;
    }

}