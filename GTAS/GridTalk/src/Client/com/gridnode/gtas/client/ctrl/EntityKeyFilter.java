/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityKeyFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-21     Andrew Hill         Created.
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.*; //nb: outside ctrl package - naughty


public class EntityKeyFilter implements IFilter
{
  Object _field = null;
  Object _value = null;

  public EntityKeyFilter(Object field, Object value)
  {
    if(field == null) throw new NullPointerException("field is null");
    if(! ( (field instanceof Number) || (field instanceof String) ) )
    {
      throw new IllegalArgumentException("field not Number or String - "
            + field.getClass().getName());
    }
    _field = field;
    _value = value;
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    try
    {
      if(object == null) return false;
      if(! (object instanceof IGTEntity) )
      {
        throw new ClassCastException("object of type "
                                      + object.getClass().getName()
                                      + " does not implement IGTEntity"); //20030424AH
      }
      IGTEntity entity = (IGTEntity)object;
      Object fieldValue = _field instanceof String ? entity.getFieldValue((String)_field)
                                                   : entity.getFieldValue((Number)_field); //20030327AH - Fix wrong cast
      return StaticUtils.objectsEqual(fieldValue, _value);
    }
    catch(Throwable t)
    {
      throw new GTClientException("EntityKeyFilter encountered error filtering object "
                + object
                + " where field=" + _field
                + " and value=" + _value,t);
    }
  }

}