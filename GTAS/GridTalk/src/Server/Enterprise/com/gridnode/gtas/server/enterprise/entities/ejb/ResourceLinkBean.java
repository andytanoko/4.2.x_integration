/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLinkBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.entities.ejb;

import com.gridnode.gtas.server.enterprise.model.ResourceLink;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A ResourceLinkBean provides persistency services for ResourceHierarchies.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class ResourceLinkBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8348612790662638778L;

	public String getEntityName()
  {
    return ResourceLink.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
    ResourceLink link = (ResourceLink)entity;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ResourceLink.FROM_TYPE, filter.getEqualOperator(),
      link.getFromType(), false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_TYPE,
      filter.getEqualOperator(), link.getToType(), false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.FROM_UID,
      filter.getEqualOperator(), link.getFromUID(), false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.TO_UID,
      filter.getEqualOperator(), link.getToUID(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "Resource link ["+ link.getEntityDescr() + "] already exists!");

  }


}