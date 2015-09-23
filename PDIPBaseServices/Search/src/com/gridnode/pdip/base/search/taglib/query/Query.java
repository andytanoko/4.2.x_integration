/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Query.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.query;

import com.gridnode.pdip.base.search.helpers.Logger;

import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerObj;
import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerHome;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import java.util.Collection;


public class Query extends BodyTagSupport
{
  private PageContext pc = null;

  private String tableName;
  private boolean distinct;
  private String query;

  private ISearchManagerObj _searchObj;
  private ISearchManagerHome _searchHome;


  public int doStartTag() throws JspTagException
  {
    try{
      lookUpSearchManagerBean();
      String query = _searchObj.getSelectQueryForTable(tableName);
      System.out.println(" In doStartTag == Query.java" + query);
      setQuery(query);
      return EVAL_BODY_INCLUDE;
    }
     catch (Exception e)
     {
        throw new JspTagException(e.getMessage());
     }
  }

  public int doEndTag() throws JspTagException
  {
    try{
      Collection colResult = _searchObj.getResultFromQuery(query);
      pageContext.setAttribute("result",colResult,pageContext.PAGE_SCOPE);
      System.out.println(" In doEnd == Query.java" + colResult);
      return EVAL_BODY_INCLUDE;
    }
     catch (Exception e)
     {
        throw new JspTagException(e.getMessage());
     }
  }


  public void setTableName(String tableName)
  {
    this.tableName  = tableName;
  }

  public void setDistinct(boolean distinct)
  {
    this.distinct  = distinct;
  }

  public void setQuery(String query)
  {
    this.query  = query;
  }

  public String getQuery()
  {
    return query;
  }

  public void lookUpSearchManagerBean() throws SystemException
  {
      try
      {
	_searchHome = (ISearchManagerHome)ServiceLookup.getInstance(
					ServiceLookup.CLIENT_CONTEXT).getHome(
		      ISearchManagerHome.class);
        _searchObj = _searchHome.create();

      }
      catch (Exception ex)
      {
	Logger.err(" Exception in Query.lookUpSearchManagerBean() : ", ex);
	throw new SystemException(" Exception in lookup of SearchManagerBean ",ex);
      }
  }


}