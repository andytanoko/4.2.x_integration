/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 02 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.exports;

import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;

import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.base.exportconfig.helpers.EntityExportLogic;

import com.gridnode.pdip.framework.db.entity.IEntity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class will serialize a collection of entities into a XML file.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerFunctionEntityExporter extends AbstractEntityExporter
{
  private static PartnerFunctionEntityExporter _self = null;

  private PartnerFunctionEntityExporter()
  {
    super();
  }

  public static PartnerFunctionEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerFunctionEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new PartnerFunctionEntityExporter();
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
    Logger.debug("[PartnerFunctionEntityExporter.getAllForeignEntities] Find all foreign entities for PartnerFunction : "+entity.getEntityDescr());

    EntityExportLogic exportLogic = new EntityExportLogic();
    PartnerFunction pf = (PartnerFunction)entity;
    Collection workflowActivities = pf.getWorkflowActivities();
    for (Iterator i = workflowActivities.iterator(); i.hasNext();)
    {
      String entityName = null;
      Long uid = null;
      WorkflowActivity wf = (WorkflowActivity)i.next();
      Vector paramList = wf.getParamList();
      Integer activityType = wf.getActivityType();
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
        if (paramList.size() > 2)
        {
         uid = Long.valueOf(paramList.get(1).toString());
        }
      }

      if ((entity != null) && (uid != null))
      {
        Logger.debug("[PartnerFunctionEntityExporter.getAllForeignEntities] WorkflowActivity entityName : "+entityName);
        IEntity foreignEntity = exportLogic.getEntity(entityName, uid);
        registry = exportLogic.getAllForeignEntities(foreignEntity, registry);
        registry = addEntityToForeignList(foreignEntity, registry);
      }
    }
    return registry;
  }

}