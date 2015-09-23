/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingRuleLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 01 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for MappingRuleBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMappingRuleLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new MappingRule
   *
   * @param mappingRule The MappingRule entity.
   * @return EJBLocalObject as a proxy to MappingRuleBean for the
   *         created MappingRule.
   */
  public IMappingRuleLocalObj create(IEntity mappingRule)
    throws CreateException;

  /**
   * Load a MappingRuleBean
   *
   * @param primaryKey The primary key to the MappingRule record
   * @return EJBLocalObject as a proxy to the loaded MappingRuleBean.
   */
  public IMappingRuleLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find MappingRule records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IUserLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}