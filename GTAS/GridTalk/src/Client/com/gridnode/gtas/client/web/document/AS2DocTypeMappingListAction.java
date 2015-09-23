/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.ctrl.IGTAS2DocTypeMappingEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class AS2DocTypeMappingListAction extends EntityListAction
{
  Object[] _columns =
  {
      IGTAS2DocTypeMappingEntity.PARTNER_ID,
      IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE,
      IGTAS2DocTypeMappingEntity.DOC_TYPE,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
    return _columns;
  }
  
  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING;
  }
  
  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_AS2_DOC_TYPE_MAPPING;
  }
}
