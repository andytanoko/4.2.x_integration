/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GlobalInitialiser.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Andrew Hill         Created
 * 2002-10-25     Andrew Hill         Modify to init GlobalContext as a singleton
 *                                    instead of using servlet context to store
 * 2003-01-09     Andrew Hill         Logging, Initialise TimeServer & TimeUtil
 * 2003-03-03     Andrew Hill         Dont log the utcOffset here (dont want to invoke ctrl lookup)
 * 2003-03-31     Andrew Hill         Init config of pageSize for listview paging
 * 2003-07-01     Andrew Hill         Modify init() to suit 1.1 final signature
 * 2003-07-02     Andrew Hill         Enforce a positive page size
 */
package com.gridnode.gtas.client.web.strutsbase;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.GTSessionFactory;
import com.gridnode.gtas.client.ctrl.GlobalContext;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.pdip.framework.util.ITimeServer;
import com.gridnode.pdip.framework.util.TimeUtil;

public class GlobalInitialiser implements PlugIn
{
  private static final Log _log = LogFactory.getLog(GlobalInitialiser.class); // 20031209 DDJ
  
  private static final int DEFAULT_PAGE_SIZE = 20; //20030702AH
  
  private ServletContext _context;
  private int _pageSize = DEFAULT_PAGE_SIZE; //20030702AH

  public void destroy()
  {
    
  }

  public void setPageSize(int pageSize)
  { //20030331AH
    if(pageSize < 1)
    { //20030702AH - Prevent zero or negative page size
      if(_log.isWarnEnabled())
      {
        _log.warn("Page size ("
                  + pageSize
                  + ") is less than 1 and has been reset to the default size of "
                  + DEFAULT_PAGE_SIZE
                  + " rows");
      }
      pageSize = DEFAULT_PAGE_SIZE;
    }
    _pageSize = pageSize;
  }

  public int getPageSize()
  { //20030331AH
    return _pageSize;
  }

  public void init(ActionServlet servlet, ModuleConfig config) throws ServletException //20030701AH - Use ModuleConfig parameter
  {
    try
    {
      if(_log.isInfoEnabled())
      {
        _log.info("Initialising GlobalContext object");
      } 

      //_context = servlet.getServletContext();
      GlobalContext globalContext = GlobalContext.getInstance();
      globalContext.setDefaultPageSize( getPageSize() ); //20030331AH
      //_context.setAttribute(IGlobals.GLOBAL_CONTEXT, globalContext);

      //20030901AH
      try
      {
        if(_log.isInfoEnabled())
        {
          _log.info("Initialising shared TimeServer instance in TimeUtil class");
        }

        IGTSession gtasSession = GTSessionFactory.getSession( IGTSession.SESSION_DEFAULT,
                                                              globalContext);
        ITimeServer timeServer = gtasSession.getTimeServer();
        TimeUtil.setTimeServer(timeServer);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error providing TimeServer instance to TimeUtil", t);
      }
      //....
    }
    catch(Throwable t)
    {
      if(_log.isErrorEnabled())
      {
        _log.error("Exception caught in GlobalInitialiser PlugIn", t);
      }
      throw new ServletException("Error in GlobalInitialiser", t);
    }
  }
}