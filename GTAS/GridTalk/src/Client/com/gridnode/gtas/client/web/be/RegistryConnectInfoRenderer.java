/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.IGTRegistryConnectInfoEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class RegistryConnectInfoRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected final static Number[] _fields = { IGTRegistryConnectInfoEntity.NAME,
                                              IGTRegistryConnectInfoEntity.QUERY_URL,
                                              IGTRegistryConnectInfoEntity.PUBLISH_URL,
                                              //IGTRegistryConnectInfoEntity.PUBLISH_USER,
                                              //IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD,
                                            };

  public RegistryConnectInfoRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTRegistryConnectInfoEntity registryConnectInfo = (IGTRegistryConnectInfoEntity)getEntity();
      renderCommonFormElements(registryConnectInfo.getType(), _edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, registryConnectInfo, _fields);

      RenderingContext rContext = getRenderingContext();
      ActionErrors errors = rContext.getActionErrors();
      RegistryConnectInfoAForm form = (RegistryConnectInfoAForm)getActionForm();
      boolean isMandatory = StaticUtils.stringNotEmpty(form.getPublishUrl());
     
      bfpr.reset();
      bfpr.setBindings(registryConnectInfo, IGTRegistryConnectInfoEntity.PUBLISH_USER, errors);
      bfpr.setMandatory(isMandatory);
      bfpr.render(_target);
       
      bfpr.reset();
      bfpr.setBindings(registryConnectInfo, IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD, errors);
      bfpr.setMandatory(isMandatory);
      bfpr.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering registryConnectInfo screen", t);
    }
  }
}