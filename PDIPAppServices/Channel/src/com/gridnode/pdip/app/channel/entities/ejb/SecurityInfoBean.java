/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfoBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created.
 * Mar 05 2004    Neo Sok Lay             Implement Check Duplicate
 */
package com.gridnode.pdip.app.channel.entities.ejb;

import com.gridnode.pdip.app.channel.helpers.DAOHelper;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * The class implements the Entity Bean for the PackagingInfo.
 *
 * @author Jagadeesh.
 *
 * @since 2.0
 * @version GT 2.2.10
 */
public class SecurityInfoBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4580828219264350830L;

	public SecurityInfoBean()
  {

  }

  /**
   * To retrieve the entity name of this bean.
   *
   * @return the entity name.
   *
   */
  public String getEntityName()
  {
    return SecurityInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#checkDuplicate(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  protected void checkDuplicate(IEntity entity) throws Exception
  {
    DAOHelper.getInstance().checkDuplicate((SecurityInfo)entity, false);
  }

}

