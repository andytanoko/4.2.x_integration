package com.gridnode.pdip.app.channel.ejb;

import java.io.File;
import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;


public class ChannelHandlerBeanTest extends TestCase
{
  private IChannelManagerHome _home;
  private IChannelManagerObj _obj;


  public ChannelHandlerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ChannelHandlerBeanTest.class);
  }


  public void setUp()
  {
    try
    {
      System.out.println("b4 look up");
      lookupChannelMgr();
    }
    catch(Exception ex)
    {
      System.out.println("Cannot SetUP");
    }

  }

/*
  public void testSend() throws Exception
  {
    System.out.println("B4 Sending ");
    ChannelInfo info = new ChannelInfo();
    CommInfo cminfo = new JMSCommInfo();
    cminfo.setDescription("descrit");
    cminfo.setHost("localhost");
    cminfo.setPort(443);
    cminfo.setProtocolType("JMS");
    //cminfo.setDestination("testtopic");
    //cminfo.setUser("admin");
    //cminfo.setPassword("secret");

    info.setTptCommInfo(cminfo);
    info.setTptCommInfo(cminfo);
    info.setDescription("description");
    info.setName("name");
    info.setTptProtocolType("JMS");
    _obj.send(info,new String[]{"ab","cd"},new File[]{null},"ab");
//    _obj.send(info,new String[]{"ab","cd"},new  File[]{null},"ab");
    System.out.println("After Sending");
  }

*/
  public void testCreate() throws Exception
  {
    System.out.println("B4 Sending ");

   // for(int i=0;i<100;i++)
   // {
//    ChannelInfo info = new ChannelInfo();
    ChannelInfo info = new ChannelInfo();
    CommInfo cminfo = new CommInfo();
    SecurityInfo secinfo = new SecurityInfo();
    PackagingInfo pacinfo = new PackagingInfo();

    cminfo.setDescription("descrit");
  //  cminfo.setHost("192.168.213.167");
//    cminfo.setPort(443);

    Hashtable ht = new Hashtable();
//    ht.put("User","admin");
//    ht.put("Password","secret");
    ht.put("Destination","testtopic");
//    ht.put("Destination Type",cminfo.JMS);
//    cminfo.setProtocolDetail(ht);
    cminfo.setTptImplVersion("030000");
    cminfo.setURL("smqp://admin:secret@192.168.213.167:443/destinationtype=Topic?destination=testtopic");


//    cminfo.setProtocolType("JMS");
//    cminfo.setDestination("testtopic");
//    cminfo.setUser("admin");
//    cminfo.setPassword("secret");
//
//    info.setTptCommInfo(cminfo);
    cminfo.setProtocolType("JMS");

    info.setTptCommInfo(cminfo);
    info.setDescription("description");
    info.setName("name");
    info.setReferenceId("12121");
    secinfo.setDescription("a security");

    secinfo.setEncryptionCertificateID(new Long(26));
    secinfo.setSignatureEncryptionCertificateID(new Long(25));
    secinfo.setSignatureType("Default");
    secinfo.setEncryptionLevel(512);
    secinfo.setEncryptionType(SecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
    pacinfo.setEnvelope(PackagingInfo.DEFAULT_ENVELOPE_TYPE);
    pacinfo.setZip(false);
    pacinfo.setName("abc");

    info.setPackagingProfile(pacinfo);
    info.setSecurityProfile(secinfo);
//    SecurityDB.setPrivatepassword("xx");
    String header[] = new String[6];
    header[0]="2";
//    Integer.toString("02000");
    header[1]="0001";

    header[2]=null;
    header[3]="myfilezip";
    header[4]=null;
    header[5]="192";
//    _obj.connect(null);
   String[] dataContent = new String[21];
   dataContent = new String[2];
   dataContent[0]="0";
   dataContent[1]="1";
//    _obj.connectAndListen(cminfo,header);
//   _obj.send(info,dataContent,null,header);
//      for(int i=2;i<21;i++)
//   dataContent[i]="Content "+i;
  // _obj.send(info,null,null,header);
       _obj.send(info,dataContent,new File[]{new File("c:/a.txt")},header);
//   _obj.send(info,dataContent,new File[]{new File("c:/a.txt"),new File("c:/b.txt")},header);
  //}
  }


/*  public void testGetFeedBackListener() throws Exception
  {
    System.out.println("B4 Calling Get FeedBackListener ");
    IReceiveFeedbackHandler feedbackHandler = _obj.getReceiveFeedbackHandler("ab");
    if(feedbackHandler instanceof JMSReceiveFeedbackHandler)
    {
      System.out.println("Instance of JMSReceiveFeedbackHandler");
    }
  }


/* public void testSend()
 {
  try
  {

   _obj.send(null,null,null,null);
  }
  catch(Exception ex)
  {
    ex.printStackTrace();
  }
 }
*/
  public void lookupChannelMgr() throws Exception
  {
    _home = (IChannelManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IChannelManagerHome.class);
    assertNotNull("ChannelManager Home is null", _home);
    _obj = _home.create();
    assertNotNull("ProcedureDefManager Object is null", _obj);
  }

}