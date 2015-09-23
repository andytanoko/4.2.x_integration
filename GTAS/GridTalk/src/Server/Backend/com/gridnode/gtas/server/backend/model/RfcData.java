/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.backend.model;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import java.util.List;

public class RfcData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7163832200605388288L;
	public static final String NAME = Rfc.ENTITY_NAME;

  public RfcData(Rfc rfc)
  {
    super(rfc);
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(Rfc.ENTITY_NAME);
  }
}