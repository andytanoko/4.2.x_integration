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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFRole;
 

public class BpssBusinessPartnerRole extends GWFRole
    implements IBpssBusinessPartnerRole
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3299887212769773409L;
		protected String _roleName;

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

    // *********************** Getters for attributes **********************

    public String getRoleName()
    {
        return _roleName;
    }

    // *********************** Setters for attributes **********************

    public void setRoleName(String roleName)
    {
        _roleName = roleName;
    }
}
