/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLink.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Collection;

/**
 * This is an object model for ResourceLink entity. A ResourceLink keeps
 * tracks of the links (two-way) from one resource node to another.<P>
 *
 * The Model:<BR><PRE>
 *   UId      - UID for a ResourceLink entity instance.
 *   FromUID  - UID of the "From" resource for this ResourceLink instance.
 *   FromType - Type of the "From" resource for this ResourceLink instance.
 *   ToUID    - UID of the "To" resource for this ResourceLink instance.
 *   ToType   - Type of the "To" resource for this ResourceLink instance.
 *   Priority - Priority of the link among all other links from the same "From"
 *              resource to resources of the same resource type.
 *   NextLinks- Descendant links "From" the "To" resource of this ResourceLink.
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
public class ResourceLink
  extends    AbstractEntity
  implements IResourceLink
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4767861454077464320L;
	protected Long       _fromUID;
  protected Long       _toUID;
  protected String     _fromType;
  protected String     _toType;
  protected int        _priority;
  protected Collection _nextLinks;

  public ResourceLink()
  {
  }

  // *************** Methods from AbstractEntity ***************************
  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return getFromResource() + " --> " +
           getToResource();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ************************* Getters for attributes ***********************

  public String getFromResource()
  {
    return getFromType() + "::" + getFromUID();
  }

  public String getToResource()
  {
    return getToType() + "::" + getToUID();
  }

  public Long getFromUID()
  {
    return _fromUID;
  }

  public Long getToUID()
  {
    return _toUID;
  }

  public String getFromType()
  {
    return _fromType;
  }

  public String getToType()
  {
    return _toType;
  }

  public int getPriority()
  {
    return _priority;
  }

  public Collection getNextLinks()
  {
    return _nextLinks;
  }

  // ********************** Setters for attributes ***************************

  public void setFromUID(Long fromUID)
  {
    _fromUID = fromUID;
  }

  public void setToUID(Long toUID)
  {
    _toUID = toUID;
  }

  public void setFromType(String fromType)
  {
    _fromType = fromType;
  }

  public void setToType(String toType)
  {
    _toType = toType;
  }

  public void setPriority(int priority)
  {
    _priority = priority;
  }

  public void setNextLinks(Collection nextLinks)
  {
    _nextLinks = nextLinks;
  }
}
