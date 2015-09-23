/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Koh Han Sing        Created
 * Oct 01 2003    Neo Sok Lay         Add BizEntityUuid and RegistryQueryUrl
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for the Activity entity. A Activity describe what
 * is done to a user document. Example import, executing mapping rule, etc
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a PartnerInfo entity instance.
 *   PartnerID        - ID of the partner.
 *   PartnerName      - Name of the partner.
 *   PartnerType      - PartnerType of the partner.
 *   PartnerGroup     - PartnerGroup of the partner.
 *   NodeID           - NodeID the partner belongs to.
 *   BizEntityID      - BizEntityID of the business entity that the partner
 *                      belongs to.
 *   BizEntityUuid    - UUID of the business entity that the partner belongs to
 *   RegistryQueryUrl - Query URL of the registry that can be used to discover
 *                      the business entity (if Uuid is present).
 *   CanDelete        - Whether the PartnerInfo can be deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2
 * @since 2.0
 */
public class PartnerInfo
  extends    AbstractEntity
  implements IPartnerInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9122486440932561372L;
	protected String  _partnerID;
  protected String  _partnerName;
  protected String  _partnerType;
  protected String  _partnerGroup;
  protected Long    _nodeID;
  protected String  _bizEntityID;
  protected String  _bizEntityUuid;
  protected String  _registryQueryUrl;

  public PartnerInfo()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getPartnerID() + "-" + getPartnerName();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getPartnerID()
  {
    return _partnerID;
  }

  public String getPartnerName()
  {
    return _partnerName;
  }

  public String getPartnerType()
  {
    return _partnerType;
  }

  public String getPartnerGroup()
  {
    return _partnerGroup;
  }

  public Long getNodeID()
  {
    return _nodeID;
  }

  public String getBizEntityID()
  {
    return _bizEntityID;
  }

  public String getBizEntityUuid()
  {
    return _bizEntityUuid;
  }

  public String getRegistryQueryUrl()
  {
    return _registryQueryUrl;
  }

  // *************** Setters for attributes *************************

  public void setPartnerID(String partnerID)
  {
    _partnerID = partnerID;
  }

  public void setPartnerName(String partnerName)
  {
    _partnerName = partnerName;
  }

  public void setPartnerType(String partnerType)
  {
    _partnerType = partnerType;
  }

  public void setPartnerGroup(String partnerGroup)
  {
    _partnerGroup = partnerGroup;
  }

  public void setNodeID(Long nodeID)
  {
    _nodeID = nodeID;
  }

  public void setBizEntityID(String bizEntityID)
  {
    _bizEntityID = bizEntityID;
  }

  public void setBizEntityUuid(String bizEntityUuid)
  {
    _bizEntityUuid = bizEntityUuid;
  }

  public void setRegistryQueryUrl(String registryQueryUrl)
  {
    _registryQueryUrl = registryQueryUrl;
  }

}