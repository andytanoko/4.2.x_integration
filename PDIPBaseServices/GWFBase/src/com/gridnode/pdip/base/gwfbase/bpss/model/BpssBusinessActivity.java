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


import com.gridnode.pdip.base.gwfbase.baseentity.*;


public abstract class BpssBusinessActivity extends GWFActivity
    implements IBpssBusinessActivity, IBpssBusinessState
{

    protected String _activityName;

    // *********************** Getters for attributes **********************


    public String getActivityName()
    {
        return _activityName;
    }

    // *********************** Setters for attributes **********************
    public void setActivityName(String activityName)
    {
        _activityName = activityName;
    }

}
