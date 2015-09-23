/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXpathMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.model;

/**
 * This interface defines the XPaths used in the XPath file.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IXpathMappingFile
{
  /**
   * XPath for all the properties
   */
  public static final String PROPERTY_XPATH = "Init/Property";

  /**
   * XPath for document type
   */
  public static final String DOC_TYPE_XPATH = "Init/Property[@key='DocType']";

  /**
   * XPath for partner type
   */
  public static final String PARTNER_TYPE_XPATH = "Init/Property[@key='PartnerType']";

  /**
   * XPath for partnerId
   */
  public static final String PARTNER_ID_XPATH = "Init/Property[@key='PartnerId']";

  /**
   * XPath for document number
   */
  public static final String DOC_NUM_XPATH = "Init/Property[@key='DocNum']";

}