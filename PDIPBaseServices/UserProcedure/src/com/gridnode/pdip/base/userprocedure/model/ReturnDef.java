/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IParamDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 * Jan  19 2002    Jagadeesh              Modified : To Extend from AbstractEntity.
 */

package com.gridnode.pdip.base.userprocedure.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;


public class ReturnDef extends AbstractEntity implements IReturnDef,IOperator,IAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5846226101236692214L;
	private int _operator;
  private Object _value=null;
  private int _action = IAction.CONTINUE;
  private Long _alert=null;

  public ReturnDef()
  {
  }

  public int getOperator()
  {
    return _operator;
  }

  public Object getValue()
  {
    return _value;
  }

  public int getAction()
  {
    return _action;
  }

  public Long getAlert()
  {
    return _alert;
  }

  public void setOperator(int operator)
  {
    _operator = operator;
  }

  public void setValue(Object value)
  {
    _value = value;
  }

  public void setAction(int action)
  {
    _action = action;
  }

  public void setAlert(Long alert)
  {
    _alert = alert;
  }

  public String toString()
  {
     return   getAction()+"/"+getAlert()+"/"+getOperator()+
     "/"+getValue();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
     return   getAction()+"/"+getAlert()+"/"+getOperator()+
     "/"+getValue();
  }

  public Number getKeyId()
  {
    return null;
  }


}