/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WebServiceRenderer.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.ctrl.IGTWebServiceEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class WebServiceRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number[] _fields = //20030808AH - Rename _fields in line with our code convention
  {
    IGTWebServiceEntity.SERVICE_NAME,
    IGTWebServiceEntity.SEVICE_GROUP,
    IGTWebServiceEntity.WSDL_URL,
    IGTWebServiceEntity.END_POINT
  };
  
  
  public WebServiceRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTWebServiceEntity webService = (IGTWebServiceEntity)getEntity();
      //WebServiceAForm form = (WebServiceAForm)getActionForm();
      //RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(webService.getType(), _edit);
      renderFields(null, webService, _fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering WebService screen",t);
    }
  }
}