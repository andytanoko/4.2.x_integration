/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateEntityActionTest.java
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
import com.gridnode.pdip.framework.rpf.event.EventException;

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
public abstract class UpdateEntityActionTest extends EntityActionTest
{
  public UpdateEntityActionTest(String name)
  {
    super(name);
  }

  protected short getDefaultMsgCode()
  {
    return IErrorCode.UPDATE_ENTITY_ERROR;
  }

  public void testNormalUpdate() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testNormalUpdate] Enter ");
    try
    {
      IEntity createdEntity = createEntity((IEntity)getTestData(DEFAULT_TESTDATA));
      IEntity entity = (IEntity)getTestData(UPDATE_TESTDATA);
      entity.setFieldValue(entity.getKeyId(),createdEntity.getKey());
      _response = performEvent(createTestEvent(entity));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testNormalUpdate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnNormalUpdate();
    Log.log("TEST", "["+getClassName()+".testNormalUpdate] Exit ");
  }

  public void testUpdateNonExisting() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testUpdateNonExisting] Enter ");
    try
    {
      IEntity entity = (IEntity)getTestData(UPDATE_TESTDATA);
      entity.setFieldValue(entity.getKeyId(), NON_EXISTING_UID);
      _response = performEvent(createTestEvent(entity));
    }
    catch (EventException ex)
    {
      Log.log("TEST", "["+getClassName()+".testUpdateNonExisting]" +
        " Returning fail due to EventException: "+ ex.getMessage());
      Log.log("TEST", "["+getClassName()+".testUpdateNonExisting] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testUpdateNonExisting] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnFailedExit();
    Log.log("TEST", "["+getClassName()+".testUpdateNonExisting] Exit ");
  }

  protected abstract void assertOnNormalUpdate() throws Exception;
}