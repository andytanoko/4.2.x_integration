/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 14 2003    Neo Sok Lay         Add method: findByProcessActFilter(IDataFilter)
 * Nov 03 2003    Neo Sok Lay         Fix defect: GNDB00016047
 *                                    - findByProcessActFilter should not retrieve
 *                                      all ProcessDef if processActList is empty.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.rnif.helpers;

import com.gridnode.pdip.app.rnif.entities.ejb.IProcessDefLocalHome;
import com.gridnode.pdip.app.rnif.entities.ejb.IProcessDefLocalObj;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ProcessDef.
 */

public final class ProcessDefEntityHandler
  extends          LocalEntityHandler
{
  private ProcessDefEntityHandler()
  {
    super(ProcessDef.ENTITY_NAME);
  }

  /**
   * Get an instance of a ProcessDefEntityHandler.
   */
  public static ProcessDefEntityHandler getInstance()
  {
    ProcessDefEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ProcessDef.ENTITY_NAME, true))
    {
      handler = (ProcessDefEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ProcessDef.ENTITY_NAME, true);
    }
    else
    {
      handler = new ProcessDefEntityHandler();
      EntityHandlerFactory.putEntityHandler(ProcessDef.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IProcessDefLocalHome.class.getName(),
      IProcessDefLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IProcessDefLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IProcessDefLocalObj.class;
  }

  // ********************** Own methods ******************************

  /**
   * Find the ProcessDef whose Name is the specified.
   *
   * @return The ProcessDef having the specified ID, or <B>null</B> if
   * none found.
   */
  public ProcessDef findByProcessDefName(String defId)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcessDef.DEF_NAME, filter.getEqualOperator(),
      defId, false);

    Collection result = findByFilter(filter);
    if(result == null || result.isEmpty())
      return null;
    IProcessDefLocalObj defObj = (IProcessDefLocalObj)result.iterator().next();
    return (ProcessDef)defObj.getData();
  }



  /**
   * Create a new ProcessDef.
   *
   * @param def The ProcessDef entity.
   * @return The created ProcessDef.
   */
  public ProcessDef createProcessDef(ProcessDef def) throws Throwable
  {

    ProcessDefDAOHelper.getInstance().checkDuplicate(def, false);
    ProcessAct  act = def.getRequestAct();
    if(act == null)
    {
      act = new ProcessAct();
      def.setRequestAct(act);
    }
    return (ProcessDef)createEntity(def);
  }

  /**
   * Update a ProcessDef to the database.
   *
   * @param def The ProcessDef with changes.
   */
  public void updateProcessDef(ProcessDef def) throws Throwable
  {
    ProcessDefDAOHelper.getInstance().checkDuplicate(def, true);
    update(def);
  }

  //use ProcessDefDAOHelper as the EntityDAO.
  //everything will be taken care of by the ProcessDefDAOHelper
  protected IEntityDAO getDAO()
  {
    return ProcessDefDAOHelper.getInstance();
  }

  /**
   * Find ProcessDef entities using ProcessAct filter condition.
   * 
   * @param processActFilter Filtering condition on ProcessAct entities.
   * @return Collection ProcessDef entities that are referenced by the
   * ProcessAct entities found using the processActFilter.
   * @since GT 2.2 I1
   */
  public Collection findByProcessActFilter(IDataFilter processActFilter)
    throws Throwable
  {
    Collection processActList = ProcessDefDAOHelper.getInstance().getProcessActDAO().getEntityByFilter(processActFilter);
    
    // don't try to retrieve if processActList is empty
    if (!processActList.isEmpty())
    {
      ProcessAct processAct;
      ArrayList defUids = new ArrayList();
      for (Iterator i=processActList.iterator(); i.hasNext(); )
      {
        processAct = (ProcessAct)i.next();
        defUids.add(processAct.getProcessDefUid());
      }
      
      DataFilterImpl processDefFilter = new DataFilterImpl();
      processDefFilter.addDomainFilter(null, ProcessDef.UID, defUids, false);
      return getEntityByFilterForReadOnly(processDefFilter);
    }
    else
      return processActList;
  }
}