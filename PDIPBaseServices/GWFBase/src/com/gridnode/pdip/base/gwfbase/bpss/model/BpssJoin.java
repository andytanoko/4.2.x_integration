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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFRestriction;

 
public class BpssJoin extends GWFRestriction
    implements IBpssJoin
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3061745963675793318L;
		protected String _name;
    protected Boolean _waitForAll;

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

    //*************************************
    public String getRestrictionName()
    {
        return _name;
    }

    public Boolean getWaitForAll()
    {
        return _waitForAll;
    }

    //*************************************
    public void setRestrictionName(String name)
    {
        _name = name;
    }

    public void setWaitForAll(Boolean waitForAll)
    {
        _waitForAll = waitForAll;
    }

}
