/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNTransportHeader.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 * Nov 14 2003    Jagadeesh                Modified: Changed PACKAGE_TYPE_KEY
 *                                         to commonheaders payload.type.
 * May 10 2010    Tam Wei Xiang            #1463 - added method isRnif11()
 */


package com.gridnode.pdip.base.transport.helpers;

import java.util.Hashtable;

import com.gridnode.pdip.framework.messaging.ICommonHeaders;



public class GNTransportHeader extends GTConfigFile
{
  public GNTransportHeader()
  {
    super();
  }

  public GNTransportHeader(Hashtable property)
  {
    super(property);
  }

  public GNTransportHeader(String[] args)
  {
    super(args);
  }

  public String getPackageType()
  {
    return getProperty(ICommonHeaders.PAYLOAD_TYPE);
   // ITransportConstants.PACKAGE_TYPE_KEY);
  }

  public String getSenderPackageType()
  {
    return getProperty(ITransportConstants.PACKAGE_TYPE_SENDER_KEY);
  }


  public void setSenderPackageType(String senderPackageType)
  {
    setProperty(ITransportConstants.PACKAGE_TYPE_SENDER_KEY, senderPackageType);
  }

  public void setPackageType(String PackageType)
  {
    /** @todo Need to change this header from common headers to transport specific headers */
    /**
     * This header property is a transport property. This property is transformed from
     * ICommonHeaders.PAYLOAD_TYPE (NONE,RNIF1,RNIF2) to a transport specific property,
     * which is PACKAGE_TYPE_KEY and corresponds to value - GTAS_PACKAGE,RNIF_PACKAGE,...
     */
    setProperty(ICommonHeaders.PAYLOAD_TYPE,PackageType);
    //ITransportConstants.PACKAGE_TYPE_KEY, PackageType);
  }

  public void setSenderPayloadMessage()
  {
    setSenderPackageType(ITransportConstants.PACKAGE_TYPE_SENDER_PAYLOAD);
  }

  public void setSenderCMDMessage()
  {
    setSenderPackageType(ITransportConstants.PACKAGE_TYPE_SENDER_CMD);
  }

  public boolean isSenderCMD_SetTrustStore()
  {
    return getSenderCMD().equals(ITransportConstants.SENDER_CMD_SET_TRUSTSTORE);
  }

  public boolean isSenderCMD_SetKeyStore()
  {
    return getSenderCMD().equals(ITransportConstants.SENDER_CMD_SET_KEYSTORE);
  }

  public void setSenderCMD_SetTrustStore()
  {
    setSenderCMD(ITransportConstants.SENDER_CMD_SET_TRUSTSTORE);
  }

  public void setSenderCMD_SetKeyStore()
  {
    setSenderCMD(ITransportConstants.SENDER_CMD_SET_KEYSTORE);
  }

  public void setSenderCMD(String cmd)
  {
    setSenderCMDMessage();
    setProperty(ITransportConstants.SENDER_CMD_KEY, cmd);
  }

  public String getSenderCMD()
  {
    if(!isSenderCMDMessage())
      return "";
    return getProperty(ITransportConstants.SENDER_CMD_KEY);
  }

  public void setGTASMessage()
  {
    setPackageType(ITransportConstants.PACKAGE_TYPE_GTAS);
  }

  public void setRNMessage()
  {
      setPackageType(ITransportConstants.PACKAGE_TYPE_RosettaNet);
  }

  public boolean isSenderPayloadMessage()
  {
    return getSenderPackageType().equals(ITransportConstants.PACKAGE_TYPE_SENDER_PAYLOAD);
  }

  public boolean isSenderCMDMessage()
  {
    return getSenderPackageType().equals(ITransportConstants.PACKAGE_TYPE_SENDER_CMD);
  }

  public boolean isGTASMessage()
  {
    return getPackageType().equals(ITransportConstants.PACKAGE_TYPE_GTAS);
  }

  public boolean isRNMessage()
  {
    return getPackageType().equals(ITransportConstants.PACKAGE_TYPE_RosettaNet);
  }

  public boolean isNativeRNMessage()
  {
    //rnif 1.1
    if(getContentType().indexOf("application/x-rosettanet-agent") >= 0)
      return true;
    //rnif2.0
    return   getRNVersion().length() > 0;
  }
  
  /**
   * TWX #1463 - check whether the header is RNIF1.1
   * @return
   */
  public boolean isRNIF11()
  {
    return getContentType().indexOf("application/x-rosettanet-agent") >= 0;
  }

  public String getRNVersion()
  {
    return getProperty(IRNHeaderConstants.RN_VERSION_KEY);
  }

  public void setRNVersion(String RNVersion)
  {
    setProperty(IRNHeaderConstants.RN_VERSION_KEY, RNVersion);
  }

  public void setRNSyncMessage(boolean isSyn)
  {
    if(isSyn)
      setProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY, IRNHeaderConstants.RN_RESPONSE_TYPE_SYNC);
    else
      setProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY, IRNHeaderConstants.RN_RESPONSE_TYPE_ASYNC);
  }

  public boolean isRNReplyMessage()
  {
    return getBooleanProperty(IRNHeaderConstants.GN_MESSAGE_RESPONSE_KEY);
  }

  public void setRNReplyMessage(boolean response)
  {
    setBooleanProperty(IRNHeaderConstants.GN_MESSAGE_RESPONSE_KEY, response);
  }


  public boolean isRNSyncMessage()
  {
      String RNSync = getProperty(IRNHeaderConstants.RN_RESPONSE_TYPE_KEY);
      //boolean isRNSync = false;
      if(RNSync != null && RNSync.equals(IRNHeaderConstants.RN_RESPONSE_TYPE_SYNC))
          return true;
      else
         return false;
  }

  public String  getRNSyncMessageID()
  {
    return getProperty(IRNHeaderConstants.GN_MESSAGE_ID_KEY);
  }

  public void    setRNSyncMessageID(String ID)
  {
    setProperty(IRNHeaderConstants.GN_MESSAGE_ID_KEY, ID);
  }

  public void setContentType(String contentType)
  {
    setProperty(IRNHeaderConstants.CONTENT_TYPE_KEY, contentType);
  }

  public String getContentType()
  {
    return getProperty(IRNHeaderConstants.CONTENT_TYPE_KEY);
  }

  public void setType(String type)
  {
    setProperty(IRNHeaderConstants.TYPE_KEY, type);
  }

  public String getType()
  {
      return getProperty(IRNHeaderConstants.TYPE_KEY);
  }
}