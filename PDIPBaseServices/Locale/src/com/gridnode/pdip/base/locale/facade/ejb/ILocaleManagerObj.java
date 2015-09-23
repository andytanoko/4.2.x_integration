/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICountryCodeObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.facade.ejb;

import com.gridnode.pdip.base.locale.model.LanguageCode;
import com.gridnode.pdip.base.locale.model.CountryCode;

import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 * EJBObject for the CountryCodeBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ILocaleManagerObj extends EJBObject
{
  // ********************** CountryCode ****************************

  /**
   * Find CountryCode by NumericalCode.
   *
   * @param numericalCode The numerical code of the CountryCode to find.
   *
   * @return CountryCode found, or <b>null</b> if none found.
   */
  public CountryCode findCountryCodeByNumericalCode(Integer numericalCode)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all CountryCode(s).
   *
   * @return Collection of CountryCode(s) found.
   */
  public Collection findAllCountryCodes()
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the CountryCode by Name of country or area.
   *
   * @param name The name to search for.
   * @return CountryCode found with the specified name.
   */
  public CountryCode findCountryCodeByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the CountryCode by Alpha2Code.
   *
   * @param alpha2Code The alpha-2 code to search for.
   * @return CountryCode found with the specified Alpha2Code.
   */
  public CountryCode findCountryCodeByAlpha2Code(String alpha2Code)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the CountryCode by Alpha3Code.
   *
   * @param alpha3Code The alpha-3 code to search for.
   * @return CountryCode found with the specified Alpha3Code.
   */
  public CountryCode findCountryCodeByAlpha3Code(String alpha3Code)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all CountryCode(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of CountryCode(s) found, or empty collection if none.
   */
  public Collection findCountryCodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  // ********************* Language Code ***************************
  /**
   * Find the LanguageCode by the Alpha2Code.
   *
   * @param alpha2Code The Alpha2Code of the Language to find.
   *
   * @return LanguageCode found with the specified Alpha2Code.
   */
  public LanguageCode findLanguageCodeByAlpha2Code(String alpha2Code)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all LanguageCode(s).
   *
   * @return Collection of LanguageCode(s) found.
   */
  public Collection findAllLanguageCodes()
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the LanguageCode by Name of language.
   *
   * @param name The name to search for.
   * @return LanguageCode found with the specified name.
   */
  public LanguageCode findLanguageCodeByName(String name)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the LanguageCode by BAlpha3Code.
   *
   * @param bAlpha3Code The B Alpha-3 code to search for.
   * @return LanguageCode found with the specified BAlpha3Code.
   */
  public LanguageCode findLanguageCodeByBAlpha3Code(String bAlpha3Code)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the LanguageCode by TAlpha3Code.
   *
   * @param tAlpha3Code The T alpha-3 code to search for.
   * @return LanguageCode found with the specified TAlpha3Code.
   */
  public LanguageCode findLanguageCodeByTAlpha3Code(String tAlpha3Code)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find all LanguageCode(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of LanguageCode(s) found, or empty collection if none.
   */
  public Collection findLanguageCodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

}