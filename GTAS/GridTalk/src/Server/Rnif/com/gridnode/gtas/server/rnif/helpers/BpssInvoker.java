/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BpssInvoker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 09 2002    Neo Sok Lay         Handle the receive signal to complete
 *                                    the transaction of the sent doc.
 * Jan 29 2004    Neo Sok Lay         Modify handleReceivedSignal():
 *                                    - Check if no Non-acked gdoc is retrieved
 *                                      to prevent NullPointerException.
 * 9 Dec 2005     SC                  Determine bpss docId for insertBizDocToSend2BPSS(GridDocument, String, Boolean).
 * 13 Dec 2005    SC                  Add code to retrieve rn profile for case of processInstanceId is not null in 
 * 	                                  insertBizDocToSend2BPSS(GridDocument, String, Boolean) 
 * 06 Jan 2006    Neo Sok Lay         To use the specified processInstanceId if specified in Gdoc.
 * 08 Nov 2006    Tam Wei Xiang       Modified method handleReceivedSignal() to include the signal audit file name
 *                                    into gdoc's receiptAuditFilename of the doc we sent out.
 * 26 Jan 2007    Tam Wei Xiang       No OB doc is allowed to be inserted into WF if sender is responding role, 
 *                                    the doc is a response doc and response rnprofile existed.
 * Jul 25 2008    Tam Wei Xiang       #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.                                   
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.notification.DocumentTransactionHandler;
import com.gridnode.gtas.server.rnif.act.ValidateSignalDocAction;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.rnif.helpers.BpssGenerator;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

public class BpssInvoker
{
  public static IGWFWorkflowManagerObj getWorkflowMgr() throws ServiceLookupException
  {
    return (IGWFWorkflowManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFWorkflowManagerHome.class.getName(),
      IGWFWorkflowManagerHome.class,
      new Object[0]);
  }

  public static String getBPSSDocId(String originator, String instanceId)
  {
    return instanceId + "/" + originator;
  }

  /**
   * only business message.
   * for response message, the profile should already exit.
   */
  public static void insertBizDocToSend2BPSS(
    GridDocument gDoc,
    String defName,
    Boolean isRequestObj)
    throws RnifException
  {
    boolean isRequest= Boolean.TRUE.equals(isRequestObj);
    String instanceId= null;
    String originator= null;
    String docId= null;
    String sender= null;

    ProfileUtil profileUtil = new ProfileUtil();

    try
    {
      Long profileUid = gDoc.getRnProfileUid();
      if(profileUid != null && isRequest)
      {
        gDoc.setRnProfileUid(null);
        if(gDoc.getKey()!= null && gDoc.getUId() != 0L)
          DocumentUtil.updateDocument(gDoc);
      }
    }catch(Exception ex)
    {
      throw RnifException.documentSendEx("Error in reset the request Document's RNProfileUid!", ex);
    }


    RNProfile profile= profileUtil.getProfileMaytExist(gDoc);
    ProcessDef def= ProcessUtil.getProcessDef(defName);

//  SC
    String processInstanceId = gDoc.getProcessInstanceID();
    //NSL20060106 To set the docId base on the specified processInstanceId during import
    //if (isRequest == false && isNotEmpty(processInstanceId))
    if (isNotEmpty(processInstanceId))
    {
    	instanceId = processInstanceId;
    	//originator = gDoc.getRecipientPartnerId();
      originator = isRequest? IBpssConstants.PARTNER_CONSTANT : gDoc.getRecipientPartnerId();
    	docId = getBPSSDocId(originator, instanceId);
    	if (isRequest == false)
      {
      	try
      	{
  	      profile= profileUtil.getProfileFromProcessInstanceId(defName, processInstanceId, originator);
  	      if (profile != null)
  	      {
  	        gDoc.setRnProfileUid((Long) profile.getKey());
  	        DocumentUtil.updateDocument(gDoc);
  	      }
  	      else
  	      {
  	        throw RnifException.profileNotFoundEx(
  	          "Cannot found RNProfile for response Doc defName is "
  	            + defName
  	            + " processInstanceId is "
  	            + processInstanceId,
  	          new Exception());
  	      }
       } catch (Exception ex)
       {
         throw RnifException.profileNotFoundEx(
           "Cannot found RNProfile for response Doc "
             + gDoc.getEntityDescr()
             + " defName is "
             + defName,
           new Exception());
       }
      }
    } else
    {
	    if ((!isRequest) && (profile == null))
	    {
	      try
	      {
	        String xpath= def.getResponseDocRequestDocIdentifier();
	        if (xpath != null)
	        {
	          String uniqueValue= DocumentUtil.extractValueFromUDoc(gDoc, xpath);
	          if (uniqueValue != null)
	          {
	            String partnerKey = gDoc.getRecipientPartnerId();
	            profile= profileUtil.getProfileFromUniqueValue(defName, uniqueValue, partnerKey);
	            if (profile != null)
	            {
	              gDoc.setRnProfileUid((Long) profile.getKey());
	              DocumentUtil.updateDocument(gDoc);
	            }
	            else
	            {
	              throw RnifException.profileNotFoundEx(
	                "Cannot found RNProfile for response Doc defName is "
	                  + defName
	                  + " uniqueValue is "
	                  + uniqueValue,
	                new Exception());
	            }
	          }
	          else
	          {
	            throw RnifException.profileNotFoundEx(
	              "Cannot found RNProfile for response Doc because extracted uniqueValue is null! ",
	              new Exception());
	          }
	        }
	      }
	      catch (RnifException ex)
	      {
	        throw ex;
	      }
	
	      catch (Exception ex)
	      {
	        throw RnifException.profileNotFoundEx(
	          "Cannot found RNProfile for response Doc "
	            + gDoc.getEntityDescr()
	            + " defName is "
	            + defName,
	          new Exception());
	      }
	
	    }
	
	    if (profile != null)
	    {
	      instanceId= profile.getProcessInstanceId();
	      originator= profile.getProcessOriginatorId();
	      docId= getBPSSDocId(originator, instanceId);
	    }
    }

    //SC LOG
    log("processInstanceId = " + processInstanceId);
    log("docId = " + docId);
    
    String initiator= null;
    String responder= null;
    if (isRequest)
    {
      initiator= IBpssConstants.PARTNER_CONSTANT;
      responder= gDoc.getRecipientPartnerId();
    }
    else
    {
      //TWX 26012007 No OB doc is allowed to be inserted into WF if sender is responding role, the doc is a response doc, response rnprofile existed.
      //    The doc is considered duplicate, so we are not sending out to partner.
      RNProfile responseRnprofile = getResponseActionRnprofile(profile, profileUtil);
      if(responseRnprofile != null)
      {
        gDoc.setRnProfileUid(responseRnprofile.getUId());
        
        try
        {
          setProcessInstanceUId(gDoc, responseRnprofile);
          DocumentTransactionHandler.triggerDocumentTransaction(gDoc,true, false);
          Logger.log("Process Instance "+((profile != null)? profile.getProcessInstanceId(): "")+" already contain OB document, no other OB doc is allowed to be imported into Workflow !");
        }
        catch(Exception ex)
        {
          if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
          {
        	  throw RnifException.invokeBPSSErr("Error sending the TXMR Document Transaction", ex);
          }
          Logger.warn("Error in notifying the DocumentTransaction to OTC plug-in", ex);
        }/*
        finally
        {
          return;
        }*/
      }
        
      initiator= gDoc.getRecipientPartnerId();
      responder= IBpssConstants.PARTNER_CONSTANT;
    }

    String doctype= new BpssGenerator(def).getDocNameRefId(isRequest);
    try
    {
      Object docObj= getDocumentObject(gDoc);
      Logger.log(
        "call  getWorkflowMgr().insertDocument with docId="
          + docId
          + ",doctype="
          + doctype
          + ",docObj="
          + docObj
          + ", senderKey=null "
          + ";initiator="
          + initiator
          + "; responder="
          + responder);
      getWorkflowMgr().insertDocument(docId, doctype, docObj, null, initiator, responder);
    }
    catch (Exception ex)
    {
      throw RnifException.invokeBPSSErr(
        "Error in insertBizDocToSend2BPSS for GridDocument " + gDoc,
        ex);
    }
  }

  public static void insertDocReceived2BPSS(GridDocument gDoc, String defName) throws RnifException
  {
    String instanceId= null;
    String initiator= null;
    String responder= null;
    String docId= null;
    boolean isRequestMsg= true;
    RNProfile profile= new ProfileUtil().getProfileMustExist(gDoc);

    ProcessDef def= ProcessUtil.getProcessDef(defName);
    instanceId= profile.getProcessInstanceId();
    initiator= profile.getProcessOriginatorId();
    responder= profile.getProcessResponderId();
    docId= getBPSSDocId(initiator, instanceId);
    String sender= gDoc.getSenderPartnerId();

    if (profile.getIsSignalDoc())
    {
      try
      {
        boolean isRequestAct = initiator.equals(IBpssConstants.PARTNER_CONSTANT);
        ProcessAct curAct =  isRequestAct? def.getRequestAct(): def.getResponseAct();
        String RNGlobalSignalCode= profile.getSignalIdentityGlobalBusSignalCode();
        ValidateSignalDocAction validateSignal = new ValidateSignalDocAction(def, RNGlobalSignalCode);
        validateSignal.execute(gDoc, def, curAct);

        String BpssSignalType= getSignalType(profile);
        Logger.log(
          "call  getWorkflowMgr().insertSignal with docId="
            + docId
            + ",signalType="
            + BpssSignalType
            + ";reason=null "
            + ";sender="
            + sender);
        getWorkflowMgr().insertSignal(docId, BpssSignalType, null, sender);
//@todo add the signal doc into GTAS before Called by BPSS.
        DocumentUtil.receiveRnifDoc(gDoc);
        handleReceivedSignal(BpssSignalType, gDoc);
      }
      catch (Throwable ex)
      {
        throw RnifException.invokeBPSSErr(
          "Error in insertSignalReceived2BPSS for GridDocument " + gDoc.getEntityDescr(),
          ex);
      }
    }
    else //businessDoc
      {
      isRequestMsg= profile.getIsRequestMsg();
      String doctype= new BpssGenerator(def).getDocNameRefId(isRequestMsg);

      try
      {
        Object docObj = getDocumentObject(gDoc);
        Logger.log(
          "call  getWorkflowMgr().insertDocument with docId="
            + docId
            + ",doctype="
            + doctype
            + ",docObj="
            + docObj
            + ", senderKey="
            +sender
            + ";initiator="
            + initiator
            + "; responder="
            + responder);
        getWorkflowMgr().insertDocument(
          docId,
          doctype,
          docObj,
          sender,
          initiator,
          responder);

      }
      catch (Throwable ex)
      {
        throw RnifException.invokeBPSSErr(
          "Error in insertBizDocReceived2BPSS for GridDocument " + gDoc.getEntityDescr(),
          ex);
      }
    }
  }

  //  public static void insertSignalReceived2BPSS(GridDocument gDoc, String defName)
  //    throws RnifException
  //  {
  //    String instanceId= null;
  //    String originator= null;
  //    String docId= null;
  //    boolean isRequestMsg= true;
  //    RNProfile profile= ProfileUtil.getProfileMustExist(gDoc);
  //
  //    ProcessDef def= ProcessUtil.getProcessDef(defName);
  //    instanceId= profile.getProcessInstanceId();
  //    originator= profile.getProcessOriginatorId();
  //    docId= getBPSSDocId(originator, instanceId);
  //
  //    String sender= gDoc.getSenderPartnerId();
  //
  //  }

  public static String getSignalType(RNProfile profile) throws Exception
  {
    String globalSignalCode= profile.getSignalIdentityGlobalBusSignalCode();
    if ("Receipt Acknowledge".equals(globalSignalCode))
      return IBpssConstants.ACK_RECEIPT_SIGNAL;
    if ("Receipt Acknowledgment".equals(globalSignalCode))
      return IBpssConstants.ACK_RECEIPT_SIGNAL;
    if ("Exception".equals(globalSignalCode))
      return IBpssConstants.EXCEPTION_SIGNAL;
    if ("Receipt Acknowledgement Exception".equals(globalSignalCode))
      return IBpssConstants.EXCEPTION_SIGNAL;
    if ("General Exception".equals(globalSignalCode))
      return IBpssConstants.EXCEPTION_SIGNAL;
    throw new IllegalArgumentException("cannot determined SignalType from globalSignalCode=" + globalSignalCode);
  }

  static Object getDocumentObject(GridDocument gDoc)
  {
    if(gDoc.getKey() == null)
      return gDoc;
    long uid= gDoc.getUId();
    Logger.debug("Document's key is " + uid);
    if (uid != 0L)
    {
      Logger.debug("getDocumentObject with input gDoc =" + gDoc);
      //0 is the default Uid, assume that database will not give a uid of 0 to a GridDocument
      return new Long(uid);
    }
    return gDoc;
  }

  private static void handleReceivedSignal(String signalType, GridDocument signalDoc)
    throws Exception
  {
    Collection docKeys = ProcessInstanceActionHelper.findAckedSendBizDocKeys(signalDoc);

    if (docKeys != null && !docKeys.isEmpty())
    {
      GridDocument sentGdoc = DocumentUtil.findEarliestNoAckDoc(docKeys);
      if (sentGdoc == null) // don't do anything else since no Non-Acked document is found.
        return;
        
      boolean hasProcessCompleted = DocumentUtil.hasAnyAckedDoc(docKeys);
      
      sentGdoc.setReceiptAuditFileName(signalDoc.getAuditFileName()); //TWX 08112006 Added in the receiptAudit filename
      // complete the earliest Non-acked sent docs of the same process instance,
      // including resend docs.
      // next RN_ACK received for the same process instance will cause another
      // completeDocTrans for the next early doc
      if (IBpssConstants.ACK_RECEIPT_SIGNAL.equals(signalType))
      {
        //RN ACK
        DocumentUtil.completeDocTrans(sentGdoc, false, hasProcessCompleted);
      }
      else
      {
        //RN_Exception
        DocumentUtil.completeDocTrans(sentGdoc, true, hasProcessCompleted);
      }
    }
  }

  private static boolean isNotEmpty(String str)
  {
  	return str != null && str.trim().equals("") == false;
  }
 
//SC LOG
  private static void log(String message)
  {
  	Logger.log("[BpssInvoker] " + message);
  }
  
  /**
   * TWX 29012007 Retrieve the OB action gdoc's rnprofile.
   * @param profile
   * @param profileUtil
   * @return
   * @throws RnifException
   */
  private static RNProfile getResponseActionRnprofile(RNProfile profile, ProfileUtil profileUtil) throws RnifException
  {
    if(profile != null)
    {
      String processInstanceID = "";
      String senderDuns = profile.getReceiverGlobalBusIdentifier();
      try
      {
        processInstanceID = profile.getProcessInstanceId();
        Collection responseActionProfiles = profileUtil.getProfileKeysOfBizDocSendByDUNS(processInstanceID, senderDuns, false);
        log("size for obAction profile is greater than 0 "+(responseActionProfiles != null && responseActionProfiles.size() > 0));
        
        if(responseActionProfiles!= null && responseActionProfiles.size() > 0)
        {
          Long profileKey = (Long)responseActionProfiles.iterator().next();
          return profileUtil.getProfile(profileKey);
        }
        else
          return null;
      }
      catch(Exception ex)
      {
        throw RnifException.invokeBPSSErr(
                                          "Error in getting list of OB profiles given [processInstanceID :" +processInstanceID+", senderDuns: "+senderDuns+" isRequest: "+false+"]" ,
                                          ex);
      }
    }
    else
    {
      log("RNProfile is null");
      return null;
    }
  }
  
  private static void setProcessInstanceUId(GridDocument gDoc, RNProfile rnprofile)
                                           throws Exception
  {
    String documentId = rnprofile.getProcessInstanceId()+"/"+rnprofile.getProcessOriginatorId();
    IGWFWorkflowManagerObj wfMgr = getWorkflowMgr();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,GWFRtProcessDoc.DOCUMENT_ID, filter.getEqualOperator(),
                           documentId, false);
    Collection rtProcessDocList = wfMgr.getRtProcessDocList(filter);
    if ((rtProcessDocList != null) && (!rtProcessDocList.isEmpty()))
    {
      GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc)rtProcessDocList.iterator().next();
      gDoc.setProcessInstanceUid(rtProcessDoc.getRtBinaryCollaborationUId());
    }
    else
    {
      throw new ApplicationException("Error in finding the GWFRtProcessDoc entity given documentID: "+documentId);
    }
  }
}
