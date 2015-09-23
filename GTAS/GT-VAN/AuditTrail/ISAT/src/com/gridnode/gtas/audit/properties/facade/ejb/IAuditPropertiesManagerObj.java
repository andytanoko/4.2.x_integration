/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditPropertiesManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 22, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.properties.facade.ejb;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.EJBObject;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditPropertiesManagerObj extends EJBObject
{
  /**
   * Get the properties that belong to the category
   * @param category The properties's category
   * @return
   */
  public Properties getAuditProperties(String category) throws RemoteException;

  /**
   * Get the value as identified by the category and propertyKey. If no value can be found, 
   * the given defValue is return.
   * @param category
   * @param propertyKey
   * @param defValue
   * @return
   */
  public String getAuditProperties(String category, String propertyKey, String defValue) throws RemoteException;
}
