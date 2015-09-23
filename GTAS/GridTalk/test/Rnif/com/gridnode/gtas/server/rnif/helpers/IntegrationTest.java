package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.util.DocTestUtil;
public class IntegrationTest extends RnifTestCase
{

  public IntegrationTest(String arg0)
  { 
    super(arg0);
  }

  
//  public void testImport3A4() throws Throwable
//  { 
//    Logger.debug("testImport3A4 Enter");
//
//    try
//    {
//      DocTestUtil.import3A4Request();
//    }
//    catch (Throwable ex)
//    {
//      Logger.err("testImport3A4", ex);
//      throw ex;
//    }
//     Logger.debug("testImport3A4 Exit");
//    
//  }
  
  public void test3A4Responder() throws Throwable
  {
    Logger.debug("test3A4Responder Enter");

    try
    {
//      RNDocReceiverTest.receive3A4Request(_rnifMgr);
//
//      waitforMin(2);
//      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
      
//     DocTestUtil.import3A4Response();
      DocTestUtil.import3A4Request();
//      waitforMin(3);
//      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
//      RNDocReceiverTest.receive3A4ResponseAck(_rnifMgr);
//      waitforMin(1);
//      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
    }
    catch (Throwable ex)
    {
      Logger.err("test3A4Responder", ex);
      throw ex;
    }
     Logger.debug("test3A4Responder Exit");
  }
  
   private  void waitforMin(int min) throws Throwable
  {
      try
      {
       synchronized(token)
       {
        token.wait(min*60*1000);
       }
      }
      catch(Throwable ex)
      {
        Logger.err("Error in waiting", ex);
        throw ex;
      }
  }
  private Object token=new Object();
  
//  public void test3A4Initiator() throws Throwable
//  {
//    Logger.debug("test3A4Initiator Enter");
//
//    try
//    {
//      DocTestUtil.import3A4Request();
//      waitforMin(5);
//      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
//      
//
////      RNDocReceiverTest.receive3A4RequestAck(_rnifMgr);
////      waitforMin(3);
////      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
////      
////      RNDocReceiverTest.receive3A4Response(_rnifMgr);
////      
////      waitforMin(1);
////      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
////
////      waitforMin(3);
////      ProcessInstanceActionHelperTest.getAllProcessInstanceList(_rnifMgr);
//
//    }
//    catch (Throwable ex)
//    {
//      Logger.err("test3A4Initiator", ex);
//      throw ex;
//    }
//     Logger.debug("test3A4Initiator Exit");
//  }

}
