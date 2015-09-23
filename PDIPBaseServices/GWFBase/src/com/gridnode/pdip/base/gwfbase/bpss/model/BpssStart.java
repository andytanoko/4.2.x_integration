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


public class BpssStart extends GWFRestriction
    implements IBpssStart
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5708455951641943153L;
		protected Long _processUId;
    protected String _toBusinessStateKey;
    protected Boolean _isDownLink = Boolean.FALSE;

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

    //********************************

    public Long getProcessUID()
    {
        return _processUId;
    }

    public String getToBusinessStateKey()
    {
        return _toBusinessStateKey;
    }

    public Boolean getIsDownLink()
    {
        return _isDownLink;
    }

    public void setProcessUID(Long processUId)
    {
        _processUId = processUId;
    }

    public void setToBusinessStateKey(String toBusinessStateKey)
    {
        _toBusinessStateKey = toBusinessStateKey;
    }

    public void setIsDownLink(Boolean isDownLink)
    {
        _isDownLink = isDownLink;
    }

}
