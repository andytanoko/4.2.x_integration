/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTDomainIdentifierEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-02     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.bizreg.IDomainIdentifier;

public interface IGTDomainIdentifierEntity extends IGTEntity
{
  // Fields
  public static final Number UID                          = IDomainIdentifier.UID;
  public static final Number TYPE                         = IDomainIdentifier.TYPE;
  public static final Number VALUE                        = IDomainIdentifier.VALUE;

  /**
   * Values for TYPE field
   */
  public static final String TYPE_DUNS_NUMBER             = "DunsNumber";
  public static final String TYPE_AS2_IDENTIFIER          = "As2Identifier";
  public static final String TYPE_DISCOVERY_URL           = "DiscoveryUrl";
  public static final String TYPE_GLOBAL_LOCATION_NUMBER  = "GlobalLocationNumber";
}