/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchGridNodeKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.server.activation.model.ISearchGridNodeCriteria;

/**
 * This interface contains the additional constants required by the Search
 * GridNode handler.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface ISearchGridNodeKeys
  extends        IActivationProcessKeys
{
  /**
   * Prefix for the Search key in the configuration file.
   */
  public static final String SEARCH_KEY_PREFIX  = "SearchKey";

  /**
   * The search keys for GridNode information.
   */
  public static final Number[] GRIDNODE_KEYS = new Number[]
  {
    ISearchGridNodeCriteria.GRIDNODE_ID,
    ISearchGridNodeCriteria.GRIDNODE_NAME,
    ISearchGridNodeCriteria.CATEGORY,
  };

  /**
   * the search keys for Whitepage information.
   */
  public static final Number[] WHITEPAGE_KEYS = new Number[]
  {
    ISearchGridNodeCriteria.BUSINESS_DESC,
    ISearchGridNodeCriteria.CONTACT_PERSON,
    ISearchGridNodeCriteria.COUNTRY,
    ISearchGridNodeCriteria.DUNS,
    ISearchGridNodeCriteria.EMAIL,
    ISearchGridNodeCriteria.FAX,
    ISearchGridNodeCriteria.TEL,
    ISearchGridNodeCriteria.WEBSITE,
  };

  /**
   * SearchBy type for GridNode.
   */
  public static final String TYPE_GRIDNODE  = "GridNode";

  /**
   * SearchBy type for WhitePage.
   */
  public static final String TYPE_WHITEPAGE = "WhitePage";

  /**
   * RefName for obtaining new SearchID for a new Search query.
   */
  public static final String GN_SEARCH_KEY_ID = "SearchGridNodeQuery";

}