package com.gridnode.gtas.server.registration.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.registration.GetRegistrationPrivateCertificatePasswordStateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
 

/**
 * <p>Title: GTAS-UserManagement</p>
 * <p>Description: GTAS
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public class GetRegistrationPrivateCertificatePasswordStateAction extends AbstractGridTalkAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8155043108024092890L;
	private static final String ACTION_NAME = "GetRegistrationPrivateCertificatePasswordStateAction";
  private GetRegistrationPrivateCertificatePasswordStateEvent _event = null;
  private Boolean _issetPassword = new Boolean(false);

  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(false);
    //checkUserID();
  }

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (GetRegistrationPrivateCertificatePasswordStateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _issetPassword = (Boolean)performAction(_event);
      params = new Object[] {};
      response = constructEventResponse(params);
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(params, _event, ex);
    }
    catch (Throwable ex)
    {
      response = constructEventResponse(params, _event, new SystemException(ex));
    }
    return response;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  }

  private Object performAction(IEvent event)
    throws Throwable
  {
    GetRegistrationPrivateCertificatePasswordStateEvent certEvent = (GetRegistrationPrivateCertificatePasswordStateEvent)event;
    try
    {
      boolean isset = false;
      RegistrationInfo regino =  getRegistrationManager().getRegistrationInfo();
      short state = regino.getRegistrationState();
      if(state == regino.STATE_REGISTERED)
        isset = getCertificateManager().issetPrivatePassword();
      else
        isset = true;
      return new Boolean(isset);
    }
    catch(Throwable aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(GetRegistrationPrivateCertificatePasswordStateEvent.REGISTRATION_PRIVATE_PASSEWORD_STATE, _issetPassword);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    short errorCode =  IErrorCode.GET_PRIVATE_CERT_PASSWORD_STATE_ERROR;
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                 errorCode,
                 params,
                 ex.getType(),
                 ex.getLocalizedMessage(),
                 ex.getStackTraceString());
    return response;
  }


  private ICertificateManagerObj getCertificateManager()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               ICertificateManagerHome.class.getName(),
               ICertificateManagerHome.class,
               new Object[0]);
  }

  private IRegistrationServiceObj getRegistrationManager()
    throws ServiceLookupException
  {
     return (IRegistrationServiceObj)ServiceLocator.instance(
                  ServiceLocator.CLIENT_CONTEXT).getObj(
                  IRegistrationServiceHome.class.getName(),
                  IRegistrationServiceHome.class,
                  new Object[0]);

  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(
             IErrorCode.GET_PRIVATE_CERT_PASSWORD_STATE_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return GetRegistrationPrivateCertificatePasswordStateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}