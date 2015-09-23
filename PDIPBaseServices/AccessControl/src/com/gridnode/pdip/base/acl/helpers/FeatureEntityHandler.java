/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FeatureEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 20 2002    Goh Kan Mun             Created
 * Jul 25 2002    Neo Sok Lay             Remove dependency on ejbEntityMap.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.base.acl.helpers;

import java.util.Collection;

import com.gridnode.pdip.base.acl.entities.ejb.IFeatureLocalHome;
import com.gridnode.pdip.base.acl.entities.ejb.IFeatureLocalObj;
import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the FeatureBean.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FeatureEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = Feature.ENTITY_NAME;
  private static final Object lock = new Object();

  public FeatureEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of FeatureEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static FeatureEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new FeatureEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of RoleEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static FeatureEntityHandler getFromEntityHandlerFactory()
  {
    return (FeatureEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IFeatureLocalHome.class.getName(),
      IFeatureLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IFeatureLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IFeatureLocalObj.class;
  }

  // ********************** Own methods *******************************

  /**
   * Find the Feature with the specified feature name.
   *
   * @param feature The name of the feature.
   *
   * @return The Feature that has the specified feature name, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Feature getFeatureByFeatureName(String featureName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Feature.FEATURE, filter.getEqualOperator(),
      featureName, false);

//    try
//    {
      Collection result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return (Feature) result.iterator().next();
//    }
//    catch (Throwable t)
//    {
//      ACLLogger.errorLog("FeatureEntityHandler", "getFeatureByFeatureName",
//                         "Unable to retrieve feature with name = " + featureName, t);
//      throw new Exception(t.getMessage());
//    }
  }

  /**
   * Find the feature with the specified uId.
   *
   * @param uId The uId of the feature.
   *
   * @return The Feature that has the specified feature name, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
//  public Feature getFeatureByUId(Long uId) throws Throwable
//  {
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, Feature.UID, filter.getEqualOperator(),
//      uId, false);
//
//    try
//    {
//      Collection result = getEntityByFilterForReadOnly(filter);
//      if (result == null || result.isEmpty())
//        return null;
//      return (Feature) result.iterator().next();
//    }
//    catch (Throwable t)
//    {
//      ACLLogger.errorLog("FeatureEntityHandler", "getFeatureByUId",
//                         "Unable to retrieve Feature with uId = " + uId , t);
//      throw new Exception(t.getMessage());
//    }
//  }

  public Collection getAllFeatures() throws Throwable
  {
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, Feature.UID, filter.getEqualOperator(),
//      null, true);
//    try
//    {
      return getEntityByFilterForReadOnly(null);
//    }
//    catch (Throwable t)
//    {
//      ACLLogger.errorLog("FeatureEntityHandler", "getAllFeatures",
//                         "Unable to retrieve all Features." , t);
//      throw new Exception(t.getMessage());
//    }
  }

}