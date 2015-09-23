/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.docalert.exports;

import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.helpers.ReminderAlertEntityHandler;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.DefaultEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.base.exportconfig.helpers.EntityExportLogic;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class will retrieve the foreign entities used by the ResponseTrackRecord
 * entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ResponseTrackRecordEntityExporter extends AbstractEntityExporter
{
  private static ResponseTrackRecordEntityExporter _self = null;

  private ResponseTrackRecordEntityExporter()
  {
    super();
  }

  public static ResponseTrackRecordEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(ResponseTrackRecordEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new ResponseTrackRecordEntityExporter();
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
  public ExportRegistry getAllForeignEntities(IEntity entity,
    ExportRegistry registry)
    throws Exception
  {
    Logger.debug("[ResponseTrackRecordEntityExporter.getAllForeignEntities] Find all foreign entities for ResponseTrackRecord : "+entity.getEntityDescr());

    EntityExportLogic exportLogic = new EntityExportLogic();
    ResponseTrackRecord resptrack = (ResponseTrackRecord)entity;
    Long resptrackUid = (Long)resptrack.getKey();

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ReminderAlert.TRACK_RECORD_UID,
      filter.getEqualOperator(), resptrackUid, false);
    Collection remAlerts =
      ReminderAlertEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    for (Iterator i = remAlerts.iterator(); i.hasNext(); )
    {
      ReminderAlert remAlert = (ReminderAlert)i.next();
      Logger.debug("[ResponseTrackRecordEntityExporter.getAllForeignEntities] ReminderAlert : "+remAlert.getEntityDescr());
      registry = exportLogic.getAllForeignEntities(remAlert, registry);
      registry = addEntityToForeignList(remAlert, registry);
    }

    return DefaultEntityExporter.getInstance().getAllForeignEntities(resptrack, registry);
  }

}