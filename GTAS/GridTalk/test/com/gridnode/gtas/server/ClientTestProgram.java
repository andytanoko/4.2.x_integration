/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ClientTestProgram.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.ejb.*;
import com.gridnode.gtas.events.user.*;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.app.user.model.*;
import com.gridnode.pdip.app.user.facade.ejb.*;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import junit.framework.*;
import java.util.*;
import java.sql.Timestamp;
import java.util.BitSet;

/**
 * This is a test program that acts as a client to GTAS.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ClientTestProgram
{
  static final int OPT_SIGNON      = 0;
  static final int OPT_CREATE_USER = 1;
  static final int OPT_UPDATE_USER = 2;
  static final int OPT_DELETE_USER = 3;
  static final int OPT_VIEW_USER   = 4;
  static final int OPT_SIGNOFF     = 5;
  static final int OPT_QUIT        = 6;

  static final String ADMIN_ID = "admin";
  static final String ADMIN_NAME = "Administrator";
  static final String ADMIN_PASSWORD = "admin";
  static final String ADMIN_EMAIL = "admin@gridnode.com";
  static final String ADMIN_PHONE = "12345678";
  static final String ADMIN_PROPERTY = "This account belongs to admin user";

  static final String SESSION_ID = "abcd";

  //note: these are dependent on options set in login config
  static final String APPLICATION = "gridtalk";
  static final int    MAX_LOGIN_TRIES = 3;
  static final int    FREEZE_TIMEOUT  = 10 * 60 * 1000; //10 minutes

  BasicEventResponse response;
  UserLoginEvent event;
  UserAccount acct;
  UserAccountState acctState;

  IGridTalkClientControllerHome _controllerHome;
  IGridTalkClientControllerObj _controller;
  IUserManagerObj _mgr;
  IACLManagerObj _aclMgr;
  BitSet _optionState = new BitSet(6);
  boolean _removeAdminUser = false;
  String _currentUser;
  Map _currentUserEntity;
  Map _availUsers = new HashMap();

  public ClientTestProgram()
  {

  }

  public static void main(String args[]) throws Exception
  {
    ClientTestProgram prog = new ClientTestProgram();
    try
    {
      prog.setUp();
      if (args.length > 0)
        prog.analyseArguments(args);
      prog.displayMenu();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ClientTestProgram.main] Error ", ex);
    }
    finally
    {
      prog.tearDown();
    }
  }

  private void analyseArguments(String args[])
  {
    for (int i=0; i<args.length; i++)
    {
      if ("-createAdminUser".equalsIgnoreCase(args[i]))
        createAdminUser();
      else if ("-removeAdminOnExit".equalsIgnoreCase(args[i]))
        _removeAdminUser = true;
    }
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.debug("TEST", "[ClientTestProgram.setUp] Enter");

    Log.debug("TEST", "[ClientTestProgram.setUp] Obtaining UserManagerBean");
    _mgr = (IUserManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IUserManagerHome.class.getName(),
             IUserManagerHome.class,
             new Object[0]);

    _aclMgr = (IACLManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IACLManagerHome.class.getName(),
             IACLManagerHome.class,
             new Object[0]);

    Log.debug("TEST", "[ClientTestProgram.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.debug("TEST", "[ClientTestProgram.tearDown] Enter");

    cleanUp();

    if (_controller != null)
      _controller.remove();


    Log.debug("TEST", "[ClientTestProgram.tearDown] Exit");
  }

  private void displayMenu() throws Exception
  {
    boolean isQuit=false;
    initOptionStates();
    do
    {
      System.out.println("User Test Program Options:");
      System.out.println(getDisplayOpt(OPT_SIGNON)+"User Login"+ getEnabled(OPT_SIGNON));
      System.out.println(getDisplayOpt(OPT_CREATE_USER)+"Create User Account" + getEnabled(OPT_CREATE_USER));
      System.out.println(getDisplayOpt(OPT_UPDATE_USER)+"Update User Account"+ getEnabled(OPT_UPDATE_USER));
      System.out.println(getDisplayOpt(OPT_DELETE_USER)+"Delete User Account"+ getEnabled(OPT_DELETE_USER));
      System.out.println(getDisplayOpt(OPT_VIEW_USER)+"View User Account"+ getEnabled(OPT_VIEW_USER));
      System.out.println(getDisplayOpt(OPT_SIGNOFF)+"User Logoff"+ getEnabled(OPT_SIGNOFF));
      System.out.println(getDisplayOpt(OPT_QUIT)+"Quit"+ getEnabled(OPT_QUIT));

      char option = UIManager.getCharacter("Enter Option");
      option = Character.toUpperCase(option);

      int intOpt = option-'A';
      if (!_optionState.get(intOpt))
        System.out.println("Option is not enabled!");
      else
      {
        try
        {
          switch (intOpt)
          {
            case OPT_CREATE_USER:
                 createUserAccount();
                 break;
            case OPT_UPDATE_USER:
                 updateUserAccount();
                 break;
            case OPT_DELETE_USER:
                 deleteUserAccount();
                 break;
            case OPT_VIEW_USER:
                 viewUserAccount();
                 break;
            case OPT_SIGNON:
                 userSignOn();
                 break;
            case OPT_SIGNOFF:
                 userSignOff();
                 break;
            case OPT_QUIT:
                 System.out.println("Thank you and bye bye!");
                 isQuit = true;
                 break;
            default : System.out.println("Invalid Option!");
          }
        }
        catch (Exception ex)
        {
          Log.err("TEST", "[ClientTestProgram.displayMenu] Caught error ", ex);
        }
      }
    }
    while (!isQuit);
  }

  private void initOptionStates()
  {
    _optionState.clear(OPT_CREATE_USER);
    _optionState.clear(OPT_UPDATE_USER);
    _optionState.clear(OPT_DELETE_USER);
    _optionState.clear(OPT_VIEW_USER);
    _optionState.clear(OPT_SIGNOFF);
    _optionState.set(OPT_SIGNON);
    _optionState.set(OPT_QUIT);
  }

  private void signonState(String signonUser)
  {
    _currentUser = signonUser;
//    if (ADMIN_ID.equals(_currentUser))
//    {
      _optionState.set(OPT_CREATE_USER);
      _optionState.set(OPT_DELETE_USER);
//    }

    _optionState.set(OPT_UPDATE_USER);
    _optionState.set(OPT_VIEW_USER);
    _optionState.set(OPT_SIGNOFF);
    _optionState.clear(OPT_SIGNON);
  }

  private String getDisplayOpt(int option)
  {
    return new String("\t["+(char)(option + 'A')+"]");
  }

  private String getEnabled(int option)
  {
    return _optionState.get(option)? " (Enabled)" : " (Disabled)";
  }

  // ************************* Test cases *******************************

  private void userSignOn() throws Exception
  {
    String userID = UIManager.getString("User ID");
    String password = UIManager.getString("Password");

    Log.debug("TEST", "[ClientTestProgram.setUp] Obtaining GridTalkClientControllerBean");
    _controllerHome = (IGridTalkClientControllerHome)ServiceLocator.instance(
                        ServiceLocator.CLIENT_CONTEXT).getHome(
                        IGridTalkClientControllerHome.class.getName(),
                        IGridTalkClientControllerHome.class);
    _controller = _controllerHome.create();

    UserLoginEvent event = new UserLoginEvent(userID, password);
    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("User has successfully signed on");
      signonState(userID);
      _currentUserEntity = getUserAccount(userID);
    }
    else
    {
      System.out.println("Login not successful!");
      System.out.println("Due to: "+response.getErrorReason());
    }
  }

  private void userSignOff() throws Exception
  {
    UserLogoutEvent event = new UserLogoutEvent();
    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("User has successfully signed off");
      initOptionStates();
      _currentUser = null;
      _currentUserEntity = null;
    }
    else
    {
      System.out.println("Logout not successful!");
      System.out.println("Due to: "+response.getErrorReason());
    }
  }

  private void createUserAccount() throws Exception
  {
    String userID = UIManager.getString("User ID");
    String userName = UIManager.getString("User Name");
    String phone = UIManager.getOptionalString("Phone", "[optional]");
    String email = UIManager.getOptionalString("Email", "[optional]");
    String property = UIManager.getOptionalString("Property", "[optional]");
    String password = UIManager.getString("Initial Password");
    char enable = UIManager.getCharacter("Enable Account? [Y/N]");
    boolean enableAccount = (Character.toUpperCase(enable) == 'Y');

    CreateUserAccountEvent event = new CreateUserAccountEvent(
                                       userID, userName, password, phone,
                                       email, property, enableAccount);

    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("User Account has been successfully created.");
    }
    else
    {
      System.out.println("User Account cannot be created!");
      System.out.println("Due to: "+response.getErrorReason());
    }
  }

  private void updateUserAccount() throws Exception
  {
    UpdateUserAccountEvent event = null;

//    if (isAdminUser())
//    {
      // the accounts availabled for update should exclude DELETED user
      // accounts
      System.out.println(_availUsers.keySet());
      String toUpdate = UIManager.getOptionalString("Choose one user to update", "[enter for none]");

      if (toUpdate != null)
      {
        if (_availUsers.containsKey(toUpdate))
        {
          Map user = getUserAccount(toUpdate);
          Map acctState = (Map)user.get(IUserAccount.ACCOUNT_STATE);

          displayUser(user, true);

          System.out.println("Change Account Details:");
          String userName = UIManager.getOptionalString("User Name", "[optional]");
          String phone = UIManager.getOptionalString("Phone", "[optional]");
          String email = UIManager.getOptionalString("Email", "[optional]");
          String property = UIManager.getOptionalString("Property", "[optional]");
          boolean unfreeze = false;
          boolean resetTries = false;
          short state;
          String resetPwd = null;

          char yes;
          if (Boolean.TRUE.equals(acctState.get(IUserAccountState.IS_FREEZE)))
          {
            yes = UIManager.getCharacter("Unfreeze account? [Y/N]");
            unfreeze = (Character.toUpperCase(yes) == 'Y');
          }
          yes = UIManager.getCharacter("Reset login tries? [Y/N]");
          resetTries = (Character.toUpperCase(yes) == 'Y');

          state = ((Short)acctState.get(IUserAccountState.STATE)).shortValue();

          // the permission to disable/delete the account
          // should be checked by BL
          yes = UIManager.getCharacter("State [1. Enabled 2. Disabled]");
          switch (yes) {
            case '1' : state = IUserAccountState.STATE_ENABLED; break;
            case '2' : state = IUserAccountState.STATE_DISABLED; break;
          }

          resetPwd = UIManager.getOptionalString("Reset password to", "[optional]");

          event = new UpdateUserAccountEvent(
                      (Long)user.get(IUserAccount.UID),
                      changeIfNotEmpty((String)user.get(IUserAccount.NAME), userName),
                      changeIfNotEmpty((String)user.get(IUserAccount.PHONE),phone),
                      changeIfNotEmpty((String)user.get(IUserAccount.EMAIL),email),
                      changeIfNotEmpty((String)user.get(IUserAccount.PROPERTY),property),
                      unfreeze, resetTries, state,
                      changeIfNotEmpty(null, resetPwd));
        }
        else
          System.out.println("No such user!");
      }
//    }
//    else
//    {
//      _currentUserEntity = getUserAccount(_currentUser);
//      displayUser(_currentUserEntity, false);
//
//      System.out.println("Change Account Details:");
//      String userName = UIManager.getOptionalString("User Name", "[optional]");
//      String phone = UIManager.getOptionalString("Phone", "[optional]");
//      String email = UIManager.getOptionalString("Email", "[optional]");
//      String property = UIManager.getOptionalString("Property", "[optional]");
//
//      event = new UpdateUserAccountEvent(
//          (Long)_currentUserEntity.get(IUserAccount.UID),
//          changeIfNotEmpty((String)_currentUserEntity.get(IUserAccount.NAME), userName),
//          changeIfNotEmpty((String)_currentUserEntity.get(IUserAccount.PHONE),phone),
//          changeIfNotEmpty((String)_currentUserEntity.get(IUserAccount.EMAIL),email),
//          changeIfNotEmpty((String)_currentUserEntity.get(IUserAccount.PROPERTY),property));
//    }

    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("User Account has been successfully updated.");
    }
    else
    {
      System.out.println("User Account cannot be updated!");
      System.out.println("Due to: "+response.getErrorReason());
    }

  }

  private String changeIfNotEmpty(String oriStr, String newStr) {
    return (isEmpty(newStr)? oriStr : newStr);
  }

  private boolean isEmpty(String str)
  {
    return (str == null || str.length() == 0);
  }

  private void deleteUserAccount() throws Exception
  {
    System.out.println(_availUsers.keySet());
    String toDelete = UIManager.getOptionalString("Choose one user to delete", "[enter for none]");
    if (toDelete != null)
    {
      if (_availUsers.containsKey(toDelete))
      {
        //this check should be in the server
        if (_currentUser.equals(toDelete))
        {
          System.out.println("Cannot delete your own account!");
          return;
        }

//        DeleteUserAccountEvent event = new DeleteUserAccountEvent(toDelete);
//
//        response = (BasicEventResponse)_controller.processEvent(event);
//
//        if (response.isEventSuccessful())
//        {
//          System.out.println("User account is marked deleted.");
//          displayUser(getUserAccount(toDelete), true);
//        }
//        else
//        {
//          System.out.println("User Account cannot be deleted!");
//          System.out.println("Due to: "+response.getErrorReason());
//        }
      }
      else
       System.out.println("No such user!");
    }
  }

  private void viewUserAccount() throws Exception
  {
//    if (isAdminUser())
//    {
      adminViewAccount();
//    }
//    else
//    {
//      normalViewAccount();
//    }
  }

  // ******************* Utility methods *******************************

  private void adminViewAccount() throws Exception
  {
    GetUserAccountListEvent event = new GetUserAccountListEvent(null);
    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("Existing user accounts:");
      displayUserList((EntityListResponseData)response.getReturnData());
    }
    else
    {
      System.out.println("User Accounts cannot be retrieved!");
      System.out.println("Due to: "+response.getErrorReason());
    }
  }

  private void normalViewAccount() throws Exception
  {
    GetUserAccountEvent event = new GetUserAccountEvent((Long)_currentUserEntity.get(
                                    IUserAccount.UID));

    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      System.out.println("Your accounts information:");
      displayUser((Map)response.getReturnData(), false);
    }
    else
    {
      System.out.println("User Account cannot be retrieved!");
      System.out.println("Due to: "+response.getErrorReason());
    }

  }

  private void displayUserList(EntityListResponseData data) throws Exception
  {
    _availUsers.clear();
    Collection list = data.getEntityList();
    for (Iterator i=list.iterator(); i.hasNext(); )
    {
      Map entity = (Map)i.next();
      displayUser(entity, true);
      _availUsers.put(entity.get(IUserAccount.ID), entity.get(IUserAccount.UID));
      System.out.println();
      System.out.println("Next account.. [press Enter key]");
      System.in.read();
    }
  }

  private void displayUser(Map user, boolean displayState)
  {
    System.out.println("-- User Profile --");
    System.out.println("User ID:"+user.get(IUserAccount.ID));
    System.out.println("User Name:"+user.get(IUserAccount.NAME));
    System.out.println("Phone:"+user.get(IUserAccount.PHONE));
    System.out.println("Email:"+user.get(IUserAccount.EMAIL));
    System.out.println("Property:"+user.get(IUserAccount.PROPERTY));

    Map acctState = (Map)user.get(IUserAccount.ACCOUNT_STATE);

    System.out.println("-- Account State --");
    System.out.println("Last Login:"+acctState.get(IUserAccountState.LAST_LOGIN_TIME));
    System.out.println("Last Logout:"+acctState.get(IUserAccountState.LAST_LOGOUT_TIME));

    if (displayState)
    {
      System.out.println("Create Time:"+acctState.get(IUserAccountState.CREATE_TIME));
      System.out.println("Create By:"+acctState.get(IUserAccountState.CREATE_BY));
      System.out.println("State:"+acctState.get(IUserAccountState.STATE));
      System.out.println("Is Freezed:"+acctState.get(IUserAccountState.IS_FREEZE));
      System.out.println("Freeze Time:"+acctState.get(IUserAccountState.FREEZE_TIME));
      System.out.println("Invalid Login tries:"+acctState.get(IUserAccountState.NUM_LOGIN_TRIES));
      System.out.println("Can Delete:"+acctState.get(IUserAccountState.CAN_DELETE));
    }
  }

  private Map getUserAccount(String userId) throws Exception
  {
    GetUserAccountEvent event = new GetUserAccountEvent(userId);
    response = (BasicEventResponse)_controller.processEvent(event);

    if (response.isEventSuccessful())
    {
      return (Map)response.getReturnData();
    }
    else
    {
      System.out.println("User Account cannot be retrieved!");
      System.out.println("Due to: "+response.getErrorReason());
    }
    return null;
  }

  private boolean isAdminUser()
  {
    return ADMIN_ID.equals(_currentUser);
  }

  private void createAdminUser()
  {
    Log.debug("TEST", "[ClientTestProgram.createAdminUser] Enter");

    acct = new UserAccount();
    acct.setId(ADMIN_ID);
    acct.setPassword(new PasswordMask(ADMIN_PASSWORD));
    acct.setEmail(ADMIN_EMAIL);
    acct.setPhone(ADMIN_PHONE);
    acct.setProperty(ADMIN_PROPERTY);
    acct.setUserName(ADMIN_NAME);

    try
    {
      Long uID = _mgr.createUserAccount(acct);
      Role role = _aclMgr.getRoleByRoleName("User");
      if (role != null)
        _aclMgr.assignRoleToSubject(
          new Long(role.getUId()),
          uID,
          UserAccount.ENTITY_NAME);
      System.out.println("Admin user account has been created.");
      System.out.println("UserID: "+ADMIN_ID + " Password: "+ADMIN_PASSWORD);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "ClientTestProgram.createAdminUser]", ex);
      System.out.println("Unable to create admin user: "+ex.getMessage());
    }
    Log.debug("TEST", "[ClientTestProgram.createTestData] Exit");
  }

  private void cleanUp() throws Exception
  {
    try
    {
      if (_currentUser != null)
      {
        //not yet signoff
        userSignOff();
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ClientTestProgram.cleanUp] Error ", ex);
    }

    try
    {
      if (_removeAdminUser)
      {
        deleteAdminUser();
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ClientTestProgram.cleanUp] Error ", ex);
    }
  }

  private void deleteTestSession(String session)
  {
    try
    {
      _mgr.logout(session);
    }
    catch (Exception ex)
    {
    }
  }

  private void deleteAdminUser()
  {
    Log.debug("TEST", "[ClientTestProgram.deleteAdminUser] Enter");

    //find existing acct
    try
    {
      UserAccount acct = _mgr.findUserAccount(ADMIN_ID);
      if (acct != null)
      {
        _mgr.deleteUserAccount((Long)acct.getKey(), false);
        System.out.println("Admin user has been deleted.");
      }
    }
    catch (Exception ex)
    {

    }
    Log.debug("TEST", "[ClientTestProgram.deleteAdminUser] Exit");
  }

  private UserAccount findUserAccountByUserId(String userId)
  {
    Log.log("TEST", "[ClientTestProgram.findUserAccountByUserId] Enter");

    UserAccount acct = null;
    try
    {
      acct = _mgr.findUserAccount(userId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ClientTestProgram.findUserAccountByUserId]", ex);
    }

    Log.log("TEST", "[ClientTestProgram.findUserAccountByUserId] Exit");
    return acct;
  }

  private String maskPassword(String password)
  {
    PasswordMask mask = new PasswordMask(password, acct.getPasswordMaskLength());
    return mask.toString();
  }

}