/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEventLogger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 18 2002    Neo Sok Lay         Created
 * Jul 18 2003    Neo Sok Lay         Add method: logEventStatus(IEvent,String)
 */
package com.gridnode.gtas.server.rdm.helpers;

import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This interface defines a logger that is interested in event processing status:
 * Start, End, Error.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public interface IEventLogger
{
  /**
   * Log the event error.
   * @param event The event that encountered error during processing.
   * @param t The error the occurred.
   */
  void logEventError(IEvent event, Throwable t);

  /**
   * Logs the start of the event processing.
   *
   * @param event The event that starts processing.
   */
  void logEventStart(IEvent event);

  /**
   * Logs the end of the event processing. This does not mean that no error
   * occurred during the processing.
   *
   * @param The event that ends processing.
   */
  void logEventEnd(IEvent event);

  /**
   * Logs a status message for the event.
   * 
   * @param event The event to log message for.
   * @param message The status message to log.
   */
  void logEventStatus(IEvent event, String message);
}