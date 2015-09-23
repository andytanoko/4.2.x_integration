/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" stuff
 * 2003-07-08     Andrew Hill         Remove / refactor deprecated methods
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if NoP2P is on.
 * 2008-07-17	  Teh Yu Phei 		  Add getIsStateFieldId() (Ticket 31)
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSecurityInfoEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class SecurityInfoListAction extends AbstractProfileListAction
{
  private static final Object[] _columns = new Number[]
  { //20030708AH
    IGTSecurityInfoEntity.NAME,
    IGTSecurityInfoEntity.DESCRIPTION,
    IGTSecurityInfoEntity.IS_PARTNER,
    IGTSecurityInfoEntity.PARTNER_CAT,
    IGTSecurityInfoEntity.ENC_TYPE,
    IGTSecurityInfoEntity.SIG_TYPE,
    IGTSecurityInfoEntity.COMPRESSION_TYPE, // 20031126 DDJ
  };

  private static final Object[] _noP2Pcolumns = new Number[]
                                                      { 
                                                        IGTSecurityInfoEntity.NAME,
                                                        IGTSecurityInfoEntity.DESCRIPTION,
                                                        IGTSecurityInfoEntity.IS_PARTNER,
                                                        IGTSecurityInfoEntity.ENC_TYPE,
                                                        IGTSecurityInfoEntity.SIG_TYPE,
                                                        IGTSecurityInfoEntity.COMPRESSION_TYPE,
                                                      };

  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
  	//NSl20060425 Check for NoP2P
  	if (isNoP2P(actionContext))
  	{
  		return _noP2Pcolumns;
  	}
  	//20030708AH
    return _columns;
  }

  protected Number getIsPartnerFieldId()
  {
    return IGTSecurityInfoEntity.IS_PARTNER;
  }

  protected Number getPartnerCategoryFieldId()
  {
    return IGTSecurityInfoEntity.PARTNER_CAT;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_SECURITY_INFO;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_SECURITY_INFO; //20030708AH
  }

@Override
protected Number getIsStateFieldId() {
	// TODO Auto-generated method stub
	return null;
}
}