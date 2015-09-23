package com.gridnode.gtas.server.docalert.facade.ejb;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.docalert.helpers.*;

import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.model.Action;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.Iterator;
import java.util.Collection;

/**
 * This test case tests the response tracking functionality of the DocAlertManagerBean:
 * <p>
 * <ol>
 *   <li>startResponseTracking()</li>
 *   <li>invalidateResponseTracking()</li>
 * </ol>
 * <p>
 * Test data:<pre>
 * gtas/data/doc/udoc/po.xml
 * gtas/data/doc/udoc/por.xml
 * gtas/data/alert/email/code2email.properties
 * gtas/data/alert/email/code2role.properties
 * </pre>
 * <p>
 * Required Configurations:<pre>
 * conf/default/pdip/framework/mail.properties
 * </pre>
 * <p><b>NOTE:</b><p>
 * To test the DocAlertManagerBean.timeForReminder() method,
 * comment off the invalidateTrackingTest() in the unitTest(), and the po.xml
 * file should indicate at least 2 day earlier than test date.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DocAlertManagerBeanTest extends ActionTestHelper
{
  private IAlertManagerObj _alertMgr;
  private IDocAlertManagerObj _docAlertMgr;
  private IDocumentManagerObj _docMgr;
  private GridDocument _sentGDoc;
  private GridDocument _recvGDoc;
  private Long         _trackRecUID;
  private static final int _DAYS_TO_REMINDER_2 = 2;
  private static final int _DAYS_TO_REMINDER_5 = 5;
  private static final int _DAYS_TO_REMINDER_10 = 10;
  private static final File _PO_XML        = new File("DocAlert/po.xml");
  private static final File _POR_XML       = new File("DocAlert/por.xml");
  private static final String _DEST_UDOC_PATH = "D:/J2EE/jboss-3.0.0alpha/bin/gtas/data/doc/udoc/";

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(DocAlertManagerBeanTest.class);
  }

  public DocAlertManagerBeanTest(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.setUp] Enter");

      _alertMgr = getAlertMgr();
      _docAlertMgr = getDocAlertMgr();
      _docMgr = getDocMgr();
      super.setUp();
    }
    finally
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.setUp] Exit");
    }
  }

  protected void cleanUp()
  {
    deleteGridDocs();
    deleteAlerts();
    deleteResponseTrackRecord();
  }

  protected void cleanTestData() throws java.lang.Exception
  {
  }

  protected void prepareTestData() throws java.lang.Exception
  {
    createAlerts();
    createResponseTrackRecord();
    createSentGridDoc();
    createReceivedGridDoc();
  }

  protected void unitTest() throws java.lang.Exception
  {
    startTrackingTest();

    invalidateTrackingTest();
  }

  // not used
  protected IEJBAction createNewAction()
  {
    return null;
  }

  // not used
  protected void checkActionEffect(BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }

  protected void startTrackingTest() throws Exception
  {
    try
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.startTrackingTest] Enter");

      _docAlertMgr.startResponseTracking(_sentGDoc);

      checkActiveTrackRecordCreated();
    }
    catch (Throwable ex)
    {
      Log.err("TEST", "[DocAlertManagerBeanTest.startTrackingTest] Error", ex);
      assertTrue("startTrackingTest failed", false);
    }
    finally
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.startTrackingTest] Exit");
    }

  }

  protected void checkActiveTrackRecordCreated() throws Exception
  {
    Long sentGDocUID = (Long)_sentGDoc.getKey();

    ActiveTrackRecord rec = _docAlertMgr.findActiveTrackRecord(
                              _trackRecUID,
                              sentGDocUID,
                              _DAYS_TO_REMINDER_2);
    assertNotNull("ActiveTrackRecord not created!", rec);
  }

  protected void invalidateTrackingTest() throws Exception
  {
    try
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.invalidateTrackingTest] Enter");

      _docAlertMgr.invalidateResponseTracking(_recvGDoc);

      checkActiveTrackRecordDeleted();
    }
    catch (Throwable ex)
    {
      Log.err("TEST", "[DocAlertManagerBeanTest.invalidateTrackingTest] Error", ex);
      assertTrue("invalidateTrackingTest failed", false);
    }
    finally
    {
      Log.log("TEST", "[DocAlertManagerBeanTest.invalidateTrackingTest] Exit");
    }

  }

  protected void checkActiveTrackRecordDeleted() throws Exception
  {
    Long sentGDocUID = (Long)_sentGDoc.getKey();

    ActiveTrackRecord rec = _docAlertMgr.findActiveTrackRecord(
                              _trackRecUID,
                              sentGDocUID,
                              _DAYS_TO_REMINDER_2);
    assertNull("ActiveTrackRecord not deleted!", rec);
  }

  protected void createSentGridDoc() throws Exception
  {
    GridDocument gdoc = new GridDocument();
    gdoc.setDateTimeCreate(TimeUtil.localToUtcTimestamp());
    gdoc.setDateTimeImport(TimeUtil.localToUtcTimestamp());
    gdoc.setDateTimeSendStart(TimeUtil.localToUtcTimestamp());
    gdoc.setFolder(GridDocument.FOLDER_OUTBOUND);
    gdoc.setSenderBizEntityId("DEF");
    gdoc.setSenderNodeId(new Long(521));
    gdoc.setSenderUserId("admin");
    gdoc.setSenderRoute(GridDocument.ROUTE_DIRECT);
    gdoc.setRecipientBizEntityId("DEF");
    gdoc.setRecipientNodeId(new Long(522));
    gdoc.setRecipientPartnerId("P522");
    gdoc.setUdocDocType("PO");
    gdoc.setUdocFilename("po.xml");
    gdoc.setUdocFileType("xml");

    Long uid = (Long)_docMgr.createGridDocument(gdoc).getKey();
    _sentGDoc = _docMgr.findGridDocument(uid);
    copyUdoc(_PO_XML, "po.xml");
  }

  protected void copyUdoc(File file, String filename) throws Exception
  {
    FileReader reader = new FileReader(file);
    FileWriter writer = new FileWriter(_DEST_UDOC_PATH+filename);

    long max = file.length();
    char[] buff = new char[255];
    int offset = 0;
    while (offset < max)
    {
      int num = reader.read(buff);
      writer.write(buff, 0, num);
      offset += num;
    }
    writer.flush();
    writer.close();
  }

  protected void deleteGridDocs()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridDocument.U_DOC_DOC_TYPE,
        filter.getEqualOperator(), "PO", false);
      filter.addSingleFilter(filter.getOrConnector(),
        GridDocument.U_DOC_DOC_TYPE, filter.getEqualOperator(), "POR", false);

      Collection toDelete = _docMgr.findGridDocumentsKeys(filter);
      if (toDelete != null)
      {
        for (Iterator i=toDelete.iterator(); i.hasNext(); )
        {
          _docMgr.deleteGridDocument((Long)i.next());
        }
      }
    }
    catch (Exception ex)
    {

    }

  }

  protected void createReceivedGridDoc() throws Exception
  {
    GridDocument gdoc = new GridDocument();
    gdoc.setDateTimeCreate(TimeUtil.localToUtcTimestamp());
    gdoc.setDateTimeReceiveStart(TimeUtil.localToUtcTimestamp());
    gdoc.setDateTimeReceiveEnd(TimeUtil.localToUtcTimestamp());
    gdoc.setFolder(GridDocument.FOLDER_INBOUND);
    gdoc.setSenderBizEntityId("DEF");
    gdoc.setSenderNodeId(new Long(522));
    gdoc.setSenderUserId("admin");
    gdoc.setSenderPartnerId("P522");
    gdoc.setSenderRoute(GridDocument.ROUTE_DIRECT);
    gdoc.setRecipientBizEntityId("DEF");
    gdoc.setRecipientNodeId(new Long(521));
    gdoc.setRecipientPartnerId("P521");
    gdoc.setUdocDocType("POR");
    gdoc.setUdocFilename("por.xml");
    gdoc.setUdocFileType("xml");

    Long uid = (Long)_docMgr.createGridDocument(gdoc).getKey();
    _recvGDoc = _docMgr.findGridDocument(uid);
    copyUdoc(_POR_XML, "por.xml");
  }

  protected void createResponseTrackRecord() throws Exception
  {
    // response track record x 1
    ResponseTrackRecord trackRec = new ResponseTrackRecord();
    trackRec.setAlertRecipientXpath("/POR/BuyerCode");
    trackRec.setReceiveResponseAlert(RECV_RESPONSE);
    trackRec.setResponseDocIdXpath("/POR/PONumber");
    trackRec.setResponseDocType("POR");
    trackRec.setSentDocIdXpath("/PO/PONumber");
    trackRec.setSentDocType("PO");
    trackRec.setStartTrackDateXpath("/PO/PODate");

    Long uid = _docAlertMgr.createResponseTrackRecord(trackRec);
    createReminderAlerts(uid);
    _trackRecUID = uid;
  }

  protected void deleteResponseTrackRecord()
  {
    try
    {
      ResponseTrackRecord trackRec = _docAlertMgr.findResponseTrackRecordBySentDocType("PO");
      if (trackRec != null)
      {
        Long trackRecUID = (Long)trackRec.getKey();

        _docAlertMgr.deleteResponseTrackRecord(trackRecUID);
        //reminderalerts deletion is taken care of.
      }
    }
    catch (Exception ex)
    {

    }

  }

  protected void createReminderAlerts(Long trackRecUID) throws Exception
  {
    // 2 day reminder alert
    ReminderAlert reminderAlert = new ReminderAlert();
    reminderAlert.setAlertToRaise(REMINDER);
    reminderAlert.setDaysToReminder(_DAYS_TO_REMINDER_2);
    reminderAlert.setDocRecipientXpath("/PO/VendorCode");
    reminderAlert.setTrackRecordUID(trackRecUID);

    _docAlertMgr.createReminderAlert(reminderAlert);

    // 5 day reminder alert
    reminderAlert = new ReminderAlert();
    reminderAlert.setAlertToRaise(REMINDER);
    reminderAlert.setDaysToReminder(_DAYS_TO_REMINDER_5);
    reminderAlert.setDocRecipientXpath("/PO/VendorCode");
    reminderAlert.setTrackRecordUID(trackRecUID);
    _docAlertMgr.createReminderAlert(reminderAlert);

    // 10 day reminder alert
    reminderAlert = new ReminderAlert();
    reminderAlert.setAlertToRaise(REMINDER);
    reminderAlert.setDaysToReminder(_DAYS_TO_REMINDER_10);
    reminderAlert.setDocRecipientXpath("/PO/VendorCode");
    reminderAlert.setDocSenderXpath("/PO/BuyerCode");
    reminderAlert.setTrackRecordUID(trackRecUID);
    _docAlertMgr.createReminderAlert(reminderAlert);
  }

  private static final String RECV_RESPONSE = "RECV_RESPONSE";
  private static final String REMINDER      = "REMINDER";

  protected void createAlerts() throws Exception
  {
    // message template x 2
    Long recRespMsgUId = createRecResponseMessageTemplate();
    Long reminderMsgUId = createReminderMessageTemplate();

    // action, alert, alert action x2
    Long recRespActionUId = createAction("RECV_CONF_ACTION", "Send Confirmation Notification", recRespMsgUId);
    Long reminderActionUId = createAction("SELLER_REMINDER_ACTION", "Send Seller Reminder email", reminderMsgUId);
    Long recRespAlertUId = createAlert(RECV_RESPONSE, "Receive Order Confirmation", new Long(2));
    Long reminderAlertUId = createAlert(REMINDER, "Seller Reminder", new Long(3));

    createAlertAction(recRespAlertUId, recRespActionUId);
    createAlertAction(reminderAlertUId, reminderActionUId);
  }

  private void deleteAlerts()
  {
    deleteAlertAction(RECV_RESPONSE);
    deleteAlertAction(REMINDER);
    deleteAlert(RECV_RESPONSE);
    deleteAlert(REMINDER);
    deleteAction("RECV_CONF_ACTION");
    deleteAction("SELLER_REMINDER_ACTION");
    deleteRecResponseMessageTemplate();
    deleteReminderMessageTemplate();
  }

  private Long createRecResponseMessageTemplate()  throws Exception
  {
    MessageTemplate msg = new MessageTemplate();

    msg.setContentType(msg.CONTENT_TYPE_TEXT);
    msg.setFromAddr("<#USER=admin#>");
    StringBuffer buff= new StringBuffer("Dear Buyer,\n");
    buff.append("\tYou have received an order confirmation for doc. no ");
    buff.append("<%UserDocument.XPATH:/POR/PONumber%> from Vendor code ");
    buff.append("<%UserDocument.XPATH:/POR/VendorCode%> on ");
    buff.append("<%GridDocument.DT_CREATE%>.\n\nGridTalk Administrator");
    msg.setMessage(buff.toString());
    msg.setMessageType(msg.MSG_TYPE_EMAIL);
    msg.setName("Receive Confirmation");
    msg.setSubject("Purchase Order Confirmation");
    msg.setToAddr("<%UserDocument.EMAIL_CODE_RECIPIENTS%>");

    return _alertMgr.createMessageTemplate(msg);
  }

  private void deleteRecResponseMessageTemplate()
  {
    try
    {
      MessageTemplate msg = _alertMgr.getMessageTemplateByName("Receive Confirmation");
      if (msg != null)
        _alertMgr.deleteMessageTemplate((Long)msg.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createReminderMessageTemplate()  throws Exception
  {
    MessageTemplate msg = new MessageTemplate();

    msg.setContentType(msg.CONTENT_TYPE_TEXT);
    msg.setFromAddr("<#USER=admin#>");
    StringBuffer buff= new StringBuffer("Dear Seller,\n");
    buff.append("\tPlease be reminded that there are over-due purchase order ");
    buff.append("waiting for your confirmation.\n\nGridTalk Administrator");
    msg.setMessage(buff.toString());
    msg.setMessageType(msg.MSG_TYPE_EMAIL);
    msg.setName("Confirmation Reminder");
    msg.setSubject("Reminder: Purchase Order Confirmation over-due");
    msg.setToAddr("<%UserDocument.EMAIL_CODE_RECIPIENTS%>");

    return _alertMgr.createMessageTemplate(msg);
  }

  private void deleteReminderMessageTemplate()
  {
    try
    {
      MessageTemplate msg = _alertMgr.getMessageTemplateByName("Confirmation Reminder");
      if (msg != null)
        _alertMgr.deleteMessageTemplate((Long)msg.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAction(String name, String desc, Long msgTemplate)  throws Exception
  {
    Action action = new Action();
    action.setName(name);
    action.setDescr(desc);
    action.setMsgUid(msgTemplate);

    return _alertMgr.createAction(action);
  }

  private void deleteAction(String name)
  {
    try
    {
      Action action = _alertMgr.getActionByActionName(name);
      if (action != null)
        _alertMgr.deleteAction((Long)action.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAlert(String alertName, String desc, Long alertType)  throws Exception
  {
    Alert alert = new Alert();
    alert.setDescr(desc);
    alert.setName(alertName);
    alert.setAlertType(alertType);

    return _alertMgr.createAlert(alert);
  }

  private void deleteAlert(String name)
  {
    try
    {
      Alert alert = _alertMgr.getAlertByAlertName(name);
      if (alert != null)
        _alertMgr.deleteAlert((Long)alert.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAlertAction(Long alertUId, Long actionUId)  throws Exception
  {
    AlertAction alertAction = new AlertAction();
    alertAction.setActionUid(actionUId);
    alertAction.setAlertUid(alertUId);

    return _alertMgr.createAlertAction(alertAction);
  }

  private void deleteAlertAction(String alertName)
  {
    try
    {
      Alert alert = _alertMgr.getAlertByAlertName(alertName);
      if (alert != null)
      {
        Collection result = _alertMgr.getAlertActionsByAlertUId((Long)alert.getKey());
        for (Iterator i=result.iterator(); i.hasNext(); )
        {
          AlertAction alertAction = (AlertAction)i.next();
          _alertMgr.deleteAction((Long)alertAction.getKey());
        }
      }
    }
    catch (Exception ex)
    {

    }
  }

}