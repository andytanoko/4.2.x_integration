package com.gridnode.pdip.base.rnif.model;

import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.helper.Logger;
import com.gridnode.pdip.framework.db.DataObject;
import com.gridnode.pdip.framework.util.ReflectionUtility;

import java.lang.reflect.Field;
import java.util.Date;

public class RNPackInfo extends DataObject implements IRNPackInfo
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4151424347529573674L;
	protected boolean _isEnableSignature= false;
  protected boolean _isEnableEncryption= false;
  protected boolean _isOnlyEncryptPayload= false;
  protected String _digestAlgorithm;
  protected String _encryptionAlgorithm;
  protected int _encryptionAlgorithmLength= 0;
  protected boolean _isSecureTptRequired= false;

  protected String _UDocFileName;
  protected Date _DTSendStart;

  protected boolean _isSignalDoc= false;
  protected boolean _isRequestMsg= true;
  protected boolean _isSynchronous= false;
  protected int     _attemptCount= 1;
  
  protected boolean _isCompressRequired = false; //TWX 12 NOV 2007
  
  //25012005 Mahesh : added to pass the sigVerificationException from depackager
  protected RosettaNetException _sigVerificationException;
  
  //  protected boolean _hasAttachment = false;
  public RNPackInfo()
  {
  }

  public boolean getIsEnableSignature()
  {
    return _isEnableSignature;
  }
  public void setIsEnableSignature(boolean value)
  {
    _isEnableSignature= value;
  }
  public boolean getIsEnableEncryption()
  {
    return _isEnableEncryption;
  }
  public void setIsEnableEncryption(boolean value)
  {
    _isEnableEncryption= value;
  }

  public boolean getIsOnlyEncryptPayload()
  {
    return _isOnlyEncryptPayload;
  }
  public void setIsOnlyEncryptPayload(boolean value)
  {
    _isOnlyEncryptPayload= value;
  }

  public String getDigestAlgorithm()
  {
    return _digestAlgorithm;
  }
  public void setDigestAlgorithm(String value)
  {
    _digestAlgorithm= value;
  }

/*  public String getDocFunctionCode()
  {
    return _docFunctionCode;
  }

  public String setDocFunctionCode(String value)
  {
    _docFunctionCode = value;
  }
*/
  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }
  public void setEncryptionAlgorithm(String value)
  {
    _encryptionAlgorithm= value;
  }

  public int getEncryptionAlgorithmLength()
  {
    return _encryptionAlgorithmLength;
  }
  public void setEncryptionAlgorithmLength(int value)
  {
    _encryptionAlgorithmLength= value;
  }

  public String getUDocFileName()
  {
    return _UDocFileName;
  }

  public void setUDocFileName(String value)
  {
    _UDocFileName= value;
  }

  public Date getDTSendStart()
  {
    return _DTSendStart;
  }

  public void setDTSendStart(Date value)
  {
    _DTSendStart= value;
  }

  public boolean getIsSecureTptRequired()
  {
    return _isSecureTptRequired;
  }

  public void setIsSecureTptRequired(boolean value)
  {
    _isSecureTptRequired= value;
  }

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

  //  public boolean getHasAttachment()
  //  {
  //    return _hasAttachment;
  //  }
  //
  //  public void setHasAttachment(boolean value)
  //  {
  //    _hasAttachment = value;
  //  }

  //fields from RNProfile
  //protected String _docFunctionCode;
  protected String _receiverDomain;
  protected String _receiverGlobalBusIdentifier; //Integer
  protected String _receiverLocationId;
  protected String _senderDomain;
  protected String _senderGlobalBusIdentifier; //Integer
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
  protected String _partnerGlobalBusIdentifier; //Integer
  protected String _PIPGlobalProcessCode;
  protected String _PIPInstanceIdentifier;
  protected String _PIPVersionIdentifier;
  protected String _processTransactionId;
  protected String _processActionId;
//  protected String _processIdentityDesc;
//  protected String _processIdentityDescLang;
  protected String _RnifVersion = "RNIF2.0";
  protected String _fromGlobalPartnerClassCode;
  protected String _toGlobalPartnerClassCode;
//  protected String _transactionIdentityDesc;
//  protected String _transactionIdentityDescLang;
  protected String _RNMsgId;
  protected int _numberOfAttas= 0;

  protected String _msgDigest;
  protected boolean _isNonRepudiationRequired;
  protected Long _RNProfileUid;
  protected String _processOriginatorId;

  //  protected String _hasAttachment;      // Boolean = FALSE;
  //  protected String _attachments; //List of UIds of Attachments
  //  protected String _attachmentNames;

  //only for RNProfile String fields
  public void setFieldValue(String fieldName, String value)
  {
    try
    {
      Field field= ReflectionUtility.getAccessibleField(getClass(), fieldName);
      field.set(this, value);
    }
    catch (Exception ex)
    {
      Logger.warn("[RNPackInfo.setFieldValue]", ex);
    }
  }

  public String getFieldValue(String fieldName)
  {
    try
    {
      Field field= ReflectionUtility.getAccessibleField(getClass(), fieldName);
      Object fieldValue= field.get(this);
      return (String) fieldValue;
    }
    catch (Exception ex)
    {
      Logger.warn("[RNPackInfo.getFieldValue]", ex);
      return null;
    }
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

  public String getServiceMessageTrackingId()
  {
    return _serviceMessageTrackingId;
  }
  public void setServiceMessageTrackingId(String value)
  {
    _serviceMessageTrackingId= value;
  }

/*
  public String getActionIdentityDesc()
  {
    return _actionIdentityDesc;
  }

  public void setActionIdentityDesc(String value)
  {
    _actionIdentityDesc = value;
  }

  public String getActionIdentityDescLang()
  {
    return _actionIdentityDescLang;
  }

  public void setActionIdentityDescLang(String value)
  {
    _actionIdentityDescLang = value;
  }
*/
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

/*
  public String getTransactionIdentityDesc()
  {
    return _transactionIdentityDesc;
  }

  public void setTransactionIdentityDesc(String value)
  {
    _transactionIdentityDesc = value;
  }

  public String getTransactionIdentityDescLang()
  {
    return _transactionIdentityDescLang;
  }

  public void setTransactionIdentityDescLang(String value)
  {
    _transactionIdentityDescLang = value;
  }
*/

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

/*
  public String getProcessIdentityDesc()
  {
    return _processIdentityDesc;
  }

  public void setProcessIdentityDesc(String value)
  {
    _processIdentityDesc = value;
  }

  public String getProcessIdentityDescLang()
  {
    return _processIdentityDescLang;
  }

  public void setProcessIdentityDescLang(String value)
  {
    _processIdentityDescLang = value;
  }
*/
  public void setRnifVersion(String value)
  {
    _RnifVersion = value;
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

  public void setRNMsgId(String gnMsgId)
  {
    _RNMsgId= gnMsgId;
  }

  public String getRNMsgId()
  {
    return _RNMsgId;
  }

  public int getNumberOfAttas()
  {
    return _numberOfAttas;
  }
  public void setNumberOfAttas(int value)
  {
    _numberOfAttas= value;
  }

  public void setIsSynchronous(boolean isSynchronous)
  {
    _isSynchronous= isSynchronous;
  }

  public boolean getIsSynchronous()
  {
    return _isSynchronous;
  }

  public void setAttemptCount(int attemptCount)
  {
    _attemptCount= attemptCount;
  }

  public int getAttemptCount()
  {
    return _attemptCount;
  }

  public String getMsgDigest()
  {
    return _msgDigest;
  }
  public void setMsgDigest(String value)
  {
    _msgDigest= value;
  }

  public boolean getIsNonRepudiationRequired()
  {
    return _isNonRepudiationRequired;
  }
  public void setIsNonRepudiationRequired(boolean value)
  {
    _isNonRepudiationRequired= value;
  }

  public Long getRNProfileUid()
  {
    return _RNProfileUid;
  }

  /**
   * Return the RNIF Version (RNIF1.1, RNIF2.0 or CIDX)
   */
  public String getRnifVersion()
  {
    return _RnifVersion;
  }

  public void setRNProfileUid(Long value)
  {
    _RNProfileUid= value;
  }


  //  protected String _hasAttachment;      // Boolean = FALSE;
  //  protected String _attachments; //List of UIds of Attachments
  //  protected String _attachmentNames;

  //only for RNProfile String fields

  //  public String getAttachments()
  //  {
  //    return _attachments;
  //  }
  //  public void setAttachments(String value)
  //  {
  //    _attachments = value;
  //  }
  //  public String getAttachmentNames()
  //  {
  //    return _attachmentNames;
  //  }
  //  public void setAttachmentNames(String value)
  //  {
  //    _attachmentNames = value;
  //  }

  //25012005 Mahesh : added to pass the sigVerificationException from depackager
  public RosettaNetException getSigVerificationException()
  {
    return _sigVerificationException;
  }

  public void setSigVerificationException(RosettaNetException sigVerificationException)
  {
    _sigVerificationException= sigVerificationException;
  }

  public boolean getIsCompressRequired() 
  {
	return _isCompressRequired;
  }

  public void setIsCompressRequired(boolean compressRequired) 
  {
	_isCompressRequired = compressRequired;
  }

  //#1053 TWX 20091006
  public String getProcessOriginatorId()
  {
    return _processOriginatorId;
  }

  //#1053 TWX 20091006
  public void setProcessOriginatorId(String originatorID)
  {
    _processOriginatorId = originatorID;
  }
  
  
}
