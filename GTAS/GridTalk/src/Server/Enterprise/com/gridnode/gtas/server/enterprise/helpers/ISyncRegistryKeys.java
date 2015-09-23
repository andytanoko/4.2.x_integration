/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISyncRegistryKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 8 2003     Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

/**
 * This interface defines the constants used during registry synchronization.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface ISyncRegistryKeys
{
  /**
   * FieldIds of SearchedBusinessEntity fields to copy to the BusinessEntity.
   */
  static final Number[] SEARCHED_BE_FIELDS = 
    new Number[] {
      SearchedBusinessEntity.DESCRIPTION,
      SearchedBusinessEntity.ENTERPRISE_ID,
      SearchedBusinessEntity.ID,
      SearchedBusinessEntity.WHITE_PAGE,
    };

  /**
   * Corresponsing FieldIds of BusinessEntity fields to be copied from SearchedBusinessEntity. 
   */
  static final Number[] BE_FIELDS = 
    new Number[] {
      BusinessEntity.DESCRIPTION,
      BusinessEntity.ENTERPRISE_ID,
      BusinessEntity.ID,
      BusinessEntity.WHITE_PAGE,
    };

}
