/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BpssHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 09 2002    Neo Sok Lay         Add Raise Alert hooks.
 * Aug 22 2003    Guo Jianyu          Add setProcessInstanceUid() and setUserTrackingId()
 * Oct 31 2003    Guo Jianyu          Modified sendBizDocument() to include subpaths for uDoc
 * Feb 18 2005    Mahesh              Modified to pass exception parameters to SendExceptionAction_11 constructor
 * Jan 19 2006    Neo Sok Lay         GNDB00026521: if 0A1FailureNotification is received, cancel
 *                                    the original process instance.
 * Feb 06 2006    Tam Wei Xiang       Modified method receiveBizDocument, sendSignal
 *                                    Added method getXmlManager(), getXPathValue(String, string)    
 * Feb 08 2006    Neo Sok Lay         Use DocumentUtil.extractValueFromUDoc() to extract 0A1 values instead
 *                                    of getXPathValue(). Removed getXmlManager(), getXPathValue(String, string).  
 * Jun 16 2006    Tam Wei Xiang       Modified receivedBizDocument to send out 0A1 so
 *                                    that we compliance to RN spec. Added method
 *                                    handleFailedToReceiveBizDocument(...)
 * Nov 12 2006    Tam Wei Xiang       Indicate the creation of the OB document to OTC-plugin     
 * Dec 28 2006    Tam Wei Xiang       Swap the sequence we trigger the Document notification to 
 *                                    OTC plug-in. Modified sendBizDocument(...)      
 * Jan 10 2007    Tam Wei Xiang       Set the documentID into OB gdoc in case we fail to validate
 *                                    the document we sent out or we received.   
 * Apr 03 2007    Tam Wei Xiang       Added in Customer BE ID in RTProcessDoc during
 *                                    the sending/receive of biz document to support the Archive
 *                                    by Customer.
 *                                                                                                                            
 * May 16 2007    Tam Wei Xiang       GNDB00028358 In case validate document failed, the customer be id is also
 *                                    required to be updated into rtprocess doc.
 * Jul 25 2008    Tam Wei Xiang       #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.
 */
package com.gridnode.gtas.server.rnif.helpers;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javax.jms.JMSException;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.notification.DocumentTransactionHandler;
import com.gridnode.gtas.server.rnif.act.*;
import com.gridnode.gtas.server.rnif.actions.SendFailureNotificationAction;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.helpers.BpssGenerator;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UtilEntity;

public class BpssHandler implements Serializable
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8689634497301454241L;
	private static final String XPATH_0A1_PROPRIETARY_DOC_IDENTIFIER = "//Pip0A1FailureNotification/failedInitiatingDocumentIdentifier/ProprietaryDocumentIdentifier";
	private static final String XPATH_0A1_REASON = "//Pip0A1FailureNotification/reason/FreeFormText";
	private static final String XPATH_0A1_INSTANCE_IDENTIFIER = "//Pip0A1FailureNotification/ProcessIdentity/InstanceIdentifier";
	
	public String sendRequestDocument(
    String documentId,
    String documentType,
    Object documentObject,
    Integer retryCount,
    String partnerKey)throws Throwable
  {
    return sendBizDocument(documentId, documentType, documentObject, retryCount, partnerKey, true);
  }

  public String sendResponseDocument(
    String documentId,
    String documentType,
    Object documentObject,
    Integer retryCount,
    String partnerKey) throws Throwable
  {
    return sendBizDocument(documentId, documentType, documentObject, retryCount, partnerKey, false);
  }

  public String sendBizDocument(
    String documentId,
    String documentType,
    Object documentObject,
    Integer retryCount,
    String partnerKey,
    boolean isRequest) throws Throwable
  {
    Logger.debug(
      "BPSSHandler.sendBizDocument() with documentId="
        + documentId
        + ";documentType ="
        + documentType
        + ";documentObject ="
        + documentObject
        + ";retryCount ="
        + retryCount
        + ";partnerKey="
        + partnerKey
        + ";isRequest="
        + isRequest);
    GridDocument gDoc= null;
    ProcessDef def= null;
    ProcessAct curAct= null;
    RNProfile profile= null;
    boolean isRetry = false;
     
    try
    {
      gDoc= getDocFromDocObj(documentObject);
      String defName= BpssGenerator.getDefNameFromDocRefId(documentType);
      def= ProcessUtil.getProcessDef(defName);
      curAct= isRequest ? def.getRequestAct() : def.getResponseAct();
      
      if (retryCount == null || 1 == retryCount.intValue())
      {
        String[] seperateId= getSeperateIdsFromBPSSDocId(documentId);
        //        String originator= seperateId[0];
        String instanceId= seperateId[IRnifConstant.INDEX_PROCESS_INSTANCE_ID];
        profile= new ProfileUtil().createRNProfile(gDoc, def, instanceId, isRequest);
        gDoc.setRnProfileUid((Long) profile.getKey());
        
        DocumentUtil.updateDocument(gDoc);
      }
      else
      {
        if (curAct.getRetries() == null
          || (curAct.getRetries().compareTo(new Integer(retryCount.intValue() - 1)) >= 0))
        {
          isRetry = true;
          gDoc= (GridDocument) gDoc.clone();
          profile= new ProfileUtil().createResendRNProfile(gDoc);
          gDoc.setRnProfileUid((Long) profile.getKey());
          gDoc.setDateTimeSendStart(TimeUtil.localToUtcTimestamp());
          String udocName= gDoc.getUdocFilename();
          try
          {
            udocName=
              FileUtil.copy(
                IDocumentPathConfig.PATH_UDOC,
                gDoc.getFolder() + File.separator,
                udocName,
                IDocumentPathConfig.PATH_UDOC,
                gDoc.getFolder() + File.separator,
                udocName);
          }
          catch (Exception ex)
          {
            throw RnifException.fileProcessErr(
              "When copy the original uDoc when resend! uDocName is " + udocName,
              ex);
          }
          gDoc.setUdocFilename(udocName);
          String fullPath = DocumentUtil.getFullUDocPath(gDoc);
          gDoc.setUdocFullPath(fullPath);

          gDoc= DocumentUtil.addGridDocument(gDoc);
        }
        else
        {
          return "Cannot sendBizDocument: Exceeds maximum retry numbers!";
        }
      }
      
      
      //TWX 03042007 Add in the customer BE ID into rtprocess doc
      GWFRtProcessDoc rtprocessDoc = getGWFRtProcessDoc(documentId);
      if(rtprocessDoc != null)
      {
        setProcessInstanceIds(gDoc, documentId, rtprocessDoc.getRtBinaryCollaborationUId());
        
        //set while we are sending the doc for first time
        if(retryCount == null || 1 == retryCount.intValue())
        {
          setCustomerBEID(rtprocessDoc, gDoc, true);
        }
      }
      else
      {
        Logger.debug("WARNING: No rtProcessDoc found for " + documentId + "!");
      }
      
      setPripriotoryDocId(def, gDoc, profile, isRequest);
      setUserTrackingId(def, gDoc, profile, isRequest);
      
      //TWX 12112006 Indicate the creation of the OB document to OTC-plugin(here can also detect the resend case)  
      DocumentTransactionHandler.triggerDocumentTransaction(gDoc,false, isRetry);
      
      RNDocSender docSender= new RNDocSender();
      docSender.setSendingInfo(gDoc, defName, isRequest ? Boolean.TRUE : Boolean.FALSE);
      docSender.send(gDoc);
    }
    catch (Throwable ex)
    {
      Logger.warn("Cannot sendBizDocument ", ex);
      
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  throw ex;
      }
      
      return null;
    }
    return null;
  }

  public void sendSignal(
    String documentId,
    String signalType,
    Object reason,
    String documentType,
    Object documentObject,
    String partnerKey) throws Throwable
  {
    String reasonStr= getReasonStr(reason);

    Logger.debug(
      "BPSSHandler.sendSignal() with documentId="
        + documentId
        + ";signalType ="
        + signalType
        + ";reason ="
        + reasonStr
        + ";documentType ="
        + documentType
        + ";documentObject ="
        + documentObject
        + ";partnerKey="
        + partnerKey);
    GridDocument gDoc= null;
    ProcessDef def= null;
    ProcessAct curAct= null;
    Object param= null;
    String RNIFVersion = null;
    try
    {
      gDoc= getDocFromDocObj(documentObject);
      String defName= null;
      boolean isRequest= BpssGenerator.isRequestDocNameRefId(documentType);
      SendDocumentAction action= null;
      defName= BpssGenerator.getDefNameFromDocRefId(documentType);
      def= ProcessUtil.getProcessDef(defName);
      RNIFVersion = def.getRNIFVersion();
      Logger.debug("$$$ defName is " + defName + " rnifversion is " + RNIFVersion);
      if (IBpssConstants.EXCEPTION_TIMETO_ACK.equals(signalType)
        || IBpssConstants.EXCEPTION_TIMETO_PERFORM.equals(signalType))
      {
        if (IRnifConstant.RN_FAILNOTIFY_DEFNAME20.equals(defName) ||
            IRnifConstant.RN_FAILNOTIFY_DEFNAME11.equals(defName))
        {
          Logger.warn(
            "FailureNotification PIP "
              + documentId
              + " failed at "
              + documentType
              + ", no NoF is send!");

          AlertUtil.alertFailurePipFail(gDoc);

          return;
        }
        
        //TWX: 26012006 move up from the else clause
        String exceptionType, myReasonStr = null;
        if (IBpssConstants.EXCEPTION_TIMETO_ACK.equalsIgnoreCase(signalType))
        {
          exceptionType= "PRF.ACTN.GENERR";
          myReasonStr= "Exceed time to acknowledge receipt!";

          AlertUtil.alertMaxRetriesReached(gDoc);
          //the reason that will be included inside the 0A1
          param = new Object[]{exceptionType+"-"+myReasonStr};
        }
        else if (IBpssConstants.EXCEPTION_TIMETO_PERFORM.equalsIgnoreCase(signalType))
        {
          exceptionType= "PRF.ACTN.GENERR";
          myReasonStr= "Exceed time to perform!";

          AlertUtil.alertTimeToPerformExpired(gDoc);
          //the reason that will be included inside the 0A1
          param = new Object[]{exceptionType+"-"+myReasonStr};
        }
        //end
        
        if ((RNIFVersion != null)&&(RNIFVersion.equals(def.RNIF_1_1)))
        {
          defName = IRnifConstant.RN_FAILNOTIFY_DEFNAME11;
          action = new FailureNotificationAction_11();
        }
        else
        {
          defName= IRnifConstant.RN_FAILNOTIFY_DEFNAME20;
          action= new FailureNotificationAction();
        }
      }
      else
      {
        if (IBpssConstants.ACK_RECEIPT_SIGNAL.equals(signalType))
        {
          if((RNIFVersion != null) && (RNIFVersion.equals(def.RNIF_1_1)))
            action = new SendAckAction_11();
          else
            action = new SendAckAction();
        }
        else
        {
          String exceptionType= null;
          String myReasonStr= null;
          if (IBpssConstants.EXCEPTION_VALIDATE.equals(signalType))
          {
            //TWX 10012007 The OB doc we create is fail to validate(may happen if validate at sender enable). Link the OB
            //back to its own Process (rn profile is needed). 
            String folder = gDoc.getFolder();
            if(OutboundFolder.FOLDER_NAME.equals(folder))
            {
              setProcessInstanceIds(gDoc, documentId);
              String[] seperateId= getSeperateIdsFromBPSSDocId(documentId);
              String instanceId= seperateId[IRnifConstant.INDEX_PROCESS_INSTANCE_ID];
              RNProfile profile= new ProfileUtil().createRNProfile(gDoc, def, instanceId, isRequest);
              gDoc.setRnProfileUid((Long) profile.getKey());
            }
            
            if (reason != null && reason instanceof Object[])
            {
              exceptionType= (String) ((Object[]) reason)[0];

              AlertUtil.alertValidateDocFailed(gDoc, reasonStr);
            }
            
            //TWX 10012007 The OB doc we create is fail to validate(may happen if validate at sender enable). 
            //No exception signal is required.
            if(OutboundFolder.FOLDER_NAME.equals(folder))
            {
              DocumentUtil.updateDocument(gDoc);
              DocumentTransactionHandler.triggerDocumentTransaction(gDoc, false, false);
              return;
            }
          }
          else if (IBpssConstants.EXCEPTION_CANCEL.equalsIgnoreCase(signalType))
          {
             exceptionType= "PRF.ACTN.GENERR";
             myReasonStr= "Process is canceled!";
          }
          else
          {
             Logger.warn("unknow Signal Type encountered in sendSignal: " + signalType);
             return;
          }
          if (reasonStr == null || reasonStr.length()<1)
            reasonStr= myReasonStr;
          if((RNIFVersion != null) && (RNIFVersion.equals(def.RNIF_1_1)))
            action = new SendExceptionAction_11(exceptionType, reasonStr);
          else
            action= new SendExceptionAction(exceptionType, reasonStr);
        }
      }

      def= ProcessUtil.getProcessDef(defName);
      curAct= isRequest ? def.getRequestAct() : def.getResponseAct();
      action.execute(gDoc, isRequest, def, curAct, param);
    }
    catch (Throwable ex)
    {
      Logger.warn(
        "Cannot sendSignal "
          + signalType
          + " for GridDocument"
          + documentObject
          + " of Process "
          + documentId,
        ex);
      
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  throw ex;
      }
    }

  }

  static String getReasonStr(Object reason)
  {
    String reasonStr= "";
    if (reason != null)
    {
      if (reason instanceof String)
      {
        reasonStr= (String) reason;
      }
      else
        if (reason instanceof Object[])
        {
          Object[] objArray= (Object[]) reason;
          for (int i= 0; i < objArray.length; i++)
            if (objArray[i] != null)
              reasonStr += objArray[i].toString();
        }
    }
    return reasonStr;
  }

  public String[] validateDoc(
    String documentId,
    String documentType,
    Object documentObject,
    String specLocation,
    String specElement)
  {
    Logger.debug(
      "BPSSHandler.validateDoc() with documentId="
        + documentId
        + ";documentType ="
        + documentType
        + ";documentObject ="
        + documentObject
        + ";specLocation ="
        + specLocation
        + ";specElement="
        + specElement);
    GridDocument gDoc= null;
    ProcessDef def= null;
    ProcessAct curAct= null;
    try
    {
      gDoc= getDocFromDocObj(documentObject);
      
      //24012005 Mahesh : This status flag is set if there is any unpackaging error
      String docTransStatus=gDoc.getDocTransStatus();
      if(docTransStatus!=null && docTransStatus.indexOf("-")>-1)
      {
        return new String[]{docTransStatus.substring(0,docTransStatus.indexOf("-")),docTransStatus.substring(docTransStatus.indexOf("-")+1)};
      }
      
      String defName= BpssGenerator.getDefNameFromDocRefId(documentType);
      def= ProcessUtil.getProcessDef(defName);
      boolean isRequest= BpssGenerator.isRequestDocNameRefId(documentType);
      curAct= isRequest ? def.getRequestAct() : def.getResponseAct();
      if (!Boolean.TRUE.equals(curAct.getValidateAtSender())
        && (GridDocument.FOLDER_OUTBOUND.equals(gDoc.getFolder())))
        return null;
    }
    catch (Throwable ex)
    {
      Logger.warn("Cannot found relevent entity for validation ", ex);
      return new String[] {
        "PRF.ACTN.GENERR",
        "Cannot found relevent entity for validation " + ex.getMessage()};
    }

    try
    {
      ValidateDocAction action= new ValidateDocAction();
      action.execute(gDoc, def, curAct);
      return null;
    }
    catch (RosettaNetException ex)
    {
      Logger.warn("Error in BPSSHandler.validateDoc", ex);
      
      try
      {
        GWFRtProcessDoc rtprocessDoc = getGWFRtProcessDoc(documentId);
        if(rtprocessDoc != null)
        {
          String folder = gDoc.getFolder();
          boolean isSendingDoc = false;
          if(OutboundFolder.FOLDER_NAME.equals(folder))
          {
            isSendingDoc = true;
          }
          setCustomerBEID(rtprocessDoc, gDoc, isSendingDoc);
        }
        else
        {
          Logger.warn("No GWFRTProcessDoc found given documentId "+documentId);
        }
      }
      catch(Throwable th)
      {
        Logger.warn("Error in updating GWFRTProcessDoc with documentId "+documentId);
      }
      return new String[] { ex.getExType(), ex.getDetails()};
    }
    catch (Throwable ex)
    {
      Logger.warn("Error in BPSSHandler.validateDoc", ex);
      return new String[] { RosettaNetException.UNP_SCON_VALERR, ex.getMessage()};
    }
  }

  public void receiveBizDocument(
    String documentId,
    String documentType,
    Object documentObject,
    Boolean isRetry,
    Boolean isValid) throws Throwable
  {

    Logger.debug(
      "BPSSHandler.receiveBizDocument() with documentId="
        + documentId
        + ";documentType ="
        + documentType
        + ";documentObject ="
        + documentObject
        + ";isRetry ="
        + isRetry
        + ";isValid="
        + isValid);
    
    GridDocument gDoc= null;
    boolean isRequest = false;
    ProcessDef def = null;
    try
    {
      String defName= BpssGenerator.getDefNameFromDocRefId(documentType);
      def = ProcessUtil.getProcessDef(defName);
      isRequest= BpssGenerator.isRequestDocNameRefId(documentType);
      gDoc= getDocFromDocObj(documentObject);
      
      //TWX 03042007 Added in the customer duns and partner duns into RTProcessDoc
      GWFRtProcessDoc rtprocessDoc = getGWFRtProcessDoc(documentId);
      if(rtprocessDoc != null)
      {
        setProcessInstanceIds(gDoc, documentId, rtprocessDoc.getRtBinaryCollaborationUId());
        setCustomerBEID(rtprocessDoc, gDoc, false);
      }
      else
      {
        Logger.debug("WARNING: No rtProcessDoc found for " + documentId + "!");
      }
      
      
      setPripriotoryDocId(def, gDoc, null, isRequest);
      setUserTrackingId(def, gDoc, null, isRequest);
            
      if (Boolean.TRUE.equals(isRetry) || !Boolean.TRUE.equals(isValid))
      {
        DocumentUtil.receiveFailedRnifDoc(gDoc, isValid.booleanValue(), isRetry.booleanValue());

        AlertUtil.alertDocReceived(gDoc);
      }
      else
      {
        DocumentUtil.receiveRnifDoc(gDoc);
       
        //NSL20060119 Cancel the original business process on receive NoF
        if (IRnifConstant.RN_FAILNOTIFY_DEFNAME20.equals(defName) ||
            IRnifConstant.RN_FAILNOTIFY_DEFNAME11.equals(defName))
        {
          String oriProcessInstId = DocumentUtil.extractValueFromUDoc(gDoc, XPATH_0A1_INSTANCE_IDENTIFIER); 
          
          if (oriProcessInstId != null && oriProcessInstId.length()> 0)
          {
          	//TWX 06022006
            String reason = DocumentUtil.extractValueFromUDoc(gDoc, XPATH_0A1_REASON);
            String proprietaryDocIdentifier = DocumentUtil.extractValueFromUDoc(gDoc, XPATH_0A1_PROPRIETARY_DOC_IDENTIFIER); //only available in v2.0
            
            ProcessInstanceActionHelper.cancelProcessInstanceOnRecvNoF(gDoc, oriProcessInstId, reason, proprietaryDocIdentifier);           
          }
        }
      }
    }
    catch (Throwable ex)
    {
      Logger.warn("Cannot receiveBizDocument ", ex);
      
      String reasonStr = "Cannot receiveBizDocument. Err is "+ex.getMessage();
      
      //TWX 16062006 To better compliance to Rosettanet standard
      handleFailedToReceiveBizDocument(gDoc, isRetry.booleanValue(), isValid.booleanValue(), isRequest,
                                       documentId, documentType, def.getProcessType(),reasonStr);
      
      //19072008 TWX Propagated up the error only if JMSException
      if(JMSRedeliveredHandler.isRedeliverableException(ex))
      {
    	  throw ex;
      }
    }
  }

  private static void setPripriotoryDocId(
    ProcessDef def,
    GridDocument gDoc,
    RNProfile profile,
    boolean isRequest)
    throws RnifException
  {
    String xpath=
      isRequest ? def.getRequestDocThisDocIdentifier() : def.getResponseDocThisDocIdentifier();
    if (xpath != null)
    {
      String uniqueValue= DocumentUtil.extractValueFromUDoc(gDoc, xpath);
      if (uniqueValue != null)
      {
        ProfileUtil profileUtil= new ProfileUtil();
        if (profile == null)
          profile= profileUtil.getProfileMustExist(gDoc);
        profile.setUniqueValue(uniqueValue);
        profileUtil.updateProfile(profile);
      }
      else
      {
        Logger.warn("unique value extracted is null");
      }
    }
  }
  
  //TWX 03042007
  private static void setProcessInstanceIds(GridDocument gDoc, String documentId, Long processInstanceUID)
  {
    gDoc.setProcessInstanceUid(processInstanceUID);
    String instIds[] = getSeperateIdsFromBPSSDocId(documentId);
    gDoc.setProcessInstanceID(instIds[IRnifConstant.INDEX_PROCESS_INSTANCE_ID]);
    Logger.debug("set processInstanceUid to " + gDoc.getProcessInstanceUid());
    Logger.debug("set processInstanceId to " + gDoc.getProcessInstanceID());
  }
  
//TWX 03042007
  private static GWFRtProcessDoc getGWFRtProcessDoc(String documentId) throws Exception
  {
    IGWFWorkflowManagerObj wfMgr = ProcessInstanceActionHelper.getWorkflowMgr();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GWFRtProcessDoc.DOCUMENT_ID,
      filter.getEqualOperator(),
      documentId,
      false);
    Collection rtProcessDocList = wfMgr.getRtProcessDocList(filter);
    if ((rtProcessDocList != null) && (!rtProcessDocList.isEmpty()))
    {
      return (GWFRtProcessDoc)rtProcessDocList.iterator().next();
    }
    else
    {
      Logger.debug("WARNING: No rtProcessDoc found for " + documentId + "!");
      return null;
    }
  }
  
  /**
   * TWX 0304 2007 Set the customer BE ID into the rtprocess Doc
   * @param rtprocessDoc
   * @param gdoc
   * @param isSendingDoc
   * @throws Throwable
   */
  private static void setCustomerBEID(GWFRtProcessDoc rtprocessDoc, GridDocument gdoc, boolean isSendingDoc) throws Throwable
  {
    if(gdoc == null)
    {
      Logger.log("GDOC is null. Failed to update the Customer BE ID into GWFRTProcess Doc with document ID "+ (rtprocessDoc == null ? "": rtprocessDoc.getDocumentId()));
      return;
    }
    
    String customerBEID = "";
    if(isSendingDoc)      
    {
      customerBEID = gdoc.getSenderBizEntityId();
    }
    else
    {
      customerBEID = gdoc.getRecipientBizEntityId();
    }
    
    if(rtprocessDoc != null)
    {
      rtprocessDoc.setCustomerBEId(customerBEID);
      
      try
      {
        UtilEntity.update(rtprocessDoc, true);
        Logger.log("GDOC: ID "+gdoc.getGdocId()+" Folder "+gdoc.getFolder()+" Add Customer BE ID "+customerBEID+" to RTProcess Doc with documentID "+ rtprocessDoc.getDocumentId());
      }
      catch(Throwable th)
      {
        Logger.error(ILogErrorCodes.GT_RNIF_ERROR_UPDATE_CUS_BEID, "Error in updating the customer be ID into rtprocessdoc with document id "+rtprocessDoc == null? "" : rtprocessDoc.getDocumentId(), th);
        throw th;
      }
    }
  }
  
  public static void setProcessInstanceIds(
    GridDocument gDoc,
    String documentId)
    throws Exception
  {
    IGWFWorkflowManagerObj wfMgr = ProcessInstanceActionHelper.getWorkflowMgr();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GWFRtProcessDoc.DOCUMENT_ID,
      filter.getEqualOperator(),
      documentId,
      false);
    Collection rtProcessDocList = wfMgr.getRtProcessDocList(filter);
    if ((rtProcessDocList != null) && (!rtProcessDocList.isEmpty()))
    {
      GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc)rtProcessDocList.iterator().next();
      gDoc.setProcessInstanceUid(rtProcessDoc.getRtBinaryCollaborationUId());
      String instIds[] = getSeperateIdsFromBPSSDocId(documentId);
      gDoc.setProcessInstanceID(instIds[IRnifConstant.INDEX_PROCESS_INSTANCE_ID]);
      Logger.debug("set processInstanceUid to " + gDoc.getProcessInstanceUid());
      Logger.debug("set processInstanceId to " + gDoc.getProcessInstanceID());
    }
    else
    {
      Logger.debug("WARNING: No rtProcessDoc found for " + documentId + "!");
    }
  }
  
  public static void setUserTrackingId(
    ProcessDef def,
    GridDocument gDoc,
    RNProfile profile,
    boolean isRequest)
    throws Exception
  {
    if (isRequest)
    {
      String xpath = def.getUserTrackingIdentifier();
      String userTrackingId = DocumentUtil.extractValueFromUDoc(gDoc, xpath);
      gDoc.setUserTrackingID(userTrackingId);
      Logger.debug("Request doucment, set userTrackingId = " + gDoc.getUserTrackingID());
    }
    else
    {
      ProfileUtil profileUtil = new ProfileUtil();
      if (profile == null)
        profile = profileUtil.getProfileMustExist(gDoc);
      String instId[] = new String[2];
      instId[0] = profile.getProcessOriginatorId();
      instId[1] = profile.getProcessInstanceId();
      Collection profileKeys = profileUtil.getProfileKeysOfInstance(instId);
      if ((profileKeys != null) && (!profileKeys.isEmpty()))
      {
        Collection gDocs = DocumentUtil.findGridDocsByProfileUid(null, profileKeys);
        Iterator itr = gDocs.iterator();
        while (itr.hasNext())
        {
          GridDocument oldGDoc = (GridDocument)itr.next();
//          Logger.debug("Found an old gDoc " + oldGDoc);
          String userTrackingId = oldGDoc.getUserTrackingID();
          if ( (userTrackingId != null) && (!userTrackingId.equals("")))
          {
            gDoc.setUserTrackingID(oldGDoc.getUserTrackingID());
            Logger.debug("Response document, set userTrackingId = " +
              oldGDoc.getUserTrackingID());
            break;
          }
        }
      }
    }
  }

  GridDocument getDocFromDocObj(Object docObj) throws Exception
  {
    if (docObj == null)
      throw new FindEntityException("[BPSSHandler.getDocFromDocObj] cannot get GridDocument based on input null!");
    if (docObj instanceof GridDocument)
      return (GridDocument) docObj;
    Long gDocKey= (Long) docObj;
    GridDocument gDoc= DocumentUtil.getDocumentByKey(gDocKey);
    Logger.debug("getDocFromDocObj from input docObj =" + gDocKey + ";returns:" + gDoc);
    return gDoc;
  }

  public static String[] getSeperateIdsFromBPSSDocId(String documentId)
  {
    int index= documentId.lastIndexOf('/');
    String[] res= new String[2];
    res[IRnifConstant.INDEX_PROCESS_INITIATOR]= documentId.substring(index + 1);
    res[IRnifConstant.INDEX_PROCESS_INSTANCE_ID]= documentId.substring(0, index);
    return res;
  }
  
  //TWX 16062006
  private void send0A1(GridDocument gdoc, boolean isRequest,
                       String reasonStr)
  {
  	try
  	{ 
  		SendFailureNotificationAction nofAction = new SendFailureNotificationAction();
  		
  		nofAction.sendFailureNotification(gdoc, reasonStr);
  	}
  	catch(Exception ex)
  	{
  		Logger.error(ILogErrorCodes.GT_RNIF_SEND_0_ERROR,
  		             "[BpssHandler.send0A1] error in sending 0A1 for gdoc with gdocID:"+gdoc.getGdocId()+", folder:"+gdoc.getFolder()+", processinstanceID:"+gdoc.getProcessInstanceID()+". Error:", ex);
  	}
  }
  
  //TWX 20062006
  private void handleFailedToReceiveBizDocument(GridDocument gDoc, boolean isRetry, boolean isValid,
                                                boolean isRequest, String documentId, String documentType,
                                                String processType, String reasonStr)
  {
  	if(gDoc != null && gDoc.getProcessInstanceUid() != null
  		  && ! (Boolean.TRUE.equals(isRetry) || !Boolean.TRUE.equals(isValid))) //we will not trigger 0A1 for retry or invalid case
  	{
  		//Explicitly save the gdoc here if the exception occured prior we save it
  		if(gDoc.getGdocId() == null)
  		{
  			try
  			{
  				DocumentUtil.receiveFailedRnifDoc(gDoc, isValid, isRetry);
  			}
  			catch(RnifException rnifExc)
  			{
  				Logger.warn("[BpssHandler.handleFailedToReceiveBizDocument] cannot receiveFailedRnifDoc.  Err is "+rnifExc.getMessage());
  				return;
  			}
  		
  			try
  			{
  				gDoc = DocumentUtil.findGridDocByProfileUIDAndFolder(gDoc.getRnProfileUid(), "Inbound");
  				if(gDoc == null)
  				{
  					Logger.debug("[BpssHandler.handleFailedToReceiveBizDocument] Error in getting gdoc with profileUID :"+ gDoc.getRnProfileUid()+" folder is Inbound. No record found !!!");
  					return;
  				}
  				gDoc.setSenderUserId("system");
  			}
  			catch(Exception exc)
  			{
  				Logger.warn("[BpssHandler.handleFailedToReceiveBizDocument] Error in getting gdoc with profileUID :"+ gDoc.getRnProfileUid()+" folder is Inbound. Exception is " +
  				           exc.getMessage(), exc);
  				return;
  			}
  		}
  	
  		//If the receive msg is not the last msg in a particular PIP,
  		//we will trigger RN_EXCEPTION instead.
  		if(ProcessDef.TYPE_TWO_ACTION.equals(processType)
  				&& isRequest)
  		{
  			String partnerKey = documentId.substring(0, documentId.lastIndexOf("/")); //given the process def is two action PIP, the doc is Request Doc
  		                                                                          //and we are receiving doc, the initiatorID will be partnerID.
  			try
  			{
  				//fail the gdoc correspond PI
  				ProcessInstanceActionHelper.cancelProcessInstance(gDoc.getProcessInstanceUid(), reasonStr);
  			}
  			catch(Exception exc)
  			{
  				Logger.warn("[BpssHandler.handleFailedToReceiveBizDocument] Error in changing the state of process instance with UID "+
  				           gDoc.getProcessInstanceUid()+" to fail. RN_EXCEPTION will not be triggered. Exception is "+exc.getMessage(), exc);
  				return;
  			}
  			
  			try
  			{
  				sendSignal(documentId, IBpssConstants.EXCEPTION_CANCEL, reasonStr,
  			           documentType,gDoc, partnerKey);
  			}
  			catch(Throwable th)
  			{
  				Logger.warn("[BpssHandler.handleFailedToReceiveBizDocument] Error in sending the signal", th);
  			}

  		}
  		else
  		{
  			send0A1(gDoc, isRequest,reasonStr);
  		}
  	}
  }
 
}
