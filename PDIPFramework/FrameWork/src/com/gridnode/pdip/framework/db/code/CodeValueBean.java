/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CodeValueBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2005    Tam Wei Xiang       Created
 * Feb 07 2007			Alain Ah Ming				Log warning message if throwing up exception
 */
package com.gridnode.pdip.framework.db.code;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.log.Log;

/**
 * An entity bean to manage CodeValue db record. For the purpose of the bean,
 * pls read the detail in CodeValue.
 * 
 * @author Tam Wei Xiang
 * @since GT 2.4.9
 * @version GT 4.0 VAN
 */
public abstract class CodeValueBean
	implements EntityBean
{
	private transient EntityContext _ctx = null;
	
	public Long ejbCreate(CodeValue cv)
		throws CreateException
	{
		try
		{
			Long uid = KeyGen.getNextId("CodeValue", false);
			setUID(uid);
		}
		catch(Exception e)
		{
			Log.warn("[CodeValueBean.ejbCreate] error in getting the uid.",e);
			throw new CreateException(e.toString());
		}
		
		setCode(cv.getCode());
		setDescription(cv.getDescription());
		setEntityType(cv.getEntityType());
		setFieldID(cv.getFieldID());
		
		return null;
	}
	
	public void ejbPostCreate(CodeValue cv) 
	{
		
	}
	
	public void ejbActivate()
  {
		
  }

  public void ejbPassivate()
  {
  	
  }

  public void ejbLoad()
  {
  	
  }

  public void ejbStore()
  {
  	
  }
  
  public void setEntityContext(EntityContext ctx)
  {
  	this._ctx = ctx;
  }
  
  public void unsetEntityContext()
  {
  	this._ctx = null;
  }
  
  //---------------------- Virtual persistentce field getter and setter ----------
  public abstract Long getUID();
  public abstract void setUID(Long uid);
  
  public abstract String getCode();
  public abstract void setCode(String code);
  
  public abstract String getDescription();
  public abstract void setDescription(String desc);
  
  public abstract String getEntityType();
  public abstract void setEntityType(String entityType);
  
  public abstract Integer getFieldID();
  public abstract void setFieldID(Integer fieldID);
}
