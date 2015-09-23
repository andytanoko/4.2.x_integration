/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-18     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FakeEnumeratedConstraint;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;


public class QueryUtils
{
  static final String UPDATE_ACTION_ADD_VALUE_FILTER     = "addValueFilter";
  static final String UPDATE_ACTION_REMOVE_VALUE_FILTERS = "removeValueFilters";
  
  static final String CONNECTOR_AND  = "AND";
  static final String CONNECTOR_OR   = "OR";
  
  static final String OPERATOR_EQUAL             = "EQUAL";
  static final String OPERATOR_NOT_EQUAL         = "NOT_EQUAL";
  static final String OPERATOR_GREATER_OR_EQUAL  = "GREATER_OR_EQUAL";
  static final String OPERATOR_LESS_OR_EQUAL     = "LESS_OR_EQUAL";
  static final String OPERATOR_GREATER           = "GREATER";
  static final String OPERATOR_LESS              = "LESS";
  static final String OPERATOR_BETWEEN           = "BETWEEN";
  static final String OPERATOR_IN                = "IN"; //Not implemented in UI yet
  
  private static final String[] _connectorLabels =
  {
    "dataFilter.connector.and",
    "dataFilter.connector.or",
  };
  
  private static final String[] _connectorValues =
  {
    CONNECTOR_AND,
    CONNECTOR_OR,
  };
  
  private static final String[] _stdOperatorLabels =
  {
    "dataFilter.operator.equal",
    "dataFilter.operator.notEqual",
    "dataFilter.operator.greaterOrEqual",
    "dataFilter.operator.lessOrEqual",
    "dataFilter.operator.greater",
    "dataFilter.operator.less",
    "dataFilter.operator.between",
  };
  
  private static final String[] _binaryOperatorLabels =
  {
    "dataFilter.operator.equal",
    "dataFilter.operator.notEqual",
  };
  
  private static final String[] _binaryOperatorValues =
  {
    OPERATOR_EQUAL,
    OPERATOR_NOT_EQUAL,
  };
  
  private static final String[] _stdOperatorValues =
  {
    OPERATOR_EQUAL,
    OPERATOR_NOT_EQUAL,
    OPERATOR_GREATER_OR_EQUAL,
    OPERATOR_LESS_OR_EQUAL,
    OPERATOR_GREATER,
    OPERATOR_LESS,
    OPERATOR_BETWEEN,
  };
  
  static final IEnumeratedConstraint _connectorConstraint 
    = new FakeEnumeratedConstraint(_connectorLabels, _connectorValues);
    
  static final IEnumeratedConstraint _stdOperatorConstraint
    = new FakeEnumeratedConstraint(_stdOperatorLabels, _stdOperatorValues );
  
  static final IEnumeratedConstraint _binaryOperatorConstraint
    = new FakeEnumeratedConstraint(_binaryOperatorLabels, _binaryOperatorValues );
    
  private TimeZone _timeZone;
  private Locale _locale;
    
  public QueryUtils()
  {
  }
  
  public QueryUtils(TimeZone timeZone, Locale locale)
  {
    setTimeZone(timeZone);
    setLocale(locale);
  }   
    
  /*
   * This method must be called from the processUpdateAction() method of a dispatch action
   * for a form that uses a data filter field (as rendered by the DataFilterRenderer) 
   * that is editable. When the user clicks such buttons as add or remove
   * criteria, it will cause the updateAction field for the filter to be set clientside
   * with a value specifying which action is to be taken. A serverRefresh() then occurs
   * and this updateAction value (along with everything else) is submitted.
   * Call this method for each of your data filter fields, passing the filterForm for
   * that field.
   * The updateAction will be processed appropriately and when the form is rendered
   * again the changes will show.
   * If you pass null for the filterForm then the method does nothing and returns immediately.
   * @param filterForm The DataFilterAForm object which is the value of the filterField or null
   */  
  public static void processUpdateActions(DataFilterAForm filterForm)
  {
    if(filterForm == null) return;
    
    String updateAction = filterForm.getUpdateAction();
    
    if( UPDATE_ACTION_ADD_VALUE_FILTER.equals(updateAction) )
    {
      filterForm.addValueFilter(null);
    }
    else if( UPDATE_ACTION_REMOVE_VALUE_FILTERS.equals(updateAction) )
    {
      filterForm.removeSelectedFilters();
    }
    
    filterForm.setUpdateAction(null);
  }
  
  
  public IDataFilter constructFilter(DataFilterAForm filterForm,
                                            IGTSession gtasSession,
                                            String entityType)
    throws GTClientException
  {
    try
    {
      if(filterForm == null) return null;
      if (gtasSession == null)
        throw new NullPointerException("gtasSession is null");
      if (entityType == null)
        throw new NullPointerException("entityType is null");
      IGTManager manager = gtasSession.getManager(entityType);
      
      Object[] valueFilterForms = filterForm.getValueFilters();
      if (valueFilterForms == null)
        throw new NullPointerException("valueFilterForms array is null");
      if(valueFilterForms.length == 0)
        throw new IllegalStateException("valueFilterForms array is empty");
        
      DataFilterImpl filter = new DataFilterImpl();        
        
      for(int i=0; i < valueFilterForms.length; i++)
      {
        try
        {
          ValueFilterAForm valueFilterForm = (ValueFilterAForm)valueFilterForms[i];
          boolean negate = false; //Not implemented
          FilterConnector connector = (i == 0) ? null : constructConnector(filter, valueFilterForm.getConnector());
          Number field = constructField(filter, valueFilterForm.getField());
          Object[] params = constructParams(manager, entityType, field, valueFilterForm.getParams());
          
          String operatorString = valueFilterForm.getOperator();
          if(StaticUtils.stringEmpty(operatorString))
          {
            throw new IllegalArgumentException("operatorString is null or empty");
          }
          else if(OPERATOR_BETWEEN.equals(operatorString))
          {
            if(params.length != 2)
            {
              throw new IllegalStateException("Between comparison requires exactly 2 params"
                                              + " (found" + params.length + ")");
            }
            filter.addRangeFilter(connector, field, params[0], params[1], negate);
          }
          else if(OPERATOR_IN.equals(operatorString))
          {
            Collection paramsCollection = Arrays.asList(params);
            filter.addDomainFilter(connector, field, paramsCollection, negate);
          }
          else
          {
            FilterOperator operator = constructFilterOperator(filter, valueFilterForm.getOperator());
            if(params.length != 1)
            {
              throw new IllegalArgumentException( "Expecting 1 param for filter with operator type "
                                                  + valueFilterForm.getOperator()
                                                  + " but found"
                                                  + params.length
                                                  + " params");
            }
            filter.addSingleFilter(connector, field, operator, params[0], negate);
          }    
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error processing value filter " + i,t);
        }
      }      
      return filter;      
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error constructing IDataFilter object",t);
    }
  }
  
  private FilterConnector constructConnector(DataFilterImpl filter, String connector)
    throws GTClientException
  {
    try
    {
      if(StaticUtils.stringEmpty(connector))
      {
        return null;
      }
      else if(CONNECTOR_AND.equals(connector))
      {
        return filter.getAndConnector();
      }
      else if(CONNECTOR_OR.equals(connector))
      {
        return filter.getOrConnector();
      }
      else
      {
        throw new IllegalArgumentException("Bad connector type:" + connector);
      }
      
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error constructing FilterConnector for value:" + connector,t);
    }
  }
  
  private Number constructField(DataFilterImpl filter, String field)
    throws GTClientException
  {
    try
    {
      if(StaticUtils.stringEmpty(field))
      {
        throw new IllegalArgumentException("field is null or empty");
      }
      Number fieldId = StaticUtils.integerValue(field);
      if (fieldId == null)
        throw new NullPointerException("fieldId is null");
      return fieldId;
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error constructing field for value:" + field,t);
    }
  }
  
  private Object[] constructParams(IGTManager manager,
                                          String entityType,
                                          Number fieldId,
                                          String[] paramStrings)
    throws GTClientException
  {
    try
    {
      if(StaticUtils.stringArrayEmpty(paramStrings, true))
      { //Should we be allowing this instead of considering it invalid?
        throw new IllegalArgumentException("params is null, empty, or contains null elements");
      }
      else
      {
        IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(entityType, fieldId);
        Object[] params = new Object[paramStrings.length];  
        for(int i=0; i < params.length; i++)
        { 
          try
          {  
            String paramValueString = paramStrings[i];                 
            Object param = constructParam(paramValueString, fmi);
            if (param == null)
              throw new NullPointerException("param is null"); //Should I be checking this?
            params[i] = param;
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error creating instance of "
                                        + fmi.getValueClass()
                                        + " based on string value:"
                                        + paramStrings[i],t);
          } 
        }
        return params;
      }
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error constructing params for filtering on fieldId "
                                  + fieldId
                                  + " of entityType "
                                  + entityType
                                  + " using value(s):"
                                  + StaticUtils.implode(paramStrings,","),t);
    }
  }
  
  private Object constructParam(String paramValueString, IGTFieldMetaInfo fmi)
    throws GTClientException
  {
    try
    {
      String valueClass = fmi.getValueClass();
      if (valueClass == null)
        throw new NullPointerException("valueClass is null");
        
      if(fmi.getConstraintType() == IConstraint.TYPE_TIME)
      {
        ITimeConstraint constraint = (ITimeConstraint)fmi.getConstraint();
        java.util.Date date = DateUtils.parseDate(paramValueString,
                                                  getTimeZone(),
                                                  getLocale(),
                                                  null,
                                                  constraint.getAdjustment(),
                                                  constraint.hasDate(),
                                                  constraint.hasTime());
        //We will then convert this date object back into a string (milliseconds)
        //and let that be converted into whichever of the time and date classes was requested.
        //A little wasteful but saves time in implementation. Later refactoring will
        //be a nice luxury if we have the time.             
        paramValueString = "" + date.getTime();  
      }
        
        
      return StaticUtils.convert(paramValueString, valueClass, true, true);
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error constructing param",t);
    }
  }
  
  
  private FilterOperator constructFilterOperator(DataFilterImpl filter, String operator)
    throws GTClientException
  {
    try
    {
      if(StaticUtils.stringEmpty(operator))
      {
        throw new IllegalArgumentException("operator is null or empty");
      }
      else if(OPERATOR_BETWEEN.equals(operator))
      {
        throw new IllegalArgumentException(OPERATOR_BETWEEN
                    + " uses a rangeFilter and not a FilterOperator");  
      }
      else if(OPERATOR_IN.equals(operator))
      {
        throw new IllegalArgumentException(OPERATOR_IN
                    + " uses a domainFilter and not a FilterOperator");  
      }
      else if(OPERATOR_EQUAL.equals(operator))
      {
        return filter.getEqualOperator();
      }
      else if(OPERATOR_GREATER.equals(operator))
      {
        return filter.getGreaterOperator();
      }
      else if(OPERATOR_GREATER_OR_EQUAL.equals(operator))
      {
        return filter.getGreaterOrEqualOperator();
      }
      else if(OPERATOR_LESS.equals(operator))
      {
        return filter.getLessOperator();
      }
      else if(OPERATOR_LESS_OR_EQUAL.equals(operator))
      {
        return filter.getLessOrEqualOperator();
      }
      else if(OPERATOR_NOT_EQUAL.equals(operator))
      {
        return filter.getNotEqualOperator();
      }
      else
      {
        throw new IllegalArgumentException("Bad operator:" + operator);
      }
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error constructing operator based on value:" + operator,t);
    }
  }
  
  public Locale getLocale()
  {
    if(_locale == null) return Locale.getDefault();
    return _locale;
  }

  public TimeZone getTimeZone()
  {
    if(_timeZone == null) return TimeZone.getDefault();
    return _timeZone;
  }

  public void setLocale(Locale locale)
  {
    _locale = locale;
  }

  public void setTimeZone(TimeZone timeZone)
  {
    _timeZone = timeZone;
  }

}
