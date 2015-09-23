/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2003-12-22     Daniel D'Cotta      Moved Zip & ZipTreshold to FlowControlInfo
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory for NoP2P
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class PackagingInfoRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] _fields = new Number[]
  {
    IGTPackagingInfoEntity.NAME,
    IGTPackagingInfoEntity.DESCRIPTION,
    IGTPackagingInfoEntity.ENVELOPE,
    // IGTPackagingInfoEntity.ZIP,
    // IGTPackagingInfoEntity.ZIP_THRESHOLD,
    IGTPackagingInfoEntity.PARTNER_CAT,
    IGTPackagingInfoEntity.IS_PARTNER,
    IGTPackagingInfoEntity.REF_ID,
  };

  // 20031124 DDJ: AS2 fields
  protected static final Number[] _as2Fields = new Number[]
  {
    IGTAs2PackagingInfoExtensionEntity.IS_ACK_REQ,
    IGTAs2PackagingInfoExtensionEntity.IS_ACK_SIGNED,
    IGTAs2PackagingInfoExtensionEntity.IS_NRR_REQ,
    IGTAs2PackagingInfoExtensionEntity.IS_ACK_SYN,
    IGTAs2PackagingInfoExtensionEntity.RETURN_URL,
  };

  public PackagingInfoRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTPackagingInfoEntity pi = (IGTPackagingInfoEntity)getEntity();
      PackagingInfoAForm piForm = (PackagingInfoAForm)getActionForm();

      renderCommonFormElements(IGTEntity.ENTITY_PACKAGING_INFO, _edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, pi, _fields);
      
      //NSL20060425 Check for NoP2P
      if (isNoP2P())
      {
      	removeNode("partnerCategory_details");
      }
      // if(!StaticUtils.primitiveBooleanValue(piForm.getZip()))
      // {
      //   removeNode("zipThreshold_details");
      // }

      // 20031124 DDJ: AS2 fields
      if(IGTPackagingInfoEntity.AS2_ENVELOPE_TYPE.equals(piForm.getEnvelope()))
      {
        renderLabel("as2PackagingInfoExtension_heading", "packagingInfo.as2PackagingInfoExtension.tab.as2", false);

        IGTAs2PackagingInfoExtensionEntity as2 = (IGTAs2PackagingInfoExtensionEntity)pi.getFieldValue(IGTPackagingInfoEntity.PKG_INFO_EXTENSION);
        if(as2 == null)
        {
          IGTSession gtasSession = pi.getSession();
          IGTPackagingInfoManager manager = (IGTPackagingInfoManager)gtasSession.getManager(IGTManager.MANAGER_PACKAGING_INFO);
          as2 = manager.newAs2PackagingInfoExtension();
        }

        renderFields(bfpr, as2, _as2Fields);

        if(!piForm.getIsAckReqAsBoolean().booleanValue())
        {
          removeNode("isAckSigned_details");
          removeNode("isNrrReq_details");
          removeNode("isAckSyn_details");
          removeNode("returnUrl_details");
        }
        else
        {
          if(!piForm.getIsAckSignedAsBoolean().booleanValue())
          {
            removeNode("isNrrReq_details");
          }
          if(piForm.getIsAckSynAsBoolean().booleanValue())
          {
            removeNode("returnUrl_details");
          }
        }
      }
      else
      {
        removeNode("as2PackagingInfoExtension_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering packagingInfo screen",t);
    }
  }
  
  //NSL20060425
  protected boolean isNoP2P()
  {
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
  	
  	return gtasSession.isNoP2P();
  }

}