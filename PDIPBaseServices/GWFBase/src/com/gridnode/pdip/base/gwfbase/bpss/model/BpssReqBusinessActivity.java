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


 

public class BpssReqBusinessActivity extends BpssBusinessAction
    implements IBpssReqBusinessActivity
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1353195758534499138L;
		String _timeToAckAccept;

    public BpssReqBusinessActivity()
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

    public String getTimeToAckAccept()
    {
        return _timeToAckAccept;
    }

    public void setTimeToAckAccept(String timeToAckAccept)
    {
        _timeToAckAccept = timeToAckAccept;
    }

}
