/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGTSession.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-05-20     Neo Sok Lay         Change to construct DefaultGTManagerFactory
 *                                    instead of FakeManagerFactory.
 * 2002-07-25     Andrew Hill         Modify handleEvent() to throw ResponseException
 * 2002-10-30     Andrew Hill         Use commons logging to record events and time
 * 2002-11-03     Andrew Hill         connectToGridMaster(), disconnectFromGridMaster()
 * 2002-11-26     Andrew Hill         getUserUid();
 * 2002-12-12     Andrew Hill         getGridNodeId(); (will need refactor after merge with I7)
 * 2002-12-02     Andrew Hill         isRegistered();
 * 2002-12-20     Andrew Hill         startBackendListener()
 * 2003-01-09     Andrew Hill         getTimeServer()
 * 2003-03-03     Andrew Hill         Defer lookup of gtClientCtrl
 * 2003-04-23     Andrew Hill         isApplicationStarted()
 * 2003-04-25     Andrew Hill         Allow client to specify if wants pcpKnown check on login
 * 2003-05-02     Andrew Hill         isRegistered(noCache)
 * 2003-07-18     Andrew Hill         Use a ResponseExceptionFactory to create ResponseExceptions
 * 2003-11-05     Andrew Hill         noSecurity hack for GNDB00016109
 * 2005-03-14     Andrew Hill         Dual-level access control (admin vs user)
 * 2006-04-24     Neo Sok Lay         Add noP2P for hiding P2P functionality, and noUDDI for hiding
 *                                    UDDI functionality
 */
package com.gridnode.gtas.client.ctrl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.backend.StartBackendListenerEvent;
import com.gridnode.gtas.events.certificate.SetPrivateCertificatePasswordEvent;
import com.gridnode.gtas.events.connection.ConnectEvent;
import com.gridnode.gtas.events.connection.DisconnectEvent;
import com.gridnode.gtas.events.registration.GetRegistrationPrivateCertificatePasswordStateEvent;
import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ITimeServer;
import com.gridnode.pdip.framework.util.ServiceLocator;

class DefaultGTSession extends AbstractGTSession
{
  private static final Log _log = LogFactory.getLog(DefaultGTSession.class); // 20031209 DDJ

  private ITimeServer _timeServer; //20030109AH

  protected IGridTalkClientControllerObj _gtClientCtrl;
  private Long _userUid;
  private Integer _gridNodeId;
  private String _gridNodeName;
  private boolean _noSecurity = false; //20031105AH
  private boolean _isAdmin = false; //20050314AH
  private boolean _noP2P = false; //NSL20060424
  private boolean _noUDDI = false; //NSL20060424

  DefaultGTSession(int sessionType, GlobalContext context)
  {
    super(sessionType, context);
    _managerFactory = new DefaultGTManagerFactory(this);
  }

  protected void init() throws SessionCreationException
  {
    //getController(); 20030303AH - No longer lookup in the init method (defer to first use)
    _gtClientCtrl = null; //20030303AH
  }

  public synchronized void login( IGTHostInfo host,
                                  String userId,
                                  char[] password,
                                  boolean checkPcpKnown) throws GTClientException
  {
    _userUid = null;
    if(_isLoggedIn)
    {
      logout();
    }
    if(checkPcpKnown) //@todo: later refactor so always compulsary.
    { //20030425AH
      if( !this.isPrivateCertificatePasswordKnown() )
      { //20030423AH
        throw new LoginException(LoginException.NO_PRIVATE_CERT_PASSWORD, "Private certificate password not set");
      }
    }
    if(host != null)
    {
      throw new java.lang.UnsupportedOperationException("Host parameter not currently supported");
    }
    if(userId == null)
    {
      throw new java.lang.NullPointerException("Null userId");
    }
    if(password == null)
    {
      throw new java.lang.NullPointerException("Null password");
    }
    _userId = userId;

    //Stub code for testing-----
    String p = new String(password);
    short errType = -1;
    String message ="";
    BasicEventResponse response = null;

    try
    {
      response = (BasicEventResponse)handleEvent(new UserLoginEvent(_userId, p));
    }
    catch(Throwable t)
    {
      throw new LoginException(LoginException.SERVER_ERROR,t.getMessage(),t);
    }

    if(!response.isEventSuccessful())
    {
      message = response.getErrorReason();
      errType = response.getErrorType();
      switch(errType)
      {
        case ApplicationException.APPLICATION:
          throw new LoginException(LoginException.INVALID_LOGIN, message);
        case SystemException.SYSTEM:
          throw new ResponseException(response);
        default:
          throw new ResponseException(response);
      }
    }
    else
    {
      _isLoggedIn = true;
      initRoleCache(); //20050314AH
    }
  }
  
  private void initRoleCache() throws GTClientException
  { //20050314AH
    //Currently just looks up whether or not the user in question has the admin role
    try
    {
      IGTUserEntity user = getUser();
      Collection roles = user.getFieldEntities(IGTUserEntity.ROLES);
      Iterator i = roles.iterator();
      boolean admin = false;
      while(i.hasNext() && !admin)
      {
        IGTRoleEntity role = (IGTRoleEntity)i.next();
        String roleName = role.getFieldString(IGTRoleEntity.ROLE);
        if(IGTRoleEntity.ADMIN.equals(roleName))
        {
          admin = true;
        }
      }
      _isAdmin = admin;      
    }
    catch(Exception e)
    {
      throw new GTClientException("Error initialising user role cache",e);
    }
  }

  public synchronized void logout() throws GTClientException
  {
    if(_isLoggedIn == false) return;
    try
    {
      UserLogoutEvent event = new UserLogoutEvent();
      fireEvent(event);
      _isLoggedIn = false;
      _userUid = null; //20021126AH
      _isAdmin = false; //20050314AH
      _noP2P = false; //NSL20060424
      _noUDDI = false; //NSL20060424
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error performing logout",t);
    }
  }

  public int getManagerType(String entityType) throws GTClientException
  {
    return _managerFactory.getManagerType(entityType);
  } 

  public IGTManager getManager(String entityType) throws GTClientException
  {
    int mgrType = getManagerType(entityType);
    return getManager(mgrType);
  }

  public IGTManager getManager(int managerType) throws GTClientException
  {
    if(_isLoggedIn)
      return _managerFactory.getManager(managerType);
    else
      throw new java.lang.IllegalStateException("Not logged in");
  }

  protected void getController() throws SessionCreationException
  {
    if (_gtClientCtrl == null)
    {
      try
      {
        if(_log.isInfoEnabled())
        {
          _log.info("Attempting to get reference to IGridTalkClientController");
        } 
        long startTime = System.currentTimeMillis();
        
        _gtClientCtrl = (IGridTalkClientControllerObj)ServiceLocator.instance(
                          ServiceLocator.CLIENT_CONTEXT).getObj(
                            IGridTalkClientControllerHome.class.getName(),
                            IGridTalkClientControllerHome.class,
                            new Object[0]);
                            
        long endTime = System.currentTimeMillis();
        if(_log.isInfoEnabled())
        {
          _log.info("GridTalk client controller reference obtained (in approx " + (endTime - startTime) + " ms)");
        } 
      }
      catch (ServiceLookupException ex)
      {
        // perform a 2nd attempt to get the client controller remote object
        try
        {
          // invalidate the cache in the SeviceLocator
          if(_log.isInfoEnabled())
          {
            _log.info("Attempting to get reference to IGridTalkClientController again");
          } 
          long startTime = System.currentTimeMillis();
          
          ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).invalidate();

          _gtClientCtrl = (IGridTalkClientControllerObj)ServiceLocator.instance(
                            ServiceLocator.CLIENT_CONTEXT).getObj(
                              IGridTalkClientControllerHome.class.getName(),
                              IGridTalkClientControllerHome.class,
                              new Object[0]);
                              
          long endTime = System.currentTimeMillis();
          if(_log.isInfoEnabled())
          {
            _log.info("GridTalk client controller reference obtained (in approx " + (endTime - startTime) + " ms)");
          }

        }
        catch (ServiceLookupException e)
        {
          throw new SessionCreationException(
            SessionCreationException.SERVICE_LOOKUP_FAILURE,
            e.getMessage(),
            e);
        }
      }
    }
  }

  /**
   * feeds the specified event to the state machine of the business logic.
   *
   * @param ese is the current event
   * @return a EventResponse object which can be extend to carry any payload.
   * @exception com.sun.j2ee.blueprints.waf.event.EventException <description>
   * @exception com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException
   */
  final synchronized IEventResponse handleEvent(IEvent event)
      throws EventException, RemoteException, SessionCreationException
  {
    if(event == null)
    {
      throw new java.lang.NullPointerException("null event");
    }
    //20030303AH - We now obtain the gtClientCtrl reference the first time it is needed
    if(_gtClientCtrl == null)
    {
      getController();
    }
    //...
    String eventClass = event.getClass().getName();
    if(_log.isInfoEnabled())
    {
      _log.info("Sending " + eventClass + " to GridTalk Client Controller");
    } 
    long startTime = System.currentTimeMillis();
    
    IEventResponse response =  _gtClientCtrl.processEvent(event);
    
    long endTime = System.currentTimeMillis();
    if(_log.isInfoEnabled())
    {
      _log.info("Received response for " + eventClass + " from GridTalk Client" + " Controller (in approx " + (endTime - startTime) + " ms)");
    }
    return response;
  }

  protected Object fireEvent(IEvent event)
  throws GTClientException
  {
    if(event == null)
    {
      throw new NullPointerException("event is null");
    }
    try
    {
      BasicEventResponse response = (BasicEventResponse)handleEvent(event);
      if(response == null)
      {
        throw new java.lang.NullPointerException("Response is null for event:" + event.getClass().getName());
      }
      if (!response.isEventSuccessful())
      {
        //20030718AH - co: throw new ResponseException(response);
        throw getResponseExceptionFactory().getResponseException(response, this, event); //20030718AH
      }
      return response.getReturnData();
    }
    catch (GTClientException ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      throw new GTClientException("Error handling event:" + event.getClass().getName(),ex);
    }
  }

  public boolean isConnectedToGridMaster() throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("This method has not been implemented (isConnectedToGridMaster)");
  }

  public void connectToGridMaster(String securityPassword) throws GTClientException
  {
    if(_noSecurity) securityPassword = IGTSession.PCP_DEFAULT; //20031105AH
    //@todo: check if already connected and return if so
    if(securityPassword == null)
    {
      throw new java.lang.NullPointerException("securitypassword is null");
    }
    try
    {
      ConnectEvent event = new ConnectEvent(securityPassword);
      fireEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error connecting to GridMaster",t);
    }
  }

  public void disconnectFromGridMaster() throws GTClientException
  {
    //@todo: check if not connected and return if so
    try
    {
      DisconnectEvent event = new DisconnectEvent();
      fireEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error disconnecting from GridMaster",t);
    }
  }

  public synchronized IGTUserEntity getUser() throws GTClientException
  {
    String userId = getUserId();
    try
    {
      IGTUserManager userMgr = (IGTUserManager)getManager(IGTManager.MANAGER_USER);
      IGTUserEntity user = userMgr.getUserByID( userId );
      if(user == null)
      {
        throw new java.lang.NullPointerException("Null IGTUserEntity returned by getUserById()");
      }
      return user;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get IGTUserEntity for current user where userId=" + userId,t);
    }
  }

  public synchronized Long getUserUid() throws GTClientException
  {
    try
    {
      if(_userUid == null)
      {
        IGTUserEntity user = getUser();
        _userUid = user.getUidLong();
        return _userUid;
      }
      else
      {
        return _userUid;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get uid for current user",t);
    }
  }

  public synchronized Integer getGridNodeId() throws GTClientException
  { //20021212AH
    try
    {
      if(!isRegistered())
      {
        return null;
      }
      else if(_gridNodeId == null)
      {
        IGTRegistrationInfoManager regMgr = (IGTRegistrationInfoManager)
                                            getManager(IGTManager.MANAGER_REGISTRATION_INFO);
        IGTRegistrationInfoEntity regInfo = regMgr.getRegistrationInfo();
        _gridNodeId = (Integer)regInfo.getFieldValue(IGTRegistrationInfoEntity.GRIDNODE_ID);
        _gridNodeName = regInfo.getFieldString(IGTRegistrationInfoEntity.GRIDNODE_NAME); //20021214AH
        return _gridNodeId;
      }
      else
      {
        return _gridNodeId;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting gridnode id",t); //20021214AH
    }
  }

  public synchronized String getGridNodeName() throws GTClientException
  { //20021214AH
    try
    {
      if(!isRegistered())
      {
        return null;
      }
      else if(_gridNodeName == null)
      {
        getGridNodeId(); //invokes lookup of name as well
        return _gridNodeName;
      }
      else
      {
        return _gridNodeName;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting gridnode name",t);
    }
  }

  public boolean isRegistered() throws GTClientException
  {  //20030502AH
    return isRegistered(false);
  }

  public boolean isRegistered(boolean noCache) throws GTClientException
  {
    try
    {
      if(getContext().isRegistered() && (!noCache) )
      {
        // If we have true stored in the GlobalContext we know for sure the product is registered.
        return true;
      }
      else
      {
        // If isRegistered() in the GlobalContext returns false we cant be sure if the product is
        // or is not registered. We will do a check with GTAS. Of course this doesnt preclude
        // simultaneous registration occuring from another client...
        IGTRegistrationInfoManager rMgr = (IGTRegistrationInfoManager)
                                          getManager(IGTManager.MANAGER_REGISTRATION_INFO);
        IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)rMgr.getRegistrationInfo();
        boolean registered = IGTRegistrationInfoEntity.REG_STATE_REG.equals(rInfo.getFieldValue(IGTRegistrationInfoEntity.REG_STATE));
        getContext().setRegistered(registered); //20030502AH
        return registered;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining if product is registered",t);
    }
  }

  public void startBackendListener() throws GTClientException
  { //20021220AH
    try
    {
      IEvent event = new StartBackendListenerEvent();
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error starting backend listener",t);
    }
  }

  public synchronized ITimeServer getTimeServer() throws GTClientException
  { //20030109AH
    try
    {
      if(_timeServer == null)
      {
        _timeServer = new GtsTimeServer(this);
      }
      return _timeServer;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to obtain a TimeServer",t);
    }
  }

  /*
   * Will return a boolean flag indicating whether GTAS knows the private certificate password.
   * As we dont yet have a way to 'stop' the
   * server (short of restarting it), this method can also safely cache a true result once
   * it receives one. For false results, the method will always ask the GTAS if it has the password
   * or not.
   * @return false if app not started or true if started or was found to be started on a previous call to this method
   * @throws GTClientException
   */
  public boolean isPrivateCertificatePasswordKnown() throws GTClientException
  {  //20030423AH
    try
    {
      if( _noSecurity ) return true; //20031105AH
      GlobalContext globalContext = getContext();
      if (globalContext == null)
        throw new NullPointerException("globalContext is null");
      if(globalContext.isPcpKnown() )
      {
        return true;
      }
      else
      {
        boolean pcpKnown = false;
        GetRegistrationPrivateCertificatePasswordStateEvent event
                          = new GetRegistrationPrivateCertificatePasswordStateEvent();
        Map results = (Map)fireEvent(event);
        if (results == null)
          throw new NullPointerException("results is null");
        Boolean pcpKnownObject = (Boolean)results.get(GetRegistrationPrivateCertificatePasswordStateEvent.REGISTRATION_PRIVATE_PASSEWORD_STATE);
        if (pcpKnownObject == null)
          throw new NullPointerException("pcpKnownObject is null");
        pcpKnown = pcpKnownObject.booleanValue();
        globalContext.setIsPcpKnown(pcpKnown);
        return pcpKnown;
      }
    }
    catch (Throwable t)
    {
      throw new GTClientException("Unable to determine if Private Certificate Password is known",t); //20030505AH
    }
  }

  public void setPrivateCertificatePassword(String password) throws GTClientException
  { //20030423AH
    try
    {
      if(_noSecurity) password = IGTSession.PCP_DEFAULT; //20031105AH
      if (password == null)
        throw new NullPointerException("password is null");
      SetPrivateCertificatePasswordEvent event = new SetPrivateCertificatePasswordEvent(password);
      fireEvent(event);
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error setting private certificate password",t);
    }
  }
  
  protected IResponseExceptionFactory getResponseExceptionFactory()
  { //20030718AH
    return DefaultResponseExceptionFactory.getInstance();
  }

  public void setNoSecurity(boolean insecure)
  { //20031105AH
    _noSecurity = insecure;
    if(insecure & _log.isInfoEnabled()) _log.info("Disabling pcp security");
  }

	public boolean isNoSecurity()
	{ //20031105AH - (See comments in IGTSession interface source)
		return _noSecurity;
	}
  
  public boolean isAdmin()
  { //20050314AH
    return _isAdmin;
  }

	/**
	 * @see com.gridnode.gtas.client.ctrl.IGTSession#isNoP2P()
	 */
	public boolean isNoP2P()
	{
		return _noP2P;
	}

	/**
	 * @see com.gridnode.gtas.client.ctrl.IGTSession#setNoP2P(boolean)
	 */
	public void setNoP2P(boolean hide)
	{
		_noP2P = hide;
	}

	/**
	 * @see com.gridnode.gtas.client.ctrl.IGTSession#isNoUDDI()
	 */
	public boolean isNoUDDI()
	{
		// TODO Auto-generated method stub
		return _noUDDI;
	}

	/**
	 * @see com.gridnode.gtas.client.ctrl.IGTSession#setNoUDDI(boolean)
	 */
	public void setNoUDDI(boolean hide)
	{
		_noUDDI = hide;
	}

}
