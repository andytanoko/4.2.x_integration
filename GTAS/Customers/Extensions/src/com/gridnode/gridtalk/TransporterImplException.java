/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransporterImplException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 04 2003    Neo Sok Lay         Created
 */
package com.gridnode.gridtalk;

/**
 * Exception used to indicate errors encountered when the method specific 
 * Transporter transmits the file.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class TransporterImplException extends Exception
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7909051020481230641L;

	/**
   * Constructs a TransportImplException
   * 
   * @param msg Error message.
   */
  public TransporterImplException(String msg)
  {
    super(msg);
  }


}