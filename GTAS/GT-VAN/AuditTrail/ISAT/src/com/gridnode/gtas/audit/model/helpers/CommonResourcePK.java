/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommonResourcePK.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 02, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.model.helpers;

import java.util.Hashtable;

/**
 * This class is represent the Primary Key class for CommonResource entity
 * @author Tam Wei Xiang
 * @since GT 4.03 (GT-VAN)
 */
public class CommonResourcePK 
{
	private String _type;
	private String _code;
	private String _groupName;
	
	public CommonResourcePK(String type, String code, String groupName)
	{
		setType(type);
		setCode(code);
		setGroupName(groupName);
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String _code) {
		this._code = _code;
	}

	public String getGroupName() {
		return _groupName;
	}

	public void setGroupName(String name) {
		_groupName = name;
	}

	public String getType() {
		return _type;
	}

	public void setType(String _type) {
		this._type = _type;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof CommonResourcePK)
		{
			CommonResourcePK otherCmmPK = (CommonResourcePK)obj;
			return _type.equals(otherCmmPK.getType()) && _code.equals(otherCmmPK.getCode()) &&
			       _groupName.equals(otherCmmPK.getGroupName());
		}
		return false;
	}
  
  public int hashCode()
  {
    return (getGroupName()+getType()+getCode()).hashCode();
  }
  
  public String toString()
  {
    return "CommonResourcePK [type: "+getType()+" code: "+getCode()+" groupName: "+getGroupName()+"]";
  }
  
}
