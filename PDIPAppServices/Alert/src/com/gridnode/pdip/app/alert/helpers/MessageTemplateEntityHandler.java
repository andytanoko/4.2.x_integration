/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageTemplateEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath		              Created
 * Mar 03 2003    Neo Sok Lay             Check if canDelete during remove.
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 * Mar 03 2006    neo Sok Lay             Use generics                                   
 */

package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IMessageTemplateLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IMessageTemplateLocalObj;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the MessageTemplateBean.
 *
 * @author Srinath
 *
 */

public class MessageTemplateEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = MessageTemplate.ENTITY_NAME;
  private static final Object lock = new Object();

  public MessageTemplateEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of MessageTemplateEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static MessageTemplateEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new MessageTemplateEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of MessageTemplateEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static MessageTemplateEntityHandler getFromEntityHandlerFactory()
  {
    return (MessageTemplateEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IMessageTemplateLocalHome.class.getName(),
      IMessageTemplateLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IMessageTemplateLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IMessageTemplateLocalObj.class;
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    MessageTemplate template = (MessageTemplate)getEntityByKeyForReadOnly(uId);
    if (template.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("MessageTemplate not allowed to be deleted");
  }

  /********************** Own methods *******************************/

  /**
   * Get all the MessageTemplates.
   *
   * @return Collection of all the MessageTemplates.
   *
   * @exception Throwable thrown when an error occurs.
   */
  public Collection<MessageTemplate> getAllMessageTemplates() throws Throwable
  {
      return getEntityByFilterForReadOnly(null);
  }


  /**
   * Find the MessageTemplate with the specified MessageTemplate name.
   *
   * @param messageName The name of the message.
   *
   * @return The message that has the specified messageName , or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   */
  public MessageTemplate getMessageTemplateByName(String messageName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MessageTemplate.NAME, filter.getEqualOperator(), messageName, false);
    Collection<MessageTemplate> result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return result.iterator().next();
  }
}