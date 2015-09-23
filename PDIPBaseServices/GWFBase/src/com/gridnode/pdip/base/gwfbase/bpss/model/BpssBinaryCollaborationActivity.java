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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
 

public class BpssBinaryCollaborationActivity extends GWFActivity
    implements IBpssBinaryCollaborationActivity
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8270537978826634610L;
		protected String _activityName;
    protected Long _binaryCollaborationUId;
    protected Long _downLinkUId;

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

    // *** IGWFActivity interface methods

    public int getActivityType()
    {
        return SUB_PROCESS_TYPE;
    }

    // *********************** Getters for attributes **********************

    public String getActivityName()
    {
        return _activityName;
    }

    public Long getBinaryCollaborationUId()
    {
        return _binaryCollaborationUId;
    }

    public Long getDownLinkUId()
    {
        return _downLinkUId;
    }

    // *********************** Setters for attributes **********************

    public void setActivityName(String activityName)
    {
        _activityName = activityName;
    }

    public void setBinaryCollaborationUId(Long binaryCollaborationUId)
    {
        _binaryCollaborationUId = binaryCollaborationUId;
    }

    public void setDownLinkUId(Long downLinkUId)
    {
        _downLinkUId = downLinkUId;
    }

}
