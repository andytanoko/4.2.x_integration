/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the ProcessMapping entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ProcessMappingEntityHelper implements ICheckConflict
{

  private static ProcessMappingEntityHelper _self = null;

  private ProcessMappingEntityHelper()
  {
    super();
  }

  public static ProcessMappingEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(ProcessMappingEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new ProcessMappingEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity processMapping) throws Exception
  {
    Logger.debug("[ProcessMappingEntityHelper.checkDuplicate] Start");

    ProcessMapping mapping = (ProcessMapping)processMapping;
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcessMapping.PROCESS_DEF,
      filter.getEqualOperator(), mapping.getProcessDef(), false);
    filter.addSingleFilter(filter.getAndConnector(), ProcessMapping.PARTNER_ID,
      filter.getEqualOperator(), mapping.getPartnerID(), false);
    filter.addSingleFilter(filter.getAndConnector(), ProcessMapping.IS_INITIATING_ROLE,
      filter.getEqualOperator(), mapping.isInitiatingRole()?Boolean.TRUE:Boolean.FALSE, false);

    ProcessMappingEntityHandler handler = ProcessMappingEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (ProcessMapping)results.iterator().next();
    }
    return null;
  }
}