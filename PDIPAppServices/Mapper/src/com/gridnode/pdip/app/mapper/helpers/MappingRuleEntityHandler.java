/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingRuleEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 01 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Mar 07 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.mapper.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.mapper.entities.ejb.IMappingRuleLocalHome;
import com.gridnode.pdip.app.mapper.entities.ejb.IMappingRuleLocalObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the MappingRuleBean.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public final class MappingRuleEntityHandler
  extends          LocalEntityHandler
{
  private MappingRuleEntityHandler()
  {
    super(MappingRule.ENTITY_NAME);
  }

  /**
   * Get an instance of a MappingRuleEntityHandler.
   */
  public static MappingRuleEntityHandler getInstance()
  {
    MappingRuleEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(MappingRule.ENTITY_NAME, true))
    {
      handler = (MappingRuleEntityHandler)EntityHandlerFactory.getHandlerFor(
                  MappingRule.ENTITY_NAME, true);
    }
    else
    {
      handler = new MappingRuleEntityHandler();
      EntityHandlerFactory.putEntityHandler(MappingRule.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the MappingRule whose name is the specified.
   *
   * @param mappingRuleName The name of the MappingRule.
   * @return the MappingRule having the specified name.
   */
  public MappingRule findByMappingRuleName(String mappingRuleName)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           MappingRule.NAME,
                           filter.getEqualOperator(),
                           mappingRuleName,
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (MappingRule)result.iterator().next();
  }

  /**
   * Find the MappingRule whose type is the specified.
   *
   * @param mappingRuleType The type of the MappingRule to
   *        retrieve.
   * @return the MappingRules having the specified type.
   */
  public Collection<MappingRule> findByMappingRuleType(Short mappingRuleType)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           MappingRule.TYPE,
                           filter.getEqualOperator(),
                           mappingRuleType,
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    return result;
  }

  /**
   * Find the MappingRule whose MappingFile is the specified.
   *
   * @param mappingFile The MappingFile of the MappingRule to
   *        retrieve.
   * @return the MappingRules having the specified MappingFile.
   */
  public Collection<MappingRule> findByMappingFile(MappingFile mappingFile)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           MappingRule.MAPPING_FILE,
                           filter.getEqualOperator(),
                           mappingFile.getKey(),
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    return result;
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IMappingRuleLocalHome.class.getName(),
      IMappingRuleLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IMappingRuleLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IMappingRuleLocalObj.class;
  }
}