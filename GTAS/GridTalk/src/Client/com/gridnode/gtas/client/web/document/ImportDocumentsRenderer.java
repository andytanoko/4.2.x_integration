/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocumentsRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-26     Andrew Hill         Created
 * 2002-11-26     Andrew Hill         Render value for senderId manually
 * 2002-12-05     Andrew Hill         Attachments
 * 2003-08-26     Andrew Hill         Refactor to use OptionSource methodology for senderId
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTImportDocuments;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTUserManager;
import com.gridnode.gtas.client.web.renderers.*;

public class ImportDocumentsRenderer extends AbstractRenderer implements IBFPROptionSource
{
  private boolean _edit;

  protected static final Number[] _fields = new Number[] //20030826AH - Renamed _fields to meet convention
  {
    IGTImportDocuments.SENDER_ID, //20030826AH
    IGTImportDocuments.DOC_TYPE,
    IGTImportDocuments.RECIPIENTS,
    IGTImportDocuments.FILENAMES,
    IGTImportDocuments.ATTACHMENTS, //20021205AH
  };

  public ImportDocumentsRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    if(!_edit)
    {
      throw new java.lang.IllegalArgumentException("Viewing of importDocuments entity is not supported");
    }
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ImportDocumentsAForm form = (ImportDocumentsAForm)getActionForm();
      IGTImportDocuments importDocuments = (IGTImportDocuments)getEntity(); //20030826AH
      renderCommonFormElements(importDocuments.getType(),_edit); //20030826AH
      //20030826AH - Use optionSource for senderId
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this); 
      bfpr = renderFields(bfpr,importDocuments, _fields, null, form, null);
      //... 
      /* 20030826 - co: renderSenderId(rContext,form);
      renderLabel("docType_create","importDocuments.docType.create",false);
      renderLabel("recipients_create","importDocuments.recipients.create",false);*/
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering importDocuments screen",t);
    }
  }

  /* 20030826AH - co: protected void renderSenderId(RenderingContext rContext, ImportDocumentsAForm form)
    throws RenderingException
  {
    try
    {
      Collection senders = getAllowedSenders(rContext,form);
      IOptionValueRetriever senderRetriever = new EntityOptionValueRetriever( IGTBusinessEntityEntity.ID,
                                                                              IGTBusinessEntityEntity.ID);
      Element senderIdNode = getElementById("senderId_value",true);
      renderSelectOptions("senderId_value",senders,senderRetriever,true,"");
      renderSelectedOptions(senderIdNode, form.getSenderId() ); //20021205AH
      renderLabel("senderId_label","importDocuments.senderId",false);
      renderLabel("senderId_create","importDocuments.senderId.create",false);
      ActionErrors errors = rContext.getActionErrors();
      ActionError error = MessageUtils.getFirstError(errors, "senderId");
      if(error != null)
      {
        renderLabel("senderId_error",error.getKey(),false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering the senderId field",t);
    }
  }*/

  protected Collection getAllowedSenders(RenderingContext rContext, ImportDocumentsAForm form)
    throws RenderingException
  {
    try
    {
      IGTImportDocuments importDocs = (IGTImportDocuments)getEntity();
      IGTSession gtasSession = importDocs.getSession();
      IGTUserManager userMgr = (IGTUserManager)gtasSession.getManager(IGTManager.MANAGER_USER);
      Long userUid = gtasSession.getUserUid();
      Collection businessEntities = userMgr.getUserBusinessEntities( userUid );
      return businessEntities;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error initialising list of permissible BEs for SenderId field",t);
    }
  }
  
  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr) throws GTClientException
  { //20030826AH
    if(IGTImportDocuments.SENDER_ID.equals(bfpr.getFieldId()) )
    {
      //By using an optionSource instead of hardcoding the rendering the new diversion
      //automation will work for this field
      ImportDocumentsAForm form = (ImportDocumentsAForm)bfpr.getActionForm();
      return getAllowedSenders(rContext, form);
    }
    else
    {
      return null;
    }
  }

}