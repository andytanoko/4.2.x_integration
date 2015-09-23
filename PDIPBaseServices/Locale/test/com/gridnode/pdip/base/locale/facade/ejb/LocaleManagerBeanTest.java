/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocaleManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.facade.ejb;

import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.base.locale.model.LanguageCode;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.*;

import java.util.Collection;

/**
 * This test case tests the finder functionality of the LocaleManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LocaleManagerBeanTest extends TestCase
{
  private ILocaleManagerObj _localeMgr;

  private static final String SG_NAME = "Singapore";
  private static final Integer SG_NUM  = new Integer(702);
  private static final String  SG_ALP2 = "SG";
  private static final String  SG_ALP3 = "SGP";

  private static final String ML_NAME = "Malay";
  private static final String ML_ALP2  = "MS";
  private static final String ML_TALP3 = "MSA";
  private static final String ML_BALP3 = "MAY";

  private static final int NUM_LANGUAGES = 160;
  private static final int NUM_COUNTRIES = 230;

  public LocaleManagerBeanTest(String name)
  {
    super(name);
  }

  public void testFinders()
  {
    try
    {
      findCountryByNumericalCodeTest();
      findCountryByNameTest();
      findCountryAlpha2CodeTest();
      findCountryAlpha3CodeTest();
      findAllCountryCodes();
      findLanguageByAlpha2CodeTest();
      findLanguageByNameTest();
      findLanguageByBAlpha3CodeTest();
      findLanguageByTAlpha3CodeTest();
      findAllLanguageCodes();
    }
    catch (Throwable ex)
    {
      Log.err("TEST", "Error in testFinders", ex);
      assertTrue("testFinders failed", false);
    }
  }

  private void findCountryByNumericalCodeTest() throws Throwable
  {
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryByNumericalCodeTest] Enter ");
    CountryCode code = _localeMgr.findCountryCodeByNumericalCode(SG_NUM);
    checkSGCountryCode(code);
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryByNumericalCodeTest] Exit ");
  }

  private void findCountryByNameTest()  throws Throwable
  {
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryByNameTest] Enter ");
    CountryCode code = _localeMgr.findCountryCodeByName(SG_NAME);
    checkSGCountryCode(code);
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryByNameTest] Exit ");
  }

  private void findCountryAlpha2CodeTest() throws Throwable
  {
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryAlpha2CodeTest] Enter ");
    CountryCode code = _localeMgr.findCountryCodeByAlpha2Code(SG_ALP2);
    checkSGCountryCode(code);
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryAlpha2CodeTest] Exit ");
  }

  private void findCountryAlpha3CodeTest() throws Throwable
  {
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryAlpha3CodeTest] Enter ");
    CountryCode code = _localeMgr.findCountryCodeByAlpha3Code(SG_ALP3);
    checkSGCountryCode(code);
    Log.debug("TEST", "[LocaleManagerBeanTest.findCountryAlpha3CodeTest] Exit ");
  }

  private void findAllCountryCodes() throws Throwable
  {
    Log.debug("TEST", "[LocaleManagerBeanTest.findAllCountryCodes] Enter ");
    Collection codes = _localeMgr.findAllCountryCodes();

    assertEquals("Num CountryCodes found is incorrect", NUM_COUNTRIES , codes.size());
    Log.debug("TEST", "[CountryCodeBeanTest.findAllCountryCodes] Exit ");
  }

  private void checkSGCountryCode(CountryCode code)
  {
    assertEquals("NumericalCode is incorrect", SG_NUM, code.getFieldValue(CountryCode.NUMERICAL_CODE));
    assertEquals("Name is incorrect", SG_NAME, code.getFieldValue(CountryCode.NAME));
    assertEquals("Alpha2Code is incorrect", SG_ALP2, code.getFieldValue(CountryCode.ALPHA_2_CODE));
    assertEquals("Alpha3Code is incorrect", SG_ALP3, code.getFieldValue(CountryCode.ALPHA_3_CODE));
  }


  private void findLanguageByAlpha2CodeTest() throws Throwable
  {
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByAlpha2CodeTest] Enter ");
    LanguageCode code = _localeMgr.findLanguageCodeByAlpha2Code(ML_ALP2);
    checkMLLanguageCode(code);
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByAlpha2CodeTest] Exit ");
  }

  private void findLanguageByNameTest()  throws Throwable
  {
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByNameTest] Enter ");
    LanguageCode code = _localeMgr.findLanguageCodeByName(ML_NAME);
    checkMLLanguageCode(code);
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByNameTest] Exit ");
  }

  private void findLanguageByBAlpha3CodeTest() throws Throwable
  {
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByBAlpha3CodeTest] Enter ");
    LanguageCode code = _localeMgr.findLanguageCodeByBAlpha3Code(ML_BALP3);
    checkMLLanguageCode(code);
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByBAlpha3CodeTest] Exit ");
  }

  private void findLanguageByTAlpha3CodeTest() throws Throwable
  {
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByTAlpha3CodeTest] Enter ");
    LanguageCode code = _localeMgr.findLanguageCodeByTAlpha3Code(ML_TALP3);
    checkMLLanguageCode(code);
    Log.debug("TEST", "[LanguageCodeBeanTest.findLanguageByTAlpha3CodeTest] Exit ");
  }

  private void findAllLanguageCodes() throws Throwable
  {
    Log.debug("TEST", "[LanguageCodeBeanTest.findAllLanguageCodes] Enter ");
    Collection codes = _localeMgr.findAllLanguageCodes();

    assertEquals("Num LanguageCodes found is incorrect", NUM_LANGUAGES , codes.size());
    Log.debug("TEST", "[LanguageCodeBeanTest.findAllLanguageCodes] Exit ");
  }

  private void checkMLLanguageCode(LanguageCode code)
  {
    assertEquals("BAlpha3Code is incorrect", ML_BALP3, code.getFieldValue(LanguageCode.B_ALPHA_3_CODE));
    assertEquals("Name is incorrect", ML_NAME, code.getFieldValue(LanguageCode.NAME));
    assertEquals("Alpha2Code is incorrect", ML_ALP2, code.getFieldValue(LanguageCode.ALPHA_2_CODE));
    assertEquals("TAlpha3Code is incorrect", ML_TALP3, code.getFieldValue(LanguageCode.T_ALPHA_3_CODE));
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(LocaleManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.debug("TEST", "[LocaleManagerBeanTest.setUp] Enter ");

      _localeMgr = (ILocaleManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).
                      getObj(ILocaleManagerHome.class.getName(),
                      ILocaleManagerHome.class,
                      new Object[0]);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "Error in Setup", ex);
      assertTrue("Error in setUp", false);
    }
    finally
    {
      Log.debug("TEST", "[LocaleManagerBeanTest.setUp] Exit ");
    }

  }

  protected void tearDown() throws java.lang.Exception
  {
  }
}