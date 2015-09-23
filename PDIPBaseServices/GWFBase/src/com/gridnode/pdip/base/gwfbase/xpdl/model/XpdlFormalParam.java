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


public class XpdlFormalParam extends AbstractEntity
    implements IXpdlFormalParam
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7447379589598117387L;
		//Attributes
    protected String _formalParamId;
    protected String _mode;
    protected Integer _indexNumber;
    protected String _formalParamDescription;
    protected String _applicationId;
    protected String _processId;
    protected String _packageId;
    protected String _pkgVersionId;

    protected String _dataTypeName;
    protected Long _complexDataTypeUId;

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

    public String getFormalParamId()
    {
        return _formalParamId;
    }

    public String getMode()
    {
        return _mode;
    }

    public Integer getIndexNumber()
    {
        return _indexNumber;
    }

    public String getFormalParamDescription()
    {
        return _formalParamDescription;
    }

    public String getApplicationId()
    {
        return _applicationId;
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
    public String getDataTypeName(){
        return _dataTypeName;
    }
    public Long getComplexDataTypeUId(){
        return _complexDataTypeUId;
    }


    // ******************** Setters for attributes ***************************

    public void setFormalParamId(String formalParamId)
    {
        _formalParamId = formalParamId;
    }

    public void setMode(String mode)
    {
        _mode = mode;
    }

    public void setIndexNumber(Integer indexNumber)
    {
        _indexNumber = indexNumber;
    }

    public void setFormalParamDescription(String formalParamDescription)
    {
        _formalParamDescription = formalParamDescription;
    }

    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
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

    public void setDataTypeName(String dataTypeName){
        _dataTypeName=dataTypeName;
    }
    public void setComplexDataTypeUId(Long complexDataTypeUId){
        _complexDataTypeUId=complexDataTypeUId;
    }

}
