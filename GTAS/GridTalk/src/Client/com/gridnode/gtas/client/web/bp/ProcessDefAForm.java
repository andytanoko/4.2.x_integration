/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2003-08-20     Andrew Hill         Added userTrackingIdentifier field
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ProcessDefAForm extends GTActionFormBase
{
  // General
  private String _defName;
  private String _isSynchronous;
  private String _processType;
  private String _actionTimeOut;            // only for TwoActionProcess
  private String _rnifVersion;
  private String _processIndicatorCode;
  private String _versionIdentifier;
  private String _usageCode;
  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  //private String _digestAlgCode;

  // From
  private String _fromPartnerClassCode;     // only for RNIF 2.0
  private String _fromPartnerRoleClassCode;
  private String _fromBizServiceCode;

  // To
  private String _toPartnerClassCode;       // only for RNIF 2.0
  private String _toPartnerRoleClassCode;
  private String _toBizServiceCode;

  // Doc Identifier
  private String _requestDocThisDocIdentifier;
  private String _responseDocThisDocIdentifier;
  private String _responseDocRequestDocIdentifier;
  
  private String _userTrackingIdentifier; //20030820AH

  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  // Options
  //private String _disableDtd;
  //private String _disableSchema;
  //private String _disableSignature;
  //private String _disableEncryption;
  //private String _enableEncryptPayload;     // only for Encyption
  //private String _validateAtSender;

  // ============================================================ //

  public String getDefName()
  { return _defName; }

  public void setDefName(String defName)
  { _defName=defName; }

  public String getIsSynchronous()
  { return _isSynchronous; }

  public Boolean getIsSynchronousBoolean()
  { return StaticUtils.booleanValue(_isSynchronous); }

  public void setIsSynchronous(String isSynchronous)
  { _isSynchronous=isSynchronous; }

  public String getProcessType()
  { return _processType; }

  public void setProcessType(String processType)
  { _processType=processType; }

  public String getActionTimeOut()
  { return _actionTimeOut; }

  public Integer getActionTimeOutInteger()
  { return StaticUtils.integerValue(_actionTimeOut); }

  public void setActionTimeOut(String actionTimeOut)
  { _actionTimeOut=actionTimeOut; }

  public String getRnifVersion()
  { return _rnifVersion; }

  public void setRnifVersion(String rnifVersion)
  { _rnifVersion=rnifVersion; }

  public String getProcessIndicatorCode()
  { return _processIndicatorCode; }

  public void setProcessIndicatorCode(String processIndicatorCode)
  { _processIndicatorCode=processIndicatorCode; }

  public String getVersionIdentifier()
  { return _versionIdentifier; }

  public void setVersionIdentifier(String versionIdentifier)
  { _versionIdentifier=versionIdentifier; }

  public String getUsageCode()
  { return _usageCode; }

  public void setUsageCode(String usageCode)
  { _usageCode=usageCode; }

  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  //public String getDigestAlgCode()
  //{ return _digestAlgCode; }
  //
  //public void setDigestAlgCode(String digestAlgCode)
  //{ _digestAlgCode=digestAlgCode; }

  // ============================================================ //

  public String getFromPartnerClassCode()
  { return _fromPartnerClassCode; }

  public void setFromPartnerClassCode(String fromPartnerClassCode)
  { _fromPartnerClassCode=fromPartnerClassCode; }

  public String getFromPartnerRoleClassCode()
  { return _fromPartnerRoleClassCode; }

  public void setFromPartnerRoleClassCode(String fromPartnerRoleClassCode)
  { _fromPartnerRoleClassCode=fromPartnerRoleClassCode; }

  public String getFromBizServiceCode()
  { return _fromBizServiceCode; }

  public void setFromBizServiceCode(String fromBizServiceCode)
  { _fromBizServiceCode=fromBizServiceCode; }

  // ============================================================ //

  public String getToPartnerClassCode()
  { return _toPartnerClassCode; }

  public void setToPartnerClassCode(String toPartnerClassCode)
  { _toPartnerClassCode=toPartnerClassCode; }

  public String getToPartnerRoleClassCode()
  { return _toPartnerRoleClassCode; }

  public void setToPartnerRoleClassCode(String toPartnerRoleClassCode)
  { _toPartnerRoleClassCode=toPartnerRoleClassCode; }

  public String getToBizServiceCode()
  { return _toBizServiceCode; }

  public void setToBizServiceCode(String toBizServiceCode)
  { _toBizServiceCode=toBizServiceCode; }

  // ============================================================ //

  public String getRequestDocThisDocIdentifier()
  { return _requestDocThisDocIdentifier; }

  public void setRequestDocThisDocIdentifier(String requestDocThisDocIdentifier)
  { _requestDocThisDocIdentifier=requestDocThisDocIdentifier; }

  public String getResponseDocThisDocIdentifier()
  { return _responseDocThisDocIdentifier; }

  public void setResponseDocThisDocIdentifier(String responseDocThisDocIdentifier)
  { _responseDocThisDocIdentifier=responseDocThisDocIdentifier; }

  public String getResponseDocRequestDocIdentifier()
  { return _responseDocRequestDocIdentifier; }

  public void setResponseDocRequestDocIdentifier(String responseDocRequestDocIdentifier)
  { _responseDocRequestDocIdentifier=responseDocRequestDocIdentifier; }

  // ============================================================ //

  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  //public String getDisableDtd()
  //{ return _disableDtd; }
  //
  //public void setDisableDtd(String disableDtd)
  //{ _disableDtd=disableDtd; }
  //
  //public String getDisableSchema()
  //{ return _disableSchema; }
  //
  //public void setDisableSchema(String disableSchema)
  //{ _disableSchema=disableSchema; }
  //
  //public String getDisableSignature()
  //{ return _disableSignature; }
  //
  //public void setDisableSignature(String disableSignature)
  //{ _disableSignature=disableSignature; }
  //
  //public String getDisableEncryption()
  //{ return _disableEncryption; }
  //
  //public void setDisableEncryption(String disableEncryption)
  //{ _disableEncryption=disableEncryption; }
  //
  //public String getEnableEncryptPayload()
  //{ return _enableEncryptPayload; }
  //
  //public void setEnableEncryptPayload(String enableEncryptPayload)
  //{ _enableEncryptPayload=enableEncryptPayload; }
  //
  //public String getValidateAtSender()
  //{ return _validateAtSender; }
  //
  //public void setValidateAtSender(String validateAtSender)
  //{ _validateAtSender=validateAtSender; }

  // ============================================================ //

  public void doReset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
  {
    _isSynchronous = null;

    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //_disableDtd           = null;
    //_disableSchema        = null;
    //_disableSignature     = null;
    //_disableEncryption    = null;
    //_enableEncryptPayload = null;
    //_validateAtSender     = null;
  }
  
  public String getUserTrackingIdentifier()
  {
    return _userTrackingIdentifier;
  }

  public void setUserTrackingIdentifier(String string)
  {
    _userTrackingIdentifier = string;
  }

}