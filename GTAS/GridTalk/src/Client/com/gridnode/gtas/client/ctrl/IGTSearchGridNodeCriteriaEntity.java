/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchGridNodeCriteriaEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.activation.ISearchGridNodeCriteria;

public interface IGTSearchGridNodeCriteriaEntity extends IGTEntity
{
  //Constants for CRITERIA
  public static final Short CRITERIA_TYPE_NONE         = new Short(ISearchGridNodeCriteria.CRITERIA_NONE);
  public static final Short CRITERIA_TYPE_BY_GRIDNODE  = new Short(ISearchGridNodeCriteria.CRITERIA_BY_GRIDNODE);
  public static final Short CRITERIA_TYPE_BY_WHITEPAGE = new Short(ISearchGridNodeCriteria.CRITERIA_BY_WHITEPAGE);

  // Constants for MATCH
  public static final Short MATCH_TYPE_ALL = new Short(ISearchGridNodeCriteria.MATCH_ALL);
  public static final Short MATCH_TYPE_ANY = new Short(ISearchGridNodeCriteria.MATCH_ANY);

  // Field Ids
  public static final Number CRITERIA_TYPE  = ISearchGridNodeCriteria.CRITERIA;
  public static final Number MATCH_TYPE     = ISearchGridNodeCriteria.MATCH;
  public static final Number GRIDNODE_ID    = ISearchGridNodeCriteria.GRIDNODE_ID;
  public static final Number GRIDNODE_NAME  = ISearchGridNodeCriteria.GRIDNODE_NAME;
  public static final Number CATEGORY       = ISearchGridNodeCriteria.CATEGORY;
  public static final Number BUSINESS_DESC  = ISearchGridNodeCriteria.BUSINESS_DESC;
  public static final Number DUNS           = ISearchGridNodeCriteria.DUNS;
  public static final Number CONTACT_PERSON = ISearchGridNodeCriteria.CONTACT_PERSON;
  public static final Number EMAIL          = ISearchGridNodeCriteria.EMAIL;
  public static final Number TEL            = ISearchGridNodeCriteria.TEL;
  public static final Number FAX            = ISearchGridNodeCriteria.FAX;
  public static final Number WEBSITE        = ISearchGridNodeCriteria.WEBSITE;
  public static final Number COUNTRY        = ISearchGridNodeCriteria.COUNTRY;
}