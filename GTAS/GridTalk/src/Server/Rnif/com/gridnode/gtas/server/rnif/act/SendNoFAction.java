/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendNoFAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 19, 2006   i00107              To enable sending NoF for received doc
 *                                    in addition to sent doc.
 */

package com.gridnode.gtas.server.rnif.act;

import java.io.File;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.EnterpriseUtil;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.RNDocSender;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

abstract class SendNoFAction extends SendDocumentAction
{
  //NSL20060119
  protected String _reasonStr = RosettaNetException.FN_TIMEOUT.toString(); //default is due to timeout
  protected Boolean _useOriginalRoles = Boolean.TRUE;
  protected String _userid;
  protected String _thisNoFpartner;
  
  @Override
  protected void setExtraParam(Object param, boolean isRequest) throws RnifException
  {
   // _isInitiator = isRequest;
    //NSL20060119
    if (param != null && param instanceof Object[])
    {
      Object[] params = (Object[])param;
      if (params.length >= 1 && params[0] != null) _reasonStr = (String)params[0];
      if (params.length >= 2 && params[1] != null) _useOriginalRoles = (Boolean)params[1];
      if (params.length >= 3 && params[2] != null) _userid = (String)params[2];
    }
  }

  protected void setThisNoFPartner(GridDocument originalGDoc, RNProfile originalProfile)
  {
    if (_useOriginalRoles.booleanValue())
    {
      _thisNoFpartner = originalGDoc.getRecipientPartnerId();
    }
    else
    {
      if (originalProfile.getIsRequestMsg()) //i.e. received request from originator, so now return NoF back to originator
      {
        _thisNoFpartner = originalProfile.getProcessOriginatorId();
      }
      else //i.e. received response from responder, so now return NoF back to responder.
      {
        _thisNoFpartner = originalProfile.getProcessResponderId();
      }
    }
  }
  
  protected void sendDocument(File uDoc, String udocName, GridDocument originalGDoc) throws RnifException
  {
    /*
    String userId= originalGDoc.getSenderUserId();
    String beId =  originalGDoc.getSenderBizEntityId();
    String enterpriseId = originalGDoc.getSenderNodeId().toString();
    */
    //NSL20060119
    String userId = _userid == null ? originalGDoc.getSenderUserId() : _userid;
    String beId =  getFromRole().getBusEntId();
    String enterpriseId = getFromRole().getEnterpriseId();
    String recipientPartnerId = _thisNoFpartner; //NSL20060202
    try
    {
      Logger.debug("userId used in send Nof is :" + userId);
      udocName= FileUtil.create(IPathConfig.PATH_TEMP, userId + "/in/", udocName, uDoc);
      new RNDocSender().importAndSendDoc(
        userId,
        beId,
        enterpriseId,
        udocName,
        getUDocDocType(),
        //originalGDoc.getRecipientPartnerId());
        recipientPartnerId);
    }
    catch (Exception ex)
    {
      throw RnifException.documentSendEx("when import and send Failure Notification document", ex);
    }

  }

  //NSL20060119
  protected BusinessEntity getFromRole()
  {
    return (_useOriginalRoles.booleanValue() ? _senderBE : _recipientBE);
  }

  //NSL20060119
  protected BusinessEntity getToRole()
  {
    return (_useOriginalRoles.booleanValue() ? _recipientBE : _senderBE);
  }
  
  //NSL20060119
  protected String getSenderGlobalBusinessIdentifier(RNProfile profile)
  {
    return (_useOriginalRoles.booleanValue() ? profile.getSenderGlobalBusIdentifier() : profile.getReceiverGlobalBusIdentifier());
  }
  
  //NSL20060119
  protected String getReceiverGlobalBusinessIdentifier(RNProfile profile)
  {
    return (_useOriginalRoles.booleanValue() ? profile.getReceiverGlobalBusIdentifier() : profile.getSenderGlobalBusIdentifier());
  }
  
}
