/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 * 2002-12-02     Andrew Hill         Bodgy tweaking of avail filterings
 * 2002-12-26     Andrew Hill         abort & deny 'button' links
 * 2003-03-28     Andrew Hill         Modify call to addListviewButtonLink to pass icon url
 */
package com.gridnode.gtas.client.web.activation;

import com.gridnode.gtas.client.ctrl.IGTActivationRecordEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ActivationRecordListDecoratorRenderer  extends AbstractRenderer
{
  private static final String DENY_IMAGE_SRC = "images/actions/deny.gif"; //20030328AH
  private static final String ABORT_IMAGE_SRC = "images/actions/abort.gif"; //20030328AH

  private String _filterType;
  private String _summaryType;
  private String _actionUrl; //20021226AH

  public ActivationRecordListDecoratorRenderer( RenderingContext rContext,
                                                String filterType,
                                                String summaryType,
                                                String actionUrl )
  {
    super(rContext);
    _filterType = filterType;
    _summaryType = summaryType;
    _actionUrl = actionUrl; //20021226AH
  }

  protected void render() throws RenderingException
  {
    try
    {
      removeNode("create_details",false);
      removeNode("delete_details",false);
      RenderingContext rContext = getRenderingContext();
      if(_filterType != null)
      {
        if(IGTActivationRecordEntity.FILTER_TYPE_INCOMING_ACTIVATION.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.incomingActivation",false);
          addListviewLinkButton(rContext,
                                "deny",
                                _actionUrl,
                                "activationRecord.listview.deny",
                                DENY_IMAGE_SRC,
                                "denyMultiple",
                                "activationRecord.listview.confirmDeny"); //20030328AH
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_ACTIVATION.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.outgoingActivation",false);
          addListviewLinkButton(rContext,
                                "abort",
                                _actionUrl,
                                "activationRecord.listview.abort",
                                ABORT_IMAGE_SRC,
                                "abortMultiple",
                                "activationRecord.listview.confirmAbort"); //20030328AH
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_INCOMING_DEACTIVATION.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.incomingDeactivation",false);
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_DEACTIVATION.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.outgoingDeactivation",false);
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_APPROVED.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.approved",false);
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_DENIED.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.denied",false);
        }
        else if(IGTActivationRecordEntity.FILTER_TYPE_ABORTED.equals(_filterType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.aborted",false);
        }
        else
        {
          throw new java.lang.IllegalStateException("Unrecognised filterType:" + _filterType);
        }
      }
      else
      {
        if("activation".equals(_summaryType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.activation",false);
        }
        else if("deactivation".equals(_summaryType))
        {
          renderLabel("listview_heading","activationRecord.listview.heading.deactivation",false);
        }

      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating activationRecord listview",t);
    }
  }
}