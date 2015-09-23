/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBusinessIdentifierKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

/**
 * This interface defines the keys of the identifiers in the 
 * BusinessIdentifier namespace.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IBusinessIdentifierKeys
{
  static final String NAMESPACE_TYPE = "BusinessIdentifiers";
  
  static final String ENTERPRISE_ID = "Enterprise Id";
  static final String BUS_ENTITY_ID = "Business Entity Id";
  static final String GLOBAL_SC_CODE = "Global Supply Chain Code";
  static final String PO_BOX = "P.O. Box";
  static final String LANGUAGE = "Language";
  static final String WEBSITE = "Website";

  // use with DUNS scheme
  static final String DUNS_NUMBER = "D-U-N-S number";
  
  static final String BE_DESC_PATTERN = "{0} (from registry)";
  
}
