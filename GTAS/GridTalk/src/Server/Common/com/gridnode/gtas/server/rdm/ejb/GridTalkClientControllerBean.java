/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkClientControllerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 17 2002    Neo Sok Lay         Add temporary code to fake a session id
 *                                    on ejbCreate().
 * Jun 05 2002    Neo Sok Lay         Create real session.
 * Jun 13 2002    Neo Sok Lay         Hook security control for processing
 *                                    guarded event.
 * Nov 11 2005    Neo Sok Lay         Use java.security.Policy       
 * Feb 28 2007    Neo Sok Lay         GNDB00028183: stackoverflow when always
 *                                    set a new ACLPolicy.                            
 */
package com.gridnode.gtas.server.rdm.ejb;

import java.security.PrivilegedActionException;
import java.util.Collection;

import javax.ejb.CreateException;
import java.security.Policy;
import javax.security.auth.Subject;

import com.gridnode.gtas.server.GridTalkPrivilegedAction;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.helpers.IGtasConfig;
import com.gridnode.pdip.base.acl.auth.ACLPolicy;
import com.gridnode.pdip.base.acl.auth.ACLSecurityManager;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.ejb.EJBClientControllerBean;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * Session Bean implementation for EJBClientController EJB.
 * See the StateMachine for more details.
 */
public class GridTalkClientControllerBean extends EJBClientControllerBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3716954753909021067L;

  //only init per new class loading
  static 
  {
    initSecurity();
  }
  
	public void ejbCreate() throws CreateException
  {
    super.ejbCreate();
    try
    {
      readConfigurations();
      openNewSession();
      //initSecurity();
    }
    catch (Exception ex)
    {
      throw new CreateException("Unable to create client controller bean: "+ex.getMessage());
    }
  }

  public void ejbRemove()
  {
    try
    {
      closeSession();
    }
    catch (Exception ex)
    {
      System.out.println("Unable to close session properly: "+ex.getMessage());
    }
    super.ejbRemove();
  }

  public IEventResponse processEvent(IEvent event)
    throws EventException
  {
    keepSessionActive();

    return doPrivilegedEvent(event);
  }

  /**
   * Process a event under the privileges granted to the subject
   * authenticated for the session.
   *
   * @param event The event to perform.
   * @return The event response object from processing the event.
   * @exception EventException Error in process the event. Could be due
   * to invalid event parameters, permission denied, etc.
   *
   * @since 2.0
   */
  private IEventResponse doPrivilegedEvent(IEvent event)
    throws EventException
  {
    Subject subject = new Subject();
    Collection principals = (Collection)this.sm.getAttribute(IAttributeKeys.PRINCIPALS);
    if (principals != null)
      subject.getPrincipals().addAll(principals);

    try
    {
      return (IEventResponse)Subject.doAsPrivileged(
                               subject,
                               new GridTalkPrivilegedAction(event, sm),
                               null);
    }
    catch (PrivilegedActionException ex)
    {
      throw (EventException)ex.getException();
    }
    catch (SecurityException ex)
    {
      throw new EventException(
                  "Permission denied: "+ex.getMessage());
    }
  }

  private void readConfigurations()
  {
    ConfigurationManager manager = ConfigurationManager.getInstance();
    Configuration gtasConfig = manager.getConfig(IGtasConfig.CONFIG_NAME);

    sm.setAttribute(
        IAttributeKeys.ENTERPRISE_ID,
        gtasConfig.getString(IGtasConfig.ENTERPRISE_ID));

    sm.setAttribute(
      IAttributeKeys.APPLICATION,
      gtasConfig.getString(IGtasConfig.APPLICATION_NAME));
    //sm.setAttribute(IAttributeKeys.APPLICATION, "gridtalk");
  }

  /**
   * Initialize the security controls by installing the security policy and
   * security manager for the application.
   *
   * @since 2.0
   */
  private static void initSecurity()
  {
    //NSL20070228 Only set a new ACLPolicy if not already set
    if (!(Policy.getPolicy() instanceof ACLPolicy))
    {
      Policy.setPolicy(new ACLPolicy());
    }
    GridTalkPrivilegedAction.setSecurityManager(new ACLSecurityManager());
  }

  /**
   * Opens a new session.
   *
   * @exception Error in opening a new session.
   *
   * @since 2.0
   */
  private void openNewSession() throws Exception
  {
    /** open a new session */
    String sessionID = getSessionManager().openSession();
    sm.setAttribute(IAttributeKeys.SESSION_ID, sessionID);

    /** temporary code to fake a session id.
    Random rand = new Random(System.currentTimeMillis());
    byte[] sessionBytes = new byte[10];
    rand.nextBytes(sessionBytes);

    sm.setAttribute(IAttributeKeys.SESSION_ID, new String(sessionBytes));
    */
  }

  /**
   * Close the current session.
   *
   * @exception Error in closing the current session.
   *
   * @since 2.0
   */
  private void closeSession() throws Exception
  {
    /** close the current session. how about logout? */
    getSessionManager().closeSession((String)sm.getAttribute(IAttributeKeys.SESSION_ID));
  }

  /**
   * Keep the current session active. This is to prevent the session from
   * time-out.
   *
   * @since 2.0
   */
  private void keepSessionActive()
  {
    try
    {
      getSessionManager().keepActiveSession(
        (String)sm.getAttribute(IAttributeKeys.SESSION_ID));
    }
    catch (Exception ex)
    {
      System.out.println("Unable to keep session active: "+ex.getMessage());
    }

  }

  /**
   * Obtain the SessionManagerBean by doing a Jndi lookup.
   *
   * @return The proxy interface to the SessionManagerBean.
   * @exception ServiceLookupException Error in looking up the SessionManagerBean.
   *
   * @since 2.0
   */
  private ISessionManagerObj getSessionManager() throws ServiceLookupException
  {
    return (ISessionManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             ISessionManagerHome.class.getName(),
             ISessionManagerHome.class,
             new Object[0]);
  }
}
