/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RecipientResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2003    Neo Sok Lay         Created
 * May 13 2003    Neo Sok Lay         Add getAddressesForAddressXpath().
 * Mar 03 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.providers;

import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.ServiceLookupHelper;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * A RecipientResolver is capable of resolving addresses from:<p>
 * <UL>
 *   <LI>user id</LI>
 *   <LI>role name</LI>
 *   <LI>email code</LI>
 *   <LI>xpath for email code, and a xpath lookup file</LI>
 * </UL>
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class RecipientResolver
{
  private static final Object _lock = new Object();
  private static RecipientResolver _self = null;

  private EmailCodeRef _emailCodeRef = null;

  public static RecipientResolver getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
        {
          _self = new RecipientResolver();
        }
      }
    }
    return _self;
  }

  private RecipientResolver()
  {
    _emailCodeRef = EmailCodeRef.load();
  }

  /**
   * Resolve email addresses using a specified email code.
   *
   * @param code The email code.
   * @return A concatenated string of email addresses resolved from the code.
   */
  public String resolveAddressForCode(String code)
  {
    Set<String> emails = getAddressesForCode(code);

    return concatEmails(emails);
  }

  /**
   * Resolve email addresses using a specified role name.
   *
   * @param role The role name.
   * @return A concatenated string of email addresses resolved from the role.
   */
  public String resolveAddressForRole(String role)
  {
    Set<String> email = getAddressesForRole(role);
    return concatEmails(email);
  }

  /**
   * Resolve email addresses using a specified user id.
   *
   * @param userID The user id.
   * @return The email address of the user account.
   */
  public String resolveAddressForUser(String userID)
  {
    String email = null;
    if (!isEmptyStr(userID))
    {
      try
      {
        email = getUserEmail(userID);
      }
      catch (Throwable t)
      {
        AlertLogger.warnLog("RecipientResolver", "resolveAddressForUser", t.getMessage());
      }
    }
    if (email == null)
      email = "";

    return email;
  }

  /**
   * Resolve email addresses using a specified email code xpath.
   *
   * @param codeXpath The email code xpath.
   * @param xpathLookupFile The filename of the file to use to extract the email
   * code using <code>codeXpath</code>.
   * @return A concatenated string of email addresses resolved from the codeXpath.
   */
  public String resolveAddressForCodeXpath(
    String codeXpath, String xpathLookupFile)
  {
    Set<String> emails = getAddressesForCodeXpath(codeXpath, xpathLookupFile);

    return concatEmails(emails);
  }

  /**
   * Resolve email addresses using a specified email code.
   *
   * @param code The email code.
   * @return A set of email addresses resolved from the code.
   */
  public Set<String> getAddressesForCode(String code)
  {
    Set<String> emails = new HashSet<String>();
    EmailCode emailCode = _emailCodeRef.getEmailCode(code);
    if (emailCode != null)
    {
      emails.addAll(getAddressesForUsers(emailCode.getUserRef()));
      emails.addAll(getAddressesForRoles(emailCode.getRoleRef()));
      emails.addAll(emailCode.getEmailRef());
    }

    return emails;
  }

  /**
   * Resolve email addresses using a specified set of user ids.
   *
   * @param userRef The set of user ids.
   * @return The set of email addresses of the user accounts.
   */
  public Set<String> getAddressesForUsers(Set<String> userRef)
  {
    Set<String> list = new HashSet<String>();
    for (String user : userRef)
    {
      String email = resolveAddressForUser(user);
      list.add(email);
    }
    return list;
  }

  /**
   * Resolve email addresses using a specified set of role names.
   *
   * @param roleRef The set of role names.
   * @return A set of email addresses resolved from the roles.
   */
  public Set<String> getAddressesForRoles(Set roleRef)
  {
    Set<String> list = new HashSet<String>();
    for (Iterator i=roleRef.iterator(); i.hasNext(); )
    {
      Set<String> emails = getAddressesForRole((String)i.next());
      list.addAll(emails);
    }
    return list;
  }

  /**
   * Resolve email addresses using a specified email code xpath.
   *
   * @param codeXpath The email code xpath.
   * @param xpathLookupFile The filename of the file to use to extract the email
   * code using <code>codeXpath</code>.
   * @return A set of email addresses resolved from the codeXpath.
   */
  public Set<String> getAddressesForCodeXpath(
    String codeXpath, String xpathLookupFile)
  {
    Set<String> codes = new HashSet<String>(extractFromXpath(codeXpath, xpathLookupFile));
    Set<String> emails = new HashSet<String>();

    for (String code : codes)
    {
      emails.addAll(getAddressesForCode(code));
    }
    return emails;
  }

  /**
   * Resolve email addresses using a specified email address xpath.
   *
   * @param addressXpath The email address xpath.
   * @param xpathLookupFile The filename of the file to use to extract the
   * email addresses using <code>addressXpath</code>
   * @return A set of email addresses resolved from the addressXpath.
   */
  public Set<String> getAddressesForAddressXpath(
    String addressXpath, String xpathLookupFile)
  {
    return new HashSet<String>(extractFromXpath(addressXpath, xpathLookupFile));
  }

  /**
   * Resolve email addresses using a specified role name.
   *
   * @param role The role name.
   * @return A set of email addresses resolved from the role.
   */
  public Set<String> getAddressesForRole(String role)
  {
    Set<String> addresses = new HashSet<String>();
    if (!isEmptyStr(role))
      addresses = getUserEmailsFromRole(role);

    return addresses;
  }

  /**
   * Get the email address for the specified user.
   *
   * @param userID The user ID of the user.
   *
   * @return String EMail address of the User.
   */
  private String getUserEmail(String userID) throws Throwable
  {
    return ServiceLookupHelper.getUserManager().findUserAccount(userID).getEmail();
  }

  /**
   * Retrieve the emails of all users that assume a particular role.
   *
   * @param roleName The name of the Role
   * @return Collection of Email Ids (addresses). Empty addresses are excluded.
   *
   * @since 2.1
   */
  public Set<String> getUserEmailsFromRole(String roleName)
  {
    Set<String> emails = new HashSet<String>();
    try
    {
      IACLManagerObj mgr = ServiceLookupHelper.getAclManager();
      Role role = mgr.getRoleByRoleName(roleName);
      if (role != null)
      {
        Collection userUIds = mgr.getSubjectUIdsForRole(
                             (Long)role.getKey(), UserAccount.ENTITY_NAME);
        if (!userUIds.isEmpty())
        {
          DataFilterImpl filter = new DataFilterImpl();
          filter.addDomainFilter(null, UserAccount.UID, userUIds, false);
          filter.addSingleFilter(filter.getAndConnector(), UserAccount.EMAIL,
            filter.getNotEqualOperator(), null, false);
          filter.addSingleFilter(filter.getAndConnector(), UserAccount.EMAIL,
            filter.getNotEqualOperator(), "", false);

          BitSet exclState = new BitSet();
          exclState.set(UserAccountState.STATE_DELETED);
          Collection<UserAccount> users = ServiceLookupHelper.getUserManager().findUserAccounts(
                               filter, exclState);
          for (UserAccount ua : users)
          {
            emails.add(ua.getEmail());
          }
        }
      }
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("RecipientResolver", "getUserEmailsFromRole",
        t.getMessage());
    }

    return emails;
  }

  /**
   * Concatentate the collection of email addresses into a single string.
   *
   * @param emails The Collection of email addresses (String)
   * @return The concatenated string. Each email is delimited by comma.
   */
  public String concatEmails(Collection<String> emails)
  {
    StringBuffer buff = new StringBuffer();
    for (String email : emails)
    {
      if (!isEmptyStr(email))
      {
        if (buff.length() != 0)
          buff.append(",");
        buff.append(email);
      }
    }
    return buff.toString();
  }

  /**
   * Extract values from a xml file using a xpath.
   *
   * @param xpath The Xpath to use for extraction.
   * @param fileName The filename of the xml file.
   * @return List of values (String) extracted.
   */
  private List<String> extractFromXpath(String xpath, String fileName)
  {
    List<String> vals = null;
    if (!isEmptyStr(xpath))
    {
      // extract
      try
      {
        //vals = ServiceLookupHelper.getXmlManager().getXPathValues(fileName, xpath);
        com.gridnode.pdip.base.xml.helpers.XMLServiceHandler handler =
          com.gridnode.pdip.base.xml.helpers.XMLServiceHandler.getInstance();
        vals = handler.getXPathValues(fileName, xpath);
      }
      catch (Exception ex)
      {
        AlertLogger.warnLog("RecipientResolver", "extractFromXpath",
          ex.getMessage());
      }
    }

    if (vals == null)
      vals = new ArrayList<String>();

    return vals;
  }

  public boolean isEmptyStr(String val)
  {
    return (val == null) || (val.length() == 0);
  }

}