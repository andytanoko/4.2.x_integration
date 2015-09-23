/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionStatusBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.entities.ejb;

import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A ConnectionStatusBean provides persistency services for ConnectionStatus.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ConnectionStatusBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7333483808854900227L;

	public String getEntityName()
  {
    return ConnectionStatus.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
//    ConnectionStatus shared = (ConnectionStatus)entity;
//
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, ConnectionStatus.TO_ENTERPRISE_ID, filter.getEqualOperator(),
//      shared.getToEnterpriseID(), false);
//    filter.addSingleFilter(filter.getAndConnector(), ConnectionStatus.RESOURCE_TYPE,
//      filter.getEqualOperator(), shared.getResourceType(), false);
//    filter.addSingleFilter(filter.getAndConnector(), ConnectionStatus.RESOURCE_UID,
//      filter.getEqualOperator(), shared.getResourceUID(), false);
//
//    if (getDAO().getEntityCount(filter) > 0)
//      throw new DuplicateEntityException(
//        "Resource already shared ["+ shared.getEntityDescr() +"] !");

  }

  protected boolean isVersionCheckRequired()
  {
    return false;
  }



}