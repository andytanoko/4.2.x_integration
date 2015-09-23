/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefCollection.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 20, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since GT 2.4.7
 */
public class FieldValueCollection   //EStore Process Definition Collection
						 extends AbstractEntity
						 implements IFieldValueCollection
{
	
	private ArrayList _value;  //store a collection of process def
	
	public FieldValueCollection() {}
	
	public FieldValueCollection(ArrayList processDef)
	{
		this._value = processDef;
	}
	
	// **************** Methods from AbstractEntity *********************
	public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return new StringBuffer("ProcessDef LIst key ").append(this.getKey()).append("Process Def List size is ").append(this._value.size()).toString();
  }

  public Number getKeyId()
  {
    return UID;
  }
  
  //***************** Getters and Setters for attributes ***********************
  public ArrayList getValue()
  {
  	return this._value;
  }
  
  public void setValue(ArrayList list)
  {
  	this._value = list;
  }
}
