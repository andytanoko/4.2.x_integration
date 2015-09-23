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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFProcess;
 

public class BpssMultiPartyCollaboration extends GWFProcess
    implements IBpssMultiPartyCollaboration
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2514021483456872426L;
		protected String _multiPartyCollaborationName;

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


    public String getMultiPartyCollaborationName()
    {
        return _multiPartyCollaborationName;
    }

    // *********************** Setters for attributes **********************
    public void setMultiPartyCollaborationName(String multiPartyCollaborationName)
    {
        _multiPartyCollaborationName = multiPartyCollaborationName;
    }

}
