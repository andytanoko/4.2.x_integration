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


public class XpdlPackage extends AbstractEntity
    implements IXpdlPackage
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 994979371131188402L;
		//Attributes
    protected String _packageId;
    protected String _packageName;
    protected String _packageDescription;
    protected String _extendedAttributes;
    protected String _specificationId;
    protected String _specificationVersion;
    protected String _sourceVendorInfo;
    protected java.util.Date _creationDateTime;
    protected String _documentationUrl;
    protected String _priorityUnit;
    protected String _costUnit;
    protected String _author;
    protected String _versionId;
    protected String _codepage;
    protected String _countrykey;
    protected String _publicationStatus;
    protected Long _responsibleListUId;
    protected String _graphConformance;
    protected short _state = STATE_ENABLED;

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

    public String getPackageId()
    {
        return _packageId;
    }

    public String getPackageName()
    {
        return _packageName;
    }

    public String getPackageDescription()
    {
        return _packageDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getSpecificationId()
    {
        return _specificationId;
    }

    public String getSpecificationVersion()
    {
        return _specificationVersion;
    }

    public String getSourceVendorInfo()
    {
        return _sourceVendorInfo;
    }

    public java.util.Date getCreationDateTime()
    {
        return _creationDateTime;
    }

    public String getDocumentationUrl()
    {
        return _documentationUrl;
    }

    public String getPriorityUnit()
    {
        return _priorityUnit;
    }

    public String getCostUnit()
    {
        return _costUnit;
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

    public String getGraphConformance()
    {
        return _graphConformance;
    }

    public short getState()
    {
      return _state;
    }
    
    // ******************** Setters for attributes ***************************

    public void setPackageId(String packageId)
    {
        _packageId = packageId;
    }

    public void setPackageName(String packageName)
    {
        _packageName = packageName;
    }

    public void setPackageDescription(String packageDescription)
    {
        _packageDescription = packageDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setSpecificationId(String specificationId)
    {
        _specificationId = specificationId;
    }

    public void setSpecificationVersion(String specificationVersion)
    {
        _specificationVersion = specificationVersion;
    }

    public void setSourceVendorInfo(String sourceVendorInfo)
    {
        _sourceVendorInfo = sourceVendorInfo;
    }

    public void setCreationDateTime(java.util.Date creationDateTime)
    {
        _creationDateTime = creationDateTime;
    }

    public void setDocumentationUrl(String documentationUrl)
    {
        _documentationUrl = documentationUrl;
    }

    public void setPriorityUnit(String priorityUnit)
    {
        _priorityUnit = priorityUnit;
    }

    public void setCostUnit(String costUnit)
    {
        _costUnit = costUnit;
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

    public void setGraphConformance(String graphConformance)
    {
        _graphConformance = graphConformance;
    }
    
    public void setState(short state)
    {
      _state=state;
    }

}
