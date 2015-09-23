/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendAckAction_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * March 1 2003     Guo Jianyu              Created
 * Jan 19 2006			SC											Change from G_DIGEST_ALGO_CODE to DIGEST_ALGORITHM
 * Apr 18 2006      Neo Sok Lay             GNDB00026940: Digest is null returned from IDigestGenerator.
 *                                          Change to get from the original message's rnprofile.
 * Aug 01 2009      Tam Wei Xiang           #560: change writeInfo(...), convert to RN algo format
 */
package com.gridnode.gtas.server.rnif.act;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.framework.util.TimeUtil;

public class SendAckAction_11 extends SendSignalAction_11
{
  //static IDigestGenerator  _digestGenerator = new DigestGenerator();

  public SendAckAction_11()
  {
  }

  String getActualClassName()
  {
    return "SendAckAction_11";
  }

  String getBaseDocName(String originalFileName)
  {
    // set a unique filename
    return  "Ack_" + originalFileName;
  }

  String getGlobalSignalCode()
  {
    return "Receipt Acknowledge";
  }

  String getProcessMsgType()
  {
    return IRnifConstant.ACK_MESSAGE_TYPE_1;
  }

  String getUDocDocType()
  {
    return IRnifConstant.RN1_ACK;
  }

  Document writeInfo(GridDocument  originalGDoc, ProcessDef def,
                  ProcessAct pAct ) throws RnifException
  {
    //String value;

    DateFormat df = new SimpleDateFormat( "yyyyMMdd'T'HHmmss.SSS'Z'");
    df.setTimeZone(new SimpleTimeZone( 0, IRNConstant_11.ROSETTANET_TIMEZONE));

    Element root = new Element("ReceiptAcknowledgement");
    Element efromRole = writeFromRole(pAct, originalGDoc, def);
    root.addContent(efromRole);

    if(Boolean.TRUE.equals(pAct.getIsNonRepudiationRequired() ))
    {
    	/*NSL20060418 decommission the old implementation
      File uDocFile = null;

      try
      {
        uDocFile = DocumentUtil.getUDoc(originalGDoc);
      }
      catch(Exception ex)
      {
        throw RnifException.fileProcessErr("Cannot can udoc file for GridDocument "+ originalGDoc.getEntityDescr(), ex);
      }*/

      
//      String digestAlg = pAct.getGDigestAlgCode();
      String digestAlg = SMimeFactory.getExternalDigestAlgo(pAct.getDigestAlgorithm()); //TWX 20090801 convert to RN algo format
      
      /*NSL20060418 This always returns null!!!!
      String digest = _digestGenerator.getEncodedDigest(
                            _digestGenerator.getDigest(uDocFile, digestAlg));
      */
      RNProfile oldProfile= new ProfileUtil().getProfileMustExist(originalGDoc);
      String digest = oldProfile.getMsgDigest();

      Element nonrepudInfo = new Element( "NonRepudiationInformation");
      Element eDigestAlgo = new Element( "GlobalDigestAlgorithmCode");
      eDigestAlgo.addContent(digestAlg);
      nonrepudInfo.addContent(eDigestAlgo);

      Element originalMsgDigest = new Element( "OriginalMessageDigest");
      originalMsgDigest.addContent(digest);
      nonrepudInfo.addContent(originalMsgDigest);
      root.addContent(nonrepudInfo);
    }

    Date RevDocTime = originalGDoc.getDateTimeReceiveEnd();

    if (null != RevDocTime) {
      Element eReceivedDocDateTime = new Element("receivedDocumentDateTime");
      Element edatetime = new Element("DateTimeStamp");
      edatetime.addContent(df.format(RevDocTime));
      eReceivedDocDateTime.addContent(edatetime);
      root.addContent(eReceivedDocDateTime);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:receivedDocumentDateTime");
    }

    Element eReceivedDocID = new Element("receivedDocumentIdentifier");
    Element edocId = new Element("ProprietaryDocumentIdentifier");

    RNProfile profile= new ProfileUtil().getProfileMustExist(originalGDoc);
    edocId.addContent(profile.getUniqueValue());
    eReceivedDocID.addContent(edocId);
    root.addContent(eReceivedDocID);

    Date now = TimeUtil.localToUtcTimestamp();
    if (null != now)
    {
      Element ethisMsgDateTime = new Element("thisMessageDateTime");
      Element edatetime = new Element("DateTimeStamp");
      edatetime.addContent(df.format(now));
      ethisMsgDateTime.addContent(edatetime);
      root.addContent(ethisMsgDateTime);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:receivedDocumentDateTime");
    }

    Element ethisMsgID = new Element("thisMessageIdentifier");
    edocId = new Element("ProprietaryMessageIdentifier");
    edocId.addContent(DocumentUtil.getProprietaryDocIdByDateTime());
    ethisMsgID.addContent(edocId);
    root.addContent(ethisMsgID);

    Element etoRole = writeToRole(pAct, originalGDoc, def);
    root.addContent(etoRole);

    DocType docType = new DocType("ReceiptAcknowledgement",IRnifConstant.ACK_MESSAGE_TYPE_1);
    Document doc = new Document(root, docType);

    return doc;
  }



}