/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SystemDetails.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 26 Feb 2003    Neo Sok Lay         Created
 * 03 Mar 2006    Neo Sok Lay         Use generics
 */

package com.gridnode.pdip.app.alert.providers;

import com.gridnode.pdip.framework.util.TimeUtil;

import java.util.List;
import java.util.ArrayList;

/**
 * This Data provider provides the general System data like current system time.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I8
 */
public class SystemDetails extends AbstractDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3723977849151344694L;

	public static final String NAME = "System";

  public static final String FIELD_CURR_DATETIME    = "CURRENT_DATETIME";

  public SystemDetails()
  {
    set(FIELD_CURR_DATETIME, TimeUtil.getCurrentLocalTimestamp());
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List<String> getFieldNameList()
  {
    ArrayList<String> list = new ArrayList<String>();
    list.add(FIELD_CURR_DATETIME);
    return list;
  }
}