/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDomainIdentifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23, 2003   Neo Sok Lay         Created
 * 6 Dec 2005     SC									Add DOMAIN_NAME and TYPE for star fish no.
 * 16 Dec 2005		SC									Change name: star fish number to starfish id.
 */
package com.gridnode.gtas.model.bizreg;

/**
 * This interface defines the fieldIds for the fields in
 * the DomainIdentifier entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public interface IDomainIdentifier
{
  public static final String ENTITY_NAME = "DomainIdentifier";
  
  public static final Number UID = new Integer(0); //Long
  
  public static final Number DOMAIN_NAME = new Integer(1); //String
  
  public static final Number TYPE = new Integer(2); //String
  
  public static final Number VALUE = new Integer(3); //String
  
  public static final Number BUSINESS_ENTITY_UID = new Integer(4); //Long
  
  public static final Number CAN_DELETE = new Integer(5);
  
  // Possible values for DOMAIN_NAME
  public static final String DOMAIN_DUNS = "DUNS";
  public static final String DOMAIN_AS2 = "AS2";
  public static final String DOMAIN_UDDI = "UDDI";
  public static final String DOMAIN_UCCNET = "UCCnet";
  public static final String DOMAIN_STARFISH = "Starfish";
  
  // Possible values for TYPE
  public static final String TYPE_DUNS_NUMBER = "DunsNumber";
  public static final String TYPE_AS2_IDENTIFIER = "As2Identifier";
  public static final String TYPE_DISCOVERY_URL = "DiscoveryUrl";
  public static final String TYPE_GLOBAL_LOCATION_NUMBER = "GlobalLocationNumber";
  public static final String TYPE_STARFISH_ID = "StarfishId";
   
}
