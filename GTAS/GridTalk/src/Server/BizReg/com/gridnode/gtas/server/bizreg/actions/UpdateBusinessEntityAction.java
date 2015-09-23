/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Dec 23 2003    Neo Sok Lay         Add handling for DomainIdentifier
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.events.bizreg.UpdateBusinessEntityEvent;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * This Action class handles the update of a BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class UpdateBusinessEntityAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5803816738432380344L;

	public static final String ACTION_NAME = "UpdateBusinessEntityAction";

  private BusinessEntity _beToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getBizRegManager().updateBusinessEntity(
      (BusinessEntity)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateBusinessEntityEvent updEvent = (UpdateBusinessEntityEvent)event;

    _beToUpdate.setDescription(updEvent.getUpdDescription());

    //whitepage
    WhitePage whitePage = _beToUpdate.getWhitePage();

    ActionHelper.copyEntityFields(updEvent.getUpdWhitePage(), whitePage);

    Collection domainIdentifiers = updEvent.getUpdDomainIdentifiers();
    if (domainIdentifiers != null)
    {
      ActionHelper.setDomainIdentifiers(_beToUpdate, domainIdentifiers);
    }
    
    return _beToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateBusinessEntityEvent updEvent = (UpdateBusinessEntityEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             BusinessEntity.ENTITY_NAME,
             updEvent.getBeUID(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateBusinessEntityEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateBusinessEntityEvent updEvent = (UpdateBusinessEntityEvent)event;
    _beToUpdate = verifyNonGtasPartnerBE(updEvent);
    
    // check the domain identifiers
    Collection domainIdentifiers = updEvent.getUpdDomainIdentifiers();
    if (domainIdentifiers != null)
    {
      ActionHelper.checkDomainIdentifiers(domainIdentifiers, updEvent);
    }
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getBizRegManager().findBusinessEntity(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBusinessEntityToMap((BusinessEntity)entity);
  }

  // **************************** Own Methods *****************************

  private BusinessEntity verifyNonGtasPartnerBE(UpdateBusinessEntityEvent event)
    throws Exception
  {
    ArrayList beToUpdate = new ArrayList();
    beToUpdate.add(event.getBeUID());

    ActionHelper.verifyNonGtasPartnerBusinessEntity(beToUpdate, "update");

    return ActionHelper.verifyBusinessEntity(event.getBeUID());
  }
}