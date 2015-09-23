/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RecipientListData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 Apr 2003    Neo Sok Lay         Created
 * 22 Apr 2003    Neo Sok Lay         Add isValidFieldFormat() for valid
 *                                    field string.
 * 13 May 2003    Neo Sok Lay         Add EMAIL_ADDRESS_XPATH field.
 * 03 Mar 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.providers;

import java.util.*;

/**
 * This Data provider provides the alert recipient list.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class RecipientListData extends AbstractDetails
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8544044719942681499L;

	public static final String NAME = "RecipientList";

  public static final String FIELD_EMAIL_ADDRESS      = "EMAIL_ADDRESS:";
  public static final String FIELD_EMAIL_ADDRESS_XPATH= "EMAIL_ADDRESS_XPATH:";
  public static final String FIELD_EMAIL_CODE         = "EMAIL_CODE:";
  public static final String FIELD_EMAIL_CODE_XPATH   = "EMAIL_CODE_XPATH:";
  public static final String FIELD_USER               = "USER:";
  public static final String FIELD_ROLE               = "ROLE:";
  public static final String FIELD_DYNAMIC_RECIPIENTS = "DYNAMIC_RECIPIENTS";

  private String _xpathLookupFile;
  private List<String>   _dynamicRecipients;

  /**
   * Constructs a RecipientList data provider, with capability of providing
   * data for: FIELD_EMAIL_ADDRESS, FIELD_EMAIL_CODE, FIELD_USER, and
   * FIELD_ROLE.
   */
  public RecipientListData()
  {
  }

  /**
   * Constructs a RecipientList data provider, with additional capability of
   * providing data for FIELD_EMAIL_CODE_XPATH.
   *
   * @param xpathLookupFile The filename of the file to use for extracting
   * Email code using email code xpath.
   */
  public RecipientListData(String xpathLookupFile)
  {
    _xpathLookupFile = xpathLookupFile;
  }

  /**
   * Constructs a RecipientList data provider, with additional capability of
   * providing data for FIELD_EMAIL_CODE_XPATH and FIELD_DYNAMIC_RECIPIENTS.
   *
   * @param dynamicRecipients List of dynamic recipients. Each element in the list
   * must be one of the following formats:<p>
   * <UL>
   *   <LI><b>EMAIL_ADDRESS:</b><i>email_address@company.com</i></LI>
   *   <LI><b>EMAIL_CODE:</b><i>code_123</i></LI>
   *   <LI><b>EMAIL_CODE_XPATH:</b><i>documentA/xpath/to/code</i></LI>
   *   <LI><b>USER:</b><i>user_id</i></LI>
   *   <LI><b>ROLE:</b><i>role_name</i></LI>
   * </UL>
   * <P>Those in <b>bold</b> are the field tags and must be present. Those
   * in <i>italics</i> are to be replaced by the actual value of each field.
   * @param xpathLookupFile The filename of the file to use for extracting
   * Email code using email code xpath.
   */
  public RecipientListData(List<String> dynamicRecipients, String xpathLookupFile)
  {
    this(xpathLookupFile);
    _dynamicRecipients = dynamicRecipients;
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List<String> getFieldNameList()
  {
    ArrayList<String> list = new ArrayList<String>();
    list.add(FIELD_DYNAMIC_RECIPIENTS);
    list.add(FIELD_EMAIL_ADDRESS);
    list.add(FIELD_EMAIL_ADDRESS_XPATH);
    list.add(FIELD_EMAIL_CODE);
    list.add(FIELD_EMAIL_CODE_XPATH);
    list.add(FIELD_ROLE);
    list.add(FIELD_USER);

    return list;
  }

  protected Object getFieldValue(String fieldName)
  {
    String val = null;

    if (fieldName.equals(FIELD_DYNAMIC_RECIPIENTS))
    {
      Set<String> recipients = new HashSet<String>();
      if (_dynamicRecipients != null && !_dynamicRecipients.isEmpty())
      {
        for (String recipientTag : _dynamicRecipients)
        {
          recipients.addAll(getEmails(recipientTag));
        }
      }
      val = concatEmails(recipients);
    }
    else
    {
      Set<String> emails = getEmails(fieldName);
      val = concatEmails(emails);
    }
    return val;
  }

  /**
   * Get a set of Email addresses resolved from a specified field.
   *
   * @param fieldName The name of the field. Except for FIELD_DYNAMIC_RECIPIENTS,
   * all fields must be followed by a colon (:) and the value of the field.
   *
   * @return The set of email addresses resolved.
   */
  private Set<String> getEmails(String fieldName)
  {
    Set<String> emails = new HashSet<String>();
    int index = fieldName.indexOf(":");

    if (index > -1)
    {
      String field = fieldName.substring(0, index+1);
      String fieldVal = fieldName.substring(index+1);
      //System.out.println("Field="+field + ", fieldVal="+fieldVal);

      if (FIELD_EMAIL_ADDRESS.equals(field))
      {
        emails.add(fieldVal);
      }
      else if (FIELD_EMAIL_ADDRESS_XPATH.equals(field))
      {
        if (_xpathLookupFile != null)
          emails.addAll(RecipientResolver.getInstance().getAddressesForAddressXpath(
                        fieldVal, _xpathLookupFile));
        else
          System.err.println("Xpath lookup file not specified to extract email address!");
      }
      else if (FIELD_EMAIL_CODE.equals(field))
      {
        emails.addAll(RecipientResolver.getInstance().getAddressesForCode(fieldVal));
      }
      else if (FIELD_EMAIL_CODE_XPATH.equals(field))
      {
        if (_xpathLookupFile != null)
          emails.addAll(RecipientResolver.getInstance().getAddressesForCodeXpath(
                        fieldVal, _xpathLookupFile));
        else
          System.err.println("Xpath lookup file not specified to extract email code!");
      }
      else if (FIELD_ROLE.equals(field))
      {
        emails.addAll(RecipientResolver.getInstance().getAddressesForRole(fieldVal));
      }
      else if (FIELD_USER.equals(field))
      {
        emails.add(RecipientResolver.getInstance().resolveAddressForUser(fieldVal));
      }
    }

    return emails;
  }

  /**
   * Concatentate the collection of email addresses into a single string.
   *
   * @param emails The Collection of email addresses (String)
   * @return The concatenated string. Each email is delimited by comma.
   */
  protected static String concatEmails(Collection<String> emails)
  {
    return RecipientResolver.getInstance().concatEmails(emails);
  }

  /**
   * Validates a string as a accepted Recipient Field.
   *
   * @param field The string to be validated.
   * @return <b>true</b> if the specified field is acceptable, <b>false</b> otherwise.
   */
  public static boolean isValidFieldFormat(String field)
  {
    boolean valid = false;
    String[] fieldList = getFieldNameList().toArray(new String[0]);

    for (int i=0; i<fieldList.length && !valid; i++)
    {
      valid = (fieldList[i].regionMatches(0, field, 0, fieldList[i].length()));
    }
    return valid;
  }

/*
  public static void main(String[] args)
  {
    RecipientListData provider = new RecipientListData();
    RecipientListData provider2 = new RecipientListData(
    "file://D:\\J2EE\\jboss-3.0.5_tomcat-4.0.6\\bin\\gtas\\data\\alert\\email\\emailcode-ref.xml");

    ArrayList dyn = new ArrayList();
    dyn.add("EMAIL_ADDRESS:emaila@one.com");
    dyn.add("USER:admin");
    dyn.add("ROLE:User");
    dyn.add("EMAIL_CODE:code1");
    dyn.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");
    dyn.add("EMAIL_ADDRESS_XPATH:/EmailCodeRef/EmailRef");
    RecipientListData provider3 = new RecipientListData(
     dyn,
    "file://D:\\J2EE\\jboss-3.0.5_tomcat-4.0.6\\bin\\gtas\\data\\alert\\email\\emailcode-ref.xml");

    System.out.println("------------- Using Provider 1 -------------");
    System.out.println("Email-->"+ provider.get("EMAIL_ADDRESS:emaila@one.com"));
    System.out.println("User-->"+ provider.get("USER:admin"));
    System.out.println("Role-->"+ provider.get("ROLE:User"));
    System.out.println("EmailCode-->"+ provider.get("EMAIL_CODE:code1"));

    System.out.println("------------- Using Provider 2 -------------");

    System.out.println("EmailCodeXpath(1)-->"+
      provider2.get("EMAIL_CODE_XPATH:EmailCodeRef/EmailCode/@id[1]"));
    System.out.println("EmailCodeXpath(all)-->"+
      provider2.get("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id"));
    System.out.println("EmailAddressXpath(1)-->"+
      provider2.get("EMAIL_ADDRESS_XPATH:/EmailCodeRef/EmailCode/EmailRef[1]"));

    System.out.println("------------- Using Provider 3 -------------");
    System.out.println("DynamicRecipients-->"+
      provider3.get("DYNAMIC_RECIPIENTS"));
  }
*/
}