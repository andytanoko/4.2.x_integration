/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Notifier.java
 * -- ToBeRemoved ---
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 * Seo 02 2003    Neo Sok Lay         Extends from pdip.framework.notification.Notifier
 */
package com.gridnode.gtas.server.notify;


/**
 * This class handles broadcasting notifications to a Notifier topic. Interested
 * parties would need to implement a Message Driven Bean or a Message Listener
 * and subscribe to the Notifier topic.<p>
 *
 * When Notifier is being initialized, it reads from the <b>notifier
 * configuration file</b> the following properties:<p>
 * <li><code>jms.connection.jndi</code> - the jndi name to lookup the JMS connection
 * factory.</li>
 * <li><code>notifier.topic.jndi</code> - the jndi name to lookup the topic for
 * Notifier.</li>
 *
 * The <b>notifier configuration file</b> will be obtained from the
 * {@link ConfigurationManager} via the <code>getConfig(config_name)</code>
 * method whereby the <code>config_name</code> is "notifier". Thus this entry
 * have to be specified in the <b>config.properties</b> file for the application.
 *
 *
 * @author Neo Sok Lay
 * @version GT 2.2
 * @since 2.0 I6
 */
public class Notifier extends com.gridnode.pdip.framework.notification.Notifier
{
  
  private Notifier()
  {
  }
  
  
}