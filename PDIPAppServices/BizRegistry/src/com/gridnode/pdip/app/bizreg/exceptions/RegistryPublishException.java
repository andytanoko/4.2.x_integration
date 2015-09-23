/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryPublishException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.exceptions;

import com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

/**
 * Thrown when application error occurs during publishing of 
 * registry information objects.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryPublishException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5374712713635552037L;
	private static final String PUBLISH_UNSUPPORTED = "Publish unsupported for registry type {0}";
  private static final String NULL_REGISTRY_INFO = "Registry Info not specified";
  private static final String PROVIDER_ERROR = "Registry provider error";
  private static final String UNKNOWN_APP_ERROR = "Unknown registry error";

  private static final double[] TYPE_LIMITS = {
    IRegistryInfo.TYPE_BINDING,
    IRegistryInfo.TYPE_CONCEPT,
    IRegistryInfo.TYPE_ORGANIZATION,
    IRegistryInfo.TYPE_SCHEME,
    IRegistryInfo.TYPE_SERVICE,
    IRegistryInfo.TYPE_SPECIFICATION};
  private static final String[] TYPE_DESCS = {
    "Binding",
    "Concept",
    "Organization",
    "Scheme",
    "Service",
    "Specification"};
  private static final ChoiceFormat TYPE_FORMAT = new ChoiceFormat(TYPE_LIMITS, TYPE_DESCS);

 
  /**
   * Constructor for RegistryPublishException.
   * @param msg The error message.
   */
  public RegistryPublishException(String msg)
  {
    super(msg);
  }

  /**
   * Constructor for RegistryPublishException.
   * @param msg The error message.
   * @param ex The cause of the exception
   */
  public RegistryPublishException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  /**
   * Constructor for RegistryPublishException.
   * @param ex The cause of the exception
   */
  public RegistryPublishException(Throwable ex)
  {
    super(ex);
  }

  /**
   * Creates a RegistryPublishException caused by Unsupported registry
   * type for publishing.
   * 
   * @param type The type that is not supported.
   * @return The created exception.
   */
  public static RegistryPublishException unsupportedRegistryType(int type)
  {
    MessageFormat msg = new MessageFormat(PUBLISH_UNSUPPORTED);
    msg.setFormat(0, TYPE_FORMAT);
    String message = msg.format(new Integer[] {new Integer(type)});
    return new RegistryPublishException(message);
  }
  
  /**
   * Creates a RegistryPublishException caused by <b>null</b> registry
   * information object specified for publishing.
   * 
   * @return The created exception.
   */
  public static RegistryPublishException nullRegistryInfo()
  {
    return new RegistryPublishException(NULL_REGISTRY_INFO);
  }

  /**
   * Creates a RegistryPublishException caused by exceptions thrown
   * by the registry provider.
   * 
   * @param e The exception thrown by the registry provider.
   * @return The created exception.
   */
  public static RegistryPublishException registryProviderError(
    RegistryProviderException e)
  {
    return new RegistryPublishException(PROVIDER_ERROR, e);
  }

  /**
   * Creates a RegistryPublishException caused by other unknown 
   * application errors.
   * 
   * @param t The application error that occurs.
   * @return The created exception.
   */
  public static RegistryPublishException unknownRegistryError(
    Throwable t)
  {
    return new RegistryPublishException(UNKNOWN_APP_ERROR, t);
  }
  
}
