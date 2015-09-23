/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubstitutionList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 2003    Neo Sok Lay         Created
 * Apr 22 2003    Neo Sok Lay         Add RecipientListData data provider.
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.providers.*;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class provides a Map of object names and corresponding fieldname list
 * for use as substitution markers in the MessageTemplate.
 * <p>
 * The available substitution objects are potential data providers. This class
 * takes care of default system data providers, as well as custom data providers
 * from other modules.
 * <p>Custom data providers must be specified in the "alert.providers" properties.
 * As a rule, each custom data provider class must define the following:<p>
 * <ul>
 *   <li>Name of the data provider defined as a <b>public static final String NAME</b> field.</li>
 *   <li>Method for getting the list of available field names defined as
 *       <b>public static List getFieldNameList()<b></li>
 * </ul>
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I7
 */
public class SubstitutionList
{
  private static final Object _lock = new Object();
  private static SubstitutionList _self = null;

  private static final String PROVIDER_PROPS = "alert.providers";
  private static final String NUM_PROVIDERS  = "num.providers";
  private static final String METHOD_GETFNLIST = "getFieldNameList";
  private static final String FIELD_NAME       = "NAME";
  private static final String PREFIX           = "provider.";

  private HashMap _providerMap = new HashMap();

  private SubstitutionList()
  {
    initDefault();
    initCustom();
  }

  public static SubstitutionList getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new SubstitutionList();
      }
    }

    return _self;
  }

  /**
   * Get a Map of [Key] Object Name (String) and [Value] FieldName list
   * (Collection/List).
   */
  public Map getSubstitutionMap()
  {
    return Collections.unmodifiableMap(_providerMap);
  }

  private void initDefault()
  {
    _providerMap.put(SystemDetails.NAME, SystemDetails.getFieldNameList());
    _providerMap.put(ExceptionDetails.NAME, ExceptionDetails.getFieldNameList());
    _providerMap.put(RecipientListData.NAME, RecipientListData.getFieldNameList());
    _providerMap.put(ProcessingErrorData.NAME, ProcessingErrorData.getFieldNameList());
    // to initialise the default system data providers
  }

  protected void initCustom()
  {
    // to initialise the custom data providers from other modules.
    try
    {
      Configuration config = ConfigurationManager.getInstance().getConfig(PROVIDER_PROPS);

      int numProviders = config.getInt(NUM_PROVIDERS, 0);
      for (int i=0; i<numProviders; i++)
      {
        String provider = config.getString(PREFIX+i, null);
        if (provider != null)
        {
          Class providerClass = findProviderClass(provider);
          if (providerClass != null)
            loadProvider(providerClass);
        }
      }
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_ALERT_INITIALISE,
                   "[SubstitutionList.initCustom] Unexpected Error: "+ex.getMessage(), ex);
    }
  }

  protected final Class findProviderClass(String providerClassName)
  {
    Class providerClass = null;
    ClassLoader cl = getClass().getClassLoader();
    do
    {
      try
      {
        providerClass = Class.forName(providerClassName, true, cl);
      }
      catch (Exception ex)
      {
      }
      cl = cl.getParent();
    }
    while (providerClass == null && cl != null);

    return providerClass;
  }

  protected final void loadProvider(Class providerClass)
  {
    try
    {
      Field field = providerClass.getField(FIELD_NAME);
      String providerName = (String)field.get(null);

      Method method = providerClass.getMethod(METHOD_GETFNLIST, new Class[0]);
      List fieldNameList = (List)method.invoke(null, new Object[0]);

      _providerMap.put(providerName, fieldNameList);
    }
    catch (Exception ex)
    {
      Logger.warn("[SubstitutionList.loadProvider] Fail: "+ex.getMessage());
    }

  }
}