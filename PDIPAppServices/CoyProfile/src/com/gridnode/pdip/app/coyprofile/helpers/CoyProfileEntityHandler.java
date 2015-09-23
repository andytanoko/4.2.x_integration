/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CoyProfileEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.coyprofile.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.coyprofile.entities.ejb.ICoyProfileLocalHome;
import com.gridnode.pdip.app.coyprofile.entities.ejb.ICoyProfileLocalObj;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the CompanyProfile.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class CoyProfileEntityHandler
  extends          LocalEntityHandler
{
  private final DataFilterImpl NON_PN_FILTER = new DataFilterImpl();

  private CoyProfileEntityHandler()
  {
    super(CompanyProfile.ENTITY_NAME);

    NON_PN_FILTER.addSingleFilter(null, CompanyProfile.IS_PARTNER,
      NON_PN_FILTER.getEqualOperator(), Boolean.FALSE, false);
  }

  /**
   * Get an instance of a CoyProfileEntityHandler.
   */
  public static CoyProfileEntityHandler getInstance()
  {
    CoyProfileEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(CompanyProfile.ENTITY_NAME, true))
    {
      handler = (CoyProfileEntityHandler)EntityHandlerFactory.getHandlerFor(
                  CompanyProfile.ENTITY_NAME, true);
    }
    else
    {
      handler = new CoyProfileEntityHandler();
      EntityHandlerFactory.putEntityHandler(CompanyProfile.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ICoyProfileLocalHome.class.getName(),
      ICoyProfileLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ICoyProfileLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return ICoyProfileLocalObj.class;
  }

  // ********************** Own methods ******************************

  public void remove(Long uID) throws Throwable
  {
    CompanyProfile coyProfile = (CompanyProfile)getEntityByKeyForReadOnly(uID);
    if (coyProfile != null)
    {
      if (coyProfile.canDelete())
      {
        super.remove(uID);
      }
      else
        throw new ApplicationException("CompanyProfile not allowed to be deleted!");
    }
  }

  /**
   * Count the number of non-partner CompanyProfiles.
   *
   * @return The number of non-partner CompanyProfiles.
   */
  public int numNonPartnerProfiles() throws Throwable
  {
    return getNonPartnerProfiles().size();
  }

  /**
   * Get the keys to the non-partner CompanyProfiles.
   *
   * @return Collection of UIDs of non-partner CompanyProfiles.
   */
  public Collection getNonPartnerProfiles() throws Throwable
  {
    return getKeyByFilterForReadOnly(NON_PN_FILTER);
  }

}