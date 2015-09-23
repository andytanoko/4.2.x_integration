/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultBizEntityCreator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.def;

import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.bizreg.helpers.Logger;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

/**
 * A DefaultBizEntityCreator object allows creation of the
 * default business entity for the enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DefaultBizEntityCreator
{
  private static final String CONFIG_NAME = "def.be";

  protected DefaultBizEntityCreator()
  {
  }

  /**
   * Retrieve the UID of the default BusinessEntity for the specified
   * enterprise.
   *
   * @param enterpriseId ID of the enterprise.
   */
  public static Long getDefaultBizEntityUID(String enterpriseId)
  {
    Long uid = null;
    try
    {
      BusinessEntity be = createBe(enterpriseId);
      // if already exists, update the details
      uid = ActionHelper.getBizRegManager().findBusinessEntityKey(
             enterpriseId, be.getBusEntId());
    }
    catch (Throwable ex)
    {
      Logger.debug("[DefaultBizEntityCreator.getDefaultBizEntityUID] "+
        "Default BizEntity not found: "+ex.getMessage());
    }

    return uid;
  }

  /**
   * Create a default Business Entity for the enterprise
   *
   * @param enterpriseId Id of the enterprise
   * @param email Email from the profile of the enterprise
   * @param tel Telephone No from the profile of the enterprise
   * @param fax Fax No from the profile of the enterprise
   * @param address Address from the profile of the enterprise
   * @param city City from the profile of the enterprise
   * @param state State from the profile of the enterprise
   * @param zipCode ZipCode from the profile of the enterprise
   * @param country Country from the profile of the enterprise
   * @param language Language from the profile of the enterprise
   */
  public static Long createDefaultBizEntity(
    String enterpriseId, String email, String tel, String  fax,
    String address, String city, String  state, String zipCode,
    String country, String language) throws Throwable
  {
    BusinessEntity be = createBe(enterpriseId);

    WhitePage wp      = new WhitePage();
    wp.setAddress(address);
    wp.setCanDelete(false);
    wp.setCity(city);
    wp.setCountry(country);
    wp.setEmail(email);
    wp.setFax(fax);
    wp.setLanguage(language);
    wp.setState(state);
    wp.setTel(tel);
    wp.setZipCode(zipCode);

    be.setWhitePage(wp);

    Long uid = null;

    // if already exists, update the details
    BusinessEntity existBe = ActionHelper.getBizRegManager().findBusinessEntity(
                               enterpriseId, be.getBusEntId());

    if (existBe != null)
    {
      be.setUId(existBe.getUId());
      be.setVersion(existBe.getVersion());
      wp.setUId(existBe.getWhitePage().getUId());
      wp.setBeUId(new Long(be.getUId()));
      wp.setVersion(existBe.getWhitePage().getVersion());
      ActionHelper.getBizRegManager().updateBusinessEntity(be);
      uid = new Long(be.getUId());
    }
    else
    {
      // otherwise, create
      uid = ActionHelper.getBizRegManager().createBusinessEntity(be);
    }

    return uid;
  }

  /**
   * Creates a Business Entity object using the default information loaded
   * from the Def Be Propertiesfile.
   *
   * @param enterpriseId Id of the Enterprise.
   */
  protected static BusinessEntity createBe(String enterpriseId)
    throws Throwable
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(
                             CONFIG_NAME);
    if (config == null)
    {
      config = new Configuration();
      Logger.warn("Default Be configuration file not found!");
    }

    BusinessEntity be = new BusinessEntity();
    be.setEnterpriseId(enterpriseId);
    be.setBusEntId(config.getString("id", "DEF"));
    StringBuffer buff = new StringBuffer("Gridtalk ");
    buff.append(enterpriseId).append(" ").append(
      config.getString("description", "Default Business Entity"));
    be.setDescription(buff.toString());
    be.setCanDelete(false);
    be.setPartner(false);
    be.setState(BusinessEntity.STATE_NORMAL);

    return be;
  }


}