/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXpathMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in XpathMapping entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IXpathMapping
{
  /**
   * Name of XpathMapping entity.
   */
  public static final String  ENTITY_NAME = "XpathMapping";

  /**
   * FieldId for the UID for a XpathMapping entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-XpathMapping-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION     = new Integer(2); //Double

  /**
   * FieldId for the RootElement. A String.
   */
  public static final Number ROOT_ELEMENT  = new Integer(3); //String(120)

  /**
   * FieldId for the UID of the Xpath file. A Long.
   */
  public static final Number XPATH_UID = new Integer(4); //Long

}