/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.entities.ejb;

import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A ActivationRecordBean provides persistency services for ActivationRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationRecordBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8235673310397809707L;

	public String getEntityName()
  {
    return ActivationRecord.ENTITY_NAME;
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
//    ActivationRecord shared = (ActivationRecord)entity;
//
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, ActivationRecord.TO_ENTERPRISE_ID, filter.getEqualOperator(),
//      shared.getToEnterpriseID(), false);
//    filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.RESOURCE_TYPE,
//      filter.getEqualOperator(), shared.getResourceType(), false);
//    filter.addSingleFilter(filter.getAndConnector(), ActivationRecord.RESOURCE_UID,
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