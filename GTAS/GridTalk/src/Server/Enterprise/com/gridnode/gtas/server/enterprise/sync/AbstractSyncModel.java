/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractSyncModel.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add methods: 
 *                                    - sync(int mode).
 *                                    - isSet(modifiers,mode)
 *                                    Add default sync modes.
 */
package com.gridnode.gtas.server.enterprise.sync;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.DataObject;

/**
 * This data object provides a simple model for synchronizable objects.
 * The synchronization process is mode-aware to enable 
 * synchronization to be performed with different options at different
 * times. The default modes available are IS_PARTNER, GT_PARTNER, and
 * CAN_DELETE. Sub-classes can provide more modes starting from 0x10 (dec. 16).
 * Combination of modes can be specified in this manner, e.g:
 * <pre>
 *   MODE_IS_PARTNER | MODE_GT_PARTNER | MODE_CAN_DELETE
 * </pre>
 * <p>which means to synchronize the objects as Deletable GridTalkPartner
 * objects. 
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public abstract class AbstractSyncModel extends DataObject
{
  /**
   * Turn on to synchronize the object as belonging to a Partner.
   */
  public static final int MODE_IS_PARTNER = 0x1;
  
  /**
   * Turn on to synchronize the object as belong to a GridTalk Partner.
   */
  public static final int MODE_GT_PARTNER = 0x2;
  
  /**
   * Turn on to synchronize the object as Deletable by user.
   */
  public static final int MODE_CAN_DELETE = 0x4;

  public AbstractSyncModel()
  {
  }

  /**
   * Synchronize the object.
   */
  public abstract void sync() throws Throwable;

  /**
   * Synchronize the object using the specified modifiers as
   * synchronization options.
   * 
   * @param mode The modifiers. The details of the modes supported is 
   * required to be specified in the concrete sync models.
   * @throws Throwable Error during the synchronization.
   * @see #MODE_IS_PARTNER
   * @see #MODE_GT_PARTNER
   * @see #MODE_CAN_DELETE
   */
  public abstract void sync(int mode) throws Throwable;
 
  /**
   * Copy fields from one entity to another.
   *
   * @param from The entity from which to copy fields from.
   * @param to The entity to copy fields to.
   * @param fieldsToCopy The field IDs of the fields to copy.
   */
  protected void copyFields(IEntity from, IEntity to, Number[] fieldsToCopy)
  {
    for (int i=0; i<fieldsToCopy.length; i++)
    {
      to.setFieldValue(fieldsToCopy[i], from.getFieldValue(fieldsToCopy[i]));
    }
  }

  /**
   * Checks if a specific mode is specified in the specified modifiers.
   * 
   * @param modifiers The modifiers to check with.
   * @param mode The specific mode to check if set.
   * @return <b>true</b> if <code>mode</code> is turned on in <code>modifiers</code>.
   */
  protected boolean isSet(int modifiers, int mode)
  {
    return (modifiers & mode) == mode;
  }
  

}