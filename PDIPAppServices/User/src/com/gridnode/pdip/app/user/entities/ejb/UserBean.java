/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2002    NSL/OHL             Created
 * Apr 26 2002    Neo Sok Lay         Use UserDAOHelper as the EntityDAO
 *                                    implementation.
 *                                    Remove the Finder methods.
 */
package com.gridnode.pdip.app.user.entities.ejb;

import com.gridnode.pdip.app.user.helpers.UserDAOHelper;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A UserBean provides persistency services for User accounts.
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 1.0
 */
public class UserBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1773518339311598998L;

	public String getEntityName()
  {
    return UserAccount.class.getName();
  }

  protected IEntityDAO getDAO()
  {
    return UserDAOHelper.getInstance();
  }
}