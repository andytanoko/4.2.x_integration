/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2002-10-14     Andrew Hill         Render divert labels, kill level zero etc...
 * 2003-08-08     Andrew Hill         Support the isLocalPending field
 * 2003-12-30     Daniel D'Cotta      Optimised rendering of Channel by copying
 *                                    from ProcessMappingRenderer
 * 2006-04-28     Neo Sok Lay         To hide isLocalPending if noP2P.                                   
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class TriggerRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;
  private static final Number[] _fields = //20030808AH - Rename _fields in line with our code convention
  {
    IGTTriggerEntity.TRIGGER_TYPE,
    IGTTriggerEntity.TRIGGER_LEVEL,
    IGTTriggerEntity.PARTNER_FUNCTION_ID,
  };
  
//  private static final Number[] _ilpFields =
//  { //20030808AH
//    IGTTriggerEntity.IS_LOCAL_PENDING,
//  };

  public TriggerRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTTriggerEntity trigger = (IGTTriggerEntity)getEntity();
      TriggerAForm form = (TriggerAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(trigger.getType(), _edit);

      //20030808AH - co: BindingFieldPropertyRenderer bfpr = renderFields(_fields, this);
      BindingFieldPropertyRenderer bfpr = renderFields(null, trigger, _fields, this, form, null); //20030808AH - Use non-deprecated renderFields
      renderLabel("partnerFunctionId_create","trigger.partnerFunctionId.create",false);

      if(_edit) //20021014AH
      {
        try
        {
          // Remove the levelZero option as this may not be selected and trigger that have
          // it may not be edited anyhow!
          Element levelNode = getElementById("triggerLevel_value");
          Node zeroOption = this.getOptionElement(levelNode, "0", true);
          levelNode.removeChild(zeroOption);
        }
        catch(Throwable t)
        {
          throw new RenderingException("Error removing the level zero option",t);
        }
      }

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      
      // Only show processId for import and manualSend
      Integer triggerType = form.getTriggerTypeInteger();
      if(IGTTriggerEntity.TRIGGER_IMPORT.equals(triggerType) || IGTTriggerEntity.TRIGGER_MANUAL_SEND.equals(triggerType))
      {
        renderLabel("processDef_heading","trigger.processDef.tab", false);

        renderField(bfpr, trigger, IGTTriggerEntity.PROCESS_ID);
        //NSL20060428
        if (gtasSession.isNoP2P())
        {
        	removeNode("isLocalPending_details", false);
        }
        else
        {
        	renderField(bfpr, trigger, IGTTriggerEntity.IS_LOCAL_PENDING); //20030808AH
        }
        if(_edit)
        {
          renderLabel("processId_create","trigger.processId.create", false);
        }

        // Only show isRequest when a processId is selected
        if(StaticUtils.stringNotEmpty(form.getProcessId()))
        {
          renderField(bfpr, trigger, IGTTriggerEntity.IS_REQUEST);

          removeNode("numOfRetries_details", false);  // 20031120 DDJ
          removeNode("retryInterval_details", false); // 20031120 DDJ
        }
        else
        {
          renderField(bfpr, trigger, IGTTriggerEntity.NUM_OF_RETRIES); // 20031120 DDJ
          renderField(bfpr, trigger, IGTTriggerEntity.RETRY_INTERVAL); // 20031120 DDJ

          removeNode("isRequest_details", false);
        }
      }
      else
      {
        removeNode("processDef_details", false);
        removeNode("isLocalPending_details",false); //20030808AH
        removeNode("isRequest_details", false);     // 20031217 DDJ
        removeNode("numOfRetries_details", false);  // 20031217 DDJ
        removeNode("retryInterval_details", false); // 20031217 DDJ
      }

      Integer triggerLevel = form.getTriggerLevelInteger();
      if(IGTTriggerEntity.TRIGGER_LEVEL_0.equals(triggerLevel))
      {
        removeNode("docType_details", false);
        removeNode("partnerType_details", false);
        removeNode("partnerGroup_details", false);
        removeNode("partnerId_details", false);
        removeNode("channelUid_details", false); // 20031120 DDJ
      }
      else if(IGTTriggerEntity.TRIGGER_LEVEL_1.equals(triggerLevel))
      {
        renderField(bfpr, trigger, IGTTriggerEntity.DOC_TYPE);
        removeNode("partnerType_details", false);
        removeNode("partnerGroup_details", false);
        removeNode("partnerId_details", false);
        removeNode("channelUid_details", false); // 20031120 DDJ

/*
        if(_edit)
        { //20021014AH
          renderLabel("docType_create","trigger.docType.create",false);
        }
*/        
      }
      else if(IGTTriggerEntity.TRIGGER_LEVEL_2.equals(triggerLevel))
      {
        renderField(bfpr, trigger, IGTTriggerEntity.DOC_TYPE);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_TYPE);
        removeNode("partnerGroup_details", false);
        removeNode("partnerId_details", false);
        removeNode("channelUid_details", false); // 20031120 DDJ

        if(_edit)
        { //20021014AH
/*
          renderLabel("docType_create","trigger.docType.create",false);
          renderLabel("partnerType_create","trigger.partnerType.create",false);
*/          
          Element partnerTypeNode = getElementById("partnerType_value",false);
          if(partnerTypeNode !=null)
          { // Remove onchange handler for level 2
            partnerTypeNode.removeAttribute("onchange");
          }
        }
      }
      else if(IGTTriggerEntity.TRIGGER_LEVEL_3.equals(triggerLevel))
      {
        renderField(bfpr, trigger, IGTTriggerEntity.DOC_TYPE);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_TYPE);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_GROUP);
        removeNode("partnerId_details", false);
        removeNode("channelUid_details", false); // 20031120 DDJ

        if(_edit)
        { //20021014AH
/*
          renderLabel("docType_create","trigger.docType.create",false);
          renderLabel("partnerType_create","trigger.partnerType.create",false);
          renderLabel("partnerGroup_create","trigger.partnerGroup.create",false);
*/          
          Element partnerGroupNode = getElementById("partnerGroup_value",false);
          if(partnerGroupNode != null)
          { // Remove onchange handler for level 3
            partnerGroupNode.removeAttribute("onchange");
          }
        }
      }
      else if(IGTTriggerEntity.TRIGGER_LEVEL_4.equals(triggerLevel))
      {
        renderField(bfpr, trigger, IGTTriggerEntity.DOC_TYPE);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_TYPE);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_GROUP);
        renderField(bfpr, trigger, IGTTriggerEntity.PARTNER_ID);
        
        // 20031214 DDJ: Added filtering of channel
        if(StaticUtils.stringNotEmpty(form.getPartnerId()))
        {
          if(_edit)
          {
            // 20031230: Optimise rendering if in edit more
            renderChannelUid(rContext, bfpr, trigger, form);
          }
          else
          {
            renderField(bfpr, trigger, IGTTriggerEntity.CHANNEL_UID); // 20031120 DDJ
          }
        }
        else
        {
          removeNode("channelUid_details", false); // 20031120 DDJ
        }
/*
        if(_edit)
        { //20021014AH
          renderLabel("docType_create","trigger.docType.create",false);
          renderLabel("partnerType_create","trigger.partnerType.create",false);
          renderLabel("partnerGroup_create","trigger.partnerGroup.create",false);
          renderLabel("partnerId_create","trigger.partnerId.create",false);
        }
*/        
      }
      else
      {
        removeNode("docType_details", false);
        removeNode("partnerType_details", false);
        removeNode("partnerGroup_details", false);
        removeNode("partnerId_details", false);
        removeNode("channelUid_details", false); // 20031120 DDJ
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering trigger screen",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    //BindingFieldPropertyRenderer bfpr = (BindingFieldPropertyRenderer)context;
    TriggerAForm form = (TriggerAForm)getActionForm();
    String partnerType = form.getPartnerType();
    String partnerGroup = form.getPartnerGroup();

    if(object instanceof IGTPartnerGroupEntity)
    {
      IGTPartnerGroupEntity pg = (IGTPartnerGroupEntity)object;
      IGTPartnerTypeEntity pt = (IGTPartnerTypeEntity)StaticUtils.getFirst(pg.getFieldEntities(IGTPartnerGroupEntity.PARTNER_TYPE));
      return pt.getFieldString(IGTPartnerTypeEntity.PARTNER_TYPE).equals(partnerType);
    }
    else if(object instanceof IGTPartnerEntity)
    {
      IGTPartnerEntity p = (IGTPartnerEntity)object;
      IGTPartnerTypeEntity pt = (IGTPartnerTypeEntity)StaticUtils.getFirst(p.getFieldEntities(IGTPartnerEntity.PARTNER_TYPE));
      boolean typeOk = pt.getFieldString(IGTPartnerTypeEntity.PARTNER_TYPE).equals(partnerType);
      if(typeOk)
      {
        IGTPartnerGroupEntity pg = (IGTPartnerGroupEntity)StaticUtils.getFirst(p.getFieldEntities(IGTPartnerEntity.PARTNER_GROUP));
        if(pg == null)
        {
          return "".equals(partnerGroup) || partnerGroup == null;
        }
        else
        {
          return pg.getFieldString(IGTPartnerGroupEntity.NAME).equals(partnerGroup);
        }
      }
      else
      {
        return false;
      }
    }
/*    
    // 20031230 DDJ: Not needed more as Channel is manually rendered
    else if(object instanceof IGTChannelInfoEntity)
    { // 20031214 DDJ: Added filtering of channel
      IGTChannelInfoEntity channel = (IGTChannelInfoEntity)object;
      String partnerId = form.getPartnerId();
      if(StaticUtils.stringNotEmpty(partnerId))
      {
        IGTPartnerManager partnerMgr = (IGTPartnerManager)channel.getSession().getManager(IGTManager.MANAGER_PARTNER);
        IGTPartnerEntity partner = (IGTPartnerEntity)partnerMgr.getByKey(partnerId, IGTPartnerEntity.PARTNER_ID);
        Collection channels = (Collection)partner.getFieldValue(IGTPartnerEntity.CHANNEL);

        if(channels != null)
        {
          Long channelUid = form.getChannelUidAsLong();
          return channels.contains(channelUid);
        }
      }
      return false;
    }
*/    
    return true;
  }

  // 20031230 DDJ: copied from ProcessMappingRenderer 
  private void renderChannelUid(RenderingContext rContext,
                                BindingFieldPropertyRenderer bfpr,
                                IGTTriggerEntity trigger,
                                TriggerAForm form)
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
      String partnerId = form.getPartnerId();

      // Get appropriate channels
      IGTSession gtasSession = trigger.getSession();
      Collection channels = getApplicableChannels(rContext, gtasSession, partnerId);

      // Render the allowed channels for selection
      IGTFieldMetaInfo sendChannelUidFmi = trigger.getFieldMetaInfo(IGTTriggerEntity.CHANNEL_UID);
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
      renderSelectedOptions(sendChannelUidNode, form.getChannelUid());

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
    }
    catch(Throwable t)
    { //Oops...
      throw new RenderingException( "Error rendering ChannelUid field based"
                                    + " on selected partnerId '" + form.getPartnerId()
                                    + "'" ,t);
    }
  }

  // 20031230 DDJ: copied from ProcessMappingRenderer 
  private Collection getApplicableChannels( RenderingContext rContext,
                                            IGTSession gtasSession,
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
                                    + " ChannelUid field where partnerId=" + partnerId,t);
    }
  }
}