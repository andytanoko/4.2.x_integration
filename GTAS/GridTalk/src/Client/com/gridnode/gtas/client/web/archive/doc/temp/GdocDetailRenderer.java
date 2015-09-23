/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GdocDetailRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 27 Oct 2006    Regina Zeng         Created
 * 13 Dec 2006    Tam Wei Xiang       Render the attachment filenames
 */
package com.gridnode.gtas.client.web.archive.doc.temp;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTGdocDetailEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.document.GridDocumentAForm;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ITabDef;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.TabDef;

public class GdocDetailRenderer extends AbstractRenderer
{
  private boolean _edit;
  
  protected static final Number[] _fields = new Number[]{ 
    IGTGdocDetailEntity.G_DOC_ID,
    IGTGdocDetailEntity.G_DOC_FILENAME,
    IGTGdocDetailEntity.FOLDER,
    IGTGdocDetailEntity.U_DOC_NUM,
    IGTGdocDetailEntity.U_DOC_FILENAME,
    IGTGdocDetailEntity.U_DOC_VERSION,
    IGTGdocDetailEntity.U_DOC_DOC_TYPE,
    IGTGdocDetailEntity.U_DOC_FILE_TYPE,
    IGTGdocDetailEntity.U_DOC_FILESIZE,
    IGTGdocDetailEntity.S_G_DOC_ID,
    IGTGdocDetailEntity.R_G_DOC_ID,
    IGTGdocDetailEntity.REF_G_DOC_ID,
    IGTGdocDetailEntity.REF_U_DOC_NUM,
    IGTGdocDetailEntity.REF_U_DOC_FILENAME,
    IGTGdocDetailEntity.SOURCE_FOLDER,
    IGTGdocDetailEntity.PARTNER_FUNCTION,
    IGTGdocDetailEntity.S_NODE_ID,
    IGTGdocDetailEntity.S_BIZ_ENTITY_ID,
    IGTGdocDetailEntity.S_PARTNER_ID,
    IGTGdocDetailEntity.S_PARTNER_NAME,
    IGTGdocDetailEntity.S_PARTNER_TYPE,
    IGTGdocDetailEntity.S_PARTNER_GROUP,
    IGTGdocDetailEntity.S_USER_ID,
    IGTGdocDetailEntity.S_USER_NAME,
    IGTGdocDetailEntity.R_NODE_ID,
    IGTGdocDetailEntity.R_BIZ_ENTITY_ID,
    IGTGdocDetailEntity.R_PARTNER_ID,
    IGTGdocDetailEntity.R_PARTNER_NAME,
    IGTGdocDetailEntity.R_PARTNER_TYPE,
    IGTGdocDetailEntity.R_PARTNER_GROUP,
    IGTGdocDetailEntity.DT_IMPORT,
    IGTGdocDetailEntity.DT_SEND_END,
    IGTGdocDetailEntity.DT_TRANSACTION_COMPLETE,
    IGTGdocDetailEntity.DT_RECEIVE_END,
    IGTGdocDetailEntity.DT_EXPORT,
    IGTGdocDetailEntity.DT_VIEW,
    IGTGdocDetailEntity.R_CHANNEL_NAME,
    IGTGdocDetailEntity.ENCRYPTION_LEVEL,
    IGTGdocDetailEntity.S_ROUTE,
    IGTGdocDetailEntity.PORT_NAME,      
    IGTGdocDetailEntity.USER_TRACKING_ID, 
    IGTGdocDetailEntity.PROCESS_INSTANCE_ID,
    IGTGdocDetailEntity.DOC_TRANS_STATUS,
    IGTGdocDetailEntity.ATTACHMENT_FILENAMES,
    IGTGdocDetailEntity.AUDIT_FILENAME,
    IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME
  };
  
  protected static final ITabDef[] _tabs = {
    new TabDef("EsPi.tabs.general","general_tab"),
    new TabDef("EsPi.tabs.traceability","traceability_tab"),
    new TabDef("EsPi.tabs.partner","partner_tab"),
    new TabDef("EsPi.tabs.communication","communication_tab"),
    new TabDef("EsPi.tabs.attachments","attachments_tab"),
    new TabDef("EsPi.tabs.error","error_tab")
  };

  public GdocDetailRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTGdocDetailEntity instance = (IGTGdocDetailEntity)getEntity();
      GridDocumentAForm form = (GridDocumentAForm)getActionForm();

      renderCommonFormElements(IGTEntity.ENTITY_GDOC_DETAIL,_edit);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext); 
      IGTSession gtasSession = instance.getSession(); 
      removeEditButton();
      
      if(!gtasSession.isAdmin())
      {
        bfpr.setFbdEnabled(false);          
      }
      renderFields(bfpr,instance,_fields); 

      renderTabs(rContext, "gridDocumentTab", _tabs);

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
      renderLabel("error_heading", "EsPi.error.heading", false);
      
      renderLabel("senderCert_label", "gridDocument.senderCert", false);
      Element senderCertLabel = getElementById("senderCert_label");
      senderCertLabel.setAttribute("class", "fieldlabel");
      replaceText("senderCert_value", instance.getFieldString(IGTGdocDetailEntity.SENDER_CERT_NAME), true);
      
      renderLabel("receiverCert_label", "gridDocument.receiverCert", false);
      Element receiverCertLabel = getElementById("receiverCert_label");
      receiverCertLabel.setAttribute("class", "fieldlabel");
      replaceText("receiverCert_value", instance.getFieldString(IGTGdocDetailEntity.RECEIVER_CERT_NAME), true);
      
      /*
      renderLabel("auditFilename_label", "gridDocument.auditFileName", false);
      Element auditFilenameLabel = getElementById("auditFilename_label");
      auditFilenameLabel.setAttribute("class", "fieldlabel");
      replaceText("auditFilename_value", instance.getFieldString(IGTGdocDetailEntity.AUDIT_FILENAME), true);
      */
      
      /*
      renderLabel("receiptAuditFilename_label", "gridDocument.receiptAuditFileName", false);
      Element receiptAuditFilenameLabel = getElementById("receiptAuditFilename_label");
      receiptAuditFilenameLabel.setAttribute("class", "fieldlabel");
      replaceText("receiptAuditFilename_value", instance.getFieldString(IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME), true);
      */
      
      // Rendering of new Error tab
      renderLabel("error_label", "documentMetaInfo.remark", false);
      Element errorLabel = getElementById("error_label");
      errorLabel.setAttribute("class", "fieldlabel");
      replaceMultilineText("error_value", instance.getFieldString(IGTGdocDetailEntity.DOC_REMARKS), true);
      
      boolean hasAttachments = StaticUtils.primitiveBooleanValue(form.getHasAttachment());
      
      if(!hasAttachments)
      {
        Element attachmentsTab = getElementById("attachments_tab");
        attachmentsTab.removeAttribute("onclick");
        attachmentsTab.setAttribute("class","disabledTab");
      }
      
      String remarkValue = (String)form.getDocRemarks();
      if(remarkValue==null || "".equals(remarkValue))
      {
        Element attachmentsTab = getElementById("error_tab");
        attachmentsTab.removeAttribute("onclick");
        attachmentsTab.setAttribute("class","disabledTab");
      }
      
      makeLinks(form);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering gdoc detail screen",t);
    }
  }

  private void log(String message)
  {
    com.gridnode.gtas.client.web.archive.helpers.Logger.debug("[GdocDetailRenderer] " + message);
  }
  
  private void removeEditButton() throws RenderingException
  {
    removeNode("edit_button", true);
  }
  
  protected void makeLinks(GridDocumentAForm form) throws RenderingException
  { 
    try
    {
      boolean checkAF, checkRAF, checkSC, checkRC;
      String senderCert = form.getSenderCert();
      String receiverCert = form.getReceiverCert();
      //Element valueElementAF = getElementById("auditFilename_value",false);
      //Element valueElementRAF = getElementById("receiptAuditFilename_value",false);
      Element valueElementSC = getElementById("senderCert_value",false);
      Element valueElementRC = getElementById("receiverCert_value",false);
      
      /*
      if((StaticUtils.stringNotEmpty(auditFilename)) && (valueElementAF!=null)) 
        checkAF=true; 
      else
        checkAF=false;
      
      if((StaticUtils.stringNotEmpty(receiptAuditFilename)) && (valueElementRAF!=null))
        checkRAF=true; 
      else
        checkRAF=false; */
      
      if((StaticUtils.stringNotEmpty(senderCert)) && (valueElementSC!=null)) 
        checkSC=true; 
      else
        checkSC=false;
      
      if((StaticUtils.stringNotEmpty(receiverCert)) && (valueElementRC!=null)) 
        checkRC=true; 
      else
        checkRC=false;
      
      /**
       * Due to the time constraint for implementing template for AS2 audit, we let user download instead
       * TODO: implement another template for handling AS2 audit
      if(checkAF==true) 
        generateLink(valueElementAF,"divertToViewAuditFile");
      if(checkRAF==true) 
        generateLink(valueElementRAF,"divertToViewAuditFile");
        */
      if(checkSC==true)
        generateLink(valueElementSC,"divertToViewSCert");
      if(checkRC==true)
        generateLink(valueElementRC,"divertToViewRCert");
      else
        return;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error making field a diversion link");
    }
  }
  
  private void generateLink(Element valueElement, String url)
  {
    Node[] children = getChildArray(valueElement);
    removeAllChildren(valueElement);
    Element anchor = _target.createElement("a");
    anchor.setAttribute("href", "javascript:divertToMapping('"+url+"');");
    
    for(int i=0; i < children.length; i++)
    {
      anchor.appendChild(children[i]);
    }
    valueElement.appendChild(anchor);
  }
}
