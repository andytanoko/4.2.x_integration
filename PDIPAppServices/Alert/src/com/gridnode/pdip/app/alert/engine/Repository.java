/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Repository.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 2 Dec 2002    Srinath	      Creation
 * Jan 23 2003    Neo Sok Lay         Do not return Datasource list here. This
 *                                    should be application dependent.
 *                                    Possibly application should extend from
 *                                    Repository to provide the datasource list.
 * Apr 17 2003    Neo Sok Lay         Add a RecipientListData data provider
 *                                    to the provider list for formatting, if
 *                                    one does not already exists.
 * May 12 2003    Neo Sok Lay         Add a SystemDetails data provider
 *                                    to the provider list for formatting, if
 *                                    one does not already exists.
 * Jun 23 2003    Neo Sok Lay         Allow formatting for a list of strings
 *                                    using the same provider list.
 *                                    Moved from helpers package.
 * Nov 10 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup     
 * Mar 03 2006    Neo Sok Lay         Use generics                              
 */

package com.gridnode.pdip.app.alert.engine;

import java.util.*;

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;
import com.gridnode.pdip.app.alert.providers.RecipientListData;
import com.gridnode.pdip.app.alert.providers.SystemDetails;
import com.gridnode.pdip.app.user.helpers.UserEntityHandler;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.base.acl.helpers.RoleEntityHandler;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class is a generalised class. Any general methods will henceforth be in this class only
 *
 * @author Srinath
 */

public class Repository
{
  /**
   * This method is used for formatting the message.
   *
   * @param msg - The message that needs to be formatted
   * @param providerList The ProviderList containing the Data Providers.
   *
   * @return The formatted String
   *
   */
  public String format(String msg, IProviderList providerList) throws Throwable
  {
    MsgParser parser = new MsgParser(msg);
    parser.parseIt();
    Vector<String> fields = parser.getFields();

    if (providerList.getProvider(RecipientListData.NAME) == null)
      providerList.addProvider(new RecipientListData());
    if (providerList.getProvider(SystemDetails.NAME) == null)
      providerList.addProvider(new SystemDetails());

    HashMap<String,String> definition = ProviderMgr.getInstance().getValue(fields, providerList);
    return parser.format(definition);
  }

  /**
   * Formats an array of strings using the same provider list.
   * 
   * @param toFormat The array of strings to format.
   * @param providerList The provider list to use.
   * @return Array of formatted strings in the same order as toFormat.
   * 
   * @since GT 2.1 I1
   */
  public String[] format(String[] toFormat, IProviderList providerList) throws Throwable
  {
    if (providerList.getProvider(RecipientListData.NAME) == null)
      providerList.addProvider(new RecipientListData());
    if (providerList.getProvider(SystemDetails.NAME) == null)
      providerList.addProvider(new SystemDetails());

    String[] formatted = new String[toFormat.length];
    MsgParser parser;
    HashMap<String,String> definition;
    for (int i=0; i<formatted.length; i++)
    {
      parser = new MsgParser(toFormat[i]);
      parser.parseIt();
      definition = ProviderMgr.getInstance().getValue(parser.getFields(), providerList); 
      formatted[i] = parser.format(definition);
    }
    
    return formatted;
  }  

  protected IAlertManagerObj lookUp() throws Exception
  {
    IAlertManagerObj remote;

    remote = (IAlertManagerObj)ServiceLocator.instance(
            ServiceLocator.CLIENT_CONTEXT).getObj(
             IAlertManagerHome.class.getName(),
             IAlertManagerHome.class,
             new Object[0]);
    return remote;
  }

/*
  public ArrayList getDatasource() throws Exception
  {
    ArrayList datasourceList  = new ArrayList();

    EntityMetaInfo[] entityInfo = MetaInfoFactory.getInstance().getAllMetaInfo();
    int count = entityInfo.length;

    for (int i = 0; i < count; i++)
    {
      datasourceList.add(entityInfo[i].getEntityName());
    }
    return datasourceList;
  }
*/
  public ArrayList<ArrayList> getFieldList(String datasource) throws Exception
  {
    ArrayList<ArrayList> fieldList = new ArrayList<ArrayList>();
    ArrayList<String> innerList;
    if(datasource != null && !"".equals(datasource))
    {
      EntityMetaInfo metaInfo = MetaInfoFactory.getInstance().getMetaInfoFor(datasource);
      FieldMetaInfo[] infoList = metaInfo.getFieldMetaInfo();
      //String[] fieldNames = new String[infoList.length];

      int count1 = infoList.length;
      for (int j = 0; j < count1; j++)
      {
        innerList = new ArrayList<String>();
        innerList.add(infoList[j].getValueClass());
        innerList.add(infoList[j].getSqlName()); //why??
        fieldList.add(innerList);
      }
    }
    return fieldList;
  }

  public ArrayList<String> getActionTypeList() throws Exception
  {
    ArrayList<String> actionList = new ArrayList<String>();
    actionList.add(MessageTemplate.MSG_TYPE_EMAIL);
    actionList.add(MessageTemplate.MSG_TYPE_LOG);
    actionList.add(MessageTemplate.MSG_TYPE_ALERT_LIST);
    return actionList;
  }

  public ArrayList<String> getContentTypeList() throws Exception
  {
    ArrayList<String> contentList = new ArrayList<String>();
    contentList.add(MessageTemplate.CONTENT_TYPE_TEXT);
    contentList.add(MessageTemplate.CONTENT_TYPE_HTML);
    return contentList;
  }

  public ArrayList<String> getRoleList() throws Exception
  {
    Collection<Role> roleColl = RoleEntityHandler.getInstance().getEntityByFilterForReadOnly(null);
    ArrayList<String> innerList = new ArrayList<String>();
    for (Role role : roleColl)
    {
      innerList.add(role.getRole());
    }
    return innerList;
  }

  public ArrayList<String> getUserList() throws Exception
  {
    Collection<UserAccount> userColl = UserEntityHandler.getInstance().getEntityByFilterForReadOnly(null);
    ArrayList<String> innerList = new ArrayList<String>();
    for (UserAccount userAcct : userColl)
    {
      innerList.add(userAcct.getEmail());
    }
    return innerList;
  }

/*
  protected IACLManagerObj roleLookUp() throws Exception
  {
    IACLManagerHome home;
    IACLManagerObj remote;

    home = (IACLManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IACLManagerHome.class);
    remote = home.create();
    return remote;
  }
*/
/*
  public static void main(String[] args) throws Throwable
  {
    StringBuffer msg = new StringBuffer();
    msg.append("An email address using RecipientList: <%RecipientList.EMAIL_ADDRESS:emaila@one.com%>\n");
    msg.append("An user using RecipientList: <%RecipientList.USER:admin%>\n");
    msg.append("An user using old method: <#USER=admin#>\n");
    msg.append("A role using RecipientList: <%RecipientList.ROLE:User%>\n");
    msg.append("A role using old method: <#ROLE=User#>\n");
    msg.append("A code using RecipientList: <%RecipientList.EMAIL_CODE:code2%>\n");
    msg.append("A codexpath using RecipientList: <%RecipientList.EMAIL_CODE_XPATH:EmailCodeRef/EmailCode/@id[1]%>\n");
    msg.append("A addressxpath using RecipientList: <%RecipientList.EMAIL_ADDRESS_XPATH:EmailCodeRef/EmailCode/EmailRef[2]%>\n");
    msg.append("Dynamic Recipients using RecipientList: <%RecipientList.DYNAMIC_RECIPIENTS%>\n");

    Repository repo = new Repository();
    System.out.println("------ RecipientList not specified -----");
    System.out.println(repo.format(msg.toString(), new DefaultProviderList()));

    ArrayList dyn = new ArrayList();
    dyn.add("EMAIL_ADDRESS:emaila@one.com");
    dyn.add("USER:admin");
    dyn.add("ROLE:User");
    dyn.add("EMAIL_CODE:code1");
    dyn.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id[2]");
    dyn.add("EMAIL_ADDRESS_XPATH:/EmailCodeRef/EmailCode[3]/EmailRef");
    RecipientListData provider = new RecipientListData(
     dyn,
    "file://D:\\J2EE\\jboss-3.0.5_tomcat-4.0.6\\bin\\gtas\\data\\alert\\email\\emailcode-ref.xml");

    DefaultProviderList providerList = new DefaultProviderList(provider);
    System.out.println("------ RecipientList specified -----");
    System.out.println(repo.format(msg.toString(), providerList));

  }
*/
}