/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupBean.java
 *
 *********************************************************************************************
 * Date           Author              Changes
 *********************************************************************************************
 * Jun 05 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.entities.ejb;


import com.gridnode.pdip.app.partner.model.PartnerGroup;

// import framework related package
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This Entity Bean represents the PartnerGroup. It provides the persistency
 * services for PartnerGroup entity.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public class PartnerGroupBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6796198213874476281L;

	public String getEntityName()
  {
    return PartnerGroup.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity entity)
    throws Exception
  {
    String name = (String)entity.getFieldValue(PartnerGroup.NAME);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerGroup.NAME, filter.getEqualOperator(), name, false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new ApplicationException("Partner Group already used: " + name);
  }
}