/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessActAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2007-11-07     Tam Wei Xiang       Added option "isCompressRequired"
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.utils.StaticUtils;

public class ProcessActAForm extends GTActionFormBase
{
  // Files
  private String _msgType;
  private String _dictFile;
  private String _xmlSchema;

  // Details
  private String _bizActivityIdentifier;
  private String _bizActionCode;
  private String _retries;
  private String _timeToAcknowledge;

  // Options
  private String _isAuthorizationRequired;
  private String _isNonRepudiationRequired;
  private String _isSecureTransportRequired;
  private String _disableDtd;
  private String _disableSchema;
  private String _validateAtSender;

  // Encryption
  private String _disableEncryption;
  private String _disableSignature;
  private String _onlyEncryptPayload;
  private String _digestAlgorithm;
  private String _encryptionAlgorithm;
  private String _encryptionAlgorithmLength;
  
  // Compression
  private String _isCompressRequired;
  
  // ============================================================ //

  public String getMsgType()
  { return _msgType; }

  public Long getMsgTypeLong()
  { return StaticUtils.longValue(_msgType); }

  public void setMsgType(String msgType)
  { _msgType=msgType; }

  public String getDictFile()
  { return _dictFile; }

  public Long getDictFileLong()
  { return StaticUtils.longValue(_dictFile); }

  public void setDictFile(String dictFile)
  { _dictFile=dictFile; }

  public String getXmlSchema()
  { return _xmlSchema; }

  public Long getXmlSchemaLong()
  { return StaticUtils.longValue(_xmlSchema); }

  public void setXmlSchema(String xmlSchema)
  { _xmlSchema=xmlSchema; }

  // ============================================================ //

  public String getBizActivityIdentifier()
  { return _bizActivityIdentifier; }

  public void setBizActivityIdentifier(String bizActivityIdentifier)
  { _bizActivityIdentifier=bizActivityIdentifier; }

  public String getBizActionCode()
  { return _bizActionCode; }

  public void setBizActionCode(String bizActionCode)
  { _bizActionCode=bizActionCode; }

  public String getRetries()
  { return _retries; }

  public Integer getRetriesInteger()
  { return StaticUtils.integerValue(_retries); }

  public void setRetries(String retries)
  { _retries=retries; }

  public String getTimeToAcknowledge()
  { return _timeToAcknowledge; }

  public Integer getTimeToAcknowledgeInteger()
  { return StaticUtils.integerValue(_timeToAcknowledge); }

  public void setTimeToAcknowledge(String timeToAcknowledge)
  { _timeToAcknowledge=timeToAcknowledge; }

  // ============================================================ //

  public String getIsAuthorizationRequired()
  { return _isAuthorizationRequired; }

  public Boolean getIsAuthorizationRequiredBoolean()
  { return StaticUtils.booleanValue(_isAuthorizationRequired); }

  public void setIsAuthorizationRequired(String isAuthorizationRequired)
  { _isAuthorizationRequired=isAuthorizationRequired; }

  public String getIsNonRepudiationRequired()
  { return _isNonRepudiationRequired; }

  public Boolean getIsNonRepudiationRequiredBoolean()
  { return StaticUtils.booleanValue(_isNonRepudiationRequired); }

  public void setIsNonRepudiationRequired(String isNonRepudiationRequired)
  { _isNonRepudiationRequired=isNonRepudiationRequired; }

  public String getIsSecureTransportRequired()
  { return _isSecureTransportRequired; }

  public Boolean getIsSecureTransportRequiredBoolean()
  { return StaticUtils.booleanValue(_isSecureTransportRequired); }

  public void setIsSecureTransportRequired(String isSecureTransportRequired)
  { _isSecureTransportRequired=isSecureTransportRequired; }

  public String getDisableDtd()
  { return _disableDtd; }

  public Boolean getDisableDtdBoolean()
  { return StaticUtils.booleanValue(_disableDtd); }

  public void setDisableDtd(String disableDtd)
  { _disableDtd=disableDtd; }

  public String getDisableSchema()
  { return _disableSchema; }

  public Boolean getDisableSchemaBoolean()
  { return StaticUtils.booleanValue(_disableSchema); }

  public void setDisableSchema(String disableSchema)
  { _disableSchema=disableSchema; }

  public String getValidateAtSender()
  { return _validateAtSender; }

  public Boolean getValidateAtSenderBoolean()
  { return StaticUtils.booleanValue(_validateAtSender); }

  public void setValidateAtSender(String validateAtSender)
  { _validateAtSender=validateAtSender; }

  // ============================================================ //

  public String getDisableEncryption()
  { return _disableEncryption; }

  public Boolean getDisableEncryptionBoolean()
  { return StaticUtils.booleanValue(_disableEncryption); }

  public void setDisableEncryption(String disableEncryption)
  { _disableEncryption=disableEncryption; }

  public String getDisableSignature()
  { return _disableSignature; }

  public Boolean getDisableSignatureBoolean()
  { return StaticUtils.booleanValue(_disableSignature); }

  public void setDisableSignature(String disableSignature)
  { _disableSignature=disableSignature; }

  public String getOnlyEncryptPayload()
  { return _onlyEncryptPayload; }

  public Boolean getOnlyEncryptPayloadBoolean()
  { return StaticUtils.booleanValue(_onlyEncryptPayload); }

  public void setOnlyEncryptPayload(String onlyEncryptPayload)
  { _onlyEncryptPayload=onlyEncryptPayload; }

  public String getDigestAlgorithm()
  { return _digestAlgorithm; }

  public void setDigestAlgorithm(String digestAlgorithm)
  { _digestAlgorithm=digestAlgorithm; }

  public String getEncryptionAlgorithm()
  { return _encryptionAlgorithm; }

  public void setEncryptionAlgorithm(String encryptionAlgorithm)
  { _encryptionAlgorithm=encryptionAlgorithm; }

  public String getEncryptionAlgorithmLength()
  { return _encryptionAlgorithmLength; }

  public Integer getEncryptionAlgorithmLengthInteger()
  { return StaticUtils.integerValue(_encryptionAlgorithmLength); }

  public void setEncryptionAlgorithmLength(String encryptionAlgorithmLength)
  { _encryptionAlgorithmLength=encryptionAlgorithmLength; }
  
  public String getIsCompressRequired() 
  {
		return _isCompressRequired;
  }
  
  public Boolean getIsCompressRequiredBoolean()
  {
	  return StaticUtils.booleanValue(_isCompressRequired);
  }
  
  public void setIsCompressRequired(String compressRequired) 
  {
		_isCompressRequired = compressRequired;
  }
  
  // ============================================================ //

public void doReset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
  {
    _isAuthorizationRequired    = null;
    _isNonRepudiationRequired   = null;
    _isSecureTransportRequired  = null;
    _disableDtd                 = null;
    _disableSchema              = null;
    _validateAtSender           = null;

    _disableEncryption          = null;
    _disableSignature           = null;
    _onlyEncryptPayload         = null;
    _digestAlgorithm            = null;
    _encryptionAlgorithm        = null;
    _encryptionAlgorithmLength  = null;
    
    _isCompressRequired         = null;
  }
}