/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommonResource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 05, 2007    Tam Wei Xiang       Created
 */

package com.gridnode.gtas.audit.model;

import java.io.Serializable;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class has a direct mapping on one of the record in the resource
 *  table. The common resource is shared among the same group name.
 * @author Tam Wei Xiang
 * 
 * @hibernate.class table ="`isat_resource`"
 * 
 * @hibernate.query name = "getCommonResourceWithCategory"
 *     query = "FROM CommonResource AS cr WHERE cr.groupName != :groupName"
 * @hibernate.query
 *   query = "SELECT COUNT(*) FROM CommonResource AS cr WHERE cr.groupName=:groupName AND cr.type=:type AND cr.code=:code" 
 *   name = "getCommonResourceCount"    
 * @hibernate.query
 *   query = "FROM CommonResource AS cr WHERE cr.type=:type AND cr.code=:code"
 *   name = "getCommonResource"  
 */
public class CommonResource  extends AbstractPersistable implements IAuditTrailEntity, Serializable 
{
	private String _groupName;
	private String _type;
	private String _code;
	private String _value;
	
  public CommonResource() {}
  
	public CommonResource(String groupName, String type, String code, String value)
	{
		setGroupName(groupName);
		setType(type);
		setCode(code);
		setValue(value);
	}
	
  /**
   * @hibernate.property name = "code" column = "`code`"
   * @return
   */
	public String getCode() {
		return _code;
	}

	public void setCode(String _code) {
		this._code = _code;
	}
	
	/**
   * @hibernate.property name="type" column = "`type`"
   */
	public String getType() {
		return _type;
	}

	public void setType(String _type) {
		this._type = _type;
	}
	
	/**
   * @hibernate.property name="value" column = "`value`"
   */
	public String getValue() {
		return _value;
	}

	public void setValue(String _value) {
		this._value = _value;
	}

	/**
	   * @hibernate.property name = "groupName" column = "`group_name`"
	   */
	public String getGroupName()
	{
	    return _groupName;
	}

	public void setGroupName(String name)
	{
	  _groupName = name;
	}
	
	public String toString()
	{
    return "CommonResource [type: "+getType()+" code: "+getCode()+" value:"+getValue()+" groupName: "+getGroupName()+"]";
	}
}
