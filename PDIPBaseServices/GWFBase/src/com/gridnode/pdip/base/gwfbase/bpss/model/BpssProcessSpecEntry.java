/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;


import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class BpssProcessSpecEntry extends AbstractEntity
    implements IBpssProcessSpecEntry
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7090920491454578620L;
		protected long _specUId;
    protected String _entryName;  //e.g. catalog request
    protected String _entryType;  //e.g. BinaryCollaboration
    protected long _entryUId;
    protected long _parentEntryUId;

    public BpssProcessSpecEntry()
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

    //*********************************
    public long getSpecUId()
    {
        return _specUId;
    }

    public String getEntryName()
    {
        return _entryName;
    }

    public String getEntryType()
    {
        return _entryType;
    }

    public long getEntryUId()
    {
        return _entryUId;
    }

    public long getParentEntryUId()
    {
        return _parentEntryUId;
    }

    //*********************************
    public void setSpecUId(long specUId)
    {
        _specUId = specUId;
    }

    public void setEntryName(String entryName)
    {
        _entryName = entryName;
    }

    public void setEntryType(String entryType)
    {
        _entryType = entryType;
    }

    public void setEntryUId(long entryUId)
    {
        _entryUId = entryUId;
    }

    public void setParentEntryUId(long parentEntryUId)
    {
        _parentEntryUId = parentEntryUId;
    }
}
