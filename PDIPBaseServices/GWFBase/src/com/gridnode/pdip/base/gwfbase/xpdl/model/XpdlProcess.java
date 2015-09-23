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


import com.gridnode.pdip.base.gwfbase.baseentity.*;


public class XpdlProcess extends GWFProcess
    implements IXpdlProcess
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5735199751944031655L;
		//Attributes
    protected String _processId;
    protected String _processName;
    protected String _processDescription;
    protected String _extendedAttributes;
    protected String _durationUnit;
    protected java.util.Date _creationDateTime;
    protected String _headerDescription;
    protected Long _priority;
    protected Double _processLimit;
    protected java.util.Date _validFromDate;
    protected java.util.Date _validToDate;
    protected Double _waitingTime;
    protected Double _workingTime;
    protected Double _duration;
    protected String _author;
    protected String _versionId;
    protected String _codepage;
    protected String _countrykey;
    protected String _publicationStatus;
    protected Long _responsibleListUId;
    protected String _packageId;
    protected String _defaultStartActivityId;
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

    public String getProcessId()
    {
        return _processId;
    }

    public String getProcessName()
    {
        return _processName;
    }

    public String getProcessDescription()
    {
        return _processDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getDurationUnit()
    {
        return _durationUnit;
    }

    public java.util.Date getCreationDateTime()
    {
        return _creationDateTime;
    }

    public String getHeaderDescription()
    {
        return _headerDescription;
    }

    public Long getPriority()
    {
        return _priority;
    }

    public Double getProcessLimit()
    {
        return _processLimit;
    }

    public java.util.Date getValidFromDate()
    {
        return _validFromDate;
    }

    public java.util.Date getValidToDate()
    {
        return _validToDate;
    }

    public Double getWaitingTime()
    {
        return _waitingTime;
    }

    public Double getWorkingTime()
    {
        return _workingTime;
    }

    public Double getDuration()
    {
        return _duration;
    }

    public String getAuthor()
    {
        return _author;
    }

    public String getVersionId()
    {
        return _versionId;
    }

    public String getCodepage()
    {
        return _codepage;
    }

    public String getCountrykey()
    {
        return _countrykey;
    }

    public String getPublicationStatus()
    {
        return _publicationStatus;
    }

    public Long getResponsibleListUId()
    {
        return _responsibleListUId;
    }

    public String getPackageId()
    {
        return _packageId;
    }

    public String getDefaultStartActivityId()
    {
        return _defaultStartActivityId;
    }

    public String getPkgVersionId()
    {
        return _pkgVersionId;
    }

    // ******************** Setters for attributes ***************************

    public void setProcessId(String processId)
    {
        _processId = processId;
    }

    public void setProcessName(String processName)
    {
        _processName = processName;
    }

    public void setProcessDescription(String processDescription)
    {
        _processDescription = processDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setDurationUnit(String durationUnit)
    {
        _durationUnit = durationUnit;
    }

    public void setCreationDateTime(java.util.Date creationDateTime)
    {
        _creationDateTime = creationDateTime;
    }

    public void setHeaderDescription(String headerDescription)
    {
        _headerDescription = headerDescription;
    }

    public void setPriority(Long priority)
    {
        _priority = priority;
    }

    public void setProcessLimit(Double processLimit)
    {
        _processLimit = processLimit;
    }

    public void setValidFromDate(java.util.Date validFromDate)
    {
        _validFromDate = validFromDate;
    }

    public void setValidToDate(java.util.Date validToDate)
    {
        _validToDate = validToDate;
    }

    public void setWaitingTime(Double waitingTime)
    {
        _waitingTime = waitingTime;
    }

    public void setWorkingTime(Double workingTime)
    {
        _workingTime = workingTime;
    }

    public void setDuration(Double duration)
    {
        _duration = duration;
    }

    public void setAuthor(String author)
    {
        _author = author;
    }

    public void setVersionId(String versionId)
    {
        _versionId = versionId;
    }

    public void setCodepage(String codepage)
    {
        _codepage = codepage;
    }

    public void setCountrykey(String countrykey)
    {
        _countrykey = countrykey;
    }

    public void setPublicationStatus(String publicationStatus)
    {
        _publicationStatus = publicationStatus;
    }

    public void setResponsibleListUId(Long responsibleListUId)
    {
        _responsibleListUId = responsibleListUId;
    }

    public void setPackageId(String packageId)
    {
        _packageId = packageId;
    }

    public void setDefaultStartActivityId(String defaultStartActivityId)
    {
        _defaultStartActivityId = defaultStartActivityId;
    }

    public void setPkgVersionId(String pkgVersionId)
    {
        _pkgVersionId = pkgVersionId;
    }

}
