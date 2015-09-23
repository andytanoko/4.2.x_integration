/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportEntityList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 19 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class wraps around a ArrayList so that it can be serialize using
 * castor.
 */

public class ExportEntityList extends ArrayList
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6281865573661145595L;

	public ExportEntityList()
  {
  }

  public ExportEntityList(Collection entities)
  {
    clear();
    addAll(entities);
  }

  public Collection getEntities()
  {
    return this;
  }

}