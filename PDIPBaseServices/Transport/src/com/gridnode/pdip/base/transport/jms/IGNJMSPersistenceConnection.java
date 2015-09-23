/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGNJMSPersistenceConnection.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 02 2002    Goh Kan Mun                 Created
 * Jan 06 2003    Goh Kan Mun                 Modified - throw GNTptInvalidCommInfoException
 *                                                       at addListener method to close jndi.
 */
package com.gridnode.pdip.base.transport.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.gridnode.pdip.base.transport.exceptions.GNTptPersistenceConnectionException;
import com.gridnode.pdip.base.transport.exceptions.InvalidCommInfoException;

/**
 * This interface defines the common methods that a JMS persistence connection
 * (TopicConnection or QueueConnection) should provide.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IGNJMSPersistenceConnection
{
  /**
   * To add a message listener to the destination name specified.
   * @param           destinationName       Topic or Queue name interested in receiving message
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void addListener(String destinationName)
         throws GNTptPersistenceConnectionException, NamingException, JMSException,
                InvalidCommInfoException;

  /**
   * To close the persistence connection.
   * @exception       NamingException                     thrown when encounter a Naming error.
   */
  public void close() throws JMSException;

  /**
   * To check whether a message listener is already present.
   * @param           listenerKey       id of the listener
   * @return          true if and only if the listener is already present.
   */
  public boolean containListener(String listenerKey);

  /**
   * To retrieve the header information passed down by the BL/Channel.
   * @return          the header information.
   */
  public String[] getHeader();

  /**
   * To remove a message listener specified by the listenerKey.
   * @param           listenerKey       id of the listener
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void removeListener(String listenerKey)
         throws GNTptPersistenceConnectionException, JMSException;

  /**
   * To send the message to the specified destination.
   * @param           destinationName       queue / topic name
   * @param           msg                   Serializable message to be sent
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void send(String destinationName, Serializable msg)
         throws InvalidCommInfoException, GNTptPersistenceConnectionException, NamingException, JMSException;

  /**
   * To set a exception listener to the persistence connection.
   * @param           connectionKey       id of the persistence connection
   *
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void setExceptionListener(String connectionKey) throws JMSException;

}
