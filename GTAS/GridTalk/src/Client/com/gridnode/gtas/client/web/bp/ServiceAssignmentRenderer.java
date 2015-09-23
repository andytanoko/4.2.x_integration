/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignmentRenderer.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.ctrl.IGTServiceAssignmentEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ServiceAssignmentRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number[] _fields = //20030808AH - Rename _fields in line with our code convention
  {
    IGTServiceAssignmentEntity.USER_NAME,
    IGTServiceAssignmentEntity.USER_TYPE,
    IGTServiceAssignmentEntity.PASSWORD,
    IGTServiceAssignmentEntity.WEBSERVICE_UIDS
  };
  
  
  public ServiceAssignmentRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)getEntity();
      ServiceAssignmentAForm form = (ServiceAssignmentAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(serviceAssignment.getType(), _edit);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      renderFields(bfpr, serviceAssignment, _fields, null, form, null);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering ServiceAssignment screen",t);
    }
  }
}