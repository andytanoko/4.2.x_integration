/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created.
 */
package com.gridnode.gtas.model.enterprise;

import com.gridnode.gtas.model.bizreg.IWhitePage;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.model.channel.ISecurityInfo;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Enterprise module for
 * Registry Search function.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SearchEntityFieldID
{
  private Hashtable _table;
  private static final SearchEntityFieldID _self = new SearchEntityFieldID();

  private SearchEntityFieldID()
  {
    _table = new Hashtable();

    //SearchRegistryCriteria
    _table.put(
      ISearchRegistryCriteria.ENTITY_NAME,
      new Number[] {
        ISearchRegistryCriteria.BUS_ENTITY_DESC,
        ISearchRegistryCriteria.DUNS,
        ISearchRegistryCriteria.MATCH,
        ISearchRegistryCriteria.MESSAGING_STANDARDS,
        ISearchRegistryCriteria.QUERY_URL,
        });

    //SearchRegistryQuery
    _table.put(
      ISearchRegistryQuery.ENTITY_NAME,
      new Number[] {
        ISearchRegistryQuery.CRITERIA,
        ISearchRegistryQuery.DT_RESPONDED,
        ISearchRegistryQuery.DT_SUBMITTED,
        ISearchRegistryQuery.RESULTS,
        ISearchRegistryQuery.SEARCH_ID,
        ISearchRegistryQuery.IS_EXCEPTION,
        ISearchRegistryQuery.EXCEPTION_STR,
        });

    //SearchedBusinessEntity
    _table.put(
      ISearchedBusinessEntity.ENTITY_NAME,
      new Number[] {
        ISearchedBusinessEntity.CHANNELS,
        ISearchedBusinessEntity.DESCRIPTION,
        ISearchedBusinessEntity.ENTERPRISE_ID,
        ISearchedBusinessEntity.ID,
        ISearchedBusinessEntity.STATE,
        ISearchedBusinessEntity.UUID,
        ISearchedBusinessEntity.WHITE_PAGE,
        });

    //WhitePage
    _table.put(IWhitePage.ENTITY_NAME,
      new Number[]
      {
        IWhitePage.ADDRESS,
        IWhitePage.BUSINESS_DESC,
        IWhitePage.CITY,
        IWhitePage.CONTACT_PERSON,
        IWhitePage.COUNTRY,
        IWhitePage.DUNS,
        IWhitePage.EMAIL,
        IWhitePage.FAX,
        IWhitePage.G_SUPPLY_CHAIN_CODE,
        IWhitePage.LANGUAGE,
        IWhitePage.PO_BOX,
        IWhitePage.STATE,
        IWhitePage.TEL,
        IWhitePage.WEBSITE,
        IWhitePage.ZIP_CODE,
      });

    //ChannelInfo
    _table.put(IChannelInfo.ENTITY_NAME,
      new Number[]
      {
        IChannelInfo.DESCRIPTION,
        IChannelInfo.NAME,
        IChannelInfo.REF_ID,
        IChannelInfo.TPT_COMM_INFO,
        IChannelInfo.TPT_PROTOCOL_TYPE,
        IChannelInfo.PACKAGING_PROFILE,
        IChannelInfo.SECURITY_PROFILE,
      });

    //CommInfo
    _table.put(ICommInfo.ENTITY_NAME,
      new Number[]
      {
        ICommInfo.DESCRIPTION,
        ICommInfo.NAME,
        ICommInfo.REF_ID,
        ICommInfo.PROTOCOL_TYPE,
        ICommInfo.URL,
      });

    //PackagingInfo
    _table.put(IPackagingInfo.ENTITY_NAME,
      new Number[]
      {
        IPackagingInfo.DESCRIPTION,
        IPackagingInfo.ENVELOPE,
        IPackagingInfo.NAME,
        IPackagingInfo.REF_ID,
      });

    //SecurityInfo
    _table.put(ISecurityInfo.ENTITY_NAME,
      new Number[]
      {
        ISecurityInfo.DESCRIPTION,
        ISecurityInfo.DIGEST_ALGORITHM,
        ISecurityInfo.ENCRYPTION_CERTIFICATE_ID,
        ISecurityInfo.ENCRYPTION_LEVEL,
        ISecurityInfo.ENCRYPTION_TYPE,
        ISecurityInfo.NAME,
        ISecurityInfo.SIGNATURE_ENCRYPTION_CERTIFICATE_ID,
        ISecurityInfo.SIGNATURE_TYPE,
        ISecurityInfo.REF_ID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    return _self._table;
  }
}