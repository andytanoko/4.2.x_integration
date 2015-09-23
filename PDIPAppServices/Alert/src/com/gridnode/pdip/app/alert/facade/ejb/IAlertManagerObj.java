/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 * Jan 23 2003    Neo Sok Lay             Removed methods for Trigger entity.
 *                                        Renamed Category to AlertCategory.
 * Feb 05 2003    Neo Sok Lay             All create methods return the uid of
 *                                        the created entity.
 * Mar 03 2003    Neo Sok Lay             Throw AlertTriggeringException if
 *                                        error encountered during triggerAlert().
 * Apr 23 2003    Neo Sok Lay             Add getAlertTypeByName().
 *                                        Throw RemoteException for all methods.
 * Apr 25 2003    Neo Sok Lay             Add triggerAlert() by AlertUID.
 * Jul 10 2003    Neo Sok Lay             Add getAlertActionsByActionUId() method.
 * Jul 14 2003    Neo Sok Lay             Add methods:
 *                                        getActionKeys(IDataFilter),
 *                                        getAlertKeys(IDataFilter),
 *                                        getMessageTemplateKeys(IDataFilter)
 * Jan 04 2006    Tam Wei Xiang           Added methods:
 *                                        createJmsDestination(JmsDestination)
 *                                        updateJmsDestination(JmsDestination)
 *                                        deleteJmsDestination(Long) 
 *                                        getJmsDestinations(Filter)
 *                                        getJmsDestinationByUID(Long)  
 *                                        getJmsDestinationKeys(Filter)   
 * Mar 01 2006    Neo Sok Lay             Use generics.                                                                        
 */
package com.gridnode.pdip.app.alert.facade.ejb;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.alert.exceptions.AlertTriggeringException;
import com.gridnode.pdip.app.alert.helpers.JmsDestinationEntityHandler;
import com.gridnode.pdip.app.alert.model.*;
import com.gridnode.pdip.app.alert.providers.IProviderList;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * This interface defines the services defined in the AlertManagerNean
 *
 * @author Srinath
 *
 */

public interface IAlertManagerObj extends EJBObject
{
  /**
   * To retrieve a <code>Collection</code> of all <code>Alert</code> entities.
   */

  public Long createAlert(Alert alert)
    throws    CreateEntityException, SystemException, RemoteException;

  public Collection<Alert> getAlerts(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public Collection<Long> getAlertKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public void updateAlert(Alert alert)
    throws    UpdateEntityException, SystemException, RemoteException;

  public void deleteAlert(Long AlertUId)
    throws    DeleteEntityException, SystemException, RemoteException;

  public Alert getAlertByAlertName(String alertName)
    throws       FindEntityException, SystemException, RemoteException;
/*
  public Alert getAlertByAlertType(String alertType)
    throws       FindEntityException, SystemException;
*/
  public Alert getAlertByAlertUId(Long alertUId)
    throws       FindEntityException, SystemException, RemoteException;

  public Alert getAlertByCategoryCode(String categoryCode)
    throws       FindEntityException, SystemException, RemoteException;

  public Long createAction(Action action)
    throws    CreateEntityException, SystemException, RemoteException;

  public void updateAction(Action action)
    throws    UpdateEntityException, SystemException, RemoteException;

  public void deleteAction(Long ActionUId)
    throws    DeleteEntityException, SystemException, RemoteException;

  public Collection<Action> getActions(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public Collection<Long> getActionKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public Action getActionByActionName(String actionName)
    throws       FindEntityException, SystemException, RemoteException;

  public Action getActionByActionUId(Long actionUId)
    throws       FindEntityException, SystemException, RemoteException;

  public Long createAlertCategory(AlertCategory category)
    throws    CreateEntityException, SystemException, RemoteException;

  public Collection<AlertCategory> getAlertCategories(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public AlertCategory getAlertCategoryByUId(Long UId)
    throws       FindEntityException, SystemException, RemoteException;

  public Long createMessageTemplate(MessageTemplate msgTemplate)
    throws    CreateEntityException, SystemException, RemoteException;

  public void updateMessageTemplate(MessageTemplate msg)
    throws    UpdateEntityException, SystemException, RemoteException;

  public void deleteMessageTemplate(Long UId)
    throws    DeleteEntityException, SystemException, RemoteException;

  public Collection<MessageTemplate> getMessageTemplates(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public Collection<Long> getMessageTemplateKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public MessageTemplate getMessageTemplateByName(String messageName)
    throws       FindEntityException, SystemException, RemoteException;

  public MessageTemplate getMessageTemplateByUId(Long UId)
    throws       FindEntityException, SystemException, RemoteException;

  public Long createAlertAction(AlertAction alertAction)
    throws    CreateEntityException, SystemException, RemoteException;

  public void updateAlertAction(AlertAction alertAction)
    throws    UpdateEntityException, SystemException, RemoteException;

  public void deleteAlertAction(Long UId)
    throws    DeleteEntityException, SystemException, RemoteException;

  public Collection<AlertAction> getAlertActionsByAlertUId(Long alertUid)
    throws       FindEntityException, SystemException, RemoteException;

  public Collection<AlertAction> getAlertActionsByActionUId(Long actionUId)
    throws       FindEntityException, SystemException, RemoteException;
    
  public Collection<AlertType> getAlertTypes(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public AlertType getAlertTypeByUId(Long UId)
    throws       FindEntityException, SystemException, RemoteException;

  public AlertType getAlertTypeByName(String name)
    throws       FindEntityException, SystemException, RemoteException;

/*
  public void createTrigger(Trigger action)
    throws    CreateEntityException, SystemException;

  public Action getTriggerByTriggerLevel(int level)
    throws       FindEntityException, SystemException;
*/
  public List triggerAlert(String alertName, IProviderList providerList,
    InputStream fileAttachmentInputStream)
    throws AlertTriggeringException, SystemException, RemoteException;

  public List triggerAlert(String alertName, IProviderList providerList,
    String fileLocation)
    throws AlertTriggeringException, SystemException, RemoteException;

  public List triggerAlert(Long alertUID, IProviderList providerList,
    String fileLocation) throws AlertTriggeringException, SystemException, RemoteException;

  public Long createAlertList(AlertList alertList)
    throws    CreateEntityException, SystemException, RemoteException;

  public Collection<AlertList> getAlertLists(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  public int getAlertListsByUserUId(Long userUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve the emails of all users that assume a particular role.
   *
   * @param roleName The name of the Role
   * @return Collection of Email Ids (addresses). Empty addresses are excluded.
   *
   * @since 2.0 I7
   */
  public Collection<String> getMailIdsFromRole(String roleName)
    throws SystemException, RemoteException;


  public EmailConfig getEmailConfig() 
    throws SystemException, RemoteException;
    
  public void updateEmailConfig(EmailConfig emailConfig) 
    throws SystemException, RemoteException;
 
  /**
   * Create a new JmsDestination obj and insert into DB.
   * A new jms retry timer will be created as well.
   * @param jmsDest
   * @return The UID of JmsDestination obj.
   * @throws SystemException
   * @throws CreateEntityException
   * @throws RemoteException
   */
  public Long createJmsDestination(JmsDestination jmsDest)
		throws SystemException, CreateEntityException, RemoteException;
  
  /**
   * Update the existing record in DB(that has the same UID as jmsDest) with the 
   * new values which obtained from jmsDest.
   * 
   * The correspond jms retry timer will be updated accordingly.
   * @param jmsDest
   * @throws SystemException
   * @throws UpdateEntityException
   * @throws RemoteException
   */
  public void updateJmsDestination(JmsDestination jmsDest)
		throws SystemException, UpdateEntityException, RemoteException;
  
  /**
   * Delete the JmsDestination DB record given the UID.
   * The correspond jms retry timer will be deleted as well.
   * @param jmsDestUID PK of jms_destination table
   * @throws SystemException
   * @throws DeleteEntityException
   * @throws RemoteException
   */
  public void deleteJmsDestination(Long jmsDestUID)
		throws SystemException, DeleteEntityException, RemoteException;
  
  /**
   * Retrieve a collection of JmsDestination objs from DB given the filter.
   * @return a collection of JmsDestination obj or null if no record exist in db.
   * @throws SystemException
   * @throws FindEntityException
   * @throws RemoteException
   */
  public Collection<JmsDestination> getJmsDestinations(IDataFilter filter)
		throws SystemException, FindEntityException, RemoteException;
  
  /**
   * Retrieve a collection of JmsDestination keys given the filter
   * @param filter
   * @return a collection of jms destination keys or null if no record exist in db.
   * @throws FindEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public Collection<Long> getJmsDestinationKeys(IDataFilter filter)
		throws FindEntityException, SystemException, RemoteException;
  
  /**
   * Retrieve a particular JmsDestination obj given its UID
   * @param UID PK of jms_destination table
   * @return JmsDestination obj or null if no such record exist in db.
   * @throws SystemException
   * @throws FindEntityException
   * @throws RemoteException
   */
  public JmsDestination getJmsDestinationByUID(Long UID)
		throws SystemException, FindEntityException, RemoteException;
  
  /**
   * Added by Ming Qian for Beyonics GridOne
   */
  public JmsDestination retrieveJmsDestination(String jmsDestName)
    throws Exception, RemoteException;
  
  public Collection<JmsMessageRecord> getAssociatedJmsMessageRecord(long jmsDestUid)
    throws Exception, RemoteException;
  
  public void updateFailedMessage(JmsMessageRecord jmsMsgRecord , JmsDestination jmsDest, int maxRetries)
  throws Exception, RemoteException;
  
  public void handleFailedMessages(Collection<JmsMessageRecord> failedMessages, JmsDestination jmsDest, int maxRetries)
  throws Exception, RemoteException;
  
  public void deleteJmsMessageRecord(JmsMessageRecord msgRecord)
  throws Exception, RemoteException;
}