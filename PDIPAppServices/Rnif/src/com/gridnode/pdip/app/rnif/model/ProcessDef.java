package com.gridnode.pdip.app.rnif.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class ProcessDef extends AbstractEntity implements IProcessDef
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4596242080484656980L;
	protected ProcessAct _requestAct;
  protected ProcessAct _responseAct;
  protected String _defName;
  protected Integer _actionTimeOut;
  protected String _processType;
  protected String _RNIFVersion;
  protected String _fromPartnerRoleClassCode;
  protected String _fromBizServiceCode;
  protected String _fromPartnerClassCode;
  protected String _gToPartnerRoleClassCode;
  protected String _gToBizServiceCode;
  protected String _gToPartnerClassCode;
  protected String _gProcessIndicatorCode;
  protected String _versionIdentifier;
  protected String _gUsageCode;

  protected String _requestDocThisDocIdentifier;
  protected String _responseDocThisDocIdentifier;
  protected String _responseDocRequestDocIdentifier;
  protected String _userTrackingIdentifier;

  protected Boolean _isSynchronous;

//  protected Boolean _disableDTD;
//  protected Boolean _disableSchema;
//  protected Boolean _disableEncryption;
//  protected Boolean _disableSignature;
//  protected Boolean _validateAtSender;
//  protected Boolean _enableEncryptPayload;
//  protected String _gDigestAlgCode;

  // ******************* Methods from AbstractEntity ******************
  public String getEntityName()
  {
    return ENTITY_NAME;
  }
  public String getEntityDescr()
  {
    return getDefName()+"/"+getRNIFVersion()+"/"+getProcessType()+
           "/"+getGProcessIndicatorCode();
  }
  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************
  public ProcessAct getRequestAct()
  {
    return _requestAct;
  }
  public void setRequestAct(ProcessAct value)
  {
    _requestAct= value;
  }
  public ProcessAct getResponseAct()
  {
    return _responseAct;
  }
  public void setResponseAct(ProcessAct value)
  {
    _responseAct= value;
  }
  public String getDefName()
  {
    return _defName;
  }
  public void setDefName(String value)
  {
    _defName= value;
  }
  public Integer getActionTimeOut()
  {
    return _actionTimeOut;
  }
  public void setActionTimeOut(Integer value)
  {
    _actionTimeOut= value;
  }
  public String getProcessType()
  {
    return _processType;
  }
  public void setProcessType(String value)
  {
    _processType= value;
  }
  public String getRNIFVersion()
  {
    return _RNIFVersion;
  }
  public void setRNIFVersion(String value)
  {
    _RNIFVersion= value;
  }
  public String getFromPartnerRoleClassCode()
  {
    return _fromPartnerRoleClassCode;
  }
  public void setFromPartnerRoleClassCode(String value)
  {
    _fromPartnerRoleClassCode= value;
  }
  public String getFromBizServiceCode()
  {
    return _fromBizServiceCode;
  }
  public void setFromBizServiceCode(String value)
  {
    _fromBizServiceCode= value;
  }
  public String getFromPartnerClassCode()
  {
    return _fromPartnerClassCode;
  }
  public void setFromPartnerClassCode(String value)
  {
    _fromPartnerClassCode= value;
  }
  public String getGToPartnerRoleClassCode()
  {
    return _gToPartnerRoleClassCode;
  }
  public void setGToPartnerRoleClassCode(String value)
  {
    _gToPartnerRoleClassCode= value;
  }
  public String getGToBizServiceCode()
  {
    return _gToBizServiceCode;
  }
  public void setGToBizServiceCode(String value)
  {
    _gToBizServiceCode= value;
  }
  public String getGToPartnerClassCode()
  {
    return _gToPartnerClassCode;
  }
  public void setGToPartnerClassCode(String value)
  {
    _gToPartnerClassCode= value;
  }
  public String getGProcessIndicatorCode()
  {
    return _gProcessIndicatorCode;
  }
  public void setGProcessIndicatorCode(String value)
  {
    _gProcessIndicatorCode= value;
  }
  public String getVersionIdentifier()
  {
    return _versionIdentifier;
  }
  public void setVersionIdentifier(String value)
  {
    _versionIdentifier= value;
  }
  public String getGUsageCode()
  {
    return _gUsageCode;
  }
  public void setGUsageCode(String value)
  {
    _gUsageCode= value;
  }

  public String getRequestDocThisDocIdentifier()
  {
    return _requestDocThisDocIdentifier;
  }
  public void setRequestDocThisDocIdentifier(String value)
  {
    _requestDocThisDocIdentifier= value;
  }
  public String getResponseDocThisDocIdentifier()
  {
    return _responseDocThisDocIdentifier;
  }
  public void setResponseDocThisDocIdentifier(String value)
  {
    _responseDocThisDocIdentifier= value;
  }
  public String getUserTrackingIdentifier()
  {
    return _userTrackingIdentifier;
  }
  public void setUserTrackingIdentifier(String value)
  {
    _userTrackingIdentifier = value;
  }
  public String getResponseDocRequestDocIdentifier()
  {
    return _responseDocRequestDocIdentifier;
  }
  public void setResponseDocRequestDocIdentifier(String value)
  {
    _responseDocRequestDocIdentifier= value;
  }

  public void setIsSynchronous(Boolean isSynchronous)
  {
    _isSynchronous= isSynchronous;
  }

  public Boolean getIsSynchronous()
  {
    return _isSynchronous;
  }

//  public String getGDigestAlgCode()
//  {
//    return _gDigestAlgCode;
//  }
//  public void setGDigestAlgCode(String value)
//  {
//    _gDigestAlgCode= value;
//  }
//  public Boolean getDisableDTD()
//  {
//    return _disableDTD;
//  }
//  public void setDisableDTD(Boolean value)
//  {
//    _disableDTD= value;
//  }
//  public Boolean getDisableSchema()
//  {
//    return _disableSchema;
//  }
//  public void setDisableSchema(Boolean value)
//  {
//    _disableSchema= value;
//  }
//  public Boolean getDisableEncryption()
//  {
//    return _disableEncryption;
//  }
//  public void setDisableEncryption(Boolean value)
//  {
//    _disableEncryption= value;
//  }
//  public Boolean getDisableSignature()
//  {
//    return _disableSignature;
//  }
//  public void setDisableSignature(Boolean value)
//  {
//    _disableSignature= value;
//  }
//  public Boolean getValidateAtSender()
//  {
//    return _validateAtSender;
//  }
//  public void setValidateAtSender(Boolean value)
//  {
//    _validateAtSender= value;
//  }
//  public Boolean getEnableEncryptPayload()
//  {
//    return _enableEncryptPayload;
//  }
//  public void setEnableEncryptPayload(Boolean value)
//  {
//    _enableEncryptPayload= value;
//  }
}
