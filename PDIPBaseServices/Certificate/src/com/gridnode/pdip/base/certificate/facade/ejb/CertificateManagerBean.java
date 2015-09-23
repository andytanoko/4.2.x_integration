/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateManagerBean
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * JUL 03 2002    Jagadeesh            Created
 * SEP 13 2002    Jagadeesh            Added getCertificate(IDataFilter filter), to
 *                                     retrieve Certificate Entity.
 * Oct 02 2002    Neo Sok Lay          Added findCertificateByUID(UID).
 * 25-OCT-2002     Jagadeesh           Added: To updateCertificate MasterAndPartner
 *                                     status by UID.
 * 08-Nov-2002     Jagadeesh           Added: To Revoke a Certificate Entity by UID.
 * 13 Nov 2002     Neo Sok Lay         Get privatekey password from SecurityDB
 *                                     instead of caching here.
 * 18-Jan-2003     Jagadeesh           Modified: New methods to ImportCertificate,ExportCertificate
 *                                     displayX500Names.
 * 22-Jan-2003     Jagadeesh           Modified: To Use IX500Name constants interface constants.
 * 27-Jan-2003     Jagadeesh           Modified: To Return FileName after Certificate Export.
 * 27-Jan-2003     Jagadeesh           Modified: To Update isPartner when Import Certificate.
 * 27-Jan-2003     Jagadeesh           Modified: To Place Exported Cert in Temp Folder.
 * 10-MAR-2003     Jagadeesh           Modified: To Add ImportedCertificates to CertPathCtx
 * 28-Apr-2003     Qingsong            Added: issetPrivatePassword, setPrivatePassword, changePrivatePassword, changeCertificateName, removeCertificate
 * 14 Jul 2003    Neo Sok Lay         Add method: getCertificateKeys(IDataFilter)
 * 03-Nov-2003	  Jagadeesh		      Modified : exportCertificateTrustStore() - Method,to fix defect No: 15934		
 * 08 Jan 2004    Neo Sok Lay         Fix GNDB00016743: 
 *                                    - change displayX500Names(Name) to displayX500Names(UID)
 *                                    - change exportCertificate(Name,Filename) to exportCertificate(UID,Filename).
 *
 * 02-Apr-2004     Guo Jianyu          Added "getOwnSignCert()" and "getPartnerEncryptCert"
 * 17-Oct-2005     Neo Sok Lay         Added: insertPrivateKeyByCertificate(byte[],JSAFE_PrivateKey)
 * 26-Jul-2006     Tam Wei Xiang       Added method getX500NamesAndCertDetails.
 *                                     Change method signature for createCertificate(...) Added in
 *                                     StartDate and EndDate (in "UTC" time)
 * 31-Jul-2006     Tam Wei Xiang       To check whether the importing cert isCA cert. Modified method
 *                                     importCertificate(...) 
 * 03-Aug-2006     Tam Wei Xiang       Amend the way we access SecurityDB.
 *                                     Modified method : displayX500Names(...)
 *                                                       getX500Names(...)
 *                                                       importCertificate(...), importCertificate(...), importCertificate(...)
 *                                                       insertCertificate(...), insertCertificate(...), insertCertificate(...)
 * 08-Aug-2006     Tam Wei Xiang       Ensure the cert's direct CA is in truststore while
 *                                     we export that cert to truststore.      
 * 28-Aug-2006     Tam Wei Xiang       Modified method: removeCertificate(...) Additional handling while the cert we delete is a replacement cert.
 * Feb 09 2007			Alain Ah Ming				Use error code or log warning message            
 * May 04 2007    Neo Sok Lay         GNDB00028339: change exportCertificateKeyStore() to update previous key cert if not the same cert as now.   
 * Aug 01 2008		Wong Yee Wah        #38   add two extra parameters(swapDate, swapDate) to updateCertificate(), to create,update and delete iCalAlarm
 * Aug 09 2008    Tam Wei Xiang       #38 Modified updateCertificate(..) to avoid ORA-01722: invalid number    
 * 22 Jun 2009    Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                                                  
 */

package com.gridnode.pdip.base.certificate.facade.ejb;

import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.certificate.exceptions.*;
import com.gridnode.pdip.base.certificate.helpers.*;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.certificate.model.ICertificate;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PrincipalUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Jagadeesh
 * @since
 * @version GT 4.0 VAN
 */
public class CertificateManagerBean implements SessionBean
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3728981527634355214L;
	private SessionContext _ctx;
  private byte[][] certIterator = null, keyIterator = null;
  private int certPointer, keyPointer;
  private Object certLock = new Object();
  private Object keyLock = new Object();
  private static final String DB_SERVIC_ENAME = "Import-Cert-DB";
  private static final String CLASS_NAME = "CertificateManagerBean";

  /****************************** EJB required methods **************************************/
  public void setSessionContext(SessionContext ctx)
  {
//    try
//    {
      _ctx = ctx;
      //certJ = SecurityDB.getCertJ(); //new CertJ();   //Note: CertJ is created in SessionContext.
//    }
//    catch(ProviderManagementException ex)
//    {
//      CertificateLogger.debug("[CertificateManagerBean][setSesssionContext]"+
//      "Cannot Get the CertJ Initilized",ex);
//    }
  }

  public void ejbCreate() throws CreateException
  {
    //_privatePassword = SecurityDB.getPrivatePassword();  // Initilize privatepassword here.
    //certJ = SecurityDB.getCertJ();
  }
  /**
   * Use this method for the first time create.For Security Services reason
   * this password is made mandatory.  Any subsequent service invocation
   * will use this password for Certificate/Security related services.
   *
   *
   * @param password - PrivatePassword used for Encryption/Decryption.
   * @throws CreateException - thrown when unable to create.
   */
//  public void ejbCreate(String password) throws CreateException
//  {
//    this._privatePassword = password;
//  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public Certificate importCertificate(
    String name,
                                        String fileName,
                                        Integer partnerNodeId,
    String passWord)
    throws CertificateException, SystemException
  {
  	PKCS12Reader pkcsReader = null;
    try
    {
      CertificateLogger.log(
        "[CertificateManagerBean.importCertificate]"
          + "Begin of Import Certificate Step 1");
      File x509CertFile =
        FileUtil.getFile(
          ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
          fileName);
      String actualfileName = x509CertFile.getAbsolutePath();
      
      //secDB = SecurityDBManager.getInstance().getSecurityDB(); //TWX
      pkcsReader = getPKCS12Reader(actualfileName, passWord);
      X509Certificate cert = pkcsReader.getCertificate();

      boolean isCertExists = isCertExists(cert);
      if (isCertExists == false)
      {
        PrivateKey privateKey = pkcsReader.getPrivateKey();
        insertCertificate(partnerNodeId, name, cert);
        insertPrivateKeyByIdAndName(partnerNodeId.intValue(), name, privateKey);
        CertificateLogger.log(
          "[CertificateManagerBean.importCertificate]"
            + "After InsertCertificate Step 4");
        return findCertificateByIDAndName(partnerNodeId.intValue(), name);
      }
      throw new CertificateException("Unable to Import Certificate : Certificate Already Exists");
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.importCertificate] BL Exception",
        ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
      throw new SystemException(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
    }

  }

  public Certificate importCertificate(
    String name,
    String fileName,
    Integer ownId)
    throws CertificateException, SystemException
  {
    try
    {
      File x509CertFile =
        FileUtil.getFile(
          ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
          fileName);
      String actualfileName = x509CertFile.getAbsolutePath();

      X509Certificate importCertificate =
        GridCertUtilities.loadX509Certificate(actualfileName);
      boolean isCertExists = isCertExists(importCertificate);
      CertificateLogger.log(
        "[CertificateManagerBean][importCertificate] isExists: "
          + isCertExists);
      if (isCertExists == false)
      {
        CertificateLogger.log(
          "[CertificateManagerBean][importCertificate] In InsertCertificate ");
        insertCertificate(ownId, name, importCertificate);
        return findCertificateByIDAndName(ownId.intValue(), name);
      }
      else
        throw new CertificateException("Unable to Import Certificate : Certificate Already Exists");
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.importCertificate] BL Exception",
        ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
      throw new SystemException(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
    }
  }
  
  //TWX 31072006 To perform additional checking on the importedCert whether is a CA cert.
   public Certificate importCertificate(String name,String fileName, Long relatedCertUid,
                                        Boolean isCA)
      throws CertificateException,
             DuplicateCertificateException,
             InvalidFileTypeException,
             FileAccessException,
             SystemException
   {
    final String LOG_MESSAGE_FORMAT = "[" + CLASS_NAME + "]importCertificate]";
    try
    {
      File x509CertFile =
        FileUtil.getFile(
          ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
          fileName);
      if (x509CertFile == null)
        throw new FileAccessException(
          LOG_MESSAGE_FORMAT + " File Not Found With File Name=" + fileName);

      X509Certificate importCertificate =
        GridCertUtilities.loadX509Certificate(x509CertFile);
      boolean isCertExists = isCertExists(importCertificate);
      CertificateLogger.log(
        "[CertificateManagerBean][importCertificate] isExists: "
          + isCertExists);
      if (isCertExists == false)
      {
        CertificateLogger.log(
          "[CertificateManagerBean][importCertificate] In InsertCertificate ");
        
        //TWX 28072006 ensure it is a real CA cert
        isCACert(x509CertFile.getAbsolutePath(), isCA);
        
        insertCertificate(importCertificate, null, name, true, isCA);
        
        /* TWX
        SecurityDB.getInstance().addTrustedCertificates(
          new com.rsa.certj.cert.Certificate[] { importCertificate });
        */
        /*
        secDB = SecurityDBManager.getInstance().getSecurityDB(); //TWX
        secDB.addTrustedCertificates(new com.rsa.certj.cert.Certificate[] { importCertificate }); */
        
        //080104NSL: find by issuer and serialnum to make sure the correct cert is returned
        String issuerName =
          GridCertUtilities.writeIssuerNameToString(importCertificate.getIssuerX500Principal());
        String serialNum =
          GridCertUtilities.writeByteArrayToString(importCertificate.getSerialNumber().toByteArray());

        Certificate returnCert = findCertificateByIssureAndSerialNum(issuerName, serialNum);
        getEntityHandler().updateCertificate((Long)returnCert.getKey(),
          returnCert.getCertName(), relatedCertUid);
        
        //08122006 TWX update the relatedCert's replacement cert uid
        updateCertReplacementCertUID(relatedCertUid, (Long)returnCert.getKey());
        
        return returnCert;

      }
      else
        throw new DuplicateCertificateException("Unable to Import Certificate : Certificate Already Exists");
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.importCertificate] BL Exception",
        ex);
      
      //31072006 TWX
      if(ex instanceof InvalidCACertificateException)
      {
      	throw (InvalidCACertificateException)ex;
      }
      else
      {
      	throw new CertificateException(ex.getMessage());
      }
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
      throw new SystemException(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
    }
  }
   
  public Certificate importCertificate(
    String name,
    String fileName,
    String passWord,
    Long relatedCertUid)
    throws
      CertificateException,
                              InvalidPasswordOrFileTypeException,
                              DuplicateCertificateException,
                              FileAccessException,
                              SystemException
  {
    final String LOG_MESSAGE_FORMAT = "[" + CLASS_NAME + "]importCertificate]";
    
    PKCS12Reader pkcsReader = null;
    try
    {
      CertificateLogger.log(
        LOG_MESSAGE_FORMAT + "Begin of Import Certificate Step 1");
      String actualfileName = null;

      File x509CertFile =
        FileUtil.getFile(
          ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
          fileName);
      if (x509CertFile != null)
       actualfileName = x509CertFile.getAbsolutePath();
      else
        throw new FileAccessException(
          LOG_MESSAGE_FORMAT + "File Not Found With File Name=" + fileName);

      CertificateLogger.debug(
        LOG_MESSAGE_FORMAT + "CertFile Name: " + actualfileName);
      
      
      pkcsReader = getPKCS12Reader(actualfileName, passWord);
      X509Certificate cert = pkcsReader.getCertificate();

      boolean isCertExists = isCertExists(cert);
      if (isCertExists == false)
      {
        PrivateKey privateKey = pkcsReader.getPrivateKey();
        insertCertificate(cert, privateKey, name, false, false); //isCA will be false
        CertificateLogger.log(
          LOG_MESSAGE_FORMAT + "After InsertCertificate Step 4");
        
        /* TWX
        SecurityDB.getInstance().addTrustedCertificates(
          new com.rsa.certj.cert.Certificate[] { cert }); */
        //SecurityDB secDB = SecurityDBManager.getInstance().getSecurityDB();
        //secDB.addTrustedCertificates(new com.rsa.certj.cert.Certificate[] { cert });

        //080104NSL: find by issuer and serialnum to make sure the correct cert is returned
        String issuerName =
          GridCertUtilities.writeIssuerNameToString(cert.getIssuerX500Principal());
        String serialNum =
          GridCertUtilities.writeByteArrayToString(cert.getSerialNumber().toByteArray());

        Certificate returnCert = findCertificateByIssureAndSerialNum(issuerName, serialNum);
        getEntityHandler().updateCertificate((Long)returnCert.getKey(),
          returnCert.getCertName(), relatedCertUid);
        
        //08122006 TWX update the relatedCert's replacement cert uid
        updateCertReplacementCertUID(relatedCertUid, (Long)returnCert.getKey());
        
        return returnCert;
      }
      throw new DuplicateCertificateException("Unable to Import Certificate : Certificate Already Exists");
    }
//    catch (com.rsa.certj.pkcs12.PKCS12Exception p12Exception)
//    {
//      throw new InvalidPasswordOrFileTypeException(p12Exception.getMessage());
//    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(LOG_MESSAGE_FORMAT + " BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE_FORMAT + " Error ", th);
      throw new SystemException(LOG_MESSAGE_FORMAT + " Error ", th);
    }

  }

  private PKCS12Reader getPKCS12Reader(String fileName, String password)
    throws Exception
  {
  	PKCS12Reader pkcsreader = null;
    try
    {
      pkcsreader =
        new PKCS12Reader(fileName, password.toCharArray());
      CertificateLogger.debug(
        "[CertificateManagerBean.getPKCS12Reader]"
          + "After Creating PKCS12Reader Step 2");
      
      pkcsreader.read();
      CertificateLogger.debug(
        "[CertificateManagerBean.getPKCS12Reader]" + "After Reading Step 3");
      return pkcsreader;
    }
    catch (Exception ex)
    {
      throw ex;
    }
  }
  /*
  private com.rsa.certj.cert.X509Certificate getCertificateFromP12File(
    String fileName,
    String password)
     throws Exception
  {
    try
    {
      PKCS12Reader pkcsreader =
        new PKCS12Reader(fileName, password.toCharArray());
      CertificateLogger.debug(
        "[CertificateManagerBean.getCertificateFromP12File]"
          + "After Creating PKCS12Reader Step 2");
      pkcsreader.setCertJ(SecurityDB.getCertJ());
      pkcsreader.setServiceName(DB_SERVIC_ENAME);
      pkcsreader.read();
      CertificateLogger.debug(
        "[CertificateManagerBean.importCertificate]" + "After Reading Step 3");
      X509Certificate  cert = pkcsreader.getCertificate();
      return cert;
    }
    catch (Exception ex)
    {
      throw new Exception(ex.getMessage());
    }
  }*/

  public String exportCertificateKeyStore(Long uid)
    throws CertificateException, SystemException
  {
    final String LOG_MESSAGE =
      "[" + CLASS_NAME + "][exportCertificateKeyStore()]";
    try
    {
      Certificate certEntity =
        (Certificate) getEntityHandler().getEntityByKey(uid);
      if (JavaKeyStoreHelper.emptyString(certEntity.getPrivateKey()))
        throw new ApplicationException("This certificate cannot be exported to Keystore");
      Certificate previouscert =
        (Certificate) getEntityHandler().getPreviousKeyStoreCertificate();
      X509Certificate x509Certificate =
        GridCertUtilities.loadCertificateFromString(
          certEntity.getCertificate());
      PrivateKey rsaprivatekey =
        GridCertUtilities.loadPrivateKeyFromString(certEntity.getPrivateKey());
      GridCertUtilities.exportJavaKeyStore(
        certEntity.getCertName(),
        new X509Certificate[] { x509Certificate },
        rsaprivatekey);

      if (previouscert != null)
      {
        //NSL20070504 only change the status if not the same cert
        if (previouscert.getUId() != certEntity.getUId())   
        {
          previouscert.setInKeyStore(false);
          getEntityHandler().update(previouscert);
          certEntity.setInKeyStore(true);
          getEntityHandler().update(certEntity);
        }
      }
      else
      {
        certEntity.setInKeyStore(true);
        getEntityHandler().update(certEntity);
      }
      CertificateLogger.debug(
        "[CertificateManagerBean.exportCertificateKeyStore]"
          + "Exported Successfuly");
      return certEntity.getCertName();
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(LOG_MESSAGE + "BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
      throw new SystemException(LOG_MESSAGE + " Error ", th);
    }
  }

  public String exportCertificateTrustStore(Long uid)
    throws CertificateException, SystemException
    {
    final String LOG_MESSAGE =
      "[" + CLASS_NAME + "][exportCertificateTrustStore()]";
    try
    {
      Certificate certEntity =
        (Certificate) getEntityHandler().getEntityByKey(uid);
      X509Certificate x509Certificate =
        GridCertUtilities.loadX509CertificateByString(
          certEntity.getCertificate());
      
      //TWX 08082006
      isParentCertExistInKeyStore(certEntity);
      
      boolean isExported =
        GridCertUtilities.exportTrustedJavaKeyStore(
          certEntity.getCertName(),
          x509Certificate);
      if (isExported)
      {
       certEntity.setInTrustStore(true);
       getEntityHandler().update(certEntity);
        CertificateLogger.debug(
          "[CertificateManagerBean.exportCertificateTrustStore]"
            + "Exported Successfuly");
        return certEntity.getCertName();
    }
      else
        throw new CertificateException(
          LOG_MESSAGE
            + "[Could Not Export Certificate with UID="
            + uid
            + "to TrustStore");
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(LOG_MESSAGE + "BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
      throw new SystemException(LOG_MESSAGE + " Error ", th);
    }
  }

  //public String exportCertificate(String name,String fileName)
  public String exportCertificate(Long uid, String fileName)
    throws CertificateException, SystemException
  {
    final String LOG_MESSAGE = "[" + CLASS_NAME + "][exportCertificate()]";
    try
    {
      //Certificate certEntity = findCertificateByName(name);
      Certificate certEntity = findCertificateByUID(uid);
      X509Certificate x509Certificate =
        GridCertUtilities.loadX509CertificateByString(
          certEntity.getCertificate());
      byte[] certificateData = x509Certificate.getEncoded();
      ByteArrayInputStream byteStream =
        new ByteArrayInputStream(certificateData);
      String exportFileName =
        FileUtil.create(
          ICertificateFilePathConfig.PATH_TEMP,
                                               fileName,
          byteStream);
       /* This following code need not be executed as we are Returning File Name. Check with Daniel*/
       /*if(GridCertUtilities.writeX509Certificate(fileName,x509Certificate))
       {
        CertificateLogger.debug(LOG_MESSAGE+"Exported Successfuly");
        return certEntity;
       }
       */
      CertificateLogger.debug(
        "[CertificateManagerBean.exportCertificate]" + "Exported Successfuly");
       return exportFileName;
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(LOG_MESSAGE + "BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
      throw new SystemException(LOG_MESSAGE + " Error ", th);
    }

  }
  
  //TWX same method as displayX500Names except including the cert's serial num,
  //    startDate, endDate
  public Vector getX500NamesAndCertDetail(String fileName, String password)
  	throws InvalidPasswordOrFileTypeException, CertificateException, SystemException
  {
  	final String LOG_MESSAGE = "[" + CLASS_NAME + "]displayX500Names()]";
  	Vector certDetails = null;
  	
  	PKCS12Reader pkcs12Reader = null;
  	try
  	{
  		certDetails = new Vector();
  		//    File x509CertFile = FileUtil.getFile(IPathConfig.PATH_TEMP,
  		//                                   fileName);
  		//    String actualfileName = x509CertFile.getAbsolutePath();
  		CertificateLogger.debug(LOG_MESSAGE + "CertFile File Name: " + fileName);

  		X509Certificate cert = null;
  		
  		if (password != null && !password.equals(""))
  		{
  			pkcs12Reader = getPKCS12Reader(fileName, password);
  			cert = pkcs12Reader.getCertificate();
  		}
  		else
  		{
  			cert = GridCertUtilities.loadX509Certificate(fileName);
  		}
  		
  		Hashtable displayIssuerNames =
  			GridCertUtilities.getX500Constants(PrincipalUtil.getIssuerX509Principal(cert));
  		Hashtable displaySubjectNames =
  			GridCertUtilities.getX500Constants(PrincipalUtil.getSubjectX509Principal(cert));
  		certDetails.add(displayIssuerNames);
  		certDetails.add(displaySubjectNames);
  		
  		certDetails.add(cert.getSerialNumber().toByteArray());
  		certDetails.add(convertDateToUtc(cert.getNotBefore()));
  		certDetails.add(convertDateToUtc(cert.getNotAfter()));
  	}
  	catch (Exception ex)
  	{
  		CertificateLogger.warn(LOG_MESSAGE + " BL Exception ", ex);
  		throw new CertificateException(ex.getMessage());
  	}
  	catch (Throwable th)
  	{
  		CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
  		throw new SystemException(LOG_MESSAGE + " Error ", th);
  	}
  	return certDetails;
  }
  
  /**
   * TWX same as displayX500Names except including the cert's serial num, 
   * startDate, endDAte
   * @param uid
   * @return
   * @throws InvalidFileTypeException
   * @throws CertificateException
   * @throws SystemException
   */
  public Vector getX500NamesAndCertDetail(Long uid)
     throws InvalidFileTypeException, CertificateException, SystemException
  {
  	final String LOG_MESSAGE = "[" + CLASS_NAME + "].displayX500Names]";
  	Vector certDetail = null;
  	try
  	{
  		certDetail = new Vector();
  		X509Certificate cert = null;

  		//Certificate certEntity = findCertificateByName(name);
  		Certificate certEntity = findCertificateByUID(uid);
  		cert =
  			GridCertUtilities.loadX509CertificateByString(
  			                                              certEntity.getCertificate());

  		Hashtable displayIssuerNames =
  			GridCertUtilities.getX500Constants(PrincipalUtil.getIssuerX509Principal(cert));
  		Hashtable displaySubjectNames =
  			GridCertUtilities.getX500Constants(PrincipalUtil.getSubjectX509Principal(cert));
  		certDetail.add(displayIssuerNames);
  		certDetail.add(displaySubjectNames);
  		
  		certDetail.add(cert.getSerialNumber().toByteArray());
  		certDetail.add(convertDateToUtc(cert.getNotBefore()));
  		certDetail.add(convertDateToUtc(cert.getNotAfter()));
  	}
  	//  catch(com.rsa.certj.cert.CertificateException certExce)
  	//  {
  	//    throw new InvalidFileTypeException(certExce.getMessage());
  	//  }
  	catch (Exception ex)
  	{
  		CertificateLogger.warn(LOG_MESSAGE + "BL Exception", ex);
  		throw new CertificateException(ex.getMessage());
  	}
  	catch (Throwable th)
  	{
  		CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
  		throw new SystemException(LOG_MESSAGE + " Error ", th);
  	}

  	return certDetail;
  }  
  
  //public Vector displayX500Names(String name)
  public Vector displayX500Names(Long uid)
    throws InvalidFileTypeException, CertificateException, SystemException
  {
    final String LOG_MESSAGE = "[" + CLASS_NAME + "].displayX500Names]";
    Vector names = null;
    try
    {
      names = new Vector();
      X509Certificate cert = null;

      //Certificate certEntity = findCertificateByName(name);
      Certificate certEntity = findCertificateByUID(uid);
      cert =
        GridCertUtilities.loadX509CertificateByString(
          certEntity.getCertificate());

      Hashtable displayIssuerNames =
        GridCertUtilities.getX500Constants(PrincipalUtil.getIssuerX509Principal(cert));
      Hashtable displaySubjectNames =
        GridCertUtilities.getX500Constants(PrincipalUtil.getSubjectX509Principal(cert));
      names.add(displayIssuerNames);
      names.add(displaySubjectNames);
    }
    catch (Exception ex)
    {
      CertificateLogger.warn(LOG_MESSAGE + "BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
      throw new SystemException(LOG_MESSAGE + " Error ", th);
    }

    return names;
  }

  public Vector displayX500Names(String fileName, String password)
    throws InvalidPasswordOrFileTypeException, CertificateException, SystemException
  {
    final String LOG_MESSAGE = "[" + CLASS_NAME + "]displayX500Names()]";
    Vector names = null;
    
    PKCS12Reader pkcs12Reader = null;
    try
    {
      names = new Vector();
//      File x509CertFile = FileUtil.getFile(IPathConfig.PATH_TEMP,
//                                     fileName);
//      String actualfileName = x509CertFile.getAbsolutePath();
      CertificateLogger.debug(LOG_MESSAGE + "CertFile File Name: " + fileName);

      X509Certificate cert = null;
      
      if (password != null && !password.equals(""))
      {
        pkcs12Reader = getPKCS12Reader(fileName, password);
        cert = pkcs12Reader.getCertificate();
      }
      else
      {
        cert = GridCertUtilities.loadX509Certificate(fileName);
      }
      Hashtable displayIssuerNames =
        GridCertUtilities.getX500Constants(PrincipalUtil.getIssuerX509Principal(cert));
      Hashtable displaySubjectNames =
        GridCertUtilities.getX500Constants(PrincipalUtil.getSubjectX509Principal(cert));
      names.add(displayIssuerNames);
      names.add(displaySubjectNames);
    }
    catch (Exception ex)
    {
      CertificateLogger.warn(LOG_MESSAGE + " BL Exception ", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(LOG_MESSAGE + " Error ", th);
      throw new SystemException(LOG_MESSAGE + " Error ", th);
    }
    return names;
  }

  /****************   Finder Methods  **************************/
  /*080104NSL: remove this error-prone method. 
  public Certificate findCertificateByName(String name)
    throws FindEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.findCertificateByIDAndName] ID:Name   " + name);

    Certificate certificate = null;
    try
    {
       certificate = getEntityHandler().findCertificateByName(name);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.err(
        "[CertificateManagerBean.findCertificateByIDAndName] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.err(
        "[CertificateManagerBean.findCertificateByIDAndName] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.findCertificateByIDAndName(Name: "
          + name
          + ") Error ",
        ex);
    }
    return certificate;

  }
  */
  
  public Certificate findCertificateByIDAndName(int id, String name)
    throws FindEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.findCertificateByIDAndName] ID:Name   "
        + id
        + " : "
        + name);

    Certificate certificate = null;
    try
    {
      certificate = getEntityHandler().findCertificateByIDAndName(id, name);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByIDAndName] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByIDAndName] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.findCertificateByIDAndName(ID: "
          + id
          + "Name: "
          + name
          + ") Error ",
        ex);
    }
    return certificate;

  }

  public Certificate findCertificateByIssureAndSerialNum(
    String issuerName,
    String serialNum)
    throws FindEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.findCertificateByIssureAndSerialNum] Issure:SerialNum   "
        + issuerName
        + " : "
        + serialNum);

    Certificate certificate = null;
    try
    {
      certificate =
        getEntityHandler().findCertificateByIssureAndSerialNum(
          issuerName,
          serialNum);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByIssureAndSerialNum] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByIssureAndSerialNum] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.findCertificateByIssureAndSerialNum(Issuer: "
          + issuerName
          + "SerialNum: "
          + serialNum
          + ") Error ",
        ex);
    }
    return certificate;

  }

  public Collection getAllCertificates()
    throws FindEntityException, SystemException
  {
     CertificateLogger.log("[CertificateManagerBean.getAllCertificates]");
     Collection certificates = null;
     try
     {
        certificates = getEntityHandler().getAllCertificates();
     }
     catch (ApplicationException ex)
     {
      CertificateLogger.warn(
        "[CertificateManagerBean.getAllCertificates] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getAllCertificates] Error ",
        ex);
       throw new SystemException(
       "CertificateManagerBean.getAllCertificates: Error ",
       ex);
     }
     return certificates;
  }

  public Collection getCertificate(IDataFilter filter)
    throws FindEntityException, SystemException
  {

     CertificateLogger.log("[CertificateManagerBean.getCertificate]");
     Collection certificates = null;
     try
     {
        certificates = getEntityHandler().getCertificate(filter);
     }
     catch (ApplicationException ex)
     {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificate] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.getCertificate: Error ",
        ex);
    }
    return certificates;

  }

  /**
   * To retrieve a collection of UIDs of <code>Certificate</code> entity with the specified filter.
   * 
   * @parma filter The filtering condition
   * @return A Collection of UIDs of the Certificate entities that satisfy the filter condition.
   * @since GT 2.2 I1
   */
  public Collection getCertificateKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    CertificateLogger.log("[CertificateManagerBean.getCertificateKeys]");
    Collection certificates = null;
    try
    {
      certificates = getEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificateKeys] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificateKeys] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.getCertificateKeys: Error ",
        ex);
    }
    return certificates;
  }

  public Certificate findCertificateByUID(Long uid)
    throws FindEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.findCertificateByUID] UID: " + uid);

    Certificate certificate = null;
    try
    {
      certificate =
        (Certificate) getEntityHandler().getEntityByKeyForReadOnly(uid);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByUID] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.findCertificateByUID] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.findCertificateByUID(UID: " + uid + ") Error ",
        ex);
    }
    
    return certificate;

  }

  public Certificate getCertificate(Long uid)
    throws FindEntityException, SystemException
  {

    CertificateLogger.log("[CertificateManagerBean.getCertificate]");
    Certificate certificate = null;
    try
    {
      certificate =
        (Certificate) getEntityHandler().getEntityByKeyForReadOnly(uid);
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificate] BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.getCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.getCertificate: Error ",
        ex);
    }
    return certificate;

  }

  public boolean isCertExists(X509Certificate certificate)
    throws CertificateException, SystemException
  {
    try
    {
      String issuerName =
        GridCertUtilities.writeIssuerNameToString(certificate.getIssuerX500Principal());
      CertificateLogger.debug(
        "[CertificateManagerBean][isCertExists][IssuerName]" + issuerName);
      String serialNum =
        GridCertUtilities.writeByteArrayToString(certificate.getSerialNumber().toByteArray());
      CertificateLogger.debug(
        "[CertificateManagerBean][isCertExists][SerialNum]" + serialNum);
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
                              Certificate.ISSUERNAME,
                              filter.getEqualOperator(),
                              issuerName,
                              false);

      filter.addSingleFilter(
        filter.getAndConnector(),
                              Certificate.SERIALNUM,
                              filter.getEqualOperator(),
                              serialNum,
                              false);
      /*080104NSL
      filter.addSingleFilter(
        filter.getOrConnector(),
                              Certificate.NAME,
                              filter.getEqualOperator(),
                              name,
                              false);
      */
     Collection listCertificates = getEntityHandler().findByFilter(filter);

      if (listCertificates != null)
     {
       Iterator certs =  listCertificates.iterator();
        CertificateLogger.log(
          "[CertificateManagerBean.isCertExists]"
            + "Certs Exist"
            + certs.hasNext());
       return certs.hasNext();
     }
     return false;
/*   Need to confirm on criteria to identify if certificate exists already.

      try
      {
       Certificate cert = findCertificateByIssureAndSerialNum(issuerName,serialNum);
      }
      catch(Exception ex)
      {
        CertificateLogger.debug("[CertificateManagerBean.isCertExists]"+
        "Certificate Dose Not Exists ");
        return false;
      }
      if(cert != null)
      {
        CertificateLogger.debug("[CertificateManagerBean.isCertExists]"+
        "Certificate Id is : "+cert.getID());
        CertificateLogger.debug("[CertificateManagerBean.isCertExists]"+
              "Certificate Name is : "+cert.getCertName());
        if( (cert.getID() == id.intValue()) && (cert.getCertName().equals(name)) )
        {
          return true;
        }
      }
      return false;
*/
    }
    catch (ApplicationException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.importCertificate] BL Exception",
        ex);
      throw new CertificateException(ex.getMessage());
    }
    catch (Throwable th)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
      throw new SystemException(
        "[CertificateManagerBean][importCertificate] Error ",
        th);
    }
  }

/*****************   Create Certificate  *******************/

  public void createCertificate(
    String serialNum,
    String issuerName,
    String cert,
    String publicKey,
    Date startDate,
    Date endDate)
    throws CreateEntityException, SystemException
  {
    CertificateLogger.log("[CertificateManagerBean.createCertificate] Enter");
    try
    {
        CertificateLogger.log("From Bean Serial Num :" + serialNum);
      CertificateLogger.log("From Bean Issuer Name :" + issuerName);
      CertificateLogger.log("From Bean Cert :" + cert);
      CertificateLogger.log("From Bean Public Key " + publicKey);
      
      getEntityHandler().createCertificate(
        serialNum,
        issuerName,
        cert,
        publicKey, startDate, endDate);
    }
    catch (CreateException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.createCertificate] BL Exception",
        ex);
      throw new CreateEntityException(ex.getLocalizedMessage());
    }

    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.createCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.createCertificate(certificate: "
          + serialNum
          + ") Error ",
        ex);
    }
    CertificateLogger.log("[CertificateManagerBean.createCertificate] Exit");
  }

  public void createCertificate(
    String serialNum,
    String issuerName,
    String cert,
    String publicKey,
    String name,
    Date startDate,
    Date endDate)
    throws CreateEntityException, SystemException
  {
    CertificateLogger.log("[CertificateManagerBean.createCertificate] Enter");
    try
    {
        CertificateLogger.debug("From Bean Serial Num :" + serialNum);
      CertificateLogger.debug("From Bean Issuer Name :" + issuerName);
      CertificateLogger.debug("From Bean Cert :" + cert);
      CertificateLogger.debug("From Bean Public Key " + publicKey);
      CertificateLogger.debug("From Cert Name " + name);
      
      getEntityHandler().createCertificate(
        serialNum,
        issuerName,
        cert,
        publicKey,
        name, startDate, endDate);
    }
    catch (CreateException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.createCertificate] BL Exception",
        ex);
      throw new CreateEntityException(ex.getLocalizedMessage());
    }

    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.createCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.createCertificate(certificate: "
          + serialNum
          + ") Error ",
        ex);
    }
    CertificateLogger.log("[CertificateManagerBean.createCertificate] Exit");
  }

/****************   Insert Data by Criteria *************/

  public void updatePrivateKeyByCertificate(String privateKey, String cert)
    throws UpdateEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.updatePrivateKeyByCertificate] Enter");
    try
    {
      getEntityHandler().updatePrivateKeyByCertificate(privateKey, cert);
    }

    catch (EntityModifiedException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updatePrivateKeyByCertificate] BL Exception",
        ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updatePrivateKeyByCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.updatePrivateKeyByCertificate(certificate: "
          + cert
          + ") Error ",
        ex);
      }

  }

  public void updatePrivateKeyByPublicKey(String privateKey, String publicKey)
    throws UpdateEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.updatePrivateKeyByPublicKey] Enter");
    try
    {
      getEntityHandler().updatePrivateKeyByPublicKey(privateKey, publicKey);
    }
    catch (EntityModifiedException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updatePrivateKeyByPublicKey] BL Exception",
        ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updatePrivateKeyByPublicKey] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.updatePrivateKeyByPublicKey(certificate: "
          + publicKey
          + ") Error ",
         ex);
      }

  }

  public void updateIdAndNameByCertificate(int Id, String name, String cert)
    throws UpdateEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.updateIdAndNameByCertificate] Enter");
    try
    {
      getEntityHandler().updateIdAndNameByCertificate(Id, name, cert);
    }
    catch (EntityModifiedException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateIdAndNameByCertificate] BL Exception",
        ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateIdAndNameByCertificate] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.updatePrivateKeyByPublicKey(ID: "
          + Id
          + "Name "
          + name
          + ") Error ",
        ex);
    }

  }

  public void updateMasterAndPartnerByUId(
    Long uid,
    boolean isMaster,
    boolean isPartner)
    throws UpdateEntityException, SystemException
  {
    CertificateLogger.log(
      "[CertificateManagerBean.updateMasterAndPartnerByUID] Enter");
    try
    {
      getEntityHandler().updateMasterAndPartnerByUID(uid, isMaster, isPartner);
    }
    catch (EntityModifiedException ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateMasterAndPartnerByUID] BL Exception",
        ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateMasterAndPartnerByUID] Error ",
        ex);
      throw new SystemException(
        "CertificateManagerBean.updateMasterAndPartnerByUID(UID: "
          + uid
          + "isMaster"
          + isMaster
          + "isPartner"
          + isPartner
          + ") Error ",
        ex);
    }

  }

/****************   Delete Entity by Criteria *************/

  public void deleteCertificateByIssuerAndSerialNum(
    String issuerName,
    String serialNum)
    throws DeleteEntityException, SystemException
  {

    try
    {
      getEntityHandler().deleteCertificateByIssuerAndSerialNum(
        issuerName,
        serialNum);
      }
      catch (RemoveException ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.deleteCertificateByIssuerAndSerialNum] BL Exception",
        ex);
        throw new DeleteEntityException(ex.getMessage());
      }
      catch (Throwable ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.deleteCertificateByIssuerAndSerialNum] Error ",
        ex);
        throw new SystemException(
        "[CertificateManagerBean.deleteCertificateByIssuerAndSerialNum] Error ",
        ex);
      }

  }

   public boolean issetPrivatePassword()
   {
      return getEntityHandler().issetPrivatePassword();
   }

  public void setPrivatePassword(String password)
    throws InvalidPasswordException, ApplicationException
   {
      getEntityHandler().setPrivatePassword(password);
   }

  public void changePrivatePassword(String oldpassword, String newpassword)
               throws InvalidPasswordException, ResetPasswordException
  {
    getEntityHandler().changePrivatePassword(oldpassword, newpassword);
  }

  public void updateCertificate(Long uid, String newName, Long relatedCertUid, String swapDate, String swapTime)
    throws EntityModifiedException, UpdateEntityException, SystemException
  {
    
    Date    	dueDate;
    String  	combString;
    iCalAlarm 	icAlarm;
    
      try
      {
        getEntityHandler().updateCertificate(uid, newName, relatedCertUid);
        
        //WYW 01082008
        DataFilterImpl filter = new DataFilterImpl();
        filter.addSingleFilter(null,iCalAlarm.TASK_ID,filter.getEqualOperator(),(uid == null ? uid : uid.toString()),false); //TWX 09082008 Require convert to string to avoid
        Collection alarms = getICalAlarmDAO().getEntityByFilter(filter);                                                                    //ORA-01722: invalid number
        
        if(swapDate!=null && swapTime!=null && !swapDate.equals("") && !swapTime.equals("")) //if swap date and swap time not empty add iCalAlarm
        {

	        combString = swapDate+" "+swapTime+":00";
	        dueDate     = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(combString);

	        if(alarms.size()<1)//if iCalAlarm not exist for the certificate, create a new iCalAlarm
	        {
		        iCalAlarm alarm = new iCalAlarm();
		        alarm.setCategory("CertificateSwapping");
		        alarm.setTaskId(uid+"");
		        alarm.setSenderKey("CertificateSwapping");
		        alarm.setReceiverKey("CertificateSwapping");
		        alarm.setStartDt(dueDate);
		        alarm.setDisabled(Boolean.FALSE);
		       
		        
		        IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
		                                                                             IiCalTimeMgrHome.class.getName(),
		                                                                             IiCalTimeMgrHome.class,
		                                                                             new Object[0]);
		                                                                            mgrObject.addAlarm(alarm);
		                                                                          
	        }else if(alarms.size()>0)//if iCalAlarm alread existed for the certificate, update the existing iCalAlarm
	        {
	        	for (Iterator i=alarms.iterator(); i.hasNext();)
	            {
	        		icAlarm = (iCalAlarm)i.next();
	     
	        		icAlarm.setStartDt(dueDate);
	        		getManager().updateAlarm(icAlarm, true);
	            }
	        }
		                                                                          
	   }else if(swapDate.equals("") && swapTime.equals(""))//if swap date and swap time empty, check existency of iCalAlarm for the certificate
	   {
		   if(alarms.size()>0)//if iCalAlarm existed, cancel it(delete it)
		   {
			   getManager().cancelAlarmByFilter(filter);
		   }
		   
	   }
	
	        //TWX 24082006
	        if(relatedCertUid == null)
	        {
	        	cleanReplacementCert(uid);
	        }
	        else
	        {
	        	Certificate relatedCert = (Certificate)getEntityHandler().getEntityByKey(relatedCertUid);
	        	
	        	if(relatedCert.getReplacementCertUid()== null || 
	        			   relatedCert.getReplacementCertUid().longValue() != uid.longValue())
	        	{
	        		cleanReplacementCert(uid);
	            if(relatedCertUid != null)
	            {
	            	setReplacementCert(relatedCert, uid);
	            }
        	}
        }
      }
      catch (EntityModifiedException ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateCertificate] BL Exception",
        ex);
        throw new UpdateEntityException(ex.getMessage());
      }
      catch (Throwable ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.updateCertificate] Error ",
        ex);
        throw new SystemException(
        "[CertificateManagerBean.updateCertificate] Error ",
        ex);
      }
  }

  public String removeCertificate(Long uid)
    throws EntityModifiedException, UpdateEntityException, SystemException
  {

      try
      {
      	//TWX 25082006
      	 Certificate c = (Certificate)getEntityHandler().getEntityByKey(uid);
      	 if(hasRelatedCert(c))
      	 {
      		 Certificate relatedCert = (Certificate)getEntityHandler().getEntityByKey(c.getRelatedCertUid());
      		 CertificateLogger.log("[CertificateManagerBean.removeCertificate] RelatedCert is "+relatedCert.getCertName()+" "+" Replacement cert for the related cert is "+relatedCert.getCertName());
      		 relatedCert.setReplacementCertUid(null);
      		 getEntityHandler().update(relatedCert);
      	 }
         return getEntityHandler().removeCertificate(uid);
      }
      catch (EntityModifiedException ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removeCertificate] BL Exception",
        ex);
        throw new UpdateEntityException(ex.getMessage());
      }
      catch (Throwable ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removeCertificate] Error ",
        ex);
        throw new SystemException(
        "[CertificateManagerBean.removeCertificate] Error ",
        ex);
      }

  }
  
  /**
   * TWX 25082006 To check whether the cert entity represent by the uid has relatedCert
   * @param c Certificate entity
   * @return 
   */
  private boolean hasRelatedCert(Certificate c)
  	throws Throwable
  {
  	return c.getRelatedCertUid() != null;
  }
  
  public void removePrivateKeyByCertificate(String cert)
    throws EntityModifiedException, UpdateEntityException, SystemException
  {

      try
      {
         getEntityHandler().removePrivateKeyByCertificate(cert);
      }
      catch (EntityModifiedException ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removePrivateKeyByCertificate] BL Exception",
        ex);
        throw new UpdateEntityException(ex.getMessage());
      }
      catch (Throwable ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removePrivateKeyByCertificate] Error ",
        ex);
        throw new SystemException(
        "[CertificateManagerBean.removePrivateKeyByCertificate] Error ",
        ex);
      }

  }

  public void removePrivateKeyByPublicKey(String publicKey)
    throws EntityModifiedException, UpdateEntityException, SystemException
  {
      try
      {
          getEntityHandler().removePrivateKeyByPublicKey(publicKey);

      }
      catch (EntityModifiedException ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removePrivateKeyByPublicKey] BL Exception",
        ex);
        throw new UpdateEntityException(ex.getMessage());
      }
      catch (Throwable ex)
      {
      CertificateLogger.warn(
        "[CertificateManagerBean.removePrivateKeyByPublicKey] Error ",
        ex);
        throw new SystemException(
        "[CertificateManagerBean.removePrivateKeyByPublicKey] Error ",
        ex);
      }

  }

  public void revokeCertificateByUId(Long uid)
    throws CertificateException, SystemException
   {
    CertificateLogger.log(
      "[CertificateManagerBean.revokeCertificateByUId] Enter");
     try
     {
        getEntityHandler().revokeCertificateByUId(uid);
     }
    catch (Exception e)
     {
      CertificateLogger.warn(
        "[CertificateManagerBean.revokeCertificateByUId]"
          + "Cannot Revoke this Certificate[UID]=["
          + uid
          + "]",
        e);
      throw new CertificateException(
        "[CertificateManagerBean.revokeCertificateByUId]"
          + "Cannot Revoke this Certificate[UID]=["
          + uid
          + "]",
        e);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.revokeCertificateByUId]"
          + "Cannot Revoke this Certificate[UID]=["
          + uid
          + "]",
        ex);
      throw new SystemException(
        "[CertificateManagerBean.revokeCertificateByUId]"
          + "Cannot Revoke this Certificate[UID]=["
          + uid
          + "]",
        ex);
    }
  }

  /**
   * This Method is essentially responsible to create the EntityHandler.
   * EntityHandler acts as delagate and performs data operations on
   * Certificate Entity.(i.e - Create,Delete,Update,Select)
   * @return CertificateEntityHandler -
   */

  public CertificateEntityHandler getEntityHandler()
  {
      return CertificateEntityHandler.getInstance();
  }

//* Methods Added from SecurityDB **************/

  public void insertCertificate(String certFile)
    throws CertificateException, SystemException
  {
    X509Certificate cert = null;
    try
    {
      cert =  GridCertUtilities.loadX509Certificate(certFile);
      insertCertificate(cert);
    }
    catch (Exception ex)
    {
      CertificateLogger.warn("Could Not Insert Certificate By File Name", ex);
      throw new CertificateException(
        "[CertificateManagerBean][insertCertificate]"
          + "Could Not Insert Certificate",
        ex);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.insertCertificate] Error ",
        ex);
      throw new SystemException(
        "[CertificateManagerBean.insertCertificate] Error ",
        ex);
    }

  }

  public void insertCertificate(
    Number gridnodeID,
    String name,
    String certFile)
    throws CertificateException, SystemException
  {
    X509Certificate cert = null;
    try
    {
      CertificateLogger.debug(
        "[SecurityDBHandler] "
          + "[insertCertificate()]"
          + "In insertCertificate  ID="
          + gridnodeID
          + "Name="
          + name
          + "CertFileName"
          + certFile);
      cert = GridCertUtilities.loadX509Certificate(certFile);
      insertCertificate(gridnodeID, name, cert);
    }
    catch (Exception ex)
    {
      CertificateLogger.warn(
        "Could Not Insert Certificate By File Name ID and Name",
        ex);
      throw new CertificateException(
        "[SecurityDBHandler] "
          + "[insertCertificate()] "
          + "Could Not Insert Certificate",
        ex);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.insertCertificate] Error ",
        ex);
      throw new SystemException(
        "[CertificateManagerBean.insertCertificate] Error ",
        ex);
    }

  }

  public java.security.cert.Certificate insertCertificate(
    Number gridnodeID,
    String name,
    byte[] certData)
    throws CertificateException, SystemException
  {
    X509Certificate cert = null;
    try
    {
      CertificateLogger.debug(
        "[SecurityDBHandler] "
          + "[insertCertificate()]"
          + "In insertCertificate  ID="
          + gridnodeID
          + "Name="
          + name);
      cert = GridCertUtilities.loadCertificateFromByte(certData);
    }
    catch (Exception ex)
    {
      CertificateLogger.warn("Could Not Insert Certificate ", ex);
      throw new CertificateException(
        "[SecurityDBHandler] "
          + "[insertCertificate()] "
          + "Could Not Insert Certificate",
        ex);
    }
    insertCertificate(gridnodeID, name, cert);
    return cert;
  }

  public void insertCertificate(
    Number gridnodeID,
    String name,
    java.security.cert.Certificate cert)
    throws CertificateException
  {
    try
    {
      insertCertificate(gridnodeID.intValue(), name, cert);
    }
    catch (Exception ex)
    {
      throw new CertificateException(
        "[SecurityDBHandler] "
          + "[insertCertificate()] "
          + "Could Not insert certificate for "
          + gridnodeID,
        ex);
    }
  }
  
  /** Added **/

    public void insertCertificate(java.security.cert.Certificate cert)
    throws CertificateException, SystemException
    { 
      try
      {
        CertificateLogger.log("  In Insert Certificate  ");
      X509Certificate X509Cert = (X509Certificate) cert;
      CertificateLogger.log(
        GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()));
      CertificateLogger.log(
        GridCertUtilities.writePublicKeyToString(X509Cert.getPublicKey()));
      CertificateLogger.log(
        GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()));
      createCertificate(
        GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()),
        GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()),
        GridCertUtilities.writeCertificateToString(X509Cert),
        GridCertUtilities.writePublicKeyToString(
          X509Cert.getPublicKey()),
          convertDateToUtc(X509Cert.getNotBefore()),
          convertDateToUtc(X509Cert.getNotAfter()));
    }
    catch (Exception e)
    {
      CertificateLogger.warn(
        "[CertificateManager][insertCertificate][Cannot insert Certificate]",
        e);
      throw new CertificateException("Cannot Insert Certificate:", e);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManager][insertCertificate][Cannot insert Certificate]",
        ex);
      throw new SystemException("Cannot Insert Certificate: ", ex);
    }
  }

  public void insertCertificate(java.security.cert.Certificate cert, String certName)
    throws CertificateException, SystemException
  {
    try
    {                           
      X509Certificate X509Cert = (X509Certificate) cert;
      CertificateLogger.log("  In Insert Certificate  ");
      CertificateLogger.log(GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()));
      CertificateLogger.log(GridCertUtilities.writePublicKeyToString(
                                      X509Cert.getPublicKey())
                            );
      CertificateLogger.log(GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()));
      createCertificate(
                         GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()),
                         GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()),
                         GridCertUtilities.writeCertificateToString(X509Cert),
                         GridCertUtilities.writePublicKeyToString(
                            X509Cert.getPublicKey()),
                         certName,
                         convertDateToUtc(X509Cert.getNotBefore()), convertDateToUtc(X509Cert.getNotAfter())
                       );
    }
    catch (Exception e)
    {
      CertificateLogger.warn("[CertificateManager][insertCertificate][Cannot insert Certificate]",e);
      throw new CertificateException("Cannot Insert Certificate:", e);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn("[CertificateManager][insertCertificate][Cannot insert Certificate]",
                             ex);
      throw new SystemException("Cannot Insert Certificate: ", ex);
    }
  }
  

  public void insertCertificate(
    X509Certificate cert,
                                  PrivateKey privateKey,
                                  String certName,
    boolean isPartner, Boolean isCA)
    throws CertificateException, SystemException
    {
      try
      {
      X509Certificate X509Cert = (X509Certificate) cert;
        CertificateLogger.log("  In Insert Certificate  ");
        
      CertificateLogger.log(
        GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()));
      CertificateLogger.log(
        GridCertUtilities.writePublicKeyToString(X509Cert.getPublicKey()));
      CertificateLogger.log(
        GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()));

      if (privateKey != null)
      {
        getEntityHandler().createCertificate(
          GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()),
          GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()),
          GridCertUtilities.writeCertificateToString(X509Cert),
          GridCertUtilities.writePublicKeyToString(
            X509Cert.getPublicKey()),
          certName,
          GridCertUtilities.writePrivateKeyToString(privateKey),
          isPartner,
          convertDateToUtc(X509Cert.getNotBefore()), convertDateToUtc(X509Cert.getNotAfter()),
          isCA);
      }
      else
      {
        getEntityHandler().createCertificate(
          GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()),
          GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()),
          GridCertUtilities.writeCertificateToString(X509Cert),
          GridCertUtilities.writePublicKeyToString(
            X509Cert.getPublicKey()),
          certName,
          null,
          isPartner,
          convertDateToUtc(X509Cert.getNotBefore()), convertDateToUtc(X509Cert.getNotAfter()),
          isCA);
      }

    }
    catch (Exception e)
    {
      CertificateLogger.warn(
        "[CertificateManager][insertCertificate][Cannot insert Certificate]",
        e);
      throw new CertificateException("Cannot Insert Certificate:", e);
    }
    catch (Throwable ex)
    {
      CertificateLogger.warn(
        "[CertificateManager][insertCertificate][Cannot insert Certificate]",
        ex);
      throw new SystemException("Cannot Insert Certificate: ", ex);
    }
  }
  
  private Date convertDateToUtc(Date localTime)
  {
  	return new Date(TimeUtil.localToUtc(localTime.getTime()));
  }
  
  public void insertCRL(java.security.cert.CRL crl) throws CertificateException
    {
      throw new CertificateException("SecurityDBImplementation.insertCRL: Not Implemeted Yet!");
    }

  public void insertCertificate(int gNId, String name,
                                java.security.cert.Certificate cert1)
    throws CertificateException, SystemException
  {
    try
    {
      insertCertificate(cert1);
      insertIdAndNameByCertificate(gNId, name, cert1);
    }
    catch (Exception e)
    {
      throw new CertificateException("Cannot Insert Certifcate", e);
    }
    catch (Throwable ex)
    {
      ex.printStackTrace(); 
      throw new SystemException("Cannot Insert Certifcate", ex);
    }
  }
  
  public void insertIdAndNameByCertificate(int gNId, String name,
                                           java.security.cert.Certificate cert)
                                           throws CertificateException, SystemException
  {
    try
    {
      updateIdAndNameByCertificate(gNId,name,
                                   GridCertUtilities.writeCertificateToString((X509Certificate) cert));
    }
    catch (Exception e)
    {
      throw new CertificateException("Cannot InsertIDAndName By Certifcate", e);
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new SystemException("Cannot InsertIDAndName By Certifcate", ex);
    }
  }

  public java.security.cert.Certificate selectCertificateByIdAndName(
    int gNId,
    String name)
    throws CertificateException
  {
    try
    {
      Certificate cert = findCertificateByIDAndName(gNId, name);
      if (cert == null || cert.getCertificate() == null)
         return null;
        return GridCertUtilities.loadCertificateFromString(cert.getCertificate());
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "selectCertificateByGNIdAndName: "
          + e.getMessage());
      }
      catch (Throwable ex)
      {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateByGNIdAndName: "
          + ex.getMessage());
      }

  }

  public PrivateKey selectPrivateKeyByIdAndName(int gNId, String name)
    throws CertificateException
  {
      try
      {
      Certificate cert = findCertificateByIDAndName(gNId, name);
      if (cert == null || cert.getPrivateKey() == null)
          return null;
        return GridCertUtilities.loadPrivateKeyFromString(cert.getPrivateKey());
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "selectPrivateKeyByGNIdAndName: "
          + e.getMessage());
      }
      catch (Throwable ex)
      {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectPrivateKeyByGNIdAndName: "
          + ex.getMessage());
      }

  }

  public void insertPrivateKeyByCertificate(
    java.security.cert.Certificate cert,
    PrivateKey privateKey)
    throws CertificateException, SystemException
  {
    try
    {
      updatePrivateKeyByCertificate(
        GridCertUtilities.writePrivateKeyToString(privateKey),
        GridCertUtilities.writeCertificateToString((X509Certificate) cert));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "Cannot Insert PrivateKey By Certificate",
        e);
    }
    catch (Throwable ex)
    {
      throw new SystemException("Cannot insert PrivateKey By Certificate", ex);
    }

  }

  public void insertPrivateKeyByCertificate(byte[] certData, PrivateKey privateKey) throws CertificateException, SystemException
  {
  	X509Certificate cert = null;
  	try
  	{
  		cert = GridCertUtilities.loadCertificateFromByte(certData);
  	}
  	catch (Exception ex)
  	{
  		CertificateLogger.warn("[insertPrivateKeyByCertificate]create X509Certificate from certData", ex);
      throw new CertificateException("[CertificateManagerBean.insertPrivateKeyByCertificate] create X509Certificate from certData", ex);
  	}
  	insertPrivateKeyByCertificate(cert, privateKey);
  }

  public void insertPrivateKeyByIdAndName(
    int gNId,
    String name,
    PrivateKey privateKey)
    throws CertificateException
  {
    try
    {
      Certificate cert = findCertificateByIDAndName(gNId, name);
      if (cert == null)
        return;
      updatePrivateKeyByCertificate(
        GridCertUtilities.writePrivateKeyToString(privateKey),
        cert.getCertificate());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "insertPrivateKeyByCertificate: "
          + e.getMessage());
    }

    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.insertPrivateKeyByCertificate: "
          + ex.getMessage());
    }

  }
  
  public void insertPrivateKeyByPublicKey(
    PublicKey publicKey,
    PrivateKey privateKey)
    throws CertificateException
  {
    try
    {
      updatePrivateKeyByPublicKey(
        GridCertUtilities.writePrivateKeyToString(privateKey),
        GridCertUtilities.writePublicKeyToString(publicKey));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "insertPrivateKeyByPublicKey: "
          + e.getMessage());
    }

    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.insertPrivateKeyByPublicKey: "
          + ex.getMessage());
    }

  }

  public int selectCertificateByIssuerAndSerialNumber(
    X500Principal issuerName,
    byte[] serialNumber,
    Vector certList)
    throws CertificateException
  {
    try
    {
      Certificate cert =
        findCertificateByIssureAndSerialNum(
          GridCertUtilities.writeIssuerNameToString(issuerName),
          GridCertUtilities.writeByteArrayToString(serialNumber));
      X509Certificate cert1 =
        GridCertUtilities.loadCertificateFromString(cert.getCertificate());
      if (cert1 != null && !certList.contains(cert1))
      {
        certList.addElement(cert1);
                return 1;
              }
        else
          return 0;
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "selectCertificateByIssuerAndSerialNumber: "
          + e.getMessage());
      }

      catch (Throwable ex)
      {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateByIssuerAndSerialNumber: "
          + ex.getMessage());
      }

  }

  public int selectCertificateBySubject(X500Principal subjectName, Vector certList)
    throws CertificateException
  {
    int count = 0;
    try
    {
      Certificate[] certlist = (Certificate[]) getAllCertificates().toArray();
      for (int i = 0; i < certlist.length; i++)
      {
        X509Certificate cert =
          GridCertUtilities.loadCertificateFromString(
            certlist[i].getCertificate());
        X500Principal foundName = cert.getSubjectX500Principal();
        if (subjectName.equals(foundName))
        {
          count++;
          if (!certList.contains(cert))
            certList.addElement(cert);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateBySubject: "
          + e.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateBySubject: "
          + ex.getMessage());
    }

    return count;
  }

  public int selectCertificateByExtensions(
    X500Principal baseName,
    X509Extensions extensions,
    Vector certList)
    throws CertificateException
  {
    int count = 0;
    try
    {
      Certificate[] certlist = (Certificate[]) getAllCertificates().toArray();
      for (int i = 0; i < certlist.length; i++)
      {
        X509Certificate cert =
          GridCertUtilities.loadCertificateFromString(
            certlist[i].getCertificate());
        X500Principal subjectName = cert.getSubjectX500Principal();
        if ((subjectName.getName().equals(baseName.getName()))
          && (extensions.equivalent(GridCertUtilities.getX509ExtensionsFromCert(cert))))
        {
          if (!certList.contains(cert))
            certList.addElement(cert);
          count++;
        }
      }
      return count;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateByExtensions: "
          + e.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectCertificateByExtensions: "
          + ex.getMessage());
    }

  }

   public java.security.cert.Certificate firstCertificate()
    {
//      synchronized (certLock)
//      {
        try
        {
          Collection rec = getAllCertificates();
      Certificate[] certlist =
        (Certificate[]) rec.toArray(new Certificate[rec.size()]);
          certIterator = new byte[certlist.length][];

      for (int i = 0; i < certlist.length; i++)
        certIterator[i] =
          GridCertUtilities.loadByteArrayFromString(
            certlist[i].getCertificate());

          certPointer = 0;
      return nextCertificate();
        }
        catch (CertificateException e)
        {
          return null;
        }
        catch (Throwable ex)
        {
          return null;
        }

  //    }
   }

  public java.security.cert.Certificate nextCertificate()
      throws CertificateException
    {
//      synchronized (certLock)
//      {
        if (certIterator == null)
      throw new CertificateException(
        "SecurityDBImplementation.nextCertificate: "
          + "cert iterator is not set up.");

    if (hasMoreCertificates())
    {
      return GridCertUtilities.loadCertificateFromByte(
        certIterator[certPointer++]);
        }
        else
        {
          certIterator = null;
          return null;
        }
  //    }
    }

  public boolean hasMoreCertificates() throws CertificateException
    {
        synchronized (certLock)
        {
          if (certIterator == null)
        throw new CertificateException(
          "SecurityDBImplementation.hasMoreCertificates: "
            + "cert iterator is not set up.");
          return (certPointer < certIterator.length);
       }
    }

  public int selectCRLByIssuerAndTime(
    X500Principal issuerName,
    java.util.Date time,
    Vector crlList)
      throws CertificateException
    {
        throw new CertificateException("SecurityDBImplementation.selectCRLByIssuerAndTime: Not Implemented Yet!");
    }

  public CRL firstCRL() throws CertificateException
  {
        throw new CertificateException("SecurityDBImplementation.firstCRL: Not Implemented Yet!");
  }

  public CRL nextCRL() throws CertificateException
    {
        throw new CertificateException("SecurityDBImplementation.nextCRL: Not Implemented Yet!");
    }

  public boolean hasMoreCRLs() throws CertificateException
    {
        throw new CertificateException("SecurityDBImplementation.hasMoreCRLs: Not Implemented Yet!");
    }

  public PrivateKey selectPrivateKeyByCertificate(
    java.security.cert.Certificate cert)
      throws CertificateException
    {
      try
      {
      X509Certificate X509Cert = (X509Certificate) cert;
      Certificate gncert =
        findCertificateByIssureAndSerialNum(
          GridCertUtilities.writeIssuerNameToString(X509Cert.getIssuerX500Principal()),
          GridCertUtilities.writeByteArrayToString(X509Cert.getSerialNumber().toByteArray()));
      if (gncert == null || gncert.getPrivateKey() == null)
          return null;
        return GridCertUtilities.loadPrivateKeyFromString(gncert.getPrivateKey());
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "selectPrivateKeyByCertificate: "
          + e.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectPrivateKeyByCertificate: "
          + ex.getMessage());
    }

    }

    public PrivateKey selectPrivateKeyByPublicKey(PublicKey publicKey)
    throws CertificateException
    {
      try
      {
        /** @todo selectPrivateKeyByPublicKey */
      Certificate[] certlist = (Certificate[]) getAllCertificates().toArray();
      for (int i = 0; i < certlist.length; i++)
      {
        PublicKey publicKey1 =
          GridCertUtilities.loadPublicKeyFromString(certlist[i].getPublicKey());
        if (GridCertUtilities.writePublicKeyToString(publicKey).equals(GridCertUtilities.writePublicKeyToString(publicKey1)))
        {
          return GridCertUtilities.loadPrivateKeyFromString(
            certlist[i].getPrivateKey());
        }
      }
      return null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "selectPrivateKeyByPublicKey: "
          + e.getMessage());
    }

    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.selectPrivateKeyByPublicKey: "
          + ex.getMessage());
    }

  }

  public PrivateKey firstPrivateKey() throws CertificateException
    {
      synchronized (keyLock)
      {
//        GNCertificate[] certlist = (GNCertificate[])dbhandler.getAllGNCertificate().toArray();
       try
       {
        Collection rec = getAllCertificates();
//System.out.println("certlist size " + rec.size());
        Certificate[] certlist =
          (Certificate[]) rec.toArray(new Certificate[rec.size()]);
//        certIterator = new byte[certlist.length][];
        byte[][] tempIterator = new byte[certlist.length][];
        String str = null;
        int n = 0;
        for (int i = 0; i < certlist.length; i++)
        {
//System.out.println("certlist[" + i + "]: " + certlist[i]);
          str = certlist[i].getPrivateKey();
          if (str != null && (!"".equals(str)))
          {
            tempIterator[n] = GridCertUtilities.loadByteArrayFromString(str);
            n++;
          }
        }
        keyIterator = new byte[n][];
        if (n != 0)
        {
          System.arraycopy(tempIterator, 0, keyIterator, 0, n);
        }

        keyPointer = 0;
        return nextPrivateKey();
        }

      catch (Throwable ex)
        {
          return null;
        }

      }
    }

  public PrivateKey nextPrivateKey() throws CertificateException
  {
    synchronized (keyLock)
    {
      if (keyIterator == null)
        throw new CertificateException(
          "FlatFileDBImplementation.nextPrivateKey: "
            + "no key iterator is set up.");
      if (hasMorePrivateKeys())
      {
        return GridCertUtilities.loadPrivateKeyFromByte(
          keyIterator[keyPointer++]);
        }
        else
        {
          keyIterator = null;
          return null;
        }
      }
    }

  public boolean hasMorePrivateKeys() throws CertificateException
  {
    synchronized (keyLock)
    {
      if (keyIterator == null)
        throw new CertificateException(
          "SecurityDBImplementation.hasMorePrivateKeys: "
            + "no key iterator is set up.");
        return (keyPointer < keyIterator.length);
        }
    }

    public void deleteCertificate(int gNId, String name)
    throws DeleteEntityException, SystemException
    {
     try
     {
      Certificate gncert = findCertificateByIDAndName(gNId, name);
      if (gncert == null
        || gncert.getIssuerName() == null
        || gncert.getSerialNumber() == null)
        return;
      deleteCertificateByIssuerAndSerialNum(
        gncert.getIssuerName(),
        gncert.getSerialNumber());
    }
    catch (Exception ex)
    {
      CertificateLogger.warn(
        "[CertificateManagerBean.deleteCertificateByIDAndName] BL Exception",
        ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new SystemException("Cannot Deltet The Certificate ", ex);
    }
  }

    public void deleteCertificate(X500Principal issuerName, byte[] serialNumber)
      throws CertificateException
    {
      try
      {
      deleteCertificateByIssuerAndSerialNum(
        GridCertUtilities.writeIssuerNameToString(issuerName),
        GridCertUtilities.writeByteArrayToString(serialNumber));
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deleteCertificate: " + e.getMessage());
      }
      catch (Throwable ex)
      {
        ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deleteCertificate: " + ex.getMessage());
    }

  }

  public void deleteCRL(Principal issuerName, java.util.Date lastUpdate)
    throws CertificateException
  {
    throw new CertificateException("SecurityDBImplementation.deleteCRL: Not Implemeted Yet");
  }

  public void deletePrivateKeyByCertificate(
    java.security.cert.Certificate cert)
    throws CertificateException
  {
    try
    {
      removePrivateKeyByCertificate(
        GridCertUtilities.writeCertificateToString((X509Certificate) cert));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "deletePrivateKeyByCertificate: "
          + e.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deletePrivateKeyByCertificate: "
          + ex.getMessage());
      }

    }

    public void deletePrivateKeyByIdAndName(int gNId, String name)
    throws CertificateException
    {
      try
      {
      Certificate gncert = findCertificateByIDAndName(gNId, name);
      if (gncert == null || gncert.getCertificate() == null)
          return;
        removePrivateKeyByCertificate(gncert.getCertificate());
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation."
          + "deletePrivateKeyByCertificate: "
          + e.getMessage());
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deletePrivateKeyByCertificate: "
          + ex.getMessage());
      }

    }

    public void deletePrivateKeyByPublicKey(PublicKey publicKey)
      throws CertificateException
    {
      try
      {
      removePrivateKeyByPublicKey(
        GridCertUtilities.writePublicKeyToString(publicKey));
      }
      catch (Exception e)
      {
      e.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deletePrivateKeyByPublicKey: "
          + e.getMessage());
      }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      throw new CertificateException(
        "SecurityDBImplementation.deletePrivateKeyByPublicKey: "
          + ex.getMessage());
    }

  }

  public String toString()
    {
      return "SecurityDB provider named: " + "Certificate Manager Bean";

    }

    public X509Certificate getOwnSignCert(Certificate signCert, long threshold)
      throws CertificateException
    {
      return getEntityHandler().getOwnSignCert(signCert, threshold);
    }

    public X509Certificate getPartnerEncryptCert(Certificate encryptCert)
      throws CertificateException
    {
      return getEntityHandler().getPartnerEncryptCert(encryptCert);
    }

    public X509Certificate getX509Certificate(Certificate cert)
      throws CertificateException
    {
      return getEntityHandler().getX509Certificate(cert);
    }

    public X509Certificate getPendingX509Cert(Certificate cert)
      throws CertificateException
    {
      if (cert == null) return null;
      Certificate pendingCert = getEntityHandler().getPendingCert(cert);

      return getX509Certificate(pendingCert);
    }

    private void isCACert(String certFilePath, Boolean isCA)
    	throws InvalidCACertificateException, Exception
    {
    	if(! isCA)
    	{
    		return; //no need to validate
    	}
    	else
    	{
    		X509Certificate cert = JavaKeyStoreHandler.loadCertificate(certFilePath);
    		boolean isRealCA = JavaKeyStoreHandler.isCACert(cert);
    		
    		if(! isRealCA)
    		{
    			throw new InvalidCACertificateException("[CertificateManagerBean.isCACert] The importing cert is not a CA cert !");
    		}
    	}
    }
    
    /**
     * TWX 08082006 Ensure the Cert's direct CA is in the keystore
     * @param cert Certificate Entity
     * @throws CertificateException
     */
    private void isParentCertExistInKeyStore(Certificate cert)
    	throws CertificateException
    {
    	byte[] certInByte = GridCertUtilities.decode(cert.getCertificate());
    	boolean isParentCertInKeyStore = false;
    	
    	try
    	{
    		isParentCertInKeyStore = JavaKeyStoreHandler.isParentCACertsInTrustStore(
                                                                                  certInByte
      																		                                       );
    		if(! isParentCertInKeyStore)
    		{
    			throw new CertificateException("[CertificateManagerBean.isParentCertExistInKeyStore] Parent cert is not in truststore !!!");
    		}
    	}
    	catch(Exception ex)
    	{
    		CertificateLogger.warn("[CertificateManagerBean.isParentCertExistInKeyStore] Error is "+ex.getMessage(), ex);
    		throw new CertificateException("[CertificateManagerBean.isParentCertExistInKeyStore] Error is "+ex.getMessage(), ex);
    	}
    }
    
    /**
     * TWX 24082006 Set the replacement cert uid of cert to replacementCertUid.
     * @param cert
     * @param replacementCertUid
     */
    private void setReplacementCert(Certificate cert, Long replacementCertUid)
    	throws Throwable
    {
    	CertificateLogger.log("[CertificateManagerBean.setReplacementCert] set certUID "+cert.getKey()+" replacement cert to id "+replacementCertUid);
    	cert.setReplacementCertUid(replacementCertUid);
    	getEntityHandler().update(cert);
    }
    
    /**
     * TWX 24082006 Clean up all the certificates's replacementCertUid which treat the certUid correspond cert as
     * their replacementCert.
     * @param certUid
     */
    private void cleanReplacementCert(Long certUid)
    	throws Throwable
    {
    	CertificateLogger.log("[CertificateManagerBean.cleanReplacementCert] replacementCert key "+certUid);
    	Collection<Certificate> certs = retrieveCerts(certUid);
    	if(certs != null && certs.size() > 0)
    	{
    		CertificateEntityHandler certHandler = getEntityHandler();
    		Iterator<Certificate> i = certs.iterator();
    		while(i.hasNext())
    		{
    			Certificate cert = i.next();
    			CertificateLogger.log("[CertificateManagerBean.cleanReplacementCert] Cert be cleanup : key "+cert.getKey()+" "+cert.getCertName());
    			
    			cert.setReplacementCertUid(null);
    			certHandler.update(cert);
    		}
    	}
    }
    
    /**
  	 * TWX 24082006 Retrieve a collection of non-revoked certificates which refer their replacement
  	 * certificate as the one that the certUid correspond.
  	 * @return
  	 */
  	private Collection<Certificate> retrieveCerts(Long certUid)
  		throws Throwable
  	{
  		IDataFilter filter = new DataFilterImpl();
  		filter.addSingleFilter(null, ICertificate.REVOKEID, 
  		                           filter.getEqualOperator(), new Integer(0), false);
  		filter.addSingleFilter(filter.getAndConnector(), ICertificate.REPLACEMENT_CERT_UID, filter.getEqualOperator(), certUid, false);
  		return getEntityHandler().getEntityByFilter(filter);
  	}
  	
    /**
     * TWX 11122006 To update the cert's(that identify by the certUID) replacement cert UID to the replacementCertUID. If the given relatedCertUID
     * is null, no update will be performed.
     * @param certUID it indicate the identifier of a certificate that required to be updated the replacementCertUID
     * @param replacementCertUID it indicate the cert identifier that will be used as replacement cert
     * @throws Throwable
     */
    private void updateCertReplacementCertUID(Long certUID, Long replacementCertUID) throws Throwable
    {
      if(certUID != null)
      {
        Certificate relatedCert = (Certificate)getEntityHandler().getEntityByKey(certUID);
        setReplacementCert(relatedCert, replacementCertUID);
      }
    }
    
  	//Test delete
  	public Certificate getReplacementCert(Certificate c) throws Throwable
  	{
  		return getEntityHandler().getReplacementCert(c, -1);
  	}
  	
  	public Certificate getReplacementCert(Certificate c, long threshold) throws Throwable
  	{
  		return getEntityHandler().getReplacementCert(c, threshold);
  	}
  	
  	public IEntityDAO getICalAlarmDAO()
    {
      return EntityDAOFactory.getInstance().getDAOFor(iCalAlarm.class.getName());
    }
  	
  	public static IiCalTimeMgrObj getManager() throws ServiceLookupException
    {
      return (IiCalTimeMgrObj) ServiceLocator
        .instance(ServiceLocator.CLIENT_CONTEXT)
        .getObj(
          IiCalTimeMgrHome.class.getName(),
          IiCalTimeMgrHome.class,
          new Object[0]);
    }
  	
}