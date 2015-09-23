package com.gridnode.gtas.server.rnif.helpers;


import com.gridnode.pdip.base.transport.comminfo.HttpCommInfo;
import com.gridnode.pdip.base.transport.handler.HTTPTransportHandler;
import com.gridnode.pdip.base.transport.helpers.IRNHeaderConstants;

import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestSuite that runs all the sample tests
 *
 */
public class AllTests
{

  

  public static Test suite ( )
  {
    TestSuite suite= new TestSuite("All Process JUnit Tests");

//    suite.addTest(new TestSuite(RNPackInfoTest.class));
//    suite.addTest(new TestSuite(ActionHelperTest.class));
    suite.addTest(new TestSuite(BPSSInvokerTest.class));
    suite.addTest(new TestSuite(IntegrationTest.class));
    suite.addTest(new TestSuite(RNPackagerTest.class));
    suite.addTest(new TestSuite(RNDocReceiverTest.class));
    suite.addTest(new TestSuite(ProcessInstanceActionHelperTest.class));
   suite.addTest(new TestSuite(CertficateTest.class));
     suite.addTest(new TestSuite(BPSSHandlerTest.class));

  //    suite.addTest(new TestSuite(RosettanetMessageConvertorTest.class));
    return suite;
  }
    public static void main(String[] args) throws Exception
    {
        junit.textui.TestRunner.run(CertficateTest.class);
//        httpTest(args);;
    }
    
   public AllTests(String arg0)
  {
    
  }
  
   public static void httpTest(String args[])
      throws Exception
  {
//    Hashtable headers = new Hashtable();
//    headers.put(IRNHeaderConstants.RN_VERSION_KEY, "2.0");
//    headers.put(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY, IRNHeaderConstants.RN_RESPONSE_TYPE_ASYNC);
//    HttpCommInfo commInfo = new HttpCommInfo("http://192.168.213.193:8081/HttpReceiver/receiver");
//    commInfo.setGatewayURL("http://192.168.213.193:8081/HttpReceiver/sender");
//
//    byte[] fileData = new byte[1];
//    String[] dataToSend = new String[2];
//    HTTPTransportHandler hand = new HTTPTransportHandler();
//    hand.send(commInfo,headers, null,fileData);
  }

}