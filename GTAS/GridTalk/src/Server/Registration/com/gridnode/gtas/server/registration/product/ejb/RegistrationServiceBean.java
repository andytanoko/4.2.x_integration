/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Apr 07 2003    Koh Han Sing        Added in GridTalkLicense methods
 * Oct 26 2005    Neo Sok Lay         Business methods throwing Throwable is not acceptable
 *                                    for SAP J2EE deployment
 * Jun 22 2006    Tam Wei Xiang       GNDB00026893 modified upgradeLicense(...) and
 *                                    initRegistration(...) to handle the 
 *                                    LicenseFileExpiredException.                                   
 */
package com.gridnode.gtas.server.registration.product.ejb;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.registration.entities.ejb.IGridTalkLicenseLocalObj;
import com.gridnode.gtas.server.registration.exceptions.InvalidSecurityPasswordException;
import com.gridnode.gtas.server.registration.exceptions.LicenseFileExpiredException;
import com.gridnode.gtas.server.registration.exceptions.ProductRegistrationException;
import com.gridnode.gtas.server.registration.helpers.GridTalkLicenseEntityHandler;
import com.gridnode.gtas.server.registration.helpers.Logger;
import com.gridnode.gtas.server.registration.model.GridTalkLicense;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.product.RegistrationLogic;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.util.PasswordMask;

/**
 * This EJB handles the Product Registration services.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RegistrationServiceBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2986212702439699672L;
	private SessionContext    _ctx;

  public RegistrationServiceBean()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
    _ctx = ctx;
  }

  public void ejbCreate()
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /**
   * @see IRegistrationServiceObj#initRegistration
   */
  public RegistrationInfo initRegistration(
    Integer gridnodeID, String gridnodeName,
    String prodKeyF1, String prodKeyF2, String prodKeyF3, String prodKeyF4,
    CompanyProfile coyProfile, String osName, String osVersion, String machineName)
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "initRegistration";
    Object[] params     = new Object[] {
                            gridnodeID,
                            gridnodeName,
                            prodKeyF1,
                            prodKeyF2,
                            prodKeyF3,
                            prodKeyF4,
                            coyProfile,
                          };

    RegistrationInfo regInfo = null;

    try
    {
      logger.logEntry(methodName, params);

      regInfo = RegistrationLogic.getInstance().initRegistration(
                  gridnodeID, gridnodeName, prodKeyF1, prodKeyF2, prodKeyF3, prodKeyF4,
                  coyProfile, osName, osVersion, machineName);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      rollback();
      
      //TWX 22062006
      if(t instanceof LicenseFileExpiredException)
      {
      	throw (LicenseFileExpiredException)t;
      }//end
      
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return regInfo;
  }

  /**
   * @see IRegistrationServiceObj#confirmRegistration
   */
  public void confirmRegistration(String securityPassword)
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "confirmRegistration";
    Object[] params     = new Object[] {
                            new PasswordMask(securityPassword).getMask(),
                          };

    try
    {
      logger.logEntry(methodName, params);

      RegistrationLogic.getInstance().confirmRegistration(securityPassword);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      rollback();
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IRegistrationServiceObj#cancelRegistration
   */
  public void cancelRegistration()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "cancelRegistration";
    Object[] params     = new Object[] {
                          };

    try
    {
      logger.logEntry(methodName, params);

      RegistrationLogic.getInstance().cancelRegistration();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IRegistrationServiceObj@getRegistrationInfo
   */
  public RegistrationInfo getRegistrationInfo() throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "getRegistrationInfo";
    Object[] params     = new Object[] {
                          };

    RegistrationInfo regInfo = null;
    try
    {
      logger.logEntry(methodName, params);

      regInfo = RegistrationLogic.getInstance().getRegistrationInfo();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return regInfo;
  }

  /**
   * @see IRegistrationServiceObj#undoRegistration
   */
  public void undoRegistration()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "undoRegistration";
    Object[] params     = new Object[] {
                          };

    try
    {
      logger.logEntry(methodName, params);

      RegistrationLogic.getInstance().undoRegistration();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IRegistrationServiceObj@getRegisteredProductKey
   */
  public String getRegisteredProductKey()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "getRegisteredProductKey";
    Object[] params     = new Object[] {
                          };

    String prodKey = null;
    try
    {
      logger.logEntry(methodName, params);

      prodKey = RegistrationLogic.getInstance().getRegisteredProductKey();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return prodKey;
  }

  /**
   * @see IRegistrationServiceObj@getCertificateRequest
   */
  public File getCertificateRequest()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "getCertificateRequest";
    Object[] params     = new Object[] {
                          };

    File certReq = null;
    try
    {
      logger.logEntry(methodName, params);

      certReq = RegistrationLogic.getInstance().getCertificateRequest();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return certReq;
  }

  /**
   * @see IRegistrationServiceObj@getGridMasterCert
   */
  public Long getGridMasterCert()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "getGridMasterCert";
    Object[] params     = new Object[] {
                          };

    Long certUID = null;
    try
    {
      logger.logEntry(methodName, params);

      certUID = RegistrationLogic.getInstance().getGridMasterCert();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return certUID;
  }

  /**
   * @see IRegistrationServiceObj#verifyAndSetSecurityPassword
   */
  public void verifyAndSetSecurityPassword(String password)
    throws InvalidSecurityPasswordException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "verifyAndSetSecurityPassword";
    Object[] params     = new Object[] {
                            new PasswordMask(password).getMask(),
                          };

    try
    {
      logger.logEntry(methodName, params);

      RegistrationLogic.getInstance().verifyAndSetSecurityPassword(password);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new InvalidSecurityPasswordException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IRegistrationServiceObj@getMasterCert
   */
  public Long getMasterCert()
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "getMasterCert";
    Object[] params     = new Object[] {
                          };

    Long certUID = null;
    try
    {
      logger.logEntry(methodName, params);

      certUID = RegistrationLogic.getInstance().getMasterCert();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return certUID;
  }

  /**
   * Upgrades the existing GridTalk license.
   */
  public RegistrationInfo upgradeLicense(String prodKeyF1, String prodKeyF2,
    String prodKeyF3, String prodKeyF4, String osName, String osVersion,
    String machineName)
    throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "upgradeLicense";
    Object[] params     = new Object[] {
                            prodKeyF1,
                            prodKeyF2,
                            prodKeyF3,
                            prodKeyF4,
                          };

    RegistrationInfo regInfo = null;

    try
    {
      logger.logEntry(methodName, params);

      regInfo = RegistrationLogic.getInstance().upgradeLicense(
                  prodKeyF1, prodKeyF2, prodKeyF3, prodKeyF4,
                  osName, osVersion, machineName);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      rollback();
      
      //TWX 22062006
      if(t instanceof LicenseFileExpiredException)
      {
      	throw (LicenseFileExpiredException)t;
      }//end 
      
      throw new ProductRegistrationException(t.getMessage());
      	
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return regInfo;

  }

  // ********************* Utility methods ********************************

  private void rollback() throws ProductRegistrationException, SystemException
  {
    FacadeLogger logger = Logger.getRegistrationFacadeLogger();
    String methodName   = "rollback";
    Object[] params     = new Object[] {
                          };
    try
    {
      logger.logEntry(methodName, params);
      RegistrationLogic.getInstance().rollback();
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new ProductRegistrationException(
                  "Unable to rollback registration: "+
                  t.getMessage());
    }
    finally
    {
      logger.logEntry(methodName, params);
    }

  }

  // ********************* GridTalk License ********************************

  /**
   * Create a new GridTalkLicense.
   *
   * @param gridTalkLicense The GridTalkLicense entity.
   */
  public Long createGridTalkLicense(GridTalkLicense gridTalkLicense)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[RegistrationServiceBean.createGridTalkLicense] Enter");

    try
    {
      IGridTalkLicenseLocalObj obj =
      (IGridTalkLicenseLocalObj)getGridTalkLicenseEntityHandler().create(gridTalkLicense);

      Logger.log("[RegistrationServiceBean.createGridTalkLicense] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[RegistrationServiceBean.createGridTalkLicense] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[RegistrationServiceBean.createGridTalkLicense] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.createGridTalkLicense] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.createGridTalkLicense(GridTalkLicense) Error ",
        ex);
    }
  }

  /**
   * Update a GridTalkLicense
   *
   * @param gridTalkLicense The GridTalkLicense entity with changes.
   */
  public void updateGridTalkLicense(GridTalkLicense gridTalkLicense)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[RegistrationServiceBean.updateGridTalkLicense] Enter");

    try
    {
      getGridTalkLicenseEntityHandler().update(gridTalkLicense);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[RegistrationServiceBean.updateGridTalkLicense] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.updateGridTalkLicense] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.updateGridTalkLicense(GridTalkLicense) Error ",
        ex);
    }

    Logger.log("[RegistrationServiceBean.updateGridTalkLicense] Exit");
  }

  /**
   * Delete a GridTalkLicense.
   *
   * @param gridTalkLicenseUId The UID of the GridTalkLicense to delete.
   */
  public void deleteGridTalkLicense(Long gridTalkLicenseUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[RegistrationServiceBean.deleteGridTalkLicense] Enter");

    try
    {
      getGridTalkLicenseEntityHandler().remove(gridTalkLicenseUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[RegistrationServiceBean.deleteGridTalkLicense] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[RegistrationServiceBean.deleteGridTalkLicense] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.deleteGridTalkLicense] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.deleteGridTalkLicense(gridTalkLicenseUId) Error ",
        ex);
    }

    Logger.log("[RegistrationServiceBean.deleteGridTalkLicense] Exit");
  }

  /**
   * Find a GridTalkLicense using the GridTalkLicense UID.
   *
   * @param gridTalkLicenseUId The UID of the GridTalkLicense to find.
   * @return The GridTalkLicense found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridTalkLicense findGridTalkLicense(Long gridTalkLicenseUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[RegistrationServiceBean.findGridTalkLicense] UID: "+
      gridTalkLicenseUid);

    GridTalkLicense gridTalkLicense = null;

    try
    {
      gridTalkLicense =
        (GridTalkLicense)getGridTalkLicenseEntityHandler().
          getEntityByKeyForReadOnly(gridTalkLicenseUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.findGridTalkLicense(gridTalkLicenseUId) Error ",
        ex);
    }

    return gridTalkLicense;
  }

  /**
   * Find a GridTalkLicense using the LicenseUid
   *
   * @param licenseUid The Uid of the License to find.
   * @return The GridTalkLicense found, or <B>null</B> if none exists.
   */
  public GridTalkLicense findGridTalkLicenseByLicenseUid(Long licenseUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[RegistrationServiceBean.findGridTalkLicense] licenseUid: "+licenseUid);

    GridTalkLicense gridTalkLicense = null;

    try
    {
      gridTalkLicense =
        (GridTalkLicense)getGridTalkLicenseEntityHandler().findByLicenseUid(licenseUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicense] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.findGridTalkLicense(licenseUid) Error ",
        ex);
    }

    return gridTalkLicense;
  }

  /**
   * Find a number of GridTalkLicense that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridTalkLicense found, or empty collection if none
   * exists.
   */
  public Collection findGridTalkLicenses(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[RegistrationServiceBean.findGridTalkLicenses] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gridTalkLicenses = null;
    try
    {
      gridTalkLicenses =
        getGridTalkLicenseEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicenses] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicenses] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicenses] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.findGridTalkLicenses(filter) Error ",
        ex);
    }

    return gridTalkLicenses;
  }

  /**
   * Find a number of GridTalkLicense that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridTalkLicense found, or empty collection if
   * none exists.
   */
  public Collection findGridTalkLicensesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[RegistrationServiceBean.findGridTalkLicensesKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection gridTalkLicensesKeys = null;
    try
    {
      gridTalkLicensesKeys =
        getGridTalkLicenseEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicensesKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicensesKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.findGridTalkLicensesKeys] Error ", ex);
      throw new SystemException(
        "RegistrationServiceBean.findGridTalkLicensesKeys(filter) Error ",
        ex);
    }

    return gridTalkLicensesKeys;
  }

  // ********************* Methods for NodeLock

  /**
   * Validates license to see if the node information is correct and it is not
   * expired. Will trigger alerts if found license has expired or is expiring.
   * Will revoke the license if the nodelock information doesn't match
   */
  public void checkLicense()
    throws ProductRegistrationException
  {
    Logger.debug("[RegistrationServiceBean.checkLicense] Enter");
    try
    {
    	RegistrationLogic.getInstance().checkLicense();
    }
    catch (Throwable t)
    {
    	Logger.warn("[RegistrationServiceBean.checkLicense] Error", t);
    	throw new ProductRegistrationException("Unable to validate license", t);
    }
    finally
    {
    	Logger.debug("[RegistrationServiceBean.checkLicense] Exit");
    }
  }

  /**
   * Checks the nodelock information in the license file matches with the current
   * machine.
   */
  public List checkNodeLock(String licenseFilename)
    throws ProductRegistrationException
  {
    Logger.debug("[RegistrationServiceBean.checkNodeLock] Enter");
    try
    {
    	return RegistrationLogic.getInstance().checkNodeLock(licenseFilename);
    }
    catch (Throwable t)
    {
    	Logger.warn("[RegistrationServiceBean.checkNodeLock] Error", t);
    	throw new ProductRegistrationException("Unable to verify node lock information in license file", t);
    }
    finally
    {
    	Logger.debug("[RegistrationServiceBean.checkNodeLock] Exit");
    }
  }

  /**
   * Checks whether the license is valid
   */
  public boolean isLicenseValid()
    throws Exception
  {
    try
    {
      Logger.debug("[RegistrationServiceBean.isLicenseValid] Enter");
      return RegistrationLogic.getInstance().isLicenseValid();
    }
    catch (Throwable ex)
    {
      Logger.warn("[RegistrationServiceBean.isLicenseValid] Unable to check license");
      return false;
    }
  }

  // ********************* Methods for EntityHandler

  private GridTalkLicenseEntityHandler getGridTalkLicenseEntityHandler()
  {
     return GridTalkLicenseEntityHandler.getInstance();
  }


}