/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityDescriptorList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9, 2003        Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.io.Serializable;

/**
 * This interface defines a List-like structure that will contain a
 * homogeneous list of IEntityDescriptor structures. By homogeneous,
 * it means that all IEntityDescriptor objects contained in this list
 * are of the same Entity Type as defined in this list.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IEntityDescriptorList extends Serializable
{
  /**
   * Get the Entity Type of the EntityDescriptors that this list will
   * contain.
   * 
   * @return The Entity Type.
   */
  String getEntityType();
  
  /**
   * The FieldId of the Key field of entities of Entity Type specified in this list.
   * 
   * @return Key field FieldId.
   */
  Number getKeyId();
  
  /**
   * Get all EntityDescriptor objects contained in this list.
   * 
   * @return Array of IEntityDescriptor(s) contained in this list.
   */
  IEntityDescriptor[] getEntityDescriptors();
}
