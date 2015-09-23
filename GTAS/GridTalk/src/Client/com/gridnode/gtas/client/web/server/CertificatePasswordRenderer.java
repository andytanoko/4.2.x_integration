/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificatePasswordRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.server;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class CertificatePasswordRenderer extends AbstractRenderer
{
  private static final String[] _fieldnames = new String[]
  {
    "oldPassword",
    "securityPassword",
    "confirmPassword",
  };

  public CertificatePasswordRenderer( RenderingContext rContext )
  {
    super(rContext);
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      renderCommonFormElements("certificatePassword",true);

      ActionErrors actionErrors = rContext.getActionErrors();
      for(int i=0; i < _fieldnames.length; i++)
      {
        String field = _fieldnames[i];
        renderLabel(field + "_label","certificatePassword." + field, false);
        ActionError error = MessageUtils.getFirstError(actionErrors, field);
        if(error != null) renderLabel(field + "_error",error.getKey());
      }

      renderLabel("security_message","registrationInfo.security.message",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering registrationInfo screen",t);
    }
  }
}

