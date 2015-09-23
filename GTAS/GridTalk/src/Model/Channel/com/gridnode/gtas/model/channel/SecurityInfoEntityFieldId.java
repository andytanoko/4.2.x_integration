/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh               Created
 * Nov 23 2003    Guo Jianyu              Added support for compression
 * Nov 26 2003    Guo Jianyu              Added encryptionALgorithm
 * Feb 06 2007    Chong SoonFui           Commented ISecurityInfo.CAN_DELETE
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


public class SecurityInfoEntityFieldId
{
  private Hashtable _table;
  private static SecurityInfoEntityFieldId _self = null;

  public SecurityInfoEntityFieldId()
  {
     _table = new Hashtable();

    _table.put(ISecurityInfo.ENTITY_NAME,
      new Number[]
      {
        ISecurityInfo.NAME,
        ISecurityInfo.DESCRIPTION,
        ISecurityInfo.DIGEST_ALGORITHM,
        ISecurityInfo.ENCRYPTION_CERTIFICATE_ID,
        ISecurityInfo.ENCRYPTION_LEVEL,
        ISecurityInfo.ENCRYPTION_TYPE,
        ISecurityInfo.SIGNATURE_ENCRYPTION_CERTIFICATE_ID,
        ISecurityInfo.SIGNATURE_TYPE,
        ISecurityInfo.UID,
//        ISecurityInfo.CAN_DELETE,
        ISecurityInfo.REF_ID,
        ISecurityInfo.PARTNER_CAT,
        ISecurityInfo.IS_DISABLE,
        ISecurityInfo.IS_PARTNER,
        ISecurityInfo.COMPRESSION_TYPE,
        ISecurityInfo.COMPRESSION_METHOD,
        ISecurityInfo.COMPRESSION_LEVEL,
        ISecurityInfo.SEQUENCE,
        ISecurityInfo.ENCRYPTION_ALGORITHM
      });
 }

   public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new SecurityInfoEntityFieldId();
    }
    return _self._table;
  }

}

