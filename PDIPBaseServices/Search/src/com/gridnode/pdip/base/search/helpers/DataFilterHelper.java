/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 08 2003    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.helpers;

import com.gridnode.pdip.framework.db.filter.SQLFilterFactory;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import com.gridnode.pdip.framework.exceptions.SystemException;

import java.util.Collection;

public class DataFilterHelper
{

  public static final String LEFT_BRACKET   = "(";
  public static final String RIGHT_BRACKET  = ")";
  public static final String WHERE          = " where ";

  private static DataFilterHelper _dataFilterHelper = null;

  private DataFilterHelper()
  {

  }

  public static DataFilterHelper instance()
  {
    if (_dataFilterHelper == null)
    {
          _dataFilterHelper = new DataFilterHelper();
    }
    return _dataFilterHelper;
  }

  public String addComparisonConditionToQuery(String query,Object connector,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate) throws SystemException
  {
    try
    {
      DataFilterImpl filterImpl     = new DataFilterImpl();
      SQLFilterFactory sqlFactory   = new SQLFilterFactory();
      String condition              = "";
      FilterConnector filterConnector;

      if(connector == null)
      {
	filterConnector = null;
      }
      else if(connector.toString().equals(SearchFilterOperator.AND_CONNECTOR))
      {
	filterConnector = sqlFactory.getAndConnector();
      }
      else
      {
	filterConnector = sqlFactory.getOrConnector();
      }

      if(isFieldValue)
      {
	 return appendConditionToQueryWithDbField(query,filterConnector,field,compOperator,value,negate);
      }
      else
      {
	  if(compOperator.equals(SearchFilterOperator.EQUAL_OPERATOR))
	  {
	    filterImpl.addSingleFilter(null,field,sqlFactory.getEqualOperator(),value,negate);
	  }

	  if(compOperator.equals(SearchFilterOperator.GREATER_THAN_OPERATOR))
	  {
	    filterImpl.addSingleFilter(null,field,sqlFactory.getGreaterOperator(),value,negate);
	  }

	  if(compOperator.equals(SearchFilterOperator.GREATER_THAN_EQUALTO_OPERATOR))
	  {
	    filterImpl.addSingleFilter(null,field,sqlFactory.getGreaterOrEqualOperator(),value,negate);
	  }

	  if(compOperator.equals(SearchFilterOperator.LESS_THAN_OPERATOR))
	  {
	    filterImpl.addSingleFilter(null,field,sqlFactory.getLessOperator(),value,negate);
	  }

	  if(compOperator.equals(SearchFilterOperator.LESS_THAN_EQUALTO_OPERATOR))
	  {
	    filterImpl.addSingleFilter(null,field,sqlFactory.getLessOrEqualOperator(),value,negate);
	  }

	condition = filterImpl.applySyntax();
	return appendConditionToQuery(query,filterConnector,condition);
      }
    }
      catch(Exception e)
      {
	Logger.err( " Exception in DataFilterHelper.addComparisonConditionToQuery() : ", e);
	throw new SystemException(" Exception in addComparisonConditionToQuery ",e);
      }
  }

    private String appendConditionToQuery(String query,FilterConnector filterConnector,String condition) throws SystemException
  {
    try
    {
      String sqlQuery  = "";
      String connector  = "";

      if(filterConnector != null)
        connector  = filterConnector.getName();

      if(query.toLowerCase().indexOf("where") > -1)
	sqlQuery = query + " " + connector + " " + LEFT_BRACKET + " " + condition.toString() + " " + RIGHT_BRACKET;
      else
	sqlQuery = query + " " + WHERE + " " + LEFT_BRACKET + " " + condition.toString() + " " + RIGHT_BRACKET;

	return sqlQuery;
    }
    catch(Exception e)
    {
      Logger.err( " Exception in DataFilterHelper.appendConditionToQuery() : ", e);
      throw new SystemException(" Exception in appendConditionToQuery ",e);
    }
  }

  public String appendConditionToQueryWithDbField(String query,FilterConnector filterConnector,Object field,Object compOperator,Object value,boolean negate) throws SystemException
  {
    try
    {
      SQLFilterFactory sqlFactory   = new SQLFilterFactory();


      String sqlQuery   = "";
      String connector  = "";
      String negation   = "";
      if(negate)
	negation  = sqlFactory.getNotOperator().getName();

      if(filterConnector != null)
        connector  = filterConnector.getName();

      if(query.toLowerCase().indexOf("where") > -1)
	sqlQuery = query + " " + connector + " " + negation + " " +LEFT_BRACKET + " " + field + compOperator + value + " " + RIGHT_BRACKET;
      else
	sqlQuery = query + " " + WHERE + " " + negation + " " + LEFT_BRACKET + " " + field + compOperator + value + " " + RIGHT_BRACKET;

	return sqlQuery;
    }
    catch(Exception e)
    {
      Logger.err( " Exception in DataFilterHelper.appendConditionToQueryWithDbField() : ", e);
      throw new SystemException(" Exception in appendConditionToQueryWithDbField ",e);
    }
  }


  public String addRangeConditionToQuery(String query,Object connector,Object field,Object lowValue,Object highValue,boolean negate) throws SystemException
  {
    try
    {
      DataFilterImpl filterImpl     = new DataFilterImpl();
      SQLFilterFactory sqlFactory   = new SQLFilterFactory();
      String condition              = "";
      FilterConnector filterConnector;

      if(connector == null)
      {
	filterConnector = null;
      }
      else if(connector.toString().equals(SearchFilterOperator.AND_CONNECTOR))
      {
	filterConnector = sqlFactory.getAndConnector();
      }
      else
      {
	filterConnector = sqlFactory.getOrConnector();
      }

      return translateRangeConditionToQuery(query,filterConnector,field,lowValue,highValue,negate);

    }
    catch(Exception e)
    {
      Logger.err( " Exception in DataFilterHelper.addComparisonConditionToQuery() : ", e);
      throw new SystemException(" Exception in addComparisonConditionToQuery ",e);
    }

  }

  private String translateRangeConditionToQuery(String query,FilterConnector filterConnector,Object field,Object lowValue,Object highValue,boolean negate) throws SystemException
  {
     String sqlQuery = "";
      try
      {
	SQLFilterFactory sqlFactory   = new SQLFilterFactory();
	String negation   = "";
        String connector  = "";

	if(negate)
	  negation  = sqlFactory.getNotOperator().getName();

        if(filterConnector != null)
          connector  = filterConnector.getName();

	String condition  = field + " " + sqlFactory.getBetweenOperator().getName() + " " + lowValue + " " + sqlFactory.getAndConnector().getName() + " " + highValue;

	if(query.toLowerCase().indexOf("where") > -1)
	  sqlQuery = query + " " + connector + " " + negation  + " " + LEFT_BRACKET + " " + condition + " " + RIGHT_BRACKET;
	else
	  sqlQuery = query + " " + WHERE + " " + negation + " " + LEFT_BRACKET + " " + condition + " " + RIGHT_BRACKET;

      }
      catch(Exception e)
      {
	Logger.err( " Exception in DataFilterHelper.appendRangeConditionToQuery() : ", e);
	throw new SystemException(" Exception in appendRangeConditionToQuery ",e);
      }
      return sqlQuery;

  }

  public String addInConditionToQuery(String query,Object connector,Object field,Collection valueList,boolean negate) throws SystemException
  {
    try
    {
      DataFilterImpl filterImpl     = new DataFilterImpl();
      SQLFilterFactory sqlFactory   = new SQLFilterFactory();
      String condition              = "";
      FilterConnector filterConnector;

      if(connector == null)
      {
	filterConnector = null;
      }
      else if(connector.toString().equals(SearchFilterOperator.AND_CONNECTOR))
      {
	filterConnector = sqlFactory.getAndConnector();
      }
      else
      {
	filterConnector = sqlFactory.getOrConnector();
      }


      return translateInConditionToQuery(query,filterConnector,field,valueList,negate);

    }
    catch(Exception e)
    {
      Logger.err( " Exception in DataFilterHelper.addInConditionToQuery() : ", e);
      throw new SystemException(" Exception in addInConditionToQuery ",e);
    }
  }

  private String translateInConditionToQuery(String query,FilterConnector filterConnector,Object field,Collection valueList,boolean negate) throws SystemException
  {
     String sqlQuery = "";
      try
      {
	SQLFilterFactory sqlFactory   = new SQLFilterFactory();
	String negation   = "";
        String connector  = "";

	String strList    = valueList.toString();

	strList = strList.substring(1,strList.length()-1);

	if(negate)
	  negation  = sqlFactory.getNotOperator().getName();

        if(filterConnector != null)
          connector  = filterConnector.getName();

	String condition  = field + " " + sqlFactory.getInOperator().getName() + " " + LEFT_BRACKET  + strList + RIGHT_BRACKET;

	if(query.toLowerCase().indexOf("where") > -1)
	  sqlQuery = query + " " + connector + " " + negation  + " " + LEFT_BRACKET + " " + condition + " " + RIGHT_BRACKET;
	else
	  sqlQuery = query + " " + WHERE + " " + negation + " " + LEFT_BRACKET + " " + condition + " " + RIGHT_BRACKET;

      }
      catch(Exception e)
      {
	Logger.err( " Exception in DataFilterHelper.translateInConditionToQuery() : ", e);
	throw new SystemException(" Exception in translateInConditionToQuery ",e);
      }
      return sqlQuery;
  }
}