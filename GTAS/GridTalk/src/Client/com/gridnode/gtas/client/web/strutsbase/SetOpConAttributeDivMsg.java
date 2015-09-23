/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetOpConAttributeDivMsg.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

/**
 * A simple implementation of ISetOpConAttributeDivMsg suitable for general use.
 * Nb - it is not recomended to change the properties once the msg has been added to the queue.
 */
public class SetOpConAttributeDivMsg implements ISetOpConAttributeDivMsg
{
  protected Object _attribute;
  protected Object _value;
  protected boolean _propagating;
  protected OperationContext _source;
  
  public Object getAttribute()
  {
    return _attribute;
  }

  public boolean isPropagating()
  {
    return _propagating;
  }

  public OperationContext getSource()
  {
    return _source;
  }

  public Object getValue()
  {
    return _value;
  }

  public void setAttribute(Object object)
  {
    _attribute = object;
  }

  public void setPropagating(boolean b)
  {
    _propagating = b;
  }

  public void setSource(OperationContext context)
  {
    _source = context;
  }

  public void setValue(Object object)
  {
    _value = object;
  }

}
