/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CodeValue.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.db.code;

import java.io.Serializable;

/**
 * This class is the value obj for CodeValueBean.
 * 
 * For those field we need to populate into the drop down list, list box etc. We may
 * not fetch directly from the entity directly. For example, we won't retrieve the distinct docType
 * from com.gridnode.gtas.server.estore.model.DocumentFieldMetaInfo since such a query will not be efficient if the db record
 * continue grow in the future.
 * 
 * It is good we maintain a seperate table to keep track of such info that require
 * we populate into list box for example.
 * 
 * @author Tam Wei Xiang
 * @since GT 2.4.9
 */
public class CodeValue
	implements Serializable
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -71619365822319290L;
	private String _code;          //the unique key for the entity. Eg. The unique key for document_type is docType 
	private String _description;   //the description for the code
	private String _entityType;    //The class name of the entity
	private Integer _fieldID;      //the field id of an entity
	
	public CodeValue(String code, String desc, String type, Integer fieldID)
	{
		this._code = code;
		this._description = desc;
		this._entityType = type;
		this._fieldID = fieldID;
	}

	/**
	 * @return Returns the _code.
	 */
	public String getCode()
	{
		return _code;
	}

	/**
	 * @param _code The _code to set.
	 */
	public void setCode(String _code)
	{
		this._code = _code;
	}

	/**
	 * @return Returns the _description.
	 */
	public String getDescription()
	{
		return _description;
	}

	/**
	 * @param _description The _description to set.
	 */
	public void setDescription(String _description)
	{
		this._description = _description;
	}

	/**
	 * @return Returns the _entityType.
	 */
	public String getEntityType()
	{
		return _entityType;
	}

	/**
	 * @param type The _entityType to set.
	 */
	public void setEntityType(String type)
	{
		_entityType = type;
	}
	
	public Integer getFieldID()
	{
		return _fieldID;
	}
	
	public void setFieldID(Integer fieldID)
	{
		_fieldID = fieldID;
	}
}
