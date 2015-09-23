/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryObjectMappingBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.entities.ejb;

import com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * Bean for managing the RegistryObjectMapping entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryObjectMappingBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6917923722230309660L;

	/**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#getEntityName()
   */
  public String getEntityName()
  {
    return RegistryObjectMapping.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.ejb.AbstractEntityBean#isVersionCheckRequired()
   */
  protected boolean isVersionCheckRequired()
  {
    return false;
  }

}
