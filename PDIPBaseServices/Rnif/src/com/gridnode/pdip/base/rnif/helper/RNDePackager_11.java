/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNDePackager_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Created
 * Apr 26 2004      Neo Sok Lay             GNDB00021906: Create a namespace  
 *                                          for NoNamespace for RNIF1
 * Oct 18 2005      Lim Soon Hsiung         Change the implementation for writing out XML content from
 *                                          mail part to XML file.
 * May 03 2006      Neo Sok Lay             Set IsRequestMsg to false for Signal doc  
 * Jun 26 2006      Neo Sok Lay             GNDB00027376: Set IsRequestMsg to false for response doc
 * Jul 08 2009      Tam Wei Xiang           #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                       
 */
package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.rnif.model.IRNConstant_11;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.rnif.exception.ILogErrorCodes;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.xml.exceptions.XMLException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import com.gridnode.xml.adapters.GNElement;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * The RNIF1.1 Unpackager.
 *
 * @author Guo Jianyu
 *
 * @version GT 4.0
 * @since 1.0
 */
public class RNDePackager_11 extends RNDePackager implements IRNConstant_11
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1216724262043691941L;


	public RNDePackager_11()
  {
    try
    {
      /*040426NSL
      IXMLServiceLocalHome homeObj = (IXMLServiceLocalHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(IXMLServiceLocalHome.class);
      IXMLServiceLocalObj xmlService = homeObj.create();
      namespace = xmlService.newNamespace("","");
      */
      namespace = XMLServiceHandler.getInstance().newNoNamespace(); 
    }
    catch(Exception e)
    {
      Logger.error(ILogErrorCodes.RNIF_1_1_DEPACKAGER_INITIALIZE,"[RNDePackager_11.init] Error initializing RNIF_NAMESPACE: "+e.getMessage(), e);
    }
  }

  protected File[] unpackFile(File rnfile, String udocfilename, RNPackInfo packInfo)
    throws RosettaNetException, SecurityServiceException
  {
/*
    SMimeFactory factory= SMimeFactory.newInstance(null, null);
    IMime signedenvelope= null;
    signedenvelope= factory.generateMime(rnfile);
    boolean isSigned= false;
    IMime envelope= null;
    IMime envelope2GenDigest = null;


    if (signedenvelope.isSigned())
    {
      IPart part= signedenvelope.getPart(0);
      if (!part.isMultipart())
        throw RosettaNetException.unpMesgSignErr(
          "signedenvelope's part(0) must be a multipart mime!");
      envelope= part.getMultipart();
      envelope2GenDigest = envelope;
      isSigned= true;
    }
    else
    {
      envelope= signedenvelope;
    }

    if (envelope.getPartCount() <= 0)
    {
      throw RosettaNetException.unpMesgGenErr("Cannot read RosettaNet Message");
    }
    IPart part;
    String dtd;

    // Read preamble
    part= envelope.getPart(0);
    readPreamble(packInfo, part);

    // Read delivery header
    part= envelope.getPart(1);
    readDeliveryHeader(packInfo, part);
*/

    try
    {
      DataInputStream dis = new DataInputStream(new FileInputStream(rnfile));

      // verify version No.
      int rnVersion = dis.readInt();
      if (rnVersion != RN_VERSION_NUMBER )
        throw RosettaNetException.unpMesgGenErr("Invalid RN Version: " + rnVersion);

      // retrieve RNO content (Service Message)
      int contentLength = dis.readInt();
      byte[] svcMesg = new byte[contentLength];
      if (dis.read(svcMesg) != contentLength)
        throw RosettaNetException.unpMesgGenErr("Error reading RNO content");

      //read signature
      boolean doSign = false;
      byte[] sign = null;
      if (dis.available() > 0)
      {
        int signLength = dis.readInt();
        if (signLength > 0)
        {
          doSign = true;
          sign = new byte[signLength];
          if (dis.read(sign) != signLength)
            throw RosettaNetException.unpMesgGenErr("Error reading digital signature");
        }
      }

      dis.close();

      SMimeFactory factory= SMimeFactory.newInstance(null, null);

      //write the service message into a temporary file
      File tmpFile= XMLUtil.createTempFile(udocfilename, svcMesg);

      //generate the IMime object
      IMime svcMesgObj = factory.generateMime(tmpFile);
      String contentType = svcMesgObj.getContentType();
      if (contentType.equals("Application/x-ChemXML"))
        packInfo.setRnifVersion(PROTOCOL_CIDX);
      else
        packInfo.setRnifVersion(PROTOCOL_RNIF);
      Logger.debug("[RNDePackager_11.unpackFile()] RNIFVersion is " + packInfo.getRnifVersion());
      if (svcMesgObj.getPartCount() <= 0)
        throw RosettaNetException.unpMesgGenErr("Cannot read any parts of service message");

      IPart part;

      //read preamble
      part = svcMesgObj.getPart(0);
      contentType = part.getContentType();
      if (contentType.indexOf(PREAMBLE_HEADER)<0)
        throw RosettaNetException.unpPrmbReadErr("Invalid preamble rnsubtype:" + contentType);

      readPreamble(packInfo, part);

      //read service header
      part = svcMesgObj.getPart(1);
      contentType = part.getContentType();
      if (contentType.indexOf(SERVICE_HEADER)<0)
        throw RosettaNetException.unpShdrReadErr("Invalid service header rnsubtype:" + contentType);

      readServiceHeader(packInfo, part);

      String senderDUNS= packInfo.getSenderGlobalBusIdentifier();
      Logger.debug("### senderDUNS is " + senderDUNS);
      RNCertInfo securityInfo= new RNCertInfo();
      try
      {
        if (_infoFinder != null)
        {
          securityInfo= _infoFinder.getSecurityInfoFromDUNS(senderDUNS);
        }
      }
      catch (Exception ex)
      {
        Logger.warn(ex);
        //throw RosettaNetException.unpMesgGenErr("Error in finding Security info based on senderDUNS:" + senderDUNS );
      }

      factory =  SMimeFactory.newInstance(getOwnEncryptCert(securityInfo),
        getPartnerSignCert(securityInfo));

      //verify the signature
      if (doSign)
      {
        try
        {
          Logger.debug("Verifying signature");
          factory.verify(svcMesg, sign);
        }
        catch(Exception e)
        {
          Logger.warn("Verification failed", e);
          X509Certificate pendingCert = getPendingCert(securityInfo.get_partnerSignCertificate());
          if (pendingCert == null)
          {
            Logger.debug("No pending cert found.");
            throw RosettaNetException.unpMesgSignErr("Message verification failed." + e.getMessage());
          }
          else
          {
            Logger.debug("Pending cert found. Try verifying with the pending cert...");
            factory.setPartnerCert(pendingCert);
            factory.verify(svcMesg, sign);
          }
        }
      }

      //Test the message digest
      String digestAlg= _infoFinder.getDigestAlgorithm(packInfo);
      DigestGenerator dg= new DigestGenerator();
      String digest= dg.getEncodedDigest(svcMesg, digestAlg);
      packInfo.setMsgDigest(digest);

      //read service content
      part = svcMesgObj.getPart(2);
      contentType = part.getContentType();
      if (contentType.indexOf(SERVICE_CONTENT)<0)
        throw RosettaNetException.unpSconReadErr("Invalid service content rnsubtype:" + contentType);

      List files= new ArrayList();
      try
      {
          // LSH:18Oct05 remove this implementation as it corrupt the XML content due to wrong encoding used.
//        String contentStr= (String) part.getContent();
//        FileWriter writer = new FileWriter(tmpFile.getAbsolutePath(), false);  //overwrite the file
//        writer.write(contentStr);
//        writer.close();

        // LSH:18Oct05  New implementation
        XMLUtil.writeXmlFile(part, tmpFile.getAbsolutePath());

        udocfilename= tmpFile.getAbsolutePath();
        Logger.debug("wrote service content to [" + udocfilename + "]");
        files.add(tmpFile);
      }
      catch (Exception ex)
      {
        throw RosettaNetException.unpMesgGenErr("Cannot write service content.");
      }

      File[] res= new File[files.size() + 1];
      for (int i= 0; i < files.size(); i++)
      {
        File element= (File) files.get(i);
        res[i + 1]= element;
      }

      return res;
    }
    catch(IOException e)
    {
      throw RosettaNetException.unpMesgGenErr(e.getMessage());
    }


/*    if (isSigned)
    {
      try
      {
        Logger.debug("signedenvelope is" + new String(signedenvelope.getContentByte(true)));
        signedenvelope= factory.generateMime(rnfile);
        Logger.debug("second signedenvelope is" + new String(signedenvelope.getContentByte(true)));
        envelope= ((IPart) factory.verify(signedenvelope)).getMultipart();
      }
      catch (SecurityServiceException ex)
      {
        Logger.err(ex);
        throw RosettaNetException.unpMesgSignErr("Message verification failed." + ex.getMessage());
      }
    }

    // read service header and service content
    IPart sheader, scontent;
    ArrayList attIPartList= new ArrayList();
    int count= envelope.getPartCount();
    if (count == 3)
    {
      // decrypt
      part= envelope.getPart(2);
      if (part.isEncrypted())
      {
        //Logger.debug("Service content is encrypted. (3 parts)");
        IMime payloadcontainer= (IMime) factory.decrypt(part);
        sheader= payloadcontainer.getPart(0);
        scontent= payloadcontainer.getPart(1);
        // Koh Han Sing 20020503
        int iPartCount= payloadcontainer.getPartCount();
        if (iPartCount > 2)
        {
          for (int i= 2; i < iPartCount; i++)
          {
            attIPartList.add(payloadcontainer.getPart(i));
          }
        }
      }
      else
      {
        sheader= null;
        scontent= null;
        throw RosettaNetException.unpMesgDcryptErr("Expected an encrypted message.");
        // alternative is to proceed without encryption
      }
    }
    else //must be 4 parts
      {
      sheader= envelope.getPart(2);
      scontent= envelope.getPart(3);
      if (scontent.isEncrypted())
      {
        //Logger.debug("Service content is encrypted. (4 parts)");
        IMime payloadcontainer= (IMime) factory.decrypt(scontent);
        scontent= payloadcontainer.getPart(0);
        // Koh Han Sing 20020503
        int iPartCount= payloadcontainer.getPartCount();
        if (iPartCount > 1)
        {
          for (int i= 1; i < iPartCount; i++)
          {
            attIPartList.add(payloadcontainer.getPart(i));
          }
        }
      }
      else
      {
        //Logger.debug("Service content not encrypted. (4 parts)");
        for (int i= 4; i < count; i++)
        {
          attIPartList.add(envelope.getPart(i));
        }
      }
    }

    // Read service header
    readServiceHeader(packInfo, sheader);

    // Attachment / manifest check is performed in the ValidateDocAction

    // Service Content - this is either the second part of a payload
    // container, or the fourth part of the normal envelope. The fourth
    // part may also be encrypted
    if (!SERVICE_CONTENT_CL.equals(scontent.getHeader(this.CONTENT_LOC)[0]))
    {
      throw RosettaNetException.unpSconReadErr(
        "Invalid Content-location header for Service Content");
    }

    List files= new ArrayList();
    try
    {
      String contentstr= (String) scontent.getContent();
      //byte[] contentByte = scontent.getContentByte(false);
      File tmpfile= XMLUtil.createTempFile(udocfilename, contentstr);
      udocfilename= tmpfile.getAbsolutePath();
      Logger.debug("wrote service content to [" + udocfilename + "]");
      files.add(tmpfile);
    }
    catch (IOException ex)
    {
      throw RosettaNetException.unpMesgGenErr("Cannot write service content.");
    }

    //*** @@todo attachment ...

    File[] res= new File[files.size() + 1];
    for (int i= 0; i < files.size(); i++)
    {
      File element= (File) files.get(i);
      res[i + 1]= element;
    }

    if (isSigned && !packInfo.getIsSignalDoc())
    {
      String digestAlg= _infoFinder.getDigestAlgorithm(packInfo);
      DigestGenerator dg= new DigestGenerator();
      byte[] envelopeContent= envelope2GenDigest.getContentByte(true);
      String digest= dg.getEncodedDigest(envelopeContent, digestAlg);
      packInfo.setMsgDigest(digest);
    }

    return res;
    */
  }

  /**
   * Reads a RosettaNet Preamble and updates the RNPackInfo.
   */
  void readPreamble(RNPackInfo packInfo, IPart preamble) throws RosettaNetException
  {
    File preambleFile =
      validateParts(
      preamble,
      PREAMBLE_FILE,
      PREAMBLE_DTD,
      PREAMBLE_DIC,
      PREAMBLE_XSD,
      RosettaNetException.UNP_PRMB_READERR,
      RosettaNetException.UNP_PRMB_VALERR);

    GNElement root = null;
    try
    {
      root = XMLUtil.getRoot(preambleFile);
      checkElement(root, "root", RosettaNetException.UNP_PRMB_READERR);

      //GlobalUsageCode
      updatePackInfoField(
        packInfo,
        RNPackInfo.GLOBAL_USAGE_CODE,
        root,
        "GlobalUsageCode");
      Logger.debug("Got GlobalUsageCode: " + packInfo.getGlobalUsageCode());
    }
    catch (Exception e)
    {
      Logger.warn(e);
      throw RosettaNetException.unpMesgGenErr(e.getMessage());
    }
  }

  /**
   * Reads a RosettaNet Service Header and updates the RNPackInfo.
   */
  void readServiceHeader(RNPackInfo packInfo, IPart sHeader) throws RosettaNetException
  {

    File sHeaderfile=
      validateParts(
        sHeader,
        SHEADER_FILE,
        SERVICE_HEADER_DTD,
        SERVICE_HEADER_DIC,
        SERVICE_HEADER_XSD,
        RosettaNetException.UNP_SHDR_READERR,
        RosettaNetException.UNP_SHDR_VALERR);

    GNElement root = null;

    try
    {
      root = XMLUtil.getRoot(sHeaderfile);
      checkElement(root, "root", RosettaNetException.UNP_SHDR_READERR);

      GNElement processControl= getChild(root, "ProcessControl");
      checkElement(processControl, "ProcessControl", RosettaNetException.UNP_SHDR_READERR);

      GNElement processIdentity = getChild(processControl, "ProcessIdentity");
      checkElement(processIdentity, "ProcessIdentity", RosettaNetException.UNP_SHDR_READERR);

      GNElement serviceRoute = getChild(processControl, "ServiceRoute");
      checkElement( serviceRoute, "ServiceRoute", RosettaNetException.UNP_SHDR_READERR);

      GNElement transControl = getChild(processControl, "TransactionControl");
      checkElement( transControl, "TransactionControl", RosettaNetException.UNP_SHDR_READERR);

      //** ProcessIdentity
      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_GLOBAL_PROCESS_CODE,
        processIdentity,
        "GlobalProcessIndicatorCode");

        // Update the Originator ID (GNID) and the initiating partner (DUNS)
//        if (null == doc.getProcessOriginatorId() )
//        {
      String originatorid = getChild(getChild(processIdentity, "initiatingPartner"),
                                  "GlobalBusinessIdentifier").getText();
      if (originatorid.length() <= 0)
      {
        throw new RosettaNetException(RosettaNetException.UNP_SHDR_VALERR,
          "Cannot read initiatingPartner.GlobalBusinessIdentifier");

      }

      // Use the GridNodeID instead of the DUNS for the Originator ID.
/*      int originator_gnode;

      BusinessEntity be = ReceiveRNetMessageCommand.getBusinessEntity( originatorid);
      if (null == be)
      {
        throw new RosettaNetException(RosettaNetException.UNP_SHDR_VALERR,
          "Business Entity with DUNS number is not found:" + originatorid);
      }

      originator_gnode = be.getNodeId();

      //** ?? keep the following line? - jianyu 26/02/03
      doc.setFieldValue( GridDocument.PROCESS_ORIGINATOR_ID, new Integer( originator_gnode));
*/
      updatePackInfoField(
        packInfo,
        RNPackInfo.PARTNER_GLOBAL_BUS_IDENTIFIER,
        getChild(processIdentity, "initiatingPartner"), "GlobalBusinessIdentifier");

//      doc.setFieldValue( GridDocument.PARTNER_GLOBAL_BUS_IDENTIFIER, Integer.valueOf( originatorid));
//        updateIntegerDocField( doc,
//                        GridDocument.PROCESS_ORIGINATOR_ID,
//                        processIdentity.getChild("initiatingPartner"),
//                        "GlobalBusinessIdentifier");
//        }

      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_INSTANCE_IDENTIFIER,
        processIdentity,
        "InstanceIdentifier");

      updatePackInfoField(
        packInfo,
        RNPackInfo.PIP_VERSION_IDENTIFIER,
        processIdentity,
        "VersionIdentifier");


      //** ServiceRoute
      updatePackInfoField(
        packInfo,
        RNPackInfo.FROM_GLOBAL_BUS_SERVICE_CODE,
        getChild(getChild(serviceRoute, "fromService"), "BusinessServiceDescription"),
        "GlobalBusinessServiceCode");

      updatePackInfoField(
        packInfo,
        RNPackInfo.TO_GLOBAL_BUS_SERVICE_CODE,
        getChild(getChild(serviceRoute, "toService"), "BusinessServiceDescription"),
        "GlobalBusinessServiceCode");

      //** TransactionControl
      String attemptCountStr = getChild(transControl, "AttemptCount").getText();
      packInfo.setAttemptCount((new Integer(attemptCountStr)).intValue());

      GNElement partRoleRoute = getChild(transControl, "PartnerRoleRoute");
      checkElement( partRoleRoute, "PartnerRoleRoute", RosettaNetException.UNP_SHDR_READERR);

      updatePackInfoField(
        packInfo,
        RNPackInfo.FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE,
        getChild(getChild(partRoleRoute, "fromRole"), "PartnerRoleDescription"),
        "GlobalPartnerRoleClassificationCode");

      updatePackInfoField(
        packInfo,
        RNPackInfo.TO_GLOBAL_PARTNER_ROLE_CLASS_CODE,
        getChild(getChild(partRoleRoute, "toRole"), "PartnerRoleDescription"),
        "GlobalPartnerRoleClassificationCode");

      GNElement transIdentity = getChild(transControl, "TransactionIdentity");
      updatePackInfoField(
        packInfo,
        RNPackInfo.BUS_ACTIVITY_IDENTIFIER,
        transIdentity,
        "GlobalTransactionCode");

      updatePackInfoField(
        packInfo,
        RNPackInfo.PROCESS_TRANSACTION_ID,
        transIdentity,
        "InstanceIdentifier");

      GNElement actionControl = getChild(transControl, "ActionControl");
      if(null != actionControl)
      {
        packInfo.setIsSignalDoc(false);
        GNElement actionIdentity = getChild(actionControl, "ActionIdentity");
        updatePackInfoField(
          packInfo,
          RNPackInfo.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE,
          actionIdentity,
          "GlobalBusinessActionCode");

        updatePackInfoField(
          packInfo,
          RNPackInfo.PROCESS_ACTION_ID,
          actionIdentity,
          "InstanceIdentifier");

        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.ACTION_IDENTITY_VERSION_IDENTIFIER,
          actionIdentity,
          "VersionIdentifier");

        //GlobalDocumentFunctionCode is derived...

        GNElement inResponseTo = getChild(actionControl, "inResponseTo");
        if (null != inResponseTo)
        {
          packInfo.setIsRequestMsg(false); //NSL20060626 Since it is a response
          GNElement ractionIdentity = getChild(inResponseTo, "ActionIdentity");
          updatePackInfoField(
            packInfo,
            RNPackInfo.IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE,
            ractionIdentity,
            "GlobalBusinessActionCode");
          updatePackInfoField(
            packInfo,
            RNPackInfo.IN_RESPONSE_TO_ACTION_ID,
            ractionIdentity,
            "InstanceIdentifier");
          //update inResponseTo.ActionIdentity.GlobalBusinessActionCode ->
          //                      ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE ...done before

        }

        GNElement partRoute = getChild(actionControl, "PartnerRoute");
        checkElement( partRoute, "PartnerRoute", RosettaNetException.UNP_SHDR_READERR);

        GNElement fromPartnerDescr = getChild(getChild(partRoute,"fromPartner"), "PartnerDescription");
        GNElement toPartnerDescr = getChild(getChild(partRoute,"toPartner"), "PartnerDescription");
        updatePackInfoField(
          packInfo,
          RNPackInfo.SENDER_GLOBAL_BUS_IDENTIFIER,
          getChild(fromPartnerDescr, "BusinessDescription"),
          "GlobalBusinessIdentifier");

        updatePackInfoField(
          packInfo,
          RNPackInfo.FROM_GLOBAL_PARTNER_CLASS_CODE,
          fromPartnerDescr,
          "GlobalPartnerClassificationCode");

        updatePackInfoField(
          packInfo,
          RNPackInfo.RECEIVER_GLOBAL_BUS_IDENTIFIER,
          getChild(toPartnerDescr, "BusinessDescription"),
          "GlobalBusinessIdentifier");

        updatePackInfoField(
          packInfo,
          RNPackInfo.TO_GLOBAL_PARTNER_CLASS_CODE,
          toPartnerDescr,
          "GlobalPartnerClassificationCode");

      }
      else
      { //SignalControl
        packInfo.setIsSignalDoc(true);
        packInfo.setIsRequestMsg(false); //NSL20060503 Signal will always be non-request
        GNElement signalControl = getChild(transControl, "SignalControl");
        checkElement( signalControl, "SignalControl", RosettaNetException.UNP_SHDR_READERR);

        GNElement inResponseTo = getChild(signalControl, "inResponseTo");
        checkElement( inResponseTo, "inResponseTo", RosettaNetException.UNP_SHDR_READERR);

        // ?? Shouldn't the following fields be set to IN_REPLY_TO_* ??
        GNElement ractionIdentity = getChild(inResponseTo, "ActionIdentity");
        updatePackInfoField(
          packInfo,
          RNPackInfo.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE,
          ractionIdentity,
          "GlobalBusinessActionCode");

        updatePackInfoField(
          packInfo,
          RNPackInfo.PROCESS_ACTION_ID,
          ractionIdentity,
          "InstanceIdentifier");

        updatePackInfoOptionalField(
          packInfo,
          RNPackInfo.IN_REPLY_TO_VERSION_IDENTIFIER,
          ractionIdentity,
          "VersionIdentifier");

        //Update SingalControl.InstanceIdentifier ... done before

        GNElement partRoute = getChild(signalControl, "PartnerRoute");
        checkElement( partRoute, "PartnerRoute", RosettaNetException.UNP_SHDR_READERR);

        GNElement fromPartnerDescr = getChild(getChild(partRoute, "fromPartner"), "PartnerDescription");
        GNElement toPartnerDescr = getChild(getChild(partRoute, "toPartner"), "PartnerDescription");
        updatePackInfoField(
          packInfo,
          RNPackInfo.SENDER_GLOBAL_BUS_IDENTIFIER,
          getChild(fromPartnerDescr, "BusinessDescription"),
          "GlobalBusinessIdentifier");

        updatePackInfoField(
          packInfo,
          RNPackInfo.FROM_GLOBAL_PARTNER_CLASS_CODE,
          fromPartnerDescr,
          "GlobalPartnerClassificationCode");

        updatePackInfoField(
          packInfo,
          RNPackInfo.RECEIVER_GLOBAL_BUS_IDENTIFIER,
          getChild(toPartnerDescr, "BusinessDescription"),
          "GlobalBusinessIdentifier");

        updatePackInfoField(
          packInfo,
          RNPackInfo.TO_GLOBAL_PARTNER_CLASS_CODE,
          toPartnerDescr,
          "GlobalPartnerClassificationCode");

        GNElement signalId = getChild(signalControl, "SignalIdentity");
        checkElement( signalId, "SignalIdentity", RosettaNetException.UNP_SHDR_READERR);

        updatePackInfoField(
          packInfo,
          RNPackInfo.SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE,
          signalId,
          "GlobalBusinessSignalCode");

        updatePackInfoField(
          packInfo,
          RNPackInfo.SIGNAL_IDENTITY_VERSION_IDENTIFIER,
          signalId,
          "VersionIdentifier");

      }
    }
    catch (Exception e)
    {
      Logger.warn(e);
      throw RosettaNetException.unpMesgGenErr(e.getMessage());
    }
  }


  protected File validateParts(
    IPart part,
    String filePrefix,
    String dtdname,
    String dicname,
    String schemaname,
    String readExName,
    String valExName)
    throws RosettaNetException
  {
    String content= null;
    try
    {
      content= (String) part.getContent();
    }
    catch (SecurityServiceException ex)
    {
      throw new RosettaNetException(readExName, "Cannot read content for part " + filePrefix + ".");
    }

    String dtd;
    try
    {
      dtd= XMLUtil.extractDTDName(content);
    }
    catch (XMLException e)
    {
      throw new RosettaNetException(readExName, "Cannot extract DTD: " + e.getMessage());
    }
    catch (ServiceLookupException e)
    {
      throw new RosettaNetException(readExName, "Cannot extract DTD: " + e.getMessage());
    }

    if (null == dtd)
    {
      throw RosettaNetException.unpMesgGenErr("RosettaNet Message does not have a DTD.");
    }
    if (!dtdname.equals(dtd))
    {
      throw new RosettaNetException(readExName, "Preamble DTD is not valid.");
    }

    // Write the content to a temp file and validate it.
    String dtdpath= null;
    //    String dicpath = FileUtil.getFile(IRnifPathConfig.PATH_DICTIONARY, dicname).getAbsolutePath();
    String schemapath= null;
    String tempfile= null;
    File tmpfile= null;
    try
    {
      tmpfile= XMLUtil.createTempFile(filePrefix, content);
      tempfile= tmpfile.getAbsolutePath();

    }
    catch (IOException ex)
    {
      throw RosettaNetException.fileProcessErr("Cannot write to file.");
    }
    try
    {
      dtdpath= FileUtil.getFile(IRnifPathConfig.PATH_DTD, dtdname).getAbsolutePath();
      //    dicpath = FileUtil.getFile(IRnifPathConfig.PATH_DICTIONARY, dicname).getAbsolutePath();
      schemapath= FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schemaname).getAbsolutePath();
    }
    catch (FileAccessException ex)
    {
      throw new RosettaNetException(valExName, ex.getMessage());
    }

    Logger.debug(
      "RNDePackager_11.validateParts() - validating:\n"
        + "\tfile:"
        + tempfile
        + "\n\tdtd: "
        + dtdpath
        + "\n\tschema: "
        + schemapath
        + "\n");
    ArrayList errlist= new ArrayList();

    XMLUtil.validateDTD(tempfile, dtdpath, errlist);
    XMLUtil.validateSchema(tempfile, schemapath, dtdpath, errlist);

    if (errlist.isEmpty())
      return tmpfile;
    StringBuffer errbuf= new StringBuffer();
    Iterator i= errlist.iterator();
    while (i.hasNext())
    {
      errbuf.append((String) i.next());
      errbuf.append("\n");
    }
    throw new RosettaNetException(valExName, errbuf.toString());
  }
}