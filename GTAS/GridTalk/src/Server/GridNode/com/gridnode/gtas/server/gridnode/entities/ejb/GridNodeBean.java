/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.entities.ejb;

import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A GridNodeBean provides persistency services for GridNode entities.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GridNodeBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3640812769575220652L;

	public String getEntityName()
  {
    return GridNode.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
//    GridNode link = (GridNode)entity;
//
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, GridNode.FROM_TYPE, filter.getEqualOperator(),
//      link.getFromType(), false);
//    filter.addSingleFilter(filter.getAndConnector(), GridNode.TO_TYPE,
//      filter.getEqualOperator(), link.getToType(), false);
//    filter.addSingleFilter(filter.getAndConnector(), GridNode.FROM_UID,
//      filter.getEqualOperator(), link.getFromUID(), false);
//    filter.addSingleFilter(filter.getAndConnector(), GridNode.TO_UID,
//      filter.getEqualOperator(), link.getToUID(), false);
//
//    if (getDAO().getEntityCount(filter) > 0)
//      throw new DuplicateEntityException(
//        "Resource link ["+ link.getEntityDescr() + "] already exists!");
//
  }


}