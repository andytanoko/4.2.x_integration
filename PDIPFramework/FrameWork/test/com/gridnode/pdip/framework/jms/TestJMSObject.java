
package com.gridnode.pdip.framework.jms;

import javax.jms.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestJMSObject extends TestCase
{

    public TestJMSObject(String s)
    {
        super(s);
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    public void testJMSQueue() throws Exception{
        execJMSQueue(JMSUtilFactory.DEFAULT_JMSCONGIG,false,Session.AUTO_ACKNOWLEDGE);
    }
    public void testJMSTopic() throws Exception{
        execJMSTopic(JMSUtilFactory.DEFAULT_JMSCONGIG,false,Session.AUTO_ACKNOWLEDGE);
    }


    public void execJMSQueue(String configName,boolean transacted,int ackType) throws Exception
    {

        JMSUtilFactory jmsUtilFactory = JMSUtilFactory.getInstance(configName);

        assertNotNull("Unable to get JMSUtilFactory ", jmsUtilFactory);
        JMSObject queueObj = jmsUtilFactory.createQueueObject();

        assertNotNull("Unable to create JMSObject", queueObj);
        queueObj.initSession(transacted,ackType);

        Queue queue = (Queue) queueObj.createTempDestination();
        Queue receiveQueue =queue;
        assertNotNull("Unable to create TempQueue", queue);

        queueObj.initProducer(queue);
        Message msg = queueObj.getSession().createMessage();

        msg.setStringProperty("TestKey", "QueueMessageTest");
        TestJMSListener testListener = new TestJMSListener();

        queueObj.initConsumer(receiveQueue, testListener);
        queueObj.sendMessage(queue,msg);
        //if it fails ,increase the delay time
        Thread.sleep(1000);
        assertEquals("Unable to send message to queue :" + testListener.getMessageString(), "QueueMessageTest", testListener.getMessageString());
        queueObj.closeSession();
    }

    public void execJMSTopic(String configName,boolean transacted,int ackType) throws Exception
    {
        JMSUtilFactory jmsUtilFactory = JMSUtilFactory.getInstance(configName);
        assertNotNull("Unable to get JMSUtilFactory ", jmsUtilFactory);

        JMSObject topicObj = jmsUtilFactory.createTopicObject();
        assertNotNull("Unable to create JMSObject", topicObj);

        topicObj.initSession(transacted,ackType);

        Topic topic = (Topic) topicObj.createTempDestination();
        Topic receiveTopic = topic;
        assertNotNull("Unable to create topic", topic);
        topicObj.initProducer(topic);

        Message msg = topicObj.getSession().createMessage();

        msg.setStringProperty("TestKey", "TopicMessageTest");
        TestJMSListener testListener = new TestJMSListener();

        topicObj.initConsumer(receiveTopic, testListener);
        topicObj.sendMessage(msg);
        //if it fails ,increase the delay time
        Thread.sleep(1000);
        assertEquals("Unable to send message to Topic :" + testListener.getMessageString(), "TopicMessageTest", testListener.getMessageString());
        topicObj.closeSession();
    }


    class TestJMSListener implements MessageListener
    {
        String msg;
        public String getMessageString()
        {
            return msg;
        }

        public void onMessage(Message message)
        {
            try
            {
                msg = message.getStringProperty("TestKey");
            }
            catch (JMSException jex)
            {
                System.out.println("Error in onMessage");
                jex.printStackTrace();
            }
        }
    }

    public static Test suite()
    {
        return new TestSuite(TestJMSObject.class);
    }

    public static void main(String args[]) throws Exception
    {
        junit.textui.TestRunner.run(suite());
    }

}
