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


public class XpdlDataField extends AbstractEntity
    implements IXpdlDataField
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8132288498668436368L;
		//Attributes
    protected String _dataFieldId;
    protected String _dataFieldName;
    protected String _dataFieldDescription;
    protected String _extendedAttributes;
    protected Boolean _isArray;
    protected String _initialValue;
    protected Long _lengthBytes;
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

    public String getDataFieldId()
    {
        return _dataFieldId;
    }

    public String getDataFieldName()
    {
        return _dataFieldName;
    }

    public String getDataFieldDescription()
    {
        return _dataFieldDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public Boolean getIsArray()
    {
        return _isArray;
    }

    public String getInitialValue()
    {
        return _initialValue;
    }

    public Long getLengthBytes()
    {
        return _lengthBytes;
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

    public void setDataFieldId(String dataFieldId)
    {
        _dataFieldId = dataFieldId;
    }

    public void setDataFieldName(String dataFieldName)
    {
        _dataFieldName = dataFieldName;
    }

    public void setDataFieldDescription(String dataFieldDescription)
    {
        _dataFieldDescription = dataFieldDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setIsArray(Boolean isArray)
    {
        _isArray = isArray;
    }

    public void setInitialValue(String initialValue)
    {
        _initialValue = initialValue;
    }

    public void setLengthBytes(Long lengthBytes)
    {
        _lengthBytes = lengthBytes;
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
