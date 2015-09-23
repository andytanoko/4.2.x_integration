/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigEntityList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2003    Koh Han Sing        Created
 * Jul 09 2003    Neo Sok Lay         Refactor: Extend from EntityDescriptorList.
 */
package com.gridnode.gtas.model.exportconfig;

//import com.gridnode.gtas.server.exportconfig.helpers.Logger;

import com.gridnode.pdip.framework.rpf.model.EntityDescriptor;
import com.gridnode.pdip.framework.rpf.model.EntityDescriptorList;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class contain a collection of ConfigEntityDescriptor. The
 * ConfigEntityDescriptors are of the same entity type.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.1 I1
 */
public class ConfigEntityList extends EntityDescriptorList
{ 
  /**
	 */
	private static final long serialVersionUID = 6926609598143101525L;

	public ConfigEntityList()
  {
    super();
  }

  public String getEntityName()
  {
    return getEntityType();
  }

  public Collection getConfigEntityDescriptors()
  {
    return getEntityDescriptorCollection();
  }

  public void setEntityName(String entityName)
  {
    setEntityType(entityName);
  }

  public void setConfigEntityDescriptors(Collection configEntityDescriptors)
  {
    clear();
    addConfigEntityDescriptors(configEntityDescriptors);
  }

  public void addConfigEntityDescriptors(Collection configEntityDescriptors)
  {
    for (Iterator i = configEntityDescriptors.iterator(); i.hasNext();)
      addConfigEntityDescriptor((ConfigEntityDescriptor) i.next());
  }

  public void addConfigEntityDescriptor(ConfigEntityDescriptor configEntityDescriptor)
  {
    super.addEntityDescriptor(configEntityDescriptor);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.EntityDescriptorList#addEntityDescriptor(EntityDescriptor)
   */
  public void addEntityDescriptor(EntityDescriptor descriptor)
  {
    addConfigEntityDescriptor((ConfigEntityDescriptor) descriptor);
  }

}