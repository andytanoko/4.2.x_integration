/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchedGridNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

/**
 * This interface contains the field IDs for SearchedGridNode entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface ISearchedGridNode
{
  /**
   * Entity name for SearchGridNodeQuery.
   */
  public static final String ENTITY_NAME = "SearchedGridNode";

  /**
   * FieldID for GridNodeID. An Integer.
   */
  public static final Number GRIDNODE_ID   = new Integer(0);

  /**
   * FieldID for GridNodeName. A String.
   */
  public static final Number GRIDNODE_NAME = new Integer(1);

  /**
   * FieldID for State. A Short.
   */
  public static final Number STATE         = new Integer(2);

  // Values for STATE

  /**
   * Possible value for STATE. This indicates that the GridNode belongs to "This"
   * GridTalk.
   */
  static final short STATE_ME             = 0;

  /**
   * Possible value for STATE. This indicates that the GridNode is an activated
   * partner.
   */
  static final short STATE_ACTIVE         = 1;

  /**
   * Possible value for STATE. This indicates that the GridNode is not an
   * activated partner.
   */
  static final short STATE_INACTIVE       = 2;

  /**
   * Possible value for STATE. This indicates that the GridNode is now in the
   * progress of activation.
   */
  static final short STATE_PENDING        = 3;


}