package com.gridnode.gtas.server.rnif.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class RNProfile extends AbstractEntity implements IRNProfile
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4727140987657277813L;
	protected Long _documentUid;
  protected String _processInstanceId; // = null;
  protected String _processOriginatorId; // = null;
  protected String _processResponderId; // = null;
  //	protected String  _processMsgType;
  protected String _processDefName;

  protected String _receiverDomain;
  protected String _receiverGlobalBusIdentifier;
  protected String _receiverLocationId;
  protected String _RNIFVersion;
  protected String _senderDomain;
  protected String _senderGlobalBusIdentifier;
  protected String _senderLocationId;
  protected String _deliveryMessageTrackingId;
  protected String _busActivityIdentifier;
  protected String _fromGlobalPartnerRoleClassCode;
  protected String _fromGlobalBusServiceCode;
  protected String _inReplyToGlobalBusActionCode;
  protected String _inReplyToMessageStandard;
  protected String _inReplyToStandardVersion;
  protected String _inReplyToVersionIdentifier;
  protected String _inResponseToActionID;
  protected String _serviceMessageTrackingId;
  protected String _actionIdentityGlobalBusActionCode;
  protected String _actionIdentityToMessageStandard;
  protected String _actionIdentityStandardVersion;
  protected String _actionIdentityVersionIdentifier;
  protected String _signalIdentityGlobalBusSignalCode;
  protected String _signalIdentityVersionIdentifier;
  protected String _toGlobalPartnerRoleClassCode;
  protected String _toGlobalBusServiceCode;
  protected String _globalUsageCode;
  protected String _partnerGlobalBusIdentifier;
  protected String _PIPGlobalProcessCode;
  protected String _PIPInstanceIdentifier;
  protected String _PIPVersionIdentifier;

  protected String _processTransactionId;
  protected String _processActionId;
  protected String _fromGlobalPartnerClassCode;
  protected String _toGlobalPartnerClassCode;

  protected String _userTrackingID;
  
  //for attachment
  protected Integer _numberOfAttas= DEFAULT_ATTACH_NUM;
  //	protected Boolean  _hasAttachment;      //  = Boolean.FALSE;
  //	protected String  _attachments;    //List of UIds of Attachments
  //	protected String  _attachmentNames;

  //@@todo newly added
  protected boolean _isSignalDoc= false;
  protected boolean _isRequestMsg= true;
  protected String _uniqueValue; //do not need to put into Packinfo
  protected String _gnMsgId;
  protected Integer _attemptCount= INITIAL_ATTEMPT_COUNT;
  protected String _msgDigest;

  // ******************* Methods from AbstractEntity ******************
  public String getEntityName()
  {
    return ENTITY_NAME;
  }
  public String getEntityDescr()
  {
    return getEntityName();
  }
  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************
  public Long getDocumentUid()
  {
    return _documentUid;
  }
  public void setDocumentUid(Long value)
  {
    _documentUid= value;
  }
  public String getProcessInstanceId()
  {
    return _processInstanceId;
  }
  public void setProcessInstanceId(String value)
  {
    _processInstanceId= value;
  }
  public String getProcessOriginatorId()
  {
    return _processOriginatorId;
  }
  public void setProcessOriginatorId(String value)
  {
    _processOriginatorId= value;
  }
  //	public String getProcessMsgType(){
  //		return _processMsgType;
  //	}
  //	public void setProcessMsgType(String value){
  //		  _processMsgType = value;
  //	}
  public String getProcessDefName()
  {
    return _processDefName;
  }
  public void setProcessDefName(String value)
  {
    _processDefName= value;
  }
  public String getReceiverDomain()
  {
    return _receiverDomain;
  }
  public void setReceiverDomain(String value)
  {
    _receiverDomain= value;
  }
  public String getReceiverGlobalBusIdentifier()
  {
    return _receiverGlobalBusIdentifier;
  }
  public void setReceiverGlobalBusIdentifier(String value)
  {
    _receiverGlobalBusIdentifier= value;
  }
  public String getReceiverLocationId()
  {
    return _receiverLocationId;
  }
  public void setReceiverLocationId(String value)
  {
    _receiverLocationId= value;
  }

  public String getRNIFVersion()
  {
    return _RNIFVersion;
  }

  public void setRNIFVersion(String value)
  {
    _RNIFVersion = value;
  }

  public String getSenderDomain()
  {
    return _senderDomain;
  }
  public void setSenderDomain(String value)
  {
    _senderDomain= value;
  }
  public String getSenderGlobalBusIdentifier()
  {
    return _senderGlobalBusIdentifier;
  }
  public void setSenderGlobalBusIdentifier(String value)
  {
    _senderGlobalBusIdentifier= value;
  }
  public String getSenderLocationId()
  {
    return _senderLocationId;
  }
  public void setSenderLocationId(String value)
  {
    _senderLocationId= value;
  }
  public String getDeliveryMessageTrackingId()
  {
    return _deliveryMessageTrackingId;
  }
  public void setDeliveryMessageTrackingId(String value)
  {
    _deliveryMessageTrackingId= value;
  }
  public String getBusActivityIdentifier()
  {
    return _busActivityIdentifier;
  }
  public void setBusActivityIdentifier(String value)
  {
    _busActivityIdentifier= value;
  }
  public String getFromGlobalPartnerRoleClassCode()
  {
    return _fromGlobalPartnerRoleClassCode;
  }
  public void setFromGlobalPartnerRoleClassCode(String value)
  {
    _fromGlobalPartnerRoleClassCode= value;
  }
  public String getFromGlobalBusServiceCode()
  {
    return _fromGlobalBusServiceCode;
  }
  public void setFromGlobalBusServiceCode(String value)
  {
    _fromGlobalBusServiceCode= value;
  }
  public String getInReplyToGlobalBusActionCode()
  {
    return _inReplyToGlobalBusActionCode;
  }
  public void setInReplyToGlobalBusActionCode(String value)
  {
    _inReplyToGlobalBusActionCode= value;
  }
  public String getInReplyToMessageStandard()
  {
    return _inReplyToMessageStandard;
  }
  public void setInReplyToMessageStandard(String value)
  {
    _inReplyToMessageStandard= value;
  }
  public String getInReplyToStandardVersion()
  {
    return _inReplyToStandardVersion;
  }
  public void setInReplyToStandardVersion(String value)
  {
    _inReplyToStandardVersion= value;
  }
  public String getInReplyToVersionIdentifier()
  {
    return _inReplyToVersionIdentifier;
  }
  public void setInReplyToVersionIdentifier(String value)
  {
    _inReplyToVersionIdentifier= value;
  }

  public String getInResponseToActionID()
  {
    return _inResponseToActionID;
  }

  public void setInResponseToActionID(String value)
  {
    _inResponseToActionID = value;
  }

  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String value)
  {
    _userTrackingID = value;
  }

  public String getServiceMessageTrackingId()
  {
    return _serviceMessageTrackingId;
  }
  public void setServiceMessageTrackingId(String value)
  {
    _serviceMessageTrackingId= value;
  }
  public String getActionIdentityGlobalBusActionCode()
  {
    return _actionIdentityGlobalBusActionCode;
  }
  public void setActionIdentityGlobalBusActionCode(String value)
  {
    _actionIdentityGlobalBusActionCode= value;
  }
  public String getActionIdentityToMessageStandard()
  {
    return _actionIdentityToMessageStandard;
  }
  public void setActionIdentityToMessageStandard(String value)
  {
    _actionIdentityToMessageStandard= value;
  }
  public String getActionIdentityStandardVersion()
  {
    return _actionIdentityStandardVersion;
  }
  public void setActionIdentityStandardVersion(String value)
  {
    _actionIdentityStandardVersion= value;
  }
  public String getActionIdentityVersionIdentifier()
  {
    return _actionIdentityVersionIdentifier;
  }
  public void setActionIdentityVersionIdentifier(String value)
  {
    _actionIdentityVersionIdentifier= value;
  }
  public String getSignalIdentityGlobalBusSignalCode()
  {
    return _signalIdentityGlobalBusSignalCode;
  }
  public void setSignalIdentityGlobalBusSignalCode(String value)
  {
    _signalIdentityGlobalBusSignalCode= value;
  }
  public String getSignalIdentityVersionIdentifier()
  {
    return _signalIdentityVersionIdentifier;
  }
  public void setSignalIdentityVersionIdentifier(String value)
  {
    _signalIdentityVersionIdentifier= value;
  }
  public String getToGlobalPartnerRoleClassCode()
  {
    return _toGlobalPartnerRoleClassCode;
  }
  public void setToGlobalPartnerRoleClassCode(String value)
  {
    _toGlobalPartnerRoleClassCode= value;
  }
  public String getToGlobalBusServiceCode()
  {
    return _toGlobalBusServiceCode;
  }
  public void setToGlobalBusServiceCode(String value)
  {
    _toGlobalBusServiceCode= value;
  }
  public String getGlobalUsageCode()
  {
    return _globalUsageCode;
  }
  public void setGlobalUsageCode(String value)
  {
    _globalUsageCode= value;
  }
  public String getPartnerGlobalBusIdentifier()
  {
    return _partnerGlobalBusIdentifier;
  }
  public void setPartnerGlobalBusIdentifier(String value)
  {
    _partnerGlobalBusIdentifier= value;
  }
  public String getPIPGlobalProcessCode()
  {
    return _PIPGlobalProcessCode;
  }
  public void setPIPGlobalProcessCode(String value)
  {
    _PIPGlobalProcessCode= value;
  }
  public String getPIPInstanceIdentifier()
  {
    return _PIPInstanceIdentifier;
  }
  public void setPIPInstanceIdentifier(String value)
  {
    _PIPInstanceIdentifier= value;
  }
  public String getPIPVersionIdentifier()
  {
    return _PIPVersionIdentifier;
  }
  public void setPIPVersionIdentifier(String value)
  {
    _PIPVersionIdentifier= value;
  }
  public String getProcessTransactionId()
  {
    return _processTransactionId;
  }
  public void setProcessTransactionId(String value)
  {
    _processTransactionId= value;
  }
  public String getProcessActionId()
  {
    return _processActionId;
  }
  public void setProcessActionId(String value)
  {
    _processActionId= value;
  }
  public String getFromGlobalPartnerClassCode()
  {
    return _fromGlobalPartnerClassCode;
  }
  public void setFromGlobalPartnerClassCode(String value)
  {
    _fromGlobalPartnerClassCode= value;
  }
  public String getToGlobalPartnerClassCode()
  {
    return _toGlobalPartnerClassCode;
  }
  public void setToGlobalPartnerClassCode(String value)
  {
    _toGlobalPartnerClassCode= value;
  }
  public Integer getNumberOfAttas()
  {
    return _numberOfAttas;
  }
  public void setNumberOfAttas(Integer value)
  {
    _numberOfAttas= value;
  }
  //	public Boolean getHasAttachment(){
  //		return _hasAttachment;
  //	}
  //	public void setHasAttachment(Boolean value){
  //		  _hasAttachment = value;
  //	}
  //	public String getAttachments(){
  //		return _attachments;
  //	}
  //	public void setAttachments(String value){
  //		  _attachments = value;
  //	}
  //	public String getAttachmentNames(){
  //		return _attachmentNames;
  //	}
  //	public void setAttachmentNames(String value){
  //		  _attachmentNames = value;
  //	}

  public boolean getIsSignalDoc()
  {
    return _isSignalDoc;
  }

  public void setIsSignalDoc(boolean value)
  {
    _isSignalDoc= value;
  }

  public boolean getIsRequestMsg()
  {
    return _isRequestMsg;
  }

  public void setIsRequestMsg(boolean value)
  {
    _isRequestMsg= value;
  }

  public boolean isInitiatorDoc()
  {
    return _partnerGlobalBusIdentifier.equals(_senderGlobalBusIdentifier);
  }

  public void setUniqueValue(String uniqueValue)
  {
    _uniqueValue= uniqueValue;
  }

  public String getUniqueValue()
  {
    return _uniqueValue;
  }

  public void setGnMsgId(String gnMsgId)
  {
    _gnMsgId= gnMsgId;
  }

  public String getGnMsgId()
  {
    return _gnMsgId;
  }

  public void setAttemptCount(Integer attemptCount)
  {
    _attemptCount= attemptCount;
  }

  public Integer getAttemptCount()
  {
    return _attemptCount;
  }

  public void increaseAttemptCount()
  {
    if (_attemptCount == null)
      _attemptCount= INITIAL_ATTEMPT_COUNT;
    else
      _attemptCount= new Integer(_attemptCount.intValue());
  }

  public void setProcessResponderId(String processResponderId)
  {
    _processResponderId= processResponderId;
  }

  public String getProcessResponderId()
  {
    return _processResponderId;
  }

  public String getMsgDigest()
  {
    return _msgDigest;
  }
  public void setMsgDigest(String value)
  {
    _msgDigest= value;
  }

  static final Integer INITIAL_ATTEMPT_COUNT= new Integer(1);
  static final Integer DEFAULT_ATTACH_NUM= new Integer(0);
}
