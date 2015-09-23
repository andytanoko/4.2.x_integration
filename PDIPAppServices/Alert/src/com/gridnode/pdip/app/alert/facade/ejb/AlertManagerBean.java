/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertManagerBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 * Jan 23 2003    Neo Sok Lay             Removed methods for Trigger entity.
 *                                        Renamed Category to AlertCategory.
 * Feb 05 2003    Neo Sok Lay             All create methods return the uid of
 *                                        the created entity.
 * Feb 06 2003    Neo Sok Lay             Change Id to Uid.
 * Mar 03 2003    Neo Sok Lay             Refactor to use FacadeLogger, feedback
 *                                        exception more accurately.
 * Apr 23 2003    Neo Sok Lay             Add getAlertTypeByName().
 * Apr 25 2003    Neo Sok Lay             Add triggerAlert() by AlertUID.
 * Jun 20 2003    Neo Sok Lay             Invoke AlertActor to trigger alert instead
 *                                        of AlertEntityHandler.
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
 * Feb 28 2006    Neo Sok Lay             Use generics. Cleanup javadoc.            
 */
package com.gridnode.pdip.app.alert.facade.ejb;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.alert.engine.AlertActor;
import com.gridnode.pdip.app.alert.exceptions.AlertTriggeringException;
import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.*;
import com.gridnode.pdip.app.alert.mail.AlertMailService;
import com.gridnode.pdip.app.alert.model.*;
import com.gridnode.pdip.app.alert.providers.IProviderList;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.config.ConfigurationManager;


/**
 * This beans manages the Alert.
 *
 * @author Srinath
 *
 */

public class AlertManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1659292715674480215L;
	
	transient private SessionContext _sessionContext = null;

  public AlertManagerBean()
  {
  }

  /* *****************************SessionBean Interfaces ***************************** */

  public void setSessionContext(SessionContext sessionContext)
  {
    _sessionContext = sessionContext;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  	_sessionContext = null;
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /* *****************************Service provide by this class ***************************** */

  /**
   * To create a new <code>Alert</code> entity.
   *
   * @param  alert The new <code>Alert</code> entity.
   * @return	   UID of the create Alert.
   * @exception CreateEntityException Thrown when the create operation fails.
   *
   */
  public Long createAlert(Alert alert)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createAlert";
    Object[] params = new Object[] {
                          alert,
                      };
    Long uid = null;

    try
    {
      logger.logEntry(method, params);
      uid = (Long)AlertEntityHandler.getInstance().createEntity(alert).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }


  /**
   * To update a <code>Alert</code> entity.
   *
   * @param           alert    the modified <code>Alert</code> entity.
   * @exception       UpdateEntityException thrown when an error occurs updating the <code>Alert</code> entity.
   * @since 2.0
   * @version 2.0
   */
  public void updateAlert(Alert alert)
    throws    UpdateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateAlert";
    Object[] params = new Object[] {
                          alert,
                      };

    try
    {
      logger.logEntry(method, params);

      AlertEntityHandler.getInstance().update(alert);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To remove a <code>Alert</code> entity with the specified AlertUId.
   *
   * @param           alertUId    the uId of the <code>Alert</code> entity.
   * @exception  DeleteEntityException    thrown when an error occurs removing the <code>Alert</code> entity.
   * @since 2.0
   * @version 2.0
   */
  public void deleteAlert(Long alertUId)
    throws    DeleteEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "deleteAlert";
    Object[] params = new Object[] {
                          alertUId,
                      };
    try
    {
      logger.logEntry(method, params);

      AlertEntityHandler.getInstance().remove(alertUId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

 /**
   * To retrieve a <code>Collection</code> of <code>Alert</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>Alert</code> entities.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>Alert</code> entities.
   * @since 2.0
   * @version 2.0
   */
  public Collection<Alert> getAlerts(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlerts";
    Object[] params = new Object[] {
                          filter,
                      };

    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = AlertEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }

 /**
   * To retrieve a <code>Collection</code> of UIDs of <code>Alert</code> entities.
   *
   * @param filter The filtering condition.
   * @return  <code>Collection</code> of UIDs of all <code>Alert</code> entities
   * that satisfy the filtering condition.
   * @exception  FindEntityException An error occurs retrieving the <code>Alert</code> entities.
   * @since GT 2.2 I1
   * @version GT 2.2 I1
   */
  public Collection<Long> getAlertKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertKeys";
    Object[] params = new Object[] {
                          filter,
                      };

    Collection<Long> results = null;
    try
    {
      logger.logEntry(method, params);
      results = AlertEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }

 /**
   * To retrieve a <code>Alert</code> entity by specified name.
   *
   * @param           alertName     the name of the <code>Alert</code> entity.
   * @return          the <code>Alert</code> entity with the specidied name.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>Alert</code> entity.
   * @since 2.0
   * @version 2.0
   */

  public Alert getAlertByAlertName(String alertName)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertByAlertName";
    Object[] params = new Object[] {
                          alertName,
                      };
    Alert alert = null;
    try
    {
      logger.logEntry(method, params);

      alert = AlertEntityHandler.getInstance().getAlertByAlertName(alertName);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return alert;
  }

 /*
   * To retrieve a <code>Alert</code> entity by specified name.
   *
   * @param           alertName     the name of the <code>Alert</code> entity.
   *
   * @return          the <code>Alert</code> entity with the specidied name.
   *
   * @exception       thrown when an error occurs retrieving the <code>Alert</code> entity.
   *
   * @since 2.0
   * @version 2.0
   */
/*
  public Alert getAlertByAlertType(String alertType)
    throws       FindEntityException, SystemException
  {
    try
    {
      AlertLogger.debugLog("AlertManagerBean", "getAlertByAlertType", "alertType: "+alertType);
      return AlertEntityHandler.getInstance().getAlertByAlertType(alertType);
    }
    catch (ApplicationException ex)
    {
      AlertLogger.errorLog("AlertManagerBean", "getAlertByAlertType", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      AlertLogger.errorLog("AlertManagerBean", "getAlertByAlertType", "System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      AlertLogger.errorLog("AlertManagerBean", "getAlertByAlertType", "Error ", ex);
      throw new SystemException(
        "AlertManagerBean.getAlertByAlertType(alertType) Error ",
        ex);
    }
    finally
    {
      AlertLogger.debugLog("AlertManagerBean", "getAlertByAlertType", "Exit ");
    }
  }
*/

 /**
   * To retrieve a <code>Alert</code> entity by specified uId.
   *
   * @param           alertUId      the uId of the <code>Alert</code> entity.
   * @return          the <code>Alert</code> entity with the specified uId.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>Alert</code> entity.
   */
  public Alert getAlertByAlertUId(Long alertUId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertByAlertUId";
    Object[] params = new Object[] {
                          alertUId,
                      };
    Alert alert = null;
    try
    {
      logger.logEntry(method, params);

      alert = (Alert)AlertEntityHandler.getInstance().getEntityByKeyForReadOnly(alertUId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return alert;
  }

 /**
   * To retrieve a <code>Alert</code> entity by specified name.
   *
   * @param           categoryCode     the name of the <code>AlertCategory</code> entity.
   * @return          the <code>Alert</code> entity with the specidied name.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>AlertCategory</code> entity.
   */

  public Alert getAlertByCategoryCode(String categoryCode)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertByCategoryCode";
    Object[] params = new Object[] {
                          categoryCode,
                      };

    Alert alert = null;
    try
    {
      logger.logEntry(method, params);

      AlertCategory category = AlertCategoryEntityHandler.getInstance().getAlertCategoryByCode(
                                 categoryCode);
      alert = (category != null)
              ? AlertEntityHandler.getInstance().getAlertByCategory((Long)category.getKey())
              : null;
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return alert;
  }

  /**
   * To create a new <code>Action</code> entity.
   *
   * @param  action The new <code>Action</code> entity.
   * @return	   UId of the created Action.
   * @exception  CreateEntityException  Thrown when the create operation fails.
   */
  public Long createAction(Action action)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createAction";
    Object[] params = new Object[] {
                          action,
                      };

    Long uid = null;
    try
    {
      logger.logEntry(method, params);
      uid = (Long)ActionEntityHandler.getInstance().createEntity(action).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }

  /**
   * To update a <code>Action</code> entity.
   *
   * @param action    The modified <code>Action</code> entity.
   * @exception       UpdateEntityException thrown when an error occurs updating the <code>Action</code> entity.
   */
  public void updateAction(Action action)
    throws    UpdateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateAction";
    Object[] params = new Object[] {
                          action,
                      };
    try
    {
      logger.logEntry(method, params);

      ActionEntityHandler.getInstance().update(action);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To remove a <code>Action</code> entity with the specified ActionUId.
   *
   * @param actionUId    The uId of the <code>Action</code> entity.(Type : Long)
   * @exception  DeleteEntityException thrown when an error occurs removing the <code>Action</code> entity.
   */
  public void deleteAction(Long actionUId)
    throws    DeleteEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "deleteAction";
    Object[] params = new Object[] {
                          actionUId,
                      };
    try
    {
      logger.logEntry(method, params);

      ActionEntityHandler.getInstance().remove(actionUId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

 /**
   * To retrieve a <code>Collection</code> of <code>Action</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>Action</code> entities.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>Action</code> entities.
   */
  public Collection<Action> getActions(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getActions";
    Object[] params = new Object[] {
                          filter,
                      };

    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = ActionEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }

 /**
   * To retrieve a <code>Collection</code> of the UIDs of <code>Action</code> entities.
   *
   * @param filter The filtering condition.
   * @return <code>Collection</code> of UIDs of all <code>Action</code> entities that
   * satisfy the filtering condition.
   * @exception FindEntityException an error occurs retrieving the <code>Action</code> entities.
   * @since GT 2.2 I1
   */
  public Collection<Long> getActionKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getActionKeys";
    Object[] params = new Object[] {
                          filter,
                      };

    Collection<Long> results = null;
    try
    {
      logger.logEntry(method, params);
      results = ActionEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }

  /**
   * To retrieve a <code>Action</code> entity by specified name.
   *
   * @param  actionName the name of the <code>Action</code> entity.
   * @return the <code>Action</code> entity with the specidied name.
   * @exception       FindEntityException thrown when an error occurs retrieving the <code>Action</code> entity.
   */
  public Action getActionByActionName(String actionName)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getActionByActionName";
    Object[] params = new Object[] {
                          actionName,
                      };

    Action action = null;
    try
    {
      logger.logEntry(method, params);

      action = ActionEntityHandler.getInstance().getActionByActionName(actionName);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return action;
  }

  /**
   * To retrieve a <code>Action</code> entity by specified uId.
   *
   * @param   actionUId   actionUId-the uId of the <code>Action</code> entity.
   * @return          the <code>Action</code> entity with the specified uId.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>Action</code> entity.
   */
  public Action getActionByActionUId(Long actionUId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getActionByActionUId";
    Object[] params = new Object[] {
                          actionUId,
                      };

    Action action = null;
    try
    {
      logger.logEntry(method, params);
      action = (Action)ActionEntityHandler.getInstance().getEntityByKeyForReadOnly(actionUId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return action;
  }

/*********** Methods for categories ***********************/

  /**
   * To create a new <code>AlertCategory</code> entity.
   *
   * @param  category The new <code>AlertCategory</code> entity.
   * @return	   UID of the created AlertCategory.
   * @exception  CreateEntityException  Thrown when the create operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Long createAlertCategory(AlertCategory category)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createAlertCategory";
    Object[] params = new Object[] {
                          category,
                      };
    Long uid = null;
    try
    {
      logger.logEntry(method, params);
      uid = (Long)AlertCategoryEntityHandler.getInstance().createEntity(category).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }



 /**
   * To retrieve a <code>Collection</code> of <code>AlertCategory</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>AlertCategory</code> entities.
   * @exception   FindEntityException    thrown when an error occurs retrieving the <code>AlertCategory</code> entities.
   */
  public Collection<AlertCategory> getAlertCategories(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertCategories";
    Object[] params = new Object[] {
                          filter,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = AlertCategoryEntityHandler.getInstance().getEntityByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return results;
  }

  /**
   * To retrieve a <code>AlertCategory</code> entity by specified uId.
   *
   * @param    uId - the uId of the <code>AlertCategory</code> entity.
   * @return          the <code>AlertCategory</code> entity with the specified uId.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>AlertCategory</code> entity.
   */
  public AlertCategory getAlertCategoryByUId(Long uId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertCategoryByUId";
    Object[] params = new Object[] {
                          uId,
                      };
    AlertCategory cat = null;
    try
    {
      logger.logEntry(method, params);
      cat = (AlertCategory)AlertCategoryEntityHandler.getInstance().getEntityByKeyForReadOnly(uId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return cat;
  }

   /*********** Methods for MessageTemplate *********/

  /**
   * To create a new <code>MessageTemplate</code> entity.
   *
   * @param  msgTemplate The new <code>MessageTemplate</code> entity.
   * @return	   UID of the created MessageTemplate.
   * @exception  CreateEntityException  Thrown when the create operation fails.
   */
  public Long createMessageTemplate(MessageTemplate msgTemplate)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createMessageTemplate";
    Object[] params = new Object[] {
                          msgTemplate,
                      };
    Long uid = null;
    try
    {
      logger.logEntry(method, params);
      uid = (Long)MessageTemplateEntityHandler.getInstance().createEntity(msgTemplate).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }


  /**
   * To update a <code>MessageTemplate</code> entity.
   *
   * @param  msg the modified <code>MessageTemplate</code> entity.
   * @exception   UpdateEntityException    thrown when an error occurs updating the <code>MessageTemplate</code> entity.
   */
  public void updateMessageTemplate(MessageTemplate msg)
    throws    UpdateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateMessageTemplate";
    Object[] params = new Object[] {
                          msg,
                      };
    try
    {
      logger.logEntry(method, params);

      MessageTemplateEntityHandler.getInstance().update(msg);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To remove a <code>MessageTemplate</code> entity with the specified UId.
   *
   * @param uId - the uId of the <code>MessageTemplate</code> entity.
   * @exception   DeleteEntityException    thrown when an error occurs removing the <code>MessageTemplate</code> entity.
   */
  public void deleteMessageTemplate(Long uId)
    throws    DeleteEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "deleteMessageTemplate";
    Object[] params = new Object[] {
                          uId,
                      };
    try
    {
      logger.logEntry(method, params);

      MessageTemplateEntityHandler.getInstance().remove(uId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To retrieve a <code>Collection</code> of <code>Message Templates</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>MessageTemplate</code> entities.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>MessageTemplate</code> entities.
   */
  public Collection<MessageTemplate> getMessageTemplates(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getMessageTemplates";
    Object[] params = new Object[] {
                          filter,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = MessageTemplateEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return results;
  }

  /**
   * To retrieve a <code>Collection</code> of UIDs of <code>Message Templates</code> entities.
   *
   * @param filter The filtering condition.
   * @return <code>Collection</code> of UIDs of all <code>MessageTemplate</code> entities that
   * satisfy the filtering condition.
   * @exception FindEntityException An error occurs retrieving the <code>MessageTemplate</code> entities.
   * @since GT 2.2 I1
   */
  public Collection<Long> getMessageTemplateKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getMessageTemplateKeys";
    Object[] params = new Object[] {
                          filter,
                      };
    Collection<Long> results = null;
    try
    {
      logger.logEntry(method, params);
      results = MessageTemplateEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return results;
  }

  /**
   * To retrieve a <code>MessageTemplate</code> entity by specified uId.
   *
   * @param   uId - the uId of the <code>MessageTemplate</code> entity.
   * @return  the <code>MessageTemplate</code> entity with the specified uId.
   * @exception   FindEntityException    thrown when an error occurs retrieving the <code>MessageTemplate</code> entity.
   */

  public MessageTemplate getMessageTemplateByUId(Long uId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getMessageTemplateByUId";
    Object[] params = new Object[] {
                          uId,
                      };
    MessageTemplate template = null;
    try
    {
      logger.logEntry(method, params);

      template = (MessageTemplate)MessageTemplateEntityHandler.getInstance().getEntityByKeyForReadOnly(uId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return template;
  }

  /**
   * To retrieve a <code>MessageTemplate</code> entity by specified name.
   *
   * @param   messageName - the name of the <code>MessageTemplate</code> entity.
   * @return  the <code>MessageTemplate</code> entity with the specidied name.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>MessageTemplate</code> entity.
   */
  public MessageTemplate getMessageTemplateByName(String messageName)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getMessageTemplateByName";
    Object[] params = new Object[] {
                          messageName,
                      };
    MessageTemplate template = null;
    try
    {
      logger.logEntry(method, params);

      template = MessageTemplateEntityHandler.getInstance().getMessageTemplateByName(messageName);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return template;
  }

   /*********** Methods for AlertAction *********/

  /**
   * To create a new <code>AlertAction</code> entity.
   *
   * @param  alertAction The new <code>AlertAction</code> entity.
   * @return	   UID of the created AlertAction.
   * @exception CreateEntityException   Thrown when the create operation fails.
   */
  public Long createAlertAction(AlertAction alertAction)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createAlertAction";
    Object[] params = new Object[] {
                          alertAction,
                      };
    Long uid = null;
    try
    {
      logger.logEntry(method, params);
      uid = (Long)AlertActionEntityHandler.getInstance().createEntity(alertAction).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }

  /**
   * To update a <code>AlertAction</code> entity.
   *
   * @param           alertAction    the modified <code>AlertAction</code> entity.
   * @exception   UpdateEntityException    thrown when an error occurs updating the <code>AlertAction</code> entity.
   */
  public void updateAlertAction(AlertAction alertAction)
    throws    UpdateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateAlertAction";
    Object[] params = new Object[] {
                          alertAction,
                      };
    try
    {
      logger.logEntry(method, params);

      AlertActionEntityHandler.getInstance().update(alertAction);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To remove a <code>AlertAction</code> entity with the specified UId.
   *
   * @param           uId the uId of the <code>AlertAction</code> entity.
   * @exception  DeleteEntityException     thrown when an error occurs removing the <code>AlertAction</code> entity.
   */
  public void deleteAlertAction(Long uId)
    throws    DeleteEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "deleteAlertAction";
    Object[] params = new Object[] {
                          uId,
                      };
    try
    {
      logger.logEntry(method, params);

      AlertActionEntityHandler.getInstance().remove(uId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
  }

  /**
   * To retrieve a <code>AlertAction</code> entity by specified AlertUID.
   *
   * @param           alertUId     the AlertUId of the <code>AlertAction</code> entity.
   * @return          Collection of  the <code>AlertAction</code> entity with the specidied name.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>AlertAction</code> entity.
   */
  public Collection<AlertAction> getAlertActionsByAlertUId(Long alertUId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertActionsByAlertUId";
    Object[] params = new Object[] {
                          alertUId,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);

      results = AlertActionEntityHandler.getInstance().getAlertActionsByAlertUid(alertUId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }

  /**
   * To retrieve a <code>AlertAction</code> entity by specified ActionUID.
   *
   * @param           actionUId     the ActionUId of the <code>AlertAction</code> entity.
   * @return          Collection of  the <code>AlertAction</code> entity with the specidied ActionUId.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>AlertAction</code> entity.
   */
  public Collection<AlertAction> getAlertActionsByActionUId(Long actionUId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertActionsByActionUId";
    Object[] params = new Object[] {
                          actionUId,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);

      results = AlertActionEntityHandler.getInstance().getAlertActionsByActionUid(actionUId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return results;
  }
  
  /**
   * To retrieve a <code>Collection</code> of <code>AlertType</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>AlertType</code> entities.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>AlertType</code> entities.
   */
  public Collection<AlertType> getAlertTypes(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertTypes";
    Object[] params = new Object[] {
                          filter,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = AlertTypeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return results;
  }

  /**
   * To retrieve a <code>AlertType</code> entity by specified uId.
   *
   * @param           uId The uId of the <code>AlertType</code> entity.
   * @return          AlertType the <code>AlertType</code> entity with the specified uId.
   * @exception    FindEntityException   thrown when an error occurs retrieving the <code>AlertType</code> entity.
   */
  public AlertType getAlertTypeByUId(Long uId)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertTypeByUId";
    Object[] params = new Object[] {
                          uId,
                      };
    AlertType type = null;
    try
    {
      logger.logEntry(method, params);
      type = (AlertType)AlertTypeEntityHandler.getInstance().getEntityByKeyForReadOnly(uId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return type;
  }


  /**
   * To retrieve a <code>AlertType</code> entity by specified name.
   *
   * @param           name The name of the <code>AlertType</code> entity.
   * @return          AlertType the <code>AlertType</code> entity with the specified name.
   * @exception   FindEntityException    thrown when an error occurs retrieving the <code>AlertType</code> entity.
   */
  public AlertType getAlertTypeByName(String name)
    throws       FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertTypeByName";
    Object[] params = new Object[] {
                          name,
                      };
    AlertType type = null;
    try
    {
      logger.logEntry(method, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, AlertType.NAME, filter.getEqualOperator(),
        name, false);
      Collection<AlertType> results = getAlertTypes(filter);
      if (!results.isEmpty())
        type = (AlertType)results.toArray()[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return type;
  }

  /**
   * Trigger the Alert
   *
   * @param         alertName The name of the Alert.
   * @param         providerList    Provider List containing the Data Providers.
   * @param         fileAttachmentInputStream The input stream of the file attachment
   * @return The success/Failure of each Alert Actions triggered will be returned.
   * @exception AlertTriggeringException thrown when an error occurs.
   */
  public List triggerAlert(String alertName, IProviderList providerList,
    InputStream fileAttachmentInputStream)
    throws AlertTriggeringException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "triggerAlert";
    Object[] params = new Object[] {
                          alertName, providerList, fileAttachmentInputStream,
                      };
    List triggerResults = null;
    try
    {
      logger.logEntry(method, params);
      Alert alert = getAlertByAlertName(alertName);
      if (alert != null)
        triggerResults = AlertActor.getInstance().triggerAlert(
                           alert,
                           providerList,
                           fileAttachmentInputStream);
      else
         throw new Exception("Unknown Alert to trigger: "+alertName);
    }
    catch (Throwable t)
    {
      logger.logWarn(method, params, t);
      throw new AlertTriggeringException(t.getMessage());
    }
    finally
    {
      logger.logExit(method, params);
    }
    return triggerResults;
  }

  /**
   * Trigger the Alert
   *
   * @param         alertName The name of the Alert.
   * @param         providerList Provider list containing the data providers.
   * @param         fileLocation Location of the file.(absolute path)
   * @return  The success/Failure of each Alert Action triggered will be returned.
   * @exception AlertTriggeringException thrown when an error occurs.
   */
  public List triggerAlert(String alertName, IProviderList providerList,
    String fileLocation) throws AlertTriggeringException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "triggerAlert";
    Object[] params = new Object[] {
                          alertName, providerList, fileLocation,
                      };
    List triggerResults = null;
    try
    {
      logger.logEntry(method, params);

      Alert alert = getAlertByAlertName(alertName);

      if (alert != null)
        triggerResults = AlertActor.getInstance().triggerAlert(
                           alert,
                           providerList,
                           fileLocation);
      else
        throw new Exception("Unknown Alert to trigger: "+alertName);
    }
    catch (Throwable t)
    {
      logger.logWarn(method, params, t);
      throw new AlertTriggeringException(t.getMessage());
    }
    finally
    {
      logger.logExit(method, params);
    }
    return triggerResults;
  }

  /**
   * Trigger the Alert
   *
   * @param         alertUID  The UID of the Alert.
   * @param         providerList Provider list containing the data providers.
   * @param         fileLocation Location of the file.(absolute path)
   * @return  The success/Failure of each Alert Action triggered will be returned.
   * @exception AlertTriggeringException thrown when an error occurs.
   */
  public List triggerAlert(Long alertUID, IProviderList providerList,
    String fileLocation) throws AlertTriggeringException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "triggerAlert";
    Object[] params = new Object[] {
                          alertUID, providerList, fileLocation,
                      };
    List triggerResults = null;
    try
    {
      logger.logEntry(method, params);

      Alert alert = getAlertByAlertUId(alertUID);

      triggerResults = AlertActor.getInstance().triggerAlert(
                         alert,
                         providerList,
                         fileLocation);
    }
    catch (Throwable t)
    {
      logger.logWarn(method, params, t);
      throw new AlertTriggeringException(t.getMessage());
    }
    finally
    {
      logger.logExit(method, params);
    }
    return triggerResults;
  }

/************* Methods for Alert List *****************/

  /**
   * To create a new <code>AlertList</code> entity.
   *
   * @param  alertList The new <code>AlertList</code> entity.
   * @return	   UID of the created AlertList.
   * @exception CreateEntityException   Thrown when the create operation fails.
   */
  public Long createAlertList(AlertList alertList)
    throws    CreateEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "createAlertList";
    Object[] params = new Object[] {
                          alertList,
                      };
    Long uid = null;
    try
    {
      logger.logEntry(method, params);
      uid = (Long)AlertListEntityHandler.getInstance().createEntity(alertList).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }

    return uid;
  }


  /**
   * To retrieve a <code>Collection</code> of <code>AlertList</code> entities.
   *
   * @param filter The filtering condition.
   * @return          a <code>Collection</code> of all <code>AlertList</code> entities.
   * @exception  FindEntityException     thrown when an error occurs retrieving the <code>AlertList</code> entities.
   */
  public Collection<AlertList> getAlertLists(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertLists";
    Object[] params = new Object[] {
                          filter,
                      };
    Collection results = null;
    try
    {
      logger.logEntry(method, params);
      results = AlertListEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return results;
  }


  /**
   * To retrieve the number of AlertList(s) for a user
   *
   * @param	userUid	User UID for whom the unread alert list is required.
   * @return    The count of alertlists for the specified user
   * @exception FindEntityException thrown when an error occurs retrieving the <code>AlertList</code> entities.
   */
  public int getAlertListsByUserUId(Long userUid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getAlertListsByUserUId";
    Object[] params = new Object[] {
                          userUid,
                      };
    int count = 0;
    try
    {
      logger.logEntry(method, params);
      count = AlertListEntityHandler.getInstance().getAlertListsByUserUid(userUid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return count;
  }

  /**
   * Retrieve the emails of all users that assume a particular role.
   *
   * @param roleName The name of the Role
   * @return Collection of Email Ids (addresses). Empty addresses are excluded.
   *
   * @since 2.0 I7
   */
  public Collection<String> getMailIdsFromRole(String roleName)
    throws SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getMailIdsFromRole";
    Object[] params = new Object[] {
                          roleName,
                      };

    ArrayList<String> mailIds = new ArrayList<String>();
    try
    {
      logger.logEntry(method, params);

      Role role = ServiceLookupHelper.getAclManager().getRoleByRoleName(roleName);
      if (role != null)
      {
        Collection userUIds = ServiceLookupHelper.getAclManager().getSubjectUIdsForRole(
                             (Long)role.getKey(), UserAccount.ENTITY_NAME);
        if (!userUIds.isEmpty())
        {
          DataFilterImpl filter = new DataFilterImpl();
          filter.addDomainFilter(null, UserAccount.UID, userUIds, false);
          filter.addSingleFilter(filter.getAndConnector(), UserAccount.EMAIL,
            filter.getNotEqualOperator(), null, false);
          filter.addSingleFilter(filter.getAndConnector(), UserAccount.EMAIL,
            filter.getNotEqualOperator(), "", false);

          BitSet exclState = new BitSet();
          exclState.set(UserAccountState.STATE_DELETED);
          Collection users = ServiceLookupHelper.getUserManager().findUserAccounts(
                               filter, exclState);
          for (Iterator iter=users.iterator(); iter.hasNext(); )
          {
            UserAccount ua = (UserAccount)iter.next();
            mailIds.add(ua.getEmail());
          }
        }
      }
    }
    catch (Throwable t)
    {
      logger.logWarn(method, params, t);
      logger.logMessage(method, params, "Error: "+t.getMessage());
    }
    finally
    {
      logger.logExit(method, params);
    }
    return mailIds;
  }
  

  public EmailConfig getEmailConfig() throws SystemException
  {
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "getEmailConfig";
    Object[] params = new Object[] {};
    EmailConfig emailConfig = null;
    try
    {
      AlertMailService mailService = AlertMailService.getInstance();
      emailConfig = mailService.getEmailConfig();
    }
    catch(Throwable th)
    {
      logger.logWarn(method,params,th);
    }
    return emailConfig;
  }
    
  public void updateEmailConfig(EmailConfig emailConfig) throws SystemException
  { 
    FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateEmailConfig";
    Object[] params = new Object[] {emailConfig};

    try
    {
      AlertMailService mailService = AlertMailService.getInstance();
      mailService.saveEmailConfig(emailConfig);
    }
    catch(Throwable th)
    {
      logger.logWarn(method,params,th);
    }
  }
  
  /************************ Method for JmsDestination *****************/
  /**
   * Create a new JmsDestination obj and insert into DB.
   * A new jms retry timer will be created as well.
   * @param jmsDest
   * @return The UID of JmsDestination obj.
   * @throws SystemException
   * @throws CreateEntityException
   * @throws SystemException
   */
  public Long createJmsDestination(JmsDestination jmsDest)
  	throws SystemException, CreateEntityException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
  	String method = "createJmsDestination";
  	Object[] params = new Object[]{jmsDest};
  	Long pk = null;
  	try
  	{
  		pk = getJmsDestinationEntityHandler().createJmsDestination(jmsDest);
  	}
  	catch (Throwable t)
    {
      logger.logCreateError(method, params, t);
    }
    finally
    {
      logger.logExit(method, params);
    }
    return pk;
  }
  
  /**
   * Update the existing record in DB(that has the same UID as jmsDest) with the 
   * new values which obtained from jmsDest.
   * @param jmsDest
   * @throws SystemException
   * @throws UpdateEntityException
   */
  public void updateJmsDestination(JmsDestination jmsDest)
  	throws SystemException, UpdateEntityException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
    String method = "updateJmsDestination";
    Object[] params = new Object[] {jmsDest};
  	try
  	{
  		getJmsDestinationEntityHandler().updateJmsDestination(jmsDest);
  	}
  	catch(Throwable ex)
  	{
  		logger.logUpdateError(method, params, ex);
  	}
  	finally
  	{
  		logger.logExit(method, params);
  	}
  }
  
  /**
   * Delete the JmsDestination DB record given the UID
   * @param jmsDestUID PK of jms_destination table
   * @throws SystemException
   * @throws DeleteEntityException
   */
  public void deleteJmsDestination(Long jmsDestUID)
  	throws SystemException, DeleteEntityException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
  	String method = "deleteJmsDestination";
  	Object[] params = new Object[]{jmsDestUID};
  	try
  	{
  		getJmsDestinationEntityHandler().deleteJmsDestinationByUID(jmsDestUID);
  	}
  	catch(Throwable th)
  	{
  		logger.logDeleteError(method, params, th);
  	}
  	finally
  	{
  		logger.logExit(method, params);
  	}
  }
  
  /**
   * Retrieve a collection of JmsDestination objs from DB given the filter.
   * @return a collection of JmsDestination obj or null if no record exist in db.
   * @throws SystemException
   * @throws FindEntityException
   */
  public Collection<JmsDestination> getJmsDestinations(IDataFilter filter)
  	throws SystemException, FindEntityException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
  	String method = "getJmsDestination";
  	Object[] params = new Object[]{filter};
  	Collection c = null;
  	try
  	{
  		c = getJmsDestinationEntityHandler().findJmsDestinationByFilter(filter);
  	}
  	catch(Throwable th)
  	{
  		logger.logFinderError(method, params, th);
  	}
  	finally
  	{
  		logger.logExit(method, params);
  	}
  	return c;
  }
  
/**
   * Retrieve a particular JmsDestination obj given its UID
   * @param UID PK of jms_destination table
   * @return JmsDestination obj or null if no such record exist in db.
   * @throws SystemException
   * @throws FindEntityException
   */
  public JmsDestination getJmsDestinationByUID(Long UID)
  	throws SystemException, FindEntityException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
  	String method = "getJmsDestinationByUID";
  	Object[] params = new Object[]{UID};
  	JmsDestination jmsDest = null;
  	try
  	{
  		jmsDest = getJmsDestinationEntityHandler().findByUID(UID);
  	}
  	catch(Throwable th)
  	{
  		logger.logFinderError(method, params, th);
  	}
  	finally
  	{
  		logger.logExit(method, params);
  	}
  	return jmsDest;
  }
  
  /**
   * Retrieve a collection of JmsDestination keys given the filter
   * @param filter
   * @return a collection of jms destination keys or null if no record exist in db.
   * @throws FindEntityException
   * @throws SystemException
   */
  public Collection<Long> getJmsDestinationKeys(IDataFilter filter)
  	throws FindEntityException, SystemException
  {
  	FacadeLogger logger = AlertLogger.getManagerFacadeLogger();
  	String method = "getJmsDestination";
  	Object[] params = new Object[]{filter};
  	Collection<Long> c = null;
  	try
  	{
  		c = getJmsDestinationEntityHandler().getKeyByFilterForReadOnly(filter);
  	}
  	catch(Throwable th)
  	{
  		logger.logFinderError(method, params, th);
  	}
  	finally
  	{
  		logger.logExit(method, params);
  	}
  	return c;
  }
  
  /**
   * get the jmsdestination entity handler
   * @return EntityHandler for JmsDestination
   */
  private JmsDestinationEntityHandler getJmsDestinationEntityHandler()
  {
  	return JmsDestinationEntityHandler.getInstance();
  }
  /************************ end Method for JmsDestination *****************/
  
  /**
   * Retrieve the JmsDestination given its name which is the unique key for
   * table jms_destination
   * @param jmsDestName
   * @return
   * Added by Ming Qian for Beyonics GridOne
   */
  public JmsDestination retrieveJmsDestination(String jmsDestName)
    throws Exception, RemoteException
  { 
    return JmsDestinationEntityHandler.getInstance().findByJmsDestName(jmsDestName);
  }
  
  public Collection<JmsMessageRecord> getAssociatedJmsMessageRecord(long jmsDestUid)
  throws Exception, RemoteException
  {
    DataFilterImpl filter = new DataFilterImpl();
    //filter.addSingleFilter(null, JmsMessageRecord.JMS_DESTINATION_NAME, filter.getEqualOperator(),
    //                       jmsDestname, false);
    filter.addSingleFilter(null, JmsMessageRecord.JMS_DESTINATION_UID, filter.getEqualOperator(),
                           new Long(jmsDestUid), false);
    filter.addSingleFilter(filter.getAndConnector(), JmsMessageRecord.PERMANENT_FAILED, filter.getNotEqualOperator(),
                           true, false);
    filter.addOrderField(JmsMessageRecord.ALERT_TIME_IN_LONG,true);
    return JmsMessageRecordEntityHandler.getInstance().findJmsMessageRecordByFilter(filter);
  }
  
  public void updateFailedMessage(JmsMessageRecord jmsMsgRecord , JmsDestination jmsDest, int maxRetries)
  throws Exception, RemoteException
  {    
    int numRetries = jmsMsgRecord.getMsgData().getRetryCount()+1;
    //maxRetries = jmsDest.getMaximumRetries();
    
    //indicate that the jms msg has reached maximum retried.
    if(numRetries > maxRetries && maxRetries != -1)
    {
      jmsMsgRecord.setPermanentFailed(Boolean.TRUE);
    }
    else
    {
      jmsMsgRecord.getMsgData().setRetryCount(numRetries);
    }
    JmsMessageRecordEntityHandler.getInstance().updateJmsMessageRecord(jmsMsgRecord);
  }
  
  public void handleFailedMessages(Collection<JmsMessageRecord> failedMessages, JmsDestination jmsDest, int maxRetries)
  throws Exception, RemoteException
  {
    for(JmsMessageRecord msgRecord : failedMessages)
    {
      updateFailedMessage(msgRecord, jmsDest, maxRetries);
    }
  }
  
  public void deleteJmsMessageRecord(JmsMessageRecord msgRecord)
  throws Exception, RemoteException
  {
    JmsMessageRecordEntityHandler.getInstance().deleteJmsMessageRecord((Long)msgRecord.getKey());
  }
  
}