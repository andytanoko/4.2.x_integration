/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.ctrl.IGTPartnerGroupEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class PartnerGroupRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected final static Number[] fields = {  IGTPartnerGroupEntity.DESCRIPTION,
                                              IGTPartnerGroupEntity.NAME,
                                              IGTPartnerGroupEntity.PARTNER_TYPE, };

  public PartnerGroupRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTPartnerGroupEntity partnerGroup = (IGTPartnerGroupEntity)getEntity();
      renderCommonFormElements(partnerGroup.getType(),_edit);
      if(_edit)
      {
        if(partnerGroup.getFieldMetaInfo(IGTPartnerGroupEntity.PARTNER_TYPE).isEditable(partnerGroup.isNewEntity()))
        { // Render label for the diversion link
          renderLabel("partnerType_create","partnerGroup.partnerType.create",false);
        }
        else
        { // Remove the diversion link if partnerType not editable
          removeNode("partnerType_create",false);
        }
      }
      renderFields(null, partnerGroup, fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering partnerGroup screen",t);
    }
  }
}