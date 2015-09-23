/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateBizCertMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.Map;

import com.gridnode.gtas.events.partnerprocess.CreateBizCertMappingEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class CreateBizCertMappingAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8316060945428036260L;

	public static final String ACTION_NAME = "CreateBizCertMappingAction";

  private Certificate _partnerCert = null;
  private Certificate _ownCert     = null;

  protected Class getExpectedEventClass()
  {
    return CreateBizCertMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findBizCertMappingByUID(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateBizCertMappingEvent createEvent = (CreateBizCertMappingEvent)event;

    BizCertMapping newBizCertMapping = new BizCertMapping();
    newBizCertMapping.setPartnerID(createEvent.getPartnerID());
    newBizCertMapping.setPartnerCert(_partnerCert);
    newBizCertMapping.setOwnCert(_ownCert);

    return newBizCertMapping;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateBizCertMappingEvent createEvent = (CreateBizCertMappingEvent)event;
    return new Object[]
           {
             BizCertMapping.ENTITY_NAME,
             createEvent.getPartnerID(),
             String.valueOf(createEvent.getPartnerCertUID()),
             String.valueOf(createEvent.getOwnCertUID()),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createBizCertMapping((BizCertMapping)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBizCertMappingToMap((BizCertMapping)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    CreateBizCertMappingEvent createEvent = (CreateBizCertMappingEvent)event;

    // get partner's enterprise id
    String enterpriseId = ActionHelper.getPartnerEnterpriseId(createEvent.getPartnerID());

    // verify partner cert belongs to the partner's enterprise or not belong to anyone.
    _partnerCert = ActionHelper.verifyCertificate(
                     createEvent.getPartnerCertUID(),
                     enterpriseId,
                     true);

    // verify own cert belongs to this GridTalk.
    _ownCert     = ActionHelper.verifyCertificate(
                     createEvent.getOwnCertUID(),
                     getEnterpriseID(),
                     false);
  }


}