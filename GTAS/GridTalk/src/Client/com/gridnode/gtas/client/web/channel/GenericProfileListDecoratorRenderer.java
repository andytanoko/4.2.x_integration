/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenericProfileListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-09     Andrew Hill         Created
 * 2006-04-25     Neo Sok Lay         Support NoP2P option
 * 2008-07-17     Teh Yu Phei		  Add GenericProfileListDecoratorRenderer( RenderingContext rContext,
 * 										                                       String isState, String prefix) (Ticket 31)
 * 									  Change render() (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTProfileEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class GenericProfileListDecoratorRenderer  extends AbstractRenderer
{
	private static final String ACTIVATE_IMAGE_SRC = "images/actions/activate.gif"; //2003021AH
  private static final Short PARTNER_CAT_OTHERS = IGTProfileEntity.PARTNER_CAT_OTHERS;
  private static final Short PARTNER_CAT_GRIDTALK = IGTProfileEntity.PARTNER_CAT_GRIDTALK;

  private Boolean _isPartner;
  private Short _partnerCategory;
  private String _prefix;
  private String _isState;

  public GenericProfileListDecoratorRenderer( RenderingContext rContext,
                                              Boolean isPartner,
                                              Short partnerCategory,
                                              String prefix)
  {
    super(rContext);
    _isPartner = isPartner;
    _partnerCategory = partnerCategory;
    _prefix = prefix;
    _isState = null;
    if( (_isPartner != null) && (_partnerCategory != null) )
    {
      throw new java.lang.IllegalArgumentException("Only one or none of isPartner and partnerCategory may be specified");
    }
  }

  public GenericProfileListDecoratorRenderer( RenderingContext rContext,
          String isState,
          String prefix)
  {
	  super(rContext);
	  _isPartner = null;
	  _partnerCategory = null;
	  _prefix = prefix;
	  _isState = isState;
  }
  
  protected void render() throws RenderingException
  {
  	//NSL20060426
	RenderingContext rContext = getRenderingContext();
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest()); 
  	
    if(_isPartner != null)
    {
      if(Boolean.FALSE.equals(_isPartner))
      {
        // my profile
        renderLabel("listview_heading",_prefix + ".listview.heading.own",false);
      }
      else
      {
        // partner profile
        renderLabel("listview_heading",_prefix + ".listview.heading.partner",false);
        if (!gtasSession.isNoP2P()) //NSL20060425 if noP2P, allow delete function since all partners will be in the single grouping
        {
        	removeNode("delete_details",false);
        }
      }
      appendCreateParam(_isPartner);
    }
    else if(_partnerCategory != null)
    {
      if(PARTNER_CAT_GRIDTALK.equals(_partnerCategory))
      {
        // gridtalk partner profiles
        renderLabel("listview_heading",_prefix + ".listview.heading.gridTalkPartner",false);
        removeNode("create_details",false);
        removeNode("delete_details",false);
      }
      else if(PARTNER_CAT_OTHERS.equals(_partnerCategory))
      {
        // other partner profile
        renderLabel("listview_heading",_prefix + ".listview.heading.othersPartner",false);
        appendCreateParam(Boolean.TRUE);
      }
      else
      {
        throw new java.lang.IllegalStateException("Unknown partnerCategory value " + _partnerCategory);
      }
    }
    else if (_isState != null && _isState.equals("1"))
    {
    	renderLabel("listview_heading",_prefix + ".listview.heading.deleted",false);
        removeNode("create_details",false);
        removeNode("delete_details",false);
        
        String activateMsg = rContext.getResourceLookup().getMessage("businessEntity.listview.confirmActivate");
        Element activateLinkButton = createButtonLink("activate",
                                                    "businessEntity.listview.activate",
                                                    ACTIVATE_IMAGE_SRC,
                                                    "submitMultipleEntities('activate','" + activateMsg + "');",
                                                    true);
        
        Node refreshLink = getElementById("manual_refresh",true);
        refreshLink.getParentNode().appendChild(activateLinkButton);
    }
    else
    {
      // all profile
      if (!gtasSession.isNoP2P()) //NSL20060425 if noP2P, allow delete function since all profiles will have delete enabled
      {
      	removeNode("delete_details",false);
      }
    }
  }

  private void appendCreateParam(Boolean isPartner)
    throws RenderingException
  {
    Element createLink = getElementById("create",false);
    if(createLink != null)
    {
      String href = createLink.getAttribute("href");
      if(href != null)
      {
        if(isPartner != null)
        {
          href = StaticWebUtils.addParameterToURL(href, "isPartner","" + isPartner);
        }
        createLink.setAttribute("href",href);
      }
    }
  }
}