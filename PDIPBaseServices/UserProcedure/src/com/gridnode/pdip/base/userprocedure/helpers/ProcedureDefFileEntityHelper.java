/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.userprocedure.helpers;

import java.util.Collection;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class contains utitlies methods for the ProcedureDefFile entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ProcedureDefFileEntityHelper implements ICheckConflict
{
  private static ProcedureDefFileEntityHelper _self = null;

  private ProcedureDefFileEntityHelper()
  {
    super();
  }

  public static ProcedureDefFileEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(ProcedureDefFileEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new ProcedureDefFileEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity procedureDefFile)
    throws Exception
  {
    Logger.debug("[ProcedureDefFileEntityHelper.checkDuplicate] Start");
    String name = procedureDefFile.getFieldValue(ProcedureDefFile.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcedureDefFile.NAME, filter.getEqualOperator(),
      name, false);

    ProcedureDefFileEntityHandler handler = ProcedureDefFileEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (ProcedureDefFile)results.iterator().next();
    }
    return null;
  }
}