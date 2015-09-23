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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlConstants;
 

public class XpdlActivity extends GWFActivity
    implements IXpdlActivity
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4447834385043890252L;
		//Attributes
    protected String _activityId;
    protected String _activityName;
    protected String _activityDescription;
    protected String _extendedAttributes;
    protected Double _activityLimit;
    protected Boolean _isRoute;
    protected String _implementationType;
    protected String _performerId;
    protected String _startMode;
    protected String _finishMode;
    protected Long _priority;
    protected String _instantiation;
    protected Double _cost;
    protected Double _waitingTime;
    protected Double _duration;
    protected String _iconUrl;
    protected String _documentationUrl;
    protected Long _transitionRestrictionListUId;
    protected Double _workingTime;
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

    public int getActivityType()
    {
        if (XpdlConstants.AUTOMATIC_MODE.equals(_startMode))
            return AUTOMATIC_TYPE;
        else if (XpdlConstants.MANUAL_MODE.equals(_startMode))
            return MANUAL_TYPE;
        //                else if(XpdlConstants.IT_SUBFLOW.equals(_implementationType))
        //                    return SUB_PROCESS_TYPE;
        return -1;
    }

    // ******************** Getters for attributes ***************************

    public String getActivityId()
    {
        return _activityId;
    }

    public String getActivityName()
    {
        return _activityName;
    }

    public String getActivityDescription()
    {
        return _activityDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public Double getActivityLimit()
    {
        return _activityLimit;
    }

    public Boolean getIsRoute()
    {
        return _isRoute;
    }

    public String getImplementationType()
    {
        return _implementationType;
    }

    public String getPerformerId()
    {
        return _performerId;
    }

    public String getStartMode()
    {
        return _startMode;
    }

    public String getFinishMode()
    {
        return _finishMode;
    }

    public Long getPriority()
    {
        return _priority;
    }

    public String getInstantiation()
    {
        return _instantiation;
    }

    public Double getCost()
    {
        return _cost;
    }

    public Double getWaitingTime()
    {
        return _waitingTime;
    }

    public Double getDuration()
    {
        return _duration;
    }

    public String getIconUrl()
    {
        return _iconUrl;
    }

    public String getDocumentationUrl()
    {
        return _documentationUrl;
    }

    public Long getTransitionRestrictionListUId()
    {
        return _transitionRestrictionListUId;
    }

    public Double getWorkingTime()
    {
        return _workingTime;
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

    public void setActivityId(String activityId)
    {
        _activityId = activityId;
    }

    public void setActivityName(String activityName)
    {
        _activityName = activityName;
    }

    public void setActivityDescription(String activityDescription)
    {
        _activityDescription = activityDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setActivityLimit(Double activityLimit)
    {
        _activityLimit = activityLimit;
    }

    public void setIsRoute(Boolean isRoute)
    {
        _isRoute = isRoute;
    }

    public void setImplementationType(String implementationType)
    {
        _implementationType = implementationType;
    }

    public void setPerformerId(String performerId)
    {
        _performerId = performerId;
    }

    public void setStartMode(String startMode)
    {
        _startMode = startMode;
    }

    public void setFinishMode(String finishMode)
    {
        _finishMode = finishMode;
    }

    public void setPriority(Long priority)
    {
        _priority = priority;
    }

    public void setInstantiation(String instantiation)
    {
        _instantiation = instantiation;
    }

    public void setCost(Double cost)
    {
        _cost = cost;
    }

    public void setWaitingTime(Double waitingTime)
    {
        _waitingTime = waitingTime;
    }

    public void setDuration(Double duration)
    {
        _duration = duration;
    }

    public void setIconUrl(String iconUrl)
    {
        _iconUrl = iconUrl;
    }

    public void setDocumentationUrl(String documentationUrl)
    {
        _documentationUrl = documentationUrl;
    }

    public void setTransitionRestrictionListUId(Long transitionRestrictionListUId)
    {
        _transitionRestrictionListUId = transitionRestrictionListUId;
    }

    public void setWorkingTime(Double workingTime)
    {
        _workingTime = workingTime;
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
