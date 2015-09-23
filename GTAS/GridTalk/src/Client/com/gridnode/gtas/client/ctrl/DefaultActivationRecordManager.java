/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultActivationRecordManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-15     Andrew Hill         Created
 * 2002-12-26     Andrew Hill         abort(), deny(), approve()
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.activation.*;

class DefaultActivationRecordManager extends DefaultAbstractManager
  implements IGTActivationRecordManager
{
  DefaultActivationRecordManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_ACTIVATION_RECORD, session);
  }

  public void deny(long[] uids) throws GTClientException
  { //20021226AH
    try
    {
      if( (uids == null) || (uids.length == 0) ) return;
      Collection recordUids = StaticCtrlUtils.getLongCollection(uids);
      DenyGridNodeActivationEvent event = new DenyGridNodeActivationEvent(recordUids);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error denying activation requests",t);
    }
  }

  public void abort(long[] uids) throws GTClientException
  { //20021226AH
    try
    {
      if( (uids == null) || (uids.length == 0) ) return;
      Collection recordUids = StaticCtrlUtils.getLongCollection(uids);
      AbortGridNodeActivationEvent event = new AbortGridNodeActivationEvent(recordUids);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error aborting activation requests",t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
      IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)record.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);
      Short doAction = (Short)record.getFieldValue(IGTActivationRecordEntity.DO_ACTION);
      if(IGTActivationRecordEntity.DO_ACTION_REQUEST.equals(doAction))
      {
        throw new java.lang.IllegalStateException("Activation requests where doAction is 'request' may not be updated");
      }
      else if(IGTActivationRecordEntity.DO_ACTION_APPROVE.equals(doAction))
      {
        Collection exchangeBeUids = (Collection)activation.getFieldValue(IGTGridNodeActivationEntity.APPROVER_BE_LIST);
        ApproveGridNodeActivationEvent event = new ApproveGridNodeActivationEvent(record.getUidLong(),exchangeBeUids);
        handleEvent(event);
      }
      else if(IGTActivationRecordEntity.DO_ACTION_DENY.equals(doAction))
      {
        Collection recordUids = new ArrayList(1);
        recordUids.add(record.getUidLong());
        DenyGridNodeActivationEvent event = new DenyGridNodeActivationEvent(recordUids);
        handleEvent(event);
      }
      else if(IGTActivationRecordEntity.DO_ACTION_ABORT.equals(doAction))
      {
        Collection recordUids = new ArrayList(1);
        recordUids.add(record.getUidLong());
        AbortGridNodeActivationEvent event = new AbortGridNodeActivationEvent(recordUids);
        handleEvent(event);
      }
      else
      {
        throw new java.lang.IllegalArgumentException("Invalid value for doAction:" + doAction);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
      IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)record.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);

      Short doAction = (Short)record.getFieldValue(IGTActivationRecordEntity.DO_ACTION);
      if(IGTActivationRecordEntity.DO_ACTION_REQUEST.equals(doAction))
      {
        Integer gridNodeId = (Integer)record.getFieldValue(IGTActivationRecordEntity.GRIDNODE_ID);
        String gridNodeName = record.getFieldString(IGTActivationRecordEntity.GRIDNODE_NAME);
        String activateReason = activation.getFieldString(IGTGridNodeActivationEntity.ACTIVATE_REASON);
        Collection exchangeBeUids = (Collection)activation.getFieldValue(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST);

        SubmitGridNodeActivationEvent event = new SubmitGridNodeActivationEvent(gridNodeId,
                                                                                gridNodeName,
                                                                                activateReason,
                                                                                exchangeBeUids);
        //handleCreateEvent(event, (AbstractGTEntity)entity);
        handleEvent(event); //20021127AH
        // At this point the copy of the entity at trhe p-tier is not uptodate with what gtas has.
        // Code using this IGTEntity type should be aware of this and not make use of the entity object
        // it has been passed to gtas. They will need to work out there own method of obtaining
        // the uptodate record if they need to use it. Nb we wont have a uid ether.
      }
      else
      {
        throw new java.lang.IllegalArgumentException("doAction value " + doAction
                                                      + " may not be used for new activationRecords");
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ACTIVATION_RECORD;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ACTIVATION_RECORD;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetActivationRecordEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetActivationRecordListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  { //20030718AH
    throw new NotApplicableException("No event exists to delete ActivationRecords");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACTIVATION_RECORD.equals(entityType))
    {
      return new DefaultActivationRecordEntity();
    }
    else if(IGTEntity.ENTITY_GRIDNODE_ACTIVATION.equals(entityType))
    {
      return new DefaultGridNodeActivationEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    String entityType = entity.getType();
    if(IGTEntity.ENTITY_ACTIVATION_RECORD.equals(entityType))
    {
      DefaultActivationRecordEntity record = (DefaultActivationRecordEntity)entity;
      record.setNewFieldValue(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_OUTGOING);
      record.setNewFieldValue(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION);
      record.setNewFieldValue(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE);
      record.setNewFieldValue(IGTActivationRecordEntity.DO_ACTION, IGTActivationRecordEntity.DO_ACTION_REQUEST);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACTIVATION_RECORD.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];
      sharedVfmi[0] = new VirtualSharedFMI("activationRecord.doAction",IGTActivationRecordEntity.DO_ACTION);
      Properties detail = new Properties();
      detail.setProperty("type","enum");
      detail.setProperty( "activationRecord.doAction.request",
                          IGTActivationRecordEntity.DO_ACTION_REQUEST.toString());
      detail.setProperty( "activationRecord.doAction.approve",
                          IGTActivationRecordEntity.DO_ACTION_APPROVE.toString());
      detail.setProperty( "activationRecord.doAction.deny",
                          IGTActivationRecordEntity.DO_ACTION_DENY.toString());
      detail.setProperty( "activationRecord.doAction.abort",
                          IGTActivationRecordEntity.DO_ACTION_ABORT.toString());
      IEnumeratedConstraint constraint = new EnumeratedConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);


      sharedVfmi[1] = new VirtualSharedFMI("activationRecord.filterType",IGTActivationRecordEntity.FILTER_TYPE);
      Properties detail1 = new Properties();
      detail1.setProperty("type","enum");
      detail1.setProperty( "activationRecord.filterType.incomingActivation",
                          IGTActivationRecordEntity.FILTER_TYPE_INCOMING_ACTIVATION);
      detail1.setProperty( "activationRecord.filterType.outgoingActivation",
                          IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_ACTIVATION);
      detail1.setProperty( "activationRecord.filterType.incomingDeactivation",
                          IGTActivationRecordEntity.FILTER_TYPE_INCOMING_DEACTIVATION);
      detail1.setProperty( "activationRecord.filterType.outgoingDeactivation",
                          IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_DEACTIVATION);
      detail1.setProperty( "activationRecord.filterType.approved",
                          IGTActivationRecordEntity.FILTER_TYPE_APPROVED);
      detail1.setProperty( "activationRecord.filterType.denied",
                          IGTActivationRecordEntity.FILTER_TYPE_DENIED);
      detail1.setProperty( "activationRecord.filterType.aborted",
                          IGTActivationRecordEntity.FILTER_TYPE_DEACTIVATED);
      IEnumeratedConstraint constraint1 = new EnumeratedConstraint(detail1);
      sharedVfmi[1].setConstraint(constraint1);

      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACTIVATION_RECORD.equals(entityType))
    {
      if(entity.isNewEntity())
      {
        entity.setNewFieldValue(IGTActivationRecordEntity.DO_ACTION,
                                IGTActivationRecordEntity.DO_ACTION_REQUEST);
        entity.setNewFieldValue(IGTActivationRecordEntity.FILTER_TYPE,
                                IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_ACTIVATION);
        entity.setNewFieldValue(IGTActivationRecordEntity.CURRENT_TYPE,
                                IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION);
        entity.setNewFieldValue(IGTActivationRecordEntity.ACT_DIRECTION,
                                IGTActivationRecordEntity.DIRECTION_OUTGOING);
      }
      else
      {
        entity.setNewFieldValue(IGTActivationRecordEntity.DO_ACTION,null);
        entity.setNewFieldValue(IGTActivationRecordEntity.FILTER_TYPE,
                                getFilterType((IGTActivationRecordEntity)entity));
      }
    }
  }

  protected String getFilterType(IGTActivationRecordEntity entity) throws GTClientException
  {
    try
    {
      Short currentType = (Short)entity.getFieldValue(IGTActivationRecordEntity.CURRENT_TYPE);
      if(IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION.equals(currentType))
      {
        Short actDirection = (Short)entity.getFieldValue(IGTActivationRecordEntity.ACT_DIRECTION);
        if(IGTActivationRecordEntity.DIRECTION_INCOMING.equals(actDirection))
        {
          return IGTActivationRecordEntity.FILTER_TYPE_INCOMING_ACTIVATION;
        }
        else if(IGTActivationRecordEntity.DIRECTION_OUTGOING.equals(actDirection))
        {
          return IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_ACTIVATION;
        }
        else
        {
          throw new java.lang.IllegalThreadStateException("Unrecognised value for actDirection:" + actDirection);
        }
      }
      else if(IGTActivationRecordEntity.CURRENT_TYPE_DEACTIVATION.equals(currentType))
      {
        Short deactDirection = (Short)entity.getFieldValue(IGTActivationRecordEntity.DEACT_DIRECTION);
        if(IGTActivationRecordEntity.DIRECTION_INCOMING.equals(deactDirection))
        {
          return IGTActivationRecordEntity.FILTER_TYPE_INCOMING_DEACTIVATION;
        }
        else if(IGTActivationRecordEntity.DIRECTION_OUTGOING.equals(deactDirection))
        {
          return IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_DEACTIVATION;
        }
        else
        {
          throw new java.lang.IllegalThreadStateException("Unrecognised value for deactDirection:" + deactDirection);
        }
      }
      else if(IGTActivationRecordEntity.CURRENT_TYPE_APPROVAL.equals(currentType))
      {
        return IGTActivationRecordEntity.FILTER_TYPE_APPROVED;
      }
      else if(IGTActivationRecordEntity.CURRENT_TYPE_DENIAL.equals(currentType))
      {
        return IGTActivationRecordEntity.FILTER_TYPE_DENIED;
      }
      else if(IGTActivationRecordEntity.CURRENT_TYPE_ABORTION.equals(currentType))
      {
        return IGTActivationRecordEntity.FILTER_TYPE_ABORTED;
      }
      else
      {
        throw new java.lang.IllegalStateException("Unrecognised value for currentType:" + currentType);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining filterType for activationRecord" + entity,t);
    }
  }



//............................................................................................................
//Imitation gtas layer!
  /*public Collection getAll() throws GTClientException //undo when finished testing
  {
    Collection returnList = new Vector();
    try
    {
      ArrayList list = new ArrayList();
      //for(long i=0; i < 12; i++)
      for(long i=0; i < 16; i++)
      {
        list.add(getFakedMap(new Long(i)));
      }

      if(list != null)
      {
        Iterator i=list.iterator();
        while( i.hasNext() )
        { // We iterate through the collection
          Map fieldMap = (Map)i.next();
          if(fieldMap == null)
          { // If one of the elements is null then we throw an exception
            throw new java.lang.NullPointerException("No field map for entity in collection!");
          }
          // Create an entity object to hold the values from the map
          AbstractGTEntity entity = initEntityObjects(getEntityType());
          // Initialise the entity object from the map
          initEntityFields(getEntityType(), entity, fieldMap);
          // And add the entity object to our collection to be returned
          returnList.add(entity);
        }
      }
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling faked get list", ex);
    }
    // Finally we return the collection of entities
    return returnList;
  }

  public IGTEntity getByUid(Long uid) throws GTClientException
  {
    try
    {
      IEvent event = getGetEvent(uid);
      return hackedGetEvent(uid);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error attempting to get entity by uid:" + uid, e);
    }
  }

  protected final AbstractGTEntity hackedGetEvent(Long uid)
    throws GTClientException
  {
    try
    {
      // Pass the event to handleEvent() which should return us a fieldMap
      Map fieldMap = getFakedMap(uid);
      // Create an entity object to house the returned data
      AbstractGTEntity entity = initEntityObjects(getEntityType());
      // Initialise its fields from the fieldMap
      initEntityFields(getEntityType(), entity, fieldMap);
      // Return the initialised entity
      return entity;
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling faked event", ex);
    }
  }

  private HashMap getFakedMap(Long uid) throws GTClientException
  {
    if(uid.longValue() < 2) return getFakedOutgoingMap(uid);
    if(uid.longValue() < 4) return getFakedApprovalMap(uid);
    if(uid.longValue() < 6) return getFakedDenialMap(uid);
    if(uid.longValue() < 8) return getFakedAbortionMap(uid);
    if(uid.longValue() < 10) return getFakedDeactivationMap(uid);
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, null);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, null );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, null );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to submit purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private HashMap getFakedOutgoingMap(Long uid) throws GTClientException
  {
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_OUTGOING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, null);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "Outgoing FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, null );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, null );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to receive purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private HashMap getFakedApprovalMap(Long uid) throws GTClientException
  {
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, null);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "Approved FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, null );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_APPROVAL );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to receive purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    activationMap.put(IGTGridNodeActivationEntity.APPROVER_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private HashMap getFakedDenialMap(Long uid) throws GTClientException
  {
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, null);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "Denied FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, null );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, null );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_DENIAL );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to receive purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    activationMap.put(IGTGridNodeActivationEntity.APPROVER_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private HashMap getFakedAbortionMap(Long uid) throws GTClientException
  {
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "Aborted FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, null );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, null );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_ABORTION );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to receive purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    activationMap.put(IGTGridNodeActivationEntity.APPROVER_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private HashMap getFakedDeactivationMap(Long uid) throws GTClientException
  {
    Random rnd = new Random(uid.longValue());
    HashMap recordMap = new HashMap();
    recordMap.put(IGTActivationRecordEntity.UID, uid );
    recordMap.put(IGTActivationRecordEntity.ACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    recordMap.put(IGTActivationRecordEntity.DEACT_DIRECTION, IGTActivationRecordEntity.DIRECTION_INCOMING);
    Integer gnid = new Integer(uid.intValue());
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_ID, gnid);
    recordMap.put(IGTActivationRecordEntity.GRIDNODE_NAME, "Deactivated FakeNode " + gnid);
    recordMap.put(IGTActivationRecordEntity.DT_REQUESTED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.DT_APPROVED, null );
    recordMap.put(IGTActivationRecordEntity.DT_ABORTED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DENIED, null );
    recordMap.put(IGTActivationRecordEntity.DT_DEACTIVATED, new Date(rnd.nextLong()) );
    recordMap.put(IGTActivationRecordEntity.IS_LATEST, Boolean.TRUE );
    recordMap.put(IGTActivationRecordEntity.CURRENT_TYPE, IGTActivationRecordEntity.CURRENT_TYPE_DEACTIVATION );
    HashMap activationMap = new HashMap();
    activationMap.put(IGTGridNodeActivationEntity.ACTIVATE_REASON, "to receive purchase orders (" + uid + ")");
    Collection requestorBeList = getFakedRequestorBeListMaps();
    activationMap.put(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST, requestorBeList);
    activationMap.put(IGTGridNodeActivationEntity.APPROVER_BE_LIST, requestorBeList);
    recordMap.put(IGTActivationRecordEntity.ACTIVATION_DETAILS, activationMap);
    return recordMap;
  }

  private Collection getFakedRequestorBeListMaps()
  {
    ArrayList results = new ArrayList();

    for(long i=0; i < 4; i++)
    {
      results.add( getFakeBeMap(i) );
    }
    return results;
  }

  private Map getFakeBeMap(long i)
  {
    HashMap beMap = new HashMap();
    beMap.put(IGTBusinessEntityEntity.UID,new Long(i));
    beMap.put(IGTBusinessEntityEntity.ID,"Be" + i);
    beMap.put(IGTBusinessEntityEntity.ENTERPRISE_ID,"666" + i);
    beMap.put(IGTBusinessEntityEntity.DESCRIPTION,"Faked Business Entity " + i);
    beMap.put(IGTBusinessEntityEntity.IS_PARTNER, Boolean.TRUE);
    beMap.put(IGTBusinessEntityEntity.IS_PUBLISHABLE, Boolean.FALSE);
    beMap.put(IGTBusinessEntityEntity.IS_SYNC_TO_SERVER, Boolean.TRUE);
    beMap.put(IGTBusinessEntityEntity.STATE, IGTBusinessEntityEntity.STATE_PENDING);
    HashMap wpMap = new HashMap();
    wpMap.put(IGTWhitePageEntity.UID, new Long(i));
    wpMap.put(IGTWhitePageEntity.BUSINESS_DESC,"Blah");
    beMap.put(IGTBusinessEntityEntity.WHITE_PAGE, wpMap);
    return beMap;
  }*/
}