/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 05 2003    Koh Han Sing        Created
 * Mar 03 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.imports;

import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.AlertEntityHandler;
import com.gridnode.pdip.app.alert.helpers.AlertActionEntityHandler;

import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.util.Collection;
import java.util.Properties;

/**
 * This class will know how to import a Alert entity.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.1 I1
 */

public class AlertEntityImporter extends AbstractEntityImporter
{

  private static AlertEntityImporter _self = null;

  private AlertEntityImporter()
  {
    super();
  }

  public static AlertEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new AlertEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    AlertLogger.debugLog("AlertEntityImporter", "checkFields", "Start");
    boolean noUnlinkForeignEntities = true;

    Alert alert = (Alert)entityToImport.getEntity();
    String entityName = alert.getEntityName();
    AlertLogger.debugLog("AlertEntityImporter", "checkFields", "entityName = "+entityName);
    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      boolean isForeignFieldLinked = true;
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type = properties.getProperty("type");
      if (type != null)
      {
        if (type.equals("foreign"))
        {
          isForeignFieldLinked = checkForeignEntity(entityToImport,
                                                    fMetaInfo,
                                                    registry,
                                                    properties);
        }
        else if (type.equals("dynamic") || type.equals("embedded"))
        {
          isForeignFieldLinked = checkEmbeddedEntity(entityToImport,
                                                     fMetaInfo,
                                                     registry);
        }

        if (!isForeignFieldLinked)
        {
          noUnlinkForeignEntities = false;
        }
      }
    }

    if (noUnlinkForeignEntities)
    {
      AlertLogger.debugLog("AlertEntityImporter", "checkFields", "noUnlinkForeignEntities");
      entityToImport.updateEntity();
      try
      {
        if (entityToImport.getNewUid() != null)
        {
          Long uidToOverwrite = entityToImport.getNewUid();
          AlertLogger.debugLog("AlertEntityImporter", "checkFields",
                               "uidToOverwrite ="+uidToOverwrite);
          Collection<AlertAction> currBindings =
            AlertActionEntityHandler.getInstance().getAlertActionsByAlertUid(
                                                     uidToOverwrite);
          unbindAlertActions(currBindings);
          IEntity oldALert =
            AlertEntityHandler.getInstance().getEntityByKey(uidToOverwrite);
          double oldVersion = oldALert.getVersion();
          AlertLogger.debugLog("AlertEntityImporter", "checkFields",
                               "oldVersion ="+oldVersion);
          alert.setVersion(oldVersion);
          alert.setFieldValue(Alert.UID, uidToOverwrite);
          AlertEntityHandler.getInstance().update(alert);
          alert = (Alert)AlertEntityHandler.getInstance().getEntityByKey(uidToOverwrite);
        }
        else
        {
          alert = (Alert)AlertEntityHandler.getInstance().createEntity(alert);
        }
      }
      catch (Throwable ex)
      {
        throw new ImportConfigException("Error saving Alert", ex);
      }

      Collection<Long> bindings = alert.getBindActions();
      bindAlertActions((Long)alert.getKey(), bindings);

      ImportEntity persistedEntity = new ImportEntity(
                                           alert,
                                           entityToImport.getOldUid(),
                                           (Long)alert.getKey(),
                                           false,
                                           false);
      registry.getPersistedList().addImportEntity(persistedEntity);
      registry.getImportList().removeImportEntity(entityToImport);
      registry.getConflictList().removeImportEntity(entityToImport);
      return true;
    }
    else
    {
      AlertLogger.debugLog("AlertEntityImporter", "checkFields", " got UnlinkForeignEntities");
    }
    return false;
  }

  private void bindAlertActions(Long alertUid, Collection<Long> actionUids)
    throws Exception
  {
    try
    {
      AlertLogger.debugLog("AlertEntityImporter", "bindAlertActions",
                           "alertUid ="+alertUid);
      if (actionUids != null)
      {
        for (Long actionUid : actionUids) 
        {
          AlertLogger.debugLog("AlertEntityImporter", "bindAlertActions",
                               "actionUid =" + actionUid);
          AlertAction alertAction = new AlertAction();
          alertAction.setActionUid(actionUid);
          alertAction.setAlertUid(alertUid);
          AlertActionEntityHandler.getInstance().createEntity(alertAction);
        }
      }
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException("Error binding alert to alert action for alert with uid "
        +alertUid, ex);
    }
  }

  private void unbindAlertActions(Collection<AlertAction> bindings)
    throws Exception
  {
    try
    {
      for (AlertAction alertAction : bindings)
      {
        AlertActionEntityHandler.getInstance().remove((Long)alertAction.getKey());
      }
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException("Error unbinding alert action", ex);
    }
  }
}
