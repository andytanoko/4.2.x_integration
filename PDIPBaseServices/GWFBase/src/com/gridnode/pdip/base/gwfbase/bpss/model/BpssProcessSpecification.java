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


public class BpssProcessSpecification extends AbstractEntity
    implements IBpssProcessSpecification
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8317991987509915030L;
		protected String _specUUId;
    protected String _specVersion;
    protected String _specName;  //e.g. catalog request
    protected String _specTimestamp;  //e.g. BinaryCollaboration

    public BpssProcessSpecification()
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
    public String getSpecUUId()
    {
        return _specUUId;
    }

    public String getSpecName()
    {
        return _specName;
    }

    public String getspecTimestamp()
    {
        return _specTimestamp;
    }

    public String getSpecVersion()
    {
        return _specVersion;
    }

    //*********************************
    public void setSpecUUId(String specUUId)
    {
        _specUUId = specUUId;
    }

    public void setSpecName(String specName)
    {
        _specName = specName;
    }

    public void setSpecTimestamp(String specTimestamp)
    {
        _specTimestamp = specTimestamp;
    }

    public void setSpecVersion(String specVersion)
    {
        _specVersion = specVersion;
    }
}
