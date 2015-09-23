/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UidGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 29, 2006   i00107              Created
 */

package com.gridnode.util;

import java.util.UUID;

/**
 * @author i00107
 * Generator for unique identifiers based on UUID.
 */
public class UidGenerator
{
  public static String createUid()
  {
    return UUID.randomUUID().toString();
  }
}
