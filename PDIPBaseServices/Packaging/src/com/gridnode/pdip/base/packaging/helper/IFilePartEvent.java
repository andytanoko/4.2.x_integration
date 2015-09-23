/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFilePartEvent.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Mar 20 2003    Goh Kan Mun                 Modified - Add in event to clean up.
 */
package com.gridnode.pdip.base.packaging.helper;

/**
 * This interface keeps the event Id of the split file operation.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IFilePartEvent
{
  public static final int _SEND_FILE_PART           = 7004;

  public static final int _SEND_FILE_PARTS_FINISHED = 7006;

  public static final int _ACK_FILE_PARTS           = 7010;

  public static final int _EXTEND_TIMEOUT           = 7012;

  public static final int _CANCEL_SEND_GRIDDOC      = 7016;
}