/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public abstract class GetEntityActionTest extends EntityActionTest
{
  public GetEntityActionTest(String name)
  {
    super(name);
  }

  protected short getDefaultMsgCode()
  {
    return IErrorCode.FIND_ENTITY_BY_KEY_ERROR;
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    // no special handling requires here, simply return null
    return null;
  }

  public void testGetByUID() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testGetByUID] Enter ");
    try
    {
      IEntity entity = createEntity((IEntity)getTestData(DEFAULT_TESTDATA));
      Object[] params = new Object[] {entity.getKey()};
      _response = performEvent(createTestEvent(getEventClass(), params));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testGetByUID] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnReturnData(
      (Map)_response.getReturnData(),
      (IEntity)getTestData(DEFAULT_TESTDATA));
    Log.log("TEST", "["+getClassName()+".testGetByUID] Exit ");
  }

//  public void testGetNonExisting() throws Exception
//  {
//    Log.log("TEST", "["+getClassName()+".testGetNonExisting] Enter ");
//    try
//    {
//      Object[] params = new Object[] {NON_EXISTING_UID};
//      _response = performEvent(createTestEvent(getEventClass(), params));
//    }
//    catch (Exception ex)
//    {
//      Log.err("TEST", "["+getClassName()+".testGetNonExisting] Error Exit ", ex);
//      assertTrue("Event Exception", false);
//    }
//    assertOnFailedExit();
//    Log.log("TEST", "["+getClassName()+".testGetNonExisting] Exit ");
//  }

  protected abstract void assertOnReturnData(Object returnObj , IEntity entity);
}