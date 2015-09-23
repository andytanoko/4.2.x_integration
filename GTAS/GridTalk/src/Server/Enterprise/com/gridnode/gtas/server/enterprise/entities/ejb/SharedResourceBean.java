/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SharedResourceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.entities.ejb;

import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A SharedResourceBean provides persistency services for SharedResource.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SharedResourceBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8910857025553154487L;

	public String getEntityName()
  {
    return SharedResource.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
    SharedResource shared = (SharedResource)entity;

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SharedResource.TO_ENTERPRISE_ID, filter.getEqualOperator(),
      shared.getToEnterpriseID(), false);
    filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_TYPE,
      filter.getEqualOperator(), shared.getResourceType(), false);
    filter.addSingleFilter(filter.getAndConnector(), SharedResource.RESOURCE_UID,
      filter.getEqualOperator(), shared.getResourceUID(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "Resource already shared ["+ shared.getEntityDescr() +"] !");

  }


}