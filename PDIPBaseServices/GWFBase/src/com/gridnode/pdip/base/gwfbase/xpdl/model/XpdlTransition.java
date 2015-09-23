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


public class XpdlTransition extends AbstractEntity
    implements IXpdlTransition
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2466378903471159598L;
		//Attributes
    protected String _transitionId;
    protected String _transitionName;
    protected String _transitionDescription;
    protected String _extendedAttributes;
    protected String _fromActivityId;
    protected String _toActivityId;
    protected String _loopType;
    protected String _conditionType;
    protected String _conditionExpr;
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

    public String getTransitionId()
    {
        return _transitionId;
    }

    public String getTransitionName()
    {
        return _transitionName;
    }

    public String getTransitionDescription()
    {
        return _transitionDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getFromActivityId()
    {
        return _fromActivityId;
    }

    public String getToActivityId()
    {
        return _toActivityId;
    }

    public String getLoopType()
    {
        return _loopType;
    }

    public String getConditionType()
    {
        return _conditionType;
    }

    public String getConditionExpr()
    {
        return _conditionExpr;
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

    public void setTransitionId(String transitionId)
    {
        _transitionId = transitionId;
    }

    public void setTransitionName(String transitionName)
    {
        _transitionName = transitionName;
    }

    public void setTransitionDescription(String transitionDescription)
    {
        _transitionDescription = transitionDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setFromActivityId(String fromActivityId)
    {
        _fromActivityId = fromActivityId;
    }

    public void setToActivityId(String toActivityId)
    {
        _toActivityId = toActivityId;
    }

    public void setLoopType(String loopType)
    {
        _loopType = loopType;
    }

    public void setConditionType(String conditionType)
    {
        _conditionType = conditionType;
    }

    public void setConditionExpr(String conditionExpr)
    {
        _conditionExpr = conditionExpr;
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
