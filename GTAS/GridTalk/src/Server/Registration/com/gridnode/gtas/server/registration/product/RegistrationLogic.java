/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationLogic.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2002    Neo Sok Lay         Created
 * Jan 03 2003    Neo Sok Lay         Send notification when My GridNode is
 *                                    created.
 *                                    When creating cert request, replace if
 *                                    already exists, otherwise the file will
 *                                    be renamed.
 * Apr 03 2003    Koh Han Sing        Added nodeLock feature
 * Apr 24 2003    Qingsong            change insertCertificateAndPrivateKey
 *                                    call changeprivatepassword instead of SecurityDB.setPrivatepassword(password).
 * May 08 2003    Neo Sok Lay         Raise alert through AlertUtil instead of
 *                                    direct call to AlertManager.
 * Jun 04 2003    Koh Han Sing        Set _regInfo.licenseState in checkExpiry
 * Jul 29 2003    Neo Sok Lay         GNDB00014753: Synchronization is screwed up when
 *                                    2 getInstance() calls overlaps.
 * Dec 04 2003    Neo Sok Lay         GNDB00016392: _regInfo's LicenseState is not set to 
 *                                    Valid when a license expiring alert is being sent.
 * Feb 18 2004    Neo Sok Lay         Delegate CheckLicense scheduling to ScheduleHelper.
 * Oct 14 2005    Neo Sok Lay         RSA cert is not serializable, change to pass cert bytes.
 * Mar 28 2006    Neo Sok Lay         Broadcast license state on change
 * Jun 22 2006    Tam Wei Xiang       Fixed GNDB00026893 - modified method checkExpiry():
 *                                    Dun alter the state(both the registration n license)
 *                                    of _regInfo.
 * Aug 04 2006    Tam Wei Xiang       Amend the way we access SecurityDB. Modified method
 *                                    verifyAndSetSecurityPassword(...) 
 * June 29 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib                                  
 */
package com.gridnode.gtas.server.registration.product;

import com.gridnode.gtas.server.gridnode.model.GnCategory;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.notify.EnterpriseCreatedNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.registration.exceptions.InconsistentRegistrationStateException;
import com.gridnode.gtas.server.registration.exceptions.InvalidProductKeyException;
import com.gridnode.gtas.server.registration.exceptions.InvalidSecurityPasswordException;
import com.gridnode.gtas.server.registration.exceptions.LicenseFileExpiredException;
import com.gridnode.gtas.server.registration.exceptions.ProductRegistrationException;
import com.gridnode.gtas.server.registration.helpers.*;
import com.gridnode.gtas.server.registration.model.GridTalkLicense;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.nodelock.LicenseFileGenerator;
import com.gridnode.gtas.server.registration.nodelock.NodeLockUtil;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.helpers.SecurityDB;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBManager;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.certificate.rsa.RDNAttributes;
import com.gridnode.pdip.base.certificate.rsa.RSACertRequest;
import com.gridnode.pdip.base.certificate.rsa.RSAKeyPair;
import com.gridnode.pdip.base.certificate.rsa.RSASelfSignedCert;
import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * This class handles the registration process. The current state of the
 * registration must be tracked and this should be the sole place to track the
 * registration progress.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since 2.0 I5
 */
public final class RegistrationLogic
{
  // Instantiate once and for all
  private static final RegistrationLogic _self = new RegistrationLogic();
  //Don't synchronize by 3rd object, synchronize on _self instead.
  //private static final Object _lock = new Object();

  public static final Integer GM_NODEID_MIN       = new Integer(900);
  public static final Integer GM_NODEID_MAX       = new Integer(999);
  public static final Integer GM_NODEID           = GM_NODEID_MIN;
  public static final String GT_CERT_NAME     = "GridTalk";
  public static final String GM_CERT_NAME     = "GridMaster";
  public static final String CERT_FILE_EXT    = ".cer";
  public static final String REQ_FILE_EXT     = ".req";

  public final String PRODUCT_NAME     = "GridTalk (Enterprise Edition - GTAS)";
  public final String PRODUCT_VERSION  = "2.0";


  private ProductKey        _prodKey;
  private RegistrationInfo  _regInfo;

  private RegistrationLogic()
  {
    try
    {
      initRegistrationState();
    }
    catch (Throwable t)
    {
      throw new RuntimeException("Error initializing Registration module!", t);
    }
  }

  /**
   * Get an instance of this RegistrationLogic class.
   */
  public static RegistrationLogic getInstance()
    throws Throwable
  {
    /*030729NSL: Instantiate as "final"
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
        {
          _self = new RegistrationLogic();
          //_self.resetRegistrationState();
          _self.initRegistrationState();
        }
      }
    }
    */
    return _self;
  }

  /**
   * Initialise the current state of registration. Product registration might
   * have completed already, this method will set the State & RegistrationInfo correctly.
   */
  private synchronized void initRegistrationState()
    throws Throwable
  {
    //synchronized (_lock)
    //{
      _regInfo = new RegistrationInfo();

      // Get the company profile, license, and gridnode
      CompanyProfile coyProfile = ServiceLookupHelper.getGridNodeManager().getMyCompanyProfile();
      License license = getLicense();
      GridNode gridnode = getGridNode();
      /**@todo need to check gridnode and gridmaster certificate also*/

      if (license != null && gridnode != null) // already registered
      {
        GridTalkLicense gtlicense = getGtLicense(license.getUId());
        if (gtlicense != null)
        {
          setRegistration(
            new Integer(gridnode.getID()),
            gridnode.getName(),
            license.getProductKey().substring(0, 5),
            license.getProductKey().substring(5, 11),
            license.getProductKey().substring(11, 16),
            license.getProductKey().substring(16, 22),
            coyProfile,
            gtlicense.getOsName(),
            gtlicense.getOsVersion(),
            gtlicense.getMachineName());

          _regInfo.setRegistrationState(RegistrationInfo.STATE_REGISTERED);

          try
          {
            checkExpiry(gtlicense);
          }
          catch (Exception ex)
          {
            Logger.warn("Error", ex);
          }

        }
        else
        {
          license.setState(License.STATE_REVOKED);
          ServiceLookupHelper.getLicenseManager().updateLicense(license);
          setRegistration(
            new Integer(gridnode.getID()),
            gridnode.getName(),
            license.getProductKey().substring(0, 5),
            license.getProductKey().substring(5, 11),
            license.getProductKey().substring(11, 16),
            license.getProductKey().substring(16, 22),
            coyProfile,
            "",
            "",
            "");
          _regInfo.setRegistrationState(RegistrationInfo.STATE_EXPIRED);
          _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_REVOKED);
        }
      }
      else
      {
        _regInfo.setCompanyProfile(coyProfile);
        undoRegistration();
      }
    //}
  }

  private RegistrationInfo setRegistration(Integer gridnodeID, String gridnodeName,
    String prodKeyF1, String prodKeyF2, String prodKeyF3, String prodKeyF4,
    CompanyProfile coyProfile, String osName, String osVersion, String machineName)
    throws Throwable
  {
    //synchronized (_lock)
    //{
      //make sure the company profile is updated
      _regInfo.setCompanyProfile(coyProfile);

      // validate the product key
      _prodKey = checkProductKey(prodKeyF1+prodKeyF2+prodKeyF3+prodKeyF4, gridnodeID);

      // cache the registration info
      _regInfo.setGridnodeID(gridnodeID);
      _regInfo.setGridnodeName(gridnodeName);
      _regInfo.setProductKeyF1(prodKeyF1);
      _regInfo.setProductKeyF2(prodKeyF2);
      _regInfo.setProductKeyF3(prodKeyF3);
      _regInfo.setProductKeyF4(prodKeyF4);
      _regInfo.setBizConnections(new Integer(_prodKey.getNumConnections()));
      _regInfo.setCategory(_prodKey.getCategory());
      _regInfo.setLicenseEndDate(_prodKey.getEndDate());
      _regInfo.setLicenseStartDate(_prodKey.getStartDate());

      // Koh Han Sing
      _regInfo.setOsName(osName);
      _regInfo.setOsVersion(osVersion);
      _regInfo.setMachineName(machineName);
    //}

    return getRegistrationInfo();
  }


  /**
   * Initialise the RegistrationInfo.
   *
   * @param gridnodeID GridNode ID being registered
   * @param gridnodeName GridNode Name being registered
   * @param prodKeyF1 Product Key F1
   * @param prodKeyF2 Product Key F2
   * @param prodKeyF3 Product Key F3
   * @param prodKeyF4 Product Key F4
   * @param coyProfile Company Profile of GridNode being registered.
   */
  public synchronized RegistrationInfo initRegistration(Integer gridnodeID, String gridnodeName,
    String prodKeyF1, String prodKeyF2, String prodKeyF3, String prodKeyF4,
    CompanyProfile coyProfile, String osName, String osVersion, String machineName)
    throws Throwable
  {
    //synchronized (_lock)
    //{
      //check the registration state
      checkRegistrationState(RegistrationInfo.STATE_REG_IN_PROGRESS);

      //make sure the company profile is updated
      _regInfo.setCompanyProfile(coyProfile);

      // validate the product key
      _prodKey = checkProductKey(prodKeyF1+prodKeyF2+prodKeyF3+prodKeyF4, gridnodeID);

      // Koh Han Sing 20030403
      checkExpiry();

      // cache the registration info
      _regInfo.setGridnodeID(gridnodeID);
      _regInfo.setGridnodeName(gridnodeName);
      _regInfo.setProductKeyF1(prodKeyF1);
      _regInfo.setProductKeyF2(prodKeyF2);
      _regInfo.setProductKeyF3(prodKeyF3);
      _regInfo.setProductKeyF4(prodKeyF4);
      _regInfo.setBizConnections(new Integer(_prodKey.getNumConnections()));
      _regInfo.setCategory(_prodKey.getCategory());
      _regInfo.setLicenseEndDate(_prodKey.getEndDate());
      _regInfo.setLicenseStartDate(_prodKey.getStartDate());

      // Koh Han Sing
      _regInfo.setOsName(osName);
      _regInfo.setOsVersion(osVersion);
      _regInfo.setMachineName(machineName);

      // transit to initiated state.
      _regInfo.setRegistrationState(RegistrationInfo.STATE_REG_IN_PROGRESS);
    //}

    return getRegistrationInfo();
  }

  /**
   * Confirm the RegistrationInfo initialized earlier. This will generate
   * the Public and Private key pairs and create license and gridnode records.
   *
   * @param securityPassword Security password for the Private key.
   */
  public synchronized void confirmRegistration(String securityPassword)
    throws Throwable
  {
    //synchronized (_lock)
    //{
      // check the registration state
      checkRegistrationState(RegistrationInfo.STATE_REGISTERED);

      // create RSAKeyPair
      RSAKeyPair keyPair = new RSAKeyPair();
      if (!keyPair.isKeyPairGenerated())
        throw new Exception("Unable to generate Public and Private key pair!");

      // create RDNAttributes
      RDNAttributes rdnAttr = new RDNAttributes(
                                getCountryName(),
                                getOrganizationName(),
                                getOrganizationUnitName(),
                                getCommonName());

      // create RSACertRequest
      RSACertRequest certReq = new RSACertRequest(rdnAttr, keyPair);

      // create RSASelfSignedCert
      RSASelfSignedCert cert = new RSASelfSignedCert(
                                 rdnAttr, keyPair, certReq,
                                 _regInfo.getLicenseStartDate(),
                                 _regInfo.getLicenseEndDate());

      // insert certifcate  & insert private key
      insertCertificateAndPrivateKey(cert, keyPair, securityPassword);
      // save certificate request
      saveCertificateRequest(certReq);

      // create license record
      createLicense();

      // create GridNode record
      createGridNode();

      // insert gm certificate
      insertGmCert();

      // transit to confirmed state
      _regInfo.setRegistrationState(RegistrationInfo.STATE_REGISTERED);

      // start scheduler to check license*/
      createScheduler();
    //}
  }


  
  /**
   * Cancel the registration process. This will only succeed if the
   * registration has not been completed.
   */
  public synchronized void cancelRegistration()
    throws Throwable
  {
    //synchronized (_lock)
    //{
      //check the registration state
      checkRegistrationState(RegistrationInfo.STATE_NOT_REGISTERED);

      resetRegistrationState();
    //}
  }

  /**
   * Get the current RegistrationInfo. This method returns a copy of the
   * RegistrationInfo. Any changes to this returned copy will not affect the
   * RegistrationInfo in this class.
   */
  public synchronized RegistrationInfo getRegistrationInfo()
  {
    return (RegistrationInfo)_regInfo.clone();
  }

  /**
   * Undo the Registration. Any created License, GridNode, or Certificate records
   * will be deleted.
   */
  public synchronized void undoRegistration() throws Throwable
  {
    //synchronized (_lock)
    //{
      License license = getValidLicense();
      GridNode gridnode = getGridNode();
      Integer gnId = null;
      if (license != null)
      {
        ServiceLookupHelper.getLicenseManager().deleteLicense(
          (Long)license.getKey());
      }

      if (gridnode != null)
      {
        ServiceLookupHelper.getGridNodeManager().deleteGridNode(
          (Long)gridnode.getKey());
        gnId = new Integer(gridnode.getID());
      }

      if (gnId == null)
        gnId = _regInfo.getGridnodeID();
      deleteCertificate(gnId, GT_CERT_NAME);
      deleteCertificate(GM_NODEID, GM_CERT_NAME);
      deleteCertificateRequest();

      resetRegistrationState();
    //}
  }

  /**
   * Rollback the registration process.
   */
  public synchronized void rollback()
    throws Throwable
  {
    //synchronized (_lock)
    //{
      if (_regInfo.getRegistrationState() == RegistrationInfo.STATE_REG_IN_PROGRESS)
      {
        undoRegistration();
      }
      else if (_regInfo.getRegistrationState() == RegistrationInfo.STATE_NOT_REGISTERED)
      {
        resetRegistrationState();
      }
    //}
  }

  /**
   * Obtain the registered product key.
   *
   * @throws InconsistentRegistrationStateException Registration has not been
   * completed.
   */
  public synchronized String getRegisteredProductKey()
    throws Throwable
  {
    assertRegistrationCompleted();

    return (_regInfo.getProductKeyF1() + _regInfo.getProductKeyF2() +
            _regInfo.getProductKeyF3() + _regInfo.getProductKeyF4());
  }

  /**
   * Obtain the certificate request generated during product registration.
   *
   * @throws InconsistentRegistrationStateException Registration has not been
   * completed.
   */
  public synchronized File getCertificateRequest()
    throws Throwable
  {
    assertRegistrationCompleted();

    String filename = GT_CERT_NAME+_regInfo.getGridnodeID()+REQ_FILE_EXT;

    return FileUtil.getFile(IRegistrationPathConfig.PATH_CERT, filename);
  }

  /**
   * Obtain the UID of the GridMaster certificate created during product
   * registration.
   *
   * @throws InconsistentRegistrationStateException Registration has not been
   * completed.
   */
  public synchronized Long getGridMasterCert()
    throws Throwable
  {
    assertRegistrationCompleted();

    Long certKey =
      (Long)ServiceLookupHelper.getCertificateManager().findCertificateByIDAndName(
        GM_NODEID.intValue(), GM_CERT_NAME).getKey();

    return certKey;
  }

  /**
   * Verify the security password. If the verification passes, set this
   * to unlock the private key.
   *
   * @param The password to be verified and set.
   * @throws InconsistentRegistrationStateException Registration has not been
   * completed.
   */
  public synchronized void verifyAndSetSecurityPassword(String password)
    throws Throwable
  {
  	SecurityDB secDB = null;
  	try
  	{
  		assertRegistrationCompleted();

  		ICertificateManagerObj certMgr = ServiceLookupHelper.getCertificateManager();
  		Certificate cert = certMgr.findCertificateByIDAndName(
  		                _regInfo.getGridnodeID().intValue(), GT_CERT_NAME);

  		byte[] key = GridCertUtilities.decode(cert.getPrivateKey());
  		boolean correct = GridCertUtilities.loadPKCS8PrivateKeyData(
                        password.toCharArray(), key) != null;
  		if (!correct)
  		{
        throw new InvalidSecurityPasswordException();
  		}
  	}
  	catch(Throwable th)
  	{
  		throw th;
  	}
  }

  /**
   * Obtain the UID of the Master certificate for the registered GridTalk.
   *
   * @throws InconsistentRegistrationStateException Registration has not been
   * completed.
   */
  public synchronized Long getMasterCert()
    throws Throwable
  {
    assertRegistrationCompleted();

    Long certKey =
      (Long)ServiceLookupHelper.getCertificateManager().findCertificateByIDAndName(
        _regInfo.getGridnodeID().intValue(), GT_CERT_NAME).getKey();

    return certKey;
  }

  public synchronized RegistrationInfo upgradeLicense(String prodKeyF1, String prodKeyF2,
    String prodKeyF3, String prodKeyF4, String osName, String osVersion,
    String machineName)
    throws Throwable
  {
    //synchronized (_lock)
    //{
      // validate the product key
      _prodKey = checkProductKey(prodKeyF1+prodKeyF2+prodKeyF3+prodKeyF4,
                 _regInfo.getGridnodeID());

      // Koh Han Sing 20030403
      checkExpiry();

      // cache the registration info
      _regInfo.setProductKeyF1(prodKeyF1);
      _regInfo.setProductKeyF2(prodKeyF2);
      _regInfo.setProductKeyF3(prodKeyF3);
      _regInfo.setProductKeyF4(prodKeyF4);
      _regInfo.setBizConnections(new Integer(_prodKey.getNumConnections()));
      _regInfo.setCategory(_prodKey.getCategory());
      _regInfo.setLicenseEndDate(_prodKey.getEndDate());
      _regInfo.setLicenseStartDate(_prodKey.getStartDate());

      // Koh Han Sing
      _regInfo.setOsName(osName);
      _regInfo.setOsVersion(osVersion);
      _regInfo.setMachineName(machineName);

      // revoke existing valid license if any
      disableOldLicense();

      // create license record
      createLicense();

      // start scheduler to check license*/
      createScheduler();
    //}

    return getRegistrationInfo();
  }

  /**
   * Create the GridNode record.
   */
  private void createGridNode() throws Throwable
  {
    try
    {
      GridNode gridnode = new GridNode();
      gridnode.setCategory(_regInfo.getCategory());
      gridnode.setCoyProfileUID((Long)_regInfo.getCompanyProfile().getKey());
      gridnode.setID(_regInfo.getGridnodeID().toString());
      gridnode.setName(_regInfo.getGridnodeName());
      gridnode.setState(GridNode.STATE_ME);

      ServiceLookupHelper.getGridNodeManager().createGridNode(gridnode);

      notifyEnterpriseCreated(gridnode);
    }
    catch (Throwable t)
    {
      Logger.warn("[RegistrationLogic.createGridNode] Error creating GridNode ", t);
      throw t;
    }
  }

  /**
   * Get the registered GridNode record.
   *
   * @return The registered GridNode, if any. Otherwise <b>null</b>.
   */
  private GridNode getGridNode()
  {
    GridNode gridnode = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE,
        filter.getEqualOperator(), new Short(GridNode.STATE_ME), false);
      Object[] results = ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(
                           filter).toArray();

      if (results.length > 0)
        gridnode = (GridNode)results[0];
    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.getGridNode] "+t.getMessage());
    }

    return gridnode;
  }

  private void deleteCertificate(Integer nodeID, String certName)
  {
    try
    {
      //SecurityDBHandler.getInstance().deleteCertificate(
      ServiceLookupHelper.getCertificateManager().deleteCertificate(
        nodeID.intValue(), certName);
    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.deleteCertificate] "+t.getMessage());
    }
  }

/**@todo to check if certificate exist
  private boolean isCertificateExist(Integer nodeID, String certName)
  {
    boolean exist = false;
    try
    {
      ServiceLookupHelper.getCertificateManager().findCertificateByIDAndName(
        nodeID.intValue(), certName);

      exist = true;
    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.isCertificateExist] "+t.getMessage());
    }
  }
*/
  /**
   * Check the current registration state and validate whether the state is
   * consistent for transition to another state.
   *
   * @param transitionState The state to transit to.
   * @throw InconsistentRegistrationStateException The current state is not
   * consistent for transition to the <code>transitionState</code>.
   */
  private void checkRegistrationState(int transitionState)
    throws InconsistentRegistrationStateException
  {
    String completeMsg = "Registration has already been completed!";

    // once confirmRegistration() can only be called once.
    if (_regInfo.getRegistrationState() == RegistrationInfo.STATE_REGISTERED)
      throw new InconsistentRegistrationStateException(completeMsg);

    if (transitionState == RegistrationInfo.STATE_REGISTERED)
    {
      // initRegistration() must be called before confirmRegistration()
      if (_regInfo.getRegistrationState() != RegistrationInfo.STATE_REG_IN_PROGRESS)
         throw new InconsistentRegistrationStateException(
           "Registration Info must been initialized before confirmation!");
    }
  }

  private void disableOldLicense() throws Throwable
  {
    try
    {
      GridTalkLicense gtLicense = getGridTalkLicense();
      License license = ServiceLookupHelper.getLicenseManager().findLicense(
                          gtLicense.getLicenseUid());
      if (license.getState() == License.STATE_VALID)
      {
        ServiceLookupHelper.getLicenseManager().revokeLicense(gtLicense.getLicenseUid());
      }
    }
    catch (ProductRegistrationException ex)
    {
      Logger.warn("[RegistrationLogic.disableOldLicense] Error", ex);
    }
    catch (Throwable t)
    {
      Logger.warn("[RegistrationLogic.disableOldLicense] Error", t);
      throw t;
    }
  }

  /**
   * Create the License record.
   */
  private void createLicense() throws Throwable
  {
    try
    {
      License license = new License();
      license.setEndDate(_regInfo.getLicenseEndDate());
      license.setProductKey(
        _regInfo.getProductKeyF1() +
        _regInfo.getProductKeyF2() +
        _regInfo.getProductKeyF3() +
        _regInfo.getProductKeyF4());
      license.setProductName(PRODUCT_NAME);
      license.setProductVersion(PRODUCT_VERSION);
      license.setStartDate(_regInfo.getLicenseStartDate());

      //Koh Han Sing 20030403
      String startDate = Long.toString(_regInfo.getLicenseStartDate().getTime());
      startDate = NodeLockUtil.waxOn(startDate);
      String endDate = Long.toString(_regInfo.getLicenseEndDate().getTime());
      endDate = NodeLockUtil.waxOn(endDate);

      Long licenseUid = ServiceLookupHelper.getLicenseManager().createLicense(license);
      license = ServiceLookupHelper.getLicenseManager().findLicense(licenseUid);
      _regInfo.setLicenseState(license.getState());

      GridTalkLicense gtLicense = new GridTalkLicense();
      gtLicense.setLicenseUid(licenseUid);
      gtLicense.setOsName(_regInfo.getOsName());
      gtLicense.setOsVersion(_regInfo.getOsVersion());
      gtLicense.setMachineName(_regInfo.getMachineName());
      gtLicense.setStartDate(startDate);
      gtLicense.setEndDate(endDate);

      ServiceLookupHelper.getRegistrationServiceBean().createGridTalkLicense(gtLicense);
      _regInfo.setRegistrationState(RegistrationInfo.STATE_REGISTERED);
      
      //NSL20060328 broadcase change of license state
      AlertUtil.notifyLicenseState(getRegistrationInfo());
    }
    catch (Throwable t)
    {
      Logger.warn("[RegistrationLogic.createLicense] Error creating License ", t);
      throw t;
    }
  }

  /**
   * Get the registered License record.
   *
   * @return The registered License, if any. Otherwise <b>null</b>.
   */
  private License getValidLicense()
  {
    License license = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, License.PRODUCT_NAME, filter.getEqualOperator(),
        PRODUCT_NAME, false);
      filter.addSingleFilter(filter.getAndConnector(), License.PRODUCT_VERSION,
        filter.getEqualOperator(), PRODUCT_VERSION, false);
      filter.addSingleFilter(filter.getAndConnector(), License.STATE,
        filter.getEqualOperator(), new Short(License.STATE_VALID), false);
      filter.setOrderFields(new Object[] {License.START_DATE});
      Collection licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);

      if (licenses.isEmpty())
      {
        filter = new DataFilterImpl();
        filter.addSingleFilter(null, License.PRODUCT_NAME, filter.getEqualOperator(),
          PRODUCT_NAME, false);
        filter.addSingleFilter(filter.getAndConnector(), License.PRODUCT_VERSION,
          filter.getEqualOperator(), PRODUCT_VERSION, false);
        filter.addSingleFilter(filter.getAndConnector(), License.STATE,
          filter.getEqualOperator(), new Short(License.STATE_NOT_COMMENCED), false);
        filter.setOrderFields(new Object[] {License.START_DATE});
        licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);
        if (licenses.isEmpty())
        {
          throw new ApplicationException("No valid License found for product "+
            PRODUCT_NAME + ", Version "+PRODUCT_VERSION);
        }

        license = (License)licenses.toArray()[0];
        Date currentDate = new Date();
        if (!currentDate.before(license.getStartDate()) &&
            !currentDate.after(license.getEndDate()))
        {
          license.setState(License.STATE_VALID);
          ServiceLookupHelper.getLicenseManager().updateLicense(license);
        }
      }
      else
      {
        license = (License)licenses.toArray()[0];
      }

    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.getLicense] "+t.getMessage());
    }

    return license;
  }

  /**
   * Get the registered License record. Even if it is expired.
   *
   * @return The registered License, if any. Otherwise <b>null</b>.
   */
  private License getLicense()
  {
    // The codes here sucks, must refactor
    License license = null;
    try
    {
      DataFilterImpl filter = getSearchFilter(License.STATE_VALID);
      Collection licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);

      if (licenses.isEmpty())
      {
        filter = getSearchFilter(License.STATE_NOT_COMMENCED);
        licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);
        if (licenses.isEmpty())
        {
          filter = getSearchFilter(License.STATE_EXPIRED);
          licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);
          if (licenses.isEmpty())
          {
            filter = getSearchFilter(License.STATE_REVOKED);
            licenses = ServiceLookupHelper.getLicenseManager().findLicenses(filter);
            if (licenses.isEmpty())
            {
              throw new ApplicationException("No valid License found for product "+
                    PRODUCT_NAME + ", Version "+PRODUCT_VERSION);
            }
            else
            {
              license = (License)licenses.toArray()[0];
            }
          }
          else
          {
            license = (License)licenses.toArray()[0];
          }
        }
        else
        {
          license = (License)licenses.toArray()[0];
          Date currentDate = new Date();
          if (!currentDate.before(license.getStartDate()) &&
              !currentDate.after(license.getEndDate()))
          {
            license.setState(License.STATE_VALID);
            ServiceLookupHelper.getLicenseManager().updateLicense(license);
          }
        }
      }
      else
      {
        license = (License)licenses.toArray()[0];
      }

    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.getLicense] "+t.getMessage());
    }

    return license;
  }


  private DataFilterImpl getSearchFilter(short state)
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, License.PRODUCT_NAME, filter.getEqualOperator(),
      PRODUCT_NAME, false);
    filter.addSingleFilter(filter.getAndConnector(), License.PRODUCT_VERSION,
      filter.getEqualOperator(), PRODUCT_VERSION, false);
    filter.addSingleFilter(filter.getAndConnector(), License.STATE,
      filter.getEqualOperator(), new Short(state), false);
    filter.setOrderFields(new Object[] {License.START_DATE});
    return filter;
  }

  /**
   * Get the registered License record.
   *
   * @return The registered License, if any. Otherwise <b>null</b>.
   */
  private GridTalkLicense getGtLicense(long licenseUid)
  {
    GridTalkLicense license = null;
    try
    {
      license = GridTalkLicenseEntityHandler.getInstance().findByLicenseUid(new Long(licenseUid));
    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.getGtLicense] "+t.getMessage());
    }

    return license;
  }

  /**
   * Insert this GridTalk's certificate with private key.
   *
   * @param certificate The generated self-signed certificate.
   * @param keyPair The generated Public and Private key pair.
   * @param password The password for the Private key.
   */
  private void insertCertificateAndPrivateKey(
    RSASelfSignedCert certificate, RSAKeyPair keyPair, String password)
    throws Throwable
  {
    /*
    SecurityDBHandler.getInstance().setPassword(password);
    SecurityDBHandler.getInstance().insertCertificate(
      _regInfo.getGridnodeID(), GT_CERT_NAME, certificate.getCertificate());
    SecurityDBHandler.getInstance().insertPrivateKeyByCert(
      keyPair.getPrivateKey(), certificate.getCertificate());
    */
    //SecurityDB.setPrivatepassword(password); 
    ICertificateManagerObj certMgr = ServiceLookupHelper.getCertificateManager();
    certMgr.changePrivatePassword("GridNode" , password);
    /*051014NSL RSA cert is not serializable....
    certMgr.insertCertificate(
      _regInfo.getGridnodeID(), GT_CERT_NAME, certificate.getCertificate());
    certMgr.insertPrivateKeyByCertificate(
      certificate.getCertificate(), keyPair.getPrivateKey());
      */
    byte[] certBytes = GridCertUtilities.writeX509Certificate(certificate.getCertificate());
    certMgr.insertCertificate(_regInfo.getGridnodeID(), GT_CERT_NAME, certBytes);
    certMgr.insertPrivateKeyByCertificate(certBytes, keyPair.getPrivateKey());

  }

  /**
   * Save the Certificate Request file.
   *
   * @param certReq The generated certificate request.
   */
  private void saveCertificateRequest(RSACertRequest certReq)
    throws Throwable
  {
    //String filename = FileUtil.getFile(PATH_CERT,
    //                    GT_CERT_NAME+_regInfo.getGridnodeID()+REQ_FILE_EXT).getAbsolutePath();

    String filename = GT_CERT_NAME+_regInfo.getGridnodeID()+REQ_FILE_EXT;
    GridCertUtilities.writePKCS10CertRequest(
      filename, certReq.getCertRequest());

    FileUtil.replace(IRegistrationPathConfig.PATH_CERT, filename, new FileInputStream(filename));
  }

  /**
   * Delete the generated Certificate Request file, if any.
   */
  private void deleteCertificateRequest()
  {
    String reqFilename = GT_CERT_NAME+_regInfo.getGridnodeID()+REQ_FILE_EXT;

    try
    {
      if (FileUtil.exist(IRegistrationPathConfig.PATH_CERT, reqFilename))
      {
        FileUtil.delete(IRegistrationPathConfig.PATH_CERT, reqFilename);
      }
    }
    catch (Throwable t)
    {
      Logger.log("[RegistrationLogic.deleteCertificateRequest] Unable to delete Cert Request: "+
        t.getMessage());
    }

  }

  /**
   * Insert the GridMaster Certificate appropriate for use by this GridTalk.
   * The GridMaster Certificate selected for insertion is based on the
   * NodeUsage of the GridNode Category.
   */
  private void insertGmCert()
    throws Throwable
  {
    String certFile = FileUtil.getFile(IRegistrationPathConfig.PATH_CERT,
                        GM_CERT_NAME + getNodeUsage() + CERT_FILE_EXT).getAbsolutePath();

    ServiceLookupHelper.getCertificateManager().insertCertificate(GM_NODEID, GM_CERT_NAME, certFile);
    //SecurityDBHandler.getInstance().insertCertificate(GM_NODEID, GM_CERT_NAME, certFile);
  }

  /**
   * Get the NodeUsage for a GridNode base on its Category.
   *
   * @return The NodeUsage of the registering GridNode.
   */
  private String getNodeUsage()
  {
    String nodeUsage = "";
    try
    {
      GnCategory category = ServiceLookupHelper.getGridNodeManager().findGnCategoryByCode(
                              _regInfo.getCategory());

      nodeUsage = category.getNodeUsage();
    }
    catch (Throwable ex)
    {
      Logger.log("[RegistrationLogic.getNodeUsage] Unable to find Category code: "+
        ex.getMessage());
    }

    return nodeUsage;
  }

  /**
   * Get a 2-char country code base on the country code in the CompanyProfile
   * for use in Certificate.
   *
   * @return A 2-char country code.
   */
  private String getCountryName()
  {
    String alpha3Code = _regInfo.getCompanyProfile().getCountry();
    String returnCode = null;

    try
    {
      CountryCode country =
        ServiceLookupHelper.getLocaleManager().findCountryCodeByAlpha3Code(
          alpha3Code);

      if (country != null)
        returnCode = country.getAlpha2Code();

    }
    catch (Throwable ex)
    {
      Logger.log("[RegistrationLogic.getCountryName] Unable to find country code: "+
        ex.getMessage());
    }
    finally
    {
      // can't find code, just return the first 2 char
      if (returnCode == null)
        returnCode = alpha3Code.substring(0, 2);
    }

    return returnCode;
  }

  /**
   * Get the organization name as the name of the registered GridNode for use
   * in Certificate.
   */
  private String getOrganizationName()
  {
    return _regInfo.getGridnodeName();
  }

  /**
   * Get the Organization Unit name for use in Certificate.
   */
  private String getOrganizationUnitName()
  {
    StringBuffer buf = new StringBuffer("GridTalk ");
    buf.append(_regInfo.getGridnodeID()).append(" (").append(
      _regInfo.getCategory()).append(")");
    return buf.toString();
  }

  /**
   * Get the Common Name for use in Certicate.
   */
  private String getCommonName()
  {
    StringBuffer buf = new StringBuffer("GridTalk ");
    buf.append(_regInfo.getGridnodeID()).append(" Certificate");

    return buf.toString();
  }

  /**
   * Validate the product key string.
   *
   * @param prodKeyStr The Product key string.
   * @param gridnodeID The GridNode ID that the Product key string registers.
   * @return ProductKey object if the product key string is valid for the
   * GridNode ID.
   */
  private ProductKey checkProductKey(String prodKeyStr, Integer gridnodeID)
    throws Throwable
  {
    ProductKey pKey = null;

    try
    {
      pKey = ProductKey.getProductKey(prodKeyStr, gridnodeID.intValue());
    }
    catch (InvalidProductKeyException ex)
    {
      Logger.warn("[RegistrationLogic.checkProductKey] Error ", ex);
      throw ex;
    }
    catch (Throwable t)
    {
      Logger.warn("[RegistrationLogic.checkProductKey] Error ", t);
      throw new InvalidProductKeyException(t.getMessage());
    }

    return pKey;
  }

  /**
   * Checks whether the license has expired.
   */
  private void checkExpiry()
    throws Throwable
  {
    long startDate = _prodKey.getStartDate().getTime();
    long endDate = _prodKey.getEndDate().getTime();
    long currentTime = System.currentTimeMillis();

    if (endDate < currentTime)
    {
      //_regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_EXPIRED);
      //_regInfo.setRegistrationState(RegistrationInfo.STATE_EXPIRED);
      //throw new InvalidProductKeyException("License has expired");
    	
    	//TWX 22062006
    	throw new LicenseFileExpiredException("License has expired");
    }
    else if (startDate > currentTime)
    {
      _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_NOT_COMMENCED);
    }
    else //031204NSL: set that the license is valid
    {
      _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_VALID);
    }
  }

  /**
   * Creates a scheduler to check for license validity.
   */
  private void createScheduler()
    throws Exception
  {
/*040218NSL
    clearExistingScheduler();

    iCalAlarm task = new iCalAlarm();
    task.setCategory(INodeLockConstant.CATEGORY);

    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+1, 0, 0, 0);

    task.setStartDt(cal.getTime());


    task.setRepeat(null);  //repeat == null means repeat indefinitely
    task.setDelayPeriod(new Long(INodeLockConstant.INTERVAL));   //in seconds

    task = ServiceLookupHelper.getTimeManager().addAlarm(task);
*/
    ScheduleHelper.scheduleCheckLicense();
  }

  /**
   * Removes all existing license scheduler.
   */
/*040218NSL
  private void clearExistingScheduler()
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      iCalAlarm.CATEGORY,
      filter.getEqualOperator(),
      INodeLockConstant.CATEGORY,
      false);
    ServiceLookupHelper.getTimeManager().cancelAlarmByFilter(filter);
  }
*/
  /**
   * Reset registration state.
   */
  private void resetRegistrationState()
  {
    _prodKey = null;
    CompanyProfile coyProfile = null;
    if (_regInfo != null)
      coyProfile = _regInfo.getCompanyProfile();

    _regInfo = new RegistrationInfo();
    _regInfo.setCompanyProfile(coyProfile);
  }

  private void assertRegistrationCompleted()
    throws InconsistentRegistrationStateException
  {
    if (_regInfo.getRegistrationState() != RegistrationInfo.STATE_REGISTERED)
      throw InconsistentRegistrationStateException.registrationNotCompleted();
  }

  /**
   * Broadcast that the Enterprise has been created.
   */
  private void notifyEnterpriseCreated(GridNode gn) throws Exception
  {
    EnterpriseCreatedNotification notification =
      new EnterpriseCreatedNotification(gn.getID(), _regInfo.getCompanyProfile());

    Notifier.getInstance().broadcast(notification);
  }

  // ********************* Methods for NodeLock

  public synchronized List checkNodeLock(String licenseFilename)
    throws Exception
  {
    Logger.debug("[RegistrationLogic.checkNodeLock] Enter");
    File licenseFile = FileUtil.getFile(IRegistrationPathConfig.PATH_LICENSE,
                                        licenseFilename);
    LicenseFileGenerator generator = new LicenseFileGenerator();
    generator.validateLicenseFile(licenseFile.getAbsolutePath());

    ArrayList list = new ArrayList();
    list.add(generator.getProductKeyF1());
    list.add(generator.getProductKeyF2());
    list.add(generator.getProductKeyF3());
    list.add(generator.getProductKeyF4());
    list.add(generator.getOsName());
    list.add(generator.getOsVersion());
    list.add(generator.getMachineName());

    Logger.debug("[RegistrationLogic.checkNodeLock] End");
    return list;
  }

  public synchronized void checkLicense()
    throws Throwable
  {
    Logger.debug("[RegistrationLogic.checkLicense] Enter");

    GridTalkLicense lastestLicense = getGridTalkLicense();
    if (validateNodeLock(lastestLicense))
    {
      checkExpiry(lastestLicense);
    }
    AlertUtil.notifyLicenseState(getRegistrationInfo()); //NSL20060331
    Logger.debug("[RegistrationLogic.checkLicense] End");
  }

  public synchronized boolean isLicenseValid()
  {
    try
    {
      if (_regInfo.getLicenseState() == License.STATE_VALID)
      {
        Logger.debug("[RegistrationLogic.isLicenseValid] TRUE");
        return true;
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[RegistrationLogic.isLicenseValid] Exception", ex);
    }
    Logger.debug("[RegistrationLogic.isLicenseValid] FALSE");
    return false;
  }

  // ********************* Utility methods ********************************

  /**
   * Retreive the GridTalkLicense
   */
  private GridTalkLicense getGridTalkLicense()
    throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GridTalkLicense.LICENSE_UID,
      filter.getEqualOperator(),
      null,
      true);
    filter.setOrderFields(new Object[] {GridTalkLicense.UID});
    Collection result = GridTalkLicenseEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
    {
      throw new ProductRegistrationException("No license found!");
    }
    Object[] gtlicenses = result.toArray();
    int lastposition = gtlicenses.length - 1;
    Object obj = gtlicenses[lastposition];
    return (GridTalkLicense)gtlicenses[lastposition];
  }

  private boolean validateNodeLock(GridTalkLicense gtlicense)
    throws Exception
  {
    String osName = gtlicense.getOsName();
    String osVersion = gtlicense.getOsVersion();
    String machineName = gtlicense.getMachineName();
    boolean valid = NodeLockUtil.validateNode(osName, osVersion, machineName);
    if (!valid)
    {
      License license = ServiceLookupHelper.getLicenseManager().findLicense(gtlicense.getLicenseUid());
      _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_REVOKED);
      license.setState(License.STATE_REVOKED);
      ServiceLookupHelper.getLicenseManager().updateLicense(license);
    }
    return valid;
  }

  private void checkExpiry(GridTalkLicense gtlicense)
    throws Exception
  {
    String sDate = NodeLockUtil.waxOff(gtlicense.getStartDate());
    long startDate = Long.parseLong(sDate);
    String eDate = NodeLockUtil.waxOff(gtlicense.getEndDate());
    long endDate = Long.parseLong(eDate);
    long currentTime = System.currentTimeMillis();

    License license = ServiceLookupHelper.getLicenseManager().findLicense(gtlicense.getLicenseUid());
    /*030508NSL: Delegate to AlertUtil
    GridNode gridnode = ServiceLookupHelper.getGridNodeManager().findMyGridNode();
    DefaultProviderList list = new DefaultProviderList();
    list.addProvider(new LicenseData(license));
    list.addProvider(new GridNodeData(gridnode));
    */

    //031204NSL: Put the license to Valid state first if its time to commence
    if (license.getState()==License.STATE_NOT_COMMENCED)
    {
      if (startDate < currentTime)
      {
        Logger.debug("[RegistrationLogic.checkExpiry] update from not commenced to valid");
        license.setState(License.STATE_VALID);
        ServiceLookupHelper.getLicenseManager().updateLicense(license);
      }
      else
      {
        // license not commenced yet, just return
        _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_NOT_COMMENCED);
        return;
      }
    }
    
    //031204NSL: Check for expiry if the license is valid
    if (license.getState()==License.STATE_VALID)
    {
      //expired
      if (endDate <= currentTime)
      {
        _regInfo.setRegistrationState(RegistrationInfo.STATE_EXPIRED);
        _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_EXPIRED);
        license.setState(License.STATE_EXPIRED);
        ServiceLookupHelper.getLicenseManager().updateLicense(license);
        /*030508NSL: Delegate to AlertUtil
        ServiceLookupHelper.getAlertManager().triggerAlert(
                              INodeLockConstant.EXPIRED_ALERT_NAME,
                              list,
                              "");
        */
        AlertUtil.alertLicenseExpired(license);
      }
      else
      {
        //031204NSL: Not expired, Set the license state to valid
        _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_VALID);

        //031204NSL: Send alert if the license is expiring soon
        //daysToExpiry must be long! otherwise the multiplication will be wrong.
        long daysToExpiry = ConfigHelper.getInstance().expiryWarningPeriod();
        Logger.debug("[RegistrationLogic.checkExpiry] ExpiryWarningPeriod = "+daysToExpiry);
        Logger.debug("[RegistrationLogic.checkExpiry] expiryDate = "+endDate);
        Logger.debug("[RegistrationLogic.checkExpiry] currentTime = "+currentTime);
              
        if ((endDate-(daysToExpiry*86400000)) <= currentTime)
        {
          /*030508NSL: Delegate to AlertUtil
          Date date = new Date(endDate);
          String msg = "License will expire on "+date.toString();
          ServiceLookupHelper.getAlertManager().triggerAlert(
                                INodeLockConstant.EXPIRING_ALERT_NAME,
                                list,
                                "");
          */
        
          AlertUtil.alertLicenseExpiring(license);
        }
      }
    }
    else //not valid: expired or revoked
    {
      _regInfo.setRegistrationState(RegistrationInfo.STATE_EXPIRED);
      if (license.getState()==License.STATE_EXPIRED)
        _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_EXPIRED);
      else //revoked
        _regInfo.setLicenseState(RegistrationInfo.STATE_LICENSE_REVOKED);
    }
  }
}