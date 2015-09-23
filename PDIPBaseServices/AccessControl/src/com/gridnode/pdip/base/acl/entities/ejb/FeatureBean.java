/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FeatureBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 20 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.base.acl.entities.ejb;

import com.gridnode.pdip.base.acl.model.Feature;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * The class implements the Entity Bean for the Feature.
 *
 * @author Goh Kan Mun
 *
 * @since 2.0
 * @version 2.0
 */

public class FeatureBean extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5579223902442309179L;

	public FeatureBean()
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
    return Feature.ENTITY_NAME;
  }

}