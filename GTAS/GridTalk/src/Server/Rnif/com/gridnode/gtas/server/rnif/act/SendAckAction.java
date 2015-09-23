package com.gridnode.gtas.server.rnif.act;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

public class SendAckAction extends SendSignalAction
{

  //static IDigestGenerator _digestGenerator= new DigestGenerator();

  public SendAckAction()
  {
    super();
  }

  Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException
  {
    Element root= new Element("ReceiptAcknowledgment");
    if (Boolean.TRUE.equals(pAct.getIsNonRepudiationRequired()))
    {

      RNProfile oldProfile= new ProfileUtil().getProfileMustExist(originalGDoc);
      String digest= oldProfile.getMsgDigest();

      //         File uDocFile = null;
      //          
      //         try
      //         { 
      //           uDocFile = DocumentUtil.getUDoc(originalGDoc);
      //         }catch(Exception ex)
      //         {
      //           throw RnifException.fileProcessErr("Cannot can udoc file for GridDocument "+ originalGDoc.getEntityDescr(), ex);
      //         }
      //        String digestAlg = pAct.getGDigestAlgCode();
      //         String digest = _digestGenerator.getEncodedDigest(
      //                            _digestGenerator.getDigest(uDocFile, digestAlg));

      Element nonrepudInfo= new Element("NonRepudiationInformation");
      Element originalMsgDigest= new Element("OriginalMessageDigest");
      originalMsgDigest.addContent(digest);
      nonrepudInfo.addContent(originalMsgDigest);

      root.addContent(nonrepudInfo);
    }
    DocType docType= new DocType("ReceiptAcknowledgment", IRnifConstant.ACK_MESSAGE_TYPE_2);
    Document doc= new Document(root, docType);

    return doc;

  }

  String getBaseDocName(String originalFileName)
  {
    // set a unique filename
    return "Ack_" + originalFileName;
  }

  String getActualClassName()
  {
    return "SendAckAction";
  }

  String getProcessMsgType()
  {
    return IRnifConstant.ACK_MESSAGE_TYPE_2;
  }

  String getUDocDocType()
  {
    return IRnifConstant.RN2_ACK;
  }

  String getGlobalSignalCode()
  {
    return "Receipt Acknowledgment";
  }

}