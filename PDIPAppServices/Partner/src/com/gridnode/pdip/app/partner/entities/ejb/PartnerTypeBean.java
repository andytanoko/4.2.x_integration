/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeBean.java
 *
 *********************************************************************************************
 * Date           Author              Changes
 *********************************************************************************************
 * Jun 05 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.entities.ejb;


import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.app.partner.helpers.Logger;

// import framework related package
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;



/**
 * This Entity Bean represents the PartnerType. It provides the persistency
 * services for PartnerType entity.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public class PartnerTypeBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7227103523666080030L;

	public String getEntityName()
  {
    return PartnerType.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity entity)
    throws Exception
  {
    Logger.log("[PartnerTypeBean.checkDuplicate] Enter ");
    String name = (String)entity.getFieldValue(PartnerType.NAME);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerType.NAME, filter.getEqualOperator(), name, false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new ApplicationException("Partner Type already used: " + name);
  }
}