/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Mahesh              Created
 * Apr 25 2002    Neo Sok Lay         Add methods to retrieve entity by a filter.
 * Apr 26 2002    Neo Sok Lay         Add descriptions of specific errors thrown.
 * May 02 2002    Neo Sok Lay         Add method to get the count of entities.
 * Jan 05 2004    Mahesh              Added new create method to create entity with uid
 *                                    passed with entity
 * Oct 21 2005    Neo Sok Lay         Support select distinct by fields for single field
 *                                    as well as entity. 
 * Jun 09 2007    Tam Wei Xiang       Support Select MIN, MAX                                                                     
 */
package com.gridnode.pdip.framework.db.dao;

import java.util.Collection;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
 
public interface IEntityDAO {

    /**
     * This method will be called by ejbCreate method of the entity bean.
     * This method should take care of creating unique id for this entity record,
     * and returning the primary key
     *
     * @param entity
     * @return Long
     * @throws Exception
     * @exception SystemException System service errors
     * @exception ApplicationException Invalid input data error.
     */
    public Long create(IEntity entity) throws Exception;

		/**
		 * This method will be called by ejbCreate method of the entity bean.
		 * This method will not take care of creating unique id for this entity
		 * record if useUID id true and returning the primary key
		 *
		 * @param entity
		 * @return Long
		 * @throws Exception
		 * @exception SystemException System service errors
		 * @exception ApplicationException Invalid input data error.
		 */
		public Long create(IEntity entity,boolean useUID) throws Exception;


    /**
     * This method will be called by ejbLoad method of the entity bean.
     * This method should load the entity with this passed primary key from presistance store
     * and return the entity
     *
     * If the entity consists of another dependent entity then the instance of the dependent entity should be
     * created and primary key of that dependent entity should be set, it should not set all the dependent entity data.
     * So that it leaves the option for bean developer to do entity lazy loading if required
     *
     * @param primaryKey
     * @return IEntity
     * @throws Exception
     * @exception SystemException System service errors
     * @exception ApplicationException Requested data record not found in database.
     */
    public IEntity load(Long primaryKey) throws Exception;

    /**
     * This method will be called by ejbStore method of the entity bean.
     * This method should update the entity in the presistance store.
     *
     * If the entity consists of dependent entity then the primary key of the
     * dependent entity is set
     *
     * @param entity
     * @throws Exception
     * @exception SystemException System service errors.
     */
    public void store(IEntity entity) throws  Exception;

    /**
     * This method will be called by ejbRemove method of entity bean.
     * This method should remove the entity from presistance store with passed primary key
     *
     * @param primaryKey
     * @throws Exception
     * @exception SystemException System service errors
     */
    public void remove(Long primaryKey) throws  Exception;

    /**
     * This method will be called by ejbFindByPrimaryKey method or any business methods of the entity bean.
     * This method should check wether the entity exists in the presistance store,
     * and return the primary key.If the entity does not exists then it should throw
     * exception
     *
     * @param primaryKey
     * @return Long
     * @throws Exception
     * @exception SystemException System service errors
     * @exception ApplicationException Requested data record not found in database.
     */
    public Long findByPrimaryKey(Long primaryKey) throws Exception;

    /**
     * This method will be called by finder method or any business methods of the entity bean.
     * This method returns the Collection of primary keys satisfied by the filter criteria,else
     * it will return the empty Collection object
     *
     * @param filter
     * @return Collection
     * @throws Exception
     * @exception SystemException System service errors.
     */
    public Collection findByFilter(IDataFilter filter) throws Exception;

    /**
     * Retrieve a Collection of Entity objects based on a filtering condition.
     *
     * @param filter The filtering condition.
     * @return a Collection of Entity objects retrieved based on the specified
     * filter, or empty colleciton if no objects satisfied the condition.
     * @exception SystemException System service errors.
     */
    public Collection getEntityByFilter(IDataFilter filter) throws Exception;

    /**
     * Get the count of number of entities that satisfy a filtering condition.
     *
     * @param filter The filtering condition. <B>null</B> if none.
     * @return The number of entities that satisfy a filtering condition. If no
     * filtering condition is specified, the total number of entities is returned.
     *
     * @exception SystemException System service errors.
     */
    public int getEntityCount(IDataFilter filter) throws Exception;

    /**
   * Get the values for a particular field
   * @param fieldId The field for whose values to return
   * @param filter The filter condition for the selection. If distinct is specified,
   * the returned results would contain only unique values.
   * @return Collection of values for the specified field
   * @throws Exception
     */
    public Collection getFieldValuesByFilter(Number fieldId,IDataFilter filter) throws Exception;
    
    /**
     * Get the min value for a particular field and allowed to specify filter 
     * @param fieldId The field for whose values to return
     * @param filter The filter condition for the selection.
     * @return a value for the specified field
     * @throws Exception
     */
    public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception;
    
    /**
     * Get the max value for a particular field and allowed to specify filter 
     * @param fieldId The field for whose values to return
     * @param filter The filter condition for the selection.
     * @return a value for the specified field
     * @throws Exception
     */
    public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception;
}