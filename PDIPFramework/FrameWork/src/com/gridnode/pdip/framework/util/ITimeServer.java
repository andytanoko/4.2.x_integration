/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITimeServer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.util;

/**
 * Defines the interface for a TimeServer that provides the time service.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public interface ITimeServer
{
  /**
   * Get the offset of Utc time to local time in milliseconds. A positive offset
   * means the local time is in the negative timezone, and a negative offset
   * means the local time is in the positive timezone.
   * @return Offset from Utc time and Local time. This identifies how many
   * more (or less) milliseconds difference is the local time away from the
   * Utc time.
   */
  long getUtcOffset();
}