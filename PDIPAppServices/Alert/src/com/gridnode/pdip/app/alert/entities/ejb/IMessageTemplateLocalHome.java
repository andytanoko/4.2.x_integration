/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageTemplateLocalHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 13 2002    Srinath	          Created
 */

package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * This interface defines the method of creating the EJBobject interface for the MessageTemplateBean.
 *
 * @author Srinath
 *
 */

public interface IMessageTemplateLocalHome extends EJBLocalHome
{
  /**
   * Create a new MessageTemplate for Alert.
   *
   * @param message The MessageTemplate entity.
   * @return EJBLocalObject as a proxy to MessageTemplateBean for the created MessageTemplate.
   */
  public IMessageTemplateLocalObj create(IEntity message) throws CreateException;

  /**
   * Load a MessageTemplateBean
   *
   * @param primaryKey The primary key to the Message record
   * @return EJBLocalObject as a proxy to the loaded MessageTemplateBean.
   */
  public IMessageTemplateLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Message records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IMessageTemplateLocalObjs for the found Message.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}