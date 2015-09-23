/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendExceptionAction_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * March 1 2003     Guo Jianyu              Created
 * Feb  18 2005     Mahesh                  Modified Constructor to pass
 *                                          exceptionCode and exceptionStr
 */
package com.gridnode.gtas.server.rnif.act;

import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.pdip.framework.util.TimeUtil;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.Date;

public class SendExceptionAction_11 extends SendSignalAction_11
{
  private String _exception;
  private String _exceptionCode;

  public SendExceptionAction_11(String exceptionCode, String exceptionStr)
  {
    super();
    _exceptionCode= exceptionCode;
    _exception= exceptionStr;
  }
  
  String getActualClassName()
  {
    return "SendExceptionAction_11";
  }

  String getBaseDocName(String originalFileName)
  {
    // set a unique filename
    return "Except_" + originalFileName;
  }

  String getGlobalSignalCode()
  {
    return "General Exception";
  }

  String getProcessMsgType()
  {
    return IRnifConstant.EXCEPTION_MESSAGE_TYPE_1;
  }

  String getUDocDocType()
  {
    return IRnifConstant.RN1_EXCEPTION;
  }

  void setExtraParam(Object param)
  {
    _exception= (String) ((Object[]) param)[0];
    _exceptionCode= (String) ((Object[]) param)[1];
  }

  Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException
  {

    String value;
    Integer i;
    DateFormat df = new SimpleDateFormat( "yyyyMMdd'T'HHmmss.SSS'Z'");
    df.setTimeZone( new SimpleTimeZone( 0, IRNConstant_11.ROSETTANET_TIMEZONE));

    Element root = new Element("Exception");
    Element efromRole = writeFromRole(pAct, originalGDoc, def);
    root.addContent(efromRole);

    Element eReason = new Element("reason");
    Element eFreeTxt = new Element("FreeFormText");
    if(_exception != null)
    {
      StringBuffer err = new StringBuffer();
      err.append("GlobalMessageExceptionCode : " + _exceptionCode+"\n");
      err.append(_exception);
      eFreeTxt.addContent(err.toString());
    }
    eReason.addContent(eFreeTxt);
    root.addContent(eReason);

    Date now = TimeUtil.localToUtcTimestamp();
    if (null != now)
    {
      Element etheMsgDateTime = new Element("theMessageDatetime");
      Element edatetime = new Element("DateTimeStamp");
      edatetime.addContent(df.format(now));
      etheMsgDateTime.addContent(edatetime);
      root.addContent(etheMsgDateTime);
    }
    else
    {
      throw RnifException.ackReceiptEx("exp:theMessageDatetime");
    }

    Date OffDocTime = originalGDoc.getDateTimeReceiveEnd();
    if (null != OffDocTime)
    {
      Element eOffendDocDateTime = new Element("theOffendingDocumentDateTime");
      Element edatetime = new Element("DateTimeStamp");
      edatetime.addContent(df.format(now));
      eOffendDocDateTime.addContent(edatetime);
      root.addContent(eOffendDocDateTime);
    }

    Element eOffendDocID = new Element("theOffendingDocumentIdentifier");
    Element edocId = new Element("ProprietaryDocumentIdentifier");
    RNProfile profile = new ProfileUtil().getProfileMustExist(originalGDoc);
    edocId.addContent(profile.getUniqueValue());
    eOffendDocID.addContent(edocId);
    root.addContent(eOffendDocID);

    Element ethisMsgID = new Element("thisMessageIdentifier");
    Element emsgId = new Element("ProprietaryMessageIdentifier");
    emsgId.addContent(DocumentUtil.getProprietaryDocIdByDateTime());
    ethisMsgID.addContent(emsgId);
    root.addContent(ethisMsgID);

    Element etoRole = writeToRole(pAct, originalGDoc, def);
    root.addContent(etoRole);

    DocType docType = new DocType("Exception", IRnifConstant.EXCEPTION_MESSAGE_TYPE_1);
    Document doc = new Document(root, docType);

    return doc;

  }

}