/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProfileUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 5, 2003    Neo Sok Lay         Fix GNDB00016432: RNProfile fields
 *                                    related to GWFRtProcessDoc.documentId
 *                                    are truncated - ProcessInstanceId, PIPInstanceIdentifier,
 *                                    ProcessTransactionId, ProcessActionId
 * 13 Dec 2005	  SC                  Add method getProfileFromProcessInstanceId.
 * 19 Jan 2006    Neo Sok Lay         GNDB00026521: Add method getProfileKeysOfRequestingMsg().
 * 06 Feb 2006    Tam Wei Xiang       Modified method getProfileKeysOfRequestingMsg()
 * 09 Jul 2007    Neo Sok Lay         GNDB00028407: Set optional tags --
 *                                    RECEIVER_DOMAIN, RECEIVER_LOCATION_ID, SENDER_DOMAIN, 
 *                                    SENDER_LOCATION_ID
 * 16 Jul 2007    Neo Sok Lay         Set optional fields from options file. 
 * 06 Oct 2009    Tam Wei Xiang       #1053: Merge changes (add optional field) from GT402X. 
 *                                           Allow user to configure the DeliveryHeader's LocationID
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.model.IRNProfile;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.base.rnif.helper.RNPackager;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Iterator;

public class ProfileUtil
{
  IRnifManagerObj _rnifMgr= null;
  public static IRnifManagerObj getRnifManager() throws RnifException
  {
    try
    {
      return (IRnifManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
        IRnifManagerHome.class.getName(),
        IRnifManagerHome.class,
        new Object[0]);
    }
    catch (ServiceLookupException ex)
    {
      throw RnifException.profileNotFoundEx("Cannot lookup RnifMgr Object!", ex);
    }
  }

  public ProfileUtil() throws RnifException
  {
    _rnifMgr= getRnifManager();
  }

  public ProfileUtil(IRnifManagerObj rnifMgr) throws RnifException
  {
    if (rnifMgr != null)
      _rnifMgr= rnifMgr;
    else
      _rnifMgr= getRnifManager();
  }

  RNProfile addProfile(RNProfile profile) throws RnifException
  {
    try
    {
      return _rnifMgr.createRNProfile(profile);
    }
    catch (Exception ex)
    {
      throw RnifException.profileCreateEx(profile.toString(), ex);
    }
  }

  RNProfile getProfile(Long profileUid) throws RnifException
  {
    try
    {
      if (profileUid == null || profileUid.intValue() == 0)
        return null;
      return _rnifMgr.findRNProfile(profileUid);
    }
    catch (Exception ex)
    {
      throw RnifException.profileNotFoundEx(profileUid.toString(), ex);
    }
  }

  public void updateProfile(RNProfile profile) throws RnifException
  {
    try
    {
      _rnifMgr.updateRNProfile(profile);
    }
    catch (Exception ex)
    {
      throw RnifException.profileUpdateEx(profile.getEntityDescr(), ex);
    }
  }

  Collection getProfileKeysOfInstance(String[] instId) throws Exception
  {
    Number[] fieldIds=
      new Number[] { RNProfile.PROCESS_ORIGINATOR_ID, RNProfile.PROCESS_INSTANCE_ID };

    IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, instId);

    Collection profileKeys= _rnifMgr.findRNProfilesKeys(filter);
    
    //031205NSL: Hack to retrieve RNProfiles whose ProcessInstanceId previously truncated due to 
    //           insufficient column size 80.
    if (profileKeys == null || profileKeys.isEmpty() && instId[1].length() > 80)
    {
      filter = EntityUtil.getEqualFilter(fieldIds, new String[]{instId[0],instId[1].substring(0, 80)});
      profileKeys = _rnifMgr.findRNProfilesKeys(filter);
    }
    return profileKeys;
  }

  public Collection getProfilesByFilter(IDataFilter filter) throws Exception
  {
    Collection profileKeys= _rnifMgr.findRNProfiles(filter);
    return profileKeys;
  }

  void deleteProfileofInstance(String[] instId, boolean alsoDeleteDocument) throws RnifException
  {
    try
    {
      Collection profileKeys= getProfileKeysOfInstance(instId);
      if (profileKeys != null && !profileKeys.isEmpty())
      {
        DocumentUtil.deleteReleventGridDoc(profileKeys, alsoDeleteDocument);

        Iterator iter= profileKeys.iterator();
        for (; iter.hasNext();)
        {
          Long profileUid= (Long) iter.next();
          try
          {
            _rnifMgr.deleteRNProfile(profileUid);
          }
          catch (Exception ex)
          {
            throw RnifException.profileDeleteEx(profileUid.toString(), ex);
          }
        }
      }
    }
    catch (RnifException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw RnifException.profileDeleteEx(
        "in ProfileUtil.deleteProfileofInstance, with instID " + instId[0] + "/" + instId[1],
        ex);
    }
  }

  public RNProfile getProfileMustExist(GridDocument gDoc) throws RnifException
  {
    RNProfile profile;
    Long profileUid= gDoc.getRnProfileUid();

    if (profileUid == null || profileUid.intValue() == 0L)
      throw RnifException.profileNotFoundEx("Document has no RNProfile!", null);
    profile= getProfile(profileUid);
    if (profile == null)
      throw RnifException.profileNotFoundEx(
        "There is no RNProfile with uid " + profileUid + "!",
        null);
    return profile;
  }

  public RNProfile getProfileMaytExist(GridDocument gDoc)
  {
    RNProfile profile;
    Long profileUid= gDoc.getRnProfileUid();

    if (profileUid == null || profileUid.intValue() == 0L)
      return null;
    try
    {
      return getProfile(profileUid);
    }
    catch (RnifException ex)
    {
      Logger.warn("[ProfileUtil.getProfile()]", ex);
      return null;
    }
  }

  public ProcessAct getSenderCurAct(ProcessDef def, boolean isInitiator, boolean isSignal)
  {
    boolean isRequestAct= isInitiator != isSignal;
    return isRequestAct ? def.getRequestAct() : def.getResponseAct();
  }

  RNProfile createRNProfile(
    GridDocument gDoc,
    ProcessDef def,
    String instanceId,
    boolean isRequest)
    throws Exception
  {
    RNProfile profile= null;
    if (isRequest)
      profile= createRequestRNProfile(gDoc, def, instanceId);
    else
      profile= createResponseRNProfile(gDoc, def, instanceId);

    profile= addProfile(profile);
    return profile;
  }

  static final String DUNS = "DUNS";
  
  RNProfile createRequestRNProfile(GridDocument gDoc, ProcessDef def, String instanceId)
    throws Exception
  {
    RNProfile profile= new RNProfile();

    profile.setProcessDefName(def.getDefName());
    profile.setProcessOriginatorId(IBpssConstants.PARTNER_CONSTANT);
    profile.setProcessResponderId(gDoc.getRecipientPartnerId());
    profile.setProcessInstanceId(instanceId);

    profile.setRNIFVersion(def.getRNIFVersion());

    profile.setIsRequestMsg(true);
    profile.setIsSignalDoc(false);

    String partnerDUNS=
      EnterpriseUtil.getDUNS(gDoc.getRecipientNodeId(), gDoc.getRecipientBizEntityId());
    String myDUNS= EnterpriseUtil.getDUNS(gDoc.getSenderNodeId(), gDoc.getSenderBizEntityId());

    profile.setReceiverGlobalBusIdentifier(partnerDUNS);
    profile.setSenderGlobalBusIdentifier(myDUNS);
    String initDUNS= myDUNS;
    profile.setPartnerGlobalBusIdentifier(initDUNS);

    profile.setDeliveryMessageTrackingId(DocumentUtil.getUniqueMsgTrackId());

    // Service Header fields
    //    profile.setNumberOfAttas(new Integer(0));

    ProcessAct curAct= def.getRequestAct();
    profile.setBusActivityIdentifier(curAct.getBizActivityIdentifier());
    profile.setActionIdentityGlobalBusActionCode(curAct.getGBizActionCode()); //only for normal doc

    /*NSL20070716 These are optional fields
    profile.setActionIdentityStandardVersion(def.getVersionIdentifier());
    profile.setActionIdentityToMessageStandard(def.getVersionIdentifier());
    profile.setActionIdentityVersionIdentifier(def.getVersionIdentifier());
    */
    profile.setGlobalUsageCode(def.getGUsageCode());
    profile.setPIPGlobalProcessCode(def.getGProcessIndicatorCode());
    profile.setPIPVersionIdentifier(def.getVersionIdentifier());

    profile.setPIPInstanceIdentifier(instanceId);
    profile.setProcessTransactionId(instanceId);

    profile.setProcessActionId(RNPackager.getUniqueID() + "_" + instanceId);
    profile.setAttemptCount(new Integer(1));

    //request message
    profile.setFromGlobalPartnerClassCode(def.getFromPartnerClassCode());
    profile.setToGlobalPartnerClassCode(def.getGToPartnerClassCode());

    profile.setFromGlobalPartnerRoleClassCode(def.getFromPartnerRoleClassCode());
    profile.setToGlobalPartnerRoleClassCode(def.getGToPartnerRoleClassCode());

    profile.setFromGlobalBusServiceCode(def.getFromBizServiceCode());
    profile.setToGlobalBusServiceCode(def.getGToBizServiceCode());
    
    //NSL20070716 Set optional fields from options file
    RnifOptions opts = new RnifOptions(def, gDoc.getSenderBizEntityId(), gDoc.getRecipientBizEntityId());
    if (!opts.isNoOptions())
    {
      profile.setActionIdentityToMessageStandard(opts.getActionIdentityMessageStandard());
      profile.setActionIdentityVersionIdentifier(opts.getActionIdentityVersionIdentifier());
      
      //TWX 18012008 Make it configurable
      profile.setReceiverDomain(opts.getRequestMsgReceiverIDDomain());
      profile.setSenderDomain(opts.getRequestMsgSenderIDDomain());
      
      //NSL20070709 Setup optional fields
      //TWX 20091005 refine logic that allow user to specify the locationID value
      String receiverLocationId = "";
      String senderLocationId = "";
      if(opts.getRequestSenderLocationID() != null)
      {
        senderLocationId = opts.getRequestSenderLocationID();
      }
      else
      {
        senderLocationId = EnterpriseUtil.getLocation(gDoc.getSenderNodeId(), gDoc.getSenderBizEntityId());
      }
      
      if(opts.getRequestReceiverLocationID() != null)
      {
        receiverLocationId = opts.getRequestReceiverLocationID();
      }
      else
      {
        receiverLocationId = EnterpriseUtil.getLocation(gDoc.getRecipientNodeId(), gDoc.getRecipientBizEntityId());
      }
   
      profile.setReceiverLocationId(receiverLocationId);
      profile.setSenderLocationId(senderLocationId);
    }
    return profile;

  }

  RNProfile createResponseRNProfile(GridDocument gDoc, ProcessDef def, String instanceId)
    throws Exception
  {
    RNProfile oldProfile= getProfileMustExist(gDoc);
    RNProfile profile= (RNProfile) oldProfile.clone();

    setReplyProfileFields(profile, oldProfile);

    profile.setIsRequestMsg(false);
    profile.setMsgDigest(null);

    profile.setDeliveryMessageTrackingId(DocumentUtil.getUniqueMsgTrackId());
    // Service Header fields
    //    profile.setNumberOfAttas(new Integer(0));

    ProcessAct curAct= def.getResponseAct();
    profile.setBusActivityIdentifier(curAct.getBizActivityIdentifier());
    profile.setActionIdentityGlobalBusActionCode(curAct.getGBizActionCode());
    profile.setProcessActionId(RNPackager.getUniqueID() + "_" + instanceId);
    
    //NSL20070716 Set optional fields from options file
    RnifOptions opts = new RnifOptions(def, gDoc.getSenderBizEntityId(), gDoc.getRecipientBizEntityId());
    if (!opts.isNoOptions())
    {
      profile.setActionIdentityToMessageStandard(opts.getActionIdentityMessageStandard());
      profile.setActionIdentityVersionIdentifier(opts.getActionIdentityVersionIdentifier());
      

      
      /* we will base on the location ID/Sender ID Domain given in the document we receive
      profile.setReceiverDomain(opts.getResponseMsgReceiverIDDomain());
      profile.setSenderDomain(opts.getResponseMsgSenderIDDomain());
      
      
      String receiverLocationId = "";
      String senderLocationId = "";
      if(opts.getResponseSenderLocationID() != null)
      {
        senderLocationId = opts.getResponseSenderLocationID();
      }
      else
      {
        senderLocationId = EnterpriseUtil.getLocation(gDoc.getSenderNodeId(), gDoc.getSenderBizEntityId());
      }
      
      if(opts.getResponseReceiverLocationID() != null)
      {
        receiverLocationId = opts.getResponseReceiverLocationID();
      }
      else
      {
        receiverLocationId = EnterpriseUtil.getLocation(gDoc.getRecipientNodeId(), gDoc.getRecipientBizEntityId());
      }
   
      Logger.log("createRN response msg receiverLocationId: "+receiverLocationId + " senderLocationID:"+senderLocationId);
      
      profile.setReceiverLocationId(receiverLocationId);
      profile.setSenderLocationId(senderLocationId);*/
    }
    
    return profile;
  }

  public void setReplyProfileFields(RNProfile profile, RNProfile oldProfile)
  {
    Logger.debug("[ProfileUtil.setReplyProfileFields]oldProfile.RNIFVersion is " +
      oldProfile.getRNIFVersion());
    profile.setGlobalUsageCode(oldProfile.getGlobalUsageCode());
    profile.setRNIFVersion(oldProfile.getRNIFVersion());
    profile.setAttemptCount(oldProfile.getAttemptCount());
    profile.setSenderDomain(oldProfile.getReceiverDomain());
    profile.setReceiverDomain(oldProfile.getSenderDomain());
    profile.setSenderLocationId(oldProfile.getReceiverLocationId());
    profile.setReceiverLocationId(oldProfile.getSenderLocationId());

    profile.setReceiverGlobalBusIdentifier(oldProfile.getSenderGlobalBusIdentifier());
    profile.setSenderGlobalBusIdentifier(oldProfile.getReceiverGlobalBusIdentifier());

    profile.setFromGlobalPartnerRoleClassCode(oldProfile.getToGlobalPartnerRoleClassCode());
    profile.setFromGlobalBusServiceCode(oldProfile.getToGlobalBusServiceCode());
    profile.setFromGlobalPartnerClassCode(oldProfile.getToGlobalPartnerClassCode());
    profile.setToGlobalPartnerRoleClassCode(oldProfile.getFromGlobalPartnerRoleClassCode());
    profile.setToGlobalBusServiceCode(oldProfile.getFromGlobalBusServiceCode());
    profile.setToGlobalPartnerClassCode(oldProfile.getFromGlobalPartnerClassCode());

    //response message, need to set the in reply to fields
    profile.setInReplyToGlobalBusActionCode(oldProfile.getActionIdentityGlobalBusActionCode());
    profile.setInReplyToVersionIdentifier(oldProfile.getActionIdentityVersionIdentifier());
    profile.setServiceMessageTrackingId(oldProfile.getDeliveryMessageTrackingId());
    profile.setProcessTransactionId(oldProfile.getProcessTransactionId());
    profile.setInResponseToActionID(oldProfile.getProcessActionId());

    //the following two is optional, may be omitted
    profile.setInReplyToMessageStandard(oldProfile.getActionIdentityToMessageStandard());
    //profile.setInReplyToStandardVersion(oldProfile.getActionIdentityStandardVersion());
  }

  public RNProfile createSignalRNProfileFields(
    RNProfile originalProfile,
    String signalCode,
    String signalVersion)
    throws Exception
  {
    RNProfile profile= (RNProfile) originalProfile.clone();

    profile.setDeliveryMessageTrackingId(DocumentUtil.getUniqueMsgTrackId());

    profile.setIsRequestMsg(false);
    profile.setIsSignalDoc(true);
    profile.setUniqueValue(null);
    profile.setMsgDigest(null);

    setReplyProfileFields(profile, originalProfile);
    // Service Header fields
    //  profile.setNumberOfAttas(new Integer(0));

    profile.setSignalIdentityGlobalBusSignalCode(signalCode);
    profile.setSignalIdentityVersionIdentifier(signalVersion);

    profile= addProfile(profile);
    return profile;
  }

  public RNProfile getProfileFromUniqueValue(
    String processDefName,
    String uniqueValue,
    String partnerKey)
    throws FindEntityException
  {
    try
    {
      Number[] fieldIds=
        new Number[] {
          IRNProfile.PROCESS_DEF_NAME,
          IRNProfile.UNIQUE_VALUE,
          IRNProfile.PROCESS_ORIGINATOR_ID,
          IRNProfile.IS_REQUEST_MSG };
      Object[] values= new Object[] { processDefName, uniqueValue, partnerKey, Boolean.TRUE };

      IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, values);
      Collection list= _rnifMgr.findRNProfiles(filter);
      if (list == null || list.isEmpty())
        return null;
      Iterator iter= list.iterator();
      return (RNProfile) iter.next();
    }
    catch (Exception ex)
    {
      throw new FindEntityException(
        "Error in finding RNProfile based on " + processDefName + ", " + processDefName,
        ex);
    }
  }

  public RNProfile createResendRNProfile(GridDocument gDoc) throws RnifException
  {
    RNProfile profile= getProfileMustExist(gDoc);
    profile= (RNProfile) profile.clone();
    profile.setAttemptCount(new Integer(profile.getAttemptCount().intValue() + 1));
    profile= addProfile(profile);
    return profile;
  }

  /**
   * Method getBizDocProfileKeysOfInstanceByDUNS. get the business documents send by given BE.
   * @param instId  ProcessInstance Id.
   * @param originatorId ProcessInstance Originator
   * @param senderDUNS  the party which send out the Document
   * @return Collection  a collection of Profile Uids.
   * @throws Exception
   */
  //indrectly used by alert module
  Collection getProfileKeysOfBizDocSendByDUNS(
    String instId,
    String originatorId,
    String senderDUNS)
    throws Exception
  {
    Number[] fieldIds=
      new Number[] {
        RNProfile.PROCESS_ORIGINATOR_ID,
        RNProfile.PROCESS_INSTANCE_ID,
        RNProfile.SENDER_GLOBAL_BUS_IDENTIFIER,
        RNProfile.IS_SIGNAL_DOC };

    Object[] values= new Object[] { originatorId, instId, senderDUNS, Boolean.FALSE };

    IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, values);

    Collection profileKeys= _rnifMgr.findRNProfilesKeys(filter);

    //031205NSL: Hack to retrieve RNProfiles whose ProcessInstanceId previously truncated due to 
    //           insufficient column size 80.
    if (profileKeys == null || profileKeys.isEmpty() && instId.length() > 80)
    {
      values[1] = instId.substring(0, 80);
      filter = EntityUtil.getEqualFilter(fieldIds, values);
      profileKeys = _rnifMgr.findRNProfilesKeys(filter);
    }
    
    return profileKeys;
  }

  public RNProfile getProfileFromProcessInstanceId(	String processDefName,
													String processInstanceId,
													String partnerKey) throws FindEntityException
  {
	try
	{
      Number[] fieldIds = new Number[] {
        IRNProfile.PROCESS_DEF_NAME, IRNProfile.PROCESS_INSTANCE_ID,
        IRNProfile.PROCESS_ORIGINATOR_ID, IRNProfile.IS_REQUEST_MSG
      };
	  Object[] values = new Object[] {
	    processDefName, processInstanceId, partnerKey, Boolean.TRUE
	  };

      IDataFilter filter = EntityUtil.getEqualFilter(fieldIds, values);
      Collection list = _rnifMgr.findRNProfiles(filter);
      if (list == null || list.isEmpty()) return null;
      Iterator iter = list.iterator();
      return (RNProfile) iter.next();
	}
	catch (Exception ex)
	{
      throw new FindEntityException("Error in finding RNProfile based on "
	  	                            + processDefName + ", " + processDefName,
                                    ex);
	}
  }
  
  /**
   *   
   * Get the RNProfile UIDs of the requesting message
   * @param instanceIdentifier The PIPInstanceIdentifier in the service header of the requesting message
   * @param possibleOriginatorIds The possible originators of the requesting message
   * @return Collection of UIDs of RNProfile(s) found.
   * @throws FindEntityException
   */
  public Collection getProfileKeysOfRequestingMsg(String instanceIdentifier, Collection possibleOriginatorIds,
                                                  String proprietaryDocIdentifier)
    throws FindEntityException
   {
    try
    {
      Number[] fieldIds = new Number[] {
          IRNProfile.PIP_INSTANCE_IDENTIFIER, IRNProfile.IS_REQUEST_MSG
        };
      Object[] values = {instanceIdentifier, Boolean.TRUE};
      IDataFilter filter = EntityUtil.getEqualFilter(fieldIds, values);
      filter.addDomainFilter(filter.getAndConnector(), IRNProfile.SENDER_GLOBAL_BUS_IDENTIFIER, possibleOriginatorIds, false);
      
      //TWX 06022006 To avoid the scenario where two trading partner both with 
      //             fresh GT installed and using same PIP (They serve as initiator and
      //             responder. Both Process instance ID is same.).
      if(proprietaryDocIdentifier != null && !proprietaryDocIdentifier.equals(""))
      {
      	filter.addSingleFilter(filter.getAndConnector(), IRNProfile.UNIQUE_VALUE, filter.getEqualOperator(),
      	                       proprietaryDocIdentifier, false);
      }
      
      return _rnifMgr.findRNProfilesKeys(filter);
    }
    catch (Exception ex)
    {
      throw new FindEntityException("Error in finding requesting RNProfile based on "+instanceIdentifier+", originators in "+possibleOriginatorIds);
    }
   }
  
  /**
   * Get the RNProfile UIDs of the message sent by the specified party
   * @param instId The process instance id
   * @param senderDUNS The sender DUNS number
   * @param isRequestMsg Whether the message is a business request message
   * @return Collection of UIDs of RNProfile(s) found.
   * @throws FindEntityException
   */
  public Collection getProfileKeysOfBizDocSendByDUNS(String instId, String senderDUNS, boolean isRequestMsg)
    throws FindEntityException
  {
    try
    {
      Number[] fieldIds=
        new Number[] {
          RNProfile.PROCESS_INSTANCE_ID,
          RNProfile.SENDER_GLOBAL_BUS_IDENTIFIER,
          RNProfile.IS_REQUEST_MSG,
          RNProfile.IS_SIGNAL_DOC };
      
      Object[] values= new Object[] { instId, senderDUNS, new Boolean(isRequestMsg), Boolean.FALSE };
      
      IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, values);
      
      return _rnifMgr.findRNProfilesKeys(filter);
    }
    catch (Exception ex)
    {
      throw new FindEntityException("Error in finding RNProfile for bizdoc sent by DUNS based on "+instId+", "+senderDUNS+", "+isRequestMsg);
    }
    
  }
  
}
