/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceDescriptorCreator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.model.rnif.RnifFieldID;
import com.gridnode.gtas.server.rnif.model.ProcessInstance;
import com.gridnode.pdip.framework.db.dependency.DefaultEntityDescriptorCreator;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptor;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptorList;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * DescriptorCreator for ProcessInstance. This is used for
 * creating EntityDescriptor(s) based on process instance Map objects.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ProcessInstanceDescriptorCreator
{
  /**
   * Constructor for ProcessInstanceDescriptorCreator.
   */
  public ProcessInstanceDescriptorCreator()
  {
  }

  /**
   * Create entity descriptors for the specified set of ProcessInstance Map objects.
   * 
   * @param processInstanceMaps Set of Map objects representing the process instances.
   * @return An EntityDescriptorList holding the EntityDescriptor(s) created for 
   * each process instance Map objects.
   */
  public IEntityDescriptorList createDescriptorList(Set processInstanceMaps)
  {
    EntityDescriptorList descrList = new EntityDescriptorList(ProcessInstance.ENTITY_NAME, ProcessInstance.UID);
   
    Map map;
    for (Iterator i = processInstanceMaps.iterator(); i.hasNext();)
    {
      map = (Map) i.next();
      descrList.addEntityDescriptor(createDescriptor(map));
    }
    return descrList;
  }

  /**
   * Create EntityDescriptor for the specified process instance Map object.
   * 
   * @param processInstanceMap A Map of process instance fieldids and values.
   * @return The EntityDescriptor created.
   */
  public EntityDescriptor createDescriptor(Map processInstanceMap)
  {
    ProcessInstance instance =
      (ProcessInstance) ProcessInstance.convertMapToEntity(
        RnifFieldID.getProcessInstanceFieldID(),
        new HashMap(processInstanceMap));

    return DefaultEntityDescriptorCreator.getInstance().createDescriptor(
      instance);
  }
}
