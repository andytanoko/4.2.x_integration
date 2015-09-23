/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */

package com.gridnode.gtas.server.document.helpers;


import com.gridnode.pdip.app.rnif.helpers.Logger;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.gtas.server.document.helpers.AS2DocTypeMappingEntityHandler;

import java.util.Collection;

public class AS2DocTypeMappingEntityHelper implements ICheckConflict
{
	private static AS2DocTypeMappingEntityHelper _self = null;

  private AS2DocTypeMappingEntityHelper()
  {
    super();
  }

  public static AS2DocTypeMappingEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(AS2DocTypeMappingEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new AS2DocTypeMappingEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity as2DocTypeMapping) throws Exception
  {
    Logger.debug("[AS2DocTypeMappingHelper.checkDuplicate] Start");
    String as2DocType = as2DocTypeMapping.getFieldValue(AS2DocTypeMapping.AS2_DOC_TYPE).toString();
    String partnerId = as2DocTypeMapping.getFieldValue(AS2DocTypeMapping.PARTNER_ID).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AS2DocTypeMapping.AS2_DOC_TYPE, filter.getEqualOperator(),
    		as2DocType, false);
    filter.addSingleFilter(filter.getAndConnector(), AS2DocTypeMapping.PARTNER_ID, filter.getEqualOperator(),
    		partnerId, false);

    AS2DocTypeMappingEntityHandler handler = AS2DocTypeMappingEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (AS2DocTypeMapping)results.iterator().next();
    }
    return null;
  }
  
}
