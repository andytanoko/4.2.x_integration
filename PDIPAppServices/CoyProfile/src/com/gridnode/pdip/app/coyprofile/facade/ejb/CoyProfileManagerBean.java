/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CoyProfileManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.facade.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.coyprofile.helpers.CoyProfileEntityHandler;
import com.gridnode.pdip.app.coyprofile.helpers.Logger;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This bean provides services to manage the Company Profiles.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CoyProfileManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3628593855254169073L;
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

  // ************************ Implementing methods in ICoyProfileManagerObj

  /**
   * Create a new CompanyProfile.
   *
   * @param coyProfile The CompanyProfile entity.
   * @return The UID of the created CompanyProfile.
   */
  public Long createCompanyProfile(CompanyProfile coyProfile)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "createCompanyProfile";
    Object[] params     = new Object[] {coyProfile};

    Long key = null;
    try
    {
      logger.logEntry(methodName, params);

      if (!coyProfile.isPartner().booleanValue() &&
          getEntityHandler().numNonPartnerProfiles() > 0)
        throw new DuplicateEntityException("Only one Non Partner Company Profile is allowed!");

      CompanyProfile created = (CompanyProfile)getEntityHandler().createEntity(coyProfile);
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
   * Update a CompanyProfile.
   *
   * @param coyProfile The CompanyProfile entity with changes.
   */
  public void updateCompanyProfile(CompanyProfile coyProfile)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "updateCompanyProfile";
    Object[] params     = new Object[] {coyProfile};

    try
    {
      logger.logEntry(methodName, params);
      getEntityHandler().update(coyProfile);
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
   * Delete a CompanyProfile.
   *
   * @param beUId The UID of the CompanyProfile to delete.
   */
  public void deleteCompanyProfile(Long profileUId)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "deleteCompanyProfile";
    Object[] params     = new Object[] {profileUId};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().remove(profileUId);
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


  // ********************** Finders ******************************************

  /**
   * Find a CompanyProfile using the CompanyProfile UID.
   *
   * @param uID The UID of the CompanyProfile to find.
   * @return The CompanyProfile found
   * @exception FindCompanyProfileException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public CompanyProfile findCompanyProfile(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "findCompanyProfile";
    Object[] params     = new Object[] {uID};

    CompanyProfile coyProfile = null;

    try
    {
      logger.logEntry(methodName, params);

      coyProfile = (CompanyProfile)getEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return coyProfile;
  }

  /**
   * Find a number of CompanyProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of CompanyProfiles found, or empty collection if none
   * exists.
   * @exception FindEntityException Error in executing the finder.
   */
  public Collection findCompanyProfiles(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "findCompanyProfiles";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = getEntityHandler().getEntityByFilterForReadOnly(filter);
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
   * Find the keys of the CompanyProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of CompanyProfiles found, or empty
   * collection if none.
   * @excetpion FindEntityException Error in executing the finder.
   */
  public Collection findCompanyProfilesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "findCompanyProfilesKeys";
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


  /**
   * Find the non-partner CompanyProfile. Should be only one and only one that
   * exists in the database.
   *
   * @return CompanyProfile The non-partner CompanyProfile.
   */
  public CompanyProfile findMyCompanyProfile()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "findMyCompanyProfile";
    Object[] params     = new Object[] {};

    CompanyProfile coyProfile = null;

    try
    {
      logger.logEntry(methodName, params);

      Collection results = getEntityHandler().getNonPartnerProfiles();

      if (results != null && !results.isEmpty())
        coyProfile = (CompanyProfile)getEntityHandler().getEntityByKeyForReadOnly(
                       (Long)results.toArray()[0]);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return coyProfile;
  }

  private CoyProfileEntityHandler getEntityHandler()
  {
     return CoyProfileEntityHandler.getInstance();
  }
}