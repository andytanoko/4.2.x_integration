/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultUserManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-20     Neo Sok Lay         Created
 * 2002-05-21     Andrew Hill         Made class & constructor package protected
 * 2002-08-19     Andrew Hill         Virtual BEs field
 * 2002-08-20     Andrew Hill         Virtual Roles field
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2002-11-26     Andrew Hill         getUserBusinessEntities()
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.acl.AddRoleToUserEvent;
import com.gridnode.gtas.events.acl.GetRoleListForUserEvent;
import com.gridnode.gtas.events.acl.RemoveRoleFromUserEvent;
import com.gridnode.gtas.events.enterprise.GetBizEntityListForUserEvent;
import com.gridnode.gtas.events.enterprise.SetBizEntityListForUserEvent;
import com.gridnode.gtas.events.user.ChangeAccountPasswordEvent;
import com.gridnode.gtas.events.user.CreateUserAccountEvent;
import com.gridnode.gtas.events.user.DeleteUserAccountEvent;
import com.gridnode.gtas.events.user.GetUserAccountEvent;
import com.gridnode.gtas.events.user.GetUserAccountListEvent;
import com.gridnode.gtas.events.user.UpdateUserAccountEvent;
import com.gridnode.gtas.model.acl.IRole;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultUserManager extends DefaultAbstractManager implements IGTUserManager
{
  DefaultUserManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_USER, session);
  }

  public void updatePasswordOnly(IGTUserEntity entity, String oldPassword)
    throws GTClientException
  { //20050318AH
    try
    {
      Long uid = (Long)entity.getFieldValue(IGTUserEntity.UID);
      String newPassword = entity.getFieldString(IGTUserEntity.NEW_PASSWORD);
      ChangeAccountPasswordEvent event = new ChangeAccountPasswordEvent(uid,oldPassword,newPassword);
      handleEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Could not update password for user " + entity, e);
    }
  }

  protected void updateBes(IGTEntity entity, Collection bes)
    throws GTClientException
  {
    try
    {
      if(bes == null)
      {
        bes = new Vector();
      }
      SetBizEntityListForUserEvent event = new SetBizEntityListForUserEvent(entity.getUidLong(),bes);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating BE list for user " + entity,t);
    }
  }

  protected void updateRoles(IGTEntity entity, Collection roles)
    throws GTClientException
  {
    try
    {
      if(roles == null)
      {
        roles = new Vector();
      }
      //At the moment we cannot just change the list of user roles to another list via an event
      //but rather have to add and remove individually. This will change in I5, so for now
      //we use the lazy method below. (Earlier (before roles was a vf) I was actually calculating
      //the differences in the dispatch action, but to save time will not do that here)
      //We get a list of the users existing roles, remove the lot, and then go through the
      //list of new roles and add them all. Not very efficient!
      try
      {
        GetRoleListForUserEvent event = new GetRoleListForUserEvent(entity.getUidLong());
        EntityListResponseData results = (EntityListResponseData)handleEvent(event);
        Collection oldRolesMaps = (Collection)results.getEntityList();
        Iterator i = oldRolesMaps.iterator();
        while(i.hasNext())
        {
          Map orm = (Map)i.next();
          Long oldUid = (Long)orm.get(IRole.UID);
          RemoveRoleFromUserEvent rrfuEvent = new RemoveRoleFromUserEvent(entity.getUidLong(),oldUid);
          handleEvent(rrfuEvent);
        }
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error removing all existing roles from user",t);
      }
      try
      {
        Iterator i = roles.iterator();
        while(i.hasNext())
        {
          Long roleUid = (Long)i.next();
          AddRoleToUserEvent d2 = new AddRoleToUserEvent(entity.getUidLong(), roleUid); // gettit? gettit? ARTUE d2? rofl! eh? what? you dont think its funny???? Well. No - I suppose YOU wouldnt. :-(
          handleEvent(d2);
        }
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error adding new roles to user",t);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating BE list for user " + entity,t);
    }
  }

  public void update(IGTEntity entity, IUserAdminOptions options) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      Collection bes = null;
      Collection roles = null;
      boolean doUpdateBes = entity.isFieldDirty(IGTUserEntity.BES);
      if(doUpdateBes)
      { // Grab this field value as it will be lost in the update handling
        bes = (Collection)entity.getFieldValue(IGTUserEntity.BES);
      }
      boolean doUpdateRoles = entity.isFieldDirty(IGTUserEntity.ROLES);
      if(doUpdateRoles)
      {
        roles = (Collection)entity.getFieldValue(IGTUserEntity.ROLES);
      }

      UpdateUserAccountEvent event = new UpdateUserAccountEvent(
                                         (Long)entity.getFieldValue(IGTUserEntity.UID),
                                         entity.getFieldString(IGTUserEntity.USER_NAME),
                                         entity.getFieldString(IGTUserEntity.PHONE),
                                         entity.getFieldString(IGTUserEntity.EMAIL),
                                         entity.getFieldString(IGTUserEntity.PROPERTY),
                                         options.isUnfreezeAccount(),
                                         options.isResetBadLoginAttempts(),
                                         options.getState(),
                                         options.getNewPassword());
      handleUpdateEvent(event, (AbstractGTEntity)entity);
      
      

      if(doUpdateBes)
      {
        updateBes(entity,bes);
      }
      if(doUpdateRoles)
      {
        updateRoles(entity,roles);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

 protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      Collection bes = null;
      Collection roles = null;
      boolean doUpdateBes = entity.isFieldDirty(IGTUserEntity.BES);
      if(doUpdateBes)
      { // Grab this field value as it will be lost in the update handling
        bes = (Collection)entity.getFieldValue(IGTUserEntity.BES);
      }
      boolean doUpdateRoles = entity.isFieldDirty(IGTUserEntity.ROLES);
      if(doUpdateRoles)
      {
        roles = (Collection)entity.getFieldValue(IGTUserEntity.ROLES);
      }
      UpdateUserAccountEvent event = new UpdateUserAccountEvent(
                                         (Long)entity.getFieldValue(IGTUserEntity.UID),
                                         entity.getFieldString(IGTUserEntity.USER_NAME),
                                         entity.getFieldString(IGTUserEntity.PHONE),
                                         entity.getFieldString(IGTUserEntity.EMAIL),
                                         entity.getFieldString(IGTUserEntity.PROPERTY));

      handleUpdateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateBes)
      {
        updateBes(entity,bes);
      }
      if(doUpdateRoles)
      {
        updateRoles(entity,roles);
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
      IGTEntity profile = (IGTEntity)entity.getFieldValue(IGTUserEntity.PROFILE);
      Short state = (Short)profile.getFieldValue(IGTAccountStateEntity.STATE);
      boolean enabled = (state==null || state.equals(IGTAccountStateEntity.STATE_ENABLED));

      Collection bes = null;
      Collection roles = null;
      boolean doUpdateBes = entity.isFieldDirty(IGTUserEntity.BES);
      if(doUpdateBes)
      { // Grab this field value as it will be lost in the update handling
        bes = (Collection)entity.getFieldValue(IGTUserEntity.BES);
      }
      boolean doUpdateRoles = entity.isFieldDirty(IGTUserEntity.ROLES);
      if(doUpdateRoles)
      {
        roles = (Collection)entity.getFieldValue(IGTUserEntity.ROLES);
      }

      CreateUserAccountEvent event = new CreateUserAccountEvent(
                                         entity.getFieldString(IGTUserEntity.USER_ID),
                                         entity.getFieldString(IGTUserEntity.USER_NAME),
                                         entity.getFieldString(IGTUserEntity.PASSWORD),
                                         entity.getFieldString(IGTUserEntity.PHONE),
                                         entity.getFieldString(IGTUserEntity.EMAIL),
                                         entity.getFieldString(IGTUserEntity.PROPERTY),
                                         enabled);
      handleCreateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateBes)
      {
        updateBes(entity,bes);
      }
      if(doUpdateRoles)
      {
        updateRoles(entity,roles);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create user", e);
    }
  }

  public IGTUserEntity getUserByID(String userId) throws GTClientException
  {
    try
    {
      GetUserAccountEvent event = new GetUserAccountEvent(userId);
      return (IGTUserEntity)handleGetEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to get user by UserId", e);
    }
  }

  /**
   * Convenience method
   */
  public IGTUserEntity getUserByUID(long uid) throws GTClientException
  {
    return (IGTUserEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_USER;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_USER;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetUserAccountEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetUserAccountListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteUserAccountEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACCOUNT_STATE.equals(entityType))
    {
      return new DefaultAccountStateEntity();
    }
    return new DefaultUserEntity();
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_USER.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];
      //bes
      sharedVfmi[0] = new VirtualSharedFMI("user.bes", IGTUserEntity.BES);
      sharedVfmi[0].setCollection(true);
      sharedVfmi[0].setValueClass("java.utils.Vector");
      sharedVfmi[0].setElementClass("java.lang.Long");
      Properties detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","businessEntity.uid");
      detail.setProperty("foreign.display","businessEntity.id");
      detail.setProperty("foreign.cached","false");
      IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);

      //roles
      sharedVfmi[1] = new VirtualSharedFMI("user.roles", IGTUserEntity.ROLES);
      sharedVfmi[1].setCollection(true);
      sharedVfmi[1].setValueClass("java.utils.Vector");
      sharedVfmi[1].setElementClass("java.lang.Long");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","role.uid");
      detail.setProperty("foreign.display","role.role");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[1].setConstraint(constraint);
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTUserEntity.ROLES, new UnloadedFieldToken());
    entity.setNewFieldValue(IGTUserEntity.BES, new UnloadedFieldToken());
  }

  public Collection getUserBusinessEntities(long userUid)
    throws GTClientException
  {
    return getUserBusinessEntities( new Long(userUid) );
  }

  public Collection getUserBusinessEntities(Long userUid)
    throws GTClientException
  {
    try
    {
      if(userUid == null)
      {
        throw new java.lang.NullPointerException("null userUid");
      }
      GetBizEntityListForUserEvent event = new GetBizEntityListForUserEvent(userUid);
      DefaultBusinessEntityManager beMgr = (DefaultBusinessEntityManager)
                                            _session.getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
      Collection bes = beMgr.handleGetListEvent(event);
      if(bes == null) return new ArrayList();
      return bes;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting BusinessEntities for user with uid=" + userUid,t);
    }
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      if(IGTUserEntity.BES.equals(fieldId))
      {
        if(entity.isNewEntity())
        {
          entity.setNewFieldValue(IGTUserEntity.BES, null);
        }
        else
        {
          IForeignEntityConstraint constraint = (IForeignEntityConstraint)
                                                getSharedFieldMetaInfo(IGTEntity.ENTITY_USER,
                                                fieldId).getConstraint();
          Collection bes = getUserBusinessEntities(entity.getUidLong());
          bes = extractKeys(constraint,bes);
          entity.setNewFieldValue(IGTUserEntity.BES, bes);
        }
      }
      else if(IGTUserEntity.ROLES.equals(fieldId))
      {
        IForeignEntityConstraint constraint = (IForeignEntityConstraint)
                                                getSharedFieldMetaInfo(IGTEntity.ENTITY_USER,
                                                fieldId).getConstraint();

        GetRoleListForUserEvent event = new GetRoleListForUserEvent(entity.getUidLong());
        EntityListResponseData results = (EntityListResponseData)handleEvent(event);
        Collection roles = (Collection)results.getEntityList();
        roles = processMapCollection(constraint,roles);
        roles = extractKeys(constraint,roles);
        entity.setNewFieldValue(IGTUserEntity.ROLES, roles);
      }
      else
      {
        throw new java.lang.IllegalStateException("Field " + fieldId + " of entity " + entity
                                                  + " is not load-on-demand");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
    }
  }
}