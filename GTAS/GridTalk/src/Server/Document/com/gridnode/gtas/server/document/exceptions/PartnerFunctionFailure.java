/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionFailure.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.gtas.server.document.helpers.IDocProcessingErrorConstants;
import com.gridnode.gtas.server.document.helpers.AlertDelegate;
import com.gridnode.gtas.server.document.model.GridDocument;

/**
 * This is a Throwable object to indicate a failure during partner function
 * execution.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class PartnerFunctionFailure
  extends    Throwable
  implements IDocProcessingErrorConstants
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3124682307353300408L;
	private String _type;
  private String _reason;
  private Throwable _exception;
  private GridDocument _gdoc;

  /**
   * Construct a PartnerFunctionFailure object
   *
   * @param type The type of failure.
   * @param reason The reason for the failure
   * @param t Exception that causes the failure.
   * @see IDocProcessingErrorConstants
   */
  public PartnerFunctionFailure(String type, String reason, Throwable t)
  {
    super("PartnerFunctionFailure>> Type:"+type +", Reason: "+reason);
    _type = type;
    _reason = reason;
    _exception = t;
  }

  /**
   * Construct a PartnerFunctionFailure object
   *
   * @param gdoc The document that is related to this failure.
   * @param type The type of failure.
   * @param reason The reason for the failure
   * @param t Exception that causes the failure.
   * @see IDocProcessingErrorConstants
   */
  public PartnerFunctionFailure(GridDocument gdoc,
                                String type,
                                String reason,
                                Throwable t)
  {
    this(type, reason, t);
    _gdoc = gdoc;
  }

  public String getType()
  {
    return _type;
  }

  public String getReason()
  {
    return _reason;
  }

  public Throwable getException()
  {
    return _exception;
  }

  public GridDocument getDocument()
  {
    return _gdoc;
  }

  public void setDocument(GridDocument gdoc)
  {
    _gdoc = gdoc;
  }

  /**
   * Raise alert base on the information in this PartnerFunctionFailure object.
   */
  public void raiseAlert()
  {
    AlertDelegate.raisePartnerFunctionFailureAlert(
      getDocument(),
      getType(),
      getReason(),
      getException());
  }

  /**
   * Raise alert base on the information in this PartnerFunctionFailure object.
   *
   * @param gdoc To set into the Document field of this object and raise alert
   * base on this document and other information in this object.
   */
  public void raiseAlert(GridDocument gdoc)
  {
    setDocument(gdoc);
    raiseAlert();
  }


}