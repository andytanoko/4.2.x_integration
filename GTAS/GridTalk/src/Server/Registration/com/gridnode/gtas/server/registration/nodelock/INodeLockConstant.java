/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INodeLockConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.nodelock;

public interface INodeLockConstant
{
  // Key for encryption of nodelock information
  public static final String KEY      = "4902505182373";

  // License type GridNode Lab which does not require nodelock
  public static final String GNL      = "GNL";

  // Category used by the MDB which will check the license
  public static final String CATEGORY = "CheckLicense";

  // Interval to check license in seconds
  public static final long INTERVAL = 86400;

  public static final String EXPIRED_ALERT_NAME = "LICENSE_EXPIRED";

  public static final String EXPIRING_ALERT_NAME = "LICENSE_EXPIRING";

}