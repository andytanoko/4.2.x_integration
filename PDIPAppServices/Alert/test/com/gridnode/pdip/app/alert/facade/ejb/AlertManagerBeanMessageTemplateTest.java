/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertManagerBeanActionTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.facade.ejb;

import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.helpers.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;


import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Document me
 *
 */

public class AlertManagerBeanMessageTemplateTest
  extends    TestCase
{
  private static final String NAME_1 = "Test1";
  private static final String CONTENTTYPE_1 = "1";
  private static final String MESSAGETYPE_1 = "2";
  private static final String FROM_1 = "from1@yahoo.com";
  private static final String TO_1 = "to1@to.com";
  private static final String CC_1 = "cc1@cc.com";
  private static final String SUBJECT_1 = "Subject_1";
  private static final String MESSAGE_1 = "Message_1";
  private static final String LOCATION_1 = "C:\\output\\sample1.txt";
  private static final Boolean APPEND_1 = Boolean.FALSE;

  private static final String NAME_2 = "Name2";
  private static final String CONTENTTYPE_2 = "2";
  private static final String MESSAGETYPE_2 = "1";
  private static final String FROM_2 = "from2@yahoo.com";
  private static final String TO_2 = "to2@to.com";
  private static final String CC_2 = "cc2@cc.com";
  private static final String SUBJECT_2 = "Subject_2";
  private static final String MESSAGE_2 = "Message_2";
  private static final String LOCATION_2 = "C:\\output\\sample2.txt";
  private static final Boolean APPEND_2 = Boolean.FALSE;

  private static final String CLASSNAME = "AlertManagerBeanMessageTemplateTest";

  IAlertManagerHome home;
  IAlertManagerObj remote;

  public AlertManagerBeanMessageTemplateTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AlertManagerBeanMessageTemplateTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    home = (IAlertManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IAlertManagerHome.class);
    assertNotNull("Home is null", home);
    remote = home.create();
    assertNotNull("remote is null", remote);
    //createTestData();
    //cleanup();
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  private MessageTemplate retrieveTestMessageTemplate(String msgName) throws Exception
  {
    return remote.getMessageTemplateByName(msgName);
  }

  private void deleteTestMessageTemplate(String msgName) throws Exception
  {
    MessageTemplate retrieve = retrieveTestMessageTemplate(msgName);
    if (retrieve != null)
       remote.deleteMessageTemplate(new Long(retrieve.getUId()));
  }

  private MessageTemplate addTestMessageTemplate(String name, String contentType, String messageType, String fromaddr, String toaddr, String cc, String subject, String message, String location, Boolean append)
          throws Exception
  {
    MessageTemplate inserted = createMessageTemplate(name, contentType, messageType, fromaddr, toaddr, cc, subject, message, location, append);
    remote.createMessageTemplate(inserted);
    return remote.getMessageTemplateByName(inserted.getName());
  }

  private MessageTemplate createMessageTemplate(String name, String contentType, String messageType, String fromaddr, String toaddr, String cc, String subject, String message, String location, Boolean append)
  {
    MessageTemplate msg = new MessageTemplate();
    msg.setName(name);
    msg.setContentType(contentType);
    msg.setMessageType(messageType);
    msg.setFromAddr(fromaddr);
    msg.setToAddr(toaddr);
    msg.setCcAddr(cc);
    msg.setSubject(subject);
    msg.setMessage(message);
    msg.setLocation(location);
    msg.setAppend(append);
    return msg;
  }

  private void checkIdenticalMessageTemplate(MessageTemplate msg1, MessageTemplate msg2, boolean checkUId)
  {
    assertEquals("Name not the same!", msg1.getName(), msg2.getName());
    assertEquals("Category not the same!", msg1.getContentType(), msg2.getContentType());
    assertEquals("Description not the same!", msg1.getFromAddr(), msg2.getFromAddr());
    if (checkUId)
       assertEquals("UId not the same!", msg1.getUId(), msg2.getUId());
  }

  public void testCreateMsg() throws Exception
  {
    try
    {
      MessageTemplate retrieved = null;

      // test create msg.
      MessageTemplate msg1 = createMessageTemplate(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplatePass(msg1, NAME_1);

      // test create duplicate MessageTemplate.
      MessageTemplate msg2 = createMessageTemplate(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg2, "Able to add in duplicate MessageTemplate into the database.");

      // test create 2nd MessageTemplate
      MessageTemplate msg3 = createMessageTemplate(NAME_2, CONTENTTYPE_2, MESSAGETYPE_2, FROM_2, TO_2, CC_2, SUBJECT_2, MESSAGE_2, LOCATION_2, APPEND_2);
      checkCreateMessageTemplatePass(msg3, NAME_2);

      // test create MessageTemplate with same msgName, content type, action type and subject but different message.
      MessageTemplate msg4 = createMessageTemplate(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_2, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg4, "same msgName, content type, action type and subject " +
                                       "but different message.");

      // test create MessageTemplate with same msgName, content type, action type and message but different subject.
      MessageTemplate msg5 = createMessageTemplate(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_2, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg5, "Able to add in same msgName, content type, action type " +
                                       "and message but different subject.");

      // test create MessageTemplate with same msgName, content type, message and subject but different action type .
      MessageTemplate msg6 = createMessageTemplate(NAME_1, CONTENTTYPE_1, MESSAGETYPE_2, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg6, "able to add in same msgName, content type, message  "+
                                        "and subject but different action type");

      // test create MessageTemplate with same msgName, actiontype, message and subject but different content type .
      MessageTemplate msg7 = createMessageTemplate(NAME_1, CONTENTTYPE_2, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg6, "able to add in same msgName, actiontype, message "+
                                        "and subject but different content type");

      // test create MessageTemplate with same content type, action type, message and subject but different msgname.
      MessageTemplate msg9 = createMessageTemplate(NAME_2, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkCreateMessageTemplateFail(msg6, "able to add in same content type, action type, message "+
                                        "and subject but different msgname");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testCreateMessageTemplate", "fails", ex);
      throw ex;
    }
    finally
    {
      deleteTestMessageTemplate(NAME_1);
      deleteTestMessageTemplate(NAME_2);
    }
  }

  private void checkCreateMessageTemplatePass(MessageTemplate msg, String msgName) throws Exception
  {
    remote.createMessageTemplate(msg);
  }

  private void checkCreateMessageTemplateFail(MessageTemplate msg, String errorMessage)
  {
    try
    {
      remote.createMessageTemplate(msg);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

/*
  public void testUpdateMessage() throws Exception
  {
    try
    {
      Message existing = null;

      // test update non-existing alert.
      Message msg1 = createMessage(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkUpdateMessageFail(msg1, "Able to update non-existing msg.");

      // create alert in database
      existing = addTestMessage(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);

      // test update existing alert change desc2.
      existing.setFromAddr(FROM_2);

      // test update existing alert change dataType using updated version.
      existing = retrieveTestMessage(NAME_1);
      existing.setToAddr(TO_2);

      // test update existing alert change category using updated version.
      existing = retrieveTestMessage(NAME_1);
      existing = remote.getMessageByMessageUId(new Long(existing.getUId()));
      existing.setCcAddr(CC_2);

      // test update existing alert change alertName using updated version.
      existing = retrieveTestMessage(NAME_1);
      assertNull("Alert 2 exist. Test environment not set properly.", retrieveTestMessage(NAME_2));
      existing.setName(NAME_2);
      checkUpdateMessagePass(existing, NAME_2);
      assertNull("Alert 1 exist. Result not correct. Two copies of same data: Name1 and Name2!", retrieveTestMessage(NAME_1));

      //delete all alerts created
      deleteTestMessage(existing.getName());
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testUpdateAlert", "fails", ex);
      throw ex;
    }
    finally
    {
      deleteTestMessage(NAME_1);
      deleteTestMessage(NAME_2);
    }
  }



  private void checkUpdateMessageFail(Message msg, String errorMessage)
  {
    try
    {
      remote.updateMessage(msg);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkUpdateMessagePass(Message msg, String msgName) throws Exception
  {
    remote.updateMessage(msg);
    Message retrieved = retrieveTestMessage(msgName);
    checkIdenticalMessage(msg, retrieved, false);
  }

  public void testDeleteMessage() throws Exception
  {
    try
    {
      Message insert1 = null;
      // Delete non-existing Alert
      Message msg1 = createMessage(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkDeleteMessageFail(msg1, "delete non-existing alert");

      // Create Alert.
      insert1 = addTestMessage(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);

      // Delete existing alert
      checkDeleteMessagePass(insert1, NAME_1);

    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testDeleteAlert", "fails", ex);
      throw ex;
    }
  }

  private void checkDeleteMessagePass(Message msg, String msgName) throws Exception
  {
    remote.deleteMessage(new Long(msg.getUId()));
    Message retrieve = retrieveTestMessage(msgName);
    assertNull("Delete not successful.", retrieve);
  }

  private void checkDeleteMessageFail(Message msg, String errorMessage) throws Exception
  {
    try
    {
      remote.deleteMessage(new Long(msg.getUId()));
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetMessageByMessageName() throws Exception
  {
    try
    {
      Message msg = addTestMessage(NAME_1, CONTENTTYPE_1, MESSAGETYPE_1, FROM_1, TO_1, CC_1, SUBJECT_1, MESSAGE_1, LOCATION_1, APPEND_1);
      checkGetMessageByMessageNameSuccess(NAME_1, msg);
      remote.deleteMessage(new Long(msg.getUId()));
      // Find non-exisitng
      checkGetMessageByMessageNameFail("Sample", "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetMessageByMessageName", "fails", ex);
      throw ex;
    }
  }

  private void checkGetMessageByMessageNameFail(String msgName, String errorMsg) throws Exception
  {
    try
    {
      Message findMsg = remote.getMessageByMessageName(msgName);
      assertNull(errorMsg, findMsg);
    }
    catch(Exception e)
    {
    }
  }

  private void checkGetMessageByMessageNameSuccess(String msgName, Message msg) throws Exception
  {
    try
    {
      Message findMsg = remote.getMessageByMessageName(msgName);
      assertNotNull(findMsg);
    }
    catch(Exception e)
    {
    }
  }
*/
}