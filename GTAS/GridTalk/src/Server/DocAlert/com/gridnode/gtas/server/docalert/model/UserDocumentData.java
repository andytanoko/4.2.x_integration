/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserDocumentData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 26 Jan 2003    Neo Sok Lay         Created
 * 20 Feb 2003    Neo Sok Lay         Return [XPath is not found in user document]
 *                                    if xpath extraction returns null value.
 * 20 May 2003    Neo Sok Lay         Change get() to getFieldValue() - called
 *                                    by super class.
 */

package com.gridnode.gtas.server.docalert.model;

import com.gridnode.gtas.server.docalert.helpers.DocExtractionHelper;

import com.gridnode.pdip.app.alert.providers.AbstractDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Data provider for User document.
 * This currently allows extracting values from the user document (xml only)
 * using Xpath, and using custom email code recipients determined during run-time
 * from the user document.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UserDocumentData extends AbstractDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3046567592544945792L;

	public static final String NAME = "UserDocument";

  public static final String FIELD_XPATH = "XPATH:";
  public static final String FIELD_EMAILCODE_RECPT = "EMAIL_CODE_RECIPIENTS";

  private static final String XPATH_NOT_FOUND = "[XPath is not found in "+
                                                NAME +"]";

  protected String _filename;

  // Input filename must be full path filename of the user doc
  public UserDocumentData(String filename, String recipients)
  {
    _filename = filename;
    set(FIELD_EMAILCODE_RECPT, recipients);
  }


  // Return requested field
  // fieldName will be prefixed with "XPATH:".
  // This method will first extract the prefix String and pass the remaining
  // which is the XPath to the DocExtractionHelper. The value return from the
  // DocExtractionHelper will be returned.
  // If optimisation required, store copy of Document object, subsequent call
  // will use the method with Document object so the XPathHelper do not have to
  // read file again.
  // Consider : this class will need to import JDOM's Document.
  protected Object getFieldValue(String fieldName)
  {
    Object val = null;
    if (fieldName.startsWith(FIELD_XPATH))
    {
      String xPath = fieldName.substring(FIELD_XPATH.length());

      val = DocExtractionHelper.getValueAtXpath(xPath, _filename);

      if (val == null)
        val = XPATH_NOT_FOUND;
    }
    else
    {
      val = super.getFieldValue(fieldName);
    }

    return val;
  }

  public String getName()
  {
    return NAME;
  }

  public String getType()
  {
    return TYPE_NORMAL;
  }

  /**
   * Get the names of fields available in this data provider.
   * @return List of field names (String).
   */
  public static final List getFieldNameList()
  {
    ArrayList list = new ArrayList();
    list.add(FIELD_XPATH);
    list.add(FIELD_EMAILCODE_RECPT);

    return list;
  }
}