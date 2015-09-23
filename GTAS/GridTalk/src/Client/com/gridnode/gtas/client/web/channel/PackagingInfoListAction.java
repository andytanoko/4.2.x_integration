/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 * 2003-07-08     Andrew Hill         Remove / Refactor some deprecated methods
 * 2003-12-22     Daniel D'Cotta      Moved Zip & ZipTreshold to FlowControlInfo
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if NoP2P is on.
 * 2008-07-17	  Teh Yu Phei 		  Add getIsStateFieldId() (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPackagingInfoEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class PackagingInfoListAction extends AbstractProfileListAction
{
  private static final Object[] _columns = new Number[]
  { //20030708AH
    IGTPackagingInfoEntity.NAME,
    IGTPackagingInfoEntity.DESCRIPTION,
    IGTPackagingInfoEntity.IS_PARTNER,
    IGTPackagingInfoEntity.PARTNER_CAT,
    IGTPackagingInfoEntity.ENVELOPE,
    // IGTPackagingInfoEntity.ZIP,
  };

  private static final Object[] _noP2Pcolumns = new Number[]
                                                      { 
                                                        IGTPackagingInfoEntity.NAME,
                                                        IGTPackagingInfoEntity.DESCRIPTION,
                                                        IGTPackagingInfoEntity.IS_PARTNER,
                                                        IGTPackagingInfoEntity.ENVELOPE,
                                                      };

  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
  	//NSL20060425 Check for NoP2P
  	if (isNoP2P(actionContext))
  	{
  		return _noP2Pcolumns;
  	}
  	//20030708AH
    return _columns;
  }

  protected Number getIsPartnerFieldId()
  {
    return IGTPackagingInfoEntity.IS_PARTNER;
  }

  protected Number getPartnerCategoryFieldId()
  {
    return IGTPackagingInfoEntity.PARTNER_CAT;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PACKAGING_INFO;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_PACKAGING_INFO; //20030708AH
  }

@Override
protected Number getIsStateFieldId() {
	// TODO Auto-generated method stub
	return null;
}
}