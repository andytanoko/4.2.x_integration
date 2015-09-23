/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 * Apr 10 2003    Koh Han Sing        Removed revalidateLicenses() method
 */
package com.gridnode.pdip.app.license.facade.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.license.exceptions.InvalidLicenseException;
import com.gridnode.pdip.app.license.exceptions.LicenseRevocationException;
import com.gridnode.pdip.app.license.helpers.LicenseEntityHandler;
import com.gridnode.pdip.app.license.helpers.Logger;
import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This bean provides services to manage the Licenses.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LicenseManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2565067635153046455L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
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

  // ************************ Implementing methods in ILicenseManagerObj

  /**
   * Create a new License.
   *
   * @param license The License entity.
   * @return The UID of the created License.
   */
  public Long createLicense(License license)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "createLicense";
    Object[] params     = new Object[] {license};

    Long key = null;
    try
    {
      logger.logEntry(methodName, params);

      checkValidityPeriod(license.getStartDate(), license.getEndDate());
      setState(license);

      License created = (License)getEntityHandler().createEntity(license);
      key = (Long)created.getKey();
    }
    catch (Throwable ex)
    {
      logger.logCreateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return key;
  }

  /**
   * Revoke a License.
   *
   * @param licenseUID The UID of the License to revoke.
   */
  public void revokeLicense(Long licenseUID)
    throws LicenseRevocationException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "revokeLicense";
    Object[] params     = new Object[] {licenseUID};

    try
    {
      logger.logEntry(methodName, params);

      License license = (License)getEntityHandler().getEntityByKey(licenseUID);
      license.setState(License.STATE_REVOKED);

      getEntityHandler().update(license);
    }
    catch (Throwable ex)
    {
      logger.logWarn(methodName, params, ex);
      throw new LicenseRevocationException(ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Delete a License.
   *
   * @param beUId The UID of the License to delete.
   */
  public void deleteLicense(Long licenseUId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "deleteLicense";
    Object[] params     = new Object[] {licenseUId};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().remove(licenseUId);
    }
    catch (Throwable ex)
    {
      logger.logDeleteError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Update a License.
   *
   * @param license The License to update.
   */
  public void updateLicense(License license)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "updateLicense";
    Object[] params     = new Object[] {license};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().update(license);
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Re-validate the "Valid" Licenses. The "Valid" licenses' states will be
   * updated accordingly if they are found to be not valid any more.
   */
/*  public void revalidateLicenses()
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "revalidateLicenses";
    Object[] params     = new Object[] {};

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, License.STATE,
        filter.getEqualOperator(), new Short(License.STATE_VALID), false);
      filter.setOrderFields(new Object[] {License.START_DATE});

      Collection results = getEntityHandler().getEntityByFilterForReadOnly(filter);

      Object[] licenses = results.toArray();
      License license;
      for (int i=0; i<licenses.length; i++)
      {
        license = (License)licenses[i];
        //setState(license);
        if (setState(license))
           getEntityHandler().update(license);
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }
*/
  // ********************** Finders ******************************************

  /**
   * Find a License using the License UID.
   *
   * @param uID The UID of the License to find.
   * @return The License found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public License findLicense(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "findLicense";
    Object[] params     = new Object[] {uID};

    License license = null;

    try
    {
      logger.logEntry(methodName, params);

      //revalidateLicenses();

      license = (License)getEntityHandler().getEntityByKey(uID);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return license;
  }

  /**
   * Find a Valid License using the License UID.
   *
   * @param uID The UID of the License to find.
   * @return The Valid License found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public License findValidLicense(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "findValidLicense";
    Object[] params     = new Object[] {uID};

    License license = null;

    try
    {
      logger.logEntry(methodName, params);

      //revalidateLicenses();

      license = (License)getEntityHandler().getEntityByKey(uID);
      if (!license.isLicenseValid())
      {
        throw new InvalidLicenseException("License found but not valid!");
      }
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return license;
  }

  /**
   * Find a Valid License using the licensed Product name and version.
   *
   * @param prodName    The name of the licensed product.
   * @param prodVersion The version of the licensed product.
   * @return The Valid License found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public License findValidLicense(String prodName, String prodVersion)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "findValidLicense";
    Object[] params     = new Object[] {prodName, prodVersion};

    License license = null;

    try
    {
      logger.logEntry(methodName, params);

      //revalidateLicenses();

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, License.PRODUCT_NAME, filter.getEqualOperator(),
        prodName, false);
      filter.addSingleFilter(filter.getAndConnector(), License.PRODUCT_VERSION,
        filter.getEqualOperator(), prodVersion, false);
      filter.addSingleFilter(filter.getAndConnector(), License.STATE,
        filter.getEqualOperator(), new Short(License.STATE_VALID), false);
      filter.setOrderFields(new Object[] {License.START_DATE});

      Collection results = getEntityHandler().getEntityByFilter(filter);

      if (results != null && !results.isEmpty())
      {
        license = (License)results.toArray()[0];
      }
      else
        throw new FindEntityException("No valid License found for product "+
          prodName + ", Version "+prodVersion);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return license;
  }

  /**
   * Find a number of Licenses that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Licenses found, or empty collection if none
   * exists.
   * @exception FindEntityException Error in executing the finder.
   */
  public Collection findLicenses(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "findLicenses";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      //revalidateLicenses();

      results = getEntityHandler().getEntityByFilter(filter);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Find the keys of the Licenses that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of Licenses found, or empty
   * collection if none.
   * @excetpion FindEntityException Error in executing the finder.
   */
  public Collection findLicensesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLicenseFacadeLogger();
    String methodName   = "findLicensesKeys";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = getEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  // ******************** Private Methods ************************

  private void checkValidityPeriod(Date startDate, Date endDate)
    throws ApplicationException
  {
    if (startDate.after(endDate))
      throw new ApplicationException("Invalid Validity Period: " +
                startDate + " > " + endDate);
  }

  private boolean setState(License license)
  {
    Date currentDate = new Date();
    boolean stateChanged = false;

    switch (license.getState())
    {
      case License.STATE_REVOKED:
      case License.STATE_EXPIRED:
           break;
      case License.STATE_NOT_COMMENCED:
           if (!currentDate.before(license.getStartDate()) &&
               !currentDate.after(license.getEndDate()))
           {
             license.setState(License.STATE_VALID);
             stateChanged = true;
           }
      case License.STATE_VALID:
           if (currentDate.after(license.getEndDate()))
           {
             license.setState(License.STATE_EXPIRED);
             stateChanged = true;
           }
      default : break;
    }

    return stateChanged;
  }

  private LicenseEntityHandler getEntityHandler()
  {
     return LicenseEntityHandler.getInstance();
  }

}