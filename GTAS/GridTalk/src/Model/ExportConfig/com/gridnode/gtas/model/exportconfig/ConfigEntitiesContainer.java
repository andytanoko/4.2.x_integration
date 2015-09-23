/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigEntitiesContainer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2003    Koh Han Sing        Created
 * Jul 09 2003    Neo Sok Lay         Refactor: Extend from EntityDescriptorListSet.
 */
package com.gridnode.gtas.model.exportconfig;

import com.gridnode.pdip.framework.rpf.model.EntityDescriptorListSet;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorList;

import java.util.Collection;

/**
 * This class contain the ConfigEntityList. This object will be pass to the UI.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.1 I1
 */
public class ConfigEntitiesContainer extends EntityDescriptorListSet
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3041147474627478891L;

	public ConfigEntitiesContainer()
  {
  }

  public Collection getConfigEntityLists()
  {
    return getEntityDescriptorLists();
  }

  public void setConfigEntityLists(Collection configEntityLists)
  {
    setEntityDescriptorLists(configEntityLists);
  }

  public void addConfigEntityList(ConfigEntityList configEntityList)
  {
    super.addEntityDescriptorList(configEntityList);
  }


  public ConfigEntityList getConfigEntityList(String entityName)
  {
    return (ConfigEntityList)getEntityDescriptorList(entityName);
  }

  public void removeConfigEntityList(String entityName)
  {
    removeEntityDescriptorList(entityName);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#addEntityDescriptorList(IEntityDescriptorList)
   */
  public void addEntityDescriptorList(IEntityDescriptorList descriptorList)
  {
    addConfigEntityList((ConfigEntityList)descriptorList);
  }

}