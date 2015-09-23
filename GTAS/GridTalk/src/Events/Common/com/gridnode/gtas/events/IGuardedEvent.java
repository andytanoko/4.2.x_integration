/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGuardedEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events;

import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This interface defines an event which be translated to an action that is
 * to protected against access to by a client.
 * <P>
 * The guarded event simply indicates the feature and action that is
 * being protected.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public interface IGuardedEvent extends IEvent
{
  /**
   * Get the name of the Feature that is being protected.
   */
  String getGuardedFeature();

  /**
   * Get the name of the Action that is being protected.
   */
  String getGuardedAction();
}