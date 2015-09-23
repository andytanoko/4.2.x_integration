package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.GetPartnerGroupEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.GetEntityActionTest;

import com.gridnode.pdip.app.partner.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.ArrayList;
import java.util.Map;

public class GetPartnerGroupActionTest extends GetEntityActionTest
{
  private static String PARTNER_TYPE       = "DTB";
  private static String PARTNER_TYPE_DESCR = "Distributor";
  private static String NAME               = "LD";
  private static String DESCRIPTION        = "Local Distributor";

  private PartnerGroup _entity = null;

  public GetPartnerGroupActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetPartnerGroupActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "GetPartnerGroupActionTest";
  }

  protected Class getEventClass()
  {
    return GetPartnerGroupEvent.class;
  }

  protected Class getActionClass()
  {
    return GetPartnerGroupAction.class;
  }

  protected IEntityTestHelper getEntityTestHelper()
  {
    if (_helper == null)
      _helper = new PartnerGroupTestHelper();

    return _helper;
  }

  protected void setUp() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".setUp] Enter ");
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
    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected void assertOnReturnData(Object returnObj , IEntity entity)
  {
    _entity = (PartnerGroup)entity;

    assertNotNull("Null Response Data", returnObj);
    assertTrue("Incorrect Response Data Type", returnObj instanceof Map);

    Map entityMap = null;

    // assert Partner Group info
    entityMap = (Map)returnObj;
    assertEquals(
      "Mismatch Name",
      _entity.getName(),
      entityMap.get(IPartnerGroup.NAME));

    assertEquals(
      "Mismatch Description",
      _entity.getDescription(),
      entityMap.get(IPartnerGroup.DESCRIPTION));

    // assert embedded Partner Type info
    entityMap = (Map)entityMap.get(IPartnerGroup.PARTNER_TYPE);
    PartnerType partnerType = ((PartnerGroup)entity).getPartnerType();
    assertEquals(
      "Mistmatch Partner Type",
      partnerType.getName(),
      entityMap.get(IPartnerType.NAME));
  }
}