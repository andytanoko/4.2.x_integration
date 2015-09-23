/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 * Apr 11 2003    Koh Han Sing        Add in license state
 * Feb 06 2007    Chong SoonFui       Commented ICompanyProfile.CAN_DELETE
 */
package com.gridnode.gtas.model.registration;

import com.gridnode.gtas.model.gridnode.ICompanyProfile;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Registration module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RegistrationEntityFieldID
{
  private Hashtable _table;
  private static RegistrationEntityFieldID _self = null;

  private RegistrationEntityFieldID()
  {
    _table = new Hashtable();

    //CompanyProfile
    _table.put(ICompanyProfile.ENTITY_NAME,
      new Number[]
      {
        ICompanyProfile.ADDRESS,
        ICompanyProfile.ALT_EMAIL,
        ICompanyProfile.ALT_TEL,
//        ICompanyProfile.CAN_DELETE,
        ICompanyProfile.CITY,
        ICompanyProfile.COUNTRY,
        ICompanyProfile.COY_NAME,
        ICompanyProfile.EMAIL,
        ICompanyProfile.FAX,
        ICompanyProfile.IS_PARTNER,
        ICompanyProfile.LANGUAGE,
        ICompanyProfile.STATE,
        ICompanyProfile.TEL,
        ICompanyProfile.UID,
        ICompanyProfile.ZIP_CODE,
      });

    //RegistrationInfo
    _table.put(IRegistrationInfo.ENTITY_NAME,
      new Number[]
      {
        IRegistrationInfo.BIZ_CONNECTIONS,
        IRegistrationInfo.CATEGORY,
        IRegistrationInfo.COMPANY_PROFILE,
        IRegistrationInfo.GRIDNODE_ID,
        IRegistrationInfo.GRIDNODE_NAME,
        IRegistrationInfo.LIC_END_DATE,
        IRegistrationInfo.LIC_START_DATE,
        IRegistrationInfo.PRODUCT_KEY_F1,
        IRegistrationInfo.PRODUCT_KEY_F2,
        IRegistrationInfo.PRODUCT_KEY_F3,
        IRegistrationInfo.PRODUCT_KEY_F4,
        IRegistrationInfo.REGISTRATION_STATE,
        IRegistrationInfo.LICENSE_FILE,
        IRegistrationInfo.OS_NAME,
        IRegistrationInfo.OS_VERSION,
        IRegistrationInfo.MACHINE_NAME,
        IRegistrationInfo.LICENSE_STATE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new RegistrationEntityFieldID();
    }
    return _self._table;
  }
}