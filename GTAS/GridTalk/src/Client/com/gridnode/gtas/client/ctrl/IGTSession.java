/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSession.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-10     Andrew Hill         Created
 * 2002-11-03     Andrew Hill         connectToGridMaster(), disconnectFromGridMaster()
 * 2002-11-26     Andrew Hill         getUser(), getUserUid()
 * 2002-12-12     Andrew Hill         getGridNodeId()
 * 2002-12-02     Andrew Hill         isRegistered()
 * 2002-12-20     Andrew Hill         startBackendListener()
 * 2003-09-01     Andrew Hill         getTimeServer();
 * 2003-04-23     Andrew Hill         isPcpKnown()
 * 2003-04-25     Andrew Hill         Allow client to specify if wants pcpKnown check on login
 * 2003-05-02     Andrew Hill         isRegistered(noCache)
 * 2003-11-05     Andrew Hill         noSecurity
 * 2005-03-14     Andrew Hill         Added the isAdmin() method to interface
 * 2006-04-24     Neo Sok Lay         Added isNoP2P(), setNoP2P(), isNoUDDI(), setNoUDDI()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import com.gridnode.pdip.framework.util.ITimeServer;

/**
 * A GTSession encapsulates a connection with the GridTalk server (B-Tier).
 * From the session one can obtain other objects such as GTManagers that provide functionality
 * for managing and controlling entities and activities in GridTalk.
 * Before doing so one must login to the server using the login() method.
 * When one has finished with a session use the logout() method to inform the GridTalk server that
 * the session is to be disposed. Once you have logged out the GTSession should be left to the
 * garbage collector, and if you wish to log in again you must create a new instance.
 */
public interface IGTSession
{

  public static final int SESSION_DEFAULT = 2;

  /**
   * Is the current session valid?
   * @return true if session valid, false if logged out.
   */
  public boolean isLoggedIn();

  /**
   * Connect to GridTalk and authenticate. The IGTHostInfo passed is usually specific to the
   * session type. See documentation for appropriate implementing class for details. If not required
   * by the session type you may pass null for this parameter.
   * @param hostInfo - information class used when connecting
   * @param userName - user ID for authentication
   * @param password - user password
   * @param checkPcpKnown - if true will cause exception if gtas doesnt know the private cert password yet
   * @throws LoginException
   */
  public void login(IGTHostInfo host, String userName, char[] password, boolean checkPcpKnown)
    throws GTClientException;

  /**
   * Invalidate this Session
   */
  public void logout() throws GTClientException;

  /**
   * Get the userid associated with the session
   * @return userId
   */
  public String getUserId();

  /**
   * return the type id for the session.
   * @return session type id
   */
  public int getSessionType();

  /**
   * Get an IGTManager for entity operations.
   * @param managerType constant value as defined in IGTManager
   * @return IGTManager
   * @throws GTClientException
   */
  public IGTManager getManager(int managerType) throws GTClientException;

  /**
   * Get an IGTManager for entity operations with specified entityType.
   * @param entityType constant value as defined in IGTEntity
   * @return IGTManager
   * @throws GTClientException
   */
  public IGTManager getManager(String entityType) throws GTClientException;

  /*
   * Given the entityType will return the manager type id relating to the type of manager responsible
   * for managing that type of entity.
   * @param entityType constant value of entity type id as defined in IGTEntity
   * @return manager tyoe constant as defined in IGTmanager
   * @throws GTClientException
   */
  public int getManagerType(String entityType) throws GTClientException;

  /*
   * Instruct GTAS to connect to the GridMaster.
   * @param securityPassword the provate certificate password
   * @throws GTClientException
   */
  public void connectToGridMaster(String securityPassword) throws GTClientException;

  /*
   * Instruct GTAS to disconnect from the GridMaster
   * @throws GTClientException
   */
  public void disconnectFromGridMaster() throws GTClientException;

  /*
   * Will return true if the GTAS is connected to the GridMaster.
   * 20030423 - THIS METHOD IS NOT YET IMPLEMENTED
   * @throws GTClientException
   * @throws UnsupportedOperationException
   */
  public boolean isConnectedToGridMaster() throws GTClientException;

  /*
   * Get the value object for the current user
   * @return user
   * @throws GTClientException
   */
  public IGTUserEntity getUser() throws GTClientException;

  /*
   * Get the uid of the current user's entity
   * @return userUid
   * @throws GTClientException
   */
  public Long getUserUid() throws GTClientException;

  /*
   * Get the node ID of GTAS's gridnode
   * @return gnId or null if unregistered
   * @throws GTClientException
   */
  public Integer getGridNodeId() throws GTClientException; //20021212AH

  /*
   * Get the name of the GTAS's gridnode
   * @return gnName or null if unregistered
   * @throws GTClientException
   */
  public String getGridNodeName() throws GTClientException; //20021214AH

  /*
   * Return true if GTAS has been registered already. This version will
   * a cached true value from the GlobalContext.
   * @throws GTClientException
   */
  public boolean isRegistered() throws GTClientException;

  /*
   * Return true if GTAS has been registered already
   * @param noCache set to true to force explicit test with GTAS instead of caching a true value
   * @throws GTClientException
   */
  public boolean isRegistered(boolean noCache) throws GTClientException;

  /*
   * Starts the backend listener that will listen for new documents to import from
   * the 'backend'
   * @throws GTClientException
   */
  public void startBackendListener() throws GTClientException; //20021220AH

  /*
   * Get an instance of an ITimeServer for use with the TimeUtils code.
   * The returned instance maintains a reference to the IGTSession that created it which it
   * uses to communicate with GTAS occasionaly to update its utcOffset value.
   * @return timeServer
   */
  public ITimeServer getTimeServer() throws GTClientException; //20030901AH

  /*
   * Returns true if GTAS knows the private certificate password. (aka: security password)
   * GTAS will know it if it has been passed to it by either setting it during registration,
   * by connecting to GridMaster, by calling setPrivateCertificatePassword explicitly
   * or if the product is unegistered where it is assumed by GTAS that the 'default' password
   * applies.
   * @return isAppStarted
   * @throws GTClientException
   */
  public boolean isPrivateCertificatePasswordKnown() throws GTClientException; //20030423AH

  /*
   * Before the GTAS application can be considered 'started' it needs to know the private
   * certificate password which can be set with this method. (aka: security password)
   * @param password
   * @throws GTClientException
   */
  public void setPrivateCertificatePassword(String password) throws GTClientException; //20030423AH
  
  /**
   * Returns true if this GridTalk is configured to run without security for the private
   * certificate password. If this is the case then the UI will present no options for setting
   * or entering the private security password, but will instead always use the default password.
   * This is not to be a permanent feature, but is implemented in response to GNDB00016109 as
   * a workaround for customer issues where GT is being run as a s service.
   */
  public boolean isNoSecurity();
  public void setNoSecurity(boolean insecure);
  
  /**
   * Basic multilevel access control divided between users who have a limited set of
   * screens and info presented to them by the UI, and admins who have all the traditional
   * abilities.
   * @return true if user has the "admin" role
   */
  public boolean isAdmin(); //20050314AH
  
  public static final String PCP_DEFAULT = "GridNode"; //20031105AH - I feel unclean
  
  /**
   * Returns the flag to indicate whether to hide P2P functions.
   * @return true if this GridTalk is configured to run without P2P functionality.
   */
  public boolean isNoP2P();
  public void setNoP2P(boolean hide);
  
  /**
   * Returns the flag to indicate whether to hide UDDI functions.
   * @return true if this GridTalk is configured to run without UDDI functionality.
   */
  public boolean isNoUDDI();
  public void setNoUDDI(boolean hide);
}