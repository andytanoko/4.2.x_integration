package com.gridnode.gtas.server.rnif.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.EnterpriseUtil;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.util.TimeUtil;

public class FailureNotificationAction extends SendNoFAction
{
  static String RN_DATEPATTERN= "yyyyMMdd'T'HHmmss'.'SSS'Z'";
  static SimpleDateFormat sdf= new SimpleDateFormat(RN_DATEPATTERN);
  

  public FailureNotificationAction()
  {
    super();
  }

  @Override
  protected Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException
  {
    RNProfile profile= new ProfileUtil().getProfileMustExist(originalGDoc);
    
    setThisNoFPartner(originalGDoc, profile); //NSL20060202 Determine the recipient partner for this NoF
    
    DocType docType=
      new DocType("Pip0A1FailureNotification", IRnifConstant.FAILURE_NOTIFY_MSG_TYPE_2);

    Element root= new Element("Pip0A1FailureNotification");

    Element child1= new Element("ActionControl");
    Element child2= new Element("ActionIdentity");
    Element child3= new Element("GlobalBusinessActionCode");

    Object actionCode= profile.getActionIdentityGlobalBusActionCode();
    if (actionCode != null)
      child3.addContent(actionCode.toString());
    child2.addContent(child3);

    child1.addContent(child2);

    child2= new Element("messageTrackingID");
    child3= new Element("InstanceIdentifier");
    child3.addContent(profile.getDeliveryMessageTrackingId());
    child2.addContent(child3);
    child1.addContent(child2);
    root.addContent(child1);

    Date initialTime= originalGDoc.getDateTimeCreate();
    if(initialTime == null)
      initialTime = originalGDoc.getDateTimeSendStart();
    String timeStr= sdf.format(initialTime);

    child1= new Element("failedInitiatingDocumentDateTime");
    child2= new Element("DateTimeStamp");
    child2.addContent(timeStr);
    child1.addContent(child2);
    root.addContent(child1);

    child1= new Element("failedInitiatingDocumentIdentifier");
    child2= new Element("ProprietaryDocumentIdentifier");
    child2.addContent(profile.getUniqueValue());
    child1.addContent(child2);
    root.addContent(child1);

    child1= writeFromRole(def, pAct, originalGDoc, profile);
    root.addContent(child1);

    child1= new Element("GlobalDocumentFunctionCode");
    child1.addContent("Request");
    root.addContent(child1);

    String indicatorCode= profile.getPIPGlobalProcessCode();
    child1= new Element("ProcessIdentity");
    child2= new Element("GlobalProcessIndicatorCode");
    child2.addContent(indicatorCode);
    child1.addContent(child2);
    child2= new Element("InstanceIdentifier");
    child2.addContent(profile.getPIPInstanceIdentifier());
    child1.addContent(child2);
    child2= new Element("VersionIdentifier");
    child2.addContent(profile.getPIPVersionIdentifier());
    child1.addContent(child2);
    root.addContent(child1);

    child1= new Element("reason");
    child2= new Element("FreeFormText");
    //child2.addContent(RosettaNetException.FN_TIMEOUT.toString());
    child2.addContent(_reasonStr); //NSL20060117 don't fix the reason
    child1.addContent(child2);
    root.addContent(child1);

    Date time= TimeUtil.localToUtcTimestamp();
    child1= new Element("thisDocumentGenerationDateTime");
    child2= new Element("DateTimeStamp");
    child2.addContent(sdf.format(time));
    child1.addContent(child2);
    root.addContent(child1);

    child1= new Element("thisDocumentIdentifier");
    child2= new Element("ProprietaryDocumentIdentifier");
    child2.addContent(DocumentUtil.getProprietaryDocIdByDateTime());
    child1.addContent(child2);
    root.addContent(child1);

    child1= writeToRole(def, pAct, originalGDoc, profile);
    root.addContent(child1);

    Document doc= new Document(root, docType);
    return doc;
  }

  protected Element writeFromRole(ProcessDef def, ProcessAct pAct, GridDocument originalGDoc, RNProfile profile)
    throws RnifException
  {

    String value;
    HashMap info= getContactInfo(getFromRole()); //NSL20060117
    if (null == info)
    {
      throw RnifException.failureNotificationGenError(
        "Cannot get contact information for BE: " + getSenderGlobalBusinessIdentifier(profile));
    }

    Element efromRole= new Element("fromRole");

    Element efromRoleDesc= new Element("PartnerRoleDescription");

    Element ecustomerInfo= writeContactInfo(info);
    efromRoleDesc.addContent(ecustomerInfo);

    value= def.getFromPartnerRoleClassCode();
    if (null != value)
    {
      Element efromGblPartRoleClassCode= new Element("GlobalPartnerRoleClassificationCode");
      efromGblPartRoleClassCode.addContent(value);
      efromRoleDesc.addContent(efromGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:fromRole.PartnerRoleDescription");
    }

    Element efromPartDesc= new Element("PartnerDescription");
    Element efromBusDesc= new Element("BusinessDescription");
    value= getSenderGlobalBusinessIdentifier(profile);
    if (null != value)
    {
      Element eGblBusId= new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(value);
      efromBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:fromPartner.GlobalBusinessIdentifier");
    }

    value=
      EnterpriseUtil.getSupplyChainCode(getFromRole()); //NSL20060117
    if (null != value)
    {
      Element eGblSupChainCode= new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      efromBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:fromPartner.GlobalSupplyChainCode");
    }
    efromPartDesc.addContent(efromBusDesc);

    value= def.getFromPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode= new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      efromPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:fromPartner.GlobalPartnerClassificationCode");
    }

    efromRoleDesc.addContent(efromPartDesc);

    efromRole.addContent(efromRoleDesc);
    return efromRole;
  }
  
  protected Element writeToRole(ProcessDef def, ProcessAct pAct, GridDocument originalGDoc, RNProfile profile)
    throws RnifException
  {
    String value;
    //Integer i;
    String intValue;

    Element etoRole= new Element("toRole");
    Element etoRoleDesc= new Element("PartnerRoleDescription");
    value= def.getGToPartnerRoleClassCode();
    if (null != value)
    {
      Element etoGblPartRoleClassCode= new Element("GlobalPartnerRoleClassificationCode");
      etoGblPartRoleClassCode.addContent(value);
      etoRoleDesc.addContent(etoGblPartRoleClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toRole.PartnerRoleDescription");
    }

    Element etoPartDesc= new Element("PartnerDescription");
    Element etoBusDesc= new Element("BusinessDescription");
    intValue= getReceiverGlobalBusinessIdentifier(profile); //NSL20060117
    if (null != intValue)
    {
      Element eGblBusId= new Element("GlobalBusinessIdentifier");
      eGblBusId.addContent(intValue);
      etoBusDesc.addContent(eGblBusId);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toPartner.GlobalBusinessIdentifier");
    }

    value=
      EnterpriseUtil.getSupplyChainCode(getToRole());
    if (null != value)
    {
      Element eGblSupChainCode= new Element("GlobalSupplyChainCode");
      eGblSupChainCode.addContent(value);
      etoBusDesc.addContent(eGblSupChainCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError("failPip:toPartner.GlobalSupplyChainCode");
    }
    etoPartDesc.addContent(etoBusDesc);

    value= def.getGToPartnerClassCode();
    if (null != value)
    {
      Element eGblPartClassCode= new Element("GlobalPartnerClassificationCode");
      eGblPartClassCode.addContent(value);
      etoPartDesc.addContent(eGblPartClassCode);
    }
    else
    {
      throw RnifException.failureNotificationGenError(
        "failPip:toPartner.GlobalPartnerClassificationCode");
    }

    etoRoleDesc.addContent(etoPartDesc);
    etoRole.addContent(etoRoleDesc);
    return etoRole;
  }

  protected Element writeContactInfo(HashMap info)
  {
    String value;
    Element contactInfo= new Element("ContactInformation");

    Element child4= new Element("contactName");
    Element child5= new Element("FreeFormText");
    value= (String) info.get(NNAME);
    if (value != null)
    {
      child5.addContent(value);
    }
    child4.addContent(child5);
    contactInfo.addContent(child4);

    child4= new Element("EmailAddress");
    value= (String) info.get(NEMAIL);
    if (value != null)
    {
      child4.addContent(value);
    }
    contactInfo.addContent(child4);

    child4= new Element("facsimileNumber");
    child5= new Element("CommunicationsNumber");
    value= (String) info.get(NFAX);
    if (value != null)
    {
      child5.addContent(value);
    }
    child4.addContent(child5);
    contactInfo.addContent(child4);

    child4= new Element("telephoneNumber");
    child5= new Element("CommunicationsNumber");
    value= (String) info.get(NTEL);
    if (value != null)
    {
      child5.addContent(value);
    }
    child4.addContent(child5);
    contactInfo.addContent(child4);
    return contactInfo;

  }

  @Override
  protected String getBaseDocName(String originalFileName)
  {
    // set a unique filename
    return "FailNoti_" + originalFileName;
  }

  @Override
  protected String getActualClassName()
  {
    return "FailureNotificationAction";
  }

  @Override
  protected String getProcessMsgType()
  {
    return IRnifConstant.FAILURE_NOTIFY_MSG_TYPE_2;
  }

  @Override
  protected String getUDocDocType()
  {
    return IRnifConstant.RN2_FAILNOTF;
  }

}