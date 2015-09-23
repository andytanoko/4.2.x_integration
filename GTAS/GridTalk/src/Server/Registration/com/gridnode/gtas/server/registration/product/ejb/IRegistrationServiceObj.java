/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistrationServiceObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Apr 07 2003    Koh Han Sing        Added in GridTalkLicense methods
 * Oct 20 2005    Neo Sok Lay         No corresponding business method in the bean 
 *                                    class com.gridnode.gtas.server.registration.product.ejb.RegistrationServiceBean 
 *                                    was found for method:
 *                                    - getRegistrationInfo
 *                                    - checkNodeLock
 *                                    Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method upgradeLicense does not throw java.rmi.RemoteException
 *                                    - The business method createGridTalkLicense does not throw java.rmi.RemoteException
 *                                    - The business method updateGridTalkLicense does not throw java.rmi.RemoteException
 *                                    - The business method deleteGridTalkLicense does not throw java.rmi.RemoteException
 *                                    - The business method findGridTalkLicense does not throw java.rmi.RemoteException
 *                                    - The business method findGridTalkLicenseByLicenseUid does not throw java.rmi.RemoteException
 *                                    - The business method findGridTalkLicenses does not throw java.rmi.RemoteException
 *                                    - The business method findGridTalkLicensesKeys does not throw java.rmi.RemoteException
 * Oct 26 2005    Neo Sok Lay         Business methods throwing Throwable is not acceptable
 *                                    for SAP J2EE deployment
 */
package com.gridnode.gtas.server.registration.product.ejb;

import com.gridnode.gtas.server.registration.exceptions.ProductRegistrationException;
import com.gridnode.gtas.server.registration.exceptions.InvalidSecurityPasswordException;
import com.gridnode.gtas.server.registration.model.GridTalkLicense;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;

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

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * EJB proxy object for the RegistrationServiceBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I5
 */
public interface IRegistrationServiceObj extends EJBObject
{
  /**
   * Initialize the RegistrationInfo.
   *
   * @param gridnodeID GridNode ID being registered.
   * @param gridnodeName Name of GridNode to be registered.
   * @param prodKeyF1 Product key F1 for the GridNode.
   * @param prodKeyF2 Product key F2 for the GridNode.
   * @param prodKeyF3 Product key F3 for the GridNode.
   * @param prodKeyF4 Product key F4 for the GridNode.
   * @param coyProfile CompanyProfile of this enterprise.
   */
  public RegistrationInfo initRegistration(Integer gridnodeID, String gridnodeName,
    String prodKeyF1, String prodKeyF2, String prodKeyF3, String prodKeyF4,
    CompanyProfile coyProfile, String osName, String osVersion, String machineName)
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Confirm the Registration.
   *
   * @param securityPassword The security password for generated private key.
   */
  public void confirmRegistration(String securityPassword)
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Abort the Registration.
   */
  public void cancelRegistration()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Obtain the cached RegistrationInfo.
   *
   * @return The RegistrationInfo.
   */
  public RegistrationInfo getRegistrationInfo()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Undo the Registration. Any created License, GridNode, or Certificate records
   * will be deleted.
   */
  public void undoRegistration()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Obtain the Product key of the registered GridTalk.
   *
   * @throws ProductRegistrationException Registration has not been completed.
   */
  public String getRegisteredProductKey()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Obtain the Certificate request file generated during product registration.
   *
   * @throws ProductRegistrationException Registration has not been completed.
   */
  public File getCertificateRequest()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Obtain the UID of the GridMaster certificate created during product
   * registration.
   *
   * @throws ProductRegistrationException Registration has not been
   * completed.
   */
  public Long getGridMasterCert()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Verify the security password. If the verification passes, set this
   * to unlock the private key.
   *
   * @param The password to be verified and set.
   */
  public void verifyAndSetSecurityPassword(String password)
    throws InvalidSecurityPasswordException, SystemException, RemoteException;

  /**
   * Obtain the UID of the Master certificate for the registered GridTalk.
   *
   * @throws ProductRegistrationException Registration has not been
   * completed.
   */
  public Long getMasterCert()
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Upgrades the existing GridTalk license.
   */
  public RegistrationInfo upgradeLicense(String prodKeyF1, String prodKeyF2,
    String prodKeyF3, String prodKeyF4, String osName, String osVersion,
    String machineName)
    throws ProductRegistrationException, SystemException, RemoteException;

  /**
   * Create a new GridTalkLicense.
   *
   * @param gridTalkLicense The GridTalkLicense entity.
   */
  public Long createGridTalkLicense(GridTalkLicense gridTalkLicense)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a GridTalkLicense
   *
   * @param gridTalkLicense The GridTalkLicense entity with changes.
   */
  public void updateGridTalkLicense(GridTalkLicense gridTalkLicense)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a GridTalkLicense.
   *
   * @param gridTalkLicenseUId The UID of the GridTalkLicense to delete.
   */
  public void deleteGridTalkLicense(Long gridTalkLicenseUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a GridTalkLicense using the GridTalkLicense UID.
   *
   * @param gridTalkLicenseUId The UID of the GridTalkLicense to find.
   * @return The GridTalkLicense found, or <B>null</B> if none exists with that
   * UID.
   */
  public GridTalkLicense findGridTalkLicense(Long gridTalkLicenseUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a GridTalkLicense using the LicenseUid
   *
   * @param licenseUid The Uid of the License to find.
   * @return The GridTalkLicense found, or <B>null</B> if none exists.
   */
  public GridTalkLicense findGridTalkLicenseByLicenseUid(Long licenseUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridTalkLicense that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of GridTalkLicense found, or empty collection if none
   * exists.
   */
  public Collection findGridTalkLicenses(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of GridTalkLicense that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of GridTalkLicense found, or empty collection if
   * none exists.
   */
  public Collection findGridTalkLicensesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Validates license to see if the node information is correct and it is not
   * expired
   */
  public void checkLicense()
    throws ProductRegistrationException, RemoteException;

  /**
   * Checks the nodelock information in the license file matches with the current
   * machine.
   */
  public List checkNodeLock(String licenseFilename)
    throws ProductRegistrationException, RemoteException;

  /**
   * Checks whether the license is valid
   */
  public boolean isLicenseValid()
    throws Exception, RemoteException;
}