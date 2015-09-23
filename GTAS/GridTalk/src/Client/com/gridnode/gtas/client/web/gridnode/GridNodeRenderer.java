/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.gridnode;

import com.gridnode.gtas.client.ctrl.IGTGridNodeEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
 
public class GridNodeRenderer extends AbstractRenderer
{
  protected boolean _edit;

  protected static final Number[] fields = {
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.CATEGORY,
    IGTGridNodeEntity.STATE,
    IGTGridNodeEntity.ACTIVATION_REASON,
    IGTGridNodeEntity.DT_CREATED,
    IGTGridNodeEntity.DT_REQ_ACTIVATE,
    IGTGridNodeEntity.DT_ACTIVATED,
    IGTGridNodeEntity.DT_DEACTIVATED,
  };

  public GridNodeRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    if(edit) throw new java.lang.IllegalArgumentException("edit may not be true for gridNode");
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      GridNodeAForm form = (GridNodeAForm)getActionForm();
      IGTGridNodeEntity gridNode = (IGTGridNodeEntity)getEntity();
      renderCommonFormElements(gridNode.getType(),_edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null,gridNode,fields);
      Short state = form.getStateShort();
      if(IGTGridNodeEntity.STATE_ACTIVE.equals(state))
      {
        removeNode("dtDeactivated_details",false);
      }
      else if(IGTGridNodeEntity.STATE_ME.equals(state))
      {
        removeNode("dtActivated_details",false);
        removeNode("dtDeactivated_details",false);
        removeNode("dtReqActivate_details",false);
        removeNode("activationReason_details",false);
      }
      else if(IGTGridNodeEntity.STATE_GM.equals(state))
      {
        removeNode("dtDeactivated_details",false);
        removeNode("dtReqActivate_details",false);
        removeNode("activationReason_details",false);
      }
      else if(IGTGridNodeEntity.STATE_INACTIVE.equals(state))
      {
        
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering gridNode screen",t);
    }
  }
}

