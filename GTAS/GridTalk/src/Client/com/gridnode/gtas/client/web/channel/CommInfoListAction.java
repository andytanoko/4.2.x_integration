/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 * 2002-12-04     Andrew Hill         Refactored to new suit new CommInfo model
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if noP2P is on.
 * 2008-07-17	  Teh Yu Phei 		  Add getIsStateFieldId() (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.IGTCommInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class CommInfoListAction extends AbstractProfileListAction
{
  private static final Number[] _columns = new Number[]
  {
    IGTCommInfoEntity.NAME,
    IGTCommInfoEntity.DESCRIPTION,
    IGTCommInfoEntity.IS_PARTNER,
    IGTCommInfoEntity.PARTNER_CAT,
    IGTCommInfoEntity.PROTOCOL_TYPE,
    IGTCommInfoEntity.REF_ID,
    IGTCommInfoEntity.URL,
  };

  private static final Number[] _noP2Pcolumns = new Number[]
                                                      {
                                                        IGTCommInfoEntity.NAME,
                                                        IGTCommInfoEntity.DESCRIPTION,
                                                        IGTCommInfoEntity.IS_PARTNER,
                                                        IGTCommInfoEntity.PROTOCOL_TYPE,
                                                        IGTCommInfoEntity.REF_ID,
                                                        IGTCommInfoEntity.URL,
                                                      };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_channel";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
  {
  	//NSL20060425 Check for NoP2P
  	if (isNoP2P(actionContext))
  	{
  		return _noP2Pcolumns;
  	}
    return _columns;
  }

  protected Number getIsPartnerFieldId()
  {
    return IGTCommInfoEntity.IS_PARTNER;
  }

  protected Number getPartnerCategoryFieldId()
  {
    return IGTCommInfoEntity.PARTNER_CAT;
  }


  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_COMM_INFO;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "commInfo";
  }

@Override
protected Number getIsStateFieldId() {
	// TODO Auto-generated method stub
	return null;
}

  /*protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new GridTalkMappingRuleListDecoratorRenderer(rContext,
                                                          isHeaderTransformation(actionContext)) );
  }*/
}