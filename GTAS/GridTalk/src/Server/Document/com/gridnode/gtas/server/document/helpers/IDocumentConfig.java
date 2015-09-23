/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 08 2004    Jagadeesh           Created
 * Jan 29 2004    Neo Sok Lay         Add P2P_ACK_RNDOCTYPES.
 */

package com.gridnode.gtas.server.document.helpers;

public interface IDocumentConfig
{
  public static final String CONFIG_NAME = "document";

  public static final String BACKWARDCOMPATIBLE_OUTBOUND_MAPPING =
    "backwardcompatible.outbound.mapping";

  public static final String BACKWARDCOMPATIBLE_INBOUND_MAPPING =
    "backwardcompatible.inbound.mapping";

  /**
   * Use for keeping the RN doctypes that will want a TransComplete
   * base on P2P acknowledgement.
   */
  public static final String P2P_ACK_RNDOCTYPES = "p2p.ack.rndoctypes";
}