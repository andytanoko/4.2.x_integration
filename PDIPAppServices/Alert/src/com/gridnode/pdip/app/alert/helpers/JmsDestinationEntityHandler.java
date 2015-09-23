/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005   Tam Wei Xiang       Created
 * Mar 03 2006    Neo Sok Lay         Use generics
 * Apr 19 2006    Neo Sok Lay         GNDB00026982: Config entity importer uses
 *                                    the common method createEntity() and updateEntity()
 *                                    to insert or update the record, but here addition
 *                                    of retry timer is outside these two methods.
 */
package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import javax.ejb.CreateException;

import com.gridnode.pdip.app.alert.entities.ejb.IJmsDestinationLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IJmsDestinationLocalObj;
import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
/**
 * This class handle the call to the methods in local obj interface and home interface of
 * JmsDestination entity bean.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsDestinationEntityHandler
	extends LocalEntityHandler
{
	
	private final String JMS_RETRY_TIMER_CATEGORY="JmsRetryTimer";
	private final String JMS_ENTITY_NAME = JmsDestination.ENTITY_NAME;
	
	private JmsDestinationEntityHandler()
	{
		super(JmsDestination.ENTITY_NAME);
	}
	
	public static synchronized JmsDestinationEntityHandler getInstance()
	{
		JmsDestinationEntityHandler handler = null;
		if(EntityHandlerFactory.hasEntityHandlerFor(JmsDestination.ENTITY_NAME, true))
		{
			handler = (JmsDestinationEntityHandler)EntityHandlerFactory.getHandlerFor(JmsDestination.ENTITY_NAME, true);
		}
		else
		{
			handler = new JmsDestinationEntityHandler();
			EntityHandlerFactory.putEntityHandler(JmsDestination.ENTITY_NAME, true,
			                                      handler);
		}
		
		return handler;
	}
	
	/**
	 * Insert a new JmsDestination into DB. A new jms retry timer will be created as well.
	 * JmsDestination's name is the unique key for the table jms_destination.
	 * @param jmsDest
	 * @return the primary key of jms destination 
	 * @throws SystemException
	 * @throws CreateEntityException
	 * @throws DuplicateEntityException
	 * @throws Exception
	 */
	public Long createJmsDestination(JmsDestination jmsDest)
		throws SystemException, CreateEntityException, DuplicateEntityException, Exception
	{
		Long pk = null;
		try
		{
			if(isDuplicate(jmsDest.getName()))
			{
				Exception ex = new Exception("JmsDestination with name "+jmsDest.getName()+" already exists in DB.");
				throw new DuplicateEntityException("[JmsDestinationEntityHandler.createJmsDestination] Duplicate jmsDestination found.", ex);
			}
			AlertLogger.debugLog("JmsDestinationEntityHandler", "createJmsDestination", "create Jms dest.");
			pk = (Long)createEntity(jmsDest).getKey();
		}
		catch (CreateEntityException ex)
		{
			throw ex;
		}
		catch(CreateException ex)
		{
			throw new CreateEntityException("[JmsDestinationEntityHandler.createJmsDestination] Unable to create JmsDestination with name "+ jmsDest.getName(), ex);
		}
		catch(Throwable ex)
		{
			throw new SystemException("[JmsDestinationEntityHandler.createJmsDestination] Unexpected error occured.", ex);
		}
		/*NSL20060419 Move to createEntity(entity)
		if(jmsDest.getMaximumRetries() != 0)
    {
    	Long retryInterval = new Long(jmsDest.getRetryInterval()*60);
    	addJmsRetryTimer(retryInterval, jmsDest.getName());
    }*/
		return pk;
	}
	
	
	/**
	 * This method does not check for duplicate before insertion
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#createEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity createEntity(IEntity entity) throws Throwable
	{
		JmsDestination jmsDest = (JmsDestination)entity;
		entity = super.createEntity(jmsDest);
		
		//add retry timer
		if(jmsDest.getMaximumRetries() != 0)
		{
			Long retryInterval = new Long(jmsDest.getRetryInterval()*60);
			addJmsRetryTimer(retryInterval, jmsDest.getName());
		}
		return entity;
	}

	/**
	 * This method does not check for duplicate Name with existing record
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
		JmsDestination jmsDest = (JmsDestination)entity;
		
    //NSL20060216 Check with the old JmsDestination, since the Name may change
    JmsDestination oldJmsDest = findByUID(new Long(jmsDest.getUId()));
		boolean isJmsTimerExist = isExistJmsTimer(oldJmsDest.getName(),JMS_RETRY_TIMER_CATEGORY, 
		                			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
		
		if(jmsDest.getMaximumRetries() == 0 && isJmsTimerExist)
		{
			//jms timer will be deleted
			removeJmsRetryTimer(oldJmsDest.getName(), JMS_RETRY_TIMER_CATEGORY, 
			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
		}
		else if(jmsDest.getMaximumRetries()!=0 && !isJmsTimerExist)
		{
			//a new jmsTimer will be created.
			addJmsRetryTimer(new Long(jmsDest.getRetryInterval()*60), jmsDest.getName());
		}
		else if(jmsDest.getMaximumRetries()!=0 && isJmsTimerExist)
		{
			//update the exisitng iCalAlarm whether or not the retry interval of
			//JmsDestination has been changed.
			iCalAlarm alarm = this.getJmsRetryTimer(oldJmsDest.getName(),JMS_RETRY_TIMER_CATEGORY, 
			              			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
			alarm.setDelayPeriod(new Long(jmsDest.getRetryInterval()*60));
      alarm.setTaskId(jmsDest.getName());
			ServiceLookupHelper.getICalManager().updateAlarm(alarm, false);
		}
    return super.updateEntity(jmsDest);
	}

	/**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public void update(IEntity entity) throws Throwable
	{
		updateEntity(entity);
	}

	/**
	 * Update the Jms destination obj into DB. The correspond jms retry timer
	 * will be updated accordingly.
	 * @param jmsDest JmsDestination to update
	 */
	public void updateJmsDestination(JmsDestination jmsDest)
		throws Exception
	{	
    //NSL20060216 Check for duplicate
    JmsDestination existingJmsDest = findByJmsDestName(jmsDest.getName());
    if (existingJmsDest != null && jmsDest.getUId() != existingJmsDest.getUId())
    {
      Exception ex = new Exception("JmsDestination with name "+jmsDest.getName()+" already exists in DB.");
      throw new DuplicateEntityException("[JmsDestinationEntityHandler.updateJmsDestination] Duplicate jmsDestination found.", ex);
    }
    
    /*NSL20060419 Move to updateEntity(entity)

    //NSL20060216 Check with the old JmsDestination, since the Name may change
    JmsDestination oldJmsDest = findByUID(new Long(jmsDest.getUId()));
		boolean isJmsTimerExist = isExistJmsTimer(oldJmsDest.getName(),JMS_RETRY_TIMER_CATEGORY, 
		                			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
		
		if(jmsDest.getMaximumRetries() == 0 && isJmsTimerExist)
		{
			//jms timer will be deleted
			removeJmsRetryTimer(oldJmsDest.getName(), JMS_RETRY_TIMER_CATEGORY, 
			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
		}
		else if(jmsDest.getMaximumRetries()!=0 && !isJmsTimerExist)
		{
			//a new jmsTimer will be created.
			addJmsRetryTimer(new Long(jmsDest.getRetryInterval()*60), jmsDest.getName());
		}
		else if(jmsDest.getMaximumRetries()!=0 && isJmsTimerExist)
		{
			//update the exisitng iCalAlarm whether or not the retry interval of
			//JmsDestination has been changed.
			iCalAlarm alarm = this.getJmsRetryTimer(oldJmsDest.getName(),JMS_RETRY_TIMER_CATEGORY, 
			              			                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
			alarm.setDelayPeriod(new Long(jmsDest.getRetryInterval()*60));
      alarm.setTaskId(jmsDest.getName());
			ServiceLookupHelper.getICalManager().updateAlarm(alarm, false);
		}*/
		try
		{
			//super.updateEntity(jmsDest);
			updateEntity(jmsDest);
		}
		catch(Throwable ex)
		{
			throw new UpdateEntityException("[JmsDestinationEntityHandler.updateJmsDestination] Unable to perform update on JmsDestination with name "+ jmsDest.getEntityDescr(), ex);
		}
	}
	
	/**
	 * Delete the jms destination record from DB given the uid.
	 * The correspond jms retry timer will be deleted as well.
	 * @param uid PK of jms destination record.
	 * @throws DeleteEntityException
	 */
	public void deleteJmsDestinationByUID(Long uid)
		throws DeleteEntityException
	{
		try
		{
			JmsDestination jmsDest = (JmsDestination)getEntityByKeyForReadOnly(uid);
			if(jmsDest.canDelete())
			{
				super.remove(uid);
				if(jmsDest.getMaximumRetries() != 0)
				{
					this.removeJmsRetryTimer(jmsDest.getName(), JMS_RETRY_TIMER_CATEGORY, 
								                    JMS_ENTITY_NAME, JMS_ENTITY_NAME);
				}
			}
			else
			{
				throw new ApplicationException("[JmsDestinationEntityHandler.deleteJmsDestinationByUID] JmsDestination with name "+jmsDest.getEntityDescr() +" not allowed to be deleted");
			}
		}
		catch(Throwable th)
		{
			throw new DeleteEntityException("[JmsDestinationEntityHandler.deleteJmsDestinationByUID] Unable to delete JmsDestination with UID "+uid, th);
		}
	}
	
	/**
	 * Find jms destination obj given the uid
	 * @param uid jms destination's PK
	 * @return The JmsDestination found
	 * @throws FindEntityException
	 */
	public JmsDestination findByUID(Long uid)
		throws FindEntityException
	{
		try
		{
			/*
			DataFilterImpl filter = new DataFilterImpl();
			filter.addSingleFilter(null, JmsDestination.UID, 
		                       filter.getEqualOperator(), uid, false);
			Collection result = super.getEntityByFilterForReadOnly(filter);
			if(result !=null && result.size() > 0)
			{
				return (JmsDestination)result.iterator().next();
			}
			return null;
			*/
			return (JmsDestination)getEntityByKeyForReadOnly(uid);
		}
		catch (ApplicationException ex)
		{
			//record not found
			return null;
		}
		catch(Exception ex)
		{
			throw new FindEntityException("[JmsDestinationEntityHandler.findByUID] Error retrieving JmsDestination with UID "+ uid, ex);
		}
	}
	
	/**
	 * Retrieve a collection of JmsDestination objs from DB given the filter.
	 * @param filter
	 * @return Collectio of JmsDestination(s) found
	 * @throws Exception
	 */
	public Collection<JmsDestination> findJmsDestinationByFilter(IDataFilter filter)
		throws Exception
	{
		return super.getEntityByFilterForReadOnly(filter);
	}
	
	/**
	 * Locate the JmsDestination obj given the jmsDestName.
	 * @param jmsDestName The name of JmsDestination. It is the unique key for table jms_destination
	 * @return JmsDestination found
	 * @throws FindEntityException
	 */
	public JmsDestination findByJmsDestName(String jmsDestName)
		throws FindEntityException
	{
		try
		{
			DataFilterImpl filter = new DataFilterImpl();
			filter.addSingleFilter(null, JmsDestination.NAME, 
		                       filter.getEqualOperator(), jmsDestName, false);
			Collection<JmsDestination> result = super.getEntityByFilterForReadOnly(filter);
			if(result !=null && result.size() > 0)
			{
				return result.iterator().next();
			}
			return null;
		}
		catch(Exception ex)
		{
			throw new FindEntityException("Unable to locate JmsDestination with name "+ jmsDestName, ex);
		}
	}
	
	/**
	 * Check whether the Jms Destination obj is existed in DB.
	 * @param jmsDestName Unique key for table jms_destination.
	 * @return <b>true</b> if JmsDestination already exists, <b>false</b> otherwise.
	 * @throws Exception
	 */
	private boolean isDuplicate(String jmsDestName)
		throws Exception
	{
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, JmsDestination.NAME, filter.getEqualOperator(), 
		                       jmsDestName, false);
		if(super.getEntityCount(filter) > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether the jms retry timer exist in DB given the jms Destination name.
	 * While we create the iCalAlarm, the task ID is the jmsDestname.
	 * @param jmsDestName
	 * @return <b>true</b> if the timer already exists, <b>false</b> otherwise
	 */
	private boolean isExistJmsTimer(String jmsDestName, String category, String senderKey,
	                                String receiverKey)
		throws Exception
	{
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, iCalAlarm.TASK_ID, 
		                       filter.getEqualOperator(), jmsDestName, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, 
			                     filter.getEqualOperator(), category, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.SENDER_KEY, 
			                     filter.getEqualOperator(), senderKey, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, 
			                     filter.getEqualOperator(), receiverKey, false);
		Collection result = ServiceLookupHelper.getICalManager().findAlarms(filter);
		if(result!=null && result.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Remove away the jms retry timer given the taskID which is the jms destination name.
	 * @param taskID The jms destination name.
	 * @param category The category that jms retry timer belong. In this case, it is 'JmsRetryTimer'
	 * @param senderKey jms destination entity name
	 * @param receiverKey jms destination entity name
	 * @throws DeleteEntityException
	 */
	private void removeJmsRetryTimer(String taskID, String category, String senderKey,
	                                 String receiverKey)
		throws DeleteEntityException
	{
		try
		{
			DataFilterImpl filter = new DataFilterImpl();
			filter.addSingleFilter(null, iCalAlarm.TASK_ID,
		                       filter.getEqualOperator(), taskID, false);
			filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, 
			                     filter.getEqualOperator(), category, false);
			filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.SENDER_KEY, 
			                     filter.getEqualOperator(), senderKey, false);
			filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, 
			                     filter.getEqualOperator(), receiverKey, false);
			ServiceLookupHelper.getICalManager().cancelAlarmByFilter(filter);
		}
		catch(Exception ex)
		{
			throw new DeleteEntityException("[JmsDestinationEntityHandler.removeJmsRetryTimer] Unable to remove jms retry timer with task ID "+ taskID, ex);
		}
	}
	
	/**
	 * Retrieve a particular iCalAlarm obj given the taskID which is the jmsDestination name
	 * @param taskID The jms destination name.
	 * @return The retry timer found
	 * @throws Exception
	 */
	private iCalAlarm getJmsRetryTimer(String taskID, String category, String senderKey,
	                                   String receiverKey)
		throws Exception
	{
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, iCalAlarm.TASK_ID, 
		                       filter.getEqualOperator(), taskID, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, 
			                     filter.getEqualOperator(), category, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.SENDER_KEY, 
			                     filter.getEqualOperator(), senderKey, false);
		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, 
			                     filter.getEqualOperator(), receiverKey, false);
		Collection<iCalAlarm> result = ServiceLookupHelper.getICalManager().findAlarms(filter);
		if(result!=null && result.size() > 0)
		{
			return result.iterator().next();
		}
		return null;
	}
	
	/**
	 * Create a new jms retry timer into DB. 
	 * @param retryInterval
	 * @param jmsDestName
	 * @throws Exception
	 */
	private void addJmsRetryTimer(Long retryInterval, String jmsDestName)
		throws Exception
  {
		iCalAlarm alarm = new iCalAlarm();
		alarm.setCategory(JMS_RETRY_TIMER_CATEGORY);
		alarm.setTaskId(jmsDestName);
		alarm.setSenderKey(JMS_ENTITY_NAME);
		alarm.setReceiverKey(JMS_ENTITY_NAME);
		alarm.setDelayPeriod(retryInterval); //setDelayPeriod expect time in milli second
	
		alarm.setDisabled(Boolean.FALSE);
		ServiceLookupHelper.getICalManager().addAlarm(alarm);
  }
	
	/**
	 * Return the proxy interface class (LocalObj) of JmsDestinationBean
	 */
	protected Class getProxyInterfaceClass()
	{
		return IJmsDestinationLocalObj.class;
	}
	
	/**
	 * Return the home interface of JmsDestinationBean
	 */
	protected Class getHomeInterfaceClass()
	{
		return IJmsDestinationLocalHome.class;
	}
	
}
