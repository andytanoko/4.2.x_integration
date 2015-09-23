/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DynamicEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-31     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.*;

class DynamicEntityConstraint extends AbstractEntityConstraint implements IDynamicEntityConstraint
{
  private static final String TYPE_DELIMETER = ";";

  private String[] _allowedTypes = null;

  DynamicEntityConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_DYNAMIC_ENTITY, detail);

    // 20021106 DDJ: Workaround as _allowedTypes would not be set by calling the super constructor
    initialiseEntityConstraint(IConstraint.TYPE_DYNAMIC_ENTITY, detail);
  }

  protected void initialiseEntityConstraint(int type, Properties detail)
  {
    String dynamicTypes = detail.getProperty("dynamic.types",null);
    if(dynamicTypes == null)
    {
      throw new java.lang.IllegalArgumentException("Dynamic entity types not specified");
    }
    StringTokenizer tokenizer = new StringTokenizer(dynamicTypes,TYPE_DELIMETER);
    _allowedTypes = new String[tokenizer.countTokens()];
    int index = 0;
    while(tokenizer.hasMoreTokens())
    {
      _allowedTypes[index] = tokenizer.nextToken();
      index++;
    }
  }

  public String getEntityType()
  {
// 20021101 DDJ: Changed to throw an UnsupportedOperationException
//    return null;
    throw new java.lang.UnsupportedOperationException("use getAllowedTypes() instead");
  }

  public String[] getAllowedTypes()
  {
    return _allowedTypes;
  }
}