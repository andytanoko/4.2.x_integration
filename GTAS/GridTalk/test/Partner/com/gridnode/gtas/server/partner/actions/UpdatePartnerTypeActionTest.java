package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.UpdatePartnerTypeEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.UpdateEntityActionTest;

import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import junit.framework.*;

public class UpdatePartnerTypeActionTest extends UpdateEntityActionTest
{
  private static String NAME            = "3PL";
  private static String DESCRIPTION     = "3rd Party Logistic";
  private static String UPD_NAME        = "3PL";
  private static String UPD_DESCRIPTION = "Third Party Logistic";

  private PartnerType _entity = null;

  public UpdatePartnerTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdatePartnerTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "UpdatePartnerTypeActionTest";
  }

  protected Class getEventClass()
  {
    return UpdatePartnerTypeEvent.class;
  }

  protected Class getActionClass()
  {
    return UpdatePartnerTypeAction.class;
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

    // initialise update test data in test data set
    entity = new PartnerType();
    entity.setName(UPD_NAME);
    entity.setDescription(UPD_DESCRIPTION);
    putTestData(UPDATE_TESTDATA, entity);
    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    _entity = (PartnerType)entity;
    return new UpdatePartnerTypeEvent(
                 new Long(_entity.getUId()),
                 _entity.getDescription());
  }

  protected void assertOnNormalUpdate() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".assertOnNormalUpdate] Enter ");
    PartnerTypeTestHelper helper = (PartnerTypeTestHelper)getEntityTestHelper();
    PartnerType entity = (PartnerType)helper.getByName(UPD_NAME);
    assertEquals("Mismatch Name", UPD_NAME, entity.getName());
    assertEquals("Mismatch Description", UPD_DESCRIPTION, entity.getDescription());
  }
}