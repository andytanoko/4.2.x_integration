/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWebServiceLocalHome.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public interface IWebServiceLocalHome extends EJBLocalHome
{
  /**
   * Create a new WebService.
   *
   * @param serviceAssignment The WebService entity.
   * @return EJBLocalObject as a proxy to WebServiceBean for the created WebService.
   */
  public IWebServiceLocalObj create(IEntity serviceAssignment) throws CreateException;

  /**
   * Load a WebServiceBean
   *
   * @param primaryKey The primary key to the WebService record
   * @return EJBLocalObject as a proxy to the loaded WebServiceBean.
   */
  public IWebServiceLocalObj findByPrimaryKey(Long primaryKey) throws FinderException;

  /**
   * Find WebService records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IWebServiceLocalObjs for the found WebServices.
   */
  public Collection findByFilter(IDataFilter filter) throws FinderException;

}
