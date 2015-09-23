/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A TriggerBean provides persistency services for Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class TriggerBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7248019089177159545L;

	public String getEntityName()
  {
    return Trigger.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity trigger)
    throws Exception
  {
    Integer triggerLevel = (Integer)trigger.getFieldValue(Trigger.TRIGGER_LEVEL);
    Integer triggerType = (Integer)trigger.getFieldValue(Trigger.TRIGGER_TYPE);
    String docType = null;
    String partnerType = null;
    String partnerGroup = null;
    String partnerId = null;

    if (trigger.getFieldValue(Trigger.DOC_TYPE) != null)
    {
      docType = trigger.getFieldValue(Trigger.DOC_TYPE).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_TYPE) != null)
    {
      partnerType = trigger.getFieldValue(Trigger.PARTNER_TYPE).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_GROUP) != null)
    {
      partnerGroup = trigger.getFieldValue(Trigger.PARTNER_GROUP).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_ID) != null)
    {
      partnerId = trigger.getFieldValue(Trigger.PARTNER_ID).toString();
    }

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Trigger.TRIGGER_LEVEL,
      filter.getEqualOperator(),
      triggerLevel,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.TRIGGER_TYPE,
      filter.getEqualOperator(),
      triggerType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.DOC_TYPE,
      filter.getEqualOperator(),
      docType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_TYPE,
      filter.getEqualOperator(),
      partnerType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_GROUP,
      filter.getEqualOperator(),
      partnerGroup,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_ID,
      filter.getEqualOperator(),
      partnerId,
      false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "Trigger : "+triggerLevel+"-"+docType+"-"+partnerType+"-"+
                   partnerGroup+"-"+partnerId+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}