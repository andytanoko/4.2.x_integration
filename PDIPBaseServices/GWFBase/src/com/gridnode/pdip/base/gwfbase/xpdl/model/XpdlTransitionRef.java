/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.xpdl.model;


import com.gridnode.pdip.framework.db.entity.*;


public class XpdlTransitionRef extends AbstractEntity
    implements IXpdlTransitionRef
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4521554813828262312L;
		//Attributes 
    protected String _transitionId;
    protected Long _listUId;

    //Abstract methods of AbstractEntity
    public String getEntityName()
    {
        return ENTITY_NAME;
    }

    public Number getKeyId()
    {
        return UID;
    }

    public String getEntityDescr()
    {
        return ENTITY_NAME + ":" + _uId;
    }

    // ******************** Getters for attributes ***************************

    public String getTransitionId()
    {
        return _transitionId;
    }

    public Long getListUId()
    {
        return _listUId;
    }

    // ******************** Setters for attributes ***************************

    public void setTransitionId(String transitionId)
    {
        _transitionId = transitionId;
    }

    public void setListUId(Long listUId)
    {
        _listUId = listUId;
    }

}
