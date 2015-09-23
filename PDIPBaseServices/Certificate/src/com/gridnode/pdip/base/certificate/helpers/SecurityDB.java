/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityDB
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * JUL 03 2002    Jagadeesh           Created
 * OCT 24 2002    Jagadeesh           Modified: CertificateEntityHandler to
 *                                    handle all database operations.
 * Dec 26 2002    Jagadeesh           Modified: static method provided to get
 *                                    instance of SecurityDB.
 *                                    CertJ instance variable is used.
 *
 * Feb 21 2003    Jagadeesh           Modified : To check for CertPath- And CertJ.
 *                                    Note:- The CertJ library (i.e)
 *                                    com.rsa.certj.pkcs7.SignedData would insertCertificate
 *                                    in DataBase - (As the Provider is the GridNodeDB).
 *                                    (See.SignedData.verifySignature()).
 *
 * Mar 09 2003    Jagadeesh           Added: Methods to Added TrusteCertificate when a
 *                                    a new Certificate is Imported, and initilize
 *                                    CertPathCtx.
 *
 *Apr 28  2003    Qingsong            Added: isPasswordset
 *                                    Modified: setPrivatepassword, setPassword
 *
 *Jun 05  2003    Jagadeesh           Added: Enhance to support CertJ2.1.
 *                                    Methods Added:
 *                                    -------------
 *                                    1.setupCertificateIterator()
 *                                    2.setupCRLIterator()
 *                                    3.setupPrivateKeyIterator()
 *                                    4.isPrivateKeyIteratorSetup()
 *                                    5.isCertificateIteratorSetup()
 *                                    6.isCRLIteratorSetup()
 * Jun 04 2004   Jagadeesh            Modified : To synchronize getCertJ(), and 
 *                                    getDBService(). To synchronize on getInstance 
 *                                    at Method level. 
 *                                    
 *                                    The above modification is due to bug reported by 
 *                                    while at customer place. [On 04/06/2004].      
 * Oct 17 2005   Neo Sok Lay          Change from private to protected access to improve
 *                                    performance: privatepassword, SecurityDBImplementation().
 * Jul 26 2006   Tam Wei Xiang        Modified method insertCertificate(Certificate). New method
 *                                    signature for entityHandler.createCertificate(...)   
 * Aug 04 2006   Tam Wei Xiang        Amend the way we access SecurityDB instance 
 * Dec 28 2006   Neo Sok Lay          Modify addTrustedCertificates(): Assign pathCerts to _trustedCertificate
 * Feb 09 2007		Alain Ah Ming				Log error codes or warning message
 * Jul 28 2009   Tam Wei Xiang        #560 - commented the class as it is RSA JSAFE specific implementation
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.util.TimeUtil;
//import com.rsa.certj.*;
//import com.rsa.certj.cert.*;
//import com.rsa.certj.provider.path.PKIXCertPath;
//import com.rsa.certj.provider.revocation.CRLCertStatus;
//import com.rsa.certj.spi.db.DatabaseException;
//import com.rsa.certj.spi.db.DatabaseInterface;
//import com.rsa.certj.spi.path.CertPathCtx;
//import com.rsa.jsafe.JSAFE_PrivateKey;
//import com.rsa.jsafe.JSAFE_PublicKey;
//import com.rsa.jsafe.JSAFE_SecretKey;
//import com.rsa.jsafe.JSAFE_SymmetricCipher;

/**
 *
 * This class is a "ProviderImplementation(Note:1)" for Certificate Entity in GTAS.
 *
 * The database specific operations are handled by a LocalEntity Bean.
 *
 * If you intend to use this ProviderImplementation as a "BLACK-BOX" for other
 * "GTAS" specific applications(Certificate Entity Model), you have to uncomment
 * the Code Block in SecurityDB(String name, String privatepassword) constructor.
 *
 * This enables SecurityDB class to be used as Stand-Alone client, and acts as a
 * deligate to Certificate specific services.
 *
 * Please note that all Certificate specific services are "Encode" and "Decode".
 * Which means you need to (Encode/Decode) your Strings before you use this Services.
 * Use <code>GridCertUtilities</code> class to Encode or Decode.
 *
 *
 * Code Example:
 * ----------------------------------------------------------------------------
 *
 *     CertJ _certJ = null;
 *     SecurityDB _securityDB=null;
 *     SecurityDB.SecurityDBImplementation _implementation = null;
 *
 *     _certJ = new CertJ(); //This call can take few seconds.
 *
 *     Provider[] providers = SecurityDB.createProviders("GRIDNODE_DB");
 *     _securityDB = (SecurityDB)providers[0];
 *     _certJ.addProvider(providers);
 *     _implementation =
 *         (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
 *     _implementation.inserCertificate(....);
 *  ---------------------------------------------------------------------------
 *
 * Note:1
 * ------
 * RSA-Library provides three types of DataStore :
 * 1.FileDataStore
 * 2.MemoryDataStore
 * 3.DatabaseDataStore.(Implemented in GTAS to use LocalEntityBean)
 *
 * @version GT 4.0 VAN
 */

public class SecurityDB 
//extends Provider
{
//
//  //******** Private Password is set to Default. GT1.x has this information passsed ***/
  static private String defprivatepassword = "GridNode";
  static private boolean issetPassword = false;
  static protected String privatepassword = null;
//
//  private CertJ _certJ = null;
//  //private static boolean _initilized = false; 
//
//  private SecurityDB _securityDB = null;
//  private DatabaseService _dbService = null;
//  private SecurityDBImplementation _implementation = null;
//
//  private static final String GRIDNODE_DB = "GridNodeSecurityDB";
//
//  private CertPathCtx _pathCtx = null;
//  
//  //TWX 04082006
//  private static com.rsa.certj.cert.Certificate[] _trustedCertificate = null;
//  private static Date _validationTime = null;
//  private static final Object _trustedCertLock = new Object();
//  
//  public void cleanup()
//  {
//  	_certJ = null;
//  	_securityDB = null;
//  	_dbService = null;
//  	_implementation = null;
//  }
//  
//  public void setPrivatepassword(String privatepassword)
//  {
//  	CertificateLogger.log("[SecurityDB] Setting private password...");
//    SecurityDB.privatepassword = privatepassword;
//    issetPassword = true;
//  }
//  /*This Method is added here: Since there is a requirement for a place holder for password
//   *and this password is static through out one application session(i.e Login -to - Logout).
//   */
//
  public boolean isPasswordset()
  {
  	CertificateLogger.log("[SecurityDB] is password set...");
    return issetPassword;
  }
//
  public String getPrivatePassword()
  {
  	CertificateLogger.log("[SecurityDB] Getting private password...");
    return (privatepassword == null ? defprivatepassword : privatepassword);
  }

  public void setPassword(String pw)
  {
  	CertificateLogger.log("[SecurityDB] Setting password...");
    privatepassword = pw;
    issetPassword = true;
  }
//  //????
//  
//  
//  public CertJ getCertJ()
//    throws ProviderManagementException
//  {
//    return _certJ;
//  }
//
//  public DatabaseService getDBService()
//  {
//    return _dbService;
//  }
//
//  public CertPathCtx getCertPathContext()
//  {
////  TWX 04082006
//  		synchronized(_trustedCertLock)
//  		{
//  			Date validationDate = _pathCtx.getValidationTime();
//  	
//  			if(validationDate.getTime() != _validationTime.getTime())
//  			{
//  				_pathCtx =
//  								new CertPathCtx(
//  			                CertPathCtx.PF_IGNORE_REVOCATION,
//  			                _trustedCertificate,
//  			                null,
//  			                _validationTime,
//  			                _dbService); 
//  			}
//  		}	
//    return _pathCtx;
//  }
//
//  public DatabaseService bindService(CertJ certj, String name)
//    throws ProviderManagementException, InvalidParameterException
//  {
//    return (DatabaseService)certj.bindService(CertJ.SPT_DATABASE, name);
//  }
//
//  public void unregisterService(CertJ certj, String name)
//    throws InvalidParameterException
//  {
//    certj.unregisterService(CertJ.SPT_DATABASE, name);
//    certj.unregisterService(CertJ.SPT_CERT_PATH, name + " Cert Path");
//    certj.unregisterService(CertJ.SPT_CERT_STATUS, name + " Cert Status");
//  }
//
//  public void registerService(CertJ certj, String name)
//    throws InvalidParameterException, ProviderManagementException
//  {
//    Provider[] providers = createProviders(name);
//    certj.registerService(providers[0]);
//    certj.registerService(providers[1]);
//    certj.registerService(providers[2]);
//  }
//
//  public Provider[] createProviders(String name)
//    throws InvalidParameterException
//  {
//    Provider[] providers = new Provider[3];
//    providers[0] = databaseProvider(name);
//    providers[1] = certPathProvider(name + " Cert Path");
//    providers[2] = certStatusProvider(name + " Cert Status");
//    return providers;
//  }
//
//  public Provider certStatusProvider(String name)
//    throws InvalidParameterException
//  {
//    return new CRLCertStatus(name);
//  }
//
//  public Provider certPathProvider(String name)
//    throws InvalidParameterException
//  {
//    return new PKIXCertPath(name);
//  }
//
//  public Provider databaseProvider(String name)
//    throws InvalidParameterException
//  {
//    //return new SecurityDB(name);
//  	return this;
//  }
//  
//  public SecurityDB() throws InvalidParameterException, Exception
//  {
//  	this(GRIDNODE_DB);
//  }
//  
//  public SecurityDB(String name) throws InvalidParameterException, Exception
//  {
//    this(
//      name,
//      (privatepassword == null ? defprivatepassword : privatepassword));
//  }
//
//  private SecurityDB(String name, String privatePassword)
//    throws InvalidParameterException, Exception
//  {
//    super(CertJ.SPT_DATABASE, name);
//    privatepassword = privatePassword;
//    
//    try
//    {
//    	initDB();
//    }
//    catch(Exception ex)
//    {
//    	CertificateLogger.warn(
//    	                      "[SecurityDB][SecurityDB()] Failed to Create SecurityDB",
//    	                      ex);
//    	throw new Exception("[SecurityDB][SecurityDB(name, privatePassword)] Failed to Create SecurityDB");                    
//    }
//  }
//
//  private void initDB() throws Exception
//  {
//    try
//    {
//      CertificateLogger.log(
//        "[SecurityDB][initDB()] Before Initializing CertJ Services ");
//      _certJ = new CertJ(); //This call can take few seconds.
//      CertificateLogger.debug("[SecurityDB][initDB()] After Creating .. ");
//      Provider[] providers = createProviders(GRIDNODE_DB);
//      _securityDB = (SecurityDB)providers[0];
//      for (int i = 0; i < providers.length; i++)
//      {
//        _certJ.addProvider(providers[i]);
//      }
//      _implementation =
//        (SecurityDB.SecurityDBImplementation)_securityDB.instantiate(_certJ);
//
//      _dbService =
//        (DatabaseService)_certJ.bindService(CertJ.SPT_DATABASE, GRIDNODE_DB);
//      _pathCtx = initilizeCertPathContext();
//      //_initilized = true;
//      CertificateLogger.log(
//        "[SecurityDB][initDB()] After Initializing CertJ Services  .. ");
//      //return _initilized;
//    }
//    catch (Exception ex)
//    {
//      CertificateLogger.warn(
//        "[SecurityDB][initDB()] SecurityDB Init Failed  ",
//        ex);
//      ex.printStackTrace();
//      throw new InvalidParameterException("[SecurityDB][initDB()] SecurityDB Init Failed");
//    }
//
//  }
//
//  public ProviderImplementation instantiate(CertJ certJ)
//    throws ProviderManagementException
//  {
//    try
//    {
//      SecurityDBImplementation newimp =
//        new SecurityDBImplementation(certJ, getName());
//      return newimp;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      throw new ProviderManagementException(
//        "SecurityDB.instantiate: " + e.getMessage());
//    }
//  }
//
//  /**
//   * This Method instantiates the default provider implementation.
//   * @return ProviderImplementation - Default Provider Implementation
//   * @throws ProviderManagementException
//   */
//  public ProviderImplementation instantiate()
//    throws ProviderManagementException
//  {
//    try
//    {
//      CertJ certJ = new CertJ();
//      Provider[] providers = createProviders(GRIDNODE_DB);
//      SecurityDB securityDB = (SecurityDB)providers[0];
//
//      for (int i = 0; i < providers.length; i++)
//      {
//        certJ.addProvider(providers[i]);
//      }
//      return (SecurityDB.SecurityDBImplementation)securityDB.instantiate(certJ);
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//      throw new ProviderManagementException(
//        "SecurityDB.instantiate: " + ex.getMessage());
//    }
//  }
//
//  public void addTrustedCertificates(
//    com.rsa.certj.cert.Certificate[] certificates)
//    throws Exception
//  {
//    if (certificates != null)
//    {
//      CertificateLogger.log(
//        "[SecurityDB][addTrustedCertificates()][Begin of Add to TrustStore ");
//
//      //com.rsa.certj.cert.Certificate[] pathCerts = _pathCtx.getTrustedCerts();
//      synchronized(_trustedCertLock) //TWX 04082006
//      {
//      	com.rsa.certj.cert.Certificate[] pathCerts = _trustedCertificate;
//      
//      
//      	for (int i = 0; i < certificates.length; i++)
//      		pathCerts = addMoreTrustedCerts(pathCerts, certificates[i]);
//      
//        _trustedCertificate = pathCerts; //NSL20061228
//      	_validationTime = new Date();
//      /*
//      _pathCtx =
//        new CertPathCtx(
//          CertPathCtx.PF_IGNORE_REVOCATION,
//          pathCerts,
//          null,
//          new Date(),
//          _dbService); */
//      	CertificateLogger.log(
//      	                      "[SecurityDB][addTrustedCertificates()][End of Add to TrustStore-Success]"
//      	                      + "[With CertsLength=]"
//      	                      + pathCerts.length);
//      }
//    }
//    else
//    {
//      CertificateLogger.warn(
//        "[SecurityDB][addTrustedCertificates()][No Certificates To Add]");
//    }
//  }
//  
////TWX 04082006
//  private static synchronized com.rsa.certj.cert.Certificate[] getCertificates()
//  	throws Exception
//  {
//    return null;
////  	try
////    {
////  		synchronized(_trustedCertLock)
////  		{
////  			Collection dbcerts =
////  				CertificateEntityHandler.getInstance().getAllCertificates();
////  			if (dbcerts != null)
////  			{
////  				com.gridnode.pdip.base.certificate.model.Certificate[] gtascer =
////  					(
////  							com.gridnode.pdip.base.certificate.model.Certificate[])dbcerts
////  							.toArray(new com.gridnode.pdip.base.certificate.model.Certificate[] {});
////  				CertificateLogger.debug(
////  				                        "[SecurityDB][getCertificates()][Cert to Add Length=]"
////  				                        + gtascer.length);
////  				com.rsa.certj.cert.Certificate[] tCerts =
////  					new com.rsa.certj.cert.Certificate[gtascer.length];
////  				for (int i = 0; i < gtascer.length; i++)
////  				{
////  					com.rsa.certj.cert.X509Certificate certificate =
////  						GridCertUtilities.loadX509CertificateByString(
////  						                                              gtascer[i].getCertificate());
////  					tCerts[i] = certificate;
////  					//tCerts = addMoreTrustedCerts(tCerts,certificate);
////  					CertificateLogger.debug(
////  					                        "[SecurityDB][getCertificates()] Certificate To Add "
////  					                        + gtascer[i].getUId());
////  				}
////        
////  				//TWX 04082006 keep a centralised trusted cert
////        	_trustedCertificate = tCerts;
////        	_validationTime = new Date();
////        
////        	return _trustedCertificate;
////        }
////      	else
////      	{
////      		CertificateLogger.debug(
////          	"[SecurityDB][getCertificates()][No Certs To Add]");
////      	}
////  		}
////      return null;
////    }
////    catch (Throwable th)
////    {
////      CertificateLogger.warn(
////        "[SecurityDB][getCertificates()]Could Not Initilize",
////        th);
////      throw new Exception("[SecurityDB][getCertificates()] Error in obtaining certificates. "+th.getMessage());
////    }
//  }
//  
//  private CertPathCtx initilizeCertPathContext() throws Exception
//  {
//  	//TWX 04082006 avoid initialize multiple times
//  	synchronized(_trustedCertLock)
//  	{
//  		if(_trustedCertificate != null)
//    	{
//  				CertificateLogger.log("SecurityDB initializeCertPathContext: get from cache trustedCert");
//  				return (
//          		new CertPathCtx(CertPathCtx.PF_IGNORE_REVOCATION, _trustedCertificate,
//          		                null, _validationTime,_dbService
//                          		)
//             		);
//  		}
//  	}
//  	
//    try
//    {
//    	com.rsa.certj.cert.Certificate[] tCerts = getCertificates();
//      
//    	if(tCerts != null)
//    	{
//        return (
//          new CertPathCtx(
//            CertPathCtx.PF_IGNORE_REVOCATION,
//            tCerts,
//            null,
//            _validationTime,
//            _dbService));
//    	}
//    	return null;
//    }
//    catch (Throwable th)
//    {
//      CertificateLogger.warn(
//        "[SecurityDB][initilizeCertPathContext()]Could Not Initilize",
//        th);
//      throw new Exception("[SecurityDB][initilizeCertPathContext()] Could Not Initilize");
//    }
//
//  }
//
//  private com.rsa.certj.cert.Certificate[] addMoreTrustedCerts(
//    com.rsa.certj.cert.Certificate[] certs,
//    com.rsa.certj.cert.Certificate newCert)
//  {
//    com.rsa.certj.cert.Certificate[] newCerts =
//      new com.rsa.certj.cert.Certificate[certs.length + 1];
//    System.arraycopy(certs, 0, newCerts, 0, certs.length);
//    newCerts[certs.length] = newCert;
//    return newCerts;
//  }
//  
//  /************* Methods added since new SMIME Introduction 26-DEC-2002 ****************/
//  /** ToDo To introduce Creational Exception **/
//  /*
//  public static synchronized SecurityDB getInstance()
//  {
//    try
//    {
//      return null;
//    }
//    catch (Exception ex)
//    {
//      CertificateLogger.err(
//        "[SecurityDB][getInstance()] Failed to Create SecurityDB",
//        ex);
//      ex.printStackTrace();
//    }
//    return null;
//  }*/
//
//  /*040604NSL These two methods are not used at the moment --- may cause concurrency problems
//  public static SecurityDB getInstance(String name,String privatePassword)
//  {
//   try
//   {
//      if (_securityDB == null)
//      {
//        synchronized (SecurityDB.class)
//        {
//         _securityDB = new SecurityDB(name,privatePassword);
//        }
//      }
//      return _securityDB;
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.err("[SecurityDB][getInstance()]"+
//      " Failed to Create SecurityDB",ex);
//      ex.printStackTrace();
//    }
//    return null;
//  }
//  
//  public static SecurityDB getInstance(String privatePassword)
//  {
//   try
//   {
//      if (_securityDB == null)
//      {
//        synchronized (SecurityDB.class)
//        {
//          _securityDB = new SecurityDB(GRIDNODE_DB,privatePassword);
//        }
//       }
//       return _securityDB;
//    }
//    catch(Exception ex)
//    {
//      CertificateLogger.err("[SecurityDB][getInstance()]"+
//      " Failed to Create SecurityDB",ex);
//      ex.printStackTrace();
//    }
//    return null;
//  }
//  */
//  /*
//  public static void main(String args[]) throws Exception
//  {
//    SecurityDB db = SecurityDB.getInstance();
//  }*/
//
//  public final class SecurityDBImplementation
//    extends ProviderImplementation
//    implements DatabaseInterface
//  {
//    private byte[][] certIterator = null, keyIterator = null;
//    private int certPointer, keyPointer;
//    private Object certIteratorLock = new Object();
//    private Object certLock = new Object();
//    private Object keyLock = new Object();
//    private Object keyIteratorLock = new Object();
//
//    protected SecurityDBImplementation(CertJ certJ, String name)
//      throws InvalidParameterException
//    {
//      super(certJ, name);
//    }
//    /*
//    private X509CRL loadCRLFromByte(byte[] crlData) throws DatabaseException
//    {
//      try
//      {
//        X509CRL theCRL = new X509CRL(crlData, 0, 0);
//        return (theCRL);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.loadCRLFromByte: " + e.getMessage());
//      }
//    }
//
//    private byte[] writeCRLToByte(X509CRL crl) throws DatabaseException
//    {
//      try
//      {
//        byte[] crlData = new byte[crl.getDERLen(0)];
//        crl.getDEREncoding(crlData, 0, 0);
//        return crlData;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.writeCRLToByte: " + e.getMessage());
//      }
//    }*/
//
//    private JSAFE_PublicKey loadPublicKeyFromByte(byte[] keyData)
//      throws DatabaseException
//    {
//      JSAFE_PublicKey publicKey = null;
//      try
//      {
//        publicKey = JSAFE_PublicKey.getInstance(keyData, 0, certJ.getDevice());
//        return publicKey;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.loadPublicKeyFromByte: " + e.getMessage());
//      }
//    }
//
//    private byte[] writeCertificateToByte(X509Certificate certificate)
//      throws DatabaseException
//    {
//      try
//      {
//        byte[] certificateData = new byte[certificate.getDERLen(0)];
//        certificate.getDEREncoding(certificateData, 0, 0);
//        return certificateData;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.writeCertificateToByte: " + e.getMessage());
//      }
//    }
//
//    private byte[] writePublicKeyToByte(JSAFE_PublicKey publicKey)
//      throws DatabaseException
//    {
//      try
//      {
//        byte[][] keyData = publicKey.getKeyData("RSAPublicKeyBER");
//        return keyData[0];
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.writePublicKeyToByte: " + e.getMessage());
//      }
//    }
//
//    private String writeByteArrayToString(byte[] data) throws DatabaseException
//    {
//      try
//      {
//        return GridCertUtilities.encode(data);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.writeByteArrayToString: " + e.getMessage());
//      }
//    }
//
//    private byte[] loadByteArrayFromString(String data)
//      throws DatabaseException
//    {
//      try
//      {
//        return GridCertUtilities.decode(data);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.loadByteArrayFromString: "
//            + e.getMessage());
//      }
//    }
//
//    private String writeIssuerNameToString(X500Name issuerName)
//      throws DatabaseException
//    {
//      try
//      {
//        return GridCertUtilities.encode(issuerName.toString().getBytes());
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.writeSerialNumberToByte: "
//            + e.getMessage());
//      }
//    }
//
//    private X509Certificate loadCertificateFromByte(byte[] certificateData)
//      throws DatabaseException
//    {
//      try
//      {
//        X509Certificate cert = new X509Certificate(certificateData, 0, 0);
//        return (cert);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.loadCertificateFromByte: "
//            + e.getMessage());
//      }
//    }
//
//    private JSAFE_PrivateKey loadPrivateKeyFromByte(byte[] keyData)
//      throws DatabaseException
//    {
//      JSAFE_SymmetricCipher decrypter = null;
//      JSAFE_SecretKey key = null;
//      JSAFE_PrivateKey privateKey = null;
//
//      try
//      {
//        decrypter = JSAFE_SymmetricCipher.getInstance(keyData, 0, "Java");
//        key = decrypter.getBlankKey();
//        char[] password1 = new char[privatepassword.length()];
//        privatepassword.getChars(0, privatepassword.length(), password1, 0);
//        key.setPassword(password1, 0, privatepassword.length());
//        decrypter.decryptInit(key, null);
//        privateKey =
//          decrypter.unwrapPrivateKey(keyData, 0, keyData.length, true);
//        return (privateKey);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.loadPrivateKeyFromByte: " + e.getMessage());
//      }
//    }
//    /*
//    private String hexEncode(byte value)
//    {
//      char[] hex = new char[2];
//      int lower = value & 0xf;
//      if (lower < 0)
//        lower += 16;
//      int higher = value >> 4;
//      if (higher < 0)
//        higher += 16;
//      if (lower < 10)
//        hex[1] = (char) ('0' + lower);
//      else
//        hex[1] = (char) ('A' + (lower - 10));
//      if (higher < 10)
//        hex[0] = (char) ('0' + higher);
//      else
//        hex[0] = (char) ('A' + (higher - 10));
//      return (new String(hex));
//    }*/
//
//    private byte[] writePrivateKeyToByte(JSAFE_PrivateKey privateKey)
//      throws DatabaseException
//    {
//      if (privateKey != null)
//      {
//        JSAFE_SymmetricCipher encrypter = null;
//        JSAFE_SecretKey key = null;
//        byte[] salt =
//          {
//            (byte)0x00,
//            (byte)0x11,
//            (byte)0x22,
//            (byte)0x33,
//            (byte)0x44,
//            (byte)0x55,
//            (byte)0x66,
//            (byte)0x77 };
//        try
//        {
//          encrypter =
//            JSAFE_SymmetricCipher.getInstance(
//              "PBE/SHA1/DES/CBC/PKCS5PBE-5-56",
//              "Java");
//          encrypter.setSalt(salt, 0, salt.length);
//
//          key = encrypter.getBlankKey();
//          char[] password1 = new char[20];
//          privatepassword.getChars(
//            0,
//            (privatepassword.length() > 20 ? 20 : privatepassword.length()),
//            password1,
//            0);
//          key.setPassword(password1, 0, privatepassword.length());
//
//          encrypter.encryptInit(key, null);
//
//          byte[] privateKeyData = encrypter.wrapPrivateKey(privateKey, true);
//
//          return privateKeyData;
//        }
//        catch (Exception e)
//        {
//          e.printStackTrace();
//          throw new DatabaseException(
//            "SecurityDBImplementation.writePrivateKeyToByte: "
//              + e.getMessage());
//        }
//      }
//      else
//      {
//        CertificateLogger.log("Private Key is Null ");
//        throw new DatabaseException("SecurityDBImplementation.writePrivateKeyToByte: ");
//      }
//
//    }
//
//    public void insertCertificate(com.rsa.certj.cert.Certificate cert)
//      throws DatabaseException
//    {
//      try
//      {
//        CertificateLogger.log(
//          "[SecurityDB][insertCertificate]In Insert Certificate");
//
//        String issuerName =
//          writeIssuerNameToString(((X509Certificate)cert).getIssuerName());
//        String serialNum =
//          writeByteArrayToString(((X509Certificate)cert).getSerialNumber());
//        String subjectPublicKey =
//          writeByteArrayToString(
//            writePublicKeyToByte(
//              ((X509Certificate)cert).getSubjectPublicKey(certJ.getDevice())));
//
//        CertificateLogger.debug(
//          "[SecurityDB][insertCertificate]IssuerName-->" + issuerName);
//        CertificateLogger.debug(
//          "[SecurityDB][insertCertificate]SerialNum-->" + serialNum);
//        CertificateLogger.debug(
//          "[SecurityDB][insertCertificate]Subject PublicKey-->"
//            + subjectPublicKey);
//
//        if (null
//          == getEntityHandler().findCertificateByIssureAndSerialNum(
//            issuerName,
//            serialNum))
//        {
//          CertificateLogger.log(
//            "[SecurityDB][insertCertificate] Inserting Certificate As Cert Dose Not Exists");
//          
//          //TWX 20060726 new method signature for createCertificate
//          getEntityHandler().createCertificate(
//            serialNum,
//            issuerName,
//            writeByteArrayToString(
//              writeCertificateToByte((X509Certificate)cert)),
//            subjectPublicKey,
//            convertDateToUtc(((X509Certificate)cert).getStartDate()), 
//            convertDateToUtc(((X509Certificate)cert).getEndDate()));
//        }
//        else
//          CertificateLogger.log(
//            "[SecurityDB][insertCertificate] Not Inserting Certificate As Cert Already Exists");
//
//        CertificateLogger.log(
//          "[SecurityDB][insertCertificate]End Insert Certificate");
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertCertificate: " + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertCertificate: " + ex.getMessage());
//      }
//
//    }
//    
//    //TWX 27072006
//    private Date convertDateToUtc(Date localTime)
//    {
//    	return new Date(TimeUtil.localToUtc(localTime.getTime()));
//    }
//    
//    public void insertCRL(CRL crl) throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.insertCRL: Not Implemeted Yet!");
//    }
//
//    public void insertCertificate(
//      int gNId,
//      String name,
//      com.rsa.certj.cert.Certificate cert)
//      throws DatabaseException
//    {
//      try
//      {
//        insertCertificate(cert);
//        insertIdAndNameByCertificate(gNId, name, cert);
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertCertificate: " + ex.getMessage());
//      }
//    }
//
//    public void insertIdAndNameByCertificate(
//      int gNId,
//      String name,
//      com.rsa.certj.cert.Certificate cert)
//      throws DatabaseException
//    {
//      try
//      {
//        getEntityHandler().updateIdAndNameByCertificate(
//          gNId,
//          name,
//          writeByteArrayToString(
//            writeCertificateToByte((X509Certificate)cert)));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "insertGNIdAndNameByCertificate: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertGNIdAndNameByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public com.rsa.certj.cert.Certificate selectCertificateByIdAndName(
//      int gNId,
//      String name)
//      throws DatabaseException
//    {
//      try
//      {
//        Certificate cert =
//          getEntityHandler().findCertificateByIDAndName(gNId, name);
//        if (cert == null || cert.getCertificate() == null)
//          return null;
//        return loadCertificateFromByte(
//          loadByteArrayFromString(cert.getCertificate()));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectCertificateByGNIdAndName: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateByGNIdAndName: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public JSAFE_PrivateKey selectPrivateKeyByIdAndName(int gNId, String name)
//      throws DatabaseException
//    {
//      try
//      {
//        Certificate cert =
//          getEntityHandler().findCertificateByIDAndName(gNId, name);
//        if (cert == null || cert.getPrivateKey() == null)
//          return null;
//        return loadPrivateKeyFromByte(
//          loadByteArrayFromString(cert.getPrivateKey()));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectPrivateKeyByGNIdAndName: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectPrivateKeyByGNIdAndName: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public void insertPrivateKeyByCertificate(
//      com.rsa.certj.cert.Certificate cert,
//      JSAFE_PrivateKey privateKey)
//      throws DatabaseException
//    {
//      try
//      {
//        getEntityHandler().updatePrivateKeyByCertificate(
//          writeByteArrayToString(writePrivateKeyToByte(privateKey)),
//          writeByteArrayToString(
//            writeCertificateToByte((X509Certificate)cert)));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "insertPrivateKeyByCertificate: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertPrivateKeyByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public void insertPrivateKeyByIdAndName(
//      int gNId,
//      String name,
//      JSAFE_PrivateKey privateKey)
//      throws DatabaseException
//    {
//      try
//      {
//        Certificate cert =
//          getEntityHandler().findCertificateByIDAndName(gNId, name);
//        if (cert == null)
//          return;
//        getEntityHandler().updatePrivateKeyByCertificate(
//          writeByteArrayToString(writePrivateKeyToByte(privateKey)),
//          cert.getCertificate());
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "insertPrivateKeyByCertificate: "
//            + e.getMessage());
//      }
//
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertPrivateKeyByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public void insertPrivateKeyByPublicKey(
//      JSAFE_PublicKey publicKey,
//      JSAFE_PrivateKey privateKey)
//      throws DatabaseException
//    {
//      try
//      {
//        getEntityHandler().updatePrivateKeyByPublicKey(
//          writeByteArrayToString(writePrivateKeyToByte(privateKey)),
//          writeByteArrayToString(writePublicKeyToByte(publicKey)));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "insertPrivateKeyByPublicKey: "
//            + e.getMessage());
//      }
//
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.insertPrivateKeyByPublicKey: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public int selectCertificateByIssuerAndSerialNumber(
//      X500Name issuerName,
//      byte[] serialNumber,
//      Vector certList)
//      throws DatabaseException
//    {
//      try
//      {
//        CertificateLogger.log(
//          "[SecurityDB][selectCertificateByIssuerAndSerialNumber()]"
//            + "IssuerName:-->"
//            + issuerName.toString());
//
//        CertificateLogger.log(
//          "[SecurityDB][selectCertificateByIssuerAndSerialNumber()]"
//            + "IssuerName:-->"
//            + writeIssuerNameToString(issuerName));
//
//        CertificateLogger.log(
//          "[SecurityDB][selectCertificateByIssuerAndSerialNumber()]"
//            + "SerialNo:-->"
//            + writeByteArrayToString(serialNumber));
//
//        Certificate cert =
//          getEntityHandler().findCertificateByIssureAndSerialNum(
//            writeIssuerNameToString(issuerName),
//            writeByteArrayToString(serialNumber));
//        X509Certificate cert1 =
//          loadCertificateFromByte(
//            loadByteArrayFromString(cert.getCertificate()));
//        if (cert1 != null && !certList.contains(cert1))
//        {
//          certList.addElement(cert1);
//          return 1;
//        }
//        else
//          return 0;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectCertificateByIssuerAndSerialNumber: "
//            + e.getMessage());
//      }
//
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateByIssuerAndSerialNumber: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public int selectCertificateBySubject(
//      X500Name subjectName,
//      Vector certList)
//      throws DatabaseException
//    {
//      int count = 0;
//      try
//      {
//        System.out.println("In selectCertificateBySubject : ### ");
//        Collection certCollection = getEntityHandler().getAllCertificates();
//        if (certCollection != null)
//        {
//          Object[] certlist = certCollection.toArray();
//          for (int i = 0; i < certlist.length; i++)
//          {
//            com.gridnode.pdip.base.certificate.model.Certificate certEntity =
//              null;
//            if (certlist[i]
//              instanceof com.gridnode.pdip.base.certificate.model.Certificate)
//              certEntity =
//                (com.gridnode.pdip.base.certificate.model.Certificate)certlist[i];
//            else
//            {
//              throw new DatabaseException(
//                "SecurityDBImplementation.selectCertificateBySubject: "
//                  + "Not a Expected Certificate :");
//            }
//            X509Certificate cert =
//              loadCertificateFromByte(
//                loadByteArrayFromString(certEntity.getCertificate()));
//            X500Name foundName = cert.getSubjectName();
//            //Gets Subject with CertPath.CertJ.
//            if (subjectName.equals(foundName))
//            {
//              System.out.println("Subjects are Equal " + foundName);
//              count++;
//              if (!certList.contains(cert))
//              {
//                System.out.println("Adding Certificate to CertList.....##");
//                certList.addElement(cert);
//              }
//            }
//          }
//        }
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateBySubject: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateBySubject: "
//            + ex.getMessage());
//      }
//      return count;
//    }
//
//    public int selectCertificateByExtensions(
//      X500Name baseName,
//      X509V3Extensions extensions,
//      Vector certList)
//      throws DatabaseException
//    {
//      int count = 0;
//      try
//      {
//        Certificate[] certlist =
//          (Certificate[])getEntityHandler()
//            .getAllCertificates()
//            .toArray(new Certificate[] {
//        });
//        for (int i = 0; i < certlist.length; i++)
//        {
//          X509Certificate cert =
//            loadCertificateFromByte(
//              loadByteArrayFromString(certlist[i].getCertificate()));
//          X500Name subjectName = cert.getSubjectName();
//          if ((subjectName.contains(baseName))
//            && (CertJUtils.compareExtensions(extensions, cert.getExtensions())))
//          {
//            if (!certList.contains(cert))
//              certList.addElement(cert);
//            count++;
//          }
//        }
//        return count;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateByExtensions: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.selectCertificateByExtensions: "
//            + ex.getMessage());
//      }
//    }
//
//    public com.rsa.certj.cert.Certificate firstCertificate()
//    {
//      try
//      {
//        synchronized (certIteratorLock)
//        {
//          setupCertificateIterator();
//          return nextCertificate();
//        }
//      }
//      catch (DatabaseException ex)
//      {
//        return null;
//      }
//    }
//
//    public com.rsa.certj.cert.Certificate nextCertificate()
//      throws DatabaseException
//    {
//
//      synchronized (certIteratorLock)
//      {
//        if (!isCertificateIteratorSetup())
//          setupCertificateIterator();
//        synchronized (certLock)
//        {
//          if (certIterator == null)
//            throw new DatabaseException(
//              "SecurityDBImplementation.nextCertificate: "
//                + "cert iterator is not set up.");
//
//          if (hasMoreCertificates())
//          {
//            return loadCertificateFromByte(certIterator[certPointer++]);
//          }
//          else
//          {
//            certIterator = null;
//            return null;
//          }
//        }
//      }
//
//    }
//
//    public boolean hasMoreCertificates() throws DatabaseException
//    {
//      synchronized (certIteratorLock)
//      {
//        if (!isCertificateIteratorSetup())
//          setupCertificateIterator();
//        return (certPointer < certIterator.length);
//      }
//    }
//
//    public int selectCRLByIssuerAndTime(
//      X500Name issuerName,
//      java.util.Date time,
//      Vector crlList)
//      throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.selectCRLByIssuerAndTime: Not Implemented Yet!");
//    }
//
//    public CRL firstCRL() throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.firstCRL: Not Implemented Yet!");
//    }
//
//    public CRL nextCRL() throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.nextCRL: Not Implemented Yet!");
//    }
//
//    public boolean hasMoreCRLs() throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.hasMoreCRLs: Not Implemented Yet!");
//    }
//
//    public JSAFE_PrivateKey selectPrivateKeyByCertificate(
//      com.rsa.certj.cert.Certificate cert)
//      throws NotSupportedException, DatabaseException
//    {
//      try
//      {
//        Certificate gncert =
//          getEntityHandler().findCertificateByIssureAndSerialNum(
//            writeIssuerNameToString(((X509Certificate)cert).getIssuerName()),
//            writeByteArrayToString(((X509Certificate)cert).getSerialNumber()));
//        if (gncert == null || gncert.getPrivateKey() == null)
//          return null;
//        return loadPrivateKeyFromByte(
//          loadByteArrayFromString(gncert.getPrivateKey()));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectPrivateKeyByCertificate: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectPrivateKeyByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public JSAFE_PrivateKey selectPrivateKeyByPublicKey(JSAFE_PublicKey publicKey)
//      throws
//        com.rsa.certj.NotSupportedException,
//        com.rsa.certj.spi.db.DatabaseException
//    {
//      try
//      {
//        Certificate[] certlist =
//          (Certificate[])getEntityHandler()
//            .getAllCertificates()
//            .toArray(new Certificate[] {
//        });
//        for (int i = 0; i < certlist.length; i++)
//        {
//          JSAFE_PublicKey publicKey1 =
//            loadPublicKeyFromByte(
//              loadByteArrayFromString(certlist[i].getPublicKey()));
//          if (CertJUtils
//            .byteArraysEqual(
//              writePublicKeyToByte(publicKey),
//              writePublicKeyToByte(publicKey1)))
//            return loadPrivateKeyFromByte(
//              loadByteArrayFromString(certlist[i].getPrivateKey()));
//        }
//        return null;
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectPrivateKeyByPublicKey: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "selectPrivateKeyByPublicKey: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public JSAFE_PrivateKey firstPrivateKey()
//    {
//      try
//      {
//        synchronized (keyIteratorLock)
//        {
//          setupPrivateKeyIterator();
//          return nextPrivateKey();
//        }
//      }
//      catch (DatabaseException e)
//      {
//        return null;
//      }
//      catch (Throwable ex)
//      {
//        return null;
//      }
//    }
//
//    public JSAFE_PrivateKey nextPrivateKey() throws DatabaseException
//    {
//      synchronized (keyIteratorLock)
//      {
//        if (!isPrivateKeyIteratorSetup())
//          setupPrivateKeyIterator();
//
//        if (hasMorePrivateKeys())
//        {
//          return loadPrivateKeyFromByte(keyIterator[keyPointer++]);
//        }
//        else
//        {
//          keyIterator = null;
//          return null;
//        }
//      }
//    }
//
//    public boolean hasMorePrivateKeys() throws DatabaseException
//    {
//      synchronized (keyIteratorLock)
//      {
//        if (!isPrivateKeyIteratorSetup())
//          setupPrivateKeyIterator();
//        return (keyPointer < keyIterator.length);
//      }
//    }
//
//    public void deleteCertificate(int gNId, String name)
//      throws DatabaseException
//    {
//      try
//      {
//        Certificate gncert =
//          getEntityHandler().findCertificateByIDAndName(gNId, name);
//        if (gncert == null
//          || gncert.getIssuerName() == null
//          || gncert.getSerialNumber() == null)
//          return;
//        getEntityHandler().deleteCertificateByIssuerAndSerialNum(
//          gncert.getIssuerName(),
//          gncert.getSerialNumber());
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "deleteCertificateByIssuerAndSerialNum: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deleteCertificateByIssuerAndSerialNum: "
//            + ex.getMessage());
//      }
//    }
//
//    public void deleteCertificate(X500Name issuerName, byte[] serialNumber)
//      throws DatabaseException
//    {
//      try
//      {
//        getEntityHandler().deleteCertificateByIssuerAndSerialNum(
//          writeIssuerNameToString(issuerName),
//          writeByteArrayToString(serialNumber));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deleteCertificate: " + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deleteCertificate: " + ex.getMessage());
//      }
//
//    }
//
//    public void deleteCRL(X500Name issuerName, java.util.Date lastUpdate)
//      throws DatabaseException
//    {
//      throw new DatabaseException("SecurityDBImplementation.deleteCRL: Not Implemeted Yet");
//    }
//
//    public void deletePrivateKeyByCertificate(
//      com.rsa.certj.cert.Certificate cert)
//      throws DatabaseException, NotSupportedException
//    {
//      try
//      {
//        getEntityHandler().removePrivateKeyByCertificate(
//          writeByteArrayToString(
//            writeCertificateToByte((X509Certificate)cert)));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "deletePrivateKeyByCertificate: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deletePrivateKeyByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public void deletePrivateKeyByIdAndName(int gNId, String name)
//      throws DatabaseException, NotSupportedException
//    {
//      try
//      {
//        Certificate gncert =
//          getEntityHandler().findCertificateByIDAndName(gNId, name);
//        if (gncert == null || gncert.getCertificate() == null)
//          return;
//        getEntityHandler().removePrivateKeyByCertificate(
//          gncert.getCertificate());
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation."
//            + "deletePrivateKeyByCertificate: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deletePrivateKeyByCertificate: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public void deletePrivateKeyByPublicKey(JSAFE_PublicKey publicKey)
//      throws DatabaseException
//    {
//      try
//      {
//        getEntityHandler().removePrivateKeyByPublicKey(
//          writeByteArrayToString(writePublicKeyToByte(publicKey)));
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deletePrivateKeyByPublicKey: "
//            + e.getMessage());
//      }
//      catch (Throwable ex)
//      {
//        ex.printStackTrace();
//        throw new DatabaseException(
//          "SecurityDBImplementation.deletePrivateKeyByPublicKey: "
//            + ex.getMessage());
//      }
//
//    }
//
//    public CertificateEntityHandler getEntityHandler()
//    {
//      return CertificateEntityHandler.getInstance();
//    }
//
//    public String toString()
//    {
//      return "SecurityDB provider named: " + super.getName();
//    }
//
//    /***** Methods Added to support CertJ2.1  on  05-Jun-2003  ****************/
//
//    public void setupCertificateIterator() throws DatabaseException
//    {
//      synchronized (certIteratorLock)
//      {
//        try
//        {
//          Collection rec = getEntityHandler().getAllCertificates();
//          Certificate[] certlist =
//            (Certificate[])rec.toArray(new Certificate[rec.size()]);
//          certIterator = new byte[certlist.length][];
//          for (int i = 0; i < certlist.length; i++)
//            certIterator[i] =
//              loadByteArrayFromString(certlist[i].getCertificate());
//          certPointer = 0;
//        }
//        catch (DatabaseException e)
//        {
//          throw e;
//        }
//        catch (Throwable ex)
//        {
//          ex.printStackTrace();
//        }
//      }
//    }
//
//    public void setupPrivateKeyIterator() throws DatabaseException
//    {
//      synchronized (keyLock)
//      {
//        try
//        {
//          Collection rec = getEntityHandler().getAllCertificates();
//          Certificate[] certlist =
//            (Certificate[])rec.toArray(new Certificate[rec.size()]);
//          byte[][] tempIterator = new byte[certlist.length][];
//          String str = null;
//          int n = 0;
//          for (int i = 0; i < certlist.length; i++)
//          {
//            str = certlist[i].getPrivateKey();
//            if (str != null && (!"".equals(str)))
//            {
//              tempIterator[n] = loadByteArrayFromString(str);
//              n++;
//            }
//          }
//          keyIterator = new byte[n][];
//          if (n != 0)
//          {
//            System.arraycopy(tempIterator, 0, keyIterator, 0, n);
//          }
//          keyPointer = 0;
//        }
//        catch (DatabaseException e)
//        {
//          throw e;
//        }
//        catch (Throwable ex)
//        {
//          ex.printStackTrace();
//        }
//      }
//    }
//
//    public boolean isCertificateIteratorSetup()
//    {
//      synchronized (certIteratorLock)
//      {
//        return (certIterator != null);
//      }
//    }
//
//    public boolean isPrivateKeyIteratorSetup()
//    {
//      synchronized (keyLock)
//      {
//        return (keyIterator != null);
//      }
//    }
//
//    public void setupCRLIterator() throws NotSupportedException
//    {
//      throw new NotSupportedException(
//        "SecurityDBImplementation.setupCRLIterator:" + "Not supported Yet!");
//    }
//
//    public boolean isCRLIteratorSetup() throws NotSupportedException
//    {
//      throw new NotSupportedException(
//        "SecurityDBImplementation.isCRLIteratorSetup:" + "Not supported Yet!");
//    }
//
//  }
}
