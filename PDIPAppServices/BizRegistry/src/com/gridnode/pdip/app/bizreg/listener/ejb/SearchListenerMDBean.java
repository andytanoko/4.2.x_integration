/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchListenerMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.listener.ejb;

import com.gridnode.pdip.app.bizreg.helpers.Logger;
import com.gridnode.pdip.app.bizreg.search.SearchRegistryHandler;
import com.gridnode.pdip.app.bizreg.search.SearchRequestNotification;
import com.gridnode.pdip.app.bizreg.search.SearchResultsNotification;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.util.Collection;

/**
 * This MDB listens to the Notifier topic for notifications of Search "PublicRegistry"
 * activities.<p>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SearchListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 578422678765909554L;
	private MessageDrivenContext _mdx = null;

  public SearchListenerMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove() 
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      Object notification = ((ObjectMessage)message).getObject();
      if (notification instanceof SearchRequestNotification)
      {
        handleNotification((SearchRequestNotification)notification);
      }
      else if (notification instanceof SearchResultsNotification)
      {
        handleNotification((SearchResultsNotification)notification);
      }
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the SearchRequestNotification
   * 
   * @param notification The SearchRequestNotification to process.
   */
  private void handleNotification(SearchRequestNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      // delegate to the SearchRegistryHandler
      SearchRegistryHandler.getInstance().performSearch(
        notification.getSearchId(),
        (SearchRegistryCriteria)notification.getSearchCriteria());
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the SearchResultsNotification
   * 
   * @param notification The SearchResultsNotification to process.
   */
  private void handleNotification(SearchResultsNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      // checks if exception has occurred
      // delegates to SearchRegistryHandler to handle acccordingly
      if (notification.isException())
      {
        SearchRegistryHandler.getInstance().onSearchException(
          notification.getSearchId(),
          notification.getSearchException());
      }
      else
      {
        SearchRegistryHandler.getInstance().onSearchResultsReturned(
          notification.getSearchId(),
          (Collection)notification.getSearchResults());
      }
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

}