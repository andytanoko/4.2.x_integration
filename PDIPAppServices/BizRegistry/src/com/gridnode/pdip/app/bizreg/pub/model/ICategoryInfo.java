/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IIdentifierInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 17 2003    Neo Sok Lay         Add possible Taxonomy values for UDDI Types.
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of a Category information object.
 * A Category information object contains information of a classification
 * for a registry information object. A Category information object can
 * be classified from either a Scheme or Concept information object. 
 * Typical implementation of a Category information object is a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface ICategoryInfo
{
  /**
   * The number of fields in a Category information object.
   */
  static final int NUM_FIELDS = 4;
  
  /**
   * Index to the ConceptKey field of the Category information object.
   */
  static final int FIELD_CONCEPT_KEY = 0;

  /**
   * Index to the SchemeKey field of the Category information object.
   */
  static final int FIELD_SCHEME_KEY = 1;

  /**
   * Index to the Name field of the Category information object.
   */
  static final int FIELD_CATEGORY_NAME = 2;

  /**
   * Index to the Value field of the Category information object.
   */
  static final int FIELD_CATEGORY_VALUE = 3;
  
  /**
   * Taxonomy value of Identifier.
   */  
  static final String TAXONOMY_IDENTIFIER = "identifier";
  
  /**
   * Taxonomy value of Namespace.
   */
  static final String TAXONOMY_NAMESPACE = "namespace";
  
  /**
   * Taxonomy value of Categorization. May be automatically assigned for
   * Schemes.
   */
  static final String TAXONOMY_CATEGORIZATION = "categorization";
  
  /**
   * Taxonomy value of PostalAddress.
   */
  static final String TAXONOMY_POSTALADDRESS = "postalAddress";
  
  /**
   * Taxonomy value of Specification. May be automatically assigned for
   * Concepts.
   */
  static final String TAXONOMY_SPECIFICATION = "specification";
  
  /**
   * Taxonomy value of Specification.
   */
  static final String TAXONOMY_PROTOCOL = "protocol";
  
}
