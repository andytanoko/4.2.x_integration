/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * May 03 2002    Neo Sok Lay         Add DELETE,INSERT, and UPDATE syntax.
 * Oct 15 2003    Neo Sok Lay         Enable ORDER Syntax to support specification
 *                                    on ascending or descending orders.
 * Oct 21 2005    Neo Sok Lay         Support SELECT syntax to support specification
 *                                    on distinct fields.  
 * Jan 25 2007    Neo Sok Lay         Add EngineType.      
 * Jun 07 2007    Tam Wei Xiang       Support select MAX, MIN                                                              
 */
package com.gridnode.pdip.framework.db.filter;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.Serializable;

/**
 * A flyweight factory for creating components for a type of data filter.
 * Components include {@link FilterOperator} and {@link FilterConnector} types.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class DataFilterFactory implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5191170341244014244L;

	protected Hashtable _components = new Hashtable();

  protected String    _factoryName = "default";
  
  protected String _engineType;

  /**
   * Construct a factory.
   *
   * @since 1.0a build 0.9.9.6
   */
  public DataFilterFactory()
  {
    createComponents();
  }

  /**
   * Creates the components of the factory.
   *
   * @since 1.0a build 0.9.9.6
   */
  private void createComponents()
  {
    putComponent("and", createAndConnector());
    putComponent("or",  createOrConnector());
    putComponent("=",   createEqualOperator());
    putComponent("<>",  createNotEqualOperator());
    putComponent("<",   createLessOperator());
    putComponent("<=",  createLessOrEqualOperator());
    putComponent(">",   createGreaterOperator());
    putComponent(">=",  createGreaterOrEqualOperator());
    putComponent("in",  createInOperator());
    putComponent("b/w", createBetweenOperator());
    putComponent("not", createNotOperator());
    putComponent("like",createLikeOperator());
    putComponent("loc", createLocateOperator());
    
    putComponent("select",       createSelectSyntax());
    putComponent("selectDistinct", createSelectDistinctSyntax());
    putComponent("update",       createUpdateSyntax());
    putComponent("delete",       createDeleteSyntax());
    putComponent("insert",       createInsertSyntax());
    putComponent("condition",    createConditionSyntax());
    putComponent("order",        createOrderSyntax());
    putComponent("ascendOrder",  createAscendOrderSyntax());
    putComponent("descendOrder", createDescendOrderSyntax());
    putComponent("selectMax",    createSelectMaxSyntax());
    putComponent("selectMin",    createSelectMinSyntax());
  }

  /**
   * Puts a component into the factory.
   *
   * @param key The key to identify the component
   * @param component The component to put
   *
   * @since 1.0a build 0.9.9.6
   */
  private void putComponent(Object key, Object component)
  {
    _components.put(key, component);
  }

  /**
   * Creates an "And" connector.
   *
   * @return The "And" connector
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createAndConnector()
  {
    return new FilterConnector("AND");
  }

  /**
   * Get the "And" connector.
   *
   * @return The "And" connector of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterConnector getAndConnector()
  {
    return (FilterConnector)getComponent("and");
  }

  /**
   * Creates an "Or" connector.
   *
   * @return The "Or" connector
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createOrConnector()
  {
    return new FilterConnector("OR");
  }

  /**
   * Get the "Or" connector.
   *
   * @return The "Or" connector of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterConnector getOrConnector()
  {
    return (FilterConnector)getComponent("or");
  }

  /**
   * Creates an "Equal" operator.
   *
   * @return The "Equal" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createEqualOperator()
  {
    return new FilterOperator("=");
  }

  /**
   * Get the "Equal" operator.
   *
   * @return The "Equal" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getEqualOperator()
  {
    return (FilterOperator)getComponent("=");
  }

  /**
   * Create a "Not Equal" operator.
   *
   * @return The "Not Equal" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotEqualOperator()
  {
    return new FilterOperator("<>");
  }

  /**
   * Get the "Not Equal" operator.
   *
   * @return The "Not Equal" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getNotEqualOperator()
  {
    return (FilterOperator)getComponent("<>");
  }

  /**
   * Creates a "Greater Than" operator.
   *
   * @return The "Greater Than" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createGreaterOperator()
  {
    return new FilterOperator(">");
  }

  /**
   * Get the "Greater than" operator.
   *
   * @return The "Greater Than" operator of the filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getGreaterOperator()
  {
    return (FilterOperator)getComponent(">");
  }

  /**
   * Creates a "Greater Than or Equal" operator.
   *
   * @return The "Greater Than or Equal" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createGreaterOrEqualOperator()
  {
    return new FilterOperator(">=");
  }

  /**
   * Get the "Greater Than or Equal" operator.
   *
   * @return The "Greather Than or Equal" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getGreaterOrEqualOperator()
  {
    return (FilterOperator)getComponent(">=");
  }

  /**
   * Creates a "Less Than" operator.
   *
   * @return The "Less Than" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLessOperator()
  {
    return new FilterOperator("<");
  }

  /**
   * Get the "Less than" operator.
   *
   * @return The "Less Than" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getLessOperator()
  {
    return (FilterOperator)getComponent("<");
  }

  /**
   * Creates a "Less Than or Equal" operator.
   *
   * @return The "Less Than or Equal" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLessOrEqualOperator()
  {
    return new FilterOperator("<=");
  }

  /**
   * Get a "Less than or equal" operator.
   *
   * @return The "Less Than or Equal" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getLessOrEqualOperator()
  {
    return (FilterOperator)getComponent("<=");
  }

  /**
   * Creates a "Not" operator.
   *
   * @return The "Not" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotOperator()
  {
    return new FilterOperator("NOT");
  }

  /**
   * Get the "Not" operator.
   *
   * @return The "Not" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getNotOperator()
  {
    return (FilterOperator)getComponent("not");
  }

  /**
   * Creates a "Like" operator.
   *
   * @return The "Like" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLikeOperator()
  {
    return new FilterOperator("LIKE");
  }

  /**
   * Get the "Like" operator.
   *
   * @return The "Like" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getLikeOperator()
  {
    return (FilterOperator)getComponent("like");
  }

  /**
   * Creates an "In" operator.
   *
   * @return The "In" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createInOperator()
  {
    return new FilterOperator("IN");
  }

  /**
   * Get the "In" operator.
   *
   * @return The "In" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getInOperator()
  {
    return (FilterOperator)getComponent("in");
  }

  /**
   * Creates a "Between-And" operator.
   *
   * @return The "Between-And" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createBetweenOperator()
  {
    return new FilterOperator("BETWEEN");
  }

  /**
   * Get the "Between" operator.
   *
   * @return The "Between-And" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getBetweenOperator()
  {
    return (FilterOperator)getComponent("b/w");
  }

  /**
   * Creates a "Locate" operator.
   *
   * @return The "Locate" operator
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLocateOperator()
  {
    return new FilterOperator("LOCATE");
  }

  /**
   * Get the "Locate" operator.
   *
   * @return The "Locate" operator of the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final FilterOperator getLocateOperator()
  {
    return (FilterOperator)getComponent("loc");
  }

  /**
   * Get the name of this factory.
   *
   * @return The name of the factory.
   *
   * @since 1.0a build 0.9.9.6
   */
  public final String getFactoryName()
  {
    return _factoryName;
  }

  /**
   * Applys the syntax for criteria.
   *
   * @param filter The criteria to apply syntax on
   * @return The string expression of the criteria with applied syntax
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applySyntax(IValueFilter filter)
  {
    if (filter instanceof SingleValueFilter)
      return applySingleSyntax((SingleValueFilter)filter);
    if (filter instanceof RangeValueFilter)
      return applyRangeSyntax((RangeValueFilter)filter);
    if (filter instanceof DomainValueFilter)
      return applyDomainSyntax((DomainValueFilter)filter);

    return filter.toString();
  }

  /**
   * Applys the syntax on a "single" type criteria.
   *
   * @param filter The criteria to apply syntax on
   * @return The string expression of the criteria with applied syntax
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String applySingleSyntax(SingleValueFilter filter)
  {
    return filter.getOperator().applySyntax(filter);
  }

  /**
   * Applys the syntax on a "range" type criteria.
   *
   * @param filter The criteria to apply syntax on
   * @return The string expression of the criteria with applied syntax
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String applyRangeSyntax(RangeValueFilter filter)
  {
    return getBetweenOperator().applySyntax(filter);
  }

  /**
   * Applys the syntax on a "domain" type criteria.
   *
   * @param filter The criteria to apply syntax on
   * @return The string expression of the criteria with applied syntax
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String applyDomainSyntax(DomainValueFilter filter)
  {
    return getInOperator().applySyntax(filter);
  }

  /**
   * Applys selection syntax.
   *
   * @param objName The object to select on
   * @param objFieldNames Names of the fields to select
   * @return The selection statement
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getSelectSyntax(String objName, String[] objFieldNames)
  {
    /*031015NSL
    if (objFieldNames == null || objFieldNames.length == 0)
      return "SELECT("+objName+",*)";

    String select = "SELECT("+objName + ","+objFieldNames[0];
    for (int i=1; i<objFieldNames.length; i++)
     select += (","+objFieldNames[i]);

    select += ")";
    */

    String[] params = new String[]{objName, "*"};
    if (objFieldNames != null && objFieldNames.length > 0)
    {
      StringBuffer buff = new StringBuffer(objFieldNames[0]);
      for (int i=1; i<objFieldNames.length; i++)
        buff.append(',').append(objFieldNames[i]);
      params[1] = buff.toString();  
    }
    
    String formattedS = formatSyntax(getSelectSyntax(), params);
    return formattedS;
  }
  
  public String getSelectDistinctSyntax(String objName, String[] objFieldNames)
  {
    String[] params = new String[]{objName, "*"};
    if (objFieldNames != null && objFieldNames.length > 0)
    {
      StringBuffer buff = new StringBuffer(objFieldNames[0]);
      for (int i=1; i<objFieldNames.length; i++)
        buff.append(',').append(objFieldNames[i]);
      params[1] = buff.toString();  
      return formatSyntax(getSelectDistinctSyntax(), params);                    
    }
    else
    {
      return formatSyntax(getSelectSyntax(), params);
    }
	    
  }
  
  public String getSelectMaxSyntax(String objName, String objFieldName)
  {
    return formatSyntax(getSelectMaxSyntax(), new String[]{objName, objFieldName});
  }
  
  public String getSelectMinSyntax(String objName, String objFieldName)
  {
    return formatSyntax(getSelectMinSyntax(), new String[]{objName, objFieldName});
  }
  
  /**
   * Applys Deletion syntax.
   *
   * @param objName The object to delete
   * @return The deletion statement
   *
   * @since 2.0
   */
  public String getDeleteSyntax(String objName)
  {
    /*031015NSL
    String delete = "DELETE("+objName+")";
    return delete;
    */

    String[] params = new String[]{objName};
    return formatSyntax(getDeleteSyntax(), params);
  }

  /**
   * Applys Insert syntax.
   *
   * @param objName The object to insert into
   * @param objFieldNames Names of the fields in the insert statement
   * @return The Insert statement
   *
   * @since 2.0
   */
  public String getInsertSyntax(String objName, String[] objFieldNames)
  {
    /*031015NSL
    String insert = "INSERT("+objName + ","+objFieldNames[0];
    for (int i=1; i<objFieldNames.length; i++)
     insert += (","+objFieldNames[i]);

    insert += ")";
    return insert;
    */
    
    StringBuffer buff = new StringBuffer(objFieldNames[0]);
    for (int i=1; i<objFieldNames.length; i++)
    {
      buff.append(',').append(objFieldNames[i]);
    }
    String[] params = new String[]{objName, buff.toString()};
    
    return formatSyntax(getInsertSyntax(), params);                    
  }

  /**
   * Applys Update syntax.
   *
   * @param objName The object to update into
   * @param objFieldNames Names of the fields in the update statement
   * @return The Update statement
   *
   * @since 2.0
   */
  public String getUpdateSyntax(String objName, String[] objFieldNames)
  {
    /*031015NSL
    String insert = "UPDATE("+objName + ","+objFieldNames[0];
    for (int i=1; i<objFieldNames.length; i++)
     insert += (","+objFieldNames[i]);

    insert += ")";
    return insert;
    */

    StringBuffer buff = new StringBuffer(objFieldNames[0]);
    for (int i=1; i<objFieldNames.length; i++)
    {
      buff.append(',').append(objFieldNames[i]);
    }
    String[] params = new String[]{objName, buff.toString()};
    
    return formatSyntax(getUpdateSyntax(), params);                    
  }

  /**
   * Applys condition syntax.
   *
   * @param select The selection portion
   * @param condition The condition portion
   * @return The string with condition syntax applied on <I>select</I>
   * and <I>condition</I>.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applyConditionSyntax(String select, String condition)
  {
    /*031015NSL
    return select + " ON CONDITION("+condition+")";
    */
    
    if (condition==null || condition.length()==0)
      return select;
      
    String[] params = new String[]{
                        select,
                        condition
                      };
    return formatSyntax(getConditionSyntax(), params);                  
  }

  /**
   * Applys order syntax on a query.
   *
   * @param query The query
   * @param orderFields The fields to order by
   * @return The resulting query with ordering
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applyOrderSyntax(String query, Object[] orderFields)
  {
    if (orderFields == null || orderFields.length == 0)
      return query;

    /*031015NSL
    String str = query + " ORDER(" + orderFields[0];
    for (int i=1; i<orderFields.length; i++)
      str += ( "," + orderFields[i] );
    str += ")";  
    return str;
    */

    StringBuffer buff = new StringBuffer();
    buff.append(orderFields[0]);
    for (int i=1; i<orderFields.length; i++)
      buff.append(',').append(orderFields[i]);
    String[] params = new String[]{query, buff.toString()};
    
    return formatSyntax(getOrderSyntax(), params);                    
  }

  /**
   * Applys order syntax on a query.
   *
   * @param query The query
   * @param orderFields The fields to order by
   * @param sortOrders The sorting orders, corresponding to each orderField.
   * It is assume the the number of elements specified matches that for
   * <code>orderFields</code> if <code>orderFields != null && orderFields.length > 0</code>.
   * @return The resulting query with ordering
   *
   * @since GT 2.2 I3
   */
  public String applyOrderSyntax(String query, Object[] orderFields, boolean[] sortOrders)
  {
    if (orderFields == null || orderFields.length == 0)
      return applyOrderSyntax(query, orderFields);

    MessageFormat ascSyntax = getAscendOrderSyntax();
    MessageFormat descSyntax = getDescendOrderSyntax();
    
    StringBuffer buff = new StringBuffer();
    buff.append(formatSyntax(
      sortOrders[0]? ascSyntax : descSyntax,
      new Object[]{orderFields[0]}));
      
    for (int i=1; i<orderFields.length; i++)
    {
      buff.append(',').append(formatSyntax(
        sortOrders[i]? ascSyntax : descSyntax,
        new Object[]{orderFields[i]}));
    }
    String[] params = new String[]{query, buff.toString()};
    
    return formatSyntax(getOrderSyntax(), params);                    
  }


  /**
   * Finds the key of a component in the factory.
   *
   * @param component The component whose key to look for
   * @return The key of the component, or <B>null</B> if component is not
   * found in the factory
   *
   * @since 1.0a build 0.9.9.6
   */
  public final Object findComponentKey(Object component)
  {
    if (_components.containsValue(component))
    {
      Enumeration keys = _components.keys();
      while (keys.hasMoreElements())
      {
        Object key = keys.nextElement();
        if (_components.get(key).equals(component))
          return key;
      }
    }
    return null;
  }

  /**
   * Get a component produced by this factory.
   *
   * @param key The key of the component to obtain
   * @return The component in the factory that has the specified key
   *
   * @since 1.0a build 0.9.9.6
   */
  public final Object getComponent(Object key)
  {
    return _components.get(key);
  }
  
  /**
   * Format the syntax using the specified parameters/
   * 
   * @param syntax The syntax to format.
   * @param params The parameters to pass to the formatter to
   * form the formatted syntax.
   * @return Formatted syntax string.
   */
  protected final String formatSyntax(MessageFormat syntax, Object[]params)
  {
    return syntax.format(params);
  }

  protected MessageFormat createSelectSyntax()
  {
    return new MessageFormat("SELECT({0},{1})");
  }
  
  protected MessageFormat createSelectDistinctSyntax()
  {
	return new MessageFormat("SELECT DISTINCT({0},{1}");  
  }
  
  protected MessageFormat createDeleteSyntax()
  {
    return new MessageFormat("DELETE({0})");
  }

  protected MessageFormat createUpdateSyntax()
  {
    return new MessageFormat("UPDATE({0},{1})");
  }

  protected MessageFormat createInsertSyntax()
  {
    return new MessageFormat("INSERT({0},{1})");
  }

  protected MessageFormat createConditionSyntax()
  {
    return new MessageFormat("{0} ON CONDITION({1})");
  }

  protected MessageFormat createOrderSyntax()
  {
    return new MessageFormat("{0} ORDER({1})");
  }

  protected MessageFormat createAscendOrderSyntax()
  {
    return new MessageFormat("{0} ASC");
  }

  protected MessageFormat createDescendOrderSyntax()
  {
    return new MessageFormat("{0} DESC");
  }
  
  protected MessageFormat createSelectMaxSyntax()
  {
    return new MessageFormat("SELECT MAX ({0}, {1}");
  }
  
  protected MessageFormat createSelectMinSyntax()
  {
    return new MessageFormat("SELECT MIN ({0}, {1}");
  }
  
  protected final MessageFormat getSelectSyntax()
  {
    return (MessageFormat)getComponent("select");
  }
  
  protected final MessageFormat getSelectDistinctSyntax()
  {
	return (MessageFormat)getComponent("selectDistinct");
  }
  
  protected final MessageFormat getSelectMaxSyntax()
  {
    return (MessageFormat)getComponent("selectMax");
  }
  
  protected final MessageFormat getSelectMinSyntax()
  {
    return (MessageFormat)getComponent("selectMin");
  }
  
  protected final MessageFormat getDeleteSyntax()
  {
    return (MessageFormat)getComponent("delete");
  }

  protected final MessageFormat getUpdateSyntax()
  {
    return (MessageFormat)getComponent("update");
  }

  protected final MessageFormat getInsertSyntax()
  {
    return (MessageFormat)getComponent("insert");
  }

  protected final MessageFormat getConditionSyntax()
  {
    return (MessageFormat)getComponent("condition");
  }

  protected final MessageFormat getOrderSyntax()
  {
    return (MessageFormat)getComponent("order");
  }

  protected final MessageFormat getAscendOrderSyntax()
  {
    return (MessageFormat)getComponent("ascendOrder");
  }

  protected final MessageFormat getDescendOrderSyntax()
  {
    return (MessageFormat)getComponent("descendOrder");
  }

  /**
   * @return the engineType
   */
  public String getEngineType()
  {
    return _engineType;
  }

  /**
   * @param engineType the engineType to set
   */
  public void setEngineType(String engineType)
  {
    _engineType = engineType;
  }
  
}