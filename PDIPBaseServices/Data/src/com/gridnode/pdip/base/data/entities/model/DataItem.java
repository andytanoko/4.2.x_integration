/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 5 27 2002	MAHESH              Created
 */
package com.gridnode.pdip.base.data.entities.model;

import com.gridnode.pdip.framework.db.entity.*;

public class DataItem
extends AbstractEntity
implements IDataItem {
 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5409914253018254380L;
	//Attributes
	protected String _dataContextType;
	protected Long _contextUId;
	protected String _dataDefKey;
	protected Boolean _isCopy;
        protected String _dataDefType;
        protected String _dataDefName;

	//Abstract methods of AbstractEntity
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Number getKeyId() {
		return UID;
	}

	public String getEntityDescr() {
		return ENTITY_NAME+":"+_uId;
	}

	// ******************** Getters for attributes ***************************

	public String getDataContextType()
	{
		return _dataContextType;
	}

	public Long getContextUId()
	{
		return _contextUId;
	}

	public String getDataDefKey()
	{
		return _dataDefKey;
	}

	public Boolean getIsCopy()
	{
		return _isCopy;
	}

	public String getDataDefType()
	{
		return _dataDefType;
	}

	public String getDataDefName()
	{
		return _dataDefName;
	}


	// ******************** Setters for attributes ***************************

	public void setDataContextType(String dataContextType)
	{
		_dataContextType=dataContextType;
	}

	public void setContextUId(Long contextUId)
	{
		_contextUId=contextUId;
	}

	public void setDataDefKey(String dataDefKey)
	{
		_dataDefKey=dataDefKey;
	}

	public void setIsCopy(Boolean isCopy)
	{
		_isCopy=isCopy;
	}

	public void setDataDefType(String dataDefType)
	{
		_dataDefType=dataDefType;
	}

	public void setDataDefName(String dataDefName)
	{
		_dataDefName=dataDefName;
	}

}