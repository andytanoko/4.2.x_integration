/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.partnerfunction.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.gridnode.gtas.server.partnerfunction.entities.ejb.IPartnerFunctionLocalHome;
import com.gridnode.gtas.server.partnerfunction.entities.ejb.IPartnerFunctionLocalObj;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the PartnerFunctionBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class PartnerFunctionEntityHandler
  extends          LocalEntityHandler
{
  private PartnerFunctionEntityHandler()
  {
    super(PartnerFunction.ENTITY_NAME);
  }

  /**
   * Get an instance of a PartnerFunctionEntityHandler.
   */
  public static PartnerFunctionEntityHandler getInstance()
  {
    PartnerFunctionEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(PartnerFunction.ENTITY_NAME, true))
    {
      handler = (PartnerFunctionEntityHandler)EntityHandlerFactory.getHandlerFor(
                  PartnerFunction.ENTITY_NAME, true);
    }
    else
    {
      handler = new PartnerFunctionEntityHandler();
      EntityHandlerFactory.putEntityHandler(PartnerFunction.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * This method overwrited the method from its parent class cause we need
   * to load the workflow activities into the partner function.
   */
  public IEntity getEntityByKeyForReadOnly(Long uId) throws Exception
  {
    PartnerFunction pf = null;
    IEntity entity = super.getEntityByKeyForReadOnly(uId);
    if (entity != null)
    {
      pf = (PartnerFunction)entity;
      pf = retreiveWorkflowActivites(pf);
    }
    return pf;
  }

  /**
   * Find the PartnerFunction whose Id is the specified.
   *
   * @param partnerFunctionId The name of the PartnerFunction.
   * @return the PartnerFunction having the specified Id.
   */
  public PartnerFunction findByPartnerFunctionId(String partnerFunctionId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      PartnerFunction.PARTNER_FUNCTION_ID,
      filter.getEqualOperator(),
      partnerFunctionId,
      false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (PartnerFunction)result.iterator().next();
  }

  /**
   * This method overwrited the method from its parent class cause we need
   * to load the workflow activities into the partner function.
   */
  public Collection getEntityByFilterForReadOnly(IDataFilter filter)
    throws Exception
  {
    Vector pfs = new Vector();
    Collection entities = super.getEntityByFilterForReadOnly(filter);
    if (!entities.isEmpty())
    {
      for (Iterator i = entities.iterator(); i.hasNext(); )
      {
        PartnerFunction pf = (PartnerFunction)i.next();
        pf = retreiveWorkflowActivites(pf);
        pfs.add(pf);
      }
    }
    return pfs;
  }

  /**
   * This method retreives the workflow activities belonging to the partner
   * function and set them into it.
   */
  private PartnerFunction retreiveWorkflowActivites(PartnerFunction pf)
    throws Exception
  {
    pf.clearWorkflowActivities();
    List uids = pf.getWorkflowActivityUids();
    for (Iterator i = uids.iterator(); i.hasNext(); )
    {
      Long wfaUid = new Long(i.next().toString());
      WorkflowActivity wfa =
        (WorkflowActivity)getWorkflowActivityEntityHandler().getEntityByKeyForReadOnly(wfaUid);
      pf.addWorkflowActivity(wfa);
    }
    return pf;

  }

  /**
   * Get an instance of the WorkflowActivityEntityHandler.
   */
  private WorkflowActivityEntityHandler getWorkflowActivityEntityHandler()
  {
     return WorkflowActivityEntityHandler.getInstance();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPartnerFunctionLocalHome.class.getName(),
      IPartnerFunctionLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IPartnerFunctionLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPartnerFunctionLocalObj.class;
  }
}