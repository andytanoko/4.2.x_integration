package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;

public class BPSSHandlerTest extends RnifTestCase implements IRnifTestConstants
{

  public BPSSHandlerTest(String name)
  {
    super(name);
  }

  //  public void testSend3A4Request() throws Throwable
  //  {
  //    try
  //    {
  //      Logger.debug("testSend3A4Request Enter");
  //      GridDocument gDoc = DocTestUtil.create3A4Request();
  //      String res = new BpssHandler().sendRequestDocument(WF_DOCID_3A4, WF_DOCTYPE_REQUEST_3A4, gDoc, new Integer(0),PARTNER_KEY_3A4);
  //      Logger.log("testSend3A4Request, result is " + res);
  //      
  //    }
  //    catch (Throwable ex)
  //    {
  //      Logger.err("testSend3A4Request", ex);
  //      throw ex;
  //    }
  //    Logger.debug("testSend3A4Request Exit");
  //  }
  //
  public void testSendNoF() throws Throwable
  {
    String failedProcess= "gwf://659/Request Purchase Order/SELF";
    Long failedDocUid= new Long(288);
    
    try
    {
      Logger.debug("testSendNoF Enter");
      
//      RNDocSender docSender = new RNDocSender();
//      docSender.importAndSendDoc("admin", "hahah.xml", WF_DOCTYPE_REQUEST_3A4, PARTNER_KEY_3A4);
//      Logger.debug("testSendNoF finished phase1");


      _rnifMgr.invokeMethod(
        new BpssHandler(),
        "com.gridnode.gtas.server.rnif.helpers.BpssHandler",
        "sendSignal",
        new Class[] {
          String.class,
          String.class,
          Object.class,
          String.class,
          Object.class,
          String.class },
        new Object[] {
          failedProcess,
          IBpssConstants.EXCEPTION_TIMETO_PERFORM,
          "exceed Time To Performat 00:00:00 ",
          WF_DOCTYPE_REQUEST_3A4,
          failedDocUid,
          PARTNER_KEY_3A4 });
      Logger.log("testSendNoF finished");

    }
    catch (Throwable ex)
    {
      Logger.err("testSendNoF", ex);
      throw ex;
    }
    Logger.debug("testSendNoF Exit");

  }

}
