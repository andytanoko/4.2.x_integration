/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNPackagingHandler.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 07 2003      Jagadeesh              Changed: To get DefaultPackagingHeader
 *                                         from AbstractPackagingHandler
 * Jan 25 2005      Mahesh                 Added SIGNATURE_VERIFY_EXCEPTION to message header
 */

package com.gridnode.pdip.base.rnif.handler;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.handler.AbstractPackagingHandler;
import com.gridnode.pdip.base.packaging.handler.IPackagingHandler;
import com.gridnode.pdip.base.packaging.helper.IPackagingInfo;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.rnif.helper.*;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;


public class RNPackagingHandler
  extends AbstractPackagingHandler
  implements IPackagingHandler
{
  public static final String SENDER_DUNS= "SenderDUNS";
  public static final String RNDOC_FLAG= "IS_RN_DOC";
  public static final String RN_EVENT_ID="8900";

  protected RNPackager _packager;
  protected RNDePackager _depackager;

  private String RNVersion = "unspecified"; //IPackagingInfo.RNIF1_ENVELOPE_TYPE or
                            //IPackagingInfo.RNIF2_ENVELOPE_TYPE

  public RNPackagingHandler()
  {
  }

  public RNPackagingHandler(String RNVersion)
  {
    init(RNVersion);
  }

  protected void init(String RNVersion)
  {
    Logger.debugLog(getClassName(), "init()", "RNVersion is: [" + RNVersion + "]");
    this.RNVersion = RNVersion;
    if (RNVersion.equals(IPackagingInfo.RNIF1_ENVELOPE_TYPE))
    {   //RNIF1.1
      Logger.debugLog(getClassName(), "init()", "init RNIF1.1 packager and unpackager");
      _packager = new RNPackager_11();
      _depackager = new RNDePackager_11();
    }
    else
    {   // Make RNIF2.0 as the default RNIF
      Logger.debugLog(getClassName(), "init()", "init RNIF2.0 packager and unpackager");
      _packager = new RNPackager_20();
      _depackager = new RNDePackager_20();
    }
  }

  public String getRNVersion()
  {
    return RNVersion;
  }

  public void setSecurityInfoFinder(SecurityInfoFinder infoFinder)
  {
    _packager.setSecurityInfoFinder(infoFinder);
    _depackager.setSecurityInfoFinder(infoFinder);
  }

  public PackagingInfo packageAndEnvelope(PackagingInfo info) throws PackagingException
  {
    final String METHOD_NAME= "packageAndEnvelope()";
    try
    {
      Logger.debugLog(getClassName(), METHOD_NAME, "Packinginfo" + info);


      //RNPackInfo[] packinfoArray= new RNPackInfo[1];
      File[] files= info.getPayLoadToPackage();
      RNPackInfo packInfo = new RNPackInfo();
      packInfo = (RNPackInfo) packInfo.deserialize(files[0].getAbsolutePath());
      if (this.RNVersion.equals("unspecified"))
      {
        init(packInfo.getRnifVersion());
      }
      File[][] res= _packager.packDoc(packInfo, files);
      info.setPackagedPayLoad(res[0]);

      Hashtable headers= getDefaultPackagedHeader(info);
      headers= getHTTPHeaders(packInfo, headers);
      info.setEnvelopeHeader(headers);
      //       info.setAdditionalHeader(headers);

      return info;
    }
    catch (Exception ex)
    {
      Logger.warnLog(getClassName(), METHOD_NAME, "Exception in formating&Packaging", ex);
      throw new PackagingException("Exception in formating&Packaging", ex);
    }
  }

  public PackagingInfo unPackage(PackagingInfo info) throws PackagingException
  {
    final String METHOD_NAME= "unPackage()";
    try
    {
      Logger.debugLog(getClassName(), METHOD_NAME, "Packinfo" + info);

      //     Hashtable defaultHeader = new Hashtable();
      //     defaultHeader.put(ITransportConstants.EVENT_ID,"8900");
      //getDefaultUnPackagedHeader(arg0)(info);
      String[] eventId = getDefaultUnPackagedHeader(info);
      Arrays.fill(eventId,0,1,RN_EVENT_ID);

      /* This following block is commented, in order to get DefaultPackagigHeader
      from AbstractPackagingHandler - 07OCT2003.
      String[] eventId= new String[] { "8900", null, null, null, null, null, null, null }; //8
      */
      info.setDefaultUnPackagedHeader(eventId);
      RNPackInfo packinfo= new RNPackInfo();
      byte[] payload= info.getPayLoadToUnPackage();
      File uDoc= XMLUtil.createTempFile("UnPackage", payload);
      File[][] res= _depackager.unpackDocument(uDoc, packinfo);
      info.setUnPackagedPayLoad(res[0]);

      Hashtable extraHeader= new Hashtable();
      extraHeader.put(SENDER_DUNS, packinfo.getSenderGlobalBusIdentifier());

      extraHeader.put(RNDOC_FLAG, Boolean.TRUE);
      info.setAdditionalHeader(extraHeader);

      return info;
    }
    catch (Exception ex)
    {
      Logger.warnLog(getClassName(), METHOD_NAME, "Exception in formating&Packaging");
      throw new PackagingException("Exception in formating&Packaging", ex);
    }
  }

  /**
   * Returns a hashtable of HTTP headers as per RosettaNet standard
   */
  protected Hashtable getHTTPHeaders(RNPackInfo packinfo, Hashtable headers)
  {
    GNTransportHeader header= new GNTransportHeader(headers);
    if (RNVersion.equals(IPackagingInfo.RNIF1_ENVELOPE_TYPE))
    {   //RNIF1.1
      header.setContentType("application/x-rosettanet-agent; version=1.0");
    }
    else
    {   // RNIF2.0 is the default
      header.setRNVersion("RosettaNet/V02.00");
      header.setRNSyncMessageID(packinfo.getRNMsgId());
      header.setRNReplyMessage(!packinfo.getIsRequestMsg());
      header.setRNSyncMessage(packinfo.getIsSynchronous());
      // header.setContentType("multipart/related");
      header.setType(packinfo.getIsEnableSignature() ? "multipart/signed" : "multipart/related");
    }
    return header.getProperties();
  }

  protected String getClassName()
  {
    return this.getClass().getName();
  }

  public Message pack(PackagingInfo info, Message message)
    throws PackagingException
  {
    final String METHOD_NAME= "pack()";
    try
    {
      Logger.debugLog(getClassName(), METHOD_NAME, "Packinginfo" + info);

      //RNPackInfo[] packinfoArray= new RNPackInfo[1];
      //File[] files= info.getPayLoadToPackage();
      File[] files= message.getPayLoad();

      RNPackInfo packInfo = new RNPackInfo();
      
      //230204 Added check for non existing files.
      if (files != null && files.length>0 && files[0] !=null)
      {
        packInfo = (RNPackInfo) packInfo.deserialize(files[0].getAbsolutePath());
        if (this.RNVersion.equals("unspecified"))
        {
          init(packInfo.getRnifVersion());
        }
        File[][] res= _packager.packDoc(packInfo, files);
        //info.setPackagedPayLoad(res);
        message.setPayLoad(res[0]);
        message.getMessageHeaders().put(IRNHeaderConstants.AUDIT_FILE_NAME,res[1][0].getName());
      }

      //Hashtable messageHeaders = new Hashtable(message.getMessageHeaders());
      //messageHeaders.putAll(message.getCommonHeaders());  //To be defensive ..
      //Hashtable headers= getDefaultPackagedHeader(info); //But these headers are not used...

      Hashtable messageHeaders= getHTTPHeaders(packInfo, new Hashtable()); //Since this method dose not do any processing with pass in hashtable.
      Map reqMessageHeader = message.getMessageHeaders();
      if (reqMessageHeader == null)
        reqMessageHeader = new HashMap();
      reqMessageHeader.putAll(messageHeaders);
      message.setMessageHeaders(reqMessageHeader);
      //info.setEnvelopeHeader(headers); //Set's the Consolidated headers.
      //info.setAdditionalHeader(headers);

      return message;
    }
    catch (Exception ex)
    {
      Logger.warnLog(getClassName(), METHOD_NAME, "Exception in formating&Packaging", ex);
      throw new PackagingException("Exception in formating&Packaging", ex);
    }

  }

  public Message unPack(PackagingInfo info, Message message)
    throws PackagingException
  {
    final String METHOD_NAME= "unPack()";
    Message unPackMessage = new Message();
    try
    {
      Logger.debugLog(getClassName(), METHOD_NAME, "Packinfo" + info);

      //     Hashtable defaultHeader = new Hashtable();
      //     defaultHeader.put(ITransportConstants.EVENT_ID,"8900");
      //getDefaultUnPackagedHeader(arg0)(info);
      if (message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID)== null)
        message.getCommonHeaders().put(ICommonHeaders.MSG_EVENT_ID,RN_EVENT_ID);

      //String[] eventId = getDefaultUnPackagedHeader(info);
      //Arrays.fill(eventId,0,1,RN_EVENT_ID);

      /* This following block is commented, in order to get DefaultPackagigHeader
      from AbstractPackagingHandler - 07OCT2003.
      String[] eventId= new String[] { "8900", null, null, null, null, null, null, null }; //8
      */
      //info.setDefaultUnPackagedHeader(eventId);
      RNPackInfo packinfo= new RNPackInfo();
      //byte[] payload= info.getPayLoadToUnPackage();
      File[][] res = {{}};
      byte[] payload= message.getPayLoadData(); //byte payload
      File[] uDocFile = message.getPayLoad();   //File payload
      if (payload != null && payload.length>0)
      { 
        File uDoc= XMLUtil.createTempFile("UnPackage", payload);
        res = _depackager.unpackDocument(uDoc, packinfo);
        
        //25012005 Mahesh : Added to set SIGNATURE_VERIFY_EXCEPTION to message header        
        if(packinfo.getSigVerificationException()!=null)
          message.getMessageHeaders().put(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION,packinfo.getSigVerificationException().getExType()+"-"+packinfo.getSigVerificationException().getDetails());
          
        message.getMessageHeaders().put(IRNHeaderConstants.AUDIT_FILE_NAME,res[1][0].getName());

      }
      else if (uDocFile != null && uDocFile.length>0 && uDocFile[0] != null)
      {
        //File[] uDoc = message.getPayLoad();
        res = _depackager.unpackDocument(uDocFile[0], packinfo);
        
        //25012005 Mahesh : Added to set SIGNATURE_VERIFY_EXCEPTION to message header
        if(packinfo.getSigVerificationException()!=null)
          message.getMessageHeaders().put(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION,packinfo.getSigVerificationException().getExType()+"-"+packinfo.getSigVerificationException().getDetails());
        
        message.getMessageHeaders().put(IRNHeaderConstants.AUDIT_FILE_NAME,res[1][0].getName());

      }
      //info.setUnPackagedPayLoad(res);

      //Hashtable extraHeader= new Hashtable();
      message.getMessageHeaders().put(IRNHeaderConstants.SENDER_DUNS,
        packinfo.getSenderGlobalBusIdentifier());

      message.getMessageHeaders().put(IRNHeaderConstants.RNDOC_FLAG, Boolean.TRUE);

      //info.setAdditionalHeader(extraHeader);
      //A Defensive approch for packed message.
      unPackMessage.setCommonHeaders(message.getCommonHeaders());
      unPackMessage.setMessageHeaders(message.getMessageHeaders());
      unPackMessage.setPayLoad(res[0]);
      unPackMessage.setData(message.getData());
      return unPackMessage;
    }
    catch (Exception ex)
    {
      Logger.warnLog(getClassName(), METHOD_NAME, "Exception in formating&Packaging", ex);
      throw new PackagingException("Exception in formating&Packaging", ex);
    }
  }

}