/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance.
 * Nov 15 2005    Neo Sok Lay         Fix NullPointerException if xpath entered by user starts with //
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.exceptions.SearchDocumentException;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.model.searchquery.ICondition;

import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerHome;
import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerObj;
import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.app.searchquery.model.Condition;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.DataFilterFactory;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.lang.reflect.Method;

/**
 * This class provides searchquery services used by document management.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchQueryDelegate
{
  private static SearchQueryDelegate  instance = null;
  private static Hashtable operatorTable;

  private SearchQueryDelegate()
  {
    //logD(CLASSNAME + ".XMLDocSearcher.1", Log.DEBUG_ENTER, "030624");
    operatorTable = new Hashtable();
    DataFilterFactory dataFF = new DataFilterFactory();
    operatorTable.put(dataFF.getEqualOperator().getName(), "EqualOp");
    operatorTable.put(dataFF.getGreaterOperator().getName(), "GreaterOp");
    operatorTable.put(dataFF.getGreaterOrEqualOperator().getName(), "GreaterEqualOp");
    operatorTable.put(dataFF.getLessOperator().getName(), "LessOp");
    operatorTable.put(dataFF.getLessOrEqualOperator().getName(), "LessEqualOp");
    operatorTable.put(dataFF.getNotEqualOperator().getName(), "NotEqualOp");
    operatorTable.put(dataFF.getLikeOperator().getName(), "LikeOp");
  }

  public static SearchQueryDelegate getInstance()
  {
    if(instance == null)
    {
      synchronized(SearchQueryDelegate.class)
      {
        if(instance == null)
        {
          instance = new SearchQueryDelegate();
        }
      }
    }
    return instance;
  }

  /**
   * Obtain the EJBObject for the SearchQueryManagerBean.
   *
   * @return The EJBObject to the SearchQueryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.3
   */
  public static ISearchQueryManagerObj getManager()
    throws ServiceLookupException
  {
    return (ISearchQueryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISearchQueryManagerHome.class.getName(),
      ISearchQueryManagerHome.class,
      new Object[0]);
  }

  public Collection searchDoc(IDataFilter orgFilter, Long queryUid)
    throws Exception
  {
    Logger.log("[SearchQueryDelegate.searchDoc] Start");

    SearchQuery query = getManager().findSearchQuery(queryUid);
    if (query == null)
    {
      throw new SearchDocumentException("SearchQuery with Uid :"+queryUid+" does not exist");
    }

    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GridDocument.UID,
      filter.getNotEqualOperator(),
      null,
      false
    );

    if (orgFilter != null)
    {
      filter = orgFilter;
    }

    ArrayList udocConditions = new ArrayList();
    List conditions = query.getConditions();
    for (Iterator i = conditions.iterator(); i.hasNext(); )
    {
      Condition condition = (Condition)i.next();
      Short type = condition.getType();
      if (type.equals(ICondition.TYPE_GDOC))
      {
        filter.addFilter(filter.getAndConnector(), condition.toDataFilter());
      }
      else if (type.equals(ICondition.TYPE_UDOC))
      {
        udocConditions.add(condition);
      }
      else
      {
        throw new SearchDocumentException("Unknow condition type :"+type+
                                          " in SearchQuery Uid :"+queryUid);
      }
    }

    Collection gridDocKeys =
      GridDocumentEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);

    Logger.debug("[SearchQueryDelegate.searchDoc] Number of GridDocument matching :"+gridDocKeys.size());

    if (!udocConditions.isEmpty())
    {
      gridDocKeys = searchUdocs(gridDocKeys, udocConditions);
    }

    Logger.debug("[SearchQueryDelegate.searchDoc] returning "+gridDocKeys.size()+
                 " matching GridDocuments");
    return gridDocKeys;
  }


  private Collection searchUdocs(Collection gridDocKeys, List conditions)
    throws Exception
  {
    Logger.log("[SearchQueryDelegate.searchUdocs] Start");
    try
    {
      ArrayList matchedGridDocKeys = new ArrayList();

      for (Iterator i = gridDocKeys.iterator(); i.hasNext(); )
      {
        boolean match = true;
        Long gridDocUid = (Long)i.next();
        GridDocument gridDoc =
          (GridDocument)GridDocumentEntityHandler.getInstance().getEntityByKeyForReadOnly(gridDocUid);
        File udoc = FileHelper.getUdocFile(gridDoc);
        if (udoc != null)
        {
          for (Iterator c = conditions.iterator(); c.hasNext(); )
          {
            Condition condition = (Condition)c.next();
            String xpath = condition.getXPath();
            if (!matchXPath(udoc.getAbsolutePath(), xpath, condition))
            {
              match = false;
            }
          }
          if (match)
          {
            matchedGridDocKeys.add(gridDocUid);
          }
        }
        else
        {
          Logger.warn("[SearchQueryDelegate.searchUdocs] Unable to find udoc "+udoc.getAbsolutePath());
        }
      }

      return matchedGridDocKeys;
    }
    catch (Throwable ex)
    {
      throw new SearchDocumentException("Exception when searching udoc xpath", ex);
    }
  }


  private boolean matchXPath(String uDocFilename, String xPath, Condition condition)
  {
    boolean match = false;
    try
    {
      String tempXPath = xPath; //NSL20051115 Init to xPath
      if (!xPath.startsWith("//") && xPath.indexOf("//") == -1)
      {
        tempXPath =  "//" + xPath;
      }
//      logD(CLASSNAME + ".matchXPath.1", Log.DEBUG_VALUE,
//        "XPath = " + tempXPath);
      List nodeTexts = XMLDelegate.getManager().getXPathValues(uDocFilename, tempXPath);
      Iterator nodesIterator = nodeTexts.iterator();
      while(nodesIterator.hasNext())
      {
//        logD(CLASSNAME + ".matchXPath.2", Log.DEBUG_VALUE,
//          "has next");
        String nodeText = (String)nodesIterator.next();
        if (nodeText != null)
        {
          match = matchValue(nodeText, condition);
        }
        else
        {
          match = false;
        }
        if (match)
        {
          break;
        }
      }
      return match;
    }
    catch (Exception e)
    {
//      logD(CLASSNAME + ".matchXPath.3", Log.DEBUG_VALUE,
//        StackTraceExtractor.getStackTrace(e));
      return match;
    }
  }

  private boolean matchValue(String nodeText, Condition condition)
  {
    boolean match = false;
    if (condition.getOperator().equals(ICondition.BETWEEN))
    {
//      logD(CLASSNAME + ".matchValue.2", Log.DEBUG_VALUE, "RangeValueFilter");
//      match = matchRangeValue(nodeText, condition);
      Object valueLow = convertValue(condition.getValues().get(0));
      Object valueHigh = convertValue(condition.getValues().get(1));

      match = handleBetweenOp(nodeText, valueLow, valueHigh);
    }
    else if (condition.getOperator().equals(ICondition.IN))
    {
      match = handleInOp(nodeText, condition.getValues());
    }
    else
    {
//      logD(CLASSNAME + ".matchValue.1", Log.DEBUG_VALUE, "SingleValueFilter");
      match = matchSingleValue(nodeText, condition);
    }
    return match;
  }
  /*
  private boolean matchRangeValue(String nodeText, Condition condition)
  {
    Object valueLow = convertValue(condition.getValues().get(0));
    Object valueHigh = convertValue(condition.getValues().get(1));

    return handleBetweenOp(nodeText, valueLow, valueHigh);
  }*/

  private boolean matchSingleValue(String nodeText, Condition condition)
  {
    boolean match = false;
    Object value = convertValue(condition.getValues().get(0));
    FilterOperator filterOperator = new FilterOperator(condition.getOperatorSymbol());
//    logD(CLASSNAME + ".matchSingleValue.1", Log.DEBUG_VALUE,
//      "filterOperator = " + filterOperator);

    // reflection
    try
    {
      String methodName = "handle" + operatorTable.get(filterOperator.getName());
      Class[] paramTypes = new Class[2];
      paramTypes[0] = String.class;
      paramTypes[1] = Object.class;
      Method opMethod = getClass().getDeclaredMethod(methodName, paramTypes);

      Object[] paramList = new Object[2];
      paramList[0] = nodeText;
      paramList[1] = value;
      match = ((Boolean)opMethod.invoke(this, paramList)).booleanValue();
    }
    catch (Exception ex)
    {
      return false;
    }
    return match;
  }

  private Object convertValue(Object value)
  {
    try
    {
      return Integer.valueOf(value.toString());
    }
    catch (NumberFormatException ex)
    {}

    try
    {
      return Long.valueOf(value.toString());
    }
    catch (NumberFormatException ex)
    {}

    try
    {
      return Double.valueOf(value.toString());
    }
    catch (NumberFormatException ex)
    {}

    return value;
  }
  
  /**
   * this method is called using reflection
   */
  private boolean handleEqualOp(String nodeText, Object value)
  {
    //System.out.println("Equal ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof Boolean)
    {
      Boolean uDocValue = new Boolean(nodeText);
      if (uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String)value;
      if (valueString.indexOf("*") > -1)
      {
        if (valueString.startsWith("*"))
        {
          String matchString = valueString.substring(1);
//          logD(CLASSNAME + ".handleEqualOp.1", Log.DEBUG_VALUE,
//            "matchString = "+matchString);
          if (nodeText.endsWith(matchString))
          {
            return true;
          }
        }
        else if (valueString.endsWith("*"))
        {
          String matchString = valueString.substring(0, valueString.length()-1);
//          logD(CLASSNAME + ".handleEqualOp.2", Log.DEBUG_VALUE,
//            "matchString = "+matchString);
          if (nodeText.startsWith(matchString))
          {
            return true;
          }
        }
        else
        {
          String matchStringFront = valueString.substring(0, valueString.indexOf("*"));
          String matchStringBack = valueString.substring(valueString.lastIndexOf("*")+1);
          if (nodeText.startsWith(matchStringFront) && nodeText.endsWith(matchStringBack))
          {
            return true;
          }
        }
      }
      else
      {
        if (nodeText.equals(value))
        {
          return true;
        }
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (nodeDate.equals(date))
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleEqualOp.3", Log.DEBUG_VALUE,
//          StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   * @param nodeText
   * @param value
   * @return
   */
  private boolean handleGreaterOp(String nodeText, Object value)
  {
    //System.out.println("Greater ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (uDocValue.compareTo((Integer)value) > 0)
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (uDocValue.compareTo((Long)value) > 0)
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (uDocValue.compareTo((Double)value) > 0)
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      if (nodeText.compareTo((String)value) > 0)
      {
        return true;
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (nodeDate.compareTo(date) > 0)
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleGreaterOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   */
  private boolean handleGreaterEqualOp(String nodeText, Object value)
  {
    //System.out.println("GreaterEqual ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (uDocValue.compareTo((Integer)value) >= 0)
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (uDocValue.compareTo((Long)value) >= 0)
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (uDocValue.compareTo((Double)value) >= 0)
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      if (nodeText.compareTo((String)value) >= 0)
      {
        return true;
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (nodeDate.compareTo(date) >= 0)
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleGreaterEqualOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   * @param nodeText
   * @param value
   * @return
   */
  private boolean handleLessOp(String nodeText, Object value)
  {
    //System.out.println("Less ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (uDocValue.compareTo((Integer)value) < 0)
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (uDocValue.compareTo((Long)value) < 0)
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (uDocValue.compareTo((Double)value) < 0)
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      if (nodeText.compareTo((String)value) < 0)
      {
        return true;
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (nodeDate.compareTo(date) < 0)
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleLessOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   * @param nodeText
   * @param value
   * @return
   */
  private boolean handleLessEqualOp(String nodeText, Object value)
  {
    //System.out.println("LessEqual ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (uDocValue.compareTo((Integer)value) <= 0)
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (uDocValue.compareTo((Long)value) <= 0)
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (uDocValue.compareTo((Double)value) <= 0)
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      if (nodeText.compareTo((String)value) <= 0)
      {
        return true;
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (nodeDate.compareTo(date) <= 0)
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleLessEqualOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   * @param nodeText
   * @param value
   * @return
   */
  private boolean handleNotEqualOp(String nodeText, Object value)
  {
    //System.out.println("NotEqual ==> "+value);
    if (value instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (!uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (!uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (!uDocValue.equals(value))
      {
        return true;
      }
    }
    else if (value instanceof String)
    {
      String valueString = (String)value;
      if (valueString.indexOf("*") > -1)
      {
        if (valueString.startsWith("*"))
        {
          String matchString = valueString.substring(1);
          if (!(nodeText.endsWith(matchString)))
          {
            return true;
          }
        }
        else if (valueString.endsWith("*"))
        {
          String matchString = valueString.substring(0, valueString.length()-1);
          if (!(nodeText.startsWith(matchString)))
          {
            return true;
          }
        }
        else
        {
          String matchStringFront = valueString.substring(0, valueString.indexOf("*"));
          String matchStringBack = valueString.substring(valueString.lastIndexOf("*")+1);
          if (!(nodeText.startsWith(matchStringFront) && nodeText.endsWith(matchStringBack)))
          {
            return true;
          }
        }
      }
      else
      {
        if (!nodeText.equals(value))
        {
          return true;
        }
      }
    }
//    else if (value instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwf = (DateWithFormat)value;
//        String dateFormat = dwf.getDateFormat();
//        Date date = dwf.getDate();
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if (!nodeDate.equals(date))
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleNotEqualOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  private boolean handleBetweenOp(String nodeText, Object valueLow, Object valueHigh)
  {
    //System.out.println("Between ==> "+valueLow+"  "+valueHigh);
    if (valueLow instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if ((uDocValue.compareTo((Integer)valueLow) > 0) &&
          (uDocValue.compareTo((Integer)valueHigh) < 0))
      {
        return true;
      }
    }
    else if (valueLow instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if ((uDocValue.compareTo((Long)valueLow) > 0) &&
          (uDocValue.compareTo((Long)valueHigh) < 0))
      {
        return true;
      }
    }
    else if (valueLow instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if ((uDocValue.compareTo((Double)valueLow) > 0) &&
          (uDocValue.compareTo((Double)valueHigh) < 0))
      {
        return true;
      }
    }
    else if (valueLow instanceof String)
    {
      if ((nodeText.compareTo((String)valueLow) > 0) &&
          (nodeText.compareTo((String)valueHigh) < 0))
      {
        return true;
      }
    }
//    else if (valueLow instanceof DateWithFormat)
//    {
//      try
//      {
//        DateWithFormat dwfLow = (DateWithFormat)valueLow;
//        String dateFormat = dwfLow.getDateFormat();
//        Date dateLow = dwfLow.getDate();
//
//        DateWithFormat dwfHigh = (DateWithFormat)valueHigh;
//        Date dateHigh = dwfHigh.getDate();
//
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        Date nodeDate = formatter.parse(nodeText);
//        if ((nodeDate.compareTo(dateLow) > 0) &&
//            (nodeDate.compareTo(dateHigh) < 0))
//        {
//          return true;
//        }
//      }
//      catch (Exception e)
//      {
//        logD(CLASSNAME + ".handleBetweenOp.1", Log.DEBUG_VALUE,
//          "Not a valid date format\n" + StackTraceExtractor.getStackTrace(e));
//        return false;
//      }
//    }

    return false;
  }

  /**
   * this method is called using reflection
   * @param nodeText
   * @param value
   * @return
   */
  private boolean handleLikeOp(String nodeText, Object value)
  {
    //System.out.println("Like ==> "+value);
    String valueString = value.toString();
    if (nodeText.startsWith(valueString))
    {
      return true;
    }
    else
    {
      if (nodeText.equals(valueString))
      {
        return true;
      }
    }

    return false;
  }

  private boolean handleInOp(String nodeText, Collection values)
  {
    //System.out.println("In ==> "+values);
    HashSet set = new HashSet(values);
    Object aValue = values.iterator().next();
    if (aValue instanceof Integer)
    {
      Integer uDocValue = new Integer(nodeText);
      if (set.contains(uDocValue))
      {
        return true;
      }
    }
    else if (aValue instanceof Long)
    {
      Long uDocValue = new Long(nodeText);
      if (set.contains(uDocValue))
      {
        return true;
      }
    }
    else if (aValue instanceof Double)
    {
      Double uDocValue = new Double(nodeText);
      if (set.contains(uDocValue))
      {
        return true;
      }
    }
    else if (aValue instanceof String)
    {
      if (set.contains(nodeText))
      {
        return true;
      }
    }

    return false;
  }

}