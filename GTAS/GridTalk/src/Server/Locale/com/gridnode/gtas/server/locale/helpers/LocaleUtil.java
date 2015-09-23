/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocaleUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.gridnode.gtas.model.locale.LocaleEntityFieldID;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerHome;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerObj;
import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.base.locale.model.LanguageCode;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class provides helper methods for use in the classes
 * of the Locale module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class LocaleUtil
{
  public static final EntityOrderComparator COUNTRY_COMPARATOR =
    new EntityOrderComparator(CountryCode.NAME);

  public static final EntityOrderComparator LANGUAGE_COMPARATOR =
    new EntityOrderComparator(LanguageCode.NAME);

  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the LocaleManagerBean.
   *
   * @return The EJBObject to the LocaleManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I5
   */
  public static ILocaleManagerObj getLocaleManager()
    throws ServiceLookupException
  {
    return (ILocaleManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ILocaleManagerHome.class.getName(),
      ILocaleManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  public static Collection findCountryCodes(IDataFilter filter)
    throws Exception
  {
    ArrayList results = new ArrayList(getLocaleManager().findCountryCodesByFilter(filter));

    Collections.sort(results, COUNTRY_COMPARATOR);

    return results;
  }

  public static Collection findLanguageCodes(IDataFilter filter)
    throws Exception
  {
    ArrayList results = new ArrayList(getLocaleManager().findLanguageCodesByFilter(filter));

    Collections.sort(results, LANGUAGE_COMPARATOR);

    return results;
  }

  public static CountryCode findCountryCodeByAlpha3Code(String alpha3Code)
    throws Exception
  {
    CountryCode code = getLocaleManager().findCountryCodeByAlpha3Code(alpha3Code);

    if (code == null)
      throw new ApplicationException("Bad Country Alpha-3 Code: "+alpha3Code);

    return code;
  }

  public static LanguageCode findLangCodeByAlpha2Code(String alpha2Code)
    throws Exception
  {
    LanguageCode code = getLocaleManager().findLanguageCodeByAlpha2Code(alpha2Code);

    if (code == null)
      throw new ApplicationException("Bad Language Alpha-2 Code: "+alpha2Code);

    return code;
  }

  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
//  public static boolean isEmpty(String val)
//  {
//    return val==null || val.trim().length() == 0;
//  }

  // ******************** Conversion Helpers ******************************

  /**
   * Convert a collection of CountryCode(s) to Map objects.
   *
   * @param countryList The collection of CountryCode(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of CountryCode(s).
   *
   * @since 2.0 I5
   */
  public static Collection convertCountryCodesToMapObjects(Collection countryList)
  {
    return CountryCode.convertEntitiesToMap(
             (CountryCode[])countryList.toArray(new CountryCode[countryList.size()]),
             LocaleEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of LanguageCode(s) to Map objects.
   *
   * @param languageList The collection of LanguageCode(s) to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of LanguageCode(s).
   *
   * @since 2.0 I5
   */
  public static Collection convertLangCodesToMapObjects(Collection langList)
  {
    return LanguageCode.convertEntitiesToMap(
             (LanguageCode[])langList.toArray(new LanguageCode[langList.size()]),
             LocaleEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an CountryCode to Map object.
   *
   * @param countryCode The CountryCode to convert.
   * @return A Map object converted from the specified CountryCode.
   *
   * @since 2.0 I5
   */
  public static Map convertCountryCodeToMap(CountryCode countryCode)
  {
    return CountryCode.convertToMap(
             countryCode,
             LocaleEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an LanguageCode to Map object.
   *
   * @param languageCode The CountryCode to convert.
   * @return A Map object converted from the specified LanguageCode.
   *
   * @since 2.0 I5
   */
  public static Map convertLangCodeToMap(LanguageCode languageCode)
  {
    return LanguageCode.convertToMap(
             languageCode,
             LocaleEntityFieldID.getEntityFieldID(),
             null);
  }
}