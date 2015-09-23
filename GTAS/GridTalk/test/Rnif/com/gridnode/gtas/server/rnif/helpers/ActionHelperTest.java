package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.util.ProcessDefTestUtil;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import junit.framework.TestCase;
public class ActionHelperTest extends RnifTestCase
{

  protected IRNProcessDefManagerHome _defMgrHome= null;
  protected IRNProcessDefManagerObj _defMgr= null;
  
  public ActionHelperTest(String arg0)
  {
    super(arg0);
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

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

//  public void testAddProcessDef3A4() throws Throwable
//  {
//    try
//    {
//      Logger.debug("testAddProcessDef3A4 Enter");
//      ProcessDef def= ProcessDefTestUtil.create3A4();
//      
//      _rnifMgr = _rnifMgrHome.create();
//      Object res = _rnifMgr.invokeMethod(null, "com.gridnode.gtas.server.rnif.helpers.ActionHelper", "createProcessDef", 
//       new Class[]{ProcessDef.class}, new Object[]{def});
// //     ActionHelper.createProcessDef(def);
//    }
//    catch (Throwable ex)
//    {
//      Logger.err("testAddProcessDef3A4", ex);
//      throw ex;
//    }
//    Logger.debug("testAddProcessDef3A4 Exit");
//
//  }
//  
//  public void testGetProcessDef3A4() throws Throwable
//  {
//    Logger.debug("testGetProcessDef3A4 Enter");
//
//    try
//    {
//       _defMgr= _defMgrHome.create();
//      ProcessDef def=_defMgr.findProcessDefByName("3A4Test");
//      Logger.debug("ProcessDef retrieved is "+ def);
//    }
//    catch (Throwable ex)
//    {
//      Logger.err("testGetProcessDef3A4", ex);
//      throw ex;
//    }
//     Logger.debug("testGetProcessDef3A4 Exit");
//    
//  }
//  
  
  public void testUpdateProcessDef3A4() throws Throwable
  {
    Logger.debug("testUpdateProcessDef3A4 Enter");

    try
    {
       _defMgr= _defMgrHome.create();
      ProcessDef def= ProcessDefTestUtil.create3A4();
      String defName = def.getDefName();
      ProcessDef olddef=_defMgr.findProcessDefByName(defName);
      Logger.debug("ProcessDef retrieved is "+ olddef);
      if(olddef != null)
      {
        Long defUid = (Long)olddef.getKey();
        Object res = _rnifMgr.invokeMethod(null, "com.gridnode.gtas.server.rnif.helpers.ActionHelper", "deleteProcessDef", 
        new Class[]{Long.class}, new Object[]{defUid});
      }
      Object res = _rnifMgr.invokeMethod(null, "com.gridnode.gtas.server.rnif.helpers.ActionHelper", "createProcessDef", 
       new Class[]{ProcessDef.class}, new Object[]{def});
    }
    catch (Throwable ex)
    {
      Logger.err("testUpdateProcessDef3A4", ex);
      throw ex;
    }
     Logger.debug("testUpdateProcessDef3A4 Exit");
    
  }

}
