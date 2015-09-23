package com.gridnode.gtas.server.rnif.act;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.EntityUtil;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.pdip.base.rnif.model.IRNConstant_20;
import com.gridnode.pdip.base.rnif.helper.XMLUtil;
//import com.gridnode.pdip.base.xml.helpers.XMLDocumentUtility;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

//import org.jdom.Element;
//import org.jdom.Namespace;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNNamespace;

import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ValidateSignalDocAction extends ValidateDocAction
{
  static GNNamespace RNIF2_NAMESPACE= null;
  static GNNamespace RNIF1_NAMESPACE = null;

  static
  {
    try
    {
      /*040428NSL
      IXMLServiceLocalHome homeObj = (IXMLServiceLocalHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(IXMLServiceLocalHome.class);
      IXMLServiceLocalObj xmlService = homeObj.create();
      RNIF2_NAMESPACE = xmlService.newNamespace("rn2","http://www.rosettanet.org/RNIF/V02.00");
      RNIF1_NAMESPACE = xmlService.newNamespace("","");//RNIF1.1 doesn't use an XML namespace
      */
      RNIF2_NAMESPACE = XMLServiceHandler.getInstance().newNamespace("", "http://www.rosettanet.org/RNIF/V02.00");
      RNIF1_NAMESPACE = XMLServiceHandler.getInstance().newNoNamespace();
    }
    catch(Exception e)
    {
      Logger.warn("Error initializing RNIF_NAMESPACE", e);
    }
  }

  //final static Namespace RNIF2_NAMESPACE= IRNConstant.RNIF_NAMESPACE;

  static final int RECEIPT_ACK_MSG= 1;
  static final int EXCEPTION_MSG= 2;
  static final int RECEIPT_ACK_EXP_MSG = 3;

  int _msgType= RECEIPT_ACK_MSG;
  protected GNNamespace RNIFNamespace= null;
  //protected Namespace RNIFNamespace= null;

  private boolean isRNIF20;

  public ValidateSignalDocAction(ProcessDef def, String signalType)
  {
    isRNIF20 = def.getRNIFVersion().equals(def.RNIF_2_0);
    RNIFNamespace= isRNIF20 ? RNIF2_NAMESPACE : RNIF1_NAMESPACE;
    _msgType= getMsgType(signalType);
  }

  String getDTD(ProcessAct pAct) throws RnifException
  {
    switch (_msgType)
    {
      case RECEIPT_ACK_MSG :
        if (isRNIF20)
          return IRNConstant_20.ACKNOWLEDGEMENT_DTD;
        else
          return IRNConstant_11.ACKNOWLEDGEMENT_DTD;
      case EXCEPTION_MSG :
        if (isRNIF20)
          return IRNConstant_20.EXCEPTION_DTD;
        else
          return IRNConstant_11.EXCEPTION_DTD;
      case RECEIPT_ACK_EXP_MSG: // only in RNIF1.1
        return IRNConstant_11.RECEIPT_ACK_EXP_DTD;
      default :
        return null;
    }
  }

  String getDic(ProcessAct pAct) throws RnifException
  {
    switch (_msgType)
    {
      case RECEIPT_ACK_MSG :
        if (isRNIF20)
          return IRNConstant_20.ACKNOWLEDGEMENT_DIC;
        else
          return IRNConstant_11.ACKNOWLEDGEMENT_DIC;
      case EXCEPTION_MSG :
        if (isRNIF20)
          return IRNConstant_20.EXCEPTION_DIC;
        else
          return IRNConstant_11.EXCEPTION_DIC;
      default :
        return null;
    }
  }
  String getSchema(ProcessAct pAct) throws RnifException
  {
    switch (_msgType)
    {
      case RECEIPT_ACK_MSG :
        if (isRNIF20)
          return IRNConstant_20.ACKNOWLEDGEMENT_XSD;
        else
          return IRNConstant_11.ACKNOWLEDGEMENT_XSD;
      case EXCEPTION_MSG :
        if (isRNIF20)
          return IRNConstant_20.EXCEPTION_XSD;
        else
          return IRNConstant_11.EXCEPTION_XSD;
      default :
        return null;
    }
  }

  void furtherValidate(GridDocument gDoc, ProcessAct pAct, String[] fullPaths, List errlist)
    throws RosettaNetException
  {
    if (_msgType == RECEIPT_ACK_MSG)
    {
      if (Boolean.TRUE.equals(pAct.getIsNonRepudiationRequired()))
        validateMsgDigest(gDoc, fullPaths);
    }
  }

  void validateMsgDigest(GridDocument gDoc, String[] fullPaths) throws RosettaNetException
  {
    Logger.debug("validateMsgDigest()");

    GNElement root= null;
    try
    {
      //root= XMLDocumentUtility.getRoot(fullPaths[FULL_UDOC_PATH], fullPaths[FULL_DTD_PATH]);
      root= XMLUtil.getXMLServiceMgr().getRoot(fullPaths[FULL_UDOC_PATH], fullPaths[FULL_DTD_PATH]);
    }
    catch (Exception ex)
    {
      throw RosettaNetException.fileProcessErr(ex.toString());
    }

    GNElement digestParent= root.getChild("NonRepudiationInformation", RNIFNamespace); //040428NSL Get with namespace
    GNElement digest= null;
    if (digestParent != null)
      digest= digestParent.getChild("OriginalMessageDigest", RNIFNamespace); //040428NSL Get with namespace

    if (digest == null)
      throw RosettaNetException.ackDigestDiffErr("digest not found in the acknowledge document!");

    String receivedDigestStr= digest.getText();
    try
    {
      compareDigestInDB(receivedDigestStr, gDoc);
    }
    catch(RosettaNetException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw RosettaNetException.ackDigestDiffErr(
        "Error occured in validate the msg digest in Acknowledge document!");
    }
  }

  void compareDigestInDB(String receivedDigest, GridDocument gDoc) throws Exception
  {
    ProfileUtil profileUtil= new ProfileUtil();
    RNProfile profile= null;
    try
    {
      profile= profileUtil.getProfileMustExist(gDoc);
    }
    catch (RnifException ex)
    {
      throw RosettaNetException.fileProcessErr(
        "Can not found the RNProfile of GridDoc "
          + gDoc.getEntityDescr()
          + "; with RNProfileUID="
          + gDoc.getRnProfileUid());
    }

    //   String defName = profile.getProcessDefName();
    String instId= profile.getProcessInstanceId();
    String initiatorId= profile.getProcessOriginatorId();
    //   String responderId = profile.getProcessResponderId();
    Logger.debug("[compareDigestInDB], processInst is:" + instId + "/" + initiatorId);
    IDataFilter filter=
      EntityUtil.getEqualFilter(
        new Number[] { RNProfile.MSG_DIGEST },
        new Object[] { receivedDigest });
    Collection profiles= profileUtil.getProfilesByFilter(filter);

    if (profiles == null || profiles.isEmpty())
      throw RosettaNetException.ackDigestDiffErr(
        "Cannot found a BizDocument with the msgDigest as specified in the Ack document:"
          + receivedDigest);

    for (Iterator iterator= profiles.iterator(); iterator.hasNext();)
    {
      RNProfile originalProfile= (RNProfile) iterator.next();
      
      if (!instId.equals(originalProfile.getProcessInstanceId())
        || ! initiatorId.equals(originalProfile.getProcessOriginatorId())) //20090807 twx #841
      {
        Logger.log(
          "found a profile with same msgDigest but different belongs to a different Instance : originalProfileUid="
            + originalProfile.getUId()
            + ";inst is:"
            + originalProfile.getProcessInstanceId()
            + "/"
            + originalProfile.getProcessOriginatorId()
            + ";Ack doc's msg digest is "
            + receivedDigest);

      }
      else
      {
        return;
      }

    }
  }

  void compareDigest(String expectedDigestStr, String actualDigestStr) throws RosettaNetException
  {
    if (expectedDigestStr == null)
      Logger.warn("Digest To Compare cannot be null");

    if (!expectedDigestStr.equals(actualDigestStr))
    {
      String info=
        "expectedDigest is @" + expectedDigestStr + "@ actualDigest is @" + actualDigestStr + "@";
      throw RosettaNetException.ackDigestDiffErr(info);
    }
  }

  public static int getMsgType(String globalSignalCode)
  {
    if ("Receipt Acknowledgment".equals(globalSignalCode))
      return RECEIPT_ACK_MSG;
    if ("Receipt Acknowledge".equals(globalSignalCode))
      return RECEIPT_ACK_MSG;
    if ("Receipt Acknowledgement Exception".equals(globalSignalCode))
      return RECEIPT_ACK_EXP_MSG;
    if ("Exception".equals(globalSignalCode))
      return EXCEPTION_MSG;
    if ("General Exception".equals(globalSignalCode))
      return EXCEPTION_MSG;
    throw new IllegalArgumentException(
      "cannot determined Signal Msg Type from globalSignalCode=" + globalSignalCode);

  }
}