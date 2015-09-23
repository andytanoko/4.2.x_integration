/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         Simplify (thanks to new FilterPartnerCertListEvent(partnerid))
 * 2003-01-30     Andrew Hill         Diversions
 * 2003-02-10     Daniel D'Cotta      Did a quick fix for PartnerId
 * 2003-04-03     Andrew Hill         Did a quick fix on the quick fix so it doesnt molest the entity
 * 2003-04-04     Andrew Hill         Fix rendering of partnerId when editing existing bcm
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.renderers.*;

public class BizCertMappingRenderer extends AbstractRenderer implements IBFPROptionSource
{
  private IGTBizCertMappingManager _bcmManager;
  private IGTBizCertMappingEntity _bcm;
  private String _selectedPartnerId = null; //20030403AH

  private boolean _edit;
  private static Number[] _mainFields =
  {
    IGTBizCertMappingEntity.PARTNER_ID,
    IGTBizCertMappingEntity.PARTNER_CERT,
    IGTBizCertMappingEntity.OWN_CERT,
  };

  public BizCertMappingRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)getEntity();
      _bcm = bcm;
      BizCertMappingAForm form = (BizCertMappingAForm)getActionForm();

      IGTSession gtasSession = bcm.getSession();
      _bcmManager = (IGTBizCertMappingManager)
                    gtasSession.getManager(IGTManager.MANAGER_BIZ_CERT_MAPPING);

      renderCommonFormElements(bcm.getType(),_edit);

      // 20030210 DDJ: Quick Fix
      String partnerId = form.getPartnerId();
      //20030403AH - _bcm.setFieldValue(_bcm.PARTNER_ID, partnerId);
      _selectedPartnerId = partnerId; //20030403AH

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);

      if(_edit && (!bcm.isNewEntity()) )
      { //20030404AH - Use span not select for partner when updating existing mapping
        replaceValueNodeWithNewElement("span",IGTBizCertMappingEntity.PARTNER_ID,false,false);
        removeNode("partnerId_create",false);
      }

      bfpr = renderFields(bfpr,bcm,_mainFields);

      if(_edit)
      { //Render diversion link label
        renderLabel("partnerId_create","bizCertMapping.partnerId.create",false);
        renderLabel("partnerCert_create","bizCertMapping.partnerCert.create",false);
        renderLabel("ownCert_create","bizCertMapping.ownCert.create",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering bizCertMapping screen",t);
    }
  }

  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  {
    //20030114AH - Very first test flight of the OptionSource concept!...
    //20030115AH - Removed all the stuff to hang onto the lists of partners as we now
    //have a FilterPartnerCertEvent that takes partnerId rather than uid...

    IGTEntity entity = bfpr.getBoundEntity();
    Number fieldId = bfpr.getFieldId();

    if(_bcm != entity) return null; //Same instance? - no then not this sources job...
    if(IGTBizCertMappingEntity.PARTNER_ID.equals(fieldId))
    {
      /**
       * 20030404AH - It is interesting to note that the list of applicable partners wont include
       * the one assigned to this mapping (if we are editing an existing entity mode) as obviously it
       * already has a cert assigned! If you want to use a select box to display you must therefore
       * manually add the current partner to the list returned by this method.
       * We dont and we havent - I am modifying the renderer to use a span instead of a select.
       */
      Collection partners = _bcmManager.getApplicablePartnerList(IGTBizCertMappingManager.CONDITION_NO_CERT_MAPPING);
      return partners;
    }
    else if(IGTBizCertMappingEntity.PARTNER_CERT.equals(fieldId))
    {
      //String partnerId = (String)bfpr.getValue();
      // 20030210 DDJ: Quick Fix
      // 20030403AH: Fix quick fix to not mess with entity
      //(Method in manager returns empty list if supplied empty partnerId)
      return _bcmManager.getApplicablePartnerCertList(_selectedPartnerId); //20030115AH, 20030403AH
    }
    else if(IGTBizCertMappingEntity.OWN_CERT.equals(fieldId))
    {
      return _bcmManager.getApplicableOwnCertList();
    }
    else
    {
      return null; //Default option handling please....
    }
  }
}