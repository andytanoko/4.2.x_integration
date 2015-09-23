/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractAuditTrailEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 08, 2007    Tam Wei Xiang       Remove getUid(), setUid()
 */
package com.gridnode.gtas.audit.model;

/**
 * This class is the abstract class for TraceEventInfo, EventTrailHeader, DocumentTransaction,
 * ProcessTransaction, and BizDocument
 * @author Tam Wei Xiang
 * @since GT 4.0
 * 
 * @hibernate.class abstract = "true"
 */
public interface IAuditTrailEntity
{
  public String getGroupName();
  
  public void setGroupName(String groupName);
  
  public String getUID();
  
  public String toString();
}
