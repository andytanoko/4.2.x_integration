/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 * 2002-08-05     Andrew Hill         Refactored
 * 2002-10-08     Andrew Hill         "partnerCat" mods
 * 2002-12-04     Andrew Hill         Refactor for new CommInfo model
 * 2003-11-18     Daniel D'Cotta      Added URL description
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if NoP2P
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.IGTCommInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class CommInfoRenderer extends AbstractRenderer
{
  private boolean _edit;

  protected static final Number[] fields = new Number[]{IGTCommInfoEntity.DESCRIPTION,
                                                        IGTCommInfoEntity.NAME,
                                                        IGTCommInfoEntity.PROTOCOL_TYPE,
                                                        IGTCommInfoEntity.REF_ID,
                                                        IGTCommInfoEntity.IS_PARTNER,
                                                        IGTCommInfoEntity.PARTNER_CAT,
                                                        IGTCommInfoEntity.URL };
  public CommInfoRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTCommInfoEntity commInfo = (IGTCommInfoEntity)getEntity();
      renderCommonFormElements(commInfo.getType(),_edit);
      renderFields(null, commInfo, fields);
      //NSL20060425
      if (isNoP2P())
      {
      	removeNode("partnerCategory_details");
      }
      
      // 20031118 DDJ: Added URL description
      if(_edit)
      {
        CommInfoAForm form = (CommInfoAForm)getActionForm();
        String protocolType = form.getProtocolType();
System.out.println("### CommInfoRenderer.render(): protocolType=" + protocolType);
System.out.println("### CommInfoRenderer.render(): commInfo.url.description.http=" + getRenderingContext().getResourceLookup().getMessage("commInfo.url.description.http"));
        if(StaticUtils.stringNotEmpty(protocolType))
        {
          renderLabel("url_description_value", "commInfo.url.description." + protocolType.toLowerCase());
System.out.println("### CommInfoRenderer.render(): url_description_value.value=" + getElementById("url_description_value").getAttribute("value"));
        }
        else
        {
          removeNode("url_description_details");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering commInfo screen",t);
    }
  }
  
  //NSL20060425
  protected boolean isNoP2P()
  {
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
  	
  	return gtasSession.isNoP2P();
  }

}