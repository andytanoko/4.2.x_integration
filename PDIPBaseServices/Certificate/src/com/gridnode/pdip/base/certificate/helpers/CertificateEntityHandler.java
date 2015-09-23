/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateEntityHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-July-2002    Jagadeesh           Created.
 * 25-July-2002    Jagadeesh           Modified: To include getHome &
 *                                     getProxyInterface methods.
 * 13-Sept-2002    Jagadeesh           Added getCertificate(IDataFilter filter), to
 *                                     retrieve Certificate Entity.
 * 24-OCT-2002     Jagadeesh           Modified: Methods findCertificateByIDAndName,
 *                                     to retrieve Certificate entity for non revoked
 *                                     Certificates.
 * 25-OCT-2002     Jagadeesh           Added: To updateCertificate MasterAndPartner
 *                                     status by UID.
 *
 * 08-Nov-2002     Jagadeesh           Added: To Revoke a Certificate Entity by UID.
 * 16-Jan-2003     Neo Sok Lay         Refactor to throw correct exceptions for
 *                                     finder methods.
 *                                     Comment out unnecessary log statements.
 * 27-Jan-2003     Jagadeesh           Added: Create Entity Method to set isPartner,PrivateKey
 *
 * 10-Mar-2003     Jagadeesh           Modified: To return NULL, when findCertificateByIssureAndSerialNum
 *                                     is invoked.
 * 28-Apr-2003     Qingsong            Added:
 *                                          getAllPrivateKeyCertList, changeCertificateName, isCertExists, getPreviousKeyStoreCertificate, issetPrivatePassword, setPrivatePassword, validatePrivatePassword, getOwnMasterCertificate, changePrivatePassword, removeCertificate
 *
 * 29-Mar-2004     Guo Jianyu          Changed "changeCertificateName" to "updateCertificate"
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * May 08 2006    Neo Sok Lay          GNDB00027090: only revoke cert when need to replace with new cert.
 * 26-July-2006   Tam Wei Xiang        New field for certificate: StartDate, EndDate
 * 27-July-2006   Tam Wei Xiang        New field isCA
 * 07-Aug-2006    Tam Wei Xiang        Amend the way we access SecurityDB. Added method:
 *                                     getPrivatePassword()
 *                                     isPasswordSet()
 *                                     setSecDBPrivatePassword()
 * 28-Aug-2006    Tam Wei Xiang        Modified the way we get the pending cert(replacement cert). 
 *                                     Threshold checking also included.
 *                                     Modified method  getX509Certificate(...)                                   
 * Feb 09 2007			Alain Ah Ming				Use error code or log warning message   
 * Aug 01 2008	  Wong Yee Wah		   #38 Modified method : getX509Certificate(three params) to getCert(...)      
 * 									   Modified method : getX509Certificate(two params)
 * 									   Modified method : findCertificateByIssureAndSerialNum(....)
 * 									   Modified method : getCertificate(...)
 * 									   Modified method : removeCertificate(...)
 * 									   Added method : getDAO()
 * 08 Jun 2009      Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib							                                                                            
 */



package com.gridnode.pdip.base.certificate.helpers;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;


import com.gridnode.pdip.base.certificate.entities.ejb.ICertificateLocalHome;
import com.gridnode.pdip.base.certificate.entities.ejb.ICertificateLocalObj;
import com.gridnode.pdip.base.certificate.exceptions.CertificateException;
import com.gridnode.pdip.base.certificate.exceptions.DuplicateCertificateException;
import com.gridnode.pdip.base.certificate.exceptions.InvalidPasswordException;
import com.gridnode.pdip.base.certificate.exceptions.ResetPasswordException;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * 
 * @author Jagadeesh
 * @since 
 * @version GT 4.0 VAN
 */
public final class CertificateEntityHandler extends LocalEntityHandler
{

  private CertificateEntityHandler()
  {
    super(Certificate.ENTITY_NAME);
    //CertificateLogger.log(" Entity Name is "+Certificate.ENTITY_NAME);
  }

  /**
   * Get an instance of a PartnerEntityHandler.
   */
  public static CertificateEntityHandler getInstance()
  {
    CertificateEntityHandler handler = null;
    if (EntityHandlerFactory.hasEntityHandlerFor(Certificate.ENTITY_NAME, true))
    {
      handler = (CertificateEntityHandler)EntityHandlerFactory.getHandlerFor(
                  Certificate.ENTITY_NAME, true);
    }
    else
    {
      //CertificateLogger.log("Certificate Entity Name "+Certificate.ENTITY_NAME);
      handler = new CertificateEntityHandler();
      EntityHandlerFactory.putEntityHandler(Certificate.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  public void createCertificate(String serialNum, String issuerName,String cert, String publicKey,
                                Date startDate, Date endDate) throws Throwable
  {
        //CertificateLogger.log("Serial Num :" + serialNum);
        //CertificateLogger.log("Issuer Name :"+issuerName);
        //CertificateLogger.log("Cert :"+cert);
        //CertificateLogger.log("Public Key "+publicKey);
        Certificate gnc = new Certificate();
        gnc.setSerialNumber(serialNum);
        gnc.setIssuerName(issuerName);
        gnc.setCertificate(cert);
        gnc.setPublicKey(publicKey);
        
        //TWX
        gnc.setStartDate(startDate);
        gnc.setEndDate(endDate);
        create(gnc);
  }


  public void createCertificate(String serialNum, String issuerName,String cert, String publicKey,String name,
                                Date startDate, Date endDate) throws Throwable
  {
        CertificateLogger.debug("Serial Num :" + serialNum);
        CertificateLogger.debug("Issuer Name :"+issuerName);
        CertificateLogger.debug("Cert :"+cert);
        CertificateLogger.debug("Public Key "+publicKey);
        CertificateLogger.debug("Name "+name);

        Certificate gnc = new Certificate();
        gnc.setSerialNumber(serialNum);
        gnc.setIssuerName(issuerName);
        gnc.setCertificate(cert);
        gnc.setPublicKey(publicKey);
        gnc.setCertName(name);
        
        //TWX
        gnc.setStartDate(startDate);
        gnc.setEndDate(endDate);
        
        create(gnc);
  }

  public void createCertificate(String serialNum,
                                String issuerName,
                                String cert,
                                String publicKey,
                                String name,
                                String privateKey,
                                boolean isPartner,
                                Date startDate,
                                Date endDate,
                                Boolean isCA) throws Throwable
  {
        Certificate gnc = new Certificate();
        gnc.setSerialNumber(serialNum);
        gnc.setIssuerName(issuerName);
        gnc.setCertificate(cert);
        gnc.setPublicKey(publicKey);
        gnc.setCertName(name);
        gnc.setPrivateKey(privateKey);
        gnc.setPartner(isPartner);
        
        //TWX
        gnc.setStartDate(startDate);
        gnc.setEndDate(endDate);
        gnc.setCA(isCA);
        
        create(gnc);
  }




  public void updatePrivateKeyByCertificate(String privateKey, String cert) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.CERTIFICATE, filter.getEqualOperator(), cert, false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find the Record ");

      Certificate certificate =  (Certificate)result.iterator().next();
         //CertificateLogger.log("UID : "+certificate.getUId());
         certificate.setPrivateKey(privateKey);
         //CertificateLogger.log("Private Key :--- "+privateKey);
         update(certificate);
  }

  public void updatePrivateKeyByPublicKey(String privateKey,String publicKey) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.PUBLICKEY, filter.getEqualOperator(), publicKey, false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();
         certificate.setPrivateKey(privateKey);
         update(certificate);
  }

  public void updateIdAndNameByCertificate(int Id, String name, String cert) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Certificate.CERTIFICATE, filter.getEqualOperator(), cert, false);
    Collection result = getEntityByFilter(filter);
    if(result == null || result.isEmpty())
       throw new ApplicationException("Cannot Find the Record ");
    Certificate certificate =  (Certificate)result.iterator().next();
    certificate.setID(Id);
    certificate.setCertName(name);
    update(certificate);
  }

  public void updateMasterAndPartnerByUID(Long uid, boolean isMaster, boolean isPartner) throws Throwable
  {

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.UID, filter.getEqualOperator(),uid, false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();
      certificate.setMaster(isMaster);
      certificate.setPartner(isPartner);
      update(certificate);
  }

  /**
   * Returns Certificate Entity with the given GridNodeId and Name.
   * There can be multiple certificates with the same nodeid and name,
   * and hence there is a additional filter condition where REVOKEID==0.
   *
   * Any Certificate revoked will have its REVOKEID > 0.
   *
   * @param id -NodeID of this Certificate
   * @param name - Name Identifier.
   * @return - Certificate entity.
   * @throws ApplicationException - thrown when Certifiate cannot be retrieved.
   */
  public Certificate findCertificateByIDAndName(int id,String name) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.ID, filter.getEqualOperator(),
      new Integer(id), false);
      filter.addSingleFilter(filter.getAndConnector(), Certificate.NAME,
      filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.REVOKEID,
      filter.getEqualOperator(),new Integer(0),false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();

      return certificate;
  }


  public Certificate findCertificateByName(String name) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.NAME,
      filter.getEqualOperator(), name, false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.REVOKEID,
      filter.getEqualOperator(),new Integer(0),false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();
      return certificate;
  }



  public Certificate findCertificateByIssureAndSerialNum(String issuerName, String serialNum) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.ISSUERNAME, filter.getEqualOperator(),
      issuerName, false);
      filter.addSingleFilter(filter.getAndConnector(), Certificate.SERIALNUM,
      filter.getEqualOperator(), serialNum, false);

      Collection result = getDAO().getEntityByFilter(filter);//WYW 01082008
      if(result == null || result.isEmpty())
         return null;
         //throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();
      return certificate;

  }
  
  /**
   * TWX 20090924: Find the certificate given IssuerName, Serial number and whether it is a partner cert or own cert.
   * 
   * @param issuerName
   * @param serialNum
   * @param isPartner
   * @return the Certificate obj
   * @throws Throwable
   */
  public Certificate findCertificateByIssureAndSerialNum(String issuerName, String serialNum, boolean isPartner) throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.ISSUERNAME, filter.getEqualOperator(),
      issuerName, false);
      filter.addSingleFilter(filter.getAndConnector(), Certificate.SERIALNUM,
      filter.getEqualOperator(), serialNum, false);
      filter.addSingleFilter(filter.getAndConnector(), Certificate.IS_PARTNER, filter.getEqualOperator(), isPartner, false);

      Collection result = getDAO().getEntityByFilter(filter);//WYW 01082008
      if(result == null || result.isEmpty())
         return null;
         //throw new ApplicationException("Cannot Find the Record ");
      Certificate certificate =  (Certificate)result.iterator().next();
      return certificate;

  }

  public Hashtable getAllPrivateKeyCertList() throws Throwable
  {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,Certificate.PRIVATEKEY, filter.getNotEqualOperator(), null, false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.REVOKEID, filter.getEqualOperator(),new Integer(0),false);
      Collection result = getEntityByFilter(filter);
      Object[] certlist = result.toArray();

      Hashtable certtable = new Hashtable();
      for(int i = 0 ; i < certlist.length; i++)
      {
        Certificate acert = (Certificate)certlist[i];
        certtable.put(acert.getCertificate(), GridCertUtilities.loadPrivateKeyFromString(acert.getPrivateKey()));
      }
    return certtable;
  }

   public void updateCertificate(Long uid, String newName, Long relatedCertUid) throws Throwable
   {
      if(isCertExists(uid, newName))
        throw new DuplicateCertificateException("certificate[" + newName +"] already exists");
      Certificate cert = (Certificate)getEntityByKey(uid);
      cert.setCertName(newName);
      cert.setRelatedCertUid(relatedCertUid);
      update(cert);
   }

  public boolean isCertExists(Long uid, String name)
      throws CertificateException,SystemException
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,
                              Certificate.NAME,
                              filter.getEqualOperator(),
                              name,
                              false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.UID, filter.getEqualOperator(), uid , true);

     Collection listCertificates = getEntityByFilter(filter);
     if(listCertificates != null)
     {
       Iterator certs =  listCertificates.iterator();
       CertificateLogger.log("[CertificateEntityHandler.isCertExists]"+
       "Certs Exist"+certs.hasNext());
       if (certs.hasNext())
       {
         Certificate theCert = (Certificate)certs.next();
         if (((Long)(theCert.getKey())).equals(uid)) //the same certificate
           return false;
         else return true;
       }
       else
        return false;
     }
     else
       return false;
    }
    catch(ApplicationException ex)
    {
      CertificateLogger.warn("[CertificateEntityHandler.isCertExists] BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch(Throwable th)
    {
      CertificateLogger.warn("[CertificateEntityHandler][isCertExists] Error ",
                            th);
      throw new SystemException("[CertificateEntityHandler][isCertExists] Error ",
                                th);
    }
  }


  public Certificate getPreviousKeyStoreCertificate()
      throws CertificateException,SystemException
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,
                              Certificate.IS_IN_KS,
                              filter.getEqualOperator(),
                              new Boolean(true),
                              false);
     Collection listCertificates = getEntityByFilter(filter);
     if(listCertificates != null)
     {
       Iterator certs =  listCertificates.iterator();
       CertificateLogger.log("[CertificateEntityHandler.getPreviousKeyStoreCertificate]"+ "Certs Exist"+ certs.hasNext());
       if(certs.hasNext())
        return (Certificate)certs.next();
       else
        return null;
     }
     else
       return null;
    }
    catch(ApplicationException ex)
    {
      CertificateLogger.warn("[CertificateEntityHandler.isCertExists] BL Exception", ex);
      throw new CertificateException(ex.getMessage());
    }
    catch(Throwable th)
    {
      CertificateLogger.warn("[CertificateEntityHandler][isCertExists] Error ",
                            th);
      throw new SystemException("[CertificateEntityHandler][isCertExists] Error ",
                                th);
    }
  }

   public boolean issetPrivatePassword()
   {
      //return SecurityDB.isPasswordset();
  	 return isPasswordSet();
   }

   public void setPrivatePassword(String password) throws InvalidPasswordException, ApplicationException
   {
      if(JavaKeyStoreHelper.emptyString(password) || JavaKeyStoreHelper.emptyString(password))
        throw new InvalidPasswordException("[CertificateEntityHandler] setPrivatePassword: password cannot be empty");
      validatePrivatePassword(password);
      //SecurityDB.setPrivatepassword(password);
      setSecDBPrivatePassword(password);
   }

   public void validatePrivatePassword(String password) throws InvalidPasswordException, ApplicationException
   {
      String oldpass = getPrivatePassword();
      try
      {
      	//TWX
          //SecurityDB.setPrivatepassword(password);
      		setSecDBPrivatePassword(password);
          Certificate cert = null;
          try
          {
            cert = getOwnMasterCertificate();
          }
          catch (Throwable ex)
          {
           throw new ApplicationException("[CertificateEntityHandler] validatePrivatePassword: Internal Error", ex);
          }
          try
          {
            GridCertUtilities.loadPrivateKeyFromString(cert.getPrivateKey()).getAlgorithm();
          }
          catch (Throwable ex)
          {
           throw new InvalidPasswordException("[CertificateEntityHandler] validatePrivatePassword: password is invalid", ex);
          }
      }
      catch (InvalidPasswordException ex)
      {
        //SecurityDB.setPrivatepassword(oldpass);
      	setSecDBPrivatePassword(oldpass);
        throw ex;
      }
      catch (ApplicationException ex1)
      {
        //SecurityDB.setPrivatepassword(oldpass);
      	setSecDBPrivatePassword(oldpass);
        throw ex1;
      }
      catch(Throwable ex2)
      {
        //SecurityDB.setPrivatepassword(oldpass);
      	setSecDBPrivatePassword(oldpass);
        throw new ApplicationException("[CertificateEntityHandler] validatePrivatePassword: Internal Error", ex2);
      }
      //SecurityDB.setPrivatepassword(oldpass);
      setSecDBPrivatePassword(oldpass);
   }

   public Certificate getOwnMasterCertificate() throws Throwable
   {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,Certificate.PRIVATEKEY, filter.getNotEqualOperator(), null, false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.REVOKEID, filter.getEqualOperator(),new Integer(0),false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
         throw new ApplicationException("Cannot Find Own Certificate!");
      Certificate certificate =  (Certificate)result.iterator().next();
      return certificate;
   }

   public void changePrivatePassword(String oldpassword, String newpassword) throws InvalidPasswordException, ResetPasswordException
   {
      if(oldpassword.equals(newpassword))
        return;

      if(JavaKeyStoreHelper.emptyString(oldpassword) || JavaKeyStoreHelper.emptyString(newpassword))
        throw new InvalidPasswordException("[CertificateEntityHandler] changePrivatePassword: password cannot be empty");

      if(!oldpassword.equals(getPrivatePassword()))
        throw new InvalidPasswordException("[CertificateEntityHandler] changePrivatePassword: old password is incorrect");

      Hashtable certlist = null;
      try
      {
        certlist = getAllPrivateKeyCertList();
      }
      catch (Throwable ex)
      {
        throw new ResetPasswordException("[CertificateEntityHandler] changePrivatePassword: cannot retrieve original certificates", ex);
      }
      //SecurityDB.setPrivatepassword(newpassword);
      setSecDBPrivatePassword(newpassword);
      
      Enumeration en = certlist.keys();
      //String content = "";
      while(en.hasMoreElements())
      {
        String cert = (String)en.nextElement();
        PrivateKey key = (PrivateKey)certlist.get(cert);
        try
        {
          updatePrivateKeyByCertificate(GridCertUtilities.writePrivateKeyToString(key), cert);
        }
        catch (Throwable ex)
        {
          throw new ResetPasswordException("[CertificateEntityHandler] changePrivatePassword: cannot update certificates", ex);
        }
      }
   }

  /**
   * Get All Certificates which are not Revoked.
   * @return Collection : A set of all Certificates which are not Revoked.
   * @throws Throwable - thrown when Certifiate cannot be retrieved.
   */

   public Collection getAllCertificates() throws Throwable
   {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,Certificate.UID,filter.getNotEqualOperator(),null,false);
      filter.addSingleFilter(filter.getAndConnector(),Certificate.REVOKEID,
      filter.getEqualOperator(),new Integer(0),false);

      Collection result = getEntityByFilter(filter);
      //if(result == null || result.isEmpty())
      //   throw new Exception("Cannot Find the Record ");
      return result;
   }


   public Collection getCertificate(IDataFilter filter) throws Throwable
   {
     Collection result = null;
     if(filter != null)
     {
        result = getDAO().getEntityByFilter(filter);//WYW 01082008
     }
     else
     {
       result = getAllCertificates();
     //       throw new Exception("Filter Cannot be Null in getCertificate ");
     }
     // if(result == null || result.isEmpty())
     //    throw new Exception("Cannot Find the Record ");
      return result;
   }

   public String removeCertificate(Long uid) throws Throwable
   {
      String name = ((Certificate)getEntityByKey(uid)).getCertName();
      getDAO().remove(uid);//WYW 01082008
      return name;
   }

   public void deleteCertificateByIssuerAndSerialNum(String issuerName, String serialNum) throws Throwable
   {
       DataFilterImpl filter = new DataFilterImpl();
       filter.addSingleFilter(null,Certificate.ISSUERNAME,filter.getEqualOperator(),issuerName,false);
       filter.addSingleFilter(filter.getAndConnector(),Certificate.SERIALNUM,filter.getEqualOperator(),serialNum,false);
       removeByFilter(filter);
   }


    public void removePrivateKeyByCertificate(String cert) throws Throwable
    {
       DataFilterImpl filter = new DataFilterImpl();
       filter.addSingleFilter(null,Certificate.CERTIFICATE,filter.getEqualOperator(),cert,false);
       Collection result = getEntityByFilter(filter);
       if(result == null || result.isEmpty())
          throw new ApplicationException("Cannot Find the Record ");
       Certificate certificate =  (Certificate)result.iterator().next();
       certificate.setPrivateKey(null);
       update(certificate);
    }


    public void removePrivateKeyByPublicKey(String publicKey) throws Throwable
    {
        DataFilterImpl filter = new DataFilterImpl();
        filter.addSingleFilter(null,Certificate.PUBLICKEY,filter.getEqualOperator(),publicKey,false);
        Collection result = getEntityByFilter(filter);
        if(result == null || result.isEmpty())
            throw new ApplicationException("Cannot Find the Record ");
        Certificate certificate =  (Certificate)result.iterator().next();
        certificate.setPrivateKey(null);
        update(certificate);

    }

    public void revokeCertificateByUId(Long uid) throws Throwable
    {
        DataFilterImpl filter = new DataFilterImpl();
        filter.addSingleFilter(null,Certificate.UID,filter.getEqualOperator(),uid,false);
        Collection result = getEntityByFilter(filter);
        if(result == null || result.isEmpty())
            throw new ApplicationException("Cannot Find the Record ");
        Certificate certificate =  (Certificate)result.iterator().next();
        int revokeId = certificate.getRevokeId();
        revokeId+=1;
        certificate.setRevokeId(revokeId);
        update(certificate);
    }

  /**
   * This function returns the appropriate certificate for signing purpose.
   *
   * 1)If the expiry time of the current signature cert is at least 24(default, configurable
   * in rnif.properties) hours away,
   * return the current cert, or
   * 2)If the expiry time of the current signature cert is less than 24 hours away,
   * and there's no pending cert for the current cert or the pending cert is not yet
   * valid, return the current cert, or
   * 3)If the expiry time of the current signature cert is less than 24 hours away,
   * and there is a pending cert and it is valid, then return the pending cert, or
   * 4)If the current signature cert has already expired and the pending cert is valid,
   * replace the current cert with the pending cert in the security profile, and return
   * the new cert, or
   * 5)If the current signature cert has already expired and the pending cert is not yet
   * valid, or there's no pending cert at all, throw an exception
   */
  public X509Certificate getOwnSignCert(Certificate signCert, long threshold)
    throws CertificateException
  {
    CertificateLogger.debug("[CertificateEntityHandler.getOwnSignCert] enter");
    //X509Certificate x509SignCert = getX509Certificate(signCert, threshold);
    
    return getX509Certificate(signCert, threshold);
    /*
    if (x509SignCert == null) return null;
    
    Date endDate = x509SignCert.getEndDate();
    if (endDate == null) return x509SignCert;
    
    /*
     * TWX 25082006 the checking on threshold has been moved to getReplacementCert(...) 
     *              the following code is redundant
     *
    Date now = new Date();
    long diff = endDate.getTime() - now.getTime();
    if ( diff > threshold)
    {
      CertificateLogger.debug("[CertificateEntityHandler.getOwnSignCert] returning current cert="
        + signCert.getCertName());
      return x509SignCert; // return the current cert
    }
    Certificate pendingCert = getPendingCert(signCert);
    if ((pendingCert == null) || (!isValid(pendingCert)))
    {
      CertificateLogger.debug("[CertificateEntityHandler.getOwnSignCert] returning current cert="
        + signCert.getCertName());
      return x509SignCert;
    }

    CertificateLogger.debug("[CertificateEntityHandler.getOwnSignCert] returning pending cert="
      + pendingCert.getCertName());
    return getX509Certificate(pendingCert); */

  }

  /**
   * This function returns the appopriate partner encrypt cert.
   *
   * 1) if the current partner encrypt cert is valid and there's no pending cert or
   * the pending cert is not yet valid, return the current cert, or
   * 2) if the current cert is valid and it has a valid pending cert, return the pending cert,
   * or
   * 3) if the current cert has expired and it has a valid pending cert, replace it with
   * the pending cert, and return the pending cert, or
   * 4) if the current cert has expired and it has no valid pending cert, throw an exception.
   */
  public X509Certificate getPartnerEncryptCert(Certificate encryptCert)
    throws CertificateException
  {
    CertificateLogger.debug("[CertificateEntityHandler.getPartnerEncryptCert] enter ");
    if (encryptCert == null)
    {
      CertificateLogger.debug("[CertificateEntityHandler.getPartnerEncryptCert] return null ");
      return null;
    }

    X509Certificate x509EncryptCert = getX509Certificate(encryptCert);
    
    /*
     * TWX 25082006 the original code in getX509Certificate will do the getPendingCert if the encryptCert is invalid.
     *              If no pending cert found, getX509Certificate will throw exception. So the following code is a bit
     *              redundant
    Certificate pendingCert = getPendingCert(encryptCert);
    if ((pendingCert == null) || (!isValid(pendingCert)))
    {
      CertificateLogger.debug("[CertificateEntityHandler.getPartnerEncryptCert] return current cert="
        + encryptCert.getCertName());
      return x509EncryptCert;
    }

    CertificateLogger.debug("[CertificateEntityHandler.getPartnerEncryptCert] return pending cert="
      + pendingCert.getCertName());
    return getX509Certificate(pendingCert); */
    
    
    return x509EncryptCert;
  }

  public boolean isValid(Certificate cert)
  {
    X509Certificate x509Cert = GridCertUtilities.loadCertificateFromString(cert.getCertificate());
    return GridCertUtilities.isValid(x509Cert); 
  }
  
  public X509Certificate getX509Certificate(Certificate cert)
  	throws CertificateException
  {
  	return getX509Certificate(cert, -1);
  }
  
  //TWX 25082006 Modified the way we get the pending cert(replacement cert). Threshold checking also included.
  public X509Certificate getX509Certificate(Certificate cert, long threshold)
  throws CertificateException
  {
	  Certificate certificate = getCert(cert, threshold, false);
	  
	  if(certificate!=null)
		  return GridCertUtilities.loadCertificateFromString(certificate.getCertificate());
	  else
		  return null;
  }
  
  
  public Certificate getCert(Certificate cert, long threshold, boolean isForcedGetReplacementCert)
    throws CertificateException
  {
    if (cert == null) return null;

    X509Certificate x509Cert = GridCertUtilities.loadCertificateFromString(cert.getCertificate());

    Date now = new Date();

    if (!GridCertUtilities.isValid(x509Cert) || isForcedGetReplacementCert) // the current cert has already expired
    {
      //if there is a pending cert, replace it for the current sign cert
      //Certificate pendingCert = getPendingCert(cert);
    	Certificate pendingCert = null;
    	try
    	{
    		pendingCert = getReplacementCert(cert, threshold);
    	}
    	catch(Throwable th)
    	{
    		throw new CertificateException("Error in getting pending cer for certificate "+cert.getCertName(), th);
    	}
    	
      if (pendingCert == null)
        throw new CertificateException("current cert expired or is not yet valid and no pending cert can be found.");
      X509Certificate x509PendingCert = GridCertUtilities.loadCertificateFromString(
        pendingCert.getCertificate());
      if (!GridCertUtilities.isValid(x509PendingCert))
        throw new CertificateException("current cert expired or is not yet valid and pending cert is not valid, either.");
      
      //TWX 25082006 update the replacement cert field if the replacement cert we found is not
      //same as the previous value.
      if( ((Long)pendingCert.getKey()).longValue() != cert.getReplacementCertUid().longValue())
      {
      	cert.setReplacementCertUid((Long)pendingCert.getKey());
      	try
      	{
      		CertificateLogger.log("[CertificateEntityHandler.getCert] Set replacement cert. Replacement cert uid for certificate "+cert.getCertName()+" will be "+pendingCert.getKey());
      		update(cert);
      	}
      	catch(Throwable th)
      	{
      		throw new CertificateException("[CertificateEntityHandler.getCert] Error in updating the replacement cert UID "+pendingCert.getKey()+" for cert with UID "+cert.getKey(), th);
      	}
      }
      
      //replace the old sign cert
      //replaceCert(cert);
      //NSL20060508 just revoke the expired cert
      try
      {
    	  if (!GridCertUtilities.isValid(x509Cert))  
    		  revokeCertificateByUId(new Long(cert.getUId()));
      }
      catch (Throwable t)
      {
      	CertificateLogger.error("Error revoking expired cert", t);
      }
      return pendingCert;
    }

    return cert;
  }
  
  public Certificate getPendingCert(Certificate cert)
  {
    try
    {
      Long certUid = (Long)cert.getKey();
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Certificate.RELATED_CERT_UID, filter.getEqualOperator(),
        certUid, false);
      filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID, filter.getEqualOperator(), new Integer(0), false);
      Collection result = getEntityByFilter(filter);
      if(result == null || result.isEmpty())
        return null;
      return (Certificate)result.iterator().next();
    }
    catch(Throwable t)
    {
      CertificateLogger.warn("Error getting pending cert", t);
      return null;
    }
  }
  
  /**
   * TWX 25082006 Get the replacement cert for the given cert entity. If the replacement cert for the cert is not valid,
   *              we will continue searching for the replacement cert until a valid has found or null if we can't
   *              get any replacement cert.
   * @param cert
   * @param threshold Specify the total allowable expiring time. EG if the threshold is 23 * 60 * 60 * 1000 (1 day),then
   *                  the diff between certificate expiryDate and current date shall within the threshold
   * @return
   * @throws CertificateException
   */
  public Certificate getReplacementCert(Certificate cert, long threshold)
  	throws CertificateException
  {
  	Long replacementCertUid = cert.getReplacementCertUid();
  	if(replacementCertUid == null)
  	{
  		return null;
  	}
  	
  	Certificate replacementCert = null;
  	try
  	{
  		replacementCert = (Certificate)getEntityByKey(replacementCertUid);
  	}
  	catch(Throwable th)
  	{
  		throw new CertificateException("[CertificateEntityHandler.getReplacementCert] Replacement cert with UID ["+replacementCertUid+"] for certificate with UID "+cert.getKey()+" cannot be retrieved !!!", th);
  	}
  	
  	Date now = new Date();
  	X509Certificate x509ReplacementCert = GridCertUtilities.loadCertificateFromString(replacementCert.getCertificate());
  	
  	boolean isCertValid = ( GridCertUtilities.isValid(x509ReplacementCert) && replacementCert.getRevokeId() == 0); 
  	boolean isWithinThreshold = isWithinThreshold(x509ReplacementCert.getNotAfter(), now, threshold);
  	
  	if(isCertValid && isWithinThreshold )
  	{
  		return replacementCert;
  	}
  	else if(isCertValid && ! isWithinThreshold)
  	{
  		Certificate pendingCertForReplacementCert = getReplacementCert(replacementCert, threshold);
  		if(pendingCertForReplacementCert == null)
  		{
  			return replacementCert;
  		}
  		else
  		{
  			return pendingCertForReplacementCert;
  		}
  	}
  	else
  	{
  		return getReplacementCert(replacementCert, threshold);
  	}
  }
  
  /**
   * To check whether the Cert has expired. (Note this is not a standard X509 certificate expired checking)
   * @param cert
   * @param currentDate 
   * @return true if the expiry date of the cert is earlier than the currentDate, false otherwise.
   */
  private boolean isCertExpired(Certificate cert, Date currentDate)
	{
		Date expiryDate = new Date(TimeUtil.utcToLocal(cert.getEndDate().getTime()));
		return currentDate.getTime() > expiryDate.getTime(); 
	}
  
  /**
   * to check the time different between endDate and currentDate is within the threshold
   * @param endDate
   * @param currentDate
   * @param threshold
   * @return
   */
  private boolean isWithinThreshold(Date endDate, Date currentDate,long threshold)
  {
  	long diff = TimeUtil.utcToLocal(endDate.getTime()) - currentDate.getTime();
  	return diff > threshold;
  }
  
  /**
   * This function replaces the input cert with its pending cert.
   *
   * @return true -- if replacement is successful,
   *         false otherwise.
   */
  public boolean replaceCert(Certificate cert)
  {
    try
    {
      CertificateLogger.debug("[CertificateEntityHandler.replaceCert] enter, cert="
        + cert.getCertName());
      Certificate pendingCert = getPendingCert(cert);
      if (pendingCert == null)
      {
        CertificateLogger.debug("[CertificateEntityHandler.replaceCert] pending cert is null,"
          + " return false");
        return false;
      }

      //Copy all data except for the relatedCertUid from pendingCert to the current cert
      cert.setID(pendingCert.getID());
      cert.setCertName(pendingCert.getCertName());
      cert.setSerialNumber(pendingCert.getSerialNumber());
      cert.setIssuerName(pendingCert.getIssuerName());
      cert.setCertificate(pendingCert.getCertificate());
      cert.setPublicKey(pendingCert.getPublicKey());
      cert.setPrivateKey(pendingCert.getPrivateKey());
      cert.setRevokeId(pendingCert.getRevokeId());
      cert.setMaster(pendingCert.isMaster());
      cert.setPartner(pendingCert.isPartner());
      cert.setCategory(pendingCert.getCategory());
      cert.setInKeyStore(pendingCert.isInKeyStore());
      cert.setInTrustStore(pendingCert.isInTrustStore());
      cert.setRelatedCertUid(null);

      removeCertificate((Long)pendingCert.getKey());

      update(cert);

      CertificateLogger.debug("[CertificateEntityHandler.replaceCert] cert replaced with pending cert "
        + cert.getCertName());
      return true;
    }
    catch(Throwable t)
    {
      CertificateLogger.warn("Error in replacing cert", t);
      return false;
    }
  }
  /*
    protected Object getHome() throws Exception
    {
       return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).
        getHome(ICertificateLocalHome.class.getName(),ICertificateLocalHome.class);
    }*/
  
  //TWX 07082006
  private boolean isPasswordSet()
  {
  	SecurityDB secDB = null;
  	SecurityDBManager secDBManager = SecurityDBManager.getInstance();
  	try
  	{
  		secDB = secDBManager.getSecurityDB();
  		return secDB.isPasswordset();
  	}
  	finally
  	{
  		if(secDB != null)
  		{
  			secDBManager.releaseSecurityDB(secDB);
  		}
  	}
  }
  
  //TWX 07082006
  private String getPrivatePassword()
  {

  		return SecurityServices.getPrivatePassword();
  }
  
  //TWX 07082006
  private void setSecDBPrivatePassword(String privatePassword)
  {
  	SecurityServices.setPrivatepassword(privatePassword);
  }
  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return ICertificateLocalHome.class;
	}

		protected Class getProxyInterfaceClass() throws Exception
    {
       return ICertificateLocalObj.class;
    }
	/*	
	public static void main(String[] args) throws Throwable
	{
		CertificateEntityHandler handler = CertificateEntityHandler.getInstance();
		Certificate c = (Certificate)handler.findCertificateByName("EXP2");
		Certificate replacement = handler.getReplacementCert(c);
		System.out.println("Replacment cert is "+replacement.getCertName());
	}*/
    
    protected IEntityDAO getDAO()
    {
      return CertificateDAOHelper.getInstance();
    }
}