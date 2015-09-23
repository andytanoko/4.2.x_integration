/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditPropertiesManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 22, 2007    Tam Wei Xiang       Created
 * Jan 15, 2008    Tam Wei Xiang       Refine the log category
 */
package com.gridnode.gtas.audit.properties.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.SessionContext;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;

import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class facilitate the retrieval of the properties that is required in OTC.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class AuditPropertiesManagerBean implements SessionBean
{
  private SessionContext _ctxt = null;
  private Logger _logger = null;
  private static final String CLASS_NAME = "AuditPropertiesManager";
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
  }

  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }
  
  /**
   * Get the properties that belong to the category
   * @param category The properties's category
   * @return
   */
  public Properties getAuditProperties(String category)
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    Properties pro = configStore.getProperties(category);
    _logger.debugMessage("getAuditProperties", null, "Properties retireve for category "+category+" "+pro);
    return pro;
  }
  
  /**
   * Get the value as identified by the category and propertyKey. If no value can be found, 
   * the given defValue is return.
   * @param category
   * @param propertyKey
   * @param defValue
   * @return
   */
  public String getAuditProperties(String category, String propertyKey, String defValue)
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String propertyValue = configStore.getProperty(category, propertyKey, defValue);
    _logger.debugMessage("getAuditProperties", null, "Properties retireve for [category "+category+" propertyKey"+ propertyKey+" defValue "+defValue+"]"+propertyValue);
    return propertyValue;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
