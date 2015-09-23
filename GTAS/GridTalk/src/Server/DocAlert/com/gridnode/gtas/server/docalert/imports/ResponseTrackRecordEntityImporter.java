/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.docalert.imports;

import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.DefaultEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

/**
 * This class will know how to import a ResponseTrackRecord entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ResponseTrackRecordEntityImporter extends AbstractEntityImporter
{

  private static ResponseTrackRecordEntityImporter _self = null;

  private ResponseTrackRecordEntityImporter()
  {
    super();
  }

  public static ResponseTrackRecordEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(ResponseTrackRecordEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new ResponseTrackRecordEntityImporter();
        }
      }
    }
    return _self;
  }

  public void removeDependency(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[ResponseTrackRecordEntityImporter.removeDependency] Start");
    ResponseTrackRecord track = (ResponseTrackRecord)entityToImport.getEntity();
    Long respTrackUid = (Long)track.getKey();
    Collection results =
      registry.getImportList().getEntity(ReminderAlert.ENTITY_NAME,
                                         ReminderAlert.TRACK_RECORD_UID,
                                         respTrackUid);
    for (Iterator i = results.iterator(); i.hasNext(); )
    {
      ImportEntity reminder = (ImportEntity)i.next();
      registry.getImportList().removeImportEntity(reminder);
    }
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[ResponseTrackRecordEntityImporter.checkFields] Start");

    ResponseTrackRecord track = (ResponseTrackRecord)entityToImport.getEntity();
    Long respTrackUid = (Long)track.getKey();
    ImportEntity conflict = registry.getConflictList().getEntity(
                              ResponseTrackRecord.ENTITY_NAME,
                              respTrackUid);

    if ((conflict != null) && (!conflict.isOverwrite()))
    {

      Logger.debug("[ResponseTrackRecordEntityImporter.checkFields] ResponseTrackRecord in conflict and is not overwrite, so remove reminderalert");
      Collection results =
        registry.getImportList().getEntity(ReminderAlert.ENTITY_NAME,
                                           ReminderAlert.TRACK_RECORD_UID,
                                           respTrackUid);
      for (Iterator i = results.iterator(); i.hasNext(); )
      {
        ImportEntity reminder = (ImportEntity)i.next();
        registry.getImportList().removeImportEntity(reminder);
      }
    }

    return DefaultEntityImporter.getInstance().checkFields(entityToImport,
                                                           registry);
  }

}