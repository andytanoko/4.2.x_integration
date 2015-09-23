package com.gridnode.gtas.server.rnif.helpers.util;

import com.gridnode.gtas.events.document.ImportDocumentEvent;
import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;

public class ImportDocumentActionUtil
{
  protected String _SENDER_ID= IRnifTestConstants.MY_BEID;
  protected String _RECIPIENT= IRnifTestConstants.PARTNER_KEY_3A4;
  protected String _userID= IRnifTestConstants.USER_ID;
  protected String _password= "admin";

  private ArrayList _attachments= new ArrayList();
  
  private String _documentType= "UC";
  private ArrayList _importFiles= new ArrayList();

  IGridTalkClientControllerHome _controllerHome;
  IGridTalkClientControllerObj _controller;

//  ISessionManagerHome _sessionHome;
//  ISessionManagerObj _sessionRemote;
//  String _sessionID;

  BasicEventResponse _response;
  ImportDocumentEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;

  StateMachine _sm= new StateMachine(null, null);

  public ImportDocumentActionUtil()
  {
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[ImportDocumentActionUtil.setUp] Enter");

//    _sessionHome=
//      (ISessionManagerHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(
//        ISessionManagerHome.class);
    //    _sessionRemote = _sessionHome.create();
    //    _sessionID = _sessionRemote.openSession();
    //    _sessionRemote.authSession(_sessionID, _userID);

    //    _home = (IDocumentManagerHome)ServiceLookup.getInstance(
    //             ServiceLookup.CLIENT_CONTEXT).getHome(
    //             IDocumentManagerHome.class);
    //
    //    _remote = _home.create();
    //    
    _controllerHome=
      (IGridTalkClientControllerHome) ServiceLocator.instance(
        ServiceLocator.CLIENT_CONTEXT).getHome(
        IGridTalkClientControllerHome.class.getName(),
        IGridTalkClientControllerHome.class);
    _controller= _controllerHome.create();

    userSignOn();
    Log.log("TEST", "[ImportDocumentActionUtil.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    userSignOff();
    Log.log("TEST", "[ImportDocumentActionUtil.tearDown] Enter");
    Log.log("TEST", "[ImportDocumentActionUtil.tearDown] Exit");
  }

  //
  //  private BasicEventResponse performEvent(IEvent event)
  //    throws Exception
  //  {
  //    ImportDocumentAction action = new ImportDocumentAction();
  //    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
  //    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);
  //
  //    action.init(_sm);
  //    action.doStart();
  //    action.validateEvent(event);
  //    BasicEventResponse response = (BasicEventResponse) action.perform(event);
  //    action.doEnd();
  //    return response;
  //  }

  private BasicEventResponse performEvent(IEvent event) throws Exception
  {

    BasicEventResponse response= (BasicEventResponse) _controller.processEvent(event);

    //    ImportDocumentAction action = new ImportDocumentAction();
    //    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    //    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);
    //
    //    action.init(_sm);
    //    action.doStart();
    //    action.validateEvent(event);
    //    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    //     action.doEnd();
    return response;
  }

  public void importDoc() throws Exception
  {
    setUp();
    Log.log("TEST", "[ImportDocumentActionUtil.testDelete] Enter ");

    ArrayList recipientList= new ArrayList();
    recipientList.add(_RECIPIENT);

    _event=
      new ImportDocumentEvent(_SENDER_ID, _documentType, _importFiles, recipientList, _attachments);
    //    assertNotNull("event fileType UID is null", _event.getFileTypeUID());
    try
    {
      _response= performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[ImportDocumentActionUtil.testDelete] Error Exit ", ex);
      throw ex;
    }

    //check response
    Log.log("TEST", "response is " + _response);
    Log.log("TEST", "[ImportDocumentActionUtil.testDelete] Exit ");
    tearDown();
  }

  public void setAttachments(ArrayList attachments)
  {
    _attachments= attachments;
  }

  public ArrayList getAttachments()
  {
    return _attachments;
  }

  public void setDocumentType(String documentType)
  {
    _documentType= documentType;
  }

  public String getDocumentType()
  {
    return _documentType;
  }

  public void setImportFiles(ArrayList importFiles)
  {
    _importFiles= importFiles;
  }

  public ArrayList getImportFiles()
  {
    return _importFiles;
  }

  private void userSignOn() throws Exception
  {
    UserLoginEvent event= new UserLoginEvent(_userID, _password);
    BasicEventResponse response= (BasicEventResponse) _controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      Log.log("TEST", "User has successfully signed on");
    }
    else
    {
      Log.log("TEST", "Login not successful!");
      Log.log("TEST", "Due to: " + response.getErrorReason());
      throw new Exception("ERROR Login ");
      }
  }

  private void userSignOff() throws Exception
  {
    UserLogoutEvent event= new UserLogoutEvent();
    BasicEventResponse response= (BasicEventResponse) _controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      Log.log("TEST", "User has successfully signed off");
    }
    else
    {
      Log.log("TEST", "Logout not successful!");
      Log.log("TEST", "Due to: " + response.getErrorReason());
      throw new Exception();
    }
  }

}