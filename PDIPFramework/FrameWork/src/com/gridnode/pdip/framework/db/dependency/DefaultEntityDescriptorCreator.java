/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEntityDescriptorCreator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

import java.util.Iterator;
import java.util.Set;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptor;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptorList;

/**
 * This is a default implementation of a creator for EntityDescriptor instances based
 * on the IEntity instances.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DefaultEntityDescriptorCreator
{
  private static final DefaultEntityDescriptorCreator _self = new DefaultEntityDescriptorCreator();
  
  /**
   * Constructor for DefaultEntityDescriptorCreator.
   */
  private DefaultEntityDescriptorCreator()
  {
  }

  /**
   * Get an instance of this class
   * 
   * @return an instance of DefaultEntityDescriptorCreator
   */
  public static DefaultEntityDescriptorCreator getInstance()
  {
    return _self;
  }
  
  /**
   * Create the descriptor for the specified entity.
   * 
   * @param entity The entity instance.
   * @return The created EntityDescriptor for the specified entity instance.
   */ 
  public EntityDescriptor createDescriptor(IEntity entity)
  {
    return new EntityDescriptor(entity.getKey(), entity.getEntityDescr());
  }

  /**
   * Create an EntityDescriptorList for holding EntityDescriptor(s) for
   * the specified set of entities. It is assume that the set of entities 
   * are of the same entity type.
   * 
   * @param entitySet A Set of IEntity instances.
   * @return The created EntityDescriptorList.
   */  
  public EntityDescriptorList createDescriptorList(Set entitySet)
  {
    EntityDescriptorList descrList = null;
    IEntity entity;

    // populate the EntityDescriptorList
    for (Iterator i=entitySet.iterator(); i.hasNext(); )
    {
      entity = (IEntity)i.next();
      if (descrList == null)   
        descrList = new EntityDescriptorList(entity.getEntityName(), entity.getKeyId());
        
      descrList.addEntityDescriptor(createDescriptor(entity));
    }
    return descrList;
  }
}
