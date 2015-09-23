/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServiceIdentifierKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 * Oct 06 2003    Neo Sok Lay         Patternize the Message service name & description.
 */
package com.gridnode.gtas.server.enterprise.helpers;

/**
 * This interface defines the keys of the identifiers in the
 * ServiceIdentifiers namespace.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IServiceIdentifierKeys
{
  /* Not required anymore.
  static final String NAMESPACE_TYPE = "ServiceIdentifiers";
  
  static final String REFERENCE_ID = "channel.refid";
  static final String CHANNEL_DESC = "channel.desc";
  static final String PACKAGING_NAME = "channel.packaging.name";
  static final String PACKAGING_DESC = "channel.packaging.desc";
  static final String SECURITY_NAME = "channel.security.name";
  static final String SECURITY_DESC = "channel.security.desc";
  static final String TRANSPORT_NAME = "channel.transport.name";
  static final String TRANSPORT_DESC = "channel.transport.desc";
  */
  static final String CHANNEL_DESC_PATTERN = "Channel {0} (from registry)";
  static final String PACKAGING_NAME_PATTERN = "{0}.PKG";
  static final String PACKAGING_DESC_PATTERN = "Packaging Profile of {0} (from registry)";
  static final String SECURITY_NAME_PATTERN = "{0}.SEC";
  static final String SECURITY_DESC_PATTERN = "Security Profile of {0} (from registry)";
  static final String TRANSPORT_NAME_PATTERN = "{0}.COMM";
  static final String TRANSPORT_DESC_PATTERN = "Communication Profile of {0} (from registry)";
 
  static final String MSG_SERVICE_NAME_PATTERN = "{0}.MSG_SERVICE";
  static final String MSG_SERVICE_DESC_PATTERN = "Messaging service of Business entity {0}";
  static final String MSG_SERVICE_PROPRIETARY_OBJ_TYPE = "MessagingService";
}
