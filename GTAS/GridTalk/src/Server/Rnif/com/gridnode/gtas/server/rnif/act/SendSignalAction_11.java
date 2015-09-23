/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendSignalAction_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 28 2003      Guo Jianyu              Created
 */
package com.gridnode.gtas.server.rnif.act;

import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.helpers.EnterpriseUtil;

import org.jdom.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.HashMap;

abstract public class SendSignalAction_11 extends SendSignalAction
{

  String getGlobalSignalVersion()
  {
    return "V01.10";
  }

  Element writeFromRole(ProcessAct pAct,  GridDocument  originalGDoc, ProcessDef def )
    throws RnifException
  {

    String value;
    //Integer i;
    String intValue;

    RNProfile profile= new ProfileUtil().getProfileMustExist(originalGDoc);

    DateFormat df = new SimpleDateFormat( "yyyyMMdd'T'HHmmss.SSS'Z'");
    df.setTimeZone( new SimpleTimeZone( 0, IRNConstant_11.ROSETTANET_TIMEZONE));

    Element efromRole = new Element("fromRole");
    Element efromRoleDesc = new Element("PartnerRoleDescription");

    // contact information
    Element contactInfo = new Element( "ContactInformation");

    Logger.debug("Adding contact information for DUNS no. " + profile.getSenderGlobalBusIdentifier());
    HashMap contactHash = getContactInfo( _senderBE);
    if (null == contactHash)
    {
      contactHash = new HashMap();
    }

    // fromRole/PartnerRoleDescription/ContactInformation/contactName/FreeFormText
    Element contactName = new Element( "contactName");
    Element freeFormText = new Element( "FreeFormText");
    if (contactHash.containsKey(NNAME))
    {
      freeFormText.addContent( contactHash.get(NNAME).toString());
    }
    else
    {
      freeFormText.addContent( ".");
    }
    contactName.addContent( freeFormText);
    contactInfo.addContent( contactName);

    // fromRole/PartnerRoleDescription/ContactInformation/EmailAddress
    Element contactEmail = new Element( "EmailAddress");
    if (contactHash.containsKey(NEMAIL))
    {
      contactEmail.addContent( contactHash.get(NEMAIL).toString());
    }
    else
    {
      contactEmail.addContent( ".");
    }
    contactInfo.addContent( contactEmail);

    // fromRole/PartnerRoleDescription/ContactInformation/telephoneNumber/CommunicationsNumber
    Element contactTelephone = new Element( "telephoneNumber");
    Element tnCommsNumber = new Element( "CommunicationsNumber");
    if (contactHash.containsKey(NTEL))
    {
      tnCommsNumber.addContent( contactHash.get( NTEL).toString());
    }
    else
    {
      tnCommsNumber.addContent( ".");
    }
    contactTelephone.addContent( tnCommsNumber);
    contactInfo.addContent( contactTelephone);

    efromRoleDesc.addContent( contactInfo);

    value = def.getGToPartnerRoleClassCode();
    if (null != value)
    {
      Element efromGblPartRoleClassCode = new Element("GlobalPartnerRoleClassificationCode");
      efromGblPartRoleClassCode.addContent(value);
      efromRoleDesc.addContent(efromGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:fromRole.PartnerRoleDescription");
    }

    Element efromPartDesc = new Element("PartnerDescription");
    Element efromBusDesc = new Element("BusinessDescription");
    //i = originalGDoc.getReceiverGlobalBusIdentifier();
    intValue = profile.getReceiverGlobalBusIdentifier();
    if (null != intValue)
    {
      Element eGblBusId = new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(intValue);
      efromBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:fromPartner.GlobalBusinessIdentifier");
    }

    value = EnterpriseUtil.getSupplyChainCode(_senderBE);
    if (null != value)
    {
      Element eGblSupChainCode = new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      efromBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:fromPartner.GlobalSupplyChainCode");
    }
    efromPartDesc.addContent(efromBusDesc);

    value = def.getGToPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode = new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      efromPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:fromPartner.GlobalPartnerClassificationCode");
    }
    efromRoleDesc.addContent(efromPartDesc);
    efromRole.addContent(efromRoleDesc);
    return  efromRole;
  }

  Element writeToRole(ProcessAct pAct,  GridDocument  originalGDoc, ProcessDef def)
    throws RnifException
  {
    String value;
    Integer i;
    String intValue;

    RNProfile profile= new ProfileUtil().getProfileMustExist(originalGDoc);

    Element etoRole = new Element("toRole");
    Element etoRoleDesc = new Element("PartnerRoleDescription");
    value = def.getFromPartnerRoleClassCode();
    if (null != value)
    {
      Element etoGblPartRoleClassCode = new Element("GlobalPartnerRoleClassificationCode");
      etoGblPartRoleClassCode.addContent(value);
      etoRoleDesc.addContent(etoGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:toRole.PartnerRoleDescription");
    }

    Element etoPartDesc = new Element("PartnerDescription");
    Element etoBusDesc = new Element("BusinessDescription");
    //i = originalGDoc.getSenderGlobalBusIdentifier();
    intValue = profile.getSenderGlobalBusIdentifier();
    if (null != intValue)
    {
      Element eGblBusId = new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(intValue);
      etoBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:toPartner.GlobalBusinessIdentifier");
    }

    value = EnterpriseUtil.getSupplyChainCode(_senderBE);
    if (null != value)
    {
      Element eGblSupChainCode = new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      etoBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:toPartner.GlobalSupplyChainCode");
    }
    etoPartDesc.addContent(etoBusDesc);

    value = def.getFromPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode = new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      etoPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.ackReceiptEx("ack:toPartner.GlobalPartnerClassificationCode");
    }
    etoRoleDesc.addContent(etoPartDesc);
    etoRole.addContent(etoRoleDesc);
    return etoRole;
  }
}