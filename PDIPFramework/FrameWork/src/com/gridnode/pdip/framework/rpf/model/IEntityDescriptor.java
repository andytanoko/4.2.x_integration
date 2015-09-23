/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.io.Serializable;

/**
 * This interface defines a simple descriptor for an entity. 
 * The Entity Type of the entity is defined by the IEntityDescriptorList
 * that the descriptor resides in.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IEntityDescriptor extends Serializable
{
  /**
   * The Key field of the entity.
   * 
   * @return Key field value of the entity described by this object.
   */
  Object getKey();
  
  /**
   * A description of the entity.
   * 
   * @return A short description of the entity described by this object.
   */
  String getDescription();
}
