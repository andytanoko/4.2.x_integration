/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDescriptorListSet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This is an implementation of the IEntityDescriptorListSet interface.
 * This class uses a Hashtable as the underlying structure to hold
 * the EntityDescriptorList structures using the EntityType as the key.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDescriptorListSet implements IEntityDescriptorListSet
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6271159348569640257L;

	private static String TOSTR_PATTERN = "EntityDescriptorListSet[\n{0}\n]";
  
  private Hashtable _descriptorListTable = new Hashtable();
  
  /**
   * Constructor for EntityDescriptorListSet.
   */
  public EntityDescriptorListSet()
  {
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#getEntityDescriptorLists()
   */
  public Collection getEntityDescriptorLists()
  {
    return _descriptorListTable.values();
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#isEmpty()
   */
  public boolean isEmpty()
  {
    return _descriptorListTable.isEmpty();
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#setEntityDescriptorLists(Collection)
   */
  public void setEntityDescriptorLists(Collection descriptorLists)
  {
    _descriptorListTable.clear();
    if (descriptorLists != null)
    {
      for (Iterator i=descriptorLists.iterator(); i.hasNext(); )
      {
        addEntityDescriptorList((IEntityDescriptorList)i.next());
      }
    }
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#addEntityDescriptorList(IEntityDescriptorList)
   */
  public void addEntityDescriptorList(IEntityDescriptorList descriptorList)
  {
    _descriptorListTable.put(descriptorList.getEntityType(), descriptorList);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet#getEntityDescriptorList(String)
   */
  public IEntityDescriptorList getEntityDescriptorList(String entityType)
  {
    return (IEntityDescriptorList)_descriptorListTable.get(entityType);
  }

  /**
   * Remove the EntityDescriptorList for a specified Entity Type from this set.
   * 
   * @param entityType The Entity Type.
   */  
  public void removeEntityDescriptorList(String entityType)
  {
    _descriptorListTable.remove(entityType);
  }


  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return MessageFormat.format(TOSTR_PATTERN, new Object[]{getEntityDescriptorLists()});
  }

}
