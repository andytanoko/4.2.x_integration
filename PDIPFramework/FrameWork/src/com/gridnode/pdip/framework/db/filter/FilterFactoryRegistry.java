/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FilterFactoryRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * This class defines a registry that keeps a list of all available
 * DataFilterFactories in the system.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public class FilterFactoryRegistry
  implements Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3381541553134902944L;

	private static final String FACTORIES =
          "com.gridnode.pdip.framework.db.filter.SQLFilterFactory," +
          "com.gridnode.pdip.framework.db.filter.OQLFilterFactory," +
          "com.gridnode.pdip.framework.db.filter.DataFilterFactory";

  private static Hashtable _factories;

  /**
   * Get the filter factory with the specified name.
   *
   * @param name The name of the factory to get
   * @return The factory of the specified name. <B>null</B> if no factory
   * exists with that name.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static DataFilterFactory getFilterFactory(String name)
  {
    load();
    return (DataFilterFactory) _factories.get( name );
  }

  /**
   * Get the names of the filter factories in the registry.
   *
   * @return An array of the names of the available filter factories
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String[] getFactoryNames()
  {
    load();
    /*
    String[] names   = new String[ _factories.size() ];
    Enumeration enum = _factories.keys();
    for ( int i = 0 ; i < names.length ; ++i )
      names[ i ] = (String) enum.nextElement();
    return names;*/
    return (String[])_factories.keySet().toArray(new String[_factories.size()]);

  }

  /**
   * Loads the available filter factories into the registry.
   *
   * @since 1.0a build 0.9.9.6
   */
  private static synchronized void load()
  {
    if ( _factories == null )
    {
       _factories = new Hashtable();
       String prop = FACTORIES;
       StringTokenizer tokenizer = new StringTokenizer( prop, ", " );
       while ( tokenizer.hasMoreTokens() ) {
         prop = tokenizer.nextToken();
         try {
           Class cls = Class.forName( prop );
           DataFilterFactory factory = (DataFilterFactory) cls.newInstance();
           _factories.put( factory.getFactoryName(), factory );
         }
         catch ( Exception ex )
         {
           ex.printStackTrace(System.out);
         }
      }
    }
  }
}