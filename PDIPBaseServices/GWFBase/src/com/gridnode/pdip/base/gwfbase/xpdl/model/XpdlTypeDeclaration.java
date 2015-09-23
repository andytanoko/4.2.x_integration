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


public class XpdlTypeDeclaration extends AbstractEntity
    implements IXpdlTypeDeclaration
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1186705677738521661L;
		//Attributes
    protected String _typeId;
    protected String _typeName;
    protected String _typeDescription;
    protected String _extendedAttributes;
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

    public String getTypeId()
    {
        return _typeId;
    }

    public String getTypeName()
    {
        return _typeName;
    }

    public String getTypeDescription()
    {
        return _typeDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
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

    public void setTypeId(String typeId)
    {
        _typeId = typeId;
    }

    public void setTypeName(String typeName)
    {
        _typeName = typeName;
    }

    public void setTypeDescription(String typeDescription)
    {
        _typeDescription = typeDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
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
