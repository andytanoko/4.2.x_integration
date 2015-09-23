/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractSyncResourceDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2002    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Add method:
 *                                    - startSynchronize(syncModel, mode)
 */
package com.gridnode.gtas.server.enterprise.sync;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;

/**
 * This abstract class provides a simple contract for subclasses to act as
 * a delegate for synchronizing a type of resource (may include the related
 * resources).
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public abstract class AbstractSyncResourceDelegate
{
  /**
   * Start the synchronization.
   *
   * @param syncModel The synchronizable object.
   * @throws SynchronizationFailException Fail to synchronize.
   */
  public abstract void startSynchronize(AbstractSyncModel syncModel)
    throws SynchronizationFailException;

  /**
   * Start the synchronization.
   *
   * @param syncModel The synchronizable object.
   * @param mode The mode of synchronization, as defined for the synchronizable object.
   * @throws SynchronizationFailException Fail to synchronize.
   */
  public abstract void startSynchronize(AbstractSyncModel syncModel, int mode)
    throws SynchronizationFailException;

}