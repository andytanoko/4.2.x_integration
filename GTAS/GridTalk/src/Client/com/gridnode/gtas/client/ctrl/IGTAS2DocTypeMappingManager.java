/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCertificateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17     Wong Yee Wah         Created
 *
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTAS2DocTypeMappingManager extends IGTManager
{
  public IGTAS2DocTypeMappingEntity getAS2DocTypeMappingByUID(long uid) throws GTClientException;
  
  public Collection getApplicablePartnerList()
  throws GTClientException;

  public Collection getApplicableDocumentTypeList()
  throws GTClientException;

  
}
