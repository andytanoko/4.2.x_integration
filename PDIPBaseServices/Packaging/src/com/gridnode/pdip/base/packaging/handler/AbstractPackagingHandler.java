/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractPackagingHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 04-OCT-2002    Jagadeesh           Created.
 * 06-OCT-2003		Jagadeesh						Modified : Added New Headers to support
 * 																		BE-UDDI Support.
 * 21-OCT-2003	  Jagadeesh		        Modified : Fix GNDB00015739. Added check for !null of DocumentType Header.
 * 17-DEC-2003    Jagadeesh           Modified : To add two additional methods for packaging,
 *                                    this will be made in sync later when RNIF and AS2 Packaging
 *                                    are changed respectively.
 *
 */

package com.gridnode.pdip.base.packaging.handler;

import com.gridnode.pdip.base.packaging.helper.IPackagingConstants;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.packaging.helper.PackagingLogger;
import com.gridnode.pdip.base.packaging.exception.PackagingServiceException;
import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.IPackagingConfig;

import com.gridnode.pdip.base.transport.helpers.ITransportConstants;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

import com.gridnode.pdip.framework.messaging.Message;

import java.util.Hashtable;

/**
 * This class is extended by Concrete PackagingHandlers:-(Ex: DefaultPackagingHandler,
 * RN<<>>PackagingHandler). This class provides basic service to envelope the header.
 */

public abstract class AbstractPackagingHandler implements IPackagingHandler
{
  private static final String CLASS_NAME = "AbstractPackagingHandler";

  /**
   * This method is a template implementation, where in the default header envelope
   * is constructed.
   *
   * Note:- All Concrete PackagingHandlers should invoke this method to get Default
   * Header Envelope. And Key-Value Pair is a NotNull Value
   *
   * @param info - PackagingInfo = DataTransferObject.
   * @return - Hashtable of default header.
   * @throws PackagingServiceException - thrown when cannot add to Default Header.
   */

  public Hashtable getDefaultPackagedHeader(PackagingInfo info)
    throws PackagingServiceException
  {
    Hashtable envelopeheader = null;
    try
    {

      if ((envelopeheader = info.getEnvelopeHeader()) == null)
      {
        envelopeheader = new Hashtable();
      }

      envelopeheader.put(ITransportConstants.EVENT_ID, info.getEventId());
      envelopeheader.put(
        ITransportConstants.TRANSACTION_ID,
        info.getTransactionId());
      envelopeheader.put(
        ITransportConstants.RECEIPENT_NODE_ID,
        info.getRecepientNodeId() == null
          ? new String("0")
          : info.getRecepientNodeId());
      envelopeheader.put(
        ITransportConstants.SENDER_NODE_ID,
        info.getSenderNodeId() == null
          ? new String("0")
          : info.getSenderNodeId());

      envelopeheader.put(
        ITransportConstants.EVENT_SUB_ID,
        info.getEventSubId() == null ? new String("0") : info.getEventSubId());

      envelopeheader.put(
        ITransportConstants.PROCESS_ID,
        info.getProcessId() == null ? new String("0") : info.getProcessId());

      envelopeheader.put(
        ITransportConstants.FILE_ID,
        info.getFileId() == null
          ? ITransportConstants.UNDEFINED_FILE_ID
          : info.getFileId());
      envelopeheader.put(
        IPackagingConstants.CHANNEL_NAME,
        info.getChannelName() == null
          ? IPackagingConstants.UNDEFINDED_CHANNEL
          : info.getChannelName());
      String packageType =
        getPackagingConfiguration().getString(info.getEnvelopeType());
      if (null != packageType)
        envelopeheader.put(ITransportConstants.PACKAGE_TYPE_KEY, packageType);

      envelopeheader.put(
        ITransportConstants.SENDER_UUID,
        info.getSenderUUID() == null ? new String("0") : info.getSenderUUID());
      envelopeheader.put(
        ITransportConstants.SENDER_QUERY_URL,
        info.getSenderQueryURL() == null
          ? ITransportConstants.UNDEFINED_URL
          : info.getSenderQueryURL());
      envelopeheader.put(
        ITransportConstants.RECEIPENT_UUID,
        info.getRecipientUUID() == null
          ? new String("0")
          : info.getRecipientUUID());
      envelopeheader.put(
        ITransportConstants.RECEIPENT_QUERY_URL,
        info.getRecipeintQueryURL() == null
          ? ITransportConstants.UNDEFINED_URL
          : info.getRecipeintQueryURL());
      envelopeheader.put(
        ITransportConstants.DOCUMENT_TYPE,
        info.getDocType() == null
          ? ITransportConstants.UNDEFINED_DOC_TYPE
          : info.getDocType());
      return envelopeheader;
    } catch (Exception ex)
    {
      PackagingLogger.infoLog(
        CLASS_NAME,
        "getDefaultHeader()",
        "Could Not Package Default Header");
      throw new PackagingServiceException(
        "Could Not Package Default Header",
        ex);
    }
  }

  /**
   * Returns Default Header as expected by the BL, the order is
   * not consistent while Sending and Receiving.
   *
   * In later versions, the order will be in sync with send and receive process.
   * (i.e after Refactoring shld use helper class for creating package and unpackage
   *  headers ).
   *
   * @param info PackagingInfo, valueobject composing Packaging Information.
   * @return String[] of defaultHeader
   * @throws PackagingServiceException thrown when cannot unpackage the header.
   *
   * @author Jagadeesh.
   * @since 2.0
   */

  public String[] getDefaultUnPackagedHeader(PackagingInfo info)
    throws PackagingServiceException
  {
    Hashtable unpackagedHeader = null;
    if (info != null)
    {
      unpackagedHeader = info.getUnPackagedHeader();
      String[] unpackHeader = new String[14];
      try
      {
        unpackHeader[0] =
          (String) unpackagedHeader.get(ITransportConstants.EVENT_ID);
        unpackHeader[1] =
          (String) unpackagedHeader.get(ITransportConstants.TRANSACTION_ID);
        unpackHeader[2] =
          (String) unpackagedHeader.get(ITransportConstants.RECEIPENT_NODE_ID);
        unpackHeader[3] =
          (String) unpackagedHeader.get(ITransportConstants.SENDER_NODE_ID);
        unpackHeader[4] =
          (String) unpackagedHeader.get(ITransportConstants.EVENT_SUB_ID);
        unpackHeader[5] =
          (String) unpackagedHeader.get(ITransportConstants.PROCESS_ID);
        unpackHeader[6] =
          (String) unpackagedHeader.get(ITransportConstants.FILE_ID);
        unpackHeader[7] =
          (String) unpackagedHeader.get(IPackagingConstants.CHANNEL_NAME);
        unpackHeader[8] =
          (String) unpackagedHeader.get(ITransportConstants.PACKAGE_TYPE_KEY);
        unpackHeader[9] =
          (String) unpackagedHeader.get(ITransportConstants.SENDER_UUID);
        unpackHeader[10] =
          (String) unpackagedHeader.get(ITransportConstants.SENDER_QUERY_URL);
        unpackHeader[11] =
          (String) unpackagedHeader.get(ITransportConstants.RECEIPENT_UUID);
        unpackHeader[12] =
          (String) unpackagedHeader.get(
            ITransportConstants.RECEIPENT_QUERY_URL);
        unpackHeader[13] =
          (String) unpackagedHeader.get(ITransportConstants.DOCUMENT_TYPE);

        for (int i = 0; i < unpackHeader.length; i++)
          PackagingLogger.infoLog(
            "AbstractPackagingHandler",
            "getDefaultUnPackagedHeader()",
            "Header = [ " + unpackHeader[i] + " ] ");
        return unpackHeader;
      } catch (Exception ex)
      {
        PackagingLogger.warnLog(
          CLASS_NAME,
          "getDefaultUnPackagedHeader()",
          "Could Not UnPackage Default Header");
        throw new PackagingServiceException("Could not UnPackage Default Header");
      }
    }
    return null;
  }

  private Configuration getPackagingConfiguration()
  {
    return ConfigurationManager.getInstance().getConfig(
      IPackagingConfig.CONFIG_NAME);
  }

  public Message pack(PackagingInfo info, Message message)
    throws PackagingException
  {
    return null;
  }

  public Message unPack(PackagingInfo info, Message message)
    throws PackagingException
  {
    return null;
  }


}