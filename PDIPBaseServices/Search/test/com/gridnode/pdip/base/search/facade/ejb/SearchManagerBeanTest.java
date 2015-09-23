/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.facade.ejb;

import com.gridnode.pdip.base.search.helpers.SearchFilterOperator;

import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerObj;
import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerHome;

import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import com.gridnode.pdip.framework.log.Log;

import java.util.ArrayList;

import junit.framework.*;

public class SearchManagerBeanTest extends TestCase
{
  public static final String TEST_ENTITY          = "com.gridnode.pdip.base.time.entities.model.iCalString";
  public static final String TEST_TABLE           = "ADDRESS";

  public static final String TEST_FIELD           = "FIRSTNAME";
  public static final String TEST_EQUAL_OPERATOR  = SearchFilterOperator.EQUAL_OPERATOR;
  public static final String TEST_VALUE           = "ADDRESS";

  public static final String TEST_FIELD1           = "UID";
  public static final String TEST_EQUAL_OPERATOR1  = SearchFilterOperator.GREATER_THAN_OPERATOR;
  public static final String TEST_VALUE1           = "22";

  public static final String TEST_RANGE_FIELD      = "UID";
  public static final String TEST_LOW_VALUE        = "1";
  public static final String TEST_HIGH_VALUE       = "99999999";

  public static final String TEST_OR_CONNECTOR   = SearchFilterOperator.OR_CONNECTOR;

  public static final String TEST_IN_FIELD      = "UID";
  public static final ArrayList testList         = new ArrayList();





  private ISearchManagerObj _searchObj;
  private ISearchManagerHome _searchHome;

  static final String LogCat = "TEST";

  public SearchManagerBeanTest(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      lookUpSearchManagerBean();
    }
    catch(Exception e)
    {

    }
  }

  public void testComparisonConditionQuery()
  {
    try
    {
      String query      = _searchObj.getSelectQueryForTable(TEST_TABLE);

      Log.debug(LogCat," QUERY GENERATED1 " + query);
      query = _searchObj.addComparisonConditionToQuery(null,TEST_FIELD,TEST_EQUAL_OPERATOR,TEST_VALUE,false,false);
      Log.debug(LogCat," QUERY GENERATED2 " + query);
      Log.debug(LogCat," QUERY GENERATED3 " + _searchObj.addComparisonConditionToQuery(TEST_OR_CONNECTOR,TEST_FIELD1,TEST_EQUAL_OPERATOR1,TEST_VALUE1,false,true));

    }
    catch(Exception e)
    {
      Log.err(LogCat, "[SearchManagerBeanTest.testComparisonConditionQuery]", e);
      assertTrue("[SearchManagerBeanTest.testComparisonConditionQuery] Error",false);
    }
  }

  public void testRangeConditionQuery()
  {
    try
    {
      String query      = _searchObj.getSelectQueryForTable(TEST_TABLE);

      Log.debug(LogCat," RANGE QUERY GENERATED1 " + query);
      query = _searchObj.addRangeConditionToQuery(null,TEST_RANGE_FIELD,TEST_LOW_VALUE,TEST_HIGH_VALUE,true);
      Log.debug(LogCat," RANGE QUERY GENERATED2 " + query);

    }
    catch(Exception e)
    {
      Log.err(LogCat, "[SearchManagerBeanTest.testRangeConditionQuery]", e);
      assertTrue("[SearchManagerBeanTest.testRangeConditionQuery] Error",false);
    }
  }

  public void testInConditionQuery()
  {
    try
    {

      String query      = _searchObj.getSelectQueryForTable(TEST_TABLE);
      Log.debug(LogCat," RANGE QUERY GENERATED1 " + query);

      testList.add("2");
      testList.add("6");
      testList.add("8");

      query = _searchObj.addInConditionToQuery(null,TEST_IN_FIELD,testList,true);
      Log.debug(LogCat," RANGE QUERY GENERATED2 " + query);

    }
    catch(Exception e)
    {
      Log.err(LogCat, "[SearchManagerBeanTest.testInConditionQuery]", e);
      assertTrue("[SearchManagerBeanTest.testInConditionQuery] Error",false);
    }
  }

  public void testQueryResult()
  {
    try
    {

      String query      = _searchObj.getSelectQueryForTable(TEST_TABLE);
      Log.debug(LogCat," RANGE QUERY GENERATED1 " + query);

      testList.add("2");
      testList.add("6");
      testList.add("8");

      query = _searchObj.addInConditionToQuery(null,TEST_IN_FIELD,testList,false);
      Log.debug(LogCat," RANGE QUERY GENERATED2 " + query);

      Log.debug(LogCat," RANGE QUERY GENERATED2 " + _searchObj.getResultFromQuery(query));



    }
    catch(Exception e)
    {
      Log.err(LogCat, "[SearchManagerBeanTest.testQueryResult]", e);
      assertTrue("[SearchManagerBeanTest.testQueryResult] Error",false);
    }
  }


  public void lookUpSearchManagerBean() throws SystemException
  {
      try
      {
	_searchHome = (ISearchManagerHome)ServiceLookup.getInstance(
					ServiceLookup.CLIENT_CONTEXT).getHome(
		      ISearchManagerHome.class);
        _searchObj = _searchHome.create();
	assertNotNull("ISearchManagerHome is null", _searchHome);
        assertNotNull("ISearchManagerObj is null", _searchObj);

      }
      catch (Exception ex)
      {
	Log.log(LogCat," Exception in SearchManagerBeanTest.lookUpSearchManagerBean() : ", ex);
	throw new SystemException(" Exception in lookup of SearchManagerBean ",ex);
      }
  }



  public static Test suite()
  {
    return new TestSuite(SearchManagerBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

}