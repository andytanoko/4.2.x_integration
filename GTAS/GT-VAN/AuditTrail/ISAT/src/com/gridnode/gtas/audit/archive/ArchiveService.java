/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 4, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IArchiveServiceManagerLocalObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * 
 * @author Tam Wei Xiang 
 * @since GT VAN (GT 4.1.2)
 */
public class ArchiveService extends ServiceMBeanSupport implements
                                                       ArchiveServiceMBean
{
  private Logger _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, ArchiveService.class.getSimpleName());
  private Date _lastActivatedTime;
  
  private IArchiveServiceManagerLocalObj getArchiveServiceManager() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IArchiveServiceManagerLocalHome home = (IArchiveServiceManagerLocalHome)finder.lookup(
                                                IArchiveServiceManagerLocalHome.class.getName(), IArchiveServiceManagerLocalHome.class);
    return (IArchiveServiceManagerLocalObj)home.create();
  }
  
  public void checkArchiveStatus() throws Exception
  {
    IArchivalInitiator init = ArchiveInitiatorFactory.getInstance().getArchivalInitiator();
    init.checkArchiveStatus();
  }
  
  public void setLastActivateTime(Date date)
  {
    _lastActivatedTime = date;
  }
 
  public Date getLastActivateTime()
  {
    return _lastActivatedTime;
  }
}
