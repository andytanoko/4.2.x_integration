/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.activation.helpers;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.server.activation.entities.ejb.IActivationRecordLocalHome;
import com.gridnode.gtas.server.activation.entities.ejb.IActivationRecordLocalObj;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ActivationRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public final class ActivationRecordEntityHandler
  extends          LocalEntityHandler
{
  private final DataFilterImpl ACTIVE_FILTER = new DataFilterImpl();
  private final DataFilterImpl INACTIVE_FILTER = new DataFilterImpl();
  private final DataFilterImpl PENDING_FILTER  = new DataFilterImpl();

  private ActivationRecordEntityHandler()
  {
    super(ActivationRecord.ENTITY_NAME);
    setupFilters();
  }

	/**
   * Get an instance of a ActivationRecordEntityHandler.
   */
  public static ActivationRecordEntityHandler getInstance()
  {
    ActivationRecordEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ActivationRecord.ENTITY_NAME, true))
    {
      handler = (ActivationRecordEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ActivationRecord.ENTITY_NAME, true);
    }
    else
    {
      handler = new ActivationRecordEntityHandler();
      EntityHandlerFactory.putEntityHandler(ActivationRecord.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IActivationRecordLocalHome.class.getName(),
      IActivationRecordLocalHome.class);
  }*/
  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IActivationRecordLocalHome.class;
	}

  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IActivationRecordLocalObj.class;
  }

  // ********************** Own methods ******************************

  /**
   * Get GridNodeIDs from those Approval records.
   *
   * @return Collection of GridNode IDs.
   */
  public Collection getActiveGridNodeIDs() throws Throwable
  {
    Object[] results = getEntityByFilterForReadOnly(ACTIVE_FILTER).toArray();
    ArrayList gnIDs = new ArrayList();
    for (int i=0; i<results.length; i++)
    {
      ActivationRecord record = (ActivationRecord)results[i];
      gnIDs.add(record.getGridNodeID());
    }

    return gnIDs;
  }

  /**
   * Get GridNodeIDs from those Activation records.
   *
   * @return Collection of GridNode IDs.
   */
  public Collection getPendingGridNodeIDs() throws Throwable
  {
    Object[] results = getEntityByFilterForReadOnly(PENDING_FILTER).toArray();
    ArrayList gnIDs = new ArrayList();
    for (int i=0; i<results.length; i++)
    {
      ActivationRecord record = (ActivationRecord)results[i];
      gnIDs.add(record.getGridNodeID());
    }

    return gnIDs;
  }

  /**
   * Get GridNodeIDs from those Abortion/Denial/Deactivation records.
   *
   * @return Collection of GridNode IDs.
   */
  public Collection getInactiveGridNodeIDs() throws Throwable
  {
    Object[] results = getEntityByFilterForReadOnly(INACTIVE_FILTER).toArray();
    ArrayList gnIDs = new ArrayList();
    for (int i=0; i<results.length; i++)
    {
      ActivationRecord record = (ActivationRecord)results[i];
      gnIDs.add(record.getGridNodeID());
    }

    return gnIDs;
  }

  /**
   * Setup the data filters for re-use.
   */
  private void setupFilters()
  {
    ACTIVE_FILTER.addSingleFilter(null, ActivationRecord.CURRENT_TYPE,
      ACTIVE_FILTER.getEqualOperator(), new Short(ActivationRecord.TYPE_APPROVAL), false);
    ACTIVE_FILTER.addSingleFilter(ACTIVE_FILTER.getAndConnector(), ActivationRecord.IS_LATEST,
      ACTIVE_FILTER.getEqualOperator(), Boolean.TRUE, false);

    PENDING_FILTER.addSingleFilter(null, ActivationRecord.CURRENT_TYPE,
      PENDING_FILTER.getEqualOperator(), new Short(ActivationRecord.TYPE_ACTIVATION), false);
    PENDING_FILTER.addSingleFilter(PENDING_FILTER.getAndConnector(), ActivationRecord.IS_LATEST,
      PENDING_FILTER.getEqualOperator(), Boolean.TRUE, false);

    ArrayList inactiveTypes = new ArrayList();
    inactiveTypes.add(new Short(ActivationRecord.TYPE_ABORTION));
    inactiveTypes.add(new Short(ActivationRecord.TYPE_DEACTIVATION));
    inactiveTypes.add(new Short(ActivationRecord.TYPE_DENIAL));

    INACTIVE_FILTER.addDomainFilter(null, ActivationRecord.CURRENT_TYPE,
      inactiveTypes, false);
    INACTIVE_FILTER.addSingleFilter(INACTIVE_FILTER.getAndConnector(), ActivationRecord.IS_LATEST,
      INACTIVE_FILTER.getEqualOperator(), Boolean.TRUE, false);

  }
}