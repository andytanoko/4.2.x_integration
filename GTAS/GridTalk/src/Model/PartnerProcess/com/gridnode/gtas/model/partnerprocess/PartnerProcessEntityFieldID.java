/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerProcessEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Nov 21 2002    Neo Sok Lay         Renamed from TriggerEntityFieldID.
 *                                    Add fieldID for ProcessMapping.
 * Dec 16 2002    Daniel D'Cotta      Added triggerType and isRequest
 * Jan 10 2003    Neo Sok Lay         Add fieldID for BizCertMapping.
 * Oct 20 2003    Guo Jianyu          Add fieldID for NumOfRetries, RetryInterval
 *                                    and ChannelUID
 * Feb 06 2007    Chong SoonFui       Commented ITrigger.CAN_DELETE,IProcessMapping.CAN_DELETE
 */
package com.gridnode.gtas.model.partnerprocess;

import com.gridnode.gtas.model.certificate.CertificateEntityFieldID;
import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the PartnerProcess module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public class PartnerProcessEntityFieldID
{
  private Hashtable _table;
  private static PartnerProcessEntityFieldID _self = null;

  private PartnerProcessEntityFieldID()
  {
    _table = new Hashtable();

    // Trigger
    _table.put(ITrigger.ENTITY_NAME,
      new Number[]
      {
//        ITrigger.CAN_DELETE,
        ITrigger.DOC_TYPE,
        ITrigger.PARTNER_FUNCTION_ID,
        ITrigger.PARTNER_GROUP,
        ITrigger.PARTNER_ID,
        ITrigger.PARTNER_TYPE,
        ITrigger.PROCESS_ID,
        ITrigger.IS_LOCAL_PENDING,
        ITrigger.NUM_OF_RETRIES,
        ITrigger.RETRY_INTERVAL,
        ITrigger.CHANNEL_UID,
        ITrigger.IS_REQUEST,
        ITrigger.TRIGGER_LEVEL,
        ITrigger.TRIGGER_TYPE,
        ITrigger.UID
      });

    // Process Mapping
    _table.put(IProcessMapping.ENTITY_NAME,
      new Number[]
      {
//        IProcessMapping.CAN_DELETE,
        IProcessMapping.DOC_TYPE,
        IProcessMapping.IS_INITIATING_ROLE,
        IProcessMapping.PARTNER_ID,
        IProcessMapping.PROCESS_DEF,
        IProcessMapping.SEND_CHANNEL_UID,
        IProcessMapping.UID
      });

    // Process Mapping
    _table.put(IBizCertMapping.ENTITY_NAME,
      new Number[]
      {
//        IBizCertMapping.CAN_DELETE,
        IBizCertMapping.OWN_CERT,
        IBizCertMapping.PARTNER_CERT,
        IBizCertMapping.PARTNER_ID,
        IBizCertMapping.UID
      });

    // Certificate references
    _table.putAll(CertificateEntityFieldID.getEntityFieldID());
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new PartnerProcessEntityFieldID();
    }
    return _self._table;
  }
}