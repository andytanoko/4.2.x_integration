/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.docalert.imports;

import java.util.Collection;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.helpers.ReminderAlertEntityHandler;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.DefaultEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class will know how to import a ReminderAlert entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ReminderAlertEntityImporter extends AbstractEntityImporter
{

  private static ReminderAlertEntityImporter _self = null;

  private ReminderAlertEntityImporter()
  {
    super();
  }

  public static ReminderAlertEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(ReminderAlertEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new ReminderAlertEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[ReminderAlertEntityImporter.checkFields] Start");

    ReminderAlert remAlert = (ReminderAlert)entityToImport.getEntity();
    Long respTrackUid = remAlert.getTrackRecordUID();

    ImportEntity persisted = registry.getPersistedList().getEntity(
                              ResponseTrackRecord.ENTITY_NAME,
                              respTrackUid);
    if (persisted == null)
    {
      ImportEntity conflict = registry.getConflictList().getEntity(
                                ResponseTrackRecord.ENTITY_NAME,
                                respTrackUid);

      if ((conflict != null) && (!conflict.isOverwrite()))
      {
        Logger.debug("[ReminderAlertEntityImporter.checkFields] ResponseTrackRecord in conflict and is not overwrite, so remove reminderalert");
        registry.getImportList().removeImportEntity(entityToImport);
        return true;
      }
    }

    // Scenario One : If is persisted then must have overwritten the existing
    // ResponseTrackRecord or else the ReminderAlerts would have been removed
    // and will not reach here at all.
    //
    // Scenario Two : If not persisted and overwrite is true also must update
    updateReminderAlert(entityToImport, respTrackUid, remAlert);

    return DefaultEntityImporter.getInstance().checkFields(entityToImport,
                                                           registry);
  }

  private ImportEntity updateReminderAlert(ImportEntity entityToImport,
    Long respTrackUid, ReminderAlert remAlert)
    throws Exception
  {
    entityToImport.setIsOverwrite(true);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ReminderAlert.TRACK_RECORD_UID,
      filter.getEqualOperator(),
      respTrackUid,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      ReminderAlert.DAYS_TO_REMINDER,
      filter.getEqualOperator(),
      new Long(remAlert.getDaysToReminder()),
      false);
    Collection result =
      ReminderAlertEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    if (!result.isEmpty())
    {
      ReminderAlert old = (ReminderAlert)result.iterator().next();
      Long oldUid = (Long)old.getKey();
      entityToImport.setNewUid(oldUid);
    }
    return entityToImport;
  }

}