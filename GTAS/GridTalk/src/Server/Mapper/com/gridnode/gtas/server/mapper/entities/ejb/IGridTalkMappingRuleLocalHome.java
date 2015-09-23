/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkMappingRuleLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for GridTalkMappingRuleBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IGridTalkMappingRuleLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new GridTalkMappingRule
   *
   * @param gridTalkMappingRule The GridTalkMappingRule entity.
   * @return EJBLocalObject as a proxy to GridTalkMappingRuleBean for the
   *         created GridTalkMappingRule.
   */
  public IGridTalkMappingRuleLocalObj create(IEntity gridTalkMappingRule)
    throws CreateException;

  /**
   * Load a GridTalkMappingRuleBean
   *
   * @param primaryKey The primary key to the GridTalkMappingRule record
   * @return EJBLocalObject as a proxy to the loaded GridTalkMappingRuleBean.
   */
  public IGridTalkMappingRuleLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find GridTalkMappingRule records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IUserLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}