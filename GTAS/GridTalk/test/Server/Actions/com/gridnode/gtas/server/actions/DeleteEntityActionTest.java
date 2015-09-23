/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteEntityActionTest.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public abstract class DeleteEntityActionTest extends EntityActionTest
{
  public DeleteEntityActionTest(String name)
  {
    super(name);
  }

  protected short getDefaultMsgCode()
  {
    return IErrorCode.DELETE_ENTITY_ERROR;
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    // no special handling requires here, simply return null
    return null;
  }

  public void testDeleteByUID() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testDeleteByUID] Enter ");
    try
    {
      IEntity entity = createEntity((IEntity)getTestData(DEFAULT_TESTDATA));
      Object[] params = new Object[] {entity.getKey()};
      _response = performEvent(createTestEvent(getEventClass(), params));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testDeleteByUID] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnDeleteByUID();
    Log.log("TEST", "["+getClassName()+".testDeleteByUID] Exit ");
  }

  public void testDeleteNonExisting() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testDeleteNonExisting] Enter ");
    try
    {
      Object[] params = new Object[] {NON_EXISTING_UID};
      _response = performEvent(createTestEvent(getEventClass(), params));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testDeleteNonExisting] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnFailedExit();
    Log.log("TEST", "["+getClassName()+".testDeleteNonExisting] Exit ");
  }

  protected void assertOnDeleteByUID() throws Exception
  {
    assertEquals("Fail on Delete by UID", 0, getAllEntity().size());
  }
}