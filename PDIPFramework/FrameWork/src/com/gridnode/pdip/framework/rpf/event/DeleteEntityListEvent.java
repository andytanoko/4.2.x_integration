/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteEntityListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 14 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         Add constructor:
 *                                    - DeleteEntityListEvent(Collection,Class)
 */
package com.gridnode.pdip.framework.rpf.event;

import java.util.Arrays;
import java.util.Collection;

/**
 * This is a base class for an Event to delete a list of entities.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DeleteEntityListEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3808255168120579763L;
	public static final String KEYS = "Keys";
  
  public DeleteEntityListEvent(Collection uids)
    throws EventException
  {
    this(uids, Long.class);
  }
  
  public DeleteEntityListEvent(Long[] uids)
    throws EventException
  {
    this(Arrays.asList(uids));
  }
  
  /**
   * Constructs a DeleteEntityListEvent for keys of certain type.
   * 
   * @param keys Collection of keys.
   * @param keyType The Class type of the keys.
   * @throws EventException Invalid type of keys in the collection.
   */
  protected DeleteEntityListEvent(Collection keys, Class keyType)
    throws EventException
  {
    checkSetCollection(KEYS, keys, keyType);
  }

  public Collection getUids()
  {
    return getKeys();
  }

  public Collection getKeys()
  {
    return (Collection)getEventData(KEYS);
  }
  
  public String getEntityType()
  {
    return null;
  }
  
  public Number getKeyId()
  {
    return null;
  }
}
