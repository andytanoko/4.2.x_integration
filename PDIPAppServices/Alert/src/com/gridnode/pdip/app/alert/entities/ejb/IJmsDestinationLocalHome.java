/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsDestinationLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.alert.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * Local home interface for JmsDestination bean.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public interface IJmsDestinationLocalHome
	extends EJBLocalHome
{
	/**
	 * Create a new JmsDestination
	 * @param jmsDestination
	 * @return
	 * @throws CreateException
	 */
	public IJmsDestinationLocalObj create(IEntity jmsDestination)
		throws CreateException;
	
	/**
	 * Find JmsDestinationLocalObj using PK
	 * @param primaryKey
	 * @return
	 * @throws FinderException
	 */
	public IJmsDestinationLocalObj findByPrimaryKey(Long primaryKey)
		throws FinderException;
	
	/**
	 * Find a collection of JmsDestinationLocalObj using filter
	 * @param filter
	 * @return
	 * @throws FinderException
	 */
	public Collection findByFilter(IDataFilter filter)
		throws FinderException;
}
