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

import com.gridnode.gtas.model.document.IAS2DocTypeMapping;

public interface IGTAS2DocTypeMappingEntity extends IGTEntity 
{
  public static final Number UID            = IAS2DocTypeMapping.UID;
  public static final Number AS2_DOC_TYPE   = IAS2DocTypeMapping.AS2_DOC_TYPE;
  public static final Number DOC_TYPE       = IAS2DocTypeMapping.DOC_TYPE;
  public static final Number PARTNER_ID     = IAS2DocTypeMapping.PARTNER_ID;
}
