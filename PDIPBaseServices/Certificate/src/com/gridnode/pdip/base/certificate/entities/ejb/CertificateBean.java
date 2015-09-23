/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateBean
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * JUL 03 2002    Jagadeesh        Created
 */


package com.gridnode.pdip.base.certificate.entities.ejb;

import com.gridnode.pdip.base.certificate.model.Certificate;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

public class CertificateBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4916412659314587368L;

	public String getEntityName()
  {
    return Certificate.ENTITY_NAME;
  }

}