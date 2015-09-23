/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: ChannelServiceDelegate.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Oct 22 2002    Jagadeesh         Created
 * Dec 05 2002    Jagadeesh         Created
 * Jan 30 2003    Kan Mun           Modified - Change method name from getSenderNodeId
 *                                             to getMySenderNodeId
 * Jul 10 2003    Neo Sok Lay       Change lookupXXX() to public accessible.
 * Aug 21 2003    Jagadeesh         Added : To get MasterChannel by ReferenceId.
 * Nov 12 2003    Guo Jianyu        Added writeToAudit()
 * Apr 01 2004    Jagadeesh         Modified: To check for messages received from
 *                                            3rd party(RNIF), and check for isEmpty
 *                                            on Collections.
 * May 11 2004    Neo Sok Lay       Modify writeToAudit() to use Map instead of Hashtable.
 * Jun 07 2004    Neo Sok Lay       Add additional check in getPayLoadTypeByMessageStandard()
 *                                  for null MessageHeaders before proceeding to access
 *                                  MessageHeaders.
 * Jul 30 2004	  Jagadeesh	    Added : To get Channel by Name and isPartner Flag.
 * Mar 12 2007    Neo Sok Lay       Use UUID for unique filename.
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.transport.helpers.GNTransportHeader;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.messaging.*;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UUIDUtil;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.util.*;
import java.io.*;

public class ChannelServiceDelegate
{
  private static Random rd = new Random(System.currentTimeMillis());
  private static final String AUDIT_PATH = "common.path.audit";
  private static final String CLASS_NAME = "ChannelServiceDelegate";

  private ChannelServiceDelegate()
  {
  }

  public static String getMyNodeId() throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.IS_MASTER,
        filter.getEqualOperator(),
        Boolean.TRUE,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        ChannelInfo.IS_PARTNER,
        filter.getEqualOperator(),
        Boolean.FALSE,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info.getReferenceId();
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSenderNodeId()",
        "Cannot Retrieve SenderNodeId",
        ex);
      //throw new Exception("Cannot Retrive SenderNodeId From this Cannel");
    }
    return null;
  }

  public static ChannelInfo getRelayChannelInfo() throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.IS_RELAY,
        filter.getEqualOperator(),
        Boolean.TRUE,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
     }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getRelayChannelInfo()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception("Cannot Retrive Relay ChannelInfo ");
    }
    return null;
  }

  public static ChannelInfo getChannelInfoByRefId(String refId)
    throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.REF_ID,
        filter.getEqualOperator(),
        refId,
        false);
      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByRefId()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception("Cannot Retrive ChannelInfo with this RefId" + refId);
    }
    return null;
  }

  public static ChannelInfo getChannelInfoByName(String channelName)
    throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.NAME,
        filter.getEqualOperator(),
        channelName,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByName()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception(
        "Cannot Retrive ChannelInfo with this Name" + channelName);
    }
    return null;
  }

  /**
   * This method gets ChannelInfo based on ChannelName and isPartner flag.
   * @param channelName - Name of the Channel
   * @param isPartner - isPartner Flag boolean value (true or false).
   * @return ChannelInfo or null if no Channel found.
   * @throws java.lang.Exception
   */


  public static ChannelInfo getChannelInfoByNameAndPartnerFlag(
      String channelName,
      boolean isPartner)
      throws Exception
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME,"getChannelInfoByNameAndPartnerFlag()",
      	"[ChannelName=]"+channelName +"[isPartnerFlag=]"+isPartner);
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.NAME,
        filter.getEqualOperator(),
        channelName,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        ChannelInfo.IS_PARTNER,
        filter.getEqualOperator(),
        new Boolean(isPartner),
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByNameAndPartnerFlag()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception(
        "Cannot Retrive ChannelInfo From this Name["
          + channelName
          + "] And isPartnerFlag["
          + isPartner
          + "]");
    }
    return null;
  }



  public static ChannelInfo getChannelInfoByNameAndRefId(
    String channelName,
    String refId)
    throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        ChannelInfo.NAME,
        filter.getEqualOperator(),
        channelName,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        ChannelInfo.REF_ID,
        filter.getEqualOperator(),
        refId,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByNameAndRefId()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception(
        "Cannot Retrive ChannelInfo From this Name["
          + channelName
          + "] And RefID["
          + refId
          + "]");
    }
    return null;
  }

  /**
   * This Method retrieves MasterChannel given the Reference Id.
   * Usage : When we need to retrieve Senders Master Channel,(i.e) when partner
   * has exchanged with us his Master Channel and other Channels to transact,
   * we may want to retrieve his Master Channel.
   *
   * @param refId RefId to retrieve from
   * @return ChannelInfo
   * @throws Exception throws Exception when cannot retrieve.
   */

  public static ChannelInfo getMasterChannelInfoByRefId(String refId)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    try
    {
      filter.addSingleFilter(
        null,
        ChannelInfo.REF_ID,
        filter.getEqualOperator(),
        refId,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        ChannelInfo.IS_MASTER,
        filter.getEqualOperator(),
        Boolean.TRUE,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
      return null;
    }
    catch (FindEntityException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getMasterChannelInfoByRefId()",
        "[Cannot Retrieve ChannelInfo For ]" + filter,
        ex);
      return null;
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getMasterChannelInfoByRefId()",
        "[Cannot Retrieve ChannelInfo]",
        ex);
      throw new Exception(
        "Cannot Retrive ChannelInfo For this RefId[" + refId + "]");
    }
  }

  public static ChannelInfo getRelayChannelInfoByRefId(String refId)
    throws Exception
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        ChannelInfo.REF_ID,
        filter.getEqualOperator(),
        refId,
        false);

      filter.addSingleFilter(
        null,
        ChannelInfo.IS_RELAY,
        filter.getEqualOperator(),
        Boolean.TRUE,
        false);

      Collection col = getChannelServiceFacade().getChannelInfo(filter);
      if (!col.isEmpty())
      {
        ChannelInfo info = (ChannelInfo) col.iterator().next();
        if (info != null)
          return info;
      }
      return null;
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getRelayChannelInfo()",
        "Cannot Retrieve ChannelInfo",
        ex);
      throw new Exception("Cannot Retrive Relay ChannelInfo ");
    }

  }

  public static IChannelManagerObj getChannelServiceFacade()
    throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IChannelManagerHome.class.getName(),
        IChannelManagerHome.class,
        new Object[0]);
  }

  public static ChannelInfo getChannelInfo(Message message)
  {
    String channelName =
      (String) message.getCommonHeaders().get(ICommonHeaders.COMM_CHANNEL);
    ChannelInfo channelInfo = null;
    if (!checkNullEmpty(channelName))
    {
      try
      {
        return getChannelInfoByName(channelName);
      }
      catch (Exception ex)
      {
        return null;
      }
    }
    else
    {
      ChannelLogger.infoLog(CLASS_NAME,"getChannelInfo()","[Creating Channel From CommonHeaders]");
      channelInfo = new ChannelInfo();
      try
      {
        PackagingInfo packInfo = getPackagingProfile(message);
        SecurityInfo  secInfo  = getSecurityProfile(message.getCommonHeaders());
        FlowControlInfo flowInfo = getFlowControlProfile(message.getCommonHeaders());
        CommInfo commInfo = getCommProfile(message.getCommonHeaders());
        channelInfo.setPackagingProfile(packInfo);
        channelInfo.setSecurityProfile(secInfo);
        channelInfo.setFlowControlInfo(flowInfo);
        channelInfo.setTptCommInfo(commInfo);
      }
      catch(Exception ex)
      {
        ChannelLogger.warnLog(CLASS_NAME,"getChannelInfo()",
                               "[Cannot Create ChannelInfo]",ex);
        return null;
      }
    }
    return channelInfo;
  }

  public static ChannelInfo getChannelInfo(Message message,boolean isPartnerFlag)
  {
    String channelName =
      (String) message.getCommonHeaders().get(ICommonHeaders.COMM_CHANNEL);
    ChannelInfo channelInfo = null;
    if (!checkNullEmpty(channelName))
    {
      try
      {
        return getChannelInfoByName(channelName);
      }
      catch (Exception ex)
      {
        return null;
      }
    }
    else
    {
      ChannelLogger.infoLog(CLASS_NAME,"getChannelInfo()","[Creating Channel From CommonHeaders]");
      channelInfo = new ChannelInfo();
      try
      {
        PackagingInfo packInfo = getPackagingProfile(message);
        SecurityInfo  secInfo  = getSecurityProfile(message.getCommonHeaders());
        FlowControlInfo flowInfo = getFlowControlProfile(message.getCommonHeaders());
        CommInfo commInfo = getCommProfile(message.getCommonHeaders());
        channelInfo.setPackagingProfile(packInfo);
        channelInfo.setSecurityProfile(secInfo);
        channelInfo.setFlowControlInfo(flowInfo);
        channelInfo.setTptCommInfo(commInfo);
      }
      catch(Exception ex)
      {
        ChannelLogger.warnLog(CLASS_NAME,"getChannelInfo()",
                               "[Cannot Create ChannelInfo]",ex);
        return null;
      }
    }
    return channelInfo;
  }




  private static CommInfo getCommProfile(Map commonHeaders)
  {
    CommInfo info = new CommInfo();
    String protocolType =
      (String) commonHeaders.get(ICommonHeaders.COMM_PROTOCOL_TYPE);
    String url = (String) commonHeaders.get(ICommonHeaders.COMM_ENDPOINT_URL);
    if (!checkNullEmpty(protocolType))
      info.setProtocolType(protocolType);
    if (!checkNullEmpty(protocolType))
      info.setURL(url);
    return info;
  }

  private static PackagingInfo getPackagingProfile(Message message)
      throws Exception
  {
    //Map commonHeaders = message.getCommonHeaders();
    PackagingInfo packInfo = new PackagingInfo();
    String payLoadType = getPayLoadTypeByMessageStandard(message);
    if (payLoadType == null)
      throw new Exception("[Cannot Handle UnKnown Packaging Type]");
    else
      packInfo.setEnvelope(payLoadType);
    return packInfo;
  }

  private static SecurityInfo getSecurityProfile(Map commonHeaders)
  {
    SecurityInfo secInfo = new SecurityInfo();
    secInfo = setEncryptionCertInfo(commonHeaders, secInfo);
    secInfo = setSignatureCertInfo(commonHeaders, secInfo);
    return secInfo;
  }

  private static FlowControlInfo getFlowControlProfile(Map commonHeaders)
  {
    FlowControlInfo info = new FlowControlInfo();
    String isZip = (String) commonHeaders.get(ICommonHeaders.PAYLOAD_IS_ZIP);
    String isSplit =
      (String) commonHeaders.get(ICommonHeaders.PAYLOAD_IS_SPLIT);
    if (!checkNullEmpty(isZip))
      info.setIsZip(Boolean.getBoolean(isZip));
    if (!checkNullEmpty(isSplit))
    {
      info.setIsSplit(Boolean.getBoolean(isSplit));
      if (Boolean.getBoolean(isSplit))
      {
        info.setSplitSize(
          Integer.parseInt(
            (String) commonHeaders.get(ICommonHeaders.PAYLOAD_BLK_SPLIT_SIZE)));
        info.setSplitThreshold(
          Integer.parseInt(
            (String) commonHeaders.get(
              ICommonHeaders.PAYLOAD_BLK_SPLIT_THRESHOLD)));
      }
    }
    return info;
  }

  private static boolean checkNullEmpty(String checkString)
  {
    return (checkString == null || checkString.equals(""));
  }

  private static Long getCertUIDBySerialNoAndIssuerName(
    String issuerName,
    String serialNum)
  {
    try
    {
      if (checkNullEmpty(issuerName) || checkNullEmpty(serialNum))
        return null;
      com.gridnode.pdip.base.certificate.model.Certificate certId =
        getCertificateServiceFacade().findCertificateByIssureAndSerialNum(
          issuerName,
          serialNum);
      return (null == certId ? null : new Long(certId.getUId()));
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  private static SecurityInfo setEncryptionCertInfo(
    Map commonHeaders,
    SecurityInfo secInfo)
  {
    String encyLvl = (String) commonHeaders.get(ICommonHeaders.ENCRYPT_LEVEL);
    String encyType = (String) commonHeaders.get(ICommonHeaders.ENCRYPT_TYPE);
    String encyCertSerialNo =
      (String) commonHeaders.get(ICommonHeaders.ENCRYPT_CERT_SERIAL_NUM);
    String encyCertIssuerName =
      (String) commonHeaders.get(ICommonHeaders.ENCRYPT_CERT_ISSUER_NAME);

    secInfo.setEncryptionLevel(
      checkNullEmpty(encyLvl) ? 0 : Integer.parseInt(encyLvl));
    secInfo.setEncryptionType(
      checkNullEmpty(ICommonHeaders.ENCRYPT_TYPE) ? "" : encyType);
    Long encypCertId =
      getCertUIDBySerialNoAndIssuerName(encyCertSerialNo, encyCertIssuerName);
    secInfo.setEncryptionCertificateID(encypCertId);
    return secInfo;
  }

  private static SecurityInfo setSignatureCertInfo(
    Map commonHeaders,
    SecurityInfo secInfo)
  {
    String signType = (String) commonHeaders.get(ICommonHeaders.SIGN_TYPE);
    String signDigst =
      (String) commonHeaders.get(ICommonHeaders.SIGN_DIGEST_TYPE);
    String signCertSerialNo =
      (String) commonHeaders.get(ICommonHeaders.SIGN_CERT_SERIAL_NUM);
    String signCertIssuerName =
      (String) commonHeaders.get(ICommonHeaders.SIGN_CERT_ISSUER_NAME);
    Long signCertId =
      getCertUIDBySerialNoAndIssuerName(signCertSerialNo, signCertIssuerName);

    secInfo.setSignatureType(checkNullEmpty(signType) ? "" : signType);
    secInfo.setDigestAlgorithm(checkNullEmpty(signDigst) ? "" : signDigst);
    secInfo.setSignatureEncryptionCertificateID(signCertId);
    return secInfo;
  }

  private static ICertificateManagerObj getCertificateServiceFacade()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        ICertificateManagerHome.class.getName(),
        ICertificateManagerHome.class,
        new Object[0]);
  }

  /** @todo To extend to look for AS2 Packaging. i.e to extend GNTransportHeader to include a method
   *  to check for AS2-Header. i.e AS2-FROM*/
  private static String getPayLoadTypeByMessageStandard(Message message)
  {
    String payLoadType = null;
    Map messageHeaders = message.getMessageHeaders();
    
    //in case messageHeaders is not available, then we assume default GTAS processing
    if (messageHeaders == null || messageHeaders.size() ==0)
      return IPackagingInfo.DEFAULT_ENVELOPE_TYPE;

    String payLoadHeader = (String)messageHeaders.get(ICommonHeaders.PAYLOAD_TYPE);

    //since we expect non gtas partners or 3rd party tools to send messages via HTTP, we assume that
    //messages are received by HTTPTransport - which preprocess the message.
    GNTransportHeader header = new GNTransportHeader(new Hashtable(messageHeaders));
    if (isRNMessage(header))
    {
      if (header.getRNVersion().length() > 0)
        payLoadType = IPackagingInfo.RNIF2_ENVELOPE_TYPE;
      else
        payLoadType = IPackagingInfo.RNIF1_ENVELOPE_TYPE;
    }
    else if (messageHeaders.containsKey(IAS2Headers.AS2_FROM))
    {// AS2 messages
      payLoadType = IPackagingInfo.AS2_ENVELOPE_TYPE;
      messageHeaders.put(IAS2Headers.AUDIT_FILE_NAME, writeToAudit(new Hashtable(messageHeaders),
        message.getPayLoadData(), "AS2/", "in"));
    }
    else if (isGTASMessage(header,payLoadHeader)) //Only here for defensive check..we expect 3rd party messages only.
    {
      payLoadType = IPackagingInfo.DEFAULT_ENVELOPE_TYPE;
    }
    
    return payLoadType;
  }

  private static boolean isRNMessage(GNTransportHeader header)
  {
    return header.isNativeRNMessage();
  }

  private static boolean isGTASMessage(GNTransportHeader header,String payLoadHeader)
  {
    if (header.isGTASMessage())
      return true;
    else if (payLoadHeader != null && payLoadHeader.equals(
        IPackagingInfo.DEFAULT_ENVELOPE_TYPE))
      return true;
    else
      return false;
  }

  public synchronized static String writeToAudit(Map header, byte[] fileContent, String subPath, String fileName)
  {
    try
    {
      String headerString = "";
      Iterator allKeys = header.keySet().iterator();
      while (allKeys.hasNext())
      {
        String key = (String)allKeys.next();
        headerString = headerString + key + ": " + header.get(key) + "\r\n";
      }

      File tempFile= File.createTempFile("as2"+UUIDUtil.getRandomUUIDInStr(), null);
      FileOutputStream writer= new FileOutputStream(tempFile);
      writer.write(headerString.getBytes());
      writer.write("\r\n".getBytes());
      writer.write(fileContent);
      writer.close();

      String auditFileName = fileName + rd.nextLong();

      auditFileName = FileUtil.create(AUDIT_PATH, subPath, auditFileName, tempFile);
      ChannelLogger.debugLog(CLASS_NAME, "writeToAudit", "wrote audit file to " +
        auditFileName);
      tempFile.delete();
      if (subPath != null && subPath.length()>0)
        auditFileName = subPath + auditFileName;
      return auditFileName;
    }
    catch(Throwable t)
    {
      ChannelLogger.debugLog(CLASS_NAME, "writeToAudit", "Error:", t);
      return null;
    }
  }
}