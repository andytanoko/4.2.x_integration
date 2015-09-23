/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    MAHESH              Created
 * Jan 19 2007    Neo Sok Lay         Use ServiceLocator for JNDI lookup
 * Feb 07 2007		Alain Ah Ming				Refactor logError to include error code
 */
package com.gridnode.pdip.framework.jms;


import java.util.Hashtable;
import java.util.Properties;

import javax.jms.*;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * 
 * @author Mahesh
 * @since
 * @version GT 4.0 VAN
 */
public class JMSUtilFactory
{
    public static final String DEFAULT_JMSCONGIG = "jms";

    private static final String JNDI_SERVICE = "jms.jndiService";
    private static final String QUEUE_FACTORY = "jms.queueConnFactoryName";
    private static final String TOPIC_FACTORY = "jms.topicConnFactoryName";
    private static final String CONNECTION_USERNAME = "jms.connection.userName";
    private static final String CONNECTION_PASSWORD = "jms.connection.password";
    private static final String CONNECTION_RETRY_COUNT = "jms.connection.retryCount";
    private static final String CONNECTION_RETRY_DELAY = "jms.connection.retryDelay";
    private static final String MESSAGE_RETRY_COUNT = "jms.message.retryCount";
    private static final String MESSAGE_RETRY_DELAY = "jms.message.retryDelay";

    public static final String DEFAULT_TOPIC_FACTORY="java:/ConnectionFactory";
    public static final String DEFAULT_QUEUE_FACTORY="java:/ConnectionFactory";

    private static Hashtable jmsUtilCache = new Hashtable();

    ConnectionFactory queueConnFactory, topicConnFactory;
    Connection queueConn, topicConn;
    String jndiService;
    long  msgRetryDelay = 0;
    int  msgRetryCount = 0;

    private JMSUtilFactory(String propertiesFileKey)
    {
        logInfo("[JMSUtilFactory.JMSUtilFactory] Enter");
        try
        {
            Configuration config = ConfigurationManager.getInstance().getConfig(
            propertiesFileKey);
            Properties prop=config.getProperties();

            jndiService = prop.getProperty(JNDI_SERVICE);
            //ServiceLookup serviceLookup = ServiceLookup.getInstance(jndiService);
            JNDIFinder finder = ServiceLocator.instance(jndiService).getJndiFinder();

            String userName=prop.getProperty(CONNECTION_USERNAME);
            String password=prop.getProperty(CONNECTION_PASSWORD);

            int connRetryCount = 0;
            long connRetryDelay = 0;
            if (prop.get(CONNECTION_RETRY_COUNT) != null && prop.get(CONNECTION_RETRY_DELAY) != null)
            {
                connRetryCount = Integer.parseInt(prop.getProperty(CONNECTION_RETRY_COUNT));
                connRetryDelay = Long.parseLong(prop.getProperty(CONNECTION_RETRY_DELAY));
            }

            //logDebug("[JMSUtilFactory.JMSUtilFactory] ,CONTEXT ENV: "+serviceLookup.getInitialContext().getEnvironment());

            queueConnFactory = (QueueConnectionFactory) finder.lookup(prop.getProperty(QUEUE_FACTORY,DEFAULT_QUEUE_FACTORY));
            queueConn=getJMSConnection(userName, password, connRetryCount, connRetryDelay, JMSObject.QUEUE_TYPE);
            queueConn.start();
            logDebug("[JMSUtilFactory.JMSUtilFactory] ,After Opening Queue Connection :"+queueConn);

            topicConnFactory = (TopicConnectionFactory) finder.lookup(prop.getProperty(TOPIC_FACTORY,DEFAULT_TOPIC_FACTORY));
            topicConn=getJMSConnection(userName, password, connRetryCount, connRetryDelay, JMSObject.TOPIC_TYPE);
            topicConn.start();
            logDebug("[JMSUtilFactory.JMSUtilFactory] ,After Opening Topic Connection :"+topicConn);


            if (prop.get(MESSAGE_RETRY_COUNT) != null && prop.get(MESSAGE_RETRY_DELAY) != null)
            {
              msgRetryCount = Integer.parseInt(prop.getProperty(MESSAGE_RETRY_COUNT));
              msgRetryDelay = Long.parseLong(prop.getProperty(MESSAGE_RETRY_DELAY));
            }


        }
        catch (Exception e)
        {
            logError(ILogErrorCodes.JMS_GENERIC, "[JMSUtilFactory.JMSUtilFactory] Exception", e);
        }
    }

    public QueueConnectionFactory getQueueConnectionFactory()
    {
        return (QueueConnectionFactory) queueConnFactory;
    }

    public TopicConnectionFactory getTopicConnectionFactory()
    {
        return (TopicConnectionFactory) topicConnFactory;
    }

    public JMSObject createQueueObject() throws JMSException
    {
        return new JMSObject(JMSObject.QUEUE_TYPE, queueConn, jndiService, msgRetryCount, msgRetryDelay);
    }

    public JMSObject createTopicObject() throws JMSException
    {
        return new JMSObject(JMSObject.TOPIC_TYPE, topicConn, jndiService, msgRetryCount, msgRetryDelay);
    }

    private Connection getJMSConnection(String userName, String password,int connRetryCount,long connRetryDelay, int type) throws JMSException
    {
        JMSException rjex = null;
        int count = 0;

        do
        {   logDebug("[JMSUtilFactory.getJMSConnection] in Connection loop "+count);
            if (rjex != null)
            {
                try
                {
                    Thread.sleep(connRetryDelay);
                }
                catch (Exception e)
                {
                }
            }
            try
            {
                Connection connection = null;

                if (type == JMSObject.QUEUE_TYPE)
                {
                    if (userName != null && password != null)
                        connection = ((QueueConnectionFactory) queueConnFactory).createQueueConnection(userName, password);
                    else connection = ((QueueConnectionFactory) queueConnFactory).createQueueConnection();
                } else if (type == JMSObject.TOPIC_TYPE)
                {
                    if (userName != null && password != null)
                        connection = ((TopicConnectionFactory) topicConnFactory).createTopicConnection(userName, password);
                    else connection = ((TopicConnectionFactory) topicConnFactory).createTopicConnection();
                }
                return connection;
            }
            catch (JMSSecurityException jsex)
            {
                logError(ILogErrorCodes.JMS_GENERIC, "[JMSUtilFactory.getJMSConnection] invalid username and password ", jsex);
                throw jsex;
            }
            catch (JMSException jex)
            {
                logError(ILogErrorCodes.JMS_GENERIC, "[JMSUtilFactory.getJMSConnection] unable to open connection count=" + count, jex);
                rjex = jex;
            }
        }
        while (count++ < connRetryCount);
        throw rjex;
    }

    public void closeConnection(Connection conn)
    {
        try
        {
            if (conn != null)
                conn.close();
        }
        catch (JMSException jex)
        {
            logError(ILogErrorCodes.JMS_GENERIC, "[JMSUtilFactory.closeConnection] unable to closeConnection ", jex);
        }
        finally
        {
            logDebug("[JMSUtilFactory.closeConnection] Connection closed ");
        }
    }

    protected void finalize() throws Throwable
    {
        try
        {
            closeConnection(topicConn);
            closeConnection(queueConn);
        }
        catch (Throwable t)
        {
        }
        super.finalize();
    }


    public static JMSUtilFactory getInstance(String propertiesFile)
    {
        Object jmsUtil = jmsUtilCache.get(propertiesFile);

        if (jmsUtil == null)
        {
            synchronized(jmsUtilCache){
                jmsUtil=jmsUtilCache.get(propertiesFile);
                if (jmsUtil == null){
                    jmsUtil = new JMSUtilFactory(propertiesFile);
                    jmsUtilCache.put(propertiesFile, jmsUtil);
                }
            }
        }
        return (JMSUtilFactory)jmsUtil;
    }

    private static void logInfo(String msg)
    {
        Log.log(Log.FRAMEWORK, msg);
    }

    private static void logDebug(String msg)
    {
        Log.debug(Log.FRAMEWORK, msg);
    }

    private static void logError(String errorCode, String msg, Throwable th)
    {
        Log.error(errorCode, Log.FRAMEWORK, msg, th);
    }
}
