/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDescriptorList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9, 2003        Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;

/**
 * This is an implementation of the IEntityDescriptorList interface.
 * This class uses a HashSet as the underlying structure to hold the
 * list of EntityDescriptors.
 *
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDescriptorList implements IEntityDescriptorList
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2807006148193083025L;
	private static String TOSTR_PATTERN = "EntityDescriptorList[Type={0},KeyId={1},Descriptors=\n{2}\n]";
  private String _entityType;
  private Number _keyId;
  private HashSet _descriptorList;

  /**
   * Constructor for EntityDescriptorList.
   */
  protected EntityDescriptorList()
  {
    _descriptorList = new HashSet();
  }

  /**
   * Constructor for EntityDescriptorList.
   *
   * @param entityType Type of the entities described in this list
   * @param keyId FieldId of the Key field for the type of entity.
   */
  public EntityDescriptorList(String entityType, Number keyId)
  {
    _entityType = entityType;
    _keyId = keyId;
    _descriptorList = new HashSet();
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList#getEntityType()
   */
  public String getEntityType()
  {
    return _entityType;
  }

  /**
   * Set the Entity Type.
   *
   * @param entityType The Entity Type.
   */
  protected void setEntityType(String entityType)
  {
    _entityType = entityType;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList#getKeyId()
   */
  public Number getKeyId()
  {
    return _keyId;
  }

  /**
   * Sets the fieldID of the Key field of the entities.
   *
   * @param keyId FieldID of the Key field.
   */
  protected void setKeyId(Number keyId)
  {
    _keyId = keyId;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList#getEntityDescriptors()
   */
  public IEntityDescriptor[] getEntityDescriptors()
  {
    return (IEntityDescriptor[])_descriptorList.toArray(new IEntityDescriptor[_descriptorList.size()]);
  }

  /**
   * Get the EntityDescriptor objects contained in this list as a Collection.
   *
   * @return A Collection of EntityDescriptor objects in this list.
   */
  public Collection getEntityDescriptorCollection()
  {
    return _descriptorList;
  }

  /**
   * Add an EntityDescriptor to this list if there is no existing EntityDescriptor with
   * the same Key value.
   *
   * @param descriptor The EntityDescriptor to add.
   */
  public void addEntityDescriptor(IEntityDescriptor descriptor)
  {
    _descriptorList.add(descriptor);
  }

  /**
   * Clears this list. All EntityDescriptor objects in this list will be removed.
   */
  public void clear()
  {
    _descriptorList.clear();
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return MessageFormat.format(TOSTR_PATTERN, new Object[] {getEntityType(),getKeyId(),getEntityDescriptorCollection()});
  }

}
