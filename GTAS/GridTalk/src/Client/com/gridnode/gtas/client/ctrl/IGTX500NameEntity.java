/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTX500NameEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.certificate.IX500Name;

/**
 * This entity is a little wierd, in that it is modelled as a virtual entity
 * although its contents come from GTAS in a map...
 */
public interface IGTX500NameEntity extends IGTEntity
{
  public static final Number COUNTRY                = IX500Name.COUNTRY;
  public static final Number STATE                  = IX500Name.STATE;
  public static final Number ORGANIZATION           = IX500Name.ORGANIZATION;
  public static final Number LOCALITY               = IX500Name.LOCALITY;
  public static final Number ORGANIZATIONAL_UNIT    = IX500Name.ORGANIZATIONAL_UNIT;
  public static final Number STREET_ADDRESS         = IX500Name.STREET_ADDRESS;
  public static final Number COMMON_NAME            = IX500Name.COMMAN_NAME;
  public static final Number TITLE                  = IX500Name.TITLE;
  public static final Number EMAIL_ADDRESS          = IX500Name.EMAIL_ADDRESS;
  public static final Number BUSINESS_CATEGORY      = IX500Name.BUSINESS_CATEORY;
  public static final Number TELEPHONE_NUMBER       = IX500Name.TELEPHONE_NUMBER;
  public static final Number POSTAL_CODE            = IX500Name.POSTAL_CODE;
  public static final Number UNKNOWN_ATTRIBUTE_TYPE = IX500Name.UNKOWN_ATTRIBUTE_TYPE;

}