package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.UpdatePartnerGroupEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.UpdateEntityActionTest;

import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.ArrayList;

public class UpdatePartnerGroupActionTest extends UpdateEntityActionTest
{
  private static String PARTNER_TYPE       = "DTB";
  private static String PARTNER_TYPE_DESCR = "Distributor";
  private static String NAME               = "LD";
  private static String DESCRIPTION        = "Local Distributor";
  private static String UPD_NAME           = "OD";
  private static String UPD_DESCRIPTION    = "Oversea Distributor";

  private PartnerGroup _entity = null;

  public UpdatePartnerGroupActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdatePartnerGroupActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "UpdatePartnerGroupActionTest";
  }

  protected Class getEventClass()
  {
    return UpdatePartnerGroupEvent.class;
  }

  protected Class getActionClass()
  {
    return UpdatePartnerGroupAction.class;
  }

  protected IEntityTestHelper getEntityTestHelper()
  {
    if (_helper == null)
      _helper = new PartnerGroupTestHelper();

    return _helper;
  }

  protected void setUp() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".setup] Enter ");
    // do all your test environment initialisation here ....

    /**@todo initialise test data from test input file */
    PartnerGroupTestHelper helper = (PartnerGroupTestHelper)getEntityTestHelper();

    // prepare dependent embedded entity
    PartnerType partnerType = new PartnerType();
    partnerType.setName(PARTNER_TYPE);
    partnerType.setDescription(PARTNER_TYPE_DESCR);
    partnerType = helper.createPartnerType(partnerType);

    // initialise default test data in test data set
    PartnerGroup entity = null;
    entity = new PartnerGroup();
    entity.setName(NAME);
    entity.setDescription(DESCRIPTION);
    entity.setPartnerType(partnerType);
    putTestData(DEFAULT_TESTDATA, entity);

    // initialise update test data in test data set
    entity = new PartnerGroup();
    entity.setName(UPD_NAME);
    entity.setDescription(UPD_DESCRIPTION);
    entity.setPartnerType(partnerType);
    putTestData(UPDATE_TESTDATA, entity);

    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    _entity = (PartnerGroup)entity;
    return new UpdatePartnerGroupEvent(
                 new Long(_entity.getUId()),
                 _entity.getName(),
                 _entity.getDescription());
  }

  protected void assertOnNormalUpdate() throws Exception
  {
    PartnerGroupTestHelper helper = (PartnerGroupTestHelper)getEntityTestHelper();
    PartnerGroup entity = (PartnerGroup)helper.getByName(UPD_NAME);
    assertNotNull("Null Record", entity);
    assertEquals("Mismatch Name", UPD_NAME, entity.getName());
    assertEquals("Mismatch Description", UPD_DESCRIPTION, entity.getDescription());

    // assert Partner Type info
    PartnerType partnerType = entity.getPartnerType();
    assertEquals("Mismatch Partner Type", PARTNER_TYPE, partnerType.getName());
  }
}