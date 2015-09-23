/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICoyProfileManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for CoyProfileManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ICoyProfileManagerObj
  extends        EJBObject
{
  /**
   * Create a new CompanyProfile.
   *
   * @param coyProfile The CompanyProfile entity.
   * @return The UID of the created CompanyProfile
   */
  public Long createCompanyProfile(CompanyProfile coyProfile)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a CompanyProfile.
   *
   * @param coyProfile The CompanyProfile entity with changes.
   */
  public void updateCompanyProfile(CompanyProfile coyProfile)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a CompanyProfile.
   *
   * @param profileUId The UID of the CompanyProfile to delete.
   */
  public void deleteCompanyProfile(Long profileUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a CompanyProfile using the CompanyProfile UID.
   *
   * @param uID The UID of the CompanyProfile to find.
   * @return The CompanyProfile found, or <B>null</B> if none exists with that
   * UID.
   */
  public CompanyProfile findCompanyProfile(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of CompanyProfiles using a filtering condition
   *
   * @param filter The filtering condition of the CompanyProfiles to find.
   * @return A Collection of CompanyProfiles found, or empty collection if none
   * exists.
   */
  public Collection findCompanyProfiles(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the CompanyProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of CompanyProfiles found, or empty
   * collection if none.
   */
  public Collection findCompanyProfilesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the non-partner CompanyProfile. Should be only one and only one that
   * exists in the database.
   *
   * @return CompanyProfile The non-partner CompanyProfile.
   */
  public CompanyProfile findMyCompanyProfile()
    throws FindEntityException, SystemException, RemoteException;

}