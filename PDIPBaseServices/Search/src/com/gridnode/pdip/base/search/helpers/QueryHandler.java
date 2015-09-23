/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: QueryHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 06 2003    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.helpers;

import com.gridnode.pdip.framework.db.filter.SQLFilterFactory;

import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.dao.DataSourceSelector;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import com.gridnode.pdip.framework.db.filter.SQLFilterFactory;

import com.gridnode.pdip.framework.exceptions.SystemException;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class QueryHandler
{
    private static final String  DISTINCT = " distinct ";

    private static QueryHandler _queryHandler = null;

    private QueryHandler()
    {

    }

    static public QueryHandler instance()
    {
      if (_queryHandler == null)
      {
	    _queryHandler = new QueryHandler();
      }
      return _queryHandler;
    }

 /** Method for obtaining the values from database of an entity's field name
  *
  * @param entityName Entity Name
  * @param fieldName Field Name for which the values have to be populated
  *
  * @return the collection of values fetched from Database
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

    public Collection getValuesForFieldName(String tableName,String fieldName) throws SystemException
    {
      Collection coll = new ArrayList();
      try{

	  Connection conn           = DataSourceSelector.getInstance().getDataSource(DataSourceSelector.USERDB).getConnection();
	  SQLFilterFactory sqlFilter = new SQLFilterFactory();
	  String fieldNames[] = {fieldName};
	  String sqlQuery = sqlFilter.getSelectSyntax(tableName,fieldNames);
	  Statement stmt   = conn.createStatement();
	  ResultSet rs     = stmt.executeQuery(sqlQuery);

	  while(rs.next())
	  {
	    coll.add(rs.getString(1));
	  }

      }
      catch(Exception e)
      {
	  Logger.err(" Error in QueryHandler.getValuesForFieldName ",e);
	  throw new SystemException(" Exception in getValuesForFieldName "+tableName+"."+fieldName,e);

      }

      return coll;
    }

    public Collection getValuesForFieldName(String tableName,String fieldName,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate,boolean distinct) throws SystemException
    {
      Collection coll = new ArrayList();
      try{

	  Connection conn           = DataSourceSelector.getInstance().getDataSource(DataSourceSelector.USERDB).getConnection();
	  SQLFilterFactory sqlFilter = new SQLFilterFactory();

	  if(distinct)
	     fieldName    = DISTINCT + " " + fieldName;
	  String fieldNames[] = {fieldName};

	  String sqlQuery = sqlFilter.getSelectSyntax(tableName,fieldNames);
	  sqlQuery  = DataFilterHelper.instance().addComparisonConditionToQuery(sqlQuery,null,field,compOperator,value,isFieldValue,negate);
	  Statement stmt   = conn.createStatement();
	  ResultSet rs     = stmt.executeQuery(sqlQuery);

	  while(rs.next())
	  {
	    coll.add(rs.getString(1));
	  }

      }
      catch(Exception e)
      {
	  Logger.err(" Error in QueryHandler.getValuesForFieldName ",e);
	  throw new SystemException(" Exception in getValuesForFieldName "+tableName+"."+fieldName,e);

      }

      return coll;
    }



    public String getSelectQueryForTable(String tableName) throws SystemException
    {
      try
      {
	SQLFilterFactory sqlFactory = new SQLFilterFactory();
	return sqlFactory.getSelectSyntax(tableName,null);
      }
      catch(Exception e)
      {
	Logger.err( " Exception in QueryHandler.getQueryForTable() : ", e);
	throw new SystemException(" Exception in generating query for table "+tableName,e);
      }
    }

    public Collection getResultFromQuery(String query) throws SystemException
    {
      Collection coll = new ArrayList();
      try{

	  HashMap hRow     = new HashMap();
	  Connection conn           = DataSourceSelector.getInstance().getDataSource(DataSourceSelector.USERDB).getConnection();
	  SQLFilterFactory sqlFilter = new SQLFilterFactory();
	  Statement stmt   = conn.createStatement();
	  ResultSet rs     = stmt.executeQuery(query);

	  ResultSetMetaData rsMetaData  = rs.getMetaData();

          while(rs.next())
	  {
	    hRow  = new HashMap();
	    for(int i=1;i<rsMetaData.getColumnCount();i++)
	    {
           	hRow.put(rsMetaData.getColumnName(i),rs.getString(i));
	    }
	    coll.add(hRow);

	  }

      }
      catch(Exception e)
      {
	  Logger.err(" Error in QueryHandler.getResultFromQuery ",e);
	  throw new SystemException(" Exception in getResultFromQuery "+query,e);

      }

      return coll;
    }

}