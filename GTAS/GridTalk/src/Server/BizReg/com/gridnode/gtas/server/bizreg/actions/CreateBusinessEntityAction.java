/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Set PartnerCategory for Partner BE.
 * Dec 23 2003    Neo Sok Lay         Add handling for DomainIdentifier.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.CreateBusinessEntityEvent;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Map;

/**
 * This Action class handles the creation of a new BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class CreateBusinessEntityAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6186642464455558336L;
	public static final String ACTION_NAME = "CreateBusinessEntityAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateBusinessEntityEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getBizRegManager().findBusinessEntity(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateBusinessEntityEvent addEvent = (CreateBusinessEntityEvent)event;

    BusinessEntity newBe = new BusinessEntity();
    newBe.setBusEntId(addEvent.getBeID());
    newBe.setDescription(addEvent.getDescription());
    newBe.setPartner(addEvent.isPartnerBE());
    if (addEvent.isPartnerBE()) // can only create for partner of "Other" category.
      newBe.setPartnerCategory(IBusinessEntity.CATEGORY_OTHERS);
    else
      newBe.setEnterpriseId(getEnterpriseID()); //own enterpriseID
    newBe.setState(BusinessEntity.STATE_NORMAL);

    WhitePage whitePage = new WhitePage();
    ActionHelper.copyEntityFields(addEvent.getWhitePage(), whitePage);
    newBe.setWhitePage(whitePage);

    Collection domainIdentifiers = addEvent.getDomainIdentifiers();
    if (domainIdentifiers != null)
    {
      ActionHelper.setDomainIdentifiers(newBe, domainIdentifiers);
    }
    return newBe;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateBusinessEntityEvent addEvent = (CreateBusinessEntityEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             BusinessEntity.ENTITY_NAME,
             addEvent.getBeID(),
             String.valueOf(addEvent.isPartnerBE()),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return ActionHelper.getBizRegManager().createBusinessEntity(
                            (BusinessEntity)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBusinessEntityToMap((BusinessEntity)entity);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doSemanticValidation(com.gridnode.pdip.framework.rpf.event.IEvent)
   */
  protected void doSemanticValidation(IEvent event) throws Exception
  {
    CreateBusinessEntityEvent addEvent = (CreateBusinessEntityEvent)event;
    Collection domainIdentifiers = addEvent.getDomainIdentifiers();
    if (domainIdentifiers != null)
    {
      ActionHelper.checkDomainIdentifiers(domainIdentifiers, addEvent);
    }
  }

  // ****************** Own methods **********************************
  

  
}