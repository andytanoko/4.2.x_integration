/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceMgmtManagerBean.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.facade.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.servicemgmt.facade.IServiceMgmtManager;
import com.gridnode.pdip.app.servicemgmt.helpers.Logger;
import com.gridnode.pdip.app.servicemgmt.helpers.ServiceAssignmentEntityHandler;
import com.gridnode.pdip.app.servicemgmt.helpers.WebServiceEntityHandler;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

public class ServiceMgmtManagerBean implements SessionBean, IServiceMgmtManager
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8214621450643293756L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ************************ Implementing methods in IServiceMgmtManagerObj

  public Long createWebService(WebService webService) throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "createWebService";
    Object[] params = new Object[] { webService };

    Long key = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long) getWebServiceEntityHandler().createEntity(webService).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  public void updateWebService(WebService webService) throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "updateWebService";
    Object[] params = new Object[] { webService };

    try
    {
      logger.logEntry(methodName, params);

      getWebServiceEntityHandler().update(webService);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  public void deleteWebService(Long uId) throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "deleteWebService";
    Object[] params = new Object[] { uId };

    try
    {
      logger.logEntry(methodName, params);

      getWebServiceEntityHandler().remove(uId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  public WebService findWebService(Long uID) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findWebService";
    Object[] params = new Object[] { uID };

    WebService webService = null;

    try
    {
      logger.logEntry(methodName, params);

      webService = (WebService) getWebServiceEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return webService;
  }

  public Collection findWebServices(IDataFilter filter) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findWebServices";
    Object[] params = new Object[] { filter };

    Collection webServices = null;

    try
    {
      logger.logEntry(methodName, params);

      webServices = getWebServiceEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return webServices;
  }

  public Collection findWebServicesKeys(IDataFilter filter) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findWebServicesKeys";
    Object[] params = new Object[] { filter };

    Collection webServicesKeys = null;

    try
    {
      logger.logEntry(methodName, params);

      webServicesKeys = getWebServiceEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return webServicesKeys;
  }

  public Long createServiceAssignment(ServiceAssignment assignment) throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "createServiceAssignment";
    Object[] params = new Object[] { assignment };

    Long key = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long) getServiceAssignmentEntityHandler().createEntity(assignment).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  public void updateServiceAssignment(ServiceAssignment assignment) throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "updateServiceAssignment";
    Object[] params = new Object[] { assignment };

    try
    {
      logger.logEntry(methodName, params);

      getServiceAssignmentEntityHandler().update(assignment);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  public ServiceAssignment findServiceAssignment(Long uID) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findServiceAssignment";
    Object[] params = new Object[] { uID };

    ServiceAssignment serviceAssignment = null;

    try
    {
      logger.logEntry(methodName, params);

      serviceAssignment = (ServiceAssignment) getServiceAssignmentEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return serviceAssignment;
  }

  public ServiceAssignment findServiceAssignment(String userName, String userType) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findServiceAssignment";
    Object[] params = new Object[] { userName, userType };

    ServiceAssignment serviceAssignment = null;

    try
    {
      logger.logEntry(methodName, params);

      serviceAssignment = (ServiceAssignment) getServiceAssignmentEntityHandler().findServiceAssignment(userName, userType);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return serviceAssignment;
  }

  public Collection findServiceAssignments(IDataFilter filter) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findServiceAssignments";
    Object[] params = new Object[] { filter };

    Collection serviceAssignments = null;

    try
    {
      logger.logEntry(methodName, params);

      serviceAssignments = getServiceAssignmentEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return serviceAssignments;
  }

  public Collection findServiceAssignmentKeys(IDataFilter filter) throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findServiceAssignmentKeys";
    Object[] params = new Object[] { filter };

    Collection serviceAssignmentKeys = null;

    try
    {
      logger.logEntry(methodName, params);

      serviceAssignmentKeys = getServiceAssignmentEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return serviceAssignmentKeys;
  }

  public void deleteServiceAssignment(Long uId) throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "deleteServiceAssignment";
    Object[] params = new Object[] { uId };

    try
    {
      logger.logEntry(methodName, params);

      getServiceAssignmentEntityHandler().remove(uId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  public WebService findAssignedWebService(String userName, String userType, String serviceName, String serviceGroup)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName = "findAssignedWebService";
    Object[] params = new Object[] { userName, userType, serviceName, serviceGroup };

    WebService webService = null;
    try
    {
      logger.logEntry(methodName, params);

      webService = (WebService) getWebServiceEntityHandler().findWebService(serviceName, serviceGroup);
      if (webService == null)
      {
        logger.logMessage(methodName, params, "no webService found with specified serviceName,serviceGroup");
        return null;
      }

      ServiceAssignment serviceAssignment = (ServiceAssignment) getServiceAssignmentEntityHandler().findServiceAssignment(userName, userType);
      if (serviceAssignment == null)
      {
        logger.logMessage(methodName, params, "no serviceAssignment found with specified userName,userType");
        return null;
      }

      Collection assignedUIds = serviceAssignment.getWebServiceUIds();
      if (assignedUIds == null || !assignedUIds.contains(webService.getKey().toString()))
      {
        logger.logMessage(methodName, params, "webService is not assigned to user");
        return null;
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return webService;
  }

  public WebService findAssignedWebService(String userName, String userType, String serviceName)
    throws FindEntityException, SystemException
  {
    return findAssignedWebService(userName, userType, serviceName,WebService.INTERNAL_GROUP);
  }

  // ************************ Helper methods ************************

  private WebServiceEntityHandler getWebServiceEntityHandler()
  {
    return WebServiceEntityHandler.getInstance();
  }

  private ServiceAssignmentEntityHandler getServiceAssignmentEntityHandler()
  {
    return ServiceAssignmentEntityHandler.getInstance();
  }

}
