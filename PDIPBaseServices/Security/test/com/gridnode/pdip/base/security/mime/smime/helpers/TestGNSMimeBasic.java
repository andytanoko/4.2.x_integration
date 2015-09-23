/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestGNSMimeBasic.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 30, 2006   i00107           Created
 * 08 Jun 2009    Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.security.mime.smime.helpers;

import java.io.File;
import java.security.cert.X509Certificate;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.security.mime.smime.ISMimeDePackager;
import com.gridnode.pdip.base.security.mime.smime.ISMimePackager;
import com.gridnode.pdip.base.security.mime.smime.SMimeFactory2;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class TestGNSMimeBasic
{

  public static void main(String[] args) throws Exception
  {

    ICertificateManagerObj certMgr = (ICertificateManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ICertificateManagerHome.class.getName(), ICertificateManagerHome.class, new Object[0]);
    
    MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
    mc.addMailcap("application/xml;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edifact;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-x12;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-consent;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("message/disposition-notification;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/*;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    CommandMap.setDefaultCommandMap(mc);
    
    boolean toMIME = true;
    File encFile = new File("d:\\temp\\sample_enc.dat");
    
    if (toMIME)
    {
      Certificate cert = certMgr.findCertificateByIDAndName(520, "GridTalk");
      X509Certificate signCert = certMgr.getX509Certificate(cert);
      Certificate partnerCert = certMgr.findCertificateByIDAndName(0, "519 Cert");
      X509Certificate encCert = certMgr.getX509Certificate(partnerCert);
      
      byte[] content = SMimeHelper.getBytesFromFile("d:\\temp\\sample.zip");
      //byte[] content = SMimeHelper.getBytesFromFile("c:\\data1.dat");
      MimeBodyPart msg = SMimeHelper.createPart(content, "application/octet-stream");
      msg.setFileName("sample.zip");
      SMimeFactory fac = SMimeFactory.newInstance(signCert,encCert,"RC2/CBC/PKCS5Padding","SHA1",40);
      //SMimeFactory fac = SMimeFactory.newInstance(null,null,"RC2/CBC/PKCS5Padding","SHA1",40);
      ISMimePackager smime = SMimeFactory2.getSMimePackager("AS2",fac);
      smime.setPKCS7Encoding(SMimeHelper.ENCODING_BINARY);
      //fac.setCompressLevel(9);
      smime.appendAction(ISMimePackager.ACTION_ENCRYPT);
      smime.setActionProperty(ISMimePackager.ACTION_ENCRYPT,
                              ISMimePackager.SCOPE_ALL);
      smime.appendAction(ISMimePackager.ACTION_SIGN);
      smime.setActionProperty(ISMimePackager.ACTION_SIGN,
                              ISMimePackager.SCOPE_ALL);
      
      //smime.setSignScope(GNSMimeBasic.SCOPE_ALL);
      
//    MimeBodyPart msg = new MimeBodyPart();
//    msg.setText("Hello world!");
//    SMimeHelper.setContentType(msg, "text/plain");
      
//    MimeBodyPart msg1 = new MimeBodyPart();
//    msg1.setText("Hello world 1!");
//    SMimeHelper.setContentType(msg1, "text/plain");
//    
//    MimeBodyPart msg2 = new MimeBodyPart();
//    msg2.setText("Hello world 2!");
//    SMimeHelper.setContentType(msg2, "text/plain");
      
      smime.setContent(msg);
      
//    smime.setAttachments(new MimeBodyPart[]{msg1, msg2});
      MimeMessage enc = smime.packDocument();
//    
      System.out.println(new String("----orig message start----"));
      System.out.println(new String(SMimeHelper.getBytesFromMime(enc)));
      System.out.println(new String("----orig message end----"));
      
      SMimeHelper.createFile(encFile, SMimeHelper.getBytesFromMime(enc));
    }
    else
    {
      Certificate cert = certMgr.findCertificateByIDAndName(519, "GridTalk");
      X509Certificate ownCert = certMgr.getX509Certificate(cert);
      Certificate partnerCert = certMgr.findCertificateByIDAndName(0, "520 Cert");
      X509Certificate encCert = certMgr.getX509Certificate(partnerCert);

      SMimeFactory fac = SMimeFactory.newInstance(ownCert,encCert);
      ISMimeDePackager smime1 =  SMimeFactory2.getSMimeDePackager("AS2",fac);
      MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile(encFile));
      smime1.setMessage(me);
      MimeMessage me1 = smime1.dePackDocument();
      System.out.println(new String("----de message start----"));
        System.out.println(new String( SMimeHelper.getContentBytesFromMime(me1)));
      System.out.println(new String("----de message end----"));
      System.out.println("Filename = "+me1.getFileName());
    }
//    seu.verify(smime.getPackedMessage());
//    ISMimeDePackager smime1 =  SMimeFactory2.getSMimeDePackager("AS2",fac);
//    System.out.println(new String( smime1.deCompress(enc)));
//    System.out.println(new String(smime1.deCompress(enc)));
//    smime1.setExpandMessage(false);
//    MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile(encFile));
//    byte[] con = SMimeHelper.getContentBytesFromMime(me);
//
//    SMimeHelper.createFile(new File("c:\\a.zip") ,con);
//    smime1.setMessage(me);

//    MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile("c:\\2.txt"));
    /*
    ISMimeDePackager smime1 =  SMimeFactory2.getSMimeDePackager("AS2",fac);
    MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile(encFile));
    smime1.setMessage(me);
    MimeMessage me1 = smime1.dePackDocument();
    System.out.println(new String("----de message start----"));
      System.out.println(new String( SMimeHelper.getContentBytesFromMime(me1)));
    System.out.println(new String("----de message end----"));
    */
 }

}