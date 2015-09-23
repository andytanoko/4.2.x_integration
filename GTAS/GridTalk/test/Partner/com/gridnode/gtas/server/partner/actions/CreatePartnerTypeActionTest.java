package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.CreatePartnerTypeEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.CreateEntityActionTest;

import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import junit.framework.*;
import java.util.ArrayList;

public class CreatePartnerTypeActionTest extends CreateEntityActionTest
{
  // default test data set
  private static String NAME = "3PL";
  private static String DESCRIPTION = "3rd Party Logistic";

  private PartnerType _entity = null;

  public CreatePartnerTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreatePartnerTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "CreatePartnerTypeActionTest";
  }

  protected Class getEventClass()
  {
    return CreatePartnerTypeEvent.class;
  }

  protected Class getActionClass()
  {
    return CreatePartnerTypeAction.class;
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
    /**@todo initialise test data from test input file */

    PartnerType entity = null;

    // initialise default test data in test data set
    entity = new PartnerType();
    entity.setName(NAME);
    entity.setDescription(DESCRIPTION);
    putTestData(DEFAULT_TESTDATA, entity);
    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    _entity = (PartnerType)entity;
    return new CreatePartnerTypeEvent(
                 _entity.getName(),
                 _entity.getDescription());
  }

  protected void assertOnNormalCreate() throws Exception
  {
    PartnerTypeTestHelper helper = (PartnerTypeTestHelper)getEntityTestHelper();
    _entity = (PartnerType)helper.getByName(NAME);
    assertNotNull("Null Record", _entity);
    assertEquals("Mismatch Name", NAME, _entity.getName());
    assertEquals("Mismatch Description", DESCRIPTION, _entity.getDescription());
  }
}