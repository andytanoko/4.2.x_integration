/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SharedResource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
/**
 * This is an object model for SharedResource entity. A SharedResource keeps
 * tracks of the resources that are shared to a partner Enterprise.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a SharedResource entity instance.
 *   ToEnterpriseID - ID of the Enterprise that the resource is shared to.
 *   ResourceType   - Type of the resource that is shared.
 *   ResourceUID    - UID of the resource that is shared.
 *   IsSync         - Whether the shared resource is synchronized to the destination
 *                    enterprise.
 *   SyncChecksum   - Checksum used for synchronization purpose.
 *   IsDeleted      - Whether the shared resource has been deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SharedResource
  extends    AbstractEntity
  implements ISharedResource
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -112005523664843560L;
	protected String     _toEnterpriseID;
  protected Long       _resourceUID;
  protected String     _resourceType;
  protected short      _state           = STATE_UNSYNC;
  protected long       _syncChecksum;

  public SharedResource()
  {
  }

  // *************** Methods from AbstractEntity ***************************
  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return getResource() + " --> " +
           getToEnterpriseID();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ************************* Getters for attributes ***********************

  public String getResource()
  {
    return getResourceType() + "::" + getResourceUID();
  }

  public Long getResourceUID()
  {
    return _resourceUID;
  }

  public String getToEnterpriseID()
  {
    return _toEnterpriseID;
  }

  public String getResourceType()
  {
    return _resourceType;
  }

  public short getState()
  {
    return _state;
  }

  public long getSyncChecksum()
  {
    return _syncChecksum;
  }

  // ********************** Setters for attributes ***************************

  public void setResourceUID(Long resourceUID)
  {
    _resourceUID = resourceUID;
  }

  public void setToEnterpriseID(String enterpriseID)
  {
    _toEnterpriseID = enterpriseID;
  }

  public void setResourceType(String resourceType)
  {
    _resourceType = resourceType;
  }

  public void setState(short state)
  {
    _state = state;
  }

  public void setSyncChecksum(long syncChecksum)
  {
    _syncChecksum = syncChecksum;
  }
}
