/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Condition.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2003    Koh Han Sing        Created
 * Jun 20 2006    Tam Wei Xiang       Modified method toDataFilter()
 *                                    This is to fix the defect GNDB00026581.
 *                                    Instead of using the size of the _values 
 *                                    to determine the filter we need to construct,
 *                                    we use the _operator to decide.
 */
package com.gridnode.pdip.app.searchquery.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class Condition
  extends AbstractEntity
  implements ICondition
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2293992012793128159L;
	protected Number      _field;
  protected String      _xpath;
  protected Short       _operator;
  protected ArrayList   _values;
  protected Short       _type;

  protected Hashtable   _operatorMap;

  public Condition()
  {
    _operatorMap = new Hashtable();
    _operatorMap.put(ICondition.BETWEEN, ICondition.BETWEEN_OPERATOR);
    _operatorMap.put(ICondition.EQUAL, ICondition.EQUAL_OPERATOR);
    _operatorMap.put(ICondition.GREATER, ICondition.GREATER_OPERATOR);
    _operatorMap.put(ICondition.GREATER_EQUAL, ICondition.GREATER_EQUAL_OPERATOR);
    _operatorMap.put(ICondition.IN, ICondition.IN_OPERATOR);
    _operatorMap.put(ICondition.LESS, ICondition.LESS_OPERATOR);
    _operatorMap.put(ICondition.LESS_EQUAL, ICondition.LESS_EQUAL_OPERATOR);
    _operatorMap.put(ICondition.LIKE, ICondition.LIKE_OPERATOR);
    _operatorMap.put(ICondition.LOCATE, ICondition.LOCATE_OPERATOR);
    _operatorMap.put(ICondition.NOT, ICondition.NOT_OPERATOR);
    _operatorMap.put(ICondition.NOT_EQUAL, ICondition.NOT_EQUAL_OPERATOR);
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("Type :");
    sb.append(getType());
    sb.append("/Field :");
    sb.append(getField());
    sb.append("/Operator :");
    sb.append(getOperator());
    sb.append("/Values :");
    sb.append(getValues().toString());
    return sb.toString();
  }

  public Number getKeyId()
  {
    return null;
  }

  // ***************** Getters for attributes ***********************

  public void setField(Number field)
  {
    _field = field;
  }

  public void setXPath(String xpath)
  {
    _xpath = xpath;
  }

  public void setOperator(Short op)
  {
    _operator = op;
  }

  public void setValues(List values)
  {
    _values = new ArrayList(values);
  }

  public void setType(Short type)
  {
    _type = type;
  }

  public Number getField()
  {
    return _field;
  }

  public String getXPath()
  {
    return _xpath;
  }

  public Short getOperator()
  {
    return _operator;
  }

  public List getValues()
  {
    return _values;
  }

  public Short getType()
  {
    return _type;
  }

  public IDataFilter toDataFilter()
  {
    if (_field == null || _values == null)
    {
      return null;
    }
    
    //TWX 20062006
    if(_operator.equals(ICondition.IN))
    {
    	return toDomainFilter();
    }
    else if(_operator.equals(ICondition.BETWEEN))
    {
    	return toRangeFilter();
    }
    else
    {
    	return toSingleFilter();
    }
    /*
     * If the operator is 'IN' and we specify one value,
     * the following will contruct a wrong filter. This will have
     * problem while we convert the filter to SQL statement
    if (_values.size() == 1)
    {
      return toSingleFilter();
    }
    else if (_values.size() > 1)
    {
      if (_operator.equals(ICondition.BETWEEN))
      {
        return toRangeFilter();
      }

      return toDomainFilter();
    } 

    return null; */
  }

  public String getOperatorSymbol()
  {
    return (String)_operatorMap.get(_operator);
  }

  private IDataFilter toSingleFilter()
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      _field,
       new FilterOperator(_operatorMap.get(_operator).toString()),
      _values.get(0),
      false
    );
    return filter;
  }

  private IDataFilter toDomainFilter()
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addDomainFilter(
      null,
      _field,
      _values,
      false
    );
    return filter;
  }

  private IDataFilter toRangeFilter()
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addRangeFilter(
      null,
      _field,
      _values.get(0),
      _values.get(1),
      false
    );
    return filter;
  }

}