/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-07-31     Andrew Hill         Renamed & refactored
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTPartnerTypeEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class PartnerTypeRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = {  IGTPartnerTypeEntity.PARTNER_TYPE,
                                            IGTPartnerTypeEntity.DESCRIPTION, };

  public PartnerTypeRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
	  try 
	  {
		  renderCommonFormElements(IGTEntity.ENTITY_PARTNER_TYPE, _edit);
		  renderFields(null, getEntity(), fields);
	  } 
	  catch (Throwable t) 
	  {
		  throw new RenderingException("Error rendering partnerType screen",t);  
	  }
  }
}