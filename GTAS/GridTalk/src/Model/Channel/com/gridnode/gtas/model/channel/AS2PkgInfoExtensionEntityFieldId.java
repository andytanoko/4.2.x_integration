/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2PkgInfoExtensionEntityFieldId.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Nov 03 2003      Guo Jianyu              Created
 */
package com.gridnode.gtas.model.channel;

import java.util.Hashtable;

public class AS2PkgInfoExtensionEntityFieldId
{
  private Hashtable _table;
  private static AS2PkgInfoExtensionEntityFieldId _self = null;

  private AS2PkgInfoExtensionEntityFieldId()
  {
    _table = new Hashtable();

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
      _self = new AS2PkgInfoExtensionEntityFieldId();
    }
    return _self._table;
  }

}