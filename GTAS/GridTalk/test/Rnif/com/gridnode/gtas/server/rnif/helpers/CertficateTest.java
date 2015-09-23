package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.util.DocTestUtil;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import java.io.File;

import junit.framework.TestCase;

public class CertficateTest extends TestCase
{

  ICertificateManagerHome _certMgrHome= null;
  ICertificateManagerObj _certMgr= null;

  public ICertificateManagerHome getCertMgrHome() throws Exception
  {
    return (ICertificateManagerHome) ServiceLookup.getInstance(
      ServiceLookup.CLIENT_CONTEXT).getHome(
      ICertificateManagerHome.class);
  }

  public CertficateTest(String name)
  {
    super(name);
  }
  public void testImportCert() throws Throwable
  {
    String ownCertFileName= "encryptkey-rsa1024-123456789.p12";
    String ownPassword= "mysecret";
    Integer ownId= new Integer(523);
    String partnerCertFileName= "encryptcert-987654321.der";
    Integer partnerId= new Integer(1000);
 //   ownCertFileName = new File(ownCertFileName);
 //   partnerCertFileName = new File(partnerCertFileName);
    try
    {
      Logger.debug("testImportCert Enter");
//      File srcFile = new File("testdata/rnif/cert/" + ownCertFileName);
//      File destFile= new File("C:\\jboss\\bin\\gtas\\data\\sys\\pkcs12\\import\\" + ownCertFileName);
//      DocTestUtil.copyFile(srcFile, destFile);
//      
//      srcFile = new File("testdata/rnif/cert/" + partnerCertFileName);
//      destFile= new File("C:\\jboss\\bin\\gtas\\data\\sys\\cert\\import\\" + partnerCertFileName);
//      DocTestUtil.copyFile(srcFile, destFile);
//            
      //import Own Cert
      _certMgrHome= getCertMgrHome();
      _certMgr= _certMgrHome.create();
      _certMgr.importCertificate("3A4OwnCert", ownCertFileName, ownId, ownPassword);

      _certMgr.importCertificate("3A4PartnerCert", partnerCertFileName, partnerId);

    }
    catch (Throwable ex)
    {
      Logger.err("testImportCert", ex);
      throw ex;
    }
    Logger.debug("testImportCert Exit");

  }

}
