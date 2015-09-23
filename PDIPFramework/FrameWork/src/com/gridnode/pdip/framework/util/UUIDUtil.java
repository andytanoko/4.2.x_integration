/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UUIDGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 20, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.util;

import java.util.UUID;

/**
 * This class is used to obtain the Universally Unique Identifier (UUID).
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class UUIDUtil
{
  /**
   * 
   * @return a RandomUUID(type 4) in string representation.
   */
  public static String getRandomUUIDInStr()
  {
    return UUID.randomUUID().toString();
  }
  
  /**
   * 
   * @return a RandomUUID(type 4)
   */
  public static UUID getRandomUUID()
  {
    return UUID.randomUUID();
  }
  
  /**
   * 
   * @param name a byte array to be used to construct a UUID
   * @return a NameBasedUUID(type 3) in string representation
   */
  public static String getNameBasedUUIDInStr(byte[] name)
  {
    return UUID.nameUUIDFromBytes(name).toString();
  }
  
  /**
   * 
   * @param name a byte array to be used to construct a UUID
   * @return a NameBasedUUID(type 3)
   */
  public static UUID getNameBasedUUID(byte[] name)
  {
   return UUID.nameUUIDFromBytes(name); 
  }
}
