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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFTransition;

public class BpssTransition extends GWFTransition
    implements IBpssTransition
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 336854788678866467L;
		protected Boolean _onInitiation;
    protected String _conditionGuard;
    protected String _fromBusinessStateKey;
    protected String _toBusinessStateKey;
    protected String _expressionLanguage;
    protected String _conditionExpression;

    public BpssTransition()
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

    //*********************************
    public Boolean getOnInitiation()
    {
        return _onInitiation;
    }

    public String getConditionGuard()
    {
        return _conditionGuard;
    }

    public String getFromBusinessStateKey()
    {
        return _fromBusinessStateKey;
    }

    public String getToBusinessStateKey()
    {
        return _toBusinessStateKey;
    }

    public String getExpressionLanguage()
    {
        return _expressionLanguage;
    }

    public String getConditionExpression()
    {
        return _conditionExpression;
    }


    //*********************************
    public void setOnInitiation(Boolean onInitiation)
    {
        _onInitiation = onInitiation;
    }

    public void setConditionGuard(String conditionGuard)
    {
        _conditionGuard = conditionGuard;
    }

    public void setFromBusinessStateKey(String fromBusinessStateKey)
    {
        _fromBusinessStateKey = fromBusinessStateKey;
    }

    public void setToBusinessStateKey(String toBusinessStateKey)
    {
        _toBusinessStateKey = toBusinessStateKey;
    }

    public void setExpressionLanguage(String expressionLanguage)
    {
        _expressionLanguage=expressionLanguage;
    }

    public void setConditionExpression(String conditionExpression)
    {
        _conditionExpression = conditionExpression;
    }


}
