package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.pdip.base.rnif.model.RNPackInfo;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import junit.framework.TestCase;

public class RNPackInfoTest extends TestCase
{

  public RNPackInfoTest(String name)
  {
    super(name);
  }

  protected void setUp()
  {}

  protected void tearDown()
  {}

  public void testSerialize()
  {
    RNPackInfo packinfo = null;
    RNPackInfo newPackinfo = null;
    
    String str = null;
    String newstr =null;
    try
    {
     StringWriter writer = new StringWriter();
     
      //File packinfoFile = new File("testdata/rnif/packinfo.xml");
      packinfo = createPackInfo();
      packinfo.serialize(writer); 
      str = writer.toString();
     
      writer = new StringWriter();
      newPackinfo = (RNPackInfo) packinfo.deserialize(new StringReader(str));
  //    File newpackinfoFile = new File("testdata/rnif/new_packinfo.xml");
      newPackinfo.serialize(writer);
      newstr = writer.toString();

    } catch (Exception ex)
    {
      Logger.err(ex);
    }
    assertEquals(str, newstr);

  }

  public static RNPackInfo createPackInfo()
  {
    RNPackInfo packinfo = new RNPackInfo();

    packinfo.setUDocFileName ("3A4REQ.xml");
    packinfo.setDTSendStart(new Date());

    packinfo.setReceiverDomain("DUNS");
    packinfo.setReceiverGlobalBusIdentifier("123456789"); //Integer
    packinfo.setReceiverLocationId("Santa Clara");
    packinfo.setSenderDomain("DUNS");
    packinfo.setSenderGlobalBusIdentifier("987654321"); //Integer
    packinfo.setSenderLocationId("Hong Kong");
    packinfo.setDeliveryMessageTrackingId("543543");
    packinfo.setBusActivityIdentifier("Create Purchase Order");
    packinfo.setFromGlobalPartnerRoleClassCode("Buyer");
    packinfo.setFromGlobalBusServiceCode("Buyer Service");
    packinfo.setInReplyToGlobalBusActionCode(null);
    packinfo.setInReplyToMessageStandard(null);
    packinfo.setInReplyToStandardVersion(null);
    packinfo.setInReplyToVersionIdentifier(null);
    packinfo.setServiceMessageTrackingId(null);
    packinfo.setActionIdentityGlobalBusActionCode("Purchase Order Request Action");
    packinfo.setActionIdentityToMessageStandard(null);
    packinfo.setActionIdentityStandardVersion(null);
    packinfo.setActionIdentityVersionIdentifier("01.02");
    packinfo.setSignalIdentityGlobalBusSignalCode(null);
    packinfo.setSignalIdentityVersionIdentifier(null);
    packinfo.setToGlobalPartnerRoleClassCode("Buyer Service");
    packinfo.setToGlobalBusServiceCode("Seller Service");
    packinfo.setGlobalUsageCode("Test");
    packinfo.setPartnerGlobalBusIdentifier("987654321"); //Integer
    packinfo.setPIPGlobalProcessCode("3A4");
    packinfo.setPIPInstanceIdentifier("121212");
    packinfo.setPIPVersionIdentifier("01.02");
    packinfo.setProcessTransactionId(null);
    packinfo.setProcessActionId(null);
    packinfo.setFromGlobalPartnerClassCode(null);
    packinfo.setToGlobalPartnerClassCode(null);
    packinfo.setNumberOfAttas(0);   

    return packinfo;

  }

}
