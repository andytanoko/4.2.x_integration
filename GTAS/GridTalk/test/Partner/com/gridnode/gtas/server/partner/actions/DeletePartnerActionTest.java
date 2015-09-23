package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.DeletePartnerEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.DeleteEntityActionTest;

import com.gridnode.pdip.app.partner.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.ArrayList;

public class DeletePartnerActionTest extends DeleteEntityActionTest
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

  public DeletePartnerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeletePartnerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "DeletePartnerActionTest";
  }

  protected Class getEventClass()
  {
    return DeletePartnerEvent.class;
  }

  protected Class getActionClass()
  {
    return DeletePartnerAction.class;
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

  protected void assertOnDeleteByUID() throws Exception
  {
    // mark delete only, so record should still exists
    assertEquals("Fail on Delete by UID", 1, getAllEntity().size());
  }
}