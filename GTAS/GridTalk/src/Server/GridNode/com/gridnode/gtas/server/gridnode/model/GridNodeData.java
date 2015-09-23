/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.gridnode.model;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import java.util.List;

public class GridNodeData extends EntityDetails
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4009398981032828876L;
	public static final String NAME = GridNode.ENTITY_NAME;

  public GridNodeData(GridNode gridnode)
  {
    super(gridnode);
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(GridNode.ENTITY_NAME);
  }
}