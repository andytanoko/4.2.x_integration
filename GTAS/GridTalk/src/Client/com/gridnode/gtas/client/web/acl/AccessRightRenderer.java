/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-08-01     Andrew Hill         Refactored
 */
package com.gridnode.gtas.client.web.acl;

import java.util.Collection;

import com.gridnode.gtas.client.ctrl.IGTAccessRightEntity;
import com.gridnode.gtas.client.web.renderers.*;

public class AccessRightRenderer extends AbstractRenderer
{
  private boolean _edit;
  private Collection _actions;
  private Collection _dataTypes;

  protected final static Number[] fields = new Number[]{
                                            IGTAccessRightEntity.ACTION,
                                            IGTAccessRightEntity.DATA_TYPE,
                                            IGTAccessRightEntity.DESCRIPTION,
                                            IGTAccessRightEntity.FEATURE,
                                            IGTAccessRightEntity.ROLE, };

  public AccessRightRenderer( RenderingContext rContext,
                              boolean edit,
                              Collection actions,
                              Collection dataTypes)
  {
    super(rContext);
    _edit = edit;
    _actions = actions;
    _dataTypes = dataTypes;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTAccessRightEntity accessRight = (IGTAccessRightEntity)getEntity();

      renderCommonFormElements(accessRight.getType(),_edit);
      if(_edit)
      {
        // Render options for feature & dataType that were looked up for us in the action
        IOptionValueRetriever strRetriever = new StringValueRetriever();
        renderSelectOptions("actionName_value", _actions, strRetriever, true, "generic.empty");
        renderSelectOptions("dataType_value", _dataTypes, strRetriever, true, "generic.empty");
      }
      renderFields(null, accessRight, fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering accessRight screen",t);
    }
  }
}