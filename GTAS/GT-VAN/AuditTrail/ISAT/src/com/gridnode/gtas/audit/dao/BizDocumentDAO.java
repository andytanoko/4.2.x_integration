/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizDocumentDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 15, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.dao;

import com.gridnode.gtas.audit.model.BizDocument;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class BizDocumentDAO extends AuditTrailEntityDAO
{

  public BizDocumentDAO()
  {
    super(false);
  }
  
  public BizDocumentDAO(boolean newSession)
  {
    super(newSession);
  }
  
  public BizDocument getBizDocumentByUID(String bizDocumentUID)
  {
    String queryName = getPersistenceClass().getName()+".getBusinessDocumentByUID";
    String[] paramName = new String[]{"uid"};
    String[] paramValue = new String[]{bizDocumentUID};
    
    return (BizDocument)queryOne(queryName, paramName, paramValue);
  }
  
  @Override
  public Class getPersistenceClass()
  {
    // TODO Auto-generated method stub
    return BizDocument.class;
  }

}
