package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

public class EntityBeanTest extends RnifTestCase implements IRnifTestConstants
{
  protected IRNProcessDefManagerHome _defMgrHome= null;
  protected IRNProcessDefManagerObj _defMgr= null;
  

  public EntityBeanTest(String name)
  {
    super(name);
  }

  public static IRNProcessDefManagerHome getProcessDefMgrHome() throws ServiceLookupException
  {
    return (IRNProcessDefManagerHome) ServiceLookup.getInstance(
      ServiceLookup.CLIENT_CONTEXT).getHome(
      IRNProcessDefManagerHome.class);
  }

  protected void setUp()
  {
    try
    {
      super.setUp();
      _defMgrHome= getProcessDefMgrHome();

    }
    catch (ServiceLookupException ex)
    {
      Logger.err("Error setup", ex);
    }
  }

  public void testGetProcessDef() throws Throwable
  {
    for (int i= 0; i < 200; i++)
    {
      Thread thread= new Thread()
      {
        public void run() 
        {
          try
          {
          _defMgr= _defMgrHome.create();
          ProcessDef def= _defMgr.findProcessDefByName("3A4Test");
          Logger.debug("ProcessDef retrieved is " + def);
          }catch(Throwable ex)
          {
            Logger.err("Error in test", ex);
          }
        }
      };
      thread.start();
    }
  }

}
