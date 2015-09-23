/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 02 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.docalert.exports;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.base.exportconfig.helpers.EntityExportLogic;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This class will retrieve the foreign entities used by the ReminderAlert
 * entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ReminderAlertEntityExporter extends AbstractEntityExporter
{
  private static ReminderAlertEntityExporter _self = null;

  private ReminderAlertEntityExporter()
  {
    super();
  }

  public static ReminderAlertEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(ReminderAlertEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new ReminderAlertEntityExporter();
        }
      }
    }
    return _self;
  }

  /**
   * This method returns a collection of foreign entities found in the entity
   * passed it.
   *
   * @param entity The entity whose foreign entities are to be retrieved.
   * @returns an Collection of IEntity. Empty is no foreign entities are found.
   */
  public ExportRegistry getAllForeignEntities(IEntity entity, ExportRegistry registry)
    throws Exception
  {
    Logger.debug("[ReminderAlertEntityExporter.getAllForeignEntities] Find all foreign entities for PartnerFunction : "+entity.getEntityDescr());

    EntityExportLogic exportLogic = new EntityExportLogic();
    ReminderAlert reminderAlert = (ReminderAlert)entity;
    String alertName = reminderAlert.getAlertToRaise();
    IEntity alert = ServiceLookupHelper.getAlertMgr().getAlertByAlertName(alertName);

    if (alert != null)
    {
      Logger.debug("[ReminderAlertEntityExporter.getAllForeignEntities] alert : "+alertName);
      registry = exportLogic.getAllForeignEntities(alert, registry);
      registry = addEntityToForeignList(alert, registry);
    }
    return registry;
  }

}