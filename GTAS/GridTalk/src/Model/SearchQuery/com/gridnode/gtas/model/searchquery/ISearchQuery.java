/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.searchquery;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in SearchQuery entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public interface ISearchQuery
{
  /**
   * Name of SearchQuery entity.
   */
  public static final String  ENTITY_NAME = "SearchQuery";

  /**
   * FieldId for the UID for a SearchQuery entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-SearchQuery-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for the Name of the SearchQuery. A String.
   */
  public static final Number NAME        = new Integer(3); //String(30)

  /**
   * FieldId for the Description of the SearchQuery. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the User who created this SearchQuery. A String.
   */
  public static final Number CREATED_BY  = new Integer(5); //String(80)

  /**
   * FieldId for the Collection of Conditions for this SearchQuery. A Collection.
   */
  public static final Number CONDITIONS  = new Integer(6); //ArrayList

  public static final Number IS_PUBLIC = new Integer(7); //Boolean
}