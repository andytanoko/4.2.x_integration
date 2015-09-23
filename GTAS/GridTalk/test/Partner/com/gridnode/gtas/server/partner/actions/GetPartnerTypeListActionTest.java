package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.GetPartnerTypeListEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.GetEntityListActionTest;

import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.app.partner.model.IPartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;

import java.util.ArrayList;
import java.util.Map;
import junit.framework.*;

public class GetPartnerTypeListActionTest extends GetEntityListActionTest
{
  public GetPartnerTypeListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetPartnerTypeListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "GetPartnerTypeListActionTest";
  }

  protected Class getEventClass()
  {
    return GetPartnerTypeListEvent.class;
  }

  protected Class getActionClass()
  {
    return GetPartnerTypeListAction.class;
  }

  protected DataFilterImpl getListFilter()
  {
    if (_filter == null)
    {
      _filter = new DataFilterImpl();
      _filter.addSingleFilter(null, PartnerType.UID, _filter.getEqualOperator(), null, true);
      _filter.setOrderFields(new Number[] {PartnerType.UID});
    }
    return _filter;
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
    entity.setName("3PL");
    entity.setDescription("3rd Party Logistic");
    putTestData("TESTDATA_SET_1", entity);

    entity = new PartnerType();
    entity.setName("SP");
    entity.setDescription("Supplier");
    putTestData("TESTDATA_SET_2", entity);

    entity = new PartnerType();
    entity.setName("CU");
    entity.setDescription("Customer");
    putTestData("TESTDATA_SET_3", entity);
    Log.log("TEST", "["+getClassName()+".setUp] Enter");
  }

  protected void assertOnListItem(Object returnObj, IEntity entity)
  {
    Log.log("TEST", "["+getClassName()+".assertOnListItem] Enter");
    assertNotNull("Null Response Data", returnObj);
    assertTrue("Incorrect Response Data Type", returnObj instanceof Map);

    Map entityMap = (Map)returnObj;
    Log.log("TEST", "["+getClassName()+".assertOnListItem] " +
            "Partner Type: " + ((PartnerType)entity).getName());


    assertEquals(
      "Mismatch Name",
      ((PartnerType)entity).getName(),
      entityMap.get(IPartnerType.NAME));

    assertEquals(
      "Mismatch Description",
      ((PartnerType)entity).getDescription(),
      entityMap.get(IPartnerType.DESCRIPTION));
    Log.log("TEST", "["+getClassName()+".assertOnListItem] Exit");
  }
}