/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTPublishBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTRegistryConnectInfoEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class PublishBusinessEntityRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;

  protected final static Number[] _fields = { IGTPublishBusinessEntityEntity.BE,
                                              IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO, };

  public PublishBusinessEntityRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTPublishBusinessEntityEntity publishBusinessEntity = (IGTPublishBusinessEntityEntity)getEntity();
      PublishBusinessEntityAForm form = (PublishBusinessEntityAForm)getActionForm();
      renderCommonFormElements(publishBusinessEntity.getType(), _edit);
      renderFields(null, publishBusinessEntity, _fields, this, form, null);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering publishBusinessEntity screen", t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTBusinessEntityEntity)
    { 
      Boolean isPartner = (Boolean)((IGTBusinessEntityEntity)object).getFieldValue(IGTBusinessEntityEntity.IS_PARTNER);
      return !isPartner.booleanValue();
    }
    else if(object instanceof IGTRegistryConnectInfoEntity)
    { 
      String publishUrl = ((IGTRegistryConnectInfoEntity)object).getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_URL);
      return StaticUtils.stringNotEmpty(publishUrl);
    }
    return true;
  }
}