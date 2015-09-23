package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;
import com.gridnode.gtas.server.rnif.helpers.util.ProcessDefTestUtil;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.util.Collection;

public class ProfileUtilTest extends RnifTestCase implements IRnifTestConstants
{

  ProcessDef def = null;
  public ProfileUtilTest(String name)
  {
    super(name);
  }

  protected void setUp()
  {
    try
    {
      super.setUp();
      IRNProcessDefManagerHome defMgrHome=
        (IRNProcessDefManagerHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(
          IRNProcessDefManagerHome.class);

      IRNProcessDefManagerObj defMgr= defMgrHome.create();
      def= defMgr.findProcessDefByName(DEF_NAME_3A4);
      Logger.debug("ProcessDef retrieved is " + def);
      if (def == null)
      {
        def= ProcessDefTestUtil.create3A4();
        def=
          (ProcessDef) _rnifMgr.invokeMethod(
            null,
            "com.gridnode.gtas.server.rnif.helpers.ActionHelper",
            "createProcessDef",
            new Class[] { ProcessDef.class },
            new Object[] { def });

      }

    }
    catch (Throwable ex)
    {
      Logger.err("BPSSInvokerTest.setUp", ex);
      fail();
    }
    Logger.debug("BPSSInvokerTest.setUp Exit");

  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testCreate3A4RequestProfile() throws Throwable
  {
    try
    {
      Logger.debug("testCreate3A4RequestProfile Enter");
      //      GridDocument gDoc = DocTestUtil.create3A4Request();

      IDocumentManagerHome _home;
      IDocumentManagerObj _remote;
      _home=
        (IDocumentManagerHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(
          IDocumentManagerHome.class);

      _remote= _home.create();
      
      Number[] fieldIds = new Number[]{GridDocument.FOLDER, GridDocument.R_PARTNER_ID};
      Object[] values = new Object[]{GridDocument.FOLDER_OUTBOUND, PARTNER_KEY_3A4};
     IDataFilter filter = EntityUtil.getEqualFilter(fieldIds, values);
      
       Collection list = _remote.findGridDocuments(filter);
       if(list == null || list.isEmpty())
        throw new  Exception("No Outbound GridDocument Exist yet");
      GridDocument gDoc = (GridDocument)list.iterator().next();
      Object res = new ProfileUtil().createRNProfile(gDoc, def,  "3A4TestINSTID", true);
      
//      invokeMethod(
//        null,
//        "com.gridnode.gtas.server.rnif.helpers.ProfileUtil",
//        "createRNProfile",
//        new Class[] { GridDocument.class, ProcessDef.class, String.class, Boolean.class },
//        new Object[] { gDoc, def, "3A4TestINSTID", Boolean.TRUE});
     Logger.debug("ProfileUtil.createRNProfile res ==" + res);
    }
    catch (Throwable ex)
    {
      Logger.err("testCreate3A4RequestProfile", ex);
      throw ex;
    }
    Logger.debug("testCreate3A4RequestProfile Exit");

  }

}
