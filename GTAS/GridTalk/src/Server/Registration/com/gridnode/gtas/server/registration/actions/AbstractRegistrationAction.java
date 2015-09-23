/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRegistrationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Change RegistrationServiceBean to stateless
 */
package com.gridnode.gtas.server.registration.actions;

import java.util.Map;

import com.gridnode.gtas.model.registration.RegistrationEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This abstract class provides the helper methods for Action classes of
 * the Registration module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public abstract class AbstractRegistrationAction extends AbstractGridTalkAction
{
  protected static final String REG_BEAN_HANDLE = "RegistrationBean";

  /**
   * Get the RegistrationBean.
   *
   * @return The Ejb proxy object to the RegistrationBean.
   * @throws Error in getting the RegistrationBean.
   */
  protected IRegistrationServiceObj getRegistrationBean()
    throws Exception
  {
//    Handle regBeanHandle = (Handle)this.sm.getAttribute(REG_BEAN_HANDLE);
    IRegistrationServiceObj regObj = null;
//
//    if (regBeanHandle == null)
//    {
      regObj = (IRegistrationServiceObj)ServiceLocator.instance(
                  ServiceLocator.CLIENT_CONTEXT).getObj(
                  IRegistrationServiceHome.class.getName(),
                  IRegistrationServiceHome.class,
                  new Object[0]);
//      this.sm.setAttribute(REG_BEAN_HANDLE, regObj.getHandle());
//    }
//    else
//      regObj = (IRegistrationServiceObj)regBeanHandle.getEJBObject();

    return regObj;
  }

  /**
   * Convert the RegistrationInfo to Map representation.
   *
   * @param regInfo The RegistrationInfo to convert.
   * @retunr Map representation for <code>regInfo</code>.
   */
  protected Map convertToMap(RegistrationInfo regInfo)
  {
    return RegistrationInfo.convertToMap(
             regInfo,
             RegistrationEntityFieldID.getEntityFieldID(),
             null);
  }

  protected RegistrationInfo getRegistrationInfo() throws Throwable
  {
    RegistrationInfo regInfo = getRegistrationBean().getRegistrationInfo();
    if (regInfo.getCompanyProfile() == null)
      regInfo.setCompanyProfile(new CompanyProfile());
    else
    {
      if (getEnterpriseID() == null)
        setEnterpriseID(regInfo.getGridnodeID().toString());
    }

    return regInfo;
  }
}