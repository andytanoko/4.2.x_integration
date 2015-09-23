package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.DeletePartnerTypeEvent;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.gtas.server.actions.DeleteEntityActionTest;

import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import java.util.ArrayList;
import junit.framework.*;

public class DeletePartnerTypeActionTest extends DeleteEntityActionTest
{
  // default test data set
  private static String NAME        = "3PL";
  private static String DESCRIPTION = "3rd Party Logistic";

  public DeletePartnerTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeletePartnerTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "DeletePartnerTypeActionTest";
  }

  protected Class getEventClass()
  {
    return DeletePartnerTypeEvent.class;
  }

  protected Class getActionClass()
  {
    return DeletePartnerTypeAction.class;
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
    Log.log("TEST", "["+getClassName()+".setUp] Enter");
  }
}