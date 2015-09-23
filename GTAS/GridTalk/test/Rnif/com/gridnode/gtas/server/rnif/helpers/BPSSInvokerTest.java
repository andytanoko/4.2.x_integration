package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;
import com.gridnode.gtas.server.rnif.helpers.util.ProcessDefTestUtil;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.util.Collection;

public class BPSSInvokerTest extends RnifTestCase implements IRnifTestConstants
{

  public BPSSInvokerTest(String name)
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
      ProcessDef def= defMgr.findProcessDefByName("3A4Test");
      Logger.debug("ProcessDef retrieved is " + def + def.getRequestAct() + def.getResponseAct());
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

  public void testSend3A4Request() throws Throwable
  {
    try
    {
      Logger.debug("testSend3A4Request Enter");
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

      _rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.BpssInvoker",
        "insertBizDocToSend2BPSS",
        new Class[] { GridDocument.class, String.class, Boolean.class },
        new Object[] { gDoc, DEF_NAME_3A4, Boolean.TRUE });
    }
    catch (Throwable ex)
    {
      Logger.err("testSend3A4Request", ex);
      throw ex;
    }
    Logger.debug("testSend3A4Request Exit");

  }

}
