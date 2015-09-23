/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveProxyManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 7, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import javax.ejb.SessionContext;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;

import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class serves as a proxy to the GT archive module
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveProxyManagerBean implements SessionBean
{
  private static final String CLASS_NAME = ArchiveProxyManagerBean.class.getSimpleName();
  private Logger _logger;
  
  public Long getEarliestProcessStartDate(Hashtable criteria) throws Exception
  {
    String mn = "getEarliestProcessStartDate";
    
    Long earliestDate = null;
    IEntityDAO dao = getEntityDAO(GWFArchiveProcess.ENTITY_NAME);
    Collection processUIDs = (Collection)dao.getMinValuesByFilter(GWFArchiveProcess.UID, null);
    if(processUIDs != null && processUIDs.size() > 0)
    {
      Long processUID = (Long)processUIDs.iterator().next();
      if(processUID != null)
      {
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, GWFArchiveProcess.UID, filter.getEqualOperator(), processUID, false); 
      
        Collection archiveProcesses = dao.getEntityByFilter(filter);
        if(archiveProcesses != null && archiveProcesses.size() > 0)
        {
          GWFArchiveProcess process = (GWFArchiveProcess)archiveProcesses.iterator().next();
          Date processStartTime = process.getProcessStartTime();
        
          _logger.logMessage(mn, null, "Earliest date is "+processStartTime);
          if(processStartTime != null)
          {
            earliestDate = processStartTime.getTime();
            earliestDate = TimeUtil.utcToLocal(earliestDate);
          }
        }
      }
    }
    
    return earliestDate;
  }
  
  public Long getEarlistGDocDateTimCreate(Hashtable criteria) throws Exception
  {
    String mn = "getEarlistGDocDateTimCreate";
    
    Long earliestDate = null;
    IEntityDAO dao = getEntityDAO(GridDocument.ENTITY_NAME);
    Collection gdocUIDs = (Collection)dao.getMinValuesByFilter(GridDocument.UID, null);
    if(gdocUIDs != null && gdocUIDs.size() > 0)
    {
      Long gdocUID = (Long)gdocUIDs.iterator().next();
      
      if(gdocUID != null)
      {
        _logger.logMessage(mn, null, "Retrieve Gdoc is "+gdocUID);
      
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, GridDocument.UID, filter.getEqualOperator(), gdocUID, false);
        Collection gdocs = dao.getEntityByFilter(filter);
        if(gdocs.size() > 0)
        {
          GridDocument gdoc = (GridDocument)gdocs.iterator().next();
          Date dateTimeCreate = gdoc.getDateTimeCreate();
          if(dateTimeCreate != null)
          {
            _logger.logMessage(mn, null, "GDoc earliest date is "+dateTimeCreate);
            earliestDate = dateTimeCreate.getTime();
            earliestDate = TimeUtil.utcToLocal(earliestDate);
          }
        }
      }
    }
    
    return earliestDate;
  }
  
  private IEntityDAO getEntityDAO(String entityName)
  {
    return EntityDAOFactory.getInstance().getDAOFor(entityName);
  }
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove() throws EJBException, RemoteException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext arg0) throws EJBException,
                                                    RemoteException
  {
    // TODO Auto-generated method stub

  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}
