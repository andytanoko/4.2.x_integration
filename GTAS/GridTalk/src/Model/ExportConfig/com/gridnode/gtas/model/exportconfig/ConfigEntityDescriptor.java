/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigEntityDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2003    Koh Han Sing        Created
 * Jul 09 2003    Neo Sok Lay         Refactor: Extend from EntityDescriptor.
 */
package com.gridnode.gtas.model.exportconfig;

import com.gridnode.pdip.framework.rpf.model.EntityDescriptor;

/**
 * This class contain information on an instance of an enitity. It will contain
 * information to display on the UI for the user to select.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.1 I1
 */
public class ConfigEntityDescriptor extends EntityDescriptor
{ 
  //private boolean   _isSelected = false;

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2100304995838120096L;

	public ConfigEntityDescriptor()
  {
  }

  public Long getUid()
  {
    return (Long) getKey();
  }

  //  public boolean isSelected()
  //  {
  //    return _isSelected;
  //  }

  public void setUid(Long uid)
  {
    setKey(uid);
  }

  //  public void setIsSelected(boolean isSelected)
  //  {
  //    _isSelected = isSelected;
  //  }
}