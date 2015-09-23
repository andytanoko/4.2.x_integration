/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyDescriptorFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 8 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

import java.util.HashMap;

/**
 * A factory that produce the dependency descriptors for an entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DependencyDescriptorFactory
{
  private static final DependencyDescriptorFactory _self = new DependencyDescriptorFactory();
  private HashMap _descriptorMap;
  
  /**
   * Constructor for DependencyDescriptorFactory.
   */
  private DependencyDescriptorFactory()
  {
    _descriptorMap = new HashMap();
  }

  /**
   * Get an instance of this factory.
   */
  public static DependencyDescriptorFactory getInstance()
  {
    return _self;
  }
 
  /**
   * Get the dependency descriptor for an entity. 
   * 
   * @param entityName Name of the entity
   * @return A DependencyDescriptor for the specified entity.
   */ 
  public DependencyDescriptor getDependencyDescriptor(String entityName)
  {
    DependencyDescriptor descriptor;

    if (_descriptorMap.containsKey(entityName)) //already loaded before
    {
      descriptor = (DependencyDescriptor)_descriptorMap.get(entityName);
    }
    else //not loaded before, load now
    {
      descriptor = DependencyDescriptorHandler.getInstance().loadDescriptor(entityName);
      if (descriptor == null) // no descriptor found, manufacture one with no checker
      {
        descriptor = new DependencyDescriptor();
        descriptor.setName(entityName);
      }
      _descriptorMap.put(entityName, descriptor);
    }
    
    return descriptor;
  }
}
