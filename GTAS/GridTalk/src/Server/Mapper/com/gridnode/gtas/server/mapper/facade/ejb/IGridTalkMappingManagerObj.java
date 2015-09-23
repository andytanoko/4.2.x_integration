/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkMappingManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 04 2002    Koh Han Sing        Created
 * Feb 13 2003    Koh Han Sing        Add in XpathMapping
 * Jun 10 2003    Koh Han Sing        Remove XpathMapping
 * Sep 07 2005    Neo Sok Lay         Change createGridTalkMappingRule() to return Long.
 *                                    Change for J2EE compliance
 */
package com.gridnode.gtas.server.mapper.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.mapper.model.IDocumentMetaInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for MappingManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public interface IGridTalkMappingManagerObj
  extends        EJBObject
{
  /**
   * Create a new GridTalkMappingRule.
   *
   * @param gridTalkMappingRule The GridTalkMappingRule entity.
   * @return the key of the create GridTalkMappingRule.
   */
  public Long createGridTalkMappingRule(GridTalkMappingRule gridTalkMappingRule)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a GridTalkMappingRule
   *
   * @param gridTalkMappingRule The GridTalkMappingRule entity with changes.
   */
  public void updateGridTalkMappingRule(GridTalkMappingRule gridTalkMappingRule)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GridTalkMappingRule.
   *
   * @param gridTalkMappingRuleUId The UID of the GridTalkMappingRule to delete.
   */
  public void deleteGridTalkMappingRule(Long gridTalkMappingRuleUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a GridTalkMappingRule using the GridTalkMappingRule UID.
   *
   * @param mappingFileUId The UID of the GridTalkMappingRule to find.
   * @return The GridTalkMappingRule found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridTalkMappingRule findGridTalkMappingRule(Long gridTalkMappingRuleUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a GridTalkMappingRule using the GridTalkMappingRule Name.
   *
   * @param gridTalkMappingRuleName The Name of the GridTalkMappingRule to find.
   * @return The GridTalkMappingRule found, or <B>null</B> if none exists.
   */
  public GridTalkMappingRule findGridTalkMappingRule(String gridTalkMappingRuleName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridTalkMappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridTalkMappingRule found, or empty collection if none
   * exists.
   */
  public Collection findGridTalkMappingRules(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridTalkMappingRule that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridTalkMappingRule found, or empty collection if
   * none exists.
   */
  public Collection findGridTalkMappingRulesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public Collection execute(Long gtMappingRuleUid, IDocumentMetaInfo gdoc)
    throws Exception, RemoteException;

  public Collection execute(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception, RemoteException;

  public IDocumentMetaInfo transformHeader(GridTalkMappingRule gtMappingRule,
                                           IDocumentMetaInfo gdoc)
                                           throws Exception, RemoteException;

  public Collection transform(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception, RemoteException;

  public Collection convert(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception, RemoteException;

  public Collection split(GridTalkMappingRule gtMappingRule, IDocumentMetaInfo gdoc)
    throws Exception, RemoteException;


}
