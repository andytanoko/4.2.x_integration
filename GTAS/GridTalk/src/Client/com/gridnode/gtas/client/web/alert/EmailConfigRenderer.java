/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailConfigRenderer.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;

public class EmailConfigRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = new Number[]
  {
    IGTEmailConfigEntity.SMTP_SERVER_HOST,
    IGTEmailConfigEntity.SMTP_SERVER_PORT, 
    IGTEmailConfigEntity.AUTH_USER, 
    IGTEmailConfigEntity.AUTH_PASSWORD, 
    IGTEmailConfigEntity.RETRY_INTERVAL, 
    IGTEmailConfigEntity.MAX_RETRIES, 
    IGTEmailConfigEntity.SAVE_FAILED_EMAILS, 
  };

  public EmailConfigRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_EMAIL_CONFIG, _edit);
      renderFields(null, getEntity(), fields);
      
      // Hack for custom date (as BFPR only renders standard dates)
      //EmailConfigAForm form = (EmailConfigAForm)getActionForm();
      //renderElementValue("startTime_value", form.getStartTime());
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering emailConfig screen",t);
    }
  }
}