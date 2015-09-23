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

public class DataItemLocationMap
extends AbstractEntity
implements IDataItemLocationMap {
 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4802287224080946255L;
	//Attributes
	protected Long _dataItemUId;
        protected String _dataFieldName;
	protected String _dataLocation;

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

	public Long getDataItemUId()
	{
		return _dataItemUId;
	}

	public String getDataFieldName()
	{
		return _dataFieldName;
	}

	public String getDataLocation()
	{
		return _dataLocation;
	}


	// ******************** Setters for attributes ***************************

	public void setDataItemUId(Long dataItemUId)
	{
		_dataItemUId=dataItemUId;
	}

	public void setDataFieldName(String dataFieldName)
	{
		_dataFieldName=dataFieldName;
	}

	public void setDataLocation(String dataLocation)
	{
		_dataLocation=dataLocation;
	}


}