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


 

public class BpssBusinessTransActivity extends BpssBusinessActivity
    implements IBpssBusinessTransActivity
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6509640593303340498L;
		protected String _timeToPerform;
    protected Boolean _isConcurrent;
    protected Boolean _isLegallyBinding;
    protected Long _businessTransUId;
    protected String _fromAuthorizedRole;
    protected String _toAuthorizedRole;

    public BpssBusinessTransActivity()
    {
    }

    public int getActivityType()
    {
        //return MANUAL_TYPE;
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

    //*********************************
    //*********************************
    public String getTimeToPerform()
    {
        return _timeToPerform;
    }

    public Boolean getIsConcurrent()
    {
        return _isConcurrent;
    }

    public Boolean getIsLegallyBinding()
    {
        return _isLegallyBinding;
    }

    public Long getBusinessTransUId()
    {
        return _businessTransUId;
    }

    public String getFromAuthorizedRole()
    {
        return _fromAuthorizedRole;
    }

    public String getToAuthorizedRole()
    {
        return _toAuthorizedRole;
    }

    public void setTimeToPerform(String timeToPerform)
    {
        _timeToPerform = timeToPerform;
    }

    public void setIsConcurrent(Boolean isConcurrent)
    {
        _isConcurrent = isConcurrent;
    }

    public void setIsLegallyBinding(Boolean isLegallyBinding)
    {
        _isLegallyBinding = isLegallyBinding;
    }

    public void setBusinessTransUId(Long businessTransUId)
    {
        _businessTransUId = businessTransUId;
    }

    public void setFromAuthorizedRole(String fromAuthorizedRole)
    {
        _fromAuthorizedRole = fromAuthorizedRole;
    }

    public void setToAuthorizedRole(String toAuthorizedRole)
    {
        _toAuthorizedRole = toAuthorizedRole;
    }

}
