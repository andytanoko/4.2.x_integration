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


public class XpdlSubFlow extends AbstractEntity
    implements IXpdlSubFlow
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4463534635048239067L;
		//Attributes 
    protected String _subFlowId;
    protected String _subFlowType;
    protected String _actualParameters;
    protected String _activityId;
    protected String _processId;
    protected String _packageId;
    protected String _pkgVersionId;

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

    public String getSubFlowId()
    {
        return _subFlowId;
    }

    public String getSubFlowType()
    {
        return _subFlowType;
    }

    public String getActualParameters()
    {
        return _actualParameters;
    }

    public String getActivityId()
    {
        return _activityId;
    }

    public String getProcessId()
    {
        return _processId;
    }

    public String getPackageId()
    {
        return _packageId;
    }

    public String getPkgVersionId()
    {
        return _pkgVersionId;
    }

    // ******************** Setters for attributes ***************************

    public void setSubFlowId(String subFlowId)
    {
        _subFlowId = subFlowId;
    }

    public void setSubFlowType(String subFlowType)
    {
        _subFlowType = subFlowType;
    }

    public void setActualParameters(String actualParameters)
    {
        _actualParameters = actualParameters;
    }

    public void setActivityId(String activityId)
    {
        _activityId = activityId;
    }

    public void setProcessId(String processId)
    {
        _processId = processId;
    }

    public void setPackageId(String packageId)
    {
        _packageId = packageId;
    }

    public void setPkgVersionId(String pkgVersionId)
    {
        _pkgVersionId = pkgVersionId;
    }

}
