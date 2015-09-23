/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 * Jul 21 2003    Neo SOk Lay         Add method: getEntityMayNotExist(uid)
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2 I1
 * @since 2.0.2
 */
public abstract class EntityActionTest extends GridTalkActionTest
{
  public static final Long NON_EXISTING_UID = new Long(-99999);
  public static final String DEFAULT_TESTDATA = "Default Test Data";
  public static final String UPDATE_TESTDATA  = "Default Test Data for Update";

  protected IEntityTestHelper _helper;

  public EntityActionTest(String name)
  {
    super(name);
  }

  protected abstract IEntityTestHelper getEntityTestHelper();

  protected abstract IEvent createTestEvent(IEntity entity) throws Exception;

  protected IEntity createEntity(IEntity entity)
  {
    Log.log("TEST", "["+getClassName()+".createEntity] Enter");

    try
    {
      return getEntityTestHelper().create(entity);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".createEntity]", ex);
      assertTrue("Error in createEntity", false);
    }
    Log.log("TEST", "["+getClassName()+".createEntity] Exit");
    return null;
  }

  protected void deleteEntity(Long uID)
  {
    Log.log("TEST", "["+getClassName()+".deleteEntity] Enter");
    try
    {
      getEntityTestHelper().delete(uID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".deleteEntity]", ex);
      assertTrue("Error in deleteEntity", false);
    }
    Log.log("TEST", "["+getClassName()+".deleteEntity] Exit");
  }

  protected IEntity getEntity(Long uID)
  {
    Log.log("TEST", "["+getClassName()+".getEntity] Enter");
    try
    {
      return getEntityTestHelper().get(uID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".getEntity]", ex);
      assertTrue("Error in getEntity", false);
    }
    Log.log("TEST", "["+getClassName()+".getEntity] Exit");
    return null;
  }

  protected Collection getAllEntity()
  {
    Log.log("TEST", "["+getClassName()+".getAllEntity] Enter");
    try
    {
      return getEntityTestHelper().getAll();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".getAllEntity]", ex);
      assertTrue("Error in getAllEntity", false);
    }
    Log.log("TEST", "["+getClassName()+".getAllEntity] Exit");
    return null;
  }

  protected void cleanUp() throws Exception
  {
    getEntityTestHelper().deleteAll();
  }
  
  
  protected IEntity getEntityMayNotExist(Long uID)
  {
    log("getEntityMayNotExist", "Enter");
    IEntity entity = null;
    try
    {
      entity = getEntityTestHelper().get(uID);
    }
    catch (Exception ex)
    {
      log("getEntityMayNotExist", "Entity does not exists - UID: "+uID);
    }
    finally
    {
      log("getEntityMayNotExist", "Exit");
    }
    return entity;
  }
  
}