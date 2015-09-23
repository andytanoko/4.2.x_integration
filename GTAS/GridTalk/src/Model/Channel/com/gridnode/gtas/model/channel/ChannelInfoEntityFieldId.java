/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoEntityFieldId.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 * Jul 11 2002    Goh Kan Mun             Modified - Update for CommInfo and ChannelInfo
 * Nov 28 2003    Guo Jianyu              Updated packagingInfo and securityInfo
 * Dec 22 2003    Jagadeesh               Modified - Added FlowControlInfo Embedded Entity.
 * 2006-01-19			SC											Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 * Feb 06 2007    Chong SoonFui           Commented IChannelInfo.CAN_DELETE
 */

package com.gridnode.gtas.model.channel;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the ChannelInfo module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class ChannelInfoEntityFieldId
{

  private Hashtable _table;
  private static ChannelInfoEntityFieldId _self = null;

  public ChannelInfoEntityFieldId()
  {
    _table = new Hashtable();

    _table.put(IChannelInfo.ENTITY_NAME,
      new Number[]
      {
        IChannelInfo.DESCRIPTION,
        IChannelInfo.NAME,
        IChannelInfo.REF_ID,
        IChannelInfo.TPT_COMM_INFO,
        IChannelInfo.TPT_PROTOCOL_TYPE,
        IChannelInfo.UID,
        IChannelInfo.IS_MASTER,
        IChannelInfo.IS_PARTNER,
        IChannelInfo.PACKAGING_PROFILE,
        IChannelInfo.SECURITY_PROFILE,
//        IChannelInfo.CAN_DELETE,
        IChannelInfo.PARTNER_CAT,
        IChannelInfo.IS_DISABLE,
        IChannelInfo.FLOWCONTROL_PROFILE,
      });

    _table.put(ICommInfo.ENTITY_NAME,
      new Number[]
      {
        ICommInfo.DESCRIPTION,
     //   ICommInfo.HOST,
        ICommInfo.NAME,
      //  ICommInfo.PORT,
        ICommInfo.REF_ID,
     //   ICommInfo.PROTOCOL_DETAIL,
        ICommInfo.TPT_IMPL_VERSION,
        ICommInfo.PROTOCOL_TYPE,
      //  ICommInfo.PROTOCOL_VERSION,
        ICommInfo.UID,
        ICommInfo.PARTNER_CAT,
        ICommInfo.IS_DISABLE,
        ICommInfo.IS_PARTNER,
      });

    _table.put(IPackagingInfo.ENTITY_NAME,
      new Number[]
      {
        IPackagingInfo.DESCRIPTION,
        IPackagingInfo.ENVELOPE,
        IPackagingInfo.NAME,
//        IPackagingInfo.ZIP,
//        IPackagingInfo.ZIPTHRESHOLD,
        IPackagingInfo.UID,
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
        ISecurityInfo.UID,
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

    _table.put(IFlowControlInfo.ENTITY_NAME,
      new Number[]
      {
        IFlowControlInfo.IS_ZIP,
        IFlowControlInfo.ZIP_THRESHOLD,
        IFlowControlInfo.IS_SPLIT,
        IFlowControlInfo.SPLIT_THRESHOLD,
        IFlowControlInfo.SPLIT_SIZE
      });

  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ChannelInfoEntityFieldId();
    }
    return _self._table;
  }

}

