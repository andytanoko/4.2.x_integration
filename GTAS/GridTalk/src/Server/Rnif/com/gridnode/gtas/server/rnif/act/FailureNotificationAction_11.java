/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FailureNotificationAction_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 28 2003     Guo Jianyu              Created
 * Sep 29 2003     Guo Jianyu              Stopped using IRnifConfig
 * Jan 17 2006      Neo Sok Lay             Change method access modifiers to protected.
 *                                          Get sender/receipient identifications from
 *                                          getters.
 */
package com.gridnode.gtas.server.rnif.act;

import java.util.Date;
import java.util.HashMap;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.*;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * Sends 0A1 for RNIF1.1.
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since 1.0
 */
public class FailureNotificationAction_11 extends FailureNotificationAction
{

  public FailureNotificationAction_11()
  {
  }

  @Override
  protected String getActualClassName()
  {
    return "FailureNotificationAction_11";
  }

  @Override
  protected String getProcessMsgType()
  {
    return IRnifConstant.FAILURE_NOTIFY_MSG_TYPE_1;
  }

  @Override
  protected String getUDocDocType()
  {
    return IRnifConstant.RN1_FAILNOTF;
  }

  @Override
  protected Element writeContactInfo(HashMap info)
  {
    String value ;
    Element  contactInfo = new Element("ContactInformation");

    Element  child4 = new Element("contactName");
    Element  child5 = new Element("FreeFormText");
    value = (String)info.get(NNAME);
    if(value != null)
    {
      child5.addContent(value);
    }
    child4.addContent(child5);
    contactInfo.addContent(child4);

    child4 = new Element("telephoneNumber");
    child5 = new Element("CommunicationsNumber");
    value = (String)info.get(NTEL);
    if(value != null)
    {
      child5.addContent(value);
    }
    child4.addContent(child5);
    contactInfo.addContent(child4);


    child4 = new Element("EmailAddress");
    value = (String)info.get(NEMAIL);
    if(value != null)
    {
      child4.addContent(value);
    }
    contactInfo.addContent(child4);

    return contactInfo;

  }

  @Override
  protected Element writeFromRole(ProcessDef def, ProcessAct pAct, GridDocument originalGDoc, RNProfile profile)
    throws RnifException
  {
    String value;

    HashMap info= getContactInfo(getFromRole()); //NSL20060117
    if (null == info)
    {
      throw RnifException.failureNotificationGenError(
        "Cannot get contact information for BE: " + getSenderGlobalBusinessIdentifier(profile)); //NSL20060117
    }

    Element efromRole = new Element("fromRole");
    Element efromRoleDesc = new Element("PartnerRoleDescription");

    value= def.getFromPartnerRoleClassCode();
    if (null != value)
    {
      Element efromGblPartRoleClassCode = new Element("GlobalPartnerRoleClassificationCode");
      efromGblPartRoleClassCode.addContent(value);
      efromRoleDesc.addContent(efromGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:fromRole.PartnerRoleDescription");
    }

    Element efromPartDesc = new Element("PartnerDescription");

    value= def.getFromPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode = new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      efromPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:fromPartner.GlobalPartnerClassificationCode");
    }

    Element efromBusDesc = new Element("BusinessDescription");
    value= getSenderGlobalBusinessIdentifier(profile); //NSL20060117
    if (null != value)
    {
      Element eGblBusId = new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(value);
      efromBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:fromPartner.GlobalBusinessIdentifier");
    }

    value = EnterpriseUtil.getSupplyChainCode(getFromRole()); //NSL20060117
    if (null != value)
    {
      Element eGblSupChainCode = new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      efromBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:fromPartner.GlobalSupplyChainCode");
    }

    efromPartDesc.addContent(efromBusDesc);

    efromRoleDesc.addContent(efromPartDesc);

    Element ecustomerInfo = writeContactInfo(info);
    efromRoleDesc.addContent(ecustomerInfo);

    efromRole.addContent(efromRoleDesc);
    return  efromRole;
  }

  @Override
  protected Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException
  {
    RNProfile profile= new ProfileUtil().getProfileMustExist(originalGDoc);
    
    setThisNoFPartner(originalGDoc, profile); //NSL20060202 Determine the recipient partner for this NoF
    
    DocType docType = new DocType("Pip0A1FailureNotification",IRnifConstant.FAILURE_NOTIFY_MSG_TYPE_1);

    Element root = new Element("Pip0A1FailureNotification");

    Element child1, child2, child3;


    child1= new Element("reason");
    child2 = new Element("FreeFormText");
    child2.addContent(_reasonStr); //NSL20060117
    child1.addContent(child2);
    root.addContent(child1);

    child1 = new Element( "ProcessIdentity");
    child2 = new Element ("GlobalProcessCode");

//    String pIndicatorCode = profile.getPIPGlobalProcessCode();
//    child2.addContent(config.getString(IRnifConfig.GLOBAL_PROCESS_INDICATOR_CODE + pIndicatorCode));
    child2.addContent(profile.getBusActivityIdentifier());
    child1.addContent(child2);

    child2 = new Element ("VersionIdentifier");
    child2.addContent(profile.getPIPVersionIdentifier());
    child1.addContent(child2);
    child2 = new Element ("GlobalProcessIndicatorCode");
    child2.addContent(profile.getPIPGlobalProcessCode());
    child1.addContent(child2);
    child2 = new Element ("InstanceIdentifier");
    child2.addContent(profile.getPIPInstanceIdentifier());
    child1.addContent(child2);
    child2= new Element("description");
    child3 = new Element("FreeFormText");
    child2.addContent(child3);
    child1.addContent(child2);
    root.addContent(child1);

    child1 = new Element( "TransactionIdentity");
    child2 = new Element ("GlobalTransactionCode");
    child2.addContent(profile.getBusActivityIdentifier() );
    child1.addContent(child2);
    child2 = new Element ("InstanceIdentifier");
    child2.addContent(profile.getProcessTransactionId());
    child1.addContent(child2);
    child2= new Element("description");
    child3 = new Element("FreeFormText");
    child2.addContent(child3);
    child1.addContent(child2);
    root.addContent(child1);

    child1 = new Element( "ActionIdentity");
    child2 = new Element ("GlobalBusinessActionCode");
    child2.addContent(profile.getActionIdentityGlobalBusActionCode() );
    child1.addContent(child2);
    child2 = new Element ("VersionIdentifier");
    child2.addContent(profile.getActionIdentityVersionIdentifier());
    child1.addContent(child2);
    child2 = new Element ("InstanceIdentifier");
    child2.addContent(profile.getProcessActionId());
    child1.addContent(child2);
    child2= new Element("description");
    child3 = new Element("FreeFormText");
    child2.addContent(child3);
    child1.addContent(child2);
    root.addContent(child1);

    root.addContent(writeFromRole(def, pAct, originalGDoc, profile));
    root.addContent(writeToRole(def, pAct, originalGDoc, profile));

    Date time= TimeUtil.localToUtcTimestamp();
    child1= new Element("thisDocumentGenerationDateTime");
    child2 = new Element("DateTimeStamp");
    child2.addContent(sdf.format(time));
    child1.addContent(child2);
    root.addContent(child1);

    child1= new Element("thisDocumentIdentifier");
    child2 = new Element("ProprietaryDocumentIdentifier");
    child2.addContent(DocumentUtil.getProprietaryDocIdByDateTime());
    child1.addContent(child2);
    root.addContent(child1);

    child1= new Element("GlobalDocumentFunctionCode");
    child1.addContent("Request");
    root.addContent(child1);

    Document doc = new Document(root, docType);
    return doc;
  }

  @Override
  protected Element writeToRole(ProcessDef def, ProcessAct pAct, GridDocument originalGDoc, RNProfile profile)
    throws RnifException
  {
    String value;

    Element etoRole = new Element("toRole");
    Element etoRoleDesc = new Element("PartnerRoleDescription");
    value= def.getGToPartnerRoleClassCode();
    if (null != value)
    {
      Element etoGblPartRoleClassCode = new Element("GlobalPartnerRoleClassificationCode");
      etoGblPartRoleClassCode.addContent(value);
      etoRoleDesc.addContent(etoGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toRole.PartnerRoleDescription");
    }

    Element etoPartDesc = new Element("PartnerDescription");

    value= def.getGToPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode = new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      etoPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:toPartner.GlobalPartnerClassificationCode");
    }

    Element etoBusDesc = new Element("BusinessDescription");
    value= getReceiverGlobalBusinessIdentifier(profile); //NSL20060117
    if (null != value)
    {
      Element eGblBusId = new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(value);
      etoBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toPartner.GlobalBusinessIdentifier");
    }


    value = EnterpriseUtil.getSupplyChainCode(getFromRole()); //NSL20060117
    if (null != value)
    {
      Element eGblSupChainCode = new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      etoBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toPartner.GlobalSupplyChainCode");
    }

    etoPartDesc.addContent(etoBusDesc);

    etoRoleDesc.addContent(etoPartDesc);
    etoRole.addContent(etoRoleDesc);
    return etoRole;
  }

}