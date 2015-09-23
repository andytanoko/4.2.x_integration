/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of a Connection information object.
 * A Connection information object contains the information required to
 * publish or inquire information to/from a public registry.
 * Typical implementation of a Connection information object is a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IConnectionInfo
{
  /**
   * The number of fields in a Connection information object.
   */
  static final int NUM_FIELDS = 4;
  
  /**
   * Index to the QueryUrl field of the Connection information object.
   */
  static final int FIELD_QUERY_URL = 0;

  /**
   * Index to the PublishUrl field of the Connection information object.
   */
  static final int FIELD_PUBLISH_URL = 1;

  /**
   * Index to the User field of the Connection information object.
   */
  static final int FIELD_USER = 2;

  /**
   * Index to the Password field of the Connection information object.
   */
  static final int FIELD_PASSWORD = 3;
  
}
