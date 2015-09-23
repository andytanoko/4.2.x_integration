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


public class XpdlTool extends AbstractEntity
    implements IXpdlTool
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -617342087003351877L;
		//Attributes 
    protected String _toolId;
    protected String _toolType;
    protected String _toolDescription;
    protected String _actualParameters;
    protected String _extendedAttributes;
    protected String _loopKind;
    protected String _conditionExpr;
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

    public String getToolId()
    {
        return _toolId;
    }

    public String getToolType()
    {
        return _toolType;
    }

    public String getToolDescription()
    {
        return _toolDescription;
    }

    public String getActualParameters()
    {
        return _actualParameters;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getLoopKind()
    {
        return _loopKind;
    }

    public String getConditionExpr()
    {
        return _conditionExpr;
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

    public void setToolId(String toolId)
    {
        _toolId = toolId;
    }

    public void setToolType(String toolType)
    {
        _toolType = toolType;
    }

    public void setToolDescription(String toolDescription)
    {
        _toolDescription = toolDescription;
    }

    public void setActualParameters(String actualParameters)
    {
        _actualParameters = actualParameters;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setLoopKind(String loopKind)
    {
        _loopKind = loopKind;
    }

    public void setConditionExpr(String conditionExpr)
    {
        _conditionExpr = conditionExpr;
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
