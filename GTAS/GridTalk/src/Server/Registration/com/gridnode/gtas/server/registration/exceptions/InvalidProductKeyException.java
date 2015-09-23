/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidProductKeyException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when Product Key validation fails.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class InvalidProductKeyException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7579656515473077231L;

	/**
   * Thrown if Invalid product key or nodeId does not match product key.
   *
   * @param prodKey The Product key being validated.
   * @param nodeId Node Id for the product key.
   */
  public InvalidProductKeyException(String prodKey, int nodeId)
  {
    super("Invalid Product Key for Gridnode: "+prodKey+","+nodeId);
  }

  /**
   * Thrown when exception occurs while validating the product key which
   * causes validation to discontinue.
   *
   * @param msg The error message.
   */
  public InvalidProductKeyException(String msg)
  {
    super("Product Key validation fails due to " +msg);
  }
}