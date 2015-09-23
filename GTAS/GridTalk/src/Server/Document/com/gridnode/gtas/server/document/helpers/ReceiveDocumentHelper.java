/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Koh Han Sing        Created
 * Jan 17 2003    Neo Sok Lay         Set SendEnd and ReceiveEnd timestamps.
 *                                    Cache the ReceiveTrxId for sending ack
 *                                    later on.
 * Apr 03 2003    Goh Kan Mun         Retrieve the correct element for send end timestamp.
 *                                    Timestamp is the third elements of the data.
 * Oct 01 2003    Neo Sok Lay         Cater for the case when Normal document is
 *                                    received from Non-GT partner via SOAP.
 * Oct 13 2003    Neo Sok Lay         Fixed: Set RecipientBizEntityUuid/RegistryQueryUrl
 *                                    incorrectly from SENDER_UUID/SENDER_REGISTRY_QUERY_URL
 *                                    in createGdocFromHeader().
 *                                    Do not retrieve Partner when createGdocFromHeader()
 * Nov 05 2003    Neo Sok Lay         Create GridDoc from additionalHeaders in addition to header.
 *
 * Jan 09 2004    Jagadeesh           Added : Support for transformation of GDoc 2.x to GDoc 1.x
 * Jan 26 2004    Neo Sok Lay         Modify processNormalDoc() to call out for receiving backward compatible
 *                                    Rnif doc.
 *                                    Modify receiveRnifDoc() to pass isTransformationReq and receivedGdoc parameters.
 * Feb 04 2004    Neo Sok Lay         GNDB00017046: check for empty ProcessDefId.
 *                                    GNDB00017038: handle Attachments from GT1 case.
 * Jun 08 2004    Neo Sok Lay         Modified: receiveNormalDoc() to peep the root element of received
 *                                    "Gdoc" file to determine whether it's really a GridDocument file or
 *                                    RNPackinfo file, rather then depend on Castor to complain.
 * Jan 25 2005    Mahesh              Added new receiveRnifDoc method to pass sigVerifyExMsg
 * Aug 29 2005	  Sumedh			  For method: processRnifDoc - add handling attachments functionality - successful tested.
 *									  For method: receiveNormaldoc - try to add hanlding attachments functionality 
 *									  but when testing this functionality, GTAS log shows error: 
 *									  cannot handle unknown packaging type.
 *									  This error has to be fixed before this method can be tested.
 *									  Handling of attachments code remains in this method.
 * Mar 22 2006    Neo Sok Lay         GNDB00026249: Get sender partner/be/duns and recipient be/duns from
 *                                    ChannelReceiveHeader.
 * Oct 25 2006    Tam Wei Xiang       modified method : processRnifDoc(..) to add the tracingID into gdoc
 * Jan 19 2007    Neo Sok Lay         Change to use common Notifier to broadcast notifications.
 * Oct 05 2010    Tam Wei Xiang       #1897: Modify getRecipientPartnerInfo(..) Derive Partner BE for
 *                                           web service end point.                                   
 */
package com.gridnode.gtas.server.document.helpers;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import com.gridnode.gtas.server.document.exceptions.PartnerNotFoundException;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.AttachmentInfo;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.PartnerInfo;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.ReceiveRNDocNotification;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelServiceDelegate;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.db.XmlObjectDeserializer;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.gtas.server.document.exceptions.*;
public class ReceiveDocumentHelper
{

  public static void receiveRnifDoc(
    String[] header,
    String[] dataReceived,
    File[] filesReceived,
    boolean isTransformationReq,
    GridDocument receivedGdoc,
    String sigVerifyExMsg)
    throws Exception
  {
    ReceiveRNDocNotification notification =
      new ReceiveRNDocNotification(header, dataReceived, filesReceived, isTransformationReq, receivedGdoc,sigVerifyExMsg);
    //ReceiveRNDocNotifier.getInstance().broadcast(notification);
    Notifier.getInstance().broadcast(notification); //NSL20070119
  }

  public static void receiveRnifDoc(
    String[] header,
    String[] dataReceived,
    File[] filesReceived,
    boolean isTransformationReq,
    GridDocument receivedGdoc)
    throws Exception
  {
    ReceiveRNDocNotification notification =
      new ReceiveRNDocNotification(header, dataReceived, filesReceived, isTransformationReq, receivedGdoc);
    //ReceiveRNDocNotifier.getInstance().broadcast(notification);
    Notifier.getInstance().broadcast(notification); //NSL20070119
  }

  public static void receiveRnifDoc(
    String[] header,
    String[] dataReceived,
    File[] filesReceived,
    int from_route,
    GridDocument receivedGdoc)
    throws Exception
  {
    ReceiveRNDocNotification notification =
      new ReceiveRNDocNotification(header, dataReceived, filesReceived, from_route, receivedGdoc);
    //ReceiveRNDocNotifier.getInstance().broadcast(notification);
    Notifier.getInstance().broadcast(notification); //NSL20070119
  }


  public static GridDocument processRnifDoc(
    GridDocument gdoc,
    String[] header,
    String[] dataReceived,
    File[] filesReceived)
    throws Exception
  {
    Logger.debug("[ReceiveDocumentHelper.processRnifDoc] Start");
    File udocFile = filesReceived[0];

    //031001NSL
    gdoc.setSenderBizEntityUuid(header[ChannelReceiveHeader.SENDER_UUID_POS]);
    gdoc.setSenderRegistryQueryUrl(header[ChannelReceiveHeader.SENDER_REGISTRY_QUERY_URL]);
    gdoc.setRecipientBizEntityUuid(header[ChannelReceiveHeader.RECIPIENT_UUID_POS]);
    gdoc.setRecipientRegistryQueryUrl(header[ChannelReceiveHeader.RECIPIENT_REGISTRY_QUERY_URL]);

    setReceiveTrxId(gdoc, header);
    setUdocInfo(gdoc, udocFile);
    setTimestamps(gdoc, dataReceived);
    
    //25102006 TWX
    gdoc.setTracingID(header[ChannelReceiveHeader.TRACING_ID]);
    
    String eventId = header[ChannelReceiveHeader.EVENT_ID_POS];
    if (IDocumentEvents.SEND_GRIDDOC.equals(eventId))
      gdoc.setSenderRoute(GridDocument.ROUTE_DIRECT);
    else if (IDocumentEvents.DOWNLOAD_GRIDDOC.equals(eventId))
      gdoc.setSenderRoute(GridDocument.ROUTE_GRIDMASTER);

    /*020117NSL
    String udocFilename = udocFile.getName();
    String tempUdocFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

    gdoc.setUdocFilename(udocFilename+":"+tempUdocFilename);
    gdoc.setUdocFileSize(new Long(Math.round(udocFile.length()/1000)));
    */
    
    Logger.debug("ReceiveDocumentHelper::processRnifDoc");
    handleAttachment(gdoc, filesReceived);
    return gdoc;
  }
  
  private static void handleAttachment(GridDocument gdoc, File[] filesReceived)
  		throws CreateAttachmentException
  {
  	Logger.debug("[ReceiveDocumentHelper::handleAttachment] START");
    //if (gdoc.hasAttachment().booleanValue() && filesReceived.length>1)
    debugFilesReceived(filesReceived);
    if (filesReceived.length >= 2)
    {
      Logger.debug("Has attachment");
      gdoc.setHasAttachment(Boolean.TRUE);
      ArrayList<Long> attachmentUids = new ArrayList<Long>();
      for (int i = 1; i < filesReceived.length; i++)
      {
        File attachmentFile = filesReceived[i];
        String filename = attachmentFile.getName();
        Long uid =
          AttachmentHelper.createAttachment(
            attachmentFile,
            filename,
            Boolean.FALSE,
            null,
            "");
        Logger.debug("uid = " + uid);
        attachmentUids.add(uid);
      }
      gdoc.setAttachments(attachmentUids);
      gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
    }
    Logger.debug("handleAttachment END");

//    deleteFilesReceived(filesReceived);
	/* only deletes filesReceived[0], don't delete attachments */
	filesReceived[0].delete();
    Logger.debug("[ReceiveDocumentHelper::handleAttachment] END");
  }
  
  private static void debugFilesReceived(File[] filesReceived)
  {
  	Logger.debug("filesReceived.length = " + filesReceived.length);
  	for (int i = 0; i < filesReceived.length; i++)
  	{
  		Logger.debug(i + ": " + filesReceived[i].getPath());
  	}
  }

  public static GridDocument receiveNormalDoc(
    String[] header,
    String[] dataReceived,
    File[] filesReceived,
    Hashtable additionalHeaders)
    throws Exception
  {
    Logger.debug("[ReceiveDocumentHelper.receiveNormalDoc] Start");
    File udocFile = filesReceived[0];
    File gdocFile = null;

    if (filesReceived.length > 1)
      gdocFile = filesReceived[1];

    Logger.debug(
      "[ReceiveDocumentHelper.receiveDoc] udocFile = "
        + udocFile.getAbsolutePath());
    Logger.debug(
      "[ReceiveDocumentHelper.receiveDoc] gdocFile = "
        + (gdocFile==null?null:gdocFile.getAbsolutePath()));

    //int attachmentStartIndex = -1;
    GridDocument gdoc = new GridDocument();
    if (gdocFile != null)
    {
      int from_route=-1;
      //boolean transformationReq = false;
      //JD:15/01/04
      if (header[ChannelReceiveHeader.SENDER_NODEID_POS] != null)
      {
         //Since Master Channel is the only :: factor to know, we are receiving from GT1/GT2.
         ChannelInfo info = ChannelServiceDelegate.getMasterChannelInfoByRefId(
              header[ChannelReceiveHeader.SENDER_NODEID_POS]);
         if (info != null)
         {
            String implVersion = info.getTptCommInfo().getTptImplVersion();
            //If BackwardCompatible Channel Ex:- GT1.x Channel.Transform GDOC to GT1.x
            if (implVersion != null && implVersion.startsWith("02"))
            {
                Logger.debug(
                  "[ReceiveDocumentHelper.receiveDoc][Transforming Gdoc]["+info);
               gdocFile = TransformationHelper.transformDocument(gdocFile,
                                TransformationHelper.INBOUND_TRANSFORMATION);
               from_route = IDocumentConstants.ROUTE_GT1_GM;
               //transformationReq = true;
            }
         }

      }

      //040608NSL: Peep the xml file root element
      if (XMLDelegate.checkRootElement(gdocFile, "GridDocument"))
      {
        Logger.log("[ReceiveDocumentHelper.receiveNormalDoc()][B4 Deserializing GridDocument]");
        gdoc = (GridDocument) gdoc.deserialize(gdocFile.getAbsolutePath());
      }
      else
      {
        // assume it is RNIF
        Logger.log("[ReceiveDocumentHelper.receiveNormalDoc] Not a Gdoc, should be RBM from GM");
        receiveRnifDoc(header, dataReceived, filesReceived, IDocumentConstants.ROUTE_GM, gdoc);
        return null;
      }
      
      /*040608NSL: Castor doesn't complain anymore!!
      try
      {
        Logger.log("[ReceiveDocumentHelper.receiveNormalDoc()][B4 Deserializing GridDocument]");
        gdoc = (GridDocument) gdoc.deserialize(gdocFile.getAbsolutePath());
      }
      catch (Throwable ex)
      {
        Logger.log("[ReceiveDocumentHelper.receiveNormalDoc()][Exception while Deserializing GridDocument "+
                   "Setting from_route to ROUTE_GM]");
        receiveRnifDoc(header, dataReceived, filesReceived, IDocumentConstants.ROUTE_GM, gdoc);
        return null;
      }
      */
      
      //040126NSL: check if need to handle backward compatible Rnif doc

      if (from_route>0 && gdoc.getProcessDefId() !=null && gdoc.getProcessDefId().trim().length()>0)
      {
        Logger.debug("[ReceiveDocumentHelper.receiveNormalDoc] Calling for handling backward compatible Rnif Doc");
        receiveRnifDoc(header, dataReceived, filesReceived, from_route, gdoc);
        return null; //set to null to disable further processing
      }

     /* if (transformationReq && gdoc.getProcessDefId()!=null && gdoc.getProcessDefId().trim().length()>0) //check for empty string
      {
        Logger.debug("[ReceiveDocumentHelper.receiveNormalDoc] Calling for handling backward compatible Rnif Doc");
        receiveRnifDoc(header, dataReceived, filesReceived, true, gdoc);
        return null; //set to null to disable further processing
      }
      */

      //attachmentStartIndex = (from_route == IDocumentConstants.ROUTE_GT1_GM? 2 : 2); // GT1 version contain extra "null" element?? missing?
    }
    else // in case the sender is Non-GT that does not give a GridDocument
      gdoc = createGdocFromHeader(header, additionalHeaders);

    setReceiveTrxId(gdoc, header);
    setUdocInfo(gdoc, udocFile);
    setTimestamps(gdoc, dataReceived);
    /*020117NSL
    String udocFilename = udocFile.getName();
    String tempUdocFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

    gdoc.setUdocFilename(udocFilename+":"+tempUdocFilename);
    gdoc.setUdocFileSize(new Long(Math.round(udocFile.length()/1000)));
    */

//    if (gdoc.hasAttachment().booleanValue() && filesReceived.length>attachmentStartIndex)
//    {
//      gdoc.setAttachmentLinkUpdated(Boolean.FALSE);
//      gdoc = processAttachments(gdoc, filesReceived, attachmentStartIndex);
//    }
//
//    deleteFilesReceived(filesReceived);

	Logger.debug("ReceiveDocumentHelper::receiveNormalDoc");
	handleAttachment(gdoc, filesReceived);
//    Logger.debug("[ReceiveDocumentHelper.receiveNormalDoc] End");
    return gdoc;
  }
  /*
  private static GridDocument performGdocTransformation(GridDocument gdoc,File gdocFile)
    throws Exception
  {
    try
    {
      File transformedGdoc = getTransformedGDocFile(getInBoundMappingFileName(),gdocFile);
      gdoc = (GridDocument) gdoc.deserialize(transformedGdoc.getAbsolutePath());
      return gdoc;
    }
    catch(FileAccessException fileEx)
    {
      Logger.err("[ReceiveDocumentHelper][receiveNormalDoc()]"+
      "[Could Not Transform BackwardCompatible GridDocument with FileName]"+getInBoundMappingFileName(),fileEx);
    }
    catch(XMLException xmlEx)
    {
      Logger.err("[ReceiveDocumentHelper][receiveNormalDoc()][Could Not Transform BackwardCompatible GridDocument]",xmlEx);
      throw new Exception("[Could Not Transform BackwardCompatible GridDocument]"+
        xmlEx.getMessage());
    }
    catch(ServiceLookupException slEx)
    {
      Logger.err("[ReceiveDocumentHelper][receiveNormalDoc()][Could Not Lookup XMLServiceFacade]",slEx);
      throw new Exception("[Could Not Lookup XMLServiceFacade]"+
        slEx.getMessage());
    }
    return null;
  }*/
  /*
  private static File getTransformedGDocFile(String mappingFileName,File gdocFile)
    throws XMLException,FileAccessException,ServiceLookupException
  {

      File xslFile = FileUtil.getFile(IMapperPathConfig.PATH_XSL,mappingFileName);
      File transformedGdoc = XMLDelegate.getManager().transform(
              xslFile.getAbsolutePath(),
              gdocFile.getAbsolutePath()
              );
      if (transformedGdoc == null)
        throw new XMLException("[BackwardCompatible Transformation Unsuccessful]");
      else
        return transformedGdoc;
  }*/
  /*
  private static String getInBoundMappingFileName()
  {
    return getDocumentConfig().getString(
      IDocumentConfig.BACKWARDCOMPATIBLE_INBOUND_MAPPING);
  }


  private static String getOutBoundMappingFileName()
  {
    return getDocumentConfig().getString(
      IDocumentConfig.BACKWARDCOMPATIBLE_OUTBOUND_MAPPING);
  }*/

  /*
  private static Configuration getDocumentConfig()
  {

     return ConfigurationManager.getInstance().getConfig(
                          IDocumentConfig.CONFIG_NAME);
  }*/


  /**
   * Create GridDocument based on the header information array received.
   *
   * @param header The received Header information, as specified in ChannelReceiveHeader.
   * @return The created GridDocument.
   * @throws Exception Unable to determine the Partner information.
   * @see ChannelReceiveHeader.
   */
  protected static GridDocument createGdocFromHeader(String[] header, Hashtable additionalHeaders)
    throws Exception
  {
    GridDocument gdoc = new GridDocument();

    //TODO determine sender & recipient from additonal headers
    /*031105NSL
    PartnerInfo senderPInfo =
      BizRegDelegate.getPartnerInfo(
        header[ChannelReceiveHeader.SENDER_UUID_POS],
        header[ChannelReceiveHeader.SENDER_REGISTRY_QUERY_URL],
        false);
    PartnerInfo recipientPInfo =
      BizRegDelegate.getPartnerInfo(
        header[ChannelReceiveHeader.RECIPIENT_UUID_POS],
        header[ChannelReceiveHeader.RECIPIENT_REGISTRY_QUERY_URL],
        false);
    */
    PartnerInfo senderPInfo = getSenderPartnerInfo(header, additionalHeaders);
    PartnerInfo recipientPInfo = getRecipientPartnerInfo(header, additionalHeaders);

    gdoc.setSenderBizEntityId(senderPInfo.getBizEntityID());
    gdoc.setSenderBizEntityUuid(senderPInfo.getBizEntityUuid());
    gdoc.setSenderNodeId(senderPInfo.getNodeID());
    gdoc.setSenderRegistryQueryUrl(senderPInfo.getRegistryQueryUrl());
    gdoc.setSenderRoute(GridDocument.ROUTE_DIRECT);

    gdoc.setRecipientBizEntityId(recipientPInfo.getBizEntityID());
    gdoc.setRecipientBizEntityUuid(recipientPInfo.getBizEntityUuid());
    gdoc.setRecipientNodeId(recipientPInfo.getNodeID());
    gdoc.setRecipientRegistryQueryUrl(recipientPInfo.getRegistryQueryUrl());
    gdoc.setRecipientChannelName(header[ChannelReceiveHeader.CHANNEL_NAME_POS]);
    gdoc.setUdocDocType(header[ChannelReceiveHeader.DOCUMENT_TYPE_POS]);

    //11102010 #1897 TWX
    gdoc.setTracingID(header[ChannelReceiveHeader.TRACING_ID]);
    
    gdoc.setCreateBy("System");
    return gdoc;
  }

  private static PartnerInfo getRecipientPartnerInfo(String[] header, Hashtable additionalHeaders)
    throws Exception
  {
  	/*NSL20060322
    String beId = (String)additionalHeaders.get(ICommonHeaders.RECIPENT_BE_ID);
    String duns = (String)additionalHeaders.get(ICommonHeaders.RECIPENT_BE_DUNS);
    */
    String beId = header[ChannelReceiveHeader.RECIPIENT_BEID_POS];
    String duns = header[ChannelReceiveHeader.RECIPIENT_DUNS_POS];
    String gnId = header[ChannelReceiveHeader.RECIPIENT_NODEID_POS];
    String uuid = header[ChannelReceiveHeader.RECIPIENT_UUID_POS];
    String regUrl = header[ChannelReceiveHeader.RECIPIENT_REGISTRY_QUERY_URL];
    String incomingConnectionType = header[ChannelReceiveHeader.INCOMING_CONNECTION_TYPE]; //TWX 20101005 #1897
    
    PartnerInfo partnerInfo = null;
    if (!isBlank(beId) && !isBlank(gnId))
    {
      Long gnIdInt = Long.valueOf(gnId);
      partnerInfo = new PartnerInfo();
      partnerInfo.setBizEntityID(beId);
      partnerInfo.setNodeID(gnIdInt);
    }
    else if (!isBlank(duns))
    {
      partnerInfo = BizRegDelegate.getPartnerInfoByDuns(duns, false);
    }
    else if (!isBlank(uuid) && !isBlank(regUrl))
    {
      partnerInfo = BizRegDelegate.getPartnerInfo(uuid, regUrl, false);
    }
    else if(!isBlank(beId) && !isBlank(incomingConnectionType)) ////TWX 20101005 #1897 receive the msg from web service end point
    {
      BusinessEntity defBe = BizRegDelegate.getDefaultBusinessEntity();
      BusinessEntity be = BizRegDelegate.getBusinessEntityByBeId(beId, defBe.getEnterpriseId(),false); //unique key beId+enterpriseId
      if(be == null)
      {
        throw new BusinessEntityNotFoundException("No BusinessEntity can be found in the system given beId="+beId);
      }
      
      partnerInfo = new PartnerInfo();
      partnerInfo.setBizEntityID(be.getBusEntId());
      partnerInfo.setNodeID(Long.valueOf(be.getEnterpriseId()));
    }
    else // use the default be for receiving
    {
      BusinessEntity defBe = BizRegDelegate.getDefaultBusinessEntity();
      Long gnIdInt = Long.valueOf(defBe.getEnterpriseId());
      partnerInfo = new PartnerInfo();
      partnerInfo.setBizEntityID(defBe.getBusEntId());
      partnerInfo.setNodeID(gnIdInt);
    }

    return partnerInfo;
  }

  private static PartnerInfo getSenderPartnerInfo(String[] header, Hashtable additionalHeaders)
    throws Exception
  {
  	/*NSL20060322
    String partnerId = (String)additionalHeaders.get(ICommonHeaders.SENDER_ID);
    String beId = (String)additionalHeaders.get(ICommonHeaders.SENDER_BE_ID);
    String duns = (String)additionalHeaders.get(ICommonHeaders.SENDER_BE_DUNS);
    */
    String partnerId = header[ChannelReceiveHeader.SENDER_PARTNER_POS];
    String beId = header[ChannelReceiveHeader.SENDER_BEID_POS];
    String duns = header[ChannelReceiveHeader.SENDER_DUNS_POS];
    String gnId = header[ChannelReceiveHeader.SENDER_NODEID_POS];
    String uuid = header[ChannelReceiveHeader.SENDER_UUID_POS];
    String regUrl = header[ChannelReceiveHeader.SENDER_REGISTRY_QUERY_URL];

    PartnerInfo partnerInfo = null;
    if (!isBlank(partnerId))
    {
      partnerInfo = BizRegDelegate.getPartnerInfo(partnerId, true);
    }
    else if (!isBlank(beId))
    {
      Long gnIdInt = isBlank(gnId)?null : Long.valueOf(gnId);
      partnerInfo = BizRegDelegate.getPartnerInfo(gnIdInt, beId);
    }
    else if (!isBlank(duns))
    {
      partnerInfo = BizRegDelegate.getPartnerInfoByDuns(duns, false);
    }
    else if (!isBlank(uuid) && !isBlank(regUrl))
    {
      partnerInfo = BizRegDelegate.getPartnerInfo(uuid, regUrl, false);
    }
    else
      throw new PartnerNotFoundException("Missing sender information. Unable to determine sender partner!");

    return partnerInfo;
  }

  private static boolean isBlank(String val)
  {
    return (val==null || val.trim().length()==0);
  }
  protected static void setReceiveTrxId(GridDocument gdoc, String[] header)
  {
    String eventId = header[ChannelReceiveHeader.EVENT_ID_POS];
    if (header[1] == null)
      return;
    String trxId = header[ChannelReceiveHeader.TRANSACTION_ID_POS];
    //don't care if receive by other means
    if (IDocumentEvents.SEND_GRIDDOC.equals(eventId)
      || IDocumentEvents.DOWNLOAD_GRIDDOC.equals(eventId))
      gdoc.setReceiveTrxId(trxId);

  }

  protected static void setUdocInfo(GridDocument gdoc, File udocFile)
    throws Exception
  {
    String udocFilename = udocFile.getName();
    String tempUdocFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());

    gdoc.setUdocFilename(udocFilename);
    gdoc.setTempUdocFilename(tempUdocFilename);
    File udoc = FileHelper.getUdocFile(gdoc);
    gdoc.setUdocFullPath(udoc.getAbsolutePath());

    gdoc.setUdocFileSize(getFileSize(udocFile));
  }

  protected static void setTimestamps(GridDocument gdoc, String[] dataReceived)
  {
    if (dataReceived == null || dataReceived.length == 0)
      dataReceived = new String[] { null, null };

    Date sendEnd = null, receiveStart = null, receiveEnd;

    if (dataReceived[0] != null)
      receiveStart = Timestamp.valueOf(dataReceived[0]);
    else // default to current time
      receiveStart = TimeUtil.localToUtcTimestamp();

    if (GridDocument.ROUTE_GRIDMASTER.equals(gdoc.getSenderRoute()))
    {
      sendEnd = Timestamp.valueOf(dataReceived[2]);
      receiveEnd = TimeUtil.localToUtcTimestamp();
    }
    else
    {
      sendEnd = TimeUtil.localToUtcTimestamp();
      receiveEnd = sendEnd;
    }

    gdoc.setDateTimeReceiveEnd(receiveEnd);
    gdoc.setDateTimeReceiveStart(receiveStart);
    gdoc.setDateTimeSendEnd(sendEnd);
  }

  protected static Long getFileSize(File file)
  {
    return new Long(Math.round(file.length() / 1000));
  }

  protected static void deleteFilesReceived(File[] filesReceived)
    throws Exception
  {
    for (int i = 0; i < filesReceived.length; i++)
    {
      File file = filesReceived[i];
      file.delete();
    }
  }

  protected static GridDocument processAttachments(
    GridDocument gdoc,
    File[] filesReceived,
    int attachmentInfoIndex)
    throws Exception
  {
    Logger.debug("[ReceiveDocumentHelper.processAttachments] Start");
    String partnerId = "";
    File infoFile = filesReceived[attachmentInfoIndex];
    Logger.debug("[ReceiveDocumentHelper.processAttachments] Files Received length = "+filesReceived.length);
    List attachmentList = gdoc.getAttachments();
    XmlObjectDeserializer deserializer = new XmlObjectDeserializer();
    AttachmentInfo attInfo =
      (AttachmentInfo) deserializer.deserialize(
        AttachmentInfo.class,
        infoFile.getAbsolutePath());
    try
    {
      PartnerInfo pInfo =
        BizRegDelegate.getPartnerInfo(
          gdoc.getSenderNodeId(),
          gdoc.getSenderBizEntityId());
      partnerId = pInfo.getPartnerID();
      Logger.debug(
        "[ReceiveDocumentHelper.processAttachments] Sender partnerId = "
          + partnerId);
    }
    catch (Exception ex)
    {
      throw ex;
    }
    catch (Throwable throwable)
    {
      throw new PartnerNotFoundException(throwable);
    }

    int attStartIndex = attachmentInfoIndex + 1; //index where the actual attachments start
    if (filesReceived.length > attStartIndex)
    {
      // attachments is send together with this udoc
      Logger.debug(
        "[ReceiveDocumentHelper.processAttachments] Attachments is send together with this udoc");
      ArrayList<Long> newList = new ArrayList<Long>();
      Iterator attIterator = attInfo.iterator();
      Iterator uidIterator = attachmentList.iterator();

      for (int i = attStartIndex; i < filesReceived.length; i++)
      {
        File attFile = filesReceived[i];
        Attachment attachment = (Attachment) attIterator.next();
        Long orgUid = new Long(uidIterator.next().toString());
        Long newUid =
          AttachmentHelper.createAttachment(
            attFile,
            attachment.getOriginalFilename(),
            Boolean.FALSE,
            orgUid,
            partnerId);
        newList.add(newUid);
      }
      gdoc.setAttachments(newList);
      gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
    }
    else
    {
      // attachments is send with another udoc
      Logger.debug(
        "[ReceiveDocumentHelper.processAttachments] Attachments is send with another udoc");
      List newUids = AttachmentHelper.findIncomingAttachments(gdoc, partnerId);
      if (!newUids.isEmpty())
      {
        Logger.debug(
          "[ReceiveDocumentHelper.processAttachments] Attachments has been sent eariler, updating now");
        gdoc.setAttachments(newUids);
        gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
      }
      else
      {
        Logger.debug(
          "[ReceiveDocumentHelper.processAttachments] Attachments not yet arrived");
        gdoc.setAttachmentLinkUpdated(Boolean.FALSE);
      }
    }
    Logger.debug("[ReceiveDocumentHelper.processAttachments] End");
    return gdoc;
  }
}
