package com.gridnode.pdip.app.channel.ejb;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;

import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;

//import com.gridnode.pdip.base.transport.helpers.IGTTransportFeedbackListener;
//import com.gridnode.pdip.base.transport.helpers.IGTTransportReceiveListener;

//import com.gridnode.pdip.app.channel.handler.jmschannel.JMSReceiveFeedbackHandler;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
//import com.gridnode.pdip.app.channel.model.JMSCommInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;


import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import junit.framework.*;
import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class ChannelManagerBeanTest extends TestCase
{

  IChannelManagerHome _channelManagerHome;
  IChannelManagerObj _channelManagerObj;
  PackagingInfo packagingInfo;
  SecurityInfo securityInfo;
  public static final String CLASS_NAME="ChannelManagerBeanTest";

  public ChannelManagerBeanTest(String name)
  {
    super(name);
  }


  public static Test suite()
  {
    return new TestSuite(ChannelManagerBeanTest.class);
  }

  public void setUp() throws Exception
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"setUp","In SetUP Enter");
      lookupChannelMgr();
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME,"setUp","SetUp Exit");
    }
  }

  public void lookupChannelMgr() throws Exception
  {
    _channelManagerHome = (IChannelManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IChannelManagerHome.class);
    assertNotNull("ChannelManager Home is null", _channelManagerHome);
    _channelManagerObj = _channelManagerHome.create();
    assertNotNull("ProcedureDefManager Object is null", _channelManagerObj);
  }

/*
  public void testCreatePackagingInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo"," ChannelManagerBeanTest [testCreatePackagingInfo] Enter");
      createtestDataPackagingInfo();
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Before Calling");
      Long uid=_channelManagerObj.createPackagingInfo(packagingInfo);
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","After Calling");
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","UID IS "+uid);

    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }


  public void testDeletePackagingInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testDeletePackagingInfo"," ChannelManagerBeanTest [testDeletePackagingInfo] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testDeletePackagingInfo","Before Calling");
  //    _channelManagerObj.deletePackigingInfo(new Long(1));
      ChannelLogger.debugLog(CLASS_NAME,"testDeletePackagingInfo","After Calling");
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testDeletePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void testGetPackagingInfoByID()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByID"," ChannelManagerBeanTest [testGetPackagingInfoByID] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Before Calling");
      PackagingInfo packagingInfo =_channelManagerObj.getPackagingInfo(new Long(2));
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","After Calling");
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","UID IS "+packagingInfo.getUId());
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Name IS "+packagingInfo.getName());
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Description IS "+packagingInfo.getDescription());
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Envelope IS "+packagingInfo.getEnvelope());
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Zip IS "+packagingInfo.isZip());
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Envelope IS "+packagingInfo.getZipThreshold());
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }


  public void testGetPackagingInfoByFilter()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByID"," ChannelManagerBeanTest [testGetPackagingInfoByFilter] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Before Calling");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,PackagingInfo.NAME,filter.getEqualOperator(),
      new String("Package Name"),false);
      Collection col =_channelManagerObj.getPackigingInfo(filter);
      Iterator itr = col.iterator();
      while(itr.hasNext())
      {
        PackagingInfo packagingInfo = (PackagingInfo)itr.next();
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","After Calling");
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","UID IS "+packagingInfo.getUId());
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Name IS "+packagingInfo.getName());
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Description IS "+packagingInfo.getDescription());
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Envelope IS "+packagingInfo.getEnvelope());
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Zip IS "+packagingInfo.isZip());
        ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo","Envelope IS "+packagingInfo.getZipThreshold());
      }
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreatePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }


  public void testGetPackagingInfoUIDs()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByIDs"," ChannelManagerBeanTest [testGetPackagingInfoUIDs] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoUIDs","Before Calling");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,PackagingInfo.NAME,filter.getEqualOperator(),
      new String("Package Name"),false);
      Collection col =_channelManagerObj.getPackagingInfoUIDs(filter);
      Iterator itr = col.iterator();
      while(itr.hasNext())
      {
        Long uid = (Long)itr.next();
        ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByIDs","After Calling");
        ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByIDs","UID IS "+uid);
      }
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetPackagingInfoByIDs",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }



  public void testUpdatePackagingInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testUpdatePackagingInfo"," ChannelManagerBeanTest [testUpdatePackagingInfo] Enter");
      PackagingInfo info = _channelManagerObj.getPackagingInfo(new Long(4));
      info.setDescription("Updated Description ");
      _channelManagerObj.updatePackagingInfo(info);
      ChannelLogger.debugLog(CLASS_NAME,"testUpdatePackagingInfo"," ChannelManagerBeanTest [testUpdatePackagingInfo] Enter");
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testUpdatePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }
  }



  private void createtestDataPackagingInfo()
  {
    packagingInfo = new PackagingInfo();
    packagingInfo.setName("Package Name");
    packagingInfo.setDescription("Package Description");
    packagingInfo.setEnvelope("RNIF");
    packagingInfo.setZip(true);
    packagingInfo.setZipThreshold(500);
  }

 */


  public void testCreateSecuirtyInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreateSecuirtyInfo"," ChannelManagerBeanTest [testCreateSecuirtyInfo] Enter");
      createtestDataSecurityInfo();
      ChannelLogger.debugLog(CLASS_NAME,"testCreateSecuirtyInfo","Before Calling");
      Long uid=_channelManagerObj.createSecurityInfo(securityInfo);
      ChannelLogger.debugLog(CLASS_NAME,"testCreateSecuirtyInfo","After Calling");
      ChannelLogger.debugLog(CLASS_NAME,"testCreateSecuirtyInfo","UID IS "+uid);
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testCreateSecuirtyInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }

/*  public void testDeleteSecurityInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testDeleteSecurityInfo"," ChannelManagerBeanTest [testDeleteSecurityInfo] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testDeleteSecurityInfo","Before Calling");
      _channelManagerObj.deleteSecurityInfo(new Long(10));
      ChannelLogger.debugLog(CLASS_NAME,"testDeleteSecurityInfo","After Calling");
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testDeleteSecurityInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }
*/

  public void testGetSecurityInfoByID()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID"," ChannelManagerBeanTest [testGetSecurityInfoByID] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","Before Calling");
      SecurityInfo securityInfo =_channelManagerObj.getSecurityInfo(new Long(3));
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","After Calling");
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","UID IS "+securityInfo.getUId());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","Name IS "+securityInfo.getName());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","Description IS "+securityInfo.getDescription());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","EncryptionType IS "+securityInfo.getEncryptionType());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","EncryptionLevel IS "+securityInfo.getEncryptionLevel());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","Certificate IS "+securityInfo.getEncryptionCertificateID());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","Signature Type IS "+securityInfo.getSignatureType());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","DigestAlgorithm IS "+securityInfo.getDigestAlgorithm());
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID","SignatureCertificate IS "+securityInfo.getSignatureEncryptionCertificateID());
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByID",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void testGetSecurityInfoByFilter()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter"," ChannelManagerBeanTest [testGetSecurityInfoByFilter] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","Before Calling");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,SecurityInfo.NAME,filter.getEqualOperator(),
      new String("Security Profile"),false);
      Collection col =_channelManagerObj.getSecurityInfo(filter);
      Iterator itr = col.iterator();
      while(itr.hasNext())
      {
        SecurityInfo securityInfo = (SecurityInfo)itr.next();
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","After Calling");
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","UID IS "+securityInfo.getUId());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","Name IS "+securityInfo.getName());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","Description IS "+securityInfo.getDescription());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","EncryptionType IS "+securityInfo.getEncryptionType());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","EncryptionLevel IS "+securityInfo.getEncryptionLevel());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","Certificate IS "+securityInfo.getEncryptionCertificateID());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","Signature Type IS "+securityInfo.getSignatureType());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","DigestAlgorithm IS "+securityInfo.getDigestAlgorithm());
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter","SignatureCertificate IS "+securityInfo.getSignatureEncryptionCertificateID());
      }
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoByFilter",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void testGetSecurityInfoUIDs()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoUIDs"," ChannelManagerBeanTest [testGetSecurityInfoUIDs] Enter");
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoUIDs","Before Calling");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,PackagingInfo.NAME,filter.getEqualOperator(),
      new String("Security Profile"),false);
      Collection col =_channelManagerObj.getSecurityInfoUIDs(filter);
      Iterator itr = col.iterator();
      while(itr.hasNext())
      {
        Long uid = (Long)itr.next();
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoUIDs","After Calling");
        ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoUIDs","UID IS "+uid);
      }
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testGetSecurityInfoUIDs",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }



  public void testUpdateSecurityInfo()
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"testUpdateSecurityInfo"," ChannelManagerBeanTest [testUpdateSecurityInfo] Enter");
      SecurityInfo info = _channelManagerObj.getSecurityInfo(new Long(4));
      info.setDescription("Updated Description ");
      _channelManagerObj.updateSecurityInfo(info);
      ChannelLogger.debugLog(CLASS_NAME,"testUpdateSecurityInfo"," ChannelManagerBeanTest [testUpdateSecurityInfo] Exit");
    }
    catch(Exception ex)
    {
      ChannelLogger.debugLog(CLASS_NAME,"testUpdatePackagingInfo",
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }
  }



  private void createtestDataSecurityInfo()
  {
    securityInfo = new SecurityInfo();
    securityInfo.setName("Security Profile");
    securityInfo.setDescription("Security Description ");
    securityInfo.setEncryptionType("NONE");
    securityInfo.setEncryptionLevel(64);
    securityInfo.setEncryptionCertificateID(new Long(5));
    securityInfo.setSignatureType("NONE");
    securityInfo.setDigestAlgorithm("MD5");
    securityInfo.setSignatureEncryptionCertificateID(new Long(8));
  }



}