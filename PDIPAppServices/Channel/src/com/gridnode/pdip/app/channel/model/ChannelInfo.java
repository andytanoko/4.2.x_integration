/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Add in name field.
 *                                                 - Change field names.
 * Aug 27 2002    Neo Sok Lay             Call CommInfo pre/postSerialize and
 *                                        postDeserialize() methods.
 * Sep 16 2002    Jagadeesh               Added isMaster/isPartner.
 * Sep 17 2002    Jagadeesh               Added PackagingProfile/SecurityProfile.
 * Oct 03 2002    Ang Meng Hua            Change _isMasterChannel and _isPartnerChannel
 *                                        to _isMaster and _isPartner and the
 *                                        getters/setters for these attributes
 * Oct 03 2002    Neo Sok Lay             Init boolean fields to false. Without
 *                                        this FMI cannot be found.
 * Oct 04 2002    Ang Meng Hua            Change partnerCategory from String to Short
 * Nov 26 2002    Jagadeesh               Added isRelay Filed to identify Relay Channel.
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 */
package com.gridnode.pdip.app.channel.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for ChannelInfo entity. A ChannelInfo keeps the generic inforamtion about
 * a channel.<P>
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a ChannelInfo entity instance.
 *   Name             - Name of this ChannelInfo entity.
 *   Description      - Description of this ChannelInfo entity.
 *   RefId            - Reference ID.
 *   TptProtocolType  - Type of transport protocol used.
 *   TptCommInfo      - Transport communication information of Channel.
 *   Version          - Version of the ChannelInfo entity.
 *   isMaster         - Identify this Channel to be Master Channel.
 *   isPartner        - Identify this Channel to be Partner Channel.
 *   PackagingProfile - PackagingProfile information of Channel.
 *   SecurityProfile  - SecurityProfile information of Channel.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.2 I1
 * @since 2.0
 *
 */
public class ChannelInfo extends AbstractEntity implements IChannelInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1169157838367671892L;
	private String _name = null;
  private String _description = null;
  private String _refId = null;
  private CommInfo _tptCommInfo = null;
  private PackagingInfo _packagingInfo = null;
  private SecurityInfo _securityInfo = null;
  private String _tptProtocolType = null;
  protected boolean _isMaster = false;
  protected boolean _isPartner = false;
  //private String _partnerCategory = null;
  protected Short _partnerCategory;
  private boolean _isDisable = false;
  private boolean _isRelay = false;
  private FlowControlInfo _flowControlInfo = null;

  public ChannelInfo()
  {
  }

  public String getEntityDescr()
  {
    return new StringBuffer()
      .append(getName())
      .append('/')
      .append(getDescription())
      .toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  public String getDescription()
  {
    return _description;
  }

  public String getName()
  {
    return _name;
  }

  public String getReferenceId()
  {
    return _refId;
  }

  public CommInfo getTptCommInfo()
  {
    return _tptCommInfo;
  }

  public String getTptProtocolType()
  {
    return _tptProtocolType;
  }

  public boolean isPartner()
  {
    return _isPartner;
  }

  public boolean isMaster()
  {
    return _isMaster;
  }

  public PackagingInfo getPackagingProfile()
  {
    return _packagingInfo;
  }

  public SecurityInfo getSecurityProfile()
  {
    return _securityInfo;
  }

  public Short getPartnerCategory()
  {
    return _partnerCategory;
  }

  public boolean isDisable()
  {
    return _isDisable;
  }

  public boolean isRelay()
  {
    return _isRelay;
  }

  public FlowControlInfo getFlowControlInfo()
  {
    return _flowControlInfo;
  }

  // ******************** Setters for attributes ***************************

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public void setReferenceId(String refId)
  {
    _refId = refId;
  }

  public void setTptCommInfo(CommInfo tptCommInfo)
  {
    _tptCommInfo = tptCommInfo;
  }

  public void setTptProtocolType(String tptProtocolType)
  {
    _tptProtocolType = tptProtocolType;
  }

  public void setIsPartner(boolean isPartner)
  {
    _isPartner = isPartner;
  }

  public void setIsMaster(boolean isMaster)
  {
    _isMaster = isMaster;
  }

  public void setPackagingProfile(PackagingInfo packagingInfo)
  {
    _packagingInfo = packagingInfo;
  }

  public void setSecurityProfile(SecurityInfo securityInfo)
  {
    _securityInfo = securityInfo;
  }

  public void setPartnerCategory(Short partnerCategory)
  {
    _partnerCategory = partnerCategory;
  }

  public void setIsDisable(boolean isDisable)
  {
    _isDisable = isDisable;
  }

  public void setIsRelay(boolean isRelay)
  {
    _isRelay = isRelay;
  }

  public void setFlowControlInfo(FlowControlInfo flowControlinfo)
  {
    _flowControlInfo = flowControlinfo;
  }

  public void preSerialize()
  {
    //if (_tptCommInfo != null)
    _tptCommInfo.preSerialize();
    _packagingInfo.preSerialize();
    _securityInfo.preSerialize();
  }

  public void postSerialize()
  {
    //if (_tptCommInfo != null)
    _tptCommInfo.postSerialize();
    _packagingInfo.postSerialize();
    _securityInfo.postSerialize();
  }

  public void postDeserialize()
  {
    if (_tptCommInfo != null)
      _tptCommInfo.postDeserialize();
    if (_packagingInfo != null)
      _packagingInfo.postDeserialize();
    if (_securityInfo != null)
      _securityInfo.postDeserialize();
  }

}