/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.model;

import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.app.alert.providers.EntityDetails;

import java.util.List;

public class LicenseData extends EntityDetails
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3766786448656494103L;
	public static final String NAME = License.ENTITY_NAME;

  public LicenseData(License license)
  {
    super(license);
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(License.ENTITY_NAME);
  }
}