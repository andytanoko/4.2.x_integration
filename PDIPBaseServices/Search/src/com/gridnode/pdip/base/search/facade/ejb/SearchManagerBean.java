/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.facade.ejb;

import com.gridnode.pdip.base.search.helpers.FrameworkDbHelper;
import com.gridnode.pdip.base.search.helpers.QueryHandler;
import com.gridnode.pdip.base.search.helpers.DataFilterHelper;
import com.gridnode.pdip.base.search.helpers.Logger;

import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.SQLFilterFactory;

import com.gridnode.pdip.framework.exceptions.SystemException;

import java.util.Collection;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.CreateException;

/**
 * This bean manages the Search.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */

public class SearchManagerBean implements SessionBean
{
  transient private SessionContext _sessionCtx = null;
  private String query;


  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
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

 /** Method for adding comparison condition(=,>= etc) to query
  *
  * @param connector the connector for multiple conditions
  * @param field the table column forming part of the filter
  * @param compOperator the comparision operator
  * @param value the value in the condition
  * @param isFieldValue indicted whether the value is an exact value or column name
  * @param negate true indicate NOT condition has to be applied to the filter condition
  *
  * @return query String
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

  public String addComparisonConditionToQuery(Object connector,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate) throws SystemException
  {
    try
    {
      String query  = DataFilterHelper.instance().addComparisonConditionToQuery(getQuery(),connector,field,compOperator,value,isFieldValue,negate);
      setQuery(query);
      return query;

    }
    catch(Exception e)
    {
      Logger.err( " Exception in SearchManagerBean.addComparisonConditionToQuery() : ", e);
      throw new SystemException(" Exception in addComparisonConditionToQuery ",e);
    }
  }

 /** Method for adding between condition to query
  *
  * @param connector the connector for multiple conditions
  * @param field the table column forming part of the filter
  * @param lowValue the lower range value
  * @param highValue the higher range value
  * @param negate true indicate NOT condition has to be applied to the filter condition
  *
  * @return query String
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

  public String addRangeConditionToQuery(Object connector,Object field,Object lowValue,Object highValue,boolean negate) throws SystemException
  {
    try
    {
      String query  = DataFilterHelper.instance().addRangeConditionToQuery(getQuery(),connector,field,lowValue,highValue,negate);
      setQuery(query);
      return query;
    }
    catch(Exception e)
    {
      Logger.err( " Exception in SearchManagerBean.addComparisonConditionToQuery() : ", e);
      throw new SystemException(" Exception in addComparisonConditionToQuery ",e);
    }
  }

 /** Method for adding the condition to query for a list of values
  *
  * @param connector the connector for multiple conditions
  * @param field the table column forming part of the filter
  * @param valueList the list of values that are to be queried on
  * @param negate true indicate NOT condition has to be applied to the filter condition
  *
  * @return query String
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public String addInConditionToQuery(Object connector,Object field,Collection valueList,boolean negate) throws SystemException
  {
    try
    {
      String query  = DataFilterHelper.instance().addInConditionToQuery(getQuery(),connector,field,valueList,negate);
      setQuery(query);
      return query;
    }
    catch(Exception e)
    {
      Logger.err( " Exception in SearchManagerBean.addInConditionToQuery() : ", e);
      throw new SystemException(" Exception in addInConditionToQuery ",e);
    }
  }

 /** Method for generating simple query for the given table name
  *
  * @param tableName table name
  *
  * @return query String
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public String getSelectQueryForTable(String tableName) throws SystemException
  {
    try
    {
      String query  = QueryHandler.instance().getSelectQueryForTable(tableName);
      setQuery(query);
      return query;

    }
    catch(Exception e)
    {
      Logger.err( " Exception in SearchManagerBean.getQueryForTable() : ", e);
      throw new SystemException(" Exception in finding table "+tableName,e);
    }
  }

 /** Method for obtaining the result set of the query
  *
  * @param query query string
  *
  * @return collection of values
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */



   public Collection getResultFromQuery(String query) throws SystemException
   {
      try
      {
	return QueryHandler.instance().getResultFromQuery(query);
      }
      catch(Exception e)
      {
	Logger.err( " Exception in SearchManagerBean.getResultFromQuery() : ", e);
	throw new SystemException(" Exception in SearchManagerBean.getResultFromQuery() while executing query "+query,e);
      }
   }

 /** Method for obtaining the result set of the query(which is a member of the stateful bean)
  *
  *
  * @return collection of values
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

   public Collection getResultFromQuery() throws SystemException
   {
      try
      {
	return QueryHandler.instance().getResultFromQuery(getQuery());
      }
      catch(Exception e)
      {
	Logger.err( " Exception in SearchManagerBean.getResultFromQuery() : ", e);
	throw new SystemException(" Exception in SearchManagerBean.getResultFromQuery() while executing query "+getQuery(),e);
      }
   }

 /** Method for obtaining the values in the tablename.column name
  *  along with the filter condition
  *
  *
  * @return collection of values for the given column name
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

    public Collection getValuesForFieldName(String tableName,String fieldName,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate,boolean distinct) throws SystemException
    {
      try
      {
	return QueryHandler.instance().getValuesForFieldName(tableName,fieldName,field,compOperator,value,isFieldValue,negate,distinct);
      }
      catch(Exception e)
      {
	Logger.err( " Exception in SearchManagerBean.getValuesForFieldName() : ", e);
	throw new SystemException(" Exception in SearchManagerBean.getValuesForFieldName() : "+tableName+"."+fieldName,e);
      }
   }


  public String getQuery()
  {
    return query;
  }
  public void setQuery(String query)
  {
    this.query = query;
  }


}