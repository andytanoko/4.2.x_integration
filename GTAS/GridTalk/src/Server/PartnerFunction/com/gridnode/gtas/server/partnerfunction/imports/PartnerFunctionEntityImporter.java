/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 04 2003    Koh Han Sing        Created
 * Feb 27 2004    Jagadeesh           Modified:GNDB00016828 processXXX methods to
 *                                    use loaded entity, in place of entitytoImport.
 */
package com.gridnode.gtas.server.partnerfunction.imports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

/**
 * This class will know how to import a entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerFunctionEntityImporter extends AbstractEntityImporter
{

  private static PartnerFunctionEntityImporter _self = null;

  private PartnerFunctionEntityImporter()
  {
    super();
  }

  public static PartnerFunctionEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerFunctionEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new PartnerFunctionEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[PartnerFunctionEntityImporter.checkFields] Start");
    boolean noUnlinkForeignEntities = true;
    PartnerFunction pf = (PartnerFunction)entityToImport.getEntity();
    ArrayList wfs = pf.getWorkflowActivities();
    Logger.debug("[PartnerFunctionEntityImporter.checkFields] wfs.size ="+wfs.size());
    ArrayList newWfs = new ArrayList();
    for (Iterator i = wfs.iterator(); i.hasNext(); )
    {
      boolean foreignEntityLinked = false;
      String entityName = null;
      Long uid = null;

      WorkflowActivity wf = (WorkflowActivity)i.next();
      WorkflowActivity newWf = new WorkflowActivity();
      Vector paramList = wf.getParamList();
      Integer activityType = wf.getActivityType();
    Logger.debug("[PartnerFunctionEntityImporter.checkFields] activityType ="+activityType);
    Logger.debug("[PartnerFunctionEntityImporter.checkFields] Description ="+wf.getDescription());
      newWf.setActivityType(activityType);
      newWf.setDescription(wf.getDescription());
      if (activityType.equals(WorkflowActivity.EXIT_TO_PORT))
      {
        entityName = "Port";
        uid = Long.valueOf(paramList.get(0).toString());
      }
      else if (activityType.equals(WorkflowActivity.MAPPING_RULE))
      {
        entityName = "GridTalkMappingRule";
        uid = Long.valueOf(paramList.get(0).toString());
      }
      else if (activityType.equals(WorkflowActivity.USER_PROCEDURE))
      {
        entityName = "UserProcedure";
        uid = Long.valueOf(paramList.get(0).toString());
      }
      else if (activityType.equals(WorkflowActivity.RAISE_ALERT))
      {
        entityName = "Alert";
        newWf.addParam(paramList.get(0));
        if (paramList.size() > 2)
        {
         uid = Long.valueOf(paramList.get(1).toString());
        }
      }

      // Process foreign entities used in the workflow activities
      if ((entityName != null) && (uid != null))
      {
        Logger.debug("[PartnerFunctionEntityImporter.checkFields] entityName ="+entityName);
        Logger.debug("[PartnerFunctionEntityImporter.checkFields] uid ="+uid);
        ImportEntity persistedEntity =
          registry.getPersistedList().getEntity(entityName, uid);
        if (persistedEntity != null)
        {
          Logger.debug("[PartnerFunctionEntityImporter.checkFields] persistedEntity != null");
          Long newUid = persistedEntity.getNewUid();
          newWf.addParam(newUid);
          newWfs.add(newWf);
          foreignEntityLinked = true;
        }
        else
        {
          ImportEntity conflictEntity =
             registry.getConflictList().getEntity(entityName, uid);
          if (conflictEntity != null)
          {
            //Logger.debug("[PartnerFunctionEntityImporter.checkFields] ----Entity to Import]"+entityToImport);
            if (!conflictEntity.isOverwrite())
            {
              ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
              registry.getPersistedList().addImportEntity(loadedEntity);
              //Logger.debug("[PartnerFunctionEntityImporter.checkFields] ----Loaded Entity]"+loadedEntity.getEntity());
              registry.getImportList().removeImportEntity(loadedEntity);
              registry.getConflictList().removeImportEntity(loadedEntity);
//              registry.getImportList().removeImportEntity(entityToImport);
//              registry.getConflictList().removeImportEntity(entityToImport);
              Long newUid = loadedEntity.getNewUid();
              newWf.addParam(newUid);
              newWfs.add(newWf);
              foreignEntityLinked = true;
            }
          }
        }
      }
      else
      {
        Logger.debug("[PartnerFunctionEntityImporter.checkFields] entityName == null");
        newWfs.add(newWf);
        foreignEntityLinked = true;
      }

      if (!foreignEntityLinked)
      {
        noUnlinkForeignEntities = false;
      }
    }

    Logger.debug("[PartnerFunctionEntityImporter.checkFields] after wf iteration");

    if (noUnlinkForeignEntities)
    {
      Logger.debug("[PartnerFunctionEntityImporter.checkFields] noUnlinkForeignEntities");
      pf.clearWorkflowActivities();
      pf.clearWorkflowActivityUids();
      for (Iterator i = newWfs.iterator(); i.hasNext(); )
      {
        WorkflowActivity wf = (WorkflowActivity)i.next();
        Long uid = ActionHelper.getManager().createWorkflowActivity(wf);
        pf.addWorkflowActivity(wf);
        pf.addWorkflowActivityUid(uid);
      }

      if (entityToImport.getNewUid() != null)
      {
        // Overwrite existing partner function
        Long uidToOverwrite = entityToImport.getNewUid();
        PartnerFunction oldPf =
          ActionHelper.getManager().findPartnerFunction(uidToOverwrite);
        double version = oldPf.getVersion();
        pf.setVersion(version);
        pf.setFieldValue(PartnerFunction.UID, uidToOverwrite);
        ActionHelper.getManager().updatePartnerFunction(pf);
        pf = ActionHelper.getManager().findPartnerFunction(uidToOverwrite);
      }
      else
      {
        Long newUid = ActionHelper.getManager().createPartnerFunction(pf);
        pf = ActionHelper.getManager().findPartnerFunction(newUid);
      }

      ImportEntity persistedEntity = new ImportEntity(
                                           pf,
                                           entityToImport.getOldUid(),
                                           (Long)pf.getKey(),
                                           false,
                                           false);
      registry.getPersistedList().addImportEntity(persistedEntity);
      registry.getImportList().removeImportEntity(entityToImport);
      registry.getConflictList().removeImportEntity(entityToImport);
      return true;
    }
    else
    {
      Logger.debug("[PartnerFunctionEntityImporter.checkFields] got UnlinkForeignEntities");
    }
    return false;
  }


}