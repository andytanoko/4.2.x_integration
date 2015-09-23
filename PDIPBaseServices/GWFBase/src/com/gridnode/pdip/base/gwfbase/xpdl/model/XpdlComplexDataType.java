/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 28 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.xpdl.model;


import com.gridnode.pdip.framework.db.entity.*;


public class XpdlComplexDataType extends AbstractEntity
    implements IXpdlComplexDataType
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5841176809103530609L;
		//Attributes
    protected String _dataTypeName;
    protected Long _complexDataTypeUId;
    protected Long _subTypeUId;
    protected Integer _arrayLowerIndex;
    protected Integer _arrayUpperIndex;
    //protected Long _memberParentTypeInfoUId;

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

    public String getDataTypeName(){
        return _dataTypeName;
    }
    public Long getComplexDataTypeUId(){
        return _complexDataTypeUId;
    }
    public Long getSubTypeUId(){
        return _subTypeUId;
    }
    public Integer getArrayLowerIndex(){
        return _arrayLowerIndex;
    }
    public Integer getArrayUpperIndex(){
        return _arrayUpperIndex;
    }
/*
    public Long getMemberParentTypeInfoUId(){
        return _memberParentTypeInfoUId;
    }
*/
    // ******************** Setters for attributes ***************************

    public void setDataTypeName(String dataTypeName){
        _dataTypeName=dataTypeName;
    }
    public void setComplexDataTypeUId(Long complexDataTypeUId){
        _complexDataTypeUId=complexDataTypeUId;
    }
    public void setSubTypeUId(Long subTypeUId){
        _subTypeUId=subTypeUId ;
    }
    public void setArrayLowerIndex(Integer arrayLowerIndex){
        _arrayLowerIndex=arrayLowerIndex;
    }
    public void setArrayUpperIndex(Integer arrayUpperIndex){
        _arrayUpperIndex=arrayUpperIndex;
    }
/*
    public void setMemberParentTypeInfoUId(Long memberParentTypeInfoUId){
        _memberParentTypeInfoUId=memberParentTypeInfoUId;
    }
*/
}
