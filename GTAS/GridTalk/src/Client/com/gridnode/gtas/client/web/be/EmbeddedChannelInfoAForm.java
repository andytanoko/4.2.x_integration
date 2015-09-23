/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedBusinessEntityAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-30     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class EmbeddedChannelInfoAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _refId;
 
  private String _packagingInfo_name;
  private String _packagingInfo_description;
  private String _packagingInfo_refId;
  private String _packagingInfo_envelope;

  private String _securityInfo_name;
  private String _securityInfo_description;
  private String _securityInfo_refId;
  private String _securityInfo_encType;
  private String _securityInfo_encLevel;
  private String _securityInfo_sigType;
  private String _securityInfo_digestAlgorithm;

  private String _commInfo_name;
  private String _commInfo_description;
  private String _commInfo_refId;
  private String _commInfo_protocolType;
  private String _commInfo_url;

  /**
   * @return
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * @return
   */
  public String getName()
  {
    return _name;
  }

  /**
   * @return
   */
  public String getCommInfo_description()
  {
    return _commInfo_description;
  }

  /**
   * @return
   */
  public String getCommInfo_name()
  {
    return _commInfo_name;
  }

  /**
   * @return
   */
  public String getCommInfo_protocolType()
  {
    return _commInfo_protocolType;
  }

  /**
   * @return
   */
  public String getCommInfo_refId()
  {
    return _commInfo_refId;
  }

  /**
   * @return
   */
  public String getCommInfo_url()
  {
    return _commInfo_url;
  }

  /**
   * @return
   */
  public String getPackagingInfo_description()
  {
    return _packagingInfo_description;
  }

  /**
   * @return
   */
  public String getPackagingInfo_envelope()
  {
    return _packagingInfo_envelope;
  }

  /**
   * @return
   */
  public String getPackagingInfo_name()
  {
    return _packagingInfo_name;
  }

  /**
   * @return
   */
  public String getPackagingInfo_refId()
  {
    return _packagingInfo_refId;
  }

  /**
   * @return
   */
  public String getRefId()
  {
    return _refId;
  }

  /**
   * @return
   */
  public String getSecurityInfo_description()
  {
    return _securityInfo_description;
  }

  /**
   * @return
   */
  public String getSecurityInfo_digestAlgorithm()
  {
    return _securityInfo_digestAlgorithm;
  }

  /**
   * @return
   */
  public String getSecurityInfo_encLevel()
  {
    return _securityInfo_encLevel;
  }

  /**
   * @return
   */
  public String getSecurityInfo_encType()
  {
    return _securityInfo_encType;
  }

  /**
   * @return
   */
  public String getSecurityInfo_name()
  {
    return _securityInfo_name;
  }

  /**
   * @return
   */
  public String getSecurityInfo_refId()
  {
    return _securityInfo_refId;
  }

  /**
   * @return
   */
  public String getSecurityInfo_sigType()
  {
    return _securityInfo_sigType;
  }

  /**
   * @param string
   */
  public void setDescription(String string)
  {
    _description = string;
  }

  /**
   * @param string
   */
  public void setName(String string)
  {
    _name = string;
  }

  /**
   * @param string
   */
  public void setCommInfo_description(String string)
  {
    _commInfo_description = string;
  }

  /**
   * @param string
   */
  public void setCommInfo_name(String string)
  {
    _commInfo_name = string;
  }

  /**
   * @param string
   */
  public void setCommInfo_protocolType(String string)
  {
    _commInfo_protocolType = string;
  }

  /**
   * @param string
   */
  public void setCommInfo_refId(String string)
  {
    _commInfo_refId = string;
  }

  /**
   * @param string
   */
  public void setCommInfo_url(String string)
  {
    _commInfo_url = string;
  }

  /**
   * @param string
   */
  public void setPackagingInfo_description(String string)
  {
    _packagingInfo_description = string;
  }

  /**
   * @param string
   */
  public void setPackagingInfo_envelope(String string)
  {
    _packagingInfo_envelope = string;
  }

  /**
   * @param string
   */
  public void setPackagingInfo_name(String string)
  {
    _packagingInfo_name = string;
  }

  /**
   * @param string
   */
  public void setPackagingInfo_refId(String string)
  {
    _packagingInfo_refId = string;
  }

  /**
   * @param string
   */
  public void setRefId(String string)
  {
    _refId = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_description(String string)
  {
    _securityInfo_description = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_digestAlgorithm(String string)
  {
    _securityInfo_digestAlgorithm = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_encLevel(String string)
  {
    _securityInfo_encLevel = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_encType(String string)
  {
    _securityInfo_encType = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_name(String string)
  {
    _securityInfo_name = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_refId(String string)
  {
    _securityInfo_refId = string;
  }

  /**
   * @param string
   */
  public void setSecurityInfo_sigType(String string)
  {
    _securityInfo_sigType = string;
  }

}