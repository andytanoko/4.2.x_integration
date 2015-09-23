/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizRegEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Add PartnerCategory field.
 * Dec 23 2003    Neo Sok Lay         Add DomainIdentifier fieldIds.
 * Feb 06 2007    Chong SoonFui       Commented IDomainIdentifier.CAN_DELETE
 */
package com.gridnode.gtas.model.bizreg;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the BusinessRegistry module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class BizRegEntityFieldID
{
  private Hashtable _table;
  private static final BizRegEntityFieldID _self = new BizRegEntityFieldID();

  private BizRegEntityFieldID()
  {
    _table = new Hashtable();

    //BusinessEntity
    _table.put(IBusinessEntity.ENTITY_NAME,
      new Number[]
      {
        IBusinessEntity.CAN_DELETE,
        IBusinessEntity.DESCRIPTION,
        IBusinessEntity.ENTERPRISE_ID,
        IBusinessEntity.ID,
        IBusinessEntity.IS_PARTNER,
        IBusinessEntity.PARTNER_CATEGORY,
        IBusinessEntity.STATE,
        IBusinessEntity.UID,
        IBusinessEntity.WHITE_PAGE,
        IBusinessEntity.DOMAIN_IDENTIFIERS,
      });

    //WhitePage
    _table.put(IWhitePage.ENTITY_NAME,
      new Number[]
      {
        IWhitePage.ADDRESS,
        IWhitePage.BE_UID,
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
        IWhitePage.UID,
        IWhitePage.WEBSITE,
        IWhitePage.ZIP_CODE,
      });
      
    //DomainIdentifier  
    _table.put(IDomainIdentifier.ENTITY_NAME,
      new Number[]
      {
        IDomainIdentifier.TYPE,
        IDomainIdentifier.UID,
        IDomainIdentifier.VALUE,
//        IDomainIdentifier.CAN_DELETE,    
      });
  }

  public static Hashtable getEntityFieldID()
  {
    return _self._table;
  }
}