/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchGridNodeQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

/**
 * This interface contains the field IDs for SearchGridNodeQuery entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface ISearchGridNodeQuery
{
  /**
   * Entity name for SearchGridNodeQuery.
   */
  public static final String ENTITY_NAME = "SearchGridNodeQuery";

  /**
   * FieldID for SearchID. A Long.
   */
  public static final Number SEARCH_ID   = new Integer(0);

  /**
   * FieldID for DTSubmitted. A Timestamp.
   */
  public static final Number DT_SUBMITTED= new Integer(1);

  /**
   * FieldID for DTResponded. A Timestamp.
   */
  public static final Number DT_RESPONDED= new Integer(2);

  /**
   * FieldID for SearchCriteria. A SearchGridNodeCriteria.
   */
  public static final Number CRITERIA    = new Integer(3);

  /**
   * FieldID for SearchResults. A Collection of SearchedGridNode.
   */
  public static final Number RESULTS     = new Integer(4);

}