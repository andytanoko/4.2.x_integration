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


public class XpdlApplication extends AbstractEntity
    implements IXpdlApplication
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -595418224481548314L;
		//Attributes
    protected String _applicationId;
    protected String _applicationName;
    protected String _applicationDescription;
    protected String _extendedAttributes;
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

    public String getApplicationId()
    {
        return _applicationId;
    }

    public String getApplicationName()
    {
        return _applicationName;
    }

    public String getApplicationDescription()
    {
        return _applicationDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
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

    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
    }

    public void setApplicationName(String applicationName)
    {
        _applicationName = applicationName;
    }

    public void setApplicationDescription(String applicationDescription)
    {
        _applicationDescription = applicationDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
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
