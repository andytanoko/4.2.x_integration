/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-28     Andrew Hill         Created
 * 2002-0?-??     Daniel              Tabbed panels
 * 2002-11-07     Andrew Hill         Render i18n labels for tabs
 * 2002-12-05     Andrew Hill         Attachments
 * 2002-12-31     Daniel D'Cotta      Refactored to use new tabbed panels and
 *                                    commented out Attachments
 * 2003-01-29     Andrew Hill         Re-enable rendering of attachments field
 * 2003-08-04     Andrew Hill         Use non-deprecated renderFields() method
 * 2003-08-21     Andrew Hill         processInstanceId, userTrackingIdentifier
 * 2003-08-25     Andrew Hill         processInstanceUid to be used instead of processInstanceId
 * 2005-03-15     Andrew Hill         Disable the diversion links (except processInstance) for non-admin users
 * 2009-03-18     Ong Eu Soon         Add Audit File Name, Receipt Audit File Name & Doc transaction Status
 */
package com.gridnode.gtas.client.web.document;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ITabDef;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.TabDef;

public class GridDocumentRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] _fields = new Number[]{ //20030804AH - Renamed _fields in accordance with convention
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.G_DOC_FILENAME,
    IGTGridDocumentEntity.FOLDER,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.U_DOC_VERSION,
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_FILE_TYPE,
    IGTGridDocumentEntity.U_DOC_FILESIZE,
    IGTGridDocumentEntity.S_G_DOC_ID,
    IGTGridDocumentEntity.R_G_DOC_ID,
    IGTGridDocumentEntity.REF_G_DOC_ID,
    IGTGridDocumentEntity.REF_U_DOC_NUM,
    IGTGridDocumentEntity.REF_U_DOC_FILENAME,
    IGTGridDocumentEntity.SOURCE_FOLDER,
    IGTGridDocumentEntity.PARTNER_FUNCTION,
    IGTGridDocumentEntity.S_NODE_ID,
    IGTGridDocumentEntity.S_BIZ_ENTITY_ID,
    IGTGridDocumentEntity.S_PARTNER_ID,
    IGTGridDocumentEntity.S_PARTNER_NAME,
    IGTGridDocumentEntity.S_PARTNER_TYPE,
    IGTGridDocumentEntity.S_PARTNER_GROUP,
    IGTGridDocumentEntity.S_USER_ID,
    IGTGridDocumentEntity.S_USER_NAME,
    IGTGridDocumentEntity.R_NODE_ID,
    IGTGridDocumentEntity.R_BIZ_ENTITY_ID,
    IGTGridDocumentEntity.R_PARTNER_ID,
    IGTGridDocumentEntity.R_PARTNER_NAME,
    IGTGridDocumentEntity.R_PARTNER_TYPE,
    IGTGridDocumentEntity.R_PARTNER_GROUP,
    IGTGridDocumentEntity.DT_IMPORT,
    IGTGridDocumentEntity.DT_SEND_END,
    IGTGridDocumentEntity.DT_TRANSACTION_COMPLETE,
    IGTGridDocumentEntity.DT_RECEIVE_END,
    IGTGridDocumentEntity.DT_EXPORT,
    IGTGridDocumentEntity.DT_VIEW,
    IGTGridDocumentEntity.R_CHANNEL_NAME,
    IGTGridDocumentEntity.ENCRYPTION_LEVEL,
    IGTGridDocumentEntity.S_ROUTE,
    IGTGridDocumentEntity.PORT_NAME,
    IGTGridDocumentEntity.ATTACHMENT_FILENAMES,
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030821AH
    IGTGridDocumentEntity.AUDIT_FILE_NAME,
    IGTGridDocumentEntity.DOC_TRANS_STATUS,
    IGTGridDocumentEntity.RECEIPT_AUDIT_FILE_NAME,
  };

  protected static final ITabDef[] _tabs = {
    new TabDef("gridDocument.tabs.general","general_tab"),
    new TabDef("gridDocument.tabs.traceability","traceability_tab"),
    new TabDef("gridDocument.tabs.partner","partner_tab"),
    new TabDef("gridDocument.tabs.communication","communication_tab"),
    new TabDef("gridDocument.tabs.attachments","attachments_tab"),
  };

  public GridDocumentRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTGridDocumentEntity doc = (IGTGridDocumentEntity)getEntity();
      GridDocumentAForm form = (GridDocumentAForm)getActionForm();

      renderCommonFormElements(IGTEntity.ENTITY_GRID_DOCUMENT,_edit);
      //20030804AH - co: renderFields(fields);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext); //20050315AH
      //20050315AH - Hide diversion links from non-admins
      IGTSession gtasSession = doc.getSession();
      if(!gtasSession.isAdmin())
      {
        bfpr.setFbdEnabled(false); 
        //Also remove edit button
        removeNode("edit_button",false); 
      }
      //...
      renderFields(bfpr,doc,_fields); //20030804AH, 20050315AH
      //nb: even non-admin allowed to divert to the processInstance
      makeProcessInstanceLink(rContext, form, doc); //20030822AH

      renderTabs(rContext, "gridDocumentTab", _tabs);
//    TWX 18072007:  Add in the pop up to notify the user if there is any input error from user 
      if(_edit)
      {
        includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS);
        appendEventMethod(getBodyNode(),"onload","tabErrorNotifier();");
      }
      
      renderLabel("general_heading","gridDocument.general.heading",false);
      renderLabel("embedded_heading","gridDocument.embedded.heading",false);
      renderLabel("traceability_heading","gridDocument.traceability.heading",false);
      renderLabel("partnerFunction_heading","gridDocument.partnerFunction.heading",false);
      renderLabel("sender_heading","gridDocument.sender.heading",false);
      renderLabel("receiver_heading","gridDocument.receiver.heading",false);
      renderLabel("communication_heading","gridDocument.communication.heading",false);
      renderLabel("channel_heading","gridDocument.channel.heading",false);
      renderLabel("routing_heading","gridDocument.routing.heading",false);
      renderLabel("port_heading","gridDocument.port.heading",false);
      renderLabel("attachments_heading","gridDocument.attachments.heading",false);

      boolean hasAttachments = StaticUtils.primitiveBooleanValue(form.getHasAttachment());
      if(!hasAttachments)
      {
        Element attachmentsTab = getElementById("attachments_tab");
        attachmentsTab.removeAttribute("onclick");
        attachmentsTab.setAttribute("class","disabledTab");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering gridDocument screen",t);
    }
  }
  
  protected void makeProcessInstanceLink( RenderingContext rContext,
                                            GridDocumentAForm form,
                                            IGTGridDocumentEntity gdoc)
    throws RenderingException
  { //20030822AH
    try
    {
      String processInstanceUid = form.getProcessInstanceUid();
      if( StaticUtils.stringEmpty(processInstanceUid) )
      {
        return;
      }
      Element valueElement = getElementById("processInstanceUid_value",false);
      if(valueElement == null)
      {
        return;
      }
      Node[] children = getChildArray(valueElement);
      removeAllChildren(valueElement);
      Element anchor = _target.createElement("a");
      anchor.setAttribute("href", "javascript:divertToMapping('divertViewProcessInstance');");
      for(int i=0; i < children.length; i++)
      {
        anchor.appendChild(children[i]);
      }
      valueElement.appendChild(anchor);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error making processInstanceId field a diversion link");
    }
  }
}