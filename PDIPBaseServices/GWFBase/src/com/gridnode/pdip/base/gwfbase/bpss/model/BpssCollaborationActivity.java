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



 
public class BpssCollaborationActivity extends BpssBusinessActivity
    implements IBpssCollaborationActivity
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2881491605356244093L;
		protected Long _binaryCollaborationUId;

    public BpssCollaborationActivity()
    {
    }

    public int getActivityType()
    {
        return SUB_PROCESS_TYPE;
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

    public void setBinaryCollaborationUId(Long binaryCollaborationUId)
    {
        _binaryCollaborationUId = binaryCollaborationUId;
    }

    public Long getBinaryCollaborationUId()
    {
        return _binaryCollaborationUId;
    }

}
