/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RangeCondition.java
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

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;


public class RangeCondition extends TagSupport
{
  private Object  connector;
  private Object  field;
  private Object  lowValue;
  private Object  highValue;
  private boolean negate;

  private ISearchManagerObj _searchObj;
  private ISearchManagerHome _searchHome;


  public int doStartTag() throws JspTagException
  {
    try{

       Query queryHandler = (Query) TagSupport.findAncestorWithClass(this,
             Query.class);
        if (queryHandler == null)
        {
            throw new JspTagException("This tag should be used only as a child tag");
        }


      lookUpSearchManagerBean();
      _searchObj.setQuery(queryHandler.getQuery());
      String query = _searchObj.addRangeConditionToQuery(connector,field,lowValue,highValue,negate);
      queryHandler.setQuery(query);
      System.out.println(" In doStartTag == RangeCondition.java" + query);
      return EVAL_BODY_INCLUDE;
    }
     catch (Exception e)
     {
        throw new JspTagException(e.getMessage());
     }
  }

  public void setConnector(Object connector)
  {
    this.connector = connector;
  }

  public void setField(Object field)
  {
    this.field = field;
  }

  public void setLowValue(Object lowValue)
  {
    this.lowValue = lowValue;
  }

  public void setHighValue(Object highValue)
  {
    this.highValue = highValue;
  }

  public void setNegate(boolean negate)
  {
    this.negate = negate;
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
	Logger.err(" Exception in RangeCondition.lookUpSearchManagerBean() : ", ex);
	throw new SystemException(" Exception in lookup of SearchManagerBean ",ex);
      }
  }



}