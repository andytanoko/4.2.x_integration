/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 * 2003-01-23     Andrew Hill         Allow sendChannelUid selection in both responding role
 * 2003-02-17     Daniel D'Cotta      Modified to also display docType if TwoActionProcess
 */
package com.gridnode.gtas.client.web.bp;

import org.w3c.dom.*;
import java.util.*;
import org.apache.struts.action.*;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.*;

public class ProcessMappingRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;
  private static final Number _commonFields[] =
  {
    IGTProcessMappingEntity.PROCESS_DEF,
    IGTProcessMappingEntity.PARTNER_ID,
    IGTProcessMappingEntity.IS_INITIATING_ROLE,
  };
  private static final Number _sendChannelUidFields[] =
  { //Using an array to satisfy the renderFields() method. (I dont trust renderField() its too old)
    IGTProcessMappingEntity.SEND_CHANNEL_UID,
  };
  private static final Number _docTypeFields[] =
  {
    IGTProcessMappingEntity.DOC_TYPE,
  };

  public ProcessMappingRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)getEntity();
      ProcessMappingAForm form = (ProcessMappingAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(mapping.getType(), _edit);

      boolean isInitiatingRole = form.getIsInitiatingRolePrimitiveBoolean();

      BindingFieldPropertyRenderer bfpr = renderFields(null, mapping, _commonFields, this, form, "");
      renderLabel("processDef_create","processMapping.processDef.create",false);
      renderLabel("partnerId_create","processMapping.partnerId.create",false);

      boolean displayDocType = false;
      if(isInitiatingRole)
      {
        // Do not hide docType if TwoActionProcess
        String processDefName = form.getProcessDef();
        if(processDefName != null && processDefName.length() != 0)
        {
          IGTSession gtasSession = mapping.getSession();
          IGTProcessDefManager manager = (IGTProcessDefManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_DEF);
          IGTProcessDefEntity processDef = (IGTProcessDefEntity)StaticUtils.getFirst(manager.getByKey(processDefName, IGTProcessDefEntity.DEF_NAME));
          if(processDef != null)
            if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE)))
              displayDocType = true;
        }
      }
      else
      {
        // Do not hide docType if respondingRole
        displayDocType = true;
      }

      if(displayDocType)
      {
        renderFields(bfpr, mapping, _docTypeFields, this, form, "");
        renderLabel("docType_create", "processMapping.docType.create", false);
      }
      else
        removeNode("docType_details", false);

      renderSendChannelUid(rContext,bfpr,mapping,form); //20030123AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering processMapping screen",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTPartnerEntity)
    {
      if( ((BindingFieldPropertyRenderer)context).getFieldId().equals(IGTProcessMappingEntity.PARTNER_ID) )
      {
        Short state = (Short)((IGTPartnerEntity)object).getFieldValue(IGTPartnerEntity.STATE);
        return IGTPartnerEntity.STATE_ENABLED.equals(state);
      }
    }
    return true;
  }

  private void renderSendChannelUid(RenderingContext rContext,
                                    BindingFieldPropertyRenderer bfpr,
                                    IGTProcessMappingEntity mapping,
                                    ProcessMappingAForm form)
    throws RenderingException
  { //20030123AH - Factored out the code for this field into this method
    // Render the field manually due to the complex needs for filtering
    // This is a lot more cumbersome than using the bfpr - which is a one line call however
    // the bfpr filtering mechanism will be particulaly inneficient in this particular case!
    // If partnerId not selected then we hide the field. Nb: if we are in view mode we render
    // the field as 'normal' using a bfpr. If the view layout is using a select node (unlikely)
    // this could be bad...
    // @todo: give bfpr a way of overriding normal entity list lookup mechanism for these fields
    // nb: later this code will be refactored somewhat when a convienience event is provided to
    // return the list of channels based on partnerId.
    try
    {
      if(_edit)
      {
        if(StaticUtils.stringNotEmpty(form.getPartnerId()))
        {
          String partnerId = form.getPartnerId();
          // Get appropriate channels
          IGTSession gtasSession = mapping.getSession();
          Collection channels = getApplicableChannels(rContext, gtasSession, mapping, partnerId);
          // Render the allowed channels for selection
          IGTFieldMetaInfo sendChannelUidFmi = mapping.getFieldMetaInfo(IGTProcessMappingEntity.SEND_CHANNEL_UID);
          IForeignEntityConstraint constraint = (IForeignEntityConstraint)sendChannelUidFmi.getConstraint();
          IGTChannelInfoManager channelMgr = (IGTChannelInfoManager)
                                              gtasSession.getManager(IGTManager.MANAGER_CHANNEL_INFO);
          Number displayField = channelMgr.getFieldId(IGTEntity.ENTITY_CHANNEL_INFO,
                                                      constraint.getDisplayFieldName());
          Number valueField = channelMgr.getFieldId(IGTEntity.ENTITY_CHANNEL_INFO,
                                                      constraint.getKeyFieldName());
          IOptionValueRetriever channelRetriever = new EntityOptionValueRetriever(
                                                       displayField,
                                                       valueField);
          String sendChannelUidFieldName = sendChannelUidFmi.getFieldName();
          String sendChannelUidValueId = sendChannelUidFieldName + "_value";
          Element sendChannelUidNode = getElementById(sendChannelUidValueId,true);
          renderSelectOptions(sendChannelUidValueId,channels,channelRetriever,true,"");
          renderSelectedOptions(sendChannelUidNode, form.getSendChannelUid());
          // Render the field label
          String sendChannelUidLabelId = sendChannelUidFieldName + "_label";
          renderLabel(sendChannelUidLabelId,sendChannelUidFmi.getLabel(),false);
          // Render any error messages associated with the field
          String sendChannelUidErrorId = sendChannelUidFmi.getFieldName() + "_error";
          ActionErrors errors = rContext.getActionErrors();
          ActionError error = MessageUtils.getFirstError(errors, sendChannelUidFieldName);
          if(error != null)
          {
            renderLabel(sendChannelUidErrorId,error.getKey(),false);
          }
          //20030123AH - No longer allow diversion for this fields as its not very useful!
          removeNode("sendChannelUid_create",false);
          //renderLabel("sendChannelUid_create","processMapping.sendChannelUid.create",false);
        }
        else
        { //No partnerId - remove field
          removeNode("sendChannelUid_details",false);
        }
      }
      else
      { //VIew mode - render normally using bfpr (and hope its a TEXT_PARENT etc...!)
        renderFields(bfpr,mapping,_sendChannelUidFields);
      }
    }
    catch(Throwable t)
    { //Oops...
      throw new RenderingException( "Error rendering sendChannelUid field based"
                                    + " on selected partnerId '" + form.getPartnerId()
                                    + "'" ,t);
    }
  }

  private Collection getApplicableChannels( RenderingContext rContext,
                                            IGTSession gtasSession,
                                            IGTProcessMappingEntity mapping,
                                            String partnerId)
    throws RenderingException
  { //20030123AH - Factored channel retrival out into this method.
    //Have implemented some naughty code that caches channels for the selected partner
    //in the operation context. (Since we dont have direct diversions...
    //This caching will need to be taken out later when it gets easier to create the channels and
    //assign to be and partner etc... by diverting from ProcessMapping screen...
    try
    {
      if(partnerId == null) throw new NullPointerException("partnerId is null"); //20030416AH
      //Evil caching stuff to aid redraw speed
      OperationContext opCon = rContext.getOperationContext();
      String channelCacheKey = "cachedChannels." + partnerId;
      Collection channels = (Collection)opCon.getAttribute(channelCacheKey);
      if(channels == null)
      { //Havent cached channel list for this partner - must do it the hard way
        IGTPartnerManager partnerMgr = (IGTPartnerManager)
                                        gtasSession.getManager(IGTManager.MANAGER_PARTNER);
        IGTPartnerEntity partner = partnerMgr.getPartnerByPartnerId(partnerId);
        Collection bes = partner.getFieldEntities(IGTPartnerEntity.BE);
        IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)StaticUtils.getFirst(bes);
        channels = (be == null) ? bes : be.getFieldEntities(IGTBusinessEntityEntity.CHANNELS);
        opCon.setAttribute(channelCacheKey,channels);
      }
      return channels;
    }
    catch(Throwable t)
    {
      throw new RenderingException( "Error retrieving list of applicable channels for"
                                    + " sendChannelUid field where partnerId=" + partnerId,t);
    }
  }
}