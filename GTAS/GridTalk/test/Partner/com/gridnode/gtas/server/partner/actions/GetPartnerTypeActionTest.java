package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.GetPartnerTypeEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.GetEntityActionTest;

import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.app.partner.model.IPartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;

import java.util.ArrayList;
import java.util.Map;
import junit.framework.*;

public class GetPartnerTypeActionTest extends GetEntityActionTest
{
  private static String NAME        = "3PL";
  private static String DESCRIPTION = "3rd Party Logistic";

  private PartnerType _entity = null;

  public GetPartnerTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetPartnerTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "GetPartnerTypeActionTest";
  }

  protected Class getEventClass()
  {
    return GetPartnerTypeEvent.class;
  }

  protected Class getActionClass()
  {
    return GetPartnerTypeAction.class;
  }

  protected IEntityTestHelper getEntityTestHelper()
  {
    if (_helper == null)
      _helper = new PartnerTypeTestHelper();
    return _helper;
  }

  protected void setUp() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".setUp] Enter");
    // do all your initialisation here ....

    PartnerType entity = null;

    // initialise default test data in test data set
    entity = new PartnerType();
    entity.setName(NAME);
    entity.setDescription(DESCRIPTION);
    putTestData(DEFAULT_TESTDATA, entity);
    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected void assertOnReturnData(Object returnObj , IEntity entity)
  {
    _entity = (PartnerType)entity;

    assertNotNull("Null Response Data", returnObj);
    assertTrue("Incorrect Response Data Type", returnObj instanceof Map);

    Map entityMap = (Map)returnObj;
    assertEquals(
      "Mismatch Name",
      _entity.getName(),
      entityMap.get(IPartnerType.NAME));

    assertEquals(
      "Mismatch Description",
      _entity.getDescription(),
      entityMap.get(IPartnerType.DESCRIPTION));
  }
}