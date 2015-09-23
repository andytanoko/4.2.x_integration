/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityDescriptorListSet
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * This interface defines a Set-like structure that contains a heterogeneous
 * list of IEntityDescriptorList structures. By heterogeneous, it means 
 * the IEntityDescriptorList(s) are of different Entity Types. 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IEntityDescriptorListSet extends Serializable
{
  /**
   * Get all EntityDescriptorList(s) this set contains.
   * 
   * @return Collection of IEntityDescriptorList objects.
   */
  Collection getEntityDescriptorLists();
  
  /**
   * Whether this set is empty, i.e. there are no EntityDescriptorList object
   * in this set.
   * 
   * @return <b>true</b> if the set is empty, <b>false</b> otherwise.
   */
  boolean isEmpty();
  
  /**
   * Set the EntityDescriptorList(s) that this set will contain.
   * 
   * @param descriptorLists Collection of IEntityDescriptorList objects.
   */
  void setEntityDescriptorLists(Collection descriptorLists);
  
  /**
   * Add a EntityDescriptorList to this set if there is no EntityDescriptorList
   * of the same Entity Type.
   * 
   * @param descriptorList The EntityDescriptorList to add.
   */
  void addEntityDescriptorList(IEntityDescriptorList descriptorList); 
  
  /**
   * Get an EntityDescriptorList contained in this set that has the
   * specified Entity Type.
   * 
   * @param entityType The Entity Type.
   * @return The EntityDescriptorList for the specified entityType, or
   * <b>null</b> if none exists.
   */
  IEntityDescriptorList getEntityDescriptorList(String entityType);
}
