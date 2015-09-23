/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPartnerFunctionManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-16     Andrew Hill         Created
 * 2002-09-23     Andrew Hill         cookWorkflowActivity()
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2002-12-20     Andrew Hill         handle port for exit to export wfa
 * 2003-01-15     Andrew Hill         New wfa types & move port selection to exitToPort type
 * 2003-01-16     Andrew Hill         UserProcedure wfas
 * 2003-05-15     Andrew Hill         raiseAlert wfas
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2004-04-01     Daniel D'Cotta      Added support for SUSPEND_ACTIVITY
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.events.partnerfunction.CreatePartnerFunctionEvent;
import com.gridnode.gtas.events.partnerfunction.DeletePartnerFunctionEvent;
import com.gridnode.gtas.events.partnerfunction.GetPartnerFunctionEvent;
import com.gridnode.gtas.events.partnerfunction.GetPartnerFunctionListEvent;
import com.gridnode.gtas.events.partnerfunction.UpdatePartnerFunctionEvent;
import com.gridnode.gtas.model.partnerfunction.IWorkflowActivity;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultPartnerFunctionManager extends DefaultAbstractManager
  implements IGTPartnerFunctionManager
{

  DefaultPartnerFunctionManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_PARTNER_FUNCTION, session);
  }

  private List extractWorkflowActivities(IGTPartnerFunctionEntity pf)
    throws GTClientException
  {
    try
    {
      List activities = (List)pf.getFieldValue(IGTPartnerFunctionEntity.WORKFLOW_ACTIVITIES);
      if(activities != null)
      {
        List returnList = new Vector(activities.size());
        Iterator i = activities.iterator();
        while(i.hasNext())
        {
          IGTWorkflowActivityEntity activity = (IGTWorkflowActivityEntity)i.next();
          if(i == null) throw new java.lang.NullPointerException("Null WorkflowActivity entity found in activity list of " + pf);
          List wfParams = new ArrayList(); //20030515AH
          Integer type = (Integer)activity.getFieldValue(IGTWorkflowActivityEntity.TYPE);
          if(type == null) throw new java.lang.NullPointerException("Null activity type in " + activity);
          wfParams.add(type);
          wfParams.add(activity.getFieldString(IGTWorkflowActivityEntity.DESCRIPTION));
          if(IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type)
              || IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type)
              || IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type) )
          { //20030117AH
            wfParams.addAll( (List)activity.getFieldValue(IGTWorkflowActivityEntity.PARAM_LIST) );
          }
          else if( IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type) )
          { //20030515AH
            List params = (List)activity.getFieldValue(IGTWorkflowActivityEntity.PARAM_LIST);
            if(params != null)
            {
              wfParams.addAll( params );
            }
          }
          
          // 20040401 DDJ: 
          // if suspension is required, then 3 fields will be added to the back of the vector,
          // they are a String constant "SUSPEND_ACTIVITY", a Long, an Integer (bad design!)
          Long dispatchInterval = (Long)activity.getFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL);
          Integer dispatchCount = (Integer)activity.getFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT);
//System.out.println("SUSPEND_ACTIVITY, " + (dispatchInterval != null ? dispatchInterval.toString() : "null") + ", " + (dispatchCount != null ? dispatchCount.toString() : "null"));
          if(dispatchInterval != null && dispatchCount != null)
          {
            if(dispatchInterval.longValue() > 0 && dispatchCount.intValue() > 0)
            {
              wfParams.add("SUSPEND_ACTIVITY");
              wfParams.add(dispatchInterval);
              wfParams.add(dispatchCount);
            }
          }          
          
          returnList.add(wfParams);
        }
        return returnList;
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting workflow activity list from " + pf,t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)entity;
    try
    {
      List workflowActivities = extractWorkflowActivities(pf);
      UpdatePartnerFunctionEvent event = new UpdatePartnerFunctionEvent(
        pf.getUidLong(),
        pf.getFieldString(IGTPartnerFunctionEntity.DESCRIPTION),
        (Integer)pf.getFieldValue(IGTPartnerFunctionEntity.TRIGGER_ON),
        workflowActivities
      );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTPartnerFunctionEntity pf = (IGTPartnerFunctionEntity)entity;
    try
    {
      List workflowActivities = extractWorkflowActivities(pf);
      CreatePartnerFunctionEvent event = new CreatePartnerFunctionEvent(
        pf.getFieldString(IGTPartnerFunctionEntity.ID),
        pf.getFieldString(IGTPartnerFunctionEntity.DESCRIPTION),
        (Integer)pf.getFieldValue(IGTPartnerFunctionEntity.TRIGGER_ON),
        workflowActivities
      );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PARTNER_FUNCTION;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PARTNER_FUNCTION;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPartnerFunctionEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPartnerFunctionListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePartnerFunctionEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PARTNER_FUNCTION.equals(entityType))
    {
      return new DefaultPartnerFunctionEntity();
    }
    else if(IGTEntity.ENTITY_WORKFLOW_ACTIVITY.equals(entityType))
    {
      return new DefaultWorkflowActivityEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_WORKFLOW_ACTIVITY.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[7]; // 20040401 DDJ: Added 2 for suspend fields
      //mappingRuleUids
      {
        sharedVfmi[0] = new VirtualSharedFMI( "workflowActivity.mappingRuleUids",
                                              IGTWorkflowActivityEntity.MAPPING_RULE_UIDS);
        sharedVfmi[0].setCollection(true);
        sharedVfmi[0].setMandatoryCreate(true);
        sharedVfmi[0].setMandatoryUpdate(true);
        sharedVfmi[0].setValueClass("java.util.Collection");
        sharedVfmi[0].setElementClass("java.lang.Long");
        Properties detail = new Properties();
        detail.setProperty("type","foreign");
        detail.setProperty("foreign.key","gridTalkMappingRule.uid");
        detail.setProperty("foreign.display","gridTalkMappingRule.name");
        detail.setProperty("foreign.cached","false");
        IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[0].setConstraint(constraint);
      }

      //portUids
      {
        sharedVfmi[1] = new VirtualSharedFMI( "workflowActivity.portUids",
                                              IGTWorkflowActivityEntity.PORT_UIDS);
        sharedVfmi[1].setCollection(true);
        sharedVfmi[1].setMandatoryCreate(false);
        sharedVfmi[1].setMandatoryUpdate(false);
        sharedVfmi[1].setValueClass("java.util.Collection");
        sharedVfmi[1].setElementClass("java.lang.Long");
        Properties detail = new Properties();
        detail.setProperty("type","foreign");
        detail.setProperty("foreign.key","port.uid");
        detail.setProperty("foreign.display","port.name");
        detail.setProperty("foreign.cached","false");
        IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[1].setConstraint(constraint);
      }

      //userprocedure
      { //20030116AH
        sharedVfmi[2] = new VirtualSharedFMI( "workflowActivity.userProcedureUids",
                                              IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS);
        sharedVfmi[2].setCollection(true);
        sharedVfmi[2].setMandatoryCreate(true);
        sharedVfmi[2].setMandatoryUpdate(true);
        sharedVfmi[2].setValueClass("java.util.Collection");
        sharedVfmi[2].setElementClass("java.lang.Long");
        Properties detail = new Properties();
        detail.setProperty("type","foreign");
        detail.setProperty("foreign.key","userProcedure.uid");
        detail.setProperty("foreign.display","userProcedure.name");
        detail.setProperty("foreign.cached","false");
        IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[2].setConstraint(constraint);
      }

      //alertType
      { //20030116AH
        sharedVfmi[3] = new VirtualSharedFMI( "workflowActivity.alertType",
                                              IGTWorkflowActivityEntity.ALERT_TYPE);
        sharedVfmi[3].setCollection(false);
        sharedVfmi[3].setMandatoryCreate(true);
        sharedVfmi[3].setMandatoryUpdate(true);
        sharedVfmi[3].setValueClass("lava.lang.String");
        Properties detail = new Properties();
        detail.setProperty("type","foreign");
        detail.setProperty("foreign.key","alertType.name");
        detail.setProperty("foreign.display","alertType.description");
        detail.setProperty("foreign.cached","false");
        IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[3].setConstraint(constraint);
      }

      //userDefinedAlert
      { //20030515AH
        sharedVfmi[4] = new VirtualSharedFMI( "workflowActivity.userDefinedAlertUids",
                                              IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS);
        sharedVfmi[4].setCollection(true);
        sharedVfmi[4].setMandatoryCreate(true);
        sharedVfmi[4].setMandatoryUpdate(true);
        sharedVfmi[4].setValueClass("java.util.Collection");
        sharedVfmi[4].setElementClass("java.lang.Long");
        Properties detail = new Properties();
        detail.setProperty("type","foreign");
        detail.setProperty("foreign.key","alert.uid");
        detail.setProperty("foreign.display","alert.name");
        detail.setProperty("foreign.cached","false");
        IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[4].setConstraint(constraint);
      }

      // dispatchInterval
      { // 20040401 DDJ
        sharedVfmi[5] = new VirtualSharedFMI( "workflowActivity.dispatchInterval",
                                              IGTWorkflowActivityEntity.DISPATCH_INTERVAL);
        sharedVfmi[5].setCollection(false);
        sharedVfmi[5].setMandatoryCreate(false);
        sharedVfmi[5].setMandatoryUpdate(false);
        sharedVfmi[5].setValueClass("java.lang.Long");
        Properties detail = new Properties();
        detail.setProperty("type","range");
        detail.setProperty("range.min","1");
        IRangeConstraint constraint = new SingleRangeConstraint(detail, "java.lang.Long");
        sharedVfmi[5].setConstraint(constraint);
      }
      
      // dispatchCount
      { // 20040401 DDJ
        sharedVfmi[6] = new VirtualSharedFMI( "workflowActivity.dispatchCount",
                                              IGTWorkflowActivityEntity.DISPATCH_COUNT);
        sharedVfmi[6].setCollection(false);
        sharedVfmi[6].setMandatoryCreate(false);
        sharedVfmi[6].setMandatoryUpdate(false);
        sharedVfmi[6].setValueClass("java.lang.Integer");
        Properties detail = new Properties();
        detail.setProperty("type","range");
        detail.setProperty("range.min","1");
        IRangeConstraint constraint = new SingleRangeConstraint(detail, "java.lang.Integer");
        sharedVfmi[6].setConstraint(constraint);
      }
      
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_WORKFLOW_ACTIVITY.equals(entityType))
    {
      //List paramList = (List)fieldMap.get(IWorkflowActivity.PARAM_LIST); // 20040401 DDJ
      List unfiltered = (List)fieldMap.get(IWorkflowActivity.PARAM_LIST);
      List paramList = unfiltered;
  
      // if suspension is required, then 3 fields will be added to the back of the vector,
      // they are a String constant "SUSPEND_ACTIVITY", a Long, an Integer (bad design!)
//System.out.println("SUSPEND_ACTIVITY: begin");
      int paramListSize = unfiltered.size();
      if(paramListSize >= 3)
      {
        if("SUSPEND_ACTIVITY".equals(unfiltered.get(paramListSize - 3))) 
        {
          // copy the vector and exclude the last 3 items
          paramList = new Vector(paramListSize - 3);
          for(int i = 0; i < paramListSize - 3; i++)
          {
            paramList.add(unfiltered.get(i));
          }
          
          // set the suspend fields
//System.out.println("SUSPEND_ACTIVITY: unfiltered.get(paramListSize - 2)=" + unfiltered.get(paramListSize - 2).toString());
//System.out.println("SUSPEND_ACTIVITY: unfiltered.get(paramListSize - 1)=" + unfiltered.get(paramListSize - 1).toString());
          //entity.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL, (Long)unfiltered.get(paramListSize - 2));
          //entity.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT, (Integer)unfiltered.get(paramListSize - 1));
          entity.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL, StaticUtils.longValue((String)unfiltered.get(paramListSize - 2))); // 20040402 DDJ
          entity.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT, StaticUtils.integerValue((String)unfiltered.get(paramListSize - 1))); // 20040402 DDJ
        }
      }
//System.out.println("SUSPEND_ACTIVITY: end");

      Integer type = (Integer)fieldMap.get(IWorkflowActivity.ACTIVITY_TYPE);
      if(IWorkflowActivity.MAPPING_RULE.equals(type))
      {
        entity.setNewFieldValue(IGTWorkflowActivityEntity.MAPPING_RULE_UIDS, paramList);
      }
      else if(IWorkflowActivity.USER_PROCEDURE.equals(type))
      {
        entity.setNewFieldValue(IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS, paramList);
      }
      else if(IWorkflowActivity.EXIT_TO_PORT.equals(type)) //20030115AH
      {
        entity.setNewFieldValue(IGTWorkflowActivityEntity.PORT_UIDS, paramList);
      }
      else if(IWorkflowActivity.RAISE_ALERT.equals(type)) //20030515AH
      {
        if( paramList != null && paramList.size() > 0)
        {
          String alertType = (String)paramList.get(0);
          entity.setNewFieldValue(IGTWorkflowActivityEntity.ALERT_TYPE, alertType);
          if(paramList.size() > 1)
          {
            List userDefinedAlerts = paramList.subList(1, paramList.size());
            entity.setNewFieldValue(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS, userDefinedAlerts);
          }
          else
          {
             entity.setNewFieldValue(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS, Collections.EMPTY_LIST);
          }
        }
      }
    }
  }

  public IGTWorkflowActivityEntity newWorkflowActivity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_WORKFLOW_ACTIVITY);
    entity.setNewEntity(true);
    return (IGTWorkflowActivityEntity)entity;
  }

  public List cookWorkflowActivity(IGTWorkflowActivityEntity workflowEntity) throws GTClientException
  {
    //Modified 20021220AH - For ports (which are optional)
    //Modified 20030115AH - New types
    DefaultWorkflowActivityEntity workflow = (DefaultWorkflowActivityEntity)workflowEntity; //20030115AH
    if(workflow == null) throw new java.lang.NullPointerException("null workflow activity entity");
    try
    {
      boolean doCook = false;
      List returnList = null;
      Collection entities = null;
      Number descriptionField = null;
      Integer type = (Integer)workflow.getFieldValue(IGTWorkflowActivityEntity.TYPE);
      if(IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type))
      {
        entities = workflow.getFieldEntities(IGTWorkflowActivityEntity.MAPPING_RULE_UIDS);
        if(entities == null) throw new java.lang.IllegalStateException("No mappingRule entities");
        descriptionField = IGTGridTalkMappingRuleEntity.DESCRIPTION;
        doCook = true;
      }
      else if(IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type))
      { //20030116AH
        entities = workflow.getFieldEntities(IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS);
        if(entities == null) throw new java.lang.IllegalStateException("No userProcedure entities");
        descriptionField = IGTUserProcedureEntity.DESCRIPTION;
        doCook = true;
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type)) //20030115AH
      { //20030115AH
        try
        {
          entities = workflow.getFieldEntities(IGTWorkflowActivityEntity.PORT_UIDS);
          if( (entities == null) || entities.isEmpty() )
          {
            doCook = false;
            workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                      IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_PORT);
          }
          else
          {
            descriptionField = IGTPortEntity.NAME;
            doCook = true;
          }
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error initialising description for exitToPort wfa",t);
        }
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_IMPORT.equals(type))
      { //20030115AH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_IMPORT);
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_OUTBOUND.equals(type))
      { //20030115AH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_OUTBOUND);
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_EXPORT.equals(type))
      { //20030115AH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_EXPORT);
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_WORKFLOW.equals(type))
      { //20030115AH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_EXIT_WORKFLOW);
      }
      else if(IGTWorkflowActivityEntity.TYPE_SAVE_TO_FOLDER.equals(type))
      { //20030115AH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_SAVE_TO_FOLDER);
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_CHANNEL.equals(type))
      { //20030129AMH
        workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                        IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_CHANNEL);
      }
      else if(IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type))
      { //20030515AH
        try
        {
          entities = workflow.getFieldEntities(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS);
          if( (entities == null) || entities.isEmpty() )
          {
            doCook = false;
            workflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                      IGTWorkflowActivityEntity.DESCRIPTION_RAISE_ALERT
                                      + " '"
                                      + workflow.getFieldString(IGTWorkflowActivityEntity.ALERT_TYPE)
                                      + "'");
            //20030519 - As we wont be cooking this one we need to set up the
            //paramList here. (Unique amoung the workflowActivity types, raise_alert
            //has a param for its alertType as well as optional FER params)
            ArrayList paramList = new ArrayList(1);
            paramList.add( workflow.getFieldString(IGTWorkflowActivityEntity.ALERT_TYPE) );
            workflow.setNewFieldValue(IGTWorkflowActivityEntity.PARAM_LIST,paramList);
            //...
          }
          else
          {
            descriptionField = IGTAlertTypeEntity.NAME;
            doCook = true;
            //20030519AH - nb: alertType will be inserted at head of paramList
            //later on in the cooking iteration code
          }
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error initialising params for raiseAlert wfa",t);
        }
      }
      if( (doCook) && (entities != null) && (entities.size() > 0) )
      {
        returnList = new ArrayList(entities.size());
        Iterator i = entities.iterator();
        while(i.hasNext())
        {
          IGTWorkflowActivityEntity roasted
             = (IGTWorkflowActivityEntity)cookWithValue(workflow,descriptionField,(IGTEntity)i.next());
          //uglyhack.begin()
          if(IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(roasted.getFieldValue(IGTWorkflowActivityEntity.TYPE)))
          {
            //20030519AH: Andrew , you really gotta refactor this cooking stuff.
            //It dont taste very good!
            //Unlike the other wfa types that make use of param_list, the
            //raiseAlert one has two hetrogonous values in its one.
            //The first is the alertType, the second is the userDefinedAlertUids
            //(actually there is always only one but we are pretending their
            //code be many). The way the cooking code works when its cooking an
            //entity is to set the paramList with the uids of the entities
            //that are referred to be they mappingRules, ports, or in this case
            //alerts - however in so doing we dont get the alertType - so we
            //hack it in now here.
            String alertType = roasted.getFieldString(IGTWorkflowActivityEntity.ALERT_TYPE);
            List paramList = (List)roasted.getFieldValue(IGTWorkflowActivityEntity.PARAM_LIST);
            paramList.add(0,alertType);
          }
          //uglyhack.end()
          returnList.add(roasted);
        }
      }
      else if(doCook == false)
      {
        returnList = new ArrayList(1);
        returnList.add(workflow);
        return returnList;
      }
      else
      {
        throw new java.lang.IllegalStateException("Expecting paramList values but found none for " + workflow);
      }
      return returnList;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error cooking workflowActivity " + workflow,t);
    }
  }

  private AbstractGTEntity cookWithValue( IGTWorkflowActivityEntity rawEntity,
                                          Number descriptionField,
                                          IGTEntity entity)
    throws GTClientException
  { //this code is getting really ugly.... :-(
    if(rawEntity == null) throw new java.lang.NullPointerException("null rawEntity");
    if(entity == null) throw new java.lang.NullPointerException("null entity");
    try
    {
      Integer type = (Integer)rawEntity.getFieldValue(IGTWorkflowActivityEntity.TYPE);
      AbstractGTEntity cookedWorkflow = (AbstractGTEntity)newWorkflowActivity();
      cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.TYPE,type );
      List paramList = new ArrayList(1);
      paramList.add(entity.getUidLong());
      cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.PARAM_LIST, paramList );

      if(IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type))
      { //20030515AH - Do this early as we want to get at the alertType when setting the description
        cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.ALERT_TYPE, rawEntity.getFieldValue(IGTWorkflowActivityEntity.ALERT_TYPE) );
        cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS,paramList); //20021220AH
      }

      if(descriptionField != null)
      { //20030115AH
        if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type))
        { //special case
          cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                          IGTWorkflowActivityEntity.DESCRIPTION_EXIT_TO_PORT + " '"
                                          + entity.getFieldValue(descriptionField) + "'" );
        }
        else if(IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type))
        { //20030515AH
          cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                      IGTWorkflowActivityEntity.DESCRIPTION_RAISE_ALERT
                                      + " '"
                                      + cookedWorkflow.getFieldString(IGTWorkflowActivityEntity.ALERT_TYPE)
                                      + ":"
                                      + entity.getFieldValue(descriptionField) + "'");
        }
        else
        {
          cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.DESCRIPTION,
                                      entity.getFieldValue(descriptionField) );
        }
      }
      // Make sure the virtual fields on the new cooked workflowActivity entities are kept
      // up to date with appropriate value
      if(IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type))
      {
        cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.MAPPING_RULE_UIDS,paramList);
      }
      else if(IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type))
      { //20030116AH
        cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS,paramList);
      }
      else if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type))
      { //20030115AH
        cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.PORT_UIDS,paramList); //20021220AH
      }

      cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL, (Long)rawEntity.getFieldValue(IGTWorkflowActivityEntity.DISPATCH_INTERVAL) );  // 20040401 DDJ
      cookedWorkflow.setNewFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT, (Integer)rawEntity.getFieldValue(IGTWorkflowActivityEntity.DISPATCH_COUNT) );     // 20040401 DDJ

      return cookedWorkflow;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error cooking workflow activity " + rawEntity
                                  + " of type " + rawEntity.getFieldString(IGTWorkflowActivityEntity.TYPE)
                                  + " with foreign entity=" + entity,t);
    }
  }
}