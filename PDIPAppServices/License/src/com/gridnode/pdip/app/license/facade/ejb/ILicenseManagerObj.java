/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILicenseManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 * Apr 10 2003    Koh Han Sing        Removed revalidateLicenses() method
 */
package com.gridnode.pdip.app.license.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.license.exceptions.LicenseRevocationException;
import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for LicenseManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ILicenseManagerObj
  extends        EJBObject
{
  /**
   * Create a new License.
   *
   * @param license The License entity.
   * @return The UID of the created License
   */
  public Long createLicense(License license)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Revoke a License.
   *
   * @param licenseUID The UID of the License to revoke.
   */
  public void revokeLicense(Long licenseUID)
    throws LicenseRevocationException, SystemException, RemoteException;

  /**
   * Delete a License.
   *
   * @param licenseUId The UID of the License to delete.
   */
  public void deleteLicense(Long licenseUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Update a License.
   *
   * @param license The License to update.
   */
  public void updateLicense(License license)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Re-validate the "Valid" Licenses. The "Valid" licenses' states will be
   * updated accordingly if they are found to be not valid any more.
   */
//  public void revalidateLicenses()
//    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Find a License using the License UID.
   *
   * @param uID The UID of the License to find.
   * @return The License found, or <B>null</B> if none exists with that
   * UID.
   */
  public License findLicense(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a Valid License using the License UID.
   *
   * @param uID The UID of the License to find.
   * @return The Valid License found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public License findValidLicense(Long uID)
    throws FindEntityException, SystemException, RemoteException;

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
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of Licenses using a filtering condition
   *
   * @param filter The filtering condition of the Licenses to find.
   * @return A Collection of Licenses found, or empty collection if none
   * exists.
   */
  public Collection findLicenses(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the Licenses that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of Licenses found, or empty
   * collection if none.
   */
  public Collection findLicensesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;


}