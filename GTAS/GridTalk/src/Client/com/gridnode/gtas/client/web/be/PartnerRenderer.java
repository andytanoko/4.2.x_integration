/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 * 2002-08-01     Andrew Hill         Refactored to use new metaInfo aware BFPR
 * 2003-08-04     Andrew Hill         Remove channel diversion rendering and call non-deprecated methods
 */
package com.gridnode.gtas.client.web.be;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityManager;
import com.gridnode.gtas.client.ctrl.IGTChannelInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerEntity;
import com.gridnode.gtas.client.ctrl.IGTPartnerGroupEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class PartnerRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;
  private Collection _channels = null;
  protected static final Number[] _fields = {  IGTPartnerEntity.CREATE_BY, //20030804AH - Renamed _fields to comply with our conventions
                                              IGTPartnerEntity.CREATE_TIME,
                                              IGTPartnerEntity.DESCRIPTION,
                                              IGTPartnerEntity.NAME,
                                              IGTPartnerEntity.PARTNER_GROUP,
                                              IGTPartnerEntity.PARTNER_ID,
                                              IGTPartnerEntity.PARTNER_TYPE,
                                              IGTPartnerEntity.STATE,
                                              IGTPartnerEntity.BE,
                                              IGTPartnerEntity.CHANNEL, };

  public PartnerRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTPartnerEntity partner = (IGTPartnerEntity)getEntity();
      PartnerAForm form = (PartnerAForm)getActionForm();

      renderCommonFormElements(partner.getType(),_edit);
      if(_edit)
      { // Render diversion labels
        renderLabel("partnerType_create","partner.partnerType.create",false);
        renderLabel("partnerGroup_create","partner.partnerGroup.create",false);
        renderLabel("be_create","partner.be.create",false);
        //20030804AH - co: renderLabel("channel_create","partner.channel.create",false);
      }

      Long beUid = StaticUtils.longValue( form.getBe() );
      if(beUid != null)
      {
        IGTBusinessEntityManager beMgr = (IGTBusinessEntityManager)partner.getSession().getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
        IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)beMgr.getByUid(beUid.longValue()); //20030804AH - Call non-deprecated method
        _channels = (Collection)be.getFieldValue(IGTBusinessEntityEntity.CHANNELS);
      }
      else
      {
        removeNode("channel_details",false);
        form.setChannel("null");
      }

      //20030804AH - co: BindingFieldPropertyRenderer bfpr = renderFields(fields,this);
      renderFields(null, partner, _fields, this, form, null); //20030804AH - Call non-deprecated method
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering partner screen",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTPartnerGroupEntity)
    { // If we are filtering a partnerGroup check its partnerType matches that on the form
      return ((IGTPartnerGroupEntity)object).getFieldString(IGTPartnerGroupEntity.PARTNER_TYPE).equals(((PartnerAForm)getActionForm()).getPartnerType());
    }
    if(object instanceof IGTBusinessEntityEntity)
    {
      return ((Boolean)((IGTBusinessEntityEntity)object).getFieldValue(IGTBusinessEntityEntity.IS_PARTNER)).booleanValue();
    }
    if(object instanceof IGTChannelInfoEntity)
    {
      if(_channels == null) return false;
      Long channelUid = ((IGTChannelInfoEntity)object).getUidLong();
      return _channels.contains(channelUid);
    }
    return true;
  }
}

