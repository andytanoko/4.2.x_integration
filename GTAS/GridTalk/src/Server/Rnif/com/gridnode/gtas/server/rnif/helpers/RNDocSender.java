/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNDocSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 26 2004    Neo Sok Lay         Modify prepareFilesToSend() to
 *                                    handle backward compatible payloads
 * Mar 11 2004    Jagadeesh           Modified: To handle upload of RNIF Document
 *                                    to GM.
 * Jul 01 2004    Neo Sok Lay         Do not re-determine the partner channel if
 *                                    already present. 
 * Jan 26 2005    Mahesh              Added getRouteLevel method to overide the 
 *                                    SendDocumentHelper.getRouteLevel                                    
 * OCT 21 2005	  Tam Wei Xiang       Added method createPackInfo(GridDocument gDoc, RNProfile profile)
 * 				                            modified method createPackInfo(Gdoc)  
 * Sep 20 2006    Tam Wei Xiang       Change the gdoc ownCert, TpCert to senderCert,
 *                                    receiverCert
 * Nov 12 2007    Tam Wei Xiang       Added "is_compress_required" flag into RNPackInfo.                                  
 * NOV 15 2005    Tam Wei Xiang       modified method setSendingInfo();  
 * Jan 05 2007    Tam Wei Xiang       Trigger the event of CHANNEL_Connectivity for OTC.
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename. 
 * Aug 11 2009    Tam Wei Xiang       #841 - the non repudiation flag is not captured
 *                                    and persisted in db                                   
 */
package com.gridnode.gtas.server.rnif.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.events.document.ImportDocumentEvent;
import com.gridnode.gtas.server.document.actions.ImportDocumentAction;
import com.gridnode.gtas.server.document.helpers.AttachmentEntityHandler;
import com.gridnode.gtas.server.document.helpers.IDocumentConstants;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.helpers.SendDocumentHelper;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.DocChannelInfo;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.helper.RNCertInfo;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

public class RNDocSender extends SendDocumentHelper
{

  public GridDocument setSendingInfo(GridDocument gdoc, String defName, Boolean isInitiator)
    throws Exception
  {
    //    if (gdoc.getRecipientPartnerId() != null)
    //    {
    //      PartnerInfo pInfo = BizRegDelegate.getPartnerInfo(gdoc.getRecipientPartnerId());
    //      if (pInfo != null)
    //      {
    //        gdoc.setRecipientPartnerName(pInfo.getPartnerName());
    //        gdoc.setRecipientPartnerGroup(pInfo.getPartnerGroup());
    //        gdoc.setRecipientPartnerType(pInfo.getPartnerType());
    //        gdoc.setRecipientBizEntityId(pInfo.getBizEntityID());
    //        gdoc.setRecipientNodeId(pInfo.getNodeID());
    //      }
    //      else
    //      {
    //        Logger.err("[RNDocSender.setSendingInfo] Unable to retrieve ParterInfo for partner "
    //          + gdoc.getRecipientPartnerId());
    //      }
    //    }
    gdoc.setDateTimeSendStart(TimeUtil.localToUtcTimestamp());
    if (gdoc.getRecipientChannelUid() == null) //only determine the channel only if not present
    {
      DocChannelInfo cInfo= null;
      try
      {
        cInfo = ProcessUtil.getPartnerProcessChannelInfo(defName, gdoc.getRecipientPartnerId(), isInitiator);
      }
      catch(Exception ex)
      {
        //TODO trigger the channel connect fail
        sendDocumentFlowNotification(gdoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, false, ex.getMessage(), new Date(), ex);
        throw ex;
      }
      if (cInfo != null)
      {
        gdoc.setRecipientChannelUid(cInfo.getChannelUid());
        gdoc.setRecipientChannelName(cInfo.getChannelName());
        gdoc.setRecipientChannelProtocol(cInfo.getChannelProtocol());
      }
    }
    
    //  TWX: 14 NOV 2005
    RNCertInfo certInfo = getBizCertMappingForPartner(gdoc.getRecipientPartnerId());
    
    //  TWX: 19 Jan 2006
    if(certInfo.get_ownSignCertificate() != null)
    {
    	gdoc.setSenderCert((Long)certInfo.get_ownSignCertificate().getKey());
    }
    if(certInfo.get_partnerEncryptCertificate() != null)
    {
    	gdoc.setReceiverCert((Long)certInfo.get_partnerEncryptCertificate().getKey());
    }
    
    gdoc.setOriginalDoc(Boolean.TRUE);
    
    return gdoc;
  }

  //NSLnote: the original implementation is to have another Channel for RN over P2P which has master channel's security & comm profile but RN packaging
  //however, probably mahesh tested with the master channel itself for sending RN, thus the send does not work out since the packaging profile
  // of master channel does not handle RN packaging.
  // thus, he changed to use ROUTE_GM if the master comm profile is used so that the RN packaging is done before passing on to the channel module
  // this change enables RN over P2P using the master channel itself. however, that means the configuration of the original implementation will
  // no longer work out anymore...since there will be attempt of another RN packaging callback from channel - which would fail.
  //--> this change is very important to note!!!
  //
  //This route level returned only affects the packaging but not the Route in griddocument, but it may be confusing...
  //so it is recommended to change ROUTE_GM to some other names
  protected int getRouteLevel(ChannelInfo channelInfo,boolean isUpload) 
  {
    Logger.debug("RNDocSender.getRouteLevel isUpload="+isUpload+", channelInfo.TptCommInfo.isDefaultTpt="+channelInfo.getTptCommInfo().isDefaultTpt());
    if (channelInfo.getTptCommInfo().getTptImplVersion().startsWith("02"))
      return IDocumentConstants.ROUTE_GT1_GM;
    /*26012005 Mahesh : overrided this SendDocumentHelper.getRouteLevel and removed check on isUpload
    if (isUpload) //If only on upload we check if we need to send to GM,applys only for RNIF
    {
      if (channelInfo.getTptCommInfo().isDefaultTpt())
        return IDocumentConstants.ROUTE_GM;
    }
    */
    if (channelInfo.getTptCommInfo().isDefaultTpt())
      return IDocumentConstants.ROUTE_GM;

    //return -1;
    return IDocumentConstants.ROUTE_DIRECT;
  }
  
  protected File[] prepareFilesToSend(GridDocument gdoc,int route_lvl) throws Exception
  {
    Logger.debug("RNDocSender.prepareFilesToSend is called with arguments " + gdoc);
    ArrayList filesList= new ArrayList();
    //String udocFilename= gdoc.getUdocFilename();
    File udocFile = DocumentUtil.getUDoc(gdoc);
Logger.debug("## Retrieved udocFile name is " + udocFile.getAbsolutePath() );
Logger.debug("## Route Level  " + route_lvl );

    //File udocFile= FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, udocFilename);

    //040126NSL: handle backwardcompatible payloads
    File[] filesToSend = null;

    if (IDocumentConstants.ROUTE_GT1_GM == route_lvl)
    {
      Logger.debug("[RNDocSender.prepareFilesToSend] Preparing Backward compatible file payloads");
      File[] payloads = super.prepareFilesToSend(gdoc, IDocumentConstants.ROUTE_GT1_GM);
      filesToSend = BackwardCompatibleRNHandler.prepareFilesToSend(createPackInfo(gdoc), payloads);
      //filesToSend[0]=RBM, filesToSend[1]=Gdoc, filesToSend[2]=null
    }
    else if (IDocumentConstants.ROUTE_GM == route_lvl)
    {
      filesToSend = RelayRNHandler.prepareFilesToSend(createPackInfo(gdoc),
          getFilesToSend(gdoc,udocFile));
      //filesToSend[0]=RBM, filesToSend[1]=RNPackinfo
    }
    else
    {
      File packinfoFile= preparePackInfo(gdoc);
      filesList.add(packinfoFile);
      filesList.add(udocFile);

      filesList= getAttachmentFiles(gdoc, filesList);
      filesToSend= convertToFileArray(filesList);
      //filesToSend[0]=RNpackinfo, filesToSend[1]=udoc, filesToSend[2~]=attachments 
      
      //for direct send, the packaging to RBM is done thru callback from Channel module
    }

    Logger.debug(
      "RNDocSender.prepareFilesToSend exited with result " + filesToSend[0] + ";" + filesToSend[1]);
    return filesToSend;
  }

  /**
   * Get the files to send via GM
   * @param gridDoc
   * @param udocFile
   * @return Array of files: Element 0=udoc, Element 1=RNpackinfo, Element 2~=attachments
   * @throws Exception
   */
  private File[] getFilesToSend(GridDocument gridDoc, File udocFile) throws Exception
  {
    ArrayList filesList = new ArrayList();
    File packinfoFile= preparePackInfo(gridDoc);

    filesList.add(udocFile);  //0 is always udoic file
    filesList.add(packinfoFile); // since we send via GM, we send packInfoFile

    filesList= getAttachmentFiles(gridDoc, filesList);
    return convertToFileArray(filesList);
  }

/*
  protected File[] prepareFilesToSend(GridDocument gdoc,boolean isTransformationReq) throws Exception
  {
    Logger.debug("RNDocSender.prepareFilesToSend is called with arguments " + gdoc);
    ArrayList filesList= new ArrayList();
    String udocFilename= gdoc.getUdocFilename();
    File udocFile = DocumentUtil.getUDoc(gdoc);
Logger.debug("## Retrieved udocFile name is " + udocFile.getAbsolutePath() );
    //File udocFile= FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, udocFilename);

    //040126NSL: handle backwardcompatible payloads
    File[] filesToSend = null;
    if (isTransformationReq)
    {
      Logger.debug("[RNDocSender.prepareFilesToSend] Preparing Backward compatible file payloads");
      File[] payloads = super.prepareFilesToSend(gdoc, isTransformationReq);
      filesToSend = BackwardCompatibleRNHandler.prepareFilesToSend(createPackInfo(gdoc), payloads);
    }
    else
    {
      File packinfoFile= preparePackInfo(gdoc);
      filesList.add(packinfoFile);
      filesList.add(udocFile);

      filesList= getAttachmentFiles(gdoc, filesList);
      filesToSend= convertToFileArray(filesList);
    }

    Logger.debug(
      "RNDocSender.prepareFilesToSend exited with result " + filesToSend[0] + ";" + filesToSend[1]);
    return filesToSend;
  }
*/
  //21 OCT 2005 Modified by TWX
  protected RNPackInfo createPackInfo(GridDocument gDoc) throws Exception
  {
    RNProfile profile= null;
    profile= new ProfileUtil().getProfileMustExist(gDoc);

    return createPackInfo(gDoc, profile);
  }
  
  //21 OCT 2005 Added by TWX (It is also used by EStore logic to genrate RNPackInfo)
  public RNPackInfo createPackInfo(GridDocument gDoc, RNProfile profile) throws Exception
  {
  	RNPackInfo packinfo= new RNPackInfo();
    PackInfoConvertor convertor= new PackInfoConvertor();
    convertor.profileToPackInfo(profile, packinfo);

    String defName= profile.getProcessDefName();
    ProcessDef def= ProcessUtil.getProcessDef(defName);
    boolean isInitiator= profile.isInitiatorDoc();
    boolean isSignal= profile.getIsSignalDoc();
    ProcessAct curAct= ProcessUtil.getSenderCurAct(def, isInitiator, isSignal);

    packinfo.setIsSynchronous(Boolean.TRUE.equals(def.getIsSynchronous()));

    packinfo.setIsEnableSignature(!Boolean.TRUE.equals(curAct.getDisableSignature()));
    packinfo.setIsEnableEncryption(!Boolean.TRUE.equals(curAct.getDisableEncryption()));
    packinfo.setIsOnlyEncryptPayload(Boolean.TRUE.equals(curAct.getOnlyEncryptPayload()));

    packinfo.setIsSecureTptRequired(Boolean.TRUE.equals(curAct.getIsSecureTransportRequired()));

    packinfo.setDigestAlgorithm(curAct.getDigestAlgorithm());
    packinfo.setEncryptionAlgorithm(curAct.getEncryptionAlgorithm());
    packinfo.setEncryptionAlgorithmLength(
      curAct.getEncryptionAlgorithmLength() == null
        ? 0
        : curAct.getEncryptionAlgorithmLength().intValue());

    packinfo.setUDocFileName(DocumentUtil.getUDocName(gDoc));
    packinfo.setDTSendStart(gDoc.getDateTimeSendStart());
    
    packinfo.setIsCompressRequired(curAct.getIsCompressRequired().booleanValue()); //TWX: 12NOV2007 Added isCompressedRequired
    
    packinfo.setIsNonRepudiationRequired(curAct.getIsNonRepudiationRequired()); //TWX: 20090811 reflect non repudiation changes.
    
    return packinfo;
  }
  
  protected File preparePackInfo(GridDocument gDoc) throws Exception
  {
    RNPackInfo packinfo = createPackInfo(gDoc);
    File res= File.createTempFile("SendRNPackInfo"+UUIDUtil.getRandomUUIDInStr(), ".xml");
    packinfo.serialize(res.getAbsolutePath());
    return res;
  }

  protected ArrayList getAttachmentFiles(GridDocument gdoc, ArrayList filesList) throws Exception
  {
    if (gdoc.hasAttachment().booleanValue())
    {
      List attachmentUids= gdoc.getAttachments();
      if (!attachmentUids.isEmpty())
      {
        for (Iterator w= attachmentUids.iterator(); w.hasNext();)
        {
          Logger.log("[RNDocSender.getAttachmentFiles] uid = "+w.next().toString());
        }
        for (Iterator i= attachmentUids.iterator(); i.hasNext();)
        {
          Long attachmentUid= new Long(i.next().toString());
          Attachment attachment= null;
          try
          {
            attachment=
              (Attachment) AttachmentEntityHandler.getInstance().getEntityByKey(attachmentUid);
          }
          catch (Throwable ex)
          {
            throw new ApplicationException(
              "Unable to retrieve attachment " + attachmentUid + " from database",
              ex);
          }
          try
          {
            File attFile=
              FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, attachment.getFilename());
            filesList.add(attFile);
          }
          catch (Exception ex)
          {
            throw new ApplicationException(
              "Unable to find attachment file "
                + attachment.getFilename()
                + " in attachment directory",
              ex);
          }
        }

      }
    }
    return filesList;
  }

  public void importAndSendDoc(String userId, String beId, String enterpriseId, String uDocName, String udocType, String partnerId)
    throws Exception
  {
    StateMachine sm= new StateMachine(null, null);

    ISessionManagerObj sessionMgr=
      (ISessionManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
        ISessionManagerHome.class.getName(),
        ISessionManagerHome.class,
        new Object[0]);
    String sessionID= sessionMgr.openSession();
    Logger.log("call SessionMgr.authSession with argument=" + sessionID + "; " + userId);
    sessionMgr.authSession(sessionID, userId);
    int i= 0;
    for (; i < 10; i++)
    {
      String sessionSubject= sessionMgr.getSessionAuthSubject(sessionID);
      if (sessionSubject == null || sessionSubject.length() < 1)
      {
        Logger.warn("authSession is not updated into database when try i=" + i);
        sessionMgr.authSession(sessionID, userId);
      }
      else
      {
        Logger.warn("getSessionAuthSubject returned=" + sessionSubject);
        break;
      }
    }
    if (i >= 10)
      Logger.warn(
        "authSession is not updated into database after being called 10 times, will not try again!");
    sm.setAttribute(IAttributeKeys.SESSION_ID, sessionID);
    sm.setAttribute(IAttributeKeys.USER_ID, userId);
    sm.setAttribute(IAttributeKeys.ENTERPRISE_ID, enterpriseId);
    ArrayList udocList= new ArrayList();
    udocList.add(uDocName);
    ArrayList partnerList= new ArrayList();
    partnerList.add(partnerId);

    ImportDocumentEvent event=
      new ImportDocumentEvent(beId, udocType, udocList, partnerList, new ArrayList());

    ImportDocumentAction action= new ImportDocumentAction();
    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response= (BasicEventResponse) action.perform(event);
    action.doEnd();
  }
  
  //TWX: 14 NOV 2005
  private RNCertInfo getBizCertMappingForPartner(String partnerID)
  	throws Exception
  {
  	return ProcessUtil.getBizCertMappingForPartner(partnerID);
  }
  
  private void sendDocumentFlowNotification(GridDocument gdoc, EDocumentFlowType docFlowType, boolean isDocumentFlowSucess, String errReason,
                                            Date eventOccurTime, Throwable th) throws SystemException
  {
    DocumentFlowNotifyHandler.triggerNotification(docFlowType, eventOccurTime, gdoc.getFolder(), gdoc.getGdocId(), isDocumentFlowSucess, errReason,
                                                  gdoc.getTracingID(), gdoc.getUdocDocType(), gdoc.getSenderBizEntityId(), gdoc.getRecipientBizEntityId(),
                                                  "", (Long)gdoc.getKey(), gdoc.getSrcFolder(), th);
  }
}
