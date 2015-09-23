/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.log.Log;

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
public abstract class CreateEntityActionTest extends EntityActionTest
{
  public CreateEntityActionTest(String name)
  {
    super(name);
  }

  protected short getDefaultMsgCode()
  {
    return IErrorCode.CREATE_ENTITY_ERROR;
  }

  public void testNormalCreate() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testNormalCreate] Enter ");
    try
    {
      IEntity entity = (IEntity)getTestData(DEFAULT_TESTDATA);
      _response = performEvent(createTestEvent(entity));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testNormalCreate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }
    assertOnNormalExit();
    assertOnNormalCreate();
    Log.log("TEST", "["+getClassName()+".testNormalCreate] Exit ");
  }

  public void testDuplicateCreate() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testDuplicateCreate] Enter ");
    try
    {
      createEntity((IEntity)getTestData(DEFAULT_TESTDATA));
      IEntity entity = (IEntity)getTestData(DEFAULT_TESTDATA);
      _response = performEvent(createTestEvent(entity));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testDuplicateCreate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }
    assertOnFailedExit();
    Log.log("TEST", "["+getClassName()+".testDuplicateCreate] Exit ");
  }

  protected abstract void assertOnNormalCreate() throws Exception;
}