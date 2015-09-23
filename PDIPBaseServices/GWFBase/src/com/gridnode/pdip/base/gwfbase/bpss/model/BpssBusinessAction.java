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


public abstract class BpssBusinessAction extends GWFActivity
{

    protected String _businessActionName;
    protected Boolean _isIntelligibleChkReq;
    protected Boolean _isAuthReq;
    protected Boolean _isNonRepudiationReq;
    protected Boolean _isNonRepudiationOfReceiptReq;
    protected String _timeToAckReceipt;

    public int getActivityType()
    {
        return AUTOMATIC_TYPE;
    }

    // *** IGWFActivity interface methods
    public String getActivityName()
    {
        return _businessActionName;
    }

    // *********************** Getters for attributes **********************


    public String getBusinessActionName()
    {
        return _businessActionName;
    }

    public Boolean getIsIntelligibleChkReq()
    {
        return _isIntelligibleChkReq;
    }

    public Boolean getIsAuthReq()
    {
        return _isAuthReq;
    }

    public String getTimeToAckReceipt()
    {
        return _timeToAckReceipt;
    }

    public Boolean getIsNonRepudiationReq()
    {
        return _isNonRepudiationReq;
    }

    public Boolean getIsNonRepudiationOfReceiptReq()
    {
        return _isNonRepudiationOfReceiptReq;
    }

    // *********************** Setters for attributes **********************
    public void setBusinessActionName(String businessActionName)
    {
        _businessActionName = businessActionName;
    }

    public void setIsIntelligibleChkReq(Boolean isIntelligibleChkReq)
    {
        _isIntelligibleChkReq = isIntelligibleChkReq;
    }

    public void setIsAuthReq(Boolean isAuthReq)
    {
        _isAuthReq = isAuthReq;
    }

    public void setTimeToAckReceipt(String timeToAckReceipt)
    {
        _timeToAckReceipt = timeToAckReceipt;
    }

    public void setIsNonRepudiationReq(Boolean isNonRepudiationReq)
    {
        _isNonRepudiationReq = isNonRepudiationReq;
    }

    public void setIsNonRepudiationOfReceiptReq(Boolean isNonRepudiationOfReceiptReq)
    {
        _isNonRepudiationOfReceiptReq = isNonRepudiationOfReceiptReq;
    }

}
