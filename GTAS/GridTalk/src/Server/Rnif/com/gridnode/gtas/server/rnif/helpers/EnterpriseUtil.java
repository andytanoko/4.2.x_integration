/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Neo Sok Lay         Fix defect: GNDB00015933 Filter off deleted
 *                                    partners.
 * Apr 03 2006    Neo Sok Lay         Only retrieve Enabled partners.     
 * Jul 09 2007    Neo Sok Lay         GNDB00028407: Add getCountry(gnodeid, beid)                              
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.bizreg.helpers.Logger;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerHome;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.IWhitePage;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerHome;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerObj;
import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EnterpriseUtil
{

  final static String  DEFAULT_DUNS = "000000000";
  

  public static IBizRegistryManagerObj getBizRegistryManager() throws ServiceLookupException
  {
    return (IBizRegistryManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IBizRegistryManagerHome.class.getName(),
      IBizRegistryManagerHome.class,
      new Object[0]);
  }
  

  public static IEnterpriseHierarchyManagerObj getEnterpriseHierarchyManager()
    throws ServiceLookupException
  {
    return (IEnterpriseHierarchyManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IEnterpriseHierarchyManagerHome.class.getName(),
        IEnterpriseHierarchyManagerHome.class,
        new Object[0]);
  }

  public static IPartnerManagerObj getPartnerManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IPartnerManagerHome.class.getName(),
        IPartnerManagerHome.class,
        new Object[0]);
  }

  public static ILocaleManagerObj getLocaleManager() throws ServiceLookupException
  {
    return (ILocaleManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      ILocaleManagerHome.class.getName(),
      ILocaleManagerHome.class,
      new Object[0]);
  }
  

  public static BusinessEntity getBE4DUNS(String dunsNum) throws FindEntityException
  {
    IDataFilter filter= new DataFilterImpl();
    filter.addSingleFilter(null, IWhitePage.DUNS, filter.getEqualOperator(), dunsNum, false);
    Collection beList= null;
    try
    {
      beList= getBizRegistryManager().findBusinessEntitiesByWhitePage(filter);
    }
    catch (Exception ex)
    {
      throw new FindEntityException(
        "Error in finding Business Entity based on DUNS num: " + dunsNum,
        ex);
    }
    if (beList == null || beList.isEmpty())
      throw new FindEntityException("Business Entity not found based on DUNS num " + dunsNum);
    return (BusinessEntity) beList.iterator().next();
  }



  public static WhitePage getWhitePage4DUNS(String dunsNum) throws FindEntityException
  {
     return  getBE4DUNS(dunsNum).getWhitePage();
  }


  public static Partner get1stPartner4BE(BusinessEntity be) throws FindEntityException
  {
    Collection partnerList= null;
    try
    {
      partnerList= getEnterpriseHierarchyManager().getPartnersForBizEntity(new Long(be.getUId()));
    }
    catch (Exception ex)
    {
      throw new FindEntityException(
        "Error in finding Partner based on be: " + be.getDescription(),
        ex);
    }

    if (partnerList == null || partnerList.isEmpty())
      throw new FindEntityException("Partner not found based on be: " + be.getDescription());
    
    /*031023NSL Filter off those Deleted partners. The enable/disable will be checked elsewhere*/
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, Partner.UID, partnerList, false);
    /*filter.addSingleFilter(filter.getAndConnector(), Partner.STATE,
      filter.getEqualOperator(), new Short(Partner.STATE_DELETED), true);*/
    //NSL20060403 Only retrieve enabled partners
    filter.addSingleFilter(filter.getAndConnector(), Partner.STATE,
                           filter.getEqualOperator(), new Short(Partner.STATE_ENABLED), false);
    
    ArrayList partnerEntityList = new ArrayList();
    try
    {
      partnerEntityList.addAll(getPartnerManager().findPartner(filter));
    }
    catch (Exception e)
    {
      throw new FindEntityException("Error in finding Partner", e);
    }

    //in case a null is returned from findPartner()...
    if (partnerEntityList.isEmpty() || partnerEntityList.contains(null))
      throw new FindEntityException("No Enabled Partner found based on Business Entity: " + be.getDescription());

    // make sure the order is maintained.
    Collections.sort(partnerEntityList, new EntityOrderComparator(Partner.UID, partnerList));

    return (Partner)partnerEntityList.iterator().next();
    /*031023NSL End*/
    
    /*031023NSL  
    Long partnerUid =  (Long) partnerList.iterator().next();
    try
    {
     return  getPartnerManager().findPartner(partnerUid);
    }
    catch (Exception ex)
    {
      throw new FindEntityException(
        "Error in finding Partner based on Uid: " +partnerUid,
        ex);
    }
    */
  }


  public static Partner get1stPartner4DUNS(String dunsNum) throws FindEntityException
  {
    BusinessEntity be= getBE4DUNS(dunsNum);
    return get1stPartner4BE(be);
  }
  

  public static BusinessEntity getBE(Long gnodeId, String beId)
  {
    BusinessEntity be = null;
    try
    {
      String nodeIdStr = gnodeId == null? null: gnodeId.toString();
      be= getBizRegistryManager().findBusinessEntity(nodeIdStr, beId);
    }
    catch (Exception ex)
    {
      Logger.warn("Cannot find BusinessEntity for gridnode: "+gnodeId +" BE: " + beId,
            ex);
      return null;    
    }
    return be;
  }

    

  public static String getDUNS(Long gnodeId, String beId)
  {
    BusinessEntity be = getBE(gnodeId, beId);
    if(be == null)
    {
      Logger.warn("Cannot find BusinessEntity for gridnode: "+gnodeId +" BE: " + beId,
            new Exception());
        return null;   
    }    
    
    WhitePage whitePage = be.getWhitePage();
    String dunsNum= whitePage.getDUNS();
    if ((null == dunsNum) || (dunsNum.length() == 0))
      return DEFAULT_DUNS;
    return dunsNum;
  }
  
  static String DEFAULT_SUPPLY_CHAIN_CODE= "Information Technology";
  static public String getSupplyChainCode(BusinessEntity be)
  {
    WhitePage whitePage = be.getWhitePage();
    String spcCode= whitePage.getGlobalSupplyChainCode();

    if ((null == spcCode) || (spcCode.trim().length() == 0))
      return DEFAULT_DUNS;
    return spcCode;
  }

  public static String getLocation(Long gnodeId, String beId)
  {
    BusinessEntity be = getBE(gnodeId, beId);
    if(be == null)
    {
      Logger.err("Cannot find BusinessEntity for gridnode: "+gnodeId +" BE: " + beId,
            new Exception());
        return null;   
    }    
    
    WhitePage whitePage = be.getWhitePage();
    String country= whitePage.getCountry();
    return getCountryName(country);
  }

  private static String getCountryName(String alpha3Code)
  {
    if ((null == alpha3Code) || (alpha3Code.length() == 0))
      return null;
    
    try
    {
      CountryCode code = getLocaleManager().findCountryCodeByAlpha3Code(alpha3Code);
      if (code != null)
      {
        return code.getName();
      }
    }
    catch (Exception ex)
    {
      Logger.err("Cannot find Country name for code: "+alpha3Code);
    }
    return null;
  }
}
