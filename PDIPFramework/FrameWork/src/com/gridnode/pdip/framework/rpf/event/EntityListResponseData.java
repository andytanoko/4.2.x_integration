/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListResponseData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.event;

import java.util.Collection;

/**
 * This is a data wrapper for returning list of entities to the client in
 * an EventResponse.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class EntityListResponseData
  implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3509305057142981859L;
	protected Collection _entityList;
  protected int        _startRow    = 0;
  protected int        _totalRowsRemaining  = 0;
  protected String     _listID;

  /**
   * Construct an EntityListResponseData with a list of all available entities
   * to be returned.
   *
   * @param entityList List of entities.
   *
   * @since 2.0
   */
  public EntityListResponseData(Collection entityList)
  {
    _entityList = entityList;
  }

  /**
   * Construct an EntityListResponseData with a list of entities, indicating
   * the starting row in the result set of the first entity in the entityList,
   * and how many more rows left in the result set.
   *
   * @param entityList List of entities.
   * @param startRow Starting row index of first entity of the entityList in the
   * result set.
   * @param rowsRemaining Number of remaining rows in the result set, after the
   * entityList.
   * @param listID A Unique ID for the list. This is to be used for paging
   * purpose.
   *
   * @since 2.0
   */
  public EntityListResponseData(
    Collection entityList, int startRow, int rowsRemaining, String listID)
  {
    _entityList = entityList;
    _startRow = startRow;
    _totalRowsRemaining = rowsRemaining;
    _listID = listID;
  }

  public Collection getEntityList()
  {
    return _entityList;
  }

  public int getStartRow()
  {
    return _startRow;
  }

  public int getRowsRemaining()
  {
    return _totalRowsRemaining;
  }

  public String getListID()
  {
    return _listID;
  }
}