/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 * 2003-07-08     Andrew Hill         Remove / refactor deprecated methods
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.ctrl.IGTDocumentTypeEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class DocumentTypeListAction extends EntityListAction
{
  Object[] _columns =
  { //20030708AH
    IGTDocumentTypeEntity.DOC_TYPE,
    IGTDocumentTypeEntity.DESCRIPTION
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }
  
  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_DOCUMENT_TYPE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_DOCUMENT_TYPE; //20030708AH
  }
}