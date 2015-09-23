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

public class StringData
extends AbstractEntity
implements IStringData,IData {
 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5108924514602983853L;
	//Attributes
	protected String _data;
	protected String _dataType;

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

	public Object getData()
	{
		return _data;
	}

	public String getDataType()
	{
		return _dataType;
	}


	// ******************** Setters for attributes ***************************

	public void setData(Object obj)
	{       if(obj!=null)
                    _data=obj.toString();
                else _data=null;
	}

	public void setDataType(String dataType)
	{
		_dataType=dataType;
	}


}