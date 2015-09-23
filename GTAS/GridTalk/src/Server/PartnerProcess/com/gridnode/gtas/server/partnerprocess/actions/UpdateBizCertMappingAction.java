/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateBizCertMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.Map;

import com.gridnode.gtas.events.partnerprocess.UpdateBizCertMappingEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UpdateBizCertMappingAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6669875161845634957L;

	private BizCertMapping _mapping;

  public static final String ACTION_NAME = "UpdateBizCertMappingAction";

  protected Class getExpectedEventClass()
  {
    return UpdateBizCertMappingEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBizCertMappingToMap((BizCertMapping)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateBizCertMappingEvent updEvent = (UpdateBizCertMappingEvent)event;
    _mapping = ActionHelper.getManager().findBizCertMappingByUID(updEvent.getUID());

    // get partner's enterprise id
    String enterpriseId = ActionHelper.getPartnerEnterpriseId(_mapping.getPartnerID());

    // verify partner cert belongs to the partner's enterprise or not belong to anyone.
    Certificate partnerCert = ActionHelper.verifyCertificate(
                                updEvent.getPartnerCertUID(),
                                enterpriseId,
                                true);

    // verify own cert belongs to this GridTalk.
    Certificate ownCert = ActionHelper.verifyCertificate(
                            updEvent.getOwnCertUID(),
                            getEnterpriseID(),
                            false);

    _mapping.setPartnerCert(partnerCert);
    _mapping.setOwnCert(ownCert);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    return _mapping;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateBizCertMapping((BizCertMapping)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findBizCertMappingByUID(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateBizCertMappingEvent updEvent = (UpdateBizCertMappingEvent)event;
    return new Object[]
           {
             BizCertMapping.ENTITY_NAME,
             String.valueOf(updEvent.getUID()),
             String.valueOf(updEvent.getPartnerCertUID()),
             String.valueOf(updEvent.getOwnCertUID()),
           };
  }

}