/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPersistable.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 29, 2006   i00107             Created
 */

package com.gridnode.util.db;

/**
 * @author i00107
 * This interface defines the standard attributes of a persistable object.
 */
public interface IPersistable
{
  /**
   * @return The unique identifier of the object. This value will be assigned
   * for unsaved instances.
   * @hibernate.id generator-class = "assigned" column = "\"uid\""
   */
  String getUID();
  void setUID(String uid);
  
  /**
   * @return The version of the object. This value will be <b>null</b> for
   * unsaved instances.
   * @hibernate.version column = "\"version\"" unsaved-value = "null"
   */
  Integer getVersion();
  void setVersion(Integer version);
  
}
