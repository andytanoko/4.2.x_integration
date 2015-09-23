/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 30 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
 

/**
 * Helper for DAO level checking.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ResponseTrackRecordDAOHelper
{
  private static ResponseTrackRecordDAOHelper _self = null;
  private static Object                  _lock = new Object();

  private ResponseTrackRecordDAOHelper()
  {

  }

  public static ResponseTrackRecordDAOHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new ResponseTrackRecordDAOHelper();
      }
    }
    return _self;
  }

  /**
   * Check if the specified ResponseTrackRecord will result in duplicate when
   * created or updated.
   *
   * @param record The ResponseTrackRecord to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the record itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * ResponseTrackRecord will result in duplicates.
   */
  public void checkDuplicate(
    ResponseTrackRecord record, boolean checkKey) throws Exception
  {
    checkDuplicate(record, ResponseTrackRecord.SENT_DOC_TYPE, checkKey);
    checkDuplicate(record, ResponseTrackRecord.RESPONSE_DOC_TYPE, checkKey);
  }

  private void checkDuplicate(
    ResponseTrackRecord record,
    Number fieldId,
    boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, fieldId,
      filter.getEqualOperator(), record.getFieldValue(fieldId), false);

    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), ResponseTrackRecord.UID,
        filter.getNotEqualOperator(), record.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "ResponseTrackRecord ["+ record.getEntityDescr() + "] duplicate in field "+
        fieldId +"!");
  }

  /**
   * Check whether a ResponseTrackRecord can be deleted.
   *
   * @param mapping The ResponseTrackRecord to check.
   *
   * @exception ApplicationException The ResponseTrackRecord is not allowed to be
   * deleted.
   */
//  public void checkCanDelete(ResponseTrackRecord record) throws Exception
//  {
//    if (!record.canDelete())
//      throw new ApplicationException("ResponseTrackRecord not allowed to be deleted!");
//  }

  private IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ResponseTrackRecord.ENTITY_NAME);
  }


}