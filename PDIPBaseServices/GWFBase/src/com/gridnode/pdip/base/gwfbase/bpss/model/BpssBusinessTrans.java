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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFProcess;

 
public class BpssBusinessTrans extends GWFProcess
    implements IBpssBusinessTrans
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6778928132686483667L;
		protected String _businessTransName;
    protected String _pattern;
    protected String _preCondition;
    protected String _postCondition;
    protected Boolean _isGuaranteedDeliveryRequired;
    protected String _beginsWhen;
    protected String _endsWhen;
    protected Long _bpssReqBusinessActivity;
    protected Long _bpssResBusinessActivity;

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

    // *********************** Getters for attributes **********************


    public String getBusinessTransName()
    {
        return _businessTransName;
    }

    public String getPattern()
    {
        return _pattern;
    }

    public String getPreCondition()
    {
        return _preCondition;
    }

    public String getPostCondition()
    {
        return _postCondition;
    }

    public Boolean getIsGuaranteedDeliveryRequired()
    {
        return _isGuaranteedDeliveryRequired;
    }

    public String getBeginsWhen()
    {
        return _beginsWhen;
    }

    public String getEndsWhen()
    {
        return _endsWhen;
    }

    public Long getBpssReqBusinessActivityUId()
    {
        return _bpssReqBusinessActivity;
    }

    public Long getBpssResBusinessActivityUId()
    {
        return _bpssResBusinessActivity;
    }

    // *********************** Setters for attributes **********************
    public void setBusinessTransName(String businessTransName)
    {
        _businessTransName = businessTransName;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public void setPreCondition(String preCondition)
    {
        _preCondition = preCondition;
    }

    public void setPostCondition(String postCondition)
    {
        _postCondition = postCondition;
    }

    public void setIsGuaranteedDeliveryRequired(Boolean isGuaranteedDeliveryRequired)
    {
        _isGuaranteedDeliveryRequired = isGuaranteedDeliveryRequired;
    }

    public void setBeginsWhen(String beginsWhen)
    {
        _beginsWhen = beginsWhen;
    }

    public void setEndsWhen(String endsWhen)
    {
        _endsWhen = endsWhen;
    }

    public void setBpssReqBusinessActivityUId(Long bpssReqBusinessActivity)
    {
        _bpssReqBusinessActivity = bpssReqBusinessActivity;
    }

    public void setBpssResBusinessActivityUId(Long bpssResBusinessActivity)
    {
        _bpssResBusinessActivity = bpssResBusinessActivity;
    }

}
