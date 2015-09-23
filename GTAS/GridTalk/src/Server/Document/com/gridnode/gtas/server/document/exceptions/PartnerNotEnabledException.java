/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerNotEnabledException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception indicates that an operation requires that a Partner be in the
 * Enabled state before it can continue execution.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class PartnerNotEnabledException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6508373585649459201L;

	private PartnerNotEnabledException(String message)
  {
    super(message);
  }

  public static final PartnerNotEnabledException create(Partner partner)
  {
    StringBuffer buff = new StringBuffer();
    buff.append("Partner ").append(partner.getPartnerID());
    buff.append(" is not Enabled for document transaction.");

    return new PartnerNotEnabledException(buff.toString());
  }

}