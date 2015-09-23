/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePackagingInfoEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh               Created
 * Nov 03 2003    Guo Jianyu              Added "IAS2PackagingInfoExtension"
 * 2006-01-19			SC											Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 * Feb 06 2007    Chong SoonFui           Commented IPackagingInfo.CAN_DELETE
 */


package com.gridnode.gtas.model.channel;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the PackagingInfo module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public class PackagingInfoEntityFieldId
{
  private Hashtable _table;
  private static PackagingInfoEntityFieldId _self = null;

  public PackagingInfoEntityFieldId()
  {
    _table = new Hashtable();

    _table.put(IPackagingInfo.ENTITY_NAME,
      new Number[]
      {
        IPackagingInfo.NAME,
        IPackagingInfo.DESCRIPTION,
        IPackagingInfo.ENVELOPE,
//        IPackagingInfo.ZIP,
//        IPackagingInfo.ZIPTHRESHOLD,
        IPackagingInfo.UID,
//        IPackagingInfo.CAN_DELETE,
        IPackagingInfo.REF_ID,
        IPackagingInfo.PARTNER_CAT,
        IPackagingInfo.IS_DISABLE,
        IPackagingInfo.IS_PARTNER,
        IPackagingInfo.PKG_INFO_EXTENSION
      });

    _table.put(IAS2PackagingInfoExtension.ENTITY_NAME,
      new Number[]
      {
        IAS2PackagingInfoExtension.IS_ACK_REQ,
        IAS2PackagingInfoExtension.IS_ACK_SIGNED,
        IAS2PackagingInfoExtension.IS_NRR_REQ,
        IAS2PackagingInfoExtension.IS_ACK_SYN,
        IAS2PackagingInfoExtension.RETURN_URL
      }
    );
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PackagingInfoEntityFieldId();
    }
    return _self._table;
  }



}

