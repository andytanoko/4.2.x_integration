/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpathMappingBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A XpathMappingBean provides persistency services for XpathMapping.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class XpathMappingBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1622658812072512112L;

	public String getEntityName()
  {
    return XpathMapping.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity xpathMapping)
    throws Exception
  {
    String rootElement =
      xpathMapping.getFieldValue(XpathMapping.ROOT_ELEMENT).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           XpathMapping.ROOT_ELEMENT,
                           filter.getEqualOperator(),
                           rootElement,
                           false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "XpathMapping : root element "+rootElement+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}