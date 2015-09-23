/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 07 2003    Koh Han Sing        Created
 * Feb 28 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.exports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.pdip.app.alert.helpers.AlertActionEntityHandler;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.base.exportconfig.exception.ExportConfigException;
import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.DefaultEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This class will know how to export a Alert entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class AlertEntityExporter extends AbstractEntityExporter
{
  private static AlertEntityExporter _self = null;

  private AlertEntityExporter()
  {
    super();
  }

  public static AlertEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new AlertEntityExporter();
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
    try
    {
      AlertLogger.debugLog("AlertEntityExporter", "getAllForeignEntities",
        "Find all foreign entities for Alert : "+entity.getEntityDescr());

      Alert alert = (Alert)entity;
      Long alertUid = (Long)alert.getKey();
      Collection currBindings =
        AlertActionEntityHandler.getInstance().getAlertActionsByAlertUid(alertUid);
      ArrayList<Long> actionUids = new ArrayList<Long>();
      for (Iterator i=currBindings.iterator(); i.hasNext(); )
      {
        AlertAction alertAction = (AlertAction)i.next();
        actionUids.add(alertAction.getActionUid());
      }
      alert.setBindActions(actionUids);
      return DefaultEntityExporter.getInstance().getAllForeignEntities(alert, registry);
    }
    catch (Throwable ex)
    {
      throw new ExportConfigException("Error exporting alert "+entity.getEntityDescr());
    }
  }

}