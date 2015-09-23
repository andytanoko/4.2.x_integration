/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchRegistryCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 09 2003    Neo Sok Lay         Add DUNS field.
 */
package com.gridnode.pdip.app.bizreg.search.model;

/**
 * This interface defines the field IDs and constants for the
 * SearchRegistryCriteria entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public interface ISearchRegistryCriteria
{
  public static final String ENTITY_NAME  = "SearchRegistryCriteria";

  /**
   * FieldId for BusEntityDesc. A String.
   */
  public static final Number BUS_ENTITY_DESC= new Integer(1); 

  /**
   * FieldId for MessagingStandards. A Collection of String.
   */
  public static final Number MESSAGING_STANDARDS = new Integer(2); 

  /**
   * FieldId for DUNS. A String.
   */
  public static final Number DUNS        = new Integer(3); 

  /**
   * FieldId for QueryUrl. A String.
   */
  public static final Number QUERY_URL = new Integer(4);
  
  /**
   * FieldId for Match. A Short.
   */
  public static final Number MATCH = new Integer(5);
   
  // Values for MATCH
  /**
   * Possible value for MATCH. This indicates to perform all criteria match
   * search.
   */
  public static final short MATCH_ALL         = 1;

  /**
   * Possible value for MATCH. This indicates to perform any criteria match
   * search.
   */
  public static final short MATCH_ANY         = 2;

}