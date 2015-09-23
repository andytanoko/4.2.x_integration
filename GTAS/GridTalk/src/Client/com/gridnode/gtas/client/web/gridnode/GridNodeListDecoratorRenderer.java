/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 * 2002-12-26     Andrew Hill         Use addListviewLinkButton() for deactivate button
 * 2003-03-28     Andrew Hill         Pass deactivate imageUrl to button creation call
 * 2003-04-02     Andrew Hill         Correct typo in deactivate.gif url
 */
package com.gridnode.gtas.client.web.gridnode;

import com.gridnode.gtas.client.ctrl.IGTGridNodeEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
 
public class GridNodeListDecoratorRenderer  extends AbstractRenderer
{
  private static final String DEACTIVATE_IMAGE_SRC = "images/actions/deactivate.gif"; //20030402AH

  private Short _state;
  private String _deactivateUrl;

  public GridNodeListDecoratorRenderer( RenderingContext rContext,
                                        Short state,
                                        String deactivateUrl )
  {
    super(rContext);
    _state = state;
    _deactivateUrl = deactivateUrl;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      if(_state != null)
      {
        if(IGTGridNodeEntity.STATE_ME.equals(_state))
        {
          // Display list. No buttons. No selection
          renderLabel("listview_heading","gridNode.listview.heading.me",false);
        }
        else if(IGTGridNodeEntity.STATE_DELETED.equals(_state))
        {
          // Display list. No buttons. No selection
          renderLabel("listview_heading","gridNode.listview.heading.deleted",false);
        }
        else if(IGTGridNodeEntity.STATE_ACTIVE.equals(_state))
        {
          // Display list with buttons for deactivate
          //addDeactivateButton(rContext);
          addListviewLinkButton(rContext,
                                "deactivate",
                                _deactivateUrl,
                                "gridNode.listview.deactivate",
                                DEACTIVATE_IMAGE_SRC,
                                "deactivate",
                                "gridNode.listview.confirmDeactivate"); //20021226AH, 20030328AH
          renderLabel("listview_heading","gridNode.listview.heading.active",false);
        }
        else if(IGTGridNodeEntity.STATE_INACTIVE.equals(_state))
        {
          // Display list with buttons for activate, delete
          renderLabel("listview_heading","gridNode.listview.heading.inactive",false);
          //20030425AH - Delete event not made yet. Hide the delete button
          removeNode("delete_details",false); //20030425AH
        }
        else if(IGTGridNodeEntity.STATE_GM.equals(_state))
        {
          // Display list. No buttons. No selection
          renderLabel("listview_heading","gridNode.listview.heading.gm",false);
        }
        else
        {
          throw new java.lang.IllegalStateException("Unrecognised state:" + _state);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating GridNode listview",t);
    }
  }
}