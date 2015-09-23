/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessAct.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Unknown	        Unknown		              Created
 * 19 Jan 06	      SC			                unsupport G_DIGEST_ALG_CODE field.
 * 14 Nov 07        Tam Wei Xiang           Added isCompressedRequired
 */

package com.gridnode.pdip.app.rnif.model;


//import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

 public class ProcessAct extends AbstractEntity
	 implements IProcessAct
{ 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7851424876250041463L;
	protected Long  _processDefUid;
	protected Integer  _processDefAct;
	protected Long   _msgType;
	protected Integer  _retries;
	protected Integer  _timeToAcknowledge;
	protected Boolean  _isAuthorizationRequired;
	protected Boolean  _isNonRepudiationRequired;
	protected Boolean  _isSecureTransportRequired;
	protected String  _bizActivityIdentifier;
	protected String  _gBizActionCode;
	protected Long  _dictFile;
	protected Long  _XMLSchema;

  protected Boolean _disableDTD;
  protected Boolean _disableSchema;
  protected Boolean _disableEncryption;
  protected Boolean _disableSignature;
  protected Boolean _validateAtSender;
  protected Boolean _onlyEncryptPayload;
//  protected String _gDigestAlgCode;
  
  protected String   _digestAlgorithm = MD5_ALG;
  protected String   _encryptionAlgorithm = RC2_ALG;
  protected Integer  _encryptionAlgorithmLength = new Integer(128);
  
  protected Boolean _isCompressRequired = new Boolean(false); //TWX 14 NOV 2007

	// ******************* Methods from AbstractEntity ******************
	public String getEntityName(){
		return ENTITY_NAME;
	 }
	public String getEntityDescr(){
		return getEntityName(); 
	}
	public Number getKeyId(){
		 return UID; 
	}

	 // ******************* get/set Methods******************
	public Long getProcessDefUid(){
		return _processDefUid;
	}
	public void setProcessDefUid(Long value){
		  _processDefUid = value;
	}
	public Integer getProcessDefAct(){
		return _processDefAct;
	}
	public void setProcessDefAct(Integer value){
		  _processDefAct = value;
	}
	public Long getMsgType(){
		return _msgType;
	}
	public void setMsgType(Long value){
		  _msgType = value;
	}
	public Integer getRetries(){
		return _retries;
	}
	public void setRetries(Integer value){
		  _retries = value;
	}
	public Integer getTimeToAcknowledge(){
		return _timeToAcknowledge;
	}
	public void setTimeToAcknowledge(Integer value){
		  _timeToAcknowledge = value;
	}
	public Boolean getIsAuthorizationRequired(){
		return _isAuthorizationRequired;
	}
	public void setIsAuthorizationRequired(Boolean value){
		  _isAuthorizationRequired = value;
	}
	public Boolean getIsNonRepudiationRequired(){
		return _isNonRepudiationRequired;
	}
	public void setIsNonRepudiationRequired(Boolean value){
		  _isNonRepudiationRequired = value;
	}
	public Boolean getIsSecureTransportRequired(){
		return _isSecureTransportRequired;
	}
	public void setIsSecureTransportRequired(Boolean value){
		  _isSecureTransportRequired = value;
	}
	public String getBizActivityIdentifier(){
		return _bizActivityIdentifier;
	}
	public void setBizActivityIdentifier(String value){
		  _bizActivityIdentifier = value;
	}
	public String getGBizActionCode(){
		return _gBizActionCode;
	}
	public void setGBizActionCode(String value){
		  _gBizActionCode = value;
	}
	public Long getDictFile(){
		return _dictFile;
	}
	public void setDictFile(Long value){
		  _dictFile = value;
	}
	public Long getXMLSchema(){
		return _XMLSchema;
	}
	public void setXMLSchema(Long value){
		  _XMLSchema = value;
	}
  
//  public String getGDigestAlgCode()
//  {
//    return _gDigestAlgCode;
//  }
//  public void setGDigestAlgCode(String value)
//  {
//    _gDigestAlgCode= value;
//  }
  public Boolean getDisableDTD()
  {
    return _disableDTD;
  }
  public void setDisableDTD(Boolean value)
  {
    _disableDTD= value;
  }
  public Boolean getDisableSchema()
  {
    return _disableSchema;
  }
  public void setDisableSchema(Boolean value)
  {
    _disableSchema= value;
  }
  public Boolean getDisableEncryption()
  {
    return _disableEncryption;
  }
  public void setDisableEncryption(Boolean value)
  {
    _disableEncryption= value;
  }
  public Boolean getDisableSignature()
  {
    return _disableSignature;
  }
  public void setDisableSignature(Boolean value)
  {
    _disableSignature= value;
  }
  public Boolean getValidateAtSender()
  {
    return _validateAtSender;
  }
  public void setValidateAtSender(Boolean value)
  {
    _validateAtSender= value;
  }
  public Boolean getOnlyEncryptPayload()
  {
    return _onlyEncryptPayload;
  }
  public void setOnlyEncryptPayload(Boolean value)
  {
    _onlyEncryptPayload= value;
  }

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    _digestAlgorithm= digestAlgorithm;
  }

  public String getDigestAlgorithm()
  {
    return _digestAlgorithm;
  }

  public void setEncryptionAlgorithm(String encryptionAlgorithm)
  {
    _encryptionAlgorithm= encryptionAlgorithm;
  }

  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }

  public void setEncryptionAlgorithmLength(Integer  encryptionAlgorithmLength)
  {
    _encryptionAlgorithmLength= encryptionAlgorithmLength;
  }

  public Integer getEncryptionAlgorithmLength()
  {
    return _encryptionAlgorithmLength;
  }
  public Boolean getIsCompressRequired() 
  {
	return _isCompressRequired;
  }
  
  public void setIsCompressRequired(Boolean compressRequired) 
  {
	_isCompressRequired = compressRequired;
  }
  
  
}
