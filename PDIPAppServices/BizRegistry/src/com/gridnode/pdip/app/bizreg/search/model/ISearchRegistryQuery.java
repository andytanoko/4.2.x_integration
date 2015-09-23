/**
 * This software is the proprietary information of Registry Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) Registry Pte Ltd. All Rights Reserved.
 *
 * File: ISearchRegistryQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.search.model;

/**
 * This interface contains the field IDs for SearchRegistryQuery entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public interface ISearchRegistryQuery
{
  /**
   * Entity name for SearchRegistryQuery.
   */
  public static final String ENTITY_NAME = "SearchRegistryQuery";

  /**
   * FieldID for SearchID. A String.
   */
  public static final Number SEARCH_ID   = new Integer(0);

  /**
   * FieldID for DTSubmitted. A Timestamp.
   */
  public static final Number DT_SUBMITTED= new Integer(1);

  /**
   * FieldID for DTResponded. A Timestamp.
   */
  public static final Number DT_RESPONDED= new Integer(2);

  /**
   * FieldID for SearchCriteria. A SearchRegistryCriteria.
   */
  public static final Number CRITERIA    = new Integer(3);

  /**
   * FieldID for SearchResults. A Collection.
   */
  public static final Number RESULTS     = new Integer(4);
  
  /**
   * FieldID for IsException. A Boolean.
   */
  public static final Number IS_EXCEPTION = new Integer(5);
  
  /**
   * FieldID for ExceptionStr. A String.
   */
  public static final Number EXCEPTION_STR = new Integer(6);

  /**
   * FieldID for RawSearchResults. A Collection.
   */
  public static final Number RAW_RESULTS     = new Integer(7);
  

}