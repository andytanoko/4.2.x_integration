/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldDisplay.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 23 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

import com.gridnode.pdip.base.search.helpers.FrameworkDbHelper;
import com.gridnode.pdip.base.search.helpers.QueryHandler;
import com.gridnode.pdip.base.search.helpers.SearchFilterOperator;
import com.gridnode.pdip.base.search.helpers.Logger;

import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerObj;
import com.gridnode.pdip.base.search.facade.ejb.ISearchManagerHome;

import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FieldDisplay extends TagSupport
{
    private String tableName;
    private String fieldName;
    private Object field;
    private Object compOperator;
    private Object value;
    private boolean negate;
    private boolean isFieldValue;
    private boolean distinct;

    private ISearchManagerObj _searchObj;
    private ISearchManagerHome _searchHome;

    public int doStartTag() throws JspTagException
    {
        try
        {
	  lookUpSearchManagerBean();

	  //FieldMetaInfo fieldMetaInfo[] = FrameworkDbHelper.instance().getFieldMetaInfo(entityName);
	  JspWriter out  = pageContext.getOut();

          out.println(IDisplayOptions.HTML_ROW_START_TAG);
	  out.println(IDisplayOptions.HTML_CELL_START_TAG);
	  out.print(fieldName);
	  out.println(IDisplayOptions.HTML_CELL_END_TAG);

	  out.println(IDisplayOptions.HTML_CELL_START_TAG);
	  Collection collValues = _searchObj.getValuesForFieldName(tableName,fieldName,field,compOperator,value,isFieldValue,negate,distinct);
	  Iterator iter = collValues.iterator();

	  out.println(IDisplayOptions.HTML_SELECT_START_TAG+" NAME=\""+fieldName+"\">");

	  while(iter.hasNext())
	  {
	      String fieldValue=iter.next().toString();
	      out.println(IDisplayOptions.HTML_OPTION_START_TAG + " value = \"" +fieldValue+ "\">");
	      out.println(fieldValue);
	      out.println(IDisplayOptions.HTML_OPTION_END_TAG);
	  }

	  out.println(IDisplayOptions.HTML_SELECT_END_TAG);
          out.println(IDisplayOptions.HTML_CELL_END_TAG);

	  out.println(IDisplayOptions.HTML_ROW_END_TAG);

      	  return SKIP_BODY;
        }
        catch (Exception e)
        {
	  throw new JspTagException(e.getMessage());
        }
    }

    public void setTableName(String tableName)
    {
      this.tableName = tableName;
    }

    public void setFieldName(String fieldName)
    {
      this.fieldName = fieldName;
    }
    public void setField(Object field)
    {
      this.field = field;
    }
    public void setCompOperator(Object compOperator)
    {
      this.compOperator = compOperator;
    }
    public void setValue(Object value)
    {
      this.value = value;
    }
    public void setNegate(boolean negate)
    {
      this.negate = negate;
    }
     public void setIsFieldValue(boolean isFieldValue)
    {
      this.isFieldValue = isFieldValue;
    }

    public void setDistinct(boolean distinct)
    {
      this.distinct = distinct;
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
	Logger.err(" Exception in FieldDisplay.lookUpSearchManagerBean() : ", ex);
	throw new SystemException(" Exception in lookup of SearchManagerBean ",ex);
      }
  }


}