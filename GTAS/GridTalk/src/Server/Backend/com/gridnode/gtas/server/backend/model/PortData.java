/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 12 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.backend.model;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import java.util.List;

public class PortData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1406901966221815707L;
	public static final String NAME = Port.ENTITY_NAME;

  public PortData(Port port)
  {
    super(port);
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(Port.ENTITY_NAME);
  }
}