/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 21 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
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
 * @version 2.1
 * @since 2.1
 */
public class AlertTriggerDAOHelper
{
  private static AlertTriggerDAOHelper _self = null;
  private static Object                  _lock = new Object();

  private AlertTriggerDAOHelper()
  {

  }

  public static AlertTriggerDAOHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new AlertTriggerDAOHelper();
      }
    }
    return _self;
  }

  /**
   * Check if the specified AlertTrigger will result in duplicate when
   * created or updated.
   *
   * @param trigger The AlertTrigger to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the trigger itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * AlertTrigger will result in duplicates.
   */
  public void checkDuplicate(
    AlertTrigger trigger, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();

    filter.addSingleFilter(null, AlertTrigger.LEVEL, filter.getEqualOperator(),
                           trigger.getFieldValue(AlertTrigger.LEVEL), false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.ALERT_TYPE,
                           filter.getEqualOperator(), trigger.getAlertType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.DOC_TYPE,
                           filter.getEqualOperator(), trigger.getDocumentType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_TYPE,
                           filter.getEqualOperator(), trigger.getPartnerType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_GROUP,
                           filter.getEqualOperator(), trigger.getPartnerGroup(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_ID,
                           filter.getEqualOperator(), trigger.getPartnerId(),
                           false);

    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.UID,
        filter.getNotEqualOperator(), trigger.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "Alert Trigger ["+ trigger.getEntityDescr() + "] already exists!");
  }

  /**
   * Check whether a AlertTrigger can be deleted.
   *
   * @param trigger The AlertTrigger to check.
   *
   * @exception ApplicationException The AlertTrigger is not allowed to be
   * deleted.
   */
  public void checkCanDelete(AlertTrigger trigger) throws Exception
  {
    if (!trigger.canDelete())
      throw new ApplicationException("AlertTrigger not allowed to be deleted!");
  }

  private IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(AlertTrigger.ENTITY_NAME);
  }


}