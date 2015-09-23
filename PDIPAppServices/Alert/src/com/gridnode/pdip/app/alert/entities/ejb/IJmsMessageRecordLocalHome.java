/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsMessageRecordLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.alert.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;


/**
 * Local home interface for JmsMessageRecord bean.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public interface IJmsMessageRecordLocalHome
	extends EJBLocalHome
{
	/**
	 * Create a new JmsMessageRecord
	 * @param jmsMessageRecord
	 * @return
	 * @throws CreateException
	 */
	public IJmsMessageRecordLocalObj create(IEntity jmsMessageRecord)
		throws CreateException;
	
	/**
	 * Find JmsMessageRecordLocalObj using PK
	 * @param primaryKey
	 * @return
	 * @throws FinderException
	 */
	public IJmsMessageRecordLocalObj findByPrimaryKey(Long primaryKey)
		throws FinderException;
	
	/**
	 * Find a collection of JmsMessageRecordLocalObj using filter
	 * @param filter
	 * @return
	 * @throws FinderException
	 */
	public Collection findByFilter(IDataFilter filter)
		throws FinderException;
}
