/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IIdentifierInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of a Link information object.
 * A Link information object models a named URI to content that may reside
 * outside the public registry. 
 * A typical implementation of a Link information object would be a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface ILinkInfo
{
  /**
   * The number of fields in a Link information object.
   */
  static final int NUM_FIELDS = 2;
  
  /**
   * Index to the URL field of the Link information object.
   */
  static final int FIELD_LINK_URL = 0;
  
  /**
   * Index to the Description field of the Link information object.
   */
  static final int FIELD_LINK_DESCRIPTION = 1;
}
