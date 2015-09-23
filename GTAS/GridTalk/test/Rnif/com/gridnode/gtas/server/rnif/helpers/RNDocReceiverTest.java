package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;

import java.io.File;

public class RNDocReceiverTest extends RnifTestCase implements IRnifTestConstants
{

  public RNDocReceiverTest(String name)
  {
    super(name);
  }


//  public void testInitiatorReceive3A4()throws Throwable
//  {
//   try
//   {
// //   receive3A4RequestAck();
//    receive3A4Response();
//   }
//    catch (Throwable ex)
//    {
//      Logger.err("testInitiatorReceive3A4", ex);
//      throw ex;
//    }
//    Logger.debug("testInitiatorReceive3A4 Exit");
//  }

  public static  void receive3A4RequestAck(IRnifManagerObj rnifMgr) throws Throwable
  {
     Logger.debug("receive3A4RequestAck Enter");
      //      GridDocument gDoc = DocTestUtil.create3A4Request();
      
     File[] files = new File[] {
         new File("testdata\\rnif\\3A4\\initiator\\3A4RequestAck_packinfo.xml").getAbsoluteFile(),
         new File(UDOC_ACK).getAbsoluteFile()
       };
     rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.RNDocReceiver",
        "receiveRNDoc",
        new Class[] { files.getClass() },
        new Object[] {files });
   }   
      
   public  static void receive3A4Response(IRnifManagerObj rnifMgr) throws Throwable
  {
    {
     Logger.debug("receive3A4Response Enter");
      File[] files = new File[] {
         new File("testdata\\rnif\\3A4\\initiator\\3A4Response_packinfo.xml").getAbsoluteFile(),
         new File(UDOC_3A4_RESPONSE).getAbsoluteFile()
       };
      rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.RNDocReceiver",
        "receiveRNDoc",
        new Class[] { files.getClass() },
        new Object[] {files });
    }
  }
  
  public void testResponderReceive3A4()throws Throwable
  {
   try
   {
    receive3A4Request(_rnifMgr);
    receive3A4ResponseAck(_rnifMgr);
   }
    catch (Throwable ex)
    {
      Logger.err("testInitiatorReceive3A4", ex);
      throw ex;
    }
    Logger.debug("testInitiatorReceive3A4 Exit");
  }
  
  public  static void receive3A4Request(IRnifManagerObj rnifMgr) throws Throwable
  {
      File[] files = new File[] {
         new File("testdata\\rnif\\3A4\\responder\\3A4Request_packinfo.xml").getAbsoluteFile(),
         new File(UDOC_3A4_REQUEST).getAbsoluteFile()
       };
     GridDocument gDoc = (GridDocument) rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.RNDocReceiver",
        "receiveRNDoc",
        new Class[] { files.getClass() },
        new Object[] {files });
        
    Logger.debug("receive3A4Request Exit");
  }

  public static  void receive3A4ResponseAck(IRnifManagerObj rnifMgr) throws Throwable
  {
     Logger.debug("receive3A4ResponseAck Enter");
       
     File[] files = new File[] {
         new File("testdata\\rnif\\3A4\\responder\\3A4ResponseAck_packinfo.xml").getAbsoluteFile(),
         new File(UDOC_ACK).getAbsoluteFile()
       };
      rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.RNDocReceiver",
        "receiveRNDoc",
        new Class[] { files.getClass() },
        new Object[] {files });
   }   

}
