/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-22     Andrew Hill         Created
 * 2002-08-05     Andrew Hill         Refactored
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory for NoP2P
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;

public class ChannelInfoRenderer extends AbstractRenderer
implements IFilter
{
  private boolean _edit;
  private boolean _isPartner;

  protected static final Number[] fields = new Number[]
  {
    IGTChannelInfoEntity.DESCRIPTION,
    IGTChannelInfoEntity.NAME,
    IGTChannelInfoEntity.REF_ID,
    IGTChannelInfoEntity.TPT_COMM_INFO_UID,
    IGTChannelInfoEntity.TPT_PROTOCOL_TYPE,
    IGTChannelInfoEntity.PACKAGING_PROFILE,
    IGTChannelInfoEntity.SECURITY_PROFILE,
    IGTChannelInfoEntity.IS_PARTNER,
    IGTChannelInfoEntity.PARTNER_CAT,
  };

  protected static final Number[] _fcFields = new Number[]
  {
    IGTFlowControlInfoEntity.IS_ZIP,
    //IGTFlowControlInfoEntity.ZIP_THRESHOLD,
    IGTFlowControlInfoEntity.IS_SPLIT,
    //IGTFlowControlInfoEntity.SPLIT_SIZE,
    //IGTFlowControlInfoEntity.SPLIT_THRESHOLD,
  };


  public ChannelInfoRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)getEntity();
      ChannelInfoAForm form = (ChannelInfoAForm)getActionForm();
      _isPartner = StaticUtils.primitiveBooleanValue(form.getIsPartner());
      renderCommonFormElements(channelInfo.getType(),_edit);
      if(_edit)
      {
        renderLabel("tptCommInfoUid_create","channelInfo.tptCommInfoUid.create",false);
        renderLabel("packagingProfile_create","channelInfo.packagingProfile.create",false);
        renderLabel("securityProfile_create","channelInfo.securityProfile.create",false);
      }
      BindingFieldPropertyRenderer bfpr = renderFields(null, channelInfo, fields, this, form, "");
      //NSL20060425 Check for NoP2P
      if (!isNoP2P())
      {
      	// FlowControl
      	renderLabel("flowControl_heading", "channelInfo.flowControlInfo");
      	
      	IGTFlowControlInfoEntity flowControl = (IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO);
      	renderFields(null, flowControl, _fcFields);
      	
      	if(form.getIsZipAsBoolean().booleanValue())
      	{
      		renderField(null, flowControl, IGTFlowControlInfoEntity.ZIP_THRESHOLD);
      	}
      	else
      	{
      		removeNode("zipThreshold_details");
      	}
      	if(form.getIsSplitAsBoolean().booleanValue())
      	{
      		renderField(null, flowControl, IGTFlowControlInfoEntity.SPLIT_SIZE);
      		renderField(null, flowControl, IGTFlowControlInfoEntity.SPLIT_THRESHOLD);
      	}
      	else
      	{
      		removeNode("splitSize_details");
      		removeNode("splitThreshold_details");
      	}
      }
      else
      {
      	removeNode("partnerCategory_details");
      	removeNode("flowControl_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering channelInfo screen",t);
    }
  }

  //NSL20060425
  protected boolean isNoP2P()
  {
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
  	
  	return gtasSession.isNoP2P();
  }

  protected boolean isProtocolType(String type)
    throws GTClientException
  {
    ChannelInfoAForm form = (ChannelInfoAForm)getRenderingContext().getOperationContext().getActionForm();
    String protocolType = form.getTptProtocolType();
    if(type == null)
    {
      return (protocolType == null) || ("".equals(protocolType));
    }
    else
    {
      return type.equals(protocolType);
    }
  }

  /*protected boolean isRefId(String value)
    throws GTClientException
  {
    ChannelInfoAForm form = (ChannelInfoAForm)getRenderingContext().getOperationContext().getActionForm();
    String refId = form.getRefId();
    if( (refId == null) || ("".equals(refId)) )
    { // If our refId not defined then all match
      return true;
    }
    if(value == null)
    {
      return (refId == null) || ("".equals(refId));
    }
    else
    {
      return value.equals(refId);
    }
  }*/

  public boolean allows(Object object, Object context) throws GTClientException
  {
    // the commInfo must have same type
    // 20021008AH - refId killed :-)
    if(object instanceof IGTCommInfoEntity)
    {
      String protocolType = ((IGTCommInfoEntity)object).getFieldString(IGTCommInfoEntity.PROTOCOL_TYPE);
      // Any commInfo we select must have a matching protocolType
      if(isProtocolType(protocolType))
      {
        //String refId = ((IGTCommInfoEntity)object).getFieldString(IGTCommInfoEntity.REF_ID);
        //return isRefId(refId);
        Boolean isPartnerCommInfo = (Boolean)((IGTCommInfoEntity)object).getFieldValue(IGTCommInfoEntity.IS_PARTNER);
        Short partnerCatCommInfo = (Short)((IGTCommInfoEntity)object).getFieldValue(IGTCommInfoEntity.PARTNER_CAT);
        if(_isPartner)
        {
          // If we are a partner ChannelInfo, the commInfos must be selected from those whose
          // partnerCat is others
          return (IGTCommInfoEntity.PARTNER_CAT_OTHERS.equals(partnerCatCommInfo));
        }
        else
        {
          // If however we are not a partner channelInfo, then we may select only those commInfos
          // which are not partner commInfos
          return (Boolean.FALSE.equals(isPartnerCommInfo));
        }
      }
      else
      {
        return false;
      }
    }
    else if(object instanceof IGTSecurityInfoEntity)
    {
      Boolean isPartnerSecurityInfo = (Boolean)((IGTSecurityInfoEntity)object).getFieldValue(IGTSecurityInfoEntity.IS_PARTNER);
      Short partnerCatSecurityInfo = (Short)((IGTSecurityInfoEntity)object).getFieldValue(IGTSecurityInfoEntity.PARTNER_CAT);
      if(_isPartner)
      {
        // If we are a partner ChannelInfo, the securityInfos must be selected from those whose
        // partnerCat is others
        return (IGTSecurityInfoEntity.PARTNER_CAT_OTHERS.equals(partnerCatSecurityInfo));
      }
      else
      {
        // If however we are not a partner channelInfo, then we may select only those securityInfos
        // which are not partner commInfos
        return (Boolean.FALSE.equals(isPartnerSecurityInfo));
      }
    }
    else if(object instanceof IGTPackagingInfoEntity)
    {
      Boolean isPartnerPackagingInfo = (Boolean)((IGTPackagingInfoEntity)object).getFieldValue(IGTPackagingInfoEntity.IS_PARTNER);
      Short partnerCatPackagingInfo = (Short)((IGTPackagingInfoEntity)object).getFieldValue(IGTPackagingInfoEntity.PARTNER_CAT);
      if(_isPartner)
      {
        // If we are a partner ChannelInfo, the packagingInfos must be selected from those whose
        // partnerCat is others
        return (IGTPackagingInfoEntity.PARTNER_CAT_OTHERS.equals(partnerCatPackagingInfo));
      }
      else
      {
        // If however we are not a partner channelInfo, then we may select only those packagingInfos
        // which are not partner commInfos
        return (Boolean.FALSE.equals(isPartnerPackagingInfo));
      }
    }
    return true;
  }
}