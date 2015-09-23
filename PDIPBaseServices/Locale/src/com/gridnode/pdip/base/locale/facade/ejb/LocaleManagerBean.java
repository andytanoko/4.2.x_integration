/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CountryCodeBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.facade.ejb;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.locale.helpers.CountryCodeEntityHandler;
import com.gridnode.pdip.base.locale.helpers.LanguageCodeEntityHandler;
import com.gridnode.pdip.base.locale.helpers.Logger;
import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.base.locale.model.LanguageCode;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * EntityBean for CountryCode. No creation or update service provided.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LocaleManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6912351005606253985L;
	private SessionContext _ctx;

  public LocaleManagerBean()
  {
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

  public void setSessionContext(SessionContext ctx)
  {
    _ctx = ctx;
  }

  // **************************** Methods ********************************

  // ********************** CountryCode ****************************

  /**
   * Find CountryCode by NumericalCode.
   *
   * @param numericalCode The numerical code of the CountryCode to find.
   *
   * @return CountryCode found, or <b>null</b> if none found.
   */
  public CountryCode findCountryCodeByNumericalCode(Integer numericalCode)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findCountryCodeByNumericalCode";
    Object[] params     = new Object[] {numericalCode};

    CountryCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, CountryCode.NUMERICAL_CODE, filter.getEqualOperator(),
        numericalCode, false);
      Collection results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (CountryCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find all CountryCode(s).
   *
   * @return Collection of CountryCode(s) found.
   */
  public Collection findAllCountryCodes()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findAllCountryCodes";
    Object[] params     = new Object[] {};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(null);
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
   * Find the CountryCode by Name of country or area.
   *
   * @param name The name to search for.
   * @return CountryCode found with the specified name.
   */
  public CountryCode findCountryCodeByName(String name)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findCountryCodeByName";
    Object[] params     = new Object[] {name};

    CountryCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, CountryCode.NAME, filter.getEqualOperator(),
        name, false);
      Collection results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (CountryCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }


  /**
   * Find the CountryCode by Alpha2Code.
   *
   * @param alpha2Code The alpha-2 code to search for.
   * @return CountryCode found with the specified Alpha2Code.
   */
  public CountryCode findCountryCodeByAlpha2Code(String alpha2Code)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findCountryCodeByAlpha2Code";
    Object[] params     = new Object[] {alpha2Code};

    CountryCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, CountryCode.ALPHA_2_CODE, filter.getEqualOperator(),
        alpha2Code, false);
      Collection results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (CountryCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find the CountryCode by Alpha3Code.
   *
   * @param alpha3Code The alpha-3 code to search for.
   * @return CountryCode found with the specified Alpha3Code.
   */
  public CountryCode findCountryCodeByAlpha3Code(String alpha3Code)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findCountryCodeByAlpha3Code";
    Object[] params     = new Object[] {alpha3Code};

    CountryCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, CountryCode.ALPHA_3_CODE, filter.getEqualOperator(),
        alpha3Code, false);
      Collection results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (CountryCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find all CountryCode(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of CountryCode(s) found, or empty collection if none.
   */
  public Collection findCountryCodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findCountryCodesByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = CountryCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
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


  // ********************* Language Code ***************************
  /**
   * Find the LanguageCode by the Alpha2Code.
   *
   * @param alpha2Code The Alpha2Code of the Language to find.
   *
   * @return LanguageCode found with the specified Alpha2Code.
   */
  public LanguageCode findLanguageCodeByAlpha2Code(String alpha2Code)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLanguageFacadeLogger();
    String methodName   = "findLanguageCodeByAlpha2Code";
    Object[] params     = new Object[] {alpha2Code};

    LanguageCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, LanguageCode.ALPHA_2_CODE, filter.getEqualOperator(),
        alpha2Code, false);
      Collection results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (LanguageCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find all LanguageCode(s).
   *
   * @return Collection of LanguageCode(s) found.
   */
  public Collection findAllLanguageCodes()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLanguageFacadeLogger();
    String methodName   = "findAllLanguageCodes";
    Object[] params     = new Object[] {};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(null);
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
   * Find the LanguageCode by Name of language.
   *
   * @param name The name to search for.
   * @return LanguageCode found with the specified name.
   */
  public LanguageCode findLanguageCodeByName(String name)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLanguageFacadeLogger();
    String methodName   = "findLanguageCodeByName";
    Object[] params     = new Object[] {name};

    LanguageCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, LanguageCode.NAME, filter.getEqualOperator(),
        name, false);
      Collection results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (LanguageCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find the LanguageCode by BAlpha3Code.
   *
   * @param bAlpha3Code The B Alpha-3 code to search for.
   * @return LanguageCode found with the specified BAlpha3Code.
   */
  public LanguageCode findLanguageCodeByBAlpha3Code(String bAlpha3Code)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findLanguageCodeByBAlpha3Code";
    Object[] params     = new Object[] {bAlpha3Code};

    LanguageCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, LanguageCode.B_ALPHA_3_CODE, filter.getEqualOperator(),
        bAlpha3Code, false);
      Collection results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (LanguageCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find the LanguageCode by TAlpha3Code.
   *
   * @param tAlpha3Code The T alpha-3 code to search for.
   * @return LanguageCode found with the specified TAlpha3Code.
   */
  public LanguageCode findLanguageCodeByTAlpha3Code(String tAlpha3Code)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCountryFacadeLogger();
    String methodName   = "findLanguageCodeByTAlpha3Code";
    Object[] params     = new Object[] {tAlpha3Code};

    LanguageCode code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, LanguageCode.T_ALPHA_3_CODE, filter.getEqualOperator(),
        tAlpha3Code, false);
      Collection results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (LanguageCode)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find all LanguageCode(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of LanguageCode(s) found, or empty collection if none.
   */
  public Collection findLanguageCodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getLanguageFacadeLogger();
    String methodName   = "findLanguageCodesByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = LanguageCodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
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

}