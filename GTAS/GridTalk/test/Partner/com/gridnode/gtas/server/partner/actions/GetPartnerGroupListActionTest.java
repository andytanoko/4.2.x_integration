package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.GetPartnerGroupListEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.GetEntityListActionTest;

import com.gridnode.pdip.app.partner.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.ArrayList;
import java.util.Map;

public class GetPartnerGroupListActionTest extends GetEntityListActionTest
{
  public GetPartnerGroupListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetPartnerGroupListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "GetPartnerGroupListActionTest";
  }

  protected Class getEventClass()
  {
    return GetPartnerGroupListEvent.class;
  }

  protected Class getActionClass()
  {
    return GetPartnerGroupListAction.class;
  }

  protected DataFilterImpl getListFilter()
  {
    if (_filter == null)
    {
      _filter = new DataFilterImpl();
      _filter.addSingleFilter(null, PartnerGroup.UID, _filter.getEqualOperator(), null, true);
      _filter.setOrderFields(new Number[] {PartnerGroup.UID});
    }
    return _filter;
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
    partnerType.setName("SP");
    partnerType.setDescription("Supplier");
    partnerType = helper.createPartnerType(partnerType);

    // initialise default test data in test data set
    PartnerGroup entity = null;
    entity = new PartnerGroup();
    entity.setName("LSP");
    entity.setDescription("Local Supplier");
    entity.setPartnerType(partnerType);
    putTestData("TESTDATA_SET_1", entity);

    entity = new PartnerGroup();
    entity.setName("OSP");
    entity.setDescription("Oversea Supplier");
    entity.setPartnerType(partnerType);
    putTestData("TESTDATA_SET_2", entity);

    // another set of embedded entity
    partnerType = new PartnerType();
    partnerType.setName("DTB");
    partnerType.setDescription("Distributor");
    partnerType = helper.createPartnerType(partnerType);

    entity = new PartnerGroup();
    entity.setName("LD");
    entity.setDescription("Local Distributor");
    entity.setPartnerType(partnerType);
    putTestData("TESTDATA_SET_3", entity);

    entity = new PartnerGroup();
    entity.setName("OD");
    entity.setDescription("Oversea Distributor");
    entity.setPartnerType(partnerType);
    putTestData("TESTDATA_SET_4", entity);

    Log.log("TEST", "["+getClassName()+".setUp] Exit");
  }

  protected void assertOnListItem(Object returnObj, IEntity entity)
  {
    assertNotNull("Null Response Data", returnObj);
    assertTrue("Incorrect Response Data Type", returnObj instanceof Map);

    Log.log("TEST", "["+getClassName()+".assertOnListItem] " +
            "Partner Group: " + ((PartnerGroup)entity).getName());

    Map entityMap = null;

    // assert Partner Group info
    entityMap = (Map)returnObj;
    assertEquals(
      "Mismatch Name",
      ((PartnerGroup)entity).getName(),
      entityMap.get(IPartnerGroup.NAME));

    assertEquals(
      "Mismatch Description",
      ((PartnerGroup)entity).getDescription(),
      entityMap.get(IPartnerGroup.DESCRIPTION));

    // assert embedded Partner Type info
    entityMap = (Map)entityMap.get(IPartnerGroup.PARTNER_TYPE);
    PartnerType partnerType = ((PartnerGroup)entity).getPartnerType();
    assertEquals(
      "Mismatch Partner Type",
      partnerType.getName(),
      entityMap.get(IPartnerType.NAME));
  }
}