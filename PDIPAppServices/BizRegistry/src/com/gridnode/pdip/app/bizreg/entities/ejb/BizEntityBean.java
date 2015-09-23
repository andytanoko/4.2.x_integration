/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizEntityBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import com.gridnode.pdip.app.bizreg.helpers.BizEntityDAOHelper;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A BizEntityBean provides persistency services for BusinessEntities.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class BizEntityBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3198135313985263196L;

	public String getEntityName()
  {
    return BusinessEntity.ENTITY_NAME;
  }

  protected IEntityDAO getDAO()
  {
    return BizEntityDAOHelper.getInstance();
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
    BizEntityDAOHelper.getInstance().checkDuplicate(
      (BusinessEntity)entity, false); // don't check key
  }


}