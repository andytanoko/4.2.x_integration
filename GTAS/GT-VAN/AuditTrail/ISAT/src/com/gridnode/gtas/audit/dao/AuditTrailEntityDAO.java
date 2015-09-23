/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailEntityDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 28, 2006    Tam Wei Xiang       Created
 * Jan 08, 2007    Tam Wei Xiang       Change the UID from Long to String
 */
package com.gridnode.gtas.audit.dao;

import org.hibernate.exception.ConstraintViolationException;

import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.db.DAO;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is the abstract class for the DAO that handle the OTC entity.
 * NOTE: the caller require to under the JTA transaction. EG call from SessionBean, MDBean
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public abstract class AuditTrailEntityDAO extends DAO
{
  private static final String CLASS_NAME = "AuditTrailEntityDAO";
  private Logger _logger = null;
  
  public AuditTrailEntityDAO()
  {
    _logger = getLogger(); 
  }
  
  public AuditTrailEntityDAO(boolean newSession)
  {
    super(newSession);
    _logger = getLogger();
  }
  
  public String insertAuditTrailEntity(IAuditTrailEntity entity)
  {
    Object[] params = new Object[]{entity};
    String uid = (String)create(entity);
    return uid;
  }
  
  /**
   * Load the Persistent class as define in the concrete DAO given the entityUID.
   * @param entityUID
   * @return
   */
  public IAuditTrailEntity loadAuditTrailEntity(String entityUID)
  {
    return (IAuditTrailEntity)load(getPersistenceClass(), entityUID);
  }
  
  /**
   * Use while loading the persistent class other than the default Persistent Class that a DAO handle.
   * @param persistentClass
   * @param entityUID
   * @return
   */
  public IAuditTrailEntity loadAuditTrailEntity(Class persistentClass, String entityUID)
  {
    return (IAuditTrailEntity)load(persistentClass, entityUID);
  }
  
  /**
   * Get the class that the concrete DAO is handling.
   * @return
   */
  public abstract Class getPersistenceClass();
  
  /**
   * Check whether the given ex is the Hibernate's ConstraintViolationException
   * @param ex The exception that will be checked
   * @return true if it is an hibernate's constraint violation exception.
   */
  public boolean isConstraintViolation(Throwable ex)
  {
    if(ex == null)
    {
      return false;
    }
    
    if(ex != null && ex instanceof ConstraintViolationException)
    {
      return true;
    }
    else
    {
      return isConstraintViolation(ex.getCause());
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
