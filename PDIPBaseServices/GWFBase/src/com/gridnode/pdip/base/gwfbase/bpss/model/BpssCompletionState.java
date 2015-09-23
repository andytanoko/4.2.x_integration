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


public class BpssCompletionState extends GWFRestriction
    implements IBpssCompletionState
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3418736697992379061L;
		protected Long _processUId;
    protected String _processType;
    protected String _conditionGuard;
    protected String _fromBusinessStateKey;
    protected Long _mpcUId;
    protected String _expressionLanguage;
    protected String _conditionExpression;
    protected String _completionType;

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

    //********************************

    public Long getProcessUID()
    {
        return _processUId;
    }

    public String getProcessType()
    {
        return _processType;
    }

    public String getConditionGuard()
    {
        return _conditionGuard;
    }

    public String getFromBusinessStateKey()
    {
        return _fromBusinessStateKey;
    }

    public Long getMpcUId()
    {
        return _mpcUId;
    }

    public String getExpressionLanguage()
    {
        return _expressionLanguage;
    }

    public String getConditionExpression()
    {
        return _conditionExpression;
    }

    public String getCompletionType()
    {
        return _completionType;
    }

    //*******************************
    public void setProcessUID(Long processUId)
    {
        _processUId = processUId;
    }

    public void setProcessType(String processType)
    {
        _processType = processType;
    }

    public void setConditionGuard(String conditionGuard)
    {
        _conditionGuard = conditionGuard;
    }

    public void setFromBusinessStateKey(String fromBusinessStateKey)
    {
        _fromBusinessStateKey = fromBusinessStateKey;
    }

    public void setMpcUId(Long mpcUId)
    {
        _mpcUId = mpcUId;
    }

    public void setExpressionLanguage(String expressionLanguage)
    {
        _expressionLanguage=expressionLanguage;
    }

    public void setConditionExpression(String conditionExpression)
    {
        _conditionExpression = conditionExpression;
    }

    public void setCompletionType(String completionType)
    {
        _completionType=completionType;
    }

}
