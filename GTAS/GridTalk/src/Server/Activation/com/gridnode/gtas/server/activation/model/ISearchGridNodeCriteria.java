/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchGridNodeCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

/**
 * This interface defines the field IDs and constants for the
 * SearchGridNodeCriteria entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface ISearchGridNodeCriteria
{
  public static final String ENTITY_NAME  = "SearchGridNodeCriteria";

  /**
   * FieldID for CriteriaType. A Short.
   */
  public static final Number CRITERIA     = new Integer(1);

  /**
   * FieldID for MatchType. A Short.
   */
  public static final Number MATCH        = new Integer(2);

  /**
   * FieldID for GridNodeID. An Integer.
   */
  public static final Number GRIDNODE_ID  = new Integer(3);

  /**
   * FieldID for GridNodeName. A String.
   */
  public static final Number GRIDNODE_NAME= new Integer(4); //String(80)

  /**
   * FieldID for Category. A String.
   */
  public static final Number CATEGORY     = new Integer(5); //String(3)

  /**
   * FieldId for BusDesc. A String.
   */
  public static final Number BUSINESS_DESC= new Integer(6);  //string(80)

  /**
   * FieldId for DUNS. A String.
   */
  public static final Number DUNS        = new Integer(7);  //string(14)

  /**
   * FieldId for ContactPerson. A String.
   */
  public static final Number CONTACT_PERSON= new Integer(8);  //string(80)

  /**
   * FieldId for Email. A String.
   */
  public static final Number EMAIL      = new Integer(9);  //string(50)

  /**
   * FieldId for Tel. A String.
   */
  public static final Number TEL        = new Integer(10);  //string(16)

  /**
   * FieldId for Fax. A String.
   */
  public static final Number FAX        = new Integer(11);  //string(16)

  /**
   * FieldId for Website. A String.
   */
  public static final Number WEBSITE    = new Integer(12);  //string(80)

  /**
   * FieldId for Country. A String of 3-char code.
   */
  public static final Number COUNTRY    = new Integer(13); //string(3)


  // Values for CRITERIA
  /**
   * Possible value for CRITERIA. This indicates no criteria is specified. Search
   * all.
   */
  public static final short CRITERIA_NONE         = 1;

  /**
   * Possible value for CRITERIA. This indicates to search by GridNode information.
   */
  public static final short CRITERIA_BY_GRIDNODE  = 2;

  /**
   * Possible value for CRITERIA. This indicates to search by Whitepage information.
   */
  public static final short CRITERIA_BY_WHITEPAGE = 3;

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