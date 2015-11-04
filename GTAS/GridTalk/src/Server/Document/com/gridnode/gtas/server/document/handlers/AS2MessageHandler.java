/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.sendmd
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2MessageHandler.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 29 2003      Guo Jianyu              Created
 * Nov 10 2005      Neo Sok Lay             Use ServiceLocator instead of ServiceLookup
 * Apr 18 2006      Neo Sok Lay             Refactor handlerMessage().
 *                                          Change preSend(): Set DocTransStatus to null
 *                                          if MDN not required. 
 * Jun 09 2006      Neo Sok Lay             GNDB00027259: Temp udoc file not closed resulting
 *                                          in 0 bytes copied to Inbound folder. 
 * Aug 30 2006      Neo Sok Lay             GNDB00027767: 
 *                                          - Prepare PAYLOAD_FILENAME in send headers
 *                                          - Use Filename from MIME as udoc filename
 *                                          - Use DocumentManager for gdoc update instead of directly use entity handler 
 * Sep 08 2006      Neo Sok Lay             GNDB00027800: 
 *                                          - Set content handler for application/octet-stream
 *                                          - Generic content type if file type not expected in contentType properties.  
 * Nov 08 2006      Tam Wei Xiang           Modified method updateRequestGridDoc(...), some operations will rely
 *                                          on the DocumentManager.handleDocAccepted                                                                                                                         
 *                                          - Generic content type if file type not expected in contentType properties.
 * Jan 22 2007      Neo Sok Lay             GNDB00028098:
 *                                          - Missing space after colons in MDN content.                                            
 *                                          - Generic content type if file type not expected in contentType properties.
 * Oct 03 2007      Tam Wei Xiang           GNDB00028458: Trigger Document Received by Partner alert
 *                                          after received the MDN.
 * Apr 11 2008      Tam Wei Xiang           #20: The AS2 doc received by TP alert failed to be triggered due to merge.
 * May 21 2008      Tam Wei Xiang           #44: The "tracingID" need to be set while we receiving the incoming
 * Jun 11 2009      Tam Wei Xiang           #472: Ensure the feedback handler has completed the
 *                                                update on the GDOC prior we update the MDN status.                                                                               
 * 01 July 2009      Tam Wei Xiang          #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 * 04 Aug 2009       Tam Wei Xiang          #840: Digest algo that is used in MDN generation is always SHA-1,
 *                                                modify sendMDN(...)
 * 18 May 2012      Tam Wei Xiang           #3588: Derive the own Business Entity given AS2-To                                                                             
 *                                                                                                                                 
 */
package com.gridnode.gtas.server.document.handlers;

import java.io.*;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Header;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.*;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerHome;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerObj;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelServiceDelegate;
import com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.GNMimeUtility;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.security.mime.smime.ISMimeDePackager;
import com.gridnode.pdip.base.security.mime.smime.ISMimePackager;
import com.gridnode.pdip.base.security.mime.smime.SMimeFactory2;
import com.gridnode.pdip.base.security.mime.smime.exceptions.GNSMimeException;
import com.gridnode.pdip.base.security.mime.smime.helpers.SMimeHelper;
import com.gridnode.pdip.base.transport.comminfo.HttpCommInfo;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportServiceHome;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportServiceObj;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;

public class AS2MessageHandler implements IMessageHandler, IAS2Headers
{
  static
  {
    MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
    mc.addMailcap("application/xml;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edifact;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-x12;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-consent;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("message/disposition-notification;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap(GNMimeUtility.GENERIC_CONTENT_TYPE+";; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    CommandMap.setDefaultCommandMap(mc);
  }

  private static final int MAXIMUM_SLEEP = 15;
  private static final long SLEEP_LENGTH = 2*1000; //2 seconds
  private static Random random = new Random();
  private SMimeFactory sf = null;

  public static Configuration config = ConfigurationManager.getInstance().getConfig(
    "contentType");

  public String[] preSend(GridDocument gDoc, ChannelInfo channelInfo)
  {
    String msgID = GNMimeUtility.getUniqueMessageIDValue();

    if (msgID.startsWith("<") && msgID.endsWith(">"))
    {
      gDoc.setUserTrackingID(msgID.substring(1, msgID.length()-1));
    }
    else
    {
      gDoc.setUserTrackingID(msgID);
      msgID = "<" + msgID + ">";  //AS2 spec requires Message-ID to be wrapped with "<" and ">"
    }
    Logger.debug("[AS2MessageHandler.preSend] setting msgID to " + gDoc.getUserTrackingID());

    //include extra headers in the DispatchMessage based on its
    //indirectly embedded packaging profile
    String[] moreHeader = null;
    PackagingInfo pkgInfo = channelInfo.getPackagingProfile();
    if ((pkgInfo!= null) && pkgInfo.AS2_ENVELOPE_TYPE.equalsIgnoreCase(pkgInfo.getEnvelope()))
    {
      moreHeader = new String[10]; //NSL20060830
      moreHeader[0] = msgID;

      String myBEID = null;
      try
      {
        myBEID = gDoc.getSenderBizEntityId();
        DataFilterImpl filter = new DataFilterImpl();
        filter.addSingleFilter(null, BusinessEntity.ID, filter.getEqualOperator(),
          myBEID, false);
        filter.addSingleFilter(filter.getAndConnector(), BusinessEntity.IS_PARTNER,
          filter.getEqualOperator(), Boolean.FALSE, false);
        Collection beList = BizRegDelegate.getBizRegistryManager().findBusinessEntities(filter);
        if (beList != null && !beList.isEmpty())
        {
          Iterator itr = beList.iterator();
          BusinessEntity myBE = (BusinessEntity)itr.next();
          moreHeader[1] = getAS2Identifier(myBE); //AS2-From
        }

      }
      catch(Exception e)
      {
        Logger.warn("[AS2MessageHandler.preSend] Exception getting AS2-From for BE: " + myBEID,e);
      }

      try
      {
        moreHeader[2] = getAS2Identifier(getPartnerBE(getPartner(gDoc.getRecipientPartnerId())));
      }
      catch(Exception e)
      {
        Logger.warn("[AS2MessageHandler.preSend] Exception getting AS2-To for partnerID: " + gDoc.getRecipientPartnerId(),e);
      }

      AS2PackagingInfoExtension pkgInfoExtension =
       (AS2PackagingInfoExtension)pkgInfo.getPkgInfoExtension();

      if (pkgInfoExtension != null)
      {
        moreHeader[3] = new Boolean(pkgInfoExtension.getIsAckReq()).toString();
        moreHeader[4] = new Boolean(pkgInfoExtension.getIsAckSigned()).toString();
        moreHeader[5] = new Boolean(pkgInfoExtension.getIsAckSyn()).toString();
        moreHeader[6] = pkgInfoExtension.getReturnURL();
      }
      else
      {
        moreHeader[3] = "false";
        moreHeader[4] = "false";
        moreHeader[5] = "false";
        moreHeader[6] = "";
      }
      // convert file type to content type. e.g. convert "xml" to "application/XML"
      moreHeader[7] = convertFileType2ContentType(gDoc.getUdocFileType());

      //String uDocType = gDoc.getUdocDocType();
/*      if (uDocType != null && uDocType.length() > 0)
        moreHeader[8] = "Test Case " + uDocType.substring(uDocType.length()-1);
        //This "subject" is for facilitating AS2 interop test only. It should not be used any
        //longer after the interop test ends.
      else
*/ // test over
      moreHeader[8] = "AS2 Message";  //generic subject

      moreHeader[9] = gDoc.getUdocFilename(); //NSL20060830 PAYLOAD_FILENAME
      
      if ("true".equalsIgnoreCase(moreHeader[3]))
        gDoc.setDocTransStatus("Ongoing");
      else
      {
      	gDoc.setDocTransStatus(null);
      }
    }
    return moreHeader;
  }

  /**
   * This method handles the incoming AS2 message before it creates its corresponding
   * gDoc and insert it into BPSS
   */
  public void handlerMessage(String[] defaultHeader,
                             String[] dataReceived,
                             File[] filesReceived,
                             Hashtable header
                             )
  {
    //String dispositionType = PROCESSED;
    //String errMsg = "";
    /**
     * @todo unpack the data
     */
    //X509Certificate partnerCert = null;
    //X509Certificate ownCert = null;

    String as2From = (String)header.get(IAS2Headers.AS2_FROM);
    if (as2From != null)
    {
      as2From = as2From.trim();
      if (as2From.startsWith("\"") && as2From.endsWith("\""))
      {
        as2From = as2From.substring(1, as2From.length()-1);
      }
      else if (!as2From.startsWith("\"") && !as2From.endsWith("\""))
      {}
      else
      {
        Logger.warn("[AS2MessageHandler.handlerMessage] Invalid AS2 identifier: " + as2From + "\nMessage ignored.");
        return;
        //errMsg = "Error: authentication-failed";
      }
    }
    else
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Null AS2-From value. Message ignored.");
      return;
      //errMsg = "Error: authentication-failed";
    }

    ArrayList<String> possiblePartnerIds = new ArrayList<String>();
    //String partnerId = null;
    try
    {
      BusinessEntity partnerBE = BizRegDelegate.getBizRegistryManager(
        ).findBusinessEntityByDomainIdentifier(DomainIdentifier.TYPE_AS2_IDENTIFIER,
        as2From);
      Collection partnerUIDs = BizRegDelegate.getEnterpriseHierarchyManager(
        ).getPartnersForBizEntity((Long)partnerBE.getKey());
      if (partnerUIDs != null && !partnerUIDs.isEmpty())
      {
      	/*
        Iterator itr = partnerUIDs.iterator();
        if (itr.hasNext())
        {
          Long partnerUID = (Long)itr.next();
          Partner partner = BizRegDelegate.getPartnerManager().findPartner(partnerUID);
          partnerId = partner.getPartnerID();
        }
        */
      	DataFilterImpl filter = new DataFilterImpl();
      	filter.addDomainFilter(null, Partner.UID, partnerUIDs, false);
      	filter.addSingleFilter(filter.getAndConnector(), Partner.STATE, filter.getEqualOperator(), Partner.STATE_ENABLED, false);
      	Collection partnerList = BizRegDelegate.getPartnerManager().findPartner(filter);
      	if (partnerList != null)
      	{
      		for (Object o : partnerList)
      		{
      			Partner partner = (Partner)o;
      			possiblePartnerIds.add(partner.getPartnerID());
      		}
      	}
      }
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Exception getting partner based on AS2-From: " + as2From, e);
      return;
    }

    //if (partnerId == null)
    if (possiblePartnerIds.isEmpty())
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Can't find partner based on AS2-From: " + as2From);
      return;
    }


    //retrieve all possible mappings
    Collection certMappings = null;
    IDataFilter filter = null;
    try
    {
      filter = new DataFilterImpl();
      //filter.addSingleFilter(null,BizCertMapping.PARTNER_ID, filter.getEqualOperator(), partnerId, false);
      filter.addDomainFilter(null, BizCertMapping.PARTNER_ID, possiblePartnerIds, false);
      Collection  mappingCol= getPartnerProcessManager().findBizCertMappingByFilter(filter);
      if (mappingCol != null && !mappingCol.isEmpty())
      {
      	/*
        BizCertMapping certMapping = (BizCertMapping)mappingCol.iterator().next();
        ownCert = extractX509Certificate(certMapping.getOwnCert());
        partnerCert = extractX509Certificate(certMapping.getPartnerCert());
        */
      	certMappings = mappingCol;
      }
      else
      {
        Logger.warn("[AS2MessageHandler.handlerMessage] Can't find cert mapping based on partner ID(s):" + possiblePartnerIds + //partnerId +
          ". The partner may not exist.");
        return;
      }
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Can't find cert mapping based on partner ID(s):" + possiblePartnerIds + //partnerId +
        ". The partner may not exist.", e);
      return;
    }
    
    File smimeFile = filesReceived[0];
    byte[] smimeContent = null;

    try
    {
      smimeContent = SMimeHelper.getBytesFromFile(smimeFile);
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Can't read file from " + smimeFile, e);
      return;
    }

    String contentType = null;
    Enumeration allKeys = header.keys();
    while (allKeys.hasMoreElements())
    {
      String key = (String)allKeys.nextElement();
      if (key.equalsIgnoreCase("Content-Type"))
      {
        contentType = (String)header.get(key);
        break;
      }
    }

    Logger.debug("Content-Type is " + contentType);
    MimeMessage incomingMsg = null;
    try
    {
      incomingMsg = SMimeHelper.createMessage(smimeContent);
    }
    catch(javax.mail.MessagingException e)
    {
      Logger.warn("[AS2MessageHandler.handlerMessage] Can't create message:", e);
      return;
    }

    boolean[] flags = new boolean[3]; //toSendMDN, toSignMDN, failed
    String[] disposition = {PROCESSED, ""}; //dispositionType, errMsg
    Vector micalg = new Vector();
    String digestAlgo = checkMDNOptions(header, flags, disposition, micalg);
    
    //try all possible mappings
    int attempts = certMappings.size();
    int i = 0;
    for (Object o : certMappings)
    {
      i++;
    	boolean sendMDNOnFail = (i == attempts);
    	BizCertMapping certMapping =  (BizCertMapping)o;
      boolean processed = processReceivedMessage(defaultHeader, header, certMapping, incomingMsg, 
                                                 flags, disposition, micalg, digestAlgo, sendMDNOnFail);
      if (processed)
      {
      	Logger.log("Result of processing: Disposition-Type="+disposition[0] + ", error-msg="+disposition[1]);
      	break;
      }
    }
  }
  
  protected boolean processReceivedMessage(String[] defaultHeader, Hashtable header, 
                                        BizCertMapping certMapping, MimeMessage incomingMsg,
                                        boolean[] flags, String[] disposition, Vector micalg, String digestAlgo,
                                        boolean sendMDNonFail)
  {
  	String partnerId = certMapping.getPartnerID();
    X509Certificate ownCert = extractX509Certificate(certMapping.getOwnCert());
    X509Certificate partnerCert = extractX509Certificate(certMapping.getPartnerCert());

    ISMimeDePackager smimeDepackager = null;
    try
    {
      sf = SMimeFactory.newInstance(ownCert, partnerCert);
      Certificate ownGTCert = certMapping.getOwnCert();
      sf.setPrivateKey(getOwnPrivateKey(ownGTCert));
      smimeDepackager = SMimeFactory2.getSMimeDePackager("AS2", sf);
      
    }
    catch(SecurityServiceException e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedMessage] Can't instantiate SMimeFactory", e);
      return false;
    }

    if (digestAlgo != null)
    {
    	sf.setDigestAlgorithm(digestAlgo);
    }
    smimeDepackager.setMessage(incomingMsg);

    MimeMessage unpackedMsg = null;
    String contentType = "";
    try
    {
      unpackedMsg = smimeDepackager.dePackDocument();
      contentType = unpackedMsg.getContentType().toLowerCase().trim();
      
      //NSL20060830 Try to get the payload filename from the Mime part header
      String payloadFilename = unpackedMsg.getFileName(); 
      if (payloadFilename != null && payloadFilename.trim().length()>0)
      {
        header.put(IAS2Headers.PAYLOAD_FILENAME, payloadFilename);
      }
    }
    catch(GNSMimeException e)
    {
      Logger.warn("Error unpacking AS2 message: ", e);
      Logger.warn("Message ignored.");
      short eType = e.getType();
      if (eType == e.GNSMIME_EXCEPTION_DECOMPRESS)
        disposition[1] = "Error: decompression-failed";
      else if (eType == e.GNSMIME_EXCEPTION_DECRYPT)
        disposition[1] = "Error: decryption-failed";
      else if (eType == e.GNSMIME_EXCEPTION_VERIFY)
        disposition[1] = "Error: integrity-check-failed";
      else
        disposition[1] = "Error: unexpected-processing-error";
      if (sendMDNonFail)
      {
      	flags[2] = true;
      	//disposition type should still be "processed", as required by AS2 std
      }
      else
      {
      	return false;
      }
    }
    catch(javax.mail.MessagingException e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedMessage] Can't get content type:", e);
    }

    boolean isRequest;
    if (contentType.startsWith("multipart/report")) //MDN
      isRequest = false;
    else // a request message
      isRequest = true;

    String[] sendResult = sendMDN(header, flags, disposition, micalg, smimeDepackager);
    
    String sentMDNStatus = sendResult[0];
    String receiptAuditFileName = sendResult[1];

    boolean failed = flags[2];
    if (failed) // the message processing failed, ignore the message and return;
      return true;

    String auditFileName = (String)header.get(IAS2Headers.AUDIT_FILE_NAME);

    // a request message
    if (isRequest)
    {
    	processReceivedRequest(defaultHeader, header, partnerId, sentMDNStatus, auditFileName, receiptAuditFileName, unpackedMsg);
    }
    else
    { // an MDN (aka, receipt)
    	processReceivedMDN(unpackedMsg, auditFileName);
    }
    return true;
  }

  protected String[] sendMDN(Hashtable header, boolean[] flags, String[] disposition, Vector micalg, ISMimeDePackager smimeDepackager)
  {
    boolean toSendMDN = flags[0];
    boolean toSignMDN = flags[1];
    boolean failed = flags[2];
    int invalidMICAlg = 0;
    String receiptAuditFileName = "";

    String sentMDNStatus = "MDN sent OK";
    if (toSendMDN)
    {
      try
      {
        if (toSignMDN)
        {
          if (invalidMICAlg == micalg.size())
          {
            //all MIC algorithms are invalid
            Logger.warn("All MIC algorithms are invalid!");
            disposition[0] = "failed";
            disposition[1] = "Failure: unsupported MIC-algorithms";
          }
          else
          {
            if (failed)
            {
              for (int i=0; i<micalg.size(); i++)
              {
                String alg = (String)micalg.get(i);
                if ( alg.equalsIgnoreCase("sha1") || alg.equalsIgnoreCase("md5") || alg.equalsIgnoreCase("sha224")
						 || alg.equalsIgnoreCase("sha256")  || alg.equalsIgnoreCase("sha384")  || alg.equalsIgnoreCase("sha512") )
                {
                  header.put(MICALG, alg.toUpperCase());
                  break;
                }
              }
            }
            else
            {
              //MIC should be set only when message contents are successfully processed
              String digestAlgo = (String)micalg.get(0);
              header.put(MICALG, ((String)micalg.get(0)).toUpperCase());
              
              //TWX 20090804 set the digest algo given the user selected algo under Security Profile
              //RFC 4130: For signed messages, the algorithm used to calculate the MIC MUST be
              //the same as that used on the message that was signed
              smimeDepackager.setDigestAlgorithm(digestAlgo);
              
              int i=1;
//Logger.debug("Before getMessageDigest");
              byte[] digest = smimeDepackager.getMessageDigest();
//Logger.debug("After getMessageDigest, digest is " + digest);
              while ((digest == null) && (i < micalg.size()))
              {
                // try other digest algorithm
                header.put(MICALG, ((String)micalg.get(i)).toUpperCase());
                smimeDepackager.setDigestAlgorithm(((String)micalg.get(i++)).toUpperCase());
                digest = smimeDepackager.getMessageDigest();
              }
              header.put(MIC, digest);
            }
          }
        }
        header.put(DISPOSITION_TYPE, disposition[0]);
        header.put(ERROR, disposition[1]);
        receiptAuditFileName = sendMDN(header);
      }
      catch(Throwable t)
      {
        Logger.warn("[AS2MessageHandler.sendMDN] Error sending MDN", t);
        sentMDNStatus = "Error sending MDN: " + t.getLocalizedMessage();
      }
    }
    return new String[]{sentMDNStatus, receiptAuditFileName};
  }
  
  protected String checkMDNOptions(Hashtable header, boolean[] flags, String[] disposition, Vector micalg)
  {
    boolean toSendMDN = false;
    boolean toSignMDN = false;
    boolean failed = false;
    int invalidMICAlg = 0;
    String protocol = null;
    String protocolImportance = null;
    String micalgImportance = null;
    String digestAlgo = null;

    if (header.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_TO))
    { // MDN requested
      toSendMDN = true;
      try
      {
        //Get the MIC of the incoming message
        String MDNOption = (String)header.get(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS);
        if (MDNOption != null && !MDNOption.equals(""))
        {
          StringTokenizer st = new StringTokenizer(MDNOption, ";");
          while (st.hasMoreTokens())
          {
            String parameter = st.nextToken().trim();
            StringTokenizer ost = new StringTokenizer(parameter, "=");
            if (ost.hasMoreTokens())
            {
              String key = ost.nextToken().trim();
              String value = null;
              if (key.equalsIgnoreCase(IAS2Headers.SIGNED_RECEIPT_PROTOCOL))
              {
                if (ost.hasMoreTokens())
                {
                  value = ost.nextToken();
                  StringTokenizer vst = new StringTokenizer(value, ",");
                  if (vst.hasMoreTokens())
                  {
                    protocolImportance = vst.nextToken().trim();
                    if (vst.hasMoreElements())
                    {
                      protocol = vst.nextToken().trim();
                    }
                  }
                }
              }
              else if (key.equalsIgnoreCase(IAS2Headers.SIGNED_RECEIPT_MICALG))
              {
                if (ost.hasMoreTokens())
                {
                  value = ost.nextToken();
                  StringTokenizer vst = new StringTokenizer(value, ",");
                  if (vst.hasMoreTokens())
                  {
                    micalgImportance = vst.nextToken().trim();
                    if (vst.hasMoreTokens())
                    {
                      while (vst.hasMoreTokens())
                      {
                        String alg = vst.nextToken().trim();
                        if (!alg.equalsIgnoreCase("sha1") &&
                          !alg.equalsIgnoreCase("md5") &&
                          !alg.equalsIgnoreCase("sha256")) //added by Nazir on 10/20/2015
                        {
                          if (alg.equalsIgnoreCase("rsa-sha1")) //backward compatible
                            alg = "sha1";
                          else if (alg.equalsIgnoreCase("rsa-md5")) //backward compatible
                            alg = "md5";
                          else if (alg.equalsIgnoreCase("rsa-sha256")) //backward compatible //added by Nazir on 10192015
                            alg = "sha256";
						  else
                            invalidMICAlg++;
                        }
                        micalg.add(alg);
                      }
                    }
                    else
                    {
                      micalg.add(new String("sha1")); //sha1 is the default algorithm
                    }
                  }
                }
              }
            }
          }
          if (micalg.size() == 0) //the incomign message doesn't specify MIC algorithm
          {
            micalg.add(new String("sha1")); //use the default sha1 algorithm
          }
          if (protocol == null || !protocol.equalsIgnoreCase(
              IAS2Headers.PKCS7_SIGNATURE))
          {
            disposition[0] = "failed";
            disposition[1] = "Failure: unsupported format";
            //do not fail it as per AS2 std recommendation
            //will return an unsigned MDN
          }
          else
          {
            //compute MIC
            //smimeDepackager.setDigestAlgorithm(((String)micalg.get(0)).toUpperCase());
          	digestAlgo = ((String)micalg.get(0)).toUpperCase();
            toSignMDN = true;
          }

        }
      }
      catch(Exception e)
      {
        Logger.warn("Error extracting disposition-notification-options:", e);
        disposition[0] = "failed";
        disposition[1] = "Failure: unable to interpret disposition-notification-options";
        //do not fail it as per AS2 std recommendation
        //will return an unsigned MDN
      }
      
      flags[0] = toSendMDN;
      flags[1] = toSignMDN;
      flags[2] = failed;
      
    }
    return digestAlgo;
  }
  protected void processReceivedRequest(String[] defaultHeader, Hashtable header, String partnerId, String sentMDNStatus,
                                        String auditFileName, String receiptAuditFileName,
                                        MimeMessage unpackedMsg)
  {
    String messageID = (String)header.get(IAS2Headers.MESSAGE_ID);
    if (messageID.startsWith("<") && messageID.endsWith(">"))
      messageID = messageID.substring(1, messageID.length()-1);
    Logger.debug("Received AS2 request message with message-id: "
      + messageID);

    BusinessEntity receiverBE = null;
    try
    {
      //#3588 TWX we will derive the own BE ID from AS2 ID instead.
      //receiverBE = getMyOwnBE();
      String as2To = (String)header.get(IAS2Headers.AS2_TO);
      receiverBE = getOwnBEByDomainId(DomainIdentifier.TYPE_AS2_IDENTIFIER, as2To);
      if(receiverBE == null)
      {
        Logger.debug("Can not locate own BE Id given recipient AS2 ID = "+as2To);
        return;
      }
      
      Logger.debug("recipient = "+as2To+" beId="+receiverBE.getBusEntId());
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedRequest] Error retrieving my own BE", e);
    }

    Partner partner = null;
    try
    {
      partner = getPartner(partnerId);
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedRequest] Error finding partner based on partner ID:" + partnerId, e);
    }

    BusinessEntity senderBE = null;
    try
    {
      senderBE = getPartnerBE(partner);
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedRequest] Error getting partner BE", e);
    }

    //check if the message with the same messageID from the same partner has been
    // received before
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GridDocument.USER_TRACKING_ID,
      filter.getEqualOperator(),
      messageID,
      false
    );
    filter.addSingleFilter(
      filter.getAndConnector(),
      GridDocument.FOLDER,
      filter.getEqualOperator(),
      GridDocument.FOLDER_INBOUND,
      false
    );
    String senderBEID = null;
    if (senderBE != null)
      senderBEID = senderBE.getBusEntId();

    filter.addSingleFilter(
      filter.getAndConnector(),
      GridDocument.S_BIZ_ENTITY_ID,
      filter.getEqualOperator(),
      senderBEID,
      false
    );

    IDocumentManagerObj docMgr = null;
    
    Collection gDocList = null;
    try
    {
      docMgr = ReceiveDocumentHandler.getDocumentManager();
      gDocList = docMgr.findGridDocuments(filter);
    }
    catch(Exception e)
    {
      Logger.warn("[AS2MessageHandler.processReceivedRequest] Error find GridDocument", e);
      return;
    }

    String AS2Status = null;
    if (!sentMDNStatus.startsWith("Error"))
      AS2Status = PROCESSED;
    else
      AS2Status = "Failed - " + sentMDNStatus;
    if (gDocList != null && !gDocList.isEmpty())
    { // this message was received before. The incoming is a resend.
      Iterator itr = gDocList.iterator();
      GridDocument gDoc = (GridDocument)itr.next();
      /*
      String AS2Status = null;
      if (!sentMDNStatus.startsWith("Error"))
        AS2Status = PROCESSED;
      else
        AS2Status = "Failed - " + sentMDNStatus;
      gDoc.setDocTransStatus(AS2Status);*/
      try
      {
        //GridDocumentEntityHandler.getInstance().update(gDoc);
        gDoc = docMgr.getCompleteGridDocument(gDoc);
        gDoc.setDocTransStatus(AS2Status);
        docMgr.updateGridDocument(gDoc);
      }
      catch(Throwable t)
      {
        Logger.warn("[AS2MessageHandler.processReceivedRequest] Error updating GridDocument", t);
      }
      return; //don't create gDoc for duplicate messages.
    }

    //create gDoc
    GridDocument gDoc = new GridDocument();
    gDoc.setUserTrackingID(messageID);
    gDoc.setAuditFileName(auditFileName);
    gDoc.setReceiptAuditFileName(receiptAuditFileName);
    
    //#44 TWX 21052008 : set tracing ID for txmr
    String tracingID = defaultHeader[ChannelReceiveHeader.TRACING_ID];
    if(tracingID != null)
    {
      gDoc.setTracingID(tracingID);
    }
    
    //senderInfo
    if (senderBE != null)
    {
      gDoc.setSenderBizEntityId(senderBE.getBusEntId());
      String enterpriseId = senderBE.getEnterpriseId();
      gDoc.setSenderNodeId(enterpriseId==null? null: new Long(enterpriseId));
    }
    if (partner != null)
    {
      gDoc.setSenderPartnerGroup(partner.getPartnerGroup()==null? null:partner.getPartnerGroup().getEntityName());
      gDoc.setSenderPartnerId(partner.getPartnerID());
      gDoc.setSenderPartnerType(partner.getPartnerType()==null? null: partner.getPartnerType().getEntityName());
      gDoc.setSenderPartnerName(partner.getEntityName());
    }

    //recipientInfo
    if (receiverBE != null)
    {
      gDoc.setRecipientBizEntityId(receiverBE.getBusEntId());
      gDoc.setRecipientNodeId(new Long(receiverBE.getEnterpriseId()));
    }

    try
    {
      gDocList = null;
      File[] files = new File[2];

      byte[] udocContent = SMimeHelper.getContentBytesFromMime(unpackedMsg);

      //gDoc.setUdocDocType(getUDocType(unpackedMsg, smimeDepackager, header, udocContent.length));
      //gDoc.setUdocDocType("AS2 Message");
      
      //WYW 20081112 : extract as2 document type from AS2 message, and use the extracted AS2 document type and partner id 
      //               match with AS2DocTypeMapping, inorder to get the internal document type and set it to the udoc doc type
      //               . if mapping not found for that AS2 document type and partnerid. "AS2 Message" will be use for the
      //               internal document type
      gDoc.setUdocDocType(getInternalDocType(extractAs2DocType(new String(udocContent)),partner.getPartnerID()));

      //NSL20060830 Set udoc filename from header, if found
      String payloadFilename = (String)header.get(IAS2Headers.PAYLOAD_FILENAME);
      if (payloadFilename == null)
      {
        payloadFilename = "AS2in"+String.valueOf(random.nextInt())+".udoc";
      }
      
      //String prefix = String.valueOf(random.nextInt());

      //File tempUdoc = File.createTempFile("AS2in" + prefix, ".udoc");
      
      String tempFolder = "AS2in"+random.nextInt()+"/";
      ByteArrayInputStream bais = new ByteArrayInputStream(udocContent);
      FileUtil.create(IDocumentPathConfig.PATH_TEMP, tempFolder, payloadFilename, bais, true);
      File tempUdoc = FileUtil.getFile(IDocumentPathConfig.PATH_TEMP, tempFolder, payloadFilename);
      //FileOutputStream fos = new FileOutputStream(tempUdoc);
      //fos.write(udocContent);
      //fos.close(); //NSL20060609 must close the file! otherwise 0 bytes will be copied to Inbound folder
      tempUdoc.deleteOnExit();

      files[0] = tempUdoc;

      //prefix = String.valueOf(random.nextInt());
      //File tempGdoc = File.createTempFile(prefix, ".xml");
      String gdocfn = String.valueOf(random.nextInt())+".xml";
      FileUtil.createNewLocalFile(IDocumentPathConfig.PATH_TEMP, tempFolder, gdocfn);
      File tempGdoc = FileUtil.getFile(IDocumentPathConfig.PATH_TEMP, tempFolder, gdocfn);
      String gdocFullPath = tempGdoc.getAbsolutePath();
      gDoc.serialize(gdocFullPath);
      files[1] = tempGdoc;
      tempGdoc.deleteOnExit();

      ReceiveDocumentHandler receiveHandler = new ReceiveDocumentHandler();
      receiveHandler.handlerMessage(defaultHeader, null, files, null);
      String receiveDocStatus = receiveHandler.getReceiveStatus();
      //String AS2Status = null;
      if (!receiveDocStatus.startsWith("Error") && !sentMDNStatus.startsWith("Error"))
        AS2Status = PROCESSED;
      else
        AS2Status = "Failed - " + receiveDocStatus + ";" + sentMDNStatus;

      //IDocumentManagerObj mgr = receiveHandler.getDocumentManager();

      gDocList = docMgr.findGridDocuments(filter);
      if (gDocList != null && !gDocList.isEmpty())
      {
        Logger.debug("gDocList is not null! ");
        Iterator itr = gDocList.iterator();
        GridDocument gdoc = (GridDocument)itr.next();
        gdoc = docMgr.getCompleteGridDocument(gdoc); //NSL20060830
        gdoc.setDocTransStatus(AS2Status);
        gdoc.setDateTimeTransComplete(new Date(TimeUtil.localToUtc()));
        //GridDocumentEntityHandler.getInstance().update(gdoc);
        //String gDocFullPath = FileHelper.getGdocFile(gdoc).getAbsolutePath();
        //gdoc.serialize(gDocFullPath);
        docMgr.updateGridDocument(gdoc); //NSL20060830
      }
    }
    catch(Throwable t)
    {
      Logger.warn("[AS2MessageHandler.processReceivedRequest] Error receiving document:", t);
      /*
      IDocumentManagerObj mgr = null;
      try
      {
        mgr = ReceiveDocumentHandler.getDocumentManager();
      }
      catch(Exception e)
      {
        Logger.err("Error getting Document Manager", e);
        return;
      }
      */
      try
      {
        gDocList = docMgr.findGridDocuments(filter);
      }
      catch(Exception e)
      {
        Logger.warn("Error finding griddocument on filter: " + filter, e);
        return;
      }
      if (gDocList != null && !gDocList.isEmpty())
      {
        try
        {
          Iterator itr = gDocList.iterator();
          GridDocument gdoc = (GridDocument)itr.next();
          gdoc = docMgr.getCompleteGridDocument(gdoc); //NSL20060830
          gdoc.setDocTransStatus("Failed - " + t.getLocalizedMessage());
          gdoc.setDateTimeTransComplete(new Date(TimeUtil.localToUtc()));
          //GridDocumentEntityHandler.getInstance().update(gdoc);
          docMgr.updateGridDocument(gdoc); //NSL20060830
        }
        catch(Throwable t2)
        {
          Logger.warn("[AS2MessageHandler.processReceivedRequest] Error updating incoming AS2 document status", t2);
        }
      }

    }  	
  }
  
  protected void processReceivedMDN(MimeMessage unpackedMsg, String auditFileName)
  {
    String origMsgID = null;

    byte[] msgContent = null;
    /**
     * @todo handle error messages in MDN. For now they are ignored.
     */
    try
    {
      MimeMultipart content = (MimeMultipart)unpackedMsg.getContent();

      //get the second part of the MDN, the machine-readable part
      MimeBodyPart bodyPart = (MimeBodyPart)content.getBodyPart(1);
      msgContent = (byte[])bodyPart.getContent();

      //Logger.debug("object type is " + msgContent.getClass().getName());
      //extract the original message ID
      origMsgID = extractOrigMsgID(new ByteArrayInputStream(msgContent));
      Logger.debug("Received an MDN with Original-Message-ID: " + origMsgID);
    }
    catch(Exception e)
    {
      Logger.warn("Error extracting Original-Message-Id:", e);
      return;
    }
    
    updateRequestGridDoc(origMsgID, auditFileName, msgContent);
  	
  }
  
  protected void updateRequestGridDoc(String origMsgID, String auditFileName, byte[] msgContent)
  {
  	DataFilterImpl filter;
    try
    {
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridDocument.USER_TRACKING_ID,
        filter.getEqualOperator(), origMsgID, false);

      Collection gDocList = null;
      GridDocument gDoc = null;

      for (int i=0; i<MAXIMUM_SLEEP; i++)
      {
        //gDocList = GridDocumentEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
        gDocList = ReceiveDocumentHandler.getDocumentManager().findGridDocuments(filter);
        if ((gDocList != null) && (!gDocList.isEmpty()))
        {
          Iterator itr = gDocList.iterator();
          gDoc = (GridDocument)itr.next();
          String _status = gDoc.getDocTransStatus();
          String _audit = gDoc.getAuditFileName();
          Date dateTimeSentEnd = gDoc.getDateTimeSendEnd(); //TWX 20090611 #472
          
          if (!_status.startsWith("Failed") &&
              (dateTimeSentEnd == null)) 
          {
            //feedback handler hasn't updated this gDoc yet.
            Thread.currentThread().sleep(SLEEP_LENGTH); //wait for a while
          }
          else
            break;  //feedback handler has updated the gDoc. Proceed.
        }
        else
        {
          Logger.warn("Can't find the original GridDocument with Message-ID: " +
            origMsgID);
          return;
        }
      }

      //NSL20060830
      IDocumentManagerObj mgr = ReceiveDocumentHandler.getDocumentManager();
      
      Logger.log("[AS2MessageHandler.updateRequestGridDoc] Refetch for gdoc is disable. MsgDigest is "+gDoc.getMessageDigest());
      //gDoc = mgr.getCompleteGridDocument(gDoc);
      
      gDoc.setReceiptAuditFileName(auditFileName);
      ByteArrayInputStream bis = new ByteArrayInputStream(msgContent);
      gDoc.setDocTransStatus(extractStatus(bis));
      String origMessageDigest = gDoc.getMessageDigest();
      String returnedMessageDigest = extractMessageDigest(
        new ByteArrayInputStream(msgContent));
      if (origMessageDigest != null && origMessageDigest.length() > 0)
      {
        if (origMessageDigest.equals(returnedMessageDigest))
          Logger.debug("Returned message digest matches with original one.");
        else
        {
          String msg = "Error: Returned message digest doesn't match with original one."
                + " Expected: [" + origMessageDigest + "], received: [" + returnedMessageDigest
                + "]";
          Logger.debug(msg);
          gDoc.setDocTransStatus(gDoc.getDocTransStatus() + " " + msg);
        }
      }
      else
      {
        Logger.debug("Original message digest is empty, no need to compare message digests.");
      }
      
      mgr.handleDocAccepted(gDoc, null, null, false, true);
      
      //#20: moved up from the commented block
      //TWX 02102007 Trigger alert indicate the TP has received the document (it doesn't mean the doc has been processed successfully by TP)
      AlertDelegate.raiseDocReceivedByPartnerAlert(gDoc);
      
      /* TWX the rest will be handled by handleDocAccepted
      //update the gdoc transaction status
      gDoc.setDateTimeTransComplete(new Date(TimeUtil.localToUtc()));
      try
      {
        //GridDocumentEntityHandler.getInstance().update(gDoc);
        //String gDocFullPath = FileHelper.getGdocFile(gDoc).getAbsolutePath();
        //gDoc.serialize(gDocFullPath);
        
        mgr.updateGridDocument(gDoc); //NSL20060830
        

      }
      catch(Throwable t)
      {
        Logger.err("Error updating original request document with Message-ID:" +
              origMsgID, t);
      } */
    }
    catch(Exception e)
    {
      Logger.warn("Error finding the original GridDoucment with Message-ID:" +
        origMsgID, e);
    }

  }
  protected String sendMDN(Hashtable origHeader) throws Exception
  {
    String origMsgID = (String)origHeader.get(IAS2Headers.MESSAGE_ID);
    String origSender = (String)origHeader.get(IAS2Headers.AS2_FROM);
    String origReceiver = (String)origHeader.get(IAS2Headers.AS2_TO);
    String partOne =
      "MDN for - \r\n" +
      "  Message-Id: " + origMsgID + "\r\n" +
      "  From: " + origSender  + "\r\n" +
      "  To: " + origReceiver + "\r\n\r\n" +
      "Status: processed\r\n" +
      "Comment: This is not a guarantee that the message has been completely\r\n" +
      "processed or understood by the receiving translator.\r\n";

    String reportingUA = "GridTalk Application Server";

    String finalRecipient = "AS2;" + origSender;

    /**
     * @todo as2-version and host
     */
    String disposition = "automatic-action/MDN-sent-automatically; "
      + (String)origHeader.get(DISPOSITION_TYPE);

    String errMsg = (String)origHeader.get(ERROR);
    if (errMsg != null && !errMsg.equals(""))
    {
      Logger.debug("Sending error MDN. Error is " + errMsg);
      disposition = disposition + "/" + errMsg;
    }

    String partTwo =
      "Reporting-UA: " + reportingUA + "\r\n" +
      "Original-Recipient: " + finalRecipient + "\r\n" +
      "Final-Recipient: " + finalRecipient + "\r\n" + //it should be the same as original-recipient, as specified by AS2.
      "Original-Message-ID: " + origMsgID + "\r\n" +
      "Disposition: " + disposition + "\r\n";

    byte[] digest = (byte[])origHeader.get(MIC);
    if (digest != null && digest.length>0)
    {
      String digestStr = GridCertUtilities.encode(digest);
      partTwo = partTwo + "Received-content-MIC: " + digestStr + "," +
        ((String)origHeader.get(MICALG)).toLowerCase() + "\r\n";
    }
//Logger.debug("sendMDN: before getPackager");
    ISMimePackager smimePackager = SMimeFactory2.getSMimePackager("AS2", sf);
//Logger.debug("sendMDN: after getPackager");
    MimeBodyPart bodyPartOne = SMimeHelper.createPart(partOne.getBytes(), "text/plain");
//Logger.debug("sendMDN: after createPartOne");
    SMimeHelper.setContentEncoding(bodyPartOne, SMimeHelper.ENCODING_7BIT);
    MimeBodyPart bodyPartTwo = SMimeHelper.createPart(partTwo.getBytes(), "message/disposition-notification");
//Logger.debug("sendMDN: after createPartTwo");
    SMimeHelper.setContentEncoding(bodyPartTwo, SMimeHelper.ENCODING_7BIT);

    MimeMultipart multipart = new MimeMultipart("report; Report-Type=disposition-notification");
    multipart.addBodyPart(bodyPartOne);
    multipart.addBodyPart(bodyPartTwo);
    MimeBodyPart finalPart = SMimeHelper.createPart(multipart);
//Logger.debug("sendMDN: after create final part");

    smimePackager.setContent(finalPart);
    String digestAlg = (String)origHeader.get(MICALG);
    if (digestAlg != null && digestAlg.length()>0)
    {
      smimePackager.appendAction(smimePackager.ACTION_SIGN);
      smimePackager.setActionProperty(smimePackager.ACTION_SIGN,
        smimePackager.SCOPE_ALL);
      sf.setDigestAlgorithm(digestAlg);
    }
//Logger.debug("sendMDN: Before packDocument");
    MimeMessage MDNmsg = smimePackager.packDocument();
//Logger.debug("sendMDN: after packDocument");
    String contentType = MDNmsg.getContentType();
    Enumeration allHeaders = MDNmsg.getAllHeaders();
    while (allHeaders.hasMoreElements())
    {
      Header aHeader = (Header)allHeaders.nextElement();
      MDNmsg.removeHeader(aHeader.getName());
    }
//Logger.debug("sendMDN: Before new bytearray");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//Logger.debug("sendMDN: after new bytearray");
    MDNmsg.writeTo(bos);
//Logger.debug("sendMDN: after writeTo");
    byte[] packedMDN = bos.toByteArray();
//Logger.debug("sendMDN: after toByteArray");
    // generate headers
    Hashtable header = new Hashtable();

    header.put(IAS2Headers.CONTENT_TYPE, contentType);
    header.put(IAS2Headers.AS2_VERSION, "1.1");
    header.put(IAS2Headers.AS2_FROM, origReceiver);
    header.put(IAS2Headers.AS2_TO, origSender);

    String msgID = GNMimeUtility.getUniqueMessageIDValue();
    if (msgID.startsWith("<") && msgID.endsWith(">"))
    {
    }
    else
    {
      msgID = "<" + msgID + ">";  //AS2 spec requires Message-ID to be wrapped with "<" and ">"
    }

    Logger.debug("[AS2MessageHandler.sendMDN] setting msgID to " + msgID);

    header.put(IAS2Headers.MESSAGE_ID, msgID);
    header.put(IAS2Headers.SUBJECT, "AS2 MDN");

    Date now = new Date();
    SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z");
    df.setTimeZone(TimeZone.getTimeZone("GMT")); // AS2 should use GMT

    header.put(IAS2Headers.DATE, df.format(now));
    if (origHeader.containsKey(IAS2Headers.RECEIPT_DELIVERY_OPTION))
      header.put(MDN, "async");
    else
    {
      header.put(MDN, "sync");
      header.put(IAS2Headers.ORIGINAL_MESSAGE_ID, origMsgID);
    }

    // invoke Transport to send the MDN
    String returnURL = (String)origHeader.get(IAS2Headers.RECEIPT_DELIVERY_OPTION);
    if ((returnURL == null) || (returnURL.length() == 0 ))
      returnURL = "http://www.dummy.com:8080/AS2";  // this URL only serves to initialize the HttpCommInfo underneath
    HttpCommInfo commInfo = new HttpCommInfo();
    commInfo.setURL(returnURL);

    Enumeration keys = header.keys();
    while (keys.hasMoreElements())
    {
      //remove newlines as folded headers are not allowed in AS2
      String key = (String)keys.nextElement();
      String value = (String)header.get(key);
      StringTokenizer st = new StringTokenizer(value, "\r\n");
      String newValue = "";
      while (st.hasMoreTokens())
      {
        newValue = newValue + st.nextToken();
      }
      header.put(key, newValue);
    }

    ITransportServiceObj transportMgr = lookupTransportServiceMgr();
    Logger.debug("Sending MDN...");
    com.gridnode.pdip.framework.messaging.Message theMessage =
      new com.gridnode.pdip.framework.messaging.Message();
    theMessage.setCommonHeaders(new Hashtable());
    theMessage.setMessageHeaders(header);
    theMessage.setPayLoad(packedMDN);
    transportMgr.send(commInfo, theMessage);
    return ChannelServiceDelegate.writeToAudit(header, packedMDN, "AS2/", "outMDN");
  }

  private BusinessEntity getMyOwnBE() throws Exception
  {
    return BizRegDelegate.getDefaultBusinessEntity();
  }

  private BusinessEntity getOwnBEByDomainId(String domainType, String domainId) throws Exception
  {
    return BizRegDelegate.getBizRegistryManager().findBusinessEntityByDomainIdentifier(domainType, domainId);
  }
  
  private Partner getPartner(String partnerID) throws Exception
  {
    return BizRegDelegate.getPartnerManager().findPartnerByID(partnerID);
  }

  private BusinessEntity getPartnerBE(Partner partner) throws Exception
  {
    Long bizEntUid = BizRegDelegate.getEnterpriseHierarchyManager().getBizEntityForPartner(
                          new Long(partner.getUId()));
    if (bizEntUid != null)
    {
      return	BizRegDelegate.getBizRegistryManager().findBusinessEntity(bizEntUid);
    }
    return null;
  }

  public ITransportServiceObj lookupTransportServiceMgr() throws Exception
  {
  	/*
    ITransportServiceHome tptCntrlHome = (ITransportServiceHome)ServiceLookup.getInstance(
                    ServiceLookup.LOCAL_CONTEXT).getHome(
                    ITransportServiceHome.class);
    return tptCntrlHome.create();
    */
  	return (ITransportServiceObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
  	                ITransportServiceHome.class.getName(),
  	                ITransportServiceHome.class,
  	                new Object[0]);
  }

  public IPartnerProcessManagerObj getPartnerProcessManager() throws ServiceLookupException
  {
    return (IPartnerProcessManagerObj) ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerProcessManagerHome.class.getName(),
      IPartnerProcessManagerHome.class,
      new Object[0]);
  }

  private X509Certificate extractX509Certificate(Certificate cert)
  {
    if (cert == null)
      return null;
    Logger.debug("[extractX509Certificate] cert uid is " + cert.getUId());
    X509Certificate X509cert= GridCertUtilities.loadX509CertificateByString(cert.getCertificate());
    return X509cert;
  }

  private String extractOrigMsgID(InputStream inputStream) throws Exception
  {
    String origMsgID = "";
    Properties prop = new Properties();
    prop.load(inputStream);
    Enumeration allKeys = prop.keys();
    while (allKeys.hasMoreElements())
    {
      String key = (String)allKeys.nextElement();
      if (key.equalsIgnoreCase(IAS2Headers.ORIGINAL_MESSAGE_ID))
      {
        origMsgID = ((String)prop.get(key)).trim();
        break;
      }
    }
    if (origMsgID != null && !origMsgID.equals(""))
    {
      if (origMsgID.startsWith("<") && origMsgID.endsWith(">"))
        origMsgID = origMsgID.substring(1, origMsgID.length()-1);
    }
    return origMsgID;
  }

  private String extractStatus(InputStream inputStream) throws Exception
  {
    String status = "";
    Properties prop = new Properties();
    prop.load(inputStream);
    Enumeration allKeys = prop.keys();
    while (allKeys.hasMoreElements())
    {
      String key = (String)allKeys.nextElement();
      if (key.equalsIgnoreCase(IAS2Headers.DISPOSITION))
      {
        status = ((String)prop.get(key)).trim();
        break;
      }
    }
    if (status != null && !status.equals(""))
    {
      int index = status.indexOf(";");
      if (index > 0 && index+1 < status.length())
      {
        return status.substring(index+1).trim();
      }
    }
    return "unknown";
  }

  private String extractMessageDigest(InputStream inputStream) throws Exception
  {
    String digest = "";
    Properties prop = new Properties();
    prop.load(inputStream);
    Enumeration allKeys = prop.keys();
    while (allKeys.hasMoreElements())
    {
      String key = (String)allKeys.nextElement();
      if (key.equalsIgnoreCase(IAS2Headers.RECEIVED_CONTENT_MIC))
      {
        digest = ((String)prop.get(key)).trim();
        break;
      }
    }
    if (digest != null && !digest.equals(""))
    {
      int index = digest.indexOf(",");
      if (index > 0)
      {
        return digest.substring(0,index).trim();
      }
    }
    return "";
  }

  public String convertFileType2ContentType(String fileType)
  {
    String contentType = config.getString(fileType);
    if (contentType == null || contentType.equals(""))
    {
      //return "text/plain";  //default content type
      return GNMimeUtility.GENERIC_CONTENT_TYPE; //NSL20060908 use generic content type
    }
    return contentType;
  }

  private String getAS2Identifier(BusinessEntity be) throws Exception
  {
    Collection domainIds = be.getDomainIdentifiers();
    if (domainIds != null )
    {
      Iterator itr2 = domainIds.iterator();
      while (itr2.hasNext())
      {
        DomainIdentifier domain = (DomainIdentifier)itr2.next();
        if (domain.getType().equalsIgnoreCase(
          DomainIdentifier.TYPE_AS2_IDENTIFIER))
        {
          return domain.getValue();
        }
      }
    }
    return null;
  }

  public void resend(GridDocument gDoc, String channelUID) throws Throwable
  {
    //retrieve the audit file
    String auditFileName = gDoc.getAuditFileName();

    int index = auditFileName.indexOf('/');

    File auditFile = null;

    if (index >= 0)
      auditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT,
                                    auditFileName.substring(0, index + 1),
                                    auditFileName.substring(index+1));
    if (!auditFile.exists())
      throw new Exception("[AS2MessageHandler.resend] cannot find audit file: " +
        auditFileName);

    MimeMessage msg = SMimeHelper.createMessage(new FileInputStream(auditFile));
    Hashtable header = new Hashtable();

    Enumeration allHeaders = msg.getAllHeaders();
    while (allHeaders.hasMoreElements())
    {
      Header aHeader = (Header)allHeaders.nextElement();
      header.put(aHeader.getName(), aHeader.getValue());
      msg.removeHeader(aHeader.getName());
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    msg.writeTo(bos);
    byte[] tempMsgBytes = bos.toByteArray();
    byte[] msgBytes = new byte[tempMsgBytes.length - 2];

    //remove the leading "\r\n" characters
    System.arraycopy(tempMsgBytes, 2, msgBytes, 0, msgBytes.length);

    ChannelInfo channelInfo = ChannelDelegate.getChannelInfo(new Long(channelUID));
    if (channelInfo == null)
      throw new Exception("[AS2MessageHandler.resend] cannot find channel with UID: " +
        channelUID);

    CommInfo commInfo = channelInfo.getTptCommInfo();

    HttpCommInfo httpCommInfo = new HttpCommInfo();
    httpCommInfo.setURL(commInfo.getURL());

    ITransportServiceObj transportMgr = lookupTransportServiceMgr();
    Logger.debug("[AS2MessageHandler.resend] resending GDoc UID = " + gDoc.getKey()
      + "...");

    com.gridnode.pdip.framework.messaging.Message theMessage =
      new com.gridnode.pdip.framework.messaging.Message();
    theMessage.setCommonHeaders(new Hashtable());
    theMessage.setMessageHeaders(header);
    theMessage.setPayLoad(msgBytes);

    transportMgr.send(httpCommInfo, theMessage);
  }
  
  
  //WYW 20081112 : extract AS2 document type fromt the as2Message
  private String extractAs2DocType(String msgContent)
  {
    String rDocType = "Unknown document type...";
    String dSaperator  = "";
    String cSaperator  = "";
    int DocTypeFromIndex = 0;
    int DocTypeToIndex  = 0;
    int UNHIndex        = 0;
    int STIndex         = 0;
    
    //System.out.println("full string :"+msgContent);
    
    try {
	    if(msgContent.length()>10)
	    {
		    if(msgContent.indexOf("ISA") != -1 || msgContent.indexOf("GS") != -1 || msgContent.indexOf("BAK") != -1){
		      //System.out.println("AS2 Type : X12");
		      //System.out.println("ST index : "+msgContent.indexOf("ST"));
		      
		      STIndex = msgContent.indexOf("ST");
		      
		      dSaperator = msgContent.substring(3,4);
		      DocTypeFromIndex = msgContent.indexOf(dSaperator,STIndex)+1;
		      DocTypeToIndex = msgContent.indexOf(dSaperator,STIndex+4);
		        
		      //System.out.println("Document Type :"+msgContent.substring(DocTypeFromIndex, DocTypeToIndex));
		      if(DocTypeFromIndex < DocTypeToIndex)
		    	  rDocType = msgContent.substring(DocTypeFromIndex, DocTypeToIndex);
		    }else if(msgContent.indexOf("UNA") != -1 || msgContent.indexOf("UNB") != -1 || msgContent.indexOf("UNH") != -1){
		      //System.out.println("AS2 Type : EDI");
		      //System.out.println("UNH index : "+msgContent.indexOf("UNH"));
		      
		      UNHIndex = msgContent.indexOf("UNH");
		      
		      if(msgContent.indexOf("UNA")!=-1)
		      {
		    	  cSaperator = msgContent.substring(3,4);
		    	  dSaperator = msgContent.substring(4,5);
		      }else{
		    	  cSaperator = ":";
		    	  dSaperator = "+";
		      }
		      
		      DocTypeFromIndex = msgContent.indexOf(dSaperator,UNHIndex+4)+1;
		      DocTypeToIndex = msgContent.indexOf(cSaperator,UNHIndex+1);
		      
		      //System.out.println("Document Type :"+msgContent.substring(DocTypeFromIndex, DocTypeToIndex));
		      if(DocTypeFromIndex < DocTypeToIndex)
		    	  rDocType = msgContent.substring(DocTypeFromIndex, DocTypeToIndex);
		    }
	    }
    } catch (Exception e) {
        e.printStackTrace();
        //System.out.println("exp return doctype : "+rDocType);
        return rDocType;
    }
    
    //System.out.println("return doctype : "+rDocType);
    return rDocType;
  }
  
  
//WYW 20081112 : search for the internal document type with the as2 Doc Type and partner Id
  private String getInternalDocType(String as2DocType, String partnerId)
  {
    String intDocType = "AS2 Message";
    
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AS2DocTypeMapping.AS2_DOC_TYPE, filter.getEqualOperator(),
                           as2DocType, false);
    filter.addSingleFilter(filter.getAndConnector(), AS2DocTypeMapping.PARTNER_ID, filter.getEqualOperator(),
                           partnerId, false);
    try{
      Collection certs = getDocumentManager().findAS2DocTypeMappingByFilter(filter);
      for (Iterator i=certs.iterator(); i.hasNext(); )
      {
        AS2DocTypeMapping as2 = (AS2DocTypeMapping)i.next();
        intDocType = as2.getDocType();
      }
    }
    catch(Exception e)
    {
      Logger.err("Error finding as2DocTypeMapping on filter: " + filter, e);
      return intDocType;
    }
    
    return intDocType;
  }
  
  
  private IDocumentManagerObj getDocumentManager() throws Exception
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
        IDocumentManagerHome.class.getName(),
        IDocumentManagerHome.class,
        new Object[0]);
  }

  private PrivateKey getOwnPrivateKey(Certificate ownGTCert)
  {
    return GridCertUtilities.loadPrivateKeyFromString(ownGTCert.getPrivateKey());
  }
  
  /**
   * This function is written solely for identifying incoming test cases of the
   * AS2 interop test. It should not be used any longer after the interop test ends.
   *//*
  private String getUDocType(MimeMessage msg, ISMimeDePackager smimeDepackager,
      Hashtable header, int msgLength)
  {
    int dataNum = 0; // which test data is used
    int megaBytes = 1024*1024;

    if (msgLength > 40*megaBytes) // bigger than 40M
      dataNum = 5;  //test data 5
    else if (msgLength > megaBytes) //bigger than 1M
      dataNum = 4;  //test data 4
    else
    {
      try
      {
        byte[] contentBytes = SMimeHelper.getContentBytesFromMime(msg);
       String content = new String(contentBytes).trim();
        if (content.startsWith("ISA*00*"))
          dataNum = 1;  //test data 1
        else if(content.startsWith("UNA:+.?"))
          dataNum = 2;  //test data 2
        else if(content.startsWith("<?xml"))
          dataNum = 3;  //test data 3
      }
      catch(Exception e)
      {
        Logger.err("Error telling test data number", e);
        return "AS2_DOC";
      }
    }

    Vector actions = smimeDepackager.getActions();
    String actionStr = "";

    for (int i=actions.size()-1; i>=0; i--)
    {
      String action = (String)actions.get(i);
      String temp = null;
      if (smimeDepackager.ACTION_SIGN.equals(action))
        temp = "S";
      else if (smimeDepackager.ACTION_ENCRYPT.equals(action))
        temp = "E";
      else if (smimeDepackager.ACTION_COMPRESS.equals(action))
        temp = "C";
      if (actionStr.length() == 0)
        actionStr = temp;
      else
        actionStr = actionStr + ";" + temp;
    }

    boolean signMDN = header.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_OPTIONS);
    boolean isSync = !header.containsKey(IAS2Headers.RECEIPT_DELIVERY_OPTION);
    boolean isSSL = false;

    if (!isSync)
    {
      String url = ((String)header.get(IAS2Headers.RECEIPT_DELIVERY_OPTION)).trim().toLowerCase();
      if (url.startsWith("https"))
        isSSL = true;
    }

    Logger.debug("getUDocType(): test data " + dataNum + ", action=" + actionStr +
      ", signMDN = " + signMDN + ", isSync=" + isSync + ", isSSL = " + isSSL);

    if ((dataNum == 1) && actionStr.equals("S;E") && (signMDN == false) && (isSync == true))
      return "AS2_A";
    if ((dataNum == 2) && actionStr.equals("S;E") && (signMDN == true) && (isSync == true))
      return "AS2_B";
    if ((dataNum == 3) && actionStr.equals("S;E") && (signMDN == true) && (isSync == false) &&
      (isSSL == true))
      return "AS2_C";
    if ((dataNum == 3) && (actionStr.equals("C;E") || actionStr.equals("E;C")) &&
      (signMDN == true) && (isSync == true) )
      return "AS2_D";
    if ((dataNum == 2) && actionStr.equals("E") && (signMDN == true) && (isSync == true))
      return "AS2_E";
    if ((dataNum == 2) && actionStr.equals("S") && (signMDN == true) && (isSync == true))
      return "AS2_F";
    if ((dataNum == 3) && (actionStr.equals("C;S") || actionStr.equals("S;C")) &&
      (signMDN == true) && (isSync == true))
      return "AS2_G";
    if ((dataNum == 1) && actionStr.equals("S") && (signMDN == true) &&
      (isSync == false) && (isSSL == false))
      return "AS2_H";
    if ((dataNum == 4) && actionStr.equals("S") && (signMDN == true) &&
      (isSync == false) && (isSSL = true))
      return "AS2_I";
    if ((dataNum == 5) && (actionStr.equals("C;S;E") || actionStr.equals("S;C;E") || actionStr.equals("S;E;C"))
      && (signMDN == true) && (isSync == false) && (isSSL == false))
      return "AS2_J";

    return "AS2_DOC"; // default udoc type
  }*/
}
