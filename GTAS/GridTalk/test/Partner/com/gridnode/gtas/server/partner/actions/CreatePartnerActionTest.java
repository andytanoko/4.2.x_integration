package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.CreatePartnerEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.CreateEntityActionTest;

import com.gridnode.pdip.app.partner.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.ArrayList;

public class CreatePartnerActionTest extends CreateEntityActionTest
{
 // default test data set
  private static String PARTNER_ID          = "00001000";
  private static String NAME                = "Malcom Inc.";
  private static String DESCRIPTION         = "Malcom Inc.";
  private static String PARTNER_TYPE        = "DTB";
  private static String PARTNER_TYPE_DESCR  = "Distributor";
  private static String PARTNER_GROUP       = "LD";
  private static String PARTNER_GROUP_DESCR = "Local Distributor";
  private static short  STATE               = Partner.STATE_ENABLED;

  private Partner _entity = null;

  public CreatePartnerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreatePartnerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "CreatePartnerActionTest";
  }

  protected Class getEventClass()
  {
    return CreatePartnerEvent.class;
  }

  protected Class getActionClass()
  {
    return CreatePartnerAction.class;
  }

  protected IEntityTestHelper getEntityTestHelper()
  {
    if (_helper == null)
      _helper = new PartnerTestHelper();

    return _helper;
  }

  protected void setUp() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".setUp] Enter ");
    // do all your test environment initialisation here ....

    /**@todo initialise test data from test input file */
    PartnerTestHelper helper = (PartnerTestHelper)getEntityTestHelper();

    // prepare dependent embedded entity
    PartnerType partnerType = new PartnerType();
    partnerType.setName(PARTNER_TYPE);
    partnerType.setDescription(PARTNER_TYPE_DESCR);
    partnerType = helper.createPartnerType(partnerType);

    PartnerGroup partnerGroup = new PartnerGroup();
    partnerGroup.setName(PARTNER_GROUP);
    partnerGroup.setDescription(PARTNER_GROUP_DESCR);
    partnerGroup.setPartnerType(partnerType);
    partnerGroup = helper.createPartnerGroup(partnerGroup);

    // initialise default test data in test data set
    Partner entity = null;
    entity = new Partner();
    entity.setPartnerID(PARTNER_ID);
    entity.setName(NAME);
    entity.setDescription(DESCRIPTION);
    entity.setPartnerType(partnerType);
    entity.setPartnerGroup(partnerGroup);
    entity.setState(STATE);
    putTestData(DEFAULT_TESTDATA, entity);
    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    _entity = (Partner)entity;
    boolean state = (_entity.getState() == Partner.STATE_ENABLED)
                    ? true : false;

    Log.log("TEST", new Long((_entity.getPartnerType()).getUId()));
    return new CreatePartnerEvent(
                 _entity.getPartnerID(),
                 _entity.getName(),
                 _entity.getDescription(),
                 new Long((_entity.getPartnerType()).getUId()),
                 new Long((_entity.getPartnerGroup()).getUId()),
                 state);
  }

  protected void assertOnNormalCreate() throws Exception
  {
    PartnerTestHelper helper = (PartnerTestHelper)getEntityTestHelper();
    _entity = (Partner)helper.getByPartnerID(PARTNER_ID);
    assertNotNull("Null Record", _entity);
    assertEquals("Mismatch Partner ID", PARTNER_ID, _entity.getPartnerID());
    assertEquals("Mismatch Name", NAME, _entity.getName());
    assertEquals("Mismatch Description", DESCRIPTION, _entity.getDescription());
    assertEquals("Mismatch State", STATE, _entity.getState());

    // assert Partner Type info
    PartnerType partnerType = _entity.getPartnerType();
    assertEquals("Mismatch Partner Type", PARTNER_TYPE, partnerType.getName());

    // assert Partner Group info
    PartnerGroup partnerGroup = _entity.getPartnerGroup();
    assertEquals("Mismatch Partner Group", PARTNER_GROUP, partnerGroup.getName());
  }
}