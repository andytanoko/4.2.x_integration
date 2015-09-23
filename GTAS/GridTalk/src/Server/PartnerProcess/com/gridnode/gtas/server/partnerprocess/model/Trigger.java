/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Trigger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Koh Han Sing        Created
 * Jul 24 2003    Neo Sok Lay         Change EntityDesc.
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Oct 20 2003    Guo Jianyu          Add numOfRetries, retryInterval and channelUID
 */
package com.gridnode.gtas.server.partnerprocess.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Trigger entity. A Trigger is used to
 * determine which partner function and process to execute based on
 * the document type, partner id, partner type, partner group.<P>
 *
 * The Model:<BR><PRE>
 *   UId       - UID for a Trigger entity instance.
 *   CanDelete - Whether the Trigger can be deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public class Trigger
       extends AbstractEntity
       implements ITrigger
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5591271248121709817L;
	protected Integer _triggerLevel;
  protected String  _partnerFunctionId;
  protected String  _processId = "";
  protected String  _docType;
  protected String  _partnerType;
  protected String  _partnerGroup;
  protected String  _partnerId;
  protected Integer _triggerType;
  protected Boolean _isRequest;
  protected Boolean _isLocalPending = Boolean.TRUE;
  protected Integer _numOfRetries;
  protected Integer _retryInterval;
  protected Long    _channelUID;

  public Trigger()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityDescr()
  {
    StringBuffer criteria = new StringBuffer();
    int level = getLevel().intValue();
    if (level > LEVEL_0.intValue())
      criteria.append("/Doc Type ").append(getDocumentType());
    if (level > LEVEL_1.intValue())
    {
      if (level < LEVEL_4.intValue())
      {
        criteria.append("/Partner Type ").append(getPartnerType());
        if (level == LEVEL_3.intValue())
          criteria.append("/Partner Group ").append(getPartnerGroup());
      }
      else
        criteria.append("/Partner ID ").append(getPartnerId());
    }

    return new StringBuffer("Level ").append(getLevel())
                .append("/Partner Function ").append(getPartnerFunctionId())
                .append(criteria.toString()).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // *************** Getters for attributes *************************

  public Integer getLevel()
  {
    return _triggerLevel;
  }

  public String getPartnerFunctionId()
  {
    return _partnerFunctionId;
  }

  public String getProcessId()
  {
    return _processId;
  }

  public String getDocumentType()
  {
    return _docType;
  }

  public String getPartnerGroup()
  {
    return _partnerGroup;
  }

  public String getPartnerType()
  {
    return _partnerType;
  }

  public String getPartnerId()
  {
    return _partnerId;
  }

  public Integer getTriggerType()
  {
    return _triggerType;
  }

  public Boolean isRequest()
  {
    return _isRequest;
  }

  public Boolean isLocalPending()
  {
    return _isLocalPending;
  }

  public Integer getNumOfRetries()
  {
    return _numOfRetries;
  }

  public Integer getRetryInterval()
  {
    return _retryInterval;
  }

  public Long getChannelUID()
  {
    return _channelUID;
  }
  // *************** Setters for attributes *************************

  public void setLevel(Integer triggerLevel)
  {
    _triggerLevel = triggerLevel;
  }

  public void setPartnerFunctionId(String partnerFunctionId)
  {
    _partnerFunctionId = partnerFunctionId;
  }

  public void setProcessId(String processId)
  {
    _processId = processId;
  }

  public void setDocumentType(String docType)
  {
    _docType = docType;
  }

  public void setPartnerGroup(String partnerGroup)
  {
    _partnerGroup = partnerGroup;
  }

  public void setPartnerType(String partnerType)
  {
    _partnerType = partnerType;
  }

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  public void setTriggerType(Integer triggerType)
  {
    _triggerType = triggerType;
  }

  public void setIsRequest(Boolean isRequest)
  {
    _isRequest = isRequest;
  }

  public void setIsLocalPending(Boolean isLocalPending)
  {
    _isLocalPending = isLocalPending;
  }

  public void setNumOfRetries(Integer numOfRetries)
  {
    _numOfRetries = numOfRetries;
  }

  public void setRetryInterval(Integer retryInterval)
  {
    _retryInterval = retryInterval;
  }

  public void setChannelUID(Long channelUID)
  {
    _channelUID = channelUID;
  }
}