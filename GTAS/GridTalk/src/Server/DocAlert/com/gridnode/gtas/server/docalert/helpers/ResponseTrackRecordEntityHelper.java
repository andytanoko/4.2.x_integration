/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the ResponseTrackRecord entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ResponseTrackRecordEntityHelper implements ICheckConflict
{

  private static ResponseTrackRecordEntityHelper _self = null;

  private ResponseTrackRecordEntityHelper()
  {
    super();
  }

  public static ResponseTrackRecordEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(ResponseTrackRecordEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new ResponseTrackRecordEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity responseTrackRecord) throws Exception
  {
    Logger.debug("[ResponseTrackRecordEntityHelper.checkDuplicate] Start");
    String sentDocType =
      responseTrackRecord.getFieldValue(ResponseTrackRecord.SENT_DOC_TYPE).toString();
    String responseDocType =
      responseTrackRecord.getFieldValue(ResponseTrackRecord.RESPONSE_DOC_TYPE).toString();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ResponseTrackRecord.SENT_DOC_TYPE,
      filter.getEqualOperator(),
      sentDocType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      ResponseTrackRecord.RESPONSE_DOC_TYPE,
      filter.getEqualOperator(),
      responseDocType,
      false);

    ResponseTrackRecordEntityHandler handler =
      ResponseTrackRecordEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (ResponseTrackRecord)results.iterator().next();
    }
    return null;
  }
}