/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * Bean for managing the RegistryConnectInfo entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryConnectInfoBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2424229606952900143L;

	/**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#getEntityName()
   */
  public String getEntityName()
  {
    return RegistryConnectInfo.ENTITY_NAME;
  }
  
  /**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#checkDuplicate(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  protected void checkDuplicate(IEntity entity) throws Exception
  {
    String name = ((RegistryConnectInfo)entity).getName();
    
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryConnectInfo.NAME, filter.getEqualOperator(),
      name, false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException("RegistryConnectInfo of Name "+name
        + " already exists");
  }

}
